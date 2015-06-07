package com.fmc.edu;

import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fmc.edu.adapter.SyllabusAdapter;
import com.fmc.edu.customcontrol.AlertWindowControl;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.entity.SyllabusEntity;
import com.fmc.edu.http.FMCMapFutureCallback;
import com.fmc.edu.http.HttpTools;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.koushikdutta.ion.builder.Builders;

import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SyllabusActivity extends BaseActivity {
    private HorizontalScrollView scrollView;
    private RadioGroup rgTab;
    private RadioButton rbTabMon;
    private RadioButton rbTabTue;
    private RadioButton rbTabWeb;
    private RadioButton rbTabThu;
    private RadioButton rbTabFri;
    private RadioButton rbTabSat;
    private RadioButton rbTabSun;
    private ListView listView;
    private TopBarControl topBar;
    private SyllabusAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_syllabus);
        initViews();
        initViewEvent();
        initRadioButtonWidth();
        initPageData();
    }

    private void initViews() {
        scrollView = (HorizontalScrollView) findViewById(R.id.syllabus_scroll_view);
        rgTab = (RadioGroup) findViewById(R.id.syllabus_rg_tab);
        rbTabMon = (RadioButton) findViewById(R.id.syllabus_rb_tab_mon);
        rbTabTue = (RadioButton) findViewById(R.id.syllabus_rb_tab_tue);
        rbTabWeb = (RadioButton) findViewById(R.id.syllabus_rb_tab_wed);
        rbTabThu = (RadioButton) findViewById(R.id.syllabus_rb_tab_thu);
        rbTabFri = (RadioButton) findViewById(R.id.syllabus_rb_tab_fri);
        rbTabSat = (RadioButton) findViewById(R.id.syllabus_rb_tab_sat);
        rbTabSun = (RadioButton) findViewById(R.id.syllabus_rb_tab_sun);
        listView = (ListView) findViewById(R.id.syllabus_list_view);
        topBar = (TopBarControl) findViewById(R.id.syllabus_top_bar);
    }

    private void initViewEvent() {
        rgTab.setOnCheckedChangeListener(rgTabOnCheckedChangedListener);
        topBar.setOnOperateOnClickListener(topBarOnOperateOnClickListener);
    }

    private void initRadioButtonWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels / 5;
        ViewGroup.LayoutParams params = rbTabMon.getLayoutParams();
        params.width = width;
        rbTabMon.setLayoutParams(params);
        rbTabTue.setLayoutParams(params);
        rbTabWeb.setLayoutParams(params);
        rbTabThu.setLayoutParams(params);
        rbTabFri.setLayoutParams(params);
        rbTabSat.setLayoutParams(params);
        rbTabSun.setLayoutParams(params);
    }

    private void initPageData() {
        Bundle bundle = getIntent().getExtras();
        if (null == bundle) {
            return;
        }

        List<SyllabusEntity> list = (List<SyllabusEntity>) bundle.getSerializable("list");
        mAdapter = new SyllabusAdapter(SyllabusActivity.this, list);
        listView.setAdapter(mAdapter);
    }

    private RadioGroup.OnCheckedChangeListener rgTabOnCheckedChangedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            View view = findViewById(checkedId);
            int tag = ConvertUtils.getInteger(view.getTag());
            changeWeekChecked(tag);
        }
    };

    private TopBarControl.OnOperateOnClickListener topBarOnOperateOnClickListener = new TopBarControl.OnOperateOnClickListener() {
        @Override
        public void onBackClick(View view) {

        }

        @Override
        public void onOperateClick(View v) {
            mProgressControl.showWindow();

            try {
                Builders.Any.B withB = MyIon.with(SyllabusActivity.this)
                        .load(AppConfigUtils.getServiceHost() + "school/submitClassCourse");
                withB.setMultipartParameter("classId", ConvertUtils.getString(FmcApplication.getLoginUser().classId, ""));
                for (SyllabusEntity syllabusEntity : mAdapter.mItems) {
                    Map<String, Object> item = syllabusEntity.toMapBySyllabus();
                    JSONObject json = new JSONObject(item);
                    withB.setMultipartParameter("courses", Base64.encodeToString(json.toString().getBytes(), Base64.DEFAULT));
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
                                    ToastToolUtils.showLong("上传失败,服务器出错");
                                    return;
                                }
                                ToastToolUtils.showLong("上传成功");
                            }
                        });

            } catch (Exception ex) {
                mProgressControl.dismiss();
                ToastToolUtils.showLong(ex == null ? "上传失败" : ex.getMessage());
            }
        }
    };


    private void setOnScrollX(int tag) {
        if (tag == 1 || tag == 2 || tag == 3) {
            scrollView.setScrollX(0);
        }
        if (tag == 4) {
            scrollView.setScrollX(ConvertUtils.getInteger(rbTabTue.getX(), 0));
        }
        if (tag == 5 || tag == 6 || tag == 7) {
            scrollView.setScrollX(ConvertUtils.getInteger(rbTabWeb.getX(), 0));
        }
    }

    private void changeWeekChecked(final int week) {
        mProgressControl.showWindow();
        LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(this);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("classId", loginUserEntity.classId);
        params.put("week", week);
        MyIon.httpPost(this, "school/requestClassCourseList", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                setOnScrollX(week);
                List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("syllabusList");
                mAdapter.addAllItems(SyllabusEntity.toSyllabusEntity(list), true);
            }
        });
    }
}
