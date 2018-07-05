package com.whatie.ati.androiddemo.views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.Toast;

import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.application.WhatieApplication;
import com.whatie.ati.androiddemo.database.db.DeviceDaoOpe;
import com.d9lab.ati.whatiesdk.bean.BaseListResponse;
import com.d9lab.ati.whatiesdk.bean.BaseModelResponse;
import com.d9lab.ati.whatiesdk.bean.DeviceVo;
import com.d9lab.ati.whatiesdk.bean.User;
import com.d9lab.ati.whatiesdk.callback.DevicesCallback;
import com.d9lab.ati.whatiesdk.callback.UserCallback;
import com.d9lab.ati.whatiesdk.ehome.EHome;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.d9lab.ati.whatiesdk.util.Code;
import com.d9lab.ati.whatiesdk.util.SharedPreferenceUtils;
import com.d9lab.ati.whatiesdk.util.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.model.Response;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by 神火 on 2018/6/27.
 */

public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";
    private static final int PERMISSION_REQUEST_CODE = 0;
    private String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION};

    @BindView(R.id.iv_splash)
    ImageView ivSplash;
    private Handler mHandler = new Handler();

    private Runnable animRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.removeCallbacks(localDeviceRunnable);
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    };

    private Runnable aloginRunnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }
    };

    private Runnable localDeviceRunnable = new Runnable() {
        @Override
        public void run() {
            initDevicesLocal();
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.act_splash;
    }


    @Override
    protected void initViews() {
//        startAnim();
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initDatas() {
        checkPermission();
    }


    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(animRunnable);
        mHandler.removeCallbacks(aloginRunnable);
        mHandler.removeCallbacks(localDeviceRunnable);
        super.onDestroy();
    }

    private void startLogin() {

        if ((int) SharedPreferenceUtils.get(mContext, Code.SP_USER_ID, -1) != -1) {
            mHandler.postDelayed(localDeviceRunnable,10000);
            loginAuto();
        } else {
            startALogin();
        }
    }

    private void startAnim() {
        mHandler.postDelayed(animRunnable, 2000);
    }

    private void startALogin() {
        mHandler.postDelayed(aloginRunnable, 2000);
    }

    private void loginAuto() {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", (String) SharedPreferenceUtils.get(mContext, Code.SP_USER_EMAIL, ""));
        params.put("password", (String) SharedPreferenceUtils.get(mContext, Code.SP_MD5_PASSWORD, ""));
        params.put("accessId", EHome.getAPPID());
        params.put("accessKey", EHome.getSECRETKEY());

        OkGo.<BaseModelResponse<User>>post(Urls.LOGIN)
                .tag(mContext)
                .cacheMode(CacheMode.NO_CACHE)
                .retryCount(0)
                .params(params)
                .execute(new UserCallback() {
                    @Override
                    public void onSuccess(Response<BaseModelResponse<User>> response) {
                        if (response.body().isSuccess()) {
                            EHome.getInstance().setLogin(true);
                            EHome.getInstance().setmUser(response.body().getValue());
                            EHome.getInstance().setToken(response.body().getToken());
                            EHome.getInstance().startMqttService();
                            initDevicesOnline();
//                            SharedPreferenceUtils.put(mContext, "userId", Integer.valueOf(response.body().getValue().getId()));
                            Toast.makeText(mContext, "Auto login success!", Toast.LENGTH_SHORT).show();
                        } else {
                            initDevicesLocal();
                        }

                    }

                    @Override
                    public void onError(Response<BaseModelResponse<User>> response) {
                        super.onError(response);
                        if (response.body() != null) {
                            if (response.body().getMessage() != null || !response.body().getMessage().isEmpty()) {
                                Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                initDevicesLocal();
                            } else {
                                Toast.makeText(mContext, "login fail", Toast.LENGTH_SHORT).show();
                                initDevicesLocal();
                            }
                        }
                    }
                });
    }

    private void initDevicesOnline() {
        EHomeInterface.getINSTANCE().getMyDevices(mContext, new DevicesCallback() {
            @Override
            public void onSuccess(Response<BaseListResponse<DeviceVo>> response) {
                if (response.body().isSuccess()) {
                    if (response.body().getList().isEmpty()) {
                        DeviceDaoOpe.deleteAllDevices(mContext);
                    } else {
                        DeviceDaoOpe.deleteAllDevices(mContext);
                        DeviceDaoOpe.saveDevices(mContext, response.body().getList());
                        WhatieApplication.getInstance().initLocalData();
                        EHomeInterface.getINSTANCE().saveDevices(response.body().getList());
                    }
                } else {
                    EHomeInterface.getINSTANCE().saveDevices(DeviceDaoOpe.queryAllDeviceVos(mContext));
                }
                startAnim();
            }

            @Override
            public void onError(Response<BaseListResponse<DeviceVo>> response) {
                super.onError(response);
                EHomeInterface.getINSTANCE().saveDevices(DeviceDaoOpe.queryAllDeviceVos(mContext));
                startAnim();
            }
        });
    }

    private void initDevicesLocal() {
        EHomeInterface.getINSTANCE().saveDevices(DeviceDaoOpe.queryAllDeviceVos(mContext));
        startAnim();
    }


    private void checkPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            } else {
                startLogin();
            }
        } else {
            startLogin();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                LogUtil.log(TAG, "onRequestPermissionsResult: "+ permissions.toString());
                Toast.makeText(getApplicationContext(), getString(R.string.permissions_failed), Toast.LENGTH_SHORT).show();

            }
            startLogin();
        }
    }


}
