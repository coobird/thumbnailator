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
 * This interface is implemented by classes which calculate how to position an
 * object inside of an enclosing object.
 * 
 * @author coobird
 *
 */
public interface Position {
	/**
	 * Calculates the position of an object enclosed by an enclosing object.
	 * 
	 * @param enclosingWidth		The width of the enclosing object that is
	 * 								to contain the enclosed object.
	 * @param enclosingHeight		The height of the enclosing object that is
	 * 								to contain the enclosed object.
	 * @param width					The width of the object that is to be
	 * 								placed inside an enclosing object.
	 * @param height				The height of the object that is to be
	 * 								placed inside an enclosing object.
	 * @param insetLeft				The inset on the left-hand side of the
	 * 								object to be enclosed.
	 * @param insetRight			The inset on the right-hand side of the
	 * 								object to be enclosed.
	 * @param insetTop				The inset on the top side of the
	 * 								object to be enclosed.
	 * @param insetBottom			The inset on the bottom side of the
	 * 								object to be enclosed.
	 * @return						The position to place the object.
	 */
	public Point calculate(
			int enclosingWidth, int enclosingHeight, int width,	int height,
			int insetLeft, int insetRight, int insetTop, int insetBottom
	);
}
