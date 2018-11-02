package com.whatie.ati.androiddemo.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.utils.BaseRecyclerAdapter;
import com.whatie.ati.androiddemo.utils.RecyclerViewHolder;
import com.whatie.ati.androiddemo.utils.ToastUtil;
import com.whatie.ati.androiddemo.widget.MySwipeRefreshLayout;
import com.d9lab.ati.whatiesdk.bean.BaseListResponse;
import com.d9lab.ati.whatiesdk.bean.BaseResponse;
import com.d9lab.ati.whatiesdk.bean.ClockVo;
import com.d9lab.ati.whatiesdk.bean.DeviceVo;
import com.d9lab.ati.whatiesdk.callback.BaseCallback;
import com.d9lab.ati.whatiesdk.callback.ClockCallback;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.d9lab.ati.whatiesdk.event.MqttCancelTimerSuccessEvent;
import com.d9lab.ati.whatiesdk.event.MqttSetTimerSuccessEvent;
import com.d9lab.ati.whatiesdk.util.Code;
import com.lzy.okgo.model.Response;
import com.whatie.ati.androiddemo.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018\4\28 0028.
 */

public class TimerListActivity extends BaseActivity{
    private static final String TAG = "TimerListActivity";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.srl_alarm_list)
    MySwipeRefreshLayout srlAlarmList;
    @BindView(R.id.rv_alarm_list)
    RecyclerView xrvAlarmList;
    @BindView(R.id.tv_no_timer)
    TextView tvNoTimer;

    private ArrayList<ClockVo> mClocks = new ArrayList<>();
    private ArrayList<Integer> mClockIds = new ArrayList<>();
    private BaseRecyclerAdapter<ClockVo> mAdapter;
    final List<String> mdayItems = new ArrayList<>();
    private DeviceVo mDevice;

    public static final String FROM_ADD = "form_add";
    public static final String FROM_CHANGE = "form_change";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(mContext);
        mLoadingDialog.show();
        refreshClocks(mDevice.getDevice().getId());
    }
    public void refreshClocks(int deviceId) {
        EHomeInterface.getINSTANCE().getTimerList(mContext, deviceId, new ClockCallback() {
            @Override
            public void onSuccess(Response<BaseListResponse<ClockVo>> response) {
                if (response.body().isSuccess()) {
                    onRefreshSuccess(response.body().getList());
                } else {
                    onRefreshFailed(response.body().getMessage());
                }
            }

            @Override
            public void onError(Response<BaseListResponse<ClockVo>> response) {
                super.onError(response);
                onRefreshFailed(Code.NETWORK_WRONG);
            }
        });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void onRefreshSuccess(List<ClockVo> clocks) {
        mLoadingDialog.dismiss();
        mClocks.clear();
        mClocks.addAll(clocks);
        mAdapter.notifyDataSetChanged();
        srlAlarmList.setRefreshing(false);
        if(mClocks.size() > 0){
            tvNoTimer.setVisibility(View.GONE);
        }else {
            tvNoTimer.setVisibility(View.VISIBLE);
        }
    }


    public void onRefreshFailed(String msg) {
        mLoadingDialog.dismiss();
        srlAlarmList.setRefreshing(false);
        Toast.makeText(mContext, msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_alarm_list;
    }


    @Override
    protected void initViews() {
        tvTitle.setText(getString(R.string.alarm_list_title));
        tvTitleRight.setVisibility(View.VISIBLE);
        tvTitleRight.setText(getString(R.string.alarm_add));
        mDevice = (DeviceVo) getIntent().getSerializableExtra(Code.DEVICE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        xrvAlarmList.setLayoutManager(layoutManager);

        mdayItems.add(getString(R.string.timer_day_monday));
        mdayItems.add(getString(R.string.timer_day_tuesday));
        mdayItems.add(getString(R.string.timer_day_wednesday));
        mdayItems.add(getString(R.string.timer_day_thursday));
        mdayItems.add(getString(R.string.timer_day_friday));
        mdayItems.add(getString(R.string.timer_day_saturday));
        mdayItems.add(getString(R.string.timer_day_sunday));
    }

    @Override
    protected void initEvents() {
        srlAlarmList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mLoadingDialog.show();
                refreshClocks(mDevice.getDevice().getId());
            }
        });
    }

    public String showTimeType(String type) {
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
            }
            if (type.equals("1111111")) {
                strt = getString(R.string.everyday);
            }
            if (type.equals("0000000")) {
                strt = getString(R.string.never);
            }
        }
        return strt;
    }

    public String showTime(String time) {
        String t = "";
        if (time != null && !"".equals(time)) {
            t = time.substring(0, 2) + ":" + time.substring(2, time.length());
        }
        return t;
    }

    @Override
    protected void initDatas() {
        mLoadingDialog.show();
        mAdapter = new BaseRecyclerAdapter<ClockVo>(mContext, mClocks) {

            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_alarm;
            }

            @Override
            public void bindData(RecyclerViewHolder holder, int position, final ClockVo item) {
                if (Code.PRODUCT_TYPE_PLUG.equals(mDevice.getProductName())) {
                    String[] d = item.getDeviceClock().getDps().split("_");
                    int dps = d[0].equals(Code.TRUE) ? R.string.device_on : R.string.device_off;
                    holder.setText(R.id.tv_alarm_device_state, getString(dps));
                } else if (Code.PRODUCT_TYPE_RGBLIGHT.equals(mDevice.getProductName()) || Code.PRODUCT_TYPE_MONOLIGHT.equals(mDevice.getProductName())) {
                    String[] d = item.getDeviceClock().getDps().split("_");
                    int dps = d[0].equals(Code.TRUE) ? R.string.device_on : R.string.device_off;
                    holder.setText(R.id.tv_alarm_device_state, getString(dps));
                } else if (Code.PRODUCT_TYPE_STRIP.equals(mDevice.getProductName())) {
                    HashMap dps = JSON.parseObject(item.getDeviceClock().getDps(), new TypeReference<HashMap<String, Object>>(){});
                    Log.d(TAG, "bindData: dps : " + dps.toString());
                    if ((Boolean) dps.get(Code.STRIP_CONTROL_STATUS)) {
                        holder.setText(R.id.tv_alarm_device_state, getString(R.string.device_on));
                    } else {
                        holder.setText(R.id.tv_alarm_device_state, getString(R.string.device_off));
                    }
                }
                holder.setText(R.id.tv_alarm_time, showTime(item.getFinishTimeApp()));
                holder.setText(R.id.tv_alarm_day, showTimeType(item.getDeviceClock().getTimerType()));

                holder.setSwitchButtonState(R.id.sw_alarm_item, item.getDeviceClock().getClockStatus());
                holder.setClickListener(R.id.sw_alarm_item, new SwitchButton.OnShortClickListener() {
                    @Override
                    public void onClicked(SwitchButton view) {
                        editTimerState(item);
                    }
                });


                holder.setClickListener(R.id.rl_item_alarm, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent alarmchange = new Intent(TimerListActivity.this, TimeSetActivity.class);
                        alarmchange.setAction(FROM_CHANGE);

                        alarmchange.putExtra(Code.CLOCK, item);
                        alarmchange.putExtra(Code.PRODUCT_NAME, mDevice.getProductName());
                        startActivity(alarmchange);
                    }
                });

            }
        };
        xrvAlarmList.setAdapter(mAdapter);
    }

    private void editTimerState(ClockVo clockVo) {
        mLoadingDialog.show();
        EHomeInterface.getINSTANCE().updateTimerStatus(mContext, clockVo.getDeviceClock().getId(), !clockVo.getDeviceClock().getClockStatus(), new BaseCallback() {
            @Override
            public void onSuccess(Response<BaseResponse> response) {
                if (response.body().isSuccess()) {
                    mLoadingDialog.dismiss();
                    ToastUtil.showShort(mContext, R.string.scene_toast_edit_success);
                } else {
                    mLoadingDialog.dismiss();
                    if(response.body() != null) {
                        ToastUtil.showShort(mContext, ToastUtil.codeToStringId(response.body().getCode()));
                    } else {
                        ToastUtil.showShort(mContext, getString(R.string.network_error) + response.code());
                    }
                }
            }

            @Override
            public void onError(Response<BaseResponse> response) {
                super.onError(response);
                mLoadingDialog.dismiss();
                if(response.body() != null) {
                    ToastUtil.showShort(mContext, ToastUtil.codeToStringId(response.body().getCode()));
                } else {
                    ToastUtil.showShort(mContext, getString(R.string.network_error) + response.code());
                }
            }
        });

    }

    @OnClick({R.id.ll_title_left, R.id.ll_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_title_left:
                finish();
                break;
            case R.id.ll_title_right:

                Intent addTimerIntent = new Intent(TimerListActivity.this, TimeSetActivity.class);
                addTimerIntent.setAction(FROM_ADD);
                addTimerIntent.putExtra(Code.DEVICE_ID, mDevice.getDevice().getId());
                addTimerIntent.putExtra(Code.PRODUCT_NAME, mDevice.getProductName());
                startActivity(addTimerIntent);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void onEventMainThread(MqttSetTimerSuccessEvent event) {
        refreshClocks(mDevice.getDevice().getId());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void onEventMainThread(MqttCancelTimerSuccessEvent event) {
        refreshClocks(mDevice.getDevice().getId());
    }

}
