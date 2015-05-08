package com.fmc.edu;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;

import com.fmc.edu.adapter.MainMenuAdapter;
import com.fmc.edu.entity.MenuItemEntity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private GridView gridMenu;
    private TextView txtTeacher;
    private TextView txtClassGrade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initData();
    }

    private void initViews() {
        gridMenu = (GridView) findViewById(R.id.main_grid_menu);
        txtTeacher = (TextView) findViewById(R.id.main_txt_teacher);
        txtClassGrade = (TextView) findViewById(R.id.main_txt_class_grade);
    }

    private void initData() {
        MainMenuAdapter adapter = new MainMenuAdapter(this, getMenuItemList());
        gridMenu.setAdapter(adapter);

    }

    private List<MenuItemEntity> getMenuItemList() {
        List<MenuItemEntity> list = new ArrayList<MenuItemEntity>();
        list.add(new MenuItemEntity("校园动态", R.mipmap.ic_school_dynamic, false));
        list.add(new MenuItemEntity("班级动态", R.mipmap.ic_grade_dynamic, true));
        list.add(new MenuItemEntity("课程表", R.mipmap.ic_syllabus_dynamic, false));
        list.add(new MenuItemEntity("亲子教育", R.mipmap.ic_parenting, false));
        list.add(new MenuItemEntity("育儿课堂", R.mipmap.ic_kids_school, false));
        list.add(new MenuItemEntity("校园吧", R.mipmap.ic_campus, false));
        list.add(new MenuItemEntity("智能定位", R.mipmap.ic_location, false));
        list.add(new MenuItemEntity("注册审批", R.mipmap.ic_audit, false));
        return list;
    }
}
