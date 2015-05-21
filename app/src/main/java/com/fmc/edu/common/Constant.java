package com.fmc.edu.common;

import android.os.Environment;

/**
 * Created by Candy on 2015-05-21.
 */
public class Constant {
    public static final int PAGE_SIZE = 10;
    public static final String TEMP_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/fmc_edu/temp";
    public static final String CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/fmc_edu/cache";
}
