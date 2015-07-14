package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.utils.ConvertUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/7/14.
 */
public class PickUpAdapter extends FmcBaseAdapter<Map<String, Object>> {
    public PickUpAdapter(Context context, List<Map<String, Object>> items) {
        super(context, items);
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

        Map<String, Object> item = mItems.get(position);
        txtDate.setText(ConvertUtils.getString(item.get("date"), ""));
        txtWeek.setText(ConvertUtils.getString(item.get("week"), ""));
        txtTime.setText(ConvertUtils.getString(item.get("time"), ""));
        txtParent.setText(ConvertUtils.getString(item.get("parent"), ""));
        return convertView;
    }
}
