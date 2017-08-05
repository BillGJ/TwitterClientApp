package com.codepath.apps.mysimpletweets.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Ebillson GJ on 8/3/2017.
 */

public class User implements Serializable {

    //list attributes
    private  String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private String tagLine;
    private int followerCount;
    private int friendCount;
    private int statusCount;

    public String getTagLine() {
        return tagLine;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public int getFriendCount() {
        return friendCount;
    }

    public int getStatusCount() {
        return statusCount;
    }




    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }


    //Deserialize the user json => User
    public static User fromJSON(JSONObject json){
        User u = new User();
        //extract and fill the values

        try {
                u.name = json.getString("name");
                u.uid = json.getLong("id");
                u.screenName = json.getString("screen_name");
                u.profileImageUrl = json.getString("profile_image_url");
                u.tagLine = json.getString("description");
                u.followerCount = json.getInt("followers_count");
                u.friendCount = json.getInt("friends_count");
                u.statusCount = json.getInt("statuses_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //return a user
        return u;


    }
}
