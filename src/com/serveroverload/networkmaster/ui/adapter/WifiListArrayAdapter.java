package com.serveroverload.networkmaster.ui.adapter;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.serveroverload.networkmaster.R;
import com.serveroverload.networkmaster.app.AppController;

// TODO: Auto-generated Javadoc
/**
 * The Class ProductListArrayAdapter.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class WifiListArrayAdapter extends ArrayAdapter<ScanResult>

{

	private RequestQueue queue;

	/**
	 * Instantiates a new product list array adapter.
	 *
	 * @param context
	 *            the context
	 * @param resource
	 *            the resource
	 * @param mScanResults
	 *            the list of recordings
	 */
	public WifiListArrayAdapter(Context context, int resource,
			List<ScanResult> mScanResults) {
		super(context, resource, mScanResults);
		queue = Volley.newRequestQueue(getContext());
		this.context = context;
		this.listOfProducts = mScanResults;
	}

	/** The context. */
	private final Context context;

	/** The list of recordings. */
	private final List<ScanResult> listOfProducts;

	/**
	 * The Class ViewHolder.
	 */
	private class ViewHolder {

		/** The quanitity. */
		TextView SSID, BSID, frequency, routerName, strength;
		ProgressBar dbmStrength;
		ImageView imagView; // SwipeLayout currentSwipeLayout;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View,
	 * android.view.ViewGroup)
	 */
	@SuppressLint("NewApi")
	@Override
	public View getView(final int productIndex, View convertView,
			ViewGroup parent) {

		/** The holder. */
		final ViewHolder holder;

		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.wifi_list_item, parent,
					false);

			holder = new ViewHolder();

			holder.SSID = (TextView) convertView.findViewById(R.id.ssid);

			holder.BSID = (TextView) convertView.findViewById(R.id.mac_address);

			holder.frequency = (TextView) convertView
					.findViewById(R.id.channel);

			holder.routerName = (TextView) convertView
					.findViewById(R.id.router);

			holder.strength = (TextView) convertView
					.findViewById(R.id.strength);

			holder.SSID.setSelected(true);

			holder.imagView = ((ImageView) convertView
					.findViewById(R.id.product_thumb));

			holder.dbmStrength = (ProgressBar) convertView
					.findViewById(R.id.dbm_strength);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.SSID.setText(listOfProducts.get(productIndex).SSID);

		holder.BSID.setText(listOfProducts.get(productIndex).BSSID);

		holder.frequency.setText(""
				+ listOfProducts.get(productIndex).frequency + "MHz");

		holder.routerName.setSelected(true);

		makeJsonArrayRequest("http://www.macvendorlookup.com/api/v2/"
				+ listOfProducts.get(productIndex).BSSID, holder.routerName);

		holder.strength.setText("" + listOfProducts.get(productIndex).level
				+ "dBm");

		int level = WifiManager.calculateSignalLevel(
				listOfProducts.get(productIndex).level, 5);

		String capabilities = listOfProducts.get(productIndex).capabilities;

		// int difference = level * 100 /
		// listOfProducts.get(productIndex).level;

		if (level >= 4) {
			if (capabilities.toUpperCase().contains("WEP")) {
				// WEP Network
			} else if (capabilities.toUpperCase().contains("WPA")
					|| capabilities.toUpperCase().contains("WPA2")) {

				holder.imagView
						.setBackgroundResource(R.drawable.wifi_excelent_locked);
			} else {
				holder.imagView.setBackgroundResource(R.drawable.wifi_excelent);
			}

		} else if (level < 4 && level >= 3) {

			if (capabilities.toUpperCase().contains("WEP")) {
				// WEP Network
			} else if (capabilities.toUpperCase().contains("WPA")
					|| capabilities.toUpperCase().contains("WPA2")) {

				holder.imagView
						.setBackgroundResource(R.drawable.wifi_high_locked);
			} else {
				holder.imagView.setBackgroundResource(R.drawable.wifi_high);
			}

		} else if (level < 3 && level >= 2) {

			if (capabilities.toUpperCase().contains("WEP")) {
				// WEP Network
			} else if (capabilities.toUpperCase().contains("WPA")
					|| capabilities.toUpperCase().contains("WPA2")) {

				holder.imagView
						.setBackgroundResource(R.drawable.wifi_med_locked);
			} else {
				holder.imagView.setBackgroundResource(R.drawable.wifi_med);
			}

		} else if (level < 2 && level >= 1) {

			if (capabilities.toUpperCase().contains("WEP")) {
				// WEP Network
			} else if (capabilities.toUpperCase().contains("WPA")
					|| capabilities.toUpperCase().contains("WPA2")) {

				holder.imagView
						.setBackgroundResource(R.drawable.wifi_low_locked);
			} else {
				holder.imagView.setBackgroundResource(R.drawable.wifi_low);
			}

		}

		else if (level < 1 && level >= 0) {

			if (capabilities.toUpperCase().contains("WEP")) {
				// WEP Network
			} else if (capabilities.toUpperCase().contains("WPA")
					|| capabilities.toUpperCase().contains("WPA2")) {

				holder.imagView.setBackgroundResource(R.drawable.wifi_disabled);
			} else {
				holder.imagView.setBackgroundResource(R.drawable.wifi_disabled);
			}

		}

		int levelValue = listOfProducts.get(productIndex).level;

		if (levelValue > -100 && levelValue <= -85) {
			holder.dbmStrength.getProgressDrawable().setColorFilter(
					Color.MAGENTA, Mode.SRC_IN);
		} else if (levelValue > -85 && levelValue <= -60) {
			holder.dbmStrength.getProgressDrawable().setColorFilter(Color.BLUE,
					Mode.SRC_IN);
		} else if (levelValue > -60 && levelValue <= -40) {
			holder.dbmStrength.getProgressDrawable().setColorFilter(
					Color.GREEN, Mode.SRC_IN);
		}

		holder.dbmStrength.setProgress(Math.abs((levelValue + 40) * 100 / 60));

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Utiles.switchFragmentWithAnimation(R.id.frag_root,
				// ProductDetailsFragment.newInstance(categoryIndex,
				// productIndex), ((HomeActivity) (getContext())),
				// "ProductDetailsFragment", AnimationType.SLIDE_RIGHT);

			}
		});

		return convertView;
	}

	/**
	 * Method to make json array request where response starts with [
	 * */
	private void makeJsonArrayRequest(String urlJsonArry,
			final TextView txtResponse) {

		// showpDialog();

		JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
				new Response.Listener<JSONArray>() {
					@Override
					public void onResponse(JSONArray response) {
						// Log.d(TAG, response.toString());

						try {

							if (null != response) {

								txtResponse.setText(response.getJSONObject(0)
										.getString("company")
										+ "\n"
										+ response.getJSONObject(0).getString(
												"country"));
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// hidepDialog();
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// VolleyLog.d(TAG, "Error: " + error.getMessage());
						// Toast.makeText(getContext(),
						// error.getCause().toString(),
						// Toast.LENGTH_SHORT).show();
						// hidepDialog();
					}
				});

		req.setRetryPolicy(new DefaultRetryPolicy(5000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(req);
	}

}
