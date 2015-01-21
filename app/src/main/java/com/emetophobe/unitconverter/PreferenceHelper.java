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

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;


public class PreferenceHelper {
	private static final String PREF_PRECISION = "pref_precision";
	private static final String PREF_VALUE = "pref_value";
	private static final String PREF_INDEX = "pref_index";

	private static final String DEFAULT_VALUE = "1.0";
	private static final String DEFAULT_PRECISION = "5";

	private SharedPreferences mSharedPrefs;
	private SharedPreferences mConverterPrefs;

	public PreferenceHelper(Context context, String converterName) {
		mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		mConverterPrefs = context.getSharedPreferences(converterName + "Prefs", 0);
	}

	/**
	 * Returns the precision preference.
	 *
	 * @return an integer value between 0 and 10.
	 */
	public int getPrecision() {
		return MathUtils.parseInt(mSharedPrefs.getString(PREF_PRECISION, DEFAULT_PRECISION));
	}

	/**
	 * Returns the saved unit value (as a string).
	 *
	 * @return The string value.
	 */
	public String getValue() {
		return mConverterPrefs.getString(PREF_VALUE, DEFAULT_VALUE);
	}

	/**
	 * Saves the converter unit value.
	 *
	 * @param value The string value.
	 */
	public void setValue(String value) {
		SharedPreferences.Editor editor = mConverterPrefs.edit();
		editor.putString(PREF_VALUE, value);
		editor.apply();
	}

	/**
	 * Returns the saved spinner position.
	 *
	 * @return The spinner position.
	 */
	public int getIndex() {
		return mConverterPrefs.getInt(PREF_INDEX, 0);
	}

	/**
	 * Saves the spinner position.
	 *
	 * @param index The spinner position.
	 */
	public void setIndex(int index) {
		SharedPreferences.Editor editor = mConverterPrefs.edit();
		editor.putInt(PREF_INDEX, index);
		editor.apply();
	}

	/**
	 * Registers a shared preference listener.
	 *
	 * @param listener The callback reference.
	 */
	public void registerListener(OnSharedPreferenceChangeListener listener) {
		mSharedPrefs.registerOnSharedPreferenceChangeListener(listener);
	}

	/**
	 * Unregisters a shared preference listener.
	 *
	 * @param listener The callback reference.
	 */
	public void unregisterListener(OnSharedPreferenceChangeListener listener) {
		mSharedPrefs.unregisterOnSharedPreferenceChangeListener(listener);
	}
}
