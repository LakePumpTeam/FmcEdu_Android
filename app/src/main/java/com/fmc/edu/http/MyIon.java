package com.fmc.edu.http;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import com.fmc.edu.customcontrol.AlertWindowControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.NetworkUtils;
import com.fmc.edu.utils.StringUtils;
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
    public static com.koushikdutta.ion.builder.LoadBuilder<com.koushikdutta.ion.builder.Builders.Any.B> with(Context context) throws NetworkErrorException {
        if (!NetworkUtils.isNetworkConnected(context)) {
            ToastToolUtils.showLong("网络不可用");
            throw new NetworkErrorException("网络不可用");
        }
        return Ion.with(context);
    }

    public static ResponseFuture<String> setUrlAndBodyParams(Context context, String url, Map<String, Object> params) throws Exception {
        Builders.Any.B withB = MyIon.with(context).load(url);
        if (null == params) {
            return withB.asString(Charset.forName("utf8"));
        }
        for (String key : params.keySet()) {
            Object value = params.get(key);
            try {
                if (StringUtils.isEmptyOrNull(value)) {
                    withB.setBodyParameter(key, "");
                    continue;
                }
                byte[] bytes = value.toString().getBytes();
                withB.setBodyParameter(key, Base64.encodeToString(bytes, Base64.DEFAULT));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return withB.asString(Charset.forName("utf8"));
    }

    public static void httpPost(final Context context, String url, Map<String, Object> params, final ProgressControl progressControl, final AfterCallBack afterCallBack) {
        try {
            MyIon.setUrlAndBodyParams(context, url, params)
                    .setCallback(new FMCMapFutureCallback(progressControl) {
                        @Override
                        public void onTranslateCompleted(Exception e, Map<String, ?> result) {

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
                            if (ConvertUtils.getInteger(mapData.get("isSuccess")) != 0) {
                                AlertWindowControl alertWindowControl = new AlertWindowControl(context);
                                alertWindowControl.showWindow(new TextView(context), "提示", ConvertUtils.getString(mapData.get("businessMsg")));
                                return;
                            }

                            afterCallBack.afterCallBack(mapData);
                        }
                    });
        } catch (NetWorkUnAvailableException e) {
            if (null != progressControl && progressControl.isShowing()) {
                progressControl.dismiss();
            }
            e.printStackTrace();
        } catch (Exception e) {
            if (null != progressControl && progressControl.isShowing()) {
                progressControl.dismiss();
            }
            e.printStackTrace();
        }
    }

    public static void httpPost1(final Context context, String method, Map<String, Object> params, ProgressControl progressControl, final AfterCallBack afterCallBack) {
        try {
            progressControl.showWindow();
            String url = AppConfigUtils.getServiceHost() + method;
            MyIon.setUrlAndBodyParams(context, url, params)
                    .setCallback(new FMCMapFutureCallback(progressControl) {
                        @Override
                        public void onTranslateCompleted(Exception e, Map<String, ?> result) {

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
                            if (ConvertUtils.getInteger(mapData.get("isSuccess")) != 0) {
                                AlertWindowControl alertWindowControl = new AlertWindowControl(context);
                                alertWindowControl.showWindow(new TextView(context), "提示", ConvertUtils.getString(mapData.get("businessMsg")));
                                return;
                            }

                            afterCallBack.afterCallBack(mapData);
                        }
                    });
        } catch (NetWorkUnAvailableException e) {
            if (null != progressControl && progressControl.isShowing()) {
                progressControl.dismiss();
            }
            e.printStackTrace();
        } catch (Exception e) {
            if (null != progressControl && progressControl.isShowing()) {
                progressControl.dismiss();
            }
            e.printStackTrace();
        }
    }

    public interface AfterCallBack {
        void afterCallBack(Map<String, Object> data);
    }

}
