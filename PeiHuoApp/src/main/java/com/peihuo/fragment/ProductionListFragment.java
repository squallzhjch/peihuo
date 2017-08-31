package com.peihuo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peihuo.R;

/**
 * Created by 123 on 2017/8/30.
 * 生产计划单
 */

public class ProductionListFragment extends BaseListFragment {


    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_production_list, null);
        return view;
    }

    @Override
    protected void initView(View view) {
    }
}
