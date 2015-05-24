package com.fmc.edu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmc.edu.DynamicDetailActivity;
import com.fmc.edu.FmcApplication;
import com.fmc.edu.R;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.entity.CommentItemEntity;
import com.fmc.edu.entity.DynamicItemEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/10.
 */
public class KidsSchoolAdapter extends FmcBaseAdapter<DynamicItemEntity> {
    public KidsSchoolAdapter(Context context, List<DynamicItemEntity> items) {
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
        TextView txtLikeCount = (TextView) convertView.findViewById(R.id.item_kids_school_txt_like_count);

        DynamicItemEntity item = mItems.get(position);
        txtTitle.setText(item.subject);
        txtContent.setText(item.content);
        txtDate.setText(item.createDate);
        txtLikeCount.setText(ConvertUtils.getString(item.likeCount, "0"));
        String imgUrl = null != item.imageUrls && item.imageUrls.size() > 0 ? item.imageUrls.get(0).origUrl : "";
        ImageLoaderUtil.initCacheImageLoader(mContext).displayImage(imgUrl, imgPhoto);
        return convertView;
    }
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
