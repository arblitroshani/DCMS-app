package me.arblitroshani.androidtest.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.extras.GlideApp;
import me.arblitroshani.androidtest.model.DoctorBasic;

public class ServiceDoctorsAdapter extends RecyclerView.Adapter<ServiceDoctorsAdapter.ViewHolder> {

    private List<DoctorBasic> myDataset;

    private FirebaseStorage storage;
    private StorageReference storageRefDoctors;

    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivPhoto;
        public TextView tvName;

        public ViewHolder(View v) {
            super(v);
            ivPhoto = v.findViewById(R.id.imageView);
            tvName = v.findViewById(R.id.tvName);
        }
    }

    public ServiceDoctorsAdapter(List<DoctorBasic> myDataset) {
        this.myDataset = myDataset;
        storage = FirebaseStorage.getInstance();
        storageRefDoctors = storage.getReference().child("doctorPhotos");
    }

    @Override
    public ServiceDoctorsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_doctor, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DoctorBasic doctor = myDataset.get(position);
        holder.tvName.setText(doctor.getName());
        GlideApp.with(context)
                .load(storageRefDoctors.child(doctor.getPhotoUrl()))
                .circleCrop()
                .into(holder.ivPhoto);
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }
}
