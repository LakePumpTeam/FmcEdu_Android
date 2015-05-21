package com.fmc.edu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.customcontrol.ImageShowControl;
import com.fmc.edu.entity.DynamicEntity;
import com.fmc.edu.entity.ImageItemEntity;
import com.fmc.edu.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Candy on 2015/5/21.
 */
public class DynamicListAdapter extends BaseAdapter {
    private List<DynamicEntity> mItems;
    private Context mContext;
    private ImageLoader mImageLoader;

    public DynamicListAdapter(Context context, List<DynamicEntity> items, ImageLoader imageLoader) {
        mItems = items;
        mContext = context;
        mImageLoader = imageLoader;
    }

    @Override
    public int getCount() {
        if (null == mItems) {
            return 0;
        }
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        if (null == mItems) {
            return null;
        }
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_dynamic_list, null);
        }
        TextView txtTitle = (TextView) convertView.findViewById(R.id.item_dynamic_list_txt_title);
        TextView txtContent = (TextView) convertView.findViewById(R.id.item_dynamic_list_txt_content);
        TextView txtDate = (TextView) convertView.findViewById(R.id.item_dynamic_list_txt_date);
        TextView txtReadAll = (TextView) convertView.findViewById(R.id.item_dynamic_list_txt_read_all);
        GridView gridPicture = (GridView) convertView.findViewById(R.id.item_dynamic_list_grid_picture);
        DynamicEntity dynamicItem = mItems.get(position);
        if (null != mImageLoader) {
            DynamicItemGridAdapter adapter = new DynamicItemGridAdapter(mContext, getImageList(dynamicItem), mImageLoader);
            gridPicture.setAdapter(adapter);
        }
        gridPicture.setOnItemClickListener(gridOnItemClickListener);
        //TODO图片的绑定
        txtTitle.setText(dynamicItem.Title);
        txtContent.setText(dynamicItem.Content);
        txtDate.setText(dynamicItem.datetime);
        txtReadAll.setTag(dynamicItem.id);
        txtReadAll.setOnClickListener(txtReadAllOnClickListener);
        return convertView;
    }

    private List<ImageItemEntity> getImageList(DynamicEntity dynamicItem) {
        List<ImageItemEntity> list = new ArrayList<ImageItemEntity>();
        List<String> thumbnailList = dynamicItem.ThumbnailList;
        List<String> artWorkList = dynamicItem.ArtWorkList;
        if (null == thumbnailList) {
            return null;
        }
        for (int i = 0; i < thumbnailList.size(); i++) {
            ImageItemEntity item = new ImageItemEntity();
            String thumbnailUrl = thumbnailList.get(i);
            String artWorkUrl = artWorkList.size() < i ? "" : artWorkList.get(i);
            item.imageURL = thumbnailUrl;
            item.bigImageURL = artWorkUrl;
            list.add(item);
        }
        return list;
    }

    private View.OnClickListener txtReadAllOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //查看全文的跳转
        }
    };

    private AdapterView.OnItemClickListener gridOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ImageItemEntity imageItemEntity = (ImageItemEntity) view.getTag();
            if (null == imageItemEntity || StringUtils.isEmptyOrNull(imageItemEntity.bigImageURL)) {
                return;
            }
            Bitmap bigBitMap = mImageLoader.loadImageSync(imageItemEntity.bigImageURL);
            ImageShowControl imageShowControl = new ImageShowControl(mContext);
            imageShowControl.showWindow(view, bigBitMap);
        }
    };
}
