package com.codepath.apps.tweeter.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.TimeFormatter;
import com.codepath.apps.tweeter.TwitterApplication;
import com.codepath.apps.tweeter.TwitterClient;
import com.codepath.apps.tweeter.activities.ComposeActivity;
import com.codepath.apps.tweeter.activities.ProfileActivity;
import com.codepath.apps.tweeter.activities.TimelineActivity;
import com.codepath.apps.tweeter.models.Tweet;
import com.codepath.apps.tweeter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by yiblet on 6/28/16.
 */
public class TweetView extends FrameLayout {
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvScreenName) TextView tvScreenName;
    @BindView(R.id.ivProfile) ImageView ivProfile;
    @BindView(R.id.tvTimeSince) TextView tvTimeSince;
    @BindView(R.id.tvTweetText) TextView tvTweetText;
    @BindView(R.id.tvRetweet) TextView tvRetweet;
    @BindView(R.id.tvRetweetSign) TextView tvRetweetSign;
    @BindView(R.id.ibReply) ImageButton ibReply;
    @BindView(R.id.ibFavorite) ImageButton ibFavorite;
    @BindView(R.id.ibRetweet) ImageButton ibRetweet;
    TwitterClient client = TwitterApplication.getRestClient();
    Tweet tweet;
    User user;
    boolean isRetweet;


    private void setRetweetVisible() {
        tvRetweet.setVisibility(VISIBLE);
        tvRetweetSign.setVisibility(VISIBLE);
    }

    private void setTvRetweetGone() {
        tvRetweet.setVisibility(GONE);
        tvRetweetSign.setVisibility(GONE);
    }

    public void setTweet(Tweet tweet) {
        if (tweet.isRetweet()) {
            setRetweetVisible();
            tvRetweet.setText(tweet.getUser().getName() + " Retweeted");
            this.tweet = tweet.getRetweeted_status();
        } else {
            this.tweet = tweet;
            setTvRetweetGone();
        }
        this.user = this.tweet.getUser();
        tvName.setText(user.getName());
        tvScreenName.setText("@" + user.getScreen_name());
        Picasso.with(getContext()).load(user.getProfile_image_url())
                .transform( new RoundedCornersTransformation(5, 0))
                .into(ivProfile);
        tvTweetText.setText(this.tweet.getText());
        tvTimeSince.setText(TimeFormatter.getTimeDifference(tweet.getCreated_at()));
//        setRetweeted(tweet.isRetweeted());
//        setFavorited(tweet.isFavorited());
    }

    private void setFavorited(boolean favorited) {
        tweet.setFavorited(favorited);
        int i;
        if (favorited) {
            i = R.drawable.favorited;
        } else {
            i = R.drawable.favorite;
        }
        Picasso.with(getContext()).load(i).into(ibFavorite);
    }

    private void setRetweeted(boolean retweeted) {
        tweet.setRetweeted(retweeted);
        int i;
        if (retweeted) {
            i = R.drawable.retweeted;
        } else {
            i = R.drawable.retweet;
        }
        Picasso.with(getContext()).load(i).into(ibRetweet);
    }


    public TweetView(Context context) {
        super(context);
        init();
    }

    public TweetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TweetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_tweet, this);
        ButterKnife.bind(this);
        tvName = (TextView) findViewById(R.id.tvName);
        tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        tvTimeSince = (TextView) findViewById(R.id.tvTimeSince);
        tvTweetText = (TextView) findViewById(R.id.tvTweetText);
        tvRetweet = (TextView) findViewById(R.id.tvRetweet);
        tvRetweetSign = (TextView) findViewById(R.id.tvRetweetSign);
        ibReply = (ImageButton) findViewById(R.id.ibReply);

        ivProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), ProfileActivity.class);
                i.putExtra("user", user);
                getContext().startActivity(i);
            }
        });

        ibReply.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent((Activity) getContext(), ComposeActivity.class);
                i.putExtra("user", user);
                i.putExtra("reply", tweet.getId_str());
                ((Activity) getContext()).startActivityForResult(i, TimelineActivity.RETWEET_REQUEST);
            }
        });
        ibRetweet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setRetweeted(!tweet.isRetweeted(), true);

            }
        });

    }
    private void setRetweeted(boolean b, boolean b1) {
        setRetweeted(b);
        if (b1) {
            if (b) {
                client.postRetweet(tweet.getId_str(), new JsonHttpResponseHandler());
            } else {
                client.postUnretweet(tweet.getId_str(), new JsonHttpResponseHandler());
            }
        }
    }

}
