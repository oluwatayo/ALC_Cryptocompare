package com.example.root.crptocompare;

/**
 * Created by root on 11/4/17.
 */

public class Currency {
    private String mBase;
    private String mCrypt;
    private String mEth;

    public Currency(String mBase, String mCrypt, String mEth) {
        this.mBase = mBase;
        this.mCrypt = mCrypt;
        this.mEth = mEth;
    }

    public String getmBase() {
        return mBase;
    }

    public String getmCrypt() {
        return mCrypt;
    }

    public String getmEth() {
        return mEth;
    }
}
