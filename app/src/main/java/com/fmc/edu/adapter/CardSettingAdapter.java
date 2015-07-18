package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.utils.ConvertUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/7/16.
 */
public class CardSettingAdapter extends FmcBaseAdapter<Map<String, Object>> {
    public CardSettingAdapter(Context context, List<Map<String, Object>> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_card_setting, null);
        }

        TextView txtCardNo = (TextView) convertView.findViewById(R.id.item_card_setting_txt_card_no);
        TextView txtParent = (TextView) convertView.findViewById(R.id.item_card_setting_txt_parent);
        TextView txtLose = (TextView) convertView.findViewById(R.id.item_card_setting_tv_lose);
        TextView txtComment = (TextView) convertView.findViewById(R.id.item_card_setting_txt_comment);

        Map<String, Object> item = mItems.get(position);
        txtCardNo.setText(ConvertUtils.getString(item.get("cardNo"), ""));
        txtParent.setText(ConvertUtils.getString(item.get("parent"), ""));
        boolean isLose = ConvertUtils.getBoolean(item.get("isLose"), false);
        txtLose.setEnabled(isLose);
        txtLose.setText(isLose ? "已挂失" : "点此挂失");
        txtComment.setText(ConvertUtils.getString(item.get("comment"), ""));
        return convertView;
    }
}

