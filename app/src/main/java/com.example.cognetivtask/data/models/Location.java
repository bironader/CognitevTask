package com.example.cognetivtask.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Location {

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("crossStreet")
    @Expose
    private String cross_street;

    @SerializedName("lat")
    @Expose
    private double lat;

    @SerializedName("lng")
    @Expose
    private double lng;

    @SerializedName("formattedAddress")
    @Expose
    private List<String> formatted_address;

}
