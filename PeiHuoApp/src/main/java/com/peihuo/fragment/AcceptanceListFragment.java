package com.peihuo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peihuo.R;
import com.peihuo.adapter.AcceptanceListAdapter;
import com.peihuo.db.QueryAcceptanceListCallback;
import com.peihuo.entity.AcceptanceForm;

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
        QueryAcceptanceListCallback callback = new QueryAcceptanceListCallback(getContext(), 10, 0, new QueryAcceptanceListCallback.OnLoadDataListener() {
            @Override
            public void onSuccess(List<AcceptanceForm> list) {
                mAdapter.setData(list);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {

            }
        });
    }
}
