package com.codepath.apps.mysimpletweets.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ebillson GJ on 8/3/2017.
 */

//Parse the Json + Store the Data, Encapsulate state logic or display logic


public class Tweet implements Serializable {

    //List out the attributes
    private  String body;
    private  long uid;// unique id for the user
    private User user;// store embedded user object
    private String createdAt;
    private ArrayList<String> photoUrls;
    private Integer retweetCount;
    private Integer favoriteCount;

    public ArrayList<String> getPhotoUrls() {
        return photoUrls;
    }

    public Integer getRetweetCount() {
        return retweetCount;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }



    public String getBody() {
        return body;
    }


    public User getUser() {
        return user;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    //Deserialize the JSON and built tweet objects
    //Tweet.fromJSON(""(....))==> <Tweet>

    public static Tweet fromJSON(JSONObject jsonObject){

        Tweet tweet = new Tweet();
        // Extract the values from the json, store them

        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid= jsonObject.getLong("id");
            tweet.createdAt= jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));


            // retweet and favorite count
            tweet.retweetCount = jsonObject.getInt("retweet_count");
            tweet.favoriteCount = jsonObject.getInt("favorite_count");

            // take tweet media if Json has entities and media
            tweet.photoUrls = new ArrayList<>();
            if (jsonObject.has("entities") && jsonObject.getJSONObject("entities").has("media")) {
                JSONArray medias = jsonObject.getJSONObject("entities").getJSONArray("media");
                for (int i = 0; i < medias.length(); i++) {
                    JSONObject media = medias.getJSONObject(i);

                    if (media.getString("type").equals("photo")) {
                        tweet.photoUrls.add(media.getString("media_url"));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Return the tweet object
        return tweet;

    }

    //tweets.fromJSONArray({[...], [...])} => List<Tweet>
    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray){

        ArrayList<Tweet> tweets = new ArrayList<>();

        //Iterate the Json Array and create tweets
        for (int i = 0 ; i<jsonArray.length(); i++){

            try {
                JSONObject tweetJson = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(tweetJson);
                if (tweet != null){

                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }

        //return the finished list
        return tweets;


    }

}
