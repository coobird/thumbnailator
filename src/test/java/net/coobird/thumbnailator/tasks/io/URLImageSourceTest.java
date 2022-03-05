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

package net.coobird.thumbnailator.tasks.io;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;


import net.coobird.thumbnailator.TestUtils;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.builders.ThumbnailParameterBuilder;
import net.coobird.thumbnailator.geometry.AbsoluteSize;
import net.coobird.thumbnailator.geometry.Coordinate;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.geometry.Region;
import net.coobird.thumbnailator.test.BufferedImageAssert;
import net.coobird.thumbnailator.test.BufferedImageComparer;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Enclosed.class)
public class URLImageSourceTest {

	public static class Tests {
		@Test
		public void proxySpecfied() throws IOException {
			// given
			Proxy proxy = Proxy.NO_PROXY;
			URLImageSource source = new URLImageSource(
					TestUtils.getResource("Thumbnailator/grid.png")
					, proxy);

			// when
			BufferedImage img = source.read();

			// then
			assertEquals(100, img.getWidth());
			assertEquals(100, img.getHeight());
			assertEquals("png", source.getInputFormatName());
		}

		@Test(expected = NullPointerException.class)
		public void givenNullURL() {
			try {
				// given
				// when
				URL url = null;
				new URLImageSource(url);
			} catch (NullPointerException e) {
				// then
				assertEquals("URL cannot be null.", e.getMessage());
				throw e;
			}
		}

		@Test(expected = NullPointerException.class)
		public void givenNullString() throws IOException {
			try {
				// given
				// when
				String url = null;
				new URLImageSource(url);
			} catch (NullPointerException e) {
				// then
				assertEquals("URL cannot be null.", e.getMessage());
				throw e;
			}
		}

		@Test(expected = NullPointerException.class)
		public void givenURL_givenNullProxy() throws IOException {
			try {
				// given
				// when
				new URLImageSource(
						TestUtils.getResource("Thumbnailator/grid.png"),
						null);
			} catch (NullPointerException e) {
				// then
				assertEquals("Proxy cannot be null.", e.getMessage());
				throw e;
			}
		}

		@Test(expected = NullPointerException.class)
		public void givenString_givenNullProxy() throws IOException {
			try {
				// given
				// when
				new URLImageSource("file:actualUrlDoesntMatter", null);
			} catch (NullPointerException e) {
				// then
				assertEquals("Proxy cannot be null.", e.getMessage());
				throw e;
			}
		}

		@Test
		public void fileExists_Png() throws IOException {
			// given
			URLImageSource source = new URLImageSource(
					TestUtils.getResource("Thumbnailator/grid.png")
			);

			// when
			BufferedImage img = source.read();

			// then
			assertEquals(100, img.getWidth());
			assertEquals(100, img.getHeight());
			assertEquals("png", source.getInputFormatName());
		}

		@Test
		public void fileExists_Jpeg() throws IOException {
			// given
			URLImageSource source = new URLImageSource(
					TestUtils.getResource("Thumbnailator/grid.jpg")
			);

			// when
			BufferedImage img = source.read();

			// then
			assertEquals(100, img.getWidth());
			assertEquals(100, img.getHeight());
			assertEquals("JPEG", source.getInputFormatName());
		}

		@Test
		public void fileExists_Bmp() throws IOException {
			// given
			URLImageSource source = new URLImageSource(
					TestUtils.getResource("Thumbnailator/grid.bmp")
			);

			// when
			BufferedImage img = source.read();

			// then
			assertEquals(100, img.getWidth());
			assertEquals(100, img.getHeight());
			assertEquals("bmp", source.getInputFormatName());
		}

		@Test(expected = IOException.class)
		public void fileDoesNotExists() throws IOException {
			// given
			URLImageSource source = new URLImageSource(new URL("file:notfound"));

			try {
				// when
				source.read();
			} catch (IOException e) {
				// then
				assertThat(e.getMessage(), containsString("Could not open connection to URL:"));
				throw e;
			}
			fail();
		}

