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

import java.util.List;
import com.whatie.ati.androiddemo.database.greenDao.DaoSession;
import com.whatie.ati.androiddemo.database.greenDao.DeviceDBDao;
import com.whatie.ati.androiddemo.database.greenDao.RoomDBDao;
import com.whatie.ati.androiddemo.database.greenDao.HomeDBDao;

@Entity
public class HomeDB {
    @Id
    private Long homeId;
    private String homeJson;

    @ToMany(referencedJoinProperty = "homeId")
    private List<RoomDB> roomDBs;

    @ToMany(referencedJoinProperty = "homeId")
    private List<DeviceDB> deviceDBs;
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 732537959)
    private transient HomeDBDao myDao;

    @Generated(hash = 149257568)
    public HomeDB(Long homeId, String homeJson) {
        this.homeId = homeId;
        this.homeJson = homeJson;
    }

    @Generated(hash = 494549957)
    public HomeDB() {
    }

    public Long getHomeId() {
        return this.homeId;
    }

    public void setHomeId(Long homeId) {
        this.homeId = homeId;
    }

    public String getHomeJson() {
        return this.homeJson;
    }

    public void setHomeJson(String homeJson) {
        this.homeJson = homeJson;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 249298476)
    public List<RoomDB> getRoomDBs() {
        if (roomDBs == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RoomDBDao targetDao = daoSession.getRoomDBDao();
            List<RoomDB> roomDBsNew = targetDao._queryHomeDB_RoomDBs(homeId);
            synchronized (this) {
                if (roomDBs == null) {
                    roomDBs = roomDBsNew;
                }
            }
        }
        return roomDBs;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 206770554)
    public synchronized void resetRoomDBs() {
        roomDBs = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 105298641)
    public List<DeviceDB> getDeviceDBs() {
        if (deviceDBs == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            DeviceDBDao targetDao = daoSession.getDeviceDBDao();
            List<DeviceDB> deviceDBsNew = targetDao._queryHomeDB_DeviceDBs(homeId);
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
    @Generated(hash = 1042735943)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getHomeDBDao() : null;
    }

}
