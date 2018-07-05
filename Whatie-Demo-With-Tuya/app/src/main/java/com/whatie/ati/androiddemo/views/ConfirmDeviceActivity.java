package com.whatie.ati.androiddemo.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.database.db.WifiDaoOpe;
import com.whatie.ati.androiddemo.utils.NetworkUtils;
import com.d9lab.ati.whatiesdk.bean.BaseModelResponse;
import com.d9lab.ati.whatiesdk.callback.BaseStringCallback;
import com.d9lab.ati.whatiesdk.ehome.EHome;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.d9lab.ati.whatiesdk.smartconfig.EspWifiAdminSimple;
import com.d9lab.ati.whatiesdk.util.Code;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by liz on 2018/3/15.
 */

public class ConfirmDeviceActivity extends BaseActivity {

    private static final String TAG = "ConfirmDeviceActivity";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_left)
    ImageView ivTitleLeft;
    //    @BindView(R.id.tv_title_left)
//    TextView tvTitleLeft;
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.iv_title_right)
    ImageView ivTitleRight;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.ll_title_right)
    LinearLayout llTitleRight;
    @BindView(R.id.tv_adding_device_name)
    TextView tvAddingDeviceName;
    @BindView(R.id.tv_adding_device_next)
    TextView tvAddingDeviceNext;
    @BindView(R.id.iv_confirm_network_gif)
    ImageView ivConfirmNetworkGif;

    private PopupWindow selectWifiWindow;
    private View selectWifiView;
    private PopupWindow confirmWifiWindow;
    private View confirmWifiView;
    private EspWifiAdminSimple mWifiAdmin;
    private NetworkUtils netWorkConnectedUtil;

    private String ssid;
    private String password;
    private String ip;
    private String token;

    @Override
    protected void onResume() {
        super.onResume();
        if (selectWifiWindow != null) selectWifiWindow.dismiss();
        if (confirmWifiWindow != null) confirmWifiWindow.dismiss();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_confirm_device;
    }


    @Override
    protected void initViews() {
        tvTitle.setText(getString(R.string.confirm_device_title));
        tvAddingDeviceName.setText(getIntent().getStringExtra(Code.PRODUCT_NAME));
        Glide.with(mContext)
                .load(R.drawable.ic_confirm_network)
                .into(ivConfirmNetworkGif);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initDatas() {

    }

    @OnClick({R.id.ll_title_left, R.id.tv_adding_device_name, R.id.tv_adding_device_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.tv_adding_device_name:

                break;
            case R.id.tv_adding_device_next:
                if (EHome.getInstance().isLogin()) {
                    if (netWorkConnectedUtil.isWifiConnected(mContext)) {
                        showConfirmWifiWindow();
                        //6.0 以下无法判断，scanResults为空列表
//                        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//                        String tempSsidString = wifiInfo.getSSID();
//                        if (tempSsidString != null && tempSsidString.length() > 2) {
//                            String wifiSsid = tempSsidString.substring(1, tempSsidString.length() - 1);
//                            List<ScanResult> scanResults=wifiManager.getScanResults();
//                            LogUtil.log(TAG, scanResults.size()+"");
//                            for(ScanResult scanResult:scanResults){
//                                LogUtil.log(TAG, "scanResult.SSID:" +scanResult.SSID +"   wifiSsid:"+wifiSsid);
//                                if(scanResult.SSID.equals(wifiSsid)){
//                                    if(scanResult.frequency > 2400 && scanResult.frequency < 2500){
//                                        showConfirmWifiWindow();
//                                    }else {
//                                        showGotoSelectWindow();
//                                    }
//                                    break;
//                                }
//                            }
//                        }
                    } else {
                        showGotoSelectWindow();
                    }
                } else {
                    startActivity(new Intent(ConfirmDeviceActivity.this, LoginActivity.class));
                }
                break;
        }
    }

    private void showGotoSelectWindow() {
        if (selectWifiWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            selectWifiView = layoutInflater.inflate(R.layout.pop_connect_wifi, null);
            selectWifiWindow = new PopupWindow(selectWifiView, RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
        }
        setBackgroundAlpha(0.5f);
        selectWifiWindow.setAnimationStyle(R.style.PopupWindowAnimationFromTop);
        selectWifiWindow.setFocusable(true);
        selectWifiWindow.setOutsideTouchable(true);
        selectWifiWindow.showAtLocation(ConfirmDeviceActivity.this.findViewById(R.id.ll_confirm_wifi),
                Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
        selectWifiWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                selectWifiWindow.dismiss();
                setBackgroundAlpha(1.0f);
            }
        });
        selectWifiWindow.setTouchInterceptor(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        TextView gotoSelectWifi = (TextView) selectWifiView.findViewById(R.id.tv_goto_select_wifi);
        gotoSelectWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)); //直接进入手机中的wifi网络设置界面
            }
        });
    }

    private void showConfirmWifiWindow() {
        if (confirmWifiWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            confirmWifiView = layoutInflater.inflate(R.layout.pop_input_wifi_password, null);
            confirmWifiWindow = new PopupWindow(confirmWifiView, RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
        }
        setBackgroundAlpha(0.5f);
        confirmWifiWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        confirmWifiWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        confirmWifiWindow.setFocusable(true);
        confirmWifiWindow.setOutsideTouchable(true);
        confirmWifiWindow.showAtLocation(ConfirmDeviceActivity.this.findViewById(R.id.ll_confirm_wifi),
                Gravity.CENTER, 0, 0);
        confirmWifiWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                confirmWifiWindow.dismiss();
                setBackgroundAlpha(1.0f);
            }
        });
        confirmWifiWindow.setTouchInterceptor(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        mWifiAdmin = new EspWifiAdminSimple(mContext);
        final TextView tvCancelConfirmWifi = (TextView) confirmWifiView.findViewById(R.id.tv_cancel_confirm_wifi);
        TextView tvNextConfirmWifi = (TextView) confirmWifiView.findViewById(R.id.tv_next_confirm_wifi);
        TextView tvCurrentWifi = (TextView) confirmWifiView.findViewById(R.id.tv_current_wifi_name);
        TextView tvChangeWifi = (TextView) confirmWifiView.findViewById(R.id.tv_change_wifi);
        ssid = mWifiAdmin.getWifiConnectedSsid();
        Log.d(TAG, "showConfirmWifiWindow: ssid = " + ssid);
        tvCurrentWifi.setText(ssid);
        final EditText etPassword = (EditText) confirmWifiView.findViewById(R.id.et_wifi_password);
        if (WifiDaoOpe.queryWifiByName(mContext, ssid) != null) {
            password = WifiDaoOpe.queryWifiByName(mContext, ssid).getPassword();
            etPassword.setText(password);
        }

        tvCancelConfirmWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmWifiWindow.dismiss();
                setBackgroundAlpha(1.0f);
            }
        });
        tvNextConfirmWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoadingDialog.show();
                getNetToken(etPassword.getText().toString().trim());
            }
        });
        tvChangeWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmWifiWindow.dismiss();
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
    }

    private void setBackgroundAlpha(float f) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = f;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    private void getNetToken(final String password) {
        EHomeInterface.getINSTANCE().getNetToken(mContext, new BaseStringCallback() {
            @Override
            public void onSuccess(Response<BaseModelResponse<String>> response) {
                if (response.body().isSuccess()) {
                    confirmWifiWindow.dismiss();
                    mLoadingDialog.dismiss();
                    Intent addingIntent = new Intent(ConfirmDeviceActivity.this, SmartconfigActivity.class);
                    addingIntent.putExtra(Code.NET_TOKEN, response.body().getValue());
                    addingIntent.putExtra(Code.PASSWORD, password);
                    startActivity(addingIntent);
                } else {
                    mLoadingDialog.dismiss();
                    Toast.makeText(mContext,response.body().getMessage(),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onError(Response<BaseModelResponse<String>> response) {
                super.onError(response);
                mLoadingDialog.dismiss();
                Toast.makeText(mContext,getString(R.string.toast_network_error),Toast.LENGTH_SHORT).show();

            }

        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
