package com.fmc.edu.http;

import android.content.Context;
import android.util.Base64;
import android.widget.PopupWindow;

import com.fmc.edu.utils.NetworkUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.koushikdutta.ion.future.ResponseFuture;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by Candy on 2015/5/3.
 */
public class MyIon {
    public static com.koushikdutta.ion.builder.LoadBuilder<com.koushikdutta.ion.builder.Builders.Any.B> with(Context context) throws NetWorkUnAvailableException {
        if (!NetworkUtils.isNetworkConnected(context)) {
            ToastToolUtils.showLong("网络不可用");
            throw new NetWorkUnAvailableException("网络不可用");
        }
        return Ion.with(context);
    }

    public static ResponseFuture<String> setUrlAndBodyParams(Context context, String url, Map<String, Object> params, PopupWindow popupWindow) {

        try {
            Builders.Any.B withB = MyIon.with(context).load(url);
            if (null == params) {
                return withB.asString(Charset.forName("utf8"));
            }

            for (String key : params.keySet()) {
                String value = params.get(key).toString();
                withB.setBodyParameter(key, Base64.encodeToString(value.getBytes(), Base64.DEFAULT));
            }
            return withB.asString(Charset.forName("utf8"));
        } catch (NetWorkUnAvailableException e) {
            e.printStackTrace();
            popupWindow.dismiss();
            return null;
        }
    }
}
