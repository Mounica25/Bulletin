package com.example.android.newsfeed;

public class Newsfeed {

    private String mTitle, mSectionName, mUrl, mPublicationDate;

    public Newsfeed(String title, String sectionName, String publicationDate, String url) {
        mTitle = title;
        mSectionName = sectionName;
        mPublicationDate = publicationDate;
        mUrl = url;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSectionName() {
        return mSectionName;
    }

    public String getmPublicationDate() {
        return mPublicationDate;
    }

    public String getmUrl() {
        return mUrl;
    }
}