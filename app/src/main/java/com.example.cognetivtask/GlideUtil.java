package com.example.cognetivtask;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.congnitevtask.R;

public class GlideUtil {

    private GlideUtil() {

    }

    public static void loadImageURL(Context mContext, String imageURL, ImageView imageView) {
        Glide.with(mContext)
                .load(imageURL)
                .placeholder(R.drawable.ic_photo_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView);
    }

}
