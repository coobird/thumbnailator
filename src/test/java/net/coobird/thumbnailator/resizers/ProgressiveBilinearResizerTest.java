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

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Test;

public class ProgressiveBilinearResizerTest {

	/**
	 * Test for
	 * {@link ProgressiveBilinearResizer#resize(BufferedImage, BufferedImage)}
	 * where,
	 * 
	 * 1) source image is null.
	 * 2) destination image is null.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an NullPointerException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=NullPointerException.class)
	public void resizeNullAndNull() {
		BufferedImage srcImage = null;
		BufferedImage destImage = null;
		
		new ProgressiveBilinearResizer().resize(srcImage, destImage);
		
		fail();
	}
	
	/**
	 * Test for
	 * {@link ProgressiveBilinearResizer#resize(BufferedImage, BufferedImage)}
	 * where,
	 * 
	 * 1) source image is specified.
	 * 2) destination image is null.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an NullPointerException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=NullPointerException.class)
	public void resizeSpecifiedAndNull() {
		BufferedImage srcImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		BufferedImage destImage = null;
		
		new ProgressiveBilinearResizer().resize(srcImage, destImage);
		
		fail();
	}
	
	/**
	 * Test for
	 * {@link ProgressiveBilinearResizer#resize(BufferedImage, BufferedImage)}
	 * where,
	 * 
	 * 1) source image is null.
	 * 2) destination image is specified.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an NullPointerException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=NullPointerException.class)
	public void resizeNullAndSpecified() {
		BufferedImage srcImage = null;
		BufferedImage destImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		new ProgressiveBilinearResizer().resize(srcImage, destImage);
		
		fail();
	}

}
