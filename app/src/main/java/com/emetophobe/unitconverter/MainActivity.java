/*
 * Copyright (C) 2013-2015 Mike Cunningham
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.emetophobe.unitconverter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.emetophobe.unitconverter.fragments.GenericConverterFragment;
import com.emetophobe.unitconverter.fragments.NavDrawerFragment;
import com.emetophobe.unitconverter.fragments.TemperatureConverterFragment;


public class MainActivity extends ActionBarActivity implements NavDrawerFragment.NavigationDrawerCallbacks {
	private NavDrawerFragment mNavDrawerFragment;

	private String[] mConverterNames;
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the toolbar.
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// Set up the navigation drawer.
		mNavDrawerFragment = (NavDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mNavDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

		mConverterNames = getResources().getStringArray(R.array.converter_names);
		mTitle = getTitle();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main_menu, menu);
			ActionBar actionBar = getSupportActionBar();
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setTitle(mTitle);
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				startActivity(new Intent(this, SettingsActivity.class));
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		mTitle = mConverterNames[position];

		GenericConverterFragment fragment;
		if (ConverterType.fromInteger(position) == ConverterType.TEMPERATURE) {
			fragment = new TemperatureConverterFragment();
		} else {
			fragment = new GenericConverterFragment();
		}

		Bundle args = new Bundle();
		args.putInt(GenericConverterFragment.EXTRA_CONVERTER_TYPE, position);
		fragment.setArguments(args);

		getFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
	}
}