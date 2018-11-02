package com.whatie.ati.androiddemo.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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


import com.d9lab.ati.whatiesdk.event.DeviceStatusNotifyEvent;
import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.constants.EhomeResource;
import com.whatie.ati.androiddemo.database.db.DeviceDaoOpe;
import com.whatie.ati.androiddemo.utils.BaseRecyclerAdapter;
import com.whatie.ati.androiddemo.utils.NetworkUtils;
import com.whatie.ati.androiddemo.utils.RecyclerViewHolder;
import com.whatie.ati.androiddemo.utils.ToastUtil;
import com.whatie.ati.androiddemo.widget.MySwipeRefreshLayout;
import com.d9lab.ati.whatiesdk.bean.BaseListResponse;
import com.d9lab.ati.whatiesdk.bean.BaseResponse;
import com.d9lab.ati.whatiesdk.bean.DeviceVo;
import com.d9lab.ati.whatiesdk.callback.BaseCallback;
import com.d9lab.ati.whatiesdk.callback.DevicesCallback;
import com.d9lab.ati.whatiesdk.ehome.EHome;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.d9lab.ati.whatiesdk.event.MqttReceiveLightModeEvent;
import com.d9lab.ati.whatiesdk.event.MqttReceiveLightModePowerEvent;
import com.d9lab.ati.whatiesdk.event.MqttReceiveOffEvent;
import com.d9lab.ati.whatiesdk.event.MqttReceiveOnEvent;
import com.d9lab.ati.whatiesdk.event.MqttReceiveStatusEvent;
import com.d9lab.ati.whatiesdk.event.MqttReceiveUnbindEvent;
import com.d9lab.ati.whatiesdk.util.Code;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.okgo.model.Response;
import com.whatie.ati.androiddemo.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 神火 on 2018/6/7.
 */

