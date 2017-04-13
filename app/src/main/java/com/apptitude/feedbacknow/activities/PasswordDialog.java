package com.apptitude.feedbacknow.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.apptitude.feedbacknow.R;
import com.apptitude.feedbacknow.utils.CommonUtils;
import com.apptitude.feedbacknow.utils.Constants;

/**
 * Created by hle59 on 3/2/2017.
 */

public class PasswordDialog extends Dialog {

    private EditText mEdtPass;
    private Button mBtnConfirm;
    private Handler mHandlerShowSetting;
    private Context mContext;

    public PasswordDialog(@NonNull Context context, @StyleRes int themeResId, Handler handlerShowSetting) {
        super(context, themeResId);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mHandlerShowSetting = handlerShowSetting;
        setContentView(R.layout.password_dialog);
        initLayout();
    }

    private void initLayout()
    {
        mEdtPass = (EditText)findViewById(R.id.edt_password);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String val = mEdtPass.getText().toString();
                String pass = CommonUtils.getDynamicPassword();
                if(val.equalsIgnoreCase(pass)) {
                    hideSoftKeyboard((Activity) mContext, mEdtPass);
                    hide();
                    mHandlerShowSetting.sendEmptyMessage(Constants.SHOW_SETTING_DIALOG);
                } else {
                    String message = "Password is incorrect. Please try again!";
                    CommonUtils.showDialog(mContext, mContext.getString(R.string.title_alert_dialog), message);
                }
            }
        });
    }

    private void hideSoftKeyboard(Activity activity, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
