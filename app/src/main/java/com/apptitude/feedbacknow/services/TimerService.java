package com.apptitude.feedbacknow.services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.apptitude.feedbacknow.libs.Mail;
import com.apptitude.feedbacknow.utils.CommonUtils;

import java.util.Timer;
import java.util.TimerTask;

public class TimerService extends Service {

	// constant
    public static final long NOTIFY_INTERVAL = 7 * 24 * 60 * 60 * 1000;
 
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();
    // timer handling
    private Timer mTimer = null;
 
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
 
    @Override
    public void onCreate() {
        // cancel if already existed
        if(mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        sendEmail();
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
        
    }
 
    class TimeDisplayTimerTask extends TimerTask {
 
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
 
                @Override
                public void run() {
                   new ExcuteHttpPostThread().execute();
                }
 
            });
        }
    }
    
    private void sendEmail(){
//    	Mail m = new Mail("survey.app.fuhuapri@gmail.com", "12345678x@X");
        Mail m = new Mail("feedbacknow.apptitude@gmail.com", "F33db@ckN0W@PPt1tud3");
        String[] toArr = {"huylx86@gmail.com"};//{"admin-fhps@moe.edu.sg", "fhps@moe.edu.sg"};
        m.setTo(toArr); 
        m.setFrom("feedbacknow.apptitude@gmail.com");
        m.setSubject("Survey Report"); 
        
        String fromDate = CommonUtils.getString(this, CommonUtils.PREF_FROM_DATE, "");
        String toDate = CommonUtils.getString(this, CommonUtils.PREF_TO_DATE, "");
        
        m.setBody(String.format("Please refer to attached %s to %s CSV report. Thank you.", fromDate, toDate)); 
        try { 
        	  String path = CommonUtils.getString(this, CommonUtils.FILE_WORKING, "");
	          m.addAttachment(path); 
	          m.send();
        } catch(Exception e) { 
          Log.e("MailApp", "Could not send email", e); 
        } 
    }
    
    class ExcuteHttpPostThread extends AsyncTask<Void, Void, Void> {
				
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
			sendEmail();
			return null;
		}
	}
}
