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

package net.coobird.thumbnailator.util.exif;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.test.BufferedImageAssert;
import net.coobird.thumbnailator.util.BufferedImages;

import org.junit.Test;

/**
 * Tests that the {@link ImageFilter}s returned by the
 * {@link ExifFilterUtils#getFilterForOrientation(Orientation)} method can
 * correctly orient an image according to the Exif orientation metadata.
 * <p>
 * The source images were created from the examples of how the letter "F"
 * would appear when the various Exif Orientations are applied, as seen in
 * <a href="http://sylvana.net/jpegcrop/exif_orientation.html">this page</a>.
 * 
 * @author coobird
 *
 */
public class ExifFilterUtilsTest {
	
	@Test
	public void correctOrientation1() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("src/test/resources/Exif/source_1.jpg"));
		img = BufferedImages.copy(img, BufferedImage.TYPE_INT_ARGB);
		
		// when
		BufferedImage result =
				ExifFilterUtils.getFilterForOrientation(Orientation.TOP_LEFT).apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result,
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}
	
	@Test
	public void correctOrientation2() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("src/test/resources/Exif/source_2.jpg"));
		img = BufferedImages.copy(img, BufferedImage.TYPE_INT_ARGB);
		
		// when
		BufferedImage result =
				ExifFilterUtils.getFilterForOrientation(Orientation.TOP_RIGHT).apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result,
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}
	
	@Test
	public void correctOrientation3() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("src/test/resources/Exif/source_3.jpg"));
		img = BufferedImages.copy(img, BufferedImage.TYPE_INT_ARGB);
		
		// when
		BufferedImage result =
				ExifFilterUtils.getFilterForOrientation(Orientation.BOTTOM_RIGHT).apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result,
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}
	
	@Test
	public void correctOrientation4() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("src/test/resources/Exif/source_4.jpg"));
		img = BufferedImages.copy(img, BufferedImage.TYPE_INT_ARGB);
		
		// when
		BufferedImage result =
				ExifFilterUtils.getFilterForOrientation(Orientation.BOTTOM_LEFT).apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result,
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}
	
	@Test
	public void correctOrientation5() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("src/test/resources/Exif/source_5.jpg"));
		img = BufferedImages.copy(img, BufferedImage.TYPE_INT_ARGB);
		
		// when
		BufferedImage result =
				ExifFilterUtils.getFilterForOrientation(Orientation.LEFT_TOP).apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result,
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}
	
	@Test
	public void correctOrientation6() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("src/test/resources/Exif/source_6.jpg"));
		img = BufferedImages.copy(img, BufferedImage.TYPE_INT_ARGB);
		
		// when
		BufferedImage result =
				ExifFilterUtils.getFilterForOrientation(Orientation.RIGHT_TOP).apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result,
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}
	
	@Test
	public void correctOrientation7() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("src/test/resources/Exif/source_7.jpg"));
		img = BufferedImages.copy(img, BufferedImage.TYPE_INT_ARGB);
		
		// when
		BufferedImage result =
				ExifFilterUtils.getFilterForOrientation(Orientation.RIGHT_BOTTOM).apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result,
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}
	
	@Test
	public void correctOrientation8() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("src/test/resources/Exif/source_8.jpg"));
		img = BufferedImages.copy(img, BufferedImage.TYPE_INT_ARGB);
		
		// when
		BufferedImage result =
				ExifFilterUtils.getFilterForOrientation(Orientation.LEFT_BOTTOM).apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result,
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}
}
