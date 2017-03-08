package com.apptitude.feedbacknow.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.apptitude.feedbacknow.R;
import com.apptitude.feedbacknow.models.SettingModel;
import com.apptitude.feedbacknow.utils.CommonUtils;
import com.apptitude.feedbacknow.utils.Constants;
import com.apptitude.feedbacknow.utils.SurveySubmissionUtils;

import java.io.IOException;

import pl.droidsonroids.gif.GifImageView;

public class RemarkActivity extends Activity {

	private String status = "";
	private Context context;
	private EditText edtRemark;
	private TextView mTvFeedbackMainTitle, mTvFeedbackSubTitle;
	private View mMainView;
	private ImageView mIvLogo;
	private CountDownTimer countDownTimer;
	private SurveySubmissionUtils surveySubmission;
	private GifImageView mIvLogGif, mIvBgGif;
	private TextView mTvLastSubmission, mTvNextSubmission;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.remark_activity);
		
		context = this;
		surveySubmission = new SurveySubmissionUtils(context);

		mMainView = findViewById(R.id.parent);
		setupUI(this, mMainView);
		
		status = getIntent().getStringExtra("Status");

		mTvFeedbackMainTitle = (TextView)findViewById(R.id.tv_feedback_main_title);
		mTvFeedbackSubTitle = (TextView)findViewById(R.id.tv_feedback_sub_title);
		mIvLogo = (ImageView)findViewById(R.id.iv_logo);
		mIvLogGif = (GifImageView)findViewById(R.id.iv_logo_gif);
		mIvBgGif = (GifImageView)findViewById(R.id.iv_bg_gif);
		mTvLastSubmission = (TextView)findViewById(R.id.tv_last_submission);
		mTvNextSubmission = (TextView)findViewById(R.id.tv_next_submission);

		final ImageView btnSubmit = (ImageView)findViewById(R.id.btn_submit);
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					countDownTimer.cancel();
					context.startActivity(new Intent(context, SubmittedActivity.class));
					finish();
					surveySubmission.submitSurvey(status, edtRemark.getText().toString());
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
		
//		createFolder();
//
//		String fileName = CommonUtils.getString(context, CommonUtils.FILE_WORKING, "");
//		if(!fileName.equalsIgnoreCase("")){
//			pathFile = fileName;
//		}
		
//		tvCountDown = (TextView)findViewById(R.id.count_down);
		countDownTimer = new CountDownTimer(10000, 1000) {

		     public void onTick(long millisUntilFinished) {
				 int i = (int)millisUntilFinished / 1000;
				 switch(i) {
					 case 1:
					 	btnSubmit.setImageResource(R.drawable.ic_submit_1);
						 break;
					 case 2:
						 btnSubmit.setImageResource(R.drawable.ic_submit_2);
						 break;
					 case 3:
						 btnSubmit.setImageResource(R.drawable.ic_submit_3);
						 break;
					 case 4:
						 btnSubmit.setImageResource(R.drawable.ic_submit_4);
						 break;
					 case 5:
						 btnSubmit.setImageResource(R.drawable.ic_submit_5);
						 break;
					 case 6:
						 btnSubmit.setImageResource(R.drawable.ic_submit_6);
						 break;
					 case 7:
						 btnSubmit.setImageResource(R.drawable.ic_submit_7);
						 break;
					 case 8:
						 btnSubmit.setImageResource(R.drawable.ic_submit_8);
						 break;
					 case 9:
						 btnSubmit.setImageResource(R.drawable.ic_submit_9);
						 break;
					 case 10:
						 btnSubmit.setImageResource(R.drawable.ic_submit_10);
						 break;
				 }
//		         tvCountDown.setText(String.valueOf());
		     }

		     public void onFinish() {
		    	 try {
					 context.startActivity(new Intent(context, SubmittedActivity.class));
					 finish();
					 surveySubmission.submitSurvey(status, edtRemark.getText().toString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		     }
		 }.start();

		initScreen();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		registerReceiver(mReceiverUpdateStatusReport, new IntentFilter(Constants.ACTION_UPDATE_REPORT_STATUS));
		super.onResume();
	}

	@Override
	protected void onPause() {
		unregisterReceiver(mReceiverUpdateStatusReport);
		super.onPause();
	}

	BroadcastReceiver mReceiverUpdateStatusReport = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String lastSubmission = CommonUtils.getLastSubmittedRport(context);
			String nextSubmission = CommonUtils.getNextSubmittedRport(context);
			mTvLastSubmission.setText(getString(R.string.last_report_submission) + " " + lastSubmission);
			mTvNextSubmission.setText(getString(R.string.next_report_submission) + " " + nextSubmission);
		}
	};

	private void initScreen()
	{
		SettingModel setting = CommonUtils.getSetting(this);
		if(setting != null){
			String feedbackMainTitle = setting.getFeedbackMainTitle();
			if(feedbackMainTitle != null){
				mTvFeedbackMainTitle.setText(feedbackMainTitle);
			}

			String feedbackSubTitle = setting.getFeedbackSubTitle();
			if(feedbackSubTitle != null){
				mTvFeedbackSubTitle.setText(feedbackSubTitle);
			}

			String bgPath = setting.getBackgroundPath();
			if(bgPath != null && !bgPath.equalsIgnoreCase("")){
				if(CommonUtils.getExtensionFileName(bgPath).equalsIgnoreCase("gif")) {
					try {
						CommonUtils.setImageGifFromPath(mIvBgGif, bgPath);
						mIvBgGif.setVisibility(View.VISIBLE);
					} catch (IOException e) {
						e.printStackTrace();
						mIvBgGif.setVisibility(View.GONE);
						CommonUtils.setImageFromPath(mMainView, bgPath);
					}
				} else {
					mIvBgGif.setVisibility(View.GONE);
					CommonUtils.setImageFromPath(mMainView, bgPath);
				}
			}

			String logoPath = setting.getLogoPath();
			if(logoPath != null && !logoPath.equalsIgnoreCase("")){
				if(CommonUtils.getExtensionFileName(logoPath).equalsIgnoreCase("gif")) {
					try {
						CommonUtils.setImageGifFromPath(mIvLogGif, logoPath);
						mIvLogGif.setVisibility(View.VISIBLE);
						mIvLogo.setVisibility(View.GONE);
					} catch (IOException e) {
						e.printStackTrace();
						mIvLogGif.setVisibility(View.GONE);
						mIvLogo.setVisibility(View.VISIBLE);
						CommonUtils.setImageFromPath(mIvLogo, logoPath);
					}
				} else {
					mIvLogGif.setVisibility(View.GONE);
					mIvLogo.setVisibility(View.VISIBLE);
					CommonUtils.setImageFromPath(mIvLogo, logoPath);
				}
			}
		}
		String lastSubmission = CommonUtils.getLastSubmittedRport(context);
		String nextSubmission = CommonUtils.getNextSubmittedRport(context);
		mTvLastSubmission.setText(getString(R.string.last_report_submission) + " " + lastSubmission);
		mTvNextSubmission.setText(getString(R.string.next_report_submission) + " " + nextSubmission);
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
