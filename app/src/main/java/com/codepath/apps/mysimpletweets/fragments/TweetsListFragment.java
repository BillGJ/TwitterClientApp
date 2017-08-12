package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.utils.EndlessScrollListener;

import java.util.ArrayList;


public class TweetsListFragment extends Fragment{

    protected TwitterClient client;
    private ArrayList<Tweet> tweets;
    private TweetsArrayAdapter aTweets;
    private ListView lvTweets;
    protected User user;
    protected SwipeRefreshLayout swipeContainer;

    // inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);

        // find list view
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        // connect adapter to list view
        lvTweets.setAdapter(aTweets);

        // add endless scroll listener
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // find the oldest tweet id
                long oldestId = getOldestTweetId();

                // populate timeline with tweets before the tweet with the oldest Id
                if(TweetsListFragment.this instanceof HomeTimelineFragment) {
                    ((HomeTimelineFragment) TweetsListFragment.this).populateTimeline(oldestId);

                } else if(TweetsListFragment.this instanceof MentionsTimelineFragment) {
                    ((MentionsTimelineFragment) TweetsListFragment.this).populateTimeline(oldestId);

                } else if(TweetsListFragment.this instanceof UserTimelineFragment) {
                 //   Toast.makeText(TweetsListFragment.this,"ok", Toast.LENGTH_SHORT).show();
                    ((UserTimelineFragment) TweetsListFragment.this).populateTimeline(user.getScreenName(), oldestId);
                }

                return true;
            }
        });


        // Lookup the Swipe Container view//
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        //Listen for Swipe Refresh to fetch Movies again
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Make sure to call swipeContainer.setRefreshing(false) once the
                // network has completed successfully
                //clear list before refreshing

                if(TweetsListFragment.this instanceof HomeTimelineFragment) {
                    ((HomeTimelineFragment) TweetsListFragment.this).clear();
                    ((HomeTimelineFragment) TweetsListFragment.this).populateTimeline(Long.parseLong("0"));

                } else if(TweetsListFragment.this instanceof MentionsTimelineFragment) {
                    ((MentionsTimelineFragment) TweetsListFragment.this).clear();
                    ((MentionsTimelineFragment) TweetsListFragment.this).populateTimeline(Long.parseLong("0"));

                } else if(TweetsListFragment.this instanceof UserTimelineFragment) {
                    ((UserTimelineFragment) TweetsListFragment.this).clear();
                    ((UserTimelineFragment) TweetsListFragment.this).populateTimeline(user.getScreenName(), Long.parseLong("0"));
                }
            }
        });

        return v;
    }


    // creation lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = TwitterApplication.getRestClient();
        // create the array list (data source)
        tweets = new ArrayList<>();
        // construct the adapter from data source
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    }

    public void addAll(ArrayList<Tweet> tweets, long oldestId) {
        // clear list if populating for the first time, or on refresh
        if(oldestId == 0) {
            //aTweets.clear();
        }
        aTweets.addAll(tweets);
        aTweets.notifyDataSetChanged();
        //Log.d("DEBUG", tweets.toString());

        // scroll to first element in list view
        if (oldestId == 0)
            lvTweets.setSelectionAfterHeaderView();
    }

    public void clear() {
        aTweets.clear();
    }

    public void addTweetInTimeline(Tweet tweet) {
        // add it on top of tweets array list, before the tweet at index 0
        tweets.add(0, tweet);
        aTweets.notifyDataSetChanged();
        lvTweets.setSelectionAfterHeaderView();
    }

    // get oldest tweet id in the ArrayList tweets to search for other tweets before
    // this last one
    public Long getOldestTweetId() {
        // if we want to sort the tweets first

        return tweets.get(tweets.size()-1).getUid();
    }

}