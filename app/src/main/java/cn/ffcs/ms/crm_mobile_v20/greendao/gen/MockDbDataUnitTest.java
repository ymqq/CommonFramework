package cn.ffcs.ms.crm_mobile_v20.greendao.gen;

import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.ffcs.ms.crm_mobile_v20.MyApplication;
import cn.ffcs.ms.crm_mobile_v20.entities.Banner;
import cn.ffcs.ms.crm_mobile_v20.entities.BannerModel;
import cn.ffcs.ms.crm_mobile_v20.entities.Order;
import cn.ffcs.ms.crm_mobile_v20.entities.OrderModel;
import cn.ffcs.ms.crm_mobile_v20.entities.Scene;
import cn.ffcs.ms.crm_mobile_v20.entities.SceneModel;

/**
 * Created by Vic on 16/12/24.
 */

public class MockDbDataUnitTest {

    public MockDbDataUnitTest() {

    }

    public void initDbData() {

        initBannerData();
        initSceneData();
        initOrderData();
    }

    private void initBannerData() {
        BannerModel bannerModel = new BannerModel(MyApplication.INSTANCE.getDaoSession().getBannerDao());
        List<Banner> banners = new ArrayList<>();

        Banner banner1 = new Banner((long)1, "http://ww2.sinaimg.cn/mw600/7f166dc7jw1erpdvadatnj20pu0iygow.jpg", "AD");
        banners.add(banner1);

        Banner banner2 = new Banner((long)2, "http://article.fd.zol-img.com.cn/t_s640x2000/g1/M07/0C/0D/Cg-4jlOpQXeIBoCqAAGvjZdPGQYAAOjOwDjYVcAAa-l268.jpg", "AD");
        banners.add(banner2);

        Banner banner3 = new Banner((long)3, "http://image.tianjimedia.com/uploadImages/2014/286/18/EPC9AWT3DOOQ.jpg", "AD");
        banners.add(banner3);

        Banner banner4 = new Banner((long)4, "http://easyread.ph.126.net/QFc4ZoIPbzoQ1G-AX0gl8g==/7916669537455527976.jpg", "AD");
        banners.add(banner4);

        bannerModel.addBanners(banners);
    }

    private void initSceneData() {
        SceneDao sceneDao = MyApplication.INSTANCE.getDaoSession().getSceneDao();
        SceneModel sceneModel = new SceneModel(sceneDao);
        List<Scene> scenes = new ArrayList<>();

        Scene scene1 = new Scene((long)1, "天翼", "", "SCENE", true);
        scenes.add(scene1);

        Scene scene2 = new Scene((long)2, "宽带", "", "SCENE", true);
        scenes.add(scene2);

        Scene scene3 = new Scene((long)3, "终端", "", "SCENE", true);
        scenes.add(scene3);

        Scene scene4 = new Scene((long)4, "iTV", "", "SCENE", true);
        scenes.add(scene4);

        Scene scene5 = new Scene((long)5, "帮助", "", "SCENE", true);
        scenes.add(scene5);

        Scene scene6 = new Scene((long)6, "设置", "", "SETTING", true);
        scenes.add(scene6);

        Scene scene7 = new Scene((long)7, "更多", "", "MORE", false);
        scenes.add(scene7);

        sceneModel.addScenes(scenes);
    }

    private Date getBeforeDate(int beforeYear, int beforeMonth, int beforeDay) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year - beforeYear, month - beforeMonth, day - beforeDay);
        return calendar.getTime();
    }

    private void initOrderData() {
        OrderDao orderDao = MyApplication.INSTANCE.getDaoSession().getOrderDao();
        OrderModel orderModel = new OrderModel(orderDao);
        List<Order> orders = new ArrayList<>();

        Order order1 = new Order((long)1, "天翼", getBeforeDate(1, 0, 0), 200,
                "天翼189套餐办理+华为Mate7 128G 银色", 80000, "AE98008991", "移动语音新装", "18906919988");
        orders.add(order1);

        Order order2 = new Order((long)2, "天翼", getBeforeDate(1, 0, 0), 400,
                "天翼189套餐办理+华为Mate7 128G 银色", 80000, "AE98008991", "移动语音新装", "18906919988");
        orders.add(order2);

        Order order3 = new Order((long)3, "天翼", getBeforeDate(0, 1, 0), 200,
                "天翼189套餐办理+华为Mate7 128G 银色", 80000, "AE98008991", "移动语音新装", "18906919988");
        orders.add(order3);

        Order order4 = new Order((long)4, "天翼", getBeforeDate(0, 1, 8), 200,
                "天翼189套餐办理+华为Mate7 128G 银色", 80000, "AE98008991", "移动语音新装", "18906919988");
        orders.add(order4);

        Order order5 = new Order((long)5, "宽带", getBeforeDate(0, 0, 3), 200,
                "有线宽带100M 光纤", 129900, "KD98008991", "有线宽带新装", "18906919988");
        orders.add(order5);

        Order order6 = new Order((long)6, "天翼", getBeforeDate(0, 0, 3), 200,
                "天翼189套餐办理+华为Mate7 32G 银色", 10000, "AE98008981", "移动语音新装", "18906919988");
        orders.add(order6);

        Order order7 = new Order((long)7, "天翼", getBeforeDate(0, 0, 2), 200,
                "乐享4G 169套餐 + iPhone7 128G 银色", 800000, "IP9900990", "移动语音新装", "17709519900");
        orders.add(order7);

        Order order8 = new Order((long)8, "天翼", getBeforeDate(0, 0, 2), 200,
                "天翼189套餐办理+华为Mate7 128G 银色", 80000, "AE98008991", "移动语音新装", "18906919988");
        orders.add(order8);

        Order order9 = new Order((long)9, "iTV", getBeforeDate(0, 0, 1), 200,
                "高清iTV 100M", 120000, "ITV8008991", "高清iTV新装", "18906919988");
        orders.add(order9);

        Order order10 = new Order((long)10, "天翼", getBeforeDate(0, 0, 0), 400,
                "天翼189套餐办理+华为Mate7 64G 黑色", 50000, "AE98008991", "移动语音新装", "18906919988");
        orders.add(order10);

        Order order11 = new Order((long)11, "天翼", getBeforeDate(0, 0, 0), 300,
                "天翼189套餐办理+华为Mate7 128G 银色", 80000, "AE98008991", "移动语音新装", "18906919988");
        orders.add(order11);

        orderModel.addOrders(orders);
    }

    public void readDbData() {
        readBannerData();
        readSceneData();
        readOrderData();
    }

    private void readBannerData() {
        BannerDao bannerDao = MyApplication.INSTANCE.getDaoSession().getBannerDao();

        List<Banner> banners = bannerDao.loadAll();
        for (Banner banner : banners) {
            Log.i("DBUNIT", "Banner Id: " + banner.getId());
        }
    }

    private void readSceneData() {
        SceneDao sceneDao = MyApplication.INSTANCE.getDaoSession().getSceneDao();

        List<Scene> scenes = sceneDao.loadAll();
        for (Scene scene : scenes) {
            Log.i("DBUNIT", "Scene Id: " + scene.getId() + ", Name: " + scene.getName());
        }
    }

    private void readOrderData() {
        OrderDao orderDao = MyApplication.INSTANCE.getDaoSession().getOrderDao();

        List<Order> orders = orderDao.loadAll();
        for (Order order : orders) {
            Log.i("DBUNIT", "Order Id: " + order.getId() + ", Scene: " + order.getScene() + ", Date: " + order.getTime());
        }
    }
}
