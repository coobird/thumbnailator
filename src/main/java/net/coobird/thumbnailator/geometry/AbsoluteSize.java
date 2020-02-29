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

import java.awt.Dimension;

/**
 * A {@link Size} object which indicates that the size of the enclosed object
 * should be the specified absolute size.
 * 
 * @author coobird
 * @since	0.3.4
 *
 */
public class AbsoluteSize implements Size {
	/**
	 * The size of the object.
	 */
	private final Dimension size;
	
	/**
	 * Instantiates an object which indicates size of an object.
	 * 
	 * @param size		Size of the enclosed object.
	 * @throws NullPointerException		If the size is {@code null}.
	 */
	public AbsoluteSize(Dimension size) {
		if (size == null) {
			throw new NullPointerException("Size cannot be null.");
		}
		this.size = new Dimension(size);
	}
	
	/**
	 * Instantiates an object which indicates size of an object.
	 * 
	 * @param width		Width of the enclosed object.
	 * @param height	Height of the enclosed object.
	 * @throws IllegalArgumentException		If the width and/or height is less
	 * 										than or equal to {@code 0}.
	 */
	public AbsoluteSize(int width, int height) {
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException(
					"Width and height must be greater than 0."
			);
		}
		this.size = new Dimension(width, height);
	}

	public Dimension calculate(int width, int height) {
		if (width <= 0 || height <= 0) {
			throw new IllegalArgumentException(
					"Width and height must be greater than 0."
			);
		}
		return new Dimension(size);
	}

	/** 
	 * Returns a {@code String} representation of this object.
	 * 
	 * @return		{@code String} representation of this object.
	 */
	@Override
	public String toString() {
		return "AbsoluteSize [width=" + size.width + ", height=" + size.height + "]";
	}
}
