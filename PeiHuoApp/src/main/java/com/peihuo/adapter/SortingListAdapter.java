package com.peihuo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.peihuo.R;
import com.peihuo.entity.SortingOrder;

import java.util.List;

/**
 * Created by 123 on 2017/8/31.
 * 分拣单列表
 */

public class SortingListAdapter extends BaseAdapter {

    private List<SortingOrder> mList;
    Context mContext;

    public SortingListAdapter(Context context) {
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
                    R.layout.adapter_sorting_list, null);

            viewHolder = new ViewHolder();
            viewHolder.code = (TextView) convertView.findViewById(R.id.adapter_sorting_code);
            viewHolder.batch = (TextView) convertView.findViewById(R.id.adapter_sorting_batch);
            viewHolder.operation = (Button) convertView.findViewById(R.id.adapter_sorting_operation);
            viewHolder.position = (Button) convertView.findViewById(R.id.adapter_sorting_position);
            viewHolder.serial = (TextView) convertView.findViewById(R.id.adapter_sorting_serial);
            viewHolder.status = (TextView) convertView.findViewById(R.id.adapter_sorting_status);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(mList != null && mList.size() > position) {
            SortingOrder order = mList.get(position);
            viewHolder.code.setText(order.getCode());
            viewHolder.batch.setText(order.getBatchCount());
            viewHolder.status.setText(order.getAcceptanceState());
        }

        return convertView;
    }

    class ViewHolder {
        TextView code, batch, serial, position, status;
        Button operation;
    }
}
