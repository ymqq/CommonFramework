package cn.ffcs.ms.crm_mobile_v20.main.home;

import java.util.ArrayList;
import java.util.List;

import cn.ffcs.ms.crm_mobile_v20.entities.Banner;
import cn.ffcs.ms.crm_mobile_v20.entities.IBannerModel;
import cn.ffcs.ms.crm_mobile_v20.entities.ISceneModel;
import cn.ffcs.ms.crm_mobile_v20.entities.Scene;

/**
 * Created by Vic on 16/12/22.
 */

public class MainTabHomePresenter implements MainTabHomeConstract.Presenter {
    private MainTabHomeConstract.View mView;
    private IBannerModel mBannerModel;
    private ISceneModel mSceneModel;

    public MainTabHomePresenter(MainTabHomeConstract.View view, IBannerModel bannerModel, ISceneModel sceneModel) {
        mView = view;
        mBannerModel = bannerModel;
        mSceneModel = sceneModel;
    }

    @Override
    public void loadBanners() {
        mBannerModel.loadAllBanners(new IBannerModel.GetBannersCallback() {
            @Override
            public void onSuccessed(List<Banner> banners) {
                mView.showBanners(banners);
            }

            @Override
            public void onFailed(String err) {
                mView.showMessage(err);
            }
        });
    }

    @Override
    public void loadBusinessScenes() {
        mSceneModel.loadAllScenes(new ISceneModel.GetScenesCallback() {
            @Override
            public void onSuccessed(List<Scene> scenes) {
                mView.showBusinessScenes(scenes);
            }

            @Override
            public void onFailed(String err) {
                mView.showMessage(err);
            }
        });
    }

    @Override
    public void start() {
        loadBanners();
        loadBusinessScenes();
    }
}
