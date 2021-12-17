package com.imooc.picker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.imooc.picker.selector.ImageSelector;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestImageActivity extends AppCompatActivity {

    private ArrayList<String> mImageList;
    private final int SELECT_IMAGE_REQUEST = 0x0011;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_image);
    }

    // 选择图片
    public void selectImage(View view){
        ImageSelector.create()
                .setCount(9)
                .multi()
                .origin(mImageList)
                .showCamera(true)
                .start(this,SELECT_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE_REQUEST && data != null) {
                mImageList = data.getStringArrayListExtra(SelectImageActivity.EXTRA_RESULT);
                // 做一下显示
                Log.e("TAG", mImageList.toString());
            }
        }
    }
}