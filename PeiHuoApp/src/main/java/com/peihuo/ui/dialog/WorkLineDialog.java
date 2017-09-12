package com.peihuo.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.peihuo.R;
import com.peihuo.entity.WorkLine;
import com.peihuo.system.SharedConfigHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hb on 2017/9/3.
 */

public class WorkLineDialog extends Dialog {
    public WorkLineDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
    }

    private Button selectButton, okButton;
    private LinearLayout selectLayout;
    private ArrayList<WorkLine> listData;
    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_work_line);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        selectButton = (Button)findViewById(R.id.work_line_select_button);
        selectLayout = (LinearLayout) findViewById(R.id.work_line_list_layout);
        scrollView = (ScrollView) findViewById(R.id.work_line_select_scrollview);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(scrollView.getVisibility() == View.VISIBLE){
                    scrollView.setVisibility(View.GONE);
                }else{
                    scrollView.setVisibility(View.VISIBLE);
                }
            }
        });
        okButton = (Button) findViewById(R.id.work_line_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
        setData(listData);
    }


    public void setButtonOnClickListener(View.OnClickListener listener){
        if(okButton != null){
            okButton.setOnClickListener(listener);
        }
    }

    public void setData(ArrayList<WorkLine> list) {
        listData = list;
        if(selectButton != null && scrollView != null && list != null && list.size() > 0) {
            selectButton.setText(list.get(0).getPipeline());
            selectLayout.removeAllViews();
            for(final WorkLine line:list) {
                View view = View.inflate(getContext(), R.layout.work_line_only_text, null);
                ((TextView) view).setText(line.getPipeline());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,0,1);
                selectLayout.addView(view, params);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        scrollView.setVisibility(View.GONE);
                        SharedConfigHelper.getInstance().setWorkLineId(line.getId());
                        selectButton.setText(((TextView)v).getText());
                    }
                });
            }
        }
    }
}
