package com.peihuo.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.peihuo.R;
import com.peihuo.adapter.SortingListAdapter;
import com.peihuo.entity.SortingForm;
import com.peihuo.net.QuerySortingListCallback;
import com.peihuo.system.SharedConfigHelper;

import java.util.ArrayList;

/**
 * Created by hb on 2017/8/29.
 * 分拣单列表
 */

public class SortingListFragment extends BaseListFragment {

    private SortingListAdapter mAdapter;
    private QuerySortingListCallback callback;

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sorting_list, null);
        return view;
    }

    @Override
    protected void initView(View view) {
        mAdapter = new SortingListAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        String userId = SharedConfigHelper.getInstance().getUserId();

        callback = new QuerySortingListCallback(getContext(), userId, 10, 0, new QuerySortingListCallback.OnLoadDataListener() {
            @Override
            public void onSuccess(ArrayList<SortingForm> list) {
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
                mAdapter.addData(list);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError() {
                mListView.onRefreshComplete();
            }
        });

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.e("jingo","onRefresh");
                int count = mAdapter.getCount();
                int page = 0;
                if (count % 10 > 0) {
                    page = count / 10 + 1;
                } else {
                    page = count / 10;
                }
                callback.loadData(SharedConfigHelper.getInstance().getUserId(),10, page);
            }
        });
    }
}
