package com.example.vivekgoel.zumperproject.DataModel.AllRestaurants;

import java.io.Serializable;
import java.util.List;

public class RestaurantFeed implements Serializable {

    private String next_page_token;
    private List<Restaurants> results;

    public String getNext_page_token() {
        return next_page_token;
    }

    public List<Restaurants> getRestaurants() {
        return results;
    }
}
