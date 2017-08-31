package com.handsome.app3;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rv;
    private RecycleViewAdapter adapter;
    private List<Message> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecycleViewAdapter(list);
        rv.setAdapter(adapter);

        try {
            new ItemTouchBuilder()
                    .setList(list)
                    .setAdapter(adapter)
                    .setSwappable(false)
                    .create(rv)
                    .attachToRecyclerView(rv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        list = new ArrayList<>();
        list.add(new Message("Hensen", "下午1:22", "老板：哈哈哈", R.drawable.icon1));
        list.add(new Message("流年不利", "早上10:31", "美女：呵呵哒", R.drawable.icon2));
        list.add(new Message("1402", "下午1:55", "嘻嘻哈哈", R.drawable.icon3));
        list.add(new Message("Unstoppable", "下午4:32", "美美哒", R.drawable.icon4));
        list.add(new Message("流年不利", "晚上7:22", "萌萌哒", R.drawable.icon2));
        list.add(new Message("Hensen", "下午1:22", "哈哈哈", R.drawable.icon1));
        list.add(new Message("Hensen", "下午1:22", "哈哈哈", R.drawable.icon1));
        list.add(new Message("Hensen", "下午1:22", "哈哈哈", R.drawable.icon1));
        list.add(new Message("Hensen", "下午1:22", "老板：哈哈哈", R.drawable.icon1));
        list.add(new Message("流年不利", "早上10:31", "美女：呵呵哒", R.drawable.icon2));
        list.add(new Message("1402", "下午1:55", "嘻嘻哈哈", R.drawable.icon3));
        list.add(new Message("Unstoppable", "下午4:32", "美美哒", R.drawable.icon4));
        list.add(new Message("流年不利", "晚上7:22", "萌萌哒", R.drawable.icon2));
        list.add(new Message("Hensen", "下午1:22", "哈哈哈", R.drawable.icon1));
        list.add(new Message("Hensen", "下午1:22", "哈哈哈", R.drawable.icon1));
        list.add(new Message("Hensen", "下午1:22", "哈哈哈", R.drawable.icon1));
        list.add(new Message("Hensen", "下午1:22", "老板：哈哈哈", R.drawable.icon1));
        list.add(new Message("流年不利", "早上10:31", "美女：呵呵哒", R.drawable.icon2));
        list.add(new Message("1402", "下午1:55", "嘻嘻哈哈", R.drawable.icon3));
//        list.add(new Message("Unstoppable", "下午4:32", "美美哒", R.drawable.icon4));
//        list.add(new Message("流年不利", "晚上7:22", "萌萌哒", R.drawable.icon2));
//        list.add(new Message("Hensen", "下午1:22", "哈哈哈", R.drawable.icon1));
//        list.add(new Message("Hensen", "下午1:22", "哈哈哈", R.drawable.icon1));
//        list.add(new Message("Hensen", "下午1:22", "哈哈哈", R.drawable.icon1));
//        list.add(new Message("Hensen", "下午1:22", "老板：哈哈哈", R.drawable.icon1));
//        list.add(new Message("流年不利", "早上10:31", "美女：呵呵哒", R.drawable.icon2));
//        list.add(new Message("1402", "下午1:55", "嘻嘻哈哈", R.drawable.icon3));
//        list.add(new Message("Unstoppable", "下午4:32", "美美哒", R.drawable.icon4));
//        list.add(new Message("流年不利", "晚上7:22", "萌萌哒", R.drawable.icon2));
//        list.add(new Message("Hensen", "下午1:22", "哈哈哈", R.drawable.icon1));
//        list.add(new Message("Hensen", "下午1:22", "哈哈哈", R.drawable.icon1));
//        list.add(new Message("Hensen", "下午1:22", "哈哈哈", R.drawable.icon1));
    }

}
