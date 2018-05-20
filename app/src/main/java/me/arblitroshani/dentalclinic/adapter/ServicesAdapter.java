package me.arblitroshani.dentalclinic.adapter;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.GlideApp;
import me.arblitroshani.dentalclinic.activity.ServiceDetailsActivity;
import me.arblitroshani.dentalclinic.model.Service;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ViewHolder> {

    private List<Service> myDataset;
    private List<String> serviceIds;

    private FirebaseStorage storage;
    private StorageReference storageRefServices;

    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_card_main_1_title)
        TextView tvTitle;

        @BindView(R.id.tv_card_main_1_subtitle)
        TextView tvSubtitle;

        @BindView(R.id.img_main_card_1)
        ImageView ivPicture;

        @BindView(R.id.card_main)
        CardView cvMain;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,  view);
        }
    }

    public ServicesAdapter(List<Service> myDataset, List<String> serviceIds) {
        this.myDataset = myDataset;
        this.serviceIds = serviceIds;
        storage = FirebaseStorage.getInstance();
        storageRefServices = storage.getReference().child("service");
    }

    @Override
    public ServicesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_card_service, parent, false);
        final ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Service currentService = myDataset.get(position);
        holder.tvTitle.setText(currentService.getTitle());
        holder.tvSubtitle.setText(currentService.getSubtitle());
        GlideApp.with(context)
                .load(storageRefServices.child(currentService.getPhotoUrl()))
                .into(holder.ivPicture);
        holder.cvMain.setOnClickListener(view -> {
            Intent i = new Intent(context, ServiceDetailsActivity.class);
            i.putExtra("service_to_display", currentService);
            i.putExtra("service_id", serviceIds.get(position));
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }
}