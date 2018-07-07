package com.codepath.apps.restclienttemplate.models;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;

import org.parceler.Parcels;

public class ProfileActivity extends AppCompatActivity {
    ImageView ivProfilePicture;
    TextView tvFollowers;
    TextView tvFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        User user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        // Link the views and their ID's
        ivProfilePicture = findViewById(R.id.ivProfilePicture);
        tvFollowers = findViewById(R.id.tvFollowers);
        tvFollowing = findViewById(R.id.tvFollowing);

        //load the number of followers into the followers and following.


        String followerCount = Integer.toString(user.followerCount) + " followers";
        tvFollowers.setText(followerCount);
        String followingCount = Integer.toString(user.followingCount) + " following";
        tvFollowing.setText(followingCount);
        Glide.with(this).load(user.profileImageUrl).into(ivProfilePicture);

    }
}
