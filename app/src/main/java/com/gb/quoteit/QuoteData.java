package com.gb.quoteit;

/**
 * Created by gauravbhoyar on 04/02/16.
 */
public class QuoteData  {

    public String getIMAGE() {
        return IMAGE;
    }

    public void setIMAGE(String IMAGE) {
        this.IMAGE = IMAGE;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public String getFAV() {
        return FAV;
    }

    public void setFAV(String FAV) {
        this.FAV = FAV;
    }

    public String getCOL4() {
        return COL4;
    }

    public void setCOL4(String COL4) {
        this.COL4 = COL4;
    }

    public String getCOl5() {
        return COl5;
    }

    public void setCOl5(String COl5) {
        this.COl5 = COl5;
    }

    String IMAGE;

    public QuoteData(String IMAGE, String DATE, String FAV, String COL4, String COl5) {
        this.IMAGE = IMAGE;
        this.DATE = DATE;
        this.FAV = FAV;
        this.COL4 = COL4;
        this.COl5 = COl5;
    }

    String DATE;
    String FAV;
    String COL4;
    String COl5;
}
