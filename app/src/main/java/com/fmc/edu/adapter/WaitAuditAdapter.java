package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.customcontrol.ParentDetailInfoControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;

import java.util.HashMap;
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
        btnAgree.setTag(item);
        btnRefuse.setTag(item);
        btnAgree.setOnClickListener(btnAgreeOnClickListener);
        btnRefuse.setOnClickListener(btnRefuseOnClickListener);
        txtParentName.setOnClickListener(txtParentNameOnClickListener);
        txtParentName.setTag(item.get("userId"));
        return convertView;
    }

    private View.OnClickListener txtParentNameOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            ProgressControl mProgressControl = new ProgressControl(mContext);
            mProgressControl.showWindow(v);
            Map<String, Object> params = new HashMap<>();
            params.put("parentId", v.getTag());
            MyIon.httpPost(mContext, AppConfigUtils.getServiceHost() + "profile/requestGetRelateInfo", params, mProgressControl, new MyIon.AfterCallBack() {
                @Override
                public void afterCallBack(Map<String, Object> data) {
                    ParentDetailInfoControl parentDetailInfoControl = new ParentDetailInfoControl(mContext, data);
                    parentDetailInfoControl.showWindow(v);
                }
            });
        }
    };

    private View.OnClickListener btnAgreeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            auditParentRegister(v, (Map<String, Object>) v.getTag(), 1);
        }
    };
    private View.OnClickListener btnRefuseOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            auditParentRegister(v, (Map<String, Object>) v.getTag(), 2);
        }
    };

    private void auditParentRegister(final View view, Map<String, Object> item, int auditState) {
        ProgressControl mProgressControl = new ProgressControl(mContext);
        mProgressControl.showWindow(view);
        LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(mContext);
        Map<String, Object> params = new HashMap<>();
        String[] parentIds = new String[1];
        parentIds[0] = ConvertUtils.getString(item.get("parentId"));
        params.put("teacherId", loginUserEntity.userId);
        params.put("parentIds", parentIds);
        params.put("setPass", auditState);
        MyIon.httpPost(mContext, AppConfigUtils.getServiceHost() + "profile/requestParentAuditAll", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                view.setEnabled(false);
            }
        });
    }
}
