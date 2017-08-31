package cn.ffcs.ms.crm_mobile_v20.main.home;

import java.util.List;

import cn.ffcs.itbg.itpd.core.Base.BaseMvpPresenter;
import cn.ffcs.itbg.itpd.core.Base.BaseMvpView;
import cn.ffcs.ms.crm_mobile_v20.entities.Banner;
import cn.ffcs.ms.crm_mobile_v20.entities.Scene;

/**
 * Created by Vic on 16/12/22.
 * 采用Google官方FragmentLess实现方案，由于APP结构为tab方式，所以fragment充当方案中的Activity角色，
 * 负责组装M、V、P
 */

public interface MainTabHomeConstract {

    interface View extends BaseMvpView<Presenter> {
        void showBanners(List<Banner> banners);
        void showBusinessScenes(List<Scene> scenes);
        void openBusinessScene(Scene scene);
        void showMessage(String message);
    }

    interface Presenter extends BaseMvpPresenter {
        // 加载广告图片
        void loadBanners();
        // 加载业务场景配置
        void loadBusinessScenes();
    }
}
