package com.imooc.picker;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.imooc.picker.selector.SelectImageListener;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 图片选择
 */
public class SelectImageActivity extends AppCompatActivity implements View.OnClickListener {
    // 选择图片模式
    public static final String EXTRA_SELECT_MODE = "selectMode";
    // 是否显示相机
    public static final String EXTRA_SHOW_CAMERA = "shoCamera";
    // 选择最大数量
    public static final String EXTRA_SELECT_COUNT = "selectCount";
    // 选择图片集合
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "EXTRA_DEFAULT_SELECTED_LIST";
    // 返回选择图片列表的EXTRA_KEY
    public static final String EXTRA_RESULT = "EXTRA_RESULT";

    // 加载所有的数据
    private static final int LOADER_TYPE = 0x0021;

    // 多选
    public static final int MODE_MULTI = 0x0011;
    // 单选
    public static final int MODE_SINGLE = 0x0012;
    // 单选或者多选，int类型的type
    private int mMode = MODE_MULTI;
    // int 类型的图片张数
    private int mMaxCount = 8;
    // boolean 类型的是否显示拍照按钮
    private boolean mShowCamera = true;
    // ArraryList<String> 已经选择好的图片
    private ArrayList<String> mResultList;


    private RecyclerView mImageListRv;
    private TextView mSelectPreview;
    private TextView mSelectNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image);

        mImageListRv=findViewById(R.id.image_list_rv);
        mSelectPreview=findViewById(R.id.select_preview);
        mSelectNum=findViewById(R.id.select_num);

        initTitle();

        initData();
    }

    protected void initData() {
        // 1.获取传递过来的参数
        Intent intent = getIntent();
        mMode = intent.getIntExtra(EXTRA_SELECT_MODE, mMode);
        mMaxCount = intent.getIntExtra(EXTRA_SELECT_COUNT, mMaxCount);
        mShowCamera = intent.getBooleanExtra(EXTRA_SHOW_CAMERA, mShowCamera);
        mResultList = intent.getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
        if (mResultList == null) {
            mResultList = new ArrayList<>();
        }
        PermissionX.init(this)
                .permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                        if (allGranted){
                            // 2.初始化本地图片数据
                            initImageList();
                            //3.改变显示
                            exchangeViewShow();
                        }
                    }
                });
    }
    private void exchangeViewShow() {
        if (mResultList.size() > 0) {
            mSelectPreview.setEnabled(true);
            mSelectPreview.setOnClickListener(this);
        } else {
            mSelectPreview.setEnabled(false);
            mSelectPreview.setOnClickListener(null);
        }
        mSelectNum.setText(mResultList.size() + "/" + mMaxCount);
    }
    /**
     * 2.ContentProvider获取内存卡中所有的图片
     */
    private void initImageList() {
        // 耗时操作，开线程，AsyncTask,
        // int id 查询全部
        LoaderManager.getInstance(this).initLoader(LOADER_TYPE,null,mLoaderCallback);
    }
    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        public final String[] IMAGE_PROJECTION= {
                MediaStore.Images.Media.DATA,// 路径
                MediaStore.Images.Media.DISPLAY_NAME,// 路径
                MediaStore.Images.Media.DATE_ADDED,// 路径
                MediaStore.Images.Media.MIME_TYPE,// 路径
                MediaStore.Images.Media.SIZE,// 路径
                MediaStore.Images.Media._ID// 路径
        };

        @NonNull
        @Override
        public Loader onCreateLoader(int id, @Nullable Bundle args) {
            CursorLoader cursorLoader = new CursorLoader(SelectImageActivity.this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,IMAGE_PROJECTION,
                    IMAGE_PROJECTION[4] + ">0 AND "+ IMAGE_PROJECTION[3] + "=? OR "+IMAGE_PROJECTION[3] + "=? ",
                    new String[]{"image/jpeg", "image/png"},IMAGE_PROJECTION[2]+" DESC");
            return cursorLoader;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            // 解析，封装到集合  只保存String路径
            if (data!=null && data.getCount()>0){
                ArrayList<String> images = new ArrayList<>();
                if (mShowCamera){
                    images.add("");
                }
                //不断遍历循环
                while (data.moveToNext()){
                    // 只保存路径
                    String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                    images.add(path);
                }
                // 显示列表数据
                showImageList(images);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader loader) {

        }
    };
    /**
     * 3.展示获取到的图片显示到列表
     */
    private void showImageList(ArrayList<String> images) {
        SelectImageListAdapter adapter = new SelectImageListAdapter(this,images,mResultList,mMaxCount);
        mImageListRv.setLayoutManager(new GridLayoutManager(this,4));
        adapter.setSelectImageListener(new SelectImageListener() {
            @Override
            public void select() {
                exchangeViewShow();
            }
        });
        mImageListRv.setAdapter(adapter);
    }

//    @OnClick(R.id.select_finish)
    public void selectFinish(View view){
        Intent intent=new Intent();
        intent.putStringArrayListExtra(EXTRA_RESULT,mResultList);
        setResult(RESULT_OK,intent);
        finish();
    }


    protected void initTitle() {
       // TODO
//        new DefaultNavigationBar
//                .Builder(this,findViewById(R.id.view_root))
//                .setTitle("所有图片")
//                .setLeftClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v){
//                        AlertDialog dialog= new AlertDialog.Builder(SelectImageActivity.this)
//                                .setContentView(R.layout.ensure_dialog)
//                                .setText(R.id.tv_message,"是否确定退出")
//                                .setCancelable(true)
//                                .fromBottom(true)
//                                .fromBottom(false)
//                                .show();
//                        dialog.setOnclickListener(R.id.ensure, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                setResult(RESULT_CANCELED);
//                                finish();
//                            }
//                        });
//                        dialog.setOnclickListener(R.id.cancel, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                dialog.dismiss();
//                            }
//                        });
//                    }
//                })
//                .build();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 1.第一个要把图片加到集合

        // 2.调用sureSelect()方法


        // 3.通知系统本地有图片改变，下次进来可以找到这张图片
        // notify system the image has change
        // sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mTempFile));
    }
}