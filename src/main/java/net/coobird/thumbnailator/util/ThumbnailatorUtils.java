/*
 * Thumbnailator - a thumbnail generation library
 *
 * Copyright (c) 2008-2022 Chris Kroells
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

package net.coobird.thumbnailator.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

import net.coobird.thumbnailator.ThumbnailParameter;

/**
 * A utility class for Thumbnailator.
 * 
 * @author coobird
 *
 */
public final class ThumbnailatorUtils {
	/**
	 *  This class is not intended to be instantiated.
	 */
	private ThumbnailatorUtils() {}
	
	/**
	 * Returns a {@link List} of supported output formats.
	 * 
	 * @return		A {@link List} of supported output formats. If no formats
	 * 				are supported, an empty list is returned.
	 */
	public static List<String> getSupportedOutputFormats() {
		String[] formats = ImageIO.getWriterFormatNames();
		
		if (formats == null) {
			return Collections.emptyList();
		} else {
			return Arrays.asList(formats);
		}
	}
	
	/**
	 * Returns whether a specified format is supported for output.
	 *
	 * @param format	The format to check whether it is supported or not.
	 * @return			{@code true} if the format is supported, {@code false}
	 * 					otherwise.
	 */
	public static boolean isSupportedOutputFormat(String format)
	{
		if (format == ThumbnailParameter.ORIGINAL_FORMAT) {
			return true;
		}
		
		for (String supportedFormat : getSupportedOutputFormats()) {
			if (supportedFormat.equalsIgnoreCase(format)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns a {@link List} of supported output formats types for a specified
	 * output format.
	 *
	 * @param format	The output format.
	 * @return		A {@link List} of supported output formats types. If no
	 * 				formats types are supported, or if compression is not
	 * 				supported for the specified format, then an empty list
	 * 				is returned.
	 */
	public static List<String> getSupportedOutputFormatTypes(String format) {
		if (format == ThumbnailParameter.ORIGINAL_FORMAT) {
			return Collections.emptyList();
		}
		
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(format);
		if (!writers.hasNext()) {
			return Collections.emptyList();
		}
		
		String[] types;
		try {
			types = writers.next().getDefaultWriteParam().getCompressionTypes();
		} catch (UnsupportedOperationException e) {
			return Collections.emptyList();
		}
		
		if (types == null) {
			return Collections.emptyList();
		} else {
			return Arrays.asList(types);
		}
	}
	
	/**
	 * Returns whether a specified format type is supported for a specified
	 * output format.
	 *
	 * @param format	The format to check whether it is supported or not.
	 * @param type		The format type to check whether it is supported or not.
	 * @return			{@code true} if the format type is supported by the
	 * 					specified supported format, {@code false} otherwise.
	 */
	public static boolean isSupportedOutputFormatType(String format, String type) {
		if (!isSupportedOutputFormat(format)) {
			return false;
		}
		
		if (format == ThumbnailParameter.ORIGINAL_FORMAT
				&& type == ThumbnailParameter.DEFAULT_FORMAT_TYPE) {
			return true;

		} else if (format == ThumbnailParameter.ORIGINAL_FORMAT
				&& type != ThumbnailParameter.DEFAULT_FORMAT_TYPE) {
			return false;

		} else if (type == ThumbnailParameter.DEFAULT_FORMAT_TYPE) {
			return true;
		}
		
		for (String supportedType : getSupportedOutputFormatTypes(format)) {
			if (supportedType.equals(type)) {
				return true;
			}
		}
		
		return false;
	}
}
