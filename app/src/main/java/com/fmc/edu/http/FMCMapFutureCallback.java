package com.fmc.edu.http;

import android.util.Base64;

import com.fmc.edu.utils.JsonToMapUtils;
import com.fmc.edu.utils.JsonUtils;
import com.koushikdutta.async.future.FutureCallback;

import java.util.Map;

/**
 * Created by Candy on 2015/5/7.
 */
public abstract class FMCMapFutureCallback implements FutureCallback<String> {

    public abstract void onTranslateCompleted(Exception e, Map<String, ?> result);

    @Override
    public void onCompleted(Exception e, String result) {
        String decodeResult = new String(Base64.decode(result, Base64.DEFAULT));
        Map<String,?> mapResult =JsonToMapUtils.getMap(decodeResult);
        onTranslateCompleted(e, mapResult);

    }
}
