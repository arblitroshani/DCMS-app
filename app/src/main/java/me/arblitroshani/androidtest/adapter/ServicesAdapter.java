package me.arblitroshani.androidtest.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
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
import me.arblitroshani.androidtest.activity.ServiceDetailsActivity;
import me.arblitroshani.androidtest.GlideApp;
import me.arblitroshani.androidtest.model.Service;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    private List<Service> myDataset;
    private List<String> serviceIds;

    private FirebaseStorage storage;
    private StorageReference storageRefServices;

    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public TextView tvSubtitle;
        public ImageView ivPicture;
        public CardView cvMain;
        public ViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tv_card_main_1_title);
            tvSubtitle = v.findViewById(R.id.tv_card_main_1_subtitle);
            ivPicture = v.findViewById(R.id.img_main_card_1);
            cvMain = v.findViewById(R.id.card_main);
        }
    }

    public ServicesAdapter(List<Service> myDataset, List<String> serviceIds) {
        this.myDataset = myDataset;
        this.serviceIds = serviceIds;
        storage = FirebaseStorage.getInstance();
        storageRefServices = storage.getReference().child("services");
    }

    @Override
    public ServicesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_card_service, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Service currentService = myDataset.get(position);
        holder.tvTitle.setText(currentService.getTitle());
        holder.tvSubtitle.setText(currentService.getSubtitle());
        GlideApp.with(context)
                .load(storageRefServices.child(currentService.getPhotoUrl()))
                .into(holder.ivPicture);
        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ServiceDetailsActivity.class);
                i.putExtra("service_to_display", currentService);
                i.putExtra("service_id", serviceIds.get(position));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }
}
