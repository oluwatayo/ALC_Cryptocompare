package com.example.root.crptocompare;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 11/4/17.
 */

public class CurrencyAdapter extends ArrayAdapter<Currency> {

    public CurrencyAdapter(@NonNull Context context, @NonNull ArrayList<Currency> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currencyView = convertView;
        if(currencyView == null){
            currencyView = LayoutInflater.from(getContext()).inflate(R.layout.currency_list, parent, false);
        }
        Currency currency = getItem(position);
        TextView base = (TextView) currencyView.findViewById(R.id.baseCurrency);
        base.setText(currency.getmBase());
        TextView crypto = (TextView) currencyView.findViewById(R.id.crypto);
        crypto.setText(currency.getmCrypt());

        return currencyView;
    }
}
