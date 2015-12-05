package com.serveroverload.networkmaster.network;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class ImageLoaderTask.
 */
public class NetworkSpeedCheckTask extends AsyncTask<Void, Void, String> {

	/**
	 * @param networkSped
	 */
	public NetworkSpeedCheckTask(TextView networkSped) {
		this.networkSped = networkSped;
	}

	String linkSpeed3G = "0 Kb/s";

	TextView networkSped;

	/**
	 * Before starting background do some work.
	 * */
	@Override
	protected void onPreExecute() {
		networkSped.setText("fetching");
	}

	@Override
	protected String doInBackground(Void... params) {

		try {

			long startTime = System.nanoTime();
			HttpGet httpRequest;
			httpRequest = new HttpGet(new URL("Http://Google.com").toURI());

			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response = (HttpResponse) httpClient
					.execute(httpRequest);

			long endTime = System.nanoTime();

			HttpEntity entity = response.getEntity();
			BufferedHttpEntity bufHttpEntity;
			bufHttpEntity = new BufferedHttpEntity(entity);

			// You can re-check the size of your file
			final long contentLength = bufHttpEntity.getContentLength();

			linkSpeed3G = internetSpeed((endTime - startTime), contentLength,
					false);

			// // Bandwidth : size(KB)/time(s)
			// float bandwidth = contentLength
			// / ((endTime - startTime) * 1000);

			//

			Log.d("NetworkManger", "-----------[BENCHMARK] Dowload time :"
					+ (endTime - startTime) + " ms");

			// Log
		} catch (MalformedURLException e) {
			linkSpeed3G = "Failure";
			e.printStackTrace();
		} catch (URISyntaxException e) {
			linkSpeed3G = "Failure";
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			linkSpeed3G = "Failure";
			e.printStackTrace();
		} catch (IOException e) {
			linkSpeed3G = "Failure";
			e.printStackTrace();
		} finally {
		}

		return linkSpeed3G;

	}

	/**
	 * Update list ui after process finished.
	 */
	protected void onPostExecute(String file_url) {

		if (null != networkSped) {

			networkSped.setText(linkSpeed3G);
			if (linkSpeed3G.equalsIgnoreCase("Failure")) {
				networkSped.setTextColor(0xE43F3F);
			} else {
				networkSped.setTextColor(0xff669900);
			}

		}
	}

	public static String internetSpeed(long nSecs, long bytes, boolean showBits) {

		if (nSecs != 0) {

			BigDecimal secondsBigDecimal = BigDecimal.valueOf(nSecs).divide(
					BigDecimal.valueOf(1000000000), 2, RoundingMode.HALF_UP);
			// long secs = nSecs / 1000000000;

			if (showBits) {
				BigDecimal bitsBigDecimal = BigDecimal.valueOf(bytes * 8);
				// long bits = bytes * 8;

				if (secondsBigDecimal.signum() == 1) {

					// float speed = bits / secs;
					BigDecimal speeedBigDecimal = bitsBigDecimal.divide(
							secondsBigDecimal, 2, RoundingMode.HALF_UP);

					long Kbit = 1024;
					long Mbit = Kbit * 1024;
					long Gbit = Mbit * 1024;

					float speed = speeedBigDecimal.floatValue();

					if (speed < Kbit)
						return String.valueOf(speed) + " bit/Sc";
					if (speed > Kbit && speed < Mbit)
						return String.valueOf(speed / Kbit) + " Kb/S";
					if (speed > Mbit && speed < Gbit)
						return String.valueOf(speed / Mbit) + " Mb/S";
					if (speed > Gbit)
						return String.valueOf(speed / Gbit) + " Gb/S";
				}
				return "???";

			} else {

				BigDecimal bitsBigDecimal = BigDecimal.valueOf(bytes);
				// long bits = bytes * 8;

				if (secondsBigDecimal.signum() == 1) {

					// float speed = bits / secs;
					BigDecimal speeedBigDecimal = bitsBigDecimal.divide(
							secondsBigDecimal, 2, RoundingMode.HALF_UP);

					long Kbit = 1024;
					long Mbit = Kbit * 1024;
					long Gbit = Mbit * 1024;

					float speed = speeedBigDecimal.floatValue();

					if (speed < Kbit)
						return String.valueOf(speed) + " Byte/Sc";
					if (speed > Kbit && speed < Mbit)
						return String.valueOf(speed / Kbit) + " KB/S";
					if (speed > Mbit && speed < Gbit)
						return String.valueOf(speed / Mbit) + " MB/S";
					if (speed > Gbit)
						return String.valueOf(speed / Gbit) + " GB/S";
				}
				return "???";

			}
		}
		return "###";
	}

	public void saveUrl(final String filename, final String urlString)
			throws MalformedURLException, IOException {
		BufferedInputStream in = null;
		FileOutputStream fout = null;
		try {
			in = new BufferedInputStream(new URL(urlString).openStream());
			fout = new FileOutputStream(filename);

			final byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1) {
				fout.write(data, 0, count);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (fout != null) {
				fout.close();
			}
		}
	}
}
