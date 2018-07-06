package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class User {
    public String name;
    public long UID;
    public String screenName;
    public String profileImageUrl;
    public int followingCount;
    public int followerCount;

    public User() {}

    // deserialize the JSON
    public static User fromJSON(JSONObject jsonObject) throws JSONException{
        User user = new User();
        user.name = jsonObject.getString("name");
        user.UID = jsonObject.getLong("id");
        user.screenName = jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url");
        user.followingCount = jsonObject.getInt("friends_count");
        user.followerCount = jsonObject.getInt("followers_count");
        return user;
    }

}
