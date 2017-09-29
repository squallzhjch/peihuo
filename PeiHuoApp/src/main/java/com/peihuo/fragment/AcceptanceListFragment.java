package com.peihuo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.peihuo.R;
import com.peihuo.adapter.AcceptanceListAdapter;
import com.peihuo.entity.AcceptanceForm;
import com.peihuo.net.QueryAcceptanceListCallback;
import com.peihuo.system.SharedConfigHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2017/8/30.
 * 验收单
 */

public class AcceptanceListFragment extends BaseListFragment {
    private AcceptanceListAdapter mAdapter;
    private QueryAcceptanceListCallback callback;
    private int mState = 1;
    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acceptance_list, null);
        return view;
    }

    @Override
    protected void initView(View view) {
        mAdapter = new AcceptanceListAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        callback = new QueryAcceptanceListCallback(getContext(),  SharedConfigHelper.getInstance().getWorkLineId(),10, 0, mState, new QueryAcceptanceListCallback.OnLoadDataListener() {
            @Override
            public void onSuccess(ArrayList<AcceptanceForm> list) {
                mListView.onRefreshComplete();

                if(list.size() < 10){
                    mListView.setMode(PullToRefreshBase.Mode.DISABLED);
                }else {
                    mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                }

                if (list.size() == 0) {
                    Toast.makeText(getContext(), getText(R.string.toast_noting_data), Toast.LENGTH_SHORT).show();
                    return;
                }
                mAdapter.setState(mState);
                mAdapter.addData(list);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                mListView.onRefreshComplete();
                Toast.makeText(getContext(), getText(R.string.toast_load_data_error), Toast.LENGTH_SHORT).show();

            }
        });
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                int count = mAdapter.getCount();
                int page = 0;
                if (count % 10 > 0) {
                    page = count / 10 + 1;
                } else {
                    page = count / 10;
                }
                callback.loadData(10, page);
                mListView.onRefreshComplete();
            }
        });
    }

    public void setStateType(int type){
        mState = type;
        new QueryAcceptanceListCallback(getContext(),  SharedConfigHelper.getInstance().getWorkLineId(),10, 0, mState, new QueryAcceptanceListCallback.OnLoadDataListener() {
            @Override
            public void onSuccess(ArrayList<AcceptanceForm> list) {
                mListView.onRefreshComplete();

                if(list.size() < 10){
                    mListView.setMode(PullToRefreshBase.Mode.DISABLED);
                }else {
                    mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                }

                if (list.size() == 0) {
                    Toast.makeText(getContext(), getText(R.string.toast_noting_data), Toast.LENGTH_SHORT).show();
                    return;
                }
                mAdapter.setState(mState);
                mAdapter.setData(list);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                mListView.onRefreshComplete();
                Toast.makeText(getContext(), getText(R.string.toast_load_data_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
