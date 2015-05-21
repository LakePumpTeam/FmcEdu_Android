package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.entity.ImageItemEntity;
import com.fmc.edu.entity.ImageLoaderUtil;
import com.fmc.edu.entity.SchoolDynamicEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/10.
 */
public class DynamicItemAdapter extends FmcBaseAdapter<SchoolDynamicEntity> {
    public DynamicItemAdapter(Context context, List<SchoolDynamicEntity> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_list, null);
        }
        TextView txtContent = (TextView) convertView.findViewById(R.id.item_dynamic_list_txt_content);
        TextView txtDate = (TextView) convertView.findViewById(R.id.item_dynamic_list_txt_date);
        TextView txtReadAll = (TextView) convertView.findViewById(R.id.item_dynamic_list_txt_read_all);
        GridView gridView = (GridView) convertView.findViewById(R.id.item_dynamic_list_grid_picture);
        SchoolDynamicEntity item = mItems.get(position);
        txtContent.setText(item.content);
        txtDate.setText(item.createDate);
        DynamicItemGridAdapter dynamicItemGridAdapter = new DynamicItemGridAdapter(mContext, getImageList(item), ImageLoaderUtil.initCacheImageLoader(mContext));
        gridView.setAdapter(dynamicItemGridAdapter);
        txtReadAll.setOnClickListener(txtReadAllOnclik);
        return convertView;
    }

    private List<ImageItemEntity> getImageList(SchoolDynamicEntity schoolDynamicEntity) {
        List<ImageItemEntity> list = new ArrayList<ImageItemEntity>();
        List<Map<String, String>> urlList = schoolDynamicEntity.imageUrls;
        for (int i = 0; i < urlList.size(); i++) {
            ImageItemEntity imageItemEntity = new ImageItemEntity();
            imageItemEntity.thumbUrl = urlList.get(i).get("origUrl");
            list.add(imageItemEntity);
        }
        return list;
    }

    private View.OnClickListener txtReadAllOnclik = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //查看全文的跳转
        }
    };
}
