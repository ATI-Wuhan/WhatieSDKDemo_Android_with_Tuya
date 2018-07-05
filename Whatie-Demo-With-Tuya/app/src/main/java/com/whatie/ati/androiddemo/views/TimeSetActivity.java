package com.whatie.ati.androiddemo.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.OnItemSelectedListener;
import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.utils.DatetimeUtil;
import com.whatie.ati.androiddemo.widget.togglebutton.ToggleButton;
import com.d9lab.ati.whatiesdk.bean.BaseResponse;
import com.d9lab.ati.whatiesdk.bean.ClockVo;
import com.d9lab.ati.whatiesdk.callback.BaseCallback;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.d9lab.ati.whatiesdk.event.MqttCancelTimerSuccessEvent;
import com.d9lab.ati.whatiesdk.event.MqttSetTimerSuccessEvent;
import com.d9lab.ati.whatiesdk.util.Code;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * Created by liz on 2018/5/15.
 */

public class TimeSetActivity extends BaseActivity {
    private static final String TAG = "TimeSetActivity";

    @BindView(R.id.ll_timer)
    LinearLayout llTimer;
    @BindView(R.id.wv_hour)
    WheelView Hour;
    @BindView(R.id.wv_min)
    WheelView Min;
    @BindView(R.id.tv_title)
    TextView tvtitle;
    @BindView(R.id.tv_title_right)
    TextView tvtitleright;
    @BindView(R.id.tv_title_left)
    TextView tvtitleleft;
    @BindView(R.id.rl_timer_type)
    RelativeLayout rlTimerType;
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.tv_timer_type)
    TextView tvTimerType;
    @BindView(R.id.ll_title_right)
    LinearLayout llTitleRight;
    @BindView(R.id.tb_timer_state)
    ToggleButton tbTimerState;
    @BindView(R.id.tv_delete_timer)
    TextView tvDeleteTimer;
    @BindView(R.id.et_timer_category)
    EditText etTimerCategory;

    public static final int REQUEST_REPEAT = 1;
    private int deviceId;
    private String timerType = "0000000";
    private boolean dps = false;
    private int clockId;
    private String mCategory = "";
    private MaterialDialog mConfirmExitDialog;
    final List<String> mdayItems = new ArrayList<>();
    final List<String> mhourItems = new ArrayList<>();
    final List<String> mminItems = new ArrayList<>();
    String hour = "00";
    String min = "00";

    private static String CATEGORY_DEFAULT = "Category tag";

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
        return R.layout.act_timer_add;
    }


    @Override
    protected void initViews() {

        tvtitleright.setVisibility(View.VISIBLE);
        tvtitleright.setText(getString(R.string.timer_title_save));
        tvtitleleft.setText(getString(R.string.timer_title_cancel));
        Hour.setCyclic(false);
        Min.setCyclic(false);

        mhourItems.add("00");
        mhourItems.add("01");
        mhourItems.add("02");
        mhourItems.add("03");
        mhourItems.add("04");
        mhourItems.add("05");
        mhourItems.add("06");
        mhourItems.add("07");
        mhourItems.add("08");
        mhourItems.add("09");
        mhourItems.add("10");
        mhourItems.add("11");
        mhourItems.add("12");
        mhourItems.add("13");
        mhourItems.add("14");
        mhourItems.add("15");
        mhourItems.add("16");
        mhourItems.add("17");
        mhourItems.add("18");
        mhourItems.add("19");
        mhourItems.add("20");
        mhourItems.add("21");
        mhourItems.add("22");
        mhourItems.add("23");

        mminItems.add("00");
        mminItems.add("01");
        mminItems.add("02");
        mminItems.add("03");
        mminItems.add("04");
        mminItems.add("05");
        mminItems.add("06");
        mminItems.add("07");
        mminItems.add("08");
        mminItems.add("09");
        mminItems.add("10");
        mminItems.add("11");
        mminItems.add("12");
        mminItems.add("13");
        mminItems.add("14");
        mminItems.add("15");
        mminItems.add("16");
        mminItems.add("17");
        mminItems.add("18");
        mminItems.add("19");
        mminItems.add("20");
        mminItems.add("21");
        mminItems.add("22");
        mminItems.add("23");
        mminItems.add("24");
        mminItems.add("25");
        mminItems.add("26");
        mminItems.add("27");
        mminItems.add("28");
        mminItems.add("29");
        mminItems.add("30");
        mminItems.add("31");
        mminItems.add("32");
        mminItems.add("33");
        mminItems.add("34");
        mminItems.add("35");
        mminItems.add("36");
        mminItems.add("37");
        mminItems.add("38");
        mminItems.add("39");
        mminItems.add("40");
        mminItems.add("41");
        mminItems.add("42");
        mminItems.add("43");
        mminItems.add("44");
        mminItems.add("45");
        mminItems.add("46");
        mminItems.add("47");
        mminItems.add("48");
        mminItems.add("49");
        mminItems.add("50");
        mminItems.add("51");
        mminItems.add("52");
        mminItems.add("53");
        mminItems.add("54");
        mminItems.add("55");
        mminItems.add("56");
        mminItems.add("57");
        mminItems.add("58");
        mminItems.add("59");

        mdayItems.add(getString(R.string.timer_day_monday));
        mdayItems.add(getString(R.string.timer_day_tuesday));
        mdayItems.add(getString(R.string.timer_day_wednesday));
        mdayItems.add(getString(R.string.timer_day_thursday));
        mdayItems.add(getString(R.string.timer_day_friday));
        mdayItems.add(getString(R.string.timer_day_saturday));
        mdayItems.add(getString(R.string.timer_day_sunday));

        Hour.setAdapter(new ArrayWheelAdapter<>(mhourItems));
        Min.setAdapter(new ArrayWheelAdapter<>(mminItems));

        Hour.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                hour = mhourItems.get(index);
            }
        });
        Min.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                min = mminItems.get(index);
            }
        });
        Hour.setCyclic(true);
        Min.setCyclic(true);
        if (getIntent().getAction().equals(TimerListActivity.FROM_ADD)) {
            deviceId = getIntent().getIntExtra(Code.DEVICE_ID, -1);
            tvtitle.setText(getString(R.string.add_timer_title));
            tvDeleteTimer.setVisibility(View.GONE);
            Hour.setCurrentItem(DatetimeUtil.getCurrentHourInt());
            Min.setCurrentItem(DatetimeUtil.getCurrentMinInt());
            hour = DatetimeUtil.getCurrentHourInt() + "";
            min = DatetimeUtil.getCurrentMinInt() + "";
        } else {
            ClockVo clockVo = (ClockVo) getIntent().getSerializableExtra(Code.CLOCK);
            Log.d(TAG, "initViews: ClockVo tag = " + clockVo.getDeviceClock().getTag());
            clockId = clockVo.getDeviceClock().getId();
            deviceId = clockVo.getDeviceClock().getDevice().getId();
            String[] d = clockVo.getDeviceClock().getDps().split("_");
            dps = Boolean.valueOf(d[0]);
            tbTimerState.toggle(dps);
            timerType = clockVo.getDeviceClock().getTimerType();
            showTimeType(timerType);
            Hour.setCurrentItem(Integer.valueOf(clockVo.getFinishTimeApp().substring(0, 2)));
            Min.setCurrentItem(Integer.valueOf(clockVo.getFinishTimeApp().substring(2, clockVo.getFinishTimeApp().length())));
            hour = clockVo.getFinishTimeApp().substring(0, 2);
            min = clockVo.getFinishTimeApp().substring(2, clockVo.getFinishTimeApp().length());
            tvtitle.setText(getString(R.string.edit_timer_title));
            etTimerCategory.setText(clockVo.getDeviceClock().getTag());
            tvDeleteTimer.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.rl_timer_type, R.id.ll_title_left, R.id.ll_title_right, R.id.tb_timer_state, R.id.tv_delete_timer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.rl_timer_type:
                Intent intent = new Intent(TimeSetActivity.this, TimeTypeActivity.class);
                intent.putExtra("time_type", timerType);
                startActivityForResult(intent, REQUEST_REPEAT);
                break;
            case R.id.ll_title_right:
                if (getIntent().getAction().equals(TimerListActivity.FROM_ADD)) {
                    saveTimer();
                } else {
                    editTimer();
                }
                break;
            case R.id.tb_timer_state:
                if (dps) {
                    dps = false;
                    tbTimerState.toggleOff();
                } else {
                    dps = true;
                    tbTimerState.toggleOn();
                }
                break;
            case R.id.tv_delete_timer:
                showExitConfirmDialog();
                break;
        }
    }
    public void saveTimer() {
        mLoadingDialog.show();
        if(!"".equals(etTimerCategory.getText().toString().trim())) {
            mCategory = etTimerCategory.getText().toString().trim();
        }
        EHomeInterface.getINSTANCE().addTimer(mContext, mCategory, deviceId, timerType, hour, min, dps, new BaseCallback() {
            @Override
            public void onSuccess(Response<BaseResponse> response) {
                if (!response.body().isSuccess()) {
                    mLoadingDialog.dismiss();
                }
                Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Response<BaseResponse> response) {
                super.onError(response);
                mLoadingDialog.dismiss();
                Toast.makeText(mContext, Code.NETWORK_WRONG, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void editTimer() {
        mLoadingDialog.show();
        if(!"".equals(etTimerCategory.getText().toString().trim())) {
            mCategory = etTimerCategory.getText().toString().trim();
        } else {
            mCategory = "";
        }
        Log.d(TAG, "editTimer: mCategory = " + mCategory);
        EHomeInterface.getINSTANCE().editTimer(mContext, mCategory, clockId, timerType, hour, min, dps, new BaseCallback() {
            @Override
            public void onSuccess(Response<BaseResponse> response) {
                if (!response.body().isSuccess()) {
                    mLoadingDialog.dismiss();
                }
                Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Response<BaseResponse> response) {
                super.onError(response);
                mLoadingDialog.dismiss();
                Toast.makeText(mContext, Code.NETWORK_WRONG, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showExitConfirmDialog() {
        if (mConfirmExitDialog != null) {
            mConfirmExitDialog.dismiss();
            mConfirmExitDialog = null;
        }
        mConfirmExitDialog = new MaterialDialog(mContext);
        mConfirmExitDialog.setTitle(getString(R.string.device_delete_title))
                .setMessage(getString(R.string.delete_timer_content))
                .setPositiveButton(getString(R.string.confirm_device_title), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mConfirmExitDialog.dismiss();
                        deleteTimer();

                    }
                })
                .setNegativeButton(getString(R.string.download_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mConfirmExitDialog.dismiss();
                    }
                }).show();
    }

    private void deleteTimer() {
        mLoadingDialog.show();
        EHomeInterface.getINSTANCE().removeTimer(mContext, clockId, new BaseCallback() {
            @Override
            public void onSuccess(Response<BaseResponse> response) {
                if (!response.body().isSuccess()) {
                    mLoadingDialog.dismiss();
                }
                Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Response<BaseResponse> response) {
                super.onError(response);
                mLoadingDialog.dismiss();
                Toast.makeText(mContext, Code.NETWORK_WRONG, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void showTimeType(String type) {
        String strt = "";
        if (type != null && !"".equals(type)) {
            if (!"111111".equals(type)) {
                if (type.charAt(0) == '1') {
                    strt = mdayItems.get(6) + " " + strt;
                }
                if (type.charAt(1) == '1') {
                    strt = mdayItems.get(5) + " " + strt;
                }
                if (type.charAt(2) == '1') {
                    strt = mdayItems.get(4) + " " + strt;
                }
                if (type.charAt(3) == '1') {
                    strt = mdayItems.get(3) + " " + strt;
                }
                if (type.charAt(4) == '1') {
                    strt = mdayItems.get(2) + " " + strt;
                }
                if (type.charAt(5) == '1') {
                    strt = mdayItems.get(1) + " " + strt;
                }
                if (type.charAt(6) == '1') {
                    strt = mdayItems.get(0) + " " + strt;
                }
                tvTimerType.setText(strt);
            }
            if (type.equals("1111111")) {
                tvTimerType.setText(getString(R.string.everyday));
            }
            if (type.equals("0000000")) {
                tvTimerType.setText(getString(R.string.never));
            }
        }
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void initDatas() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_REPEAT && resultCode == RESULT_OK) {
            timerType = data.getStringExtra("time_day_setting");
            showTimeType(timerType);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 5, sticky = true)
    public void onEventMainThread(MqttCancelTimerSuccessEvent event) {
        mLoadingDialog.dismiss();
        finish();
    }
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 5, sticky = true)
    public void onEventMainThread(MqttSetTimerSuccessEvent event) {
        mLoadingDialog.dismiss();
        finish();
    }



}