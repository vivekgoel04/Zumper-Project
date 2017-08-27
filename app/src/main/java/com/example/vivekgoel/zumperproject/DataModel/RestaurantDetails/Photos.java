package com.example.vivekgoel.zumperproject.DataModel.RestaurantDetails;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vivekgoel on 8/27/17.
 */

public class Photos implements Serializable {
    private List<String> html_attributions;
    private String photo_reference;

    public List<String> getHtml_attributions() {
        return html_attributions;
    }

    public String getPhoto_reference() {
        return photo_reference;
    }
}
