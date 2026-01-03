/*
 * Thumbnailator - a thumbnail generation library
 *
 * Copyright (c) 2008-2025 Chris Kroells
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

import net.coobird.thumbnailator.filters.MultilineCaption;

/**
 * An enum of text alignment options for use with {@link MultilineCaption}.
 * <p>
 * Defines how multiple lines of text should be horizontally aligned
 * relative to each other within a caption block.
 * 
 * @author artyomsv
 * @since 0.4.22
 */
public enum TextAlignment {
	/**
	 * Align text to the left edge of the caption block.
	 * <p>
	 * All lines will start at the same x-coordinate.
	 */
	LEFT,
	
	/**
	 * Center text horizontally within the caption block.
	 * <p>
	 * Each line will be centered independently based on its width.
	 */
	CENTER,
	
	/**
	 * Align text to the right edge of the caption block.
	 * <p>
	 * All lines will end at the same x-coordinate.
	 */
	RIGHT
}
