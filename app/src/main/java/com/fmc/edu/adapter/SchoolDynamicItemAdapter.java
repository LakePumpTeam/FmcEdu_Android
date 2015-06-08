package com.fmc.edu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.fmc.edu.DynamicDetailActivity;
import com.fmc.edu.FmcApplication;
import com.fmc.edu.R;
import com.fmc.edu.customcontrol.ExpandableTextViewControl;
import com.fmc.edu.customcontrol.GridViewControl;
import com.fmc.edu.customcontrol.ImageShowControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.entity.CommentItemEntity;
import com.fmc.edu.entity.DynamicItemEntity;
import com.fmc.edu.entity.ImageItemEntity;
import com.fmc.edu.enums.DynamicTypeEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ImageLoaderUtil;
import com.fmc.edu.utils.ToastToolUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/10.
 */
public class SchoolDynamicItemAdapter extends FmcBaseAdapter<DynamicItemEntity> {
    private final SparseBooleanArray mCollapsedStatus;

    public SchoolDynamicItemAdapter(Context context, List<DynamicItemEntity> items) {
        super(context, items);
        mCollapsedStatus = new SparseBooleanArray();
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
        TextView txtDate = (TextView) convertView.findViewById(R.id.item_school_dynamic_list_txt_date);
        GridViewControl gridView = (GridViewControl) convertView.findViewById(R.id.item_school_dynamic_list_grid_picture);
        ExpandableTextViewControl expandableTextViewControl = (ExpandableTextViewControl) convertView.findViewById(R.id.item_school_dynamic_list_expand_text_view);
        DynamicItemEntity item = mItems.get(position);

        expandableTextViewControl.setText(item.content, mCollapsedStatus, position, item.type == DynamicTypeEnum.SchoolNotice);
        txtDate.setText(item.createDate);
        txtTitle.setText(item.subject);

        DynamicItemGridAdapter dynamicItemGridAdapter = new DynamicItemGridAdapter(mContext, item.imageUrls, ImageLoaderUtil.initCacheImageLoader(mContext));
        gridView.setAdapter(dynamicItemGridAdapter);
        gridView.setOnTouchInvalidPositionListener(onTouchInvalidPositionListener);
        gridView.setOnItemClickListener(gridOnItemClickListener);
        convertView.setBackgroundResource(item.type == DynamicTypeEnum.SchoolNotice ? R.color.list_item_nor_color : R.drawable.selector_list_item_bg);
        convertView.setTag(item);
        convertView.setOnClickListener(viewOnclickListener);
        return convertView;
    }

    private AdapterView.OnItemClickListener gridOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            List<ImageItemEntity> imageList = ((DynamicItemGridAdapter) parent.getAdapter()).getItems();
            List<String> bigPictureUrl = ImageItemEntity.getOrigUrlList(imageList);
            if (null == bigPictureUrl || 0 == bigPictureUrl.size()) {
                ToastToolUtils.showLong("无有效图片");
                return;
            }
            ImageShowControl imageShowControl = new ImageShowControl(mContext);
            imageShowControl.showWindow(view, bigPictureUrl,position);
        }
    };

    private GridViewControl.OnTouchInvalidPositionListener onTouchInvalidPositionListener = new GridViewControl.OnTouchInvalidPositionListener() {
        @Override
        public boolean onTouchInvalidPosition(int motionEvent) {
            return false;
        }
    };

    private View.OnClickListener viewOnclickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DynamicItemEntity item = (DynamicItemEntity) v.getTag();
            if (item.type == DynamicTypeEnum.SchoolNotice) {
                return;
            }
            gotoDynamicDetailPage(v, item);
        }
    };

    private void gotoDynamicDetailPage(View view, final DynamicItemEntity item) {
        ProgressControl progressControl = new ProgressControl(mContext, view);
        progressControl.showWindow();
        final int type = DynamicTypeEnum.getValue(item.type);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("newsId", item.newsId);
        params.put("userId", FmcApplication.getLoginUser().userId);
        MyIon.httpPost(mContext, "news/requestNewsDetail", params, progressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                Bundle bundle = new Bundle();
                bundle.putInt("newsId", ConvertUtils.getInteger(data.get("newsId")));
                bundle.putInt("like", ConvertUtils.getInteger(data.get("like")));
                bundle.putInt("type", type);
                bundle.putBoolean("liked", ConvertUtils.getBoolean(data.get("liked"), false));
                bundle.putString("subject", ConvertUtils.getString(data.get("subject")));
                bundle.putString("content", ConvertUtils.getString(data.get("content")));
                bundle.putStringArrayList("imageUrl", ConvertUtils.getStringList(data.get("imageUrls")));
                bundle.putString("createDate", ConvertUtils.getString(data.get("createDate")));
                List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("commentList");
                bundle.putSerializable("commentList", (Serializable) CommentItemEntity.toCommentEntityList(list));
                Intent intent = new Intent(mContext, DynamicDetailActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }
}
