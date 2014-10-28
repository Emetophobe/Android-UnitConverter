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

import android.util.Log;


public class MathUtils {
	/**
	 * Round a number to n decimal places.
	 */
	public static double round(double number, int n) {
		double pow = Math.pow(10, n);
		return Math.round(number * pow) / pow;
	}

	/**
	 * Convert a string to an integer.
	 */
	public static int parseInt(String s) {
		int value = 0;
		try {
			value = Integer.valueOf(s);
		} catch (NumberFormatException e) {
			Log.d("MathUtils", "NumberFormatException when attempting to parse an integer from the string: " + s);
		}
		return value;
	}

	/**
	 * Convert a string to a double.
	 */
	public static double parseDouble(String s) {
		double value = 0;
		try {
			value = Double.valueOf(s);
		} catch (NumberFormatException e) {
			Log.d("MathUtils", "NumberFormatException when attempting to parse a double from the string: " + s);
		}
		return value;
	}
}
