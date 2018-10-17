package asp.citic.ptframework.common.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import asp.citic.ptframework.PTFramework;
import asp.citic.ptframework.communication.listener.PTStreamRequestListener;
import asp.citic.ptframework.logger.PTLogger;

/**
 * 文件操作助手
 */
public final class PTFileOperator {

    private final static String TAG = "PTFileOperator";

    /**
     * 私有构造方法
     */
    private PTFileOperator() {
        //To be a Utility class
    }

    /**
     * 文件是否存在
     *
     * @param path
     * @return true:存在；false：不存在
     */
    public static boolean isFileExist(String path) {
        if (path != null && path.equals("")) {
            File file = new File(path);
            if (file != null && file.exists()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件内容.
     */
    public static byte[] getFileContent(String filePath) {
        if (filePath == null) {
            return null;
        }
        InputStream ins = null;
        try {
            ins = getFileStream(filePath);
            if (ins == null) {
                return null;
            }
            return PTStreamOperator.getInputStreamBytes(ins);
        } finally {
            PTStreamOperator.close(ins);
        }
    }

    /**
     * 获取文件流.
     */
    public static InputStream getFileStream(String filePath) {
        if (filePath == null) {
            return null;
        }
        if (filePath.startsWith("file:///android_asset/")) {
            return PTFileOperator.class.getClassLoader().getResourceAsStream("assets/" + filePath.substring(22));
        } else {
            File file = new File(filePath);
            if (!file.exists() || !file.isFile()) {
                return null;
            }
            try {
                return new FileInputStream(filePath);
            } catch (FileNotFoundException e) {
                PTLogger.error(TAG,e.getMessage());
            }
        }
        return null;
    }

    /**
     * 将字符串保存为文件(覆盖模式).
     */
    public static void saveToFileByBytes(String filePath, byte[] data) {
        saveToFileByBytes(filePath, data, false);
    }

    /**
     * 将字符串保存为文件.
     */
    public static void saveToFileByBytes(String filePath, byte[] data,
                                         boolean append) {
        if (filePath == null || data == null) {
            return;
        }
        saveToFileByStream(filePath, new ByteArrayInputStream(data), append);
    }

    /**
     * 将字符串保存为文件(覆盖模式).
     */
    public static void saveToFileByString(String filePath, String src)
            throws UnsupportedEncodingException {
        saveToFileByString(filePath, src, false, null);
    }

    /**
     * 将字符串保存为文件(覆盖模式).
     */
    public static void saveToFileByString(String filePath, String src, String encoding)
            throws UnsupportedEncodingException {
        saveToFileByString(filePath, src, false, encoding);
    }

    /**
     * 将字符串保存为文件.
     */
    public static void saveToFileByString(String filePath, String data,
                                          boolean append, String encoding) throws UnsupportedEncodingException {
        if (filePath == null || data == null) {
            return;
        }
        if (encoding == null) {
            saveToFileByStream(filePath, new ByteArrayInputStream(data.getBytes()),
                    append);
        } else {
            saveToFileByStream(filePath, new ByteArrayInputStream(data.getBytes(encoding)),
                    append);
        }

    }

    /**
     * 将输入流保存为文件(覆盖模式).
     */
    public static void saveToFileByStream(String filePath, InputStream ins) {
        saveToFileByStream(filePath, ins, false);
    }

    /**
     * 将输入流保存为文件.
     */
    public static void saveToFileByStream(String filePath, InputStream ins,
                                          boolean append) {
        if (filePath == null || ins == null) {
            return;
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            PTStreamOperator.copy(ins, fos);
        } catch (FileNotFoundException e) {
            PTLogger.error(TAG,e.getMessage());
        } finally {
            PTStreamOperator.close(fos);
        }
    }

    /**
     * 解压缩文件到指定的目录.
     */
    public static boolean unZip(String zipFilePath, String destPath, PTStreamRequestListener listener) {
        if (zipFilePath == null || destPath == null || listener == null) {
            return false;
        }
        String destStr = destPath;
        if (!destPath.endsWith("/")) {
            destStr = destPath + "/";
        }
        ZipInputStream zis = null;
        int bufferSize = 4096;
        byte buf[] = new byte[bufferSize];
        try {
            InputStream inputStream = getFileStream(zipFilePath);
            zis = new ZipInputStream(new BufferedInputStream(inputStream));
            ZipEntry zipEntry;
            long total;
            long position = 0;
            int readedBytes;
            total = getZipTrueSize(zipFilePath);
            PTLogger.info(TAG + "压缩文件大小：", String.valueOf(total));
            while ((zipEntry = zis.getNextEntry()) != null) {
                File desFile = new File(destStr + zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    desFile.mkdirs();
                } else {
                    // 如果指定文件的目录不存在,则创建之.
                    File parent = desFile.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(desFile);
                    while ((readedBytes = zis.read(buf)) > 0) {
                        fos.write(buf, 0, readedBytes);
                        position += readedBytes;
                        listener.onProcess(total, position);
                    }
                    fos.close();
                }
                zis.closeEntry();
            }
        } catch (IOException e) {
            PTLogger.error(TAG,e.getMessage());
            listener.onFailed();
            return false;
        } finally {
            PTStreamOperator.close(zis);
        }
        listener.onSuccess();
        return true;

    }

    /**
     * 获取压缩包解压后大小
     *
     * @param filePath
     * @return
     */
    public static long getZipTrueSize(String filePath) {
        long size = 0;
        try {
            ZipFile file = new ZipFile(filePath);
            Enumeration<? extends ZipEntry> entries = file.entries();
            while (entries.hasMoreElements()) {
                size += entries.nextElement().getSize();
            }
        } catch (IOException e) {
            PTLogger.error(TAG,e.getMessage());
        }
        return size;
    }

    /**
     * 将文件或文件夹进行Zip压缩.
     */
    public static void zip(String srcPath, String zipFilePath) {
        zip(zipFilePath, new File(srcPath));
    }

    /**
     * 将文件或文件夹进行Zip压缩.
     */
    public static void zip(String zipFilePath, File srcFile) {
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipFilePath));
            zip(zos, srcFile, "/img");
        } catch (IOException e) {
            PTLogger.error(TAG,e.getMessage());
        } finally {
            PTStreamOperator.close(zos);
        }
    }

