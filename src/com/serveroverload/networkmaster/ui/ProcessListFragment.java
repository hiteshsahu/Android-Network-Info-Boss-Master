package com.serveroverload.networkmaster.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.serveroverload.networkmaster.R;
import com.serveroverload.networkmaster.network.Connectivity;
import com.serveroverload.networkmaster.network.Processes;
import com.serveroverload.networkmaster.ui.adapter.ProcessesListArrayAdapter;

public class ProcessListFragment extends Fragment implements OnRefreshListener {

	private SwipeRefreshLayout swipeLayout;
	private ToggleButton mobileDataToggle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_process_list, container,
				false);

		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(ProcessListFragment.this);
		swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
				R.color.holo_green_light, R.color.holo_orange_light,
				R.color.holo_red_light);

		final ListView procesList = (ListView) rootView
				.findViewById(R.id.process_list);

		mobileDataToggle = ((ToggleButton) rootView
				.findViewById(R.id.togle_processes));

		mobileDataToggle
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						procesList.setAdapter(new ProcessesListArrayAdapter(
								getActivity(), R.layout.process_list_item,
								new Processes(getActivity())
										.getRunningApplications(isChecked)));

					}
				});

		procesList.setAdapter(new ProcessesListArrayAdapter(getActivity(),
				R.layout.process_list_item, new Processes(getActivity())
						.getRunningApplications(false)));

		return rootView;
	}

	public void onPause() {
		super.onPause();
	}

	public void onResume() {
		super.onResume();
	}

	@Override
	public void onRefresh() {

		swipeLayout.setRefreshing(false);

	}

}
