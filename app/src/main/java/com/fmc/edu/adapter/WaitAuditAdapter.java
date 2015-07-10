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
import com.fmc.edu.entity.WaitAuditEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.ServicePreferenceUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/15.
 */
public class WaitAuditAdapter extends FmcBaseAdapter<WaitAuditEntity> {

    public WaitAuditAdapter(Context context, List<WaitAuditEntity> list) {
        super(context, list);
    }

    public void  updateStatus(int position,int status){
        mItems.get(position).auditStatus = status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_wait_audit, null);
        }
        WaitAuditHolder holder = new WaitAuditHolder();
        TextView txtCellphone = (TextView) convertView.findViewById(R.id.item_wait_audit_txt_cellphone);
        TextView txtParentName = (TextView) convertView.findViewById(R.id.item_wait_audit_txt_parent_name);
        Button btnAgree = (Button) convertView.findViewById(R.id.item_wait_audit_btn_agree);
        Button btnRefuse = (Button) convertView.findViewById(R.id.item_wait_audit_btn_refuse);
        holder.btnAgree = btnAgree;
        holder.btnRefuse = btnRefuse;
        WaitAuditEntity item = mItems.get(position);
        holder.item = mItems.get(position);
        holder.position = position;
        txtCellphone.setText(item.cellphone);
        txtParentName.setText(item.parentName);
        txtParentName.setTag(item.parentId);
        int auditStatus = item.auditStatus;
        if (auditStatus == 1) {
            btnAgree.setEnabled(false);
            btnAgree.setText("已通过");
            btnRefuse.setEnabled(true);
            btnRefuse.setText("  拒绝  ");
        } else if (auditStatus == 2) {
           btnRefuse.setEnabled(false);
           btnRefuse.setText("已拒绝");
           btnAgree.setEnabled(true);
           btnAgree.setText("  通过  ");
        } else {
            btnRefuse.setEnabled(true);
            btnRefuse.setText("  拒绝  ");
            btnAgree.setEnabled(true);
            btnAgree.setText("  通过  ");
        }
        btnAgree.setTag(holder);
        btnRefuse.setTag(holder);
        btnAgree.setOnClickListener(btnAgreeOnClickListener);
        btnRefuse.setOnClickListener(btnRefuseOnClickListener);
        txtParentName.setOnClickListener(txtParentNameOnClickListener);
        return convertView;
    }

    private View.OnClickListener txtParentNameOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            ProgressControl mProgressControl = new ProgressControl(mContext, v);
            mProgressControl.showWindow();
            Map<String, Object> params = new HashMap<>();
            params.put("parentId", v.getTag());
            MyIon.httpPost(mContext, "profile/requestGetRelateInfo", params, mProgressControl, new MyIon.AfterCallBack() {
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
            WaitAuditHolder holder = (WaitAuditHolder) v.getTag();
            holder.auditStatus = 1;
            auditParentRegister(v, holder);
        }
    };
    private View.OnClickListener btnRefuseOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            WaitAuditHolder holder = (WaitAuditHolder) v.getTag();
            holder.auditStatus = 2;
            auditParentRegister(v, holder);
        }
    };

    private void auditParentRegister(final View view, final WaitAuditHolder holder) {
        LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(mContext);
        Map<String, Object> params = new HashMap<>();
        params.put("teacherId", loginUserEntity.userId);
        params.put("parentIds", holder.item.parentId);
        params.put("setPass", holder.auditStatus);
        MyIon.httpPost(mContext, "profile/requestParentAudit", params, null, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                if (holder.auditStatus == 1) {
                    holder.btnAgree.setEnabled(false);
                    holder.btnAgree.setText("已通过");
                    holder.btnRefuse.setEnabled(true);
                    holder.btnRefuse.setText("  拒绝  ");
                } else if (holder.auditStatus == 2) {
                    holder.btnAgree.setEnabled(true);
                    holder.btnRefuse.setText("已拒绝");
                    holder.btnRefuse.setEnabled(false);
                    holder.btnAgree.setText("  通过  ");
                }
                updateStatus(holder.position,holder.auditStatus);
            }
        });
    }

    private class WaitAuditHolder {
        public WaitAuditEntity item;
        public Button btnAgree;
        public Button btnRefuse;
        public int auditStatus;
        public  int  position;
    }
}
