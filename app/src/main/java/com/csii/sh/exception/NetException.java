

package com.csii.sh.exception;

/**
 * 网络错误的封装
 */
public class NetException extends Exception {

    private int code;

    private String message;

    public NetException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
