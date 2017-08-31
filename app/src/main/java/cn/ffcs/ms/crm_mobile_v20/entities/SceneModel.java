package cn.ffcs.ms.crm_mobile_v20.entities;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.ffcs.ms.crm_mobile_v20.greendao.gen.SceneDao;
import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Vic on 16/12/26.
 */

public class SceneModel implements ISceneModel {

    private SceneDao mDao;

    public SceneModel(@NonNull SceneDao dao) {
        mDao = dao;
    }

    public void addScene(@NonNull Scene scene) {
        if (mDao.load(scene.getId()) == null) {
            mDao.insert(scene);
        } else {
            mDao.update(scene);
        }
    }

    public void addScenes(@NonNull List<Scene> scenes) {
        for (Scene scene : scenes) {
            addScene(scene);
        }
    }

    @Override
    public void loadAllScenes(@NonNull final GetScenesCallback callback) {
        mDao.rxPlain()
                .loadAll()
                .delay(2, TimeUnit.SECONDS) // 延时2s
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Scene>>() {
                    @Override
                    public void call(List<Scene> scenes) {
                        Log.d("LoadScene", "--------");
                        if (scenes != null && scenes.size() > 0) {
                            callback.onSuccessed(scenes);
                        } else {
                            callback.onFailed("无Scene数据！");
                        }
                    }
                });
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    List<Scene> scenes = mDao.loadAll();
                    if (scenes != null && scenes.size() > 0) {
                        callback.onSuccessed(scenes);
                    } else {
                        callback.onFailed("无Scene数据！");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        */
    }
}
