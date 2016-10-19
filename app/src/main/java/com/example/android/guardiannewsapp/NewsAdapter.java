/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.guardiannewsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {


    public NewsAdapter(Context context, List<News> newsItems) {
        super(context, 0, newsItems);
    }
 // why getView preexists in the declaration but without being filled?
    //what is the ViewGroup parent parameter and how (by which variable) is it filled?
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        News currentNewsItem = getItem(position);

        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        String newsTitle = currentNewsItem.getTitle();
        titleView.setText(newsTitle);

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        String pubDate = currentNewsItem.getDate();
        dateView.setText(pubDate);

//        //set the image
//        ImageView iv = (ImageView) listItemView.findViewById(R.id.news_thumbnail);
//        Bitmap thumbnail = currentNewsItem.getImage();
//        iv.setImageBitmap(thumbnail);

        listItemView.setEnabled(false);
        listItemView.setOnClickListener(null);

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

}
