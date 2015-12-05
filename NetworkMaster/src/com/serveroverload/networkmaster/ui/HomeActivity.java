package com.serveroverload.networkmaster.ui;

import com.serveroverload.networkmaster.R;
import com.serveroverload.networkmaster.R.id;
import com.serveroverload.networkmaster.R.layout;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;

public class HomeActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_root);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		TabListFragment fragment = new TabListFragment();
		transaction.replace(R.id.pager, fragment);
		transaction.commit();

	}

}
