package me.arblitroshani.androidtest.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.androidtest.R;
import me.arblitroshani.androidtest.activity.MainActivity;
import me.arblitroshani.androidtest.model.HomeSection;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<HomeSection> myDataset;

    private Context context;
    private Resources resources;

    private FirebaseAuth auth;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_card_main_title) TextView tvTitle;
        @BindView(R.id.tv_card_main_subtitle) TextView tvSubtitle;
        @BindView(R.id.imageView) ImageView ivIcon;
        @BindView(R.id.card_main) CardView cvMain;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public HomeAdapter(List<HomeSection> myDataset) {
        this.myDataset = myDataset;
    }

    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        auth = FirebaseAuth.getInstance();
        context = parent.getContext();
        resources = context.getResources();
        final MainActivity mainActivity = (MainActivity) context;
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_card_home, parent, false);
        final ViewHolder holder = new ViewHolder(v);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeSection section = myDataset.get(holder.getAdapterPosition());

                if (section.isRequiresLogin() && !isUserSignedIn()) {
                    new AlertDialog.Builder(context)
                            .setTitle("Warning!")
                            .setMessage("You can only open this section if you are logging in.")
                            .setPositiveButton("OK", null)
                            .setNeutralButton("LOGIN", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    mainActivity.login();
                                }
                            })
                            .show();
                } else {
                    mainActivity.replaceFragment(section.getFragmentOpen());
                }
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

    private boolean isUserSignedIn() {
        return auth.getCurrentUser() != null;
    }
}