		@Test(expected = IllegalStateException.class)
		public void fileExists_getInputFormatNameBeforeRead() throws IOException {
			// given
			URLImageSource source = new URLImageSource(
					TestUtils.getResource("Thumbnailator/grid.png")
			);

			try {
				// when
				source.getInputFormatName();
			} catch (IllegalStateException e) {
				// then
				assertEquals("Input has not been read yet.", e.getMessage());
				throw e;
			}
		}


		/*
		 *
		 *     +------+-----------+
		 *     |XXXXXX|           |
		 *     |XXXXXX|           |
		 *     +------+           |
		 *     |      region      |
		 *     |                  |
		 *     |                  |
		 *     |                  |
		 *     |                  |
		 *     +------------------+
		 *                        source
		 */
		@Test
		public void appliesSourceRegion() throws IOException {
			// given
			BufferedImage sourceImage = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			URLImageSource source = new URLImageSource(
					TestUtils.getResource("Thumbnailator/grid.png")
			);
			source.setThumbnailParameter(
					new ThumbnailParameterBuilder()
							.region(new Region(Positions.TOP_LEFT, new AbsoluteSize(40, 40)))
							.size(20, 20)
							.build()
			);

			// when
			BufferedImage img = source.read();

			// then
			BufferedImage expectedImg = sourceImage.getSubimage(0, 0, 40, 40);
			assertTrue(BufferedImageComparer.isRGBSimilar(expectedImg, img));
		}

		/*
		 *
		 *     +------------------+ source
		 *     |  +------------------+
		 *     |  |XXXXXXXXXXXXXXX|  |
		 *     |  |XXXXXXXXXXXXXXX|  |
		 *     |  |XX  final  XXXX|  |
		 *     |  |XX  region XXXX|  |
		 *     |  |XXXXXXXXXXXXXXX|  |
		 *     |  |XXXXXXXXXXXXXXX|  |
		 *     |  |XXXXXXXXXXXXXXX|  |
		 *     +--|---------------+  |
		 *        +------------------+
		 *                             region
		 */
		@Test
		public void appliesSourceRegionTooBig() throws IOException {
			// given
			BufferedImage sourceImage = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			URLImageSource source = new URLImageSource(
					TestUtils.getResource("Thumbnailator/grid.png")
			);
			source.setThumbnailParameter(
					new ThumbnailParameterBuilder()
							.region(new Region(new Coordinate(20, 20), new AbsoluteSize(100, 100)))
							.size(80, 80)
							.build()
			);

			// when
			BufferedImage img = source.read();

			// then
			BufferedImage expectedImg = sourceImage.getSubimage(20, 20, 80, 80);
			assertTrue(BufferedImageComparer.isRGBSimilar(expectedImg, img));
		}

		/*
		 *   +-----------------+
		 *   |                 |
		 *   | +---------------|--+
		 *   | |XXXXXXXXXXXXXXX|  |
		 *   | |XXXXXXXXXXXXXXX|  |
		 *   | |XXXX final XXXX|  |
		 *   | |XXXX regionXXXX|  |
		 *   | |XXXXXXXXXXXXXXX|  |
		 *   | |XXXXXXXXXXXXXXX|  |
		 *   +-----------------+  |
		 *     |                region
		 *     +------------------+
		 *                        source
		 */
		@Test
		public void appliesSourceRegionBeyondOrigin() throws IOException {
			// given
			BufferedImage sourceImage = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			URLImageSource source = new URLImageSource(
					TestUtils.getResource("Thumbnailator/grid.png")
			);
			source.setThumbnailParameter(
					new ThumbnailParameterBuilder()
							.region(new Region(new Coordinate(-20, -20), new AbsoluteSize(100, 100)))
							.size(80, 80)
							.build()
			);

			// when
			BufferedImage img = source.read();

			// then
			BufferedImage expectedImg = sourceImage.getSubimage(0, 0, 80, 80);
			assertTrue(BufferedImageComparer.isRGBSimilar(expectedImg, img));
		}

