package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.entity.CommonEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/3.
 */
public class SelectListControlAdapter extends FmcBaseAdapter<CommonEntity> {

    public SelectListControlAdapter(Context context, List<CommonEntity> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.control_select_item, null);
        }
        TextView txtId = (TextView) convertView.findViewById(R.id.select_item_txt_id);
        TextView txtName = (TextView) convertView.findViewById(R.id.select_item_txt_name);
        CommonEntity item = mItems.get(position);
        txtId.setText(item.getId());
        txtName.setText(item.getFullName());
        return convertView;
    }
}
