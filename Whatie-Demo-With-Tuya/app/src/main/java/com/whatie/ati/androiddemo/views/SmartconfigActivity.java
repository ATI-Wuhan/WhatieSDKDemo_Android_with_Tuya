package com.whatie.ati.androiddemo.views;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.database.db.WifiDaoOpe;
import com.whatie.ati.androiddemo.database.entity.WifiDB;
import com.d9lab.ati.whatiesdk.bean.Device;
import com.d9lab.ati.whatiesdk.event.MqttAlreadyBindEvent;
import com.d9lab.ati.whatiesdk.event.MqttBindSuccessEvent;
import com.d9lab.ati.whatiesdk.event.MqttINFOEvent;
import com.d9lab.ati.whatiesdk.smartconfig.EspWifiAdminSimple;
import com.d9lab.ati.whatiesdk.smartconfig.EsptouchTask;
import com.d9lab.ati.whatiesdk.smartconfig.IEsptouchResult;
import com.d9lab.ati.whatiesdk.smartconfig.IEsptouchTask;
import com.d9lab.ati.whatiesdk.smartconfig.task.__IEsptouchTask;
import com.d9lab.ati.whatiesdk.util.Code;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.netopen.hotbitmapgg.library.view.RingProgressBar;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by liz on 2018/4/24.
 */

