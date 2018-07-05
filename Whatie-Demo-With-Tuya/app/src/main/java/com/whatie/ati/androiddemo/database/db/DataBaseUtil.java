package com.whatie.ati.androiddemo.database.db;

import com.alibaba.fastjson.JSON;
import com.whatie.ati.androiddemo.database.entity.DeviceDB;
import com.whatie.ati.androiddemo.database.entity.HomeDB;
import com.whatie.ati.androiddemo.database.entity.RoomDB;
import com.whatie.ati.androiddemo.database.entity.SharedDeviceDB;
import com.whatie.ati.androiddemo.database.entity.SharingDeviceDB;
import com.d9lab.ati.whatiesdk.bean.DeviceVo;
import com.d9lab.ati.whatiesdk.bean.Home;
import com.d9lab.ati.whatiesdk.bean.Room;
import com.d9lab.ati.whatiesdk.bean.RoomVo;

import java.util.ArrayList;
import java.util.List;

public class DataBaseUtil {
    /**
     * 根据Home对象得到存入数据库的HomeDB对象
     * @param home
     * @return
     */
    public static HomeDB homeToHomeDB(Home home){
        Long homeId = (long)home.getId();
        String homeJson = JSON.toJSONString(home);
        return new HomeDB(homeId, homeJson);
    }

    /**
     * 根据数据库HomeDB对象得到Home对象
     * @param homeDB
     * @return
     */
    public static Home homeDBToHome(HomeDB homeDB){
        return JSON.parseObject(homeDB.getHomeJson(), Home.class);
    }

    /**
     * 根据Room对象得到存入数据库的RoomDB对象
     * @param room
     * @return
     */
    public static RoomDB roomToRoomDB(Room room){
        Long roomId = (long)room.getId();
        Long homeId = (long)room.getHome().getId();
        String roomJson = JSON.toJSONString(room);
        return new RoomDB(roomId, homeId, roomJson);
    }

    /**
     * 根据RoomVo对象得到存入数据库的RoomDB对象
     * @param roomVo
     * @return
     */
    public static RoomDB roomVoToRoomDB(RoomVo roomVo){
        Room room = roomVo.getRoom();
        Long roomId = (long)room.getId();
        Long homeId = (long)room.getHome().getId();
        String roomJson = JSON.toJSONString(room);
        return new RoomDB(roomId, homeId, roomJson);
    }

    /**
     * 根据数据库RoomDB对象得到Room对象
     * @param roomDB
     * @return
     */
    public static Room roomDBToRoom(RoomDB roomDB){
        return JSON.parseObject(roomDB.getRoomJson(), Room.class);
    }

    /**
     * 根据DeviceVo对象得到存入数据库的DeviceDB对象
     * @param deviceVo
     * @return
     */
    public static DeviceDB deviceVoToDeviceDB(DeviceVo deviceVo){
        Long deviceId = (long)deviceVo.getDevice().getId();
        String devId = deviceVo.getDevice().getDevId();
        Long roomId = (long)deviceVo.getRoomId();
        Long homeId = (long)deviceVo.getHomeId();
        String deviceVoJson = JSON.toJSONString(deviceVo);
        return new DeviceDB(deviceId, devId, roomId, homeId, deviceVoJson);
    }

    /**
     * 根据数据库DeviceDB对象得到DeviceVo对象
     * @param deviceDB
     * @return
     */
    public static DeviceVo deviceDBToDeviceVo(DeviceDB deviceDB){
        return JSON.parseObject(deviceDB.getDeviceVoJson(), DeviceVo.class);
    }

    /**
     * 根据数据库DeviceDB的list得到DeviceVo的list
     * @param deviceDBs
     * @return
     */
    public static List<DeviceVo> deviceDBsToDeviceVos(List<DeviceDB> deviceDBs){
        List<DeviceVo> list = new ArrayList<>();
        for (DeviceDB dbItem : deviceDBs){
            list.add(deviceDBToDeviceVo(dbItem));
        }
        return list;
    }

    /**
     * 根据DeviceVo对象得到存入数据库的SharedDeviceDB对象
     * @param deviceVo
     * @return
     */
    public static SharedDeviceDB sDeviceToSDeviceDB(DeviceVo deviceVo){
        Long deviceId = (long)deviceVo.getDevice().getId();
        String devId = deviceVo.getDevice().getDevId();
        String deviceVoJson = JSON.toJSONString(deviceVo.getDevice());
        return new SharedDeviceDB(deviceId, devId, deviceVoJson);
    }

    /**
     * 根据数据库SharedDeviceDB对象得到DeviceVo对象
     * @param sDeviceDB
     * @return
     */
    public static DeviceVo sDeviceDBToSDevice(SharedDeviceDB sDeviceDB){
        return JSON.parseObject(sDeviceDB.getDeviceVoJson(), DeviceVo.class);
    }

    /**
     * 根据DeviceVo对象得到存入数据库的SharingDeviceDB对象
     * @param deviceVo
     * @return
     */
    public static SharingDeviceDB siDeviceToSiDeviceDB(DeviceVo deviceVo){
        Long deviceId = (long)deviceVo.getDevice().getId();
        String devId = deviceVo.getDevice().getDevId();
        String deviceVoJson = JSON.toJSONString(deviceVo.getDevice());
        return new SharingDeviceDB(deviceId, devId, deviceVoJson);
    }

    /**
     * 根据数据库SharingDeviceDB对象得到DeviceVo对象
     * @param siDeviceDB
     * @return
     */
    public static DeviceVo siDeviceDBToSiDevice(SharingDeviceDB siDeviceDB){
        return JSON.parseObject(siDeviceDB.getDeviceVoJson(), DeviceVo.class);
    }

}
