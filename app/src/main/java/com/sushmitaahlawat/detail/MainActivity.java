package com.sushmitaahlawat.detail;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = "MainActivity";
    private LoginButton loginButton;
    TwitterLoginButton loginButton1;
    private CircleImageView circleImageView;
    private TextView Name;
    private TextView email;
    // Callback registration
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_main);


        //final String EMAIL = "email";
        Name = (TextView) findViewById(R.id.name);
        circleImageView = (CircleImageView) findViewById(R.id.profile);
        email = (TextView) findViewById(R.id.email);
        loginButton1 = (TwitterLoginButton) findViewById(R.id.login_button1);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        // ((LoginButton) loginButton).setReadPermissions(Arrays.asList(EMAIL));
        loginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        // If you are using in a fragment, call loginButton.setFragment(this);


        checkFbLoginStatus();

        callbackManager = CallbackManager.Factory.create();


        loginButton1.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {

                login(result);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(MainActivity.this, "Authentication Failed!", Toast.LENGTH_LONG).show();
            }
        });


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
             //   Intent intent = new Intent(MainActivity.this, FbActivity.class);
               // startActivity(intent);
            }

            @Override
            public void onCancel() {
                // App code
                Log.i(TAG, "on on error");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.i(TAG, "on on error");
            }


        });

    }


    private void init() {
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.consumer_key), getResources().getString(R.string.consumer_secret)))//pass the created app Consumer KEY and Secret also called API Key and Secret
                .debug(true)//enable debug mode
                .build();

        //finally initialize twitter with created configs
        Twitter.initialize(config);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        loginButton1.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                Name.setText("");
                circleImageView.setImageResource(0);
                Toast.makeText(MainActivity.this, "User Logged Out", Toast.LENGTH_LONG).show();
            } else {
                loaduserprofile(currentAccessToken);
            }
        }
    };

    public void loaduserprofile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String firstname = object.getString("first_name");
                    String lastname = object.getString("last_name");
                    String Email = object.getString("email");
                    String id = object.getString("id");

                    String profileImage = "https://graph.facebook.com/" + id + "/picture?type=normal";
                    Intent intent = new Intent(MainActivity.this, FbActivity.class);

                    //Adding the values to intent
                    intent.putExtra(FbActivity.FIRST_NAME, firstname);
                    intent.putExtra(FbActivity.LAST_NAME, lastname);
                    intent.putExtra(FbActivity.USER_PROFILE, profileImage);
                    intent.putExtra(FbActivity.USER_EMAIL, Email);

                    //Starting intent
                    startActivity(intent);

                    //Name.setText(firstname + " " + lastname);
                    //RequestOptions requestOptions = new RequestOptions();
                    //requestOptions.dontAnimate();

                    //Glide.with(MainActivity.this).load(profileImage).into(circleImageView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name, last_name, email, id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void checkFbLoginStatus() {

        if (AccessToken.getCurrentAccessToken() != null) {
           loaduserprofile(AccessToken.getCurrentAccessToken());
        }
    }


    public void login(Result<TwitterSession> result) {
        //Creating a twitter session with result's data
        TwitterSession session = result.data;

        //Getting the username from session
        final String username = session.getUserName();


        //This code will fetch the profile image URL
        //Getting the account service of the user logged in
        TwitterCore.getInstance().getApiClient(session).getAccountService().verifyCredentials(false, true, false).enqueue(new Callback<User>() {
            @Override
            public void failure(TwitterException e) {
                //If any error occurs handle it here
            }

            @Override
            public void success(Result<User> userResult) {
                //If it succeeds creating a User object from userResult.data
                User user = userResult.data;

                //Getting the profile image url
                String profileImage = user.profileImageUrl.replace("_normal", "");
                String followers = String.valueOf(user.followersCount);
                String description = user.description;


                //Creating an Intent
                Intent intent = new Intent(MainActivity.this, TwitterActivity.class);

                //Adding the values to intent
                intent.putExtra(TwitterActivity.TWITTER_USER_NAME, username);
                intent.putExtra(TwitterActivity.TWITTER_USER_PROFILE, profileImage);
                intent.putExtra(TwitterActivity.TWITTER_USER_FOLLOWERS, followers);
                intent.putExtra(TwitterActivity.TWITTER_USER_DESC,description);

                //Starting intent
                startActivity(intent);
            }
        });

    }



}
