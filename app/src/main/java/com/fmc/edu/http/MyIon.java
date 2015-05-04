package com.fmc.edu.http;

import android.content.Context;

import com.fmc.edu.utils.NetworkUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.koushikdutta.ion.Ion;

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
}
