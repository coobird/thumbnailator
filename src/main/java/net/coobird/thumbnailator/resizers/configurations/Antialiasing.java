/*
 * Thumbnailator - a thumbnail generation library
 *
 * Copyright (c) 2008-2020 Chris Kroells
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

/**
 * 
 */
package net.coobird.thumbnailator.resizers.configurations;

import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;

/**
 * An enum which is used to specify the antialiasing settings of the
 * resizing operations.
 * 
 * @author coobird
 *
 */
public enum Antialiasing implements ResizerConfiguration {
	/**
	 * A hint to enable antialiasing.
	 */
	ON(RenderingHints.VALUE_ANTIALIAS_ON),
	
	/**
	 * A hint to disable antialiasing.
	 */
	OFF(RenderingHints.VALUE_ANTIALIAS_OFF),
	
	/**
	 * A hint to use the default antialiasing settings.
	 */
	DEFAULT(RenderingHints.VALUE_ANTIALIAS_DEFAULT),
	;
	
	/**
	 * The field used to hold the rendering hint.
	 */
	private final Object value;
	
	/**
	 * Instantiates this enum.
	 * 
	 * @param value		The rendering hint value.
	 */
	private Antialiasing(Object value) {
		this.value = value;
	}

	public Key getKey() {
		return RenderingHints.KEY_ANTIALIASING;
	}

	public Object getValue() {
		return value;
	}
}