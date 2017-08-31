package com.handsome.app3;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Collections;
import java.util.List;

/**
 * @Desc: 获取支持触摸操作：上下拖拽、左右侧滑，以实现拖拽排序、侧滑删除/展示操作按钮的 <br/>
 * RecyclerView使用的ItemTouchHelper实例。
 * @Author: Tyras on 2017/3/14 16:21.
 */
public class ItemTouchBuilder {
    private static final String TAG = "ItemTouchBuilder";

    private ItemTouchBuilder mInstance;

    // 是否可拖拽，默认为true
    private boolean mDraggable = true;
    // 是否可长按拖拽，默认为true
    private boolean mLongPressDraggable = true;
    // 是否可侧滑，默认为true
    private boolean mSwappable = true;
    // RecyclerView对应的列表数据
    private List mList;
    // RecyclerView对应的适配器
    // 由于Drag时列表第一条，在数据超过两页时，会出现直接翻到下一页，而且没办法控制
    // 为了解决这个问题，在数据适配器外包装一层，在顶部插入一条空View。
    private DragWrapperAdapter mDragWrapperAdapter;
    private OnDragCallback mDragCallback;
    private onSwapCallback mSwapCallback;


    public interface OnDragCallback {
        void onDrag(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target);
    }

    public interface onSwapCallback {
        void onSwap(RecyclerView.ViewHolder viewHolder, int direction);
    }

    public ItemTouchBuilder() {
        mInstance = this;
    }

    public ItemTouchBuilder setList(List list) {
        mList = list;
        return mInstance;
    }

    public ItemTouchBuilder setAdapter(RecyclerView.Adapter adapter) {
        mDragWrapperAdapter = new DragWrapperAdapter(adapter);
        return mInstance;
    }

    public ItemTouchBuilder setDraggable(boolean draggable) {
        mDraggable = draggable;
        return mInstance;
    }

    public ItemTouchBuilder setLongPressDraggable(boolean longPressDraggable) {
        mLongPressDraggable = longPressDraggable;
        return mInstance;
    }

    public ItemTouchBuilder setSwappable(boolean swappable) {
        mSwappable = swappable;
        return mInstance;
    }

    public ItemTouchBuilder setOnDragCallback(OnDragCallback callback) {
        mDragCallback = callback;
        return mInstance;
    }

    public ItemTouchBuilder setOnSwapCallback(onSwapCallback callback) {
        mSwapCallback = callback;
        return mInstance;
    }

    public ItemTouchHelper create(RecyclerView recyclerView) throws Exception {
        // 这里进行条件判断
        if (mDragWrapperAdapter == null) {
            throw new Exception("构建错误：允许拖拽情况下，Adapter不能为空");
        }
        // 可拖拽，但未自定义回调，并且 mList 与 mAdapter 有为 null
        if (mDraggable && mDragCallback == null
                && (mList == null || mDragWrapperAdapter == null)) {
            throw new Exception("构建错误：允许拖拽情况下，List/Adapter 与 OnDragCallback 不能同时为空");
        }

        // 可侧滑，但未自定义回调，并且 mList 与 mAdapter 有为 null
        if (mSwappable && mSwapCallback == null
                && (mList == null || mDragWrapperAdapter == null)) {
            throw new Exception("构建错误：允许侧滑情况下，List/Adapter 与 mSwapCallback 不能同时为空");
        }

        recyclerView.setAdapter(mDragWrapperAdapter);

        return new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                // 最先回调的方法，返回int表示是否监听该方向
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN; // 拖拽
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT; // 侧滑

                // 这里通过定制方式，定制具体可操作动作
                dragFlags = mDraggable ? dragFlags : 0;
                swipeFlags = mSwappable ? swipeFlags : 0;

                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // 拖拽对应的处理方法
                if (mDragCallback == null) {
                    // 集合方法，交换顺序
                    int srcPosition = viewHolder.getAdapterPosition();
                    int targetPosition = target.getAdapterPosition();
                    // 这里由于Adapter增加了一个外包装，导致item多加了一个头，使getAdapterPosition获取到的位置+1
                    // 因此，list数据处理时，需要减1进行修复。
                    Collections.swap(mList, srcPosition - 1, targetPosition - 1);
                    mDragWrapperAdapter.notifyItemMoved(srcPosition, targetPosition);
                } else {
                    mDragCallback.onDrag(recyclerView, viewHolder, target);
                }
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // 侧滑事件
                if (mSwapCallback == null) {
                    // 直接删除
                    int position = viewHolder.getAdapterPosition();
                    // 这里由于Adapter增加了一个外包装，导致item多加了一个头，使getAdapterPosition获取到的位置+1
                    // 因此，list数据处理时，需要减1进行修复。
                    mList.remove(position - 1);
                    mDragWrapperAdapter.notifyItemRemoved(position);
                } else {
                    mSwapCallback.onSwap(viewHolder, direction);
                }
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return mLongPressDraggable && mDraggable;
            }
        });
    }


    class DragWrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private RecyclerView.Adapter mAdapter;
        public DragWrapperAdapter(RecyclerView.Adapter adapter) {
            mAdapter = adapter;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == RecyclerView.INVALID_TYPE) {
                return new RecyclerView.ViewHolder(new LinearLayout(parent.getContext())){};
            }
            return mAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position == 0) {
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
            if (position == 0) {
                return RecyclerView.INVALID_TYPE;
            }
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return mAdapter.getItemCount();
        }
    }
}
