package com.apptitude.surveyapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.apptitude.surveyapp.R;
import com.apptitude.surveyapp.services.TimerService;
import com.apptitude.surveyapp.utils.Constants;
import com.apptitude.surveyapp.utils.ImageUtils;

public class MainActivity extends FragmentActivity{

	private static final String STRONG_SATISFY = "Very Satisfied";
	private static final String SATISFY = "Satisfied";
	private static final String NEUTRAL = "Neutral";
	private static final String DISSATISFY = "Dissatisfied";
	private static final String STRONG_DISSATISFY = "Very Dissatisfied";
	private RadioButton rbStrongSatisfy, rbSatisfy, rbNeutral, rbDissatify, rbStrongDissatify;
	private ImageView ivStrongSatisfy, ivSatisfy, ivNeutral, ivDissatify, ivStrongDissatify;
	private ImageView ivEdit, ivExit;
	
	private static RadioButton rbActive;
	private Context mContext;
    private String mFilePath;
    private SettingDialog mSettingDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
        mContext = this;
		
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
                mSettingDialog = new SettingDialog(mContext);
                mSettingDialog.show();
            }
        });
		startService(new Intent(this, TimerService.class));
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void onResume() {
		if(rbActive != null){
			rbActive.setChecked(false);
		}
		super.onResume();
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
