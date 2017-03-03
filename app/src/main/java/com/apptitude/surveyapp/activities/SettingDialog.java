package com.apptitude.surveyapp.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.apptitude.surveyapp.R;
import com.apptitude.surveyapp.models.SettingModel;
import com.apptitude.surveyapp.utils.CommonUtils;
import com.apptitude.surveyapp.utils.Constants;
import com.github.jjobes.slidedaytimepicker.SlideDayTimeListener;
import com.github.jjobes.slidedaytimepicker.SlideDayTimePicker;

import java.util.Calendar;

/**
 * Created by hle59 on 3/2/2017.
 */

public class SettingDialog extends Dialog {

    private EditText mEdtDeviceDescription,mEdtMainTitle, mEdtSubTitle, mEdtInputEmail;
    private EditText mEdtDailyTime, mEdtWeeklyTime, mEdtFeedbackMainTitle, mEdtFeedbackSubTitle, mEdtSubmitMainTitle;
    private Button mBtnSelectBg, mBtnSelectLogo;
    private RadioButton mChkDaily, mChkWeekly;
    private TextView mTvBgPath, mTvLogoPath;
    private String bgPath;
    private String logoPath;

    private Context mContext;

    public SettingDialog(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setting_dialog);
        mContext = context;
        initLayout();
        setCanceledOnTouchOutside(false);
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
        mBtnSelectBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryIntent(Constants.SELECT_BG_FILE);
            }
        });
        mBtnSelectLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryIntent(Constants.SELECT_LOGO_FILE);
            }
        });
        mTvBgPath = (TextView)findViewById(R.id.tv_bg_more_description);
        mTvLogoPath = (TextView)findViewById(R.id.tv_logo_more_description);
        mEdtWeeklyTime.setInputType(InputType.TYPE_NULL);
        mEdtDailyTime.setInputType(InputType.TYPE_NULL);

        mEdtWeeklyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SlideDayTimePicker.Builder(((FragmentActivity)mContext).getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDay(1)
                        .setInitialHour(13)
                        .setInitialMinute(30)
                        //.setIs24HourTime(false)
                        //.setCustomDaysArray(getResources().getStringArray(R.array.days_of_week))
                        //.setTheme(SlideDayTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
            }
        });
        mEdtDailyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(mContext, AlertDialog.THEME_DEVICE_DEFAULT_DARK, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mEdtDailyTime.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
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
        SettingModel setting = CommonUtils.getSetting(mContext);
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
            mTvBgPath.setText(mContext.getString(R.string.upload_file_successfuly).replace("%f", CommonUtils.getFileName(setting.getBackgroundPath())));
            mTvLogoPath.setText(mContext.getString(R.string.upload_file_successfuly).replace("%f", CommonUtils.getFileName(setting.getLogoPath())));
        }
    }


    final SlideDayTimeListener listener = new SlideDayTimeListener() {

        @Override
        public void onDayTimeSet(int day, int hour, int minute)
        {
            String dayOfWeek = "";
            switch (day){
                case 1:
                    dayOfWeek = "Sun";
                    break;
                case 2:
                    dayOfWeek = "Mon";
                    break;
                case 3:
                    dayOfWeek = "Tue";
                    break;
                case 4:
                    dayOfWeek = "Wed";
                    break;
                case 5:
                    dayOfWeek = "Thu";
                    break;
                case 6:
                    dayOfWeek = "Fri";
                    break;
                case 7:
                    dayOfWeek = "Sat";
                    break;
                default:
                    dayOfWeek = "Mon";
                    break;

            }

            mEdtWeeklyTime.setText(dayOfWeek + ";" + String.valueOf(hour) + ":" + String.valueOf(minute) + ":00");
        }

        @Override
        public void onDayTimeCancel()
        {
        }
    };


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

        CommonUtils.saveSetting(mContext, setting);
    }

    private void galleryIntent(int selectType)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity)mContext).startActivityForResult(Intent.createChooser(intent, mContext.getString(R.string.select_file)), selectType);
    }

    public void setBgPath(String path){
        mTvBgPath.setText(mContext.getString(R.string.upload_file_successfuly).replace("%f", CommonUtils.getFileName(path)));
    }

    public void setLogoPath(String path){
        mTvLogoPath.setText(mContext.getString(R.string.upload_file_successfuly).replace("%f", CommonUtils.getFileName(path)));
    }

//    @Override
//    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
//
//    }
}
