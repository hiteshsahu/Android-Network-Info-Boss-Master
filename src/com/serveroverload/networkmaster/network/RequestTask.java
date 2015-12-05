package com.serveroverload.networkmaster.network;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

public class RequestTask extends AsyncTask<String, String, String> {

	/**
	 * @param context
	 */
	public RequestTask(android.content.Context context) {
		super();
		context = context;
	}

	private String response;
	private long startTime;
	private long endTime;

	Context context;

	@Override
	protected String doInBackground(String... uri) {

		InputStream stream = null;
		try {
			int bytesIn = 0;
			String downloadFileUrl = "http://www.gregbugaj.com/wp-content/uploads/2009/03/dummy.txt";
			long startCon = System.currentTimeMillis();
			URL url = new URL(downloadFileUrl);
			URLConnection con = url.openConnection();
			con.setUseCaches(false);
			long connectionLatency = System.currentTimeMillis() - startCon;
			stream = con.getInputStream();

			long start = System.currentTimeMillis();
			int currentByte = 0;
			long updateStart = System.currentTimeMillis();
			long updateDelta = 0;
			int bytesInThreshold = 0;

			while ((currentByte = stream.read()) != -1) {
				bytesIn++;
				bytesInThreshold++;
				if (updateDelta >= UPDATE_THRESHOLD) {
					int progress = (int) ((bytesIn / (double) EXPECTED_SIZE_IN_BYTES) * 100);

					calculate(updateDelta, bytesInThreshold);
					// Reset
					updateStart = System.currentTimeMillis();
					bytesInThreshold = 0;
				}
				updateDelta = System.currentTimeMillis() - updateStart;
			}

			long downloadTime = (System.currentTimeMillis() - start);
			// Prevent AritchmeticException
			if (downloadTime == 0) {
				downloadTime = 1;
			}

			// Message msg=Message.obtain(mHandler, MSG_COMPLETE_STATUS,
			// calculate(downloadTime, bytesIn));
			// msg.arg1=bytesIn;
			// mHandler.sendMessage(msg);
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage());
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
			} catch (IOException e) {
				// Suppressed
			}
		}
		return response;

	}

	/**
	 * 
	 * 1 byte = 0.0078125 kilobits 1 kilobits = 0.0009765625 megabit
	 * 
	 * @param downloadTime
	 *            in miliseconds
	 * @param bytesIn
	 *            number of bytes downloaded
	 * @return SpeedInfo containing current speed
	 */
	private SpeedInfo calculate(final long downloadTime, final long bytesIn) {
		SpeedInfo info = new SpeedInfo();
		// from mil to sec
		long bytespersecond = (bytesIn / downloadTime) * 1000;
		double kilobits = bytespersecond * BYTE_TO_KILOBIT;
		double megabits = kilobits * KILOBIT_TO_MEGABIT;
		info.downspeed = bytespersecond;
		info.kilobits = kilobits;
		info.megabits = megabits;

		System.out.println("Speed is" + info.kilobits);

		return info;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);

		// long dataSize = result.length() / 1024;
		// // takenTime = endTime - startTime;
		// long s = endTime - startTime / 1000;
		// double speed = dataSize / s;

		//Toast.makeText(context, "" + s + "kbps", Toast.LENGTH_SHORT).show();
	}

	private static class SpeedInfo {
		public double kilobits = 0;
		public double megabits = 0;
		public double downspeed = 0;
	}

	// Private fields
	private static final String TAG = RequestTask.class.getSimpleName();
	private static final int EXPECTED_SIZE_IN_BYTES = 1048576;// 1MB 1024*1024

	private static final double EDGE_THRESHOLD = 176.0;
	private static final double BYTE_TO_KILOBIT = 0.0078125;
	private static final double KILOBIT_TO_MEGABIT = 0.0009765625;

	private Button mBtnStart;
	private TextView mTxtSpeed;
	private TextView mTxtConnectionSpeed;
	private TextView mTxtProgress;
	private TextView mTxtNetwork;

	private final int MSG_UPDATE_STATUS = 0;
	private final int MSG_UPDATE_CONNECTION_TIME = 1;
	private final int MSG_COMPLETE_STATUS = 2;

	private final static int UPDATE_THRESHOLD = 300;

	private DecimalFormat mDecimalFormater;

}