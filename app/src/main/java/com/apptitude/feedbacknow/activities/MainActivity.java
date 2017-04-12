package com.apptitude.feedbacknow.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.apptitude.feedbacknow.R;
import com.apptitude.feedbacknow.models.FontStyleModel;
import com.apptitude.feedbacknow.models.SettingModel;
import com.apptitude.feedbacknow.services.SendingReportTask;
import com.apptitude.feedbacknow.utils.CommonUtils;
import com.apptitude.feedbacknow.utils.Constants;
import com.apptitude.feedbacknow.utils.ImageUtils;
import com.github.jjobes.slidedaytimepicker.SlideDayTimeListener;
import com.github.jjobes.slidedaytimepicker.SlideDayTimePicker;

import java.io.IOException;
import java.util.Calendar;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends FragmentActivity{

	private static final String STRONG_SATISFY = "Very Satisfied";
	private static final String SATISFY = "Satisfied";
	private static final String NEUTRAL = "Neutral";
	private static final String DISSATISFY = "Dissatisfied";
	private static final String STRONG_DISSATISFY = "Very Dissatisfied";
	private RadioButton rbStrongSatisfy, rbSatisfy, rbNeutral, rbDissatify, rbStrongDissatify;
	private ImageView ivStrongSatisfy, ivSatisfy, ivNeutral, ivDissatify, ivStrongDissatify;
	private ImageView ivEdit, ivExit;
	private View mMainView;
	private ImageView mIvLogo;
	private GifImageView mIvLogGif, mIvBgGif;
	private TextView mTvMainTitle, mTvSubTitle, mTvNetworkStatus;//, mTvLastSubmission, mTvNextSubmission;

	private static RadioButton rbActive;
	private Context mContext;
    private String mFilePath;
    private SettingDialog mSettingDialog;
    private PasswordDialog mPasswordDialog;
	private SendingReportTask mSendingReport;

	//Setting
	private View mViewSetting;
	private EditText mEdtDeviceDescription,mEdtMainTitle, mEdtSubTitle, mEdtInputEmail;
	private EditText mEdtFeedbackMainTitle, mEdtFeedbackSubTitle, mEdtSubmitMainTitle;
	private EditText mEdtDeviceDesFontSize, mEdtMainTitleFontSize, mEdtSubTitleFontSize, mEdtFeedbackMainTitleFontSize,
			mEdtFeedbackSubTitleFontSize, mEdtSubmittedTitleFontSize;
	private TextView mTvDeviceDesBold, mTvMainTitleBold, mTvSubTitleBold, mTvFeedbackMainTitleBold, mTvFeedbackSubTitleBold,
			mTvSubmittedTitleBold;
	private TextView mTvBgMoreResolution, mTvLogoMoreResolution;

	private Button mBtnSelectBg, mBtnSelectLogo, mBtnConfirm;
	private RadioButton mChkDaily, mChkWeekly;
	private TextView mTvBgPath, mTvLogoPath, mTvDailyTime, mTvWeeklyTime;
	private String bgPath;
	private String logoPath;

	private int dailyHour, dailyMinute, weeklyHour, weeklyMinute, dayOfWeeks;
	private boolean isDailyPreviousSending;

	private FontStyleModel mDeviceDescriptionFontStyle, mMainTitleFontStyle, mSubTitleFontStyle, mFeedbackMainTitleFontStyle,
			mFeedbackSubTitleFontStyle, mSubmittedTitleFontStyle;

	private boolean mIsSettingShow;
	private Animation mShowAnim, mHideAnim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
        mContext = this;
		mSendingReport = new SendingReportTask();

		mMainView = findViewById(R.id.ln_main_view);
		mIvLogo = (ImageView)findViewById(R.id.iv_logo);
		mIvLogGif = (GifImageView)findViewById(R.id.iv_logo_gif);
		mIvBgGif = (GifImageView)findViewById(R.id.iv_bg_gif);
		mTvMainTitle = (TextView)findViewById(R.id.tv_main_title);
		mTvSubTitle = (TextView)findViewById(R.id.tv_sub_title);
//		mViewConnectionStatus = findViewById(R.id.report_status);
		mTvNetworkStatus = (TextView)findViewById(R.id.tv_network_status);
//		mTvLastSubmission = (TextView)findViewById(R.id.tv_last_submission);
//		mTvNextSubmission = (TextView)findViewById(R.id.tv_next_submission);

		rbStrongSatisfy = (RadioButton)findViewById(R.id.rb_strong_satisfy);
		rbStrongSatisfy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				strongSatifySelected();
			}
		});		
		ivStrongSatisfy = (ImageView)findViewById(R.id.iv_strong_satisfy);
		ivStrongSatisfy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rbStrongSatisfy.setChecked(true);
				strongSatifySelected();
				
			}
		});
		
		rbSatisfy = (RadioButton)findViewById(R.id.rb_satisfy);
		rbSatisfy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				satifySelected();
			}
		});
		ivSatisfy = (ImageView)findViewById(R.id.iv_satisfy);
		ivSatisfy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rbSatisfy.setChecked(true);
				satifySelected();
				
			}
		});

		rbNeutral = (RadioButton)findViewById(R.id.rb_neutral);
		rbNeutral.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				neutralSelected();
			}
		});
		ivNeutral = (ImageView)findViewById(R.id.iv_neutral);
		ivNeutral.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rbNeutral.setChecked(true);
				neutralSelected();
				
			}
		});

		rbDissatify = (RadioButton)findViewById(R.id.rb_dissatisfied);
		rbDissatify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dissatifySelected();
				
			}
		});
		ivDissatify = (ImageView)findViewById(R.id.iv_dissatisfied);
		ivDissatify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rbDissatify.setChecked(true);
				dissatifySelected();
				
			}
		});

		rbStrongDissatify = (RadioButton)findViewById(R.id.rb_strong_dissatisfied);
		rbStrongDissatify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				strongDissatifySelected();
			}
		});
		ivStrongDissatify = (ImageView)findViewById(R.id.iv_strong_dissatisfied);
		ivStrongDissatify.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rbStrongDissatify.setChecked(true);
				strongDissatifySelected();
				
			}
		});

        ivEdit = (ImageView)findViewById(R.id.iv_edit);
        ivEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mPasswordDialog = new PasswordDialog(mContext, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mHandlerProcessing);
                mPasswordDialog.show();
            }
        });
		ivExit = (ImageView)findViewById(R.id.iv_close);
		ivExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				CommonUtils.showConfirmDialog(mContext, mContext.getString(R.string.warning_title), mContext.getString(R.string.message_close_setting),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								CommonUtils.confirmConfigApp(mContext);
								ivEdit.setVisibility(View.GONE);
								ivExit.setVisibility(View.GONE);
							}
						});
				hideSetting();
			}
		});
