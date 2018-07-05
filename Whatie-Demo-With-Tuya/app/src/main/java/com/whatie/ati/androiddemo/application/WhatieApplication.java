package com.whatie.ati.androiddemo.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.tuya.smart.sdk.TuyaSdk;
import com.tuya.smart.sdk.TuyaUser;
import com.whatie.ati.androiddemo.database.db.HomeDaoOpe;
import com.whatie.ati.androiddemo.database.db.RoomDaoOpe;
import com.whatie.ati.androiddemo.database.db.SharedDeviceDaoOpe;
import com.whatie.ati.androiddemo.database.db.SharingDeviceDaoOpe;
import com.d9lab.ati.whatiesdk.bean.BaseListResponse;
import com.d9lab.ati.whatiesdk.bean.DeviceVo;
import com.d9lab.ati.whatiesdk.bean.Home;
import com.d9lab.ati.whatiesdk.bean.RoomVo;
import com.d9lab.ati.whatiesdk.callback.DevicesCallback;
import com.d9lab.ati.whatiesdk.callback.HomeListCallback;
import com.d9lab.ati.whatiesdk.callback.RoomListCallback;
import com.d9lab.ati.whatiesdk.ehome.EHomeInterface;
import com.lzy.okgo.model.Response;


/**
 * Created by liz on 2018/4/24.
 */

public class WhatieApplication extends Application {

    private static final String TAG = "WhatieApplication";

    private static WhatieApplication mInstance;
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
        if (mInstance == null) {
            mInstance = this;
        }
        EHomeInterface.getINSTANCE().init(this);
        TuyaSdk.init(this);
    }

    public static WhatieApplication getInstance() {
        return mInstance;
    }

    public void initLocalData() {
        initHomeData();
        initRoomData();
        initSharedDeviceData();
        initSharingDeviceData();
    }

    private void initHomeData() {
        EHomeInterface.getINSTANCE().getUserHomeList(mContext, new HomeListCallback() {
            @Override
            public void onSuccess(Response<BaseListResponse<Home>> response) {
                if (response.body().isSuccess()) {
                    HomeDaoOpe.deleteAllHomes(mContext);
                    HomeDaoOpe.saveHomes(mContext, response.body().getList());
                    Log.d(TAG, "application Write home table...");
                } else {

                }
            }

            @Override
            public void onError(Response<BaseListResponse<Home>> response) {
                super.onError(response);
            }
        });
    }

    private void initRoomData() {
        EHomeInterface.getINSTANCE().getRoomList(mContext, new RoomListCallback() {
            @Override
            public void onSuccess(Response<BaseListResponse<RoomVo>> response) {
                if(response.body().isSuccess()){
                    RoomDaoOpe.deleteAllRooms(mContext);
                    RoomDaoOpe.saveRoomVos(mContext, response.body().getList());
                    Log.d(TAG, "application Write room table...");
                }else {

                }
            }

            @Override
            public void onError(Response<BaseListResponse<RoomVo>> response) {
                super.onError(response);
            }
        });
    }

    private void initSharedDeviceData() {
        EHomeInterface.getINSTANCE().querySharedDevices(mContext, new DevicesCallback() {
            @Override
            public void onSuccess(Response<BaseListResponse<DeviceVo>> response) {
                if(response.body().isSuccess()) {
                    SharedDeviceDaoOpe.deleteAllSharedDevices(mContext);
                    SharedDeviceDaoOpe.saveSharedDevices(mContext, response.body().getList());
                    Log.d(TAG, "application write shared device table...");
                }
            }

            @Override
            public void onError(Response<BaseListResponse<DeviceVo>> response) {
                super.onError(response);
            }
        });
    }

    private void initSharingDeviceData() {
        EHomeInterface.getINSTANCE().querySharingdDevices(mContext, new DevicesCallback() {
            @Override
            public void onSuccess(Response<BaseListResponse<DeviceVo>> response) {
                if(response.body().isSuccess()) {
                    SharingDeviceDaoOpe.deleteAllSharingDevices(mContext);
                    SharingDeviceDaoOpe.saveSharingDevices(mContext, response.body().getList());
                    Log.d(TAG, "application write sharing device table...");
                }
            }

            @Override
            public void onError(Response<BaseListResponse<DeviceVo>> response) {
                super.onError(response);
            }
        });
    }

    @Override
    public void onTerminate() {
        TuyaUser.getDeviceInstance().onDestroy();
        super.onTerminate();
    }
}
