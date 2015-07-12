package com.fmc.edu.receiver;

import android.content.Context;

import com.fmc.edu.utils.ToastToolUtils;

import java.util.List;

/**
 * Created by Candy on 2015/7/12.
 */
public class PushMessageReceiver extends com.baidu.android.pushservice.PushMessageReceiver {
    @Override
    public void onBind(Context context, int errorCode, String appId, String userId, String channelId, String requestId) {
    }

    @Override
    public void onUnbind(Context context, int i, String s) {

    }

    @Override
    public void onSetTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onDelTags(Context context, int i, List<String> list, List<String> list1, String s) {

    }

    @Override
    public void onListTags(Context context, int i, List<String> list, String s) {

    }

    @Override
    public void onMessage(Context context, String message, String customContentString) {
        ToastToolUtils.showShort(message);
    }

    @Override
    public void onNotificationClicked(Context context, String title, String content, String customContentString) {
        ToastToolUtils.showShort(title);
    }

    @Override
    public void onNotificationArrived(Context context, String title, String content, String customContentString) {
        ToastToolUtils.showShort(title);
    }
}
