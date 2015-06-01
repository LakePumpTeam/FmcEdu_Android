package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.entity.MultiCommonEntity;
import com.fmc.edu.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Candy on 2015/5/30.
 */
public class MultiSelectListAdapter extends FmcBaseAdapter<MultiCommonEntity> {

    public MultiSelectListAdapter(Context context, List<MultiCommonEntity> list) {
        super(context, list);
    }

    public List<MultiCommonEntity> getSelectedList() {
        List<MultiCommonEntity> selectList = new ArrayList<>();
        for (MultiCommonEntity item : mItems) {
            if (item.isCheck) {
                selectList.add(item);
            }
        }
        return selectList;
    }

    public void updateChecked(int position, boolean isCheck) {
        mItems.get(position).isCheck = isCheck;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_multi_select, null);
        }
        MultiSelectListHolder multiSelectListHolder = new MultiSelectListHolder();
        CheckBox cbCheck = (CheckBox) convertView.findViewById(R.id.multi_select_item_checked);
        TextView txtId = (TextView) convertView.findViewById(R.id.multi_select_item_txt_id);
        TextView txtName = (TextView) convertView.findViewById(R.id.multi_select_item_txt_name);

        MultiCommonEntity item = mItems.get(position);
        multiSelectListHolder.checkBox = cbCheck;
        multiSelectListHolder.position = position;

        txtId.setText(ConvertUtils.getString(item.id));
        txtName.setText(item.name);
        cbCheck.setChecked(item.isCheck);
        convertView.setTag(multiSelectListHolder);
        cbCheck.setTag(position);

        cbCheck.setOnClickListener(ckOnCheckChangedListener);
        //convertView.setOnClickListener(viewChangedListener);
        return convertView;
    }

    private View.OnClickListener ckOnCheckChangedListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = ConvertUtils.getInteger(v.getTag(), 0);
            updateChecked(position, ((CheckBox) v).isChecked());
        }
    };
//    private View.OnClickListener viewChangedListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            MultiSelectListHolder multiSelectListHolder = (MultiSelectListAdapter.MultiSelectListHolder) v.getTag();
//            boolean isCheck = multiSelectListHolder.checkBox.isChecked();
//            multiSelectListHolder.checkBox.setChecked(!isCheck);
//            updateChecked(multiSelectListHolder.position, !isCheck);
//        }
//    };

    private class MultiSelectListHolder {
        public int position;
        public CheckBox checkBox;
    }

}