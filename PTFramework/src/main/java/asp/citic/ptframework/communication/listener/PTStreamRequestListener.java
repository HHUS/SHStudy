package asp.citic.ptframework.communication.listener;

/**
 * @ingroup httpRequestModuleProtocol
 * 流读取接口
 */
public interface PTStreamRequestListener {

    /**
     * 读取数据到数据的回调.
     *
     * @param total    文件总大小
     * @param position 正在下载的位置
     */
    void onProcess(long total, long position);

    /**
     * 文件读取成功
     */
    void onSuccess();
    /**
     * 文件读取失败
     */
    void onFailed();
}
