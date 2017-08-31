package cn.ffcs.ms.crm_mobile_v20.main.home;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.rollviewpager.OnItemClickListener;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import java.util.List;

import cn.ffcs.itbg.itpd.core.Base.BaseFragment;
import cn.ffcs.ms.crm_mobile_v20.MyApplication;
import cn.ffcs.ms.crm_mobile_v20.R;
import cn.ffcs.ms.crm_mobile_v20.entities.Banner;
import cn.ffcs.ms.crm_mobile_v20.entities.BannerModel;
import cn.ffcs.ms.crm_mobile_v20.entities.IBannerModel;
import cn.ffcs.ms.crm_mobile_v20.entities.ISceneModel;
import cn.ffcs.ms.crm_mobile_v20.entities.Scene;
import cn.ffcs.ms.crm_mobile_v20.entities.SceneModel;
import cn.ffcs.ms.crm_mobile_v20.greendao.gen.DaoSession;
import cn.ffcs.ms.crm_mobile_v20.messages.MessagesActivity;
import cn.ffcs.ms.crm_mobile_v20.others.AppRecomendActivity;

/**
 * Created by chenqq on 16-12-15.
 */

public class MainTabHomeFragment extends BaseFragment implements MainTabHomeConstract.View {

    private MainTabHomeConstract.Presenter mPresenter;
    private RollPagerView mRollPagerView;
    private RecyclerView mRecyclerView;

    @Override
    protected int setLayout() {
        return R.layout.fragment_main_home;
    }

    @Override
    protected void initViews() {
        mRollPagerView = (RollPagerView) findViewById(R.id.home_rollPagerView);

        mRecyclerView = (RecyclerView) findViewById(R.id.home_recyclerView);


        ImageView imageView = new ImageView(mActivity);
        imageView.setAdjustViewBounds(true);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void initEvents() {
        mRollPagerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showMessage("点击了： " + position);
            }
        });

    }

    @Override
    protected void start() {
        initPresenter();
    }

    @Override
    public void showBanners(final List<Banner> banners) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRollPagerView.setAdapter(new StaticPagerAdapter() {
                    @Override
                    public View getView(ViewGroup container, int position) {
                        ImageView imageView = new ImageView(container.getContext());
                        imageView.setAdjustViewBounds(true);
                        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        Glide.with(mActivity)
                                .load(banners.get(position).getUrl())
                                .centerCrop()
                                .placeholder(R.drawable.ic_launcher)
                                .into(imageView);
                        return imageView;
                    }

                    @Override
                    public int getCount() {
                        return banners.size();
                    }
                });
            }
        });
    }

    @Override
    public void initPresenter() {
        DaoSession daoSession = MyApplication.INSTANCE.getDaoSession();
        IBannerModel bannerModel = new BannerModel(daoSession.getBannerDao());
        ISceneModel sceneModel = new SceneModel(daoSession.getSceneDao());
        mPresenter = new MainTabHomePresenter(this, bannerModel, sceneModel);
        mPresenter.start();
    }

    @Override
    public void showBusinessScenes(final List<Scene> scenes) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 3));
                mRecyclerView.setAdapter(new GridAdapter(scenes));
            }
        });
    }

    @Override
    public void openBusinessScene(Scene scene) {
        showSnackbar("打开【" + scene.getName() + "】");

        if ("天翼".equals(scene.getName())) {
            startActivity(new Intent(mActivity, MessagesActivity.class));
        } else if ("宽带".equals(scene.getName())) {
            startActivity(new Intent(mActivity, AppRecomendActivity.class));
        }
    }

    @Override
    public void showMessage(String message) {
        showSnackbar(message);
    }

    private class GridAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Scene> mScenes;

        public GridAdapter(List<Scene> scenes) {
            mScenes = scenes;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(mActivity).inflate(R.layout.recyclerview_tab_home_gridview_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.textView.setText(mScenes.get(position).getName());
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openBusinessScene(mScenes.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return mScenes.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.home_recyclerView_item_text);
        }
    }
}
