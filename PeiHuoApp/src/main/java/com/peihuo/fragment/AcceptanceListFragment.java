package com.peihuo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peihuo.R;

/**
 * Created by 123 on 2017/8/30.
 * 验收单
 */

public class AcceptanceListFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acceptance_list, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

    }
}
