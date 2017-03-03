package com.apptitude.surveyapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;

import com.apptitude.surveyapp.R;
import com.apptitude.surveyapp.utils.CommonUtils;

/**
 * Created by hle59 on 3/2/2017.
 */

public class PasswordDialog extends Dialog {

    private EditText mEdtPass;
    private Button mBtnConfirm;

    public PasswordDialog(Context context) {
        super(context);
        setContentView(R.layout.password_dialog);
    }

    private void initLayout()
    {
        mEdtPass = (EditText)findViewById(R.id.edt_password);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String val = mEdtPass.getText().toString();
                if(val.equalsIgnoreCase(CommonUtils.getDynamicPassword())) {

                }
            }
        });
    }
}
