package com.whatie.ati.androiddemo.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class SharedDeviceDB {
    @Id
    private Long sharedDeviceId;

    private String devId;

    private String deviceVoJson;

    @Generated(hash = 377370091)
    public SharedDeviceDB(Long sharedDeviceId, String devId, String deviceVoJson) {
        this.sharedDeviceId = sharedDeviceId;
        this.devId = devId;
        this.deviceVoJson = deviceVoJson;
    }

    @Generated(hash = 1905694815)
    public SharedDeviceDB() {
    }

    public Long getSharedDeviceId() {
        return this.sharedDeviceId;
    }

    public void setSharedDeviceId(Long sharedDeviceId) {
        this.sharedDeviceId = sharedDeviceId;
    }

    public String getDevId() {
        return this.devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getDeviceVoJson() {
        return this.deviceVoJson;
    }

    public void setDeviceVoJson(String deviceVoJson) {
        this.deviceVoJson = deviceVoJson;
    }

}
