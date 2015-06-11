package com.fmc.edu.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fmc.edu.BaseActivity;
import com.fmc.edu.ClassDynamicActivity;
import com.fmc.edu.FmcApplication;
import com.fmc.edu.R;
import com.fmc.edu.customcontrol.ExpandableTextViewControl;
import com.fmc.edu.customcontrol.ImageShowControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.entity.CommentItemEntity;
import com.fmc.edu.entity.DynamicItemEntity;
import com.fmc.edu.entity.ImageItemEntity;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ImageLoaderUtil;
import com.fmc.edu.utils.ToastToolUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/24.
 */
public class ClassDynamicItemAdapter extends FmcBaseAdapter<DynamicItemEntity> {
    private final SparseBooleanArray mCollapsedStatus;

    public ClassDynamicItemAdapter(Context context, List<DynamicItemEntity> items) {
        super(context, items);
        mCollapsedStatus = new SparseBooleanArray();
    }

    public void addComment(CommentItemEntity commentItemEntity, int position) {
        if (null == mItems.get(position).commentList) {
            mItems.get(position).commentList = new ArrayList<>();
        }

        mItems.get(position).commentList.add(commentItemEntity);
        mItems.get(position).commentCount++;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mItems) {
            return convertView;
        }
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_class_danamic_list, null);
        }
        TextView txtDate = (TextView) convertView.findViewById(R.id.item_class_dynamic_list_txt_date);
        TextView txtComment = (TextView) convertView.findViewById(R.id.item_class_dynamic_list_txt_comment);
        GridView gridView = (GridView) convertView.findViewById(R.id.item_class_dynamic_list_grid_picture);
        LinearLayout commentView = (LinearLayout) convertView.findViewById(R.id.item_class_dynamic_list_ll_comment);
        ExpandableTextViewControl expand_text_view = (ExpandableTextViewControl) convertView.findViewById(R.id.expand_text_view);
        ImageView imgDelete = (ImageView) convertView.findViewById(R.id.item_class_dynamic_list_img_delete);

        DynamicItemEntity item = mItems.get(position);
        expand_text_view.setText(item.content, mCollapsedStatus, position);
        txtComment.setText(ConvertUtils.getString(item.commentCount, "0"));
        txtDate.setText(item.createDate);

        Map<String, Object> commentItem = new HashMap<>();
        commentItem.put("newsId", item.newsId);
        commentItem.put("position", position);

        ClassDynamicItemHolder holder = new ClassDynamicItemHolder();
        holder.commentItem = commentItem;
        holder.view = convertView;
        txtComment.setTag(holder);

        imgDelete.setVisibility(item.author == FmcApplication.getLoginUser().userId ? View.VISIBLE : View.GONE);
        imgDelete.setTag(item);

        List<CommentItemEntity> commentList = item.commentList;
        commentView.removeAllViews();
        for (int i = 0; i < commentList.size(); i++) {
            String userName = commentList.get(i).userName + "：";
            String comment = commentList.get(i).comment;
            TextView textView = createText(userName, comment);
            commentView.addView(textView);
        }
        DynamicItemGridAdapter dynamicItemGridAdapter = new DynamicItemGridAdapter(mContext, item.imageUrls, ImageLoaderUtil.initCacheImageLoader(mContext));
        gridView.setAdapter(dynamicItemGridAdapter);
        gridView.setOnItemClickListener(gridOnItemClickListener);
        txtComment.setOnClickListener(txtCommentOnClickListener);
        imgDelete.setOnClickListener(imgDeleteOnClickListener);
        return convertView;
    }

    private TextView createText(String userName, String comment) {
        SpannableStringBuilder builder = new SpannableStringBuilder(userName + comment);
        TextView textView = new TextView(mContext);
        ForegroundColorSpan userNameSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_parent_name_font_color));
        ForegroundColorSpan commentSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.dynamic_class_dynamic_color));
        builder.setSpan(userNameSpan, 0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(commentSpan, userName.length(), userName.length() + comment.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
        return textView;
    }


    private View.OnClickListener txtCommentOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mContext.getClass() == ClassDynamicActivity.class) {
                ClassDynamicItemHolder holder = (ClassDynamicItemHolder) v.getTag();
                ((ClassDynamicActivity) mContext).setCommentVisible(ConvertUtils.getInteger(holder.commentItem.get("newsId"), 0), ConvertUtils.getInteger(holder.commentItem.get("position"), 0), holder.view);
            }
        }
    };

    private AdapterView.OnItemClickListener gridOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            List<ImageItemEntity> imageList = ((DynamicItemGridAdapter) parent.getAdapter()).getItems();
            ImageShowControl imageShowControl = new ImageShowControl(mContext);
            imageShowControl.showWindow(view, getOrigUrl(imageList), position);
        }
    };

    private View.OnClickListener imgDeleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DynamicItemEntity item = (DynamicItemEntity) v.getTag();
            deleteClassDynamic(item);
        }
    };

    public void deleteClassDynamic(final DynamicItemEntity item) {
        ((BaseActivity) mContext).mProgressControl.showWindow();
        Map<String, Object> params = new HashMap<>();
        params.put("newsId", item.newsId);
        params.put("userId", FmcApplication.getLoginUser().userId);
        MyIon.httpPost(mContext, "/news/requestDisableNews", params, ((BaseActivity) mContext).mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                ToastToolUtils.showShort("删除成功");
                mItems.remove(item);
                notifyDataSetChanged();
            }
        });
    }


    private List<String> getOrigUrl(List<ImageItemEntity> list) {
        List<String> origUrls = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            origUrls.add(list.get(i).origUrl);
        }
        return origUrls;
    }


    private class ClassDynamicItemHolder {
        public View view;
        public Map<String, Object> commentItem;

    }
}
