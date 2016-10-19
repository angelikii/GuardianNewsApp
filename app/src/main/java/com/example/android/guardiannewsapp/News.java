
package com.example.android.guardiannewsapp;

import android.graphics.Bitmap;

public class News {

    private String mTitle;
    private String mWebUrl;
    private String mPubDate;
    private Bitmap mNewsImage;


    public News(String title, String webUrl, String pubDate, Bitmap newsImage) {
        mTitle = title;
        mWebUrl = webUrl;
        mPubDate = pubDate;
        mNewsImage = newsImage;
    }

    public News(String title, String webUrl, String pubDate) {
        mTitle = title;
        mWebUrl = webUrl;
        mPubDate = pubDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getWebUrl() { return mWebUrl; }

    public String getDate() { return mPubDate; }

    public Bitmap getImage() { return mNewsImage; }

}
