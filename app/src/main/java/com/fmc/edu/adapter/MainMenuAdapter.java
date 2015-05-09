package com.fmc.edu.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.entity.MenuItemEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/8.
 */
public class MainMenuAdapter extends FmcBaseAdapter<MenuItemEntity> {

    public MainMenuAdapter(Context context, List<MenuItemEntity> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_main_grid, null);
        }
        TextView gridMenu = (TextView) convertView.findViewById(R.id.item_main_grid_txt_menu);
        ImageView hasDynamic = (ImageView) convertView.findViewById(R.id.item_main_grid_img_has_dynamic);

        MenuItemEntity item = mItems.get(position);
        gridMenu.setText(item.menuName);

        Drawable topDrawable = mContext.getResources().getDrawable(item.menuIconId);
        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
        gridMenu.setCompoundDrawables(null, topDrawable, null, null);
        hasDynamic.setVisibility(item.hasNewDynamic ? View.VISIBLE : View.GONE);
        return convertView;
    }
}
