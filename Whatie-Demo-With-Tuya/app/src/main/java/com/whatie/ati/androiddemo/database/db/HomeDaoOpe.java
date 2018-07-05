package com.whatie.ati.androiddemo.database.db;

import android.content.Context;
import android.util.Log;

import com.whatie.ati.androiddemo.database.entity.HomeDB;
import com.d9lab.ati.whatiesdk.bean.Home;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class HomeDaoOpe {

    private static final String TAG = "HomeDaoOpe";

    /**
     * Home表中插入单个HomeDB,如果ID存在会插入失败
     *
     * @param context
     * @param item
     */
    public static void insertHome(Context context, Home item) {

        HomeDB dbItem = DataBaseUtil.homeToHomeDB(item);
        DbManager.getDaoSession(context).getHomeDBDao().insert(dbItem);
    }

    /**
     * Home表中插入HomeDB列表
     *
     * @param context
     * @param list
     */
    public static void insertHomes(Context context, List<Home> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        for (Home item : list) {
            DbManager.getDaoSession(context).getHomeDBDao().insert(DataBaseUtil.homeToHomeDB(item));

        }
    }

    /**
     * Home表中插入单个HomeDB，如果存在就将原来的数据覆盖
     *
     * @param context
     * @param item
     */
    public static void saveHome(Context context, Home item) {
        HomeDB dbItem = DataBaseUtil.homeToHomeDB(item);
        DbManager.getDaoSession(context).getHomeDBDao().insertOrReplace(dbItem);
    }

    /**
     * Home表中插入HomeDB列表,如果存在就将原来的覆盖
     *
     * @param context
     * @param list
     */
    public static void saveHomes(Context context, List<Home> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        for (Home item : list) {
            HomeDB dbItem = DataBaseUtil.homeToHomeDB(item);
            Log.d(TAG, dbItem.toString());
            DbManager.getDaoSession(context).getHomeDBDao().insertOrReplace(dbItem);
        }
    }

    /**
     * 删除指定ID的Home
     *
     * @param context
     * @param itemId
     */
    public static void deleteHome(Context context, int itemId) {
        DbManager.getDaoSession(context).getHomeDBDao().deleteByKey((long) itemId);
    }

    /**
     * 批量删除指定ID的Home
     *
     * @param context
     * @param itemIds
     */
    public static void deleteHomes(Context context, int[] itemIds) {
        List<Long> ids = new ArrayList<>();
        for (int i : itemIds) {
            ids.add((long) i);
        }
        DbManager.getDaoSession(context).getHomeDBDao().deleteByKeyInTx(ids);
    }

    /**
     * 清空Home表
     *
     * @param context
     */
    public static void deleteAllHomes(Context context) {
        DbManager.getDaoSession(context).getHomeDBDao().deleteAll();
    }

    /**
     * 根据Id查询Home
     * @param context
     * @param itemId
     * @return
     */
    public static Home queryHomeById(Context context, int itemId) {
        return DataBaseUtil.homeDBToHome(DbManager.getDaoSession(context).
                getHomeDBDao().load((long) itemId));
    }

    /**
     * 根据Id查询HomeDB(用来查询家庭里面的设备以及房间)
     * @param context
     * @param itemId
     * @return
     */
    public static HomeDB queryHomeDBById(Context context, int itemId) {
        HomeDB dbItem = DbManager.getDaoSession(context).getHomeDBDao().load((long) itemId);
        if(dbItem != null){
            Log.d(TAG, dbItem.toString());
        }
        return dbItem;
    }

    /**
     * 查询所有Home
     *
     * @param context
     * @return
     */
    public static List<Home> queryAllHomes(Context context) {
        QueryBuilder<HomeDB> builder = DbManager.getDaoSession(context).getHomeDBDao().queryBuilder();
        List<HomeDB> dbList = builder.build().list();
        List<Home> list = new ArrayList<>();
        for (HomeDB dbItem : dbList) {
            list.add(DataBaseUtil.homeDBToHome(dbItem));
        }
        return list;
    }


}
