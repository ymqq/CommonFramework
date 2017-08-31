package cn.ffcs.ms.crm_mobile_v20.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Vic on 16/12/23.
 */

@Entity
public class Banner {
    @Id
    private long id;
    private String url;
    private String type;

    @Generated(hash = 1395185014)
    public Banner(long id, String url, String type) {
        this.id = id;
        this.url = url;
        this.type = type;
    }

    @Generated(hash = 2026719322)
    public Banner() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
