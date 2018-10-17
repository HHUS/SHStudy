package asp.citic.ptframework.resource;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

import asp.citic.ptframework.PTFramework;
import asp.citic.ptframework.PTFrameworkListener;
import asp.citic.ptframework.common.algorithm.PTConverter;
import asp.citic.ptframework.common.constants.PTFrameworkConstants;
import asp.citic.ptframework.common.hash.PTHash;
import asp.citic.ptframework.common.tools.PTFileOperator;
import asp.citic.ptframework.common.tools.PTStreamOperator;
import asp.citic.ptframework.communication.listener.PTStreamRequestListener;
import asp.citic.ptframework.logger.PTLogger;
import asp.citic.ptframework.message.PTMessageCenter;

/**
 * data压缩包管理器
 * Created by dora on 2016-12-20.
 */
public final class PTDataZipManager {

    private final static String TAG = "PTDataZipManager";
    private static SharedPreferences sharedPreferences;
    private static String assetZipPath = PTFrameworkConstants.PTResourceConstant.ASSETS_ZIP;
    private static String installZipPath = PTFrameworkConstants.PTResourceConstant.FILE_ZIP;
    private static String releasePath = PTFrameworkConstants.PTResourceConstant.PATH_RELEASE;
    private static String backupPath = PTFrameworkConstants.PTResourceConstant.PATH_BACKUP;
    private final static String ZIP_ASSET_PATH = "data.zip";

    /**
     * 私有构造方法
     */
    private PTDataZipManager() {
        //To be a Utility class
    }

    /**
     * 解压缩资源包
     */
    public static void unpackResource() {
        InputStream ins = PTFileOperator.getFileStream(assetZipPath);
        //流转成字节数组（直接用流去取MD5有问题）
        byte[] insToBytes = PTStreamOperator.getInputStreamBytes(ins);
        if (insToBytes == null) {
            PTMessageCenter.riseEvent(PTMessageCenter.EVENT_EMPTY_ZIPPACK, "");
            PTLogger.info(TAG, "data.zip压缩包为空");
            return;
        }
        //项目assets目录下data.zip的Hash值
        byte[] assetsBytes = PTHash.getHashByBytes(PTFrameworkConstants.PTHashEnumConstants.MD5, insToBytes);
        PTStreamOperator.close(ins);
        String assetsHash = PTConverter.bytesToHex(assetsBytes);
        sharedPreferences = PTFramework.getContext().getSharedPreferences("misc", Context.MODE_PRIVATE);
        //本地sp下data.zip的Hash值
        String spHash = sharedPreferences.getString("dataZipHash", "");
        int hashCase = compareHash(spHash, assetsHash);
        switch (hashCase) {
            case PTFrameworkConstants.PTDataZipConstant.NEW_LOAD:
                PTLogger.info(TAG, "用户首次安装程序");
                firstInstall(assetsHash);
                break;
            case PTFrameworkConstants.PTDataZipConstant.OLD_TO_NEWLOAD:
                PTLogger.info(TAG, "老用户升级新版本");
                updateInstall(assetsHash);
                break;
            case PTFrameworkConstants.PTDataZipConstant.BEFORE_LOAD_FAILED:
                PTLogger.info(TAG, "上次解压失败");
                beforeUnzipFailed();
                break;
            case PTFrameworkConstants.PTDataZipConstant.NO_LOAD:
                PTMessageCenter.riseEvent(PTMessageCenter.EVENT_NO_UNZIP, 0);
                PTLogger.info(TAG, "资源为最新");
                break;
            default:
                break;
        }
    }

    /**
     * 比较本地hash值和asset下data.zip的hash值
     *
     * @param spHash
     * @param assetsHash
     */
    private static int compareHash(String spHash, String assetsHash) {
        PTLogger.info(TAG, "本地hash: " + spHash);
        PTLogger.info(TAG, "工程hash: " + assetsHash);
        if (spHash == null || spHash.equals("")) {
            return PTFrameworkConstants.PTDataZipConstant.NEW_LOAD;
        }
        if (assetsHash == null || assetsHash.equals("")) {
            //assets下压缩包的Hash值获取异常 TODO:处理方式待定
            return PTFrameworkConstants.PTDataZipConstant.OLD_TO_NEWLOAD;
        }
        if (!spHash.equals(assetsHash)) {
            return PTFrameworkConstants.PTDataZipConstant.OLD_TO_NEWLOAD;
        }
        String fileZipPath = PTFrameworkConstants.PTResourceConstant.FILE_ZIP;
        if (PTFileOperator.isFileExist(fileZipPath)) {
            return PTFrameworkConstants.PTDataZipConstant.BEFORE_LOAD_FAILED;
        } else {
            return PTFrameworkConstants.PTDataZipConstant.NO_LOAD;
        }
    }

