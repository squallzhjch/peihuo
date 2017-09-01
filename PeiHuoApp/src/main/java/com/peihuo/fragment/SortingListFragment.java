package com.peihuo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peihuo.R;
import com.peihuo.adapter.SortingListAdapter;
import com.peihuo.db.GetSortingListCallback;
import com.peihuo.entity.SortingOrder;

import java.util.List;

/**
 * Created by hb on 2017/8/29.
 * 分拣单列表
 */

public class SortingListFragment extends BaseListFragment{

    private SortingListAdapter mAdapter;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sorting_list, null);
        return view;
    }

    @Override
    protected void initView(View view) {
        mAdapter = new SortingListAdapter(getContext());
        mListView.setAdapter(mAdapter);
        GetSortingListCallback callback = new GetSortingListCallback(getContext(), 10, 0, new GetSortingListCallback.OnLoadDataListener() {
            @Override
            public void onSuccess(List<SortingOrder> list) {
                mAdapter.setData(list);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {

            }
        });
    }
}
