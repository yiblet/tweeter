package com.codepath.apps.tweeter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.tweeter.Fragments.HomeFragment;
import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.TwitterApplication;
import com.codepath.apps.tweeter.TwitterClient;
import com.codepath.apps.tweeter.adapter.TweetsAdapter;
import com.codepath.apps.tweeter.models.Tweet;
import com.codepath.apps.tweeter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    MyPagerAdapter adapterViewPager;
    @BindView(R.id.tbToolbar) Toolbar tbToolbar;
    @BindView(R.id.ptTab) PagerSlidingTabStrip ptTab;
    @BindView(R.id.fab) FloatingActionButton fab;
    TextView title;
    ImageView ivProfile;
    User user;
    TwitterClient client = TwitterApplication.getRestClient();

    static String TAG = "TIMELINEACTIVIT";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        ButterKnife.bind(this);
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        ptTab.setViewPager(vpPager);
        setSupportActionBar(tbToolbar);

        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                title.setText(adapterViewPager.getPageTitle(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_timeline);
        actionBar.setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        title = (TextView) actionBar.getCustomView().findViewById(R.id.tvTitle);
        ivProfile = (ImageView) actionBar.getCustomView().findViewById(R.id.ivProfile);
        title.setText(adapterViewPager.getPageTitle(0));

        final View.OnClickListener toProfile = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TimelineActivity.this, ProfileActivity.class);
                i.putExtra("user", user);
                TimelineActivity.this.startActivity(i);
            }
        };

        client.getCurrentUser(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = new User(response);
                Picasso.with(TimelineActivity.this).load(user.getProfile_image_url()).into(ivProfile);
                ivProfile.setOnClickListener(toProfile);
                setFab();
            }
        });

    }

    public static int FAB_REQUEST = 4013;
    public static int RETWEET_REQUEST = 4014;

    private void setFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
                i.putExtra("user", user);
                startActivityForResult(i, FAB_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FAB_REQUEST && ComposeActivity.RESULT_OK == resultCode) {
            String text = data.getExtras().getString("text");
            client.postTweet(text, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    TweetsAdapter adapter = adapterViewPager.getTimeline().getAdapter();
                    List<Tweet> tweets = adapterViewPager.getTimeline().getTweets();
                    tweets.add(0, new Tweet(response));
                    adapter.notifyItemInserted(0);
                    ((LinearLayoutManager) adapterViewPager.getTimeline().getRvTweets().getLayoutManager()).scrollToPosition(0);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                    super.onFailure(statusCode, headers, responseString, throwable);
                    Log.d(TAG, "onFailure: " + responseString);
                }
            });
        }
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public HomeFragment timeline = HomeFragment.newInstance(TwitterClient.HOME_TIMELINE_ENDPOINT);
        public HomeFragment mentions = HomeFragment.newInstance(TwitterClient.MENTIONS_TIMELINE_ENDPOINT);

        public HomeFragment getTimeline() {
            return timeline;
        }

        public HomeFragment getMentions() {
            return mentions;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return timeline;
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return mentions;
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return  "Home";
                case 1:
                    return "Mentions";
                default:
                    return "" + position;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.menu_timeline, menu);

//        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
    }
}
