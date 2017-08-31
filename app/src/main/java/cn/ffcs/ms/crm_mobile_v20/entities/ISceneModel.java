package cn.ffcs.ms.crm_mobile_v20.entities;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Vic on 16/12/26.
 */

public interface ISceneModel {

    interface GetScenesCallback {
        void onSuccessed(List<Scene> scenes);
        void onFailed(String err);
    }

    void loadAllScenes(@NonNull GetScenesCallback callback);
}
