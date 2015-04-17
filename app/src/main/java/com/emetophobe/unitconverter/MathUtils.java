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

import android.util.Log;


public class MathUtils {
	/**
	 * This class cannot be instantiated
	 */
	private MathUtils() {

	}

	/**
	 * Round a number to n decimal places.
	 *
	 * @param number The number to round.
	 * @param n      The number of decimal places.
	 * @return The rounded number.
	 */
	public static double round(double number, int n) {
		double pow = Math.pow(10, n);
		return Math.round(number * pow) / pow;
	}

	/**
	 * Convert a string to an integer.
	 *
	 * @param text The string to convert.
	 * @return the integer value if successful, 0 if there was an error.
	 */
	public static int parseInt(String text) {
		int value = 0;
		try {
			value = Integer.valueOf(text);
		} catch (NumberFormatException e) {
			Log.d("MathUtils", "NumberFormatException when attempting to parse an integer from the string: " + text);
		}
		return value;
	}

	/**
	 * Convert a string to a double.
	 *
	 * @param text The string to convert.
	 * @return the double value if successful, 0 if there was an error.
	 */
	public static double parseDouble(String text) {
		double value = 0;
		try {
			value = Double.valueOf(text);
		} catch (NumberFormatException e) {
			Log.d("MathUtils", "NumberFormatException when attempting to parse a double from the string: " + text);
		}
		return value;
	}
}
