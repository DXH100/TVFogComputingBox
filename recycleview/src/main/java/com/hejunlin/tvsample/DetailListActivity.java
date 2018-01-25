/*
 * Copyright (C) 2016 hejunlin <hejunlin2013@gmail.com>
 * Github:https://github.com/hejunlin2013/TVSample
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hejunlin.tvsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hejunlin.tvsample.service.LocalService;
import com.hejunlin.tvsample.service.RemoteService;
import com.hejunlin.tvsample.widget.MetroViewBorderImpl;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by hejunlin on 2015/10/16.
 * blog: http://blog.csdn.net/hejjunlin
 */
public class DetailListActivity extends Activity implements View.OnFocusChangeListener {

    private MetroViewBorderImpl mMetroViewBorderImpl;

    //    private OnDataFinishedListener mOnDataFinishedListener;
    private RecyclerView mRecyclerView;
    private View mViewMain;
    private View mViewLook;
    private View mViewRegist;
    private FrameLayout mFlContent;
    private EditText mEtAddress;
    private Button mBtnLook;
    private LinearLayout mLlContent;
    private LinearLayout mLookLLContent;
    private EditText mEtRGAddress;
    private EditText mEtCode;
    private Button mBtnRegist;
    private LinearLayout mLlRGContent;

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int tag = (int) v.getTag();
        LogUtils.e(tag);

        if (hasFocus) {
            mFlContent.removeAllViews();
            switch (tag) {
                case 0:
                    mFlContent.addView(mViewMain);
                    break;
                case 1:
                    mFlContent.addView(mViewRegist);
                    break;
                case 2:
                    mFlContent.addView(mViewLook);
                    break;

            }
        }

    }

//    public static interface StrProcessor {
//        void OnParserJsonString(String line);
//    }
//
//    public static interface OnDataFinishedListener {
//        void onPerformData();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaillist);
        initView();

        startService(new Intent(this, LocalService.class));
        startService(new Intent(this, RemoteService.class));
//        startService(new Intent(this, JobHandleService.class));

        mMetroViewBorderImpl = new MetroViewBorderImpl(this);
        mMetroViewBorderImpl.setBackgroundResource(R.drawable.border_color);
        loadRecyclerViewMenuItem();
        mMetroViewBorderImpl.attachTo(mLlContent);
        mMetroViewBorderImpl.attachTo(mLlRGContent);
        mMetroViewBorderImpl.attachTo(mFlContent);
//        mMetroViewBorderImpl.attachTo(mLookLLContent);
//        mMetroViewBorderImpl.attachTo(mAwardRecyclerView);


    }

    private void initView() {
        mViewMain = View.inflate(this, R.layout.view_main, null);
        initLookView();

        mViewRegist = View.inflate(this, R.layout.view_regist, null);
        mFlContent = (FrameLayout) findViewById(R.id.fl_content);
        mEtAddress = (EditText) mViewMain.findViewById(R.id.et_address);
        mBtnLook = (Button) mViewMain.findViewById(R.id.tv_look);
        mLlContent = (LinearLayout) mViewMain.findViewById(R.id.ll_content);

        mEtRGAddress = (EditText) mViewRegist.findViewById(R.id.et_address);
        mEtCode = (EditText) mViewRegist.findViewById(R.id.et_address);
        mBtnRegist = (Button) mViewRegist.findViewById(R.id.tv_look);
        mLlRGContent = (LinearLayout) mViewRegist.findViewById(R.id.ll_content);
        mBtnLook.setFocusable(false);
        mEtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mBtnLook.setFocusable(true);
                } else {
                    mBtnLook.setFocusable(false);
                }
            }
        });
    }
    private ColumnChartData data; // 柱形图对应的各种属性
    private boolean hasAxes = true; // 是否要添加横纵轴的属性
    private boolean hasAxesNames = true; // 是否设置横纵轴的名字
    private boolean hasLabels = true; // 是否显示柱形图的数据
    private boolean hasLabelForSelected = false; // 是否点中显示数据
    private ColumnChartView mColumnChartCc;
    private void initLookView() {
        mViewLook = View.inflate(this, R.layout.view_look, null);
        mViewLook.setFocusable(false);
        mColumnChartCc= (ColumnChartView) mViewLook.findViewById(R.id.columnchart);
        generateSubcolumnsData();
    }
    private void generateSubcolumnsData() {

        int numColumns = 7; // 表示总共有四根柱子
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values; // 柱子的属性
        List<AxisValue> axisValueList = new ArrayList<>();
        Float[] floats = {30f, 5f, 50f, 15f,8f,20f,30f}; // 包含柱形图的数值的数组
        String[] selecedNames = {"第一天", "第二天", "第三天","第四天","第五天","第六天","第七天",}; // 包含柱子的名称的数组

        for (int i = 0; i < numColumns; ++i) {

            values = new ArrayList<SubcolumnValue>();
            values.add(new SubcolumnValue(floats[i], ChartUtils.pickColor())); // 将柱子的数据以及颜色设置给 SubcolumnValue
            axisValueList.add(new AxisValue(i).setLabel(selecedNames[i]));
            Column column = new Column(values); // 设置整根柱子的属性
            column.setHasLabels(hasLabels); // 是否显示柱子的数据
            column.setHasLabelsOnlyForSelected(hasLabelForSelected); // 是否选中显示数据，一般为false
            columns.add(column);
        }

        data = new ColumnChartData(columns);
        data.setAxisXBottom(new Axis(axisValueList)); // 设置 Y 轴的属性
        data.setAxisYLeft(new Axis()); // 设置 X 轴的属性
        mColumnChartCc.setColumnChartData(data); // 将数据设置给显示柱形图的控件

    }

    private void loadRecyclerViewMenuItem() {
        mRecyclerView = (RecyclerView) findViewById(R.id.ry_menu_item);
        mRecyclerView.setNextFocusRightId(R.id.et_address);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setFocusable(false);
        mMetroViewBorderImpl.attachTo(mRecyclerView);
        createOptionItemData(mRecyclerView, R.layout.detail_menu_item);

    }


    private void createOptionItemData(RecyclerView recyclerView, int id) {
        OptionItemAdapter adapter = new OptionItemAdapter(this, id, this);
        recyclerView.setAdapter(adapter);
        recyclerView.scrollToPosition(0);
    }

}
