package com.codepath.apps.tweeter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.tweeter.R;
import com.codepath.apps.tweeter.TwitterApplication;
import com.codepath.apps.tweeter.TwitterClient;
import com.codepath.apps.tweeter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ComposeActivity extends AppCompatActivity {
    @BindView(R.id.ivProfile) ImageView ivProfile;
    @BindView(R.id.ivClose) ImageView ivClose;
    @BindView(R.id.etText) EditText etText;
    @BindView(R.id.btTweet) Button btTweet;
    @BindView(R.id.tvChars) TextView tvChars;


    static int RESULT_OK = 200;
    boolean isReply = false;
    String in_reply_to_status_id;
    TwitterClient client = TwitterApplication.getRestClient();

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        ButterKnife.bind(this);

        user = (User) getIntent().getParcelableExtra("user");
        if (user != null) {
            Picasso.with(this).load(user.getProfile_image_url())
                .into(ivProfile);
        }

        if (getIntent().hasExtra("reply")) {
            isReply = true;
            in_reply_to_status_id = getIntent().getStringExtra("reply");
            etText.setText("@" + user.getScreen_name() + " ");
        }

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        etText.addTextChangedListener(new TextWatcher() {

            private CharSequence past;
            private boolean undo = false;

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String length = "" + (140 - etText.getText().length());
                tvChars.setText(length);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        etText.requestFocus();
        etText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(140) });
        tvChars.setText("" + (140 - etText.getText().length()));

        btTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                if (isReply) {
                    if (! etText.getText().equals("")) {
                        setResult(RESULT_OK, i);
                        client.postReply(etText.getText().toString(), in_reply_to_status_id, new JsonHttpResponseHandler());
                    } else {
                        setResult(RESULT_CANCELED);
                    }

                } else {
                    if (! etText.getText().equals("")) {
                        i.putExtra("text", etText.getText().toString());
                        setResult(RESULT_OK, i);
                    } else {
                        setResult(RESULT_CANCELED);
                    }
                }

                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }
}
