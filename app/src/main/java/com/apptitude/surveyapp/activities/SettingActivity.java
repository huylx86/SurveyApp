package com.apptitude.surveyapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.apptitude.surveyapp.R;
import com.apptitude.surveyapp.models.SettingModel;
import com.apptitude.surveyapp.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends Activity {

    private EditText mEdtDeviceDescription,mEdtMainTitle, mEdtSubTitle, mEdtInputEmail;
    private EditText mEdtDailyTime, mEdtWeeklyTime, mEdtFeedbackMainTitle, mEdtFeedbackSubTitle, mEdtSubmitMainTitle;
    private Button mBtnSelectBg, mBtnSelectLogo;
    private RadioButton mChkDaily, mChkWeekly;
    private TextView mTvBgPath, mTvLogoPath;
    private String bgPath;
    private String logoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    private void initLayout()
    {
        mEdtDeviceDescription = (EditText)findViewById(R.id.edt_device_description);
        mEdtMainTitle = (EditText)findViewById(R.id.edt_main_title);
        mEdtSubTitle = (EditText)findViewById(R.id.edt_sub_title);
        mEdtInputEmail = (EditText)findViewById(R.id.edt_input_email);
        mEdtDailyTime = (EditText)findViewById(R.id.edt_daily_time);
        mEdtWeeklyTime = (EditText)findViewById(R.id.edt_weekly_time);
        mEdtFeedbackMainTitle = (EditText)findViewById(R.id.edt_feedback_main_title);
        mEdtFeedbackSubTitle = (EditText)findViewById(R.id.edt_feedback_sub_title);
        mEdtSubmitMainTitle = (EditText)findViewById(R.id.edt_submit_main_title);
        mBtnSelectBg = (Button)findViewById(R.id.btn_select_bg);
        mBtnSelectLogo = (Button)findViewById(R.id.btn_select_logo);
        mChkDaily = (RadioButton)findViewById(R.id.chk_daily);
        mChkWeekly = (RadioButton)findViewById(R.id.chk_weekly);
    }

    private boolean isValidData()
    {
        String mainTitle = mEdtMainTitle.getText().toString();
        if(mainTitle.equalsIgnoreCase("")){
            return false;
        }

        String subTitle = mEdtSubTitle.getText().toString();
        if(subTitle.equalsIgnoreCase("")){
            return false;
        }

        String deviceDescription = mEdtDeviceDescription.getText().toString();
        if(deviceDescription.equalsIgnoreCase("")){
            return false;
        }

        if(bgPath == null || (bgPath != null && bgPath.equalsIgnoreCase(""))){
            return false;
        }
        if(logoPath == null || (logoPath != null && logoPath.equalsIgnoreCase(""))){
            return false;
        }

        String email = mEdtInputEmail.getText().toString();
        if(email.equalsIgnoreCase("")){
            return false;
        }

        if(mChkDaily.isChecked()){
            String time = mEdtDailyTime.getText().toString();
            if(time.equalsIgnoreCase("")){
                return false;
            }
        } else {
            String time = mEdtWeeklyTime.getText().toString();
            if(time.equalsIgnoreCase("")){
                return false;
            }
        }

        String feedbackMainTitle = mEdtFeedbackMainTitle.getText().toString();
        if(feedbackMainTitle.equalsIgnoreCase("")){
            return false;
        }
        String feedbackSubTitle = mEdtFeedbackSubTitle.getText().toString();
        if(feedbackSubTitle.equalsIgnoreCase("")){
            return false;
        }
        String submitMainTitle = mEdtSubmitMainTitle.getText().toString();
        if(submitMainTitle.equalsIgnoreCase("")){
            return false;
        }
        return true;
    }

    private void initSetting()
    {
        SettingModel setting = CommonUtils.getSetting(this);
        if(setting != null){
            mEdtDailyTime.setText(setting.getDailyTime());
            mEdtDeviceDescription.setText(setting.getDeviceDescription());
            mEdtFeedbackMainTitle.setText(setting.getFeedbackMainTitle());
            mEdtFeedbackSubTitle.setText(setting.getFeedbackSubTitle());
            mEdtInputEmail.setText(TextUtils.join(";", setting.getLstEmails()));
            mEdtMainTitle.setText(setting.getMainTitle());
            mEdtSubmitMainTitle.setText(setting.getSubmitMainTitle());
            mEdtWeeklyTime.setText(setting.getWeeklyTime());
            if(setting.isDailySending()){
                mChkDaily.setChecked(true);
                mChkWeekly.setChecked(false);
            } else {
                mChkDaily.setChecked(false);
                mChkWeekly.setChecked(true);
            }
            mTvBgPath.setText(getString(R.string.upload_file_successfuly) + CommonUtils.getFileName(setting.getBackgroundPath()));
            mTvLogoPath.setText(getString(R.string.upload_file_successfuly) + CommonUtils.getFileName(setting.getLogoPath()));
        }
    }

    private void saveSetting()
    {
        SettingModel setting = new SettingModel();
        setting.setBackgroundPath(bgPath);
        if(mChkDaily.isChecked()) {
            setting.setDailySending(true);
        } else {
            setting.setDailySending(false);
        }
        setting.setDailyTime(mEdtDailyTime.getText().toString());
        setting.setWeeklyTime(mEdtWeeklyTime.getText().toString());
        setting.setDeviceDescription(mEdtDeviceDescription.getText().toString());
        setting.setFeedbackMainTitle(mEdtFeedbackMainTitle.getText().toString());
        setting.setFeedbackSubTitle(mEdtFeedbackSubTitle.getText().toString());
        setting.setLogoPath(logoPath);

        String[] emails = mEdtInputEmail.getText().toString().split(";");
        setting.setLstEmails(emails);
        setting.setMainTitle(mEdtMainTitle.getText().toString());
        setting.setSubTitle(mEdtSubTitle.getText().toString());
        setting.setSubmitMainTitle(mEdtSubmitMainTitle.getText().toString());

        CommonUtils.saveSetting(this, setting);
    }

}
