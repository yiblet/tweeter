package com.codepath.apps.tweeter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.TwitterApplication;
import com.codepath.apps.tweeter.TwitterClient;
import com.codepath.apps.tweeter.adapter.TweetsAdapter;
import com.codepath.apps.tweeter.models.Tweet;
import com.codepath.apps.tweeter.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ProfileActivity extends AppCompatActivity {
    @BindView(R.id.tvFollowers)
    TextView tvFollowers;
    @BindView(R.id.tvFollowing) TextView tvFollowing;
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvScreenName) TextView tvScreenName;
    @BindView(R.id.ivProfile)
    ImageView ivProfile;
    @BindView(R.id.tvDescription) TextView tvDescription;
    @BindView(R.id.rvTweets) RecyclerView rvTweets;
    User user;
    protected TwitterClient client;
    ArrayList<Tweet> tweets = new ArrayList<Tweet>();
    TweetsAdapter adapter;
//    User currentUser;

    protected AsyncHttpResponseHandler handler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//            Log.d(TAG, "onSuccess: successs at " + url);

            for (int i = 0; i < response.length(); i++) {
                try {
                    tweets.add(new Tweet(response.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                Toast.makeText(getContext(), "failure!", Toast.LENGTH_SHORT).show();
//            Log.d(TAG, "onFailure: failure at " + url);
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        user = getIntent().getParcelableExtra("user");
        ButterKnife.bind(this);
        tvFollowers.setText(intToString(user.getFollowers_count()));
        tvFollowing.setText(intToString(user.getFriends_count()));
        tvName.setText(user.getName());
        tvDescription.setText(user.getDescription());
        tvScreenName.setText("@" + user.getScreen_name());
        Picasso.with(this).load(user.getProfile_image_url())
                .transform(new RoundedCornersTransformation(4, 0))
                .into(ivProfile);

        client = TwitterApplication.getRestClient();
        adapter = new TweetsAdapter(this, tweets);
        rvTweets.setAdapter(adapter);
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setNestedScrollingEnabled(false);
        populateTimeline();
    }

    private void populateTimeline() {
        Log.e("id", "populateTimeline: " + user.getId_() );
        client.getUserTimeline(user.getId_(), handler);
    }

    String intToString(int val) {
        Log.d("Profile", "intToString: " + val);
        if (val > 1000000) {
            return Long.toString(Math.round((((double)val) / 1000000.0))) + "m";
        } else if (val > 10000) {
            return Long.toString(Math.round((((double)val) / 1000.0))) + "k";
        } else {
            return Integer.toString(val);
        }
    }


}
