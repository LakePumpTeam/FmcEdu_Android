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
import com.fmc.edu.entity.ImageItemEntity;
import com.fmc.edu.utils.ImageLoaderUtil;
import com.fmc.edu.entity.DynamicItemEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/10.
 */
public class DynamicItemAdapter extends FmcBaseAdapter<DynamicItemEntity> {
    public DynamicItemAdapter(Context context, List<DynamicItemEntity> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mItems) {
            return convertView;
        }
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_list, null);
        }
        TextView txtTitle = (TextView) convertView.findViewById(R.id.item_dynamic_list_txt_title);
        TextView txtContent = (TextView) convertView.findViewById(R.id.item_dynamic_list_txt_content);
        TextView txtDate = (TextView) convertView.findViewById(R.id.item_dynamic_list_txt_date);
        TextView txtReadAll = (TextView) convertView.findViewById(R.id.item_dynamic_list_txt_read_all);
        TextView txtComment = (TextView) convertView.findViewById(R.id.item_dynamic_list_txt_comment);
        GridView gridView = (GridView) convertView.findViewById(R.id.item_dynamic_list_grid_picture);
        DynamicItemEntity item = mItems.get(position);
        txtTitle.setText(item.subject);
        txtContent.setText(item.content);
        txtDate.setText(item.createDate);
        txtReadAll.setTag(item.newsId);
        DynamicItemGridAdapter dynamicItemGridAdapter = new DynamicItemGridAdapter(mContext, item.imageUrls, ImageLoaderUtil.initCacheImageLoader(mContext));
        gridView.setAdapter(dynamicItemGridAdapter);
        gridView.setOnItemClickListener(gridOnItemClickListener);
        txtReadAll.setOnClickListener(txtReadAllOnclick);
        txtComment.setTag(item.newsId);
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
            ImageItemEntity imageItemEntity = (ImageItemEntity) parent.getAdapter().getItem(position);
            ImageShowControl imageShowControl = new ImageShowControl(mContext);
            imageShowControl.showWindow(view, imageItemEntity.origUrl);
        }
    };

    private View.OnClickListener txtReadAllOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            gotoDynamicDetailPage(v, ConvertUtils.getInteger(v.getTag()));
        }
    };

    private void gotoDynamicDetailPage(View view, int newsId) {
        ProgressControl progressControl = new ProgressControl(mContext);
        progressControl.showWindow(view);
        String url = AppConfigUtils.getServiceHost() + "news/requestNewsDetail";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("newsId", newsId);
        params.put("userId", FmcApplication.getLoginUser().userId);
        MyIon.httpPost(mContext, url, params, progressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                Bundle bundle = new Bundle();
                bundle.putInt("newsId", ConvertUtils.getInteger(data.get("newsId")));
                bundle.putInt("like", ConvertUtils.getInteger(data.get("like")));
                bundle.putInt("type", ConvertUtils.getInteger(data.get("type")));
                bundle.putBoolean("liked", ConvertUtils.getBoolean(data.get("liked"), false));
                bundle.putString("subject", ConvertUtils.getString(data.get("subject")));
                bundle.putString("content", ConvertUtils.getString(data.get("content")));
                bundle.putStringArrayList("imageUrl", ConvertUtils.getStringList(data.get("imageUrl")));
                bundle.putString("createDate", ConvertUtils.getString(data.get("createDate")));
                List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("commentList");
                bundle.putParcelableArrayList("commentList", (ArrayList<? extends Parcelable>) getCommentList(list));

                Intent intent = new Intent(mContext, DynamicDetailActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    private List<CommentItemEntity> getCommentList(List<Map<String, Object>> commentList) {
        List<CommentItemEntity> list = new ArrayList<CommentItemEntity>();
        for (int i = 0; i < commentList.size(); i++) {
            Map<String, Object> commentItem = commentList.get(i);
            CommentItemEntity item = new CommentItemEntity();
            item.userId = ConvertUtils.getInteger(commentItem.get("userId"));
            item.userName = ConvertUtils.getString(commentItem.get("userName"));
            item.comment = ConvertUtils.getString(commentItem.get("comment"));
            list.add(item);
        }
        return list;
    }
}
