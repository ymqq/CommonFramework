package cn.ffcs.ms.crm_mobile_v20.entities;

/**
 * Created by chenqq on 17/2/13.
 */

public class MessagePackage {
    private Message msg;

    public MessagePackage() {

    }

    public void setMessage(Message message) {
        msg = message;
    }

    public Message getMessage() {
        return msg;
    }
}
