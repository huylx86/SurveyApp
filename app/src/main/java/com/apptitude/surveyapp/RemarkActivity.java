package com.apptitude.surveyapp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.opencsv.CSVWriter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class RemarkActivity extends Activity {
	
	private static final String DATE_FORMAT_FILE = "ddMMyyyy";
	private static final String DATE_FORMAT_STORE = "dd/MM/yyyy HH:mm";
	
	private String pathFolder = "", pathFile = "";
	private String date = "", status = "";
	private Context context;
	private EditText edtRemark;
	private TextView tvCountDown;
	private CountDownTimer countDownTimer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.remark_activity);
		
		context = this;
		
		setupUI(this, findViewById(R.id.parent));
		
		status = getIntent().getStringExtra("Status");
		
		ImageView btnSubmit = (ImageView)findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					countDownTimer.cancel();
					submitSurvey();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		edtRemark = (EditText)findViewById(R.id.remark);
		edtRemark.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				countDownTimer.cancel();
				countDownTimer.start();
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
		createFolder();
		
		String fileName = CommonUtils.getString(context, CommonUtils.FILE_WORKING, "");
		if(!fileName.equalsIgnoreCase("")){
			pathFile = fileName;
		}
		
		tvCountDown = (TextView)findViewById(R.id.count_down);
		countDownTimer = new CountDownTimer(10000, 1000) {

		     public void onTick(long millisUntilFinished) {
		         tvCountDown.setText(String.valueOf(millisUntilFinished / 1000));
		     }

		     public void onFinish() {
		    	 try {
					submitSurvey();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     }
		 }.start();

		super.onCreate(savedInstanceState);
	}

	private void createFolder()
	{
		File myDirectory = new File(Environment.getExternalStorageDirectory(), "FHPS_Survey");

		if(!myDirectory.exists()) {                                 
			myDirectory.mkdirs();			
		}
		
		pathFolder = myDirectory.getAbsolutePath();
	}
	
	private void createFile() throws ParseException, IOException
	{
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_FILE);
		if(!date.equalsIgnoreCase("")){	
			Date strDate = sdf.parse(date);
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(strDate);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			
			strDate.setDate(day + 7);
			
			String toDate = sdf.format(strDate);
			
			File file = new File(pathFolder + "/" + date + "_" + toDate + ".csv");
			if(!file.exists()){
				file.createNewFile();
				pathFile = file.getAbsolutePath();
				CommonUtils.saveString(context, CommonUtils.FILE_WORKING, pathFile);
				CommonUtils.saveString(context, CommonUtils.PREF_TO_DATE, toDate);
			}
		}
	}
	
	private void submitSurvey() throws IOException
	{
		RemarkActivity.this.finish();	
		try {
			String sDate = CommonUtils.getString(context, CommonUtils.PREF_FROM_DATE, "");
			
			if(isCreateNewFile(sDate)){
				createFile();
			}
			if(!pathFile.equalsIgnoreCase("")){
				CSVWriter writer = new CSVWriter(new FileWriter(pathFile, true));
		
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_STORE);
				String dateStore = sdf.format(new Date());
				String [] fruits= {status, dateStore, edtRemark.getText().toString()};
				writer.writeNext(fruits);
				
				writer.close();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
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
			
			strDate.setDate(day + 7);
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
    		CommonUtils.saveString(context, CommonUtils.PREF_FROM_DATE, date);
    	}
    	
    	return true;
    }
	
	private void hideSoftKeyboard(Activity activity, View view) {
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	public void setupUI(final Activity activity, View view) {
	    //Set up touch listener for non-text box views to hide keyboard.
	    if(!(view instanceof EditText) && !(view instanceof ListView)) {
	        view.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View view, MotionEvent arg1) {
					hideSoftKeyboard(activity, view);
					return false;
				}
	        });
	    }

	    //If a layout container, iterate over children and seed recursion.
	    if (view instanceof ViewGroup) {
	        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
	            View innerView = ((ViewGroup) view).getChildAt(i);
	            setupUI(activity, innerView);
	        }
	    }
	}
	
	@Override
    public void onBackPressed() {
		hideSoftKeyboard(this, edtRemark);
    }
}
