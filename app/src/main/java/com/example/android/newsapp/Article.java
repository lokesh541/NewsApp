package com.example.android.newsapp;

/**
 * Created by lokesh on 29/6/16.
 */
public class Article {


    private String mTitle;
    private String mThumbNailURL;
    private  String mWebUrl;


    public Article(String ThumbNailURL, String title,String WebUrl) {

        mTitle = title;
        mThumbNailURL = ThumbNailURL;
        mWebUrl =  WebUrl;

    }


    public String getTitle() {
        return  mTitle;
    }

    public  String getThumbNailURL() {
        return mThumbNailURL;
    }

    public  String getmWebUrl() {
        return mWebUrl;
    }
}
