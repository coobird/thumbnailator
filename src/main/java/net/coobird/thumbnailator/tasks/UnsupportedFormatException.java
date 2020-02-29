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

package net.coobird.thumbnailator.tasks;

import java.io.IOException;

/**
 * An exception used to indicate that the specified format could not be
 * used in an operation.
 * 
 * @author coobird
 *
 */
public class UnsupportedFormatException extends IOException {
	/**
	 * An ID used for serialization.
	 */
	private static final long serialVersionUID = 1254432584303852552L;
	
	/**
	 * The format name which was not supported.
	 */
	private final String formatName;

	/**
	 * A constant which is used to indicate an unknown format.
	 */
	public static final String UNKNOWN = "<unknown>";

	/**
	 * Instantiates a {@link UnsupportedFormatException} with the unsupported
	 * format.
	 * 
	 * @param formatName	Format name.
	 */
	public UnsupportedFormatException(String formatName) {
		super();
		this.formatName = formatName;
	}

	/**
	 * Instantiates a {@link UnsupportedFormatException} with the unsupported
	 * format and a detailed message.
	 * 
	 * @param formatName	Format name.
	 * @param s				A message detailing the exception.
	 */
	public UnsupportedFormatException(String formatName, String s) {
		super(s);
		this.formatName = formatName;
	}

	/**
	 * Returns the format name which is not supported.
	 * 
	 * @return			Format name.
	 */
	public String getFormatName() {
		return formatName;
	}
}
