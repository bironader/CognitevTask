package com.example.cognetivtask.data.responses.places;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    public List<Group> getGroups() {
        return groups;
    }

    @SerializedName("groups")
    @Expose
    private List<Group> groups;

}
