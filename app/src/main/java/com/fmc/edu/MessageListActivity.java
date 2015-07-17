package com.fmc.edu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessageListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_message_list);
    }



    public static  void  startMessageActivity(Context context){
        Intent intent = new Intent(context,MessageListActivity.class);
        Bundle bundle = new Bundle();
        List<Map<String, Object>> list = buildMessageSettingData();
        bundle.putSerializable("list", (Serializable) list);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }



    private static List<Map<String, Object>> buildMessageSettingData() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("cardNo", "100000" + i);
            item.put("parent", "家长" + i);
            item.put("isLose", i % 2 == 0);
            item.put("comment", "备注" + i);
            list.add(item);
        }
        return list;
    }
}
