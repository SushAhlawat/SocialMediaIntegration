package com.sushmitaahlawat.detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class TwitterActivity extends AppCompatActivity {


    TextView uname;
    CircleImageView profile;
    TextView email;
    TextView des;
    public static  final String TWITTER_USER_NAME = "username";
    public static  final String TWITTER_USER_PROFILE = "profileImage";
    public static  final String TWITTER_USER_FOLLOWERS = "followers";
    public static  final String TWITTER_USER_DESC = "description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter);
        String username = getIntent().getStringExtra(TWITTER_USER_NAME);
        String profilepic = getIntent().getStringExtra(TWITTER_USER_PROFILE);
        String emailId = getIntent().getStringExtra(TWITTER_USER_FOLLOWERS);
        String description = getIntent().getStringExtra(TWITTER_USER_DESC);
        uname = (TextView) findViewById(R.id.TV_username);
        profile = (CircleImageView) findViewById(R.id.profile);
        des = (TextView)findViewById(R.id.des);
        email= (TextView) findViewById(R.id.email);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();

        Glide.with(TwitterActivity.this).load(profilepic).into(profile);
        uname.setText(username);
        email.setText(emailId);
        des.setText(description);
    }
}
