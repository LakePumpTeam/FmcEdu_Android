package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.entity.PickUpEntity;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/7/14.
 */
public class PickUpAdapter extends FmcBaseAdapter<PickUpEntity> {
    public PickUpAdapter(Context context, List<PickUpEntity> items) {
        super(context, items);
    }


    public void addAllItems(List<PickUpEntity> items, boolean isClear) {
        if (null == mItems) {
            mItems = new ArrayList<>();
        }
        if (isClear) {
            this.mItems.clear();
        }

        if (!isClear && mItems.size() > 0) {
            if (StringUtils.isEmptyOrNull(mItems.get(0).date)) {
                mItems.clear();
            }
        }

        this.mItems.addAll(items);
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pick_up, null);
        }

        TextView txtDate = (TextView) convertView.findViewById(R.id.item_pick_up_txt_date);
        TextView txtWeek = (TextView) convertView.findViewById(R.id.item_pick_up_txt_week);
        TextView txtTime = (TextView) convertView.findViewById(R.id.item_pick_up_txt_time);
        TextView txtParent = (TextView) convertView.findViewById(R.id.item_pick_up_txt_parent);

        PickUpEntity item = mItems.get(position);
        txtDate.setText(item.date);
        txtWeek.setText(item.week);
        txtTime.setText(item.time);
        txtParent.setText(item.parentName);
        return convertView;
    }
}
