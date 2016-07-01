package com.codepath.apps.tweeter.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yiblet on 6/27/16.
 */
public class Tweet {
//    protected List<User> contributors;
//    protected String[] coordinates;
    protected String created_at;
//    protected List<String> hashtags;
    protected int favorite_count;
    protected boolean favorited;
    protected String id_str;

    public String getText() {
        return text;
    }

    protected String text;

    public Tweet getRetweeted_status() {
        return retweeted_status;
    }

    public boolean isRetweet() {
        return retweeted_status != null;
    }

    //    protected String in_reply_to_screen_name;
//    protected String in_reply_to_status_id_str;
//    protected String in_reply_to_user_id_str;
//    protected String quoted_status_id_str;
//    protected Tweet quoted;
//    protected int retweet_count;
//    protected boolean retweeted;
    protected Tweet retweeted_status;
    protected User user_;

    public String getCreated_at() {
        return created_at;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public String getId_str() {
        return id_str;
    }

    public User getUser() {
        return user_;
    }

    public Tweet(JSONObject raw) {


        try {
            created_at = raw.getString("created_at");
            if (raw.getString("favorite_count").equals("null")) {
                favorite_count = -1;
            } else favorite_count = raw.getInt("favorite_count");
            String str = raw.getString("favorited");
            if (str.equals("null") || str.equals("false")) favorited = false;
            else favorited = true;
            id_str = raw.getString("id_str");
            user_ = new User(raw.getJSONObject("user"));
            text = raw.getString("text");
            try {
                JSONObject retweet = raw.getJSONObject("retweeted_status");
                if (retweet != null) {
                    retweeted_status = new Tweet(retweet);
                }
            } catch (JSONException e) {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //unimplemented fields
    //lang
    //current_user_retweet
    //place
    //possibly_sensitive
    //scopes
    //truncated
    //withheld_copyright
    //withheld_in_countries
    //withheld_scope
    //contributors

}
