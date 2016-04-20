package com.serveroverload.networkmaster.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.serveroverload.networkmaster.R;
import com.serveroverload.networkmaster.R.color;
import com.serveroverload.networkmaster.R.id;
import com.serveroverload.networkmaster.R.layout;
import com.serveroverload.networkmaster.network.Connectivity;
import com.serveroverload.networkmaster.network.NetworkSpeedCheckTask;

public class NetworkInfoFragment extends Fragment implements
		OnRefreshListener {

	/** The swipe layout. */
	private SwipeRefreshLayout swipeLayout;
	private ToggleButton wifiToggle;
	private ToggleButton mobileDataToggle;
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.frag_network, container, false);


		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(NetworkInfoFragment.this);
		swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
				R.color.holo_green_light, R.color.holo_orange_light,
				R.color.holo_red_light);

		wifiToggle = ((ToggleButton) rootView.findViewById(R.id.togle_wifi));

		mobileDataToggle = ((ToggleButton) rootView
				.findViewById(R.id.togle_data));

		wifiToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				boolean result = Connectivity.toggleWifiConnection(
						getActivity(), isChecked);

				if (result) {
					wifiToggle.setChecked(isChecked);

					fillNetworkData();
				} else {

					Toast.makeText(getActivity(), "Operation to Toggle WIFI",
							1000).show();
					// wifiToggle.setChecked(false);

				}

				fillNetworkData();
			}
		});

		mobileDataToggle
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						boolean result = Connectivity.toggleDataConnection(
								isChecked, getActivity());

						if (result) {
							mobileDataToggle.setChecked(isChecked);

							fillNetworkData();

						} else {

							Toast.makeText(getActivity(),
									"Operation to Toggle Data", 1000).show();
						}

					}
				});

		fillNetworkData();
		return rootView;

	}

	/**
	 * 
	 */
	public void fillNetworkData() {
		((TextView) rootView.findViewById(R.id.connection_status))
				.setText(Connectivity.isConnected(getActivity()) ? "Connected"
						: "NotConnected");

		((TextView) rootView.findViewById(R.id.ipv4)).setText(Connectivity
				.getIPAddress(true));

		((TextView) rootView.findViewById(R.id.ipv6)).setText(Connectivity
				.getIPAddress(false));

		// wlan0
		((TextView) rootView.findViewById(R.id.MAC_ETH)).setText(Connectivity
				.getMACAddress("eth0"));

		((TextView) rootView.findViewById(R.id.MAC_WLAN)).setText(Connectivity
				.getMACAddress("wlan0"));

		((TextView) rootView.findViewById(R.id.nw_type)).setText(Connectivity
				.isConnectedFast(getActivity()) ? "Fast" : "Slow");

		((TextView) rootView.findViewById(R.id.connection_type))
				.setText(Connectivity.isConnectedMobile(getActivity()) ? "Mobile N/W"
						: (Connectivity.isConnectedWifi(getActivity()) ? "Wifi"
								: "None"));

		if (null != Connectivity.getNetworkInfo(getActivity()))
			((TextView) rootView.findViewById(R.id.roaming))
					.setText(Connectivity.getNetworkInfo(getActivity())
							.isRoaming() ? "YES" : "NO");

		((TextView) rootView.findViewById(R.id.netwok_class))
				.setText(Connectivity.getNetworkClass(getActivity()));

		((TextView) rootView.findViewById(R.id.byte_rx)).setText(Connectivity
				.getBytesRecieved());

		((TextView) rootView.findViewById(R.id.byte_tx)).setText(Connectivity
				.getBytesTransmitted());

		new NetworkSpeedCheckTask(
				((TextView) rootView.findViewById(R.id.down_speed)))

		.execute();

		((TextView) rootView.findViewById(R.id.wifi_status))
				.setText(Connectivity.isWifiEnable(getActivity()) ? "Enable"
						: "Disable");

		if (Connectivity.isWifiEnable(getActivity())) {
			wifiToggle.setChecked(true);

			((TextView) rootView.findViewById(R.id.data_status))
					.setText(Connectivity.isMobileDataEnabled(getActivity()) ? "Enable"
							: "Disable");

		}

	}

	@Override
	public void onRefresh() {

		fillNetworkData();

		swipeLayout.setRefreshing(false);

	}


}
