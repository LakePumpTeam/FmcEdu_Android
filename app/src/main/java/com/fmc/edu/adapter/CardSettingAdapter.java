package com.fmc.edu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fmc.edu.FmcApplication;
import com.fmc.edu.R;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.ConvertUtils;

import java.io.Serializable;
import java.util.HashMap;
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
        txtParent.setText(ConvertUtils.getString(item.get("parentName"), ""));
        boolean isUnLose = ConvertUtils.getBoolean(item.get("status"), true);
        txtLose.setEnabled(isUnLose);
        txtLose.setText(!isUnLose ? "已挂失" : "点此挂失");
        txtComment.setText(ConvertUtils.getString(item.get("comment"), ""));
        txtLose.setTag(item);
        txtLose.setOnClickListener(txtLoseOnclickListener);
        return convertView;
    }
    private View.OnClickListener txtLoseOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final TextView txtView = (TextView) v;
            Map<String, Object> params = new HashMap<>();
            Map<String,Object> item = (Map<String, Object>) v.getTag();
            params.put("magneticCardId", item.get("magneticCardId"));
            MyIon.httpPost(mContext, "magneticCard/reportMagneticCardLost", params,null, new MyIon.AfterCallBack() {
                @Override
                public void afterCallBack(Map<String, Object> data) {
                    txtView.setEnabled(false);
                    txtView.setText("已挂失");

                }
            });
        }
    };

}

