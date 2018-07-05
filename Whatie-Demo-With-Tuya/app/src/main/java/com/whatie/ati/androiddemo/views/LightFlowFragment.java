package com.whatie.ati.androiddemo.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.whatie.ati.androiddemo.R;
import com.d9lab.ati.whatiesdk.bean.DeviceVo;
import com.d9lab.ati.whatiesdk.bean.LightDetail;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.d9lab.ati.whatiesdk.util.Code;

import butterknife.BindView;

/**
 * Created by 神火 on 2018/6/15.
 */

public class LightFlowFragment extends BaseFragment implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private static final String TAG = "LightFlowFragment";
    @BindView(R.id.sb_time)
    SeekBar sbTime;
    @BindView(R.id.sb_light)
    SeekBar sbLight;
    @BindView(R.id.btn_flow_to_detail)
    Button btnFlowToDetail;

    private static final String ARGS_TAG = "lightDetail";

    private int[] rgb1 = {255, 0, 0};
    private int[] rgb2 = {90, 255, 0};
    private int[] rgb3 = {0, 170, 255};
    private int[] rgb4 = {255, 0, 255};

    private int lValue = 50;
    private int tValue = 3000;

    private boolean isTracking = false;
    private boolean sendAvailable = true;

    private Handler mHandler = new Handler();
    private Runnable sbRunnable = new Runnable() {
        @Override
        public void run() {
            sendAvailable = true;
        }
    };

    private DeviceVo mDeviceVo;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.frg_light_flow;
    }

    public static LightFlowFragment newInstance() {
        return new LightFlowFragment();
    }

    public static LightFlowFragment newInstance(LightDetail detail) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_TAG, detail);
        LightFlowFragment fragment = new LightFlowFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initViews() {
        sbLight.setOnSeekBarChangeListener(this);
        sbLight.setProgress(lValue - 1);
        sbTime.setOnSeekBarChangeListener(this);
        sbTime.setProgress(10 - tValue / 1000);
        btnFlowToDetail.setOnClickListener(this);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initDatas() {
        mDeviceVo = LightDetailActivity.getItem();
    }

    private void sendFlowInst(String devId, int[] rgb1, int[]  rgb2, int[]  rgb3, int[]  rgb4, int tValue, int lValue) {

        EHomeInterface.getINSTANCE().setLightFlow(devId, rgb1, rgb2, rgb3, rgb4, tValue, lValue);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if (getArguments() != null) {
            LightDetail detail = (LightDetail) getArguments().getSerializable(ARGS_TAG);
            if (detail != null) {
                setDetails(detail.getRgb1(), detail.getRgb2(), detail.getRgb3(), detail.getRgb4(), detail.getL(), detail.getT());
            }
        }
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (seekBar.getId()) {
            case R.id.sb_light:
                lValue = i + 1;
                break;
            case R.id.sb_time:
                tValue = (10 - i) * 1000;
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
        sendFlowInst(mDeviceVo.getDevice().getDevId(), rgb1, rgb2, rgb3, rgb4, tValue, lValue);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_flow_to_detail:
                Intent flowToDetail = new Intent(getActivity(), FlowColorDetailActivity.class);
                flowToDetail.putExtra(Code.DEVICE, mDeviceVo);
                flowToDetail.putExtra(Code.LIGHT_DPS_RGB1, rgb1);
                flowToDetail.putExtra(Code.LIGHT_DPS_RGB2, rgb2);
                flowToDetail.putExtra(Code.LIGHT_DPS_RGB3, rgb3);
                flowToDetail.putExtra(Code.LIGHT_DPS_RGB4, rgb4);
                flowToDetail.putExtra(Code.LIGHT_DPS_L, lValue);
                flowToDetail.putExtra(Code.LIGHT_DPS_T, tValue);
                startActivity(flowToDetail);
                break;
        }
    }

    public void setDetails(int[] rgb1, int[] rgb2, int[] rgb3, int[] rgb4, int l, int t){
        this.rgb1 = rgb1;
        this.rgb2 = rgb2;
        this.rgb3 = rgb3;
        this.rgb4 = rgb4;
        tValue = t;
        lValue = l;
        sbLight.setProgress(l - 1);
        sbTime.setProgress(10 - t / 1000);
    }
}
