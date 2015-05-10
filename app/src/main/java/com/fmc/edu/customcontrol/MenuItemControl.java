package com.fmc.edu.customcontrol;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fmc.edu.R;

/**
 * Created by Candy on 2015/5/9.
 */
public class MenuItemControl extends LinearLayout {

    private TextView txtMenu;
    private ImageView imgHasDynamic;
    private Context mContext;
    private String mMenuName;
    private int mDrawableId;


    public MenuItemControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initAttribute(attrs);
        initView();
    }

    private void initAttribute(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.MenuItemControl);
        mMenuName = typedArray.getString(R.styleable.MenuItemControl_menuName);
        mDrawableId = typedArray.getResourceId(R.styleable.MenuItemControl_drawableId, 0);
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main_grid, null);
        txtMenu = (TextView) view.findViewById(R.id.item_main_grid_txt_menu);
        imgHasDynamic = (ImageView) view.findViewById(R.id.item_main_grid_img_has_dynamic);

        txtMenu.setText(mMenuName);
        Drawable drawable = mContext.getResources().getDrawable(mDrawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        txtMenu.setCompoundDrawables(null, drawable, null, null);
        this.setGravity(Gravity.CENTER);
        this.addView(view);
    }

    public void setHasDynamic(boolean hasDynamic) {
        imgHasDynamic.setVisibility(hasDynamic ? VISIBLE : GONE);
    }
}
