package com.whatie.ati.androiddemo.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class SharingDeviceDB {
    @Id
    private Long sharingDeviceId;

    private String devId;

    private String deviceVoJson;

    @Generated(hash = 140652513)
    public SharingDeviceDB(Long sharingDeviceId, String devId,
            String deviceVoJson) {
        this.sharingDeviceId = sharingDeviceId;
        this.devId = devId;
        this.deviceVoJson = deviceVoJson;
    }

    @Generated(hash = 1828471799)
    public SharingDeviceDB() {
    }

    public Long getSharingDeviceId() {
        return this.sharingDeviceId;
    }

    public void setSharingDeviceId(Long sharingDeviceId) {
        this.sharingDeviceId = sharingDeviceId;
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
