package com.whatie.ati.androiddemo.database.db;

import android.content.Context;

import com.whatie.ati.androiddemo.database.entity.RoomDB;
import com.d9lab.ati.whatiesdk.bean.Room;
import com.d9lab.ati.whatiesdk.bean.RoomVo;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class RoomDaoOpe {

    /**
     * Room表中插入单个RoomDB,如果ID存在会插入失败
     *
     * @param context
     * @param item
     */
    public static void insertRoom(Context context, Room item) {

        RoomDB dbItem = DataBaseUtil.roomToRoomDB(item);
        DbManager.getDaoSession(context).getRoomDBDao().insert(dbItem);
    }

    /**
     * Room表中插入RoomDB列表
     *
     * @param context
     * @param list
     */
    public static void insertRooms(Context context, List<Room> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        for (Room item : list) {
            DbManager.getDaoSession(context).getRoomDBDao().insert(DataBaseUtil.roomToRoomDB(item));

        }
    }

    /**
     * Room表中插入单个RoomDB，如果存在就将原来的数据覆盖
     *
     * @param context
     * @param item
     */
    public static void saveRoom(Context context, Room item) {
        RoomDB dbItem = DataBaseUtil.roomToRoomDB(item);
        DbManager.getDaoSession(context).getRoomDBDao().insertOrReplace(dbItem);
    }


    /**
     * Room表中插入RoomDB列表(通过List<RoomVo>),如果存在就将原来的覆盖
     *
     * @param context
     * @param list
     */
    public static void saveRoomVos(Context context, List<RoomVo> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        for (RoomVo item : list) {
            DbManager.getDaoSession(context).getRoomDBDao().insertOrReplace(DataBaseUtil.roomVoToRoomDB(item));
        }
    }

    /**
     * Room表中插入RoomDB列表,如果存在就将原来的覆盖
     *
     * @param context
     * @param list
     */
    public static void saveRooms(Context context, List<Room> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        for (Room item : list) {
            DbManager.getDaoSession(context).getRoomDBDao().insertOrReplace(DataBaseUtil.roomToRoomDB(item));

        }
    }

    /**
     * 删除指定ID的Room
     *
     * @param context
     * @param itemId
     */
    public static void deleteRoom(Context context, int itemId) {
        DbManager.getDaoSession(context).getRoomDBDao().deleteByKey((long) itemId);
    }

    /**
     * 批量删除指定ID的Room
     *
     * @param context
     * @param itemIds
     */
    public static void deleteRooms(Context context, int[] itemIds) {
        List<Long> ids = new ArrayList<>();
        for (int i : itemIds) {
            ids.add((long) i);
        }
        DbManager.getDaoSession(context).getRoomDBDao().deleteByKeyInTx(ids);
    }

    /**
     * 清空Room表
     *
     * @param context
     */
    public static void deleteAllRooms(Context context) {
        DbManager.getDaoSession(context).getRoomDBDao().deleteAll();
    }

    /**
     * 根据Id查询Room
     * @param context
     * @param itemId
     * @return
     */
    public static Room queryRoomById(Context context, int itemId) {
        return DataBaseUtil.roomDBToRoom(DbManager.getDaoSession(context).
                getRoomDBDao().load((long) itemId));
    }

    /**
     * 查询所有Room
     *
     * @param context
     * @return
     */
    public static List<Room> queryAllRooms(Context context) {
        QueryBuilder<RoomDB> builder = DbManager.getDaoSession(context).getRoomDBDao().queryBuilder();
        List<RoomDB> dbList = builder.build().list();
        List<Room> list = new ArrayList<>();
        for (RoomDB dbItem : dbList) {
            list.add(DataBaseUtil.roomDBToRoom(dbItem));
        }
        return list;
    }


}
