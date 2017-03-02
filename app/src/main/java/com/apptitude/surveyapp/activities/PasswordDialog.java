package com.apptitude.surveyapp.activities;

import android.app.Dialog;
import android.content.Context;

import com.apptitude.surveyapp.R;

/**
 * Created by hle59 on 3/2/2017.
 */

public class PasswordDialog extends Dialog {

    public PasswordDialog(Context context) {
        super(context);
        setContentView(R.layout.password_dialog);
    }
}
