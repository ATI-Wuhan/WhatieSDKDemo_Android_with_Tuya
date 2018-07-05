package com.whatie.ati.androiddemo.views;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tuya.smart.sdk.TuyaUser;
import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.constants.Constants;
import com.whatie.ati.androiddemo.constants.EhomeResource;
import com.whatie.ati.androiddemo.utils.ParseUtil;
import com.whatie.ati.androiddemo.widget.CircleImageView;
import com.d9lab.ati.whatiesdk.bean.BaseModelResponse;
import com.d9lab.ati.whatiesdk.bean.BaseResponse;
import com.d9lab.ati.whatiesdk.bean.User;
import com.d9lab.ati.whatiesdk.callback.BaseCallback;
import com.d9lab.ati.whatiesdk.callback.UserCallback;
import com.d9lab.ati.whatiesdk.ehome.EHome;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.d9lab.ati.whatiesdk.util.Code;
import com.d9lab.ati.whatiesdk.util.SharedPreferenceUtils;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Response;
import com.squareup.picasso.Picasso;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;

import static com.lzy.okgo.cache.CacheMode.NO_CACHE;

/**
 * Created by 神火 on 2018/6/27.
 */

public class PersonalActivity extends BaseActivity {
    private static final String TAG = "PersonalActivity";
    private static final int REQUEST_CHANGE_NAME = 1;

