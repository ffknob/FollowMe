package br.org.knob.followme.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.org.knob.android.framework.fragment.BaseFragment;
import br.org.knob.android.framework.model.Location;
import br.org.knob.android.framework.service.LocationService;
import br.org.knob.android.framework.util.Util;
import br.org.knob.followme.R;
import br.org.knob.followme.adapter.HistoryAdapter;
import br.org.knob.followme.adapter.helper.HistoryAdapterItemTouchHelperCallback;


public class HistoryFragment extends BaseFragment {
    private static final String TAG = "HistoryFragment";

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private List<? extends Location> locations;

    private HistoryAdapterItemTouchHelperCallback itemTouchHelperCallback;
    private ItemTouchHelper itemTouchHelper;

    private OnItemSelectedListener itemSelectedListener;
    private OnItemSwipedListener itemSwipedListener;
    private OnItemLongClickedListener itemLongClickedListener;

    public HistoryFragment() {
    }

    public static HistoryFragment newInstance() {
        HistoryFragment historyFragment = new HistoryFragment();

        return historyFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnItemSelectedListener) {
            itemSelectedListener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implemenet HistoryFragment.OnItemSelectedListener");
        }

        if (context instanceof OnItemSwipedListener) {
            itemSwipedListener = (OnItemSwipedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implemenet HistoryFragment.OnItemSwipedListener");
        }

        if (context instanceof OnItemLongClickedListener) {
            itemLongClickedListener = (OnItemLongClickedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implemenet HistoryFragment.OnItemSLongClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        itemSelectedListener = null;
        itemSwipedListener = null;
        itemLongClickedListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_history, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_history);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        setRetainInstance(true);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        LocationService locationService = new LocationService(getContext());
        locations = locationService.findAllOrderedByDate();

        historyAdapter = new HistoryAdapter(getContext(), locations, onClickItem(), onSwipeItem());
        recyclerView.setAdapter(historyAdapter);

        itemTouchHelperCallback = new HistoryAdapterItemTouchHelperCallback(historyAdapter);
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private HistoryAdapter.ItemOnClickListener onClickItem() {
        return new HistoryAdapter.ItemOnClickListener() {
            @Override
            public void onClickItem(View view, int idx) {
                if (locations != null) {
                    Location location = locations.get(idx);

                    itemSelectedListener.onHistoryLocationSelected(location);
                }
            }
        };
    }

    private HistoryAdapter.ItemOnSwipeListener onSwipeItem() {
        return new HistoryAdapter.ItemOnSwipeListener() {
            @Override
            public void onSwipeItem(View view, int idx) {
                if (locations != null) {
                    Location location = locations.get(idx);

                    itemSwipedListener.onHistoryLocationSwiped(location, historyAdapter, idx);
                }
            }
        };
    }

    private HistoryAdapter.ItemOnLongClickedListener onLongClickItem() {
        return new HistoryAdapter.ItemOnLongClickedListener() {
            @Override
            public void onLongClickItem(View view, int idx) {
                if (locations != null) {
                    Location location = locations.get(idx);

                    itemLongClickedListener.onHistoryLocationLongClicked(location, historyAdapter, idx, view);
                }
            }
        };
    }

    // Caller Activity must implement these interfaces
    public interface OnItemSelectedListener {
        void onHistoryLocationSelected(Location location);
    }

    public interface OnItemSwipedListener {
        void onHistoryLocationSwiped(Location location, HistoryAdapter historyAdapter, int idx);
    }

    public interface OnItemLongClickedListener {
        void onHistoryLocationLongClicked(Location location, HistoryAdapter historyAdapter, int idx, View view);
    }
}
