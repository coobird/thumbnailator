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

package net.coobird.thumbnailator.filters;

import static net.coobird.thumbnailator.filters.ImageFilterTestUtils.assertImageTypeRetained;
import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.test.BufferedImageAssert;
import net.coobird.thumbnailator.test.BufferedImageComparer;
import net.coobird.thumbnailator.util.BufferedImages;

import org.junit.Test;

/**
 * Tests for the {@link Rotation} filter.
 * 
 * @author coobird
 *
 */
public class RotationTest {

	/**
	 * Checks that the input image contents are not altered, when using the
	 * {@link Rotation#newRotator(double)} method.
	 */
	@Test
	public void inputContentsAreNotAltered_SpecifiedRotator() {
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		BufferedImage copyImage = BufferedImages.copy(originalImage);
		
		ImageFilter filter = Rotation.newRotator(45);
		
		// when
		filter.apply(originalImage);
		
		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}
	
	/**
	 * Checks that the input image contents are not altered, when using one of
	 * the constants defined in the {@link Rotation} class.
	 */
	@Test
	public void inputContentsAreNotAltered_UsingConstantField() {
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		BufferedImage copyImage = BufferedImages.copy(originalImage);
		
		ImageFilter filter = Rotation.LEFT_90_DEGREES;
		
		// when
		filter.apply(originalImage);
		
		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}

	@Test
	public void imageTypeForInputAndOutputIsTheSame_SpecifiedRotator() {
		assertImageTypeRetained(Rotation.newRotator(45));
	}

	@Test
	public void imageTypeForInputAndOutputIsTheSame_UsingConstantField() {
		assertImageTypeRetained(Rotation.LEFT_90_DEGREES);
	}
	
	@Test
	public void imageRotatedLeft90Degrees() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("src/test/resources/Exif/original.png"));
		
		// when
		BufferedImage result = Rotation.LEFT_90_DEGREES.apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result,
				new float[] {
						1, 1, 0,
						1, 1, 0,
						1, 1, 1,
				}
		);
	}
	
	@Test
	public void imageRotatedRight90Degrees() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("src/test/resources/Exif/original.png"));
		
		// when
		BufferedImage result = Rotation.RIGHT_90_DEGREES.apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result,
				new float[] {
						1, 1, 1,
						0, 1, 1,
						0, 1, 1,
				}
		);
	}
	
	@Test
	public void imageRotated180Degrees() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("src/test/resources/Exif/original.png"));
		
		// when
		BufferedImage result = Rotation.ROTATE_180_DEGREES.apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result,
				new float[] {
						0, 0, 1,
						1, 1, 1,
						1, 1, 1,
				}
		);
	}
	
	@Test
	public void stretchedImageRotatedLeft90Degrees() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("src/test/resources/Exif/stretch.png"));
		
		// when
		BufferedImage result = Rotation.LEFT_90_DEGREES.apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result,
				new float[] {
						1, 1, 0,
						1, 1, 0,
						1, 1, 1,
				}
		);
		
	}
	
	@Test
	public void stretchedImageRotatedRight90Degrees() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("src/test/resources/Exif/stretch.png"));
		
		// when
		BufferedImage result = Rotation.RIGHT_90_DEGREES.apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result,
				new float[] {
						1, 1, 1,
						0, 1, 1,
						0, 1, 1,
				}
		);
		
	}
	
	@Test
	public void stretchedImageRotated180Degrees() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("src/test/resources/Exif/stretch.png"));
		
		// when
		BufferedImage result = Rotation.ROTATE_180_DEGREES.apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result,
				new float[] {
						0, 0, 1,
						1, 1, 1,
						1, 1, 1,
				}
		);
	}
}
