package cn.ffcs.ms.crm_mobile_v20.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ffcs.itbg.itpd.core.Base.BaseAppCompatActivity;
import cn.ffcs.ms.crm_mobile_v20.R;
import cn.ffcs.ms.crm_mobile_v20.main.earning.MainTabEarningFragment;
import cn.ffcs.ms.crm_mobile_v20.main.home.MainTabHomeFragment;
import cn.ffcs.ms.crm_mobile_v20.main.query.MainTabQueryFragment;
import cn.ffcs.ms.crm_mobile_v20.messages.MessagesActivity;
import cn.ffcs.ms.crm_mobile_v20.others.AppRecomendActivity;
import cn.ffcs.ms.crm_mobile_v20.others.ShareActivity;
import cn.ffcs.ms.crm_mobile_v20.settings.SettingsActivity;
import cn.ffcs.ms.crm_mobile_v20.version.VersionActivity;

public class MainActivity extends BaseAppCompatActivity implements View.OnClickListener {
    public static AppCompatActivity INSTANCE;

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    private ViewPager mViewPager;
    private FragmentPagerAdapter mPagerAdapter;
    private List<Fragment> mFragmentList;

    private TextView mHomeTv, mQueryTv, mMineTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INSTANCE = this;
    }

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);

        mHomeTv = (TextView) findViewById(R.id.main_tab_home_tv);
        mQueryTv = (TextView) findViewById(R.id.main_tab_query_tv);
        mMineTv = (TextView) findViewById(R.id.main_tab_mine_tv);

        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawerLayout);

        setToolbar();
        setDrawerLayout();
    }

    @Override
    protected void initEvents() {
        mHomeTv.setOnClickListener(this);
        mQueryTv.setOnClickListener(this);
        mMineTv.setOnClickListener(this);
    }

    @Override
    protected void start() {
        initViewPager();
    }

    private void setToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setDrawerLayout() {
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.main_navView);
        if(navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }

    private void setupDrawerContent(final NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.messages_navigation_menu_item:
                        startActivity(new Intent(INSTANCE, MessagesActivity.class));
                        break;
                    case R.id.share_navigation_menu_item:
                        startActivity(new Intent(INSTANCE, ShareActivity.class));
                        break;
                    case R.id.app_recomend_navigation_menu_item:
                        startActivity(new Intent(INSTANCE, AppRecomendActivity.class));
                        break;
                    case R.id.version_navigation_menu_item:
                        startActivity(new Intent(INSTANCE, VersionActivity.class));
                        break;
                    case R.id.settings_navigation_menu_item:
                        startActivity(new Intent(INSTANCE, SettingsActivity.class));
                        break;
                    case R.id.exit_navigation_menu_item:
                        Snackbar.make(navigationView, "退出", Snackbar.LENGTH_SHORT).show();
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViewPager() {
        mFragmentList = new ArrayList<>();

        mFragmentList.add(new MainTabHomeFragment());
        mFragmentList.add(new MainTabQueryFragment());
        mFragmentList.add(new MainTabEarningFragment());

        mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        };

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.setCurrentItem(position);
                selectedTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        selectedTab(0);
    }

    private void selectedTab(int position) {
        resetTabStatus();
        switch (position) {
            case 0:
                mHomeTv.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
            case 1:
                mQueryTv.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
            case 2:
                mMineTv.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
        }

        if (mViewPager.getCurrentItem() != position) {
            mViewPager.setCurrentItem(position);
        }
    }

    private void resetTabStatus() {
        mHomeTv.setTextColor(getResources().getColor(R.color.colorPrimary));
        mQueryTv.setTextColor(getResources().getColor(R.color.colorPrimary));
        mMineTv.setTextColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.main_tab_home_tv:
                selectedTab(0);
                break;
            case R.id.main_tab_query_tv:
                selectedTab(1);
                break;
            case R.id.main_tab_mine_tv:
                selectedTab(2);
                break;
            default:
                selectedTab(0);
        }
    }

}
