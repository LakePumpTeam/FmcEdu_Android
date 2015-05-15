package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.utils.ConvertUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/15.
 */
public class WaitAuditAdapter extends FmcBaseAdapter<Map<String, Object>> {

    public WaitAuditAdapter(Context context, List<Map<String, Object>> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_wait_audit, null);
        }
        TextView txtCellphone = (TextView) convertView.findViewById(R.id.item_wait_audit_txt_cellphone);
        TextView txtParentName = (TextView) convertView.findViewById(R.id.item_wait_audit_txt_parent_name);
        Button btnAgree = (Button) convertView.findViewById(R.id.item_wait_audit_btn_agree);
        Button btnRefuse = (Button) convertView.findViewById(R.id.item_wait_audit_btn_refuse);
        Map<String, Object> item = mItems.get(position);
        txtCellphone.setText(ConvertUtils.getString(item.get("cellphone")));
        txtParentName.setText(ConvertUtils.getString(item.get("parentName")));
        btnAgree.setOnClickListener(btnAgreeOnClickListener);
        btnRefuse.setOnClickListener(btnRefuseOnClickListener);
        return convertView;
    }

    private View.OnClickListener btnAgreeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO

        }
    };
    private View.OnClickListener btnRefuseOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO
        }
    };
}
