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

package net.coobird.thumbnailator.util.exif;

/**
 * Representation for the Orientation (Tag 274) in the Exif metadata, as
 * defined in Section 4.6.4 of the Exif Specification version 2.3.
 * 
 * @author coobird
 *
 */
public enum Orientation
{
	/**
	 * Orientation 1.
	 * <ul>
	 * <li>First row: visual top of the image</li>
	 * <li>First column: visual left-hand side of the image</li>
	 * </ul>
	 */
	TOP_LEFT(1),
	
	/**
	 * Orientation 2.
	 * <ul>
	 * <li>First row: visual top of the image</li>
	 * <li>First column: visual right-hand side of the image</li>
	 * </ul>
	 */
	TOP_RIGHT(2),
	
	/**
	 * Orientation 3.
	 * <ul>
	 * <li>First row: visual bottom of the image</li>
	 * <li>First column: visual right-hand side of the image</li>
	 * </ul>
	 */
	BOTTOM_RIGHT(3),
	
	/**
	 * Orientation 4.
	 * <ul>
	 * <li>First row: visual bottom of the image</li>
	 * <li>First column: visual left-hand side of the image</li>
	 * </ul>
	 */	
	BOTTOM_LEFT(4),
	
	/**
	 * Orientation 5.
	 * <ul>
	 * <li>First row: visual left-hand side of the image</li>
	 * <li>First column: visual top of the image</li>
	 * </ul>
	 */	
	LEFT_TOP(5),
	
	/**
	 * Orientation 6.
	 * <ul>
	 * <li>First row: visual right-hand side of the image</li>
	 * <li>First column: visual top of the image</li>
	 * </ul>
	 */	
	RIGHT_TOP(6),
	
	/**
	 * Orientation 7.
	 * <ul>
	 * <li>First row: visual right-hand side of the image</li>
	 * <li>First column: visual bottom of the image</li>
	 * </ul>
	 */	
	RIGHT_BOTTOM(7),
	
	/**
	 * Orientation 8.
	 * <ul>
	 * <li>First row: visual left-hand side of the image</li>
	 * <li>First column: visual bottom of the image</li>
	 * </ul>
	 */	
	LEFT_BOTTOM(8),
	;
	
	private int value;
	private Orientation(int value)
	{
		this.value = value;
	}

	/**
	 * Returns the {@link Orientation} corresponding to the given orientation
	 * value.
	 * 
	 * @param value		The orientation value.
	 * @return			{@link Orientation} corresponding to the orientation
	 * 					value. Return {@code null} if the given value does not
	 * 					correspond to a	valid {@link Orientation}.
	 */
	public static Orientation typeOf(int value)
	{
		for (Orientation orientation : Orientation.values())
		{
			if (orientation.value == value)
			{
				return orientation;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns a textual {@link String} reprensentation of this enum.
	 * @return			A textual representation of this enum.
	 */
	@Override
	public String toString()
	{
		return "Orientation [type=" + value + "]";
	}
}
