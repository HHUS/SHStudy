package asp.citic.ptframework.common.tools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import asp.citic.ptframework.logger.PTLogger;

/**
 * JSON操作助手
 */
public class PTJSONFile extends JSONObject {
    /**
     * 类标记
     */
    private static final String TAG = "PTJSONFile";
    private transient String filePath;
    private transient boolean readOnly;
    private transient String encoding;

    /**
     * 构造函数
     */
    public PTJSONFile(InputStream ins) throws JSONException {
        this(ins, null);
    }

    /**
     * 构造函数
     */
    public PTJSONFile(InputStream ins, String encoding) throws JSONException {
        super(loadData(ins, encoding));
        this.readOnly = true;
        this.encoding = encoding;
    }

    /**
     * 构造函数
     */
    public PTJSONFile(String filePath) throws JSONException {
        this(filePath, true, Charsets.UTF_8.name());
    }

    /**
     * 构造函数
     */
    public PTJSONFile(String filePath, String encoding) throws JSONException, UnsupportedEncodingException {
        this(filePath, true, encoding);
    }

    /**
     * 构造函数
     */
    public PTJSONFile(String filePath, boolean readOnly) throws JSONException, UnsupportedEncodingException {
        this(filePath, readOnly, null);
    }

    /**
     * 构造函数
     */
    public PTJSONFile(String filePath, boolean readOnly, String encoding) throws JSONException {
        super(loadFile(filePath, encoding));
        this.filePath = filePath;
        this.readOnly = readOnly;
        this.encoding = encoding;
    }

    private static String loadFile(String filePath, String encoding) {
        return loadData(PTFileOperator.getFileStream(filePath), encoding);
    }

    private static String loadData(InputStream ins, String encoding) {
        if (ins != null) {
            try {
                if (encoding == null) {
                    return new String(PTStreamOperator.getInputStreamBytes(ins));
                } else {
                    try {
                        return new String(PTStreamOperator.getInputStreamBytes(ins), encoding);
                    } catch (UnsupportedEncodingException e) {
                        PTLogger.error(TAG,e.getMessage());
                    }
                }
            } finally {
                PTStreamOperator.close(ins);
            }
        }
        return "{}";
    }

    /**
     * 存储配置文件
     */
    public void store() throws UnsupportedEncodingException {
        if (readOnly) {
//			PTLogger.d("Can't store in read only mode!");
            return;
        }
        PTFileOperator.saveToFileByString(filePath, toString(), encoding);
    }

    /**
     * 删除配置文件
     *
     * @return 删除成功返回true，否则返回false
     */
    public boolean delete() {
        if (readOnly) {
            return false;
        }
        return new File(filePath).delete();
    }

    @Override
    public JSONObject put(String name, boolean value) throws JSONException {
        if (readOnly) {
            return this;
        }
        return super.put(name, value);
    }

    @Override
    public JSONObject put(String name, int value) throws JSONException {
        if (readOnly) {
            return this;
        }
        return super.put(name, value);
    }

    @Override
    public JSONObject put(String name, double value) throws JSONException {
        if (readOnly) {
            return this;
        }
        return super.put(name, value);
    }

    @Override
    public JSONObject put(String name, long value) throws JSONException {
        if (readOnly) {
            return this;
        }
        return super.put(name, value);
    }

    @Override
    public JSONObject put(String name, Object value) throws JSONException {
        if (readOnly) {
            return this;
        }
        return super.put(name, value);
    }

    @Override
    public JSONObject putOpt(String name, Object value) throws JSONException {
        if (readOnly) {
            return this;
        }
        return super.putOpt(name, value);
    }
}
