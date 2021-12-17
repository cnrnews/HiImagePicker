package com.imooc.picker.selector;


import android.app.Activity;
import android.content.Intent;


import com.imooc.picker.SelectImageActivity;

import java.util.ArrayList;

/**
 * 图片选择器入口类
 */
public class ImageSelector {

    // 最多可以选择多少张图片
    private int mMaxCount = 9;
    //
    private int mMode = SelectImageActivity.MODE_MULTI;
    // 是否显示拍照的相机
    private boolean mShowCamera = true;
    // 选择的图片集合
    private ArrayList<String> mOriginData;

    private ImageSelector(){}
    public static ImageSelector create(){
        return new ImageSelector();
    }

    /**
     * 单选
     * @return
     */
    public ImageSelector single(){
        mMode = SelectImageActivity.MODE_SINGLE;
        return this;
    }
    /**
     * 多选
     * @return
     */
    public ImageSelector multi(){
        mMode = SelectImageActivity.MODE_MULTI;
        return this;
    }

    /**
     * 设置可以选择多少张
     * @param maxCount
     */
    public ImageSelector setCount(int maxCount) {
        this.mMaxCount = maxCount;
        return this;
    }

    /**
     * 是否显示相机
     * @param showCamera
     * @return
     */
    public ImageSelector showCamera(boolean showCamera) {
        this.mShowCamera = showCamera;
        return this;
    }

    /**
     *原来选择好的图片
     * @param originData
     */
    public ImageSelector origin(ArrayList<String> originData) {
        this.mOriginData = originData;
        return this;
    }

    /**
     * 启动执行  权限6.0自己需要去申请
     * @param activity
     * @param requestCode
     */
    public void start(Activity activity,int requestCode){
        Intent intent = new Intent(activity, SelectImageActivity.class);
        addParamsByIntent(intent);
        activity.startActivityForResult(intent,requestCode);
    }
    /**
     * 给Intent添加参数
     *
     * @param intent
     */
    private void addParamsByIntent(Intent intent) {
        intent.putExtra(SelectImageActivity.EXTRA_SHOW_CAMERA,mShowCamera);
        intent.putExtra(SelectImageActivity.EXTRA_SELECT_COUNT,mMaxCount);
        if (mOriginData!=null && mMode == SelectImageActivity.MODE_MULTI){
            intent.putStringArrayListExtra(SelectImageActivity.EXTRA_DEFAULT_SELECTED_LIST,mOriginData);
        }
        intent.putExtra(SelectImageActivity.EXTRA_SELECT_MODE,mMode);
    }
}
