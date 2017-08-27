package com.example.vivekgoel.zumperproject.DataModel.AllRestaurants;

import java.io.Serializable;

/**
 * Created by vivekgoel on 8/26/17.
 */

public class Location implements Serializable {
    private Double lat;
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
