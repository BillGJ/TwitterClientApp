package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;




public class UserTimelineFragment extends TweetsListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user = (User) getArguments().getSerializable("user");
        String screenName = getArguments().getString("screen_name");

        if (user != null) {
            populateTimeline(user.getScreenName(), Long.parseLong("0"));
        } else {
            populateTimeline(screenName, Long.parseLong("0"));
        }

    }

    public static UserTimelineFragment newInstance(User user) {
        UserTimelineFragment fg = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fg.setArguments(args);

        return fg;
    }

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment fg = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putSerializable("screen_name", screenName);
        fg.setArguments(args);

        return fg;
    }

    // send an API request to get the timeline json
    // fill the listview by creating the tweet objects from the json
    protected void populateTimeline(String screenName, final Long oldestId) {
        client.getUserTimeline(screenName, oldestId, new JsonHttpResponseHandler() {
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
                    Toast.makeText(getActivity(), "Could not connect to internet, please verify your connection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
