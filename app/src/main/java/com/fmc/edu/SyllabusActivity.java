package com.fmc.edu;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fmc.edu.adapter.SyllabusAdapter;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.SyllabusEntity;
import com.fmc.edu.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.List;


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
            setOnScrollX(tag);
        }
    };

    private TopBarControl.OnOperateOnClickListener topBarOnOperateOnClickListener = new TopBarControl.OnOperateOnClickListener() {
        @Override
        public void onBackClick(View view) {

        }

        @Override
        public void onOperateClick(View v) {
//TODO 上传
        }
    };

    private void setOnScrollX(int tag) {
        if (tag == 0 || tag == 1 || tag == 2) {
            scrollView.setScrollX(0);
        }
        if (tag == 3) {
            scrollView.setScrollX(ConvertUtils.getInteger(rbTabTue.getX(), 0));
        }
        if (tag == 5 || tag == 6 || tag == 7) {
            scrollView.setScrollX(ConvertUtils.getInteger(rbTabWeb.getX(), 0));
        }
    }

    private List<SyllabusEntity> getListTest() {
        List<SyllabusEntity> list = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            SyllabusEntity syllabusEntity = new SyllabusEntity();
            syllabusEntity.syllabusId = i;
            syllabusEntity.sort = i;
            syllabusEntity.courseNum = "第" + i + "节";
            syllabusEntity.courseName = "数学" + i;
            list.add(syllabusEntity);
        }
        return list;

    }
}
