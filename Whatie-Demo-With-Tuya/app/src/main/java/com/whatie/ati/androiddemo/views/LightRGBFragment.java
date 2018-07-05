package com.whatie.ati.androiddemo.views;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;

import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.widget.CircleImageView;
import com.whatie.ati.androiddemo.widget.ColorBar;
import com.d9lab.ati.whatiesdk.bean.DeviceVo;
import com.d9lab.ati.whatiesdk.bean.LightDetail;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;

import butterknife.BindView;

/**
 * Created by 神火 on 2018/6/15.
 */

public class LightRGBFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "LightRGBFragment";

    @BindView(R.id.cb_rgbl_color)
    ColorBar cbRgblColor;
    @BindView(R.id.sb_light)
    SeekBar sbLight;
    @BindView(R.id.civ_color_preview)
    CircleImageView civColorPreview;

    private static final String ARGS_TAG = "lightDetail";

    private boolean isTracking = false;
    private boolean sendAvailable = true;

    private int[] rgbValue = {255, 0, 0};
    private int lValue = 50;

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

    public static LightRGBFragment newInstance() {
        return new LightRGBFragment();
    }

    public static LightRGBFragment newInstance(LightDetail detail) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_TAG, detail);
        LightRGBFragment fragment = new LightRGBFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.frg_light_rgbl;
    }


    @Override
    protected void initViews() {
        cbRgblColor.setOnSeekBarChangeListener(this);
        sbLight.setOnSeekBarChangeListener(this);
        sbLight.setProgress(lValue - 1);
        civColorPreview.setImageDrawable(new ColorDrawable(Color.rgb(rgbValue[0], rgbValue[1], rgbValue[2])));
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
        if (getArguments() != null) {
            LightDetail detail = (LightDetail) getArguments().getSerializable(ARGS_TAG);
            if (detail != null) {
                setDetails(detail.getRgb(), detail.getL());
            }
        }
    }

    private void sendRGBLInst(String devId, int rValue, int gValue, int bValue, int lValue) {
        int[] rgb = {rValue,gValue,bValue};
        EHomeInterface.getINSTANCE().updateLightRGBL(devId, rgb,lValue);
        mHandler.removeCallbacks(ctrabRunnable);
        ctrlable = false;
        mHandler.postDelayed(ctrabRunnable, 3000);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.cb_rgbl_color:
                rgbValue = cbRgblColor.getRGBValue();
                civColorPreview.setImageDrawable(new ColorDrawable(Color.rgb(rgbValue[0], rgbValue[1], rgbValue[2])));
                if(sendAvailable && isTracking) {
                    sendRGBLInst(mDeviceVo.getDevice().getDevId(), rgbValue[0], rgbValue[1], rgbValue[2], lValue);
                    sendAvailable = false;
                    mHandler.postDelayed(sbRunnable, 300);
                }
                break;
            case R.id.sb_light:
                lValue = i + 1;
                if(sendAvailable && isTracking) {
                    sendRGBLInst(mDeviceVo.getDevice().getDevId(), rgbValue[0], rgbValue[1], rgbValue[2], lValue);
                    sendAvailable = false;
                    mHandler.postDelayed(sbRunnable, 300);
                }
                break;
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
        sendRGBLInst(mDeviceVo.getDevice().getDevId(), rgbValue[0], rgbValue[1], rgbValue[2], lValue);
    }

    public void setDetails(int[] rgb, int l) {
        if(!isTracking && ctrlable) {
            rgbValue = rgb;
            lValue = l;
            cbRgblColor.setProgress(rgb);
            sbLight.setProgress(l - 1);
        }
    }

}
