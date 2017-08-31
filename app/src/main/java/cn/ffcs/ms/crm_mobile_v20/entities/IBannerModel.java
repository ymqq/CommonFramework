package cn.ffcs.ms.crm_mobile_v20.entities;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Vic on 16/12/26.
 */

public interface IBannerModel {

    interface GetBannersCallback {
        void onSuccessed(List<Banner> banners);
        void onFailed(String err);
    }

    interface GetBannerCallback {
        void onSuccessed(Banner banner);
        void onFailed(String err);
    }

    void loadAllBanners(@NonNull GetBannersCallback callback);
    void loadBanner(@NonNull Long bannerId, @NonNull GetBannerCallback callback);
}