//		startService(new Intent(this, TimerService.class));
//		registerReceiver(mReceiverUpdateStatusReport, new IntentFilter(Constants.ACTION_UPDATE_REPORT_STATUS));
		registerReceiver(mReceiverNetworkChange, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
		initScreen();
		initSettingLayout();
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		if(rbActive != null){
			rbActive.setChecked(false);
		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
//		unregisterReceiver(mReceiverUpdateStatusReport);
		unregisterReceiver(mReceiverNetworkChange);
		super.onDestroy();
	}

//	BroadcastReceiver mReceiverUpdateStatusReport = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String lastSubmission = CommonUtils.getLastSubmittedRport(context);
//			String nextSubmission = CommonUtils.getNextSubmittedRport(context);
//			mTvLastSubmission.setText(getString(R.string.last_report_submission) + " " + lastSubmission);
//			mTvNextSubmission.setText(getString(R.string.next_report_submission) + " " + nextSubmission);
//		}
//	};

	BroadcastReceiver mReceiverNetworkChange = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if(netInfo != null && netInfo.isConnected()){
//				mViewConnectionStatus.setVisibility(View.GONE);
				mTvNetworkStatus.setVisibility(View.GONE);
				CommonUtils.writeLog("Network is Online");
//				mTvLastSubmission.setVisibility(View.VISIBLE);
//				mTvNextSubmission.setVisibility(View.VISIBLE);
			} else {
//				mViewConnectionStatus.setVisibility(View.VISIBLE);
				mTvNetworkStatus.setVisibility(View.VISIBLE);
				CommonUtils.writeLog("Network is Offline");
//				mTvNetworkStatus.setText("Offline");
//				mTvLastSubmission.setVisibility(View.INVISIBLE);
//				mTvNextSubmission.setVisibility(View.INVISIBLE);
			}
		}
	};

	private void initScreen()
	{
		SettingModel setting = CommonUtils.getSetting(this);
		if(setting != null){
			String mainTitle = setting.getMainTitle();
			if(mainTitle != null){
				mTvMainTitle.setText(mainTitle);
			}

            FontStyleModel mainTitleFontStyle = CommonUtils.getMainTitleFontStyle(mContext);
            mTvMainTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, mainTitleFontStyle.getFontSize());
            if(mainTitleFontStyle.isBold()){
                mTvMainTitle.setTypeface(mTvMainTitle.getTypeface(), Typeface.BOLD);
            } else {
                mTvMainTitle.setTypeface(null, Typeface.NORMAL);
            }
			String subTitle = setting.getSubTitle();
			if(subTitle != null){
				mTvSubTitle.setText(subTitle);
			}

            FontStyleModel subTitleFontStyle = CommonUtils.getSubTitleFontStyle(mContext);
            mTvSubTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, subTitleFontStyle.getFontSize());
            if(mainTitleFontStyle.isBold()){
                mTvSubTitle.setTypeface(mTvSubTitle.getTypeface(), Typeface.BOLD);
            } else {
                mTvSubTitle.setTypeface(null, Typeface.NORMAL);
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

			if(mSendingReport != null){
				mSendingReport.cancelSendingReport(mContext);
				mSendingReport.activeSendingReport(mContext);
			}
		}

		if(CommonUtils.isConfirmConfigApp(mContext)) {
			ivEdit.setVisibility(View.GONE);
			ivExit.setVisibility(View.GONE);
		} else {
			ivEdit.setVisibility(View.VISIBLE);
			ivExit.setVisibility(View.VISIBLE);
		}
	}

	private void strongSatifySelected(){
		if(rbActive != null && rbActive != rbStrongSatisfy){
			rbActive.setChecked(false);
		}
		
		rbActive = rbStrongSatisfy;
		
		Intent intent = new Intent(MainActivity.this, RemarkActivity.class);
		intent.putExtra("Status", STRONG_SATISFY);
		startActivity(intent);
	}
	
	private void satifySelected(){
		if(rbActive != null && rbActive != rbSatisfy){
			rbActive.setChecked(false);
		}
		
		rbActive = rbSatisfy;
		
		Intent intent = new Intent(MainActivity.this, RemarkActivity.class);
		intent.putExtra("Status", SATISFY);
		startActivity(intent);
	}
	
	private void neutralSelected(){
		if(rbActive != null && rbActive != rbNeutral){
			rbActive.setChecked(false);
		}
		
		rbActive = rbNeutral;
		
		Intent intent = new Intent(MainActivity.this, RemarkActivity.class);
		intent.putExtra("Status", NEUTRAL);
		startActivity(intent);
	}
	
	private void dissatifySelected(){
		if(rbActive != null && rbActive != rbDissatify){
			rbActive.setChecked(false);
		}
		
		rbActive = rbDissatify;
		
		Intent intent = new Intent(MainActivity.this, RemarkActivity.class);
		intent.putExtra("Status", DISSATISFY);
		startActivity(intent);
	}
	
	private void strongDissatifySelected(){
		if(rbActive != null && rbActive != rbStrongDissatify){
			rbActive.setChecked(false);
		}
		
		rbActive = rbStrongDissatify;
		
		Intent intent = new Intent(MainActivity.this, RemarkActivity.class);
		intent.putExtra("Status", STRONG_DISSATISFY);
		startActivity(intent);
	}
	
	@Override
    public void onBackPressed() {
      	if(mIsSettingShow) {
			hideSetting();
		}
    }

    private void onSelectFromGalleryResult(Intent data) {

        if (data != null) {
            mFilePath = ImageUtils.getPath(mContext, data.getData());
        }

    }

    private Handler mHandlerProcessing = new Handler() {
        @Override
        public void handleMessage(Message msg) {
			switch (msg.what) {
				case Constants.SHOW_SETTING_DIALOG:
					showSetting();
					break;
			}
			super.handleMessage(msg);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            onSelectFromGalleryResult(data);
            if (requestCode == Constants.SELECT_BG_FILE) {
                setBgPath(mFilePath);
            }
            else if (requestCode == Constants.SELECT_LOGO_FILE) {
               setLogoPath(mFilePath);
            }
        }
    }


    //Setting
	private void initSettingLayout()
	{
		mViewSetting = findViewById(R.id.layout_setting);
		mEdtDeviceDescription = (EditText)findViewById(R.id.edt_device_description);
		mEdtMainTitle = (EditText)findViewById(R.id.edt_main_title);
		mEdtSubTitle = (EditText)findViewById(R.id.edt_sub_title);
		mEdtInputEmail = (EditText)findViewById(R.id.edt_input_email);
		mTvDailyTime = (TextView) findViewById(R.id.tv_daily_time);
		mTvWeeklyTime = (TextView) findViewById(R.id.tv_weekly_time);
		mEdtFeedbackMainTitle = (EditText)findViewById(R.id.edt_feedback_main_title);
		mEdtFeedbackSubTitle = (EditText)findViewById(R.id.edt_feedback_sub_title);
		mEdtSubmitMainTitle = (EditText)findViewById(R.id.edt_submit_main_title);
		mBtnSelectBg = (Button)findViewById(R.id.btn_select_bg);
		mBtnSelectLogo = (Button)findViewById(R.id.btn_select_logo);
		mChkDaily = (RadioButton)findViewById(R.id.chk_daily);
		mChkWeekly = (RadioButton)findViewById(R.id.chk_weekly);
		mBtnConfirm = (Button)findViewById(R.id.btn_confirm);
		mBtnSelectBg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				galleryIntent(Constants.SELECT_BG_FILE);
			}
		});
		mBtnSelectLogo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				galleryIntent(Constants.SELECT_LOGO_FILE);
			}
		});
		mTvBgPath = (TextView)findViewById(R.id.tv_bg_more_description);
		mTvLogoPath = (TextView)findViewById(R.id.tv_logo_more_description);
		mTvDeviceDesBold = (TextView)findViewById(R.id.tv_device_description_bold);
		mTvMainTitleBold = (TextView)findViewById(R.id.tv_main_title_bold);
		mTvSubTitleBold = (TextView)findViewById(R.id.tv_sub_title_bold);
		mTvFeedbackMainTitleBold = (TextView)findViewById(R.id.tv_feedback_main_title_bold);
		mTvFeedbackSubTitleBold = (TextView)findViewById(R.id.tv_feedback_sub_title_bold);
		mTvSubmittedTitleBold = (TextView)findViewById(R.id.tv_submitted_title_bold);
		mEdtDeviceDesFontSize = (EditText)findViewById(R.id.edt_device_description_size);
		mEdtMainTitleFontSize = (EditText)findViewById(R.id.edt_main_title_size);
		mEdtSubTitleFontSize = (EditText)findViewById(R.id.edt_sub_title_size);
		mEdtFeedbackMainTitleFontSize = (EditText)findViewById(R.id.edt_feedback_main_title_size);
		mEdtFeedbackSubTitleFontSize = (EditText)findViewById(R.id.edt_feedback_sub_title_size);
		mEdtSubmittedTitleFontSize = (EditText)findViewById(R.id.edt_submitted_title_size);
		mTvBgMoreResolution = (TextView)findViewById(R.id.tv_bg_more_resolution);
		mTvLogoMoreResolution = (TextView)findViewById(R.id.tv_logo_more_resolution);

