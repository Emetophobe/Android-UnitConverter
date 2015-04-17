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

import com.emetophobe.unitconverter.MathUtils;
import com.emetophobe.unitconverter.R;


public class TemperatureConverterFragment extends GenericConverterFragment {

	/**
	 * Returns the String array of unit names for the temperature converter.
	 */
	@Override
	protected String[] getUnitNames() {
		return getResources().getStringArray(R.array.temperature_names);
	}

	/**
	 * The temperature converter doesn't use the unit values array, so just return null.
	 */
	@Override
	protected Double[] getUnitValues() {
		return null;
	}

	/**
	 * Convert from one temperature unit type to another.
	 *
	 * @param sourceUnit The source unit type.
	 * @param destUnit   The destination unit type.
	 * @param value      The number to convert.
	 * @return The converted result.
	 */
	@Override
	protected Double convert(int sourceUnit, int destUnit, double value) {
		// Just return the value if the unit types are the same, or if the value is 0
		if (sourceUnit == destUnit || value == 0.0) {
			return value;
		}

		value = toKelvin(sourceUnit, value); // First convert the value to kelvin
		value = fromKelvin(destUnit, value); // Then convert the value from kelvin to the desired unit

		// Round the result
		return MathUtils.round(value, mPrecision);
	}

	/**
	 * Convert the unit to kelvin.
	 *
	 * @param sourceUnit The source unit type.
	 * @param value      The number to convert.
	 * @return The converted number.
	 */
	private double toKelvin(int sourceUnit, double value) {
		switch (sourceUnit) {
			case 0: // celsius to kelvin
				return value + 273.15;
			case 1: // fahrenheit to kelvin
				return (value + 459.67) * 5 / 9;
			case 3: // rankine to kelvin
				return value * 5 / 9;
			default:
				return value;
		}
	}

	/**
	 * Convert the unit from kelvin.
	 *
	 * @param destUnit The destination unit type.
	 * @param value    The number to convert.
	 * @return The converted number.
	 */
	private double fromKelvin(int destUnit, double value) {
		switch (destUnit) {
			case 0: // kelvin to celsius
				return value - 273.15;
			case 1: // kelvin to fahrenheit
				return value * 9 / 5 - 459.67;
			case 3: // kelvin to rankine
				return value * 9 / 5;
			default:
				return value;
		}
	}
}
