package com.pradipatle.cityguide.tumsar.model;

/**
 * Created by Aeon-02 on 9/11/2017.
 */

public class FbFeedsModel {
    public String created_time;
    public String feeds;

    public String getFeeds() {
        return feeds;
    }
    public void setFeeds(String feeds) {this.feeds = feeds;}

    public String getCreatedTime() {
        return created_time;
    }
    public void setCreatedTime(String created_time) {
        this.created_time = created_time;
    }
}

