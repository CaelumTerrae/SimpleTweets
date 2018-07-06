package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApp.getRestClient(this);
        findViewById(R.id.btnSend).setOnClickListener(new handleOnClick());
        String replyTo = getIntent().getStringExtra("replyTo");
        if(replyTo != null) {
            EditText etTweet = findViewById(R.id.etTweet);
            etTweet.append("@" + replyTo);
        }
    }

    private class handleOnClick implements View.OnClickListener {
        public void onClick(View view) {
            EditText etBody = (EditText) findViewById(R.id.etTweet);
            String message = etBody.getText().toString();

            client.sendTweet(message, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response){
                    Log.d("TwitterClient", response.toString());
                    try {
                        Tweet tweet = Tweet.fromJSON(response);
                        Intent i = new Intent();
                        i.putExtra("tweet", Parcels.wrap(tweet));
                        setResult(RESULT_OK, i);
                        finish();
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    Log.d("TwitterClient", response.toString());
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("TwitterClient", errorResponse.toString());
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    Log.d("TwitterClient", errorResponse.toString());
                    throwable.printStackTrace();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.d("TwitterClient", responseString);
                    throwable.printStackTrace();
                }
            });
        }
    }

}
