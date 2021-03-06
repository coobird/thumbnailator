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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.test.BufferedImageComparer;
import net.coobird.thumbnailator.util.BufferedImages;

import org.junit.Test;

/**
 * Tests for the {@link Watermark} filter.
 * 
 * @author coobird
 *
 */
public class WatermarkTest {

	private static final Position DEFAULT_POSITION = Positions.BOTTOM_RIGHT;
	private static final BufferedImage DEFAULT_WATERMARK = new BufferedImage(
			50, 50, BufferedImage.TYPE_INT_ARGB
	);
	private static final float DEFAULT_OPACITY = 0.5f;
	private static final int DEFAULT_INSET = 0;

	/**
	 * Checks that the input image contents are not altered.
	 */
	@Test
	public void inputContentsAreNotAltered() {
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		BufferedImage copyImage = BufferedImages.copy(originalImage);

		ImageFilter filter = new Watermark(
				DEFAULT_POSITION,
				DEFAULT_WATERMARK,
				DEFAULT_OPACITY,
				DEFAULT_INSET
		);

		// when
		filter.apply(originalImage);

		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}

	@Test
	public void inputContentsAreNotAlteredFor3ArgConstructor() {
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		BufferedImage copyImage = BufferedImages.copy(originalImage);

		ImageFilter filter = new Watermark(
				DEFAULT_POSITION,
				DEFAULT_WATERMARK,
				DEFAULT_OPACITY
		);

		// when
		filter.apply(originalImage);

		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}

	@Test
	public void imageTypeForInputAndOutputIsTheSame() {
		assertImageTypeRetained(
				new Watermark(Positions.BOTTOM_CENTER, DEFAULT_WATERMARK, DEFAULT_OPACITY, DEFAULT_INSET)
		);
	}

	@Test
	public void imageTypeForInputAndOutputIsTheSameFor3ArgConstructor() {
		assertImageTypeRetained(
				new Watermark(Positions.BOTTOM_CENTER, DEFAULT_WATERMARK, DEFAULT_OPACITY)
		);
	}

	@Test(expected = NullPointerException.class)
	public void constructorNullCheckForCaption() {
		new Watermark(
				null,
				DEFAULT_WATERMARK,
				DEFAULT_OPACITY,
				DEFAULT_INSET
		);
	}

	@Test(expected = NullPointerException.class)
	public void constructorNullCheckForWatermark() {
		new Watermark(
				DEFAULT_POSITION,
				null,
				DEFAULT_OPACITY,
				DEFAULT_INSET
		);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorRejectsNegativeOpacity() {
		new Watermark(
				DEFAULT_POSITION,
				DEFAULT_WATERMARK,
				-0.00001f,
				DEFAULT_INSET
		);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorRejectsOpacityLargerThan1() {
		new Watermark(
				DEFAULT_POSITION,
				DEFAULT_WATERMARK,
				1.00001f,
				DEFAULT_INSET
		);
	}
	@Test
	public void constructorAllowsPositiveInsets() {
		new Watermark(
				DEFAULT_POSITION,
				DEFAULT_WATERMARK,
				DEFAULT_OPACITY,
				5
		);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorAllowsNegativeInsets() {
		new Watermark(
				DEFAULT_POSITION,
				DEFAULT_WATERMARK,
				DEFAULT_OPACITY,
				-1
		);
	}

	private static final Color BACKGROUND_COLOR = Color.white;
	private static final Color WATERMARK_COLOR = Color.blue;

	private static final BufferedImage ORIGINAL_IMAGE;
	private static final BufferedImage WATERMARK_IMAGE;

	static {
		Graphics g;
		ORIGINAL_IMAGE = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		g = ORIGINAL_IMAGE.getGraphics();
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, 200, 200);
		g.dispose();

		WATERMARK_IMAGE = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
		g = WATERMARK_IMAGE.getGraphics();
		g.setColor(WATERMARK_COLOR);
		g.fillRect(0, 0, 50, 50);
		g.dispose();
	}

	@Test
	public void checkWatermarkDrawn() {
		// given
		ImageFilter filter = new Watermark(
				Positions.BOTTOM_RIGHT,
				WATERMARK_IMAGE,
				1.0f
		);

		// when
		BufferedImage actual = filter.apply(ORIGINAL_IMAGE);

		// then
		BufferedImage expected = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		Graphics g = expected.getGraphics();
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, 200, 200);
		g.setColor(WATERMARK_COLOR);
		g.fillRect(150, 150, 50, 50);
		g.dispose();

		assertTrue(BufferedImageComparer.isSame(actual, expected));
	}

	@Test
	public void checkWatermarkDrawnWithHalfOpacity() {
		// given
		ImageFilter filter = new Watermark(
				Positions.BOTTOM_RIGHT,
				WATERMARK_IMAGE,
				0.5f
		);

		// when
		BufferedImage actual = filter.apply(ORIGINAL_IMAGE);

		// then
		BufferedImage expected = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		Graphics g = expected.getGraphics();
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, 200, 200);
		g.setColor(new Color(0, 0, 1.0f, 0.5f));
		g.fillRect(150, 150, 50, 50);
		g.dispose();

		assertTrue(BufferedImageComparer.isSame(actual, expected));
	}

	@Test
	public void checkWatermarkDrawnWithInsets() throws IOException {
		// given
		ImageFilter filter = new Watermark(
				Positions.BOTTOM_RIGHT,
				WATERMARK_IMAGE,
				1.0f,
				5
		);

		// when
		BufferedImage actual = filter.apply(ORIGINAL_IMAGE);

		// then
		BufferedImage expected = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		Graphics g = expected.getGraphics();
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, 200, 200);
		g.setColor(WATERMARK_COLOR);
		g.fillRect(145, 145, 50, 50);
		g.dispose();

		assertTrue(BufferedImageComparer.isSame(actual, expected));
	}
}
