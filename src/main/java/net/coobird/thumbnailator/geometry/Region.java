/*
 * Thumbnailator - a thumbnail generation library
 *
 * Copyright (c) 2008-2023 Chris Kroells
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
import java.awt.Point;
import java.awt.Rectangle;

/**
 * A representation of a region, using a {@link Position} object and a
 * {@link Dimension} object.
 * 
 * @author coobird
 * @since	0.3.4
 *
 */
public final class Region {
	/**
	 * Position of the region.
	 */
	private final Position position;
	
	/**
	 * Size of the region.
	 */
	private final Size size;
	
	/**
	 * Instantiates a representation of a region from a {@link Position} and
	 * {@link Size}.
	 * 
	 * @param position		Position of the region.
	 * @param size			Size of the region.
	 * @throws NullPointerException		When the position and/or the size is
	 * 									{@code null}.
	 */
	public Region(Position position, Size size) {
		super();
		if (position == null) {
			throw new NullPointerException("Position cannot be null.");
		}
		if (size == null) {
			throw new NullPointerException("Size cannot be null.");
		}
		
		this.position = position;
		this.size = size;
	}
	
	/**
	 * Returns the position of the region.
	 * 
	 * @return 				Position of the region.
	 */
	public Position getPosition() {
		return position;
	}
	
	/**
	 * Returns the size of the region.
	 * 
	 * @return 				Size of the region.
	 */
	public Size getSize() {
		return size;
	}
	
	/**
	 * Calculates the position and size of the enclosed region, relative to the
	 * enclosing region.
	 * <p>
	 * The portions of the enclosed region which lies outside the enclosing
	 * region are ignored. Effectively, the {@link Rectangle} returned by this
	 * method is an intersection of the enclosing and enclose regions.
	 * 
	 * @param outerWidth		Width of the enclosing region.
	 * @param outerHeight		Height of the enclosing region.
	 * @param flipHorizontal	Whether enclosed region should flip
	 * 							horizontally within the enclosing region.
	 * @param flipVertical		Whether enclosed region should flip
	 * 							vertically within the enclosing region.
	 * @param swapDimensions	Whether the components of the point and
	 * 							dimension of the enclosed region should be
	 * 							swapped.
	 * @return					Position and size of the enclosed region.
	 */
	public Rectangle calculate(
			int outerWidth,
			int outerHeight,
			boolean flipHorizontal,
			boolean flipVertical,
			boolean swapDimensions
	) {
		Dimension innerDimension = size.calculate(outerWidth, outerHeight);

		Point innerPoint = position.calculate(
				outerWidth, outerHeight, innerDimension.width, innerDimension.height, 0, 0, 0, 0
		);

		if (swapDimensions) {
			innerDimension = new Dimension(innerDimension.height, innerDimension.width);
			innerPoint = new Point(innerPoint.y, innerPoint.x);
		}

		if (flipHorizontal) {
			int newX = outerWidth - innerPoint.x - innerDimension.width;
			innerPoint.setLocation(newX, innerPoint.y);
		}
		if (flipVertical) {
			int newY = outerHeight - innerPoint.y - innerDimension.height;
			innerPoint.setLocation(innerPoint.x, newY);
		}

		Rectangle outerRectangle = new Rectangle(0, 0, outerWidth, outerHeight);
		Rectangle innerRectangle = new Rectangle(innerPoint, innerDimension);

		return outerRectangle.intersection(innerRectangle);
	}

	/** 
	 * Returns a {@code String} representation of this region.
	 * 
	 * @return		{@code String} representation of this region.
	 */
	@Override
	public String toString() {
		return "Region [position=" + position + ", size=" + size + "]";
	}
}
