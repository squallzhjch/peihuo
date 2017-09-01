package com.peihuo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peihuo.R;
import com.peihuo.adapter.AcceptanceListAdapter;
import com.peihuo.adapter.SortingListAdapter;
import com.peihuo.db.GetAcceptanceListCallback;
import com.peihuo.db.GetSortingListCallback;
import com.peihuo.entity.AcceptanceOrder;
import com.peihuo.entity.SortingOrder;

import java.util.List;

/**
 * Created by 123 on 2017/8/30.
 * 验收单
 */

public class AcceptanceListFragment extends BaseListFragment {
    private AcceptanceListAdapter mAdapter;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acceptance_list, null);
        return view;
    }

    @Override
    protected void initView(View view) {
        mAdapter = new AcceptanceListAdapter(getContext());
        mListView.setAdapter(mAdapter);
        GetAcceptanceListCallback callback = new GetAcceptanceListCallback(getContext(), 10, 0, new GetAcceptanceListCallback.OnLoadDataListener() {
            @Override
            public void onSuccess(List<AcceptanceOrder> list) {
                mAdapter.setData(list);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {

            }
        });
    }
}