public class DeviceListFragment extends BaseFragment {
    private static final String INIT_DEVICE_LIST = "initDeviceList";
    private static final String TAG = "DeviceListFragment";
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_title_left)
    ImageView ivTitleLeft;
    @BindView(R.id.ll_title_left)
    LinearLayout llTitleLeft;
    @BindView(R.id.iv_title_right)
    ImageView ivTitleRight;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.ll_title_right)
    LinearLayout llTitleRight;
    @BindView(R.id.srl_device_list)
    MySwipeRefreshLayout srlDeviceList;
    @BindView(R.id.xrv_device_list)
    XRecyclerView xrvDeviceList;
    @BindView(R.id.ll_no_device)
    LinearLayout llDeviceEmpty;
    @BindView(R.id.ll_main_home)
    LinearLayout llMainHome;

    private BaseRecyclerAdapter<DeviceVo> mAdapter;
    private PopupWindow changeDeviceWindow;
    private View changeDeviceView;

    @Override
    protected int getFragmentLayoutId() {
        return R.layout.frag_home;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (EHome.getInstance().isLogin()) {
            Log.d(TAG, "onResume: device list framgent");
            getDevices();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(INIT_DEVICE_LIST, (Serializable) EHome.getInstance().getmDeviceVos());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        if (getArguments() != null) {
            getDevices();
            Log.d(TAG, "getArguments() != null");
        }
        if (savedInstanceState != null) {
            EHomeInterface.getINSTANCE().saveDevices((ArrayList<DeviceVo>) savedInstanceState.getSerializable(INIT_DEVICE_LIST));
        }

    }


    @Override
    protected void initViews() {
        tvTitle.setText(getString(R.string.home_choice_list_title));
//        tvTitleLeft.setVisibility(View.GONE);
//        deviceVo = (DeviceVo) getIntent().getSerializableExtra(Code.DEVICE);
        tvTitleRight.setText(getString(R.string.device_list_add));
        tvTitleRight.setVisibility(View.VISIBLE);
        ivTitleLeft.setVisibility(View.GONE);
//        ivTitleLeft.setImageResource(R.drawable.ic_scan);
        GridLayoutManager layoutManager = new GridLayoutManager(mContext, 1);
        xrvDeviceList.setLayoutManager(layoutManager);
        xrvDeviceList.getItemAnimator().setChangeDuration(0);
        xrvDeviceList.setPullRefreshEnabled(false);
        xrvDeviceList.setLoadingMoreEnabled(false);
        llTitleLeft.setVisibility(View.GONE);
        llDeviceEmpty.setVisibility(View.GONE);
        llMainHome.setBackgroundResource(EhomeResource.BG_HOME[Code.THEME_NOW]);

    }

    @Override
    protected void initEvents() {


        srlDeviceList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                EHomeInterface.getINSTANCE().getMyDevices(mContext, new DevicesCallback() {
                    @Override
                    public void onSuccess(Response<BaseListResponse<DeviceVo>> response) {
                        if (response.body().isSuccess()) {
                            EHomeInterface.getINSTANCE().saveDevices(response.body().getList());
                            if (response.body().getList().isEmpty()) {
                                xrvDeviceList.refreshComplete();
                                srlDeviceList.setRefreshing(false);
//                                Toast.makeText(mContext, "Device list is empty.", Toast.LENGTH_SHORT).show();

                                llDeviceEmpty.setVisibility(View.VISIBLE);
                            } else {
                                xrvDeviceList.refreshComplete();
                                srlDeviceList.setRefreshing(false);
                                mAdapter.replaceAll(response.body().getList());
                                llDeviceEmpty.setVisibility(View.GONE);
                            }

//                            mAdapter.notifyDataSetChanged();

                        } else {

                            llDeviceEmpty.setVisibility(View.VISIBLE);
                            xrvDeviceList.refreshComplete();
                            srlDeviceList.setRefreshing(false);
                            Toast.makeText(mContext, getString(R.string.device_list_get_fail), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<DeviceVo>> response) {
                        super.onError(response);
                        xrvDeviceList.refreshComplete();
                        srlDeviceList.setRefreshing(false);
                        Toast.makeText(mContext, getString(R.string.device_list_get_fail), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void initDatas() {
        mAdapter = new BaseRecyclerAdapter<DeviceVo>(mContext, EHome.getInstance().getmDeviceVos()) {
            @Override
            public int getItemLayoutId(int viewType) {
                return R.layout.item_device;
            }


            @Override
            public void bindData(RecyclerViewHolder holder, int position, final DeviceVo item) {

                final String productName = item.getProductName();
                Log.d(TAG, "BindViewData:  status=========================" + item.getDevice().getStatus());

                holder.setText(R.id.tv_device_item_name, item.getDevice().getName());

                Log.d(TAG, "BindViewData:Boolean      " + Boolean.parseBoolean(item.getFunctionValuesMap().get(Code.FUNCTION_MAP_POWER)));
                Log.d(TAG, "BindViewData:             " + item.getFunctionValuesMap().get(Code.FUNCTION_MAP_POWER));
                //判断设备是否在线
                //在线：有网+设备状态正常  或者  连接了tcp
                Log.d(TAG, "BindViewData: status  " + item.getDevice().getStatus().equals(Code.DEVICE_STATUS_NORMAL));

                Log.d(TAG, "BindViewData: mqtton  " + EHome.getInstance().isMqttOn());
                if ((item.getDevice().getStatus().equals(Code.DEVICE_STATUS_NORMAL) && NetworkUtils.isAvailable(mContext) && EHome.getInstance().isMqttOn())
                        || EHome.getLinkedTcp().containsKey(item.getDevice().getDevId())) {

                    holder.setSwitchButtonState(R.id.sw_device_item, Boolean.parseBoolean(item.getFunctionValuesMap().get(Code.FUNCTION_MAP_LOCAL_POWER)));
                    holder.setClickListener(R.id.sw_device_item, new SwitchButton.OnShortClickListener() {
                        @Override
                        public void onClicked(SwitchButton view) {
                            if (Code.PRODUCT_TYPE_PLUG.equals(productName)) {
                                Log.d(TAG, "onClick: " + item.getDevice().getStatus());
                                EHomeInterface.getINSTANCE().updateOutletsStatus(item.getDevice().getDevId(), !Boolean.parseBoolean(item.getFunctionValuesMap().get(Code.FUNCTION_MAP_LOCAL_POWER)));
                            } else if (Code.PRODUCT_TYPE_RGBLIGHT.equals(productName) || Code.PRODUCT_TYPE_MONOLIGHT.equals(productName)) {
                                sendLightPowerInst(item.getDevice().getDevId(), !Boolean.parseBoolean(item.getFunctionValuesMap().get(Code.FUNCTION_MAP_LOCAL_POWER)));
                            } else if (Code.PRODUCT_TYPE_STRIP.equals(productName)) {
                                HashMap<String, Object> dps = new HashMap<>();
                                dps.put(Code.STRIP_CONTROL_MODE, 0);
                                dps.put(Code.STRIP_CONTROL_STATUS, !Boolean.parseBoolean(item.getFunctionValuesMap().get(Code.FUNCTION_MAP_LOCAL_POWER)));
                                EHomeInterface.getINSTANCE().updateDeviceStatus(item.getDevice().getDevId(), Code.STRIP_CONTROL, dps);
                            }
                        }
                    });


                    holder.setLongClickListener(R.id.rl_device_item, new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            showChangeDeviceWindow(item);
                            return true;
                        }
                    });

                    holder.setClickListener(R.id.rl_device_item, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (Code.PRODUCT_TYPE_RGBLIGHT.equals(productName) || Code.PRODUCT_TYPE_MONOLIGHT.equals(productName)) {
                                Intent deviceControl = new Intent(getActivity(), LightDetailActivity.class);
                                deviceControl.putExtra(Code.DEVICE, item);
                                startActivity(deviceControl);
                            } else if (Code.PRODUCT_TYPE_PLUG.equals(productName)) {
                                Intent deviceControl = new Intent(getActivity(), DeviceDetailActivity.class);
                                deviceControl.putExtra(Code.DEVICE, item);
                                startActivity(deviceControl);
                            }
                        }
                    });

                } else {
                    holder.setClickListener(R.id.sw_device_item, new SwitchButton.OnShortClickListener() {
                        @Override
                        public void onClicked(SwitchButton view) {
                            ToastUtil.showShort(mContext, R.string.device_is_offline);
                        }
                    });


                    holder.setSwitchButtonState(R.id.sw_device_item, false);
                    holder.setClickListener(R.id.rl_device_item, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtil.showShort(mContext, R.string.device_is_offline);
                        }
                    });
                    holder.setLongClickListener(R.id.rl_device_item, new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            showChangeDeviceWindow(item);
                            return true;
                        }
                    });
                }


            }
        };
        xrvDeviceList.setAdapter(mAdapter);
        getDevices();
    }

    private void sendLightPowerInst(String devId, boolean willState) {
        EHomeInterface.getINSTANCE().updateLightPower(devId, willState);
        Log.d(TAG, "bindData:ColorLight      sendLightPowerInst" + willState);

    }

    public static DeviceListFragment newInstance() {
        Bundle args = new Bundle();

        DeviceListFragment fragment = new DeviceListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void getDevices() {
        EHomeInterface.getINSTANCE().getMyDevices(mContext, new DevicesCallback() {
            @Override
            public void onSuccess(Response<BaseListResponse<DeviceVo>> response) {
                if (response.body().isSuccess()) {
                    if (response.body().getList().isEmpty()) {
                        xrvDeviceList.refreshComplete();
                        srlDeviceList.setRefreshing(false);
                        llDeviceEmpty.setVisibility(View.VISIBLE);
                    } else {
                        xrvDeviceList.refreshComplete();
                        srlDeviceList.setRefreshing(false);
                        llDeviceEmpty.setVisibility(View.GONE);
                        DeviceDaoOpe.deleteAllDevices(mContext);
                        DeviceDaoOpe.saveDevices(mContext, response.body().getList());
                        EHomeInterface.getINSTANCE().saveDevices(response.body().getList());
                        mAdapter.replaceAll(response.body().getList());
                    }

                } else {
                    xrvDeviceList.refreshComplete();
                    srlDeviceList.setRefreshing(false);
                    Toast.makeText(mContext, getString(R.string.device_list_get_fail), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response<BaseListResponse<DeviceVo>> response) {
                super.onError(response);
                xrvDeviceList.refreshComplete();
                srlDeviceList.setRefreshing(false);
                Toast.makeText(mContext, getString(R.string.device_list_get_fail), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void setBackgroundAlpha(float f) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow()
                .getAttributes();
        lp.alpha = f;
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    private void showChangeDeviceWindow(final DeviceVo item) {
        if (changeDeviceWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            changeDeviceView = layoutInflater.inflate(R.layout.pop_change_delete, null);
            changeDeviceWindow = new PopupWindow(changeDeviceView, RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
        }
        setBackgroundAlpha(0.5f);
        changeDeviceWindow.setAnimationStyle(R.style.PopupWindowAnimationFromBottom);
        changeDeviceWindow.setFocusable(true);
        changeDeviceWindow.setOutsideTouchable(true);
        changeDeviceWindow.showAtLocation(getActivity().findViewById(R.id.rl_main_act),
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
                Intent intent = new Intent(getActivity(), ChangeDeviceNameActivity.class);
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
                                    Toast.makeText(mContext, getString(R.string.device_detail_delete), Toast.LENGTH_SHORT);
                                    getDevices();
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
                Intent intent = new Intent(getActivity(), ShareActivity.class);
                intent.putExtra(Code.DEVICE_ID, item.getDevice().getId());
                startActivity(intent);
                setBackgroundAlpha(1.0f);
            }
        });

        TextView setRoom = changeDeviceView.findViewById(R.id.tv_set_room);
        setRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EHomeInterface.getINSTANCE().setDeviceToRoom(mContext, item.getDevice().getId(), "TestRoom", new BaseCallback() {
                    @Override
                    public void onSuccess(Response<BaseResponse> response) {
                        if (response.body().isSuccess()) {
                            changeDeviceWindow.dismiss();
                            setBackgroundAlpha(1.0f);
                        } else {
                            changeDeviceWindow.dismiss();
                            setBackgroundAlpha(1.0f);
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse> response) {
                        super.onError(response);
                        changeDeviceWindow.dismiss();
                        setBackgroundAlpha(1.0f);
                    }
                });
            }
        });


    }

    @OnClick({R.id.ll_title_left, R.id.ll_title_right, R.id.tv_add_device_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.ll_title_right:
                startActivity(new Intent(getContext(), ProductionListActivity.class));
                break;
            case R.id.tv_add_device_button:
                Intent addDeviceIntent = new Intent(getContext(), ProductionListActivity.class);
                startActivity(addDeviceIntent);
                break;
        }
//        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void onEventMainThread(MqttReceiveUnbindEvent event) {
        EHome.getInstance().getmDeviceVos().remove(event.getIndex());
        Log.d(TAG, "onEventMainThread: MqttReceiveUnbindEvent " + event.getIndex());
//        mAdapter.notifyItemRemoved(event.getIndex() + 1);
        mAdapter.notifyDataSetChanged();
    }


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void onEventMainThread(MqttReceiveStatusEvent event) {
        EHome.getInstance().getmDeviceVos().get(event.getIndex()).getDevice().setStatus(event.getStatus());
        mAdapter.notifyItemChanged(event.getIndex() + 1);
//        mAdapter.notifyDataSetChanged();
        Log.d(TAG, "onEventMainThread: MqttReceiveStatusEvent " + event.getIndex());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void onEventMainThread(DeviceStatusNotifyEvent event) {
        Log.d(TAG, "onEventMainThread: MqttReceiveStripStatusEvent");
        mAdapter.notifyItemChanged(event.getIndex() + 1);
    }


}
