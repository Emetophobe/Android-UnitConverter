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
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.emetophobe.unitconverter.converters.Converter;
import com.emetophobe.unitconverter.converters.TemperatureConverter;


public class ConverterFragment extends ListFragment implements OnSharedPreferenceChangeListener {
	public static final String CONVERTER_TYPE = "converter_type";
	public static final String CONVERTER_NAME = "converter_name";

	private Spinner mUnitSpinner;
	private EditText mValueEdit;
	private ConverterAdapter mAdapter;

	private Converter mConverter;

	private PreferenceHelper mSharedPrefs;
	private int mPrecision;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_converter, container, false);
		mUnitSpinner = (Spinner) view.findViewById(R.id.unit_spinner);
		mValueEdit = (EditText) view.findViewById(R.id.value_edit);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Get the converter type and name.
		int converterType = getArguments().getInt(CONVERTER_TYPE);
		String converterName = getArguments().getString(CONVERTER_NAME);

		// Set up the shared preferences.
		mSharedPrefs = new PreferenceHelper(getActivity(), converterName);
		mSharedPrefs.registerListener(this);
		mPrecision = mSharedPrefs.getPrecision();

		// Set up the converter.
		if (converterType == Constants.TEMPERATURE) {
			mConverter = new TemperatureConverter(getActivity(), converterType);
		} else {
			mConverter = new Converter(getActivity(), converterType);
		}

		// Set up the list adapter.
		mAdapter = new ConverterAdapter(getActivity(), mConverter.getConversionList());
		setListAdapter(mAdapter);

		// Set up the unit spinner.
		ArrayAdapter<CharSequence> spinnerAdapter = new ArrayAdapter<CharSequence>(getActivity(),
				android.R.layout.simple_spinner_item, mConverter.getUnitNames());
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mUnitSpinner.setAdapter(spinnerAdapter);
		mUnitSpinner.setOnItemSelectedListener(mOnSpinnerItemSelected);

		// Set up the value edit text watcher.
		mValueEdit.addTextChangedListener(mOnValueEditTextChanged);

		// Restore the previous edit text value and spinner position.
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

	/**
	 * Generate the conversion data and update the adapter.
	 */
	private void updateListView() {
		// Get the unit type and value to be converted
		int sourceUnit = mUnitSpinner.getSelectedItemPosition();
		double value = MathUtils.parseDouble(mValueEdit.getText().toString());

		mConverter.generate(sourceUnit, value, mPrecision);
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * Update the listview when a spinner item is selected.
	 */
	private OnItemSelectedListener mOnSpinnerItemSelected = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			updateListView();
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// do nothing
		}
	};

	/**
	 * Update the listview when the value edit text is changed.
	 */
	private TextWatcher mOnValueEditTextChanged = new TextWatcher() {
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
	};
}
