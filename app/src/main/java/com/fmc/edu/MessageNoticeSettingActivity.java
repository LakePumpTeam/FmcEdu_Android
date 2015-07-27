package com.fmc.edu;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.fmc.edu.http.MyIon;
import com.fmc.edu.service.StillStartService;
import com.fmc.edu.utils.ServicePreferenceUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.util.HashMap;
import java.util.Map;


public class MessageNoticeSettingActivity extends BaseActivity {
    private CheckBox ckShake;
    private CheckBox ckRing;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_message_notice_setting);
        initViews();
        initViewEvent();
        initPageData();
    }

    private void initViews() {
        ckShake = (CheckBox) findViewById(R.id.message_notice_setting_ck_shake);
        ckRing = (CheckBox) findViewById(R.id.message_notice_setting_ck_ring);
        btnSave = (Button) findViewById(R.id.message_notice_setting_btn_save);
    }

    private void initViewEvent() {
        btnSave.setOnClickListener(btnSaveOnClickListener);
    }

    private void initPageData() {
        Map<String, Boolean> settingData = ServicePreferenceUtils.getNoticeSettingByPreference(this);
        if (null == settingData) {
            return;
        }
        ckShake.setChecked(settingData.get("shake"));
        ckRing.setChecked(settingData.get("ring"));
    }

    private View.OnClickListener btnSaveOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          mProgressControl.showWindow();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("userId", FmcApplication.getLoginUser().studentId);
            params.put("isBel",ckRing.isChecked());
            params.put("isVibra",ckShake.isChecked());
            MyIon.httpPost(MessageNoticeSettingActivity.this, "profile/appSetting", params, mProgressControl, new MyIon.AfterCallBack() {
                @Override
                public void afterCallBack(Map<String, Object> data) {
                    ToastToolUtils.showLong("设置成功");
                }
            });
//            Map<String, Boolean> mapData = new HashMap<String, Boolean>();
//            mapData.put("shake", ckShake.isChecked());
//            mapData.put("ring", ckRing.isChecked());
//            ServicePreferenceUtils.saveNoticeSettingPreference(MessageNoticeSettingActivity.this, mapData);
//            StillStartService.stopStartWork(MessageNoticeSettingActivity.this);
//            StillStartService.startStillStartService(MessageNoticeSettingActivity.this);
//            ToastToolUtils.showLong("设置成功");
        }
    };
}
