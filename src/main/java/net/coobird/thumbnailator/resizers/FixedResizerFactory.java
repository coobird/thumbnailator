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

package net.coobird.thumbnailator.resizers;

import java.awt.Dimension;

/**
 * A {@link ResizerFactory} that returns a specific {@link Resizer}
 * unconditionally.
 * 
 * @author coobird
 * @since	0.4.0
 */
public class FixedResizerFactory implements ResizerFactory {
	/**
	 * The resizer which is to be returned unconditionally by this class.
	 */
	private final Resizer resizer;
	
	/**
	 * Creates an instance of the {@link FixedResizerFactory} which returns
	 * the speicifed {@link Resizer} under all circumstances.
	 * 
	 * @param resizer		The {@link Resizer} instance that is to be returned
	 * 						under all circumstances.
	 */
	public FixedResizerFactory(Resizer resizer) {
		this.resizer = resizer;
	}

	public Resizer getResizer() {
		return resizer;
	}

	public Resizer getResizer(Dimension originalSize, Dimension thumbnailSize) {
		return resizer;
	}
}
