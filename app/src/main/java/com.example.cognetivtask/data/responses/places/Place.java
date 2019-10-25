package com.example.cognetivtask.data.responses.places;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.cognetivtask.GlideUtil;
import com.example.cognetivtask.data.responses.photos.PhotoRespone;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.inject.Inject;

public class Place {


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("location")
    @Expose
    private Location location;

    @SerializedName("categories")
    @Expose
    private List<Category> categories;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public String getCategory() {
        return categories.get(0).getName();
    }

    public String getFormattedAddress() {
        return location.getFormatted_address().get(0);
    }

    public void setPhotoRespone(PhotoRespone photoRespone) {
        this.photoRespone = photoRespone;
    }

    private PhotoRespone photoRespone;

    public PhotoRespone getPhotoRespone() {
        return photoRespone;
    }

    @BindingAdapter({"placeImage"})
    public static void loadImage(ImageView imageView, String imageURL) {
        GlideUtil.loadImageURL(imageView.getContext(), imageURL, imageView);
    }

}
