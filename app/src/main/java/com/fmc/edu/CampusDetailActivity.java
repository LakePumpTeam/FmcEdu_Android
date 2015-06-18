package com.fmc.edu;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fmc.edu.customcontrol.ImageShowControl;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.CampusSelectionEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ImageLoaderUtil;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CampusDetailActivity extends BaseActivity {
    private Button btnSubmit;
    private LinearLayout llPicture;
    private LinearLayout llSuggest;
    private TextView txtTitle;
    private TextView txtContent;
    private TextView txtDate;
    private TextView txtPartIn;
    private TopBarControl topBar;
    private Bundle mBundle;
    private boolean isSubmit;
    private List<CampusSelectionEntity> mSelections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_campus_detail);
        mBundle = getIntent().getExtras();
        initView();
        initViewEvent();
        initPageData();
    }

    private void initView() {
        btnSubmit = (Button) findViewById(R.id.campus_detail_btn_submit);
        llPicture = (LinearLayout) findViewById(R.id.campus_detail_ll_picture);
        llSuggest = (RadioGroup) findViewById(R.id.campus_detail_ll_suggest);
        txtTitle = (TextView) findViewById(R.id.campus_detail_txt_title);
        txtContent = (TextView) findViewById(R.id.campus_detail_txt_content);
        txtDate = (TextView) findViewById(R.id.campus_detail_txt_date);
        txtPartIn = (TextView) findViewById(R.id.campus_detail_txt_part_in);
        topBar = (TopBarControl) findViewById(R.id.campus_detail_top_bar);
    }

    private void initViewEvent() {
        btnSubmit.setOnClickListener(btnSubmitOnClickListener);
        topBar.setOnOperateOnClickListener(topBarOnOperateOnClickListener);
    }

    private void initPageData() {
        if (null == mBundle) {
            return;
        }
        String subject = mBundle.getString("subject");
        String title = subject.length() > 8 ? subject.substring(0, 8) + "..." : subject;
        topBar.setTopBarText(title);
        txtTitle.setText(subject);
        txtContent.setText(mBundle.getString("content"));
        txtDate.setText(mBundle.getString("createDate"));
        txtPartIn.setText(ConvertUtils.getString(mBundle.getInt("participationCount", 0)));
        mSelections = (List<CampusSelectionEntity>) mBundle.getSerializable("selections");
        initRgSuggest(mSelections);
        partInComment(!mBundle.getBoolean("isParticipation", false));
        List<String> actualImageUrls = getActualImageUrl(mBundle.getStringArrayList("imageUrl"));
        bindPicture(actualImageUrls);
    }

    private void initRgSuggest(List<CampusSelectionEntity> selections) {
        if (null == selections || 0 == selections.size()) {
            btnSubmit.setVisibility(View.GONE);
            return;
        }
        btnSubmit.setVisibility(View.VISIBLE);
        for (CampusSelectionEntity item : selections) {
            CheckBox checkBox = createCheckBox(item);
            RadioGroup.LayoutParams params_rb = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params_rb.setMargins(10, 10, 10, 10);
            llSuggest.addView(checkBox, params_rb);
        }
    }

    private CheckBox createCheckBox(CampusSelectionEntity campusSelection) {
        CheckBox checkBox = new CheckBox(this);
        checkBox.setTag(campusSelection.selectionId);
        checkBox.setChecked(campusSelection.isSelected);
        checkBox.setText(campusSelection.selection);
        checkBox.setButtonDrawable(R.drawable.selector_check_box_check);
        checkBox.setPadding(ConvertUtils.getInteger(getResources().getDimension(R.dimen.activity_padding_middle), 0), 0, 0, 0);
        checkBox.setTextColor(getResources().getColor(R.color.text_font_color));
        checkBox.setOnCheckedChangeListener(checkedChangeListener);
        return checkBox;
    }

    private void bindPicture(List<String> imageUrls) {
        for (int i = 0; i < imageUrls.size(); i++) {
            ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.item_single_picture, null);
            DisplayMetrics displayMetrics = new DisplayMetrics();

            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels - 20;
            imageView.setMaxWidth(screenWidth);
            imageView.setMaxHeight(screenWidth * 5);//这里其实可以根据需求而定，我这里测试为最大宽度的5倍
            Map<String, Object> map = new HashMap<>();
            map.put("position", i);
            map.put("imgUrls", imageUrls);
            imageView.setTag(map);
            imageView.setOnClickListener(imageOnClickListener);
            ImageLoaderUtil.initCacheImageLoader(this).displayImage(imageUrls.get(i), imageView);
            llPicture.addView(imageView);
        }
    }

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int selectId = ConvertUtils.getInteger(buttonView.getTag(), 0);
            updateSelections(selectId, isChecked);
        }
    };

    private void updateSelections(int selectId, boolean isChecked) {
        for (CampusSelectionEntity item : mSelections) {
            if (item.selectionId == selectId) {
                item.isSelected = isChecked;
                return;
            }
        }
    }


    private View.OnClickListener btnSubmitOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String  selectIds = getSelectedIds();
            if (StringUtils.isEmptyOrNull(selectIds)) {
                ToastToolUtils.showLong("请先选择要提交的观点");
                return;
            }

            mProgressControl.showWindow();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("newsId", mBundle.getInt("newsId"));
            params.put("userId", FmcApplication.getLoginUser().userId);
            params.put("selectionId", selectIds);
            MyIon.httpPost(CampusDetailActivity.this, "news/submitParticipation", params, mProgressControl, new MyIon.AfterCallBack() {
                @Override
                public void afterCallBack(Map<String, Object> data) {
                    isSubmit = true;
                    txtPartIn.setText(ConvertUtils.getString(mBundle.getInt("participationCount", 0) + 1));
                    ToastToolUtils.showLong("提交成功,谢谢参与");
                    partInComment(false);
                }
            });
        }
    };

    private String getSelectedIds() {
        String ids = "";
        for (CampusSelectionEntity item : mSelections) {
            if (item.isSelected) {
                ids += item.selectionId + ",";
                continue;
            }
        }
        return StringUtils.isEmptyOrNull(ids) ? "" : ids.substring(0, ids.length() - 1);
    }


    private TopBarControl.OnOperateOnClickListener topBarOnOperateOnClickListener = new TopBarControl.OnOperateOnClickListener() {
        @Override
        public void onBackClick(View view) {
            gotoBack();
        }

        @Override
        public void onOperateClick(View v) {

        }
    };

    private View.OnClickListener imageOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Map<String, Object> map = (Map<String, Object>) v.getTag();
            List<String> bigPictureUrl = (List<String>) map.get("imgUrls");
            if (null == bigPictureUrl || 0 == bigPictureUrl.size()) {
                ToastToolUtils.showLong("无有效图片");
                return;
            }
            ImageShowControl imageShowControl = new ImageShowControl(CampusDetailActivity.this);
            imageShowControl.showWindow(v, bigPictureUrl, ConvertUtils.getInteger(map.get("position"), 0));
        }
    };

    @Override
    public void onBackPressed() {
        gotoBack();
        CampusDetailActivity.this.finish();
    }

    private void gotoBack() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isSubmit", isSubmit);
        bundle.putInt("newsId", mBundle.getInt("newsId"));
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
    }

    private void partInComment(boolean isEnable) {
        llSuggest.setEnabled(isEnable);
        btnSubmit.setEnabled(isEnable);
        btnSubmit.setText(isEnable ? "提交" : "您已提交过");
        setRadioButtonEnable(isEnable);
    }

    private void setRadioButtonEnable(boolean isEnable) {
        int childCount = llSuggest.getChildCount();
        for (int i = 0; i < childCount; i++) {
            llSuggest.getChildAt(i).setEnabled(isEnable);
        }
    }

    private List<String> getActualImageUrl(List<String> oldList) {
        List<String> actualUrls = new ArrayList<>();
        for (String oldUrl : oldList) {
            actualUrls.add(AppConfigUtils.getServiceHost() + oldUrl);
        }
        return actualUrls;
    }
}
