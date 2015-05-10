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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_school_dynamic_list, null);
        }
        TextView txtTitle = (TextView) convertView.findViewById(R.id.item_school_dynamic_txt_title);
        TextView txtContent = (TextView) convertView.findViewById(R.id.item_school_dynamic_txt_content);
        TextView txtDate = (TextView) convertView.findViewById(R.id.item_school_dynamic_txt_date);
        TextView txtReadAll = (TextView) convertView.findViewById(R.id.item_school_dynamic_txt_read_all);
        ImageView img1 = (ImageView) convertView.findViewById(R.id.item_school_dynamic_img1);
        ImageView img2 = (ImageView) convertView.findViewById(R.id.item_school_dynamic_img2);
        ImageView img3 = (ImageView) convertView.findViewById(R.id.item_school_dynamic_img3);
        ImageView img4 = (ImageView) convertView.findViewById(R.id.item_school_dynamic_img4);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        int imgHeight = displayMetrics.widthPixels / 4;
        int imgWidth = (int) (imgHeight - 8 * displayMetrics.density);
        ViewGroup.LayoutParams params1 = img1.getLayoutParams();
        params1.height = imgHeight;
        params1.width = imgWidth;
        img1.setLayoutParams(params1);
        ViewGroup.LayoutParams params2 = img2.getLayoutParams();
        params2.height = imgHeight;
        params2.width = imgWidth;
        img2.setLayoutParams(params2);
        ViewGroup.LayoutParams params3 = img3.getLayoutParams();
        params3.height = imgHeight;
        params3.width = imgWidth;
        img3.setLayoutParams(params3);
        ViewGroup.LayoutParams params4 = img4.getLayoutParams();
        params4.height = imgHeight;
        params4.width = imgWidth;
        img4.setLayoutParams(params4);

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
