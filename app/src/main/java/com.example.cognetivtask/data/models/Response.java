package com.example.cognetivtask.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    @SerializedName("venues")
    @Expose
    public List<Place> places;
}
