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

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.junit.Test;

/**
 * Tests the {@link ExifUtils} class to check that the Exif Orientation
 * tag is correctly acquired by the
 * {@link ExifUtils#getExifOrientation(ImageReader, int)} method.
 * <p>
 * The Exif Orientation tags has been added to the source images by using
 * <a href="http://owl.phy.queensu.ca/~phil/exiftool/index.html">ExifTool</a>.
 * 
 * @author coobird
 *
 */
public class ExifUtilsTest {
	
	@Test
	public void exifOrientation1() throws Exception {
		// given
		ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
		reader.setInput(ImageIO.createImageInputStream(new File("src/test/resources/Exif/orientation_1.jpg")));
		
		// when
		Orientation orientation = ExifUtils.getExifOrientation(reader, 0);
		
		// then
		assertEquals(Orientation.typeOf(1), orientation);
	}
	
	@Test
	public void exifOrientation2() throws Exception {
		// given
		ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
		reader.setInput(ImageIO.createImageInputStream(new File("src/test/resources/Exif/orientation_2.jpg")));
		
		// when
		Orientation orientation = ExifUtils.getExifOrientation(reader, 0);
		
		// then
		assertEquals(Orientation.typeOf(2), orientation);
	}
	
	@Test
	public void exifOrientation3() throws Exception {
		// given
		ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
		reader.setInput(ImageIO.createImageInputStream(new File("src/test/resources/Exif/orientation_3.jpg")));
		
		// when
		Orientation orientation = ExifUtils.getExifOrientation(reader, 0);
		
		// then
		assertEquals(Orientation.typeOf(3), orientation);
	}
	
	@Test
	public void exifOrientation4() throws Exception {
		// given
		ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
		reader.setInput(ImageIO.createImageInputStream(new File("src/test/resources/Exif/orientation_4.jpg")));
		
		// when
		Orientation orientation = ExifUtils.getExifOrientation(reader, 0);
		
		// then
		assertEquals(Orientation.typeOf(4), orientation);
	}
	
	@Test
	public void exifOrientation5() throws Exception {
		// given
		ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
		reader.setInput(ImageIO.createImageInputStream(new File("src/test/resources/Exif/orientation_5.jpg")));
		
		// when
		Orientation orientation = ExifUtils.getExifOrientation(reader, 0);
		
		// then
		assertEquals(Orientation.typeOf(5), orientation);
	}
	
	@Test
	public void exifOrientation6() throws Exception {
		// given
		ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
		reader.setInput(ImageIO.createImageInputStream(new File("src/test/resources/Exif/orientation_6.jpg")));
		
		// when
		Orientation orientation = ExifUtils.getExifOrientation(reader, 0);
		
		// then
		assertEquals(Orientation.typeOf(6), orientation);
	}
	
	@Test
	public void exifOrientation7() throws Exception {
		// given
		ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
		reader.setInput(ImageIO.createImageInputStream(new File("src/test/resources/Exif/orientation_7.jpg")));
		
		// when
		Orientation orientation = ExifUtils.getExifOrientation(reader, 0);
		
		// then
		assertEquals(Orientation.typeOf(7), orientation);
	}
	
	@Test
	public void exifOrientation8() throws Exception {
		// given
		ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
		reader.setInput(ImageIO.createImageInputStream(new File("src/test/resources/Exif/orientation_8.jpg")));
		
		// when
		Orientation orientation = ExifUtils.getExifOrientation(reader, 0);
		
		// then
		assertEquals(Orientation.typeOf(8), orientation);
	}
}
