package me.arblitroshani.androidtest.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.activity.MainActivity;
import me.arblitroshani.androidtest.model.HomeSection;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<HomeSection> myDataset;

    private Context context;
    private Resources resources;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public TextView tvSubtitle;
        public ImageView ivIcon;
        public CardView cvMain;
        public ViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tv_card_main_title);
            tvSubtitle = v.findViewById(R.id.tv_card_main_subtitle);
            ivIcon = v.findViewById(R.id.imageView);
            cvMain = v.findViewById(R.id.card_main);
        }
    }

    public HomeAdapter(List<HomeSection> myDataset) {
        this.myDataset = myDataset;
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        resources = context.getResources();
        final MainActivity mainActivity = (MainActivity) context;
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_card_home, parent, false);
        final ViewHolder holder = new ViewHolder(v);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fragmentOpen = myDataset.get(holder.getAdapterPosition()).getFragmentOpen();
                mainActivity.replaceFragment(fragmentOpen);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final HomeSection currentSection = myDataset.get(position);

        holder.tvTitle.setText(currentSection.getTitle());
        holder.tvSubtitle.setText(currentSection.getSubtitle());
        holder.ivIcon.setImageDrawable(resources.getDrawable(currentSection.getIcon()));
        holder.cvMain.setCardBackgroundColor(resources.getColor(currentSection.getBackgroundColor()));
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }
}