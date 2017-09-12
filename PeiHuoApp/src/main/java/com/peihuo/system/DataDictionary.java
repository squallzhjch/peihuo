package com.peihuo.system;

import android.content.Context;

import com.peihuo.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hb on 2017/9/3.
 */

public class DataDictionary {

    private static volatile DataDictionary mInstance;

    private Map<String, String> acceptanceStateMap;//验收状态

    private Map<String, String> sortingStateMap;//分拣状态
    private Map<String, Boolean> sortingSingleOrGroupMap;//单品套餐
    private Map<String, Boolean> acceptanceSingleOrGroupMap;//单品套餐

    private Context mContext;

    public static DataDictionary getInstance() {

        if (mInstance == null) {
            synchronized (DataDictionary.class) {
                if (mInstance == null) {
                    mInstance = new DataDictionary();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
        initSortingStateMap();
        initAcceptanceStateMap();
        initSingleOrGroup();
    }

    private void initSortingStateMap() {
        sortingStateMap = new HashMap<>();
        sortingStateMap.put("0", mContext.getString(R.string.dictionary_acceptance_status_wait_sorting));
        sortingStateMap.put("1", mContext.getString(R.string.dictionary_acceptance_status_wait_acceptance));
        sortingStateMap.put("2", mContext.getString(R.string.dictionary_acceptance_status_pass));
        sortingStateMap.put("3", mContext.getString(R.string.dictionary_acceptance_status_no_pass));
    }

    private void initSingleOrGroup() {
        sortingSingleOrGroupMap = new HashMap<>();
        sortingSingleOrGroupMap.put("1111", true);
        sortingSingleOrGroupMap.put("0000", false);

        acceptanceSingleOrGroupMap = new HashMap<>();
        acceptanceSingleOrGroupMap.put("是", true);
        acceptanceSingleOrGroupMap.put("否", false);
    }

    private void initAcceptanceStateMap() {
//        acceptanceStateMap = new HashMap<>();
//        acceptanceStateMap.put("0", mContext.getString(R.string.dictionary_acceptance_status_wait_sorting));
//        acceptanceStateMap.put("1", mContext.getString(R.string.dictionary_acceptance_status_wait_acceptance));
    }

    public String getSortingState(String key) {
        if(key == null)
            return "";
        if (sortingStateMap != null && sortingStateMap.containsKey(key))
            return sortingStateMap.get(key);
        return "";
    }

    public boolean isSortingSingleOrGroup(String key) {
        if(key == null)
            return false;
        if (sortingSingleOrGroupMap != null && sortingSingleOrGroupMap.containsKey(key))
            return sortingSingleOrGroupMap.get(key);
        return false;
    }

    public boolean isAcceptanceSingleOrGroup(String key) {
        if(key == null)
            return false;
        if (acceptanceSingleOrGroupMap != null && acceptanceSingleOrGroupMap.containsKey(key))
            return acceptanceSingleOrGroupMap.get(key);
        return false;
    }
}
