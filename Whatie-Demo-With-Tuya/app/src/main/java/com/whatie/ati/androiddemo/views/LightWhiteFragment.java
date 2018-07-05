package com.whatie.ati.androiddemo.views;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;

import com.whatie.ati.androiddemo.R;
import com.d9lab.ati.whatiesdk.bean.DeviceVo;
import com.d9lab.ati.whatiesdk.bean.LightDetail;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;

import butterknife.BindView;

/**
 * Created by 神火 on 2018/6/15.
 */

public class LightWhiteFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "LightFlowFragment";

    @BindView(R.id.sb_white_light)
    SeekBar sbWhiteLight;

    private boolean isTracking = false;
    private boolean sendAvailable = true;

    private int lValue = 50;

    private static final String ARGS_TAG = "lightDetail";

    private Handler mHandler = new Handler();
    private Runnable sbRunnable = new Runnable() {
        @Override
        public void run() {
            sendAvailable = true;
        }
    };

    private boolean ctrlable = true;
    // 让seekbar在一段时间内不会被反向控制
    private Runnable ctrabRunnable = new Runnable() {
        @Override
        public void run() {
            ctrlable = true;
        }
    };
    private DeviceVo mDeviceVo;

    public static LightWhiteFragment newInstance() {
        return new LightWhiteFragment();
    }

    public static LightWhiteFragment newInstance(LightDetail detail) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_TAG, detail);
        LightWhiteFragment fragment = new LightWhiteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.frg_light_white;
    }

    @Override
    protected void initViews() {
        sbWhiteLight.setOnSeekBarChangeListener(this);
        sbWhiteLight.setProgress(lValue - 1);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initDatas() {
        mDeviceVo = LightDetailActivity.getItem();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if(getArguments() != null){
            LightDetail detail = (LightDetail) getArguments().getSerializable(ARGS_TAG);
            if(detail != null){
                setDetails(detail.getL());
            }
        }
    }

    private void sendLightInst(String devId, int lValue){
        EHomeInterface.getINSTANCE().updateLightBrightness(devId,lValue);
        mHandler.removeCallbacks(ctrabRunnable);
        ctrlable = false;
        mHandler.postDelayed(ctrabRunnable, 3000);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        lValue = i + 1;
        if(sendAvailable && isTracking) {
            sendLightInst(mDeviceVo.getDevice().getDevId(), lValue);
            sendAvailable = false;
            mHandler.postDelayed(sbRunnable, 300);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isTracking = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        isTracking = false;
        mHandler.removeCallbacks(sbRunnable);
        sendAvailable = true;
        sendLightInst(mDeviceVo.getDevice().getDevId(), lValue);
    }

    public void setDetails(int l){
        if(!isTracking && ctrlable) {
            lValue = l;
            sbWhiteLight.setProgress(l - 1);
        }
    }

}
