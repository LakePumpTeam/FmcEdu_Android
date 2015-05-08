package com.fmc.edu.customcontrol;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.fmc.edu.R;

/**
 * Created by Candy on 2015/5/9.
 */
public class MenuItemControl extends LinearLayout {
    private Context mContext;
    private String mMenuName;
    private int mDrawableId;

    public MenuItemControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initAttribute(attrs);
    }

    private void initAttribute(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.MenuItemControl);
        mMenuName = typedArray.getString(R.styleable.MenuItemControl_menuName);
        mDrawableId = typedArray.getResourceId(R.styleable.MenuItemControl_drawableId, 0);
    }
}
