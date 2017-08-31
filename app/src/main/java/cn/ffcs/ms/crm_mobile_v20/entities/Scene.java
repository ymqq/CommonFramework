package cn.ffcs.ms.crm_mobile_v20.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Vic on 16/12/23.
 * Scene: id, name, iconUrl, type, visiablity
 */
@Entity
public class Scene {
    @Id
    private long id;
    private String name;
    private String iconUrl;
    private String type;
    private boolean visiablity;

    @Generated(hash = 274782786)
    public Scene(long id, String name, String iconUrl, String type,
                 boolean visiablity) {
        this.id = id;
        this.name = name;
        this.iconUrl = iconUrl;
        this.type = type;
        this.visiablity = visiablity;
    }

    @Generated(hash = 1074887510)
    public Scene() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getVisiablity() {
        return this.visiablity;
    }

    public void setVisiablity(boolean visiablity) {
        this.visiablity = visiablity;
    }
}
