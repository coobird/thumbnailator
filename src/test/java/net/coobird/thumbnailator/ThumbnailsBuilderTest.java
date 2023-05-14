/*
 * Thumbnailator - a thumbnail generation library
 *
 * Copyright (c) 2008-2023 Chris Kroells
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

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.filters.Watermark;
import net.coobird.thumbnailator.geometry.AbsoluteSize;
import net.coobird.thumbnailator.geometry.Coordinate;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.geometry.Region;
import net.coobird.thumbnailator.name.Rename;
import net.coobird.thumbnailator.resizers.DefaultResizerFactory;
import net.coobird.thumbnailator.resizers.Resizer;
import net.coobird.thumbnailator.resizers.ResizerFactory;
import net.coobird.thumbnailator.resizers.Resizers;
import net.coobird.thumbnailator.resizers.configurations.AlphaInterpolation;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import net.coobird.thumbnailator.resizers.configurations.Dithering;
import net.coobird.thumbnailator.resizers.configurations.Rendering;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;
import net.coobird.thumbnailator.test.BufferedImageComparer;

import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.ArgumentCaptor;


/**
 * A class which tests the behavior of the builder interface of the
 * {@link Thumbnails} class.
 * 
 * @author coobird
 *
 */
@RunWith(Enclosed.class)
public class ThumbnailsBuilderTest {

	public static class Tests {
		/* Following images are for image-based comparisons */
		private static final Color BACKGROUND_COLOR = Color.black;
		private static final Color WATERMARK_COLOR = Color.white;

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

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The size method is called.</li>
		 * <li>The width is 0.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void sizeWithZeroWidth() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			try {
				// when
				Thumbnails.of(img)
						.size(0, 50)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The size method is called.</li>
		 * <li>The height is 0.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void sizeWithZeroHeight() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			try {
				// when
				Thumbnails.of(img)
						.size(50, 0)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The size method is called.</li>
		 * <li>The height is Integer.MAX_VALUE.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The image is created</li>
		 * <li>The thumbnail has the size based on the width</li>
		 * </ol>
		 */
		@Test
		public void sizeWithHeightAsALargeNumber() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(50, Integer.MAX_VALUE)
					.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The size method is called.</li>
		 * <li>The width is Integer.MAX_VALUE.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The image is created</li>
		 * <li>The thumbnail has the size based on the height</li>
		 * </ol>
		 */
		@Test
		public void sizeWithWidthAsALargeNumber() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(Integer.MAX_VALUE, 50)
					.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The size method is called.</li>
		 * <li>The width and height is 0.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void sizeWithZeroWidthAndHeight() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			try {
				// when
				Thumbnails.of(img)
						.size(0, 0)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The size method is called.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail is successfully created.</li>
		 * <li>The thumbnail dimensions are that which is specified by the
		 * size method.</li>
		 * </ol>
		 */
		@Test
		public void sizeOnly() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			BufferedImage thumbnail = Thumbnails.of(img)
					.size(50, 50)
					.asBufferedImage();

			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The size method is called twice.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void sizeTwice() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnails.of(img)
					.size(50, 50)
					.size(50, 50)
					.asBufferedImage();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The width method is called twice.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void widthTwice() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnails.of(img)
					.width(50)
					.width(50)
					.asBufferedImage();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The width method is called.</li>
		 * <li>Then, the size method is called.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void widthThenSize() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnails.of(img)
					.width(50)
					.size(50, 50)
					.asBufferedImage();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The size method is called.</li>
		 * <li>Then, the width method is called.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void sizeThenWidth() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnails.of(img)
					.size(50, 50)
					.width(50)
					.asBufferedImage();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The height method is called twice.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void heightTwice() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnails.of(img)
					.height(50)
					.height(50)
					.asBufferedImage();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The height method is called.</li>
		 * <li>Then the size method is called.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void heightThenSize() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnails.of(img)
					.height(50)
					.size(50, 50)
					.asBufferedImage();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The size method is called.</li>
		 * <li>Then, the height method is called.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void sizeThenHeight() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnails.of(img)
					.size(50, 50)
					.height(50)
					.asBufferedImage();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The size method is called.</li>
		 * <li>The keepAspectRatio method is true.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail is successfully created.</li>
		 * <li>The thumbnail dimensions are not the same as what is specified
		 * by the size method, but one that keeps the aspect ratio of the
		 * original.</li>
		 * </ol>
		 */
		@Test
		public void sizeWithAspectRatioTrue() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			BufferedImage thumbnail = Thumbnails.of(img)
					.size(120, 50)
					.keepAspectRatio(true)
					.asBufferedImage();

			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The size method is called.</li>
		 * <li>The keepAspectRatio method is false.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail is successfully created.</li>
		 * <li>The thumbnail dimensions are that which is specified by the
		 * size method.</li>
		 * </ol>
		 */
		@Test
		public void sizeWithAspectRatioFalse() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			BufferedImage thumbnail = Thumbnails.of(img)
					.size(120, 50)
					.keepAspectRatio(false)
					.asBufferedImage();

			assertEquals(120, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		@Test
		public void sizeWithScale() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.size(100, 100)
						.scale(0.5)
						.asBufferedImage();

				fail();
			} catch (IllegalStateException e) {
				// then
				assertTrue(e.getMessage().contains("scale cannot be set"));
			}
		}

		@Test
		public void sizeWithScaleTwoArg() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.size(100, 100)
						.scale(0.5, 0.5)
						.asBufferedImage();

				fail();
			} catch (IllegalStateException e) {
				// then
				assertTrue(e.getMessage().contains("scale cannot be set"));
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The forceSize method is called.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail is successfully created.</li>
		 * <li>The thumbnail dimensions are that which is specified by the
		 * size method.</li>
		 * </ol>
		 */
		@Test
		public void forceSizeOnly() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			BufferedImage thumbnail = Thumbnails.of(img)
					.forceSize(50, 50)
					.asBufferedImage();

			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The forceSize method is called twice.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void forceSizeTwice() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnails.of(img)
					.forceSize(50, 50)
					.forceSize(50, 50)
					.asBufferedImage();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The forceSize method is called.</li>
		 * <li>The keepAspectRatio method is called with true.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void forceSizeWithAspectRatioTrue() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnails.of(img)
					.forceSize(50, 50)
					.keepAspectRatio(true)
					.asBufferedImage();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The forceSize method is called.</li>
		 * <li>The keepAspectRatio method is called with false.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void forceSizeWithAspectRatioFalse() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnails.of(img)
					.forceSize(50, 50)
					.keepAspectRatio(false)
					.asBufferedImage();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The allowOverwrite method is called once.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>No exceptions are thrown.</li>
		 * </ol>
		 */
		@Test
		public void allowOverwriteCalledOnce() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			Thumbnails.of(img)
					.size(50, 50)
					.allowOverwrite(true)
					.asBufferedImage();

			// then
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The allowOverwrite method is called twice.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void allowOverwriteCalledTwice() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			Thumbnails.of(img)
					.size(50, 50)
					.allowOverwrite(true)
					.allowOverwrite(true)
					.asBufferedImage();

			// then
			fail();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The scale method is called.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail is successfully created.</li>
		 * <li>The thumbnail dimensions are determined by the value passed into the
		 * scale method.</li>
		 * </ol>
		 */
		@Test
		public void scaleOnly() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			BufferedImage thumbnail = Thumbnails.of(img)
					.scale(0.25f)
					.asBufferedImage();

			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The scale method is called.</li>
		 * <li>The keepAspectRatio method is true.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An {@link IllegalStateException} is thrown</li>
		 * </ol>
		 */
		@Test
		public void scaleWithAspectRatioTrue() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			try {
				Thumbnails.of(img)
						.scale(0.5f)
						.keepAspectRatio(true)
						.asBufferedImage();

				fail();
			} catch (IllegalStateException e) {
				assertTrue(e.getMessage().contains("scaling factor has already been specified"));
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The scale method is called.</li>
		 * <li>The keepAspectRatio method is false.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An {@link IllegalStateException} is thrown</li>
		 * </ol>
		 */
		@Test
		public void scaleWithAspectRatioFalse() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			try {
				Thumbnails.of(img)
						.scale(0.5f)
						.keepAspectRatio(false)
						.asBufferedImage();

				fail();
			} catch (IllegalStateException e) {
				assertTrue(e.getMessage().contains("scaling factor has already been specified"));
			}
		}

		@Test
		public void scaleWithScaleTwoArg() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(0.5f)
						.scale(0.5, 0.5)
						.asBufferedImage();

				fail();
			} catch (IllegalStateException e) {
				// then
				assertTrue(e.getMessage().contains("scale is already set"));
			}
		}

		@Test
		public void scaleWithSize() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(0.5f)
						.size(100, 100)
						.asBufferedImage();

