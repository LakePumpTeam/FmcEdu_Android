package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.fmc.edu.common.CrashHandler;
import com.fmc.edu.customcontrol.MultiPictureControl;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.utils.ToastToolUtils;


public class PublishDynamicActivity extends Activity {
    private TopBarControl topBarSend;
    private EditText editContent;
    private GridView gridPicture;
    private TextView txtAddPicture;

    private final static int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        setContentView(R.layout.activity_publish_dynamic);
        initViews();
        initViewEvent();
    }

    private void initViews() {
        topBarSend = (TopBarControl) findViewById(R.id.publish_dynamic_top_bar_send);
        editContent = (EditText) findViewById(R.id.publish_dynamic_edit_content);
        gridPicture = (GridView) findViewById(R.id.publish_dynamic_grid_picture);
        txtAddPicture = (TextView) findViewById(R.id.publish_dynamic_txt_add_picture);
    }

    private void initViewEvent() {
        topBarSend.setOnOperateOnClickListener(sendListener);
        txtAddPicture.setOnClickListener(addPictureClickListener);

    }

    private TopBarControl.OnOperateOnClickListener sendListener = new TopBarControl.OnOperateOnClickListener() {
        @Override
        public void onOperateClick(View v) {
            //TODO 发送动态
        }
    };

    private View.OnClickListener addPictureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Intent intent = new Intent(PublishDynamicActivity.this, MultiPictureActivity.class);
//            startActivityForResult(intent, REQUEST_CODE);
            MultiPictureControl multiPictureControl = new MultiPictureControl(PublishDynamicActivity.this);
            multiPictureControl.showWindow(txtAddPicture);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ToastToolUtils.showLong("test")
        ;
    }
}
