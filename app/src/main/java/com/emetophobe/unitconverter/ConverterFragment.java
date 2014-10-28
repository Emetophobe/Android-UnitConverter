/*
 * Copyright (C) 2013-2014 Mike Cunningham
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

import android.app.ListFragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.emetophobe.unitconverter.converters.Constants;
import com.emetophobe.unitconverter.converters.ConverterInterface;
import com.emetophobe.unitconverter.converters.GenericConverter;
import com.emetophobe.unitconverter.converters.TemperatureConverter;

import java.util.ArrayList;


public class ConverterFragment extends ListFragment implements OnSharedPreferenceChangeListener {
	public static final String CONVERTER_TYPE = "converter_type";
	public static final String CONVERTER_NAME = "converter_name";

	private Spinner mUnitSpinner;
	private EditText mValueEdit;
	private ConverterAdapter mAdapter;

	private ConverterInterface mConverter;

	private String[] mUnitNames;
	private Double[] mUnitValues;

	private PreferenceHelper mSharedPrefs;
	private int mPrecision;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_converter, container, false);
		mUnitSpinner = (Spinner) view.findViewById(R.id.unit_spinner);
		mValueEdit = (EditText) view.findViewById(R.id.value_edit);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Get the converter type and name
		int converterType = getArguments().getInt(CONVERTER_TYPE);
		String converterName = getArguments().getString(CONVERTER_NAME);

		setupConverter(converterType);

		// Setup the shared preferences
		mSharedPrefs = new PreferenceHelper(getActivity(), converterName);
		mSharedPrefs.registerListener(this);
		mPrecision = mSharedPrefs.getPrecision();

		// Create the list adapter
		mAdapter = new ConverterAdapter(getActivity());
		setListAdapter(mAdapter);

		// Setup the unit spinner
		ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<CharSequence>(getActivity(),
				android.R.layout.simple_spinner_item, mUnitNames);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mUnitSpinner.setAdapter(spinnerAdapter);
		mUnitSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				updateListView();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// do nothing
			}
		});

		// Setup the value edit
		mValueEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				updateListView();
			}

			@Override
			public void afterTextChanged(Editable s) {
				// do nothing
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// do nothing
			}
		});

		// Restore the previous unit value and spinner position
		mValueEdit.setText(mSharedPrefs.getValue());
		mUnitSpinner.setSelection(mSharedPrefs.getIndex());
	}

	@Override
	public void onPause() {
		super.onPause();
		// Save the current unit type and value
		mSharedPrefs.setValue(mValueEdit.getText().toString());
		mSharedPrefs.setIndex(mUnitSpinner.getSelectedItemPosition());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mSharedPrefs.unregisterListener(this);
	}

	/**
	 * Update the conversion list whenever the precision preference is changed.
	 */
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		mPrecision = mSharedPrefs.getPrecision();
		updateListView();
	}

	private void updateListView() {
		// Get the unit type and value to be converted
		int sourceUnit = mUnitSpinner.getSelectedItemPosition();
		double value = MathUtils.parseDouble(mValueEdit.getText().toString());

		// Create the conversion list
		ArrayList<Pair<String, Double>> list = new ArrayList<Pair<String, Double>>();
		for (int destUnit = 0; destUnit < mUnitNames.length; destUnit++) {
			// ignore identical unit types
			if (sourceUnit != destUnit) {
				list.add(new Pair<String, Double>(mUnitNames[destUnit],
						mConverter.convert(mUnitValues, sourceUnit, destUnit, value, mPrecision)));
			}
		}

		// Update the adapter with the converter list
		mAdapter.setData(list);
	}

	private void setupConverter(int converterType) {
		if (converterType == Constants.TEMPERATURE) {
			mConverter = new TemperatureConverter();
		} else {
			mConverter = new GenericConverter();
		}

		switch (converterType) {
			case Constants.AREA:
				setUnitNames(R.array.area_names);
				setUnitValues(R.array.area_units);
				break;
			case Constants.BYTES:
				setUnitNames(R.array.bytes_names);
				setUnitValues(R.array.bytes_units);
				break;
			case Constants.DENSITY:
				setUnitNames(R.array.density_names);
				setUnitValues(R.array.density_units);
				break;
			case Constants.LENGTH:
				setUnitNames(R.array.length_names);
				setUnitValues(R.array.length_units);
				break;
			case Constants.MASS:
				setUnitNames(R.array.mass_names);
				setUnitValues(R.array.mass_units);
				break;
			case Constants.TEMPERATURE:
				setUnitNames(R.array.temperature_names);
				mUnitValues = null;    // temperature is a special case and doesn't use a values array
				break;
			case Constants.TIME:
				setUnitNames(R.array.time_names);
				setUnitValues(R.array.time_units);
				break;
			case Constants.VOLUME:
				setUnitNames(R.array.volume_names);
				setUnitValues(R.array.volume_units);
				break;
		}
	}

	private void setUnitNames(int resourceId) {
		mUnitNames = getResources().getStringArray(resourceId);
	}

	private void setUnitValues(int resourceId) {
		// Convert the string-array resource to a Double array
		String[] strings = getResources().getStringArray(resourceId);
		mUnitValues = new Double[strings.length];
		for (int i = 0; i < strings.length; i++) {
			mUnitValues[i] = Double.valueOf(strings[i]);
		}
	}
}
