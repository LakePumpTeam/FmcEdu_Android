package com.fmc.edu.receiver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fmc.edu.RelatedInfoActivity;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/7/12.
 */
public class PushMessageReceiver extends com.baidu.android.pushservice.PushMessageReceiver {
    @Override
    public void onBind(Context context, int errorCode, String appId, String userId, String channelId, String requestId) {
        LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(context);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("deviceType", 3);
        params.put("userId", loginUserEntity.userId);
        params.put("channelId", channelId);
        params.put("baiduUserId", userId);
        MyIon.httpPost(context, "profile/bindBaiDuPush", params, null, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
            }
        });
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
