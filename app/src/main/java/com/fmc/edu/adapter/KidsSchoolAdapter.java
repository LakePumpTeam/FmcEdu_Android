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
import com.fmc.edu.utils.ToastToolUtils;

import java.util.List;

/**
 * Created by Candy on 2015/5/10.
 */
public class KidsSchoolAdapter extends FmcBaseAdapter<DynamicItemEntity> {
    public KidsSchoolAdapter(Context context, List<DynamicItemEntity> items) {
        super(context, items);
    }

    public void updateLikeByNewsId(int newsId, int like) {
        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i).newsId == newsId) {
                mItems.get(i).likeCount = like;
                notifyDataSetChanged();
            }
        }
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
        KidsSchoolHolder holder = new KidsSchoolHolder();
        holder.position = position;
        holder.imageList = item.imageUrls;
        imgPhoto.setTag(holder);

        String imgUrl = null != item.imageUrls && item.imageUrls.size() > 0 ? item.imageUrls.get(0).origUrl : "";
        ImageLoaderUtil.initCacheImageLoader(mContext).displayImage(imgUrl, imgPhoto);
        imgPhoto.setOnClickListener(imgPhotoOnClickListener);
        return convertView;
    }


    private View.OnClickListener imgPhotoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            KidsSchoolHolder holder = (KidsSchoolHolder) v.getTag();
            List<String> bigPictureUrl = ImageItemEntity.getOrigUrlList(holder.imageList);
            if (null == bigPictureUrl || 0 == bigPictureUrl.size()) {
                ToastToolUtils.showLong("无有效图片");
                return;
            }
            ImageShowControl imageShowControl = new ImageShowControl(mContext);
            imageShowControl.showWindow(v, bigPictureUrl, holder.position);
        }
    };

    private class KidsSchoolHolder {
        public int position;
        public List<ImageItemEntity> imageList;
    }
}
