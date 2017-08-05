package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.utils.CountFormatter;
import com.codepath.apps.mysimpletweets.utils.ParseRelativeDate;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Ebillson GJ on 8/3/2017.
 */


//taking the tweet objects and turning them into views displayed in the list
public class TweetsArrayAdapter  extends ArrayAdapter<Tweet>{

    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, 0, tweets);
    }

    //Override and setup custom template
    //View holder pattern

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1. Get the tweets

        final Tweet tweet = getItem(position);
        //2. Find or inflate the template

        if (convertView == null ){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);

        }
        //3. Find the subviews to fill with data in the template

        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        ivProfileImage.setImageResource(0);
        TextView tvTimeAgo = (TextView) convertView.findViewById(R.id.tvTimeAgo);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
        TextView tvBody = (TextView)convertView.findViewById(R.id.tvBody);

        // find media view
        ImageView ivMediaPhoto = (ImageView) convertView.findViewById(R.id.ivMediaPhoto);
        ivMediaPhoto.setImageResource(0);

        // retweet and favorite counter
        TextView tvRetweetCount = (TextView) convertView.findViewById(R.id.tvRetweetCount);
        TextView tvFavoriteCount = (TextView) convertView.findViewById(R.id.tvFavoriteCount);

        //4. Populate data into the subviews

        tvUserName.setText(tweet.getUser().getName());
        //add @ to as prefix to screen name
        tvScreenName.setText(String.format("@%s", tweet.getUser().getScreenName()));

        // set abbreviated time ago, ex: 6s, 3m, 8h
        String timeAgo = ParseRelativeDate.getAbbreviatedTimeAgo(tweet.getCreatedAt());
        tvTimeAgo.setText(timeAgo);

        tvBody.setText(tweet.getBody());

        // social counters
        tvRetweetCount.setText(CountFormatter.format(tweet.getRetweetCount()));
        tvFavoriteCount.setText(CountFormatter.format(tweet.getFavoriteCount()));

        // set the images with Picasso
        // set profile image
        String profileImageUrl = tweet.getUser().getProfileImageUrl();
        if(!TextUtils.isEmpty(profileImageUrl)) {
            Picasso.with(getContext()).load(profileImageUrl)
                    .transform(new RoundedCornersTransformation(3, 3))
                    .into(ivProfileImage);
        }

        // set media photo. if there is photos, we take only the 1st photo
        if (tweet.getPhotoUrls().size() > 0) {
            String mediaPhoto = tweet.getPhotoUrls().get(0);
            if(!TextUtils.isEmpty(mediaPhoto)) {
                Picasso.with(getContext()).load(mediaPhoto)
                        .transform(new RoundedCornersTransformation(20, 20))
                        .into(ivMediaPhoto);
            }
        }

        //5. Return the view to be inserted into the list
        // Set Events
        // profile image onclick
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TweetsArrayAdapter.this.getContext(), ProfileActivity.class);
               i.putExtra("user", tweet.getUser());
                TweetsArrayAdapter.this.getContext().startActivity(i);
            }
        });

        // username onclick
        tvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TweetsArrayAdapter.this.getContext(), ProfileActivity.class);
               i.putExtra("user", tweet.getUser());
                TweetsArrayAdapter.this.getContext().startActivity(i);
            }
        });

        return  convertView;
    }
}
