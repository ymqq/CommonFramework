package cn.ffcs.itbg.itpd.core.Base;

/**
 * Created by chenqq on 17/2/14.
 * 基础回调方法，可在该基类上扩展
 */

public interface BaseCallback {
    void onSuccess(Object result);
    void onFailure(Object result);
}
