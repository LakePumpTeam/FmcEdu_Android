package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.fmc.edu.adapter.PublishDynamicGridAdapter;
import com.fmc.edu.customcontrol.AlertWindowControl;
import com.fmc.edu.customcontrol.MultiPictureControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.ImageItemEntity;
import com.fmc.edu.http.FMCMapFutureCallback;
import com.fmc.edu.http.HttpTools;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ImageFactoryUtils;
import com.fmc.edu.utils.ImageLoaderUtil;
import com.fmc.edu.utils.RequestCodeUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.koushikdutta.ion.builder.Builders;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;


public class PublishDynamicActivity extends Activity {
    private TopBarControl topBarSend;
    private EditText editContent;
    private GridView gridPicture;
    private TextView txtAddPicture;
    private final static int REQUEST_CODE = 1;
    private ImageLoader mImageLoader;
    private String mHostUrl;
    private PublishDynamicGridAdapter mAdapter;
    private ProgressControl mProgressControl;
    private MultiPictureControl multiPictureControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this);
        setContentView(R.layout.activity_publish_dynamic);
        mProgressControl = new ProgressControl(this);
        mHostUrl = AppConfigUtils.getServiceHost();
        initViews();
        initViewEvent();
        mImageLoader = ImageLoaderUtil.initCacheImageLoader(this);
        mAdapter = new PublishDynamicGridAdapter(PublishDynamicActivity.this, null, mImageLoader);
        gridPicture.setAdapter(mAdapter);
    }

    private void initViews() {
        topBarSend = (TopBarControl) findViewById(R.id.publish_dynamic_top_bar_send);
        editContent = (EditText) findViewById(R.id.publish_dynamic_edit_content);
        gridPicture = (GridView) findViewById(R.id.publish_dynamic_grid_picture);
        txtAddPicture = (TextView) findViewById(R.id.publish_dynamic_txt_add_picture);
        gridPicture.setSelector(new ColorDrawable(Color.TRANSPARENT));

    }

    private void initViewEvent() {
        topBarSend.setOnOperateOnClickListener(sendListener);
        txtAddPicture.setOnClickListener(addPictureClickListener);
        gridPicture.setOnItemLongClickListener(gridOnItemLongClickListener);
    }

    private View.OnClickListener addPictureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PublishDynamicActivity.this, MultiPictureActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("selectedList", (Serializable) mAdapter.getItems());
            intent.putExtras(bundle);
            startActivityForResult(intent, RequestCodeUtils.SELECTED_PICTURE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!(resultCode == RESULT_OK)) {
            return;
        }

        if (requestCode == RequestCodeUtils.SELECTED_PICTURE) {
            List<ImageItemEntity> list = (List<ImageItemEntity>) data.getExtras().getSerializable("selectedList");
            mAdapter.addAll(list);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private AdapterView.OnItemLongClickListener gridOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mAdapter.removeItem(position);
            return false;
        }
    };


    private TopBarControl.OnOperateOnClickListener sendListener = new TopBarControl.OnOperateOnClickListener() {
        @Override
        public void onBackClick(View view) {

        }

        @Override
        public void onOperateClick(View v) {
            if (StringUtils.isEmptyOrNull(editContent.getText())) {
                ToastToolUtils.showLong("请输入内容呢");
                return;
            }
            mProgressControl.showWindow(topBarSend);

            String content = editContent.getText().toString();
            String base64UserId = Base64.encodeToString(String.valueOf(FmcApplication.getLoginUser().userId).getBytes(), Base64.DEFAULT);
            try {
                Builders.Any.B withB = MyIon.with(PublishDynamicActivity.this)
                        .load(mHostUrl + "news/postClassNews");
                withB.setMultipartParameter("content", Base64.encodeToString(content.getBytes(), Base64.DEFAULT))
                        .setMultipartParameter("userId", base64UserId);

                for (int i = 1; i < mAdapter.getCount() + 1; i++) {
                    String url = mAdapter.getImageUrl(i - 1);
                    if (StringUtils.isEmptyOrNull(url)) {
                        continue;
                    }

                    withB.setMultipartFile("imgs", new File(ImageFactoryUtils.getThumbnailImage(url)));
                }
                withB.asString(Charset.forName("utf8"))
                        .setCallback(new FMCMapFutureCallback(mProgressControl) {
                            @Override
                            public void onTranslateCompleted(Exception e, Map<String, ?> result) {
                                mProgressControl.dismiss();
                                if (!HttpTools.isRequestSuccessfully(e, result)) {
                                    ToastToolUtils.showLong(result.get("msg").toString());
                                    return;
                                }

                                if (StringUtils.isEmptyOrNull(result.get("data"))) {
                                    ToastToolUtils.showLong("服务器出错");
                                    return;
                                }

                                Map<String, Object> mapData = (Map<String, Object>) result.get("data");
                                if (ConvertUtils.getInteger(mapData.get("isSuccess")) != 0) {
                                    AlertWindowControl alertWindowControl = new AlertWindowControl(PublishDynamicActivity.this);
                                    alertWindowControl.showWindow(new TextView(PublishDynamicActivity.this), "提示", ConvertUtils.getString(mapData.get("businessMsg")));
                                    return;
                                }
                                ToastToolUtils.showLong("发送成功");
                                setResult(RESULT_OK);
                                PublishDynamicActivity.this.finish();
                            }
                        });

            } catch (Exception ex) {
                mProgressControl.dismiss();
                ToastToolUtils.showLong(ex == null ? "发送失败" : ex.getMessage());
            }
        }
    };

}
