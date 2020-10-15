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

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.geometry.Region;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import net.coobird.thumbnailator.util.exif.ExifFilterUtils;
import net.coobird.thumbnailator.util.exif.ExifUtils;
import net.coobird.thumbnailator.util.exif.Orientation;

/**
 * An {@link ImageSource} which uses an {@link InputStream} to read the
 * source image.
 * 
 * @author coobird
 *
 */
public class InputStreamImageSource extends AbstractImageSource<InputStream> {
	/**
	 * The index used to obtain the first image in an image file.
	 */
	private static final int FIRST_IMAGE_INDEX = 0;
	
	/**
	 * The {@link InputStream} from which the source image is to be read.
	 */
	private final InputStream is;
	
	/**
	 * Instantiates an {@link InputStreamImageSource} with the
	 * {@link InputStream} which will be used to read the source image.
	 * 
	 * @param is		The {@link InputStream} which is to be used to obtain
	 * 					the source image.
	 * @throws NullPointerException		If the {@link InputStream} is
	 * 									{@code null}.
	 */
	public InputStreamImageSource(InputStream is) {
		super();

		if (is == null) {
			throw new NullPointerException("InputStream cannot be null.");
		}
		
		this.is = is;
	}

	public BufferedImage read() throws IOException {
		ImageInputStream iis = ImageIO.createImageInputStream(is);
		
		if (iis == null) {
			throw new IOException("Could not open InputStream.");
		}
		
		Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
		if (!readers.hasNext()) {
			iis.close();
			throw new UnsupportedFormatException(
					UnsupportedFormatException.UNKNOWN,
					"No suitable ImageReader found for source data."
			);
		}
		
		ImageReader reader = readers.next();
		reader.setInput(iis);

		boolean isExceptionThrown = false;
		try {
			BufferedImage img = readImage(reader);
			return finishedReading(img);

		} catch (IOException e) {
			isExceptionThrown = true;
			throw e;

		} finally {
			/*
			 * Dispose the reader to free resources.
			 *
			 * This seems to be one of the culprits which was causing
			 * `OutOfMemoryError`s which began appearing frequently with
			 * Java 7 Update 21.
			 *
			 * Issue:
			 * http://code.google.com/p/thumbnailator/issues/detail?id=42
			 */
			reader.dispose();

			try {
				iis.close();
			} catch (IOException e) {
				// TODO If above Java 7, we can use Throwable.addSuppressed
				// Suppress this exception from superseding the original exception.
				// Original exception is likely to be more informational than this one.
				if (!isExceptionThrown) {
					throw e;
				}
			}
		}
	}

	private BufferedImage readImage(ImageReader reader) throws IOException {
		inputFormatName = reader.getFormatName();

		try {
			if (param.useExifOrientation()) {
				Orientation orientation;
				orientation = ExifUtils.getExifOrientation(reader, FIRST_IMAGE_INDEX);

				// Skip this code block if there's no rotation needed.
				if (orientation != null && orientation != Orientation.TOP_LEFT) {
					List<ImageFilter> filters = param.getImageFilters();

					// EXIF orientation filter is added to the beginning, as
					// it should be performed early to prevent mis-orientation
					// in later filters.
					filters.add(0, ExifFilterUtils.getFilterForOrientation(orientation));
				}
			}
		} catch (Exception e) {
			// If something goes wrong, then skip the orientation-related
			// processing.
			// TODO Ought to have some way to track errors.
		}

		ImageReadParam irParam = reader.getDefaultReadParam();
		int width = reader.getWidth(FIRST_IMAGE_INDEX);
		int height = reader.getHeight(FIRST_IMAGE_INDEX);

		if (param != null && param.getSourceRegion() != null) {
			Region region = param.getSourceRegion();
			Rectangle sourceRegion = region.calculate(width, height);

			irParam.setSourceRegion(sourceRegion);
		}

		/*
		 * FIXME Workaround to enable subsampling for large source images.
		 *
		 * Issue:
		 * https://github.com/coobird/thumbnailator/issues/69
		 */
		if (param != null &&
				"true".equals(System.getProperty("thumbnailator.conserveMemoryWorkaround")) &&
				width > 1800 && height > 1800 &&
				(width * height * 4 > Runtime.getRuntime().freeMemory() / 4)
		) {
			int subsampling = 1;

			// Calculate the maximum subsampling that can be used.
			if (param.getSize() != null && (param.getSize().width * 2 < width && param.getSize().height * 2 < height)) {
				int targetWidth = param.getSize().width;
				int targetHeight = param.getSize().height;

				// Handle cases where .width() or .height() is called. (Issue 161)
				targetWidth = targetWidth != Integer.MAX_VALUE ? targetWidth : targetHeight;
				targetHeight = targetHeight != Integer.MAX_VALUE ? targetHeight : targetWidth;

				double widthScaling = (double)width / (double)targetWidth;
				double heightScaling = (double)height / (double)targetHeight;

				subsampling = (int)Math.floor(Math.min(widthScaling, heightScaling));

			} else if (param.getSize() == null) {
				subsampling = (int)Math.max(1, Math.floor(1 / Math.max(param.getHeightScalingFactor(), param.getWidthScalingFactor())));
			}

			// Prevent excessive subsampling that can ruin image quality.
			// This will ensure that at least a 600 x 600 image will be used as source.
			for (; (width / subsampling) < 600 || (height / subsampling) < 600; subsampling--);

			// If scaling factor based resize is used, need to change the scaling factor.
			if (param.getSize() == null) {
				try {
					Class<?> c = param.getClass();
					Field heightField = c.getDeclaredField("heightScalingFactor");
					Field widthField = c.getDeclaredField("widthScalingFactor");
					heightField.setAccessible(true);
					widthField.setAccessible(true);
					heightField.set(param, param.getHeightScalingFactor() * (double)subsampling);
					widthField.set(param, param.getWidthScalingFactor() * (double)subsampling);

				} catch (Exception e) {
					// If we can't update the parameter, then disable subsampling.
					subsampling = 1;
				}
			}

			irParam.setSourceSubsampling(subsampling, subsampling, 0, 0);
		}

		return reader.read(FIRST_IMAGE_INDEX, irParam);
	}

	public InputStream getSource() {
		return is;
	}
}
