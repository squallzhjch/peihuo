package com.peihuo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.peihuo.fragment.AcceptanceInfoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hb on 2017/9/19.
 */

public class AcceptanceInfoAdapter extends FragmentStatePagerAdapter {
    List<Fragment> mList = new ArrayList<>();
    public AcceptanceInfoAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
//    @Override
//    public int getItemPosition(Object object) {
//        // TODO Auto-generated method stub
//        return PagerAdapter.POSITION_NONE;
//    }

    public void setData(List<Fragment> list) {
        mList = list;
    }

    public void addFragment(AcceptanceInfoFragment fragment) {
        if(fragment != null){
            mList.add(fragment);
        }
    }
}
