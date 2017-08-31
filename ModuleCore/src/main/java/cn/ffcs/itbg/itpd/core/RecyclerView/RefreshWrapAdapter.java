package cn.ffcs.itbg.itpd.core.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Desc: 增加的一个包装Adapter，实现处理HeaderView与FooterView，常规的list Item 还是与之对应的Adapter进行处理。
 * @Author: Tyras on 2017/3/16 10:51.
 */

public class RefreshWrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private View mHeaderView;
    private View mFooterView;
    private RecyclerView.Adapter mAdapter;

    public RefreshWrapAdapter(RecyclerView.Adapter adapter, View headerView, View footerView) {
        mAdapter = adapter;
        mHeaderView = headerView;
        mFooterView = footerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 头部
        if (mHeaderView != null && viewType == RecyclerView.INVALID_TYPE) {
            return new RecyclerView.ViewHolder(mHeaderView) {};
        }
        // 底部
        else if (mFooterView != null && viewType == RecyclerView.INVALID_TYPE - 1) {
            return new RecyclerView.ViewHolder(mFooterView) {};
        }
        // 其他常规列表item
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 头部位置，直接返回
        if (mHeaderView != null && position == 0) {
            return;
        }

        // 这里需要对常规列表item的position进行修复，需要排除头部与底部所占的位置
        int fixedPosition = position - 1;
        if (fixedPosition >=0 && fixedPosition < mAdapter.getItemCount()) {
            mAdapter.onBindViewHolder(holder, fixedPosition);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // 当有头部的情况下
        if (mHeaderView != null && position == 0) {
            // 这里表示头部
            return RecyclerView.INVALID_TYPE;
        }

        // 这里需要对常规列表item的position进行修复，需要排除头部与底部所占的位置
        int fixedPosition = position - 1;
        if (fixedPosition >=0 && fixedPosition < mAdapter.getItemCount()) {
            return mAdapter.getItemViewType(fixedPosition);
        }
        // 这里表示底部
        return RecyclerView.INVALID_TYPE - 1;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mHeaderView != null) {
            count += 1;
        }

        if (mFooterView != null) {
            count += 1;
        }

        // item总数需要加上HeaderView与FooterView
        return count + mAdapter.getItemCount();
    }
}
