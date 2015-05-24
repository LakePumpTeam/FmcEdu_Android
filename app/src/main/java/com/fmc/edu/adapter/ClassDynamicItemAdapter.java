package com.fmc.edu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.fmc.edu.ClassDynamicActivity;
import com.fmc.edu.DynamicDetailActivity;
import com.fmc.edu.FmcApplication;
import com.fmc.edu.R;
import com.fmc.edu.customcontrol.ImageShowControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.entity.CommentItemEntity;
import com.fmc.edu.entity.DynamicItemEntity;
import com.fmc.edu.entity.ImageItemEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/24.
 */
public class ClassDynamicItemAdapter extends FmcBaseAdapter<DynamicItemEntity> {
    public ClassDynamicItemAdapter(Context context, List<DynamicItemEntity> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mItems) {
            return convertView;
        }
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_class_danamic_list, null);
        }
        ClassDynamicItemHolder holder = new ClassDynamicItemHolder();
        TextView txtAllContent = (TextView) convertView.findViewById(R.id.item_class_dynamic_list_txt_all_content);
        TextView txtContent = (TextView) convertView.findViewById(R.id.item_class_dynamic_list_txt_content);
        TextView txtDate = (TextView) convertView.findViewById(R.id.item_class_dynamic_list_txt_date);
        TextView txtReadAll = (TextView) convertView.findViewById(R.id.item_class_dynamic_list_txt_read_all);
        TextView txtComment = (TextView) convertView.findViewById(R.id.item_class_dynamic_list_txt_comment);
        GridView gridView = (GridView) convertView.findViewById(R.id.item_class_dynamic_list_grid_picture);

        DynamicItemEntity item = mItems.get(position);
        holder.txtAllContent = txtAllContent;
        holder.txtContent = txtContent;
        holder.txtReadAll = txtReadAll;
        txtReadAll.setTag(holder);

        txtContent.setText(item.content);
        txtAllContent.setText(item.content);
        txtComment.setText(ConvertUtils.getString(item.commentCount, "0"));
        txtDate.setText(item.createDate);
        txtAllContent.setText(item.subject);

        DynamicItemGridAdapter dynamicItemGridAdapter = new DynamicItemGridAdapter(mContext, item.imageUrls, ImageLoaderUtil.initCacheImageLoader(mContext));
        gridView.setAdapter(dynamicItemGridAdapter);
        gridView.setOnItemClickListener(gridOnItemClickListener);
        txtReadAll.setOnClickListener(txtReadAllOnclick);
        txtComment.setOnClickListener(txtCommentOnClickListener);
        return convertView;
    }

    private View.OnClickListener txtCommentOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mContext.getClass() == ClassDynamicActivity.class) {
                ((ClassDynamicActivity) mContext).setCommentVisible(ConvertUtils.getInteger(v.getTag(), 0));
            }
        }
    };

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
            ClassDynamicItemHolder holder = (ClassDynamicItemHolder) v.getTag();
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

    private class ClassDynamicItemHolder {
        public TextView txtAllContent;
        public TextView txtContent;
        public TextView txtReadAll;

    }
}
