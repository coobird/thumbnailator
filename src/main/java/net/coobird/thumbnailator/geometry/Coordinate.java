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

package net.coobird.thumbnailator.geometry;

import java.awt.Point;

/**
 * This class calculates the position of an image which is to be enclosed,
 * using an absolute coordinate at which the image should be located.
 * 
 * @author coobird
 *
 */
public final class Coordinate implements Position {
	/**
	 * The horizontal position of the image to be enclosed.
	 */
	private final int x;
	
	/**
	 * The vertical position of the image to be enclosed.
	 */
	private final int y;
	
	/**
	 * Instantiates an object which calculates the position of an image, using
	 * the given coordinates.
	 * 
	 * @param x			The horizontal component of the top-left corner of the
	 * 					image to be enclosed.
	 * @param y			The vertical component of the top-left corner of the
	 * 					image to be enclosed.
	 */
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point calculate(int enclosingWidth, int enclosingHeight, int width,
			int height, int insetLeft, int insetRight, int insetTop,
			int insetBottom) {

		int x = this.x + insetLeft;
		int y = this.y + insetTop;
		
		return new Point(x, y);
	}
}
