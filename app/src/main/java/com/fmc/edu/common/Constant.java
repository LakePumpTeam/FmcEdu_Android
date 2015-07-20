package com.fmc.edu.common;

import android.os.Environment;

import com.fmc.edu.entity.CommonEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Candy on 2015-05-21.
 */
public class Constant {
    public static final int PAGE_SIZE = 10;
    public static final String TEMP_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/fmc_edu/temp";
    public static final String CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/fmc_edu/cache";

    public static List<CommonEntity> getRelationList() {
        List<CommonEntity> list = new ArrayList<CommonEntity>();
        list.add(new CommonEntity("1", "爸爸"));
        list.add(new CommonEntity("2", "妈妈"));
        list.add(new CommonEntity("3", "爷爷"));
        list.add(new CommonEntity("4", "奶奶"));
        list.add(new CommonEntity("5", "姥爷"));
        list.add(new CommonEntity("6", "姥姥"));
        list.add(new CommonEntity("7", "其他"));
        return list;
    }

}
