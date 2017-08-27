package com.example.vivekgoel.zumperproject.DataModel.RestaurantDetails;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vivekgoel on 8/26/17.
 */

public class Result implements Serializable {
    private String formatted_phone_number;
    private String international_phone_number;
    private List<Reviews> reviews;
    private List<Photos> photos;

    public String getFormatted_phone_number() {
        return formatted_phone_number;
    }

    public String getInternational_phone_number() {
        return international_phone_number;
    }

    public List<Reviews> getReviews() {
        return reviews;
    }

    public List<Photos> getPhotos() {
        return photos;
    }
}
