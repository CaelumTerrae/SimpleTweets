package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Tweet {

    public String body;
    public long UID;
    public User user;
    public String createdAt;
    public String imageUrl;
    public boolean hasMedia;

    public Tweet(){}


    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException{
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.UID = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));


        JSONObject entities = jsonObject.getJSONObject("entities");
        JSONArray media = entities.getJSONArray("media");
        tweet.hasMedia = media != null;
        if(tweet.hasMedia){
            //iterate over the media to find an image;
            for (int i = 0; i < media.length(); i++){
                String type = media.getJSONObject(i).getString("type");
                if(type.equals("photo")){
                    tweet.imageUrl = media.getJSONObject(i).getString("media_url");
                }
            }
        }else{
            tweet.imageUrl = null;
        }
        return tweet;
    }


}
