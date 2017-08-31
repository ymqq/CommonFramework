package com.handsome.app3;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * =====作者=====
 * 许英俊
 * =====时间=====
 * 2016/9/7.
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> list;
    public RecycleViewAdapter(List<Message> list) {
        this.list = list;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_username, tv_time, tv_message;
        private ImageView iv_icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_username = (TextView) itemView.findViewById(R.id.tv_username);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            tv_message = (TextView) itemView.findViewById(R.id.tv_message);
            iv_icon = (ImageView) itemView.findViewById(R.id.iv_icon);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder)holder;
        Message msg = list.get(position);
        myViewHolder.tv_username.setText((position + 1) + msg.getUsername());
        myViewHolder.tv_time.setText(msg.getTime());
        myViewHolder.tv_message.setText(msg.getMessage());
        myViewHolder.iv_icon.setBackgroundResource(msg.getImg_id());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
