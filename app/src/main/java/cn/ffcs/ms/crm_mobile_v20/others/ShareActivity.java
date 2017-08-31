package cn.ffcs.ms.crm_mobile_v20.others;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.util.Attributes;

import java.util.ArrayList;
import java.util.Arrays;

import cn.ffcs.itbg.itpd.core.Base.BaseAppCompatActivity;
import cn.ffcs.itbg.itpd.core.RecyclerView.ItemTouchBuilder;
import cn.ffcs.itbg.itpd.widgets.RecyclerView.SwipeView.SwipeWrapAdapter;
import cn.ffcs.ms.crm_mobile_v20.R;

/**
 * Created by chenqq on 16-12-15.
 */

public class ShareActivity extends BaseAppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    private ArrayList<String> mDataSet;

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_share);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Layout Managers:
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Adapter:
        String[] adapterData = new String[]{"Alabama", "Alaska", "Arizona", "Arkansas", "California",
                "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho",
                "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland",
                "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana",
                "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York",
                "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania",
                "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah",
                "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};
        mDataSet = new ArrayList<String>(Arrays.asList(adapterData));
        mAdapter = new RecyclerViewAdapter(this, recyclerView, mDataSet);
        ((RecyclerViewAdapter) mAdapter).setMode(Attributes.Mode.Single);
        recyclerView.setAdapter(mAdapter);

        /* Listeners */
        recyclerView.setOnScrollListener(onScrollListener);

        try {
            new ItemTouchBuilder()
                    .setList(mDataSet)
                    .setAdapter(mAdapter)
                    .setSwappable(false)
                    .create(recyclerView)
                    .attachToRecyclerView(recyclerView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void initEvents() {

    }

    @Override
    protected void start() {

    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            Log.e("ListView", "onScrollStateChanged");
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Could hide open views here if you wanted. //
        }
    };


    public class RecyclerViewAdapter extends SwipeWrapAdapter<RecyclerViewAdapter.SimpleViewHolder> {

        private Context mContext;
        private RecyclerView mRecyclerView;
        private ArrayList<String> mDataset;

        public RecyclerViewAdapter(Context context, RecyclerView recyclerView, ArrayList<String> objects) {
            mContext = context;
            mRecyclerView = recyclerView;
            mDataset = objects;
        }

        @Override
        public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
            return new SimpleViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
            super.onBindViewHolder(viewHolder, position);

            String item = mDataset.get(position);
            viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
            viewHolder.swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {
                    YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.trash));
                }
            });

            viewHolder.swipeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(getClass().getSimpleName(), "onItemSelected: " + viewHolder.textViewData.getText().toString());
                    Toast.makeText(view.getContext(), "onItemSelected: " + viewHolder.textViewData.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
                @Override
                public void onDoubleClick(SwipeLayout layout, boolean surface) {
                    Toast.makeText(mContext, "DoubleClick", Toast.LENGTH_SHORT).show();
                }
            });
            viewHolder.buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDataset.remove(position);
                    removeShownLayouts(viewHolder.swipeLayout);
                    mRecyclerView.getAdapter().notifyItemRemoved(position);
                    mRecyclerView.getAdapter().notifyItemRangeChanged(position, mDataset.size());
                    closeAllItems();
                }
            });
            viewHolder.textViewPos.setText((position + 1) + ".");
            viewHolder.textViewData.setText(item);
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe;
        }


        public class SimpleViewHolder extends RecyclerView.ViewHolder {
            SwipeLayout swipeLayout;
            TextView textViewPos;
            TextView textViewData;
            Button buttonDelete;

            public SimpleViewHolder(View itemView) {
                super(itemView);
                swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
                textViewPos = (TextView) itemView.findViewById(R.id.position);
                textViewData = (TextView) itemView.findViewById(R.id.text_data);
                buttonDelete = (Button) itemView.findViewById(R.id.delete);
            }
        }
    }
}
