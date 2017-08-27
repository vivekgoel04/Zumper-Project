package com.example.vivekgoel.zumperproject.DataModel.RestaurantDetails;

import java.io.Serializable;

/**
 * Created by vivekgoel on 8/26/17.
 */

public class Reviews implements Serializable {
    private String author_name;
    private String profile_photo_url;
    private int rating;
    private String relative_time_description;
    private String text;



    public String getAuthor_name() {
        return author_name;
    }

    public String getProfile_photo_url() {
        return profile_photo_url;
    }

    public int getRating() {
        return rating;
    }

    public String getRelative_time_description() {
        return relative_time_description;
    }

    public String getText() {
        return text;
    }

}
