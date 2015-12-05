/*
 * Copyright (C) 2013 Andreas Stuetz <andreas.stuetz@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.serveroverload.networkmaster.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.serveroverload.networkmaster.R;
import com.serveroverload.networkmaster.ui.adapter.PageStripPagerAdapter;

// TODO: Auto-generated Javadoc
/**
 * The Class TabbedProductListFragment.
 */
public class TabListFragment extends Fragment {

	/** The tabs. */
	private PagerSlidingTabStrip tabs;

	/** The pager. */
	private ViewPager pager;

	/** The adapter. */
	private PageStripPagerAdapter adapter;

	/** The Constant ARG_POSITION. */
	private static final String ARG_POSITION = "position";

	/** The position. */
	private int position;

	/**
	 * New instance.
	 *
	 * @param position
	 *            the position
	 * @return the super awesome card fragment
	 */
	public static TabListFragment newInstance(int position) {
		TabListFragment f = new TabListFragment();
		Bundle b = new Bundle();
		b.putInt(ARG_POSITION, position);
		f.setArguments(b);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(null!=getArguments())
		position = getArguments().getInt(ARG_POSITION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_main, container,
				false);

		tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
		pager = (ViewPager) rootView.findViewById(R.id.pager);
		adapter = new PageStripPagerAdapter(getChildFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
						.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);

		pager.setCurrentItem(position, true);
		// changeColor(currentColor);

		return rootView;
	}
}