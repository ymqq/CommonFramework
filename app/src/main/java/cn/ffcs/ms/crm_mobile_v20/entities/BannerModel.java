package cn.ffcs.ms.crm_mobile_v20.entities;

import android.support.annotation.NonNull;

import java.util.List;

import cn.ffcs.ms.crm_mobile_v20.greendao.gen.BannerDao;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Vic on 16/12/26.
 */

public class BannerModel implements IBannerModel {
    private BannerDao mDao;
    public BannerModel(BannerDao dao) {
        mDao = dao;
    }
    public void addBanner(Banner banner) {
        if (mDao.load(banner.getId()) == null) {
            mDao.insert(banner);
        } else {
            mDao.update(banner);
        }
    }
    public void addBanners(List<Banner> banners) {
        for (int i = 0, len = banners.size(); i < len; i++) {
            addBanner(banners.get(i));
        }
    }
    @Override
    public void loadAllBanners(@NonNull final GetBannersCallback callback) {
        mDao.rx()
                .loadAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Banner>>() {
                    @Override
                    public void call(List<Banner> banners) {
                        if (banners != null && banners.size() > 0) {
                            callback.onSuccessed(banners);
                        } else {
                            callback.onFailed("无Banner数据！");
                        }
                    }
                });
    }
    @Override
    public void loadBanner(@NonNull final Long bannerId, @NonNull final GetBannerCallback callback) {
        mDao.rx().load(bannerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Banner>() {
                    @Override
                    public void call(Banner banner) {
                        if (banner != null) {
                            callback.onSuccessed(banner);
                        } else {
                            callback.onFailed("无该ID:【" + bannerId + "】对应的Banner记录！");
                        }
                    }
                });
    }
}
