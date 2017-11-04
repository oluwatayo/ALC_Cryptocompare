package com.example.root.crptocompare;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Currency>> {
    private final String url = "https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms=NGN,USD,AUD,GBP,JPY,CHF,AFN,DZD,AOA,ARS,BRL,XOF,CNY,SVC,ETB,HUF,NAD,NZD,NOK,PHP";
    private CurrencyAdapter currencyAdapter;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar progressBar;
    private TextView textView;
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currencyAdapter = new CurrencyAdapter(this, new ArrayList<Currency>());

        final ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(currencyAdapter);

        textView = (TextView) findViewById(R.id.emptyCurrency);
        listView.setEmptyView(textView);

        //call doProcess Here that verifies internet connection and initializes loader
        doProcess();

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doProcess();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Currency currency = currencyAdapter.getItem(i);
                Intent intent = new Intent(MainActivity.this, Converter.class);
                assert currency != null;
                intent.putExtra("name", currency.getmBase());
                intent.putExtra("value", currency.getmCrypt());
                startActivity(intent);
            }
        });
        /*Manually Set listview OnscrollListner so that swiperefresh is only trigerred
        * when the listview is at the top
        * */
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                boolean enable = false;
                if(listView != null && listView.getChildCount() > 0){
                    // check if the first item of the list is visible
                    boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                    // check if the top of the first item is visible
                    boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                    // enabling or disabling the refresh layout
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                refreshLayout.setEnabled(enable);
            }});

    }

    @Override
    public Loader<ArrayList<Currency>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "TEST: Calling onCreateLoader");
        return new CurrencyLoader(MainActivity.this, url);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Currency>> loader, ArrayList<Currency> currencies) {
        Log.i(LOG_TAG, "TEST: Calling OnLoadFinished");
        progressBar = (ProgressBar) findViewById(R.id.loading);
        progressBar.setVisibility(View.GONE);
        currencyAdapter.clear();
        if (currencies != null && !currencies.isEmpty()) {
            currencyAdapter.addAll(currencies);
        } else {
            textView = (TextView) findViewById(R.id.emptyCurrency);
            textView.setText(getString(R.string.empty));
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Currency>> loader) {
        currencyAdapter.clear();
    }

    public void doProcess() {
        progressBar = (ProgressBar) findViewById(R.id.loading);
        textView = (TextView) findViewById(R.id.emptyCurrency);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        LoaderManager loaderManager = getLoaderManager();
        Log.i(LOG_TAG, "TEST: calling initloader");
        if (isConnected) {
            if (refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }
            loaderManager.initLoader(0, null, this);
        } else {
            textView.setText(getString(R.string.noInternet));
            progressBar.setVisibility(View.GONE);
            if (refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
