package com.imooc.picker;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.imooc.adapter.CommonRecyclerAdapter;
import com.imooc.adapter.ViewHolder;
import com.imooc.picker.selector.SelectImageListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片选择适配器
 */
public class SelectImageListAdapter extends CommonRecyclerAdapter<String> {

    private ArrayList<String> mImages;
    private int mMaxCount;

    public SelectImageListAdapter(Context context, List<String> datas, ArrayList<String> images, int maxCount) {
        super(context, datas, R.layout.media_chooser_item);
        this.mImages = images;
        this.mMaxCount=maxCount;
    }

    @Override
    public void convert(ViewHolder holder, String item) {
        if (TextUtils.isEmpty(item)){
            // 显示拍照
            holder.setViewVisibility(R.id.camera_ll, View.VISIBLE);
            holder.setViewVisibility(R.id.media_selected_indicator, View.INVISIBLE);
            holder.setViewVisibility(R.id.image, View.INVISIBLE);

            holder.setOnIntemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 调用拍照，权限很重要，6.0以上要处理
                    // http://www.jianshu.com/p/823360bb183f
                }
            });
        }else{
            // 隐藏拍照
            holder.setViewVisibility(R.id.camera_ll, View.INVISIBLE);
            holder.setViewVisibility(R.id.media_selected_indicator, View.VISIBLE);
            holder.setViewVisibility(R.id.image, View.VISIBLE);

            ImageView imageView = holder.getView(R.id.image);
            ImageView media_selected_indicator = holder.getView(R.id.media_selected_indicator);


            if (mImages.contains(item)) {
                media_selected_indicator.setSelected(true);
            } else {
                media_selected_indicator.setSelected(false);
            }
            Glide.with(mContext).load(item).centerCrop().into(imageView);
            holder.setOnIntemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                     if (!mImages.contains(item)){
                         if (mImages.size() >= mMaxCount){
                             Toast.makeText(mContext, "最多选择"+mMaxCount+"张", Toast.LENGTH_SHORT).show();
                             return;
                         }
                         mImages.add(item);
                     }else{
                         mImages.remove(item);
                     }
                    if (mImages.contains(item)) {
                        media_selected_indicator.setImageResource(R.drawable.media_chooser_ic_checked);
                    } else {
                        media_selected_indicator.setImageResource(R.drawable.media_chooser_ic);
                    }
                     if (mSelectImageListener!=null){
                         mSelectImageListener.select();
                     }
                }
            });
        }
    }
    private SelectImageListener mSelectImageListener;

    public void setSelectImageListener(SelectImageListener mSelectImageListener) {
        this.mSelectImageListener = mSelectImageListener;
    }
}
