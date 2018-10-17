package com.csii.sh.service;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.FileProvider;

import com.csii.sh.entity.DownloadInfo;
import com.csii.sh.net.APIService;
import com.csii.sh.net.RetrofitDownClient;
import com.csii.sh.net.impl.OnProgressResponseListener;
import com.csii.sh.util.FileUtils;
import com.csii.sh.util.Logger;
import com.csii.sh.util.NotificationUtils;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * <pre>
 *  autor :
 *  time  : 2018/01/17
 *  desc  :使用Retrofit下载APk或者其他资源文件
 * </pre>
 */

public class DownloadService extends IntentService {
    private static final String TAG = "DownloadService";
    private static final String KEYURL = "KeyUrl";
    private static final String KEYTYPE = "KeyType";

    private static final int MSG_START = 0;
    private static final int MSG_LOADING = 1;
    private static final int MSG_COMPLETE = 2;

    private static final OnProgressResponseListener mListener = new OnProgressResponseListener() {
        @Override
        public void onResponseProgress(long bytesRead, long contentLength, boolean done) {
            mDownloadInfo.mProcess = (int) (bytesRead * 100 / contentLength);
        }
    };

    private long mDelayedTime = 1000;

    private Map<Integer, DownloadInfo> mMap = new HashMap<>();

    private static DownloadInfo mDownloadInfo;

    private NotifyHandler mNotifyHandler;

    private HandlerThread mHandlerThread;

    //// TODO: 2016/5/18 0018 目前存在内存泄露状态 因为OnProgressResponseListener在Service中实例化 然后弃用 但是okhttp的生命周期比较长
    private final class NotifyHandler extends Handler {
        //下载操作不频繁 可以当做类变量 使用时候再创建
        private NotificationManager mNotificationManager;

        public NotifyHandler(Looper looper) {
            super(looper);
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }

        @Override
        public void handleMessage(Message msg) {
            DownloadInfo downloadInfo = (DownloadInfo) msg.obj;
            int notifyId = downloadInfo.mWorkId;
            Notification notification;
            switch (msg.what) {
                case MSG_START:
                    notification = NotificationUtils.showNotification(
                            getApplication(),
                            downloadInfo.fileName,
                            downloadInfo.mState
                    );
                    break;
                case MSG_LOADING:
                    notification = NotificationUtils.showProcessNotification
                            (getApplication(), downloadInfo.fileName, downloadInfo.mProcess);
                    break;
                case MSG_COMPLETE:
                    notification = NotificationUtils.showIntentNotification(
                            getApplication(),
                            downloadInfo.mFile,
                            downloadInfo.mMediaType,
                            downloadInfo.fileName,
                            downloadInfo.mState
                    );

                    break;
                default:
                    notification = NotificationUtils.showNotification(
                            getApplication(),
                            "错误",
                            "无法处理接受的消息"
                    );
                    break;
            }
            mNotificationManager.notify(notifyId, notification);
        }
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Logger.e(Thread.currentThread().toString() + " mProcess=" + mDownloadInfo.mProcess);
            Message message = mNotifyHandler.obtainMessage();
            message.what = MSG_LOADING;
            message.obj = mDownloadInfo;
            mNotifyHandler.sendMessage(message);

