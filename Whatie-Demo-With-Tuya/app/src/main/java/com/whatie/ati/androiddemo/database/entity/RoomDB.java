package com.whatie.ati.androiddemo.database.entity;

import com.whatie.ati.androiddemo.database.greenDao.DaoSession;
import com.whatie.ati.androiddemo.database.greenDao.DeviceDBDao;
import com.whatie.ati.androiddemo.database.greenDao.HomeDBDao;
import com.whatie.ati.androiddemo.database.greenDao.RoomDBDao;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import com.whatie.ati.androiddemo.database.greenDao.DaoSession;
import com.whatie.ati.androiddemo.database.greenDao.DeviceDBDao;
import com.whatie.ati.androiddemo.database.greenDao.HomeDBDao;
import com.whatie.ati.androiddemo.database.greenDao.RoomDBDao;

@Entity
public class RoomDB {
    @Id
    private Long roomId;

    private Long homeId;
    @ToOne(joinProperty = "homeId")
    private HomeDB homeDB;

    private String roomJson;

    @ToMany(referencedJoinProperty = "roomId")
    private List<DeviceDB> deviceDBs;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 247586510)
    private transient RoomDBDao myDao;

    @Generated(hash = 618405612)
    public RoomDB(Long roomId, Long homeId, String roomJson) {
        this.roomId = roomId;
        this.homeId = homeId;
        this.roomJson = roomJson;
    }

    @Generated(hash = 2128142708)
    public RoomDB() {
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

    public String getRoomJson() {
        return this.roomJson;
    }

    public void setRoomJson(String roomJson) {
        this.roomJson = roomJson;
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
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 802125079)
    public List<DeviceDB> getDeviceDBs() {
        if (deviceDBs == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DeviceDBDao targetDao = daoSession.getDeviceDBDao();
            List<DeviceDB> deviceDBsNew = targetDao._queryRoomDB_DeviceDBs(roomId);
            synchronized (this) {
                if (deviceDBs == null) {
                    deviceDBs = deviceDBsNew;
                }
            }
        }
        return deviceDBs;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 799906327)
    public synchronized void resetDeviceDBs() {
        deviceDBs = null;
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
    @Generated(hash = 332543221)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRoomDBDao() : null;
    }


}

