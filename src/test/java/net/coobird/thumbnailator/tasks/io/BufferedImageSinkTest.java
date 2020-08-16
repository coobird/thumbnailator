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

package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;

public class BufferedImageSinkTest {
	@Test
	public void writeImage() throws IOException {
		// given
		BufferedImage img =
			new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
		
		// when
		BufferedImageSink sink = new BufferedImageSink();
		sink.write(img);
		
		// then
		assertSame(img, sink.getSink());
	}
	
	@Test(expected=NullPointerException.class)
	public void writeNull() throws IOException {
		// given
		BufferedImage img = null;
		
		try {
			// when
			new BufferedImageSink().write(img);
			fail();
		} catch (NullPointerException e) {
			// then
			assertEquals("Cannot write a null image.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public void getSink_BeforeWrite() throws IOException {
		// given
		
		try {
			// when
			new BufferedImageSink().getSink();
			fail();
		} catch (IllegalStateException e) {
			// then
			assertEquals("BufferedImageSink has not been written to yet.", e.getMessage());
			throw e;
		}
	}
	
	@Test
	public void setOutputFormatName_DoesntAffectAnything() throws IOException {
		// given
		BufferedImageSink sink0 = new BufferedImageSink();
		BufferedImageSink sink1 = new BufferedImageSink();
		
		BufferedImage img =
			new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
		
		// when
		sink0.setOutputFormatName("PNG");
		
		sink0.write(img);
		sink1.write(img);
		
		// then
		assertSame(img, sink0.getSink());
		assertSame(img, sink1.getSink());
	}
}
