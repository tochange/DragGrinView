package com.ywanhzy.demo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ywanhzy.demo.AppConfig;
import com.ywanhzy.demo.AppContext;
import com.ywanhzy.demo.R;
import com.ywanhzy.demo.adapter.MyAdapter;
import com.ywanhzy.demo.drag.DragCallback;
import com.ywanhzy.demo.drag.DragForScrollView;
import com.ywanhzy.demo.drag.DragGridView;
import com.ywanhzy.demo.entity.MenuEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MenuManageActivity extends Activity {
    private static DragGridView dragGridView;
    private static DragGridView dragGridView2;
    private TextView tv_top_sure;
    private static AppContext appContext;
    private DragForScrollView sv_index;
    private static List<MenuEntity> indexSelect = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_manage);
        appContext = (AppContext) getApplication();
        dragGridView = (DragGridView) findViewById(R.id.gridview);
        dragGridView2 = (DragGridView) findViewById(R.id.gridview2);
        sv_index = (DragForScrollView) findViewById(R.id.sv_index);
        initView();
    }


    protected void postMenu() {
        List<MenuEntity> indexDataList = (List<MenuEntity>) appContext.readObject(AppConfig.KEY_USER_TEMP);
        String key = AppConfig.KEY_USER;
        appContext.saveObject((Serializable) indexDataList, key);
    }

    private void initView() {
        LinearLayout ll_top_sure = (LinearLayout) findViewById(R.id.ll_top_sure);
        tv_top_sure = (TextView) findViewById(R.id.tv_top_sure);
        tv_top_sure.setText("管理");
        tv_top_sure.setVisibility(View.VISIBLE);


        final MyAdapter myAdapter = new MyAdapter(this, appContext, indexSelect);
        final MyAdapter myAdapter2 = new MyAdapter(this, appContext, indexSelect);

        ll_top_sure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_top_sure.getText().toString().equals("管理")) {
                    tv_top_sure.setText("完成");
                    myAdapter.setEdit();
                    myAdapter2.setEdit();
                } else {
                    tv_top_sure.setText("管理");
                    myAdapter.endEdit();
                    myAdapter2.endEdit();
                    postMenu();
                }
            }
        });

        List<MenuEntity> indexDataList = (List<MenuEntity>) appContext.readObject(AppConfig.KEY_USER);
        if (indexDataList != null) {
            indexSelect.clear();
            indexSelect.addAll(indexDataList);
        }

        bindViewWithAdapter(myAdapter, dragGridView);
        bindViewWithAdapter(myAdapter2, dragGridView2);
    }

    private void bindViewWithAdapter(final MyAdapter myAdapter, final DragGridView dragGridView) {
        dragGridView.setAdapter(myAdapter);
        dragGridView.setDragCallback(new DragCallback() {
            @Override
            public void startDrag(int position) {
                sv_index.startDrag(position);
            }

            @Override
            public void endDrag(int position) {
                sv_index.endDrag(position);
            }
        });
        dragGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!myAdapter.getEditStatue()) {
                    Log.e("yangxj", "onItemClick(MenuManageActivity.java:97) ");
                }
            }
        });
        dragGridView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (tv_top_sure.getText().toString().equals("管理")) {
                    tv_top_sure.setText("完成");
                    myAdapter.setEdit();
                }
                dragGridView.startDrag(position);
                return false;
            }
        });
    }


}
