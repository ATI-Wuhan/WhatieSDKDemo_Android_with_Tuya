package com.whatie.ati.androiddemo.database.entity;


import com.whatie.ati.androiddemo.database.greenDao.DaoSession;
import com.whatie.ati.androiddemo.database.greenDao.DeviceDBDao;
import com.whatie.ati.androiddemo.database.greenDao.HomeDBDao;
import com.whatie.ati.androiddemo.database.greenDao.RoomDBDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import com.whatie.ati.androiddemo.database.greenDao.DaoSession;
import com.whatie.ati.androiddemo.database.greenDao.HomeDBDao;
import com.whatie.ati.androiddemo.database.greenDao.RoomDBDao;
import com.whatie.ati.androiddemo.database.greenDao.DeviceDBDao;

@Entity
public class DeviceDB {
    @Id
    private Long deviceId;

    private String devId;

    private Long roomId;
    @ToOne(joinProperty = "roomId")
    private RoomDB roomDB;

    private Long homeId;
    @ToOne(joinProperty = "homeId")
    private HomeDB homeDB;

    private String deviceVoJson;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1791253388)
    private transient DeviceDBDao myDao;

    @Generated(hash = 1489217749)
    public DeviceDB(Long deviceId, String devId, Long roomId, Long homeId,
            String deviceVoJson) {
        this.deviceId = deviceId;
        this.devId = devId;
        this.roomId = roomId;
        this.homeId = homeId;
        this.deviceVoJson = deviceVoJson;
    }

    @Generated(hash = 1363222787)
    public DeviceDB() {
    }

    public Long getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDevId() {
        return this.devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public Long getRoomId() {
        return this.roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getHomeId() {
        return this.homeId;
    }

    public void setHomeId(Long homeId) {
        this.homeId = homeId;
    }

    public String getDeviceVoJson() {
        return this.deviceVoJson;
    }

    public void setDeviceVoJson(String deviceVoJson) {
        this.deviceVoJson = deviceVoJson;
    }

    @Generated(hash = 164045810)
    private transient Long roomDB__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1680880222)
    public RoomDB getRoomDB() {
        Long __key = this.roomId;
        if (roomDB__resolvedKey == null || !roomDB__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RoomDBDao targetDao = daoSession.getRoomDBDao();
            RoomDB roomDBNew = targetDao.load(__key);
            synchronized (this) {
                roomDB = roomDBNew;
                roomDB__resolvedKey = __key;
            }
        }
        return roomDB;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 73684829)
    public void setRoomDB(RoomDB roomDB) {
        synchronized (this) {
            this.roomDB = roomDB;
            roomId = roomDB == null ? null : roomDB.getRoomId();
            roomDB__resolvedKey = roomId;
        }
    }

    @Generated(hash = 1750587761)
    private transient Long homeDB__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 370588453)
    public HomeDB getHomeDB() {
        Long __key = this.homeId;
        if (homeDB__resolvedKey == null || !homeDB__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            HomeDBDao targetDao = daoSession.getHomeDBDao();
            HomeDB homeDBNew = targetDao.load(__key);
            synchronized (this) {
                homeDB = homeDBNew;
                homeDB__resolvedKey = __key;
            }
        }
        return homeDB;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 833687473)
    public void setHomeDB(HomeDB homeDB) {
        synchronized (this) {
            this.homeDB = homeDB;
            homeId = homeDB == null ? null : homeDB.getHomeId();
            homeDB__resolvedKey = homeId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 409199162)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDeviceDBDao() : null;
    }

}
