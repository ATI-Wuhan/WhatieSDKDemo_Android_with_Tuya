package com.whatie.ati.androiddemo.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.constants.Constants;
import com.whatie.ati.androiddemo.widget.CircleImageView;
import com.d9lab.ati.whatiesdk.bean.BaseResponse;
import com.d9lab.ati.whatiesdk.bean.Device;
import com.d9lab.ati.whatiesdk.callback.BaseCallback;
import com.d9lab.ati.whatiesdk.ehome.EHome;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.d9lab.ati.whatiesdk.event.MqttReceiveStatusEvent;
import com.d9lab.ati.whatiesdk.event.MqttReceiveUnbindEvent;
import com.d9lab.ati.whatiesdk.event.StopTcpEvent;
import com.d9lab.ati.whatiesdk.util.Code;
import com.d9lab.ati.whatiesdk.util.QRCodeCons;
import com.lzy.okgo.model.Response;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by 神火 on 2018/6/27.
 */

public class DeviceSettingActivity extends BaseActivity {
    private static final String TAG = "DeviceSettingActivity";
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
    @BindView(R.id.civ_device_settings_icon)
    CircleImageView civDeviceSettingsIcon;
    @BindView(R.id.tv_device_settings_name)
    TextView tvDeviceSettingsName;
    @BindView(R.id.rl_device_settings_name)
    RelativeLayout rlDeviceSettingsName;
    @BindView(R.id.tv_device_settings_share)
    TextView tvDeviceSettingsShare;
    @BindView(R.id.rl_device_settings_share)
    RelativeLayout rlDeviceSettingsShare;
    @BindView(R.id.tv_device_settings_delete_title)
    TextView tvDeviceSettingsDeleteTitle;
    @BindView(R.id.tv_device_delete_button)
    TextView tvDeviceDeleteButton;
    private static final int REUQEST_CHANGE_NAME = 0;
    private Device device;
    private MaterialDialog mConfirmExitDialog;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(mLoadingDialog!=null){
                mLoadingDialog.dismiss();
            }
            Toast.makeText(mContext, getString(R.string.device_delete_failed), Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(mContext);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_device_setting;
    }


    @Override
    protected void initViews() {
        tvTitle.setText(getString(R.string.device_setting_title));
        device = (Device) getIntent().getSerializableExtra(Code.DEVICE);
        tvDeviceSettingsName.setText(device.getName());
        Picasso.with(mContext)
                .load(new File(Constants.getCachePath(mContext), device.getProduct().getName()))
                .error(R.mipmap.default_image)
                .into(civDeviceSettingsIcon);
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initDatas() {

    }


    @OnClick({R.id.ll_title_left, R.id.rl_device_settings_name, R.id.rl_device_settings_share, R.id.tv_device_delete_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.rl_device_settings_name:
                Intent changeName = new Intent(DeviceSettingActivity.this, ChangeDeviceNameActivity.class);
                changeName.putExtra(Code.DEV_ID, device.getDevId());
                changeName.putExtra(Code.DEVICE_NAME, device.getName());
                startActivityForResult(changeName, REUQEST_CHANGE_NAME);
                break;
            case R.id.rl_device_settings_share:
                Intent shareIntent = new Intent(DeviceSettingActivity.this, ShareActivity.class);
                shareIntent.putExtra(Code.DEVICE_ID, device.getId());
                shareIntent.putExtra(QRCodeCons.USAGE_INTENT, QRCodeCons.USAGE.SHARE_DEVICE);
                startActivity(shareIntent);
                break;
            case R.id.tv_device_delete_button:
                showExitConfirmDialog();
                break;
        }
    }

    private void showExitConfirmDialog() {
        if (mConfirmExitDialog != null) {
            mConfirmExitDialog.dismiss();
            mConfirmExitDialog = null;
        }
        mConfirmExitDialog = new MaterialDialog(mContext);
        mConfirmExitDialog.setTitle(getString(R.string.device_unbind_title))
                .setMessage(getString(R.string.device_unbind_content))
                .setPositiveButton(getString(R.string.confirm_device_title), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mConfirmExitDialog.dismiss();
                        mLoadingDialog.show();
                        mHandler.postDelayed(mRunnable, 8000);
                        EHomeInterface.getINSTANCE().removeDevice(mContext, device.getId(), new BaseCallback() {
                            @Override
                            public void onSuccess(Response<BaseResponse> response) {
                                if (response.body().isSuccess()) {
                                    EHome.getInstance().removeDevice(device.getDevId());
                                    Toast.makeText(mContext, getString(R.string.delete_device_success), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(mContext, MainActivity.class));
                                }
                            }
                        });

                    }
                })
                .setNegativeButton(getString(R.string.download_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mConfirmExitDialog.dismiss();
                    }
                }).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REUQEST_CHANGE_NAME) {
            if (resultCode == RESULT_OK) {
                tvDeviceSettingsName.setText(data.getStringExtra(Code.RETURN_DEVICE_NAME));
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 4, sticky = true)
    public void onEventMainThread(MqttReceiveUnbindEvent event) {
        mLoadingDialog.dismiss();
        mHandler.removeCallbacks(mRunnable);
        Toast.makeText(mContext, getString(R.string.device_unbind), Toast.LENGTH_SHORT).show();

    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 4, sticky = true)
    public void onEventMainThread(MqttReceiveStatusEvent event) {
        mLoadingDialog.dismiss();
        mHandler.removeCallbacks(mRunnable);
        Toast.makeText(mContext, getString(R.string.device_unbind), Toast.LENGTH_SHORT).show();
        if (EHome.getLinkedTcp().get(device.getDevId()) != null) {
            EventBus.getDefault().post(new StopTcpEvent(device.getDevId()));
        }
        startActivity(new Intent(DeviceSettingActivity.this, MainActivity.class));
    }


}