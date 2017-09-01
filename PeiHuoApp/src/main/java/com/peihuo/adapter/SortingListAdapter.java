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
import com.peihuo.system.SystemConfig;

import java.util.ArrayList;

/**
 * Created by 123 on 2017/8/31.
 * 分拣单列表
 */

public class SortingListAdapter extends BaseAdapter {

    private ArrayList<SortingForm> mList;
    Activity mActivity;

    public SortingListAdapter(Activity context) {
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
        if (mList != null && mList.size() > position) {
            SortingForm order = mList.get(position);
            viewHolder.code.setText(order.getCode());
            viewHolder.batch.setText(order.getBatchCount());
            viewHolder.status.setText(order.getAcceptanceState());
            viewHolder.operation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, SortingInfoActivity.class);
                    intent.putExtra(SystemConfig.BUNDLE_KEY_SORTING_LIST_INDEX, position);
                    intent.putExtra(SystemConfig.BUNDLE_KEY_SORTING_LIST, mList);
                    mActivity.startActivity(intent);
                    mActivity.finish();
                }
            });
        }

        return convertView;
    }

    public void setData(ArrayList<SortingForm> data) {
        this.mList = data;
    }

    class ViewHolder {
        TextView code, batch, serial, position, status;
        Button operation;
    }
}
