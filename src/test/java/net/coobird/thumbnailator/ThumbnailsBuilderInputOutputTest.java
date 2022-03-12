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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.builders.ThumbnailParameterBuilder;
import net.coobird.thumbnailator.name.ConsecutivelyNumberedFilenames;
import net.coobird.thumbnailator.name.Rename;
import net.coobird.thumbnailator.test.BufferedImageAssert;
import net.coobird.thumbnailator.test.BufferedImageComparer;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Enclosed.class)
public class ThumbnailsBuilderInputOutputTest {

	/**
	 * The temporary directory to use when creating files to use for this test.
	 */
	private static final String TMPDIR =
			"src/test/resources/tmp/ThumbnailsBuilderInputOutputTest";
	
	@BeforeClass
	public static void makeTemporaryDirectory() {
		TestUtils.makeTemporaryDirectory(TMPDIR);
	}
	
	@AfterClass
	public static void deleteTemporaryDirectory() {
		TestUtils.deleteTemporaryDirectory(TMPDIR);
	}

	public static class Tests {

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage)</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully. Image format is determined
		 * by the extension of the file.</li>
		 * </ol>
		 */
		@Test
		public void of_BufferedImage_toFile_File_NoOutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			File destFile = new File("src/test/resources/Thumbnailator/tmp.png");
			destFile.deleteOnExit();

			// when
			Thumbnails.of(img)
				.size(100, 100)
				.toFile(destFile);

			// then
			assertEquals("png", TestUtils.getFormatName(new FileInputStream(destFile)));

