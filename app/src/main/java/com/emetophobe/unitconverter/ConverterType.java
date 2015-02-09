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

public enum ConverterType {
	AREA(0), BYTES(1), DENSITY(2), LENGTH(3), MASS(4), TEMPERATURE(5), TIME(6), VOLUME(7);

	private int mValue;

	/**
	 * Construct a converter type with the specified value.
	 */
	ConverterType(int value) {
		mValue = value;
	}

	/**
	 * Returns the ConverterType that matches the specified value.
	 *
	 * @param value the specified value.
	 * @return The matching ConverterType.
	 * @throws IllegalArgumentException if the value is invalid.
	 */
	public static ConverterType fromInteger(int value) {
		for (ConverterType type : values()) {
			if (type.toInteger() == value) {
				return type;
			}
		}

		throw new IllegalArgumentException("Invalid index: " + value + ". Must be between 0 and " + (values().length - 1));
	}

	/**
	 * Returns the integer value of this type.
	 *
	 * @return the integer value.
	 */
	public int toInteger() {
		return mValue;
	}
}
