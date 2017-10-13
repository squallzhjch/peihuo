package com.peihuo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.peihuo.R;

/**
 * Created by 123 on 2017/8/31.
 */

public abstract class BaseListFragment extends Fragment {
    protected View fragmentV;

    protected PullToRefreshListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//		Log.e("jingo",  getClass().getSimpleName() + " onCreateView " );
        if(fragmentV == null) {
            fragmentV = createView(inflater, container, savedInstanceState);
            initPullList(fragmentV);
            initView(fragmentV);
        }
        ViewParent parent =  fragmentV.getParent();
        if (parent != null && parent instanceof ViewGroup){
            ((ViewGroup)parent).removeView(fragmentV);
        }
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
        }
    }

}
