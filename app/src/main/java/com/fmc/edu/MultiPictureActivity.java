package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.FrameLayout;

import com.fmc.edu.customcontrol.ImageSelectListControl;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.ImageItemEntity;


public class MultiPictureActivity extends Activity {
    private ImageSelectListControl imageSelectGridList;
    private TopBarControl topBar;
    private int MAX_PICTURE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_picture);
        initViews();
    }

    private void initViews() {
        imageSelectGridList = (ImageSelectListControl) findViewById(R.id.image_select_grid_list);
        imageSelectGridList.setOnAfterSelectedListener(onAfterSelectedListener);
    }

    private ImageSelectListControl.OnAfterSelectedListener onAfterSelectedListener = new ImageSelectListControl.OnAfterSelectedListener() {
        @Override
        public void onAfterSelected(int selectedCount) {
            String text = selectedCount + "/" + MAX_PICTURE;
            topBar.setTopBarText(text);
        }
    };

    @Override
    protected void onDestroy() {

        Log.e("destory", "**************************");

        super.onDestroy();
        imageSelectGridList.clearCache();
    }
}
