package com.csii.sh.net.impl;

/**
 * <pre>
 *  autor :
 *  time  : 2018/01/17
 *  desc  :
 * </pre>
 */

public interface OnProgressResponseListener {

    void onResponseProgress(long bytesRead, long contentLength, boolean done);


}