		@Test
		public void appliesSourceRegionNotSpecified() throws IOException {
			// given
			BufferedImage sourceImage = TestUtils.getImageFromResource("Thumbnailator/grid.png");

			URLImageSource source = new URLImageSource(
					TestUtils.getResource("Thumbnailator/grid.png")
			);
			source.setThumbnailParameter(
					new ThumbnailParameterBuilder()
							.size(20, 20)
							.build()
			);

			// when
			BufferedImage img = source.read();

			// then
			assertTrue(BufferedImageComparer.isRGBSimilar(sourceImage, img));
		}

		@Test
		public void useExifOrientationIsTrue_OrientationHonored() throws Exception {
			// given
			BufferedImage sourceImage = TestUtils.getImageFromResource("Exif/source_2.jpg");

			URLImageSource source = new URLImageSource(
					TestUtils.getResource("Exif/source_2.jpg")
			);
			ThumbnailParameter param =
					new ThumbnailParameterBuilder()
							.size(20, 20)
							.useExifOrientation(true)
							.build();

			source.setThumbnailParameter(param);

			// when
			source.read();

			// then
			BufferedImage result = param.getImageFilters().get(0).apply(sourceImage);
			BufferedImageAssert.assertMatches(
					result,
					new float[]{
							1, 1, 1,
							1, 1, 1,
							1, 0, 0,
					}
			);
		}

		@Test
		public void useExifOrientationIsFalse_OrientationIgnored() throws Exception {
			// given
			URLImageSource source = new URLImageSource(
					TestUtils.getResource("Exif/source_2.jpg")
			);

			ThumbnailParameter param =
					new ThumbnailParameterBuilder()
							.size(20, 20)
							.useExifOrientation(false)
							.build();

			source.setThumbnailParameter(param);

			// when
			BufferedImage result = source.read();

			// then
			assertTrue(param.getImageFilters().isEmpty());
			BufferedImageAssert.assertMatches(
					result,
					new float[]{
							1, 1, 1,
							1, 1, 1,
							0, 0, 1,
					}
			);
		}
	}

	@RunWith(Parameterized.class)
	public static class ExifOrientationTests {

		@Parameterized.Parameters(name = "orientation={0}")
		public static Object[] values() {
			return new Object[] { 1, 2, 3, 4, 5, 6, 7, 8};
		}

		@Parameterized.Parameter
		public int orientation;

		@Test
		public void readImageUnaffectedForOrientation() throws Exception {
			// given
			String resourceName = String.format("Exif/source_%s.jpg", orientation);
			BufferedImage sourceImage = TestUtils.getImageFromResource(resourceName);

			URLImageSource source = new URLImageSource(TestUtils.getResource(resourceName));

			ThumbnailParameter param =
					new ThumbnailParameterBuilder().size(20, 20).build();
			source.setThumbnailParameter(param);

			// when
			BufferedImage img = source.read();

			// then
			assertTrue(BufferedImageComparer.isRGBSimilar(sourceImage, img));
		}

		@Test
		public void containsCorrectFilterForOrientation() throws Exception {
			// given
			String resourceName = String.format("Exif/source_%s.jpg", orientation);
			BufferedImage sourceImage = TestUtils.getImageFromResource(resourceName);

			URLImageSource source = new URLImageSource(TestUtils.getResource(resourceName));

			ThumbnailParameter param =
					new ThumbnailParameterBuilder().size(20, 20).build();
			source.setThumbnailParameter(param);

			// when
			source.read();

			// then
			if (orientation == 1) {
				assertTrue(param.getImageFilters().isEmpty());

			} else {
				BufferedImage result = param.getImageFilters().get(0).apply(sourceImage);
				BufferedImageAssert.assertMatches(
						result,
						new float[]{
								1, 1, 1,
								1, 1, 1,
								1, 0, 0,
						}
				);
			}
		}
	}
}
