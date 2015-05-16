package com.fmc.edu.http;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.Base64;
import android.util.Log;
import android.widget.PopupWindow;

import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.NetworkUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.builder.Builders;
import com.koushikdutta.ion.future.ResponseFuture;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
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

    public static ResponseFuture<String> setUrlAndBodyParams(Context context, String url, Map<String, Object> params) throws NetWorkUnAvailableException {
        Builders.Any.B withB = MyIon.with(context).load(url);
        if (null == params) {
            return withB.asString(Charset.forName("utf8"));
        }
        for (String key : params.keySet()) {
            Object value = params.get(key);
            try {
                byte[] bytes = getBytesFromObject(value);
                withB.setBodyParameter(key, Base64.encodeToString(bytes, Base64.DEFAULT));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return withB.asString(Charset.forName("utf8"));
    }

    public static byte[] getBytesFromObject(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer || obj instanceof String || obj instanceof SpannableStringBuilder) {
            return obj.toString().getBytes();
        }
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(obj);
        return bo.toByteArray();
    }

    public static void httpPost(Context context, String url, Map<String, Object> params, final ProgressControl progressControl, final AfterCallBack afterCallBack) {
        try {
            MyIon.setUrlAndBodyParams(context, url, params)
                    .setCallback(new FMCMapFutureCallback() {
                        @Override
                        public void onTranslateCompleted(Exception e, Map<String, ?> result) {
                            if (null != progressControl) {
                                progressControl.dismiss();
                            }

                            if (!HttpTools.isRequestSuccessfully(e, result))

                            {
                                ToastToolUtils.showLong(result.get("msg").toString());
                                return;
                            }

                            if (StringUtils.isEmptyOrNull(result.get("data")))

                            {
                                ToastToolUtils.showLong("服务器出错");
                                return;
                            }

                            Map<String, Object> mapData = (Map<String, Object>) result.get("data");
                            if (ConvertUtils.getInteger(mapData.get("isSuccess")) != 0)

                            {
                                ToastToolUtils.showLong(ConvertUtils.getString(mapData.get("businessMsg")));
                                return;
                            }

                            afterCallBack.afterCallBack(mapData);
                        }
                    });
        } catch (NetWorkUnAvailableException e) {
            if (null != progressControl) {
                progressControl.dismiss();
            }
            e.printStackTrace();
        }
    }

    public static interface AfterCallBack {
        void afterCallBack(Map<String, Object> data);
    }

}
