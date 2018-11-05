package com.whatie.ati.androiddemo.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.d9lab.ati.whatiesdk.event.DeviceStatusNotifyEvent;
import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.widget.CountdownTextView;
import com.d9lab.ati.whatiesdk.bean.BaseListResponse;
import com.d9lab.ati.whatiesdk.bean.BaseResponse;
import com.d9lab.ati.whatiesdk.bean.ClockVo;
import com.d9lab.ati.whatiesdk.bean.DeviceVo;
import com.d9lab.ati.whatiesdk.callback.BaseCallback;
import com.d9lab.ati.whatiesdk.callback.ClockCallback;
import com.d9lab.ati.whatiesdk.ehome.EHome;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.d9lab.ati.whatiesdk.event.MqttCancelCdSuccessEvent;
import com.d9lab.ati.whatiesdk.event.MqttReceiveEvent;
import com.d9lab.ati.whatiesdk.event.MqttReceiveOffEvent;
import com.d9lab.ati.whatiesdk.event.MqttReceiveOnEvent;
import com.d9lab.ati.whatiesdk.event.MqttReceiveSharedOffEvent;
import com.d9lab.ati.whatiesdk.event.MqttReceiveSharedOnEvent;
import com.d9lab.ati.whatiesdk.event.MqttReceiveStatusEvent;
import com.d9lab.ati.whatiesdk.event.MqttSetCdSuccessEvent;
import com.d9lab.ati.whatiesdk.event.TcpReceiveInstEvent;
import com.d9lab.ati.whatiesdk.util.Code;
import com.d9lab.ati.whatiesdk.util.LogUtil;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by liz on 2018/4/25.
 */

