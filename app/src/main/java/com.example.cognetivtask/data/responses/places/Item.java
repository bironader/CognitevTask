package com.example.cognetivtask.data.responses.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    public Place getPlace() {
        return place;
    }

    @SerializedName("venue")
    @Expose
    private Place place;
}
