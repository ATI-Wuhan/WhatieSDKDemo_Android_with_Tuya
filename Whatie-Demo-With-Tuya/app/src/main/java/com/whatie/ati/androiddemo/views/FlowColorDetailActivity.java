package com.whatie.ati.androiddemo.views;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.widget.CircleImageView;
import com.whatie.ati.androiddemo.widget.ColorBar;
import com.d9lab.ati.whatiesdk.bean.DeviceVo;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.d9lab.ati.whatiesdk.event.MqttReceiveLightModeEvent;
import com.d9lab.ati.whatiesdk.util.Code;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

public class FlowColorDetailActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.rl_title_bg)
    RelativeLayout rlTitleBg;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.iv_title_left)
    ImageView ivTitleLeft;
    @BindView(R.id.iv_title_right)
    ImageView ivTitleRight;
    @BindView(R.id.rl_color_edit1)
    RelativeLayout rlColorEdit1;
    @BindView(R.id.rl_color_edit2)
    RelativeLayout rlColorEdit2;
    @BindView(R.id.rl_color_edit3)
    RelativeLayout rlColorEdit3;
    @BindView(R.id.rl_color_edit4)
    RelativeLayout rlColorEdit4;
    @BindView(R.id.iv_color_edit_tag1)
    ImageView ivColorEditTag1;
    @BindView(R.id.iv_color_edit_tag2)
    ImageView ivColorEditTag2;
    @BindView(R.id.iv_color_edit_tag3)
    ImageView ivColorEditTag3;
    @BindView(R.id.iv_color_edit_tag4)
    ImageView ivColorEditTag4;
    @BindView(R.id.civ_flow_preview1)
    CircleImageView civFlowPreview1;
    @BindView(R.id.civ_flow_preview2)
    CircleImageView civFlowPreview2;
    @BindView(R.id.civ_flow_preview3)
    CircleImageView civFlowPreview3;
    @BindView(R.id.civ_flow_preview4)
    CircleImageView civFlowPreview4;
    @BindView(R.id.cb_flow_color)
    ColorBar cbFlowColor;
    @BindView(R.id.btn_flow_detail_apply)
    Button btnFlowDetailApply;

    private int[] rgb1 = {255, 0, 0};
    private int[] rgb2 = {90, 255, 0};
    private int[] rgb3 = {0, 170, 255};
    private int[] rgb4 = {255, 0, 255};
    private int lValue = 1;
    private int tValue = 10000;

    private int currentEdit = 1;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            btnFlowDetailApply.setClickable(true);
            Toast.makeText(mContext, R.string.set_flow_failed,Toast.LENGTH_SHORT).show();
        }
    };
    private DeviceVo mDeviceVo;

    @Override
    protected int getContentViewId() {
        return R.layout.act_flow_color_detail;
    }

    @Override
    protected void initViews() {
        rlTitleBg.setBackground(getResources().getDrawable(R.color.white));
        tvTitle.setTextColor(getResources().getColor(R.color.light_title_text_black));
        tvTitle.setText("");
        ivTitleLeft.setImageResource(R.drawable.ic_title_back_black);
        setColorEdit(1);
    }

    @Override
    protected void initEvents() {
        rlColorEdit1.setOnClickListener(this);
        rlColorEdit2.setOnClickListener(this);
        rlColorEdit3.setOnClickListener(this);
        rlColorEdit4.setOnClickListener(this);
        llTitleLeft.setOnClickListener(this);
        btnFlowDetailApply.setOnClickListener(this);
        cbFlowColor.setOnSeekBarChangeListener(this);
        EventBus.getDefault().register(mContext);
    }

    @Override
    protected void initDatas() {
        Intent intent = getIntent();
        mDeviceVo = (DeviceVo) getIntent().getSerializableExtra(Code.DEVICE);
        rgb1 = intent.getIntArrayExtra(Code.LIGHT_DPS_RGB1);
        rgb2 = intent.getIntArrayExtra(Code.LIGHT_DPS_RGB2);
        rgb3 = intent.getIntArrayExtra(Code.LIGHT_DPS_RGB3);
        rgb4 = intent.getIntArrayExtra(Code.LIGHT_DPS_RGB4);
        civFlowPreview1.setImageDrawable(new ColorDrawable(Color.rgb(rgb1[0], rgb1[1], rgb1[2])));
        civFlowPreview2.setImageDrawable(new ColorDrawable(Color.rgb(rgb2[0], rgb2[1], rgb2[2])));
        civFlowPreview3.setImageDrawable(new ColorDrawable(Color.rgb(rgb3[0], rgb3[1], rgb3[2])));
        civFlowPreview4.setImageDrawable(new ColorDrawable(Color.rgb(rgb4[0], rgb4[1], rgb4[2])));
        lValue = intent.getIntExtra(Code.LIGHT_DPS_L, 1);
        tValue = intent.getIntExtra(Code.LIGHT_DPS_T, 10000);
    }

    private void setColorEdit(int i) {
        currentEdit = i;
        ivColorEditTag1.setVisibility(View.INVISIBLE);
        ivColorEditTag2.setVisibility(View.INVISIBLE);
        ivColorEditTag3.setVisibility(View.INVISIBLE);
        ivColorEditTag4.setVisibility(View.INVISIBLE);
        switch (i) {
            case 1:
                ivColorEditTag1.setVisibility(View.VISIBLE);
                cbFlowColor.setProgress(rgb1);
                break;
            case 2:
                ivColorEditTag2.setVisibility(View.VISIBLE);
                cbFlowColor.setProgress(rgb2);
                break;
            case 3:
                ivColorEditTag3.setVisibility(View.VISIBLE);
                cbFlowColor.setProgress(rgb3);
                break;
            case 4:
                ivColorEditTag4.setVisibility(View.VISIBLE);
                cbFlowColor.setProgress(rgb4);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_color_edit1:
                setColorEdit(1);
                break;
            case R.id.rl_color_edit2:
                setColorEdit(2);
                break;
            case R.id.rl_color_edit3:
                setColorEdit(3);
                break;
            case R.id.rl_color_edit4:
                setColorEdit(4);
                break;
            case R.id.btn_flow_detail_apply:
                btnFlowDetailApply.setClickable(false);
                mHandler.postDelayed(mRunnable, 1500);
                sendFlowInst(mDeviceVo.getDevice().getDevId(), rgb1, rgb2, rgb3, rgb4, tValue, lValue);
                break;
            case R.id.ll_title_left:
                finish();
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        switch (currentEdit) {
            case 1:
                rgb1 = cbFlowColor.getRGBValue();
                civFlowPreview1.setImageDrawable(new ColorDrawable(Color.rgb(rgb1[0], rgb1[1], rgb1[2])));
                break;
            case 2:
                rgb2 = cbFlowColor.getRGBValue();
                civFlowPreview2.setImageDrawable(new ColorDrawable(Color.rgb(rgb2[0], rgb2[1], rgb2[2])));
                break;
            case 3:
                rgb3 = cbFlowColor.getRGBValue();
                civFlowPreview3.setImageDrawable(new ColorDrawable(Color.rgb(rgb3[0], rgb3[1], rgb3[2])));
                break;
            case 4:
                rgb4 = cbFlowColor.getRGBValue();
                civFlowPreview4.setImageDrawable(new ColorDrawable(Color.rgb(rgb4[0], rgb4[1], rgb4[2])));
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    private void sendFlowInst(String devId, int[] rgb1, int[]  rgb2, int[]  rgb3, int[]  rgb4, int tValue, int lValue) {

        EHomeInterface.getINSTANCE().setLightFlow(devId, rgb1, rgb2, rgb3, rgb4, tValue, lValue);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void onEventMainThread(MqttReceiveLightModeEvent event) {
        switch (event.getLightMode()){
            case Code.FLOW_MODE_CONTROL:
                mHandler.removeCallbacks(mRunnable);
                btnFlowDetailApply.setClickable(true);
                break;
        }
    }
}
