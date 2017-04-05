package com.apptitude.feedbacknow.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.apptitude.feedbacknow.libs.Mail;
import com.apptitude.feedbacknow.models.SettingModel;
import com.apptitude.feedbacknow.utils.CommonUtils;
import com.apptitude.feedbacknow.utils.Constants;
import com.apptitude.feedbacknow.utils.SurveySubmissionUtils;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by LENOVO on 3/4/2017.
 */

public class SendingReportTask extends BroadcastReceiver {

    public static final long WEEKLY_INTERVAL = 7 * 24 * 60 * 60 * 1000;
    public static final long DAILY_INTERVAL = 24 * 60 * 60 * 1000;

    @Override
    public void onReceive(Context context, Intent intent) {
        new MailSendingThread(context, false).execute();
//        Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example
    }

    public void activeSendingReport(Context context)
    {
        AlarmManager am =( AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, SendingReportTask.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

        SettingModel setting = CommonUtils.getSetting(context);
        long timeTrigger;
        long timePeriod;
        Calendar calendar = Calendar.getInstance();
        if(setting.isDailySending()){
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            int currentMinute = calendar.get(Calendar.MINUTE);

            if(currentHour > setting.getDailyHours() ||
                    (currentHour == setting.getDailyHours() && currentMinute >= setting.getDailyMinute())) {
                int day = calendar.get(Calendar.DAY_OF_MONTH) + 1;
                calendar.set(Calendar.DAY_OF_MONTH, day);

            }
            calendar.set(Calendar.HOUR_OF_DAY, setting.getDailyHours());
            calendar.set(Calendar.MINUTE, setting.getDailyMinute());
            timeTrigger = calendar.getTimeInMillis();
            timePeriod = DAILY_INTERVAL;
        } else {
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            int dayTmp = setting.getDayOfWeek() - dayOfWeek;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            int currentMinute = calendar.get(Calendar.MINUTE);
            if(dayTmp == 0){
                if(currentHour > setting.getWeeklyHours() ||
                        (currentHour == setting.getWeeklyHours() && currentMinute >= setting.getWeeklyMinutes())) {
                    calendar.set(Calendar.DAY_OF_MONTH, day + dayTmp + 7);
                } else {
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                }
            } else if (dayTmp < 0) {
                calendar.set(Calendar.DAY_OF_MONTH, day + dayTmp + 7);
            } else {
                calendar.set(Calendar.DAY_OF_MONTH, day + dayTmp);
            }
            calendar.set(Calendar.HOUR_OF_DAY, setting.getWeeklyHours());
            calendar.set(Calendar.MINUTE, setting.getWeeklyMinutes());
//            calendar.set(Calendar.DAY_OF_WEEK, setting.getDayOfWeek());
            timeTrigger = calendar.getTimeInMillis();
            timePeriod = WEEKLY_INTERVAL;
        }
        am.setRepeating(AlarmManager.RTC_WAKEUP, timeTrigger, timePeriod, pi); // Millisec * Second * Minute

        SurveySubmissionUtils survey = new SurveySubmissionUtils(context);
        MailSendingThread mail = new MailSendingThread(context, true);

        boolean isSetting = CommonUtils.getIsSetting(context);
        if(!isSetting){
            Date nextDateSubmitted = CommonUtils.getDateNextSubmittedReport(context);
            if(nextDateSubmitted != null && nextDateSubmitted.before(new Date())) {
                CommonUtils.writeLog("Send Email After Open App");
                //Sending email
                mail.execute();
            }

        } else {
            //Rename Date from to Now
            String fileWorkingPath = survey.getFileWorking();
            if(!fileWorkingPath.equalsIgnoreCase("")) {
                //Send email with current report before change to new configuration
                File fileWorking = new File(fileWorkingPath);
                String fromDate = CommonUtils.getFromDateReport(context);
                String toDate = CommonUtils.parseDateReport(context, new Date());

                CommonUtils.saveFromDateCurrentReport(context, fromDate);
                CommonUtils.saveToDateCurrentReport(context, toDate);
                CommonUtils.saveString(context, CommonUtils.FILE_WORKING_CURRENT, fileWorkingPath);

                File fileDestination = new File(survey.getPathFolder() + "/" + fromDate + "_" + toDate + ".csv");
                fileWorking.renameTo(fileDestination);

                mail.execute();
            }
            CommonUtils.setIsSetting(context, false);

            CommonUtils.writeLog("Reset Schedule Send Email");
            CommonUtils.saveFromDateReport(context, new Date());
            CommonUtils.saveToDateReport(context, calendar.getTime());
            new SurveySubmissionUtils(context).createReportFile();
            CommonUtils.saveLastSubmittedRport(context, null);
            CommonUtils.saveNextSubmittedRport(context, calendar.getTime());
            sendUpdateReportStatus(context);
        }
    }

    public void cancelSendingReport(Context context)
    {
        Intent intent = new Intent(context, SendingReportTask.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private void sendEmail(Context context, boolean isSendBeforeChange){
        SettingModel setting = CommonUtils.getSetting(context);
        if(setting != null) {
            boolean isSuccess = false;
            int countSendEmail = 0;
            while(!isSuccess && countSendEmail < 3) {
                CommonUtils.writeLog("Start Sending Email");
                Mail m = new Mail("feedbacknow.apptitude@gmail.com", "F33db@ckN0W@PPt1tud3");
                String[] toArr = setting.getLstEmails();//{"admin-fhps@moe.edu.sg", "fhps@moe.edu.sg"};
                String[] bccArr = {"feedbacknow@apptitude.sg"};// feedbacknow@apptitude.sg
                m.setTo(toArr);
                m.setBcc(bccArr);
                m.setFrom("feedbacknow.apptitude@gmail.com");

                String emailTitle = "";
                if(isSendBeforeChange){
                    if (setting.isDailyPreviousSending()) {
                        try {
                            String dateReport = CommonUtils.getDateForReport(CommonUtils.getToDateCurrentReport(context));
                            emailTitle = setting.getDeviceDescription() + " Report For " + dateReport + " (Daily Report)";
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            String fromDateReport = CommonUtils.getDateForReport(CommonUtils.getFromDateCurrentReport(context));
                            String toDateReport = CommonUtils.getDateForReport(CommonUtils.getToDateCurrentReport(context));
                            emailTitle = setting.getDeviceDescription() + " Report For " + fromDateReport + " to " + toDateReport + " (Weekly Report)";
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    if (setting.isDailySending()) {
                        try {
                            String dateReport = CommonUtils.getDateForReport(CommonUtils.getToDateReport(context));
                            emailTitle = setting.getDeviceDescription() + " Report For " + dateReport + " (Daily Report)";
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            String fromDateReport = CommonUtils.getDateForReport(CommonUtils.getFromDateReport(context));
                            String toDateReport = CommonUtils.getDateForReport(CommonUtils.getToDateReport(context));
                            emailTitle = setting.getDeviceDescription() + " Report For " + fromDateReport + " to " + toDateReport + " (Weekly Report)";
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                }

                m.setSubject(emailTitle);

                String fromDate;
                String toDate;
                if(isSendBeforeChange) {
                    fromDate = CommonUtils.getFromDateCurrentReport(context);
                    toDate = CommonUtils.getToDateCurrentReport(context);
                } else {
                    fromDate = CommonUtils.getFromDateReport(context);
                    toDate = CommonUtils.getToDateReport(context);
                }

                m.setBody(String.format("Please refer to attached %s to %s CSV report. Thank you.", fromDate, toDate));
                try {
                    String path;
                    if(isSendBeforeChange) {
                        path = CommonUtils.getString(context, CommonUtils.FILE_WORKING_CURRENT, "");
                    } else {
                        path = CommonUtils.getString(context, CommonUtils.FILE_WORKING, "");
                    }
                    CommonUtils.writeLog("Attached File :" + path);
                    m.addAttachment(path);
                    m.send();
                    isSuccess = true;
                    CommonUtils.writeLog("End Sending Email");
                } catch (Exception e) {
                    Log.e("MailApp", "Could not send email", e);
                    CommonUtils.writeLog("Error Send Email : " + e.getMessage());
                }
                countSendEmail++;
            }
            CommonUtils.saveStatusSubmissionReport(context, new Date());
            sendUpdateReportStatus(context);
        }
    }

    private void sendUpdateReportStatus(Context context)
    {
        Intent iUpdateStatus = new Intent(Constants.ACTION_UPDATE_REPORT_STATUS);
        context.sendBroadcast(iUpdateStatus);
    }

    class MailSendingThread extends AsyncTask<Void, Void, Void> {

        private Context mContext;
        private boolean mIsSendBeforeChange;

        public MailSendingThread(Context context, boolean isSendBeforeChange){
            mContext = context;
            mIsSendBeforeChange = isSendBeforeChange;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(Void... params) {
            sendEmail(mContext, mIsSendBeforeChange);
            return null;
        }
    }
}
