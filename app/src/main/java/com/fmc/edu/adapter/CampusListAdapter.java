package com.fmc.edu.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.fmc.edu.CampusDetailActivity;
import com.fmc.edu.DynamicDetailActivity;
import com.fmc.edu.FmcApplication;
import com.fmc.edu.R;
import com.fmc.edu.customcontrol.GridViewControl;
import com.fmc.edu.customcontrol.ImageShowControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.entity.CampusEntity;
import com.fmc.edu.entity.CommentItemEntity;
import com.fmc.edu.entity.DynamicItemEntity;
import com.fmc.edu.entity.ImageItemEntity;
import com.fmc.edu.enums.DynamicTypeEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ImageLoaderUtil;
import com.fmc.edu.utils.ToastToolUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/6/2.
 */
public class CampusListAdapter extends FmcBaseAdapter<CampusEntity> {
    public CampusListAdapter(Context context, List<CampusEntity> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mItems) {
            return convertView;
        }
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_campus_list, null);
        }
        TextView txtTitle = (TextView) convertView.findViewById(R.id.item_campus_list_txt_title);
        TextView txtPartInCount = (TextView) convertView.findViewById(R.id.item_campus_list_txt_part_in_count);
        TextView txtContent = (TextView) convertView.findViewById(R.id.item_campus_list_txt_content);
        TextView txtDate = (TextView) convertView.findViewById(R.id.item_campus_list_txt_date);
        TextView txtPartIn = (TextView) convertView.findViewById(R.id.item_campus_list_txt_part_in);
        GridViewControl gridView = (GridViewControl) convertView.findViewById(R.id.item_campus_list_grid_picture);

        CampusEntity item = mItems.get(position);

        txtContent.setText(item.content);
        txtPartInCount.setText(item.partinCount);
        txtDate.setText(item.date);
        txtTitle.setText(item.subject);
        DynamicItemGridAdapter dynamicItemGridAdapter = new DynamicItemGridAdapter(mContext, item.imageUrls, ImageLoaderUtil.initCacheImageLoader(mContext));
        gridView.setAdapter(dynamicItemGridAdapter);
      //  gridView.setOnTouchInvalidPositionListener(onTouchInvalidPositionListener);
        gridView.setOnItemClickListener(gridOnItemClickListener);
        txtPartIn.setTag(item);
        txtPartIn.setOnClickListener(txtPartInOnclick);
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
            imageShowControl.showWindow(view, bigPictureUrl);
        }
    };

//    private GridViewControl.OnTouchInvalidPositionListener onTouchInvalidPositionListener = new GridViewControl.OnTouchInvalidPositionListener() {
//        @Override
//        public boolean onTouchInvalidPosition(int motionEvent) {
//            return false;
//        }
//    };

    private View.OnClickListener txtPartInOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 查看详情
            gotoCampusListPage(v, (CampusEntity) v.getTag());
        }
    };

    private void gotoCampusListPage(View view, final CampusEntity item) {
        ProgressControl progressControl = new ProgressControl(mContext,view);
        progressControl.showWindow();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("campusId", item.campusId);
        MyIon.httpPost(mContext, "news/requestNewsDetail", params, progressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                Bundle bundle = new Bundle();
                bundle.putInt("campusId", ConvertUtils.getInteger(data.get("newsId")));
                bundle.putInt("partined", ConvertUtils.getInteger(data.get("partined")));
                bundle.putBoolean("partincount", ConvertUtils.getBoolean(data.get("partincount"), false));
                bundle.putString("subject", ConvertUtils.getString(data.get("subject")));
                bundle.putString("content", ConvertUtils.getString(data.get("content")));
                bundle.putStringArrayList("imageUrl", ConvertUtils.getStringList(data.get("imageUrls")));
                bundle.putString("date", ConvertUtils.getString(data.get("date")));
                Intent intent = new Intent(mContext, CampusDetailActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }
}

