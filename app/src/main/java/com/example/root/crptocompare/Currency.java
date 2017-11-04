package com.example.root.crptocompare;

/**
 * Created by root on 11/4/17.
 */

public class Currency {
    private String mBase;
    private String mCrypt;

    public Currency(String mBase, String mCrypt) {
        this.mBase = mBase;
        this.mCrypt = mCrypt;
    }

    public String getmBase() {
        return mBase;
    }

    public String getmCrypt() {
        return mCrypt;
    }
}
