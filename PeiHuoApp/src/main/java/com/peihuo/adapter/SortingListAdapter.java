package com.peihuo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.peihuo.R;
import com.peihuo.activity.SortingInfoActivity;
import com.peihuo.entity.SortingForm;
import com.peihuo.system.DataDictionary;
import com.peihuo.system.SharedConfigHelper;
import com.peihuo.system.SystemConfig;

import java.util.ArrayList;

/**
 * Created by 123 on 2017/8/31.
 * 分拣单列表
 */

public class SortingListAdapter extends BaseAdapter {

    private SortingForm[] mList = new SortingForm[SharedConfigHelper.getInstance().getWorkLineHoleNum()];
    Activity mActivity;

    public SortingListAdapter(Activity context) {
        mActivity = context;
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mList != null && mList.length > position)
            return mList[position];
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mActivity).inflate(
                    R.layout.adapter_sorting_list, null);

            viewHolder = new ViewHolder();
            viewHolder.code = (TextView) convertView.findViewById(R.id.adapter_sorting_code);
            viewHolder.batch = (TextView) convertView.findViewById(R.id.adapter_sorting_batch);
            viewHolder.operation = (Button) convertView.findViewById(R.id.adapter_sorting_operation);
            viewHolder.position = (TextView) convertView.findViewById(R.id.adapter_sorting_position);
            viewHolder.serial = (TextView) convertView.findViewById(R.id.adapter_sorting_serial);
            viewHolder.status = (TextView) convertView.findViewById(R.id.adapter_sorting_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (mList != null && mList.length > position) {
            SortingForm order = mList[position];
            if(order == null){
                viewHolder.position.setText(String.valueOf(position + 1));
                viewHolder.operation.setEnabled(false);
            }else {
                viewHolder.operation.setEnabled(true);
                if (order.getBelongorderid() != null) {
                    viewHolder.code.setText(order.getBelongorderid());
                }
                if (order.getBatchCount() != null) {
                    viewHolder.batch.setText(order.getBatchCount());
                }
                viewHolder.status.setText(DataDictionary.getInstance().getSortingState(order.getAcceptanceState()));
                if (order.getAssemblelineno() != null) {
                    viewHolder.serial.setText(order.getAssemblelineno());
                }
                if (order.getPitposition() != null) {
                    viewHolder.position.setText(String.valueOf(position + 1));
                }
                viewHolder.operation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, SortingInfoActivity.class);
                        int index = 0;
                        ArrayList<SortingForm> list = new ArrayList<>();
                        for(int i = 0;i<mList.length;i++){
                            SortingForm form = mList[i];
                            if(form != null){
                                if(form.getPitposition().equals(String.valueOf(position + 1))){
                                    index = list.size();
                                }
                                list.add(form);
                            }

                        }
                        intent.putExtra(SystemConfig.BUNDLE_KEY_SORTING_LIST_INDEX, index);
                        intent.putExtra(SystemConfig.BUNDLE_KEY_SORTING_LIST, list);
                        mActivity.startActivity(intent);
                        mActivity.finish();
                    }
                });
            }
        }

        return convertView;
    }

    public void setData(SortingForm[] data) {
        this.mList = data;
    }

//    public void addData(ArrayList<SortingForm> list){
//        if(mList != null && list != null){
//            for(SortingForm form:list){
//                mList.add(form);
//            }
//        }
//    }

    class ViewHolder {
        TextView code, batch, serial, position, status;
        Button operation;
    }
}
