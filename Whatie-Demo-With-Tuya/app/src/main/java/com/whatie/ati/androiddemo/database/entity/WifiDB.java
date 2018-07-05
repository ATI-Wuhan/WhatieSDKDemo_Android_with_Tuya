package com.whatie.ati.androiddemo.database.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by Administrator on 2018\6\11 0011.
 */

@Entity
public class WifiDB {
    @Id
    private Long id;
    @Unique
    private String name;
    private String password;
    @Generated(hash = 1981398483)
    public WifiDB(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public WifiDB(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Generated(hash = 692129723)
    public WifiDB() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
