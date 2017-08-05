package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class HomeTimelineFragment extends TweetsListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        populateTimeline(Long.parseLong("0"));

    }

    // send an API request to get the timeline json
    // fill the listview by creating the tweet objects from the json
    protected void populateTimeline(final Long oldestId) {
        client.getHomeTimeline(oldestId, new JsonHttpResponseHandler() {
            // On SUCCESS
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //Log.d("DEBUG", response.toString());
                addAll(Tweet.fromJSONArray(response), oldestId);

                // Call swipeContainer.setRefreshing(false) to signal refresh has ended
                swipeContainer.setRefreshing(false);
            }

            // On FAILURE
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //Log.d("DEBUG", errorResponse.toString());
                if (throwable.getMessage().contains("resolve host") || throwable.getMessage().contains("failed to connect")) {
                    Toast.makeText(getActivity(),
                            "Could not connect to internet, please verify your connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

