
package com.example.android.guardiannewsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    private static final String GOOGLEBOOKS_URL =
            "https://www.googleapis.com/books/v1/volumes?q=";
  //  String userInput;
    TextView mEmptyStateView;
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        // here we create and pass an adapter to our bookListView to inflate it
        ListView bookListView = (ListView) findViewById(R.id.list);
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        bookListView.setAdapter(mAdapter);

        // we call the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //we locate the empty state view
        mEmptyStateView = (TextView) findViewById(R.id.empty_state);

        //we create and initialize a Loader Manager
        final LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(0, null, NewsActivity.this);
    }


    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        //we create and load the loader with a valid url
        return new NewsLoader(this, GOOGLEBOOKS_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsItems) {

        mAdapter.clear();

        if (newsItems != null && !newsItems.isEmpty()) {
            //in case the previous search resulted in an empty state (no books)
            mEmptyStateView.setVisibility(GONE);
            mAdapter.addAll(newsItems);
        } else {
            mEmptyStateView.setVisibility(View.VISIBLE);
            mEmptyStateView.setText(getString(R.string.emptystate));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.clear();
    }
}
