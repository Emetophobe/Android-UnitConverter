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

import com.emetophobe.unitconverter.MathUtils;


public class GenericConverter implements ConverterInterface {
	@Override
	public Double convert(Double[] units, int sourceUnit, int destUnit, double value, int precision) {
		// Just return the value if the units are the same or if the value is 0
		if (sourceUnit == destUnit || value == 0.0) {
			return value;
		}

		// Get the unit conversion values
		double unitFrom = units[sourceUnit];
		double unitTo = units[destUnit];

		// Perform the conversion
		value = value * unitFrom;
		value = value / unitTo;

		// Return the rounded result
		return MathUtils.round(value, precision);
	}
}
