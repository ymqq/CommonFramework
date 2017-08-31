package cn.ffcs.ms.crm_mobile_v20;

import android.database.sqlite.SQLiteDatabase;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.io.File;

import cn.ffcs.itbg.itpd.core.Base.BaseApplication;
import cn.ffcs.ms.crm_mobile_v20.greendao.gen.DaoMaster;
import cn.ffcs.ms.crm_mobile_v20.greendao.gen.DaoSession;
import cn.ffcs.ms.crm_mobile_v20.greendao.gen.MockDbDataUnitTest;

/**
 * Created by Vic on 16/12/23.
 */

public class MyApplication extends BaseApplication {

    public static MyApplication INSTANCE;

    private DaoMaster.DevOpenHelper mDevOpenHelper;
    private SQLiteDatabase mDb;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;


    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;

        setupLeakCanary();

        initDatabase();

        if (BuildConfig.IS_ON_CRASH_CAPTURE) {
            setupCrashCaptureHandler();
        }

        ZXingLibrary.initDisplayOpinion(this);

        // init db data test
        MockDbDataUnitTest mockDbDataUnitTest = new MockDbDataUnitTest();
        try {
            mockDbDataUnitTest.initDbData();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mockDbDataUnitTest.readDbData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void uploadCrashLog(File[] files) {
        // TODO 这里实现Crash日志文件上传
    }

    @Override
    protected String getAppPackageName() {
        return "";
    }

    private void initDatabase() {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        mDevOpenHelper = new DaoMaster.DevOpenHelper(this, "notes-db", null);
        mDb = mDevOpenHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(mDb);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }
}
