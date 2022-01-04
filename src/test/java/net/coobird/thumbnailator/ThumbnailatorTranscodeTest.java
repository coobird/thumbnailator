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

package net.coobird.thumbnailator;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.coobird.thumbnailator.TestUtils.copyResourceToTemporaryFile;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Enclosed.class)
public class ThumbnailatorTranscodeTest {

	@Ignore
	public static class InputOutputExpectationBase {
		@Parameterized.Parameters(name = "input={0}, output={1}, expected={2}")
		public static Collection<Object> testCases() {
			List<Object[]> cases = new ArrayList<Object[]>();

			Map<String, String> expectedFormatNames = new HashMap<String, String>() {{
				put("jpg", "JPEG");
				put("png", "png");
				put("bmp", "bmp");
				put("gif", "gif");
			}};

			List<String> supportedFormats = Arrays.asList("jpg", "png", "bmp", "gif");

			for (String input : supportedFormats) {
				for (String output : supportedFormats) {
					if (input.equals(output)) {
						continue;
					}
					cases.add(new Object[] { input, output, expectedFormatNames.get(output) });
				}
			}

			return Arrays.asList(cases.toArray());
		}

		@Parameterized.Parameter
		public String inputFormat;

		@Parameterized.Parameter(value = 1)
		public String outputFormat;

		@Parameterized.Parameter(value = 2)
		public String expectedFormatName;

		protected boolean isTestForGifOutputInJava5() {
			return "gif".equals(outputFormat) && System.getProperty("java.version").startsWith("1.5");
		}
	}

	@RunWith(Parameterized.class)
	public static class InputStreamToOutputStreamTest extends InputOutputExpectationBase {

		@Test
		public void createThumbnailForInputStreamToOutputStream() throws IOException {
			// Skip in Java 5, as GIF writer was first included in Java 6.
			if (isTestForGifOutputInJava5()) {
				return;
			}

			InputStream is = TestUtils.getResourceStream(String.format("Thumbnailator/grid.%s", inputFormat));
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			Thumbnailator.createThumbnail(is, os, outputFormat, 50, 50);

			InputStream thumbIs = new ByteArrayInputStream(os.toByteArray());
			BufferedImage img = ImageIO.read(thumbIs);

			assertEquals(
					expectedFormatName,
					ImageIO.getImageReaders(
							ImageIO.createImageInputStream(
									new ByteArrayInputStream(os.toByteArray()))
					).next().getFormatName()
			);
			assertEquals(50, img.getWidth());
			assertEquals(50, img.getHeight());
		}
	}

	@RunWith(Parameterized.class)
	public static class FileToFileTest extends InputOutputExpectationBase {

		@Rule
		public TemporaryFolder temporaryFolder = new TemporaryFolder();

		@Test
		public void createThumbnailForFileToFile() throws IOException {
			// Skip in Java 5, as GIF writer was first included in Java 6.
			if (isTestForGifOutputInJava5()) {
				return;
			}

			File inputFile = copyResourceToTemporaryFile(String.format("Thumbnailator/grid.%s", inputFormat), temporaryFolder);
			File outputFile = temporaryFolder.newFile(String.format("test.%s", outputFormat));

			Thumbnailator.createThumbnail(inputFile, outputFile, 50, 50);

			assertTrue(outputFile.exists());
			BufferedImage img = ImageIO.read(outputFile);
			assertEquals(
					expectedFormatName,
					ImageIO.getImageReaders(
							ImageIO.createImageInputStream(outputFile)
					).next().getFormatName()
			);
			assertEquals(50, img.getWidth());
			assertEquals(50, img.getHeight());
		}
	}
}
