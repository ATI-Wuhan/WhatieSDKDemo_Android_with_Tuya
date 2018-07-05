package com.whatie.ati.androiddemo.database.db;

import android.content.Context;

import com.whatie.ati.androiddemo.database.entity.WifiDB;
import com.whatie.ati.androiddemo.database.greenDao.WifiDBDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018\6\11 0011.
 */

public class WifiDaoOpe {
    /**
     * Wifi表中插入单个WifiDB,如果ID存在会插入失败
     *
     * @param context
     * @param item
     */
    public static void insertWifi(Context context, WifiDB item) {
        DbManager.getDaoSession(context).getWifiDBDao().insert(item);
    }

    /**
     * Wifi表中插入WifiDB列表
     *
     * @param context
     * @param list
     */
    public static void insertWifis(Context context, List<WifiDB> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        for (WifiDB item : list) {
            DbManager.getDaoSession(context).getWifiDBDao().insert(item);
        }
    }

    /**
     * Wifi表中插入单个WifiDB，如果存在就将原来的数据覆盖
     *
     * @param context
     * @param item
     */
    public static void saveWifi(Context context, WifiDB item) {
        DbManager.getDaoSession(context).getWifiDBDao().insertOrReplace(item);
    }

    /**
     * Wifi表中插入WifiDB列表，如果存在就将原来的数据覆盖
     *
     * @param context
     * @param list
     */
    public static void saveWifis(Context context, List<WifiDB> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        for (WifiDB item : list) {
            DbManager.getDaoSession(context).getWifiDBDao().insertOrReplace(item);

        }
    }

    /**
     * 删除指定ID的Wifi
     *
     * @param context
     * @param itemId
     */
    public static void deleteWifi(Context context, int itemId) {
        DbManager.getDaoSession(context).getWifiDBDao().deleteByKey((long) itemId);
    }

    /**
     * 批量删除指定ID的Wifi
     *
     * @param context
     * @param itemIds
     */
    public static void deleteWifis(Context context, int[] itemIds) {
        List<Long> ids = new ArrayList<>();
        for (int i : itemIds) {
            ids.add((long) i);
        }
        DbManager.getDaoSession(context).getWifiDBDao().deleteByKeyInTx(ids);
    }

    /**
     * 清空Wifi表
     *
     * @param context
     */
    public static void deleteAllWifis(Context context) {
        DbManager.getDaoSession(context).getWifiDBDao().deleteAll();
    }

    /**
     * 根据Id查询Wifi
     * @param context
     * @param itemId
     * @return
     */
    public static WifiDB queryWifiById(Context context, int itemId) {
        return DbManager.getDaoSession(context).getWifiDBDao().load((long) itemId);
    }

    /**
     * 根据name查询Wifi
     * @param context
     * @param name
     * @return
     */
    public static WifiDB queryWifiByName(Context context, String name) {
        return DbManager.getDaoSession(context).getWifiDBDao().queryBuilder().where(WifiDBDao.Properties.Name.eq(name)).build().unique();
    }

    /**
     * 查询所有Wifi
     *
     * @param context
     * @return
     */
    public static List<WifiDB> queryAllRooms(Context context) {
        QueryBuilder<WifiDB> builder = DbManager.getDaoSession(context).getWifiDBDao().queryBuilder();
        List<WifiDB> dbList = builder.build().list();
        List<WifiDB> list = new ArrayList<>();
        for (WifiDB dbItem : dbList) {
            list.add(dbItem);
        }
        return list;
    }

}
