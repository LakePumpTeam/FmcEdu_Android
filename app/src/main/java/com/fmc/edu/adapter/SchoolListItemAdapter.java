package com.fmc.edu.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.entity.SchoolDynamicEntity;

import java.util.List;

/**
 * Created by Candy on 2015/5/10.
 */
public class SchoolListItemAdapter extends FmcBaseAdapter<SchoolDynamicEntity> {
    public SchoolListItemAdapter(Context context, List<SchoolDynamicEntity> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_list, null);
        }
        TextView txtTitle = (TextView) convertView.findViewById(R.id.item_dynamic_list_txt_title);
        TextView txtContent = (TextView) convertView.findViewById(R.id.item_dynamic_list_txt_content);
        TextView txtDate = (TextView) convertView.findViewById(R.id.item_dynamic_list_txt_date);
        TextView txtReadAll = (TextView) convertView.findViewById(R.id.item_dynamic_list_txt_read_all);
        //TODO图片的绑定
        SchoolDynamicEntity item = mItems.get(position);
        txtTitle.setText(item.title);
        txtContent.setText(item.content);
        txtDate.setText(item.date);
        txtReadAll.setOnClickListener(txtReadAllOnclik);
        return convertView;
    }

    private View.OnClickListener txtReadAllOnclik = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //查看全文的跳转
        }
    };
}