			BufferedImage thumbnail = ImageIO.read(destFile);
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage)</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully. Image format is determined
		 * by the extension of the file.</li>
		 * </ol>
		 */
		@Test
		public void of_BufferedImage_toFile_String_NoOutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			String destFilePath = "src/test/resources/Thumbnailator/tmp.png";

			// when
			Thumbnails.of(img)
				.size(100, 100)
				.toFile(destFilePath);

			// then
			File destFile = new File(destFilePath);
			destFile.deleteOnExit();

			assertEquals("png", TestUtils.getFormatName(new FileInputStream(destFile)));

			BufferedImage thumbnail = ImageIO.read(destFile);
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage)</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A BufferedImage is returned</li>
		 * </ol>
		 */
		@Test
		public void of_BufferedImage_asBufferedImage_NoOutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
				.size(100, 100)
				.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage)</li>
		 * <li>asBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void of_BufferedImage_asBufferedImages_NoOutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			List<BufferedImage> thumbnails = Thumbnails.of(img)
				.size(100, 100)
				.asBufferedImages();

			// then
			assertEquals(1, thumbnails.size());

			assertEquals(100, thumbnails.get(0).getWidth());
			assertEquals(100, thumbnails.get(0).getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage)</li>
		 * <li>toOutputStream()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalStateException.class)
		public void of_BufferedImage_toOutputStream_NoOutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			try {
				// when
				Thumbnails.of(img)
					.size(50, 50)
					.toOutputStream(os);

				fail();
			} catch (IllegalStateException e) {
				// then
				assertEquals("Output format not specified.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage)</li>
		 * <li>toOutputStreams(Iterable<OutputStream>)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalStateException.class)
		public void of_BufferedImage_toOutputStreams_NoOutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			try {
				// when
				Thumbnails.of(img)
					.size(50, 50)
					.toOutputStreams(Arrays.asList(os));

				fail();
			} catch (IllegalStateException e) {
				// then
				assertEquals("Output format not specified.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage)</li>
		 * <li>iterableBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_BufferedImage_iterableBufferedImages_NoOutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			Iterable<BufferedImage> thumbnails = Thumbnails.of(img)
				.size(50, 50)
				.iterableBufferedImages();

			// then
			Iterator<BufferedImage> iter = thumbnails.iterator();

			BufferedImage thumbnail = iter.next();
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());

			assertFalse(iter.hasNext());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage)</li>
		 * <li>outputFormat("png")</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail is written to the specified file</li>
		 * </ol>
		 */
		@Test
		public void of_BufferedImage_toFile_File_OutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			File destFile = new File("src/test/resources/Thumbnailator/tmp.png");
			destFile.deleteOnExit();

			// when
			Thumbnails.of(img)
				.size(100, 100)
				.outputFormat("png")
				.toFile(destFile);

			// then
			BufferedImage thumbnail = ImageIO.read(destFile);
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage)</li>
		 * <li>outputFormat("png")</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail is written to the specified file</li>
		 * </ol>
		 */
		@Test
		public void of_BufferedImage_toFile_String_OutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			String destFilePath = "src/test/resources/Thumbnailator/tmp.png";

			// when
			Thumbnails.of(img)
				.size(100, 100)
				.outputFormat("png")
				.toFile(destFilePath);

			// then
			File destFile = new File(destFilePath);
			destFile.deleteOnExit();

			BufferedImage thumbnail = ImageIO.read(destFile);
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage)</li>
		 * <li>outputFormat("png")</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A BufferedImage is returned</li>
		 * </ol>
		 */
		@Test
		public void of_BufferedImage_asBufferedImage_OutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			BufferedImage thumbnail = Thumbnails.of(img)
				.size(100, 100)
				.outputFormat("png")
				.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage)</li>
		 * <li>outputFormat("png")</li>
		 * <li>asBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void of_BufferedImage_asBufferedImages_OutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			List<BufferedImage> thumbnails = Thumbnails.of(img)
				.size(100, 100)
				.outputFormat("png")
				.asBufferedImages();

			// then
			assertEquals(100, thumbnails.get(0).getWidth());
			assertEquals(100, thumbnails.get(0).getHeight());
			assertEquals(1, thumbnails.size());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage)</li>
		 * <li>outputFormat("png")</li>
		 * <li>toOutputStream()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_BufferedImage_toOutputStream_OutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			// when
			Thumbnails.of(img)
				.size(50, 50)
				.outputFormat("png")
				.toOutputStream(os);

			// then
			BufferedImage thumbnail = ImageIO.read(new ByteArrayInputStream(os.toByteArray()));
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage)</li>
		 * <li>outputFormat("png")</li>
		 * <li>toOutputStreams(Iterable<OutputStream>)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_BufferedImage_toOutputStreams_OutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			// when
			Thumbnails.of(img)
				.size(50, 50)
				.outputFormat("png")
				.toOutputStreams(Arrays.asList(os));

			// then
			BufferedImage thumbnail = ImageIO.read(new ByteArrayInputStream(os.toByteArray()));
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage)</li>
		 * <li>outputFormat("png")</li>
		 * <li>iterableBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_BufferedImage_iterableBufferedImages_OutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			Iterable<BufferedImage> thumbnails = Thumbnails.of(img)
				.size(50, 50)
				.outputFormat("png")
				.iterableBufferedImages();

			// then
			Iterator<BufferedImage> iter = thumbnails.iterator();

			BufferedImage thumbnail = iter.next();
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());

			assertFalse(iter.hasNext());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException will be thrown</li>
		 * </ol>
		 */
		@Test(expected=IllegalArgumentException.class)
		public void of_BufferedImages_toFile_File_NoOutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			File destFile = new File("src/test/resources/Thumbnailator/tmp.png");
			destFile.deleteOnExit();

			try {
				// when
				Thumbnails.of(img, img)
					.size(100, 100)
					.toFile(destFile);
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot output multiple thumbnails to one file.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully. Image format is determined
		 * by the extension of the file.</li>
		 * </ol>
		 */
		@Test(expected=IllegalArgumentException.class)
		public void of_BufferedImages_toFile_String_NoOutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			String destFilePath = "src/test/resources/Thumbnailator/tmp.png";

			try {
				// when
				Thumbnails.of(img, img)
					.size(100, 100)
					.toFile(destFilePath);
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot output multiple thumbnails to one file.", e.getMessage());
				throw e;
			}
		}



		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test(expected=IllegalArgumentException.class)
		public void of_BufferedImages_asBufferedImage_NoOutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			try {
				// when
				Thumbnails.of(img, img)
					.size(100, 100)
					.asBufferedImage();
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot create one thumbnail from multiple original images.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void of_BufferedImages_asBufferedImages_NoOutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			List<BufferedImage> thumbnails = Thumbnails.of(img, img)
				.size(100, 100)
				.asBufferedImages();

			// then
			assertEquals(2, thumbnails.size());

			assertEquals(100, thumbnails.get(0).getWidth());
			assertEquals(100, thumbnails.get(0).getHeight());
			assertEquals(100, thumbnails.get(1).getWidth());
			assertEquals(100, thumbnails.get(1).getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
		 * <li>toOutputStream()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalArgumentException.class)
		public void of_BufferedImages_toOutputStream_NoOutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			try {
				// when
				Thumbnails.of(img, img)
					.size(50, 50)
					.toOutputStream(os);

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot output multiple thumbnails to a single OutputStream.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
		 * <li>toOutputStreams(Iterable<OutputStream>)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalStateException.class)
		public void of_BufferedImages_toOutputStreams_NoOutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			try {
				// when
				Thumbnails.of(img, img)
					.size(50, 50)
					.toOutputStreams(Arrays.asList(os));

				fail();
			} catch (IllegalStateException e) {
				// then
				assertEquals("Output format not specified.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
		 * <li>iterableBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_BufferedImages_iterableBufferedImages_NoOutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			Iterable<BufferedImage> thumbnails = Thumbnails.of(img, img)
				.size(50, 50)
				.iterableBufferedImages();

			// then
			Iterator<BufferedImage> iter = thumbnails.iterator();

			BufferedImage thumbnail1 = iter.next();
			assertEquals(50, thumbnail1.getWidth());
			assertEquals(50, thumbnail1.getHeight());

			BufferedImage thumbnail2 = iter.next();
			assertEquals(50, thumbnail2.getWidth());
			assertEquals(50, thumbnail2.getHeight());

			assertFalse(iter.hasNext());
		}
		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
		 * <li>outputFormat("png")</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail is written to the specified file</li>
		 * </ol>
		 */
		@Test(expected=IllegalArgumentException.class)
		public void of_BufferedImages_toFile_File_OutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			File destFile = new File("src/test/resources/Thumbnailator/tmp.png");
			destFile.deleteOnExit();

			try {
				// when
				Thumbnails.of(img, img)
					.size(100, 100)
					.outputFormat("png")
					.toFile(destFile);
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot output multiple thumbnails to one file.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
		 * <li>outputFormat("png")</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The thumbnail is written to the specified file</li>
		 * </ol>
		 */
		@Test(expected=IllegalArgumentException.class)
		public void of_BufferedImages_toFile_String_OutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			String destFilePath = "src/test/resources/Thumbnailator/tmp.png";

			try {
				// when
				Thumbnails.of(img, img)
					.size(100, 100)
					.outputFormat("png")
					.toFile(destFilePath);
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot output multiple thumbnails to one file.", e.getMessage());
				throw e;
			}
			finally {
				// clean up
				new File(destFilePath).deleteOnExit();
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
		 * <li>outputFormat("png")</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A BufferedImage is returned</li>
		 * </ol>
		 */
		@Test(expected=IllegalArgumentException.class)
		public void of_BufferedImages_asBufferedImage_OutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			try {
				// when
				Thumbnails.of(img, img)
					.size(100, 100)
					.outputFormat("png")
					.asBufferedImage();
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot create one thumbnail from multiple original images.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
		 * <li>outputFormat("png")</li>
		 * <li>asBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void of_BufferedImages_asBufferedImages_OutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			List<BufferedImage> thumbnails = Thumbnails.of(img, img)
				.size(100, 100)
				.outputFormat("png")
				.asBufferedImages();

			// then
			assertEquals(2, thumbnails.size());

			assertEquals(100, thumbnails.get(0).getWidth());
			assertEquals(100, thumbnails.get(0).getHeight());

			assertEquals(100, thumbnails.get(1).getWidth());
			assertEquals(100, thumbnails.get(1).getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
		 * <li>outputFormat("png")</li>
		 * <li>toOutputStream()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalArgumentException.class)
		public void of_BufferedImages_toOutputStream_OutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			try {
				// when
				Thumbnails.of(img, img)
					.size(50, 50)
					.outputFormat("png")
					.toOutputStream(os);

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot output multiple thumbnails to a single OutputStream.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
		 * <li>outputFormat("png")</li>
		 * <li>toOutputStreams(Iterable<OutputStream>)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_BufferedImages_toOutputStreams_OutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			ByteArrayOutputStream os1 = new ByteArrayOutputStream();
			ByteArrayOutputStream os2 = new ByteArrayOutputStream();

			// when
			Thumbnails.of(img, img)
				.size(50, 50)
				.outputFormat("png")
				.toOutputStreams(Arrays.asList(os1, os2));

			// then
			BufferedImage thumbnail = ImageIO.read(new ByteArrayInputStream(os1.toByteArray()));
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());

			thumbnail = ImageIO.read(new ByteArrayInputStream(os2.toByteArray()));
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
		 * <li>outputFormat("png")</li>
		 * <li>iterableBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_BufferedImages_iterableBufferedImages_OutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			Iterable<BufferedImage> thumbnails = Thumbnails.of(img, img)
				.size(50, 50)
				.outputFormat("png")
				.iterableBufferedImages();

			// then
			Iterator<BufferedImage> iter = thumbnails.iterator();

			BufferedImage thumbnail = iter.next();
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());

			thumbnail = iter.next();
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());

			assertFalse(iter.hasNext());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages([BufferedImage])</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A BufferedImage is returned</li>
		 * </ol>
		 */
		@Test
		public void fromImages_Single_asBufferedImage() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			BufferedImage thumbnail = Thumbnails.fromImages(Arrays.asList(img))
				.size(100, 100)
				.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages([BufferedImage, BufferedImage])</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test(expected=IllegalArgumentException.class)
		public void fromImages_Multiple_asBufferedImage() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			try {
				// when
				Thumbnails.fromImages(Arrays.asList(img, img))
					.size(100, 100)
					.asBufferedImage();
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot create one thumbnail from multiple original images.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages([BufferedImage])</li>
		 * <li>asBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void fromImages_Single_asBufferedImages() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			List<BufferedImage> thumbnails = Thumbnails.fromImages(Arrays.asList(img))
				.size(100, 100)
				.asBufferedImages();

			// then
			assertEquals(1, thumbnails.size());

			assertEquals(100, thumbnails.get(0).getWidth());
			assertEquals(100, thumbnails.get(0).getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages([BufferedImage, BufferedImage])</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void fromImages_Multiple_asBufferedImages() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			List<BufferedImage> thumbnails = Thumbnails.fromImages(Arrays.asList(img, img))
				.size(100, 100)
				.asBufferedImages();

			// then
			assertEquals(2, thumbnails.size());

			assertEquals(100, thumbnails.get(0).getWidth());
			assertEquals(100, thumbnails.get(0).getHeight());
			assertEquals(100, thumbnails.get(1).getWidth());
			assertEquals(100, thumbnails.get(1).getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages(Iterable[BufferedImage])</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A BufferedImage is returned</li>
		 * </ol>
		 */
		@Test
		public void fromImagesIterable_Single_asBufferedImage() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			BufferedImage thumbnail = Thumbnails.fromImages((Iterable<BufferedImage>)Arrays.asList(img))
				.size(100, 100)
				.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages(Iterable[BufferedImage, BufferedImage])</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test(expected=IllegalArgumentException.class)
		public void fromImagesIterable_Multiple_asBufferedImage() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			try {
				// when
				Thumbnails.fromImages((Iterable<BufferedImage>)Arrays.asList(img, img))
					.size(100, 100)
					.asBufferedImage();
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot create one thumbnail from multiple original images.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages(Iterable[BufferedImage])</li>
		 * <li>asBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void fromImagesIterable_Single_asBufferedImages() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			List<BufferedImage> thumbnails = Thumbnails.fromImages((Iterable<BufferedImage>)Arrays.asList(img))
				.size(100, 100)
				.asBufferedImages();

			// then
			assertEquals(1, thumbnails.size());

			assertEquals(100, thumbnails.get(0).getWidth());
			assertEquals(100, thumbnails.get(0).getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages(Iterable[BufferedImage, BufferedImage])</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void fromImagesIterable_Multiple_asBufferedImages() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			// when
			List<BufferedImage> thumbnails = Thumbnails.fromImages((Iterable<BufferedImage>)Arrays.asList(img, img))
				.size(100, 100)
				.asBufferedImages();

			// then
			assertEquals(2, thumbnails.size());

			assertEquals(100, thumbnails.get(0).getWidth());
			assertEquals(100, thumbnails.get(0).getHeight());
			assertEquals(100, thumbnails.get(1).getWidth());
			assertEquals(100, thumbnails.get(1).getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(URL)</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is written to the specified file.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_URL_toFile() throws IOException {
			// given
			URL f = new File("src/test/resources/Thumbnailator/grid.png").toURL();
			File outFile = new File("src/test/resources/Thumbnailator/grid.tmp.png");
			outFile.deleteOnExit();

			// when
			Thumbnails.of(f)
				.size(50, 50)
				.toFile(outFile);

			// then
			BufferedImage fromFileImage = ImageIO.read(outFile);
			assertEquals(50, fromFileImage.getWidth());
			assertEquals(50, fromFileImage.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(URL)</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_URL_asBufferedImage() throws IOException {
			// given
			URL f1 = new File("src/test/resources/Thumbnailator/grid.png").toURL();

			// when
			BufferedImage thumbnail = Thumbnails.of(f1)
				.size(50, 50)
				.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(URL)</li>
		 * <li>asBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_URL_asBufferedImages() throws IOException {
			// given
			URL f1 = new File("src/test/resources/Thumbnailator/grid.png").toURL();

			// when
			List<BufferedImage> thumbnails = Thumbnails.of(f1)
				.size(50, 50)
				.asBufferedImages();

			// then
			assertEquals(1, thumbnails.size());

			BufferedImage thumbnail = thumbnails.get(0);
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(URL)</li>
		 * <li>toOutputStream()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_URL_toOutputStream() throws IOException {
			// given
			URL f1 = new File("src/test/resources/Thumbnailator/grid.png").toURL();
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			// when
			Thumbnails.of(f1)
				.size(50, 50)
				.toOutputStream(os);

			// then
			BufferedImage thumbnail = ImageIO.read(new ByteArrayInputStream(os.toByteArray()));
			assertEquals("png", TestUtils.getFormatName(new ByteArrayInputStream(os.toByteArray())));
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(URL)</li>
		 * <li>toOutputStreams()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_URL_toOutputStreams() throws IOException {
			// given
			URL f1 = new File("src/test/resources/Thumbnailator/grid.png").toURL();
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			// when
			Thumbnails.of(f1)
				.size(50, 50)
				.toOutputStreams(Arrays.asList(os));

			// then
			BufferedImage thumbnail = ImageIO.read(new ByteArrayInputStream(os.toByteArray()));
			assertEquals("png", TestUtils.getFormatName(new ByteArrayInputStream(os.toByteArray())));
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(URL)</li>
		 * <li>iterableBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_URL_iterableBufferedImages() throws IOException {
			// given
			URL f1 = new File("src/test/resources/Thumbnailator/grid.png").toURL();

			// when
			Iterable<BufferedImage> thumbnails = Thumbnails.of(f1)
				.size(50, 50)
				.iterableBufferedImages();

			// then
			Iterator<BufferedImage> iter = thumbnails.iterator();

			BufferedImage thumbnail = iter.next();
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());

			assertFalse(iter.hasNext());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(URL, URL)</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalArgumentException.class)
		public void of_URLs_toFile() throws IOException {
			// given
			URL f = new File("src/test/resources/Thumbnailator/grid.png").toURL();
			File outFile = new File("src/test/resources/Thumbnailator/grid.tmp.png");
			outFile.deleteOnExit();

			try {
				// when
				Thumbnails.of(f, f)
					.size(50, 50)
					.toFile(outFile);
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot output multiple thumbnails to one file.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(URL, URL)</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalArgumentException.class)
		public void of_URLs_asBufferedImage() throws IOException {
			// given
			URL f = new File("src/test/resources/Thumbnailator/grid.png").toURL();

			try {
				// when
				Thumbnails.of(f, f)
					.size(50, 50)
					.asBufferedImage();
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot create one thumbnail from multiple original images.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(URL, URL)</li>
		 * <li>asBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and returned as BufferedImages in a List</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_URLs_asBufferedImages() throws IOException {
			// given
			URL f1 = new File("src/test/resources/Thumbnailator/grid.png").toURL();
			URL f2 = new File("src/test/resources/Thumbnailator/grid.jpg").toURL();

			// when
			List<BufferedImage> thumbnails = Thumbnails.of(f1, f2)
				.size(50, 50)
				.asBufferedImages();

			// then
			assertEquals(2, thumbnails.size());

			BufferedImage thumbnail1 = thumbnails.get(0);
			assertEquals(50, thumbnail1.getWidth());
			assertEquals(50, thumbnail1.getHeight());

			BufferedImage thumbnail2 = thumbnails.get(1);
			assertEquals(50, thumbnail2.getWidth());
			assertEquals(50, thumbnail2.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(URL, URL)</li>
		 * <li>toOutputStream()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalArgumentException.class)
		public void of_URLs_toOutputStream() throws IOException {
			// given
			URL f = new File("src/test/resources/Thumbnailator/grid.png").toURL();
			OutputStream os = mock(OutputStream.class);

			try {
				// when
				Thumbnails.of(f, f)
					.size(50, 50)
					.toOutputStream(os);
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot output multiple thumbnails to a single OutputStream.", e.getMessage());
				verifyZeroInteractions(os);
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(URL, URL)</li>
		 * <li>toOutputStreams()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing will be successful.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_URLs_toOutputStreams() throws IOException {
			// given
			URL f = new File("src/test/resources/Thumbnailator/grid.png").toURL();
			ByteArrayOutputStream os1 = new ByteArrayOutputStream();
			ByteArrayOutputStream os2 = new ByteArrayOutputStream();

			// when
			Thumbnails.of(f, f)
				.size(50, 50)
				.toOutputStreams(Arrays.asList(os1, os2));

			//then
			BufferedImage thumbnail = ImageIO.read(new ByteArrayInputStream(os1.toByteArray()));
			assertEquals("png", TestUtils.getFormatName(new ByteArrayInputStream(os1.toByteArray())));
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());

			thumbnail = ImageIO.read(new ByteArrayInputStream(os2.toByteArray()));
			assertEquals("png", TestUtils.getFormatName(new ByteArrayInputStream(os2.toByteArray())));
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(URL, URL)</li>
		 * <li>iterableBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and an Iterable which can iterate over the
		 * two BufferedImages is returned.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_URLs_iterableBufferedImages() throws IOException {
			// given
			URL f1 = new File("src/test/resources/Thumbnailator/grid.png").toURL();
			URL f2 = new File("src/test/resources/Thumbnailator/grid.jpg").toURL();

			// when
			Iterable<BufferedImage> thumbnails = Thumbnails.of(f1, f2)
				.size(50, 50)
				.iterableBufferedImages();

			// then
			Iterator<BufferedImage> iter = thumbnails.iterator();

			BufferedImage thumbnail1 = iter.next();
			assertEquals(50, thumbnail1.getWidth());
			assertEquals(50, thumbnail1.getHeight());

			BufferedImage thumbnail2 = iter.next();
			assertEquals(50, thumbnail2.getWidth());
			assertEquals(50, thumbnail2.getHeight());

			assertFalse(iter.hasNext());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages([URL])</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A BufferedImage is returned</li>
		 * </ol>
		 */
		@Test
		public void fromURLs_Single_asBufferedImage() throws IOException {
			// given
			URL url = new File("src/test/resources/Thumbnailator/grid.png").toURL();

			// when
			BufferedImage thumbnail = Thumbnails.fromURLs(Arrays.asList(url))
				.size(100, 100)
				.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages([URL, URL])</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test(expected=IllegalArgumentException.class)
		public void fromURLs_Multiple_asBufferedImage() throws IOException {
			// given
			URL url = new File("src/test/resources/Thumbnailator/grid.png").toURL();

			try {
				// when
				Thumbnails.fromURLs(Arrays.asList(url, url))
					.size(100, 100)
					.asBufferedImage();
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot create one thumbnail from multiple original images.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages([URL])</li>
		 * <li>asBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void fromURLs_Single_asBufferedImages() throws IOException {
			// given
			URL url = new File("src/test/resources/Thumbnailator/grid.png").toURL();

			// when
			List<BufferedImage> thumbnails = Thumbnails.fromURLs(Arrays.asList(url))
				.size(100, 100)
				.asBufferedImages();

			// then
			assertEquals(1, thumbnails.size());

			assertEquals(100, thumbnails.get(0).getWidth());
			assertEquals(100, thumbnails.get(0).getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages([URL, URL])</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void fromURLs_Multiple_asBufferedImages() throws IOException {
			// given
			URL url = new File("src/test/resources/Thumbnailator/grid.png").toURL();

			// when
			List<BufferedImage> thumbnails = Thumbnails.fromURLs(Arrays.asList(url, url))
				.size(100, 100)
				.asBufferedImages();

			// then
			assertEquals(2, thumbnails.size());

			assertEquals(100, thumbnails.get(0).getWidth());
			assertEquals(100, thumbnails.get(0).getHeight());
			assertEquals(100, thumbnails.get(1).getWidth());
			assertEquals(100, thumbnails.get(1).getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages(Iterable[URL])</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A BufferedImage is returned</li>
		 * </ol>
		 */
		@Test
		public void fromURLsIterable_Single_asBufferedImage() throws IOException {
			// given
			URL url = new File("src/test/resources/Thumbnailator/grid.png").toURL();

			// when
			BufferedImage thumbnail = Thumbnails.fromURLs((Iterable<URL>)Arrays.asList(url))
				.size(100, 100)
				.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages(Iterable[URL, URL])</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test(expected=IllegalArgumentException.class)
		public void fromURLsIterable_Multiple_asBufferedImage() throws IOException {
			// given
			URL url = new File("src/test/resources/Thumbnailator/grid.png").toURL();

			try {
				// when
				Thumbnails.fromURLs((Iterable<URL>)Arrays.asList(url, url))
					.size(100, 100)
					.asBufferedImage();
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot create one thumbnail from multiple original images.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages(Iterable[URL])</li>
		 * <li>asBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void fromURLsIterable_Single_asBufferedImages() throws IOException {
			// given
			URL url = new File("src/test/resources/Thumbnailator/grid.png").toURL();

			// when
			List<BufferedImage> thumbnails = Thumbnails.fromURLs((Iterable<URL>)Arrays.asList(url))
				.size(100, 100)
				.asBufferedImages();

			// then
			assertEquals(1, thumbnails.size());

			assertEquals(100, thumbnails.get(0).getWidth());
			assertEquals(100, thumbnails.get(0).getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages(Iterable[URL, URL])</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void fromURLsIterable_Multiple_asBufferedImages() throws IOException {
			// given
			URL url = new File("src/test/resources/Thumbnailator/grid.png").toURL();

			// when
			List<BufferedImage> thumbnails = Thumbnails.fromURLs((Iterable<URL>)Arrays.asList(url, url))
				.size(100, 100)
				.asBufferedImages();

			// then
			assertEquals(2, thumbnails.size());

			assertEquals(100, thumbnails.get(0).getWidth());
			assertEquals(100, thumbnails.get(0).getHeight());
			assertEquals(100, thumbnails.get(1).getWidth());
			assertEquals(100, thumbnails.get(1).getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(InputStream)</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is written to the specified file.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_InputStream_toFile() throws IOException {
			// given
			InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
			File outFile = new File("src/test/resources/Thumbnailator/grid.tmp.png");
			outFile.deleteOnExit();

			// when
			Thumbnails.of(is)
				.size(50, 50)
				.toFile(outFile);

			// then
			BufferedImage fromFileImage = ImageIO.read(outFile);
			assertEquals(50, fromFileImage.getWidth());
			assertEquals(50, fromFileImage.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(InputStream)</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_InputStream_asBufferedImage() throws IOException {
			// given
			InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");

			// when
			BufferedImage thumbnail = Thumbnails.of(is)
				.size(50, 50)
				.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(InputStream)</li>
		 * <li>asBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_InputStream_asBufferedImages() throws IOException {
			// given
			InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");

			// when
			List<BufferedImage> thumbnails = Thumbnails.of(is)
				.size(50, 50)
				.asBufferedImages();

			// then
			assertEquals(1, thumbnails.size());

			BufferedImage thumbnail = thumbnails.get(0);
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(InputStream)</li>
		 * <li>toOutputStream()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_InputStream_toOutputStream() throws IOException {
			// given
			InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			// when
			Thumbnails.of(is)
				.size(50, 50)
				.toOutputStream(os);

			// then
			BufferedImage thumbnail = ImageIO.read(new ByteArrayInputStream(os.toByteArray()));
			assertEquals("png", TestUtils.getFormatName(new ByteArrayInputStream(os.toByteArray())));
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(InputStream)</li>
		 * <li>toOutputStreams()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_InputStream_toOutputStreams() throws IOException {
			// given
			InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			// when
			Thumbnails.of(is)
				.size(50, 50)
				.toOutputStreams(Arrays.asList(os));

			// then
			BufferedImage thumbnail = ImageIO.read(new ByteArrayInputStream(os.toByteArray()));
			assertEquals("png", TestUtils.getFormatName(new ByteArrayInputStream(os.toByteArray())));
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(InputStream)</li>
		 * <li>iterableBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_InputStream_iterableBufferedImages() throws IOException {
			// given
			InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");

			// when
			Iterable<BufferedImage> thumbnails = Thumbnails.of(is)
				.size(50, 50)
				.iterableBufferedImages();

			// then
			Iterator<BufferedImage> iter = thumbnails.iterator();

			BufferedImage thumbnail = iter.next();
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());

			assertFalse(iter.hasNext());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(InputStream, InputStream)</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalArgumentException.class)
		public void of_InputStreams_toFile() throws IOException {
			// given
			InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
			File outFile = new File("src/test/resources/Thumbnailator/grid.tmp.png");
			outFile.deleteOnExit();

			try {
				// when
				Thumbnails.of(is, is)
					.size(50, 50)
					.toFile(outFile);
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot output multiple thumbnails to one file.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(InputStream, InputStream)</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalArgumentException.class)
		public void of_InputStreams_asBufferedImage() throws IOException {
			// given
			InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");

			try {
				// when
				Thumbnails.of(is, is)
					.size(50, 50)
					.asBufferedImage();
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot create one thumbnail from multiple original images.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(InputStream, InputStream)</li>
		 * <li>asBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and returned as BufferedImages in a List</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_InputStreams_asBufferedImages() throws IOException {
			// given
			InputStream is1 = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
			InputStream is2 = new FileInputStream("src/test/resources/Thumbnailator/grid.jpg");

			// when
			List<BufferedImage> thumbnails = Thumbnails.of(is1, is2)
				.size(50, 50)
				.asBufferedImages();

			// then
			assertEquals(2, thumbnails.size());

			BufferedImage thumbnail1 = thumbnails.get(0);
			assertEquals(50, thumbnail1.getWidth());
			assertEquals(50, thumbnail1.getHeight());

			BufferedImage thumbnail2 = thumbnails.get(1);
			assertEquals(50, thumbnail2.getWidth());
			assertEquals(50, thumbnail2.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(InputStream, InputStream)</li>
		 * <li>toOutputStream()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalArgumentException.class)
		public void of_InputStreams_toOutputStream() throws IOException {
			// given
			InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
			OutputStream os = mock(OutputStream.class);

			try {
				// when
				Thumbnails.of(is, is)
					.size(50, 50)
					.toOutputStream(os);
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot output multiple thumbnails to a single OutputStream.", e.getMessage());
				verifyZeroInteractions(os);
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(InputStream, InputStream)</li>
		 * <li>toOutputStreams()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing will be successful.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_InputStreams_toOutputStreams() throws IOException {
			// given
			InputStream is1 = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
			InputStream is2 = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
			ByteArrayOutputStream os1 = new ByteArrayOutputStream();
			ByteArrayOutputStream os2 = new ByteArrayOutputStream();

			// when
			Thumbnails.of(is1, is2)
				.size(50, 50)
				.toOutputStreams(Arrays.asList(os1, os2));

			//then
			BufferedImage thumbnail = ImageIO.read(new ByteArrayInputStream(os1.toByteArray()));
			assertEquals("png", TestUtils.getFormatName(new ByteArrayInputStream(os1.toByteArray())));
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());

			thumbnail = ImageIO.read(new ByteArrayInputStream(os2.toByteArray()));
			assertEquals("png", TestUtils.getFormatName(new ByteArrayInputStream(os2.toByteArray())));
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(InputStream, InputStream)</li>
		 * <li>iterableBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and an Iterable which can iterate over the
		 * two BufferedImages is returned.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_InputStreams_iterableBufferedImages() throws IOException {
			// given
			InputStream is1 = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
			InputStream is2 = new FileInputStream("src/test/resources/Thumbnailator/grid.jpg");

			// when
			Iterable<BufferedImage> thumbnails = Thumbnails.of(is1, is2)
				.size(50, 50)
				.iterableBufferedImages();

			// then
			Iterator<BufferedImage> iter = thumbnails.iterator();

			BufferedImage thumbnail1 = iter.next();
			assertEquals(50, thumbnail1.getWidth());
			assertEquals(50, thumbnail1.getHeight());

			BufferedImage thumbnail2 = iter.next();
			assertEquals(50, thumbnail2.getWidth());
			assertEquals(50, thumbnail2.getHeight());

			assertFalse(iter.hasNext());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages([InputStream])</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A BufferedImage is returned</li>
		 * </ol>
		 */
		@Test
		public void fromInputStreams_Single_asBufferedImage() throws IOException {
			// given
			InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");

			// when
			BufferedImage thumbnail = Thumbnails.fromInputStreams(Arrays.asList(is))
				.size(100, 100)
				.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages([InputStream, InputStream])</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test(expected=IllegalArgumentException.class)
		public void fromInputStreams_Multiple_asBufferedImage() throws IOException {
			// given
			InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");

			try {
				// when
				Thumbnails.fromInputStreams(Arrays.asList(is, is))
					.size(100, 100)
					.asBufferedImage();
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot create one thumbnail from multiple original images.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages([InputStream])</li>
		 * <li>asBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void fromInputStreams_Single_asBufferedImages() throws IOException {
			// given
			InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");

			// when
			List<BufferedImage> thumbnails = Thumbnails.fromInputStreams(Arrays.asList(is))
				.size(100, 100)
				.asBufferedImages();

			// then
			assertEquals(1, thumbnails.size());

			assertEquals(100, thumbnails.get(0).getWidth());
			assertEquals(100, thumbnails.get(0).getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages([InputStream, InputStream])</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void fromInputStreams_Multiple_asBufferedImages() throws IOException {
			// given
			InputStream is1 = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
			InputStream is2 = new FileInputStream("src/test/resources/Thumbnailator/grid.png");

			// when
			List<BufferedImage> thumbnails = Thumbnails.fromInputStreams(Arrays.asList(is1, is2))
				.size(100, 100)
				.asBufferedImages();

			// then
			assertEquals(2, thumbnails.size());

			assertEquals(100, thumbnails.get(0).getWidth());
			assertEquals(100, thumbnails.get(0).getHeight());
			assertEquals(100, thumbnails.get(1).getWidth());
			assertEquals(100, thumbnails.get(1).getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(InputStream)</li>
		 * <li>InputStream is a FileInputStream</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is written to the specified file.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_InputStream_FileInputStream_toFile() throws IOException {
			// given
			FileInputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
			File outFile = new File("src/test/resources/Thumbnailator/grid.tmp.png");
			outFile.deleteOnExit();

			// when
			Thumbnails.of(is)
				.size(50, 50)
				.toFile(outFile);

			// then
			BufferedImage fromFileImage = ImageIO.read(outFile);
			assertEquals(50, fromFileImage.getWidth());
			assertEquals(50, fromFileImage.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages(Iterable[InputStream])</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>A BufferedImage is returned</li>
		 * </ol>
		 */
		@Test
		public void fromInputStreamsIterable_Single_asBufferedImage() throws IOException {
			// given
			InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");

			// when
			BufferedImage thumbnail = Thumbnails.fromInputStreams((Iterable<InputStream>)Arrays.asList(is))
				.size(100, 100)
				.asBufferedImage();

			// then
			assertEquals(100, thumbnail.getWidth());
			assertEquals(100, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages(Iterable[InputStream, InputStream])</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test(expected=IllegalArgumentException.class)
		public void fromInputStreamsIterable_Multiple_asBufferedImage() throws IOException {
			// given
			InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");

			try {
				// when
				Thumbnails.fromInputStreams((Iterable<InputStream>)Arrays.asList(is, is))
					.size(100, 100)
					.asBufferedImage();
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot create one thumbnail from multiple original images.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages(Iterable[InputStream])</li>
		 * <li>asBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void fromInputStreamsIterable_Single_asBufferedImages() throws IOException {
			// given
			InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");

			// when
			List<BufferedImage> thumbnails = Thumbnails.fromInputStreams((Iterable<InputStream>)Arrays.asList(is))
				.size(100, 100)
				.asBufferedImages();

			// then
			assertEquals(1, thumbnails.size());

			assertEquals(100, thumbnails.get(0).getWidth());
			assertEquals(100, thumbnails.get(0).getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromImages(Iterable[InputStream, InputStream])</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalStateException is thrown.</li>
		 * </ol>
		 */
		@Test
		public void fromInputStreamsIterable_Multiple_asBufferedImages() throws IOException {
			// given
			InputStream is1 = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
			InputStream is2 = new FileInputStream("src/test/resources/Thumbnailator/grid.png");

			// when
			List<BufferedImage> thumbnails = Thumbnails.fromInputStreams((Iterable<InputStream>)Arrays.asList(is1, is2))
				.size(100, 100)
				.asBufferedImages();

			// then
			assertEquals(2, thumbnails.size());

			assertEquals(100, thumbnails.get(0).getWidth());
			assertEquals(100, thumbnails.get(0).getHeight());
			assertEquals(100, thumbnails.get(1).getWidth());
			assertEquals(100, thumbnails.get(1).getHeight());
		}

		private void assertImageExists(File f, int width, int height) throws IOException {
			assertTrue("f exists.", f.exists());

			BufferedImage img = ImageIO.read(f);
			assertNotNull("Read image is null.", img);
			assertEquals(width, img.getWidth());
			assertEquals(height, img.getHeight());
		}

		@Rule
		public TemporaryFolder temporaryFolder = new TemporaryFolder();

		private AtomicInteger counter = new AtomicInteger(0);
		private String generatePngName() {
			return this.getClass().getName() + "_" + counter.incrementAndGet() + ".png";
		}

		private File newCopyOfPngFile() throws IOException {
			File f = temporaryFolder.newFile(generatePngName());
			TestUtils.copyFile(new File("src/test/resources/Thumbnailator/grid.png"), f);
			return f;
		}

		private File newUncreatedPngFile() throws IOException {
			return new File(temporaryFolder.getRoot(), generatePngName());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>input is a file</li>
		 * <li>output is via toFile</li>
		 * <li>where the input and output file is the same</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The destination file is overwritten</li>
		 * </ol>
		 */
		@Test
		public void toFile_File_DefaultIsOverwrite() throws IOException {
			// set up
			File f = newCopyOfPngFile();

			// given
			// when
			Thumbnails.of(f)
				.size(50, 50)
				.toFile(f);

			// then
			assertImageExists(f, 50, 50);
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>the two argument toFile(File) is called</li>
		 * <li>allowOverwrite is true</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The destination file is overwritten</li>
		 * </ol>
		 */
		@Test
		public void toFile_File_AllowOverwrite() throws IOException {
			// set up
			File f = newCopyOfPngFile();

			// given
			// when
			Thumbnails.of(f)
				.size(50, 50)
				.allowOverwrite(true)
				.toFile(f);

			// then
			assertImageExists(f, 50, 50);
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>the two argument toFile(File) is called</li>
		 * <li>allowOverwrite is false</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The destination file is overwritten</li>
		 * </ol>
		 */
		@Test
		public void toFile_File_DisallowOverwrite() throws IOException {
			// set up
			File f = newCopyOfPngFile();

			// given
			// when
			try {
				Thumbnails.of(f)
					.size(50, 50)
					.allowOverwrite(false)
					.toFile(f);

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("The destination file exists.", e.getMessage());
				assertImageExists(f, 100, 100);
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>the two argument toFile(String) is called</li>
		 * <li>allowOverwrite is true</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The destination file is overwritten</li>
		 * </ol>
		 */
		@Test
		public void toFile_String_AllowOverwrite() throws IOException {
			// set up
			File f = newCopyOfPngFile();

			// given
			// when
			Thumbnails.of(f)
				.size(50, 50)
				.allowOverwrite(true)
				.toFile(f.getAbsolutePath());

			// then
			assertImageExists(f, 50, 50);
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>the two argument toFile(String) is called</li>
		 * <li>allowOverwrite is false</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>The destination file is overwritten</li>
		 * </ol>
		 */
		@Test
		public void toFile_String_DisallowOverwrite() throws IOException {
			// set up
			File f = newCopyOfPngFile();

			// given
			// when
			try {
				Thumbnails.of(f)
					.size(50, 50)
					.allowOverwrite(false)
					.toFile(f.getAbsolutePath());

				fail();
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("The destination file exists.", e.getMessage());
				assertImageExists(f, 100, 100);
			}
		}


		@Test
		public void toFiles_Rename_WritesToSameDir_AllInputFromSameDir() throws IOException {
			// given
			File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");

			String tmpDir = TMPDIR + "/rename";
			TestUtils.makeTemporaryDirectory(tmpDir);

			File f1 = new File(tmpDir, "grid1.png");
			File f2 = new File(tmpDir, "grid2.png");
			TestUtils.copyFile(sourceFile, f1);
			TestUtils.copyFile(sourceFile, f2);

			// when
			Thumbnails.of(f1, f2)
				.size(100, 100)
				.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			assertTrue(new File(tmpDir, "thumbnail.grid1.png").exists());
			assertTrue(new File(tmpDir, "thumbnail.grid2.png").exists());

			// cleanup
			TestUtils.deleteTemporaryDirectory(tmpDir);
		}

		@Test
		public void toFiles_Rename_WritesToSameDir_InputsFromDifferentDir() throws IOException {
			// given
			File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");

			String tmpDir1 = TMPDIR + "/rename/1";
			String tmpDir2 = TMPDIR + "/rename/2";
			TestUtils.makeTemporaryDirectory(tmpDir1);
			TestUtils.makeTemporaryDirectory(tmpDir2);

			File f1 = new File(tmpDir1, "grid1.png");
			File f2 = new File(tmpDir2, "grid2.png");
			TestUtils.copyFile(sourceFile, f1);
			TestUtils.copyFile(sourceFile, f2);

			// when
			Thumbnails.of(f1, f2)
				.size(100, 100)
				.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			assertTrue(new File(tmpDir1, "thumbnail.grid1.png").exists());
			assertTrue(new File(tmpDir2, "thumbnail.grid2.png").exists());

			// cleanup
			TestUtils.deleteTemporaryDirectory(tmpDir1);
			TestUtils.deleteTemporaryDirectory(tmpDir2);
			TestUtils.deleteTemporaryDirectory(TMPDIR + "/rename");
		}

		@Test
		public void toFiles_Rename_WritesToSpecifiedDir_AllInputFromSameDir() throws IOException {
			// given
			File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");

			String tmpDir = TMPDIR + "/rename";
			String targetDir = TMPDIR + "/target";
			TestUtils.makeTemporaryDirectory(tmpDir);
			TestUtils.makeTemporaryDirectory(targetDir);

			File f1 = new File(tmpDir, "grid1.png");
			File f2 = new File(tmpDir, "grid2.png");
			TestUtils.copyFile(sourceFile, f1);
			TestUtils.copyFile(sourceFile, f2);

			// when
			Thumbnails.of(f1, f2)
				.size(100, 100)
				.toFiles(new File(targetDir), Rename.PREFIX_DOT_THUMBNAIL);

			// then
			assertFalse(new File(tmpDir, "thumbnail.grid1.png").exists());
			assertFalse(new File(tmpDir, "thumbnail.grid2.png").exists());
			assertTrue(new File(targetDir, "thumbnail.grid1.png").exists());
			assertTrue(new File(targetDir, "thumbnail.grid2.png").exists());

			// cleanup
			TestUtils.deleteTemporaryDirectory(tmpDir);
			TestUtils.deleteTemporaryDirectory(targetDir);
		}

		@Test
		public void toFiles_Rename_WritesToSpecifiedDir_InputsFromDifferentDir() throws IOException {
			// given
			File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");

			String tmpDir1 = TMPDIR + "/rename/1";
			String tmpDir2 = TMPDIR + "/rename/2";
			String targetDir = TMPDIR + "/target";
			TestUtils.makeTemporaryDirectory(tmpDir1);
			TestUtils.makeTemporaryDirectory(tmpDir2);
			TestUtils.makeTemporaryDirectory(targetDir);

			File f1 = new File(tmpDir1, "grid1.png");
			File f2 = new File(tmpDir2, "grid2.png");
			TestUtils.copyFile(sourceFile, f1);
			TestUtils.copyFile(sourceFile, f2);

			// when
			Thumbnails.of(f1, f2)
				.size(100, 100)
				.toFiles(new File(targetDir), Rename.PREFIX_DOT_THUMBNAIL);

			// then
			assertFalse(new File(tmpDir1, "thumbnail.grid1.png").exists());
			assertFalse(new File(tmpDir2, "thumbnail.grid2.png").exists());
			assertTrue(new File(targetDir, "thumbnail.grid1.png").exists());
			assertTrue(new File(targetDir, "thumbnail.grid2.png").exists());

			// cleanup
			TestUtils.deleteTemporaryDirectory(tmpDir1);
			TestUtils.deleteTemporaryDirectory(tmpDir2);
			TestUtils.deleteTemporaryDirectory(TMPDIR + "/rename");
			TestUtils.deleteTemporaryDirectory(targetDir);
		}


		@Test
		public void toFiles_Rename_WritesToSpecifiedDir_InputsFromDifferentDir_InputSameName() throws IOException {
			// given
			File sourceFile1 = new File("src/test/resources/Thumbnailator/grid.png");
			File sourceFile2 = new File("src/test/resources/Thumbnailator/igrid.png");

			String tmpDir1 = TMPDIR + "/rename/1";
			String tmpDir2 = TMPDIR + "/rename/2";
			String targetDir = TMPDIR + "/target";
			TestUtils.makeTemporaryDirectory(tmpDir1);
			TestUtils.makeTemporaryDirectory(tmpDir2);
			TestUtils.makeTemporaryDirectory(targetDir);

			File f1 = new File(tmpDir1, "grid.png");
			File f2 = new File(tmpDir2, "grid.png");
			TestUtils.copyFile(sourceFile1, f1);
			TestUtils.copyFile(sourceFile2, f2);

			// when
			Thumbnails.of(f1, f2)
				.size(100, 100)
				.toFiles(new File(targetDir), Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File out = new File(targetDir, "thumbnail.grid.png");

			assertFalse(new File(tmpDir1, "thumbnail.grid.png").exists());
			assertFalse(new File(tmpDir2, "thumbnail.grid.png").exists());
			assertTrue(out.exists());

			// by default, overwrite is allowed.
			BufferedImageComparer.isSame(ImageIO.read(sourceFile2), ImageIO.read(out));

			// cleanup
			TestUtils.deleteTemporaryDirectory(tmpDir1);
			TestUtils.deleteTemporaryDirectory(tmpDir2);
			TestUtils.deleteTemporaryDirectory(TMPDIR + "/rename");
			TestUtils.deleteTemporaryDirectory(targetDir);
		}

		@Test
		public void toFiles_Rename_WritesToSpecifiedDir_InputsFromDifferentDir_InputSameName_OverwriteFalse() throws IOException {
			// given
			File sourceFile1 = new File("src/test/resources/Thumbnailator/grid.png");
			File sourceFile2 = new File("src/test/resources/Thumbnailator/igrid.png");

			String tmpDir1 = TMPDIR + "/rename/1";
			String tmpDir2 = TMPDIR + "/rename/2";
			String targetDir = TMPDIR + "/target";
			TestUtils.makeTemporaryDirectory(tmpDir1);
			TestUtils.makeTemporaryDirectory(tmpDir2);
			TestUtils.makeTemporaryDirectory(targetDir);

			File f1 = new File(tmpDir1, "grid.png");
			File f2 = new File(tmpDir2, "grid.png");
			TestUtils.copyFile(sourceFile1, f1);
			TestUtils.copyFile(sourceFile2, f2);

			// when
			Thumbnails.of(f1, f2)
				.size(100, 100)
				.allowOverwrite(false)
				.toFiles(new File(targetDir), Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File out = new File(targetDir, "thumbnail.grid.png");

			assertFalse(new File(tmpDir1, "thumbnail.grid.png").exists());
			assertFalse(new File(tmpDir2, "thumbnail.grid.png").exists());
			assertTrue(out.exists());

			BufferedImageComparer.isSame(ImageIO.read(sourceFile1), ImageIO.read(out));

			// cleanup
			TestUtils.deleteTemporaryDirectory(tmpDir1);
			TestUtils.deleteTemporaryDirectory(tmpDir2);
			TestUtils.deleteTemporaryDirectory(TMPDIR + "/rename");
			TestUtils.deleteTemporaryDirectory(targetDir);
		}

		@Test
		public void toFiles_Rename_WritesToSpecifiedDir_InputsFromDifferentDir_InputSameName_OverwriteTrue() throws IOException {
			// given
			File sourceFile1 = new File("src/test/resources/Thumbnailator/grid.png");
			File sourceFile2 = new File("src/test/resources/Thumbnailator/igrid.png");

			String tmpDir1 = TMPDIR + "/rename/1";
			String tmpDir2 = TMPDIR + "/rename/2";
			String targetDir = TMPDIR + "/target";
			TestUtils.makeTemporaryDirectory(tmpDir1);
			TestUtils.makeTemporaryDirectory(tmpDir2);
			TestUtils.makeTemporaryDirectory(targetDir);

			File f1 = new File(tmpDir1, "grid.png");
			File f2 = new File(tmpDir2, "grid.png");
			TestUtils.copyFile(sourceFile1, f1);
			TestUtils.copyFile(sourceFile2, f2);

			// when
			Thumbnails.of(f1, f2)
				.size(100, 100)
				.allowOverwrite(true)
				.toFiles(new File(targetDir), Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File out = new File(targetDir, "thumbnail.grid.png");

			assertFalse(new File(tmpDir1, "thumbnail.grid.png").exists());
			assertFalse(new File(tmpDir2, "thumbnail.grid.png").exists());
			assertTrue(out.exists());

			BufferedImageComparer.isSame(ImageIO.read(sourceFile2), ImageIO.read(out));

			// cleanup
			TestUtils.deleteTemporaryDirectory(tmpDir1);
			TestUtils.deleteTemporaryDirectory(tmpDir2);
			TestUtils.deleteTemporaryDirectory(TMPDIR + "/rename");
			TestUtils.deleteTemporaryDirectory(targetDir);
		}


		@Test
		public void toFiles_Rename_WritesToSpecifiedDir_OutputDirDoesntExist() throws IOException {
			// given
			File sourceFile1 = new File("src/test/resources/Thumbnailator/grid.png");
			String tmpDir1 = TMPDIR + "/rename/1";
			String targetDir = TMPDIR + "/target";
			TestUtils.makeTemporaryDirectory(tmpDir1);

			File f1 = new File(tmpDir1, "grid.png");
			TestUtils.copyFile(sourceFile1, f1);

			try {
				// when
				Thumbnails.of(f1)
					.size(100, 100)
					.toFiles(new File(targetDir), Rename.PREFIX_DOT_THUMBNAIL);

				fail();
			} catch (IllegalArgumentException e) {
				// then
			}

			// cleanup
			TestUtils.deleteTemporaryDirectory(tmpDir1);
			TestUtils.deleteTemporaryDirectory(TMPDIR + "/rename");
		}

		@Test
		public void toFiles_Rename_WritesToSpecifiedDir_OutputDirIsntADir() throws IOException {
			// given
			File sourceFile1 = new File("src/test/resources/Thumbnailator/grid.png");
			String tmpDir1 = TMPDIR + "/rename/1";
			String targetDir = TMPDIR + "/target";
			TestUtils.makeTemporaryDirectory(tmpDir1);
			new File(targetDir).createNewFile();

			File f1 = new File(tmpDir1, "grid.png");
			TestUtils.copyFile(sourceFile1, f1);

			try {
				// when
				Thumbnails.of(f1)
					.size(100, 100)
					.toFiles(new File(targetDir), Rename.PREFIX_DOT_THUMBNAIL);

				fail();
			} catch (IllegalArgumentException e) {
				// then
			}

			// cleanup
			TestUtils.deleteTemporaryDirectory(tmpDir1);
			TestUtils.deleteTemporaryDirectory(TMPDIR + "/rename");
			new File(targetDir).delete();
		}

		@Test
		public void asFiles_Rename_WritesToSameDir_AllInputFromSameDir() throws IOException {
			// given
			File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");

			String tmpDir = TMPDIR + "/rename";
			TestUtils.makeTemporaryDirectory(tmpDir);

			File f1 = new File(tmpDir, "grid1.png");
			File f2 = new File(tmpDir, "grid2.png");
			TestUtils.copyFile(sourceFile, f1);
			TestUtils.copyFile(sourceFile, f2);

			// when
			List<File> result = Thumbnails.of(f1, f2)
				.size(100, 100)
				.asFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File out1 = new File(tmpDir, "thumbnail.grid1.png");
			File out2 = new File(tmpDir, "thumbnail.grid2.png");

			assertTrue(out1.exists());
			assertTrue(out2.exists());
			assertTrue(result.get(0).equals(out1));
			assertTrue(result.get(1).equals(out2));

			// cleanup
			TestUtils.deleteTemporaryDirectory(tmpDir);
		}

		@Test
		public void asFiles_Rename_WritesToSameDir_InputsFromDifferentDir() throws IOException {
			// given
			File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");

			String tmpDir1 = TMPDIR + "/rename/1";
			String tmpDir2 = TMPDIR + "/rename/2";
			TestUtils.makeTemporaryDirectory(tmpDir1);
			TestUtils.makeTemporaryDirectory(tmpDir2);

			File f1 = new File(tmpDir1, "grid1.png");
			File f2 = new File(tmpDir2, "grid2.png");
			TestUtils.copyFile(sourceFile, f1);
			TestUtils.copyFile(sourceFile, f2);

			// when
			List<File> result = Thumbnails.of(f1, f2)
				.size(100, 100)
				.asFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File out1 = new File(tmpDir1, "thumbnail.grid1.png");
			File out2 = new File(tmpDir2, "thumbnail.grid2.png");

			assertTrue(out1.exists());
			assertTrue(out2.exists());
			assertTrue(result.get(0).equals(out1));
			assertTrue(result.get(1).equals(out2));

			// cleanup
			TestUtils.deleteTemporaryDirectory(tmpDir1);
			TestUtils.deleteTemporaryDirectory(tmpDir2);
			TestUtils.deleteTemporaryDirectory(TMPDIR + "/rename");
		}

		@Test
		public void asFiles_Rename_WritesToSpecifiedDir_AllInputFromSameDir() throws IOException {
			// given
			File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");

			String tmpDir = TMPDIR + "/rename";
			String targetDir = TMPDIR + "/target";
			TestUtils.makeTemporaryDirectory(tmpDir);
			TestUtils.makeTemporaryDirectory(targetDir);

			File f1 = new File(tmpDir, "grid1.png");
			File f2 = new File(tmpDir, "grid2.png");
			TestUtils.copyFile(sourceFile, f1);
			TestUtils.copyFile(sourceFile, f2);

			// when
			List<File> result = Thumbnails.of(f1, f2)
				.size(100, 100)
				.asFiles(new File(targetDir), Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File out1 = new File(targetDir, "thumbnail.grid1.png");
			File out2 = new File(targetDir, "thumbnail.grid2.png");

			assertFalse(new File(tmpDir, "thumbnail.grid1.png").exists());
			assertFalse(new File(tmpDir, "thumbnail.grid1.png").exists());
			assertTrue(out1.exists());
			assertTrue(out2.exists());
			assertTrue(result.get(0).equals(out1));
			assertTrue(result.get(1).equals(out2));

			// cleanup
			TestUtils.deleteTemporaryDirectory(tmpDir);
			TestUtils.deleteTemporaryDirectory(targetDir);
		}

		@Test
		public void asFiles_Rename_WritesToSpecifiedDir_InputsFromDifferentDir() throws IOException {
			// given
			File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");

			String tmpDir1 = TMPDIR + "/rename/1";
			String tmpDir2 = TMPDIR + "/rename/2";
			String targetDir = TMPDIR + "/target";
			TestUtils.makeTemporaryDirectory(tmpDir1);
			TestUtils.makeTemporaryDirectory(tmpDir2);
			TestUtils.makeTemporaryDirectory(targetDir);

			File f1 = new File(tmpDir1, "grid1.png");
			File f2 = new File(tmpDir2, "grid2.png");
			TestUtils.copyFile(sourceFile, f1);
			TestUtils.copyFile(sourceFile, f2);

			// when
			List<File> result = Thumbnails.of(f1, f2)
				.size(100, 100)
				.asFiles(new File(targetDir), Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File out1 = new File(targetDir, "thumbnail.grid1.png");
			File out2 = new File(targetDir, "thumbnail.grid2.png");

			assertFalse(new File(tmpDir1, "thumbnail.grid1.png").exists());
			assertFalse(new File(tmpDir2, "thumbnail.grid2.png").exists());
			assertTrue(out1.exists());
			assertTrue(out2.exists());
			assertTrue(result.get(0).equals(out1));
			assertTrue(result.get(1).equals(out2));

			// cleanup
			TestUtils.deleteTemporaryDirectory(tmpDir1);
			TestUtils.deleteTemporaryDirectory(tmpDir2);
			TestUtils.deleteTemporaryDirectory(TMPDIR + "/rename");
			TestUtils.deleteTemporaryDirectory(targetDir);
		}

		@Test
		public void asFiles_Rename_WritesToSpecifiedDir_InputsFromDifferentDir_InputSameName() throws IOException {
			// given
			File sourceFile1 = new File("src/test/resources/Thumbnailator/grid.png");
			File sourceFile2 = new File("src/test/resources/Thumbnailator/igrid.png");

			String tmpDir1 = TMPDIR + "/rename/1";
			String tmpDir2 = TMPDIR + "/rename/2";
			String targetDir = TMPDIR + "/target";
			TestUtils.makeTemporaryDirectory(tmpDir1);
			TestUtils.makeTemporaryDirectory(tmpDir2);
			TestUtils.makeTemporaryDirectory(targetDir);

			File f1 = new File(tmpDir1, "grid.png");
			File f2 = new File(tmpDir2, "grid.png");
			TestUtils.copyFile(sourceFile1, f1);
			TestUtils.copyFile(sourceFile2, f2);

			// when
			List<File> result = Thumbnails.of(f1, f2)
				.size(100, 100)
				.asFiles(new File(targetDir), Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File out = new File(targetDir, "thumbnail.grid.png");

			assertFalse(new File(tmpDir1, "thumbnail.grid.png").exists());
			assertFalse(new File(tmpDir2, "thumbnail.grid.png").exists());
			assertTrue(out.exists());
			assertTrue(result.get(0).equals(out));

			// by default, overwrite is allowed.
			BufferedImageComparer.isSame(ImageIO.read(sourceFile2), ImageIO.read(out));

			// cleanup
			TestUtils.deleteTemporaryDirectory(tmpDir1);
			TestUtils.deleteTemporaryDirectory(tmpDir2);
			TestUtils.deleteTemporaryDirectory(TMPDIR + "/rename");
			TestUtils.deleteTemporaryDirectory(targetDir);
		}

		@Test
		public void asFiles_Rename_WritesToSpecifiedDir_InputsFromDifferentDir_InputSameName_OverwriteFalse() throws IOException {
			// given
			File sourceFile1 = new File("src/test/resources/Thumbnailator/grid.png");
			File sourceFile2 = new File("src/test/resources/Thumbnailator/igrid.png");

			String tmpDir1 = TMPDIR + "/rename/1";
			String tmpDir2 = TMPDIR + "/rename/2";
			String targetDir = TMPDIR + "/target";
			TestUtils.makeTemporaryDirectory(tmpDir1);
			TestUtils.makeTemporaryDirectory(tmpDir2);
			TestUtils.makeTemporaryDirectory(targetDir);

			File f1 = new File(tmpDir1, "grid.png");
			File f2 = new File(tmpDir2, "grid.png");
			TestUtils.copyFile(sourceFile1, f1);
			TestUtils.copyFile(sourceFile2, f2);

			// when
			List<File> result = Thumbnails.of(f1, f2)
				.size(100, 100)
				.allowOverwrite(false)
				.asFiles(new File(targetDir), Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File out = new File(targetDir, "thumbnail.grid.png");

			assertFalse(new File(tmpDir1, "thumbnail.grid.png").exists());
			assertFalse(new File(tmpDir2, "thumbnail.grid.png").exists());
			assertTrue(out.exists());
			assertTrue(result.get(0).equals(out));

			BufferedImageComparer.isSame(ImageIO.read(sourceFile1), ImageIO.read(out));

			// cleanup
			TestUtils.deleteTemporaryDirectory(tmpDir1);
			TestUtils.deleteTemporaryDirectory(tmpDir2);
			TestUtils.deleteTemporaryDirectory(TMPDIR + "/rename");
			TestUtils.deleteTemporaryDirectory(targetDir);
		}

		@Test
		public void asFiles_Rename_WritesToSpecifiedDir_InputsFromDifferentDir_InputSameName_OverwriteTrue() throws IOException {
			// given
			File sourceFile1 = new File("src/test/resources/Thumbnailator/grid.png");
			File sourceFile2 = new File("src/test/resources/Thumbnailator/igrid.png");

			String tmpDir1 = TMPDIR + "/rename/1";
			String tmpDir2 = TMPDIR + "/rename/2";
			String targetDir = TMPDIR + "/target";
			TestUtils.makeTemporaryDirectory(tmpDir1);
			TestUtils.makeTemporaryDirectory(tmpDir2);
			TestUtils.makeTemporaryDirectory(targetDir);

			File f1 = new File(tmpDir1, "grid.png");
			File f2 = new File(tmpDir2, "grid.png");
			TestUtils.copyFile(sourceFile1, f1);
			TestUtils.copyFile(sourceFile2, f2);

			// when
			List<File> result = Thumbnails.of(f1, f2)
				.size(100, 100)
				.allowOverwrite(true)
				.asFiles(new File(targetDir), Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File out = new File(targetDir, "thumbnail.grid.png");

			assertFalse(new File(tmpDir1, "thumbnail.grid.png").exists());
			assertFalse(new File(tmpDir2, "thumbnail.grid.png").exists());
			assertTrue(out.exists());
			assertTrue(result.get(1).equals(out));

			BufferedImageComparer.isSame(ImageIO.read(sourceFile2), ImageIO.read(out));

			// cleanup
			TestUtils.deleteTemporaryDirectory(tmpDir1);
			TestUtils.deleteTemporaryDirectory(tmpDir2);
			TestUtils.deleteTemporaryDirectory(TMPDIR + "/rename");
			TestUtils.deleteTemporaryDirectory(targetDir);
		}

		@Test
		public void asFiles_Rename_WritesToSpecifiedDir_OutputDirDoesntExist() throws IOException {
			// given
			File sourceFile1 = new File("src/test/resources/Thumbnailator/grid.png");
			String tmpDir1 = TMPDIR + "/rename/1";
			String targetDir = TMPDIR + "/target";
			TestUtils.makeTemporaryDirectory(tmpDir1);

			File f1 = new File(tmpDir1, "grid.png");
			TestUtils.copyFile(sourceFile1, f1);

			try {
				// when
				Thumbnails.of(f1)
					.size(100, 100)
					.asFiles(new File(targetDir), Rename.PREFIX_DOT_THUMBNAIL);

				fail();
			} catch (IllegalArgumentException e) {
				// then
			}

			// cleanup
			TestUtils.deleteTemporaryDirectory(tmpDir1);
			TestUtils.deleteTemporaryDirectory(TMPDIR + "/rename");
		}

		@Test
		public void asFiles_Rename_WritesToSpecifiedDir_OutputDirIsntADir() throws IOException {
			// given
			File sourceFile1 = new File("src/test/resources/Thumbnailator/grid.png");
			String tmpDir1 = TMPDIR + "/rename/1";
			String targetDir = TMPDIR + "/target";
			TestUtils.makeTemporaryDirectory(tmpDir1);
			new File(targetDir).createNewFile();

			File f1 = new File(tmpDir1, "grid.png");
			TestUtils.copyFile(sourceFile1, f1);

			try {
				// when
				Thumbnails.of(f1)
					.size(100, 100)
					.asFiles(new File(targetDir), Rename.PREFIX_DOT_THUMBNAIL);

				fail();
			} catch (IllegalArgumentException e) {
				// then
			}

			// cleanup
			TestUtils.deleteTemporaryDirectory(tmpDir1);
			TestUtils.deleteTemporaryDirectory(TMPDIR + "/rename");
			new File(targetDir).delete();
		}

		@Test
		public void useOriginalFormat() throws IOException {
			// given
			File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
			File f = TestUtils.createTempFile(TMPDIR, "png");
			TestUtils.copyFile(sourceFile, f);

			File destFile = TestUtils.createTempFile(TMPDIR, "jpg");

			// when
			Thumbnails.of(f)
				.size(10, 10)
				.useOriginalFormat()
				.toFile(destFile);

			// then
			File actualDestFile = new File(destFile.getParent(), destFile.getName() + ".png");
			assertTrue(actualDestFile.exists());
			assertEquals("png", TestUtils.getFormatName(new FileInputStream(actualDestFile)));
		}

		@Test
		public void determineOutputFormat() throws IOException {
			// given
			File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
			File f = TestUtils.createTempFile(TMPDIR, "png");
			TestUtils.copyFile(sourceFile, f);

			File destFile = TestUtils.createTempFile(TMPDIR, "jpg");

			// when
			Thumbnails.of(f)
				.size(10, 10)
				.determineOutputFormat()
				.toFile(destFile);

			// then
			assertTrue(destFile.exists());
			assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
		}

		@Test
		public void useExifOrientationIsTrue_OrientationHonored() throws IOException {
			// given
			File outFile1 = TestUtils.createTempFile(TMPDIR, "jpg");
			File outFile2 = TestUtils.createTempFile(TMPDIR, "jpg");

			// when
			List<File> results =
				Thumbnails.of("src/test/resources/Exif/source_2.jpg", "src/test/resources/Exif/source_1.jpg")
					.size(100, 100)
					.useExifOrientation(true)
					.asFiles(Arrays.asList(outFile1, outFile2));

			// then
			assertEquals(results.size(), 2);
			BufferedImageAssert.assertMatches(
					ImageIO.read(results.get(0)),
					new float[] {
							1, 1, 1,
							1, 1, 1,
							1, 0, 0,
					}
			);
			BufferedImageAssert.assertMatches(
					ImageIO.read(results.get(1)),
					new float[] {
							1, 1, 1,
							1, 1, 1,
							1, 0, 0,
					}
			);
		}

		@Test
		public void useExifOrientationIsFalse_OrientationIgnored() throws IOException {
			// given
			File outFile1 = TestUtils.createTempFile(TMPDIR, "jpg");
			File outFile2 = TestUtils.createTempFile(TMPDIR, "jpg");

			// when
			List<File> results =
				Thumbnails.of("src/test/resources/Exif/source_2.jpg", "src/test/resources/Exif/source_1.jpg")
					.size(100, 100)
					.useExifOrientation(false)
					.asFiles(Arrays.asList(outFile1, outFile2));

			// then
			assertEquals(results.size(), 2);
			BufferedImageAssert.assertMatches(
					ImageIO.read(results.get(0)),
					new float[] {
							1, 1, 1,
							1, 1, 1,
							0, 0, 1,
					}
			);
			BufferedImageAssert.assertMatches(
					ImageIO.read(results.get(1)),
					new float[] {
							1, 1, 1,
							1, 1, 1,
							1, 0, 0,
					}
			);
		}

		@Test
		public void toOutputStreamFailsWithoutOutputFormatSpecifiedForBufferedImage() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			try {
				// when
				Thumbnails.of(img)
					.size(100, 100)
					.toOutputStream(baos);

				fail();
			} catch (Exception e) {
				// then
			}
		}

		@Test
		public void toOutputStreamImageFormatMatchesInputForPngStream() throws IOException {
			// given
			InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			// when
			Thumbnails.of(is)
				.size(100, 100)
				.toOutputStream(baos);

			// then
			assertEquals("png", TestUtils.getFormatName(new ByteArrayInputStream(baos.toByteArray())));
		}

		@Test
		public void toOutputStreamImageFormatMatchesInputForJpegStream() throws IOException {
			// given
			InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.jpg");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			// when
			Thumbnails.of(is)
				.size(100, 100)
				.toOutputStream(baos);

			// then
			assertEquals("JPEG", TestUtils.getFormatName(new ByteArrayInputStream(baos.toByteArray())));
		}

		@Test
		public void toOutputStreamImageFormatMatchesInputForPngFile() throws IOException {
			// given
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			// when
			Thumbnails.of("src/test/resources/Thumbnailator/grid.png")
				.size(100, 100)
				.toOutputStream(baos);

			// then
			assertEquals("png", TestUtils.getFormatName(new ByteArrayInputStream(baos.toByteArray())));
		}

		@Test
		public void toOutputStreamImageFormatMatchesInputForJpegFile() throws IOException {
			// given
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			// when
			Thumbnails.of("src/test/resources/Thumbnailator/grid.jpg")
				.size(100, 100)
				.toOutputStream(baos);

			// then
			assertEquals("JPEG", TestUtils.getFormatName(new ByteArrayInputStream(baos.toByteArray())));
		}

		@Test
		public void toOutputStreamImageFormatMatchesOutputFormatForPng() throws IOException {
			// given
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			// when
			Thumbnails.of("src/test/resources/Thumbnailator/grid.jpg")
				.size(100, 100)
				.outputFormat("png")
				.toOutputStream(baos);

			// then
			assertEquals("png", TestUtils.getFormatName(new ByteArrayInputStream(baos.toByteArray())));
		}

		@Test
		public void toOutputStreamImageFormatMatchesOutputFormatForJpeg() throws IOException {
			// given
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			// when
			Thumbnails.of("src/test/resources/Thumbnailator/grid.jpg")
				.size(100, 100)
				.outputFormat("JPEG")
				.toOutputStream(baos);

			// then
			assertEquals("JPEG", TestUtils.getFormatName(new ByteArrayInputStream(baos.toByteArray())));
		}

		@Test
		public void toOutputStreamImageFormatMatchesOutputFormatForPngWithBufferedImageInput() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			// when
			Thumbnails.of(img)
				.size(100, 100)
				.outputFormat("png")
				.toOutputStream(baos);

			// then
			assertEquals("png", TestUtils.getFormatName(new ByteArrayInputStream(baos.toByteArray())));
		}

		@Test
		public void toOutputStreamImageFormatMatchesOutputFormatForJpegWithBufferedImageInput() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			// when
			Thumbnails.of(img)
				.size(100, 100)
				.outputFormat("JPEG")
				.toOutputStream(baos);

			// then
			assertEquals("JPEG", TestUtils.getFormatName(new ByteArrayInputStream(baos.toByteArray())));
		}
	}

	/**
	 * These tests check the extension matching functionality:
	 *
	 * <ul>
	 * <li>
	 * If the destination file's extension doesn't match the format of
	 * the source image, an appropriate extension is appended.
	 * </li>
	 * <li>
	 * If the outputFormat is explicitly set and the destination file
	 * extension does not match, an appropriate extension is appended.
	 * </li>
	 * </ul>
	 */
	@RunWith(Parameterized.class)
	public static class FileToFileFormatAndNameTests {

		private final static List<String> FORMAT_NAMES = Arrays.asList(
				"jpg",
				"jpeg",
				"Jpg",
				"Jpeg",
				"JPG",
				"JPEG",
				"png",
				"Png",
				"PNG",
				null // used for `outputFormat` not being called.
		);

		@Parameterized.Parameters(name = "sourceExtension={0}, destinationExtension={1}, outputFormat={2}, expectedFormat={3}")
		public static Collection<Object> testCases() {
			List<Object[]> cases = new ArrayList<Object[]>();

			Map<String, String> expectedFormatNames = new HashMap<String, String>() {{
				put("jpg", "JPEG");
				put("jpeg", "JPEG");
				put("png", "png");
			}};

			for (String sourceExtension : FORMAT_NAMES) {
				// Extension of null is not valid.
				if (sourceExtension == null) {
					continue;
				}
				for (String destinationExtension : FORMAT_NAMES) {
					// Extension of null is not valid.
					if (destinationExtension == null) {
						continue;
					}
					for (String outputFormat : FORMAT_NAMES) {
						cases.add(
								new Object[] {
										sourceExtension,
										destinationExtension,
										outputFormat,
										expectedFormatNames.get(
												outputFormat != null ?
														outputFormat.toLowerCase() :
														destinationExtension.toLowerCase()
										)
								}
						);
					}
				}
			}

			return Arrays.asList(cases.toArray());
		}

		@Parameterized.Parameter
		public String sourceExtension;

		@Parameterized.Parameter(1)
		public String destinationExtension;

		@Parameterized.Parameter(2)
		public String outputFormat;

		@Parameterized.Parameter(3)
		public String expectedFormat;

		@Rule
		public TemporaryFolder temporaryFolder = new TemporaryFolder();

		@Test
		public void checkDestinationNameAndFormat() throws IOException {
			// Choose the proper source image based on sourceExtension for this test case.
			String extension = getCanonicalFormat(sourceExtension);
			File sourceFile = TestUtils.copyResourceToFile(
					"Thumbnailator/grid." + extension,
					temporaryFolder.newFile("grid." + sourceExtension)
			);
			File destFile = new File(temporaryFolder.getRoot(), "output." + destinationExtension);

			// Add call to `.outputFormat` if test case has it.
			if (outputFormat != null) {
				Thumbnails.of(sourceFile)
						.size(10, 10)
						.outputFormat(outputFormat)
						.toFile(destFile);
			} else {
				Thumbnails.of(sourceFile)
						.size(10, 10)
						.toFile(destFile);
			}

			// then
			if (outputFormat != null &&
					!getCanonicalFormat(destinationExtension).equals(getCanonicalFormat(outputFormat))) {
				destFile = new File(destFile.getAbsolutePath() + "." + outputFormat);
			}
			assertTrue(destFile.exists());
			assertEquals(expectedFormat, TestUtils.getFormatName(new FileInputStream(destFile)));
		}

		private static String getCanonicalFormat(String s) {
			if (s == null) {
				return null;
			}
			return s.equalsIgnoreCase("png") ? "png" : "jpg";
		}
	}

	@RunWith(Parameterized.class)
	public static class OrientationTests {

		@Parameterized.Parameters(name = "orientation={0}")
		public static Object[] values() {
			return new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8 };
		}

		@Parameterized.Parameter
		public int orientation;

		@Rule
		public TemporaryFolder temporaryFolder = new TemporaryFolder();

		@Test
		public void correctOrientationFromFile() throws IOException {
			// given
			File sourceFile = TestUtils.copyResourceToTemporaryFile(
					String.format("Exif/source_%s.jpg", orientation),
					temporaryFolder
			);

			// when
			BufferedImage result =
					Thumbnails.of(sourceFile)
							.size(100, 100)
							.asBufferedImage();

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
		public void correctOrientationFromInputStream() throws Exception {
			// given
			InputStream is = TestUtils.getResourceStream(
					String.format("Exif/source_%s.jpg", orientation)
			);

			// when
			BufferedImage result =
					Thumbnails.of(is)
							.size(100, 100)
							.asBufferedImage();

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

	@RunWith(Parameterized.class)
	public static class MultipleCallsCorrectOrientationTests {

		@Parameterized.Parameters(name = "orientationIsSame={0}")
		public static Object[] values() {
			return new Object[] { true, false };
		}

		@Parameterized.Parameter
		public boolean isSame;

		@Rule
		public TemporaryFolder temporaryFolder = new TemporaryFolder();

		private List<InputStream> getSources() throws IOException {
			return Arrays.asList(
					isSame ?
							TestUtils.getResourceStream("Exif/source_2.jpg") :
							TestUtils.getResourceStream("Exif/source_1.jpg"),
					TestUtils.getResourceStream("Exif/source_1.jpg")
			);
		}

		public List<File> getFileSources() throws IOException {
			String source = isSame ? "Exif/source_1.jpg" : "Exif/source_2.jpg";
			return Arrays.asList(
					TestUtils.copyResourceToTemporaryFile(
							source, "first.jpg", temporaryFolder
					),
					TestUtils.copyResourceToTemporaryFile(
							"Exif/source_1.jpg", "second.jpg", temporaryFolder
					)
			);
		}

		private void assertOrientation(BufferedImage img) {
			BufferedImageAssert.assertMatches(
					img,
					new float[] {
							1, 1, 1,
							1, 1, 1,
							1, 0, 0,
					}
			);
		}

		@Test
		public void multipleCallsCorrectOrientation_asBufferedImages() throws IOException {
			// given
			// when
			List<BufferedImage> results =
					Thumbnails.fromInputStreams(getSources())
							.size(100, 100)
							.asBufferedImages();

			// then
			assertEquals(results.size(), 2);
			for (BufferedImage result : results) {
				assertOrientation(result);
			}
		}

		@Test
		public void multipleCallsCorrectOrientation_iterableBufferedImages() throws IOException {
			// given
			// when
			Iterable<BufferedImage> results =
					Thumbnails.fromInputStreams(getSources())
							.size(100, 100)
							.iterableBufferedImages();

			// then
			Iterator<BufferedImage> iter = results.iterator();
			assertOrientation(iter.next());
			assertOrientation(iter.next());
			assertFalse(iter.hasNext());
		}

		@Test
		public void multipleCallsCorrectOrientation_asFiles() throws IOException {
			// given
			File outFile1 = new File(temporaryFolder.getRoot(), "out1.jpg");
			File outFile2 = new File(temporaryFolder.getRoot(), "out2.jpg");

			// when
			List<File> results =
					Thumbnails.fromInputStreams(getSources())
							.size(100, 100)
							.asFiles(Arrays.asList(outFile1, outFile2));

			// then
			assertEquals(results.size(), 2);
			for (File result : results) {
				assertOrientation(ImageIO.read(result));
			}
		}

		@Test
		public void multipleCallsCorrectOrientation_asFilesRename() throws IOException {
			// given
			// when
			List<File> results =
					Thumbnails.fromFiles(getFileSources())
							.size(100, 100)
							.asFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			assertEquals(results.size(), 2);
			for (File result : results) {
				assertOrientation(ImageIO.read(result));
			}
		}

		@Test
		public void multipleCallsCorrectOrientation_toFiles() throws IOException {
			// given
			File outFile1 = new File(temporaryFolder.getRoot(), "out1.jpg");
			File outFile2 = new File(temporaryFolder.getRoot(), "out2.jpg");

			// when
			Thumbnails.fromInputStreams(getSources())
					.size(100, 100)
					.toFiles(Arrays.asList(outFile1, outFile2));

			// then
			assertOrientation(ImageIO.read(outFile1));
			assertOrientation(ImageIO.read(outFile2));
		}

		@Test
		public void multipleCallsCorrectOrientation_toFilesRename() throws IOException {
			// given
			List<File> sources = getFileSources();

			// when
			Thumbnails.fromFiles(sources)
					.size(100, 100)
					.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			for (File source : sources) {
				assertOrientation(
						ImageIO.read(
								new File(
										temporaryFolder.getRoot(), "thumbnail." + source.getName()
								)
						)
				);
			}
		}

		@Test
		public void multipleCallsCorrectOrientation_toOutputStreams() throws IOException {
			// given
			ByteArrayOutputStream os1 = new ByteArrayOutputStream();
			ByteArrayOutputStream os2 = new ByteArrayOutputStream();

			// when
			Thumbnails.fromInputStreams(getSources())
					.size(100, 100)
					.toOutputStreams(Arrays.asList(os1, os2));

			// then
			assertOrientation(ImageIO.read(new ByteArrayInputStream(os1.toByteArray())));
			assertOrientation(ImageIO.read(new ByteArrayInputStream(os2.toByteArray())));
		}
	}

	@RunWith(Parameterized.class)
	public static class FilesOutputSingleFileTests {

		@Parameterized.Parameters(name = "outputFileExists={0}, allowOverwrite={1}, expectOutputChanged={2}")
		public static Object[][] values() {
			return new Object[][] {
					new Object[] { true, true, true },
					new Object[] { false, true, true },
					new Object[] { true, false, false },
					new Object[] { false, false, true }
			};
		}

		@Parameterized.Parameter
		public boolean outputFileExists;

		@Parameterized.Parameter(1)
		public boolean allowOverwrite;

		@Parameterized.Parameter(2)
		public boolean expectOutputChanged;

		@Rule
		public TemporaryFolder temporaryFolder = new TemporaryFolder();

		// States for tests.
		File outputFile;

		private void verify() throws IOException {
			// then
			if (expectOutputChanged) {
				assertImageExists(outputFile, 50, 50);
			} else {
				assertImageExists(outputFile, 100, 100);
			}
		}

		@Test
		public void ofFileToFilesIterable() throws IOException {
			outputFile = new File(temporaryFolder.getRoot(), "first.png");

			File originalFile = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png",
					temporaryFolder
			);

			if (outputFileExists) {
				TestUtils.copyFile(originalFile, outputFile);
			}

			Thumbnails.of(originalFile)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.toFiles(Collections.singletonList(outputFile));

			verify();
		}

		@Test
		public void ofFileAsFilesIterable() throws IOException {
			outputFile = new File(temporaryFolder.getRoot(), "first.png");

			File originalFile = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png",
					temporaryFolder
			);

			if (outputFileExists) {
				TestUtils.copyFile(originalFile, outputFile);
			}

			List<File> outputFiles = Thumbnails.of(originalFile)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.asFiles(Collections.singletonList(outputFile));

			// Check contents of returned list.
			Queue<File> verificationQueue = new LinkedList<File>(outputFiles);
			int expectedListSize = 0;
			if (expectOutputChanged) {
				expectedListSize++;
				assertEquals(outputFile, verificationQueue.remove());
			}
			assertEquals(0, verificationQueue.size());
			assertEquals(expectedListSize, outputFiles.size());

			verify();
		}

		@Test
		public void ofFileToFilesRename() throws IOException {
			Rename rename = Rename.PREFIX_DOT_THUMBNAIL;

			File originalFile = TestUtils.copyResourceToFile(
					"Thumbnailator/grid.png", new File(temporaryFolder.getRoot(), "first.png")
			);

			ThumbnailParameter param =
					new ThumbnailParameterBuilder()
							.size(50, 50)
							.build();
			outputFile = new File(
					temporaryFolder.getRoot(), rename.apply(originalFile.getName(), param)
			);

			if (outputFileExists) {
				TestUtils.copyFile(originalFile, outputFile);
			}

			Thumbnails.of(originalFile)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.toFiles(rename);

			verify();
		}

		@Test
		public void ofFileAsFilesRename() throws IOException {
			Rename rename = Rename.PREFIX_DOT_THUMBNAIL;

			File originalFile = TestUtils.copyResourceToFile(
					"Thumbnailator/grid.png", new File(temporaryFolder.getRoot(), "first.png")
			);

			ThumbnailParameter param =
					new ThumbnailParameterBuilder()
							.size(50, 50)
							.build();
			outputFile = new File(
					temporaryFolder.getRoot(), rename.apply(originalFile.getName(), param)
			);

			if (outputFileExists) {
				TestUtils.copyFile(originalFile, outputFile);
			}

			List<File> outputFiles = Thumbnails.of(originalFile)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.asFiles(rename);

			// Check contents of returned list.
			Queue<File> verificationQueue = new LinkedList<File>(outputFiles);
			int expectedListSize = 0;
			if (expectOutputChanged) {
				expectedListSize++;
				assertEquals(outputFile, verificationQueue.remove());
			}
			assertEquals(0, verificationQueue.size());
			assertEquals(expectedListSize, outputFiles.size());

			verify();
		}

		@Test
		public void ofBufferedImageToFilesIterableNoOutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			outputFile = new File(temporaryFolder.getRoot(), "first.png");
			if (outputFileExists) {
				TestUtils.copyResourceToFile("Thumbnailator/grid.png", outputFile);
			}

			// when
			Thumbnails.of(img)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.toFiles(Collections.singletonList(outputFile));

			// then
			assertEquals("png", TestUtils.getFormatName(new FileInputStream(outputFile)));
			verify();
		}

		@Test
		public void ofBufferedImageToFilesIterableOutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			outputFile = new File(temporaryFolder.getRoot(), "first.png");
			if (outputFileExists) {
				TestUtils.copyResourceToFile("Thumbnailator/grid.png", outputFile);
			}

			// when
			Thumbnails.of(img)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.outputFormat("png")
					.toFiles(Collections.singletonList(outputFile));

			// then
			assertEquals("png", TestUtils.getFormatName(new FileInputStream(outputFile)));
			verify();
		}

		@Test
		public void ofBufferedImageAsFilesIterableNoOutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			outputFile = new File(temporaryFolder.getRoot(), "first.png");
			if (outputFileExists) {
				TestUtils.copyResourceToFile("Thumbnailator/grid.png", outputFile);
			}

			// when
			List<File> outputFiles = Thumbnails.of(img)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.asFiles(Collections.singletonList(outputFile));

			// then
			Queue<File> verificationQueue = new LinkedList<File>(outputFiles);
			int expectedListSize = 0;
			if (expectOutputChanged) {
				expectedListSize++;
				File f = verificationQueue.remove();
				assertEquals(outputFile, f);
				assertEquals("png", TestUtils.getFormatName(new FileInputStream(f)));
			}
			assertEquals(0, verificationQueue.size());
			assertEquals(expectedListSize, outputFiles.size());

			verify();
		}

		@Test
		public void ofBufferedImageAsFilesIterableOutputFormatSpecified() throws IOException {
			// given
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			outputFile = new File(temporaryFolder.getRoot(), "first.png");
			if (outputFileExists) {
				TestUtils.copyResourceToFile("Thumbnailator/grid.png", outputFile);
			}

			// when
			List<File> outputFiles = Thumbnails.of(img)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.outputFormat("png")
					.asFiles(Collections.singletonList(outputFile));

			// then
			Queue<File> verificationQueue = new LinkedList<File>(outputFiles);
			int expectedListSize = 0;
			if (expectOutputChanged) {
				expectedListSize++;
				File f = verificationQueue.remove();
				assertEquals(outputFile, f);
				assertEquals("png", TestUtils.getFormatName(new FileInputStream(f)));
			}
			assertEquals(0, verificationQueue.size());
			assertEquals(expectedListSize, outputFiles.size());

			verify();
		}

		@Test
		public void ofUrlToFilesIterable() throws IOException {
			outputFile = new File(temporaryFolder.getRoot(), "first.png");
			URL originalUrl = TestUtils.getResource("Thumbnailator/grid.png");

			if (outputFileExists) {
				TestUtils.copyFile(
						TestUtils.copyResourceToTemporaryFile(
								"Thumbnailator/grid.png",
								temporaryFolder
						), outputFile
				);
			}

			Thumbnails.of(originalUrl)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.toFiles(Collections.singletonList(outputFile));

			verify();
		}

		@Test
		public void ofUrlAsFilesIterable() throws IOException {
			outputFile = new File(temporaryFolder.getRoot(), "first.png");
			URL originalUrl = TestUtils.getResource("Thumbnailator/grid.png");

			if (outputFileExists) {
				TestUtils.copyFile(
						TestUtils.copyResourceToTemporaryFile(
								"Thumbnailator/grid.png",
								temporaryFolder
						), outputFile
				);
			}

			List<File> outputFiles = Thumbnails.of(originalUrl)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.asFiles(Collections.singletonList(outputFile));

			// Check contents of returned list.
			Queue<File> verificationQueue = new LinkedList<File>(outputFiles);
			int expectedListSize = 0;
			if (expectOutputChanged) {
				expectedListSize++;
				assertEquals(outputFile, verificationQueue.remove());
			}
			assertEquals(0, verificationQueue.size());
			assertEquals(expectedListSize, outputFiles.size());

			verify();
		}

		@Test
		public void ofUrlToFilesRename() throws IOException {
			Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
			URL originalUrl = TestUtils.getResource("Thumbnailator/grid.png");

			try {
				Thumbnails.of(originalUrl)
						.size(50, 50)
						.allowOverwrite(allowOverwrite)
						.toFiles(rename);
				fail();

			} catch (IllegalStateException e) {
				assertEquals(
						"Cannot create thumbnails to files if original images are not from files.",
						e.getMessage()
				);
			}
		}

		@Test
		public void ofUrlAsFilesRename() throws IOException {
			Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
			URL originalUrl = TestUtils.getResource("Thumbnailator/grid.png");

			try {
				Thumbnails.of(originalUrl)
						.size(50, 50)
						.allowOverwrite(allowOverwrite)
						.asFiles(rename);
				fail();

			} catch (IllegalStateException e) {
				assertEquals(
						"Cannot create thumbnails to files if original images are not from files.",
						e.getMessage()
				);
			}
		}

		@Test
		public void ofInputStreamToFilesIterable() throws IOException {
			// given
			outputFile = new File(temporaryFolder.getRoot(), "first.png");
			InputStream is = TestUtils.getResourceStream("Thumbnailator/grid.png");

			if (outputFileExists) {
				TestUtils.copyResourceToFile(
						"Thumbnailator/grid.png",
						outputFile
				);
			}

			// when
			Thumbnails.of(is)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.toFiles(Collections.singletonList(outputFile));

			// then
			verify();
		}

		@Test
		public void ofInputStreamAsFilesIterable() throws IOException {
			// given
			outputFile = new File(temporaryFolder.getRoot(), "first.png");
			InputStream is = TestUtils.getResourceStream("Thumbnailator/grid.png");

			if (outputFileExists) {
				TestUtils.copyResourceToFile(
						"Thumbnailator/grid.png",
						outputFile
				);
			}

			// when
			List<File> outputFiles = Thumbnails.of(is)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.asFiles(Collections.singletonList(outputFile));

			// then
			// Check contents of returned list.
			Queue<File> verificationQueue = new LinkedList<File>(outputFiles);
			int expectedListSize = 0;
			if (expectOutputChanged) {
				expectedListSize++;
				assertEquals(outputFile, verificationQueue.remove());
			}
			assertEquals(0, verificationQueue.size());
			assertEquals(expectedListSize, outputFiles.size());

			verify();
		}

		@Test(expected=IllegalStateException.class)
		public void ofInputStreamToFilesRename() throws IOException {
			// given
			InputStream is = TestUtils.getResourceStream("Thumbnailator/grid.png");

			try {
				// when
				Thumbnails.of(is)
						.size(50, 50)
						.allowOverwrite(allowOverwrite)
						.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
			} catch (IllegalStateException e) {
				// then
				assertEquals("Cannot create thumbnails to files if original images are not from files.", e.getMessage());
				throw e;
			}
		}

		@Test(expected=IllegalStateException.class)
		public void ofInputStreamAsFilesRename() throws IOException {
			// given
			InputStream is = TestUtils.getResourceStream("Thumbnailator/grid.png");

			try {
				// when
				Thumbnails.of(is)
						.size(50, 50)
						.allowOverwrite(allowOverwrite)
						.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
			} catch (IllegalStateException e) {
				// then
				assertEquals("Cannot create thumbnails to files if original images are not from files.", e.getMessage());
				throw e;
			}
		}

		private void assertImageExists(File f, int width, int height) throws IOException {
			assertTrue("f exists.", f.exists());

			BufferedImage img = ImageIO.read(f);
			assertNotNull("Read image is null.", img);
			assertEquals(width, img.getWidth());
			assertEquals(height, img.getHeight());
		}
	}

	@RunWith(Parameterized.class)
	public static class FilesOutputMultipleFilesTests {

		@Parameterized.Parameters(name = "firstExists={0}, secondExists={1}, allowOverwrite={2}, expectedFirstChanged={3}, expectSecondChanged={4}")
		public static Object[][] values() {
			return new Object[][] {
					new Object[] { true, true, true, true, true },
					new Object[] { true, false, true, true, true },
					new Object[] { false, true, true, true, true },
					new Object[] { false, false, true, true, true },
					new Object[] { true, true, false, false, false },
					new Object[] { true, false, false, false, true },
					new Object[] { false, true, false, true, false },
					new Object[] { false, false, false, true, true }
			};
		}

		@Parameterized.Parameter
		public boolean firstOutputFileExists;

		@Parameterized.Parameter(1)
		public Boolean secondOutputFileExists;

		@Parameterized.Parameter(2)
		public boolean allowOverwrite;

		@Parameterized.Parameter(3)
		public boolean expectFirstOutputChanged;

		@Parameterized.Parameter(4)
		public Boolean expectSecondOutputChanged;

		@Rule
		public TemporaryFolder temporaryFolder = new TemporaryFolder();

		// States for tests.
		File firstOutputFile;
		File secondOutputFile;

		private void verify() throws IOException {
			// then
			if (expectFirstOutputChanged) {
				assertImageExists(firstOutputFile, 50, 50);
			} else {
				assertImageExists(firstOutputFile, 100, 100);
			}

			if (expectSecondOutputChanged) {
				assertImageExists(secondOutputFile, 50, 50);
			} else {
				assertImageExists(secondOutputFile, 100, 100);
			}
		}

		@Test
		public void ofFilesToFilesIterable() throws IOException {
			firstOutputFile = new File(temporaryFolder.getRoot(), "first.png");
			secondOutputFile = new File(temporaryFolder.getRoot(), "second.png");

			File originalFile = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png",
					temporaryFolder
			);

			if (firstOutputFileExists) {
				TestUtils.copyFile(originalFile, firstOutputFile);
			}
			if (secondOutputFileExists) {
				TestUtils.copyFile(originalFile, secondOutputFile);
			}

			Thumbnails.of(originalFile, originalFile)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.toFiles(Arrays.asList(firstOutputFile, secondOutputFile));

			verify();
		}

		@Test
		public void ofFilesAsFilesIterable() throws IOException {
			firstOutputFile = new File(temporaryFolder.getRoot(), "first.png");
			secondOutputFile = new File(temporaryFolder.getRoot(), "second.png");

			File originalFile = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png",
					temporaryFolder
			);

			if (firstOutputFileExists) {
				TestUtils.copyFile(originalFile, firstOutputFile);
			}
			if (secondOutputFileExists) {
				TestUtils.copyFile(originalFile, secondOutputFile);
			}

			List<File> outputFiles = Thumbnails.of(originalFile, originalFile)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.asFiles(Arrays.asList(firstOutputFile, secondOutputFile));

			// Check contents of returned list.
			Queue<File> verificationQueue = new LinkedList<File>(outputFiles);
			int expectedListSize = 0;
			if (expectFirstOutputChanged) {
				expectedListSize++;
				assertEquals(firstOutputFile, verificationQueue.remove());
			}
			if (expectSecondOutputChanged) {
				expectedListSize++;
				assertEquals(secondOutputFile, verificationQueue.remove());
			}
			assertEquals(0, verificationQueue.size());
			assertEquals(expectedListSize, outputFiles.size());

			verify();
		}

		@Test
		public void ofFilesToFilesRename() throws IOException {
			Rename rename = Rename.PREFIX_DOT_THUMBNAIL;

			File firstOriginalFile = TestUtils.copyResourceToFile(
					"Thumbnailator/grid.png", new File(temporaryFolder.getRoot(), "first.png")
			);
			File secondOriginalFile = TestUtils.copyResourceToFile(
					"Thumbnailator/grid.png", new File(temporaryFolder.getRoot(), "second.png")
			);

			ThumbnailParameter param =
					new ThumbnailParameterBuilder()
							.size(50, 50)
							.build();
			firstOutputFile = new File(
					temporaryFolder.getRoot(), rename.apply(firstOriginalFile.getName(), param)
			);
			secondOutputFile = new File(
					temporaryFolder.getRoot(), rename.apply(secondOriginalFile.getName(), param)
			);

			if (firstOutputFileExists) {
				TestUtils.copyFile(firstOriginalFile, firstOutputFile);
			}
			if (secondOutputFileExists) {
				TestUtils.copyFile(secondOriginalFile, secondOutputFile);
			}

			Thumbnails.of(firstOriginalFile, secondOriginalFile)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.toFiles(rename);

			verify();
		}

		@Test
		public void ofFilesAsFilesRename() throws IOException {
			Rename rename = Rename.PREFIX_DOT_THUMBNAIL;

			File firstOriginalFile = TestUtils.copyResourceToFile(
					"Thumbnailator/grid.png", new File(temporaryFolder.getRoot(), "first.png")
			);
			File secondOriginalFile = TestUtils.copyResourceToFile(
					"Thumbnailator/grid.png", new File(temporaryFolder.getRoot(), "second.png")
			);

			ThumbnailParameter param =
					new ThumbnailParameterBuilder()
							.size(50, 50)
							.build();
			firstOutputFile = new File(
					temporaryFolder.getRoot(), rename.apply(firstOriginalFile.getName(), param)
			);
			secondOutputFile = new File(
					temporaryFolder.getRoot(), rename.apply(secondOriginalFile.getName(), param)
			);

			if (firstOutputFileExists) {
				TestUtils.copyFile(firstOriginalFile, firstOutputFile);
			}
			if (secondOutputFileExists) {
				TestUtils.copyFile(secondOriginalFile, secondOutputFile);
			}

			List<File> outputFiles = Thumbnails.of(firstOriginalFile, secondOriginalFile)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.asFiles(rename);

			// Check contents of returned list.
			Queue<File> verificationQueue = new LinkedList<File>(outputFiles);
			int expectedListSize = 0;
			if (expectFirstOutputChanged) {
				expectedListSize++;
				assertEquals(firstOutputFile, verificationQueue.remove());
			}
			if (expectSecondOutputChanged) {
				expectedListSize++;
				assertEquals(secondOutputFile, verificationQueue.remove());
			}
			assertEquals(0, verificationQueue.size());
			assertEquals(expectedListSize, outputFiles.size());

			verify();
		}


		@Test
		public void ofBufferedImagesToFilesIterableNoOutputFormatSpecified() throws IOException {
			firstOutputFile = new File(temporaryFolder.getRoot(), "first.png");
			secondOutputFile = new File(temporaryFolder.getRoot(), "second.png");

			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			if (firstOutputFileExists) {
				TestUtils.copyResourceToFile(
						"Thumbnailator/grid.png", firstOutputFile
				);
			}
			if (secondOutputFileExists) {
				TestUtils.copyResourceToFile(
						"Thumbnailator/grid.png", secondOutputFile
				);
			}

			Thumbnails.of(img, img)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.toFiles(Arrays.asList(firstOutputFile, secondOutputFile));

			verify();
		}

		@Test
		public void ofBufferedImagesToFilesIterableOutputFormatSpecified() throws IOException {
			firstOutputFile = new File(temporaryFolder.getRoot(), "first.png");
			secondOutputFile = new File(temporaryFolder.getRoot(), "second.png");

			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			if (firstOutputFileExists) {
				TestUtils.copyResourceToFile(
						"Thumbnailator/grid.png", firstOutputFile
				);
			}
			if (secondOutputFileExists) {
				TestUtils.copyResourceToFile(
						"Thumbnailator/grid.png", secondOutputFile
				);
			}

			Thumbnails.of(img, img)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.outputFormat("png")
					.toFiles(Arrays.asList(firstOutputFile, secondOutputFile));

			assertEquals("png", TestUtils.getFormatName(new FileInputStream(firstOutputFile)));
			assertEquals("png", TestUtils.getFormatName(new FileInputStream(secondOutputFile)));
			verify();
		}

		@Test
		public void ofBufferedImagesAsFilesIterableNoOutputFormatSpecified() throws IOException {
			firstOutputFile = new File(temporaryFolder.getRoot(), "first.png");
			secondOutputFile = new File(temporaryFolder.getRoot(), "second.png");

			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			if (firstOutputFileExists) {
				TestUtils.copyResourceToFile(
						"Thumbnailator/grid.png", firstOutputFile
				);
			}
			if (secondOutputFileExists) {
				TestUtils.copyResourceToFile(
						"Thumbnailator/grid.png", secondOutputFile
				);
			}

			List<File> outputFiles = Thumbnails.of(img, img)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.asFiles(Arrays.asList(firstOutputFile, secondOutputFile));

			Queue<File> verificationQueue = new LinkedList<File>(outputFiles);
			int expectedListSize = 0;
			if (expectFirstOutputChanged) {
				expectedListSize++;
				assertEquals(firstOutputFile, verificationQueue.remove());
			}
			if (expectSecondOutputChanged) {
				expectedListSize++;
				assertEquals(secondOutputFile, verificationQueue.remove());
			}
			assertEquals(0, verificationQueue.size());
			assertEquals(expectedListSize, outputFiles.size());

			verify();
		}

		@Test
		public void ofBufferedImagesAsFilesIterableOutputFormatSpecified() throws IOException {
			firstOutputFile = new File(temporaryFolder.getRoot(), "first.png");
			secondOutputFile = new File(temporaryFolder.getRoot(), "second.png");

			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			if (firstOutputFileExists) {
				TestUtils.copyResourceToFile(
						"Thumbnailator/grid.png", firstOutputFile
				);
			}
			if (secondOutputFileExists) {
				TestUtils.copyResourceToFile(
						"Thumbnailator/grid.png", secondOutputFile
				);
			}

			List<File> outputFiles = Thumbnails.of(img, img)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.outputFormat("png")
					.asFiles(Arrays.asList(firstOutputFile, secondOutputFile));

			Queue<File> verificationQueue = new LinkedList<File>(outputFiles);
			int expectedListSize = 0;
			if (expectFirstOutputChanged) {
				expectedListSize++;
				assertEquals("png", TestUtils.getFormatName(new FileInputStream(firstOutputFile)));
				assertEquals(firstOutputFile, verificationQueue.remove());
			}
			if (expectSecondOutputChanged) {
				expectedListSize++;
				assertEquals("png", TestUtils.getFormatName(new FileInputStream(firstOutputFile)));
				assertEquals(secondOutputFile, verificationQueue.remove());
			}
			assertEquals(0, verificationQueue.size());
			assertEquals(expectedListSize, outputFiles.size());

			verify();
		}

		@Test
		public void ofUrlsToFilesIterable() throws IOException {
			firstOutputFile = new File(temporaryFolder.getRoot(), "first.png");
			secondOutputFile = new File(temporaryFolder.getRoot(), "second.png");

			URL originalUrl = TestUtils.getResource("Thumbnailator/grid.png");
			File originalFile = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png",
					temporaryFolder
			);

			if (firstOutputFileExists) {
				TestUtils.copyFile(originalFile, firstOutputFile);
			}
			if (secondOutputFileExists) {
				TestUtils.copyFile(originalFile, secondOutputFile);
			}

			Thumbnails.of(originalUrl, originalUrl)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.toFiles(Arrays.asList(firstOutputFile, secondOutputFile));

			verify();
		}

		@Test
		public void ofUrlsAsFilesIterable() throws IOException {
			firstOutputFile = new File(temporaryFolder.getRoot(), "first.png");
			secondOutputFile = new File(temporaryFolder.getRoot(), "second.png");

			URL originalUrl = TestUtils.getResource("Thumbnailator/grid.png");
			File originalFile = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png",
					temporaryFolder
			);

			if (firstOutputFileExists) {
				TestUtils.copyFile(originalFile, firstOutputFile);
			}
			if (secondOutputFileExists) {
				TestUtils.copyFile(originalFile, secondOutputFile);
			}

			List<File> outputFiles = Thumbnails.of(originalUrl, originalUrl)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.asFiles(Arrays.asList(firstOutputFile, secondOutputFile));

			// Check contents of returned list.
			Queue<File> verificationQueue = new LinkedList<File>(outputFiles);
			int expectedListSize = 0;
			if (expectFirstOutputChanged) {
				expectedListSize++;
				assertEquals(firstOutputFile, verificationQueue.remove());
			}
			if (expectSecondOutputChanged) {
				expectedListSize++;
				assertEquals(secondOutputFile, verificationQueue.remove());
			}
			assertEquals(0, verificationQueue.size());
			assertEquals(expectedListSize, outputFiles.size());

			verify();
		}

		@Test
		public void ofUrlsToFilesRename() throws IOException {
			Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
			URL firstOriginalUrl = TestUtils.getResource("Thumbnailator/grid.png");
			URL secondOriginalUrl = TestUtils.getResource("Thumbnailator/grid.jpg");

			try {
				Thumbnails.of(firstOriginalUrl, secondOriginalUrl)
						.size(50, 50)
						.allowOverwrite(allowOverwrite)
						.toFiles(rename);
				fail();

			} catch (IllegalStateException e) {
				assertEquals(
						"Cannot create thumbnails to files if original images are not from files.",
						e.getMessage()
				);
			}
		}

		@Test
		public void ofUrlsAsFilesRename() throws IOException {
			Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
			URL firstOriginalUrl = TestUtils.getResource("Thumbnailator/grid.png");
			URL secondOriginalUrl = TestUtils.getResource("Thumbnailator/grid.jpg");

			try {
				Thumbnails.of(firstOriginalUrl, secondOriginalUrl)
						.size(50, 50)
						.allowOverwrite(allowOverwrite)
						.asFiles(rename);
				fail();

			} catch (IllegalStateException e) {
				assertEquals(
						"Cannot create thumbnails to files if original images are not from files.",
						e.getMessage()
				);
			}
		}

		@Test
		public void ofInputStreamsToFilesIterable() throws IOException {
			firstOutputFile = new File(temporaryFolder.getRoot(), "first.png");
			secondOutputFile = new File(temporaryFolder.getRoot(), "second.png");

			InputStream is1 = TestUtils.getResourceStream("Thumbnailator/grid.png");
			InputStream is2 = TestUtils.getResourceStream("Thumbnailator/grid.jpg");

			if (firstOutputFileExists) {
				TestUtils.copyResourceToFile("Thumbnailator/grid.png", firstOutputFile);
			}
			if (secondOutputFileExists) {
				TestUtils.copyResourceToFile("Thumbnailator/grid.jpg", secondOutputFile);
			}

			Thumbnails.of(is1, is2)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.toFiles(Arrays.asList(firstOutputFile, secondOutputFile));

			verify();
		}

		@Test
		public void ofInputStreamsAsFilesIterable() throws IOException {
			firstOutputFile = new File(temporaryFolder.getRoot(), "first.png");
			secondOutputFile = new File(temporaryFolder.getRoot(), "second.png");

			InputStream is1 = TestUtils.getResourceStream("Thumbnailator/grid.png");
			InputStream is2 = TestUtils.getResourceStream("Thumbnailator/grid.jpg");

			if (firstOutputFileExists) {
				TestUtils.copyResourceToFile("Thumbnailator/grid.png", firstOutputFile);
			}
			if (secondOutputFileExists) {
				TestUtils.copyResourceToFile("Thumbnailator/grid.jpg", secondOutputFile);
			}

			List<File> outputFiles = Thumbnails.of(is1, is2)
					.size(50, 50)
					.allowOverwrite(allowOverwrite)
					.asFiles(Arrays.asList(firstOutputFile, secondOutputFile));

			// Check contents of returned list.
			Queue<File> verificationQueue = new LinkedList<File>(outputFiles);
			int expectedListSize = 0;
			if (expectFirstOutputChanged) {
				expectedListSize++;
				assertEquals(firstOutputFile, verificationQueue.remove());
			}
			if (expectSecondOutputChanged) {
				expectedListSize++;
				assertEquals(secondOutputFile, verificationQueue.remove());
			}
			assertEquals(0, verificationQueue.size());
			assertEquals(expectedListSize, outputFiles.size());

			verify();
		}

		@Test(expected=IllegalStateException.class)
		public void ofInputStreamsToFilesRename() throws IOException {
			// given
			InputStream is1 = TestUtils.getResourceStream("Thumbnailator/grid.png");
			InputStream is2 = TestUtils.getResourceStream("Thumbnailator/grid.jpg");

			try {
				// when
				Thumbnails.of(is1, is2)
						.size(50, 50)
						.allowOverwrite(allowOverwrite)
						.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
			} catch (IllegalStateException e) {
				// then
				assertEquals("Cannot create thumbnails to files if original images are not from files.", e.getMessage());
				throw e;
			}
		}

		@Test(expected=IllegalStateException.class)
		public void ofInputStreamsAsFilesRename() throws IOException {
			// given
			InputStream is1 = TestUtils.getResourceStream("Thumbnailator/grid.png");
			InputStream is2 = TestUtils.getResourceStream("Thumbnailator/grid.jpg");

			try {
				// when
				Thumbnails.of(is1, is2)
						.size(50, 50)
						.allowOverwrite(allowOverwrite)
						.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
			} catch (IllegalStateException e) {
				// then
				assertEquals("Cannot create thumbnails to files if original images are not from files.", e.getMessage());
				throw e;
			}
		}

		private void assertImageExists(File f, int width, int height) throws IOException {
			assertTrue("f exists.", f.exists());

			BufferedImage img = ImageIO.read(f);
			assertNotNull("Read image is null.", img);
			assertEquals(width, img.getWidth());
			assertEquals(height, img.getHeight());
		}
	}

	public static class FileInputToOutputTests {

		@Rule
		public TemporaryFolder temporaryFolder = new TemporaryFolder();

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File)</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is written to the specified file.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_File_toFile() throws IOException {
			// given
			File f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File outFile = new File(temporaryFolder.getRoot(), "grid.tmp.png");

			// when
			Thumbnails.of(f)
					.size(50, 50)
					.toFile(outFile);

			// then
			BufferedImage fromFileImage = ImageIO.read(outFile);
			assertEquals(50, fromFileImage.getWidth());
			assertEquals(50, fromFileImage.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File)</li>
		 * <li>toFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is generated and written to a file whose name is generated
		 * from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_File_toFiles_Rename() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File expectedFile = new File(temporaryFolder.getRoot(), "thumbnail.grid.png");

			// when
			Thumbnails.of(f1)
					.size(50, 50)
					.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			BufferedImage fromFileImage1 = ImageIO.read(expectedFile);
			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File)</li>
		 * <li>asFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is generated and written to a file whose name is generated
		 * from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_File_asFiles_Rename() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);

			// when
			List<File> thumbnails = Thumbnails.of(f1)
					.size(50, 50)
					.asFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			assertEquals(1, thumbnails.size());

			BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File)</li>
		 * <li>toFiles(Iterable<File>)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is generated and written to a file whose name is generated
		 * from the Iterable<File> object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_File_toFiles_Iterable() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);

			// when
			Thumbnails.of(f1)
					.size(50, 50)
					.toFiles(new ConsecutivelyNumberedFilenames(temporaryFolder.getRoot(), "temp-%d.png"));

			// then
			File expectedFile = new File(temporaryFolder.getRoot(), "temp-0.png");

			BufferedImage fromFileImage1 = ImageIO.read(expectedFile);
			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File)</li>
		 * <li>asFiles(Iterable<File>)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is generated and written to a file whose name is generated
		 * from the Iterable<File> object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_File_asFiles_Iterable() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);

			// when
			List<File> thumbnails = Thumbnails.of(f1)
					.size(50, 50)
					.asFiles(new ConsecutivelyNumberedFilenames(temporaryFolder.getRoot(), "temp-%d.png"));

			// then
			assertEquals(1, thumbnails.size());

			BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File)</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_File_asBufferedImage() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);

			// when
			BufferedImage thumbnail = Thumbnails.of(f1)
					.size(50, 50)
					.asBufferedImage();

			// then
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File)</li>
		 * <li>asBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_File_asBufferedImages() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);

			// when
			List<BufferedImage> thumbnails = Thumbnails.of(f1)
					.size(50, 50)
					.asBufferedImages();

			// then
			assertEquals(1, thumbnails.size());

			BufferedImage thumbnail = thumbnails.get(0);
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File)</li>
		 * <li>toOutputStream()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_File_toOutputStream() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			// when
			Thumbnails.of(f1)
					.size(50, 50)
					.toOutputStream(os);

			// then
			BufferedImage thumbnail = ImageIO.read(new ByteArrayInputStream(os.toByteArray()));
			assertEquals("png", TestUtils.getFormatName(new ByteArrayInputStream(os.toByteArray())));
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File)</li>
		 * <li>toOutputStreams()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_File_toOutputStreams() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			// when
			Thumbnails.of(f1)
					.size(50, 50)
					.toOutputStreams(Arrays.asList(os));

			// then
			BufferedImage thumbnail = ImageIO.read(new ByteArrayInputStream(os.toByteArray()));
			assertEquals("png", TestUtils.getFormatName(new ByteArrayInputStream(os.toByteArray())));
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File)</li>
		 * <li>iterableBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing completes successfully.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_File_iterableBufferedImages() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);

			// when
			Iterable<BufferedImage> thumbnails = Thumbnails.of(f1)
					.size(50, 50)
					.iterableBufferedImages();

			// then
			Iterator<BufferedImage> iter = thumbnails.iterator();

			BufferedImage thumbnail = iter.next();
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());

			assertFalse(iter.hasNext());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File, File)</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalArgumentException.class)
		public void of_Files_toFile() throws IOException {
			// given
			File f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File outFile = new File(temporaryFolder.getRoot(), "grid.tmp.png");

			try {
				// when
				Thumbnails.of(f, f)
						.size(50, 50)
						.toFile(outFile);
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot output multiple thumbnails to one file.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File, File)</li>
		 * <li>toFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and written to a file whose name is
		 * generated from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_Files_toFiles_Rename() throws IOException {
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
					.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File outFile1 = new File(temporaryFolder.getRoot(), "thumbnail.grid.png");
			File outFile2 = new File(temporaryFolder.getRoot(), "thumbnail.grid.jpg");

			BufferedImage fromFileImage1 = ImageIO.read(outFile1);
			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());

			BufferedImage fromFileImage2 = ImageIO.read(outFile2);
			assertEquals(50, fromFileImage2.getWidth());
			assertEquals(50, fromFileImage2.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File, File)</li>
		 * <li>asFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and written to a file whose name is
		 * generated from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_Files_asFiles_Rename() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File f2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			);

			// when
			List<File> thumbnails = Thumbnails.of(f1, f2)
					.size(50, 50)
					.asFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File outFile1 = new File(temporaryFolder.getRoot(), "thumbnail.grid.png");
			File outFile2 = new File(temporaryFolder.getRoot(), "thumbnail.grid.jpg");

			assertEquals(2, thumbnails.size());

			BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());

			BufferedImage fromFileImage2 = ImageIO.read(thumbnails.get(1));
			assertEquals(50, fromFileImage2.getWidth());
			assertEquals(50, fromFileImage2.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File, File)</li>
		 * <li>toFiles(Iterable<File>)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and written to a file whose name is
		 * generated from the Iterable<File> object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_Files_toFiles_Iterable() throws IOException {
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
					.toFiles(new ConsecutivelyNumberedFilenames(temporaryFolder.getRoot(), "temp-%d.png"));

			// then
			File outFile1 = new File(temporaryFolder.getRoot(), "temp-0.png");
			File outFile2 = new File(temporaryFolder.getRoot(), "temp-1.png");

			BufferedImage fromFileImage1 = ImageIO.read(outFile1);
			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());

			BufferedImage fromFileImage2 = ImageIO.read(outFile2);
			assertEquals(50, fromFileImage2.getWidth());
			assertEquals(50, fromFileImage2.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File, File)</li>
		 * <li>asFiles(Iterable<File>)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and written to a file whose name is
		 * generated from the Iterable<File> object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_Files_asFiles_Iterable() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File f2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			);

			// when
			List<File> thumbnails = Thumbnails.of(f1, f2)
					.size(50, 50)
					.asFiles(new ConsecutivelyNumberedFilenames(temporaryFolder.getRoot(), "temp-%d.png"));

			// then
			assertEquals(2, thumbnails.size());

			BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());

			BufferedImage fromFileImage2 = ImageIO.read(thumbnails.get(1));
			assertEquals(50, fromFileImage2.getWidth());
			assertEquals(50, fromFileImage2.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File, File)</li>
		 * <li>asBufferedImage()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalArgumentException.class)
		public void of_Files_asBufferedImage() throws IOException {
			// given
			File f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);

			try {
				// when
				Thumbnails.of(f, f)
						.size(50, 50)
						.asBufferedImage();
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot create one thumbnail from multiple original images.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File, File)</li>
		 * <li>asBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and returned as BufferedImages in a List</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_Files_asBufferedImages() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File f2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			);

			// when
			List<BufferedImage> thumbnails = Thumbnails.of(f1, f2)
					.size(50, 50)
					.asBufferedImages();

			// then
			assertEquals(2, thumbnails.size());

			BufferedImage thumbnail1 = thumbnails.get(0);
			assertEquals(50, thumbnail1.getWidth());
			assertEquals(50, thumbnail1.getHeight());

			BufferedImage thumbnail2 = thumbnails.get(1);
			assertEquals(50, thumbnail2.getWidth());
			assertEquals(50, thumbnail2.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File, File)</li>
		 * <li>toOutputStream()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalArgumentException.class)
		public void of_Files_toOutputStream() throws IOException {
			// given
			File f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			OutputStream os = mock(OutputStream.class);

			try {
				// when
				Thumbnails.of(f, f)
						.size(50, 50)
						.toOutputStream(os);
			} catch (IllegalArgumentException e) {
				// then
				assertEquals("Cannot output multiple thumbnails to a single OutputStream.", e.getMessage());
				verifyZeroInteractions(os);
				throw e;
			}
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File, File)</li>
		 * <li>toOutputStreams()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Processing will be successful.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_Files_toOutputStreams() throws IOException {
			// given
			File f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			ByteArrayOutputStream os1 = new ByteArrayOutputStream();
			ByteArrayOutputStream os2 = new ByteArrayOutputStream();

			// when
			Thumbnails.of(f, f)
					.size(50, 50)
					.toOutputStreams(Arrays.asList(os1, os2));

			//then
			BufferedImage thumbnail = ImageIO.read(new ByteArrayInputStream(os1.toByteArray()));
			assertEquals("png", TestUtils.getFormatName(new ByteArrayInputStream(os1.toByteArray())));
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());

			thumbnail = ImageIO.read(new ByteArrayInputStream(os2.toByteArray()));
			assertEquals("png", TestUtils.getFormatName(new ByteArrayInputStream(os2.toByteArray())));
			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(File, File)</li>
		 * <li>iterableBufferedImages()</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and an Iterable which can iterate over the
		 * two BufferedImages is returned.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_Files_iterableBufferedImages() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File f2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			);

			// when
			Iterable<BufferedImage> thumbnails = Thumbnails.of(f1, f2)
					.size(50, 50)
					.iterableBufferedImages();

			// then
			Iterator<BufferedImage> iter = thumbnails.iterator();

			BufferedImage thumbnail1 = iter.next();
			assertEquals(50, thumbnail1.getWidth());
			assertEquals(50, thumbnail1.getHeight());

			BufferedImage thumbnail2 = iter.next();
			assertEquals(50, thumbnail2.getWidth());
			assertEquals(50, thumbnail2.getHeight());

			assertFalse(iter.hasNext());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFiles([File])</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is written to the specified file.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFiles_Single_toFile() throws IOException {
			// given
			File f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File outFile = new File(temporaryFolder.getRoot(), "/grid.tmp.png");

			// when
			Thumbnails.fromFiles(Arrays.asList(f))
					.size(50, 50)
					.toFile(outFile);

			// then
			BufferedImage fromFileImage = ImageIO.read(outFile);

			assertEquals(50, fromFileImage.getWidth());
			assertEquals(50, fromFileImage.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFiles([File, File])</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalArgumentException.class)
		public void fromFiles_Multiple_toFile() throws IOException {
			// given
			File f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File outFile = new File(temporaryFolder.getRoot(), "grid.tmp.png");

			// when
			Thumbnails.fromFiles(Arrays.asList(f, f))
					.size(50, 50)
					.toFile(outFile);
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFiles([File])</li>
		 * <li>toFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is generated and written to a file whose name is generated
		 * from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFiles_Single_toFiles() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File expectedFile = new File(temporaryFolder.getRoot(), "thumbnail.grid.png");

			// when
			Thumbnails.fromFiles(Arrays.asList(f1))
					.size(50, 50)
					.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			BufferedImage fromFileImage1 = ImageIO.read(expectedFile);

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFiles([File, File])</li>
		 * <li>toFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and written to a file whose name is
		 * generated from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFiles_Multiple_toFiles() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File f2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			);

			// when
			Thumbnails.fromFiles(Arrays.asList(f1, f2))
					.size(50, 50)
					.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File outFile1 = new File(temporaryFolder.getRoot(), "thumbnail.grid.png");
			File outFile2 = new File(temporaryFolder.getRoot(), "thumbnail.grid.jpg");

			BufferedImage fromFileImage1 = ImageIO.read(outFile1);
			BufferedImage fromFileImage2 = ImageIO.read(outFile2);

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
			assertEquals(50, fromFileImage2.getWidth());
			assertEquals(50, fromFileImage2.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFiles([File])</li>
		 * <li>asFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is generated and written to a file whose name is generated
		 * from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFiles_Single_asFiles() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);

			// when
			List<File> thumbnails = Thumbnails.fromFiles(Arrays.asList(f1))
					.size(50, 50)
					.asFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			assertEquals(1, thumbnails.size());

			BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFiles([File, File])</li>
		 * <li>asFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and written to a file whose name is
		 * generated from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFiles_Multiple_asFiles() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File f2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			);

			// when
			List<File> thumbnails = Thumbnails.fromFiles(Arrays.asList(f1, f2))
					.size(50, 50)
					.asFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			assertEquals(2, thumbnails.size());

			BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
			BufferedImage fromFileImage2 = ImageIO.read(thumbnails.get(1));

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
			assertEquals(50, fromFileImage2.getWidth());
			assertEquals(50, fromFileImage2.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFiles(Iterable[File])</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is written to the specified file.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFilesIterable_Single_toFile() throws IOException {
			// given
			File f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File outFile = new File(temporaryFolder.getRoot(), "grid.tmp.png");

			// when
			Thumbnails.fromFiles((Iterable<File>)Arrays.asList(f))
					.size(50, 50)
					.toFile(outFile);

			// then
			BufferedImage fromFileImage = ImageIO.read(outFile);

			assertEquals(50, fromFileImage.getWidth());
			assertEquals(50, fromFileImage.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFiles(Iterable[File, File])</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalArgumentException.class)
		public void fromFilesIterable_Multiple_toFile() throws IOException {
			// given
			File f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File outFile = new File(temporaryFolder.getRoot(), "grid.tmp.png");

			// when
			Thumbnails.fromFiles((Iterable<File>)Arrays.asList(f, f))
					.size(50, 50)
					.toFile(outFile);
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFiles(Iterable[File])</li>
		 * <li>toFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is generated and written to a file whose name is generated
		 * from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFilesIterable_Single_toFiles() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File expectedFile = new File(temporaryFolder.getRoot(), "thumbnail.grid.png");

			// when
			Thumbnails.fromFiles((Iterable<File>)Arrays.asList(f1))
					.size(50, 50)
					.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			BufferedImage fromFileImage1 = ImageIO.read(expectedFile);

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFiles(Iterable[File, File])</li>
		 * <li>toFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and written to a file whose name is
		 * generated from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFilesIterable_Multiple_toFiles() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File f2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			);

			// when
			Thumbnails.fromFiles((Iterable<File>)Arrays.asList(f1, f2))
					.size(50, 50)
					.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File outFile1 = new File(temporaryFolder.getRoot(), "thumbnail.grid.png");
			File outFile2 = new File(temporaryFolder.getRoot(), "thumbnail.grid.jpg");

			BufferedImage fromFileImage1 = ImageIO.read(outFile1);
			BufferedImage fromFileImage2 = ImageIO.read(outFile2);

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
			assertEquals(50, fromFileImage2.getWidth());
			assertEquals(50, fromFileImage2.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFiles(Iterable[File])</li>
		 * <li>asFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is generated and written to a file whose name is generated
		 * from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFilesIterable_Single_asFiles() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);

			// when
			List<File> thumbnails = Thumbnails.fromFiles((Iterable<File>)Arrays.asList(f1))
					.size(50, 50)
					.asFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			assertEquals(1, thumbnails.size());

			BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFiles(Iterable[File, File])</li>
		 * <li>asFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and written to a file whose name is
		 * generated from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFilesIterable_Multiple_asFiles() throws IOException {
			// given
			File f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File f2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			);

			// when
			List<File> thumbnails = Thumbnails.fromFiles((Iterable<File>)Arrays.asList(f1, f2))
					.size(50, 50)
					.asFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			assertEquals(2, thumbnails.size());

			BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
			BufferedImage fromFileImage2 = ImageIO.read(thumbnails.get(1));

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
			assertEquals(50, fromFileImage2.getWidth());
			assertEquals(50, fromFileImage2.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(String)</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is written to the specified file.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_String_toFile() throws IOException {
			// given
			String f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			).getAbsolutePath();
			File outFile = new File(temporaryFolder.getRoot(), "grid.tmp.png");

			// when
			Thumbnails.of(f)
					.size(50, 50)
					.toFile(outFile);

			// then
			BufferedImage fromFileImage = ImageIO.read(outFile);

			assertEquals(50, fromFileImage.getWidth());
			assertEquals(50, fromFileImage.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(String, String)</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalArgumentException.class)
		public void of_Strings_toFile() throws IOException {
			// given
			String f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			).getAbsolutePath();
			File outFile = new File(temporaryFolder.getRoot(), "grid.tmp.png");

			// when
			Thumbnails.of(f, f)
					.size(50, 50)
					.toFile(outFile);
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(String)</li>
		 * <li>toFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is generated and written to a file whose name is generated
		 * from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_String_toFiles() throws IOException {
			// given
			String f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			).getAbsolutePath();
			File expectedFile = new File(temporaryFolder.getRoot(), "thumbnail.grid.png");

			// when
			Thumbnails.of(f1)
					.size(50, 50)
					.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			BufferedImage fromFileImage1 = ImageIO.read(expectedFile);

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(String, String)</li>
		 * <li>toFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and written to a file whose name is
		 * generated from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_Strings_toFiles() throws IOException {
			// given
			String f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			).getAbsolutePath();
			String f2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			).getAbsolutePath();

			// when
			Thumbnails.of(f1, f2)
					.size(50, 50)
					.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File outFile1 = new File(temporaryFolder.getRoot(), "thumbnail.grid.png");
			File outFile2 = new File(temporaryFolder.getRoot(), "thumbnail.grid.jpg");

			BufferedImage fromFileImage1 = ImageIO.read(outFile1);
			BufferedImage fromFileImage2 = ImageIO.read(outFile2);

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
			assertEquals(50, fromFileImage2.getWidth());
			assertEquals(50, fromFileImage2.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(String)</li>
		 * <li>toFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is generated and written to a file whose name is generated
		 * from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_String_asFiles() throws IOException {
			// given
			String f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			).getAbsolutePath();

			// when
			List<File> thumbnails = Thumbnails.of(f1)
					.size(50, 50)
					.asFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			assertEquals(1, thumbnails.size());

			BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.of(String, String)</li>
		 * <li>toFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and written to a file whose name is
		 * generated from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void of_Strings_asFiles() throws IOException {
			// given
			String f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			).getAbsolutePath();
			String f2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			).getAbsolutePath();

			// when
			List<File> thumbnails = Thumbnails.of(f1, f2)
					.size(50, 50)
					.asFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			assertEquals(2, thumbnails.size());

			BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
			BufferedImage fromFileImage2 = ImageIO.read(thumbnails.get(1));

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
			assertEquals(50, fromFileImage2.getWidth());
			assertEquals(50, fromFileImage2.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFilenames([String])</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is written to the specified file.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFilenames_Single_toFile() throws IOException {
			// given
			String f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			).getAbsolutePath();
			File outFile = new File(temporaryFolder.getRoot(), "grid.tmp.png");

			// when
			Thumbnails.fromFilenames(Arrays.asList(f))
					.size(50, 50)
					.toFile(outFile);

			// then
			BufferedImage fromFileImage = ImageIO.read(outFile);

			assertEquals(50, fromFileImage.getWidth());
			assertEquals(50, fromFileImage.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFilenames([String, String])</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalArgumentException.class)
		public void fromFilenames_Multiple_toFile() throws IOException {
			// given
			String f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			).getAbsolutePath();
			File outFile = new File(temporaryFolder.getRoot(), "grid.tmp.png");

			// when
			Thumbnails.fromFilenames(Arrays.asList(f, f))
					.size(50, 50)
					.toFile(outFile);
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFilenames([String])</li>
		 * <li>toFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is generated and written to a file whose name is generated
		 * from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFilenames_Single_toFiles() throws IOException {
			// given
			String f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			).getAbsolutePath();

			File expectedFile = new File(temporaryFolder.getRoot(), "thumbnail.grid.png");

			// when
			Thumbnails.fromFilenames(Arrays.asList(f1))
					.size(50, 50)
					.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			BufferedImage fromFileImage1 = ImageIO.read(expectedFile);

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFilenames([String, String])</li>
		 * <li>toFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and written to a file whose name is
		 * generated from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFilenames_Multiple_toFiles() throws IOException {
			// given
			String f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			).getAbsolutePath();
			String f2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			).getAbsolutePath();

			// when
			Thumbnails.fromFilenames(Arrays.asList(f1, f2))
					.size(50, 50)
					.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File outFile1 = new File(temporaryFolder.getRoot(), "thumbnail.grid.png");
			File outFile2 = new File(temporaryFolder.getRoot(), "thumbnail.grid.jpg");

			BufferedImage fromFileImage1 = ImageIO.read(outFile1);
			BufferedImage fromFileImage2 = ImageIO.read(outFile2);

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
			assertEquals(50, fromFileImage2.getWidth());
			assertEquals(50, fromFileImage2.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFilenames([String])</li>
		 * <li>toFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is generated and written to a file whose name is generated
		 * from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFilenames_Single_asFiles() throws IOException {
			// given
			String f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			).getAbsolutePath();

			// when
			List<File> thumbnails = Thumbnails.fromFilenames(Arrays.asList(f1))
					.size(50, 50)
					.asFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			assertEquals(1, thumbnails.size());

			BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFilenames([String, String])</li>
		 * <li>toFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and written to a file whose name is
		 * generated from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFilenames_Multiple_asFiles() throws IOException {
			// given
			String f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			).getAbsolutePath();
			String f2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			).getAbsolutePath();

			// when
			List<File> thumbnails = Thumbnails.fromFilenames(Arrays.asList(f1, f2))
					.size(50, 50)
					.asFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File outFile1 = new File(temporaryFolder.getRoot(), "thumbnail.grid.png");
			File outFile2 = new File(temporaryFolder.getRoot(), "thumbnail.grid.jpg");

			assertEquals(2, thumbnails.size());

			BufferedImage fromFileImage1 = ImageIO.read(outFile1);
			BufferedImage fromFileImage2 = ImageIO.read(outFile2);

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
			assertEquals(50, fromFileImage2.getWidth());
			assertEquals(50, fromFileImage2.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFilenames(Iterable[String])</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is written to the specified file.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFilenamesIterable_Single_toFile() throws IOException {
			// given
			String f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			).getAbsolutePath();
			File outFile = new File(temporaryFolder.getRoot(), "grid.tmp.png");

			// when
			Thumbnails.fromFilenames((Iterable<String>)Arrays.asList(f))
					.size(50, 50)
					.toFile(outFile);

			// then
			BufferedImage fromFileImage = ImageIO.read(outFile);

			assertEquals(50, fromFileImage.getWidth());
			assertEquals(50, fromFileImage.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFilenames(Iterable[String, String])</li>
		 * <li>toFile(File)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An IllegalArgumentException is thrown.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test(expected=IllegalArgumentException.class)
		public void fromFilenamesIterable_Multiple_toFile() throws IOException {
			// given
			String f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			).getAbsolutePath();
			File outFile = new File(temporaryFolder.getRoot(), "grid.tmp.png");

			// when
			Thumbnails.fromFilenames((Iterable<String>)Arrays.asList(f, f))
					.size(50, 50)
					.toFile(outFile);
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFilenames(Iterable[String])</li>
		 * <li>toFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is generated and written to a file whose name is generated
		 * from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFilenamesIterable_Single_toFiles() throws IOException {
			// given
			String f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			).getAbsolutePath();

			File expectedFile = new File(temporaryFolder.getRoot(), "thumbnail.grid.png");

			// when
			Thumbnails.fromFilenames((Iterable<String>)Arrays.asList(f1))
					.size(50, 50)
					.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			BufferedImage fromFileImage1 = ImageIO.read(expectedFile);

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFilenames(Iterable[String, String])</li>
		 * <li>toFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and written to a file whose name is
		 * generated from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFilenamesIterable_Multiple_toFiles() throws IOException {
			// given
			String f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			).getAbsolutePath();
			String f2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			).getAbsolutePath();

			// when
			Thumbnails.fromFilenames((Iterable<String>)Arrays.asList(f1, f2))
					.size(50, 50)
					.toFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File outFile1 = new File(temporaryFolder.getRoot(), "thumbnail.grid.png");
			File outFile2 = new File(temporaryFolder.getRoot(), "thumbnail.grid.jpg");

			BufferedImage fromFileImage1 = ImageIO.read(outFile1);
			BufferedImage fromFileImage2 = ImageIO.read(outFile2);

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
			assertEquals(50, fromFileImage2.getWidth());
			assertEquals(50, fromFileImage2.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFilenames(Iterable[String])</li>
		 * <li>toFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>An image is generated and written to a file whose name is generated
		 * from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFilenamesIterable_Single_asFiles() throws IOException {
			// given
			String f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			).getAbsolutePath();

			// when
			List<File> thumbnails = Thumbnails.fromFilenames((Iterable<String>)Arrays.asList(f1))
					.size(50, 50)
					.asFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			assertEquals(1, thumbnails.size());

			BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
		}

		/**
		 * Test for the {@link Thumbnails.Builder} class where,
		 * <ol>
		 * <li>Thumbnails.fromFilenames(Iterable[String, String])</li>
		 * <li>toFiles(Rename)</li>
		 * </ol>
		 * and the expected outcome is,
		 * <ol>
		 * <li>Two images are generated and written to a file whose name is
		 * generated from the Rename object.</li>
		 * </ol>
		 * @throws IOException
		 */
		@Test
		public void fromFilenamesIterable_Multiple_asFiles() throws IOException {
			// given
			String f1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			).getAbsolutePath();
			String f2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			).getAbsolutePath();

			// when
			List<File> thumbnails = Thumbnails.fromFilenames((Iterable<String>)Arrays.asList(f1, f2))
					.size(50, 50)
					.asFiles(Rename.PREFIX_DOT_THUMBNAIL);

			// then
			File outFile1 = new File(temporaryFolder.getRoot(), "thumbnail.grid.png");
			File outFile2 = new File(temporaryFolder.getRoot(), "thumbnail.grid.jpg");

			assertEquals(2, thumbnails.size());

			BufferedImage fromFileImage1 = ImageIO.read(outFile1);
			BufferedImage fromFileImage2 = ImageIO.read(outFile2);

			assertEquals(50, fromFileImage1.getWidth());
			assertEquals(50, fromFileImage1.getHeight());
			assertEquals(50, fromFileImage2.getWidth());
			assertEquals(50, fromFileImage2.getHeight());
		}
	}
}
