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

package net.coobird.thumbnailator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import net.coobird.thumbnailator.util.ThumbnailatorUtils;

import org.junit.Test;

/**
 * Tests for the {@link ThumbnailatorUtils} class.
 * 
 * @author coobird
 *
 */
public class ThumbnailatorUtilsTest {

	@Test
	public void isSupportedOutputFormat_SupportedFormat() {
		// given a supported format
		String format = "JPEG";
		
		// when
		boolean isSupported = ThumbnailatorUtils.isSupportedOutputFormat(format);
		
		// then, it is supported.
		assertTrue(isSupported);
	}
	
	@Test
	public void isSupportedOutputFormat_UnsupportedFormat() {
		// given an unsupported format
		String format = "foobar";
		
		// when
		boolean isSupported = ThumbnailatorUtils.isSupportedOutputFormat(format);
		
		// then, it is unsupported.
		assertFalse(isSupported);
	}
	
	@Test
	public void isSupportedOutputFormat_OriginalFormat() {
		// given an unsupported format
		String format = ThumbnailParameter.ORIGINAL_FORMAT;
		
		// when
		boolean isSupported = ThumbnailatorUtils.isSupportedOutputFormat(format);
		
		// then, it is supported.
		assertTrue(isSupported);
	}

	@Test
	public void isSupportedOutputFormatType_SupportedFormat_SupportedType() {
		// given a supported format, and supported type
		String format = "JPEG";
		String type = "JPEG";
		
		// when
		boolean isSupported =
			ThumbnailatorUtils.isSupportedOutputFormatType(format, type);
		
		// then, it is supported.
		assertTrue(isSupported);
	}
	
	@Test
	public void isSupportedOutputFormatType_SupportedFormat_UnsupportedType() {
		// given a supported format, and unsupported type
		String format = "JPEG";
		String type = "foobar";
		
		// when
		boolean isSupported =
			ThumbnailatorUtils.isSupportedOutputFormatType(format, type);
		
		// then, it is unsupported.
		assertFalse(isSupported);
	}
	
	@Test
	public void isSupportedOutputFormatType_SupportedFormat_DefaultType() {
		// given a supported format, and unsupported type
		String format = "JPEG";
		String type = ThumbnailParameter.DEFAULT_FORMAT_TYPE;
		
		// when
		boolean isSupported =
			ThumbnailatorUtils.isSupportedOutputFormatType(format, type);
		
		// then, it is supported.
		assertTrue(isSupported);
	}
	
	@Test
	public void isSupportedOutputFormatType_UnsupportedFormat_SomeType() {
		// given an unsupported format, and some type
		String format = "foobar";
		String type = "foobar";
		
		// when
		boolean isSupported =
			ThumbnailatorUtils.isSupportedOutputFormatType(format, type);
		
		// then, it is unsupported.
		assertFalse(isSupported);
	}
	
	@Test
	public void isSupportedOutputFormatType_UnsupportedFormat_DefaultType() {
		// given an unsupported format, and default type
		String format = "foobar";
		String type = ThumbnailParameter.DEFAULT_FORMAT_TYPE;
		
		// when
		boolean isSupported =
			ThumbnailatorUtils.isSupportedOutputFormatType(format, type);
		
		// then, it is unsupported.
		assertFalse(isSupported);
	}
	
	@Test
	public void isSupportedOutputFormatType_OriginalFormat_SomeType() {
		// given a the original format, and some type
		String format = ThumbnailParameter.ORIGINAL_FORMAT;
		String type = "foobar";
		
		// when
		boolean isSupported =
			ThumbnailatorUtils.isSupportedOutputFormatType(format, type);
		
		// then, it is unsupported.
		assertFalse(isSupported);
	}
	
	@Test
	public void isSupportedOutputFormatType_OriginalFormat_DefaultType() {
		// given a original format, and default type
		String format = ThumbnailParameter.ORIGINAL_FORMAT;
		String type = ThumbnailParameter.DEFAULT_FORMAT_TYPE;
		
		// when
		boolean isSupported =
			ThumbnailatorUtils.isSupportedOutputFormatType(format, type);
		
		// then, it is supported.
		assertTrue(isSupported);
	}
}
