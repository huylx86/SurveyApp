package com.apptitude.feedbacknow.models;

/**
 * Created by LENOVO on 4/8/2017.
 */

public class FontStyleModel {
    private int fontSize = 38;
    private boolean isBold;

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isBold() {
        return isBold;
    }

    public void setBold(boolean bold) {
        isBold = bold;
    }
}
