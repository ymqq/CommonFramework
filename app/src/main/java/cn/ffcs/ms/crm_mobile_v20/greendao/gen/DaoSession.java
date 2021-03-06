package cn.ffcs.ms.crm_mobile_v20.greendao.gen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import cn.ffcs.ms.crm_mobile_v20.entities.Banner;
import cn.ffcs.ms.crm_mobile_v20.entities.Order;
import cn.ffcs.ms.crm_mobile_v20.entities.Scene;

import cn.ffcs.ms.crm_mobile_v20.greendao.gen.BannerDao;
import cn.ffcs.ms.crm_mobile_v20.greendao.gen.OrderDao;
import cn.ffcs.ms.crm_mobile_v20.greendao.gen.SceneDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig bannerDaoConfig;
    private final DaoConfig orderDaoConfig;
    private final DaoConfig sceneDaoConfig;

    private final BannerDao bannerDao;
    private final OrderDao orderDao;
    private final SceneDao sceneDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        bannerDaoConfig = daoConfigMap.get(BannerDao.class).clone();
        bannerDaoConfig.initIdentityScope(type);

        orderDaoConfig = daoConfigMap.get(OrderDao.class).clone();
        orderDaoConfig.initIdentityScope(type);

        sceneDaoConfig = daoConfigMap.get(SceneDao.class).clone();
        sceneDaoConfig.initIdentityScope(type);

        bannerDao = new BannerDao(bannerDaoConfig, this);
        orderDao = new OrderDao(orderDaoConfig, this);
        sceneDao = new SceneDao(sceneDaoConfig, this);

        registerDao(Banner.class, bannerDao);
        registerDao(Order.class, orderDao);
        registerDao(Scene.class, sceneDao);
    }
    
    public void clear() {
        bannerDaoConfig.clearIdentityScope();
        orderDaoConfig.clearIdentityScope();
        sceneDaoConfig.clearIdentityScope();
    }

    public BannerDao getBannerDao() {
        return bannerDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public SceneDao getSceneDao() {
        return sceneDao;
    }

}
