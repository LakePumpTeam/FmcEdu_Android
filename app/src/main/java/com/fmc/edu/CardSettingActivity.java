package com.fmc.edu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.fmc.edu.adapter.CardSettingAdapter;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.ConvertUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CardSettingActivity extends BaseActivity {

    private ListView lvList;
    private CardSettingAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_card_setting);
        findViews();
        initData();
    }

    private void findViews() {
        lvList = (ListView) findViewById(R.id.card_setting_lv_list);
    }

    private void initData() {
        List<Map<String, Object>> list = (List<Map<String, Object>>) getIntent().getExtras().getSerializable("list");
        mAdapter = new CardSettingAdapter(this, list);
        lvList.setAdapter(mAdapter);
    }

    public static void startCardSettingActivity(final BaseActivity activity) {
        activity.mProgressControl.showWindow();
        Map<String, Object> params = new HashMap<>();
        LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
        params.put("pageIndex", 1);
        params.put("studentId", loginUserEntity.studentId);
        params.put("type", 0);
        MyIon.httpPost(activity, "clock/in/clockInRecords", params, activity.mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                List<Map<String, Object>> list = ConvertUtils.getList(data.get("record"));
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) list);
                Intent intent = new Intent(activity, CardSettingActivity.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);

            }
        });
    }
}
