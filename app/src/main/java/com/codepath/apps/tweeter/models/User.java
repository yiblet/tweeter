package com.codepath.apps.tweeter.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yiblet on 6/27/16.
 */
public class User implements Parcelable {
    protected int followers_count;
    protected int friends_count;
    protected String name_;
    protected String id_;
    protected String profile_image_url;
    protected String screen_name;
    protected String description;

    public int getFollowers_count() {
        return followers_count;
    }

    public int getFriends_count() {
        return friends_count;
    }

    public String getName_() {
        return name_;
    }

    public String getId_() {
        return id_;
    }


    //    protected String profile_background_color;
//    protected String profile_background_image_url;
//    protected String profile_banner_url;
//    protected String profile_link_color;
//    protected String profile_sidebar_border_color;
//    protected String profile_sidebar_fill_color;
//    protected String profile_text_color;
//    protected List<Tweet> status_;
//    protected int statuses_count;
//    protected time_zone String;
//    protected String url;
//    protected boolean verified;

    public String getDescription() {
        return description;
    }

    public User(JSONObject raw) {
        try {
            profile_image_url = raw.getString("profile_image_url");
            screen_name = raw.getString("screen_name");
            name_ = raw.getString("name");
            id_ = raw.getString("id_str");
            followers_count = raw.getInt("followers_count");
            friends_count = raw.getInt("friends_count");
            description = raw.getString("description");
            if (description == null || description.equals("null")) {
                description = "";
            }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.followers_count);
        dest.writeInt(this.friends_count);
        dest.writeString(this.name_);
        dest.writeString(this.id_);
        dest.writeString(this.profile_image_url);
        dest.writeString(this.screen_name);
        dest.writeString(this.description);
    }

    protected User(Parcel in) {
        this.followers_count = in.readInt();
        this.friends_count = in.readInt();
        this.name_ = in.readString();
        this.id_ = in.readString();
        this.profile_image_url = in.readString();
        this.screen_name = in.readString();
        this.description = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
