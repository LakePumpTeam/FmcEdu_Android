package com.fmc.edu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.customcontrol.ImageShowControl;
import com.fmc.edu.entity.DynamicItemEntity;
import com.fmc.edu.entity.ImageItemEntity;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;

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
        imgPhoto.setTag(item.imageUrls);

        String imgUrl = null != item.imageUrls && item.imageUrls.size() > 0 ? item.imageUrls.get(0).origUrl : "";
        ImageLoaderUtil.initCacheImageLoader(mContext).displayImage(imgUrl, imgPhoto);
        imgPhoto.setOnClickListener(imgPhotoOnClickListener);

        return convertView;
    }



    private View.OnClickListener imgPhotoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            List<ImageItemEntity> imageList = (List<ImageItemEntity>) v.getTag();
            ImageShowControl imageShowControl = new ImageShowControl(mContext);
            imageShowControl.showWindow(v, getOrigUrl(imageList));
        }
    };

    private List<String> getOrigUrl(List<ImageItemEntity> list) {
        List<String> origUrls = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            origUrls.add(list.get(i).origUrl);
        }
        return origUrls;
    }



}
