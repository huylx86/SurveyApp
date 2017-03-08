package com.apptitude.feedbacknow.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.apptitude.feedbacknow.R;
import com.apptitude.feedbacknow.models.SettingModel;
import com.apptitude.feedbacknow.services.SendingReportTask;
import com.apptitude.feedbacknow.utils.CommonUtils;
import com.apptitude.feedbacknow.utils.Constants;
import com.apptitude.feedbacknow.utils.ImageUtils;

import java.io.IOException;

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
			}
		});
//		startService(new Intent(this, TimerService.class));
//		registerReceiver(mReceiverUpdateStatusReport, new IntentFilter(Constants.ACTION_UPDATE_REPORT_STATUS));
		registerReceiver(mReceiverNetworkChange, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
		initScreen();
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
//				mTvLastSubmission.setVisibility(View.VISIBLE);
//				mTvNextSubmission.setVisibility(View.VISIBLE);
			} else {
//				mViewConnectionStatus.setVisibility(View.VISIBLE);
				mTvNetworkStatus.setVisibility(View.VISIBLE);
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

			String subTitle = setting.getSubTitle();
			if(subTitle != null){
				mTvSubTitle.setText(subTitle);
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
					mSettingDialog = new SettingDialog(mContext, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mHandlerProcessing);
					mSettingDialog.show();
					break;
				case Constants.APPLY_CONFIGURATION:
					initScreen();
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
                mSettingDialog.setBgPath(mFilePath);
            }
            else if (requestCode == Constants.SELECT_LOGO_FILE) {
                mSettingDialog.setLogoPath(mFilePath);
            }
        }
    }
}
