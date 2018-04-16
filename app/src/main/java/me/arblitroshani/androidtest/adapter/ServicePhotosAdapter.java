package me.arblitroshani.androidtest.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.extras.GlideApp;

public class ServicePhotosAdapter extends RecyclerView.Adapter<ServicePhotosAdapter.ViewHolder> {

    private List<String> myDataset;

    private FirebaseStorage storage;
    private StorageReference storageRefPhotos;

    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivPhoto;

        public ViewHolder(View v) {
            super(v);
            ivPhoto = v.findViewById(R.id.ivPhoto);
        }
    }

    public ServicePhotosAdapter(List<String> myDataset, String serviceId) {
        this.myDataset = myDataset;
        storage = FirebaseStorage.getInstance();
        storageRefPhotos = storage.getReference().child("servicePhotos").child(serviceId);
    }

    @Override
    public ServicePhotosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_service_photo, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GlideApp.with(context)
                .load(storageRefPhotos.child(myDataset.get(position)))
                .centerCrop()
                .into(holder.ivPhoto);
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }
}