    ImagePicker mImagePicker;
    MaterialDialog mConfirmExitDialog;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.iv_personal_image)
    CircleImageView ivPersonalimage;
    @BindView(R.id.tv_user_email)
    TextView tvUserEmail;

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int GALLERY = 0xa0;
    public static final int STORAGE = 0;
    private static String USER_ICON_FILE_NAME_CAMERA = "IconCamera.jpg";//最终确定的头像图片，根据url下载的
    public static final String FILE_COPY_GALLERY = "copyFromGallery.jpg";//拍照、选取、剪切、上传时用的图片

    private PopupWindow popupWindow;
    private View view;
    private TextView albumTextv;
    private TextView cameraTextv;
    private TextView cancleTx;
    private Bitmap photo;
    private boolean hasPhoto = false;


    @Override
    protected int getContentViewId() {
        return R.layout.act_personal;
    }


    @Override
    protected void initViews() {
        tvTitle.setText(getString(R.string.personal_title));
        if ((int) SharedPreferenceUtils.get(mContext, Code.SP_USER_ID, -1) != -1) {
            tvUserName.setText((String) SharedPreferenceUtils.get(mContext, Code.SP_USER_NAME, ""));
            tvUserEmail.setText((String) SharedPreferenceUtils.get(mContext, Code.SP_USER_EMAIL, ""));
            saveImageCache((String) SharedPreferenceUtils.get(mContext, Code.SP_USER_THUMB, ""));
        } else {
            ivPersonalimage.setImageResource(EhomeResource.IC_AVATAR[Code.THEME_NOW]);
        }

    }

    public void saveImageCache(String url) {
        File file = new File(Constants.getAppImageFolder(), USER_ICON_FILE_NAME_CAMERA);
        if (file.exists()) {
            Picasso.with(mContext).load(file)
                    .error(R.mipmap.default_image)
                    .into(ivPersonalimage);
        } else {
            OkGo.<File>get(url)
                    .tag(this)
                    .cacheKey(url)
                    .cacheMode(NO_CACHE)
                    .cacheTime(-1)
                    .execute(new FileCallback(Constants.getAppImageFolder(), USER_ICON_FILE_NAME_CAMERA) {
                        @Override
                        public void onSuccess(Response<File> response) {
                            Picasso.with(mContext).load(new File(response.body().getAbsolutePath()))
                                    .error(R.mipmap.default_image)
                                    .into(ivPersonalimage);
                        }

                        @Override
                        public void onError(Response<File> response) {
                        }
                    });
        }
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initDatas() {

    }

    @OnClick({R.id.ll_title_left, R.id.rl_personal_name, R.id.rl_personal_change_password, R.id.tv_personal_logout, R.id.iv_personal_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_personal_name:
                if (EHome.getInstance().isLogin()) {
                    Intent intent = new Intent(PersonalActivity.this, ChangeNickNameActivity.class);
                    intent.putExtra(Code.USER_NAME, EHome.getInstance().getmUser().getName());
                    startActivityForResult(intent, REQUEST_CHANGE_NAME);
                } else {
                    startLoginAct();
                }
                break;
            case R.id.rl_personal_change_password:
                if (EHome.getInstance().isLogin()) {
                    Intent intent2 = new Intent(PersonalActivity.this, ChangePwdActivity.class);
                    startActivity(intent2);
                } else {
                    startLoginAct();
                }
                break;
            case R.id.tv_personal_logout:
                if (EHome.getInstance().isLogin()) {
                    showExitConfirmDialog();
                } else {
                    startLoginAct();
                }
                break;
            case R.id.iv_personal_image:
                if (EHome.getInstance().isLogin()) {
                    showPopWindow();
                } else {
                    startLoginAct();
                }
                break;
            case R.id.ll_title_left:
                finish();
                break;
        }
    }

    private void startLoginAct() {
        if ((int) SharedPreferenceUtils.get(mContext, Code.SP_USER_ID, -1) != -1) {
            Toast.makeText(mContext, getString(R.string.local_state_login_first), Toast.LENGTH_SHORT).show();
        }
        startActivity(new Intent(PersonalActivity.this, LoginActivity.class));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CHANGE_NAME:
                    tvUserName.setText(intent.getStringExtra("data_return"));
                    break;
                case CODE_GALLERY_REQUEST:
                    if (intent != null) {
                        try {
                            Bitmap b = BitmapFactory.decodeStream(getContentResolver().openInputStream(intent.getData()));
                            File file = new File(Constants.getAppImageFolder(), FILE_COPY_GALLERY);
                            if (!file.exists()) file.createNewFile();
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                            b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                            bos.flush();
                            bos.close();
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (ParseUtil.hasSdcard()) {
                            File tempFile = new File(Constants.getAppImageFolder(), FILE_COPY_GALLERY);
                            cropRawPhoto(Uri.fromFile(tempFile));
                        }
                    }
                    break;

                case CODE_CAMERA_REQUEST:
                    if (ParseUtil.hasSdcard()) {
                        File tempFile = new File(Constants.getAppImageFolder(), FILE_COPY_GALLERY);
                        cropRawPhoto(Uri.fromFile(tempFile));
                    }
                    break;

                case CODE_RESULT_REQUEST:
                    if (intent != null) {
                        mLoadingDialog.show();
                        updatePortrait();
                    }
                    break;
            }
        }

    }

    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("scale", true);
        intent.putExtra("crop", true);

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    private void showPopWindow() {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.pop_select_photo, null);
            popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
        }
        // 设置动画
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimationFromBottom);
        // 聚焦，不然无法监听点击
        popupWindow.setFocusable(true);
        // 设置在外面点击时候，该PopupWindow小弹窗消失
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(PersonalActivity.this.findViewById(R.id.ll_personal_activity),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);


        // 监听事件，有两个，一个是触摸外部时关闭的事件，一个触摸小窗口里时的菜单（照片或拍照）的事件
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                popupWindow.dismiss();
            }
        });

        // 第二个，点击照片或拍照，要发生跳转
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        albumTextv = (TextView) view.findViewById(R.id.tv_select_photo_form_album);
        cameraTextv = (TextView) view.findViewById(R.id.tv_select_photo_form_camera);
        cancleTx = (TextView) view.findViewById(R.id.tv_select_photo_cancel);
        albumTextv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                choseImageFromGallery();
            }
        });
        cameraTextv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                choseImageFromCameraCapture();
            }
        });
        cancleTx.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    // 从本地相册选取图片作为头像
    protected void choseImageFromGallery() {
        requestPermission();
    }

    // 启动手机相机拍摄照片作为头像
    private void choseImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (ParseUtil.hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Constants.getAppImageFolder(), FILE_COPY_GALLERY)));
        }
        try {
            startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
        } catch (SecurityException e) {
            Toast.makeText(mContext, getString(R.string.permission_camera), Toast.LENGTH_SHORT).show();
        }
    }


    private void showExitConfirmDialog() {
        if (mConfirmExitDialog != null) {
            mConfirmExitDialog.dismiss();
            mConfirmExitDialog = null;
        }
        mConfirmExitDialog = new MaterialDialog(mContext);
        mConfirmExitDialog.setTitle(getString(R.string.confirm_exit_title))
                .setMessage(getString(R.string.confirm_exit_tip))
                .setPositiveButton(getString(R.string.confirm_device_title), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mConfirmExitDialog.dismiss();
                        mLoadingDialog.show();
                        logOut();
                    }
                })
                .setNegativeButton(getString(R.string.download_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mConfirmExitDialog.dismiss();
                    }
                }).show();
    }

    private void logOut() {
        EHomeInterface.getINSTANCE().logOut(mContext, new BaseCallback() {
            @Override
            public void onSuccess(Response<BaseResponse> response) {
                mLoadingDialog.dismiss();
                EHome.getInstance().logOut();
                Picasso.with(mContext).invalidate(new File(Constants.getAppImageFolder(), USER_ICON_FILE_NAME_CAMERA));
                File file = new File(Constants.getAppImageFolder(), USER_ICON_FILE_NAME_CAMERA);
                if (file.exists()) {
                    file.delete();
                }
                TuyaUser.getDeviceInstance().onDestroy();
                startActivity(new Intent(PersonalActivity.this, LoginActivity.class));
                finish();
            }

            @Override
            public void onError(Response<BaseResponse> response) {
                super.onError(response);
                mLoadingDialog.dismiss();
                Toast.makeText(mContext, Code.NETWORK_WRONG, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void updatePortrait() {
        EHomeInterface.getINSTANCE().updateCustomerPortrait(mContext,
                new File(Constants.getAppImageFolder(), FILE_COPY_GALLERY),
                new UserCallback() {
                    @Override
                    public void onSuccess(Response<BaseModelResponse<User>> response) {
                        mLoadingDialog.dismiss();
                        if (response.body().isSuccess()) {
                            Picasso.with(mContext).invalidate(new File(Constants.getAppImageFolder(), USER_ICON_FILE_NAME_CAMERA));
                            EHome.getInstance().setmUser(response.body().getValue());
                            File file = new File(Constants.getAppImageFolder(), USER_ICON_FILE_NAME_CAMERA);
                            if (file.exists()) {
                                file.delete();
                            }
                            File file1 = new File(Constants.getAppImageFolder(), FILE_COPY_GALLERY);
                            if (file1.exists()){
                                file1.delete();
                            }
                            saveImageCache(EHome.getInstance().getmUser().getPortraitThumb().getPath());
                        } else {
                            Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Response<BaseModelResponse<User>> response) {
                        super.onError(response);
                        mLoadingDialog.dismiss();
                        Toast.makeText(mContext, Code.NETWORK_WRONG, Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void requestPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                this.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE);
            } else {
                Intent intentFromGallery = new Intent("android.intent.action.GET_CONTENT");
                intentFromGallery.setType("image/*");
                try {
                    startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
                } catch (SecurityException e) {
                    Toast.makeText(mContext, getString(R.string.permission_storage), Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            try {
                Intent intentFromGallery = new Intent("android.intent.action.GET_CONTENT");
                intentFromGallery.setType("image/*");
                startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
            } catch (Exception e) {
                Toast.makeText(mContext.getApplicationContext(), getString(R.string.permission_storage), Toast.LENGTH_SHORT).show();

            }
        }
    }

    public boolean checkPermission(@NonNull String permission) {
        return ActivityCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    mLoadingDialog.dismiss();
                    Toast.makeText(mContext, getString(R.string.permission_storage), Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }
}

