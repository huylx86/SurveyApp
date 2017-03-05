package com.apptitude.feedbacknow.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apptitude.feedbacknow.R;
import com.apptitude.feedbacknow.models.SettingModel;
import com.apptitude.feedbacknow.utils.CommonUtils;

import java.io.IOException;

import pl.droidsonroids.gif.GifImageView;

public class SubmittedActivity extends Activity {

    private TextView mTvSubmittedTitle;
    private View mMainView;
    private ImageView mIvLogo;
    private GifImageView mIvLogGif, mIvBgGif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submitted);
        initLayout();
        initScreen();

        mHandlerClose.sendEmptyMessageDelayed(0, 2000);
    }

    private void initLayout()
    {
        mTvSubmittedTitle = (TextView)findViewById(R.id.tv_submitted_title);
        mMainView = findViewById(R.id.ln_main_view);
        mIvLogo = (ImageView)findViewById(R.id.iv_logo);
        mIvLogGif = (GifImageView)findViewById(R.id.iv_logo_gif);
        mIvBgGif = (GifImageView)findViewById(R.id.iv_bg_gif);
    }

    private void initScreen()
    {
        SettingModel setting = CommonUtils.getSetting(this);
        if(setting != null) {
            String submittedTitle = setting.getSubmitMainTitle();
            if (submittedTitle != null) {
                mTvSubmittedTitle.setText(submittedTitle);
            }

            String bgPath = setting.getBackgroundPath();
            if(!bgPath.equalsIgnoreCase("")){
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
            if(!logoPath.equalsIgnoreCase("")){
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
    }

    private Handler mHandlerClose = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            finish();
            super.handleMessage(msg);
        }
    };
}
