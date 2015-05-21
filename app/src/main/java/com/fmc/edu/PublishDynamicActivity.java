package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.fmc.edu.adapter.PublishDynamicGridAdapter;
import com.fmc.edu.common.Constant;
import com.fmc.edu.common.CrashHandler;
import com.fmc.edu.customcontrol.MultiPictureControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.ImageItemEntity;
import com.fmc.edu.entity.ImageLoaderUtil;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.RequestCodeUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


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
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
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

    private TopBarControl.OnOperateOnClickListener sendListener = new TopBarControl.OnOperateOnClickListener() {
        @Override
        public void onOperateClick(View v) {
            if (StringUtils.isEmptyOrNull(editContent.getText())) {
                ToastToolUtils.showLong("请输入内容呢");
                return;
            }
            //TODO 发送动态
            ToastToolUtils.showLong("发送成功");
            PublishDynamicActivity.this.finish();
//            mProgressControl.showWindow(topBarSend);
//
//            String content = editContent.getText().toString();
//            String base64UserId = Base64.encodeToString(String.valueOf(FmcApplication.getLoginUser().userId).getBytes(), Base64.DEFAULT);
//            try {
//                MyIon.with(PublishDynamicActivity.this)
//                        .load(mHostUrl + "postClassNews")
//                        .setMultipartFile("images", ImageFactoryUtils.getThumbnailImage(mAdapter.getImageUrl(0)))
//                        .setMultipartFile("images", ImageFactoryUtils.getThumbnailImage(mAdapter.getImageUrl(1)))
//                        .setMultipartFile("images", ImageFactoryUtils.getThumbnailImage(mAdapter.getImageUrl(2)))
//                        .setMultipartFile("images", ImageFactoryUtils.getThumbnailImage(mAdapter.getImageUrl(3)))
//                        .setMultipartParameter("content", Base64.encodeToString(content.getBytes(), Base64.DEFAULT))
//                        .setMultipartParameter("userId", base64UserId)
//                        .asString(Charset.forName("utf8"))
//                        .setCallback(new FMCMapFutureCallback() {
//                            @Override
//                            public void onTranslateCompleted(Exception e, Map<String, ?> result) {
//                                if (null != mProgressControl) {
//                                    mProgressControl.dismiss();
//                                }
//
//                                if (!HttpTools.isRequestSuccessfully(e, result))
//
//                                {
//                                    ToastToolUtils.showLong(result.get("msg").toString());
//                                    return;
//                                }
//
//                                if (StringUtils.isEmptyOrNull(result.get("data")))
//
//                                {
//                                    ToastToolUtils.showLong("服务器出错");
//                                    return;
//                                }
//
//                                Map<String, Object> mapData = (Map<String, Object>) result.get("data");
//                                if (ConvertUtils.getInteger(mapData.get("isSuccess")) != 0) {
//                                    AlertWindowControl alertWindowControl = new AlertWindowControl(PublishDynamicActivity.this);
//                                    alertWindowControl.showWindow(new TextView(PublishDynamicActivity.this), "提示", ConvertUtils.getString(mapData.get("businessMsg")));
//                                    return;
//                                }
//                                ToastToolUtils.showLong("发送成功");
//                                PublishDynamicActivity.this.finish();
//                            }
//                        });
//
//            } catch (Exception ex) {
//                mProgressControl.dismiss();
//                ToastToolUtils.showLong(ex == null ? "发送失败" : ex.getMessage());
//            }
        }
    };
    private View.OnClickListener addPictureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(PublishDynamicActivity.this, MultiPictureActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("selectedList", (ArrayList<? extends Parcelable>) mAdapter.getItems());
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
            List<ImageItemEntity> list = data.getExtras().getParcelableArrayList("selectedList");
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
}
