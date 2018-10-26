package com.csii.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import timber.log.Timber;

/**
 * detail: 剪贴板相关工具类
 */
public final class ClipboardUtils {

    private ClipboardUtils() {
    }

    // 日志TAG
    private static final String TAG = ClipboardUtils.class.getSimpleName();

    /**
     * 复制文本到剪贴板
     * @param text
     */
    public static void copyText(Context context,final CharSequence text) {
        try {
            ClipboardManager clipManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 复制的数据
            ClipData clipData = ClipData.newPlainText("text", text);
            // 设置复制的数据
            clipManager.setPrimaryClip(clipData);
        } catch (Exception e){
            Timber.e(TAG, e, "copyText");
        }
    }

    /**
     * 获取剪贴板的文本
     * @return 剪贴板的文本
     */
    public static CharSequence getText(Context context) {
        try {
            ClipboardManager clipManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = clipManager.getPrimaryClip();
            if (clipData != null && clipData.getItemCount() > 0) {
                return clipData.getItemAt(0).coerceToText(context);
            }
        } catch (Exception e){
            Timber.e(TAG, e, "getText");
        }
        return null;
    }

    /**
     * 复制uri到剪贴板
     * @param uri
     */
    public static void copyUri(Context context,final Uri uri) {
        try {
            ClipboardManager clipManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 复制的数据
            ClipData clipData = ClipData.newUri(context.getContentResolver(), "", uri);
            // 设置复制的数据
            clipManager.setPrimaryClip(clipData);
        } catch (Exception e){
            Timber.e(TAG, e, "copyUri");
        }
    }

    /**
     * 获取剪贴板的uri
     * @return 剪贴板的uri
     */
    public static Uri getUri(Context context) {
        try {
            ClipboardManager clipManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = clipManager.getPrimaryClip();
            if (clipData != null && clipData.getItemCount() > 0) {
                return clipData.getItemAt(0).getUri();
            }
        } catch (Exception e){
            Timber.e(TAG, e, "getUri");
        }
        return null;
    }

    /**
     * 复制意图到剪贴板
     * @param intent 意图
     */
    public static void copyIntent(Context context,final Intent intent) {
        try {
            ClipboardManager clipManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 复制的数据
            ClipData clipData = ClipData.newIntent("intent", intent);
            // 设置复制的数据
            clipManager.setPrimaryClip(clipData);
        } catch (Exception e){
            Timber.e(TAG, e, "copyIntent");
        }
    }

    /**
     * 获取剪贴板的意图
     * @return 剪贴板的意图
     */
    public static Intent getIntent(Context context) {
        try {
            ClipboardManager clipManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = clipManager.getPrimaryClip();
            if (clipData != null && clipData.getItemCount() > 0) {
                return clipData.getItemAt(0).getIntent();
            }
        } catch (Exception e){
            Timber.e(TAG, e, "getIntent");
        }
        return null;
    }
}
