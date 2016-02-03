package com.gb.quoteit;

import com.orm.SugarRecord;

/**
 * Created by gauravbhoyar on 04/02/16.
 */
public class QuoteData extends SugarRecord {
    String mImageURL;
    String mDATE;
    String mFavourite;
    String COL5;
    String COL6;

    public QuoteData(String mImageURL, String mDATE, String mFavourite, String COL5, String COL6) {
        this.mImageURL = mImageURL;
        this.mDATE = mDATE;
        this.mFavourite = mFavourite;
        this.COL5 = COL5;
        this.COL6 = COL6;
    }

    public String getmImageURL() {
        return mImageURL;
    }

    public void setmImageURL(String mImageURL) {
        this.mImageURL = mImageURL;
    }

    public String getmDATE() {
        return mDATE;
    }

    public void setmDATE(String mDATE) {
        this.mDATE = mDATE;
    }

    public String getmFavourite() {
        return mFavourite;
    }

    public void setmFavourite(String mFavourite) {
        this.mFavourite = mFavourite;
    }

    public String getCOL5() {
        return COL5;
    }

    public void setCOL5(String COL5) {
        this.COL5 = COL5;
    }

    public String getCOL6() {
        return COL6;
    }

    public void setCOL6(String COL6) {
        this.COL6 = COL6;
    }
}
