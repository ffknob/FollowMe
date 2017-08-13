package br.org.knob.followme.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import br.org.knob.android.framework.ui.ItemTouchHelperAdapterListener;
import br.org.knob.android.framework.ui.OnSwipeTouchListener;
import br.org.knob.followme.R;

public class HistoryLocationSwipeTouchListener extends OnSwipeTouchListener {

    public HistoryLocationSwipeTouchListener(Context context, ItemTouchHelperAdapterListener itemTouchHelperAdapterListener) {
        super(context, itemTouchHelperAdapterListener);
    }

    public HistoryLocationSwipeTouchListener(Context context, RecyclerView.ViewHolder viewHolder,
                                             ItemTouchHelperAdapterListener itemTouchHelperAdapterListener,
                                             Long id) {
        super(context, viewHolder, itemTouchHelperAdapterListener, id);
    }

    @Override
    public void onClick() {
        super.onClick();

        getItemTouchHelperAdapterListener().onClickItem(getContext(), getView(), getViewHolder().getAdapterPosition(), getId());
    }

    @Override
    public void onLongClick() {
        super.onLongClick();

        if(getView() != null) {
            if (getView().isSelected()) {
                getView().setBackgroundColor(ContextCompat.getColor(getView().getContext(), R.color.white));
                getView().setSelected(false);
            } else {
                getView().setBackgroundColor(ContextCompat.getColor(getView().getContext(), R.color.colorAccent));
                getView().setSelected(true);
            }
        }
    }

    @Override
    public void onSwipeLeft() {
        super.onSwipeLeft();
        if(getViewHolder() != null && getView() != null) {
            getItemTouchHelperAdapterListener().onSwipeItem(getView().getContext(), getView(), getViewHolder().getAdapterPosition(), getId());
        }
    }

    @Override
    public void onSwipeRight() {
        super.onSwipeRight();

        onSwipeLeft();
    }
}
