package com.fmc.edu.adapter;

import android.content.Context;
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
        TextView txtTitle = (TextView) convertView.findViewById(R.id.item_school_dynamic_list_txt_title);
        TextView txtContent = (TextView) convertView.findViewById(R.id.item_school_dynamic_list_txt_content);
        TextView txtDate = (TextView) convertView.findViewById(R.id.item_school_dynamic_list_txt_date);
        TextView txtReadAll = (TextView) convertView.findViewById(R.id.item_school_dynamic_list_txt_read_all);
        GridView gridView = (GridView) convertView.findViewById(R.id.item_school_dynamic_list_grid_picture);
        DynamicItemEntity item = mItems.get(position);

        txtContent.setText(item.content);
        txtDate.setText(item.createDate);
        txtReadAll.setTag(item.newsId);
        txtTitle.setText(item.subject);
        DynamicItemGridAdapter dynamicItemGridAdapter = new DynamicItemGridAdapter(mContext, item.imageUrls, ImageLoaderUtil.initCacheImageLoader(mContext));
        gridView.setAdapter(dynamicItemGridAdapter);
        gridView.setOnItemClickListener(gridOnItemClickListener);
        txtReadAll.setOnClickListener(txtReadAllOnclick);
        return convertView;
    }

    private AdapterView.OnItemClickListener gridOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            List<ImageItemEntity> imageList = ((DynamicItemGridAdapter) parent.getAdapter()).getItems();
            ImageShowControl imageShowControl = new ImageShowControl(mContext);
            imageShowControl.showWindow(view, getOrigUrl(imageList));
            imageShowControl.showWindow(view, new ArrayList<String>());
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
           //TODO
        }
    };


}
