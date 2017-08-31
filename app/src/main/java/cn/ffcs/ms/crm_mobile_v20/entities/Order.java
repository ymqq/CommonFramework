package cn.ffcs.ms.crm_mobile_v20.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Vic on 16/12/23.
 * Order: id, type, time, status, desc, earning, meid, scene, phone
 */

@Entity
public class Order {
    @Id
    private long id;
    private String type;
    private Date time;
    private int status;
    private String desc;
    private int earning;
    private String meid;
    private String scene;
    private String phone;

    @Generated(hash = 1168593147)
    public Order(long id, String type, Date time, int status, String desc,
                 int earning, String meid, String scene, String phone) {
        this.id = id;
        this.type = type;
        this.time = time;
        this.status = status;
        this.desc = desc;
        this.earning = earning;
        this.meid = meid;
        this.scene = scene;
        this.phone = phone;
    }

    @Generated(hash = 1105174599)
    public Order() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTime() {
        return this.time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getEarning() {
        return this.earning;
    }

    public void setEarning(int earning) {
        this.earning = earning;
    }

    public String getMeid() {
        return this.meid;
    }

    public void setMeid(String meid) {
        this.meid = meid;
    }

    public String getScene() {
        return this.scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
