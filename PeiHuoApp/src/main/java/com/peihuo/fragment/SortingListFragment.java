package com.peihuo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peihuo.R;

/**
 * Created by hb on 2017/8/29.
 * 分拣单列表
 */

public class SortingListFragment extends BaseListFragment{

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sorting_list, null);
        return view;
    }

    @Override
    protected void initView(View view) {

    }
}
