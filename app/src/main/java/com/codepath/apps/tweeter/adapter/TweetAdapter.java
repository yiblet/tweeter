package com.codepath.apps.tweeter.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.codepath.apps.tweeter.models.Tweet;

import java.util.List;

/**
 * Created by yiblet on 6/27/16.
 */
public class TweetAdapter extends ArrayAdapter<Tweet> {

    public TweetAdapter(Context context, List<Tweet> tweets) {
        super(context, android.R.layout.simple_list_item_1, tweets);
    }

    public TweetAdapter(Context context, int resource) {
        super(context, resource);
    }

    public TweetAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public TweetAdapter(Context context, int resource, Tweet[] objects) {
        super(context, resource, objects);
    }

    public TweetAdapter(Context context, int resource, int textViewResourceId, Tweet[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public TweetAdapter(Context context, int resource, List<Tweet> objects) {
        super(context, resource, objects);
    }

    public TweetAdapter(Context context, int resource, int textViewResourceId, List<Tweet> objects) {
        super(context, resource, textViewResourceId, objects);
    }
}
