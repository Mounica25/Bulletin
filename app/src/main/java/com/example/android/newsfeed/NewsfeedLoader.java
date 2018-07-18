package com.example.android.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsfeedLoader extends AsyncTaskLoader {

    private String mUrl;

    public NewsfeedLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Newsfeed> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<Newsfeed> newsfeeds = QueryUtils.fetchNewsfeedData(mUrl);
        return newsfeeds;
    }
}