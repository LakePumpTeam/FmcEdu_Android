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

import com.fmc.edu.adapter.SyllabusAdapter;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.entity.CourseEntity;
import com.fmc.edu.entity.WeekCourseEntity;
import com.fmc.edu.http.FMCMapFutureCallback;
import com.fmc.edu.http.HttpTools;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.builder.Builders;

import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
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
    private List<WeekCourseEntity> mList;


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
        mList = (List<WeekCourseEntity>) bundle.getSerializable("list");
        mAdapter = new SyllabusAdapter(SyllabusActivity.this, getWeekCourseList(1));
        listView.setAdapter(mAdapter);
        ((RadioButton) rgTab.getChildAt(0)).setChecked(true);
    }

    private RadioGroup.OnCheckedChangeListener rgTabOnCheckedChangedListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            View view = findViewById(checkedId);
            int tag = ConvertUtils.getInteger(view.getTag());
            setOnScrollX(tag);
            List<CourseEntity> list = getWeekCourseList(tag);
            mAdapter.addAllItems(list, true);
        }
    };

    private TopBarControl.OnOperateOnClickListener topBarOnOperateOnClickListener = new TopBarControl.OnOperateOnClickListener() {
        @Override
        public void onBackClick(View view) {

        }

        @Override
        public void onOperateClick(View v) {
            mProgressControl.showWindow();
            Map<String, Object> course = new HashMap<>();
            course.put("week", findViewById(rgTab.getCheckedRadioButtonId()).getTag());
            course.put("classId", ConvertUtils.getString(FmcApplication.getLoginUser().classId, ""));
            course.put("courses", CourseEntity.toMapByCourseList(mAdapter.mItems));
            JSONObject json = new JSONObject(course);
            Map<String, Object> params = new HashMap<>();
            params.put("course", json.toString());

            MyIon.httpPost(SyllabusActivity.this, "school/submitClassCourse", params, mProgressControl, new MyIon.AfterCallBack() {
                @Override
                public void afterCallBack(Map<String, Object> data) {
                    updateLocalData();
                    ToastToolUtils.showLong("上传成功");
                }
            });
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

    private List<CourseEntity> getCourseList(int week) {
        if (null == mList || mList.size() == 0) {
            return new ArrayList<>();
        }

        for (WeekCourseEntity item : mList) {
            if (item.week == week) {
                return item.courseList;
            }
        }
        return mList.get(0).courseList;
    }

    private List<CourseEntity> getWeekCourseList(int week) {
        List<CourseEntity> list = new ArrayList<>();
        List<CourseEntity> oldCourseList = getCourseList(week);
        int size = null == oldCourseList ? 0 : oldCourseList.size();
        for (int i = 0; i < 12; i++) {
            if (i < size) {
                list.add(oldCourseList.get(i));
                continue;
            }
            CourseEntity syllabusEntity = new CourseEntity();
            syllabusEntity.courseId = 0;
            syllabusEntity.order = i;
            syllabusEntity.orderName = "第" + ConvertUtils.getChineseNum(i + 1) + "节";
            list.add(syllabusEntity);
        }
        return list;
    }

    private void updateLocalData() {
        int week = ConvertUtils.getInteger(findViewById(rgTab.getCheckedRadioButtonId()).getTag(), 0);
        for (WeekCourseEntity item : mList) {
            if (item.week == week) {
                item.courseList = mAdapter.mItems;
                return;
            }
        }
    }
}
