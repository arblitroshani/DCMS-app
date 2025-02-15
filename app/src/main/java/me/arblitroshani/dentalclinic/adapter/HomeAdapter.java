package me.arblitroshani.dentalclinic.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.activity.MainActivity;
import me.arblitroshani.dentalclinic.model.HomeSection;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<HomeSection> myDataset;

    private Context context;
    private Resources resources;

    private FirebaseAuth auth;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_card_main_title)
        TextView tvTitle;

        @BindView(R.id.imageView)
        ImageView ivIcon;

        @BindView(R.id.card_main)
        CardView cvMain;

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

        holder.itemView.setOnClickListener(view -> {
            HomeSection section = myDataset.get(holder.getAdapterPosition());

            if (section.isRequiresLogin() && !isUserSignedIn()) {
                new AlertDialog.Builder(context)
                        .setTitle("Warning!")
                        .setMessage("You can only open this section if you are logged in.")
                        .setPositiveButton("OK", null)
                        .setNeutralButton("LOGIN", (dialogInterface, i) -> mainActivity.login())
                        .show();
            } else {
                String fragmentOpen = section.getFragmentOpen();
                if (fragmentOpen.startsWith("Activity:")) {
                    mainActivity.startActivity(fragmentOpen.substring(9) + "Activity");
                } else {
                    mainActivity.replaceFragment(fragmentOpen);
                }
            }
        });

        holder.itemView.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ObjectAnimator upAnim = ObjectAnimator.ofFloat(view, "translationZ", 16);
                    upAnim.setDuration(150);
                    upAnim.setInterpolator(new DecelerateInterpolator());
                    upAnim.start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    ObjectAnimator downAnim = ObjectAnimator.ofFloat(view, "translationZ", 0);
                    downAnim.setDuration(150);
                    downAnim.setInterpolator(new AccelerateInterpolator());
                    downAnim.start();
                    break;
            }
            return false;
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final HomeSection currentSection = myDataset.get(position);

        holder.tvTitle.setText(currentSection.getTitle());
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