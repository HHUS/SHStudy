package com.csii.sh;

import android.content.Context;
import android.os.Environment;

import com.csii.sh.util.StreamUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * autor : sunhao
 * time  : 2018/01/31  09:51
 * desc  : 应用程序的配置类 用于保存用户相关信息及设置
 */

public class AppConfig {

    private final static String APP_CONFIG = "config";

    public static final String KEY_LOAD_IMAGE = "KEY_LOAD_IMAGE";
    public static final String KEY_NOTIFICATION_DISABLE_WHENEXIT = "KEY_NOTIFICATION_DISABLE_WHENEXIT";

    public static final String KEY_CHECK_UPDATE = "KEY_CHECK_UPDATE";

    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment.getExternalStorageDirectory()
            + File.separator + "csii" + File.separator + "csiiImage" + File.separator;

    public final static String DEFAULT_SAVE_DOENLOAD_PATH = Environment.getExternalStorageDirectory()
            + File.separator + "csii" + File.separator + "download" + File.separator;

    private Context mContext;

    private static AppConfig appConfig;

    public static AppConfig getAppConfig(Context context) {
        if (appConfig == null) {
            appConfig = new AppConfig();
            appConfig.mContext = context;
        }

        return appConfig;
    }

    public String get(String key) {
        Properties props = get();

        String result = (props != null) ? props.getProperty(key) : null;

        return result;
    }

    /**
     * 配置文件的路径为：data/data/包名/app_+APP_CONFIG/APP_CONFIG ,其中包名后面的app_是为调用时，系统自己加上的、
     * @return
     */
    public Properties get() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            // 读取app_config目录下的config
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            fis = new FileInputStream(dirConf.getPath() + File.separator + APP_CONFIG);

            props.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(fis);
        }
        return props;
    }

    private void setProps(Properties p) {
        FileOutputStream fos = null;
        try {
            // 把config建在(自定义)app_config的目录下
            File dirConf = mContext.getDir(APP_CONFIG, Context.MODE_PRIVATE);
            File conf = new File(dirConf, APP_CONFIG);
            fos = new FileOutputStream(conf);

            p.store(fos, null);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            StreamUtil.close(fos);
        }
    }

    public void set(String key, String value) {
        Properties props = get();
        props.setProperty(key, value);
        setProps(props);
    }

    public void remove(String... key) {
        Properties props = get();
        for (String k : key)
            props.remove(k);
        setProps(props);
    }

}
