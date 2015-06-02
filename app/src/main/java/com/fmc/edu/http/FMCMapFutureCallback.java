package com.fmc.edu.http;

import android.util.Base64;

import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.utils.JsonToMapUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.koushikdutta.async.future.FutureCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Candy on 2015/5/7.
 */
public abstract class FMCMapFutureCallback implements FutureCallback<String> {

    private ProgressControl mProgressControl;

    public FMCMapFutureCallback(ProgressControl progressControl) {
        mProgressControl = progressControl;
    }

    public abstract void onTranslateCompleted(Exception e, Map<String, ?> result);

    @Override
    public void onCompleted(Exception e, String result) {
        if (null != mProgressControl && mProgressControl.isShowing()) {
            mProgressControl.dismiss();
        }

        if (null != e || result.contains("403 Forbidden") || result.contains("404")) {
            ToastToolUtils.showLong("服务器出问题...");
            return;
        }
        if (result.contains("Service Unavailable")) {
            ToastToolUtils.showLong("服务器崩溃了...");
            return;
        }
        try {
            Map<String, ?> mapResult = new HashMap<>();
            String decodeResult = new String(Base64.decode(result, Base64.DEFAULT));
            mapResult = JsonToMapUtils.getMap(decodeResult);
            onTranslateCompleted(e, mapResult);

        } catch (Exception ex) {
            ToastToolUtils.showLong("服务器数据出错！");
            return;
        }

    }
}