//        mEdtWeeklyTime.setInputType(InputType.TYPE_NULL);
//        mTvDailyTime.setInputType(InputType.TYPE_NULL);

		mShowAnim = AnimationUtils.loadAnimation(this, R.anim.left_to_right_anim);
		mHideAnim = AnimationUtils.loadAnimation(this, R.anim.right_to_left_anim);

		mTvWeeklyTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				new SlideDayTimePicker.Builder(((FragmentActivity)mContext).getSupportFragmentManager())
						.setListener(listener)
						.setInitialDay(1)
						.setInitialHour(13)
						.setInitialMinute(30)
						.setIs24HourTime(true)
						//.setCustomDaysArray(getResources().getStringArray(R.array.days_of_week))
						//.setTheme(SlideDayTimePicker.HOLO_DARK)
						//.setIndicatorColor(Color.parseColor("#990000"))
						.build()
						.show();
			}
		});
		mTvDailyTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Calendar mcurrentTime = Calendar.getInstance();
				int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
				int minute = mcurrentTime.get(Calendar.MINUTE);
				TimePickerDialog mTimePicker;
				mTimePicker = new TimePickerDialog(mContext, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
						String minute;
						if(selectedMinute < 10) {
							minute = "0" + String.valueOf(selectedMinute);
						} else {
							minute = String.valueOf(selectedMinute);
						}

						String hour;
						if(selectedHour < 10) {
							hour = "0" + String.valueOf(selectedHour);
						} else {
							hour = String.valueOf(selectedHour);
						}
						dailyHour = selectedHour;
						dailyMinute = selectedMinute;
						mTvDailyTime.setText(hour + ":" + minute);
					}
				}, hour, minute, true);
				mTimePicker.setTitle("Select Time");
				mTimePicker.show();
			}
		});
		mBtnConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(isValidData()) {
					saveSetting();
					initScreen();
					hideSetting();
				}
			}
		});
		mChkDaily.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mChkDaily.setChecked(true);
				mChkWeekly.setChecked(false);
			}
		});
		mChkWeekly.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mChkWeekly.setChecked(true);
				mChkDaily.setChecked(false);
			}
		});
		mTvDeviceDesBold.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean isBold = !mDeviceDescriptionFontStyle.isBold();
				setBoldDeviceDescription(isBold);
				mDeviceDescriptionFontStyle.setBold(isBold);
			}
		});
		mTvMainTitleBold.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean isBold = !mMainTitleFontStyle.isBold();
				setBoldMainTitle(isBold);
				mMainTitleFontStyle.setBold(isBold);
			}
		});
		mTvSubTitleBold.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean isBold = !mSubTitleFontStyle.isBold();
				setBoldSubTitle(isBold);
				mSubTitleFontStyle.setBold(isBold);
			}
		});
		mTvFeedbackMainTitleBold.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean isBold = !mFeedbackMainTitleFontStyle.isBold();
				setBoldFeedbackMainTitle(isBold);
				mFeedbackMainTitleFontStyle.setBold(isBold);
			}
		});
		mTvFeedbackSubTitleBold.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean isBold = !mFeedbackSubTitleFontStyle.isBold();
				setBoldFeedbackSubTitle(isBold);
				mFeedbackSubTitleFontStyle.setBold(isBold);
			}
		});
		mTvSubmittedTitleBold.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				boolean isBold = !mSubmittedTitleFontStyle.isBold();
				setBoldSubmittedTitle(isBold);
				mSubmittedTitleFontStyle.setBold(isBold);
			}
		});

		initSetting();
	}

	private void setBoldDeviceDescription(boolean isBold)
	{
		if(isBold){
			mTvDeviceDesBold.setTextColor(ContextCompat.getColor(mContext, R.color.white));
			mTvDeviceDesBold.setTypeface(mTvDeviceDesBold.getTypeface(), Typeface.BOLD);
		} else {
			mTvDeviceDesBold.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
			mTvDeviceDesBold.setTypeface(null, Typeface.NORMAL);
		}
	}

	private void setBoldMainTitle(boolean isBold)
	{
		if(isBold){
			mTvMainTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.white));
			mTvMainTitleBold.setTypeface(mTvMainTitleBold.getTypeface(), Typeface.BOLD);
		} else {
			mTvMainTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
			mTvMainTitleBold.setTypeface(null, Typeface.NORMAL);
		}
	}

	private void setBoldSubTitle(boolean isBold)
	{
		if(isBold){
			mTvSubTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.white));
			mTvSubTitleBold.setTypeface(mTvSubTitleBold.getTypeface(), Typeface.BOLD);
		} else {
			mTvSubTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
			mTvSubTitleBold.setTypeface(null, Typeface.NORMAL);
		}
	}

	private void setBoldFeedbackMainTitle(boolean isBold)
	{
		if(isBold){
			mTvFeedbackMainTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.white));
			mTvFeedbackMainTitleBold.setTypeface(mTvFeedbackMainTitleBold.getTypeface(), Typeface.BOLD);
		} else {
			mTvFeedbackMainTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
			mTvFeedbackMainTitleBold.setTypeface(null, Typeface.NORMAL);
		}
	}

	private void setBoldFeedbackSubTitle(boolean isBold)
	{
		if(isBold){
			mTvFeedbackSubTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.white));
			mTvFeedbackSubTitleBold.setTypeface(mTvFeedbackSubTitleBold.getTypeface(), Typeface.BOLD);
		} else {
			mTvFeedbackSubTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
			mTvFeedbackSubTitleBold.setTypeface(null, Typeface.NORMAL);
		}
	}

	private void setBoldSubmittedTitle(boolean isBold)
	{
		if(isBold){
			mTvSubmittedTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.white));
			mTvSubmittedTitleBold.setTypeface(mTvSubmittedTitleBold.getTypeface(), Typeface.BOLD);
		} else {
			mTvSubmittedTitleBold.setTextColor(ContextCompat.getColor(mContext, R.color.grey));
			mTvSubmittedTitleBold.setTypeface(null, Typeface.NORMAL);
		}
	}

	private void showSetting()
	{
		mViewSetting.setVisibility(View.VISIBLE);
		mViewSetting.startAnimation(mShowAnim);
		mIsSettingShow = true;
	}

	private void hideSetting()
	{
		mViewSetting.setVisibility(View.INVISIBLE);
		mViewSetting.startAnimation(mHideAnim);
		mIsSettingShow = false;
	}

	private boolean isValidData()
	{

		String[] emails = mEdtInputEmail.getText().toString().split(";");
		for(String e : emails){
			if(!CommonUtils.isEmailValid(e)){
				String fieldName = "valid format email";
				String message = mContext.getString(R.string.alert_message).replace("%m", fieldName);
				CommonUtils.showDialog(mContext, mContext.getString(R.string.title_alert_dialog), message);
				mEdtInputEmail.requestFocus();
				return false;
			}
		}

		if(mChkDaily.isChecked()){
			String time = mTvDailyTime.getText().toString();
			if(time.equalsIgnoreCase("")){
				String fieldName = "Daily Time";
				String message = mContext.getString(R.string.alert_message).replace("%m", fieldName);
				CommonUtils.showDialog(mContext,mContext.getString(R.string.title_alert_dialog),message);
				mTvDailyTime.requestFocus();
				return false;
			}
		} else {
			String time = mTvWeeklyTime.getText().toString();
			if(time.equalsIgnoreCase("")){
				String fieldName = "Weekly Time";
				String message = mContext.getString(R.string.alert_message).replace("%m", fieldName);
				CommonUtils.showDialog(mContext,mContext.getString(R.string.title_alert_dialog), message);
				mTvWeeklyTime.requestFocus();
				return false;
			}
		}

		return true;
	}

	private void initSetting()
	{
		SettingModel setting = CommonUtils.getSetting(mContext);
		if(setting != null){
			mTvDailyTime.setText(setting.getDailyTime());
			mEdtDeviceDescription.setText(setting.getDeviceDescription());
			mEdtFeedbackMainTitle.setText(setting.getFeedbackMainTitle());
			mEdtFeedbackSubTitle.setText(setting.getFeedbackSubTitle());
			mEdtInputEmail.setText(TextUtils.join(";", setting.getLstEmails()));
			mEdtMainTitle.setText(setting.getMainTitle());
			mEdtSubTitle.setText(setting.getSubTitle());
			mEdtSubmitMainTitle.setText(setting.getSubmitMainTitle());
			mTvWeeklyTime.setText(setting.getWeeklyTime());
			if(setting.isDailySending()){
				isDailyPreviousSending = true;
				mChkDaily.setChecked(true);
				mChkWeekly.setChecked(false);
			} else {
				isDailyPreviousSending = false;
				mChkDaily.setChecked(false);
				mChkWeekly.setChecked(true);
			}
			dailyHour = setting.getDailyHours();
			dailyMinute = setting.getDailyMinute();
			weeklyHour = setting.getWeeklyHours();
			weeklyMinute = setting.getWeeklyMinutes();
			dayOfWeeks = setting.getDayOfWeek();
			bgPath = setting.getBackgroundPath();
			logoPath = setting.getLogoPath();
			if(bgPath != null) {
				mTvBgPath.setText(mContext.getString(R.string.upload_file_successfuly).replace("%f", bgPath));
//				mTvBgMoreResolution.setVisibility(View.GONE);
			}
//			else {
//				String widthBg = String.valueOf(CommonUtils.getWidthOfScreen((Activity) mContext));
//				String heightBg = String.valueOf(CommonUtils.getHeightOfScreen((Activity) mContext));
//				mTvBgMoreResolution.setText(mContext.getString(R.string.file_resolution).replace("%s", widthBg + "x" + heightBg));
//				mTvBgMoreResolution.setVisibility(View.VISIBLE);
//			}
			if(logoPath != null) {
				mTvLogoPath.setText(mContext.getString(R.string.upload_file_successfuly).replace("%f", logoPath));
//				mTvLogoMoreResolution.setVisibility(View.GONE);
			}
//			else {
//				String widthLogo = String.valueOf(CommonUtils.getWidthOfScreen((Activity) mContext));
//				String heightLogo = "180";
//				mTvLogoMoreResolution.setText(mContext.getString(R.string.file_resolution).replace("%s", widthLogo + "x" + heightLogo));
//				mTvLogoMoreResolution.setVisibility(View.VISIBLE);
//			}
		}

		String widthBg = String.valueOf(CommonUtils.getWidthOfScreen((Activity) mContext));
		String heightBg = String.valueOf(CommonUtils.getHeightOfScreen((Activity) mContext));
		mTvBgMoreResolution.setText(mContext.getString(R.string.file_resolution).replace("%s", widthBg + "x" + heightBg));

		String widthLogo = String.valueOf(CommonUtils.getWidthOfScreen((Activity) mContext));
		String heightLogo = "180";
		mTvLogoMoreResolution.setText(mContext.getString(R.string.file_resolution).replace("%s", widthLogo + "x" + heightLogo));

		mDeviceDescriptionFontStyle = CommonUtils.getDeviceDesFontStyle(mContext);
		mMainTitleFontStyle = CommonUtils.getMainTitleFontStyle(mContext);
		mSubTitleFontStyle = CommonUtils.getSubTitleFontStyle(mContext);
		mFeedbackMainTitleFontStyle = CommonUtils.getFeedbackMainTitleFontStyle(mContext);
		mFeedbackSubTitleFontStyle = CommonUtils.getFeedbackSubTitleFontStyle(mContext);
		mSubmittedTitleFontStyle = CommonUtils.getSubmittedTitleFontStyle(mContext);

		mEdtDeviceDesFontSize.setText(String.valueOf(mDeviceDescriptionFontStyle.getFontSize()));
		setBoldDeviceDescription(mDeviceDescriptionFontStyle.isBold());
		mEdtMainTitleFontSize.setText(String.valueOf(mMainTitleFontStyle.getFontSize()));
		setBoldMainTitle(mMainTitleFontStyle.isBold());
		mEdtSubTitleFontSize.setText(String.valueOf(mSubTitleFontStyle.getFontSize()));
		setBoldSubTitle(mSubTitleFontStyle.isBold());
		mEdtFeedbackMainTitleFontSize.setText(String.valueOf(mFeedbackMainTitleFontStyle.getFontSize()));
		setBoldFeedbackMainTitle(mFeedbackMainTitleFontStyle.isBold());
		mEdtFeedbackSubTitleFontSize.setText(String.valueOf(mFeedbackSubTitleFontStyle.getFontSize()));
		setBoldFeedbackSubTitle(mFeedbackSubTitleFontStyle.isBold());
		mEdtSubmittedTitleFontSize.setText(String.valueOf(mSubmittedTitleFontStyle.getFontSize()));
		setBoldSubmittedTitle(mSubmittedTitleFontStyle.isBold());

	}


	final SlideDayTimeListener listener = new SlideDayTimeListener() {

		@Override
		public void onDayTimeSet(int day, int hour, int minute)
		{
			String dayOfWeek = "";
			switch (day){
				case 1:
					dayOfWeek = "Sun";
					break;
				case 2:
					dayOfWeek = "Mon";
					break;
				case 3:
					dayOfWeek = "Tue";
					break;
				case 4:
					dayOfWeek = "Wed";
					break;
				case 5:
					dayOfWeek = "Thu";
					break;
				case 6:
					dayOfWeek = "Fri";
					break;
				case 7:
					dayOfWeek = "Sat";
					break;
				default:
					dayOfWeek = "Mon";
					break;

			}

			String strMinute;
			if(minute < 10) {
				strMinute = "0" + String.valueOf(minute);
			} else {
				strMinute = String.valueOf(minute);
			}

			String strHour;
			if(hour < 10) {
				strHour = "0" + String.valueOf(hour);
			} else {
				strHour = String.valueOf(hour);
			}
			weeklyHour = hour;
			weeklyMinute = minute;
			dayOfWeeks = day;

			mTvWeeklyTime.setText(dayOfWeek + ";" + strHour + ":" + strMinute);
		}

		@Override
		public void onDayTimeCancel()
		{
		}
	};


	private void saveSetting()
	{
		SettingModel setting = new SettingModel();
		setting.setBackgroundPath(bgPath);
		if(mChkDaily.isChecked()) {
			setting.setDailySending(true);
			CommonUtils.writeLog("Save Setting : Daily : " + dailyHour + ":" + dailyMinute);
		} else {
			setting.setDailySending(false);
			CommonUtils.writeLog("Save Setting : Weekly : " + dayOfWeeks + ", " + weeklyHour + ":" + weeklyMinute);
		}
		setting.setDailyPreviousSending(isDailyPreviousSending);
		setting.setDailyHours(dailyHour);
		setting.setDailyMinute(dailyMinute);
		setting.setWeeklyHours(weeklyHour);
		setting.setWeeklyMinutes(weeklyMinute);
		setting.setDayOfWeek(dayOfWeeks);
		setting.setDailyTime(mTvDailyTime.getText().toString());
		setting.setWeeklyTime(mTvWeeklyTime.getText().toString());
		setting.setDeviceDescription(mEdtDeviceDescription.getText().toString());
		setting.setFeedbackMainTitle(mEdtFeedbackMainTitle.getText().toString());
		setting.setFeedbackSubTitle(mEdtFeedbackSubTitle.getText().toString());
		setting.setLogoPath(logoPath);

		String[] emails = mEdtInputEmail.getText().toString().split(";");
		setting.setLstEmails(emails);
		setting.setMainTitle(mEdtMainTitle.getText().toString());
		setting.setSubTitle(mEdtSubTitle.getText().toString());
		setting.setSubmitMainTitle(mEdtSubmitMainTitle.getText().toString());

		CommonUtils.saveSetting(mContext, setting);
		CommonUtils.setIsSetting(mContext, true);

		mDeviceDescriptionFontStyle.setFontSize(Integer.parseInt(mEdtDeviceDesFontSize.getText().toString()));
		CommonUtils.saveDeviceDesFontStyle(mContext, mDeviceDescriptionFontStyle);
		mMainTitleFontStyle.setFontSize(Integer.parseInt(mEdtMainTitleFontSize.getText().toString()));
		CommonUtils.saveMainTitleFontStyle(mContext, mMainTitleFontStyle);
		mSubTitleFontStyle.setFontSize(Integer.parseInt(mEdtSubTitleFontSize.getText().toString()));
		CommonUtils.saveSubTitleFontStyle(mContext, mSubTitleFontStyle);
		mFeedbackMainTitleFontStyle.setFontSize(Integer.parseInt(mEdtFeedbackMainTitleFontSize.getText().toString()));
		CommonUtils.saveFeedbackMainTitleFontStyle(mContext, mFeedbackMainTitleFontStyle);
		mFeedbackSubTitleFontStyle.setFontSize(Integer.parseInt(mEdtFeedbackSubTitleFontSize.getText().toString()));
		CommonUtils.saveFeedbackSubTitleFontStyle(mContext, mFeedbackSubTitleFontStyle);
		mSubmittedTitleFontStyle.setFontSize(Integer.parseInt(mEdtSubmittedTitleFontSize.getText().toString()));
		CommonUtils.saveSubmittedTitleFontStyle(mContext, mSubmittedTitleFontStyle);
	}

	private void galleryIntent(int selectType)
	{
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		((Activity)mContext).startActivityForResult(Intent.createChooser(intent, mContext.getString(R.string.select_file)), selectType);
	}

	public void setBgPath(String path){
		bgPath = path;
		mTvBgPath.setText(mContext.getString(R.string.upload_file_successfuly).replace("%f", CommonUtils.getFileName(path)));
	}

	public void setLogoPath(String path){
		logoPath = path;
		mTvLogoPath.setText(mContext.getString(R.string.upload_file_successfuly).replace("%f", CommonUtils.getFileName(path)));
	}

}