public class SmartconfigActivity extends BaseActivity {
    private static final String TAG = "DeviceAddingActivity";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_left)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_title_left)
    TextView tvTitleLeft;
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.iv_title_right)
    ImageView ivTitleRight;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.ll_title_right)
    LinearLayout llTitleRight;
    @BindView(R.id.tv_adding_progress_step_2)
    TextView tvAddingProgressStep2;
    @BindView(R.id.iv_adding_progress_step_2)
    ImageView ivAddingProgressStep2;
    @BindView(R.id.ll_adding_progress_2)
    LinearLayout llAddingProgress2;
    @BindView(R.id.tv_adding_progress_step_1)
    TextView tvAddingProgressStep1;
    @BindView(R.id.iv_adding_progress_step_1)
    ImageView ivAddingProgressStep1;
    @BindView(R.id.ll_adding_progress_1)
    LinearLayout llAddingProgress1;
    @BindView(R.id.tv_adding_progress_step_3)
    TextView tvAddingProgressStep3;
    @BindView(R.id.iv_adding_progress_step_3)
    ImageView ivAddingProgressStep3;
    @BindView(R.id.ll_adding_progress_3)
    LinearLayout llAddingProgress3;
    @BindView(R.id.rpb_device_adding_progressbar)
    RingProgressBar rpbDeviceAddingProgressbar;

    public static final long TOTAL_TIME = 60000;

    private MaterialDialog ensureCancelDialog;
    private MaterialDialog failedDialog;
    private MaterialDialog successDialog;
    private CountDownTimer mCountDownTimer;
    private int currentProgress;
    private int mMillisUntilFinished;

    private boolean isDeviceSearched = false;
    private boolean isDeviceRegistered = false;
    private boolean isDeviceAdded = false;

    private EspWifiAdminSimple mWifiAdmin;
    private IEsptouchTask mEsptouchTask;
    private final Object mLock = new Object();
    private String apSsid;
    private String apPassword;
    private String password;
    private String apToken;
    private String apBssid;
    private Device device;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(mContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
        mCountDownTimer.cancel();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_adding_device;
    }


    @Override
    protected void initViews() {
        tvTitle.setText(getString(R.string.device_add_title));
        mWifiAdmin = new EspWifiAdminSimple(mContext);
        apSsid = mWifiAdmin.getWifiConnectedSsid();
        apPassword = getIntent().getStringExtra(Code.NET_TOKEN) + getIntent().getStringExtra(Code.PASSWORD);
        apToken = getIntent().getStringExtra(Code.NET_TOKEN);
        password = getIntent().getStringExtra(Code.PASSWORD);
        apBssid = mWifiAdmin.getWifiConnectedBssid();
        new EsptouchAsyncTask3().execute(apSsid, apBssid, apPassword, "1");
        mCountDownTimer = new CountDownTimer(TOTAL_TIME, 600) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (rpbDeviceAddingProgressbar != null) {
                    currentProgress = (int) ((TOTAL_TIME - millisUntilFinished) * 100 / TOTAL_TIME);
                    rpbDeviceAddingProgressbar.setProgress(currentProgress);
                }
            }

            @Override
            public void onFinish() {
                if (!isDeviceAdded && rpbDeviceAddingProgressbar != null) {
                    currentProgress = 100;
                    rpbDeviceAddingProgressbar.setProgress(currentProgress);
                    showFailedDialog();
                }
            }
        }.start();
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initDatas() {

    }


    @OnClick(R.id.ll_title_left)
    public void onViewClicked() {
        if (!isDeviceAdded) {
            showEnsureCancelDialog();
        }
    }

    @Override
    public void onBackPressed() {
        if (!isDeviceAdded) {
            showEnsureCancelDialog();
        }
    }

    private void showEnsureCancelDialog() {
        ensureCancelDialog = new MaterialDialog(mContext);
        ensureCancelDialog.setMessage(getString(R.string.device_add_question))
                .setPositiveButton("Yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        synchronized (mLock) {
                            if (__IEsptouchTask.DEBUG) {
                                Log.i(TAG, "progress dialog is canceled");
                            }
                            if (mEsptouchTask != null) {
                                mEsptouchTask.interrupt();
                                mCountDownTimer.cancel();
                                ensureCancelDialog.dismiss();
                                SmartconfigActivity.this.finish();
                            }
                        }
                    }
                })
                .setNegativeButton("No", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ensureCancelDialog.dismiss();
                    }
                }).show();
    }

    private class EsptouchAsyncTask3 extends AsyncTask<String, Void, List<IEsptouchResult>> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<IEsptouchResult> doInBackground(String... params) {
            int taskResultCount = -1;
            synchronized (mLock) {
                String apSsid = mWifiAdmin.getWifiConnectedSsidAscii(params[0]);
                String apBssid = params[1];
                String apPassword = params[2];
                String taskResultCountStr = params[3];
                taskResultCount = Integer.parseInt(taskResultCountStr);
                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, mContext);
            }
            List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
            return resultList;
        }

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {
            IEsptouchResult firstResult = result.get(0);
            if (!firstResult.isCancelled()) {
                if (firstResult.isSuc()) {
                    if (!isDeviceRegistered && !isDeviceAdded && !isDeviceSearched) {
                        isDeviceSearched = true;
                        searchDeviceSuccess();
                    }
                } else {
                    if (!isDeviceRegistered && !isDeviceAdded && !isDeviceSearched) {
                        currentProgress = 100;
                        mCountDownTimer.cancel();
//                    showFailedDialog();
                    }
                }
            }
        }
    }

    private void searchDeviceSuccess() {
        ivAddingProgressStep1.setImageResource(R.drawable.ic_done);
        tvAddingProgressStep2.setTextColor(getResources().getColor(R.color.colorAccent));
        ivAddingProgressStep2.setVisibility(View.VISIBLE);
        if (currentProgress <= 35) {
            mCountDownTimer.cancel();
            int dvalue = 35 - currentProgress;
            mCountDownTimer = new CountDownTimer(1000, 1000 / dvalue) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (rpbDeviceAddingProgressbar != null) {
                        currentProgress++;
                        rpbDeviceAddingProgressbar.setProgress(currentProgress);
                    }
                }

                @Override
                public void onFinish() {
                    restartCountDown(65 * 600, 600, 35);
                }
            }.start();
        }
    }

    private void restartCountDown(final long totalTime, final long cell, final int alreadDone) {
        mCountDownTimer = new CountDownTimer(totalTime, cell) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (rpbDeviceAddingProgressbar != null) {
                    currentProgress = (int) ((totalTime - millisUntilFinished) / cell) + alreadDone;
                    rpbDeviceAddingProgressbar.setProgress(currentProgress);
                }
            }

            @Override
            public void onFinish() {
                if (!isDeviceAdded && rpbDeviceAddingProgressbar != null) {
                    currentProgress = 100;
                    rpbDeviceAddingProgressbar.setProgress(currentProgress);
                    showFailedDialog();
                }
            }
        }.start();
    }

    private void showFailedDialog() {
        failedDialog = new MaterialDialog(mContext);
        failedDialog.setTitle(getString(R.string.device_add_failed))
                .setMessage(getString(R.string.device_add_failed_tip))
                .setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        failedDialog.dismiss();
                        SmartconfigActivity.this.finish();
                    }
                }).show();
    }

    private void showAlreadyBindDialog(String email, String devName) {
        failedDialog = new MaterialDialog(mContext);
        WifiDB item = new WifiDB(apSsid, password);
        WifiDaoOpe.saveWifi(mContext, item);
        failedDialog.setTitle(getString(R.string.device_add_already_bind))
                .setMessage(getString(R.string.device_add_device_name) + devName + "\n" + getString(R.string.device_add_connect)
                        + email + " " + getString(R.string.device_add_connect_2))
                .setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        failedDialog.dismiss();
                        SmartconfigActivity.this.finish();
                    }
                }).show();
    }

    private void showSuccessDialog(final String devId, final String devName) {
        successDialog = new MaterialDialog(mContext);
        WifiDB item = new WifiDB(apSsid, password);
        WifiDaoOpe.saveWifi(mContext, item);
        successDialog.setTitle(getString(R.string.device_add_success))
                .setMessage(getString(R.string.device_add_success_tip))
                .setPositiveButton("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        successDialog.dismiss();
                        Intent startedIntent = new Intent(SmartconfigActivity.this, MainActivity.class);
                        startedIntent.putExtra(Code.DEV_ID, devId);
                        startedIntent.putExtra(Code.DEVICE_NAME, devName);
                        startActivity(startedIntent);
                    }
                }).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void onEventMainThread(final MqttINFOEvent event) {
        Log.d(TAG, "onEventMainThread:           MqttINFOEvent");
        isDeviceRegistered = true;
        if (!isDeviceSearched) {
            ivAddingProgressStep1.setImageResource(R.drawable.ic_done);
            tvAddingProgressStep2.setTextColor(getResources().getColor(R.color.colorAccent));
            ivAddingProgressStep2.setVisibility(View.VISIBLE);
        }
        ivAddingProgressStep2.setImageResource(R.drawable.ic_done);
        tvAddingProgressStep3.setTextColor(getResources().getColor(R.color.colorAccent));
        ivAddingProgressStep3.setVisibility(View.VISIBLE);
        if (currentProgress <= 65) {
            mCountDownTimer.cancel();
            int dvalue = 65 - currentProgress;
            mCountDownTimer = new CountDownTimer(1000, 1000 / dvalue) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (rpbDeviceAddingProgressbar != null) {
                        currentProgress++;
                        rpbDeviceAddingProgressbar.setProgress(currentProgress);
                    }
                }

                @Override
                public void onFinish() {
                    restartCountDown(35 * 600, 600, 65);
                }
            }.start();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void onEventMainThread(final MqttBindSuccessEvent event) {
        Log.d(TAG, "onEventMainThread:           MqttBindSuccessEvent");
        if (isDeviceRegistered) {
            if (currentProgress < 100) {
                ivAddingProgressStep3.setImageResource(R.drawable.ic_done);
                mCountDownTimer.cancel();
                int dvalue = 100 - currentProgress;
                mCountDownTimer = new CountDownTimer(1000, 1000 / dvalue) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (rpbDeviceAddingProgressbar != null) {
                            currentProgress++;
                            rpbDeviceAddingProgressbar.setProgress(currentProgress);
                        }
                    }

                    @Override
                    public void onFinish() {
                        if (rpbDeviceAddingProgressbar != null) {
                            rpbDeviceAddingProgressbar.setProgress(100);
                        }
                        isDeviceAdded = true;
                        showSuccessDialog(event.getDevId(), event.getName());
                    }
                }.start();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void onEventMainThread(final MqttAlreadyBindEvent event) {
        Log.d(TAG, "onEventMainThread:           MqttAlreadyBindEvent");
        mCountDownTimer.cancel();
        showAlreadyBindDialog(event.getEmail(), event.getDevName());
    }

//    private static final String TAG = "SmartconfigActivity";
//    @BindView(R.id.tv_ssid)
//    TextView tvSsid;
//    @BindView(R.id.et_pwd)
//    EditText etPwd;
//    @BindView(R.id.tv_config)
//    TextView buttonConfig;
//    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.iv_title_left)
//    ImageView ivTitleLeft;
//    @BindView(R.id.ll_title_left)
//    LinearLayout llTitleLeft;
//    @BindView(R.id.iv_title_right)
//    ImageView ivTitleRight;
//    @BindView(R.id.tv_title_right)
//    TextView tvTitleRight;
//    @BindView(R.id.ll_title_right)
//    LinearLayout llTitleRight;
//
//    private EspWifiAdminSimple mWifiAdmin;
//    private IEsptouchTask mEsptouchTask;
//    private ProgressDialog mProgressDialog;
//    private final Object mLock = new Object();
//    private int productType;
//    private boolean MQTT_BIND_SUCCESS = false;
//
//    private Handler mHandler = new Handler();
//    private Runnable mRunnable = new Runnable() {
//        @Override
//        public void run() {
//
//            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
//                    .setEnabled(true);
//            mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(
//                    "Confirm");
//            mProgressDialog.setMessage("Config failed.");
//        }
//    };
//
//
//    @Override
//    protected int getContentViewId() {
//        return R.layout.act_smartconfig;
//    }
//
//    @Override
//    protected void initViews() {
//        tvTitle.setText("Smart Config");
//        mWifiAdmin = new EspWifiAdminSimple(this);
//
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EventBus.getDefault().register(mContext);
////        productType=getIntent().getIntExtra("productType",-1);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(mContext);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        String apSsid = mWifiAdmin.getWifiConnectedSsid();
//        if (apSsid != null) {
//            tvSsid.setText(apSsid);
//        } else {
//            tvSsid.setText("");
//        }
//        // check whether the wifi is connected
//        boolean isApSsidEmpty = TextUtils.isEmpty(apSsid);
//        buttonConfig.setEnabled(!isApSsidEmpty);
//        MQTT_BIND_SUCCESS = false;
//    }
//
//    @Override
//    protected void initEvents() {
//        ivTitleLeft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//    }
//
//    @Override
//    protected void initDatas() {
//
//    }
//
//    @OnClick(R.id.tv_config)
//    public void onViewClicked() {
//        mProgressDialog = new ProgressDialog(mContext);
//        mProgressDialog.setMessage("EHome is configuring, please wait for a moment...");
//        mProgressDialog.setCanceledOnTouchOutside(false);
//        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                synchronized (mLock) {
//                    if (__IEsptouchTask.DEBUG) {
//                        Log.i(TAG, "progress dialog is canceled");
//                    }
//                    if (mEsptouchTask != null) {
//                        mEsptouchTask.interrupt();
//                    }
//                }
//            }
//        });
//        mProgressDialog.setButton(DialogInterface.BUTTON_POSITIVE,
//                "Waiting...", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                    }
//                });
//        mProgressDialog.show();
//        mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
//                .setEnabled(false);
//        mProgressDialog.setCancelable(true);
//
//        EHomeInterface.getINSTANCE().getNetToken(mContext, new BaseStringCallback() {
//            @Override
//            public void onSuccess(Response<BaseModelResponse<String>> response) {
//                if (response.body().isSuccess()) {
//                    String apSsid = mWifiAdmin.getWifiConnectedSsid();
//                    String apPassword = etPwd.getText().toString().trim();
//                    String apBssid = mWifiAdmin.getWifiConnectedBssid();
//                    new WhatieAsyncTask().execute(apSsid, apBssid, response.body().getValue() + apPassword, "1");
//                } else {
//                    mProgressDialog.setMessage(response.body().getMessage());
//                    mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
//                            .setEnabled(true);
//                    mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(
//                            "Confirm");
//                }
//            }
//
//            @Override
//            public void onError(Response<BaseModelResponse<String>> response) {
//                super.onError(response);
//                mProgressDialog.setMessage(response.body().getMessage());
//                mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
//                        .setEnabled(true);
//                mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(
//                        "Confirm");
//            }
//        });
//
//
//    }
//
//    private class WhatieAsyncTask extends AsyncTask<String, Void, List<IEsptouchResult>> {
//
//        @Override
//        protected void onPreExecute() {
//        }
//
//        @Override
//        protected List<IEsptouchResult> doInBackground(String... params) {
//            int taskResultCount = -1;
//            synchronized (mLock) {
//                String apSsid = mWifiAdmin.getWifiConnectedSsidAscii(params[0]);
//                String apBssid = params[1];
//                String apPassword = params[2];
//                String taskResultCountStr = params[3];
//                taskResultCount = Integer.parseInt(taskResultCountStr);
//                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, mContext);
//            }
//            List<IEsptouchResult> resultList = mEsptouchTask.executeForResults(taskResultCount);
//            return resultList;
//        }
//
//        @Override
//        protected void onPostExecute(List<IEsptouchResult> result) {
//            Log.d(TAG, "onPostExecute: Config failed.");
//            IEsptouchResult firstResult = result.get(0);
//            if (!firstResult.isCancelled()) {
//                if (firstResult.isSuc()) {
//                    mHandler.postDelayed(mRunnable, 30000);
//                } else {
//                    mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
//                            .setEnabled(true);
//                    mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(
//                            "Confirm");
//                    mProgressDialog.setMessage("Config failed.");
//
//                }
//            }
//        }
//    }
//
////    @Override
////    public void onBackPressed() {
////
////
////
////
////    }
//
//
//    @Override
//    public void onBackPressed() {
//        if (mProgressDialog != null) {
//            mProgressDialog.dismiss();
//            mProgressDialog = null;
//            if (mEsptouchTask != null) {
//                mEsptouchTask.interrupt();
//            }
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
//    public void onEventMainThread(MqttAlreadyBindEvent event) {
//        mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE)
//                .setEnabled(true);
//        mProgressDialog.getButton(DialogInterface.BUTTON_POSITIVE).setText(
//                "Confirm");
//        mProgressDialog.setMessage("The device has been bound by " + event.getEmail());
//    }
//
//
//    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
//    public void onEventMainThread(final MqttBindSuccessEvent event) {
//        Log.d(TAG, "onEventMainThread: MqttBindSuccessEvent");
//        if (!MQTT_BIND_SUCCESS) {
//            mHandler.removeCallbacks(mRunnable);
//            mProgressDialog.dismiss();
//            EHomeInterface.getINSTANCE().getStarted(mContext, event.getDevId(), event.getName(),
//                    new BaseCallback() {
//                        @Override
//                        public void onSuccess(Response<BaseResponse> response) {
//                            if (response.body().isSuccess()) {
//                                Toast.makeText(mContext, "Device initialize success.", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(SmartconfigActivity.this, MainActivity.class));
//                            } else {
//                                Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                            mProgressDialog.dismiss();
//                        }
//
//                        @Override
//                        public void onError(Response<BaseResponse> response) {
//                            super.onError(response);
//                            Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                            mProgressDialog.dismiss();
//                        }
//                    });
////        Toast.makeText(mContext, "Config success.", Toast.LENGTH_SHORT).show();
//            MQTT_BIND_SUCCESS = true;
//        }
//
//    }


}
