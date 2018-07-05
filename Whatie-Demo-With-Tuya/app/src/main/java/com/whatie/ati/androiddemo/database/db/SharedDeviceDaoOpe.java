package com.whatie.ati.androiddemo.database.db;

import android.content.Context;

import com.whatie.ati.androiddemo.database.entity.SharedDeviceDB;
import com.whatie.ati.androiddemo.database.greenDao.SharedDeviceDBDao;
import com.d9lab.ati.whatiesdk.bean.DeviceVo;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class SharedDeviceDaoOpe {

    /**
     * SharedDevice表中插入单个SharedDeviceDB,如果ID存在会插入失败
     *
     * @param context
     * @param item
     */
    public static void insertSharedDevice(Context context, DeviceVo item) {

        SharedDeviceDB dbItem = DataBaseUtil.sDeviceToSDeviceDB(item);
        DbManager.getDaoSession(context).getSharedDeviceDBDao().insert(dbItem);
    }

    /**
     * SharedDevice表中插入SharedDeviceDB列表
     *
     * @param context
     * @param list
     */
    public static void insertSharedDevices(Context context, List<DeviceVo> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        for (DeviceVo item : list) {
            DbManager.getDaoSession(context).getSharedDeviceDBDao().insert(DataBaseUtil.sDeviceToSDeviceDB(item));

        }
    }

    /**
     * SharedDevice表中插入单个SharedDeviceDB，如果存在就将原来的数据覆盖
     *
     * @param context
     * @param item
     */
    public static void saveSharedDevice(Context context, DeviceVo item) {
        SharedDeviceDB dbItem = DataBaseUtil.sDeviceToSDeviceDB(item);
        DbManager.getDaoSession(context).getSharedDeviceDBDao().insertOrReplace(dbItem);
    }

    /**
     * SharedDevice表中插入SharedDeviceDB列表，如果存在就将原来的数据覆盖
     *
     * @param context
     * @param list
     */
    public static void saveSharedDevices(Context context, List<DeviceVo> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        for (DeviceVo item : list) {
            DbManager.getDaoSession(context).getSharedDeviceDBDao().insertOrReplace(DataBaseUtil.sDeviceToSDeviceDB(item));
        }
    }

    /**
     * 删除指定ID的SharedDevice
     *
     * @param context
     * @param itemId
     */
    public static void deleteSharedDevice(Context context, int itemId) {
        DbManager.getDaoSession(context).getSharedDeviceDBDao().deleteByKey((long) itemId);
    }

    /**
     * 批量删除指定ID的SharedDevice
     *
     * @param context
     * @param itemIds
     */
    public static void deleteSharedDevices(Context context, int[] itemIds) {
        List<Long> ids = new ArrayList<>();
        for (int i : itemIds) {
            ids.add((long) i);
        }
        DbManager.getDaoSession(context).getSharedDeviceDBDao().deleteByKeyInTx(ids);
    }

    /**
     * 清空SharedDevice表
     *
     * @param context
     */
    public static void deleteAllSharedDevices(Context context) {
        DbManager.getDaoSession(context).getSharedDeviceDBDao().deleteAll();
    }

    /**
     * 删除指定devId的Device
     *
     * @param context
     * @param devId
     */
    public static void deleteDeviceByDevId(Context context, String devId) {
        SharedDeviceDB dbItem = DbManager.getDaoSession(context).getSharedDeviceDBDao().
                queryBuilder().where(SharedDeviceDBDao.Properties.DevId.eq(devId)).build().unique();
        DbManager.getDaoSession(context).getSharedDeviceDBDao().delete(dbItem);
    }

    /**
     * 批量删除指定devId的Device
     *
     * @param context
     * @param devIds
     */
    public static void deleteDevicesByDevId(Context context, String[] devIds) {
        for (String devId : devIds) {
            SharedDeviceDB dbItem = DbManager.getDaoSession(context).getSharedDeviceDBDao().
                    queryBuilder().where(SharedDeviceDBDao.Properties.DevId.eq(devId)).build().unique();
            DbManager.getDaoSession(context).getSharedDeviceDBDao().delete(dbItem);
        }
    }

    /**
     * 根据Id查询SharedDevice
     * @param context
     * @param itemId
     * @return
     */
    public static DeviceVo querySharedDeviceById(Context context, int itemId) {
        return DataBaseUtil.sDeviceDBToSDevice(DbManager.getDaoSession(context).
                getSharedDeviceDBDao().load((long) itemId));
    }

    /**
     * 查询所有SharedDevice
     *
     * @param context
     * @return
     */
    public static List<DeviceVo> queryAllSharedDevices(Context context) {
        QueryBuilder<SharedDeviceDB> builder = DbManager.getDaoSession(context).getSharedDeviceDBDao().queryBuilder();
        List<SharedDeviceDB> dbList = builder.build().list();
        List<DeviceVo> list = new ArrayList<>();
        for (SharedDeviceDB dbItem : dbList) {
            list.add(DataBaseUtil.sDeviceDBToSDevice(dbItem));
        }
        return list;
    }

}
