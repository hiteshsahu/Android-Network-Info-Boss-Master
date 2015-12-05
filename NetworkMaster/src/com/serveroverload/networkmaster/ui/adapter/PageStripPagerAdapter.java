package com.serveroverload.networkmaster.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.serveroverload.networkmaster.ui.NetworkInfoFragment;
import com.serveroverload.networkmaster.ui.ProcessListFragment;
import com.serveroverload.networkmaster.ui.TelephonyInfoFragment;
import com.serveroverload.networkmaster.ui.WifiListFragment;

// TODO: Auto-generated Javadoc
/**
 * The Class PageStripPagerAdapter.
 */
public class PageStripPagerAdapter extends FragmentPagerAdapter {

	/**
	 * Instantiates a new page strip pager adapter.
	 *
	 * @param fm
	 *            the fm
	 */
	public PageStripPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
	 */
	@Override
	public CharSequence getPageTitle(int position) {

		return titles[position];
	}

	String[] titles = { "Network Info", "WiFi info", "Mobile Data", "Processes" };

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return 4;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	 */
	@Override
	public Fragment getItem(int position) {

		if (position == 0)
			return new NetworkInfoFragment();

		else if (position == 1) {

			return new WifiListFragment();

		} else if (position == 2) {
			return new TelephonyInfoFragment();
		} else {
			return new ProcessListFragment();
		}
	}

}