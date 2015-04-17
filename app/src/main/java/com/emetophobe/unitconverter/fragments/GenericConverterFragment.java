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

package com.emetophobe.unitconverter.fragments;

import android.app.ListFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.ArrayRes;
import android.support.annotation.NonNull;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.emetophobe.unitconverter.ConverterType;
import com.emetophobe.unitconverter.MathUtils;
import com.emetophobe.unitconverter.R;
import com.emetophobe.unitconverter.adapters.ConverterAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;


public class GenericConverterFragment extends ListFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
	public static final String EXTRA_CONVERTER_TYPE = "converter_type";
	private static final String PREF_PRECISION = "pref_precision";
	private static final String DEFAULT_PRECISION = "5";

	private ConverterType mConverterType;
	private SharedPreferences mSharedPrefs;

	protected String[] mUnitNames;
	protected Double[] mUnitValues;
	protected int mPrecision;
	private List<Pair<String, Double>> mConversionList = new ArrayList<>();

	private ConverterAdapter mAdapter;

	@InjectView(R.id.unit_spinner)
	protected Spinner mUnitSpinner;

	@InjectView(R.id.value_edit)
	protected EditText mValueEdit;


	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_converter, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ButterKnife.inject(this, view);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.reset(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Get the converter type and load the unit names and values
		mConverterType = (ConverterType) getArguments().getSerializable(EXTRA_CONVERTER_TYPE);
		mUnitNames = getUnitNames();
		mUnitValues = getUnitValues();

		// Set up the shared preferences.
		mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		mSharedPrefs.registerOnSharedPreferenceChangeListener(this);
		updatePrecision();

		// Set up the list adapter.
		mAdapter = new ConverterAdapter(getActivity(), mConversionList);
		setListAdapter(mAdapter);

		// Set up the unit spinner.
		ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<CharSequence>(getActivity(),
				android.R.layout.simple_spinner_item, mUnitNames);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mUnitSpinner.setAdapter(spinnerAdapter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mSharedPrefs.unregisterOnSharedPreferenceChangeListener(this);
	}

	/**
	 * Update the conversion list whenever the precision preference is changed.
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals(PREF_PRECISION)) {
			updatePrecision();
			updateListView();
		}
	}

	/**
	 * Called when the unit Spinner is changed.
	 */
	@OnItemSelected(R.id.unit_spinner)
	protected void onItemSelected(int position) {
		updateListView();
	}

	/**
	 * Called when the value EditText is changed.
	 */
	@OnTextChanged(R.id.value_edit)
	protected void onTextChanged(CharSequence text) {
		updateListView();
	}

	/**
	 * Stores the precision preference.
	 */
	private void updatePrecision() {
		mPrecision = MathUtils.parseInt(mSharedPrefs.getString(PREF_PRECISION, DEFAULT_PRECISION));
	}

	/**
	 * Generate the conversion data and update the adapter.
	 */
	private void updateListView() {
		// Get the unit type and value to be converted.
		int sourceUnit = mUnitSpinner.getSelectedItemPosition();
		double value = MathUtils.parseDouble(mValueEdit.getText().toString());

		// Create the conversion list
		mConversionList.clear();
		for (int destUnit = 0; destUnit < mUnitNames.length; destUnit++) {
			// ignore identical unit types
			if (sourceUnit != destUnit) {
				mConversionList.add(new Pair<String, Double>(mUnitNames[destUnit], convert(sourceUnit, destUnit, value)));
			}
		}

		mAdapter.notifyDataSetChanged();
	}

	/**
	 * Convert from one unit type to another.
	 *
	 * @param sourceUnit The source unit type.
	 * @param destUnit   The destination unit type.
	 * @param value      The number to convert.
	 * @return The converted result.
	 */
	protected Double convert(int sourceUnit, int destUnit, double value) {
		// Just return the value if the units are the same or if the value is 0
		if (sourceUnit == destUnit || value == 0.0) {
			return value;
		}

		// Get the unit conversion values
		double unitFrom = mUnitValues[sourceUnit];
		double unitTo = mUnitValues[destUnit];

		// Perform the conversion
		value = value * unitFrom;
		value = value / unitTo;

		// Return the rounded result
		return MathUtils.round(value, mPrecision);
	}

	/**
	 * Returns a String array of unit names for the current converter type.
	 */
	protected String[] getUnitNames() {
		switch (mConverterType) {
			case AREA:
				return getResources().getStringArray(R.array.area_names);
			case BYTES:
				return getResources().getStringArray(R.array.bytes_names);
			case DENSITY:
				return getResources().getStringArray(R.array.density_names);
			case LENGTH:
				return getResources().getStringArray(R.array.length_names);
			case MASS:
				return getResources().getStringArray(R.array.mass_names);
			case TIME:
				return getResources().getStringArray(R.array.time_names);
			case VOLUME:
				return getResources().getStringArray(R.array.volume_names);
			default:
				throw new IllegalArgumentException("Unknown converter type: " + mConverterType);
		}
	}

	/**
	 * Returns a Double array of unit values for the current converter type.
	 */
	protected Double[] getUnitValues() {
		switch (mConverterType) {
			case AREA:
				return getUnitValues(R.array.area_units);
			case BYTES:
				return getUnitValues(R.array.bytes_units);
			case DENSITY:
				return getUnitValues(R.array.density_units);
			case LENGTH:
				return getUnitValues(R.array.length_units);
			case MASS:
				return getUnitValues(R.array.mass_units);
			case TIME:
				return getUnitValues(R.array.time_units);
			case VOLUME:
				return getUnitValues(R.array.volume_units);
			default:
				throw new IllegalArgumentException("Unknown converter type: " + mConverterType);
		}
	}

	/**
	 * Returns a Double array from the specified string array resource identifier.
	 *
	 * @param resourceId The string array resource identifier.
	 * @return The Double array of unit values.
	 */
	private Double[] getUnitValues(@ArrayRes int resourceId) {
		String[] strings = getResources().getStringArray(resourceId);
		Double[] values = new Double[strings.length];
		for (int i = 0; i < strings.length; i++) {
			values[i] = Double.valueOf(strings[i]);
		}
		return values;
	}
}
