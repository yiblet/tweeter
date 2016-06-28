package com.codepath.apps.tweeter.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.models.Tweet;
import com.codepath.apps.tweeter.models.User;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yiblet on 6/28/16.
 */
public class TweetView extends FrameLayout {
    @BindView(R.id.tvName) TextView tvName;
    @BindView(R.id.tvScreenName) TextView tvScreenName;
    @BindView(R.id.ivProfile) ImageView ivProfile;
    @BindView(R.id.tvTimeSince) TextView tvTimeSince;
    @BindView(R.id.tvTweetText) TextView tvTweetText;
    Tweet tweet;
    User user;


    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
        this.user = tweet.getUser();

        tvName.setText(user.getName());
        tvScreenName.setText("@" + user.getScreen_name());
        Picasso.with(getContext()).load(user.getProfile_image_url()).into(ivProfile);
        tvTweetText.setText(tweet.getText());
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
        ButterKnife.bind(this);
    }
}
