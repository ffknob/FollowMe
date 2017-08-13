package br.org.knob.followme.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.org.knob.android.framework.model.Location;
import br.org.knob.android.framework.ui.ItemTouchHelperAdapterListener;
import br.org.knob.android.framework.ui.OnSwipeTouchListener;
import br.org.knob.followme.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    protected static final String TAG = "HistoryAdapter";

    private final Context context;
    private List<? extends Location> itens;

    private ItemTouchHelperAdapterListener historyLocationTouchListener;

    public HistoryAdapter(Context context,
                          List<? extends Location> itens,
                          ItemTouchHelperAdapterListener historyLocationTouchListener) {
        this.context = context;
        this.itens = (List<Location>) itens;
        this.historyLocationTouchListener = historyLocationTouchListener;
    }

    @Override
    public int getItemCount() {
        return this.itens != null ? this.itens.size() : 0;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_history, viewGroup, false);
        HistoryLocationSwipeTouchListener historyLocationSwipeTouchListener =
                new HistoryLocationSwipeTouchListener(context, historyLocationTouchListener);
        HistoryViewHolder holder = new HistoryViewHolder(view, historyLocationSwipeTouchListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(final HistoryViewHolder holder, final int position) {
        Location location = itens.get(position);

        if (location != null && holder != null) {
            // TODO: set a better title
            Bitmap snapshot = location.getSnapshot() != null ? location.getSnapshot() : null;
            String title = location.getId() != null ? "Location #" + location.getId() : "";
            String latitude = location.getLatitude() != null ? location.getLatitude() : "";
            String longitude = location.getLongitude() != null ? location.getLongitude() : "";
            String date = location.getDate() != null ? location.getDate().toString() : "";

            // Put values into respective views
            if (snapshot != null) {
                holder.snapshotView.setImageBitmap(snapshot);
            }
            holder.titleView.setText(title);
            holder.latitudeView.setText(latitude);
            holder.longitudeView.setText(longitude);
            holder.dateView.setText(date);

            // Sets dataset item id
            holder.setId(location.getId());
        }
    }

    @Override
    public long getItemId(int position) {
        Location location = itens.get(position);

        if (location != null) {
            return location.getId();
        }

        return RecyclerView.NO_ID;
    }

    public List<? extends Location> getItens() {
        return itens;
    }

    public void setItens(List<? extends Location> itens) {
        this.itens = itens;
    }

    public Context getContext() {
        return this.context;
    }

    public ItemTouchHelperAdapterListener getHistoryLocationTouchListener() {
        return this.historyLocationTouchListener;
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView snapshotView;
        TextView titleView;
        TextView latitudeView;
        TextView longitudeView;
        TextView dateView;
        CardView cardView;

        OnSwipeTouchListener onSwipeTouchListener;

        HistoryViewHolder(View view, OnSwipeTouchListener onSwipeTouchListener) {
            super(view);
            snapshotView = (ImageView) view.findViewById(R.id.adapter_history_snapshot);
            titleView = (TextView) view.findViewById(R.id.adapter_history_title);
            latitudeView = (TextView) view.findViewById(R.id.adapter_history_latitude);
            longitudeView = (TextView) view.findViewById(R.id.adapter_history_longitude);
            dateView = (TextView) view.findViewById(R.id.adapter_history_date);
            cardView = (CardView) view.findViewById(R.id.card_history);
            view.setSelected(false);

            this.onSwipeTouchListener = onSwipeTouchListener;
            onSwipeTouchListener.setViewHolder(this);
            onSwipeTouchListener.setView(itemView);
            view.setOnTouchListener(onSwipeTouchListener);
        }

        public void setId(Long id) {
            onSwipeTouchListener.setId(id);
        }

        public Long getId() { return onSwipeTouchListener.getId();}
    }
}

