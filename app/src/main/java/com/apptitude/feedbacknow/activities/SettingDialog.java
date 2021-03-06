package com.apptitude.feedbacknow.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.apptitude.feedbacknow.R;
import com.apptitude.feedbacknow.models.FontStyleModel;
import com.apptitude.feedbacknow.models.SettingModel;
import com.apptitude.feedbacknow.utils.CommonUtils;
import com.apptitude.feedbacknow.utils.Constants;
import com.github.jjobes.slidedaytimepicker.SlideDayTimeListener;
import com.github.jjobes.slidedaytimepicker.SlideDayTimePicker;

import java.util.Calendar;

/**
 * Created by hle59 on 3/2/2017.
 */

public class SettingDialog extends Dialog {

    private EditText mEdtDeviceDescription,mEdtMainTitle, mEdtSubTitle, mEdtInputEmail;
    private EditText mEdtFeedbackMainTitle, mEdtFeedbackSubTitle, mEdtSubmitMainTitle;
    private EditText mEdtDeviceDesFontSize, mEdtMainTitleFontSize, mEdtSubTitleFontSize, mEdtFeedbackMainTitleFontSize,
                     mEdtFeedbackSubTitleFontSize, mEdtSubmittedTitleFontSize;
    private TextView mTvDeviceDesBold, mTvMainTitleBold, mTvSubTitleBold, mTvFeedbackMainTitleBold, mTvFeedbackSubTitleBold,
                     mTvSubmittedTitleBold;
    private TextView mTvBgMoreResolution, mTvLogoMoreResolution;

    private Button mBtnSelectBg, mBtnSelectLogo, mBtnConfirm;
    private RadioButton mChkDaily, mChkWeekly;
    private TextView mTvBgPath, mTvLogoPath, mTvDailyTime, mTvWeeklyTime;
    private String bgPath;
    private String logoPath;

    private int dailyHour, dailyMinute, weeklyHour, weeklyMinute, dayOfWeeks;
    private boolean isDailyPreviousSending;

    private Context mContext;
    private Handler mHandlerApplyConfiguration;

    private FontStyleModel mDeviceDescriptionFontStyle, mMainTitleFontStyle, mSubTitleFontStyle, mFeedbackMainTitleFontStyle,
                           mFeedbackSubTitleFontStyle, mSubmittedTitleFontStyle;

