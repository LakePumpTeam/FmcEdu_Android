package com.fmc.edu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.fmc.edu.R;
import com.fmc.edu.customcontrol.MultiPictureControl;
import com.fmc.edu.entity.ImageItemEntity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Candy on 2015/5/20.
 */
public class MultiPictureItemAdapter extends BaseAdapter {
    private Context mContext;
    private List<ImageItemEntity> mList;
    private ImageLoader mImageLoader;

    public MultiPictureItemAdapter(Context context, ImageLoader imageLoader) {
        this.mContext = context;
        this.mImageLoader = imageLoader;
    }

    public void addAllList(List<ImageItemEntity> list) {
        mList = list;
        notifyDataSetChanged();
    }

    public List<ImageItemEntity> getSelectedList() {
        if (null == mList) {
            return null;
        }
        List<ImageItemEntity> selectedList = new ArrayList<ImageItemEntity>();
        for (int i = 0; i < mList.size(); i++) {
            ImageItemEntity itemEntity = mList.get(i);
            if (itemEntity.isCheck) {
                selectedList.add(itemEntity);
            }
        }
        return selectedList;
    }

    public int getSelectedCount() {
        List<ImageItemEntity> list = getSelectedList();
        if (null == list) {
            return 0;
        }
        return list.size();
    }

    public void setCheck(int position, boolean isCheck, View view) {
        mList.get(position).isCheck = isCheck;
        ImageView imageSelectItemImgSelect = (ImageView) view.findViewById(R.id.image_select_item_img_select);
        FrameLayout imageSelectItemFrameCover = (FrameLayout) view.findViewById(R.id.image_select_item_frame_cover);
        if (isCheck) {
            imageSelectItemFrameCover.setVisibility(View.VISIBLE);
            imageSelectItemImgSelect.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.image_checked));
        } else {
            imageSelectItemFrameCover.setVisibility(View.GONE);
            imageSelectItemImgSelect.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.image_unchecked));
        }
    }

    public void setSelectList(List<ImageItemEntity> selectedList) {
        for (int i = 0; i < selectedList.size(); i++) {
            ImageItemEntity selectedItem = selectedList.get(i);
            for (int j = 0; j < mList.size(); j++) {
                ImageItemEntity item = mList.get(j);
                if (item.imageURL == selectedItem.imageURL) {
                    item.isCheck = true;
                }
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if (null == mList) {
            return 0;
        }
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        if (null == mList) {
            return null;
        }
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.image_select_item, null);
        }
        try {
            final ImageSelectItemHolder holder = new ImageSelectItemHolder();
            holder.imageSelectItemImgSelect = (ImageView) convertView.findViewById(R.id.image_select_item_img_select);
            holder.imageSelectItemFrameCover = (FrameLayout) convertView.findViewById(R.id.image_select_item_frame_cover);
            holder.imageSelectItemSrc = (ImageView) convertView.findViewById(R.id.image_select_item_src);
            holder.position = position;

            ImageItemEntity item = mList.get(position);
            holder.imageSelectItemSrc.setImageBitmap(item.imageBitMap);
//            holder.imageSelectItemCheckSelect.setChecked(item.isCheck);
            if (item.isCheck) {
                holder.imageSelectItemFrameCover.setVisibility(View.VISIBLE);
                holder.imageSelectItemImgSelect.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.image_checked));
            } else {
                holder.imageSelectItemFrameCover.setVisibility(View.GONE);
                holder.imageSelectItemImgSelect.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.image_unchecked));
            }
            mImageLoader.displayImage("file://" + item.imageURL, holder.imageSelectItemSrc,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            super.onLoadingComplete(imageUri, view, loadedImage);
                        }

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            DisplayMetrics dm = new DisplayMetrics();
                            ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
                            ViewGroup.LayoutParams params = holder.imageSelectItemSrc.getLayoutParams();
                            params.height = dm.widthPixels / 3 - 10;
                            holder.imageSelectItemSrc.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            super.onLoadingStarted(imageUri, view);
                        }
                    });
            convertView.setTag(holder);
            // convertView.setOnClickListener(OnItemClickListener);
        } catch (Exception e) {
            Log.e("tttt", "---------------------**************---------------------");
        }
        return convertView;
    }

    private class ImageSelectItemHolder {
        ImageView imageSelectItemImgSelect;
        FrameLayout imageSelectItemFrameCover;
        ImageView imageSelectItemSrc;
        int position;
    }
}
