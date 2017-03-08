package com.apptitude.feedbacknow.utils;

import android.content.Context;
import android.os.Environment;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LENOVO on 3/5/2017.
 */

public class SurveySubmissionUtils {

    private static final String DATE_FORMAT_STORE = "dd/MM/yyyy HH:mm";

    private String pathFolder = "", pathFile = "";
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
