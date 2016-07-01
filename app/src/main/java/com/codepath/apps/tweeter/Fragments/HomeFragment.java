package com.codepath.apps.tweeter.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.TwitterApplication;
import com.codepath.apps.tweeter.TwitterClient;
import com.codepath.apps.tweeter.adapter.TweetsAdapter;
import com.codepath.apps.tweeter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by yiblet on 6/28/16.
 */
public class HomeFragment extends Fragment {
//    private String title;
//    private int page;

    String url;
    private String TAG = "HomeFragment";

    protected TwitterClient client;
    ArrayList<Tweet> tweets;
    TweetsAdapter adapter;
    SwipeRefreshLayout srRefresh;

    public RecyclerView getRvTweets() {
        return rvTweets;
    }

    @BindView(R.id.rvTweets) RecyclerView rvTweets;

    public ArrayList<Tweet> getTweets() {
        return tweets;
    }

    public TweetsAdapter getAdapter() {
        return adapter;
    }

    // newInstance constructor for creating fragment with arguments
    public static HomeFragment newInstance(String timeline) {
        HomeFragment fragmentHome = new HomeFragment();
        Bundle args = new Bundle();
//        args.putInt("someInt", page);
//        args.putString("someTitle", title);
        args.putString("url", timeline);
        fragmentHome.setArguments(args);
        return fragmentHome;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        page = getArguments().getInt("someInt", 0);
//        title = getArguments().getString("someTitle");
        tweets = new ArrayList<Tweet>();
        url = getArguments().getString("url");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: " + "sucesess");

        rvTweets = (RecyclerView) view.findViewById(R.id.rvTweets);
        srRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srRefresh);

        client = TwitterApplication.getRestClient();
        adapter = new TweetsAdapter(view.getContext(), tweets);
        rvTweets.setAdapter(adapter);
        rvTweets.setLayoutManager(new LinearLayoutManager(view.getContext()));

        srRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tweets.clear();
                client.getTimeline(url, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        handler.onSuccess(statusCode, headers, response);
                        srRefresh.setRefreshing(false);
                    }
                });

            }
        });
        populateTimeline();
    }

    protected JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//            Log.d(TAG, "onSuccess: successs at " + url);
//            Log.d(TAG, "response : " + response.toString());

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
            Log.d(TAG, "onFailure: failure at " + url);
            super.onFailure(statusCode, headers, throwable, errorResponse);
        }
    };

    protected void populateTimeline() {
        client.getTimeline(url, handler);
    }
}

