package com.example.vivekgoel.zumperproject.DataModel.AllRestaurants;

import java.io.Serializable;

/**
 * Created by vivekgoel on 8/26/17.
 */

public class OpeningHours implements Serializable {
    private boolean open_now;

    public boolean isOpen_now() {
        return open_now;
    }

}
