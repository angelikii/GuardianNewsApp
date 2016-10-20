
package com.example.android.guardiannewsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class NewsActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    private static final String NEWS_URL = "http://content.guardianapis.com/search?q=donald%20trump&api-key=test&show-fields=thumbnail";

  //  String userInput;
    TextView mEmptyStateView;
    private NewsAdapter mAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);

        // here we create and pass an adapter to our bookListView to inflate it
        ListView newsListView = (ListView) findViewById(R.id.list);
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(mAdapter);

        // we call the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //we locate the empty state view
        mEmptyStateView = (TextView) findViewById(R.id.empty_state);

        //we create and initialize a Loader Manager
        final LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(0, null, NewsActivity.this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                     @Override
                                                     public void onRefresh() {
                                                         loaderManager.restartLoader(0, null, NewsActivity.this);
                                                         mSwipeRefreshLayout.setRefreshing(false);

                                                     }
                                                 });


        //when clicked upon, the list items lead to the articles.
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNewsItem = mAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNewsItem.getWebUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });

    }


    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        //we create and load the loader with a valid url
        return new NewsLoader(this, NEWS_URL);
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
