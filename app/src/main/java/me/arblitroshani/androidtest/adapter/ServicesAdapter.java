package me.arblitroshani.androidtest.adapter;

import android.content.Context;
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
import me.arblitroshani.androidtest.model.Service;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    private List<Service> myDataset;

    private FirebaseStorage storage;
    private StorageReference storageRefServices;

    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public ImageView ivPicture;
        public ViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tv_card_main_1_title);
            ivPicture = v.findViewById(R.id.img_main_card_1);
        }
    }

    public ServicesAdapter(List<Service> myDataset) {
        this.myDataset = myDataset;
        storage = FirebaseStorage.getInstance();
        storageRefServices = storage.getReference().child("services");
    }

    @Override
    public ServicesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_service, parent, false);
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(myDataset.get(position).getTitle());
        GlideApp.with(context)
                .load(storageRefServices.child(myDataset.get(position).getPhotoUrl()))
                .into(holder.ivPicture);
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }
}
