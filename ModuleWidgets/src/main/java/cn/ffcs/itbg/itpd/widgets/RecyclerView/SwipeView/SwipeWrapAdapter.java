package cn.ffcs.itbg.itpd.widgets.RecyclerView.SwipeView;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

/**
 * @Desc:
 * @Author: Tyras on 2017/3/30 19:53.
 */

public abstract class SwipeWrapAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerSwipeAdapter<VH>  {
    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        mItemManger.bindView(viewHolder.itemView, position);
    }

    @Override
    public abstract int getItemCount();

    @Override
    public abstract int getSwipeLayoutResourceId(int position);
}
