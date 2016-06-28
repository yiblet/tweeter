package com.codepath.apps.tweeter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.tweeter.adapter.TweetAdapter;
import com.codepath.apps.tweeter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    ArrayList<Tweet> tweets = new ArrayList<Tweet>();
    TweetAdapter adapter;

    @BindView(R.id.lvTweets) ListView lvTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);

        lvTweets = (ListView) findViewById(R.id.lvTweets);

        client = TwitterApplication.getRestClient();
        adapter = new TweetAdapter(this, tweets);
        lvTweets.setAdapter(adapter);
        populateTimeline();
    }

    private void populateTimeline() {
        // sends api request

        client.getHomeTimeLine(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Toast.makeText(TimelineActivity.this, "success!", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(TimelineActivity.this, "failure!", Toast.LENGTH_SHORT).show();
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
