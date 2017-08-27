package com.example.vivekgoel.zumperproject.ServerConnection;

import com.example.vivekgoel.zumperproject.DataModel.AllRestaurants.RestaurantFeed;
import com.example.vivekgoel.zumperproject.DataModel.RestaurantDetails.Details;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by vivekgoel on 05/01/17.
 */

//APIKEY: AIzaSyB-bpw0ollWA5AKpT11Y2CL2qPFs4kC_dk

public interface APIInterface {

    @GET("/maps/api/place/textsearch/json")
    Call<RestaurantFeed> doGetNextPageRestaurantDetails(@QueryMap Map<String, String> options);


    @GET("/maps/api/place/textsearch/json")
    Call<RestaurantFeed> doGetRestaurantDetails(@QueryMap Map<String, String> options);

    @GET("/maps/api/place/details/json")
    Call<Details> doGetDetails(@QueryMap Map<String, String> options);

}