				fail();
			} catch (IllegalStateException e) {
				// then
				assertTrue(e.getMessage().contains("size cannot be set"));
			}
		}

		@Test
		public void scaleTwoArgOnly() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.scale(0.6, 0.4)
					.asBufferedImage();

			// then
			assertEquals(120, thumbnail.getWidth());
			assertEquals(80, thumbnail.getHeight());
		}

		@Test
		public void scaleTwoArgWithAspectRatioTrue() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(0.6, 0.4)
						.keepAspectRatio(true)
						.asBufferedImage();

				fail();
			} catch (IllegalStateException e) {
				// then
				assertTrue(e.getMessage().contains("scaling factor has already been specified"));
			}
		}

		@Test
		public void scaleTwoArgWithAspectRatioFalse() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(0.6, 0.4)
						.keepAspectRatio(false)
						.asBufferedImage();

				fail();
			} catch (IllegalStateException e) {
				// then
				assertTrue(e.getMessage().contains("scaling factor has already been specified"));
			}
		}

		@Test
		public void scaleTwoArgWithScaleOneArg() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(0.6, 0.4)
						.scale(0.5)
						.asBufferedImage();

				fail();
			} catch (IllegalStateException e) {
				// then
				assertTrue(e.getMessage().contains("scale is already set"));
			}
		}

		@Test
		public void scaleTwoArgWithScaleTwoArg() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(0.6, 0.4)
						.scale(0.5, 0.5)
						.asBufferedImage();

				fail();
			} catch (IllegalStateException e) {
				// then
				assertTrue(e.getMessage().contains("scale is already set"));
			}
		}

		@Test
		public void scaleTwoArgWithSize() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(0.6, 0.4)
						.size(100, 100)
						.asBufferedImage();

				fail();
			} catch (IllegalStateException e) {
				// then
				assertTrue(e.getMessage().contains("size cannot be set"));
			}
		}

		@Test
		public void scaleWithZeroFactor() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(0.0)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertTrue(e.getMessage().contains("scaling factor is equal to or less than 0"));
			}
		}

		@Test
		public void scaleTwoArgWithValidAndZeroFactor() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(0.5, 0.0)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertTrue(e.getMessage().contains("scaling factor is equal to or less than 0"));
			}
		}

		@Test
		public void scaleTwoArgWithZeroFactorAndValid() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(0.0, 0.5)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertTrue(e.getMessage().contains("scaling factor is equal to or less than 0"));
			}
		}

		@Test
		public void scaleTwoArgWithZeroFactorAndZeroFactor() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(0.0, 0.0)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertTrue(e.getMessage().contains("scaling factor is equal to or less than 0"));
			}
		}

		@Test
		public void scaleWithNaN() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(Double.NaN)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertTrue(e.getMessage().contains("scaling factor is not a number"));
			}
		}

		@Test
		public void scaleTwoArgWithValidAndNaN() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(0.5, Double.NaN)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertTrue(e.getMessage().contains("scaling factor is not a number"));
			}
		}

		@Test
		public void scaleTwoArgWithNaNAndValid() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(Double.NaN, 0.5)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertTrue(e.getMessage().contains("scaling factor is not a number"));
			}
		}

		@Test
		public void scaleTwoArgWithNaNAndNaN() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(Double.NaN, Double.NaN)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertTrue(e.getMessage().contains("scaling factor is not a number"));
			}
		}

		@Test
		public void scaleWithPositiveInfinity() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(Double.POSITIVE_INFINITY)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertTrue(e.getMessage().contains("scaling factor cannot be infinity"));
			}
		}

		@Test
		public void scaleTwoArgWithValidAndPositiveInfinity() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(0.5, Double.POSITIVE_INFINITY)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertTrue(e.getMessage().contains("scaling factor cannot be infinity"));
			}
		}

		@Test
		public void scaleTwoArgWithPositiveInfinityAndValid() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(Double.POSITIVE_INFINITY, 0.5)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertTrue(e.getMessage().contains("scaling factor cannot be infinity"));
			}
		}

		@Test
		public void scaleTwoArgWithPositiveInfinityAndPositiveInfinity() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertTrue(e.getMessage().contains("scaling factor cannot be infinity"));
			}
		}

		@Test
		public void scaleWithNegativeInfinity() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(Double.NEGATIVE_INFINITY)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertTrue(e.getMessage().contains("scaling factor is equal to or less than 0"));
			}
		}

		@Test
		public void scaleTwoArgWithValidAndNegativeInfinity() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(0.5, Double.NEGATIVE_INFINITY)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertTrue(e.getMessage().contains("scaling factor is equal to or less than 0"));
			}
		}

		@Test
		public void scaleTwoArgWithNegativeInfinityAndValid() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(Double.NEGATIVE_INFINITY, 0.5)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertTrue(e.getMessage().contains("scaling factor is equal to or less than 0"));
			}
		}

		@Test
		public void scaleTwoArgWithNegativeInfinityAndNegativeInfinity() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			try {
				Thumbnails.of(img)
						.scale(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY)
						.asBufferedImage();

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertTrue(e.getMessage().contains("scaling factor is equal to or less than 0"));
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The keepAspectRatio method is called before the size method.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An {@link IllegalStateException} is thrown</li>
		 * </ol>
		 */
		@Test
		public void keepAspectRatioBeforeSize() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			try {
				Thumbnails.of(img)
						.keepAspectRatio(false);

				fail();
			} catch (IllegalStateException e) {
				assertTrue(e.getMessage().contains("unless the size parameter has already been specified."));
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The keepAspectRatio method is called after the scale method.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An {@link IllegalStateException} is thrown</li>
		 * </ol>
		 */
		@Test
		public void keepAspectRatioAfterScale() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			try {
				Thumbnails.of(img)
						.scale(0.5f)
						.keepAspectRatio(false)
						.asBufferedImage();

				fail();
			} catch (IllegalStateException e) {
				assertTrue(e.getMessage().contains("scaling factor has already been specified"));
			}
		}

		@Test
		public void keepAspectRatioAfterScaleTwoArg() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			try {
				Thumbnails.of(img)
						.scale(0.6, 0.4)
						.keepAspectRatio(true)
						.asBufferedImage();

				fail();
			} catch (IllegalStateException e) {
				assertTrue(e.getMessage().contains("scaling factor has already been specified"));
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The resizer method is called</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The specified Resizer is called for resizing.</li>
		 * </ol>
		 */
		@Test
		public void resizerOnly() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Resizer resizer = mock(Resizer.class);

			BufferedImage thumbnail = Thumbnails.of(img)
					.size(200, 200)
					.resizer(resizer)
					.asBufferedImage();

			verify(resizer).resize(img, thumbnail);
			verifyNoMoreInteractions(resizer);
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The resizer method is called twice</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An {@link IllegalStateException} is thrown</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void resizerTwice() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnails.of(img)
					.size(200, 200)
					.resizer(Resizers.PROGRESSIVE)
					.resizer(Resizers.PROGRESSIVE)
					.asBufferedImage();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The scalingMode method is called</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail is successfully created.</li>
		 * </ol>
		 */
		@Test
		public void scalingModeOnly() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnails.of(img)
					.size(200, 200)
					.scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
					.asBufferedImage();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The scalingMode method is called twice</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An {@link IllegalStateException} is thrown</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void scalingModeTwice() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnails.of(img)
					.size(200, 200)
					.scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
					.scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
					.asBufferedImage();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The resizer method is called, then</li>
		 * <li>The scalingMode method is called</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An {@link IllegalStateException} is thrown</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void resizerThenScalingMode() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnails.of(img)
					.size(200, 200)
					.resizer(Resizers.PROGRESSIVE)
					.scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
					.asBufferedImage();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The resizerFactory method is called, then</li>
		 * <li>The scalingMode method is called</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An {@link IllegalStateException} is thrown</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void resizerFactoryThenScalingMode() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnails.of(img)
					.size(200, 200)
					.resizerFactory(DefaultResizerFactory.getInstance())
					.scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
					.asBufferedImage();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The scalingMode method is called, then</li>
		 * <li>The resizer method is called</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An {@link IllegalStateException} is thrown</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void scalingModeThenResizer() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnails.of(img)
					.size(200, 200)
					.scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
					.resizer(Resizers.PROGRESSIVE)
					.asBufferedImage();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The scalingMode method is called, then</li>
		 * <li>The resizerFactory method is called</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An {@link IllegalStateException} is thrown</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void scalingModeThenResizerFactory() throws IOException {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnails.of(img)
					.size(200, 200)
					.scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
					.resizerFactory(DefaultResizerFactory.getInstance())
					.asBufferedImage();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The imageType method is not called</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The image type of the resulting image is the same as the original
		 * image</li>
		 * </ol>
		 */
		@Test
		public void imageTypeNotCalled() throws IOException {
			BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_BYTE_INDEXED);

			BufferedImage thumbnail = Thumbnails.of(img)
					.size(200, 200)
					.asBufferedImage();

			assertEquals(BufferedImage.TYPE_BYTE_INDEXED, thumbnail.getType());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The imageType method is called with the same type as the original
		 * image</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The image type of the resulting image is the same as the original
		 * image</li>
		 * </ol>
		 */
		@Test
		public void imageTypeCalledSameType() throws IOException {
			BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_BYTE_GRAY);

			BufferedImage thumbnail = Thumbnails.of(img)
					.size(200, 200)
					.imageType(BufferedImage.TYPE_BYTE_GRAY)
					.asBufferedImage();

			assertEquals(BufferedImage.TYPE_BYTE_GRAY, thumbnail.getType());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The imageType method is called with the different type as the
		 * original image</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The image type of the resulting image is the the specified by the
		 * imageType method</li>
		 * </ol>
		 */
		@Test
		public void imageTypeCalledDifferentType() throws IOException {
			BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_BYTE_GRAY);

			BufferedImage thumbnail = Thumbnails.of(img)
					.size(200, 200)
					.imageType(BufferedImage.TYPE_BYTE_GRAY)
					.asBufferedImage();

			assertEquals(BufferedImage.TYPE_BYTE_GRAY, thumbnail.getType());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The imageType method is called twice</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void imageTypeCalledTwice() throws IOException {
			BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_BYTE_GRAY);

			Thumbnails.of(img)
					.size(200, 200)
					.imageType(BufferedImage.TYPE_BYTE_GRAY)
					.imageType(BufferedImage.TYPE_BYTE_GRAY)
					.asBufferedImage();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>sourceRegion(Region) is called with valid parameters</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail is successfully produced</li>
		 * <li>The specified range is used as the source</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void sourceRegion_Region() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.sourceRegion(new Region(new Coordinate(0, 0), new AbsoluteSize(50, 50)))
					.size(50, 50)
					.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
			assertTrue(BufferedImageComparer.isRGBSimilar(thumbnail, img.getSubimage(0, 0, 50, 50)));
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>sourceRegion(Region) is called with null</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An NullPointerException is thrown</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void sourceRegion_Region_Null() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			try {
				Thumbnails.of(img)
						.sourceRegion((Region) null)
						.size(50, 50)
						.asBufferedImage();
				fail();
			} catch (NullPointerException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>sourceRegion(Rectangle) is called with valid parameters</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail is successfully produced</li>
		 * <li>The specified range is used as the source</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void sourceRegion_Rectangle() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.sourceRegion(new Rectangle(0, 0, 50, 50))
					.size(50, 50)
					.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
			assertTrue(BufferedImageComparer.isRGBSimilar(thumbnail, img.getSubimage(0, 0, 50, 50)));
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>sourceRegion(Region) is called with null</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An NullPointerException is thrown</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void sourceRegion_Rectangle_Null() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			try {
				Thumbnails.of(img)
						.sourceRegion((Rectangle) null)
						.size(50, 50)
						.asBufferedImage();
				fail();
			} catch (NullPointerException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>sourceRegion(Position, Size) is called with valid parameters</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail is successfully produced</li>
		 * <li>The specified range is used as the source</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void sourceRegion_PositionSize() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.sourceRegion(new Coordinate(0, 0), new AbsoluteSize(50, 50))
					.size(50, 50)
					.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
			assertTrue(BufferedImageComparer.isRGBSimilar(thumbnail, img.getSubimage(0, 0, 50, 50)));
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>sourceRegion(Position, Size) is called with Position as null</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An NullPointerException is thrown</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void sourceRegion_PositionSize_PositionNull() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			try {
				Thumbnails.of(img)
						.sourceRegion(null, new AbsoluteSize(50, 50))
						.size(50, 50)
						.asBufferedImage();
				fail();
			} catch (NullPointerException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>sourceRegion(Position, Size) is called with Size as null</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An NullPointerException is thrown</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void sourceRegion_PositionSize_SizeNull() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			try {
				Thumbnails.of(img)
						.sourceRegion(new Coordinate(0, 0), null)
						.size(50, 50)
						.asBufferedImage();
				fail();
			} catch (NullPointerException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>sourceRegion(Position, int, int) is called with valid parameters</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail is successfully produced</li>
		 * <li>The specified range is used as the source</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void sourceRegion_PositionIntInt() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.sourceRegion(new Coordinate(0, 0), 50, 50)
					.size(50, 50)
					.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
			assertTrue(BufferedImageComparer.isRGBSimilar(thumbnail, img.getSubimage(0, 0, 50, 50)));
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>sourceRegion(Position, int, int) is called with Position as null</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An NullPointerException is thrown</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void sourceRegion_PositionIntInt_PositionNull() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			try {
				Thumbnails.of(img)
						.sourceRegion(null, 50, 50)
						.size(50, 50)
						.asBufferedImage();
				fail();
			} catch (NullPointerException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>sourceRegion(Position, int, int) is called with width as non-positive</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void sourceRegion_PositionIntInt_WidthNonPositive() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			try {
				Thumbnails.of(img)
						.sourceRegion(new Coordinate(0, 0), -1, 50)
						.size(50, 50)
						.asBufferedImage();
				fail();
			} catch (IllegalArgumentException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>sourceRegion(Position, int, int) is called with height as non-positive</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void sourceRegion_PositionIntInt_HeightNonPositive() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			try {
				Thumbnails.of(img)
						.sourceRegion(new Coordinate(0, 0), 50, -1)
						.size(50, 50)
						.asBufferedImage();
				fail();
			} catch (IllegalArgumentException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>sourceRegion(int, int, int, int) is called with valid parameters</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail is successfully produced</li>
		 * <li>The specified range is used as the source</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void sourceRegion_IntIntIntInt() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.sourceRegion(0, 0, 50, 50)
					.size(50, 50)
					.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
			assertTrue(BufferedImageComparer.isRGBSimilar(thumbnail, img.getSubimage(0, 0, 50, 50)));
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>sourceRegion(int, int, int, int) is called with width as non-positive</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void sourceRegion_IntIntIntInt_WidthNonPositive() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			try {
				Thumbnails.of(img)
						.sourceRegion(0, 0, -1, 50)
						.size(50, 50)
						.asBufferedImage();
				fail();
			} catch (IllegalArgumentException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>sourceRegion(int, int, int, int) is called with height as non-positive</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void sourceRegion_IntIntIntInt_HeightNonPositive() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			try {
				Thumbnails.of(img)
						.sourceRegion(0, 0, 50, -1)
						.size(50, 50)
						.asBufferedImage();
				fail();
			} catch (IllegalArgumentException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputQuality(float) is 0.0f</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The outputQuality is allowed</li>
		 * <li>The thumbnail is successfully produced</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputQuality_float_ValidArg_ZeroZero() throws IOException {
			InputStream is = TestUtils.getResourceStream("Thumbnailator/grid.jpg");
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			Thumbnails.of(is)
					.size(50, 50)
					.outputFormat("jpg")
					.outputQuality(0.0f)
					.toOutputStream(os);

			BufferedImage thumbnail = ImageIO.read(new ByteArrayInputStream(os.toByteArray()));

			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputQuality(float) is 0.5f</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The outputQuality is allowed</li>
		 * <li>The thumbnail is successfully produced</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputQuality_float_ValidArg_ZeroFive() throws IOException {
			InputStream is = TestUtils.getResourceStream("Thumbnailator/grid.jpg");
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			Thumbnails.of(is)
					.size(50, 50)
					.outputFormat("jpg")
					.outputQuality(0.5f)
					.toOutputStream(os);

			BufferedImage thumbnail = ImageIO.read(new ByteArrayInputStream(os.toByteArray()));

			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputQuality(float) is 1.0f</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The outputQuality is allowed</li>
		 * <li>The thumbnail is successfully produced</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputQuality_float_ValidArg_OneZero() throws IOException {
			InputStream is = TestUtils.getResourceStream("Thumbnailator/grid.jpg");
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			Thumbnails.of(is)
					.size(50, 50)
					.outputFormat("jpg")
					.outputQuality(1.0f)
					.toOutputStream(os);

			BufferedImage thumbnail = ImageIO.read(new ByteArrayInputStream(os.toByteArray()));

			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputQuality(float) is negative</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test(expected = IllegalArgumentException.class)
		public void outputQuality_float_InvalidArg_Negative() throws IOException {
			InputStream is = TestUtils.getResourceStream("Thumbnailator/grid.jpg");

			Thumbnails.of(is)
					.size(50, 50)
					.outputFormat("jpg")
					.outputQuality(-0.01f);
		}


		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputQuality(float) is greater than 1.0d</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test(expected = IllegalArgumentException.class)
		public void outputQuality_float_InvalidArg_OverOne() throws IOException {
			InputStream is = TestUtils.getResourceStream("Thumbnailator/grid.jpg");

			Thumbnails.of(is)
					.size(50, 50)
					.outputFormat("jpg")
					.outputQuality(1.01f);
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputQuality(double) is 0.0d</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The outputQuality is allowed</li>
		 * <li>The thumbnail is successfully produced</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputQuality_double_ValidArg_ZeroZero() throws IOException {
			InputStream is = TestUtils.getResourceStream("Thumbnailator/grid.jpg");
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			Thumbnails.of(is)
					.size(50, 50)
					.outputFormat("jpg")
					.outputQuality(0.0d)
					.toOutputStream(os);

			BufferedImage thumbnail = ImageIO.read(new ByteArrayInputStream(os.toByteArray()));

			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputQuality(double) is 0.5d</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The outputQuality is allowed</li>
		 * <li>The thumbnail is successfully produced</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputQuality_double_ValidArg_ZeroFive() throws IOException {
			InputStream is = TestUtils.getResourceStream("Thumbnailator/grid.jpg");
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			Thumbnails.of(is)
					.size(50, 50)
					.outputFormat("jpg")
					.outputQuality(0.5d)
					.toOutputStream(os);

			BufferedImage thumbnail = ImageIO.read(new ByteArrayInputStream(os.toByteArray()));

			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputQuality(double) is 1.0d</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The outputQuality is allowed</li>
		 * <li>The thumbnail is successfully produced</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputQuality_double_ValidArg_OneZero() throws IOException {
			InputStream is = TestUtils.getResourceStream("Thumbnailator/grid.jpg");
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			Thumbnails.of(is)
					.size(50, 50)
					.outputFormat("jpg")
					.outputQuality(1.0d)
					.toOutputStream(os);

			BufferedImage thumbnail = ImageIO.read(new ByteArrayInputStream(os.toByteArray()));

			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputQuality(double) is negative</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test(expected = IllegalArgumentException.class)
		public void outputQuality_double_InvalidArg_Negative() throws IOException {
			InputStream is = TestUtils.getResourceStream("Thumbnailator/grid.jpg");

			Thumbnails.of(is)
					.size(50, 50)
					.outputFormat("jpg")
					.outputQuality(-0.01d);
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputQuality(double) is greater than 1.0d</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test(expected = IllegalArgumentException.class)
		public void outputQuality_double_InvalidArg_OverOne() throws IOException {
			InputStream is = TestUtils.getResourceStream("Thumbnailator/grid.jpg");

			Thumbnails.of(is)
					.size(50, 50)
					.outputFormat("jpg")
					.outputQuality(1.01d);
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputFormat with a supported format</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>No exception is thrown.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputFormat_SupportedFormat() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			Thumbnails.of(img)
					.size(50, 50)
					.outputFormat("png");
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputFormat with a supported format</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test(expected = IllegalArgumentException.class)
		public void outputFormat_UnsupportedFormat() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			Thumbnails.of(img)
					.size(50, 50)
					.outputFormat("unsupported");

			// then
			// expect an IllegalArgumentException.
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputFormat specified</li>
		 * <li>outputFormatType is supported</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>No exception is thrown</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputFormat_Checks_FormatSpecified_TypeSupported() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			Thumbnails.of(img)
					.size(50, 50)
					.outputFormat("JPEG")
					.outputFormatType("JPEG");

			// then
			// no exception occurs
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputFormat specified</li>
		 * <li>outputFormatType is specified, but the outputFormat does not
		 * support compression.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test(expected = IllegalArgumentException.class)
		public void outputFormat_Checks_FormatSpecified_FormatDoesNotSupportCompression() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			Thumbnails.of(img)
					.size(50, 50)
					.outputFormat("PNG")
					.outputFormatType("JPEG");

			// then
			// expect an IllegalArgumentException.
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputFormat specified</li>
		 * <li>outputFormatType is unsupported</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test(expected = IllegalArgumentException.class)
		public void outputFormat_Checks_FormatSpecified_TypeUnsupported() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			Thumbnails.of(img)
					.size(50, 50)
					.outputFormat("JPEG")
					.outputFormatType("foo");

			// then
			// expect an IllegalArgumentException.
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputFormat specified</li>
		 * <li>outputFormatType is default for the output format</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>No exception is thrown</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputFormat_Checks_FormatSpecified_TypeDefault() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			Thumbnails.of(img)
					.size(50, 50)
					.outputFormat("JPEG")
					.outputFormatType(ThumbnailParameter.DEFAULT_FORMAT_TYPE);

			// then
			// no exception occurs
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputFormat is the format of the original image</li>
		 * <li>outputFormatType is specified</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test(expected = IllegalArgumentException.class)
		public void outputFormat_Checks_FormatSpecifiedAsOriginal_TypeSpecified() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			// when
			Thumbnails.of(img)
					.size(50, 50)
					.outputFormat(ThumbnailParameter.ORIGINAL_FORMAT)
					.outputFormatType("JPEG");

			// then
			// expect an IllegalArgumentException.
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputFormat is the format of the original image</li>
		 * <li>outputFormatType is specified</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>No exception is thrown.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputFormat_Checks_FormatSpecifiedAsOriginal_TypeIsDefaultForFormat() throws IOException {
			// given
			BufferedImage img = TestUtils.getImageFromResource("Thumbnailator/grid.jpg");

			// when
			Thumbnails.of(img)
					.size(50, 50)
					.outputFormat(ThumbnailParameter.ORIGINAL_FORMAT)
					.outputFormatType(ThumbnailParameter.DEFAULT_FORMAT_TYPE);

			// then
			// no exception is thrown.
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The resizer method is called with null</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A NullPointerException is thrown</li>
		 * </ol>
		 */
		@Test(expected = NullPointerException.class)
		public void resizer_Null() {
			try {
				// given
				// when
				Thumbnails.of("non-existent-file")
						.size(200, 200)
						.resizer(null);
			} catch (NullPointerException e) {
				// then
				assertEquals("Resizer is null.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The alphaInterpolation method is called with null</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A NullPointerException is thrown</li>
		 * </ol>
		 */
		@Test(expected = NullPointerException.class)
		public void alphaInterpolation_Null() {
			try {
				// given
				// when
				Thumbnails.of("non-existent-file")
						.size(200, 200)
						.alphaInterpolation(null);
			} catch (NullPointerException e) {
				// then
				assertEquals("Alpha interpolation is null.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The dithering method is called with null</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A NullPointerException is thrown</li>
		 * </ol>
		 */
		@Test(expected = NullPointerException.class)
		public void dithering_Null() {
			try {
				// given
				// when
				Thumbnails.of("non-existent-file")
						.size(200, 200)
						.dithering(null);
			} catch (NullPointerException e) {
				// then
				assertEquals("Dithering is null.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The antialiasing method is called with null</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A NullPointerException is thrown</li>
		 * </ol>
		 */
		@Test(expected = NullPointerException.class)
		public void antialiasing_Null() {
			try {
				// given
				// when
				Thumbnails.of("non-existent-file")
						.size(200, 200)
						.antialiasing(null);
			} catch (NullPointerException e) {
				// then
				assertEquals("Antialiasing is null.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The rendering method is called with null</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A NullPointerException is thrown</li>
		 * </ol>
		 */
		@Test(expected = NullPointerException.class)
		public void rendering_Null() {
			try {
				// given
				// when
				Thumbnails.of("non-existent-file")
						.size(200, 200)
						.rendering(null);
			} catch (NullPointerException e) {
				// then
				assertEquals("Rendering is null.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The scalingMode method is called with null</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A NullPointerException is thrown</li>
		 * </ol>
		 */
		@Test(expected = NullPointerException.class)
		public void scalingMode_Null() {
			try {
				// given
				// when
				Thumbnails.of("non-existent-file")
						.size(200, 200)
						.scalingMode(null);
			} catch (NullPointerException e) {
				// then
				assertEquals("Scaling mode is null.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The resizer method is called.</li>
		 * <li>Then, the resizerFactory method is called.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void resizerThenResizerFactory() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			Thumbnails.of(img)
					.size(50, 50)
					.resizer(Resizers.PROGRESSIVE)
					.resizerFactory(DefaultResizerFactory.getInstance())
					.asBufferedImage();

			// then
			fail();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The resizerFactory method is called.</li>
		 * <li>Then, the resizer method is called.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void resizerFactoryThenResizer() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			Thumbnails.of(img)
					.size(50, 50)
					.resizerFactory(DefaultResizerFactory.getInstance())
					.resizer(Resizers.PROGRESSIVE)
					.asBufferedImage();

			// then
			fail();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The resizer method is called.</li>
		 * <li>Then, the alphaInterpolation method is called.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A thumbnail is created successfully.</li>
		 * </ol>
		 */
		@Test
		public void resizerThenAlphainterpolation() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(50, 50)
					.resizer(Resizers.PROGRESSIVE)
					.alphaInterpolation(AlphaInterpolation.SPEED)
					.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The resizer method is called.</li>
		 * <li>Then, the dithering method is called.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A thumbnail is created successfully.</li>
		 * </ol>
		 */
		@Test
		public void resizerThenDithering() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(50, 50)
					.resizer(Resizers.PROGRESSIVE)
					.dithering(Dithering.DEFAULT)
					.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The resizer method is called.</li>
		 * <li>Then, the antialiasing method is called.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A thumbnail is created successfully.</li>
		 * </ol>
		 */
		@Test
		public void resizerThenAntialiasing() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(50, 50)
					.resizer(Resizers.PROGRESSIVE)
					.antialiasing(Antialiasing.DEFAULT)
					.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The resizer method is called.</li>
		 * <li>Then, the rendering method is called.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A thumbnail is created successfully.</li>
		 * </ol>
		 */
		@Test
		public void resizerThenRendering() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(50, 50)
					.resizer(Resizers.PROGRESSIVE)
					.rendering(Rendering.DEFAULT)
					.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The resizer method is called.</li>
		 * <li>Then, the alphaInterpolation method is called.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A thumbnail is created successfully.</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void resizerFactoryThenAlphainterpolation() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			Thumbnails.of(img)
					.size(50, 50)
					.resizerFactory(DefaultResizerFactory.getInstance())
					.alphaInterpolation(AlphaInterpolation.SPEED)
					.asBufferedImage();

			// then
			fail();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The resizer method is called.</li>
		 * <li>Then, the dithering method is called.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A thumbnail is created successfully.</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void resizerFactoryThenDithering() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			Thumbnails.of(img)
					.size(50, 50)
					.resizerFactory(DefaultResizerFactory.getInstance())
					.dithering(Dithering.DEFAULT)
					.asBufferedImage();

			// then
			fail();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The resizer method is called.</li>
		 * <li>Then, the antialiasing method is called.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A thumbnail is created successfully.</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void resizerFactoryThenAntialiasing() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			Thumbnails.of(img)
					.size(50, 50)
					.resizerFactory(DefaultResizerFactory.getInstance())
					.antialiasing(Antialiasing.DEFAULT)
					.asBufferedImage();

			// then
			fail();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The resizer method is called.</li>
		 * <li>Then, the rendering method is called.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A thumbnail is created successfully.</li>
		 * </ol>
		 */
		@Test(expected = IllegalStateException.class)
		public void resizerFactoryThenRendering() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			Thumbnails.of(img)
					.size(50, 50)
					.resizerFactory(DefaultResizerFactory.getInstance())
					.rendering(Rendering.DEFAULT)
					.asBufferedImage();

			// then
			fail();
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The width method is called</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail size is constrained by the width</li>
		 * <li>The thumbnail size maintains the aspect ratio of the original</li>
		 * </ol>
		 */
		@Test
		public void width() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 100).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.width(50)
					.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(25, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The height method is called</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail size is constrained by the width</li>
		 * <li>The thumbnail size maintains the aspect ratio of the original</li>
		 * </ol>
		 */
		@Test
		public void height() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 100).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.height(50)
					.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The width method is called</li>
		 * <li>The height method is called</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail size is constrained by the width</li>
		 * <li>The thumbnail size is constrained by the height</li>
		 * <li>The image is constrained to the smallest dimension</li>
		 * <li>The thumbnail size maintains the aspect ratio of the original</li>
		 * </ol>
		 */
		@Test
		public void widthAndHeight() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 100).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.width(50)
					.height(50)
					.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(25, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The height method is called</li>
		 * <li>The width method is called</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail size is constrained by the width</li>
		 * <li>The thumbnail size is constrained by the height</li>
		 * <li>The image is constrained to the smallest dimension</li>
		 * <li>The thumbnail size maintains the aspect ratio of the original</li>
		 * </ol>
		 */
		@Test
		public void heightAndWidth() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 100).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.height(50)
					.width(50)
					.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(25, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The width method is called</li>
		 * <li>The keepAspectRatio is called with false</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void widthNotPreservingTheAspectRatio() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 100).build();

			try {
				// when
				Thumbnails.of(img)
						.keepAspectRatio(false)
						.width(50)
						.asBufferedImage();

				fail();
			} catch (IllegalStateException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The height method is called</li>
		 * <li>The keepAspectRatio method is called with false</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void heightNotPreservingTheAspectRatio() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 100).build();

			try {
				// when
				Thumbnails.of(img)
						.height(50)
						.keepAspectRatio(false)
						.asBufferedImage();
			} catch (IllegalStateException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The width method is called</li>
		 * <li>The height method is called</li>
		 * <li>The keepAspectRatio is called with false</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void widthAndHeightNotPreservingTheAspectRatio() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 100).build();

			try {
				// when
				Thumbnails.of(img)
						.width(50)
						.height(50)
						.keepAspectRatio(false)
						.asBufferedImage();
			} catch (IllegalStateException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The width method is called</li>
		 * <li>The height method is called</li>
		 * <li>The keepAspectRatio is called with false</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void heightAndWidthNotPreservingTheAspectRatio() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 100).build();

			try {
				// when
				Thumbnails.of(img)
						.height(50)
						.width(50)
						.keepAspectRatio(false)
						.asBufferedImage();
			} catch (IllegalStateException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The keepAspectRatio is called with false, first</li>
		 * <li>The width method is called</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void widthNotPreservingTheAspectRatioFirst() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 100).build();

			try {
				// when
				Thumbnails.of(img)
						.keepAspectRatio(false)
						.width(50)
						.asBufferedImage();

				fail();
			} catch (IllegalStateException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The keepAspectRatio is called with false, first</li>
		 * <li>The height method is called</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void heightNotPreservingTheAspectRatioFirst() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 100).build();

			try {
				// when
				Thumbnails.of(img)
						.keepAspectRatio(false)
						.height(50)
						.asBufferedImage();
			} catch (IllegalStateException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The keepAspectRatio is called with false, first</li>
		 * <li>The width method is called</li>
		 * <li>The height method is called</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void widthAndHeightNotPreservingTheAspectRatioFirst() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 100).build();

			try {
				// when
				Thumbnails.of(img)
						.keepAspectRatio(false)
						.width(50)
						.height(50)
						.asBufferedImage();
			} catch (IllegalStateException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The keepAspectRatio is called with false, first</li>
		 * <li>The width method is called</li>
		 * <li>The height method is called</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void heightAndWidthNotPreservingTheAspectRatioFirst() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 100).build();

			try {
				// when
				Thumbnails.of(img)
						.keepAspectRatio(false)
						.height(50)
						.width(50)
						.asBufferedImage();
			} catch (IllegalStateException e) {
				// then
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The width method is called</li>
		 * <li>The keepAspectRatio is called with true</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail size is constrained by the width</li>
		 * <li>The thumbnail size maintains the aspect ratio of the original</li>
		 * </ol>
		 */
		@Test
		public void widthAndPreservingTheAspectRatio() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 100).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.width(50)
					.keepAspectRatio(true)
					.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(25, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The height method is called</li>
		 * <li>The keepAspectRatio is called with true</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail size is constrained by the width</li>
		 * <li>The thumbnail size maintains the aspect ratio of the original</li>
		 * </ol>
		 */
		@Test
		public void heightAndPreservingTheAspectRatio() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 100).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.height(50)
					.keepAspectRatio(true)
					.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The width method is called</li>
		 * <li>The height method is called</li>
		 * <li>The keepAspectRatio is called with true</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail size is constrained by the width</li>
		 * <li>The thumbnail size is constrained by the height</li>
		 * <li>The image is constrained to the smallest dimension</li>
		 * <li>The thumbnail size maintains the aspect ratio of the original</li>
		 * </ol>
		 */
		@Test
		public void widthAndHeightAndPreservingTheAspectRatio() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 100).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.width(50)
					.height(50)
					.keepAspectRatio(true)
					.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(25, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The height method is called</li>
		 * <li>The width method is called</li>
		 * <li>The keepAspectRatio is called with true</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail size is constrained by the width</li>
		 * <li>The thumbnail size is constrained by the height</li>
		 * <li>The image is constrained to the smallest dimension</li>
		 * <li>The thumbnail size maintains the aspect ratio of the original</li>
		 * </ol>
		 */
		@Test
		public void heightAndWidthAndPreservingTheAspectRatio() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 100).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.height(50)
					.width(50)
					.keepAspectRatio(true)
					.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(25, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>The resizerFactory method is called.</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The specified {@link ResizerFactory} is called when resizing.</li>
		 * </ol>
		 */
		@Test
		public void resizerFactoryCalledOnResize() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			ResizerFactory resizerFactory = mock(ResizerFactory.class);
			when(
					resizerFactory.getResizer(any(Dimension.class), any(Dimension.class))
			).thenReturn(Resizers.NULL);

			// when
			Thumbnails.of(img)
					.resizerFactory(resizerFactory)
					.size(50, 50)
					.asBufferedImage();

			// then
			verify(resizerFactory).getResizer(new Dimension(200, 200), new Dimension(50, 50));
		}

		@Test
		public void cropWithSizeBefore() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(100, 50)
					.crop(Positions.CENTER)
					.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		@Test
		public void cropWithSizeAfter() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.crop(Positions.CENTER)
					.size(100, 50)
					.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		@Test
		public void cropWithAspectRatioTrueBefore() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(100, 50)
					.keepAspectRatio(true)
					.crop(Positions.CENTER)
					.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		@Test
		public void cropWithAspectRatioTrueAfter() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.crop(Positions.CENTER)
					.size(100, 50)
					.keepAspectRatio(true)
					.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		@Test
		public void cropWithAspectRatioFalseBefore() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(100, 50)
					.keepAspectRatio(false)
					.crop(Positions.CENTER)
					.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		@Test
		public void cropWithAspectRatioFalseAfter() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.crop(Positions.CENTER)
					.size(100, 50)
					.keepAspectRatio(false)
					.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		@Test
		public void cropWithScaleBeforeShouldFail() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			try {
				// when
				Thumbnails.of(img)
						.scale(0.5)
						.crop(Positions.CENTER)
						.asBufferedImage();

				fail();
			} catch (IllegalStateException e) {
				// then
			}
		}

		@Test
		public void cropWithScaleAfterShouldFail() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			try {
				// when
				Thumbnails.of(img)
						.crop(Positions.CENTER)
						.scale(0.5)
						.asBufferedImage();

				fail();
			} catch (IllegalStateException e) {
				// then
			}
		}

		@Test
		public void cropTwice() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			try {
				// when
				Thumbnails.of(img)
						.crop(Positions.CENTER)
						.crop(Positions.CENTER);

				fail();
			} catch (IllegalStateException e) {
				// then
			}
		}

		@Test
		public void cropCenterThenWatermarkCenter() throws IOException {
			// given
			// when
			BufferedImage thumbnail = Thumbnails.of(ORIGINAL_IMAGE)
					.size(100, 100)
					.crop(Positions.CENTER)
					.watermark(Positions.CENTER, WATERMARK_IMAGE, 1.0f)
					.asBufferedImage();

			// then
			BufferedImage expected = new BufferedImageBuilder(100, 100).build();
			Graphics g = expected.getGraphics();
			g.setColor(BACKGROUND_COLOR);
			g.fillRect(0, 0, 100, 100);
			g.setColor(WATERMARK_COLOR);
			g.fillRect(25, 25, 50, 50);
			g.dispose();

			assertTrue(BufferedImageComparer.isSame(expected, thumbnail));
		}

		@Test
		public void cropCenterThenWatermarkTopLeft() throws IOException {
			// given
			// when
			BufferedImage thumbnail = Thumbnails.of(ORIGINAL_IMAGE)
					.size(100, 100)
					.crop(Positions.CENTER)
					.watermark(Positions.TOP_LEFT, WATERMARK_IMAGE, 1.0f)
					.asBufferedImage();

			// then
			BufferedImage expected = new BufferedImageBuilder(100, 100).build();
			Graphics g = expected.getGraphics();
			g.setColor(BACKGROUND_COLOR);
			g.fillRect(0, 0, 100, 100);
			g.setColor(WATERMARK_COLOR);
			g.fillRect(0, 0, 50, 50);
			g.dispose();

			assertTrue(BufferedImageComparer.isSame(expected, thumbnail));
		}

		@Test
		public void cropCenterThenWatermarkTopRight() throws IOException {
			// given
			// when
			BufferedImage thumbnail = Thumbnails.of(ORIGINAL_IMAGE)
					.size(100, 100)
					.crop(Positions.CENTER)
					.watermark(Positions.TOP_RIGHT, WATERMARK_IMAGE, 1.0f)
					.asBufferedImage();

			// then
			BufferedImage expected = new BufferedImageBuilder(100, 100).build();
			Graphics g = expected.getGraphics();
			g.setColor(BACKGROUND_COLOR);
			g.fillRect(0, 0, 100, 100);
			g.setColor(WATERMARK_COLOR);
			g.fillRect(50, 0, 50, 50);
			g.dispose();

			assertTrue(BufferedImageComparer.isSame(expected, thumbnail));
		}

		@Test
		public void cropCenterThenWatermarkBottomLeft() throws IOException {
			// given
			// when
			BufferedImage thumbnail = Thumbnails.of(ORIGINAL_IMAGE)
					.size(100, 100)
					.crop(Positions.CENTER)
					.watermark(Positions.BOTTOM_LEFT, WATERMARK_IMAGE, 1.0f)
					.asBufferedImage();

			// then
			BufferedImage expected = new BufferedImageBuilder(100, 100).build();
			Graphics g = expected.getGraphics();
			g.setColor(BACKGROUND_COLOR);
			g.fillRect(0, 0, 100, 100);
			g.setColor(WATERMARK_COLOR);
			g.fillRect(0, 50, 50, 50);
			g.dispose();

			assertTrue(BufferedImageComparer.isSame(expected, thumbnail));
		}

		@Test
		public void cropCenterThenWatermarkBottomRight() throws IOException {
			// given
			// when
			BufferedImage thumbnail = Thumbnails.of(ORIGINAL_IMAGE)
					.size(100, 100)
					.crop(Positions.CENTER)
					.watermark(Positions.BOTTOM_RIGHT, WATERMARK_IMAGE, 1.0f)
					.asBufferedImage();

			// then
			BufferedImage expected = new BufferedImageBuilder(100, 100).build();
			Graphics g = expected.getGraphics();
			g.setColor(BACKGROUND_COLOR);
			g.fillRect(0, 0, 100, 100);
			g.setColor(WATERMARK_COLOR);
			g.fillRect(50, 50, 50, 50);
			g.dispose();

			assertTrue(BufferedImageComparer.isSame(expected, thumbnail));
		}

		/**
		 * Return a {@code BufferedImage} filled with a color.
		 *
		 * @param width  Width of image.
		 * @param height Height of image.
		 * @param color  Color to fill image with.
		 * @return A {@code BufferedImage} filled with a color.
		 */
		private static BufferedImage makeColorFilledImage(int width, int height, Color color) {
			BufferedImage img = new BufferedImageBuilder(width, height).build();
			Graphics g = img.getGraphics();
			g.setColor(color);
			g.fillRect(0, 0, width, height);
			g.dispose();
			return img;
		}

		@Test
		public void cropCenterThenWatermarkCenterNonSquareOriginalWide() throws IOException {
			// given
			BufferedImage img = makeColorFilledImage(300, 200, BACKGROUND_COLOR);
			BufferedImage watermark = makeColorFilledImage(25, 25, WATERMARK_COLOR);

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(100, 100)
					.crop(Positions.CENTER)
					.watermark(Positions.CENTER, watermark, 1.0f)
					.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 50));
			assertEquals(Color.white.getRGB(), thumbnail.getRGB(50, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 99));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 99));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 99));
		}

		@Test
		public void cropCenterThenWatermarkTopLeftNonSquareOriginalWide() throws IOException {
			// given
			BufferedImage img = makeColorFilledImage(300, 200, BACKGROUND_COLOR);
			BufferedImage watermark = makeColorFilledImage(25, 25, WATERMARK_COLOR);

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(100, 100)
					.crop(Positions.CENTER)
					.watermark(Positions.TOP_LEFT, watermark, 1.0f)
					.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
			assertEquals(Color.white.getRGB(), thumbnail.getRGB(0, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 99));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 99));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 99));
		}

		@Test
		public void cropCenterThenWatermarkTopRightNonSquareOriginalWide() throws IOException {
			// given
			BufferedImage img = makeColorFilledImage(300, 200, BACKGROUND_COLOR);
			BufferedImage watermark = makeColorFilledImage(25, 25, WATERMARK_COLOR);

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(100, 100)
					.crop(Positions.CENTER)
					.watermark(Positions.TOP_RIGHT, watermark, 1.0f)
					.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 0));
			assertEquals(Color.white.getRGB(), thumbnail.getRGB(99, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 99));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 99));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 99));
		}

		@Test
		public void cropCenterThenWatermarkBottomLeftNonSquareOriginalWide() throws IOException {
			// given
			BufferedImage img = makeColorFilledImage(300, 200, BACKGROUND_COLOR);
			BufferedImage watermark = makeColorFilledImage(25, 25, WATERMARK_COLOR);

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(100, 100)
					.crop(Positions.CENTER)
					.watermark(Positions.BOTTOM_LEFT, watermark, 1.0f)
					.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 50));
			assertEquals(Color.white.getRGB(), thumbnail.getRGB(0, 99));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 99));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 99));
		}

		@Test
		public void cropCenterThenWatermarkBottomRightNonSquareOriginalWide() throws IOException {
			// given
			BufferedImage img = makeColorFilledImage(300, 200, BACKGROUND_COLOR);
			BufferedImage watermark = makeColorFilledImage(25, 25, WATERMARK_COLOR);

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(100, 100)
					.crop(Positions.CENTER)
					.watermark(Positions.BOTTOM_RIGHT, watermark, 1.0f)
					.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 99));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 99));
			assertEquals(Color.white.getRGB(), thumbnail.getRGB(99, 99));
		}

		@Test
		public void cropCenterThenWatermarkCenterNonSquareOriginalTall() throws IOException {
			// given
			BufferedImage img = makeColorFilledImage(200, 300, BACKGROUND_COLOR);
			BufferedImage watermark = makeColorFilledImage(25, 25, WATERMARK_COLOR);

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(100, 100)
					.crop(Positions.CENTER)
					.watermark(Positions.CENTER, watermark, 1.0f)
					.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 50));
			assertEquals(Color.white.getRGB(), thumbnail.getRGB(50, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 99));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 99));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 99));
		}

		@Test
		public void cropCenterThenWatermarkTopLeftNonSquareOriginalTall() throws IOException {
			// given
			BufferedImage img = makeColorFilledImage(200, 300, BACKGROUND_COLOR);
			BufferedImage watermark = makeColorFilledImage(25, 25, WATERMARK_COLOR);

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(100, 100)
					.crop(Positions.CENTER)
					.watermark(Positions.TOP_LEFT, watermark, 1.0f)
					.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
			assertEquals(Color.white.getRGB(), thumbnail.getRGB(0, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 99));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 99));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 99));
		}

		@Test
		public void cropCenterThenWatermarkTopRightNonSquareOriginalTall() throws IOException {
			// given
			BufferedImage img = makeColorFilledImage(200, 300, BACKGROUND_COLOR);
			BufferedImage watermark = makeColorFilledImage(25, 25, WATERMARK_COLOR);

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(100, 100)
					.crop(Positions.CENTER)
					.watermark(Positions.TOP_RIGHT, watermark, 1.0f)
					.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 0));
			assertEquals(Color.white.getRGB(), thumbnail.getRGB(99, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 99));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 99));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 99));
		}

		@Test
		public void cropCenterThenWatermarkBottomLeftNonSquareOriginalTall() throws IOException {
			// given
			BufferedImage img = makeColorFilledImage(200, 300, BACKGROUND_COLOR);
			BufferedImage watermark = makeColorFilledImage(25, 25, WATERMARK_COLOR);

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(100, 100)
					.crop(Positions.CENTER)
					.watermark(Positions.BOTTOM_LEFT, watermark, 1.0f)
					.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 50));
			assertEquals(Color.white.getRGB(), thumbnail.getRGB(0, 99));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 99));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 99));
		}

		@Test
		public void cropCenterThenWatermarkBottomRightNonSquareOriginalTall() throws IOException {
			// given
			BufferedImage img = makeColorFilledImage(200, 300, BACKGROUND_COLOR);
			BufferedImage watermark = makeColorFilledImage(25, 25, WATERMARK_COLOR);

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
					.size(100, 100)
					.crop(Positions.CENTER)
					.watermark(Positions.BOTTOM_RIGHT, watermark, 1.0f)
					.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 0));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(99, 50));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(0, 99));
			assertEquals(Color.black.getRGB(), thumbnail.getRGB(50, 99));
			assertEquals(Color.white.getRGB(), thumbnail.getRGB(99, 99));
		}

		@Test
		public void watermarkWithInsetsZeroIsSameAsWithoutInsetsSetWithSameScale() throws IOException {
			// given
			// when
			BufferedImage zeroInsetsResult = Thumbnails.of(ORIGINAL_IMAGE)
					.scale(1)
					.watermark(Positions.BOTTOM_RIGHT, WATERMARK_IMAGE, 1.0f, 0)
					.asBufferedImage();

			BufferedImage noInsetsSetResult = Thumbnails.of(ORIGINAL_IMAGE)
					.scale(1)
					.watermark(Positions.BOTTOM_RIGHT, WATERMARK_IMAGE, 1.0f)
					.asBufferedImage();

			// then
			assertTrue(BufferedImageComparer.isSame(noInsetsSetResult, zeroInsetsResult));
		}

		@Test
		public void watermarkWithInsetsZeroIsSameAsWithoutInsetsSetWithHalfScale() throws IOException {
			// given
			// when
			BufferedImage zeroInsetsResult = Thumbnails.of(ORIGINAL_IMAGE)
					.scale(0.5)
					.watermark(Positions.BOTTOM_RIGHT, WATERMARK_IMAGE, 1.0f, 0)
					.asBufferedImage();

			BufferedImage noInsetsSetResult = Thumbnails.of(ORIGINAL_IMAGE)
					.scale(0.5)
					.watermark(Positions.BOTTOM_RIGHT, WATERMARK_IMAGE, 1.0f)
					.asBufferedImage();

			// then
			assertTrue(BufferedImageComparer.isSame(noInsetsSetResult, zeroInsetsResult));
		}

		@Test
		public void watermarkWithInsetsZeroIsSameAsWithoutInsetsSetWithSizeResize() throws IOException {
			// given
			// when
			BufferedImage zeroInsetsResult = Thumbnails.of(ORIGINAL_IMAGE)
					.size(100, 100)
					.watermark(Positions.BOTTOM_RIGHT, WATERMARK_IMAGE, 1.0f, 0)
					.asBufferedImage();

			BufferedImage noInsetsSetResult = Thumbnails.of(ORIGINAL_IMAGE)
					.size(100, 100)
					.watermark(Positions.BOTTOM_RIGHT, WATERMARK_IMAGE, 1.0f)
					.asBufferedImage();

			// then
			assertTrue(BufferedImageComparer.isSame(noInsetsSetResult, zeroInsetsResult));
		}

		@Test
		public void watermarkWithInsetsSetUsingSizeToResize() throws IOException {
			// given
			// when
			BufferedImage insetsResult = Thumbnails.of(ORIGINAL_IMAGE)
					.size(100, 100)
					.watermark(Positions.BOTTOM_RIGHT, WATERMARK_IMAGE, 1.0f, 5)
					.asBufferedImage();

			BufferedImage expected = new BufferedImageBuilder(100, 100).build();
			Graphics g = expected.getGraphics();
			g.setColor(BACKGROUND_COLOR);
			g.fillRect(0, 0, 100, 100);
			g.setColor(WATERMARK_COLOR);
			g.fillRect(45, 45, 50, 50);
			g.dispose();

			// then
			assertTrue(BufferedImageComparer.isSame(expected, insetsResult));
		}

		@Test
		public void watermarkWithInsetsSetUsingScaleToResize() throws IOException {
			// given
			// when
			BufferedImage insetsResult = Thumbnails.of(ORIGINAL_IMAGE)
					.scale(0.5)
					.watermark(Positions.BOTTOM_RIGHT, WATERMARK_IMAGE, 1.0f, 5)
					.asBufferedImage();

			BufferedImage expected = new BufferedImageBuilder(100, 100).build();
			Graphics g = expected.getGraphics();
			g.setColor(BACKGROUND_COLOR);
			g.fillRect(0, 0, 100, 100);
			g.setColor(WATERMARK_COLOR);
			g.fillRect(45, 45, 50, 50);
			g.dispose();

			// then
			assertTrue(BufferedImageComparer.isSame(expected, insetsResult));
		}
	}

	public static class InputOutputFilesTests {

		@Rule
		public TemporaryFolder temporaryFolder = new TemporaryFolder();

		@Test
		public void renameGivenThumbnailParameter() throws IOException {
			// given
			Rename rename = mock(Rename.class);
			when(rename.apply(anyString(), any(ThumbnailParameter.class)))
					.thenReturn("thumbnail.grid.png");

			File f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);

			// when
			Thumbnails.of(f)
					.size(50, 50)
					.asFiles(rename);

			// then
			ArgumentCaptor<ThumbnailParameter> ac =
					ArgumentCaptor.forClass(ThumbnailParameter.class);

			verify(rename).apply(eq(f.getName()), ac.capture());
			assertEquals(new Dimension(50, 50), ac.getValue().getSize());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputFormat</li>
		 * <li>toFile(File)</li>
		 * <li>format name matches the file extension</li>
		 * <li>format is same as the original format</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The output format of the image is one that is specified.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputFormat_SameAsOriginal_SameAsFileExtension() throws IOException {
			File f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File outFile = new File(temporaryFolder.getRoot(), "grid.tmp.png");

			Thumbnails.of(f)
					.size(50, 50)
					.outputFormat("png")
					.toFile(outFile);

			BufferedImage fromFileImage = ImageIO.read(outFile);

			String formatName = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile)).next().getFormatName();
			assertEquals("png", formatName);

			assertEquals(50, fromFileImage.getWidth());
			assertEquals(50, fromFileImage.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputFormat</li>
		 * <li>toFile(File)</li>
		 * <li>format name matches the file extension</li>
		 * <li>format is different from the original format</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The output format of the image is one that is specified.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputFormat_DiffersFromOriginal_SameAsFileExtension() throws IOException {
			File f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File outFile = new File(temporaryFolder.getRoot(), "grid.tmp.jpg");

			Thumbnails.of(f)
					.size(50, 50)
					.outputFormat("jpg")
					.toFile(outFile);

			BufferedImage fromFileImage = ImageIO.read(outFile);

			String formatName = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile)).next().getFormatName();
			assertEquals("JPEG", formatName);

			assertEquals(50, fromFileImage.getWidth());
			assertEquals(50, fromFileImage.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputFormat</li>
		 * <li>toFile(File)</li>
		 * <li>format name matches the file extension</li>
		 * <li>format is different from the original format</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The output format of the image is one that is specified.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputFormat_DiffersFromOriginal_SameAsFileExtension_Jpeg() throws IOException {
			File f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File outFile = new File(temporaryFolder.getRoot(), "grid.tmp.jpg");

			Thumbnails.of(f)
					.size(50, 50)
					.outputFormat("jpeg")
					.toFile(outFile);

			BufferedImage fromFileImage = ImageIO.read(outFile);

			String formatName = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile)).next().getFormatName();
			assertEquals("JPEG", formatName);

			assertEquals(50, fromFileImage.getWidth());
			assertEquals(50, fromFileImage.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputFormat</li>
		 * <li>toFile(File)</li>
		 * <li>format name differs from the file extension</li>
		 * <li>format is same as the original format</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The output format of the image is one that is specified.</li>
		 * <li>The format extension is appended to the output filename.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputFormat_SameAsOriginal_DiffersFromFileExtension() throws IOException {
			File f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File outFile = new File(temporaryFolder.getRoot(), "grid.tmp.jpg");
			File expectedOutFile = new File(temporaryFolder.getRoot(), "grid.tmp.jpg.png");

			Thumbnails.of(f)
					.size(50, 50)
					.outputFormat("png")
					.toFile(outFile);

			BufferedImage fromFileImage = ImageIO.read(expectedOutFile);

			String formatName = ImageIO.getImageReaders(ImageIO.createImageInputStream(expectedOutFile)).next().getFormatName();
			assertEquals("png", formatName);

			assertEquals(50, fromFileImage.getWidth());
			assertEquals(50, fromFileImage.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputFormat</li>
		 * <li>toFile(File)</li>
		 * <li>format name differs from the file extension</li>
		 * <li>format differs from the original format</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The output format of the image is one that is specified.</li>
		 * <li>The format extension is appended to the output filename.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputFormat_DiffersFromOriginal_DiffersFromFileExtension() throws IOException {
			File f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File outFile = new File(temporaryFolder.getRoot(), "grid.tmp.png");
			File expectedOutFile = new File(temporaryFolder.getRoot(), "grid.tmp.png.jpg");

			Thumbnails.of(f)
					.size(50, 50)
					.outputFormat("jpg")
					.toFile(outFile);

			BufferedImage fromFileImage = ImageIO.read(expectedOutFile);

			String formatName = ImageIO.getImageReaders(ImageIO.createImageInputStream(expectedOutFile)).next().getFormatName();
			assertEquals("JPEG", formatName);

			assertEquals(50, fromFileImage.getWidth());
			assertEquals(50, fromFileImage.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>outputFormat</li>
		 * <li>toFile(File)</li>
		 * <li>format name differs from the file extension</li>
		 * <li>format differs from the original format</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The output format of the image is one that is specified.</li>
		 * <li>The format extension is appended to the output filename.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputFormat_DiffersFromOriginal_DiffersFromFileExtension_Jpeg() throws IOException {
			File f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File outFile = new File(temporaryFolder.getRoot(), "grid.tmp.png");
			File expectedOutFile = new File(temporaryFolder.getRoot(), "grid.tmp.png.jpeg");

			Thumbnails.of(f)
					.size(50, 50)
					.outputFormat("jpeg")
					.toFile(outFile);

			BufferedImage fromFileImage = ImageIO.read(expectedOutFile);

			String formatName = ImageIO.getImageReaders(ImageIO.createImageInputStream(expectedOutFile)).next().getFormatName();
			assertEquals("JPEG", formatName);

			assertEquals(50, fromFileImage.getWidth());
			assertEquals(50, fromFileImage.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>multiple files</li>
		 * <li>outputFormat</li>
		 * <li>toFile(File)</li>
		 * <li>format name is same as file extension</li>
		 * <li>format is same as original format</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The output format of the image is one that is specified.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputFormat_Multiple_SameAsOriginal_SameAsExtension_Both() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File f2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/igrid.png", temporaryFolder
			);

			// when
			Thumbnails.of(f1, f2)
					.size(50, 50)
					.outputFormat("png")
					.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File outFile1 = new File(temporaryFolder.getRoot(), "thumbnail.grid.png");
			File outFile2 = new File(temporaryFolder.getRoot(), "thumbnail.igrid.png");

			BufferedImage fromFileImage1 = ImageIO.read(outFile1);
			BufferedImage fromFileImage2 = ImageIO.read(outFile2);

			String formatName1 = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile1)).next().getFormatName();
			assertEquals("png", formatName1);
			String formatName2 = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile2)).next().getFormatName();
			assertEquals("png", formatName2);

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
			assertEquals(50, fromFileImage2.getWidth());
			assertEquals(50, fromFileImage2.getHeight());
		}


		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>multiple files</li>
		 * <li>outputFormat</li>
		 * <li>toFile(File)</li>
		 * <li>1st file: file extension same as format specified</li>
		 * <li>2nd file: file extension differs from format specified</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The output format of the image is one that is specified.</li>
		 * <li>For the file with the different format and extension, the extension
		 * of the specified format will be added to the file name.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputFormat_Multiple_FirstExtensionSame_SecondExtensionDifferent() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File f2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			);

			// when
			Thumbnails.of(f1, f2)
					.size(50, 50)
					.outputFormat("png")
					.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File outFile1 = new File(temporaryFolder.getRoot(), "thumbnail.grid.png");
			File outFile2 = new File(temporaryFolder.getRoot(), "thumbnail.grid.jpg.png");

			BufferedImage fromFileImage1 = ImageIO.read(outFile1);
			BufferedImage fromFileImage2 = ImageIO.read(outFile2);

			String formatName1 = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile1)).next().getFormatName();
			assertEquals("png", formatName1);
			String formatName2 = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile2)).next().getFormatName();
			assertEquals("png", formatName2);

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
			assertEquals(50, fromFileImage2.getWidth());
			assertEquals(50, fromFileImage2.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>multiple files</li>
		 * <li>outputFormat</li>
		 * <li>toFile(File)</li>
		 * <li>1st file: file extension differs from format specified</li>
		 * <li>2nd file: file extension same as format specified</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The output format of the image is one that is specified.</li>
		 * <li>For the file with the different format and extension, the extension
		 * of the specified format will be added to the file name.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputFormat_Multiple_FirstExtensionDifferent_SecondExtensionSame() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			);
			File f2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);

			// when
			Thumbnails.of(f1, f2)
					.size(50, 50)
					.outputFormat("png")
					.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File outFile1 = new File(temporaryFolder.getRoot(), "thumbnail.grid.jpg.png");
			File outFile2 = new File(temporaryFolder.getRoot(), "thumbnail.grid.png");

			BufferedImage fromFileImage1 = ImageIO.read(outFile1);
			BufferedImage fromFileImage2 = ImageIO.read(outFile2);

			String formatName1 = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile1)).next().getFormatName();
			assertEquals("png", formatName1);
			String formatName2 = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile2)).next().getFormatName();
			assertEquals("png", formatName2);

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
			assertEquals(50, fromFileImage2.getWidth());
			assertEquals(50, fromFileImage2.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>multiple files</li>
		 * <li>outputFormat</li>
		 * <li>toFile(File)</li>
		 * <li>1st file: file extension differs from format specified</li>
		 * <li>2nd file: file extension differs from format specified</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The output format of the image is one that is specified.</li>
		 * <li>For the file with the different format and extension, the extension
		 * of the specified format will be added to the file name.</li>
		 * </ol>
		 *
		 * @throws IOException
		 */
		@Test
		public void outputFormat_Multiple_FirstExtensionDifferent_SecondExtensionDifferent() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			);
			File f2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.bmp", temporaryFolder
			);

			// when
			Thumbnails.of(f1, f2)
					.size(50, 50)
					.outputFormat("png")
					.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File outFile1 = new File(temporaryFolder.getRoot(), "thumbnail.grid.jpg.png");
			File outFile2 = new File(temporaryFolder.getRoot(), "thumbnail.grid.bmp.png");

			BufferedImage fromFileImage1 = ImageIO.read(outFile1);
			BufferedImage fromFileImage2 = ImageIO.read(outFile2);

			String formatName1 = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile1)).next().getFormatName();
			assertEquals("png", formatName1);
			String formatName2 = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile2)).next().getFormatName();
			assertEquals("png", formatName2);

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
			assertEquals(50, fromFileImage2.getWidth());
			assertEquals(50, fromFileImage2.getHeight());
		}
	}

	@RunWith(Parameterized.class)
	public static class SourceRegionTests {
		// Error tolerance to deal with JPEG artifacts.
		private static final int MAX_ERROR = 3;
		private static final BufferedImage EXPECTED_GRID_IMAGE;
		private static final BufferedImage EXPECTED_F_IMAGE;
		private static final BufferedImage EXPECTED_GRID_TOP_LEFT_SHIFT_IMAGE;
		private static final BufferedImage EXPECTED_F_TOP_LEFT_SHIFT_IMAGE;
		private static final BufferedImage EXPECTED_GRID_BOTTOM_LEFT_SHIFT_IMAGE;
		private static final BufferedImage EXPECTED_F_BOTTOM_LEFT_SHIFT_IMAGE;
		private static final BufferedImage EXPECTED_GRID_BOTTOM_RIGHT_SHIFT_IMAGE;
		private static final BufferedImage EXPECTED_F_BOTTOM_RIGHT_SHIFT_IMAGE;

		static {
			InputStream is;
			try {
				is = TestUtils.getResourceStream("Thumbnailator/grid.png");
				EXPECTED_GRID_IMAGE = ImageIO.read(is).getSubimage(0, 0, 50, 50);
				is.close();

				is = TestUtils.getResourceStream("Exif/original.png");
				EXPECTED_F_IMAGE = ImageIO.read(is).getSubimage(0, 0, 80, 80);
				is.close();

				is = TestUtils.getResourceStream("Thumbnailator/grid.png");
				EXPECTED_GRID_TOP_LEFT_SHIFT_IMAGE = ImageIO.read(is).getSubimage(10, 20, 50, 30);
				is.close();

				is = TestUtils.getResourceStream("Exif/original.png");
				EXPECTED_F_TOP_LEFT_SHIFT_IMAGE = ImageIO.read(is).getSubimage(10, 20, 80, 90);
				is.close();

				is = TestUtils.getResourceStream("Thumbnailator/grid.png");
				EXPECTED_GRID_BOTTOM_LEFT_SHIFT_IMAGE = ImageIO.read(is).getSubimage(10, 50, 50, 30);
				is.close();

				is = TestUtils.getResourceStream("Exif/original.png");
				EXPECTED_F_BOTTOM_LEFT_SHIFT_IMAGE = ImageIO.read(is).getSubimage(10, 60, 80, 90);
				is.close();

				is = TestUtils.getResourceStream("Thumbnailator/grid.png");
				EXPECTED_GRID_BOTTOM_RIGHT_SHIFT_IMAGE = ImageIO.read(is).getSubimage(30, 50, 50, 30);
				is.close();

				is = TestUtils.getResourceStream("Exif/original.png");
				EXPECTED_F_BOTTOM_RIGHT_SHIFT_IMAGE = ImageIO.read(is).getSubimage(70, 60, 80, 90);
				is.close();

			} catch (Exception e) {
				throw new RuntimeException("Shouldn't happen.", e);
			}
		}

		@Parameterized.Parameters(name = "sourceImage={0}, x={2}, y={3}, width={4}, height={5}")
		public static Object[][] values() {
			return new Object[][] {
					new Object[] { "Thumbnailator/grid.png", EXPECTED_GRID_IMAGE, 0, 0, 50, 50 },
					new Object[] { "Exif/source_1.jpg", EXPECTED_F_IMAGE, 0, 0, 80, 80 },
					new Object[] { "Exif/source_2.jpg", EXPECTED_F_IMAGE, 0, 0, 80, 80 },
					new Object[] { "Exif/source_3.jpg", EXPECTED_F_IMAGE, 0, 0, 80, 80 },
					new Object[] { "Exif/source_4.jpg", EXPECTED_F_IMAGE, 0, 0, 80, 80 },
					new Object[] { "Exif/source_5.jpg", EXPECTED_F_IMAGE, 0, 0, 80, 80 },
					new Object[] { "Exif/source_6.jpg", EXPECTED_F_IMAGE, 0, 0, 80, 80 },
					new Object[] { "Exif/source_7.jpg", EXPECTED_F_IMAGE, 0, 0, 80, 80 },
					new Object[] { "Exif/source_8.jpg", EXPECTED_F_IMAGE, 0, 0, 80, 80 },
					new Object[] { "Thumbnailator/grid.png", EXPECTED_GRID_TOP_LEFT_SHIFT_IMAGE, 10, 20, 50, 30 },
					new Object[] { "Exif/source_1.jpg", EXPECTED_F_TOP_LEFT_SHIFT_IMAGE, 10, 20, 80, 90 },
					new Object[] { "Exif/source_2.jpg", EXPECTED_F_TOP_LEFT_SHIFT_IMAGE, 10, 20, 80, 90 },
					new Object[] { "Exif/source_3.jpg", EXPECTED_F_TOP_LEFT_SHIFT_IMAGE, 10, 20, 80, 90 },
					new Object[] { "Exif/source_4.jpg", EXPECTED_F_TOP_LEFT_SHIFT_IMAGE, 10, 20, 80, 90 },
					new Object[] { "Exif/source_5.jpg", EXPECTED_F_TOP_LEFT_SHIFT_IMAGE, 10, 20, 80, 90 },
					new Object[] { "Exif/source_6.jpg", EXPECTED_F_TOP_LEFT_SHIFT_IMAGE, 10, 20, 80, 90 },
					new Object[] { "Exif/source_7.jpg", EXPECTED_F_TOP_LEFT_SHIFT_IMAGE, 10, 20, 80, 90 },
					new Object[] { "Exif/source_8.jpg", EXPECTED_F_TOP_LEFT_SHIFT_IMAGE, 10, 20, 80, 90 },
					new Object[] { "Thumbnailator/grid.png", EXPECTED_GRID_BOTTOM_LEFT_SHIFT_IMAGE, 10, 50, 50, 30 },
					new Object[] { "Exif/source_1.jpg", EXPECTED_F_BOTTOM_LEFT_SHIFT_IMAGE, 10, 60, 80, 90 },
					new Object[] { "Exif/source_2.jpg", EXPECTED_F_BOTTOM_LEFT_SHIFT_IMAGE, 10, 60, 80, 90 },
					new Object[] { "Exif/source_3.jpg", EXPECTED_F_BOTTOM_LEFT_SHIFT_IMAGE, 10, 60, 80, 90 },
					new Object[] { "Exif/source_4.jpg", EXPECTED_F_BOTTOM_LEFT_SHIFT_IMAGE, 10, 60, 80, 90 },
					new Object[] { "Exif/source_5.jpg", EXPECTED_F_BOTTOM_LEFT_SHIFT_IMAGE, 10, 60, 80, 90 },
					new Object[] { "Exif/source_6.jpg", EXPECTED_F_BOTTOM_LEFT_SHIFT_IMAGE, 10, 60, 80, 90 },
					new Object[] { "Exif/source_7.jpg", EXPECTED_F_BOTTOM_LEFT_SHIFT_IMAGE, 10, 60, 80, 90 },
					new Object[] { "Exif/source_8.jpg", EXPECTED_F_BOTTOM_LEFT_SHIFT_IMAGE, 10, 60, 80, 90 },
					new Object[] { "Thumbnailator/grid.png", EXPECTED_GRID_BOTTOM_RIGHT_SHIFT_IMAGE, 30, 50, 50, 30 },
					new Object[] { "Exif/source_1.jpg", EXPECTED_F_BOTTOM_RIGHT_SHIFT_IMAGE, 70, 60, 80, 90 },
					new Object[] { "Exif/source_2.jpg", EXPECTED_F_BOTTOM_RIGHT_SHIFT_IMAGE, 70, 60, 80, 90 },
					new Object[] { "Exif/source_3.jpg", EXPECTED_F_BOTTOM_RIGHT_SHIFT_IMAGE, 70, 60, 80, 90 },
					new Object[] { "Exif/source_4.jpg", EXPECTED_F_BOTTOM_RIGHT_SHIFT_IMAGE, 70, 60, 80, 90 },
					new Object[] { "Exif/source_5.jpg", EXPECTED_F_BOTTOM_RIGHT_SHIFT_IMAGE, 70, 60, 80, 90 },
					new Object[] { "Exif/source_6.jpg", EXPECTED_F_BOTTOM_RIGHT_SHIFT_IMAGE, 70, 60, 80, 90 },
					new Object[] { "Exif/source_7.jpg", EXPECTED_F_BOTTOM_RIGHT_SHIFT_IMAGE, 70, 60, 80, 90 },
					new Object[] { "Exif/source_8.jpg", EXPECTED_F_BOTTOM_RIGHT_SHIFT_IMAGE, 70, 60, 80, 90 },
			};
		}

		@Parameterized.Parameter
		public String resourcePath;

		@Parameterized.Parameter(1)
		public BufferedImage expectedImage;

		@Parameterized.Parameter(2)
		public int x;

		@Parameterized.Parameter(3)
		public int y;

		@Parameterized.Parameter(4)
		public int width;

		@Parameterized.Parameter(5)
		public int height;

		@Rule
		public TemporaryFolder temporaryFolder = new TemporaryFolder();

		@Test
		public void sourceRegion_Region() throws IOException {
			// given
			File sourceFile = TestUtils.copyResourceToTemporaryFile(resourcePath, temporaryFolder);

			// when
			BufferedImage thumbnail = Thumbnails.of(sourceFile)
					.sourceRegion(new Region(new Coordinate(x, y), new AbsoluteSize(width, height)))
					.size(width, height)
					.asBufferedImage();

			// then
			assertEquals(width, thumbnail.getWidth());
			assertEquals(height, thumbnail.getHeight());
			assertTrue(BufferedImageComparer.isRGBSimilar(thumbnail, expectedImage, MAX_ERROR));
		}

		@Test
		public void sourceRegion_Rectangle() throws IOException {
			// given
			File sourceFile = TestUtils.copyResourceToTemporaryFile(resourcePath, temporaryFolder);

			// when
			BufferedImage thumbnail = Thumbnails.of(sourceFile)
					.sourceRegion(new Rectangle(x, y, width, height))
					.size(width, height)
					.asBufferedImage();

			// then
			assertEquals(width, thumbnail.getWidth());
			assertEquals(height, thumbnail.getHeight());
			assertTrue(BufferedImageComparer.isRGBSimilar(thumbnail, expectedImage, MAX_ERROR));
		}

		@Test
		public void sourceRegion_PositionSize() throws IOException {
			// given
			File sourceFile = TestUtils.copyResourceToTemporaryFile(resourcePath, temporaryFolder);

			// when
			BufferedImage thumbnail = Thumbnails.of(sourceFile)
					.sourceRegion(new Coordinate(x, y), new AbsoluteSize(width, height))
					.size(width, height)
					.asBufferedImage();

			// then
			assertEquals(width, thumbnail.getWidth());
			assertEquals(height, thumbnail.getHeight());
			assertTrue(BufferedImageComparer.isRGBSimilar(thumbnail, expectedImage, MAX_ERROR));
		}

		@Test
		public void sourceRegion_PositionIntInt() throws IOException {
			// given
			File sourceFile = TestUtils.copyResourceToTemporaryFile(resourcePath, temporaryFolder);

			// when
			BufferedImage thumbnail = Thumbnails.of(sourceFile)
					.sourceRegion(new Coordinate(x, y), width, height)
					.size(width, height)
					.asBufferedImage();

			// then
			assertEquals(width, thumbnail.getWidth());
			assertEquals(height, thumbnail.getHeight());
			assertTrue(BufferedImageComparer.isRGBSimilar(thumbnail, expectedImage, MAX_ERROR));
		}

		@Test
		public void sourceRegion_IntIntIntInt() throws IOException {
			// given
			File sourceFile = TestUtils.copyResourceToTemporaryFile(resourcePath, temporaryFolder);

			// when
			BufferedImage thumbnail = Thumbnails.of(sourceFile)
					.sourceRegion(x, y, width, height)
					.size(width, height)
					.asBufferedImage();

			// then
			assertEquals(width, thumbnail.getWidth());
			assertEquals(height, thumbnail.getHeight());
			assertTrue(BufferedImageComparer.isRGBSimilar(thumbnail, expectedImage, 5));
		}
	}

	@RunWith(Parameterized.class)
	public static class WatermarkPositioningTests {
		@Parameterized.Parameters(name = "position={0}, x={1}, y={2}")
		public static Object[][] values() {
			return new Object[][] {
					new Object[] { Positions.TOP_LEFT, 10, 10, 20, 20, 9, 9 },
					new Object[] { Positions.TOP_CENTER, 50, 10, 50, 20, 50, 9 },
					new Object[] { Positions.TOP_RIGHT, 89, 10, 80, 20, 89, 9 },

					new Object[] { Positions.CENTER_LEFT, 10, 50, 20, 50, 9, 50 },
					new Object[] { Positions.CENTER, 50, 50, 60, 50, 60, 60 },
					new Object[] { Positions.CENTER_RIGHT, 89, 50, 79, 50, 90, 50 },

					new Object[] { Positions.BOTTOM_LEFT, 10, 89, 20, 89, 9, 91 },
					new Object[] { Positions.BOTTOM_CENTER, 50, 89, 50, 79, 50, 91 },
					new Object[] { Positions.BOTTOM_RIGHT, 89, 89, 79, 89, 90, 91 },
			};
		}

		@Parameterized.Parameter
		public Position position;

		@Parameterized.Parameter(1)
		public int expectedXForWatermark;

		@Parameterized.Parameter(2)
		public int expectedYForWatermark;

		@Parameterized.Parameter(3)
		public int expectedXForBackground;

		@Parameterized.Parameter(4)
		public int expectedYForBackground;

		@Parameterized.Parameter(5)
		public int expectedXForBackground2;

		@Parameterized.Parameter(6)
		public int expectedYForBackground2;

		private static final BufferedImage ORIGINAL_IMAGE;
		private static final BufferedImage WATERMARK_IMAGE;

		static {
			Graphics g;
			WATERMARK_IMAGE = new BufferedImageBuilder(20, 20).build();
			g = WATERMARK_IMAGE.getGraphics();
			g.setColor(Color.BLUE);
			g.fillRect(0, 0, 20, 20);
			g.dispose();

			ORIGINAL_IMAGE = new BufferedImageBuilder(100, 100).build();
			g = ORIGINAL_IMAGE.getGraphics();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, 100, 100);
			g.dispose();
		}

		@Test
		public void watermark() throws IOException {
			Watermark watermark = new Watermark(position, WATERMARK_IMAGE, 1f);

			BufferedImage image = Thumbnails.of(ORIGINAL_IMAGE)
					.scale(1)
					.watermark(watermark)
					.asBufferedImage();

			assertEquals(Color.BLUE.getRGB(), image.getRGB(expectedXForWatermark, expectedYForWatermark));
			assertEquals(Color.WHITE.getRGB(), image.getRGB(expectedXForBackground, expectedYForBackground));
		}

		@Test
		public void watermarkPosition() throws IOException {
			BufferedImage image = Thumbnails.of(ORIGINAL_IMAGE)
					.scale(1)
					.watermark(position, WATERMARK_IMAGE, 1f)
					.asBufferedImage();

			assertEquals(Color.BLUE.getRGB(), image.getRGB(expectedXForWatermark, expectedYForWatermark));
			assertEquals(Color.WHITE.getRGB(), image.getRGB(expectedXForBackground, expectedYForBackground));
		}

		@Test
		public void watermarkPositionInset() throws IOException {
			BufferedImage image = Thumbnails.of(ORIGINAL_IMAGE)
					.scale(1)
					.watermark(position, WATERMARK_IMAGE, 1f, 10)
					.asBufferedImage();

			assertEquals(Color.BLUE.getRGB(), image.getRGB(expectedXForWatermark, expectedYForWatermark));
			assertEquals(Color.WHITE.getRGB(), image.getRGB(expectedXForBackground2, expectedYForBackground2));
		}
	}

	public static class WatermarkOrdinaryTests {
		private static final BufferedImage ORIGINAL_IMAGE = WatermarkPositioningTests.ORIGINAL_IMAGE;
		private static final BufferedImage WATERMARK_IMAGE = WatermarkPositioningTests.WATERMARK_IMAGE;

		@Test
		public void watermarkRejectsNull() {
			try {
				Thumbnails.of(ORIGINAL_IMAGE)
						.watermark((Watermark) null);
				fail();
			} catch (NullPointerException e) {
				assertEquals("Watermark is null.", e.getMessage());
			}
		}

		@Test
		public void watermarkNormalPositioning() throws IOException {
			BufferedImage thumbnail = Thumbnails.of(ORIGINAL_IMAGE)
					.watermark(WATERMARK_IMAGE)
					.scale(1)
					.asBufferedImage();

			Color expectedColor = new Color(127, 127, 255);
			assertEquals(expectedColor.getRGB(), thumbnail.getRGB(50, 50));
			assertEquals(Color.WHITE.getRGB(), thumbnail.getRGB(39, 50));
		}

		@Test
		public void watermarkNormalPositioningWithScaling() throws IOException {
			BufferedImage thumbnail = Thumbnails.of(ORIGINAL_IMAGE)
					.watermark(WATERMARK_IMAGE)
					.scale(0.5)
					.asBufferedImage();

			Color expectedColor = new Color(127, 127, 255);
			assertEquals(expectedColor.getRGB(), thumbnail.getRGB(25, 25));
			assertEquals(Color.WHITE.getRGB(), thumbnail.getRGB(14, 25));
		}

		@Test
		public void watermarkOpacityPositioning() throws IOException {
			BufferedImage thumbnail = Thumbnails.of(ORIGINAL_IMAGE)
					.watermark(WATERMARK_IMAGE, 1.0f)
					.scale(1)
					.asBufferedImage();

			assertEquals(Color.BLUE.getRGB(), thumbnail.getRGB(50, 50));
			assertEquals(Color.WHITE.getRGB(), thumbnail.getRGB(39, 50));
		}

		@Test
		public void watermarkOpacityPositioningWithScaling() throws IOException {
			BufferedImage thumbnail = Thumbnails.of(ORIGINAL_IMAGE)
					.watermark(WATERMARK_IMAGE, 1.0f)
					.scale(0.5)
					.asBufferedImage();

			assertEquals(Color.BLUE.getRGB(), thumbnail.getRGB(25, 25));
			assertEquals(Color.WHITE.getRGB(), thumbnail.getRGB(14, 25));
		}
	}

	@RunWith(Parameterized.class)
	public static class WatermarkWithOrientationInputStreamSourceTests {

		@Parameterized.Parameters(name = "orientation={0}")
		public static Object[] values() {
			return new Integer[]{1, 2, 3, 4, 5, 6, 7, 8};
		}

		@Parameterized.Parameter
		public int orientation;

		@Test
		public void watermarkExifOrientation() throws IOException {
			// given
			String resourceName = String.format("Exif/source_%s.jpg", orientation);
			InputStream is = TestUtils.getResourceStream(resourceName);

			BufferedImage watermark = new BufferedImageBuilder(25, 25).build();
			Graphics g = watermark.getGraphics();
			g.setColor(Color.blue);
			g.fillRect(0, 0, watermark.getWidth(), watermark.getHeight());
			g.dispose();

			// when
			BufferedImage thumbnail = Thumbnails.of(is)
					.size(100, 100)
					.watermark(Positions.BOTTOM_RIGHT, watermark, 1.0f)
					.asBufferedImage();
			is.close();

			// then
			assertEquals(Color.blue.getRGB(), thumbnail.getRGB(99, 99));
		}

		@Test
		public void watermarkAndCropExifOrientation() throws IOException {
			// given
			String resourceName = String.format("Exif/source_%s.jpg", orientation);
			InputStream is = TestUtils.getResourceStream(resourceName);

			BufferedImage watermark = new BufferedImageBuilder(10, 10).build();
			Graphics g = watermark.getGraphics();
			g.setColor(Color.blue);
			g.fillRect(0, 0, watermark.getWidth(), watermark.getHeight());
			g.dispose();

			// when
			BufferedImage thumbnail = Thumbnails.of(is)
					.size(50, 100)
					.crop(Positions.CENTER)
					.watermark(Positions.BOTTOM_RIGHT, watermark, 1.0f)
					.asBufferedImage();
			is.close();

			// then
			assertEquals(Color.blue.getRGB(), thumbnail.getRGB(49, 99));
		}

		@Test
		public void watermarkCenterAndCropNonCenterExifOrientation() throws IOException {
			// given
			String resourceName = String.format("Exif/source_%s.jpg", orientation);
			InputStream is = TestUtils.getResourceStream(resourceName);

			BufferedImage watermark = new BufferedImageBuilder(10, 10).build();
			Graphics g = watermark.getGraphics();
			g.setColor(Color.blue);
			g.fillRect(0, 0, watermark.getWidth(), watermark.getHeight());
			g.dispose();

			// when
			BufferedImage thumbnail = Thumbnails.of(is)
					.size(50, 100)
					.crop(Positions.TOP_LEFT)
					.watermark(Positions.CENTER, watermark, 1.0f)
					.asBufferedImage();
			is.close();

			// then
			assertEquals(Color.blue.getRGB(), thumbnail.getRGB(25, 50));
		}
	}

	@RunWith(Parameterized.class)
	public static class WatermarkWithOrientationFileSourceTests {

		@Parameterized.Parameters(name = "orientation={0}")
		public static Object[] values() {
			return new Integer[]{1, 2, 3, 4, 5, 6, 7, 8};
		}

		@Parameterized.Parameter
		public int orientation;

		@Rule
		public TemporaryFolder temporaryFolder = new TemporaryFolder();

		@Test
		public void watermarkExifOrientation() throws IOException {
			// given
			String resourceName = String.format("Exif/source_%s.jpg", orientation);
			File sourceFile = TestUtils.copyResourceToTemporaryFile(resourceName, temporaryFolder);

			BufferedImage watermark = new BufferedImageBuilder(25, 25).build();
			Graphics g = watermark.getGraphics();
			g.setColor(Color.blue);
			g.fillRect(0, 0, watermark.getWidth(), watermark.getHeight());
			g.dispose();

			// when
			BufferedImage thumbnail = Thumbnails.of(sourceFile)
					.size(100, 100)
					.watermark(Positions.BOTTOM_RIGHT, watermark, 1.0f)
					.asBufferedImage();

			// then
			assertEquals(Color.blue.getRGB(), thumbnail.getRGB(99, 99));
		}

		@Test
		public void watermarkAndCropExifOrientation() throws IOException {
			// given
			String resourceName = String.format("Exif/source_%s.jpg", orientation);
			File sourceFile = TestUtils.copyResourceToTemporaryFile(resourceName, temporaryFolder);

			BufferedImage watermark = new BufferedImageBuilder(10, 10).build();
			Graphics g = watermark.getGraphics();
			g.setColor(Color.blue);
			g.fillRect(0, 0, watermark.getWidth(), watermark.getHeight());
			g.dispose();

			// when
			BufferedImage thumbnail = Thumbnails.of(sourceFile)
					.size(50, 100)
					.crop(Positions.CENTER)
					.watermark(Positions.BOTTOM_RIGHT, watermark, 1.0f)
					.asBufferedImage();

			// then
			assertEquals(Color.blue.getRGB(), thumbnail.getRGB(49, 99));
		}

		@Test
		public void watermarkCenterAndCropNonCenterExifOrientation() throws IOException {
			// given
			String resourceName = String.format("Exif/source_%s.jpg", orientation);
			File sourceFile = TestUtils.copyResourceToTemporaryFile(resourceName, temporaryFolder);

			BufferedImage watermark = new BufferedImageBuilder(10, 10).build();
			Graphics g = watermark.getGraphics();
			g.setColor(Color.blue);
			g.fillRect(0, 0, watermark.getWidth(), watermark.getHeight());
			g.dispose();

			// when
			BufferedImage thumbnail = Thumbnails.of(sourceFile)
					.size(50, 100)
					.crop(Positions.TOP_LEFT)
					.watermark(Positions.CENTER, watermark, 1.0f)
					.asBufferedImage();

			// then
			assertEquals(Color.blue.getRGB(), thumbnail.getRGB(25, 50));
		}
	}
}
