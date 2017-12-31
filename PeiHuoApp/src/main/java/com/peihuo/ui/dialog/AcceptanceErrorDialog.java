package com.peihuo.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.peihuo.R;

/**
 * Created by hb on 2017/10/27.
 */

public class AcceptanceErrorDialog extends MyDialog {

    private Button cancelButton, okButton;

    public AcceptanceErrorDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_acceptance_error);
        setCanceledOnTouchOutside(false);

        cancelButton = (Button) findViewById(R.id.cancel);
        okButton = (Button) findViewById(R.id.submit);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    public void setOnSubmitListener(final OnSubmitListener listener){
        if(okButton != null && listener != null){
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onSubmit();
                }
            });
        }
    }

    @Override
    public void request(Object obj) {

    }

    public interface OnSubmitListener{
        void onSubmit();
    }
}
