package cn.ffcs.ms.crm_mobile_v20.entities;

/**
 * Created by chenqq on 17/1/19.
 */

public class Message
{
    // 状 ? ? 参看Constant中的定义
    public String stateCode;

    // 错误信息存放节点
    public String message;

    // 具体内容
    public Object detail;

    // jason关键 ?
    public String keyId;
    // jason关键 ?
    public String keyValue;


    public String systemCode = "310";
    public String device = "ANDROID";

    private String logId = "9999";

    private String versionCode = "9999";

    private DeviceInfo deviceInfo = new DeviceInfo();

    public class DeviceInfo {
        private String sysVerId = "334";
        private String macAddr = "F4F951846F32";
        private String deviceId = "74973a995fe9ce24d134742e0a309d43adfa8778";
        private String deviceModel = "iPad";
        private String deviceSystem = "iPhone OS";
        private String deviceSystemVersion = "10.1.3";
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDetail() {
        return detail;
    }

    public void setDetail(Object detail) {
        this.detail = detail;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }
}
