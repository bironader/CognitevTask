package com.example.cognetivtask.features.main;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cognetivtask.data.responses.places.Item;
import com.example.congnitevtask.R;
import com.example.congnitevtask.databinding.PlaceRowBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceVh> {

    public void setPlaces(List<Item> places) {
        this.places = places;
    }

    private List<Item> places;

    @Inject
    PlacesAdapter() {
        places = new ArrayList<>();
    }

    public void updatePlace(Item place){
        places.set(places.indexOf(place),place);
        notifyItemChanged(places.indexOf(place));
    }

    @NonNull
    @Override
    public PlaceVh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PlaceRowBinding placeRowBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext())
                , R.layout.place_row, parent, false);
        return new PlaceVh(placeRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceVh holder, int position) {

        holder.placeRowBinding.setPlace(places.get(position).getPlace());
    }

    @Override
    public int getItemCount() {
        return places.size();
    }


    class PlaceVh extends RecyclerView.ViewHolder {

        private PlaceRowBinding placeRowBinding;

        public PlaceVh(@NonNull PlaceRowBinding placeRowBinding) {
            super(placeRowBinding.getRoot());
            this.placeRowBinding = placeRowBinding;
        }


    }
}
