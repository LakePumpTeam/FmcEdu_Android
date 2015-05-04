package com.fmc.edu.utils;

import android.widget.Toast;

import com.fmc.edu.FmcApplication;

/**
 * Created by Candy on 2015/5/3.
 */
public class ToastToolUtils {
    public static void showLong(String msg) {
        Toast.makeText(FmcApplication.getApplication(), msg, Toast.LENGTH_LONG).show();
    }

    public static void showShort(String msg) {
        Toast.makeText(FmcApplication.getApplication(), msg, Toast.LENGTH_SHORT).show();
    }
}
