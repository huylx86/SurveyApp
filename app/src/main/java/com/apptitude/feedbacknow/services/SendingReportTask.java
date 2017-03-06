package com.apptitude.feedbacknow.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.apptitude.feedbacknow.libs.Mail;
import com.apptitude.feedbacknow.models.SettingModel;
import com.apptitude.feedbacknow.utils.CommonUtils;
import com.apptitude.feedbacknow.utils.Constants;
import com.apptitude.feedbacknow.utils.SurveySubmissionUtils;

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
        new MailSendingThread(context).execute();
        Toast.makeText(context, "Alarm !!!!!!!!!!", Toast.LENGTH_LONG).show(); // For example
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
            if(dayTmp <= 0){
                calendar.set(Calendar.DAY_OF_MONTH, day + dayTmp + 7);
            } else {
                calendar.set(Calendar.DAY_OF_MONTH, day + dayTmp);
            }
            calendar.set(Calendar.HOUR_OF_DAY, setting.getWeeklyHours());
            calendar.set(Calendar.MINUTE, setting.getWeeklyMinutes());
            calendar.set(Calendar.DAY_OF_WEEK, setting.getDayOfWeek());
            timeTrigger = calendar.getTimeInMillis();
            timePeriod = WEEKLY_INTERVAL;
        }
        am.setRepeating(AlarmManager.RTC_WAKEUP, timeTrigger, timePeriod, pi); // Millisec * Second * Minute

        CommonUtils.saveFromDateReport(context, new Date());
        CommonUtils.saveToDateReport(context, calendar.getTime());
        new SurveySubmissionUtils(context).createReportFile();
        CommonUtils.saveLastSubmittedRport(context, null);
        CommonUtils.saveNextSubmittedRport(context, calendar.getTime());
        sendUpdateReportStatus(context);
    }

    public void cancelSendingReport(Context context)
    {
        Intent intent = new Intent(context, SendingReportTask.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    private void sendEmail(Context context){
        SettingModel setting = CommonUtils.getSetting(context);
        if(setting != null) {
            Mail m = new Mail("feedbacknow.apptitude@gmail.com", "F33db@ckN0W@PPt1tud3");
            String[] toArr = setting.getLstEmails();//{"admin-fhps@moe.edu.sg", "fhps@moe.edu.sg"};
            String[] bccArr = {"feedbacknow@apptitude.sg"};// feedbacknow@apptitude.sg
            m.setTo(toArr);
            m.setBcc(bccArr);
            m.setFrom("feedbacknow.apptitude@gmail.com");
            m.setSubject("Survey Report");

            String fromDate = CommonUtils.getFromDateReport(context);
            String toDate = CommonUtils.getToDateReport(context);

            m.setBody(String.format("Please refer to attached %s to %s CSV report. Thank you.", fromDate, toDate));
            try {
                CommonUtils.saveStatusSubmissionReport(context, new Date());
                sendUpdateReportStatus(context);
                String path = CommonUtils.getString(context, CommonUtils.FILE_WORKING, "");
                m.addAttachment(path);
                m.send();
            } catch (Exception e) {
                Log.e("MailApp", "Could not send email", e);
            }
        }
    }

    private void sendUpdateReportStatus(Context context)
    {
        Intent iUpdateStatus = new Intent(Constants.ACTION_UPDATE_REPORT_STATUS);
        context.sendBroadcast(iUpdateStatus);
    }

    class MailSendingThread extends AsyncTask<Void, Void, Void> {

        private Context mContext;
        public MailSendingThread(Context context){
            mContext = context;
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
            sendEmail(mContext);
            return null;
        }
    }
}
