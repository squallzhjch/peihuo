package com.peihuo.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.peihuo.R;

/**
 * Created by hb on 2017/9/3.
 */

public class WorkLineDialog extends Dialog {
    public WorkLineDialog(@NonNull Context context) {
        super(context, R.style.MyDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.free_exercise_sure_dialog_layout);
        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);
    }
}
