package asp.citic.ptframework.taskexecutor;


import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;

import asp.citic.ptframework.common.constants.PTFrameworkConstants;
import asp.citic.ptframework.common.tools.Charsets;
import asp.citic.ptframework.communication.PTCommunicationHelper;
import asp.citic.ptframework.communication.datapackage.PTComPackage;
import asp.citic.ptframework.communication.listener.PTHttpResponseInterface;
import asp.citic.ptframework.logger.PTLogger;

/**
 * @ingroup httpAssistModuleClass
 * 网络请求异步任务管理类
 */
public class PTHttpTask extends PTAbstractTask<JSONObject> {
    /**
     * 类标记
     */
    private final static String TAG = "PTHttpTask";
    /**
     * 请求参数
     */
    private transient JSONObject jsonBusiness;
    /**
     * 服务器前缀
     */
    private transient String serverUrl;

    /**
     * 装载请求参数，开始执行任务
     *
     * @param jsonBusiness
     */
    public void setTaskDate(String serverUrl, JSONObject jsonBusiness) {
        this.serverUrl = serverUrl;
        this.jsonBusiness = jsonBusiness;
    }

    public void setListener(PTTaskListener listener) {
        super.addTaskListener(listener);
    }

    /**
     * task任务开始执行
     */
    public void excuteTask() {
        PTTaskExecuteService.addTaskGroup(PTTaskExecuteService.TASKGROUP_DEFAULT, new PTTaskGroup(
                Executors.newFixedThreadPool(5)));
        PTTaskExecuteService.excute(this);
    }

    @Override
    protected JSONObject doTask(int method) {
        PTHttpResponseInterface response;
        if (method == PTCommunicationHelper.POST){
            HttpPost request = PTCommunicationHelper.createHttpPost(serverUrl, jsonBusiness, PTFrameworkConstants.PTEncrytTypeConstant.FLAG_ENCRYPT_3DES);
            response = PTCommunicationHelper.execHttpPost(request);
        }else{
            HttpGet request = new HttpGet(serverUrl);
            response = PTCommunicationHelper.execHttpGet(request);
        }
        if (response == null) {
            return new JSONObject();
        }
        PTComPackage pkg = PTCommunicationHelper.parseComPackage(response);
        return pkg.comPkgToJson();
    }

}
