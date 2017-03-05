package com.apptitude.feedbacknow.utils;

import android.content.Context;
import android.os.Environment;

import com.apptitude.feedbacknow.models.SettingModel;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by LENOVO on 3/5/2017.
 */

public class SurveySubmissionUtils {

    private static final String DATE_FORMAT_FILE = "ddMMyyyy";
    private static final String DATE_FORMAT_STORE = "dd/MM/yyyy HH:mm";

    private String pathFolder = "", pathFile = "";
    private String date = "";
    private Context mContext;

    public SurveySubmissionUtils(Context context)
    {
        mContext = context;
        createFolder();
        String fileName = CommonUtils.getString(context, CommonUtils.FILE_WORKING, "");
		if(!fileName.equalsIgnoreCase("")){
			pathFile = fileName;
		}
    }

    private void createFolder()
    {
        File myDirectory = new File(Environment.getExternalStorageDirectory(), "FeedbackNow");

        if(!myDirectory.exists()) {
            myDirectory.mkdirs();
        }

        pathFolder = myDirectory.getAbsolutePath();
    }

    @SuppressWarnings("deprecation")
    public boolean isCreateNewFile(String sDate) throws ParseException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_FILE);
        Date strDate = new Date();

        if(!sDate.equalsIgnoreCase("")){

            strDate = sdf.parse(sDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(strDate);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            SettingModel setting = CommonUtils.getSetting(mContext);
            if(setting.isDailySending()) {
                strDate.setDate(day + 1);
            } else {
                strDate.setDate(day + 7);
            }
            strDate.setHours(0);
            strDate.setMinutes(1);
            strDate.setSeconds(2);

        } else {
            strDate.setHours(0);
            strDate.setMinutes(1);
            strDate.setSeconds(0);
        }


        Date currentDate = new Date();
        currentDate.setHours(0);
        currentDate.setMinutes(1);
        currentDate.setSeconds(1);

        if(strDate.after(currentDate)){
            return false;
        } else {
            if(!sDate.equalsIgnoreCase("")){
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(strDate);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                strDate.setDate(day + 1);
            }

            date = sdf.format(strDate);
            CommonUtils.saveString(mContext, CommonUtils.PREF_FROM_DATE, date);
        }

        return true;
    }

    private void createFile() throws ParseException, IOException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_FILE);
        if(!date.equalsIgnoreCase("")){
            Date strDate = sdf.parse(date);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(strDate);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            SettingModel setting = CommonUtils.getSetting(mContext);
            if(setting.isDailySending()) {
                strDate.setDate(day + 1);
            } else {
                strDate.setDate(day + 7);
            }

            String toDate = sdf.format(strDate);

            File file = new File(pathFolder + "/" + date + "_" + toDate + ".csv");
            if(!file.exists()){
                file.createNewFile();
                pathFile = file.getAbsolutePath();
                CommonUtils.saveString(mContext, CommonUtils.FILE_WORKING, pathFile);
                CommonUtils.saveString(mContext, CommonUtils.PREF_TO_DATE, toDate);
            }
        }
    }
    public void createReportFile() {
        String fromDate = CommonUtils.getFromDateReport(mContext);
        String toDate = CommonUtils.getToDateReport(mContext);
        if(!fromDate.equalsIgnoreCase("") && !toDate.equalsIgnoreCase("")) {
            File file = new File(pathFolder + "/" + fromDate + "_" + toDate + ".csv");
            if(!file.exists()) {
                try {
                    file.createNewFile();
                    pathFile = file.getAbsolutePath();
                    CommonUtils.saveString(mContext, CommonUtils.FILE_WORKING, pathFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                pathFile = file.getAbsolutePath();
                CommonUtils.saveString(mContext, CommonUtils.FILE_WORKING, pathFile);
            }
        }

    }
    public void submitSurvey(String status, String feedback) throws IOException
    {
        try {
//            String sDate = CommonUtils.getString(mContext, CommonUtils.PREF_FROM_DATE, "");

//            if(isCreateNewFile(sDate)){
//                createFile();
//            }
            createReportFile();
            if(!pathFile.equalsIgnoreCase("")){
                if(status != null && feedback != null) {
                    CSVWriter writer = new CSVWriter(new FileWriter(pathFile, true));

                    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STORE);
                    String dateStore = sdf.format(new Date());
                    String[] fruits = {status, dateStore, feedback};
                    writer.writeNext(fruits);

                    writer.close();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
