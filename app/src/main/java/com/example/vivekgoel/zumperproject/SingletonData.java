package com.example.vivekgoel.zumperproject;

import com.example.vivekgoel.zumperproject.DataModel.AllRestaurants.Restaurants;

import java.util.ArrayList;

/**
 * Created by vivekgoel on 8/26/17.
 */

public class SingletonData {
    private String next_page_token;
    private ArrayList<Restaurants> restaurants;
    private static SingletonData data = new SingletonData( );

    private SingletonData() { }

    public static SingletonData getInstance( ) {
        return data;
    }

    public void updateRestaurants(ArrayList<Restaurants> results) {
        if(restaurants != null)
            restaurants.clear();
        restaurants = results;
    }

    public String getNext_page_token() {
        return next_page_token;
    }

    public ArrayList<Restaurants> getRestaurants() {
        return restaurants;
    }

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }
}
