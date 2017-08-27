package com.example.vivekgoel.zumperproject.DataModel.AllRestaurants;



import java.io.Serializable;

public class Restaurants implements Serializable {

    private String formatted_address;

    private Geometry geometry;

    private String icon;
    private String id;
    private String name;

    private OpeningHours opening_hours;

    private String place_id;
    private int price_level;
    private float rating;


    public String getFormatted_address() {
        return formatted_address;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getIcon() {
        return icon;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public OpeningHours getOpening_hours() {
        return opening_hours;
    }

    public String getPlace_id() {
        return place_id;
    }

    public int getPrice_level() {
        return price_level;
    }

    public float getRating() {
        return rating;
    }
}
