package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.parceler.Parcels;

import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class DetailsActivity extends AppCompatActivity {
    TextView tvUsername;
    TextView tvBody;
    TextView tvCreatedAt;
    ImageView ivProfile;
    ImageView ivHeart;
    ImageView ivRetweet;
    ImageView ivMedia;
    TwitterClient client;
    Context context;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        context = this;

        client = TwitterApp.getRestClient(this);

        //Binding
        tvUsername = findViewById(R.id.tvUsername);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvBody = findViewById(R.id.tvBody);
        ivProfile = findViewById(R.id.ivProfilePicture);
        ivHeart = findViewById(R.id.ivHeart);
        ivRetweet = findViewById(R.id.ivRetweet);
        ivMedia = findViewById(R.id.ivMedia);
        final Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));


        ivHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.favoriteTweet(tweet.UID, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Glide.with(context).load(R.drawable.ic_vector_heart).into(ivHeart);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(context, "failure", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        ivRetweet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                client.retweet(tweet.UID, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Glide.with(context).load(R.drawable.ic_vector_retweet).into(ivRetweet);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(context, "success", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        Glide.with(this).load(tweet.user.profileImageUrl).into(ivProfile);

        tvUsername.setText(tweet.user.name);
        tvBody.setText(tweet.body);
        tvCreatedAt.setText(getRelativeTimeAgo(tweet.createdAt));

        if(tweet.imageUrl != null){
            Glide.with(this).load(tweet.imageUrl).into(ivMedia);
        }
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
