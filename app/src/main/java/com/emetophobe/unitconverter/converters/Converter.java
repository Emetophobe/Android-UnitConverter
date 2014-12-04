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

package com.emetophobe.unitconverter.converters;

import android.content.Context;
import android.util.Pair;

import com.emetophobe.unitconverter.Constants;
import com.emetophobe.unitconverter.MathUtils;
import com.emetophobe.unitconverter.R;

import java.util.ArrayList;


public class Converter {
	protected String[] mUnitNames;
	protected Double[] mUnitValues;

	protected ArrayList<Pair<String, Double>> mConversionList;

	/**
	 * Construct the converter.
	 *
	 * @param context       The context.
	 * @param converterType The converter type constant.
	 */
	public Converter(Context context, int converterType) {
		mConversionList = new ArrayList<Pair<String, Double>>();

		// Load the unit names and values
		switch (converterType) {
			case Constants.AREA:
				mUnitNames = context.getResources().getStringArray(R.array.area_names);
				mUnitValues = getUnitValues(context, R.array.area_units);
				break;

			case Constants.BYTES:
				mUnitNames = context.getResources().getStringArray(R.array.bytes_names);
				mUnitValues = getUnitValues(context, R.array.bytes_units);
				break;

			case Constants.DENSITY:
				mUnitNames = context.getResources().getStringArray(R.array.density_names);
				mUnitValues = getUnitValues(context, R.array.density_units);
				break;

			case Constants.LENGTH:
				mUnitNames = context.getResources().getStringArray(R.array.length_names);
				mUnitValues = getUnitValues(context, R.array.length_units);
				break;

			case Constants.MASS:
				mUnitNames = context.getResources().getStringArray(R.array.mass_names);
				mUnitValues = getUnitValues(context, R.array.mass_units);
				break;

			case Constants.TEMPERATURE:
				mUnitNames = context.getResources().getStringArray(R.array.temperature_names);
				mUnitValues = null;    // temperature is a special case and doesn't use a values array
				break;

			case Constants.TIME:
				mUnitNames = context.getResources().getStringArray(R.array.time_names);
				mUnitValues = getUnitValues(context, R.array.time_units);
				break;

			case Constants.VOLUME:
				mUnitNames = context.getResources().getStringArray(R.array.volume_names);
				mUnitValues = getUnitValues(context, R.array.volume_units);
				break;
		}
	}

	/**
	 * Generate the conversion list.
	 *
	 * @param sourceUnit The source unit type.
	 * @param value      The number to convert.
	 * @param precision  The decimal precision to round to.
	 */
	public void generate(int sourceUnit, double value, int precision) {
		mConversionList.clear();

		// Create the conversion list
		for (int destUnit = 0; destUnit < mUnitNames.length; destUnit++) {
			// ignore identical unit types
			if (sourceUnit != destUnit) {
				mConversionList.add(new Pair<String, Double>(mUnitNames[destUnit], convert(sourceUnit, destUnit, value, precision)));
			}
		}
	}

	/**
	 * Convert from one unit to another.
	 *
	 * @param sourceUnit The source unit type.
	 * @param destUnit   The destination unit type.
	 * @param value      The number to convert.
	 * @param precision  The decimal precision to round to.
	 * @return The converted number.
	 */
	protected Double convert(int sourceUnit, int destUnit, double value, int precision) {
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
		return MathUtils.round(value, precision);
	}


	/**
	 * Load the unit values string array and convert it to a Double array.
	 *
	 * @param context    The context.
	 * @param resourceId The string array resource identifier.
	 * @return The double array.
	 */
	private Double[] getUnitValues(Context context, int resourceId) {
		String[] strings = context.getResources().getStringArray(resourceId);
		Double[] values = new Double[strings.length];
		for (int i = 0; i < strings.length; i++) {
			values[i] = Double.valueOf(strings[i]);
		}
		return values;
	}

	/**
	 * Returns the list of unit name.
	 *
	 * @return The string array of unit names.
	 */
	public String[] getUnitNames() {
		return mUnitNames;
	}

	/**
	 * Returns the conversion list.
	 *
	 * @return The array of conversions.
	 */
	public ArrayList<Pair<String, Double>> getConversionList() {
		return mConversionList;
	}
}
