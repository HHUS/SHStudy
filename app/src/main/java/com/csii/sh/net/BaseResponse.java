package com.csii.sh.net;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * Created by on 2018/1/8.
 */

public class BaseResponse {

    /**
     * 接口返回的状态码
     */
    @SerializedName("status")
    private String status;
    /**
     * 接口响应信息
     */
    @SerializedName("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
