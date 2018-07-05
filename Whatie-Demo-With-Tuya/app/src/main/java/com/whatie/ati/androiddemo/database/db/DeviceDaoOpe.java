package com.whatie.ati.androiddemo.database.db;

import android.content.Context;
import android.util.Log;

import com.whatie.ati.androiddemo.database.entity.DeviceDB;
import com.whatie.ati.androiddemo.database.greenDao.DeviceDBDao;
import com.d9lab.ati.whatiesdk.bean.DeviceVo;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class DeviceDaoOpe {

    private static final String TAG = "DeviceDaoOpe";

    /**
     * Device表中插入单个DeviceDB,如果ID存在会插入失败
     *
     * @param context
     * @param item
     */
    public static void insertDevice(Context context, DeviceVo item) {

        DeviceDB dbItem = DataBaseUtil.deviceVoToDeviceDB(item);
        DbManager.getDaoSession(context).getDeviceDBDao().insert(dbItem);
    }

    /**
     * Device表中插入DeviceDB列表
     *
     * @param context
     * @param list
     */
    public static void insertDevices(Context context, List<DeviceVo> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        for (DeviceVo item : list) {
            DbManager.getDaoSession(context).getDeviceDBDao().insert(DataBaseUtil.deviceVoToDeviceDB(item));
        }
    }

    /**
     * Device表中插入单个DeviceDB，如果存在就将原来的数据覆盖
     *
     * @param context
     * @param item
     */
    public static void saveDevice(Context context, DeviceVo item) {
        DeviceDB dbItem = DataBaseUtil.deviceVoToDeviceDB(item);
        DbManager.getDaoSession(context).getDeviceDBDao().insertOrReplace(dbItem);
    }

    /**
     * Device表中插入DeviceDB列表，如果存在就将原来的数据覆盖
     *
     * @param context
     * @param list
     */
    public static void saveDevices(Context context, List<DeviceVo> list) {
        Log.d(TAG, "saveDevices()");
        if (null == list || list.size() <= 0) {
            return;
        }
        for (DeviceVo item : list) {
            Log.d(TAG, "save " + item.toString());
            DeviceDB dbItem = DataBaseUtil.deviceVoToDeviceDB(item);
            Log.d(TAG, dbItem.toString());
            DbManager.getDaoSession(context).getDeviceDBDao().insertOrReplace(dbItem);
        }
    }

    /**
     * 删除指定ID的Device
     *
     * @param context
     * @param itemId
     */
    public static void deleteDevice(Context context, int itemId) {
        DbManager.getDaoSession(context).getDeviceDBDao().deleteByKey((long) itemId);
    }

    /**
     * 批量删除指定ID的Device
     *
     * @param context
     * @param itemIds
     */
    public static void deleteDevices(Context context, int[] itemIds) {
        List<Long> ids = new ArrayList<>();
        for (int i : itemIds) {
            ids.add((long) i);
        }
        DbManager.getDaoSession(context).getDeviceDBDao().deleteByKeyInTx(ids);
    }

    /**
     * 删除指定devId的Device
     *
     * @param context
     * @param devId
     */
    public static void deleteDeviceByDevId(Context context, String devId) {
        DeviceDB dbItem = DbManager.getDaoSession(context).getDeviceDBDao().
                queryBuilder().where(DeviceDBDao.Properties.DevId.eq(devId)).build().unique();
        DbManager.getDaoSession(context).getDeviceDBDao().delete(dbItem);
    }

    /**
     * 批量删除指定devId的Device
     *
     * @param context
     * @param devIds
     */
    public static void deleteDevicesByDevId(Context context, String[] devIds) {
        for (String devId : devIds) {
            DeviceDB dbItem = DbManager.getDaoSession(context).getDeviceDBDao().
                    queryBuilder().where(DeviceDBDao.Properties.DevId.eq(devId)).build().unique();
            DbManager.getDaoSession(context).getDeviceDBDao().delete(dbItem);
        }
    }

    /**
     * 清空Device表
     *
     * @param context
     */
    public static void deleteAllDevices(Context context) {
        DbManager.getDaoSession(context).getDeviceDBDao().deleteAll();
    }

    /**
     * 根据Id查询DeviceVo
     * @param context
     * @param itemId
     * @return
     */
    public static DeviceVo queryDeviceVoById(Context context, int itemId) {
        return DataBaseUtil.deviceDBToDeviceVo(DbManager.getDaoSession(context).
                getDeviceDBDao().load((long) itemId));
    }

    /**
     * 根据devId查询DeviceVo
     * @param context
     * @param devId
     * @return
     */
    public static DeviceVo queryDeviceVoByDevId(Context context, String devId) {
        DeviceDB dbItem = DbManager.getDaoSession(context).getDeviceDBDao().
                queryBuilder().where(DeviceDBDao.Properties.DevId.eq(devId)).build().unique();
        if(dbItem != null) {
            return DataBaseUtil.deviceDBToDeviceVo(dbItem);
        }
        return null;
    }

    /**
     * 查询所有DeviceVo
     *
     * @param context
     * @return
     */
    public static List<DeviceVo> queryAllDeviceVos(Context context) {
        Log.d(TAG,"queryAllDeviceVos()");
        QueryBuilder<DeviceDB> builder = DbManager.getDaoSession(context).getDeviceDBDao().queryBuilder();
        List<DeviceDB> dbList = builder.build().list();
        Log.d(TAG, Boolean.toString(dbList == null) + " size > 0? " + Boolean.toString(dbList.size() > 0));
        List<DeviceVo> list = new ArrayList<>();
        for (DeviceDB dbItem : dbList) {
            list.add(DataBaseUtil.deviceDBToDeviceVo(dbItem));
            Log.d(TAG, dbItem.toString());
        }
        return list;
    }




}
