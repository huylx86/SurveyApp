package com.apptitude.feedbacknow.models;

/**
 * Created by hle59 on 3/1/2017.
 */

public class SettingModel {
    private String deviceDescription;
    private String mainTitle;
    private String subTitle;
    private String[] lstEmails;
    private int dailyHours;
    private int dailyMinute;
    private int weeklyHours;
    private int weeklyMinutes;
    private int dayOfWeek;
    private String dailyTime;
    private String weeklyTime;
    private boolean isDailySending;
    private boolean isDailyPreviousSending;
    private String feedbackMainTitle;
    private String feedbackSubTitle;
    private String submitMainTitle;
    private String backgroundPath;
    private String logoPath;

    public String getDeviceDescription() {
        return deviceDescription;
    }

    public void setDeviceDescription(String deviceDescription) {
        this.deviceDescription = deviceDescription;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String[] getLstEmails() {
        return lstEmails;
    }

    public void setLstEmails(String[] lstEmails) {
        this.lstEmails = lstEmails;
    }

    public String getDailyTime() {
        return dailyTime;
    }

    public void setDailyTime(String dailyTime) {
        this.dailyTime = dailyTime;
    }

    public String getWeeklyTime() {
        return weeklyTime;
    }

    public void setWeeklyTime(String weeklyTime) {
        this.weeklyTime = weeklyTime;
    }

    public boolean isDailySending() {
        return isDailySending;
    }

    public void setDailySending(boolean dailySending) {
        isDailySending = dailySending;
    }

    public boolean isDailyPreviousSending() {
        return isDailyPreviousSending;
    }

    public void setDailyPreviousSending(boolean dailyPreviousSending) {
        isDailyPreviousSending = dailyPreviousSending;
    }

    public String getFeedbackMainTitle() {
        return feedbackMainTitle;
    }

    public void setFeedbackMainTitle(String feedbackMainTitle) {
        this.feedbackMainTitle = feedbackMainTitle;
    }

    public String getFeedbackSubTitle() {
        return feedbackSubTitle;
    }

    public void setFeedbackSubTitle(String feedbackSubTitle) {
        this.feedbackSubTitle = feedbackSubTitle;
    }

    public String getSubmitMainTitle() {
        return submitMainTitle;
    }

    public void setSubmitMainTitle(String submitMainTitle) {
        this.submitMainTitle = submitMainTitle;
    }

    public String getBackgroundPath() {
        return backgroundPath;
    }

    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public int getDailyHours() {
        return dailyHours;
    }

    public void setDailyHours(int dailyHours) {
        this.dailyHours = dailyHours;
    }

    public int getDailyMinute() {
        return dailyMinute;
    }

    public void setDailyMinute(int dailyMinute) {
        this.dailyMinute = dailyMinute;
    }

    public int getWeeklyHours() {
        return weeklyHours;
    }

    public void setWeeklyHours(int weeklyHours) {
        this.weeklyHours = weeklyHours;
    }

    public int getWeeklyMinutes() {
        return weeklyMinutes;
    }

    public void setWeeklyMinutes(int weeklyMinutes) {
        this.weeklyMinutes = weeklyMinutes;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
