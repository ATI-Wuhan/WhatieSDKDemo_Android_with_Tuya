package com.whatie.ati.androiddemo.database.db;

import android.content.Context;

import com.whatie.ati.androiddemo.database.entity.SharingDeviceDB;
import com.whatie.ati.androiddemo.database.greenDao.SharingDeviceDBDao;
import com.d9lab.ati.whatiesdk.bean.DeviceVo;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class SharingDeviceDaoOpe {

    /**
     * SharingDevice表中插入单个SharingDeviceDB,如果ID存在会插入失败
     *
     * @param context
     * @param item
     */
    public static void insertSharingDevice(Context context, DeviceVo item) {

        SharingDeviceDB dbItem = DataBaseUtil.siDeviceToSiDeviceDB(item);
        DbManager.getDaoSession(context).getSharingDeviceDBDao().insert(dbItem);
    }

    /**
     * SharingDevice表中插入SharingDeviceDB列表
     *
     * @param context
     * @param list
     */
    public static void insertSharingDevices(Context context, List<DeviceVo> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        for (DeviceVo item : list) {
            DbManager.getDaoSession(context).getSharingDeviceDBDao().insert(DataBaseUtil.siDeviceToSiDeviceDB(item));

        }
    }

    /**
     * SharingDevice表中插入单个SharingDeviceDB，如果存在就将原来的数据覆盖
     *
     * @param context
     * @param item
     */
    public static void saveSharingDevice(Context context, DeviceVo item) {
        SharingDeviceDB dbItem = DataBaseUtil.siDeviceToSiDeviceDB(item);
        DbManager.getDaoSession(context).getSharingDeviceDBDao().insertOrReplace(dbItem);
    }

    /**
     * SharingDevice表中插入SharingDeviceDB列表，如果存在就将原来的数据覆盖
     *
     * @param context
     * @param list
     */
    public static void saveSharingDevices(Context context, List<DeviceVo> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        for (DeviceVo item : list) {
            DbManager.getDaoSession(context).getSharingDeviceDBDao().insertOrReplace(DataBaseUtil.siDeviceToSiDeviceDB(item));
        }
    }

    /**
     * 删除指定ID的SharingDevice
     *
     * @param context
     * @param itemId
     */
    public static void deleteSharingDevice(Context context, int itemId) {
        DbManager.getDaoSession(context).getSharingDeviceDBDao().deleteByKey((long) itemId);
    }

    /**
     * 批量删除指定ID的SharingDevice
     *
     * @param context
     * @param itemIds
     */
    public static void deleteSharingDevices(Context context, int[] itemIds) {
        List<Long> ids = new ArrayList<>();
        for (int i : itemIds) {
            ids.add((long) i);
        }
        DbManager.getDaoSession(context).getSharingDeviceDBDao().deleteByKeyInTx(ids);
    }

    /**
     * 清空SharingDevice表
     *
     * @param context
     */
    public static void deleteAllSharingDevices(Context context) {
        DbManager.getDaoSession(context).getSharingDeviceDBDao().deleteAll();
    }

    /**
     * 删除指定devId的Device
     *
     * @param context
     * @param devId
     */
    public static void deleteDeviceByDevId(Context context, String devId) {
        SharingDeviceDB dbItem = DbManager.getDaoSession(context).getSharingDeviceDBDao().
                queryBuilder().where(SharingDeviceDBDao.Properties.DevId.eq(devId)).build().unique();
        DbManager.getDaoSession(context).getSharingDeviceDBDao().delete(dbItem);
    }

    /**
     * 批量删除指定devId的Device
     *
     * @param context
     * @param devIds
     */
    public static void deleteDevicesByDevId(Context context, String[] devIds) {
        for (String devId : devIds) {
            SharingDeviceDB dbItem = DbManager.getDaoSession(context).getSharingDeviceDBDao().
                    queryBuilder().where(SharingDeviceDBDao.Properties.DevId.eq(devId)).build().unique();
            DbManager.getDaoSession(context).getSharingDeviceDBDao().delete(dbItem);
        }
    }

    /**
     * 根据Id查询SharingDevice
     * @param context
     * @param itemId
     * @return
     */
    public static DeviceVo querySharingDeviceById(Context context, int itemId) {
        return DataBaseUtil.siDeviceDBToSiDevice(DbManager.getDaoSession(context).
                getSharingDeviceDBDao().load((long) itemId));
    }

    /**
     * 查询所有SharingDevice
     *
     * @param context
     * @return
     */
    public static List<DeviceVo> queryAllSharingDevices(Context context) {
        QueryBuilder<SharingDeviceDB> builder = DbManager.getDaoSession(context).getSharingDeviceDBDao().queryBuilder();
        List<SharingDeviceDB> dbList = builder.build().list();
        List<DeviceVo> list = new ArrayList<>();
        for (SharingDeviceDB dbItem : dbList) {
            list.add(DataBaseUtil.siDeviceDBToSiDevice(dbItem));
        }
        return list;
    }

}
