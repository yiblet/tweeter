package com.codepath.apps.tweeter.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yiblet on 6/27/16.
 */
public class User {
//    protected int followers_count;
//    protected int friends_count;
    protected String name_;
//    protected String profile_background_color;
//    protected String profile_background_image_url;
//    protected String profile_banner_url;
    protected String profile_image_url;
//    protected String profile_link_color;
//    protected String profile_sidebar_border_color;
//    protected String profile_sidebar_fill_color;
//    protected String profile_text_color;
    protected String screen_name;
//    protected List<Tweet> status_;
//    protected int statuses_count;
//    protected time_zone String;
//    protected String url;
//    protected boolean verified;

    public User(JSONObject raw) {
        try {
            profile_image_url = raw.getString("profile_image_url");
            screen_name = raw.getString("screen_name");
            name_ = raw.getString("name");
//            friends_count = raw.getInt("friends_count");
//            followers_count = raw.getInt("followers_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name_;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public String getScreen_name() {
        return screen_name;
    }
}
