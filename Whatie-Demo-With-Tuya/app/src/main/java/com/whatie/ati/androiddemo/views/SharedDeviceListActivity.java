package com.whatie.ati.androiddemo.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
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

import com.d9lab.ati.whatiesdk.bean.BaseListResponse;
import com.d9lab.ati.whatiesdk.bean.BaseResponse;
import com.d9lab.ati.whatiesdk.bean.DeviceVo;
import com.d9lab.ati.whatiesdk.callback.BaseCallback;
import com.d9lab.ati.whatiesdk.callback.DevicesCallback;
import com.d9lab.ati.whatiesdk.ehome.EHome;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.d9lab.ati.whatiesdk.event.DeviceStatusNotifyEvent;
import com.d9lab.ati.whatiesdk.event.MqttReceiveLightModeEvent;
import com.d9lab.ati.whatiesdk.event.MqttReceiveLightModePowerEvent;
import com.d9lab.ati.whatiesdk.event.MqttReceiveSharedOffEvent;
import com.d9lab.ati.whatiesdk.event.MqttReceiveSharedOnEvent;
import com.d9lab.ati.whatiesdk.event.MqttReceiveSharedStatusEvent;
import com.d9lab.ati.whatiesdk.util.Code;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lzy.okgo.model.Response;
import com.whatie.ati.androiddemo.R;
import com.whatie.ati.androiddemo.utils.BaseRecyclerAdapter;
import com.whatie.ati.androiddemo.utils.NetworkUtils;
import com.whatie.ati.androiddemo.utils.RecyclerViewHolder;
import com.whatie.ati.androiddemo.utils.ToastUtil;
import com.whatie.ati.androiddemo.widget.MySwipeRefreshLayout;
import com.whatie.ati.androiddemo.widget.SwitchButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by liz on 2018/5/15.
 */

public class SharedDeviceListActivity extends BaseActivity {
    private static final String TAG = "SharedDeviceListActivit";

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
    @BindView(R.id.srl_shared_device_list)
    MySwipeRefreshLayout srlSharedDeviceList;
    @BindView(R.id.xrv_shared_device_list)
    XRecyclerView xrvSharedDeviceList;
    //    @BindView(R.id.fab_my_device)
//    FloatingActionButton fabMyDevice;
//    @BindView(R.id.content)
//    FrameLayout content;
    @BindView(R.id.tv_share_device_empty)
    TextView tvShareDeviceEmpty;

    private PopupWindow changeDeviceWindow;
    private View changeDeviceView;
    private BaseRecyclerAdapter<DeviceVo> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(mContext);
        getDevices();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (EHome.getInstance().isLogin()) {
            getDevices();
        }
    }

    @Override
    protected int getContentViewId() {
        return R.layout.act_shared_device_list;
    }

    @Override
    protected void initViews() {
        tvTitle.setText(getString(R.string.receive_shared_device));
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        xrvSharedDeviceList.setLayoutManager(layoutManager);
        xrvSharedDeviceList.setPullRefreshEnabled(false);
        xrvSharedDeviceList.setLoadingMoreEnabled(false);
        tvShareDeviceEmpty.setVisibility(View.GONE);
    }

    @Override
    protected void initEvents() {
        srlSharedDeviceList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDevices();
            }
        });
        ivTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initDatas() {
        mAdapter = new BaseRecyclerAdapter<DeviceVo>(mContext, EHome.getInstance().getmSharedDeviceVos()) {
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
                                Intent deviceControl = new Intent(mContext, LightDetailActivity.class);
                                deviceControl.putExtra(Code.DEVICE, item);
                                startActivity(deviceControl);
                            } else if (Code.PRODUCT_TYPE_PLUG.equals(productName)) {
                                Intent deviceControl = new Intent(mContext, DeviceDetailActivity.class);
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

        xrvSharedDeviceList.setAdapter(mAdapter);

    }

    private void sendLightPowerInst(String devId, boolean willState) {
        EHomeInterface.getINSTANCE().updateLightPower(devId, willState);
        Log.d(TAG, "bindData:ColorLight      sendLightPowerInst" + willState);

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
        changeDeviceWindow.showAtLocation(findViewById(R.id.ll_shared_device_list),
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
                EHomeInterface.getINSTANCE().removeSharedDevice(mContext, item.getDevice().getId(),
                        new BaseCallback() {
                            @Override
                            public void onSuccess(Response<BaseResponse> response) {
                                if (response.body().isSuccess()) {
                                    EHome.getInstance().removeSharedDevice(item.getDevice().getDevId());
                                    Toast.makeText(mContext, getString(R.string.device_detail_delete), Toast.LENGTH_SHORT).show();
                                    getDevices();
                                    mAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(mContext, getString(R.string.device_delete_fail), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(Response<BaseResponse> response) {
                                super.onError(response);
                                Toast.makeText(mContext, getString(R.string.device_delete_fail), Toast.LENGTH_SHORT).show();
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
//    @OnClick(R.id.fab_my_device)
//    public void onViewClicked() {
//        startActivity(new Intent(SharedDeviceListActivity.this, MainActivity.class));
//    }

    private void getDevices() {
        EHomeInterface.getINSTANCE().querySharedDevices(mContext, new DevicesCallback() {
            @Override
            public void onSuccess(Response<BaseListResponse<DeviceVo>> response) {
                if (response.body().isSuccess()) {
                    if (response.body().getList() == null) {
                        xrvSharedDeviceList.refreshComplete();
                        srlSharedDeviceList.setRefreshing(false);
//                                Toast.makeText(mContext, "Device list is empty.", Toast.LENGTH_SHORT).show();
                        tvShareDeviceEmpty.setVisibility(View.VISIBLE);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        xrvSharedDeviceList.refreshComplete();
                        srlSharedDeviceList.setRefreshing(false);
                        tvShareDeviceEmpty.setVisibility(View.GONE);
                        EHomeInterface.getINSTANCE().saveSharedDevices(response.body().getList());
                        mAdapter.replaceAll(response.body().getList());
                    }

//                            mAdapter.notifyDataSetChanged();

                } else {
                    tvShareDeviceEmpty.setVisibility(View.VISIBLE);
                    xrvSharedDeviceList.refreshComplete();
                    srlSharedDeviceList.setRefreshing(false);
                    Toast.makeText(mContext, getString(R.string.device_list_get_fail), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Response<BaseListResponse<DeviceVo>> response) {
                super.onError(response);
                xrvSharedDeviceList.refreshComplete();
                srlSharedDeviceList.setRefreshing(false);
                Toast.makeText(mContext, getString(R.string.device_list_get_fail), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN, priority = 2, sticky = true)
    public void onEventMainThread(MqttReceiveSharedStatusEvent event) {
        mAdapter.notifyItemChanged(event.getIndex() + 1);
    }


    @Subscribe(threadMode = ThreadMode.MAIN, priority = 1, sticky = true)
    public void onEventMainThread(DeviceStatusNotifyEvent event) {
        mAdapter.notifyItemChanged(event.getIndex() + 1);
    }
}
