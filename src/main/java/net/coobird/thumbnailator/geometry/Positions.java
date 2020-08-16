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

import net.coobird.thumbnailator.filters.Caption;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.filters.Watermark;

/**
 * An enum of predefined {@link Position}s.
 * <p>
 * Primary use of this enum is for selecting a position to place watermarks
 * (using the {@link Watermark} class), captions (using the {@link Caption}
 * class) and other {@link ImageFilter}s.
 * 
 * @author coobird
 *
 */
public enum Positions implements Position {
	/**
	 * Calculates the {@link Point} at which an enclosed image should be placed
	 * if it is to be placed at the top left-hand corner of the enclosing
	 * image.
	 */
	TOP_LEFT() {
		public Point calculate(int enclosingWidth, int enclosingHeight,
				int width, int height, int insetLeft, int insetRight,
				int insetTop, int insetBottom) {

			int x = insetLeft;
			int y = insetTop;
			return new Point(x, y);
		}
	},
	
	/**
	 * Calculates the {@link Point} at which an enclosed image should be placed
	 * if it is to be horizontally centered at the top of the enclosing image.
	 */
	TOP_CENTER() {
		public Point calculate(int enclosingWidth, int enclosingHeight,
				int width, int height, int insetLeft, int insetRight,
				int insetTop, int insetBottom) {

			int x = (enclosingWidth / 2) - (width / 2);
			int y = insetTop;
			return new Point(x, y);
		}
	},
	
	/**
	 * Calculates the {@link Point} at which an enclosed image should be placed
	 * if it is to be placed at the top right-hand corner of the enclosing
	 * image.
	 */
	TOP_RIGHT() {
		public Point calculate(int enclosingWidth, int enclosingHeight,
				int width, int height, int insetLeft, int insetRight,
				int insetTop, int insetBottom) {

			int x = enclosingWidth - width - insetRight;
			int y = insetTop;
			return new Point(x, y);
		}
	},
	
	/**
	 * Calculates the {@link Point} at which an enclosed image should be placed
	 * if it is to be placed vertically centered at the left-hand corner of
	 * the enclosing image.
	 */
	CENTER_LEFT() {
		public Point calculate(int enclosingWidth, int enclosingHeight,
				int width, int height, int insetLeft, int insetRight,
				int insetTop, int insetBottom) {

			int x = insetLeft;
			int y = (enclosingHeight / 2) - (height / 2);
			return new Point(x, y);
		}
	},
	
	/**
	 * Calculates the {@link Point} at which an enclosed image should be placed
	 * horizontally and vertically centered in the enclosing image.
	 */
	CENTER()
	{
		public Point calculate(int enclosingWidth, int enclosingHeight,
				int width, int height, int insetLeft, int insetRight,
				int insetTop, int insetBottom) {

			int x = (enclosingWidth / 2) - (width / 2);
			int y = (enclosingHeight / 2) - (height / 2);
			return new Point(x, y);
		}
	},

	/**
	 * Calculates the {@link Point} at which an enclosed image should be placed
	 * if it is to be placed vertically centered at the right-hand corner of
	 * the enclosing image.
	 */
	CENTER_RIGHT() {
		public Point calculate(int enclosingWidth, int enclosingHeight,
				int width, int height, int insetLeft, int insetRight,
				int insetTop, int insetBottom) {

			int x = enclosingWidth - width - insetRight;
			int y = (enclosingHeight / 2) - (height / 2);
			return new Point(x, y);
		}
	},
	
	/**
	 * Calculates the {@link Point} at which an enclosed image should be placed
	 * if it is to be placed at the bottom left-hand corner of the enclosing
	 * image.
	 */
	BOTTOM_LEFT() {
		public Point calculate(int enclosingWidth, int enclosingHeight,
				int width, int height, int insetLeft, int insetRight,
				int insetTop, int insetBottom) {

			int x = insetLeft;
			int y = enclosingHeight - height - insetBottom;
			return new Point(x, y);
		}
	},
	
	/**
	 * Calculates the {@link Point} at which an enclosed image should be placed
	 * if it is to be horizontally centered at the bottom of the enclosing
	 * image.
	 */
	BOTTOM_CENTER() {
		public Point calculate(int enclosingWidth, int enclosingHeight,
				int width, int height, int insetLeft, int insetRight,
				int insetTop, int insetBottom) {

			int x = (enclosingWidth / 2) - (width / 2);
			int y = enclosingHeight - height - insetBottom;
			return new Point(x, y);
		}
	},
	
	/**
	 * Calculates the {@link Point} at which an enclosed image should be placed
	 * if it is to be placed at the bottom right-hand corner of the enclosing
	 * image.
	 */	
	BOTTOM_RIGHT() {
		public Point calculate(int enclosingWidth, int enclosingHeight,
				int width, int height, int insetLeft, int insetRight,
				int insetTop, int insetBottom) {

			int x = enclosingWidth - width - insetRight;
			int y = enclosingHeight - height - insetBottom;
			return new Point(x, y);
		}
	},
	;
}
