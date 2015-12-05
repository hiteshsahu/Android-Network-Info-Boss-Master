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
import com.serveroverload.networkmaster.network.TelephonyBasic;
import com.serveroverload.networkmaster.network.TelephonyDualSimInfo;

public class TelephonyInfoFragment extends Fragment /*
													 * implements
													 * OnRefreshListener
													 */{

	/** The swipe layout. */
	private SwipeRefreshLayout swipeLayout;
	// private ToggleButton wifiToggle;
	private ToggleButton mobileDataToggle;
	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.frag_telephony, container, false);

		// swipeLayout = (SwipeRefreshLayout) rootView
		// .findViewById(R.id.swipe_container);
		// swipeLayout.setOnRefreshListener(TelephonyInfoFragment.this);
		// swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
		// R.color.holo_green_light, R.color.holo_orange_light,
		// R.color.holo_red_light);

		// wifiToggle = ((ToggleButton) rootView.findViewById(R.id.togle_wifi));
		//
		mobileDataToggle = ((ToggleButton) rootView
				.findViewById(R.id.togle_data));

		// wifiToggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		//
		// @Override
		// public void onCheckedChanged(CompoundButton buttonView,
		// boolean isChecked) {
		//
		// boolean result = Connectivity.toggleWifiConnection(
		// getActivity(), isChecked);
		//
		// if (result) {
		// wifiToggle.setChecked(isChecked);
		//
		// fillNetworkData();
		// } else {
		//
		// Toast.makeText(getActivity(), "Operation to Toggle WIFI",
		// 1000).show();
		// // wifiToggle.setChecked(false);
		//
		// }
		//
		// fillNetworkData();
		// }
		// });
		//
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

		if (null != getActivity() && null != rootView) {

			TelephonyBasic tel = new TelephonyBasic(getActivity());

			((TextView) rootView.findViewById(R.id.connection_type))
					.setText(tel.getIMEI());

			((TextView) rootView.findViewById(R.id.nw_operator)).setText(tel
					.getNetworkOperator());

			((TextView) rootView.findViewById(R.id.nw_operator_name))
					.setText(tel.getNetworkOperatorName());

			// wlan0
			((TextView) rootView.findViewById(R.id.phone_type)).setText(tel
					.getPhoneType());

			((TextView) rootView.findViewById(R.id.sw_roaming)).setText(tel
					.inRoaming() ? "YES" : "NO");

			((TextView) rootView.findViewById(R.id.nw_sim_serial)).setText(tel
					.getSIMSerialNumber());

			((TextView) rootView.findViewById(R.id.nw_sim_opr_name))
					.setText(tel.getSimOperatorName());

			((TextView) rootView.findViewById(R.id.sim_opr_code)).setText(tel
					.getSimOperatorCode());

			((TextView) rootView.findViewById(R.id.nw_sim_state)).setText(tel
					.getSIMState());

			((TextView) rootView.findViewById(R.id.dual_support))
					.setText(TelephonyDualSimInfo.getInstance(getActivity())
							.isDualSIM() ? "Yes" : "No");

			((TextView) rootView.findViewById(R.id.imei_sim_1))
					.setText(TelephonyDualSimInfo.getInstance(getActivity())
							.getImsiSIM1());

			((TextView) rootView.findViewById(R.id.imei_sim_2))
					.setText(TelephonyDualSimInfo.getInstance(getActivity())
							.getImsiSIM2());

			((TextView) rootView.findViewById(R.id.ready_sim_1))
					.setText(TelephonyDualSimInfo.getInstance(getActivity())
							.isSIM1Ready() ? "YES" : "NO");

			((TextView) rootView.findViewById(R.id.ready_sim_2))
					.setText(TelephonyDualSimInfo.getInstance(getActivity())
							.isSIM2Ready() ? "YES" : "NO");

			((TextView) rootView.findViewById(R.id.dual_sim_23))
					.setText(TelephonyDualSimInfo
							.api23DualSImDetection(getActivity()));

			mobileDataToggle.setChecked(Connectivity
					.isMobileDataEnabled(getActivity()));

			((TextView) rootView.findViewById(R.id.data_status))
					.setText(Connectivity.isMobileDataEnabled(getActivity()) ? "Enable"
							: "Disable");

			((TextView) rootView.findViewById(R.id.data_state)).setText(tel
					.getDataState());

			((TextView) rootView.findViewById(R.id.nw_sim_number)).setText(tel
					.getSimPhoneNumber());

		}
		//
		// @Override
		// public void onRefresh() {
		//
		// fillNetworkData();
		//
		// swipeLayout.setRefreshing(false);
		//
		// }
	}
}
