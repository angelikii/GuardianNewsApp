package com.example.android.guardiannewsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;


public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        // what is forceload doing? is it just throwing a RunTime exception?
        forceLoad();
    }
//why to use onStartLoading to force load and not to put both in loadInBackground? since the declarations are empty
    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // Perform the network request, parse the response, and extract a list of books.
        List<News> books = QueryUtils.fetchNewsData(mUrl);
        return books;
    }
}

