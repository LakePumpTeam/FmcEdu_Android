package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.fmc.edu.common.CrashHandler;
import com.fmc.edu.utils.ServicePreferenceUtils;


public class AuditingActivity extends Activity {

    private Button btnLoginOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auditing);
        initViews();
        initViewEvent();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

    private void initViews() {
        btnLoginOut = (Button) findViewById(R.id.auditing_btn_login_out);
    }

    private void initViewEvent() {

        btnLoginOut.setOnClickListener(loginOutClickListener);
    }

    private View.OnClickListener loginOutClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ServicePreferenceUtils.clearPasswordPreference(AuditingActivity.this);
            Intent intent = new Intent(AuditingActivity.this, LoginActivity.class);
            startActivity(intent);
            AuditingActivity.this.finish();
        }
    };
}
