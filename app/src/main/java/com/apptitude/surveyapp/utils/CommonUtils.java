package com.apptitude.surveyapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;

import com.apptitude.surveyapp.models.SettingModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CommonUtils {
	
	private static final String PREF_NAME = "SurveyApp";
	public static final String FILE_WORKING = "File_Working";
	public static final String PREF_FROM_DATE = "Pref_From_Date";
	public static final String PREF_TO_DATE = "Pref_To_Date";
	public static final String PREF_SETTING = "Pref_Setting";

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
}
