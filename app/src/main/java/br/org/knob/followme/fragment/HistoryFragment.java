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
import br.org.knob.android.framework.ui.ItemTouchHelperAdapterListener;
import br.org.knob.followme.R;
import br.org.knob.followme.adapter.HistoryAdapter;
import br.org.knob.followme.adapter.HistoryLocationAdapterTouchCallback;


public class HistoryFragment extends BaseFragment {
    private static final String TAG = "HistoryFragment";

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private List<? extends Location> locations;

    private HistoryLocationAdapterTouchCallback itemTouchHelperCallback;
    private ItemTouchHelper itemTouchHelper;

    private OnHistoryLocationListener onHistoryLocationListener;

    public HistoryFragment() {
    }

    public static HistoryFragment newInstance() {
        HistoryFragment historyFragment = new HistoryFragment();

        return historyFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnHistoryLocationListener) {
            this.onHistoryLocationListener = (OnHistoryLocationListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement HistoryFragment.OnHistoryLocationListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

        historyAdapter = new HistoryAdapter(getContext(), locations, ((OnHistoryLocationListener)getActivity()).onHistoryLocationListener());
        recyclerView.setAdapter(historyAdapter);

        itemTouchHelperCallback = new HistoryLocationAdapterTouchCallback(historyAdapter);
        itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    // Caller Activity must implement these interfaces
    public interface OnHistoryLocationListener {
        ItemTouchHelperAdapterListener onHistoryLocationListener();
    }
}
