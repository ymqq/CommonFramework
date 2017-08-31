package cn.ffcs.itbg.itpd.core.RecyclerView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.ffcs.itbg.itpd.core.R;

/**
 * @Desc: 自定义下拉与上拉功能的RecyclerView，实现下拉刷新、上拉加载更多。<br/>
 * 默认提供一套HeaderView与FooterView，同时提供自定义HeaderView与FooterView设置接口<br/>
 * 自定义HeaderView与FooterView时，最好同样实现默认View中的所有id，这样可以减少自定义<br/>
 * 时，额外的去增加HeaderView与FooterView的控制代码逻辑。<br/>
 * 其他关于RecyclerView的所有实现都与该控件本来的处理方式一致，未做多余处理，因此不需要<br/>
 * 担心兼容性问题，以及完全按照原有方式处理RecyclerView列表中的样式、动画等等。
 *
 * 参考：https://github.com/llxdaxia/RecyclerView
 *
 * @Author: Tyras on 2017/3/15 17:56.
 */

public class RefreshRecyclerView extends RecyclerView {
    // 常规状态
    public static final int REFRESH_STATE_NORMAL = 0;
    // 准备状态，即拉动状态
    public static final int REFRESH_STATE_READY = 1;
    // 正在刷新状态
    public static final int REFRESH_STATE_REFRESHING = 2;

    private boolean isDefaultHeaderView = true;
    private boolean isDefaultFooterView = true;
    private View mHeaderView;
    private View mHeaderViewStateIcon;
    private TextView mHeaderViewHint;
    private TextView mHeaderViewLastRefreshTime;
    private View mFooterView;
    private View mFooterViewStateIcon;
    private TextView mFooterViewHint;
    private int mHeaderViewHeight;

    private int mCurrentState = REFRESH_STATE_NORMAL;

    private boolean isTouching = false;
    private boolean isPullDown = false;
    private boolean isLoadingMore = false;
    private boolean isLoadedAll = false;

    private int mLastX, mLastY;

