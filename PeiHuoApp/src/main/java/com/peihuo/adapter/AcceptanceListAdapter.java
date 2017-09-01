package com.peihuo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.peihuo.R;
import com.peihuo.entity.AcceptanceForm;

import java.util.List;

/**
 * Created by 123 on 2017/8/31.
 * 分拣单列表
 */

public class AcceptanceListAdapter extends BaseAdapter {

    private List<AcceptanceForm> mList;
    Context mContext;

    public AcceptanceListAdapter(Context context) {
        mContext = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.adapter_acceptance_list, null);

            viewHolder = new ViewHolder();
            viewHolder.code = (TextView) convertView.findViewById(R.id.adapter_acceptance_code);
            viewHolder.batch = (TextView) convertView.findViewById(R.id.adapter_acceptance_batch);
            viewHolder.operation = (Button) convertView.findViewById(R.id.adapter_acceptance_operation);
            viewHolder.path = (TextView) convertView.findViewById(R.id.adapter_acceptance_path);
            viewHolder.customer = (TextView) convertView.findViewById(R.id.adapter_acceptance_customer);
            viewHolder.endTime = (TextView)convertView.findViewById(R.id.adapter_acceptance_end_time);
            viewHolder.total = (TextView)convertView.findViewById(R.id.adapter_acceptance_total);
            viewHolder.status = (TextView) convertView.findViewById(R.id.adapter_acceptance_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(mList != null && mList.size() > position) {
            AcceptanceForm order = mList.get(position);
            viewHolder.code.setText(order.getCode());
            viewHolder.batch.setText(order.getBatchCount());
            viewHolder.status.setText(order.getAcceptanceState());
            viewHolder.endTime.setText(order.getEndTime().substring(0,16));
            viewHolder.total.setText(String.valueOf(order.getSuitUniteProductCount()));
            viewHolder.path.setText(order.getPath());
        }

        return convertView;
    }

    public void setData(List<AcceptanceForm> data) {
        this.mList = data;
    }

    class ViewHolder {
        TextView code, path, batch, customer, endTime, total, status;
        Button operation;
    }
}
