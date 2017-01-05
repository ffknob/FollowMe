package br.org.knob.followme.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.org.knob.android.framework.fragment.BaseFragment;
import br.org.knob.android.framework.util.Util;
import br.org.knob.followme.R;
import br.org.knob.followme.adapter.HistoryAdapter;
import br.org.knob.android.framework.model.Location;
import br.org.knob.android.framework.service.LocationService;


public class HistoryFragment extends BaseFragment {
    private static final String TAG = "HistoryFragment";

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private List<? extends Location> locations;

    public HistoryFragment() {}

    public static HistoryFragment newInstance()
    {
        HistoryFragment historyFragment = new HistoryFragment();

        return historyFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        historyAdapter = new HistoryAdapter(getContext(), locations, onClickItem());
        recyclerView.setAdapter(historyAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    private HistoryAdapter.ItemOnClickListener onClickItem() {
        return new HistoryAdapter.ItemOnClickListener() {
            @Override
            public void onClickItem(View view, int idx) {
                if(locations != null) {
                    Location location = (Location) locations.get(idx);

                    // TODO: do something when card clicked
                    Util.toast(getContext(), "Location #" + location.getId());
                }
            }
        };
    }
}