    /**
     * 首次安装
     */
    private static void firstInstall(final String assetsHash) {
        //拷贝工程下的压缩包到安装目录
        InputStream input = PTFileOperator.getFileStream(assetZipPath);
        boolean isCopySuc = PTFileOperator.copyFileStream(input, installZipPath);

        if (isCopySuc) {
            //解压
            PTMessageCenter.riseEvent(PTMessageCenter.EVENT_UNZIP_START, 0);
            PTFileOperator.unZip(installZipPath, releasePath, new PTStreamRequestListener() {
                @Override
                public void onProcess(long total, long position) {
                    if (total > 0) {
                        int proTem = (int) (position * 100 / total);
                        // 当 data.zip 解压完成后，将 100% 置为 99%，还需要删除临时文件；等待临时文件删除完毕，发送 100% 的消息
                        int progress = (proTem == 100) ? 99 : proTem;
//                        PTLogger.info(TAG, "解压中：" + progress);
                        PTMessageCenter.riseEvent(PTMessageCenter.EVENT_DATAZIP_PROGRESS, progress);
                    }

                }

                @Override
                public void onSuccess() {
                    //解压成功,将release备份到backup、删除安装目录中的压缩包、更新本地MD5值
                    PTFileOperator.copyDirectiory(releasePath, backupPath);
                    PTFileOperator.deleteFile(installZipPath);
                    sharedPreferences.edit().putString("dataZipHash", assetsHash).commit();
                    PTMessageCenter.riseEvent(PTMessageCenter.EVENT_DATAZIP_PROGRESS, 100);
                    PTLogger.info(TAG, "解压成功：100");
                    PTLogger.info(TAG, "更新后的assetHash：" + assetsHash);
                }

                @Override
                public void onFailed() {
                    //解压失败,删除安装目录中的压缩包和release路径内容
                    PTLogger.info(TAG, "解压失败：");
                    PTFileOperator.deleteFile(installZipPath);
                    PTFileOperator.deleteFile(releasePath);
                    PTMessageCenter.riseEvent(PTMessageCenter.EVENT_DATAZIP_PROGRESS, 101);
                }
            });
        }
    }

    /**
     * 老用户升级新版本
     */
    private static void updateInstall(final String assetsHash) {
        //删除安装目录下的压缩包和release备份,当前release备份到backup
        PTFileOperator.deleteFile(installZipPath);
        PTFileOperator.deleteFile(backupPath);
        boolean isCopyASuc = PTFileOperator.copyDirectiory(releasePath, backupPath);
        boolean isCopyBSuc = PTFileOperator.copyAssetFile(ZIP_ASSET_PATH, installZipPath);
        if (isCopyASuc && isCopyBSuc) {
            //解压
            PTMessageCenter.riseEvent(PTMessageCenter.EVENT_UNZIP_START, 0);
            PTFileOperator.unZip(installZipPath, releasePath, new PTStreamRequestListener() {
                @Override
                public void onProcess(long total, long position) {
                    if (total > 0) {
                        int proTem = (int) ((position / total) * 100);
                        // 当 data.zip 解压完成后，将 100% 置为 99%，还需要删除临时文件；等待临时文件删除完毕，发送 100% 的消息
                        int progress = (proTem == 100) ? 99 : proTem;
                        PTMessageCenter.riseEvent(PTMessageCenter.EVENT_DATAZIP_PROGRESS, progress);
                    }
                }

                @Override
                public void onSuccess() {
                    //解压成功,将release备份到backup、删除安装目录中的压缩包、更新本地MD5值
                    PTFileOperator.copyDirectiory(releasePath, backupPath);
                    PTFileOperator.deleteFile(installZipPath);
                    sharedPreferences.edit().putString("dataZipHash", assetsHash).commit();
                    PTMessageCenter.riseEvent(PTMessageCenter.EVENT_DATAZIP_PROGRESS, 100);
                    PTLogger.info(TAG, "更新后的assetHash：" + assetsHash);
                }

                @Override
                public void onFailed() {
                    //解压失败,删除安装目录中的压缩包和release路径内容,将backup回退到release
                    PTFileOperator.deleteFile(installZipPath);
                    PTFileOperator.deleteFile(releasePath);
                    PTFileOperator.copyDirectiory(backupPath, releasePath);
                    PTMessageCenter.riseEvent(PTMessageCenter.EVENT_DATAZIP_PROGRESS, 101);
                }
            });
        }
    }

    /**
     * 上次解压失败
     */
    private static void beforeUnzipFailed() {
        //删除安装目录下的release备份,当前release备份到backup,删除release
        PTFileOperator.deleteFile(backupPath);
        boolean isCopySuc = PTFileOperator.copyDirectiory(releasePath, backupPath);
        if (isCopySuc) {
            PTFileOperator.deleteFile(releasePath);
            //解压
            PTMessageCenter.riseEvent(PTMessageCenter.EVENT_UNZIP_START, 0);
            PTFileOperator.unZip(installZipPath, releasePath, new PTStreamRequestListener() {
                @Override
                public void onProcess(long total, long position) {
                    if (total > 0) {
                        int proTem = (int) ((position / total) * 100);
                        // 当 data.zip 解压完成后，将 100% 置为 99%，还需要删除临时文件；等待临时文件删除完毕，发送 100% 的消息
                        int progress = (proTem == 100) ? 99 : proTem;
                        PTMessageCenter.riseEvent(PTMessageCenter.EVENT_DATAZIP_PROGRESS, progress);
                    }
                }

                @Override
                public void onSuccess() {
                    //解压成功,将release备份到backup、删除安装目录中的压缩包
                    PTFileOperator.copyDirectiory(releasePath, backupPath);
                    PTFileOperator.deleteFile(installZipPath);
                    PTMessageCenter.riseEvent(PTMessageCenter.EVENT_DATAZIP_PROGRESS, 100);
                }

                @Override
                public void onFailed() {
                    //解压失败,删除安装目录中的release路径内容,将backup回退到release
                    PTFileOperator.deleteFile(releasePath);
                    PTFileOperator.copyDirectiory(backupPath, releasePath);
                    PTMessageCenter.riseEvent(PTMessageCenter.EVENT_DATAZIP_PROGRESS, 101);
                }
            });
        }
    }
}
