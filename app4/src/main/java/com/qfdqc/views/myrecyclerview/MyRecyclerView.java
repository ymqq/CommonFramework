package com.qfdqc.views.myrecyclerview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by baoyunlong
 */
public class MyRecyclerView extends RecyclerView {

    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;

    MyWrapAdapter myWrapAdapter;
    View headerView, footerView;

    private int mState = STATE_NORMAL;
    int headerViewHeight;
    boolean isOnTouching;
    TextView status;
    boolean isRefresh;

    public MyRecyclerViewListener getMyRecyclerViewListener() {
        return myRecyclerViewListener;
    }

    public void setMyRecyclerViewListener(MyRecyclerViewListener myRecyclerViewListener) {
        this.myRecyclerViewListener = myRecyclerViewListener;
    }

    MyRecyclerViewListener myRecyclerViewListener;

    public MyRecyclerView(Context context) {
        super(context);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    int lastX, lastY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isOnTouching = true;
                break;
            case MotionEvent.ACTION_MOVE:
                //判断是否滑动到了头部
                if (!canScrollVertically(-1)) {
                    int dy = lastY - y;
                    int dx = lastX - x;

                    if (Math.abs(dy) > Math.abs(dx)) {
                        isRefresh = true;
                        changeHeight(dy);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isRefresh = false;
                isOnTouching = false;
                if (mState == STATE_READY) {
                    onStatusChange(STATE_REFRESHING);
                }
                autoSize();
                break;
        }

        lastX = x;
        lastY = y;

        return super.onTouchEvent(e);
    }

    private void changeHeight(int dy) {
        headerView.getLayoutParams().height -= dy;
        headerView.requestLayout();
        setStateByHeight(headerView.getHeight(),false);
    }

    public void autoSize() {
        int currentHeight = headerView.getHeight();

        int targetHeight = headerViewHeight;
        if (mState == STATE_READY || mState == STATE_REFRESHING) {
            targetHeight = headerViewHeight * 2;
        }

        if(mState==STATE_REFRESHING){
            if(currentHeight<headerViewHeight*2){
                return;
            }
        }

        ValueAnimator objectAnimator = ValueAnimator.ofInt(currentHeight, targetHeight);
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int animatedValue = (int) animation.getAnimatedValue();
                setStateByHeight(animatedValue,true);
                headerView.getLayoutParams().height = animatedValue;
                headerView.requestLayout();
            }
        });
        objectAnimator.start();
    }

    @Override
    public void setLayoutManager(final LayoutManager layout) {
        super.setLayoutManager(layout);

        this.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isRefresh) {
                    return;
                }
                if (mState != STATE_NORMAL) {
                    return;
                }
                //判断是否最后一item个显示出来
                LayoutManager layoutManager = getLayoutManager();

                //可见的item个数
                int visibleChildCount = layoutManager.getChildCount();
                if (visibleChildCount > 0 && newState == RecyclerView.SCROLL_STATE_IDLE && !isLoadMore) {
                    View lastVisibleView = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
                    int lastVisiblePosition = recyclerView.getChildLayoutPosition(lastVisibleView);
                    if (lastVisiblePosition >= layoutManager.getItemCount() - 1) {
                        footerView.setVisibility(VISIBLE);
                        isLoadMore = true;
                        if (myRecyclerViewListener != null) {
                            myRecyclerViewListener.onLoadMore();
                        }
                    } else {
                        footerView.setVisibility(GONE);
                    }
                }

            }
        });
    }

    private void setStateByHeight(int height,boolean isAuto) {
        if(mState==STATE_REFRESHING){
            return;
        }
        if (height - headerViewHeight < headerViewHeight) {
            onStatusChange(STATE_NORMAL);
        } else if (height - headerViewHeight > headerViewHeight) {
            onStatusChange(STATE_READY);
        } else if (height - headerViewHeight == headerViewHeight&&!isOnTouching&&!isAuto) {
            onStatusChange(STATE_REFRESHING);
        }
    }

    boolean isLoadMore;

    public void onStatusChange(int status) {
        mState = status;
        switch (status) {
            case STATE_READY:
                this.status.setText("松开刷新...");
                break;
            case STATE_NORMAL:
                this.status.setText("下拉刷新...");
                break;
            case STATE_REFRESHING:
                this.status.setText("正在刷新...");
                if (myRecyclerViewListener != null) {
                    myRecyclerViewListener.onRefresh();
                }
                break;
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        ArrayList<View> headers = new ArrayList<>();
        ArrayList<View> footers = new ArrayList<>();

        View refreshView = LayoutInflater.from(getContext()).inflate(R.layout.my_refresh, null);
        headerView = refreshView;

        RelativeLayout headerLayout = new RelativeLayout(getContext());
        headerLayout.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        headerLayout.addView(headerView, RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        headerLayout.setGravity(Gravity.BOTTOM);

        status = (TextView) refreshView.findViewById(R.id.status);
        headerView.post(new Runnable() {
            @Override
            public void run() {
                headerViewHeight = headerView.getHeight();
                RelativeLayout.LayoutParams l = (RelativeLayout.LayoutParams) headerView.getLayoutParams();
                l.setMargins(0, -headerViewHeight, 0, 0);
                headerView.requestLayout();
            }
        });
        headers.add(headerLayout);

        LinearLayout footerLayout = new LinearLayout(getContext());
        footerLayout.setGravity(Gravity.CENTER);
        footerLayout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        footers.add(footerLayout);
        footerLayout.setPadding(0,15,0,15);
        footerLayout.addView(new ProgressBar(getContext(), null, android.R.attr.progressBarStyleSmall));

        TextView text = new TextView(getContext());
        text.setText("正在加载...");
        footerLayout.addView(text);
        footerView=footerLayout;
        footerView.setVisibility(GONE);

        myWrapAdapter = new MyWrapAdapter(adapter, headers, footers);
        super.setAdapter(myWrapAdapter);
    }

    public interface MyRecyclerViewListener {
        void onRefresh();

        void onLoadMore();
    }

    public void setLoadMoreComplete() {
        footerView.setVisibility(GONE);
        isLoadMore = false;
    }

    public void setRefreshComplete() {
        onStatusChange(STATE_NORMAL);
        autoSize();
    }

}
