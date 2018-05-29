package me.arblitroshani.dentalclinic.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.arblitroshani.dentalclinic.R;
import me.arblitroshani.dentalclinic.activity.MainActivity;
import me.arblitroshani.dentalclinic.extra.Constants;
import me.arblitroshani.dentalclinic.model.Session;

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.ViewHolder> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(Constants.Treatments.DATE_FORMAT, Locale.getDefault());

    private List<Session> myDataset;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_card_title)
        TextView tvTitle;

        @BindView(R.id.tv_card_date)
        TextView tvDate;

        @BindView(R.id.tv_card_numSession)
        TextView tvNumSession;

        @BindView(R.id.card_main)
        CardView cvMain;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,  view);
        }
    }

    public SessionsAdapter(List<Session> myDataset) {
        this.myDataset = myDataset;
    }

    @Override
    public SessionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_card_session, parent, false);
        final ViewHolder holder = new ViewHolder(v);

        final MainActivity mainActivity = (MainActivity) context;

        holder.cvMain.setOnClickListener(view ->
            mainActivity.replaceFragment("Session", myDataset.get(holder.getAdapterPosition())));

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Session session = myDataset.get(position);
        holder.tvTitle.setText(session.getName());
        holder.tvDate.setText(DATE_FORMAT.format(session.getDate()));
        holder.tvNumSession.setText("session " + (position + 1));
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }
}