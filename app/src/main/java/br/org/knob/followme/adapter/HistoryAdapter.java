package br.org.knob.followme.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.org.knob.android.framework.model.Location;
import br.org.knob.android.framework.ui.ItemTouchHelperAdapter;
import br.org.knob.followme.R;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>
    implements ItemTouchHelperAdapter {
    protected static final String TAG = "HistoryAdapter";

    private final Context context;
    private List<? extends Location> itens;

    private ItemOnClickListener itemOnClickListener;
    private ItemOnSwipeListener itemOnSwipeListener;

    public HistoryAdapter(Context context,
                          List<? extends Location> itens,
                          ItemOnClickListener itemOnClickListener,
                          ItemOnSwipeListener itemOnSwipeListener) {
        this.context = context;
        this.itens = (List<Location>) itens;

        // Caller fragment listener for adapter's events
        this.itemOnClickListener = itemOnClickListener;
        this.itemOnSwipeListener = itemOnSwipeListener;
    }

    @Override
    public int getItemCount() {
        return this.itens != null ? this.itens.size() : 0;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_history, viewGroup, false);
        HistoryViewHolder holder = new HistoryViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final HistoryViewHolder holder, final int position) {
        Location location = itens.get(position);

        if(location != null && holder != null) {
            // TODO: set a better title
            Bitmap snapshot = location.getSnapshot() != null ? location.getSnapshot() : null;
            String title = location.getId() != null ? "Location #" + location.getId() : "";
            String latitude = location.getLatitude() != null ? location.getLatitude() : "";
            String longitude = location.getLongitude() != null ? location.getLongitude() : "";
            String date = location.getDate() != null ? location.getDate().toString() : "";

            // Put values into respective views
            if(snapshot != null) {
                holder.snapshotView.setImageBitmap(snapshot);
            }
            holder.titleView.setText(title);
            holder.latitudeView.setText(latitude);
            holder.longitudeView.setText(longitude);
            holder.dateView.setText(date);

            // Set click listener, if caller fragment defined it
            if (itemOnClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemOnClickListener.onClickItem(holder.itemView, position);
                    }
                });
            }

            // Set long click listener, if caller fragment defined it
            if (itemOnSwipeListener != null) {
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
                        return true;
                    }
                });
            }

            // Set swipe listener, if caller fragment defined it
            if (itemOnSwipeListener != null) {
                holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View viewHolder, MotionEvent event) {
                        if(MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                            itemOnSwipeListener.onSwipeItem(holder.itemView, position);
                        }
                        return false;
                    }
                });
            }
        }
    }

    public List<? extends Location> getItens() {
        return itens;
    }

    public void setItens(List<? extends Location> itens) {
        this.itens = itens;
    }

    @Override
    public boolean onItemMove(int i, int i1) {
        return false;
    }

    @Override
    public void onItemDismiss(int i) {

    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        ImageView snapshotView;
        TextView titleView;
        TextView latitudeView;
        TextView longitudeView;
        TextView dateView;
        CardView cardView;

        HistoryViewHolder(View view) {
            super(view);
            snapshotView = (ImageView) view.findViewById(R.id.adapter_history_snapshot);
            titleView = (TextView) view.findViewById(R.id.adapter_history_title);
            latitudeView = (TextView) view.findViewById(R.id.adapter_history_latitude);
            longitudeView = (TextView) view.findViewById(R.id.adapter_history_longitude);
            dateView = (TextView) view.findViewById(R.id.adapter_history_date);
            cardView = (CardView) view.findViewById(R.id.card_history);
        }
    }

    public interface ItemOnClickListener {
        void onClickItem(View view, int idx);
    }

    public interface ItemOnSwipeListener {
        void onSwipeItem(View view, int idx);
    }

    public interface ItemOnLongClickedListener {
        void onLongClickItem(View view, int idx);
    }
}

