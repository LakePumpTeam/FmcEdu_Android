package com.fmc.edu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.fmc.edu.adapter.MessageListAdapter;
import com.fmc.edu.entity.MessageListEntity;
import com.fmc.edu.enums.MessageTypeEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessageListActivity extends BaseActivity {
    private ListView lvList;
    private MessageListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_message_list);
        findViews();
        initData();
    }

    private void findViews() {
        lvList = (ListView) findViewById(R.id.message_list_lv);
    }

    private void initData() {
        List<MessageListEntity> list = (List<MessageListEntity>) getIntent().getExtras().getSerializable("list");
        mAdapter = new MessageListAdapter(this, list);
        lvList.setAdapter(mAdapter);
    }


    public static void startMessageActivity(Context context) {
        Intent intent = new Intent(context, MessageListActivity.class);
        Bundle bundle = new Bundle();
        List<MessageListEntity> list = buildMessageSettingData();
        bundle.putSerializable("list", (Serializable) list);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }


    private static List<MessageListEntity> buildMessageSettingData() {
        List<MessageListEntity> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            MessageListEntity item = new MessageListEntity();
            item.content = "提示内容";
            item.date = "2015-06-15";
            item.messageType = MessageTypeEnum.Common;
            item.typeName = MessageTypeEnum.GetMessageTypeName(i % 2);
            list.add(item);
        }
        return list;
    }
}
