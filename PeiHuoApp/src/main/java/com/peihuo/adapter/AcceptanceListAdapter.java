package com.peihuo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.peihuo.R;
import com.peihuo.activity.AcceptanceInfoActivity;
import com.peihuo.entity.AcceptanceForm;
import com.peihuo.entity.SortingForm;
import com.peihuo.system.DataDictionary;
import com.peihuo.system.SystemConfig;

import java.util.ArrayList;

/**
 * Created by 123 on 2017/8/31.
 * 分拣单列表
 */

public class AcceptanceListAdapter extends BaseAdapter {

    private ArrayList<AcceptanceForm> mList = new ArrayList<>();
    Activity mActivity;

    public AcceptanceListAdapter(Activity context) {
        mActivity = context;
    }

    @Override
    public int getCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mList != null && mList.size() > position)
            return mList.get(position);
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
                    R.layout.adapter_acceptance_list, null);

            viewHolder = new ViewHolder();
            viewHolder.code = (TextView) convertView.findViewById(R.id.adapter_acceptance_code);
            viewHolder.batch = (TextView) convertView.findViewById(R.id.adapter_acceptance_batch);
            viewHolder.operation = (Button) convertView.findViewById(R.id.adapter_acceptance_operation);
            viewHolder.path = (TextView) convertView.findViewById(R.id.adapter_acceptance_path);
            viewHolder.customer = (TextView) convertView.findViewById(R.id.adapter_acceptance_customer);
            viewHolder.startTime = (TextView)convertView.findViewById(R.id.adapter_acceptance_start_time);
            viewHolder.total = (TextView)convertView.findViewById(R.id.adapter_acceptance_total);
            viewHolder.status = (TextView) convertView.findViewById(R.id.adapter_acceptance_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(mList != null && mList.size() > position) {
            AcceptanceForm order = mList.get(position);
            if(order.getCode() != null)
                viewHolder.code.setText(order.getCode());
            if(order.getBatchCount() != null)
                viewHolder.batch.setText(order.getBatchCount());
            if(order.getAcceptanceState() != null)
                viewHolder.status.setText(DataDictionary.getInstance().getSortingState(order.getAcceptanceState()));

            if(order.getCustomerId()  != null)
                viewHolder.customer.setText(order.getCustomerId());

            if(!TextUtils.isEmpty(order.getStartTime())){
                if (order.getStartTime().length() > 16) {
                    viewHolder.startTime.setText(order.getStartTime().substring(0, 16));
                }else{
                    viewHolder.startTime.setText(order.getStartTime());
                }
            }

            viewHolder.total.setText(String.valueOf(order.getSuitUniteProductCount()));

            if(order.getTransferPath() != null) {
                viewHolder.path.setText(order.getTransferPath());
            }

            viewHolder.operation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, AcceptanceInfoActivity.class);
                    intent.putExtra(SystemConfig.BUNDLE_KEY_SORTING_LIST_INDEX, position);
                    intent.putExtra(SystemConfig.BUNDLE_KEY_SORTING_LIST, mList);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }
            });
        }

        return convertView;
    }

    public void setData(ArrayList<AcceptanceForm> data) {
        this.mList = data;
    }

    public void addData(ArrayList<AcceptanceForm> list) {
        if(mList != null && list != null){
            for(AcceptanceForm form:list){
                mList.add(form);
            }
        }
    }

    class ViewHolder {
        TextView code, path, batch, customer, startTime, total, status;
        Button operation;
    }
}
