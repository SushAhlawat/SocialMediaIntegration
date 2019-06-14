package com.sushmitaahlawat.detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;

public class FbActivity extends AppCompatActivity {
    private CircleImageView circleImageView;
    private TextView Name;
    private TextView emailId;
    public static  final String FIRST_NAME = "firstname";
    public static  final String LAST_NAME = "lastname";
    public static  final String USER_PROFILE = "profileImage";
    public static  final String USER_EMAIL = "Email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secnd);
        Name = (TextView)findViewById(R.id.name);
        circleImageView = (CircleImageView)findViewById(R.id.profile);
        emailId = (TextView)findViewById(R.id.email);

        String firstname = getIntent().getStringExtra(FIRST_NAME);
        String lastname = getIntent().getStringExtra(LAST_NAME);
        String profilepic = getIntent().getStringExtra(USER_PROFILE);
        String email = getIntent().getStringExtra(USER_EMAIL);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.dontAnimate();

        Glide.with(FbActivity.this).load(profilepic).into(circleImageView);
        Name.setText(firstname + " " + lastname);
        emailId.setText(email);
    }


}
