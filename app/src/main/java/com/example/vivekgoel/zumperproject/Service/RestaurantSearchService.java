package com.example.vivekgoel.zumperproject.Service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.vivekgoel.zumperproject.DataModel.AllRestaurants.RestaurantFeed;
import com.example.vivekgoel.zumperproject.ServerConnection.APIClient;
import com.example.vivekgoel.zumperproject.ServerConnection.APIInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vivekgoel on 8/18/17.
 */

public class RestaurantSearchService extends IntentService {
    APIInterface apiInterface = APIClient.getClient().create(APIInterface.class);;

    public RestaurantSearchService() {
        super("RestaurantSearchService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        /**
         GET List Items
         **/

            Map<String, String> map = new HashMap<>();
            map.put("query","restaurants+in+San Francisco");
            map.put("key","AIzaSyDQeeDtLLkQZPmvi_9NNS40zfdg7FbyT44");

        getData(1,map);
    }

    public void getData(int pageNumber, Map<String,String> map) {

        /**
         GET List Restaurants
         **/
        Call<RestaurantFeed> call = apiInterface.doGetRestaurantDetails(map);
        call.enqueue(new Callback<RestaurantFeed>() {

            @Override
            public void onResponse(Call<RestaurantFeed> call, Response<RestaurantFeed> response) {
                RestaurantFeed restaurantFeed = response.body();

                publishResults(restaurantFeed);
            }

            @Override
            public void onFailure(Call<RestaurantFeed> call, Throwable t) {
                call.cancel();
            }
        });
    }

    //Sending broadcast
    private void publishResults(RestaurantFeed restaurantFeed) {
        Intent newIntent = new Intent();
        newIntent.setAction("vivek.goel.DATA_LOADED");
        newIntent.putExtra("restaurantFeed",restaurantFeed);
        sendBroadcast(newIntent);
    }
}
