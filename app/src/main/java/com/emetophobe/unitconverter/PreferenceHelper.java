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
	private SharedPreferences mUnitPrefs;

	public PreferenceHelper(Context context, String converterName) {
		mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
		mUnitPrefs = context.getSharedPreferences(converterName + "Prefs", 0);
	}

	public int getPrecision() {
		return MathUtils.parseInt(mSharedPrefs.getString(PREF_PRECISION, DEFAULT_PRECISION));
	}

	public String getValue() {
		return mUnitPrefs.getString(PREF_VALUE, DEFAULT_VALUE);
	}

	public void setValue(String value) {
		SharedPreferences.Editor editor = mUnitPrefs.edit();
		editor.putString(PREF_VALUE, value);
		editor.apply();
	}

	public int getIndex() {
		return mUnitPrefs.getInt(PREF_INDEX, 0);
	}

	public void setIndex(int index) {
		SharedPreferences.Editor editor = mUnitPrefs.edit();
		editor.putInt(PREF_INDEX, index);
		editor.apply();
	}

	public void registerListener(OnSharedPreferenceChangeListener listener) {
		mSharedPrefs.registerOnSharedPreferenceChangeListener(listener);
	}

	public void unregisterListener(OnSharedPreferenceChangeListener listener) {
		mSharedPrefs.unregisterOnSharedPreferenceChangeListener(listener);
	}
}
