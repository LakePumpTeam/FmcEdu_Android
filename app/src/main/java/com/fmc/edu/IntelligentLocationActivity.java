package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;

import com.fmc.edu.customcontrol.AlertWindowControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;


public class IntelligentLocationActivity extends BaseActivity {

    private TextView txtSurrounding;
    private TextView txtLocation;
    private TextView txtIntroduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_intelligent_location);
        initViews();
        initViewEvent();
    }

    private void initViews() {
        txtSurrounding = (TextView) findViewById(R.id.intelligent_location_txt_surrounding);
        txtLocation = (TextView) findViewById(R.id.intelligent_location_txt_location);
        txtIntroduce = (TextView) findViewById(R.id.intelligent_location_txt_introduce);
    }

    private void initViewEvent() {
        txtSurrounding.setOnClickListener(txtSurroundingOnClickListener);
        txtLocation.setOnClickListener(txtLocationOnClickListener);
        txtIntroduce.setOnClickListener(txtIntroduceOnClickListener);
    }

    private View.OnClickListener txtSurroundingOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            String userCardNum = FmcApplication.getLoginUser().userCardNum;
            if (StringUtils.isEmptyOrNull(userCardNum) || "0".equals(userCardNum)) {
                AlertWindowControl alertWindowControl = new AlertWindowControl(IntelligentLocationActivity.this);
                alertWindowControl.showWindow(v, "提示", "您还未绑定设备");
                return;
            }
            if (userCardNum.length() != 11) {
                AlertWindowControl alertWindowControl = new AlertWindowControl(IntelligentLocationActivity.this);
                alertWindowControl.showWindow(v, "提示", "设备卡号错误");
                return;
            }

            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + userCardNum));
            startActivity(intent);

        }
    };
    private View.OnClickListener txtLocationOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            String userCardNum = FmcApplication.getLoginUser().userCardNum;
            if (StringUtils.isEmptyOrNull(userCardNum) || "0".equals(userCardNum)) {
                AlertWindowControl alertWindowControl = new AlertWindowControl(IntelligentLocationActivity.this);
                alertWindowControl.showWindow(v, "提示", "您还未绑定设备");
                return;
            }
            if (userCardNum.length() != 11) {
                AlertWindowControl alertWindowControl = new AlertWindowControl(IntelligentLocationActivity.this);
                alertWindowControl.showWindow(v, "提示", "设备卡号错误");
                return;
            }
            SmsManager smsMgr = SmsManager.getDefault();
            smsMgr.sendTextMessage(userCardNum, null, "测试短信发送功能", null, null);
            ToastToolUtils.showLong("已发送");
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:" + "15881037068"));
//            intent.putExtra("sms_body","sfadfafd");
//            startActivity(intent);
        }
    };
    private View.OnClickListener txtIntroduceOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(IntelligentLocationActivity.this, BraceletIntroduceActivity.class);
            startActivity(intent);
        }
    };

}
