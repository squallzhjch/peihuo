package com.peihuo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.peihuo.R;

/**
 * Created by 123 on 2017/8/31.
 */

public abstract class BaseListFragment extends Fragment {
    protected View fragmentV;

    private PullToRefreshListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//		Log.e("jingo",  getClass().getSimpleName() + " onCreateView " );
        fragmentV = createView(inflater, container, savedInstanceState);
        initPullList(fragmentV);
        initView(fragmentV);
        return fragmentV;
    }

    /**
     * 此frament的布局
     *
     * @return
     */
    protected abstract View createView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState);

    /**
     * 在此可操作控件（修改控件，赋值等）
     *
     * @param view
     */
    protected abstract void initView(View view);


    private void initPullList(View view) {
        if (view == null)
            return;
        View listView = view.findViewById(R.id.plan_activity_fragment_list);
        if (listView != null && listView instanceof PullToRefreshListView) {
            mListView = (PullToRefreshListView) listView;

            ILoadingLayout end =  mListView.getLoadingLayoutProxy(false, true);
            end.setPullLabel("1");
            end.setRefreshingLabel("2");
            end.setReleaseLabel("4");
        }
    }

}
