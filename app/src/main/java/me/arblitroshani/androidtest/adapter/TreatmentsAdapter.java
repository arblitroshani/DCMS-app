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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.androidtest.GlideApp;
import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.activity.MainActivity;
import me.arblitroshani.androidtest.activity.ServiceDetailsActivity;
import me.arblitroshani.androidtest.extra.Constants;
import me.arblitroshani.androidtest.model.Service;
import me.arblitroshani.androidtest.model.Treatment;

public class TreatmentsAdapter extends RecyclerView.Adapter<TreatmentsAdapter.ViewHolder> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(Constants.Treatments.DATE_FORMAT, Locale.getDefault());

    private List<Treatment> myDataset;
    private List<String> treatmentIds;

    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_card_title)
        TextView tvTitle;

        @BindView(R.id.tv_card_date)
        TextView tvDate;

        @BindView(R.id.tv_card_numAppointments)
        TextView tvNumAppointments;

        @BindView(R.id.card_main)
        CardView cvMain;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,  view);
        }
    }

    public TreatmentsAdapter(List<Treatment> myDataset, List<String> treatmentIds) {
        this.myDataset = myDataset;
        this.treatmentIds = treatmentIds;
    }

    @Override
    public TreatmentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_card_treatment, parent, false);

        final ViewHolder holder = new ViewHolder(v);
        final MainActivity mainActivity = (MainActivity) context;

        holder.cvMain.setOnClickListener(view ->
                mainActivity.replaceFragment("Sessions", treatmentIds.get(holder.getAdapterPosition())));

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Treatment treatment = myDataset.get(position);
        holder.tvTitle.setText(treatment.getService() + " Treatment");
        holder.tvDate.setText(DATE_FORMAT.format(treatment.getDateStarted()));
        holder.tvNumAppointments.setText(treatment.getNumSessions() + " sessions");
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }
}