    public SettingDialog(@NonNull Context context, @StyleRes int themeResId, Handler handlerApplyConfiguration) {
        super(context, themeResId);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.setting_dialog);
        mContext = context;
        mHandlerApplyConfiguration = handlerApplyConfiguration;
        initLayout();
        setCanceledOnTouchOutside(false);
    }

    private void initLayout()
    {
        mEdtDeviceDescription = (EditText)findViewById(R.id.edt_device_description);
        mEdtMainTitle = (EditText)findViewById(R.id.edt_main_title);
        mEdtSubTitle = (EditText)findViewById(R.id.edt_sub_title);
        mEdtInputEmail = (EditText)findViewById(R.id.edt_input_email);
        mTvDailyTime = (TextView) findViewById(R.id.tv_daily_time);
        mTvWeeklyTime = (TextView) findViewById(R.id.tv_weekly_time);
        mEdtFeedbackMainTitle = (EditText)findViewById(R.id.edt_feedback_main_title);
        mEdtFeedbackSubTitle = (EditText)findViewById(R.id.edt_feedback_sub_title);
        mEdtSubmitMainTitle = (EditText)findViewById(R.id.edt_submit_main_title);
        mBtnSelectBg = (Button)findViewById(R.id.btn_select_bg);
        mBtnSelectLogo = (Button)findViewById(R.id.btn_select_logo);
        mChkDaily = (RadioButton)findViewById(R.id.chk_daily);
        mChkWeekly = (RadioButton)findViewById(R.id.chk_weekly);
        mBtnConfirm = (Button)findViewById(R.id.btn_confirm);
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
        mTvDeviceDesBold = (TextView)findViewById(R.id.tv_device_description_bold);
        mTvMainTitleBold = (TextView)findViewById(R.id.tv_main_title_bold);
        mTvSubTitleBold = (TextView)findViewById(R.id.tv_sub_title_bold);
        mTvFeedbackMainTitleBold = (TextView)findViewById(R.id.tv_feedback_main_title_bold);
        mTvFeedbackSubTitleBold = (TextView)findViewById(R.id.tv_feedback_sub_title_bold);
        mTvSubmittedTitleBold = (TextView)findViewById(R.id.tv_submitted_title_bold);
        mEdtDeviceDesFontSize = (EditText)findViewById(R.id.edt_device_description_size);
        mEdtMainTitleFontSize = (EditText)findViewById(R.id.edt_main_title_size);
        mEdtSubTitleFontSize = (EditText)findViewById(R.id.edt_sub_title_size);
        mEdtFeedbackMainTitleFontSize = (EditText)findViewById(R.id.edt_feedback_main_title_size);
        mEdtFeedbackSubTitleFontSize = (EditText)findViewById(R.id.edt_feedback_sub_title_size);
        mEdtSubmittedTitleFontSize = (EditText)findViewById(R.id.edt_submitted_title_size);
        mTvBgMoreResolution = (TextView)findViewById(R.id.tv_bg_more_resolution);
        mTvLogoMoreResolution = (TextView)findViewById(R.id.tv_logo_more_resolution);

//        mEdtWeeklyTime.setInputType(InputType.TYPE_NULL);
//        mTvDailyTime.setInputType(InputType.TYPE_NULL);

        mTvWeeklyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SlideDayTimePicker.Builder(((FragmentActivity)mContext).getSupportFragmentManager())
                        .setListener(listener)
                        .setInitialDay(1)
                        .setInitialHour(13)
                        .setInitialMinute(30)
                        .setIs24HourTime(true)
                        //.setCustomDaysArray(getResources().getStringArray(R.array.days_of_week))
                        //.setTheme(SlideDayTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
            }
        });
        mTvDailyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(mContext, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String minute;
                        if(selectedMinute < 10) {
                            minute = "0" + String.valueOf(selectedMinute);
                        } else {
                            minute = String.valueOf(selectedMinute);
                        }

                        String hour;
                        if(selectedHour < 10) {
                            hour = "0" + String.valueOf(selectedHour);
                        } else {
                            hour = String.valueOf(selectedHour);
                        }
                        dailyHour = selectedHour;
                        dailyMinute = selectedMinute;
                        mTvDailyTime.setText(hour + ":" + minute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidData()) {
                    saveSetting();
                    mHandlerApplyConfiguration.sendEmptyMessage(Constants.APPLY_CONFIGURATION);
                    hide();
                }
            }
        });
        mChkDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChkDaily.setChecked(true);
                mChkWeekly.setChecked(false);
            }
        });
        mChkWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChkWeekly.setChecked(true);
                mChkDaily.setChecked(false);
            }
        });
        mTvDeviceDesBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isBold = !mDeviceDescriptionFontStyle.isBold();
                setBoldDeviceDescription(isBold);
                mDeviceDescriptionFontStyle.setBold(isBold);
            }
        });
        mTvMainTitleBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isBold = !mMainTitleFontStyle.isBold();
                setBoldMainTitle(isBold);
                mMainTitleFontStyle.setBold(isBold);
            }
        });
        mTvSubTitleBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isBold = !mSubTitleFontStyle.isBold();
                setBoldSubTitle(isBold);
                mSubTitleFontStyle.setBold(isBold);
            }
        });
        mTvFeedbackMainTitleBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isBold = !mFeedbackMainTitleFontStyle.isBold();
                setBoldFeedbackMainTitle(isBold);
                mFeedbackMainTitleFontStyle.setBold(isBold);
            }
        });
        mTvFeedbackSubTitleBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isBold = !mFeedbackSubTitleFontStyle.isBold();
                setBoldFeedbackSubTitle(isBold);
                mFeedbackSubTitleFontStyle.setBold(isBold);
            }
        });
        mTvSubmittedTitleBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isBold = !mSubmittedTitleFontStyle.isBold();
                setBoldSubmittedTitle(isBold);
                mSubmittedTitleFontStyle.setBold(isBold);
            }
        });
        initSetting();
    }

    private void setBoldDeviceDescription(boolean isBold)
    {
        if(isBold){
            mTvDeviceDesBold.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mTvDeviceDesBold.setTypeface(mTvDeviceDesBold.getTypeface(), Typeface.BOLD);
        } else {
            mTvDeviceDesBold.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
            mTvDeviceDesBold.setTypeface(null, Typeface.NORMAL);
        }
    }

    private void setBoldMainTitle(boolean isBold)
    {
        if(isBold){
            mTvMainTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mTvMainTitleBold.setTypeface(mTvMainTitleBold.getTypeface(), Typeface.BOLD);
        } else {
            mTvMainTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
            mTvMainTitleBold.setTypeface(null, Typeface.NORMAL);
        }
    }

    private void setBoldSubTitle(boolean isBold)
    {
        if(isBold){
            mTvSubTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mTvSubTitleBold.setTypeface(mTvSubTitleBold.getTypeface(), Typeface.BOLD);
        } else {
            mTvSubTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
            mTvSubTitleBold.setTypeface(null, Typeface.NORMAL);
        }
    }

    private void setBoldFeedbackMainTitle(boolean isBold)
    {
        if(isBold){
            mTvFeedbackMainTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mTvFeedbackMainTitleBold.setTypeface(mTvFeedbackMainTitleBold.getTypeface(), Typeface.BOLD);
        } else {
            mTvFeedbackMainTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
            mTvFeedbackMainTitleBold.setTypeface(null, Typeface.NORMAL);
        }
    }

    private void setBoldFeedbackSubTitle(boolean isBold)
    {
        if(isBold){
            mTvFeedbackSubTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mTvFeedbackSubTitleBold.setTypeface(mTvFeedbackSubTitleBold.getTypeface(), Typeface.BOLD);
        } else {
            mTvFeedbackSubTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
            mTvFeedbackSubTitleBold.setTypeface(null, Typeface.NORMAL);
        }
    }

    private void setBoldSubmittedTitle(boolean isBold)
    {
        if(isBold){
            mTvSubmittedTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            mTvSubmittedTitleBold.setTypeface(mTvSubmittedTitleBold.getTypeface(), Typeface.BOLD);
        } else {
            mTvSubmittedTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
            mTvSubmittedTitleBold.setTypeface(null, Typeface.NORMAL);
        }
    }

    private boolean isValidData()
    {
//        String deviceDescription = mEdtDeviceDescription.getText().toString();
//        if(deviceDescription.equalsIgnoreCase("")){
//            String fieldName = "Device Description";
//            String message = mContext.getString(R.string.alert_message).replace("%m", fieldName);
//            CommonUtils.showDialog(mContext, mContext.getString(R.string.title_alert_dialog),message);
//            mEdtDeviceDescription.requestFocus();
//            return false;
//        }
//
//        String mainTitle = mEdtMainTitle.getText().toString();
//        if(mainTitle.equalsIgnoreCase("")){
//            String fieldName = "Main Title";
//            String message = mContext.getString(R.string.alert_message).replace("%m", fieldName);
//            CommonUtils.showDialog(mContext, mContext.getString(R.string.title_alert_dialog), message);
//            mEdtMainTitle.requestFocus();
//            return false;
//        }
//
//        String subTitle = mEdtSubTitle.getText().toString();
//        if(subTitle.equalsIgnoreCase("")){
//            String fieldName = "Sub Title";
//            String message = mContext.getString(R.string.alert_message).replace("%m", fieldName);
//            CommonUtils.showDialog(mContext, mContext.getString(R.string.title_alert_dialog), message);
//            mEdtSubTitle.requestFocus();
//            return false;
//        }
//
//        if(bgPath == null || (bgPath != null && bgPath.equalsIgnoreCase(""))){
//            String message = mContext.getString(R.string.alert_message_select_background);
//            CommonUtils.showDialog(mContext, mContext.getString(R.string.title_alert_dialog), message);
//            return false;
//        }
//        if(logoPath == null || (logoPath != null && logoPath.equalsIgnoreCase(""))){
//            String message = mContext.getString(R.string.alert_message_select_logo);
//            CommonUtils.showDialog(mContext, mContext.getString(R.string.title_alert_dialog), message);
//            return false;
//        }

//        String email = mEdtInputEmail.getText().toString();
//        if(email.equalsIgnoreCase("")){
//            String fieldName = "Email(s)";
//            String message = mContext.getString(R.string.alert_message).replace("%m", fieldName);
//            CommonUtils.showDialog(mContext, mContext.getString(R.string.title_alert_dialog), message);
//            mEdtInputEmail.requestFocus();
//            return false;
//        }

        String[] emails = mEdtInputEmail.getText().toString().split(";");
        for(String e : emails){
            if(!CommonUtils.isEmailValid(e)){
                String fieldName = "valid format email";
                String message = mContext.getString(R.string.alert_message).replace("%m", fieldName);
                CommonUtils.showDialog(mContext, mContext.getString(R.string.title_alert_dialog), message);
                mEdtInputEmail.requestFocus();
                return false;
            }
        }

        if(mChkDaily.isChecked()){
            String time = mTvDailyTime.getText().toString();
            if(time.equalsIgnoreCase("")){
                String fieldName = "Daily Time";
                String message = mContext.getString(R.string.alert_message).replace("%m", fieldName);
                CommonUtils.showDialog(mContext,mContext.getString(R.string.title_alert_dialog),message);
                mTvDailyTime.requestFocus();
                return false;
            }
        } else {
            String time = mTvWeeklyTime.getText().toString();
            if(time.equalsIgnoreCase("")){
                String fieldName = "Weekly Time";
                String message = mContext.getString(R.string.alert_message).replace("%m", fieldName);
                CommonUtils.showDialog(mContext,mContext.getString(R.string.title_alert_dialog), message);
                mTvWeeklyTime.requestFocus();
                return false;
            }
        }

//        String feedbackMainTitle = mEdtFeedbackMainTitle.getText().toString();
//        if(feedbackMainTitle.equalsIgnoreCase("")){
//            String fieldName = "Feedback Main Title";
//            String message = mContext.getString(R.string.alert_message).replace("%m", fieldName);
//            CommonUtils.showDialog(mContext, mContext.getString(R.string.title_alert_dialog),message);
//            mEdtFeedbackMainTitle.requestFocus();
//            return false;
//        }
//        String feedbackSubTitle = mEdtFeedbackSubTitle.getText().toString();
//        if(feedbackSubTitle.equalsIgnoreCase("")){
//            String fieldName = "Feedback Sub Title";
//            String message = mContext.getString(R.string.alert_message).replace("%m", fieldName);
//            CommonUtils.showDialog(mContext, mContext.getString(R.string.title_alert_dialog), message);
//            mEdtFeedbackSubTitle.requestFocus();
//            return false;
//        }
//        String submitMainTitle = mEdtSubmitMainTitle.getText().toString();
//        if(submitMainTitle.equalsIgnoreCase("")){
//            String fieldName = "Submitted Screen Title";
//            String message = mContext.getString(R.string.alert_message).replace("%m", fieldName);
//            CommonUtils.showDialog(mContext, mContext.getString(R.string.title_alert_dialog),message);
//            mEdtSubmitMainTitle.requestFocus();
//            return false;
//        }
        return true;
    }

    private void initSetting()
    {
        SettingModel setting = CommonUtils.getSetting(mContext);
        if(setting != null){
            mTvDailyTime.setText(setting.getDailyTime());
            mEdtDeviceDescription.setText(setting.getDeviceDescription());
            mEdtFeedbackMainTitle.setText(setting.getFeedbackMainTitle());
            mEdtFeedbackSubTitle.setText(setting.getFeedbackSubTitle());
            mEdtInputEmail.setText(TextUtils.join(";", setting.getLstEmails()));
            mEdtMainTitle.setText(setting.getMainTitle());
            mEdtSubTitle.setText(setting.getSubTitle());
            mEdtSubmitMainTitle.setText(setting.getSubmitMainTitle());
            mTvWeeklyTime.setText(setting.getWeeklyTime());
            if(setting.isDailySending()){
                isDailyPreviousSending = true;
                mChkDaily.setChecked(true);
                mChkWeekly.setChecked(false);
            } else {
                isDailyPreviousSending = false;
                mChkDaily.setChecked(false);
                mChkWeekly.setChecked(true);
            }
            dailyHour = setting.getDailyHours();
            dailyMinute = setting.getDailyMinute();
            weeklyHour = setting.getWeeklyHours();
            weeklyMinute = setting.getWeeklyMinutes();
            dayOfWeeks = setting.getDayOfWeek();
            bgPath = setting.getBackgroundPath();
            logoPath = setting.getLogoPath();
            if(bgPath != null) {
                mTvBgPath.setText(mContext.getString(R.string.upload_file_successfuly).replace("%f", bgPath));
                mTvBgMoreResolution.setVisibility(View.GONE);
            } else {
                String widthBg = String.valueOf(CommonUtils.getWidthOfScreen((Activity) mContext));
                String heightBg = String.valueOf(CommonUtils.getHeightOfScreen((Activity) mContext));
                mTvBgMoreResolution.setText(mContext.getString(R.string.file_resolution).replace("%s", widthBg + "x" + heightBg));
                mTvBgMoreResolution.setVisibility(View.VISIBLE);
            }
            if(logoPath != null) {
                mTvLogoPath.setText(mContext.getString(R.string.upload_file_successfuly).replace("%f", logoPath));
                mTvLogoMoreResolution.setVisibility(View.GONE);
            } else {
                String widthLogo = String.valueOf(CommonUtils.getWidthOfScreen((Activity) mContext));
                String heightLogo = "180";
                mTvLogoMoreResolution.setText(mContext.getString(R.string.file_resolution).replace("%s", widthLogo + "x" + heightLogo));
                mTvLogoMoreResolution.setVisibility(View.VISIBLE);
            }
        } else {
            String widthBg = String.valueOf(CommonUtils.getWidthOfScreen((Activity) mContext));
            String heightBg = String.valueOf(CommonUtils.getHeightOfScreen((Activity) mContext));
            mTvBgMoreResolution.setText(mContext.getString(R.string.file_resolution).replace("%s", widthBg + "x" + heightBg));

            String widthLogo = String.valueOf(CommonUtils.getWidthOfScreen((Activity) mContext));
            String heightLogo = "180";
            mTvLogoMoreResolution.setText(mContext.getString(R.string.file_resolution).replace("%s", widthLogo + "x" + heightLogo));
        }
        mDeviceDescriptionFontStyle = CommonUtils.getDeviceDesFontStyle(mContext);
        mMainTitleFontStyle = CommonUtils.getMainTitleFontStyle(mContext);
        mSubTitleFontStyle = CommonUtils.getSubTitleFontStyle(mContext);
        mFeedbackMainTitleFontStyle = CommonUtils.getFeedbackMainTitleFontStyle(mContext);
        mFeedbackSubTitleFontStyle = CommonUtils.getFeedbackSubTitleFontStyle(mContext);
        mSubmittedTitleFontStyle = CommonUtils.getSubmittedTitleFontStyle(mContext);

        mEdtDeviceDesFontSize.setText(String.valueOf(mDeviceDescriptionFontStyle.getFontSize()));
        setBoldDeviceDescription(mDeviceDescriptionFontStyle.isBold());
        mEdtMainTitleFontSize.setText(String.valueOf(mMainTitleFontStyle.getFontSize()));
        setBoldMainTitle(mMainTitleFontStyle.isBold());
        mEdtSubTitleFontSize.setText(String.valueOf(mSubTitleFontStyle.getFontSize()));
        setBoldSubTitle(mSubTitleFontStyle.isBold());
        mEdtFeedbackMainTitleFontSize.setText(String.valueOf(mFeedbackMainTitleFontStyle.getFontSize()));
        setBoldFeedbackMainTitle(mFeedbackMainTitleFontStyle.isBold());
        mEdtFeedbackSubTitleFontSize.setText(String.valueOf(mFeedbackSubTitleFontStyle.getFontSize()));
        setBoldFeedbackSubTitle(mFeedbackSubTitleFontStyle.isBold());
        mEdtSubmittedTitleFontSize.setText(String.valueOf(mSubmittedTitleFontStyle.getFontSize()));
        setBoldSubmittedTitle(mSubmittedTitleFontStyle.isBold());

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

            String strMinute;
            if(minute < 10) {
                strMinute = "0" + String.valueOf(minute);
            } else {
                strMinute = String.valueOf(minute);
            }

            String strHour;
            if(hour < 10) {
                strHour = "0" + String.valueOf(hour);
            } else {
                strHour = String.valueOf(hour);
            }
            weeklyHour = hour;
            weeklyMinute = minute;
            dayOfWeeks = day;

            mTvWeeklyTime.setText(dayOfWeek + ";" + strHour + ":" + strMinute);
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
            CommonUtils.writeLog("Save Setting : Daily : " + dailyHour + ":" + dailyMinute);
        } else {
            setting.setDailySending(false);
            CommonUtils.writeLog("Save Setting : Weekly : " + dayOfWeeks + ", " + weeklyHour + ":" + weeklyMinute);
        }
        setting.setDailyPreviousSending(isDailyPreviousSending);
        setting.setDailyHours(dailyHour);
        setting.setDailyMinute(dailyMinute);
        setting.setWeeklyHours(weeklyHour);
        setting.setWeeklyMinutes(weeklyMinute);
        setting.setDayOfWeek(dayOfWeeks);
        setting.setDailyTime(mTvDailyTime.getText().toString());
        setting.setWeeklyTime(mTvWeeklyTime.getText().toString());
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
        CommonUtils.setIsSetting(mContext, true);

        mDeviceDescriptionFontStyle.setFontSize(Integer.parseInt(mEdtDeviceDesFontSize.getText().toString()));
        CommonUtils.saveDeviceDesFontStyle(mContext, mDeviceDescriptionFontStyle);
        mMainTitleFontStyle.setFontSize(Integer.parseInt(mEdtMainTitleFontSize.getText().toString()));
        CommonUtils.saveMainTitleFontStyle(mContext, mMainTitleFontStyle);
        mSubTitleFontStyle.setFontSize(Integer.parseInt(mEdtSubTitleFontSize.getText().toString()));
        CommonUtils.saveSubTitleFontStyle(mContext, mSubTitleFontStyle);
        mFeedbackMainTitleFontStyle.setFontSize(Integer.parseInt(mEdtFeedbackMainTitleFontSize.getText().toString()));
        CommonUtils.saveFeedbackMainTitleFontStyle(mContext, mFeedbackMainTitleFontStyle);
        mFeedbackSubTitleFontStyle.setFontSize(Integer.parseInt(mEdtFeedbackSubTitleFontSize.getText().toString()));
        CommonUtils.saveFeedbackSubTitleFontStyle(mContext, mFeedbackSubTitleFontStyle);
        mSubmittedTitleFontStyle.setFontSize(Integer.parseInt(mEdtSubmittedTitleFontSize.getText().toString()));
        CommonUtils.saveSubmittedTitleFontStyle(mContext, mSubmittedTitleFontStyle);
    }

    private void galleryIntent(int selectType)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity)mContext).startActivityForResult(Intent.createChooser(intent, mContext.getString(R.string.select_file)), selectType);
    }

    public void setBgPath(String path){
        bgPath = path;
        mTvBgPath.setText(mContext.getString(R.string.upload_file_successfuly).replace("%f", CommonUtils.getFileName(path)));
    }

    public void setLogoPath(String path){
        logoPath = path;
        mTvLogoPath.setText(mContext.getString(R.string.upload_file_successfuly).replace("%f", CommonUtils.getFileName(path)));
    }

//    @Override
//    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
//
//    }
}
