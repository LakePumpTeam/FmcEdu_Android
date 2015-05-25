package com.fmc.edu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.customcontrol.ImageShowControl;
import com.fmc.edu.entity.DynamicItemEntity;
import com.fmc.edu.entity.ImageItemEntity;
import com.fmc.edu.enums.DynamicTypeEnum;
import com.fmc.edu.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Candy on 2015/5/10.
 */
public class SchoolDynamicItemAdapter extends FmcBaseAdapter<DynamicItemEntity> {
    public SchoolDynamicItemAdapter(Context context, List<DynamicItemEntity> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mItems) {
            return convertView;
        }
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_school_dynamic_list, null);
        }
        SchoolDynamicItemHolder holder = new SchoolDynamicItemHolder();
        TextView txtTitle = (TextView) convertView.findViewById(R.id.item_school_dynamic_list_txt_title);
        TextView txtAllContent = (TextView) convertView.findViewById(R.id.item_school_dynamic_list_txt_all_content);
        TextView txtContent = (TextView) convertView.findViewById(R.id.item_school_dynamic_list_txt_content);
        TextView txtDate = (TextView) convertView.findViewById(R.id.item_school_dynamic_list_txt_date);
        TextView txtReadAll = (TextView) convertView.findViewById(R.id.item_school_dynamic_list_txt_read_all);
        GridView gridView = (GridView) convertView.findViewById(R.id.item_school_dynamic_list_grid_picture);

        DynamicItemEntity item = mItems.get(position);
        holder.txtAllContent = txtAllContent;
        holder.txtContent = txtContent;
        holder.txtReadAll = txtReadAll;
        txtReadAll.setTag(holder);

        txtContent.setText(item.content);
        txtDate.setText(item.createDate);
        txtReadAll.setTag(item.newsId);
        txtTitle.setText(item.subject);

        DynamicItemGridAdapter dynamicItemGridAdapter = new DynamicItemGridAdapter(mContext, item.imageUrls, ImageLoaderUtil.initCacheImageLoader(mContext));
        gridView.setAdapter(dynamicItemGridAdapter);
        gridView.setOnItemClickListener(gridOnItemClickListener);
        txtReadAll.setOnClickListener(txtReadAllOnclick);
        if (item.type == DynamicTypeEnum.SchoolNotice) {
            txtReadAll.setVisibility(View.VISIBLE);
            convertView.setBackgroundColor(Color.WHITE);
        } else {
            txtReadAll.setVisibility(View.GONE);
            convertView.setBackgroundResource(R.drawable.selector_list_item_bg);
        }

        return convertView;
    }

    private AdapterView.OnItemClickListener gridOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            List<ImageItemEntity> imageList = ((DynamicItemGridAdapter) parent.getAdapter()).getItems();
            ImageShowControl imageShowControl = new ImageShowControl(mContext);
            imageShowControl.showWindow(view, getOrigUrl(imageList));
        }
    };

    private List<String> getOrigUrl(List<ImageItemEntity> list) {
        List<String> origUrls = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            origUrls.add(list.get(i).origUrl);
        }
        return origUrls;
    }

    private View.OnClickListener txtReadAllOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SchoolDynamicItemHolder holder = (SchoolDynamicItemHolder) v.getTag();
            if (holder.txtAllContent.getVisibility() == View.VISIBLE) {
                holder.txtAllContent.setVisibility(View.GONE);
                holder.txtContent.setVisibility(View.VISIBLE);
                holder.txtReadAll.setText("查看全文>>");
            } else {
                holder.txtAllContent.setVisibility(View.VISIBLE);
                holder.txtContent.setVisibility(View.GONE);
                holder.txtReadAll.setText("收起>>");
            }
        }
    };

    private class SchoolDynamicItemHolder {
        public TextView txtAllContent;
        public TextView txtContent;
        public TextView txtReadAll;

    }

}
