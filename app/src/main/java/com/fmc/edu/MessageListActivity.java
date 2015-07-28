package com.fmc.edu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.fmc.edu.adapter.MessageListAdapter;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.entity.MessageListEntity;
import com.fmc.edu.enums.MessageTypeEnum;
import com.fmc.edu.enums.UserRoleEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;

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


    public static void startMessageActivity(final Context context) {
        BaseActivity baseActivity = (BaseActivity) context;
        baseActivity.mProgressControl.showWindow();
        Map<String, Object> params = new HashMap<>();
        LoginUserEntity currentLoginUser = FmcApplication.getLoginUser();
        params.put("userId", currentLoginUser.userId);
        params.put("pageIndex", 1);
        params.put("pageSize", Integer.MAX_VALUE);
        MyIon.httpPost(context, "profile/queryPushMessage", params,  baseActivity.mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {

                List<Map<String, Object>> list = ConvertUtils.getList(data.get("record"));
                Intent intent = new Intent(context, MessageListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) MessageListEntity.ConvertMessageList(list));
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    public static void startNoticeMessageActivity(final Context context) {
        Map<String, Object> params = new HashMap<>();
        LoginUserEntity currentLoginUser = FmcApplication.getLoginUser();
        params.put("userId", currentLoginUser.userId);
        params.put("pageIndex", 1);
        params.put("pageSize", Integer.MAX_VALUE);
        MyIon.httpPost(context, "profile/queryPushMessage", params, null, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {

                List<Map<String, Object>> list = ConvertUtils.getList(data.get("record"));
                Intent intent = new Intent(context, MessageListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) MessageListEntity.ConvertMessageList(list));
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

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
