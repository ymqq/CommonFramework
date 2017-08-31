package cn.ffcs.ms.crm_mobile_v20.entities;

/**
 * Created by chenqq on 17/1/19.
 */

public class PROMOT_BUSINESS_LOGIN_REQ {
    protected String staff_CODE;
    protected String password;

    public String getStaff_CODE() {
        return staff_CODE;
    }

    public void setStaff_CODE(String staff_CODE) {
        this.staff_CODE = staff_CODE;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