    private OnPullActionListener mPullActionListener;


    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(context);
    }

    /**
     * 初始化默认的HeaderView 与 FooterView
     *
     * @param context
     */
    private void init(Context context) {
        // inflate得到的view需要代码设置LayoutParams
        // HeaderView
        mHeaderView = LinearLayoutCompat.inflate(context, R.layout.recyclerview_refresh_headerview, null);
        mHeaderViewStateIcon = mHeaderView.findViewById(R.id.refresh_headerview_stateIcon);
        mHeaderViewHint = (TextView) mHeaderView.findViewById(R.id.refresh_headerview_hint);
        mHeaderViewLastRefreshTime = (TextView) mHeaderView.findViewById(R.id.refresh_headerview_lastRefreshTime);

        // 直接从布局中获取高度即可
        mHeaderViewHeight = getResources().getDimensionPixelOffset(R.dimen.recyclerview_refresh_headerview_height);

        // 这里需要手动设置LayoutParams，否则getLayoutParams为空
        // 并且，在使用inflate得到的布局，没有设置手动设置LayoutParams的情况下，有些布局会出问题，特别是使用LinearLayout时。
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeaderViewHeight);
        lp.setMargins(0, -mHeaderViewHeight, 0, 0);
        mHeaderView.setLayoutParams(lp);
        mHeaderView.requestLayout();

        // FooterView
        mFooterView = LinearLayoutCompat.inflate(context, R.layout.recyclerview_refresh_footerview, null);
        mFooterViewStateIcon = mFooterView.findViewById(R.id.refresh_footerview_stateIcon);
        mFooterViewHint = (TextView) mFooterView.findViewById(R.id.refresh_footerview_hint);
    }

    /**
     * 设置自定义HeaderView <br/>
     * 自定义HeaderView最好要满足默认HeaderView中所有的view id
     * @param headerView
     */
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        isDefaultHeaderView = false;

        // 从自定义的HeaderView中获取预期想要的View
        mHeaderViewStateIcon = headerView.findViewById(R.id.refresh_headerview_stateIcon);
        mHeaderViewHint = (TextView) headerView.findViewById(R.id.refresh_headerview_hint);
        mHeaderViewLastRefreshTime = (TextView) headerView.findViewById(R.id.refresh_headerview_lastRefreshTime);

        // 这里需要手动设置LayoutParams，否则getLayoutParams为空
        // 并且，在使用inflate得到的布局，没有设置手动设置LayoutParams的情况下，有些布局会出问题，特别是使用LinearLayout时。
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mHeaderView.setLayoutParams(lp);
        mHeaderView.requestLayout();

        mHeaderView.post(new Runnable() {
            @Override
            public void run() {
                mHeaderViewHeight = mHeaderView.getHeight();
                LayoutParams l = (LayoutParams) mHeaderView.getLayoutParams();
                l.setMargins(0, -mHeaderViewHeight, 0, 0);
                mHeaderView.requestLayout();
            }
        });
    }

    /**
     * 设置自定义FooterView <br/>
     * 自定义FooterView最好要满足默认FooterView中所有的view id
     * @param footerView
     */
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        isDefaultFooterView = false;

        // 从自定义FooterView中获取预期想要的View
        mFooterViewStateIcon = footerView.findViewById(R.id.refresh_footerview_stateIcon);
        mFooterViewHint = (TextView) footerView.findViewById(R.id.refresh_footerview_hint);
    }

    /**
     * 下拉刷新结束，将HeaderView状态还原
     * @param isCompleted 是否刷新成功，成功时才设置刷新时间
     */
    public void refreshed(boolean isCompleted) {
        if (isCompleted) {
            if (isDefaultHeaderView || mHeaderViewLastRefreshTime != null) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String time = format.format(new Date());
                mHeaderViewLastRefreshTime.setText(getString(R.string.recyclerview_refresh_headerview_hint_lastRefreshTimePrefix) + time);
            }
        }

        onStateChanged(REFRESH_STATE_NORMAL);
        autoSize();
    }

    /**
     * 是否加载完全部数据，用于控制FooterView的文字提示以及是否隐藏FooterView
     * @param isHideFooterView 是否隐藏FooterView
     */
    public void alreadyLoadedAll(boolean isHideFooterView) {
        isLoadedAll = true;
        if (mFooterView != null) {
            if (isHideFooterView) {
                mFooterView.setVisibility(GONE);
            }
            if (isDefaultFooterView || mFooterViewHint != null) {
                mFooterViewHint.setText(getString(R.string.recyclerview_refresh_footerview_hint_stateLoadedAll));
                if (mFooterViewStateIcon != null) {
                    mFooterViewStateIcon.setVisibility(GONE);
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        int y = (int) e.getRawY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.i("onInterceptTouchEvent", "ACTION_DOWN");
                // 这里直接触发onTouchEvent，直接返回true或者false，都会影响事件传递。
                onTouchEvent(e);
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("onInterceptTouchEvent", "ACTION_MOVE");
                // Move事件全部拦截，有RecyclerView自己处理。
                return true;
            case MotionEvent.ACTION_UP:
                Log.i("onInterceptTouchEvent", "ACTION_UP");
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i("onInterceptTouchEvent", "ACTION_CANCEL");
                break;
        }
        Log.i("onInterceptTouchEvent", "false");
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();

        Log.i("onTouchEvent", "x: " + x + " y: " + y);

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouching = true;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = mLastX - x;
                int dy = mLastY - y;

                // 判断下拉的实际距离是否大于侧滑的实际距离
                // 用于判断是否真的做下拉动作
                if (Math.abs(dy) > Math.abs(dx)) {
                    isPullDown = true;
                    changeHeight(dy);
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isPullDown = false;
                isTouching = false;
                if (mCurrentState == REFRESH_STATE_READY) {
                    onStateChanged(REFRESH_STATE_REFRESHING);
                }
                autoSize();
                break;
        }

        mLastX = x;
        mLastY = y;

        return super.onTouchEvent(e);
    }

    private boolean isPullDown() {
        LayoutManager manager = getLayoutManager();

        if (manager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) manager).findFirstCompletelyVisibleItemPosition() == (mHeaderView != null ? 1 : 0);
        } else if(manager instanceof StaggeredGridLayoutManager) {
            int aa[] = ((StaggeredGridLayoutManager) manager).findFirstVisibleItemPositions(null);
            return getChildAt(0).getY() == 0f && aa[0] == 0;
        }
        return false;
    }

    /**
     * 实时修改HeaderView的高度，控制HeaderView的下拉展示，渐渐显示与隐藏
     *
     * @param y
     */
    private void changeHeight(int y) {
        if (mHeaderView != null) {
            mHeaderView.getLayoutParams().height -= y;
            mHeaderView.requestLayout();
            setStateByHeight(mHeaderView.getHeight(), false);
        }
    }

    /**
     * 根据HeaderView的实时高度，调整下拉刷新的状态
     *
     * @param height HeaderView的实时高度
     * @param isAuto 是否自动调整
     */
    private void setStateByHeight(int height, boolean isAuto) {
        if (mCurrentState == REFRESH_STATE_REFRESHING) {
            return;
        }

        // 减掉HeaderView的高度后，再进行下拉位移判断
        int newAddHeight = height - mHeaderViewHeight;
        // 下拉位移从0~mHeaderViewHeight * 5 / 2，都当作正常状态
        if (newAddHeight > 0 && newAddHeight <= mHeaderViewHeight * 5 / 2) {
            onStateChanged(REFRESH_STATE_NORMAL);
        }
        // 下拉超过mHeaderViewHeight * 5 / 2，作为准备刷新状态
        else if (newAddHeight > mHeaderViewHeight * 5 / 2) {
            onStateChanged(REFRESH_STATE_READY);
        }
        // 当非自动，并且不是手动下拉情况下，height = 2*mHeaderViewHeight就触发刷新
        else if (newAddHeight == 0 && !isTouching && !isAuto) {
            onStateChanged(REFRESH_STATE_REFRESHING);
        }
    }

    /**
     * 设置HeaderView的实时状态，同时展示相应的提示内容与图标 <br/>
     * 并且触发OnPullActionListener.onPullDown监听事件
     *
     * @param state HeaderView的实时状态
     */
    private void onStateChanged(int state) {
        String headerHint = "";
        int visible = GONE;
        mCurrentState = state;
        switch (state) {
            case REFRESH_STATE_READY:
                headerHint = getString(R.string.recyclerview_refresh_headerview_hint_stateReady);
                visible = GONE;
                break;
            case REFRESH_STATE_NORMAL:
                headerHint = getString(R.string.recyclerview_refresh_headerview_hint_stateNormal);
                visible = GONE;
                break;
            case REFRESH_STATE_REFRESHING:
                headerHint = getString(R.string.recyclerview_refresh_headerview_hint_stateRefreshing);
                visible = VISIBLE;
                break;
            default:
                headerHint = getString(R.string.recyclerview_refresh_headerview_hint_stateNormal);
                visible = GONE;
                break;
        }

        if (isDefaultHeaderView || mHeaderViewHint != null) {
            mHeaderViewHint.setText(headerHint);
        }

        if (isDefaultHeaderView || mHeaderViewStateIcon != null) {
            mHeaderViewStateIcon.setVisibility(visible);
        }

        if (mPullActionListener != null) {
            mPullActionListener.onPullDown(state);
        }
    }

    /**
     * 自动计算下拉高度，设置给HeaderView，同时增加响应下拉的动画效果。
     */
    private void autoSize() {
        if (mHeaderView == null) {
            return;
        }

        int currentHeight = mHeaderView.getMeasuredHeight();
        int targetHeight = mHeaderViewHeight;
        if (mCurrentState == REFRESH_STATE_READY || mCurrentState == REFRESH_STATE_REFRESHING) {
            targetHeight = mHeaderViewHeight * 2;
        }

        if (mCurrentState == REFRESH_STATE_REFRESHING
                && currentHeight < mHeaderViewHeight * 2) {
            return;
        }

        ValueAnimator objectAnimator = ValueAnimator.ofInt(currentHeight, targetHeight);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                setStateByHeight(animatedValue, true);
                mHeaderView.getLayoutParams().height = animatedValue;
                mHeaderView.requestLayout();
            }
        });
        objectAnimator.start();
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);

        // 这里处理上拉加载更多
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            bindOldScrollListener();
        } else {
            bindNewScrollListener();
        }
    }

    /**
     * 绑定M之前的Scroll监听事件
     */
    private void bindOldScrollListener() {
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isPullDown) {
                    return;
                }

                if (mCurrentState != REFRESH_STATE_NORMAL) {
                    return;
                }

                if (isLoadedAll) {
                    return;
                }

                LayoutManager manager = getLayoutManager();

                // 可见的Item个数
                int visibleChildCount = manager.getChildCount();
                if (visibleChildCount > 0
                        && newState == RecyclerView.SCROLL_STATE_IDLE
                        && !isLoadingMore) {
                    View lastVisibleView = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
                    int lastVisiblePosition = recyclerView.getChildLayoutPosition(lastVisibleView);
                    if (lastVisiblePosition >= manager.getItemCount() - 1) {
                        if (mFooterView != null) {
                            mFooterView.setVisibility(VISIBLE);
                            if (isDefaultFooterView || mFooterViewHint != null) {
                                mFooterViewHint.setText(getString(R.string.recyclerview_refresh_footerview_hint_stateLoading));
                            }
                        }
                        isLoadingMore = true;
                        if (mPullActionListener != null) {
                            mPullActionListener.onPullUp();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    /**
     * 绑定新的ScrollListener监听事件
     *
     * api = Build.VERSION_CODES.M
     *
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void bindNewScrollListener() {
        // 该方法最低版本23 M
        setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (isPullDown) {
                    return;
                }

                if (mCurrentState != REFRESH_STATE_NORMAL) {
                    return;
                }

                if (isLoadedAll) {
                    return;
                }

                LayoutManager manager = getLayoutManager();

                // 可见的Item个数
                int visibleChildCount = manager.getChildCount();
                RecyclerView recyclerView = (RecyclerView) v;
                if (visibleChildCount > 0
                        && !isLoadingMore && scrollY - oldScrollY == 0
                        && !isLoadingMore) {
                    View lastVisibleView = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
                    int lastVisiblePosition = recyclerView.getChildLayoutPosition(lastVisibleView);
                    if (lastVisiblePosition >= manager.getItemCount() - 1) {
                        if (mFooterView != null) {
                            mFooterView.setVisibility(VISIBLE);
                            if (isDefaultFooterView || mFooterViewHint != null) {
                                mFooterViewHint.setText(getString(R.string.recyclerview_refresh_footerview_hint_stateLoading));
                            }
                        }

                        isLoadingMore = true;
                        if (mPullActionListener != null) {
                            mPullActionListener.onPullUp();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void setAdapter(Adapter adapter) {
        RefreshWrapAdapter wrapAdapter = new RefreshWrapAdapter(adapter, mHeaderView, mFooterView);
        super.setAdapter(wrapAdapter);
    }

    public interface OnPullActionListener {
        void onPullDown(int state);

        void onPullUp();
    }

    /**
     * 设置控件对应的监听事件
     * @param l
     */
    public void setOnPullActionListener(OnPullActionListener l) {
        mPullActionListener = l;
    }


    private String getString(int id) {
        return getResources().getString(id);
    }
}