            mNotifyHandler.postDelayed(this, mDelayedTime);
        }
    };

    public DownloadService() {
        super(TAG);
        Logger.e(TAG);
    }

    public static void launch(Activity activity, String url, String type) {
        Intent intent = new Intent(activity, DownloadService.class);
        intent.putExtra(KEYURL, url);
        intent.putExtra(KEYTYPE, type);
        activity.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logger.e(TAG);

        //线程不阻塞的回调 能够得到每次的发起操作回调 而不会阻塞
        String url = intent.getStringExtra(KEYURL);
        String type = intent.getStringExtra(KEYTYPE);
//        String filename = String.valueOf(System.currentTimeMillis()) + Utils.getPinsType(type);
        String filename = url;
        int mWorkId = url.hashCode();
        //根据参数 构造对象
        DownloadInfo mDownloadInfo = new DownloadInfo(filename, type, mWorkId, url, DownloadInfo.StateStart);
        Logger.e(mWorkId + " " + Thread.currentThread().toString());
        //根据url的hashcode 放入
        mMap.put(mWorkId, mDownloadInfo);
        sendNotifyMessage(mDownloadInfo);
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendNotifyMessage(DownloadInfo mDownloadInfo) {
        Message message = mNotifyHandler.obtainMessage();
        message.what = MSG_START;
        message.obj = mDownloadInfo;
        mNotifyHandler.sendMessage(message);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //处理队列中的消息 顺序调用 处理完一个再处理下一个
        //这里是线程阻塞方法 刚好可以好判断当前任务
        //从map中取出构造的好的 对象 开始任务
        String url = intent.getStringExtra(KEYURL);
        Logger.e(url.hashCode() + " " + Thread.currentThread().toString());
        mDownloadInfo = mMap.get(url.hashCode());
        actionDownload(url, mDownloadInfo, mListener);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.e(TAG);

        //构造HandlerThread 负责发送通知栏消息的线程
        mHandlerThread = new HandlerThread("notifyThread");
        mHandlerThread.start();
        mNotifyHandler = new NotifyHandler(mHandlerThread.getLooper());
    }


    /**
     * 阻塞当前线程的下载方法
     * 在onSubscribe中，我们需要调用request去请求资源，参数就是要请求的数量，
     * 一般如果不限制请求数量，可以写成Long.MAX_VALUE。如果你不调用
     * request，Subscriber的onNext和onComplete方法将不会被调用。
     * @param mApkUrl
     * @param downloadInfo
     * @param listener
     *
     * onSubscribe->map操作符->onNext->onComplete
     */
    private void actionDownload(String mApkUrl, final DownloadInfo downloadInfo, OnProgressResponseListener listener) {

        Flowable flowable = RetrofitDownClient.createService(APIService.class, listener).get(mApkUrl);
        /**Map操作符的作用就是将Observable所发送送的信息进行格式转换或者处理*/
        flowable.map(new Function<ResponseBody, File>() {
            @Override
            public File apply(ResponseBody responseBody) throws Exception {
                Logger.e(responseBody.contentLength() + " " + responseBody.contentType().toString());
                File file = FileUtils.getDirsFile();//构造目录文件
                Logger.e(file.getPath());

                return FileUtils.writeResponseBodyToDisk(file, responseBody, mDownloadInfo.fileName);
            }

        }).subscribe(new Subscriber<File>() {
            @Override
            public void onSubscribe(Subscription s) {
                //开始下载 线程开始轮询
                mNotifyHandler.postDelayed(mRunnable, mDelayedTime);
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(File file) {
                Logger.d(file.getAbsolutePath());
                Logger.d(Thread.currentThread().toString());
                mNotifyHandler.removeCallbacks(mRunnable);
                sendFileNotifyMessage(file, downloadInfo);
            }

            @Override
            public void onError(Throwable t) {
                Logger.e(t.toString());
            }

            @Override
            public void onComplete() {
                Logger.e("============================");
            }
        });

    }

    private void sendFileNotifyMessage(File file, DownloadInfo mDownloadInfo) {
        mDownloadInfo.mFile = file;
        mDownloadInfo.mState = "下载完成";
        Message message = mNotifyHandler.obtainMessage();
        message.what = MSG_COMPLETE;
        message.obj = mDownloadInfo;
        mNotifyHandler.sendMessage(message);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Logger.e(TAG);
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.e(TAG + " " + Thread.currentThread().toString());
        mHandlerThread.quit();//结束轮询

    }


    private void installApk() {
        File file = new File(FileUtils.getDirsFile() + "osc.apk");
        if (!file.exists())
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(getApplicationContext(), "com.csii.sh", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

//    private void setUpNotification() {
//        int icon = R.mipmap.ic_launcher;
//        CharSequence tickerText = "开始下载";
//        long when = System.currentTimeMillis();
//        mNotification = new Notification(icon, tickerText, when);
//
//        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
//
//        RemoteViews contentView = new RemoteViews(getPackageName(),
//                R.layout.layout_notification_view);
//        contentView.setTextViewText(R.id.tv_download_progress, mTitle);
//        mNotification.contentView = contentView;
//
//        Intent intent = new Intent(this, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        mNotification.contentIntent = contentIntent;
//        mNotificationManager.notify(0, mNotification);
//    }

}