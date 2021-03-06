package com.apptitude.feedbacknow.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apptitude.feedbacknow.R;
import com.apptitude.feedbacknow.models.FontStyleModel;
import com.apptitude.feedbacknow.models.SettingModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class CommonUtils {
	
	private static final String PREF_NAME = "SurveyApp";
	public static final String FILE_WORKING = "File_Working";
    public static final String FILE_WORKING_CURRENT = "File_Working_Current";
	public static final String PREF_FROM_DATE = "Pref_From_Date";
    public static final String PREF_FROM_DATE_CURRENT = "Pref_From_Date_Current";
	public static final String PREF_TO_DATE = "Pref_To_Date";
    public static final String PREF_TO_DATE_CURRENT = "Pref_To_Date_Current";
	public static final String PREF_SETTING = "Pref_Setting";
	public static final String LAST_SUBMIT_REPORT = "Last_Submit_Report";
	public static final String NEXT_SUBMIT_REPORT = "Next_Submit_Report";
	private static final String DATE_FORMAT_FILE = "ddMMyyyy";
	private static final String DATE_FORMAT_REPORT = "dd/MM/yyyy";
	private static final String DATE_FORMAT_SUBMITTED = "dd/MM/yyyy HH:mm";
	private static final String CONFIRM_CONFIGURATION_APP = "Configuration_App";
    private static final String IS_SETTING = "Is_Setting";
	private static final String DEVICE_DES_FONT_STYLE = "Device_Descript_Font_Style";
	private static final String MAIN_TITLE_FONT_STYLE = "Main_Title_Font_Style";
	private static final String SUB_TITLE_FONT_STYLE = "Sub_Title_Font_Style";
	private static final String FEEDBACK_MAIN_TITLE_FONT_STYLE = "Feedback_Main_Title_Font_Style";
	private static final String FEEDBACK_SUB_TITLE_FONT_STYLE = "Feedback_Sub_Title_Font_Style";
	private static final String SUBMITTED_TITLE_FONT_STYLE = "Submitted_Title_Font_Style";

	public static String getFileName(String path)
	{
		return path.substring(path.lastIndexOf("/")+1);
	}

	public static String getString(Context context, String key, String defValue)
	{
		if(context == null) return defValue;
		SharedPreferences settings = context.getSharedPreferences(PREF_NAME, /* MODE_PRIVATE */0);
	    return settings.getString(key, defValue);
	}
	
	public static void saveString(Context context, String key, String value) {
        if(context == null) return;

        SharedPreferences pref = context.getSharedPreferences(
        		PREF_NAME, /* MODE_PRIVATE */0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

	public static void confirmConfigApp(Context context) {
		SharedPreferences pref = context.getSharedPreferences(
				PREF_NAME, /* MODE_PRIVATE */0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(CONFIRM_CONFIGURATION_APP, true);
		editor.commit();
	}

	public static boolean isConfirmConfigApp(Context context)
	{
		SharedPreferences pref = context.getSharedPreferences(
				PREF_NAME, /* MODE_PRIVATE */0);

		return pref.getBoolean(CONFIRM_CONFIGURATION_APP, false);
	}

	public static void saveFromDateReport(Context context, Date date)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_FILE);
		String strDate = sdf.format(date);
		saveString(context, PREF_FROM_DATE, strDate);
	}

	public static String getFromDateReport(Context context)
	{
		return getString(context, PREF_FROM_DATE, "");
	}

    public static void saveToDateReport(Context context, Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_FILE);
        String strDate = sdf.format(date);
        saveString(context, PREF_TO_DATE, strDate);
    }

    public static void saveFromDateCurrentReport(Context context, String date)
    {
        saveString(context, PREF_FROM_DATE_CURRENT, date);
    }

    public static String getFromDateCurrentReport(Context context)
    {
        return getString(context, PREF_FROM_DATE_CURRENT, "");
    }

    public static void saveToDateCurrentReport(Context context, String date)
    {
        saveString(context, PREF_TO_DATE_CURRENT, date);
    }

    public static String getToDateCurrentReport(Context context)
    {
        return getString(context, PREF_TO_DATE_CURRENT, "");
    }

    public static String parseDateReport(Context context, Date date)
    {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_FILE);
        return sdf.format(date);
    }

	public static String getToDateReport(Context context)
	{
		return getString(context, PREF_TO_DATE, "");
	}

	public static String getDateForReport(String dateSubmitted) throws ParseException {
		SimpleDateFormat spfSubmit = new SimpleDateFormat(DATE_FORMAT_FILE);
		Date date = spfSubmit.parse(dateSubmitted);
		SimpleDateFormat spf = new SimpleDateFormat(DATE_FORMAT_REPORT);
		return spf.format(date);
	}

    public static void saveStatusSubmissionReport(Context context, Date date)
	{
		SettingModel setting = getSetting(context);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if(setting.isDailySending()) {
			calendar.set(Calendar.HOUR_OF_DAY, setting.getDailyHours());
			calendar.set(Calendar.MINUTE, setting.getDailyMinute());
		} else {
			calendar.set(Calendar.HOUR_OF_DAY, setting.getWeeklyHours());
			calendar.set(Calendar.MINUTE, setting.getWeeklyMinutes());
		}
		saveLastSubmittedRport(context, calendar.getTime());
		saveFromDateReport(context, calendar.getTime());

		int day = calendar.get(Calendar.DAY_OF_MONTH);
		if(setting.isDailySending()){
			calendar.set(Calendar.DAY_OF_MONTH, day + 1);
		} else {
			calendar.set(Calendar.DAY_OF_MONTH, day + 7);
		}
		saveNextSubmittedRport(context, calendar.getTime());
		saveToDateReport(context, calendar.getTime());
		//Create report file for next submission
		new SurveySubmissionUtils(context).createReportFile();
	}

	public static void saveLastSubmittedRport(Context context, Date submittedDate) {
		if(context == null) return;
		String date = "";
		if(submittedDate != null) {
			SimpleDateFormat spf = new SimpleDateFormat(DATE_FORMAT_SUBMITTED);
			date = spf.format(submittedDate);
		}
		SharedPreferences pref = context.getSharedPreferences(
				PREF_NAME, /* MODE_PRIVATE */0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(LAST_SUBMIT_REPORT, date);
		editor.commit();
	}

    public static void setIsSetting(Context context, boolean isSetting) {
        if(context == null) return;
        SharedPreferences pref = context.getSharedPreferences(
                PREF_NAME, /* MODE_PRIVATE */0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(IS_SETTING, isSetting);
        editor.commit();
    }

    public static boolean getIsSetting(Context context) {
        SharedPreferences pref = context.getSharedPreferences(
                PREF_NAME, /* MODE_PRIVATE */0);
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_SETTING, false);
    }

	public static String getLastSubmittedRport(Context context) {
		if(context == null) return "";
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getString(LAST_SUBMIT_REPORT, "");
	}

	public static String getNextSubmittedRport(Context context) {
		if(context == null) return "";
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getString(NEXT_SUBMIT_REPORT, "");
	}

	public static Date getDateNextSubmittedReport(Context context) {
        String nextSubmittedDate = getNextSubmittedRport(context);
        SimpleDateFormat spf= new SimpleDateFormat(DATE_FORMAT_SUBMITTED);
        try {
            return spf.parse(nextSubmittedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

	public static void saveNextSubmittedRport(Context context, Date submittedDate) {
		if(context == null) return;
		SimpleDateFormat spf= new SimpleDateFormat(DATE_FORMAT_SUBMITTED);
		String date = spf.format(submittedDate);
		SharedPreferences pref = context.getSharedPreferences(
				PREF_NAME, /* MODE_PRIVATE */0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(NEXT_SUBMIT_REPORT, date);
		editor.commit();
	}

	public static void saveSetting(Context context, SettingModel setting )
	{

		Gson gson = new Gson();
		String result = gson.toJson(setting);

		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(PREF_SETTING, result);
		editor.commit();
	}

	public static SettingModel getSetting(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		String strSetting = sharedPreferences.getString(PREF_SETTING, "");

		Type type = new TypeToken<SettingModel>() {}.getType();
		Gson gson = new Gson();
		SettingModel setting = gson.fromJson(strSetting, type);
		return setting;
	}

	public static String getDynamicPassword()
	{
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("ddMMyyyyHHmm", Locale.getDefault());
		return format.format(now);
	}

	public static void showDialog(final Context context, String title, String message)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT)
				.setTitle(title)
				.setMessage(message)
				.setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

					}
				})
				.show();
		TextView messageView = (TextView)alertDialog.findViewById(android.R.id.message);
		messageView.setGravity(Gravity.CENTER);

	}

	public static void showConfirmDialog(final Context context, String title, String message, DialogInterface.OnClickListener onClick)
	{
		AlertDialog alertDialog = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT)
				.setTitle(title)
				.setMessage(message)
				.setNegativeButton(context.getString(R.string.confirm), onClick)
				.show();

		TextView messageView = (TextView)alertDialog.findViewById(android.R.id.message);
		messageView.setGravity(Gravity.CENTER);

	}

	public static void setImageGifFromPath(GifImageView imageView, String path) throws IOException {
		File imgFile = new File(path);
		if(imgFile.exists()){
			GifDrawable gifFromPath = new GifDrawable(imgFile);
			imageView.setImageDrawable(gifFromPath);
		}
	}

	public static void setImageFromPath(ImageView imageView, String path)
	{
		File imgFile = new File(path);
		if(imgFile.exists()){
			Drawable drawImage = Drawable.createFromPath(imgFile.getAbsolutePath());
			imageView.setImageDrawable(drawImage);
		}
	}

	public static void setImageFromPath(View view, String path)
	{
		File imgFile = new File(path);
		if(imgFile.exists()){
			Drawable drawImage = Drawable.createFromPath(imgFile.getAbsolutePath());
			view.setBackground(drawImage);
		}
	}

	public static boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	public static String getExtensionFileName(String fileName)
	{
		String filenameArray[] = fileName.split("\\.");
		String extension = filenameArray[filenameArray.length-1];
		return extension;
	}

	public static int getWidthOfScreen(Activity activity)
	{
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size.x;

	}

	public static int getHeightOfScreen(Activity activity)
	{
		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		return size.y;

	}

	public static int dpToPx(Context context, int dp) {
		DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
	}

	public static void writeLog(String action){
		File file = new File(Environment.getExternalStorageDirectory(), "feedbackNowLog.txt");
		if (!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		SimpleDateFormat spf = new SimpleDateFormat(DATE_FORMAT_SUBMITTED);
		String date = spf.format(new Date());
		String data = "[" + date + "] : " + action;
		FileOutputStream output = null;
		try {
			output = new FileOutputStream(file, true);
			OutputStreamWriter myOutWriter = new OutputStreamWriter(output);
			myOutWriter.append(data);
			myOutWriter.append("\n\r");
			myOutWriter.close();
			output.flush();
		} catch (Exception e) {
		}finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void saveDeviceDesFontStyle(Context context, FontStyleModel fontStyle)
	{
		Gson gson = new Gson();
		String result = gson.toJson(fontStyle);

		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(DEVICE_DES_FONT_STYLE, result);
		editor.commit();
	}

	public static FontStyleModel getDeviceDesFontStyle(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		String strFontStyle = sharedPreferences.getString(DEVICE_DES_FONT_STYLE, "");

		Type type = new TypeToken<FontStyleModel>() {}.getType();
		Gson gson = new Gson();
		FontStyleModel fontStyle = gson.fromJson(strFontStyle, type);
		if(fontStyle == null) {
			fontStyle = new FontStyleModel();
		}
		return fontStyle;
	}

	public static void saveMainTitleFontStyle(Context context, FontStyleModel fontStyle)
	{
		Gson gson = new Gson();
		String result = gson.toJson(fontStyle);

		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(MAIN_TITLE_FONT_STYLE, result);
		editor.commit();
	}

	public static FontStyleModel getMainTitleFontStyle(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		String strFontStyle = sharedPreferences.getString(MAIN_TITLE_FONT_STYLE, "");

		Type type = new TypeToken<FontStyleModel>() {}.getType();
		Gson gson = new Gson();
		FontStyleModel fontStyle = gson.fromJson(strFontStyle, type);
		if(fontStyle == null) {
			fontStyle = new FontStyleModel();
		}
		return fontStyle;
	}


	public static void saveSubTitleFontStyle(Context context, FontStyleModel fontStyle)
	{
		Gson gson = new Gson();
		String result = gson.toJson(fontStyle);

		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(SUB_TITLE_FONT_STYLE, result);
		editor.commit();
	}

	public static FontStyleModel getSubTitleFontStyle(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		String strFontStyle = sharedPreferences.getString(SUB_TITLE_FONT_STYLE, "");

		Type type = new TypeToken<FontStyleModel>() {}.getType();
		Gson gson = new Gson();
		FontStyleModel fontStyle = gson.fromJson(strFontStyle, type);
		if(fontStyle == null) {
			fontStyle = new FontStyleModel();
		}
		return fontStyle;
	}

	public static void saveFeedbackMainTitleFontStyle(Context context, FontStyleModel fontStyle)
	{
		Gson gson = new Gson();
		String result = gson.toJson(fontStyle);

		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(FEEDBACK_MAIN_TITLE_FONT_STYLE, result);
		editor.commit();
	}

	public static FontStyleModel getFeedbackMainTitleFontStyle(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		String strFontStyle = sharedPreferences.getString(FEEDBACK_MAIN_TITLE_FONT_STYLE, "");

		Type type = new TypeToken<FontStyleModel>() {}.getType();
		Gson gson = new Gson();
		FontStyleModel fontStyle = gson.fromJson(strFontStyle, type);
		if(fontStyle == null) {
			fontStyle = new FontStyleModel();
		}
		return fontStyle;
	}

	public static void saveFeedbackSubTitleFontStyle(Context context, FontStyleModel fontStyle)
	{
		Gson gson = new Gson();
		String result = gson.toJson(fontStyle);

		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(FEEDBACK_SUB_TITLE_FONT_STYLE, result);
		editor.commit();
	}

	public static FontStyleModel getFeedbackSubTitleFontStyle(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		String strFontStyle = sharedPreferences.getString(FEEDBACK_SUB_TITLE_FONT_STYLE, "");

		Type type = new TypeToken<FontStyleModel>() {}.getType();
		Gson gson = new Gson();
		FontStyleModel fontStyle = gson.fromJson(strFontStyle, type);
		if(fontStyle == null) {
			fontStyle = new FontStyleModel();
		}
		return fontStyle;
	}

	public static void saveSubmittedTitleFontStyle(Context context, FontStyleModel fontStyle)
	{
		Gson gson = new Gson();
		String result = gson.toJson(fontStyle);

		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(SUBMITTED_TITLE_FONT_STYLE, result);
		editor.commit();
	}

	public static FontStyleModel getSubmittedTitleFontStyle(Context context)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		String strFontStyle = sharedPreferences.getString(SUBMITTED_TITLE_FONT_STYLE, "");

		Type type = new TypeToken<FontStyleModel>() {}.getType();
		Gson gson = new Gson();
		FontStyleModel fontStyle = gson.fromJson(strFontStyle, type);
		if(fontStyle == null) {
			fontStyle = new FontStyleModel();
		}
		return fontStyle;
	}
}
