package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {
    TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    SwipeRefreshLayout swipeContainer;
    MenuItem miActionProgressItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }
        });
        // find the recycler view
        rvTweets = findViewById(R.id.rvTweet);

        // instatiate arraylist
        tweets = new ArrayList<Tweet>();

        // construct adapter from the source
        tweetAdapter = new TweetAdapter(tweets);

        // Recycler View setup (layout manager and adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));

        // set the adapter
        rvTweets.setAdapter(tweetAdapter);
        populateTimeline();
    }

    private void populateTimeline(){
//        showProgressBar();
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                Log.d("TwitterClient", response.toString());
                // iterate through entry and convert objects to tweets
                for (int i = 0; i < response.length(); i++){
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }

                }
//                hideProgressBar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
                hideProgressBar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
//                hideProgressBar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
//                hideProgressBar();
            }
        });
    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        showProgressBar();
        client.getHomeTimeline(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Remember to CLEAR OUT old items before appending in the new ones
                tweetAdapter.clear();

                for (int i = 0; i < response.length(); i++){
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                // ...the data has come back, add new items to your adapter...
                tweetAdapter.addAll(tweets);
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
                hideProgressBar();
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    public void onComposeAction(MenuItem mi){
        // Use intents to pass the view to the compose activity
        Intent i = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivityForResult(i, 20);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // check request code and result code first

        // Use data parameter
        Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
        tweets.add(0, tweet);
        tweetAdapter.notifyItemInserted(0);
        rvTweets.scrollToPosition(0);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }
}
