package com.apptitude.surveyapp;

import android.content.Context;
import android.content.SharedPreferences;

public class CommonUtils {
	
	private static final String PREF_NAME = "SurveyApp";
	public static final String FILE_WORKING = "File_Working";
	public static final String PREF_FROM_DATE = "Pref_From_Date";
	public static final String PREF_TO_DATE = "Pref_To_Date";
	
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
}
