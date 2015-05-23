package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.entity.KidSchoolEntity;

import java.util.List;

/**
 * Created by Candy on 2015/5/10.
 */
public class KidsSchoolAdapter extends FmcBaseAdapter<KidSchoolEntity> {
    public KidsSchoolAdapter(Context context, List<KidSchoolEntity> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_kids_school_list, null);
        }
        ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.item_kids_school_img_photo);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.item_kids_school_txt_title);
        TextView txtContent = (TextView) convertView.findViewById(R.id.item_kids_school_txt_content);
        TextView txtDate = (TextView) convertView.findViewById(R.id.item_kids_school_txt_date);
        TextView txtReadAll = (TextView) convertView.findViewById(R.id.item_kids_school_txt_read_all);

        KidSchoolEntity item = mItems.get(position);
        txtTitle.setText(item.title);
        txtContent.setText(item.content);
        txtDate.setText(item.date);
        txtReadAll.setOnClickListener(txtReadAllOnclickListener);

        return convertView;
    }

    private View.OnClickListener txtReadAllOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//TODO 查看所有文件
        }
    };
}