public class DeviceDetailActivity extends BaseActivity {
    private static final String TAG = "DeviceDetailActivity";
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
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_device_control_name)
    TextView tvDeviceControlName;
    @BindView(R.id.tv_device_control_state)
    TextView tvDeviceControlState;
    @BindView(R.id.ctv_device_control_countdown_time)
    CountdownTextView ctvDeviceControlCountdownTime;
    @BindView(R.id.iv_device_detail_switch)
    ImageView ivDeviceDetailSwitch;
    @BindView(R.id.iv_device_detail_countdown)
    ImageView ivDeviceDetailCountdown;
    @BindView(R.id.iv_device_detail_alarm)
    ImageView ivDeviceDetailAlarm;
    @BindView(R.id.rl_device_detail_bg)
    RelativeLayout rlDeviceDetailBg;
    @BindView(R.id.ll_device_detail_bgC)
    LinearLayout llDeviceDetailBgC;
    @BindView(R.id.rl_device_detail_switch)
    RelativeLayout rlDeviceDetailSwitch;
    @BindView(R.id.tv_device_detail_switch)
    TextView tvDeviceDetailSwitch;
    @BindView(R.id.tv_rom_version)
    TextView tvRomVersion;

    private DeviceVo deviceVo;
    private boolean state;
    private ClockVo countdownClock;
    private int countdownClockId = -1;
    private MaterialDialog mConfirmExitDialog;
    private TimePickerView countdownTimeView;
    private PopupWindow changeDeviceWindow;
    private View changeDeviceView;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            rlDeviceDetailSwitch.setEnabled(true);
        }
    };

    @Override
    protected int getContentViewId() {
        return R.layout.act_device_detail;
    }

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
    protected void initViews() {
        tvTitle.setText(getString(R.string.device_control_title));
        deviceVo = (DeviceVo) getIntent().getSerializableExtra(Code.DEVICE);
        if(deviceVo.getDevice().getHardwareVersion() != null) {
            tvRomVersion.setText(deviceVo.getDevice().getHardwareVersion().getVersion());
        }
        tvDeviceControlName.setText(deviceVo.getDevice().getProduct().getName());
        state = Boolean.parseBoolean(deviceVo.getFunctionValuesMap().get(Code.FUNCTION_MAP_POWER));
        toggleState(state);
        getCountdownTime();

        ivTitleRight.setVisibility(View.VISIBLE);

        if (deviceVo.isHost()) {
            ivTitleRight.setImageResource(R.drawable.ic_device_setting);
        } else {
            ivTitleRight.setVisibility(View.GONE);
        }

        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        selectedDate.set(0, 0, 0, 0, 0, 0);
        startDate.set(0, 0, 0, 0, 0, 0);
        endDate.set(0, 0, 0, 23, 59, 0);
        countdownTimeView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date, View v) {
                int countdownTime = date.getHours() * 60 * 60 + date.getMinutes() * 60;
                LogUtil.log(TAG, countdownTime + "");
                if (countdownTime > 0) {
                    setCountdownTime(deviceVo.getDevice().getDevId(), !state, countdownTime);
                }
            }
        }).setType(new boolean[]{false, false, false, true, true, false})
                .setCancelText(getString(R.string.dialog_cancel))
                .setSubmitText(getString(R.string.dialog_ok))
                .isCenterLabel(false)
                .isCyclic(true)
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("", "", "", "hours", "minutes", "")
                .build();

    }

    @Override
    protected void initEvents() {

    }

    private void setBackgroundAlpha(float f) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = f;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    private void showChangeDeviceWindow(final DeviceVo item) {
        if (changeDeviceWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            changeDeviceView = layoutInflater.inflate(R.layout.pop_change_delete, null);
            changeDeviceWindow = new PopupWindow(changeDeviceView, RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
        }
        setBackgroundAlpha(0.5f);
        changeDeviceWindow.setAnimationStyle(R.style.PopupWindowAnimationFromBottom);
        changeDeviceWindow.setFocusable(true);
        changeDeviceWindow.setOutsideTouchable(true);
        changeDeviceWindow.showAtLocation(findViewById(R.id.rl_device_detail_bg),
                Gravity.CENTER, 0, 0);
        changeDeviceWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                changeDeviceWindow.dismiss();
                setBackgroundAlpha(1.0f);
            }
        });
        changeDeviceWindow.setTouchInterceptor(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        TextView changeName = changeDeviceView.findViewById(R.id.tv_select_device_change_name);
        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ChangeDeviceNameActivity.class);
                intent.putExtra(Code.DEVICE, item);
                changeDeviceWindow.dismiss();
                startActivity(intent);
                setBackgroundAlpha(1.0f);

            }
        });

        TextView delete = changeDeviceView.findViewById(R.id.tv_select_device_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDeviceWindow.dismiss();
                EHomeInterface.getINSTANCE().removeDevice(mContext, item.getDevice().getId(),
                        new BaseCallback() {
                            @Override
                            public void onSuccess(Response<BaseResponse> response) {
                                if (response.body().isSuccess()) {
                                    EHome.getInstance().removeDevice(item.getDevice().getDevId());
                                    Toast.makeText(mContext, getString(R.string.device_detail_delete), Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(mContext, getString(R.string.device_delete_failed), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Response<BaseResponse> response) {
                                super.onError(response);
                                Toast.makeText(mContext, getString(R.string.device_delete_failed), Toast.LENGTH_SHORT).show();
                            }
                        });
                setBackgroundAlpha(1.0f);

            }
        });
        TextView cancel = changeDeviceView.findViewById(R.id.tv_select_device_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDeviceWindow.dismiss();
                setBackgroundAlpha(1.0f);
            }
        });

        TextView share = changeDeviceView.findViewById(R.id.tv_select_share_device);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDeviceWindow.dismiss();
                Intent intent = new Intent(mContext, ShareActivity.class);
                intent.putExtra(Code.DEVICE_ID, item.getDevice().getId());
                startActivity(intent);
                setBackgroundAlpha(1.0f);
            }
        });


    }

    @Override
    protected void initDatas() {

    }

    private void setCountdownTime(String devId, boolean state, int duration) {
        EHomeInterface.getINSTANCE().addTimerClockWithDeviceModel(devId, state, duration);
    }

    private void cancelCountdownTime(String devId) {
        EHomeInterface.getINSTANCE().removeTimerClockWithDeviceModel(devId);
    }

    private void getCountdownTime() {
        EHomeInterface.getINSTANCE().getTimerClockWithDeviceModel(mContext, deviceVo.getDevice().getId(), new ClockCallback() {
            @Override
            public void onSuccess(Response<BaseListResponse<ClockVo>> response) {
                if (response.body().isSuccess() && response.body().getList() != null && (!response.body().getList().isEmpty())) {
                    countdownClock = response.body().getList().get(0);
                    countdownClockId = countdownClock.getDeviceClock().getClockId();
                    ctvDeviceControlCountdownTime.setVisibility(View.VISIBLE);
                    ctvDeviceControlCountdownTime.setCountdownConfigAndStart(countdownClock.getDurationTime() * 1000, 1000, Boolean.valueOf(countdownClock.getDeviceClock().getDps()));
                }
            }

            @Override
            public void onError(Response<BaseListResponse<ClockVo>> response) {
                super.onError(response);
            }
        });

    }

    @OnClick({R.id.ll_title_left, R.id.ll_title_right, R.id.rl_device_detail_switch, R.id.iv_device_detail_countdown, R.id.iv_device_detail_alarm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.ll_title_right:
                if (deviceVo.isHost()) {
                    Intent intent = new Intent(mContext, DeviceSettingActivity.class);
                    intent.putExtra(Code.DEVICE, deviceVo.getDevice());
                    startActivity(intent);
                } else {
                    ivTitleRight.setVisibility(View.GONE);
                }
                break;
            case R.id.rl_device_detail_switch:
                rlDeviceDetailSwitch.setEnabled(false);
                mHandler.postDelayed(mRunnable, 1500);
                EHomeInterface.getINSTANCE().updateOutletsStatus(deviceVo.getDevice().getDevId(), !state);
                if (countdownClockId != -1 && EHome.getInstance().isMqttOn()) {
                    cancelCountdownTime(deviceVo.getDevice().getDevId());
                }
                break;
            case R.id.iv_device_detail_countdown:
                if (EHome.getInstance().isMqttOn()) {
                    countdownTimeView.show();
                } else {
                    Toast.makeText(mContext, getString(R.string.countdown_without_network), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_device_detail_alarm:
                if (EHome.getInstance().isMqttOn()) {
                    Intent alarmIntent = new Intent(this, TimerListActivity.class);
                    alarmIntent.putExtra(Code.DEVICE, deviceVo);
                    startActivity(alarmIntent);
                } else {
                    Toast.makeText(mContext, getString(R.string.device_detail_cannot_set_alarm), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void toggleState(boolean state) {
        if (state) {
            llDeviceDetailBgC.setBackgroundResource(R.color.main_theme_blue);
            rlDeviceDetailSwitch.setBackgroundResource(R.color.device_detail_black);
            tvDeviceControlState.setText(getString(R.string.device_detail_on));
            tvDeviceDetailSwitch.setText(getString(R.string.device_off));

        } else {
            llDeviceDetailBgC.setBackgroundResource(R.color.device_detail_black);
            rlDeviceDetailSwitch.setBackgroundResource(R.color.main_theme_blue);
            tvDeviceControlState.setText(getString(R.string.device_detail_off));
            tvDeviceDetailSwitch.setText(getString(R.string.device_on));

        }
    }




    private void removeCountDown() {
        EHomeInterface.getINSTANCE().getTimerClockWithDeviceModel(mContext, deviceVo.getDevice().getId(), new ClockCallback() {
            @Override
            public void onSuccess(Response<BaseListResponse<ClockVo>> response) {
                if (response.body().isSuccess()) {
                    if (response.body().getList() != null) {
                        cancelCountdownTime(deviceVo.getDevice().getDevId());
                    }
                }
            }
        });
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
                        mLoadingDialog.show();
                        EHomeInterface.getINSTANCE().removeDevice(mContext, deviceVo.getDevice().getId(), new BaseCallback() {
                            @Override
                            public void onSuccess(Response<BaseResponse> response) {
                                mLoadingDialog.dismiss();
                                Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                if (response.body().isSuccess()) {
                                    EHome.getInstance().removeDevice(deviceVo.getDevice().getDevId());
                                    startActivity(new Intent(mContext, MainActivity.class));
                                }
                            }

                            @Override
                            public void onError(Response<BaseResponse> response) {
                                super.onError(response);
                                mLoadingDialog.dismiss();
                                Toast.makeText(mContext, getString(R.string.toast_network_error), Toast.LENGTH_SHORT).show();
//                                Toast.makeText(mContext, Code.NETWORK_WRONG,Toast.LENGTH_SHORT).show();
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


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 4, sticky = true)
    public void onEventMainThread(MqttSetCdSuccessEvent event) {
        if (event.getDevId().equals(deviceVo.getDevice().getDevId())) {
            countdownClock = new ClockVo();
            ctvDeviceControlCountdownTime.setVisibility(View.VISIBLE);
            ctvDeviceControlCountdownTime.setCountdownConfigAndStart(event.getDuration() * 1000, 1000, state);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 4, sticky = true)
    public void onEventMainThread(MqttCancelCdSuccessEvent event) {
        if (event.getDevId().equals(deviceVo.getDevice().getDevId())) {
            ctvDeviceControlCountdownTime.cancelCountdown();
            ctvDeviceControlCountdownTime.setVisibility(View.INVISIBLE);
            countdownClock = null;
            countdownClockId = -1;
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void onEventMainThread(DeviceStatusNotifyEvent event) {
        Log.d(TAG, "onEventMainThread: MqttReceiveStripStatusEvent");
        if(event.getDevId().equals(deviceVo.getDevice().getDevId())) {
            if (event.getListType().equals(DeviceStatusNotifyEvent.ListType.DEVICES_LIST)) {
                deviceVo.setFunctionValuesMap(EHome.getInstance().getmDeviceVos().get(event.getIndex()).getFunctionValuesMap());
            } else if (event.getListType().equals(DeviceStatusNotifyEvent.ListType.SHARED_DEVICES_LIST)) {
                deviceVo.setFunctionValuesMap(EHome.getInstance().getmSharedDeviceVos().get(event.getIndex()).getFunctionValuesMap());
            }
            state = Boolean.parseBoolean(deviceVo.getFunctionValuesMap().get(Code.FUNCTION_MAP_LOCAL_POWER));
            toggleState(state);
            rlDeviceDetailSwitch.setEnabled(true);
            mHandler.removeCallbacks(mRunnable);
        }
    }
}
