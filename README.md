# HiImagePicker
本地图片选择器

# 基本使用

1. 选择图片
```
 ImageSelector.create()
                .setCount(9)
                .multi()
                .origin(mImageList)
                .showCamera(true)
                .start(this,SELECT_IMAGE_REQUEST);
```
2. 接收选择图片集合路径
```
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
```
# 依赖第三方

```
implementation 'com.guolindev.permissionx:permissionx:1.6.1'
implementation  "com.github.bumptech.glide:glide:4.9.0"
implementation  "com.github.bumptech.glide:compiler:4.9.0"
```
