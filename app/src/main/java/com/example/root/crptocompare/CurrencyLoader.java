package com.example.root.crptocompare;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by root on 11/4/17.
 */

public class CurrencyLoader extends AsyncTaskLoader<ArrayList<Currency>> {
    private String string;
    private final String LOG_TAG = CurrencyLoader.class.getSimpleName();
    public CurrencyLoader(Context context, String str) {
        super(context);
        this.string = str;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<Currency> loadInBackground() {
        if(TextUtils.isEmpty(string)){
            return null;
        }
        return Networking.fetchCurrency(string);
    }
}
