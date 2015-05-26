package com.fmc.edu.http;

import android.util.Base64;

import com.fmc.edu.utils.JsonToMapUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.koushikdutta.async.future.FutureCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Candy on 2015/5/7.
 */
public abstract class FMCMapFutureCallback implements FutureCallback<String> {

    public abstract void onTranslateCompleted(Exception e, Map<String, ?> result);

    @Override
    public void onCompleted(Exception e, String result) {
        if (null != e || result.contains("403 Forbidden") || result.contains("404")) {
            ToastToolUtils.showLong("服务器出问题...");
            return;
        }
        Map<String, ?> mapResult = new HashMap<>();
        try {
            String decodeResult = new String(Base64.decode(result, Base64.DEFAULT));
            mapResult = JsonToMapUtils.getMap(decodeResult);

        } catch (Exception ex) {
            ToastToolUtils.showLong("服务器数据出错！");
            return;
        }
        onTranslateCompleted(e, mapResult);
    }
}