    /**
     * 将文件或文件夹进行Zip压缩.
     */
    private static void zip(ZipOutputStream zos, File srcFile, String baseDir)
            throws IOException {
        String tempDir = "";
        if (srcFile.isDirectory()) {
            File[] fileList = srcFile.listFiles();
            zos.putNextEntry(new ZipEntry(baseDir + "/"));
            tempDir = baseDir.length() == 0 ? "" : baseDir + "/";
            for (int i = 0; i < fileList.length; i++) {
                zip(zos, fileList[i], tempDir + fileList[i].getName());
            }
        } else {
            zos.putNextEntry(new ZipEntry(tempDir));
            FileInputStream ins = new FileInputStream(srcFile);
            int size;
            while ((size = ins.read()) != -1) {
                zos.write(size);
            }
            ins.close();
        }
    }

    /**
     * 删除文件或文件夹.
     */
    public static boolean deleteFile(String fileName) {
        return deleteFile(new File(fileName));
    }

    /**
     * 删除文件或文件夹.
     */
    public static boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            return true;
        } else if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null) {
                for (final File subFile : subFiles) {
                    if (!deleteFile(subFile)) {
                        return false;
                    }
                }
            }
        }
        return file.delete();
    }

    /**
     * 拷贝文件.
     */
    public static void copyFile(File sourceFile, File targetFile)
            throws IOException {
        // 新建文件输入流并对它进行缓冲
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);

        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff = new BufferedOutputStream(output);

        // 缓冲数组
        byte[] buffer = new byte[1024 * 5];
        int len;
        while ((len = inBuff.read(buffer)) != -1) {
            outBuff.write(buffer, 0, len);
        }
        // 刷新此缓冲的输出流
        outBuff.flush();

        // 关闭流
        inBuff.close();
        outBuff.close();
        output.close();
        input.close();
    }

    /**
     * 拷贝文件.
     */
    public static boolean copyFileStream(InputStream input, String targetFile) {
        // 新建文件输入流并对它进行缓冲
        BufferedInputStream inBuff = new BufferedInputStream(input);
        // 新建文件输出流并对它进行缓冲
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(targetFile);
            BufferedOutputStream outBuff = new BufferedOutputStream(output);
            // 缓冲数组
            byte[] buffer = new byte[1024 * 5];
            int len;
            try {
                while ((len = inBuff.read(buffer)) != -1) {
                    outBuff.write(buffer, 0, len);
                }
                // 刷新此缓冲的输出流
                outBuff.flush();
                // 关闭流
                inBuff.close();
                outBuff.close();
                output.close();
                input.close();
            } catch (IOException e) {
                PTLogger.error(TAG,e.getMessage());
                return false;
            }
        } catch (FileNotFoundException e) {
            PTLogger.error(TAG,e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 拷贝目录.
     */
    public static boolean copyDirectiory(String sourceDir, String targetDir) {
        File tarFile = new File(targetDir);
        if (tarFile != null && !tarFile.exists()) {
            // 新建目标目录
            tarFile.mkdirs();
        }
        // 获取源文件夹当前下的文件或目录
        File file = new File(sourceDir);
        File[] fileList = file.listFiles();
        if (fileList != null) {
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isFile()) {
                    // 源文件
                    File sourceFile = fileList[i];
                    // 目标文件
                    File targetFile = new File(
                            new File(targetDir).getAbsolutePath() + File.separator
                                    + fileList[i].getName());
                    try {
                        copyFile(sourceFile, targetFile);
                    } catch (IOException e) {
                        return false;
                    }
                }
                if (fileList[i].isDirectory()) {
                    // 准备复制的源文件夹
                    String dir1 = sourceDir + File.separator + fileList[i].getName();
                    // 准备复制的目标文件夹
                    String dir2 = targetDir + File.separator + fileList[i].getName();
                    copyDirectiory(dir1, dir2);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 拷贝asset文件到应用安装目录
     * @param assetName
     * @param targStr
     */
    public static boolean copyAssetFile(String assetName, String targStr) {
        try {
            InputStream ins = PTFramework.getContext().getResources().getAssets().open("data.zip");
            return copyFileStream(ins,targStr);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取平台统一路径
     */
    public static String getUniformPath(String path) {
        String tempPath = "";
        if ("/".equals(File.separator)) {
            tempPath = path.replace("\\", File.separator);
        } else {
            tempPath = path.replace("/", File.separator);
        }
        return tempPath;
    }

    /**
     * 获取文件扩展名
     */
    public static String getFileExtension(String filePath) {
        String tempPath = getUniformPath(filePath);
        int index = tempPath.lastIndexOf(File.separator);
        String tempFilePath = "";
        if (index != -1) {
            tempFilePath = tempPath.substring(index + 1);
        }
        index = tempFilePath.lastIndexOf('.');
        if (index == -1) {
            return "";
        } else {
            return tempFilePath.substring(index + 1);
        }
    }
}
