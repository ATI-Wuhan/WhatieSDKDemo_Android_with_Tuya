package com.whatie.ati.androiddemo.views;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.d9lab.ati.whatiesdk.bean.BaseResponse;
import com.d9lab.ati.whatiesdk.bean.DeviceVo;
import com.d9lab.ati.whatiesdk.bean.LightDetail;
import com.d9lab.ati.whatiesdk.callback.BaseCallback;
import com.d9lab.ati.whatiesdk.ehome.EHome;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.d9lab.ati.whatiesdk.event.DeviceStatusNotifyEvent;
import com.d9lab.ati.whatiesdk.util.Code;
import com.d9lab.ati.whatiesdk.util.ParseUtil;
import com.lzy.okgo.model.Response;
import com.whatie.ati.androiddemo.R;

import com.whatie.ati.androiddemo.utils.ToastUtil;
import com.whatie.ati.androiddemo.views.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by 神火 on 2018/6/15.
 */

public class LightDetailActivity extends BaseActivity {

    private String TAG = "LightDetailActivity";
    @BindView(R.id.iv_light_rgbl)
    ImageView ivLightRGBL;
    @BindView(R.id.iv_light_flow)
    ImageView ivLightFlow;
    @BindView(R.id.iv_light_white)
    ImageView ivLightWhite;
    @BindView(R.id.tv_light_rgbl)
    TextView tvLightRGBL;
    @BindView(R.id.tv_light_flow)
    TextView tvLightFlow;
    @BindView(R.id.tv_light_white)
    TextView tvLightWhite;
    @BindView(R.id.btn_light_detail_power)
    Button btnLightPower;
    @BindView(R.id.ll_rgbl_button)
    LinearLayout llRGBLButton;
    @BindView(R.id.ll_flow_button)
    LinearLayout llFlowButton;
    @BindView(R.id.ll_white_button)
    LinearLayout llWhiteButton;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_left)
    ImageView ivTitleLeft;
    @BindView(R.id.iv_title_right)
    ImageView ivTitleRight;
    @BindView(R.id.tv_rom_version)
    TextView tvRomVersion;
    @BindView(R.id.ll_navigation_tab)
    LinearLayout llNavigationTab;

    private View viewShelter;

    private Fragment mCurrentFragment = null;
    FragmentManager mFragmentManager = getSupportFragmentManager();
    private LightRGBFragment mLightRGBFragment;
    private LightFlowFragment mLightFlowFragment;
    private LightWhiteFragment mLightWhiteFragment;
    private MaterialDialog mConfirmExitDialog;
    private boolean powerState = false;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            btnLightPower.setClickable(true);
        }
    };

    private static DeviceVo mDeviceVo;
    private HashMap<String, String> functionValuesMap;

    @Override
    protected int getContentViewId() {
        return R.layout.act_light_detail;

    }

    @Override
    protected void initViews() {
        setStatusBarColor(R.color.white);
        setTitleBarColor(R.color.white);
        viewShelter = findViewById(R.id.view_shelter);
        tvTitle.setTextColor(getResources().getColor(R.color.light_title_text_black));
        tvTitle.setText(R.string.light_detail_title);
        ivTitleLeft.setImageResource(R.drawable.ic_title_back_black);
        ivTitleRight.setVisibility(View.VISIBLE);
        ivTitleRight.setImageResource(R.drawable.ic_light_timer);
        ivLightRGBL.setImageResource(R.drawable.ic_light_rgbl);
        ivLightFlow.setImageResource(R.drawable.ic_light_flow);

    }

    @Override
    protected void initEvents() {
        EventBus.getDefault().register(mContext);
    }

    @Override
    protected void initDatas() {
        mDeviceVo = (DeviceVo) getIntent().getSerializableExtra(Code.DEVICE);
//        if (mDeviceVo.isHost()) {
//            ivTitleRight.setImageResource(R.drawable.ic_device_setting);
//        } else {
//            ivTitleRight.setImageResource(R.drawable.ic_device_delete);
//        }
        if (mDeviceVo.getProductName().equals(Code.PRODUCT_TYPE_MONOLIGHT)) {
            llNavigationTab.setVisibility(View.GONE);
        }
        if(mDeviceVo.getDevice().getHardwareVersion() != null) {
            tvRomVersion.setText(mDeviceVo.getDevice().getHardwareVersion().getVersion());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EHomeInterface.getINSTANCE().reSubscribeDeviceTopic(mDeviceVo.getDevice().getDevId());

    }


    @OnClick({R.id.ll_rgbl_button, R.id.ll_flow_button, R.id.ll_white_button, R.id.ll_title_left, R.id.btn_light_detail_power, R.id.ll_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_rgbl_button:
                showRGBLControl();
                break;
            case R.id.ll_flow_button:
                showFlowControl();
                break;
            case R.id.ll_white_button:
                showWhiteControl();
                break;
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.ll_title_right:
                if (EHome.getInstance().isMqttOn()) {
                    Intent alarmIntent = new Intent(this, TimerListActivity.class);
                    alarmIntent.putExtra(Code.DEVICE, mDeviceVo);
                    startActivity(alarmIntent);
                } else {
                    ToastUtil.showShort(mContext, R.string.device_detail_cannot_set_alarm);
                }
                break;
            case R.id.btn_light_detail_power:
                EHomeInterface.getINSTANCE().updateLightPower(mDeviceVo.getDevice().getDevId(),!powerState);
                break;
        }
    }



    private void showRGBLControl() {
        if(mLightRGBFragment == null){
            mLightRGBFragment = LightRGBFragment.newInstance();
        }
        addOrShowFragment(mLightRGBFragment);
        rgblSelected();
    }

    private void showRGBLControl(LightDetail detail) {
        if(mLightRGBFragment == null){
            mLightRGBFragment = LightRGBFragment.newInstance(detail);
        } else {
            mLightRGBFragment.setDetails(detail.getRgb(), detail.getL());
        }
        addOrShowFragment(mLightRGBFragment);
        rgblSelected();
    }

    private void showFlowControl() {
        if(mLightFlowFragment == null){
            mLightFlowFragment = LightFlowFragment.newInstance();
        }
        addOrShowFragment(mLightFlowFragment);
        flowSelected();
    }

    private void showFlowControl(LightDetail detail) {
        if(mLightFlowFragment == null){
            mLightFlowFragment = LightFlowFragment.newInstance(detail);
        } else {
            mLightFlowFragment.setDetails(detail.getRgb1(), detail.getRgb2(), detail.getRgb3(), detail.getRgb4(), detail.getL(), detail.getT());
        }
        addOrShowFragment(mLightFlowFragment);
        flowSelected();
    }

    private void showWhiteControl() {
        if(mLightWhiteFragment == null){
            mLightWhiteFragment = LightWhiteFragment.newInstance();
        }
        addOrShowFragment(mLightWhiteFragment);
        whiteSelected();
    }
    private void showWhiteControl(LightDetail detail) {
        if(mLightWhiteFragment == null){
            mLightWhiteFragment = LightWhiteFragment.newInstance(detail);
        } else {
            mLightWhiteFragment.setDetails(detail.getL());
        }
        addOrShowFragment(mLightWhiteFragment);
        whiteSelected();
    }

    private void rgblSelected() {
        tvLightRGBL.setTextColor(getResources().getColor(R.color.white));
        ivLightRGBL.setSelected(true);
        llRGBLButton.setSelected(true);
        tvLightFlow.setTextColor(getResources().getColor(R.color.main_text));
        ivLightFlow.setSelected(false);
        llFlowButton.setSelected(false);
        tvLightWhite.setTextColor(getResources().getColor(R.color.main_text));
        ivLightWhite.setSelected(false);
        llWhiteButton.setSelected(false);
    }

    private void flowSelected() {
        tvLightRGBL.setTextColor(getResources().getColor(R.color.main_text));
        ivLightRGBL.setSelected(false);
        llRGBLButton.setSelected(false);
        tvLightFlow.setTextColor(getResources().getColor(R.color.white));
        ivLightFlow.setSelected(true);
        llFlowButton.setSelected(true);
        tvLightWhite.setTextColor(getResources().getColor(R.color.main_text));
        ivLightWhite.setSelected(false);
        llWhiteButton.setSelected(false);
    }

    private void whiteSelected() {
        tvLightRGBL.setTextColor(getResources().getColor(R.color.main_text));
        ivLightRGBL.setSelected(false);
        llRGBLButton.setSelected(false);
        tvLightFlow.setTextColor(getResources().getColor(R.color.main_text));
        ivLightFlow.setSelected(false);
        llFlowButton.setSelected(false);
        tvLightWhite.setTextColor(getResources().getColor(R.color.white));
        ivLightWhite.setSelected(true);
        llWhiteButton.setSelected(true);
    }

    private void addOrShowFragment(android.support.v4.app.Fragment fragment) {

        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (mCurrentFragment != null) {
            if (mCurrentFragment == fragment) return;
            if (!fragment.isAdded()) {
                // 如果当前fragment未被添加，则添加到Fragment管理器中
                transaction.hide(mCurrentFragment)
                        .add(R.id.fl_content, fragment)
                        .commitAllowingStateLoss();
            } else {
                transaction.hide(mCurrentFragment).show(fragment).commitAllowingStateLoss();
            }
        } else {
            if (!fragment.isAdded()) {
                mFragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .add(R.id.fl_content, fragment).commitAllowingStateLoss();
            } else {
                mFragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .hide(mCurrentFragment).show(fragment).commitAllowingStateLoss();
            }
        }
        mCurrentFragment = fragment;
    }

    private void toggleState(boolean state) {
        Log.d(TAG, "toggleState() " + state);
        if (state) {
            btnLightPower.setBackground(getResources().getDrawable(R.drawable.bg_light_power_close));
            btnLightPower.setText(getString(R.string.device_switch_close));
            viewShelter.setVisibility(View.GONE);
            powerState = true;
        } else {
            btnLightPower.setBackground(getResources().getDrawable(R.drawable.bg_light_power_open));
            btnLightPower.setText(getString(R.string.device_switch_open));
            viewShelter.setVisibility(View.VISIBLE);
            viewShelter.setOnClickListener(null);
            powerState = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
    }


    private void showExitConfirmDialog() {
        if (mConfirmExitDialog != null) {
            mConfirmExitDialog = null;
        }
        mConfirmExitDialog = new MaterialDialog(mContext);
        mConfirmExitDialog.setTitle(getString(R.string.device_delete_title))
                .setMessage(getString(R.string.device_delete_content))
                .setPositiveButton(getString(R.string.confirm_device_title), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EHomeInterface.getINSTANCE().removeDevice(mContext, mDeviceVo.getDevice().getId(),
                                new BaseCallback() {
                                    @Override
                                    public void onSuccess(Response<BaseResponse> response) {
                                        if (response.body().isSuccess()) {
                                            EHome.getInstance().removeDevice(mDeviceVo.getDevice().getDevId());

                                            startActivity(new Intent(LightDetailActivity.this, MainActivity.class));
                                        } else {
                                            if (response.body() != null) {
                                                ToastUtil.showShort(mContext, ToastUtil.codeToStringId(response.body().getCode()));
                                            } else {
                                                ToastUtil.showShort(mContext, getString(R.string.network_error) + response.code());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(Response<BaseResponse> response) {
                                        super.onError(response);
                                        if (response.body() != null) {
                                            ToastUtil.showShort(mContext, ToastUtil.codeToStringId(response.body().getCode()));
                                        } else {
                                            ToastUtil.showShort(mContext, getString(R.string.network_error) + response.code());
                                        }
                                    }
                                });
                    }
                })
                .setNegativeButton(getString(R.string.download_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
    }


    public static DeviceVo getItem() {
        return mDeviceVo;
    }


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void onEventMainThread(DeviceStatusNotifyEvent event) {
        if(event.getmDeviceVo().getDevice().getDevId().equals(mDeviceVo.getDevice().getDevId())) {
            mDeviceVo = event.getmDeviceVo();
            mHandler.removeCallbacks(mRunnable);
            btnLightPower.setClickable(true);
            functionValuesMap = mDeviceVo.getFunctionValuesMap();
            Log.d(TAG, "onEventMainThread:  " + EHome.getInstance().getmDeviceVos().get(event.getIndex()));
            Log.d(TAG, "onEventMainThread:  " + functionValuesMap.toString());
            toggleState(Boolean.parseBoolean(functionValuesMap.get(Code.FUNCTION_MAP_LOCAL_POWER)));
            switch (Integer.parseInt(functionValuesMap.get(Code.FUNCTION_MAP_LOCAL_LIGHTMODE))){
                case Code.LIGHT_MODE_FLOW:
                    Log.d(TAG, "onEventMainThread: MqttReceiveLightModeFlowEvent");
                    toggleState(true);
                    int[] rgb1 = ParseUtil.rgbStringToInts(functionValuesMap.get(Code.FUNCTION_MAP_LOCAL_RGB1));
                    int[] rgb2 = ParseUtil.rgbStringToInts(functionValuesMap.get(Code.FUNCTION_MAP_LOCAL_RGB2));
                    int[] rgb3 = ParseUtil.rgbStringToInts(functionValuesMap.get(Code.FUNCTION_MAP_LOCAL_RGB3));
                    int[] rgb4 = ParseUtil.rgbStringToInts(functionValuesMap.get(Code.FUNCTION_MAP_LOCAL_RGB4));
                    int t = Integer.parseInt(functionValuesMap.get(Code.FUNCTION_MAP_LOCAL_T));
                    int l = Integer.parseInt(functionValuesMap.get(Code.FUNCTION_MAP_LOCAL_L));
                    showFlowControl(new LightDetail(rgb1, rgb2, rgb3, rgb4, t, l));
                    break;
                case Code.LIGHT_MODE_L:
                    Log.d(TAG, "onEventMainThread: MqttReceiveLightModeLEvent");
                    toggleState(true);
                    int wl = Integer.parseInt(functionValuesMap.get(Code.FUNCTION_MAP_LOCAL_L));
                    showWhiteControl(new LightDetail(wl));
                    break;
                case Code.LIGHT_MODE_RGBL:
                    Log.d(TAG, "onEventMainThread: MqttReceiveLightModeRGBLEvent");
                    toggleState(true);
                    int[] rgb = ParseUtil.rgbStringToInts(functionValuesMap.get(Code.FUNCTION_MAP_LOCAL_RGB));
                    int lValue = Integer.parseInt(functionValuesMap.get(Code.FUNCTION_MAP_LOCAL_L));
                    showRGBLControl(new LightDetail(rgb, lValue));
                    break;
            }
        }
    }


}
