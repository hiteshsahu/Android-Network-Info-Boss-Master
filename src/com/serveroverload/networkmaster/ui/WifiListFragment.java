package com.serveroverload.networkmaster.ui;

import java.util.List;

import com.serveroverload.networkmaster.R;
import com.serveroverload.networkmaster.R.color;
import com.serveroverload.networkmaster.R.id;
import com.serveroverload.networkmaster.R.layout;
import com.serveroverload.networkmaster.ui.adapter.WifiListArrayAdapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

public class WifiListFragment extends Fragment implements OnRefreshListener {

	WifiManager wifiManager;
	WifiReceiver wifiListReciever;
	StringBuilder sb = new StringBuilder();
	private SwipeRefreshLayout swipeLayout;
	private ListView wifilist;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_wifi,
				container, false);

		wifiManager = ((WifiManager) getActivity().getSystemService(
				Context.WIFI_SERVICE));
		wifilist = (ListView) rootView.findViewById(R.id.wifi_list);

		swipeLayout = (SwipeRefreshLayout) rootView
				.findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(WifiListFragment.this);
		swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
				R.color.holo_green_light, R.color.holo_orange_light,
				R.color.holo_red_light);

		wifiListReciever = new WifiReceiver();
		getActivity().registerReceiver(wifiListReciever,
				new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		wifiManager.startScan();

		Toast.makeText(getActivity(), "Started Scanning", 1000).show();
		return rootView;
	}

	public void onPause() {
		getActivity().unregisterReceiver(wifiListReciever);
		super.onPause();
	}

	public void onResume() {
		getActivity().registerReceiver(wifiListReciever,
				new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
		super.onResume();
	}

	// Broadcast receiver class called its receive method
	// when number of wifi connections changed

	class WifiReceiver extends BroadcastReceiver {

		// This method call when number of wifi connections changed
		public void onReceive(Context c, Intent intent) {

			if (intent.getAction().equals(
					WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
				List<ScanResult> mScanResults = wifiManager.getScanResults();
				// add your logic here

				// sb = new StringBuilder();
				// sb.append("\n        Number Of Wifi connections :"
				// + mScanResults.size() + "\n\n");

				// for (int i = 0; i < mScanResults.size(); i++) {
				//
				// sb.append(new Integer(i + 1).toString() + ". ");
				// sb.append((mScanResults.get(i)).toString());
				// sb.append("\n\n");
				// }

				// Toast.makeText(getActivity(), sb, 5000).show();

				WifiListArrayAdapter adapter = new WifiListArrayAdapter(
						getActivity(), R.layout.wifi_list_item, mScanResults);

				wifilist.setAdapter(adapter);

				// wifiManager.startScan();

			}

			// mainText.setText(sb);
		}
	}

	@Override
	public void onRefresh() {

		wifiManager.startScan();

		Toast.makeText(getActivity(), "Started Scanning", 1000).show();

		swipeLayout.setRefreshing(false);

	}

}
