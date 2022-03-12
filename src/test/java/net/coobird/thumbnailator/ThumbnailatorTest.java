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

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.builders.ThumbnailParameterBuilder;
import net.coobird.thumbnailator.name.Rename;
import net.coobird.thumbnailator.resizers.DefaultResizerFactory;
import net.coobird.thumbnailator.resizers.ResizerFactory;
import net.coobird.thumbnailator.tasks.SourceSinkThumbnailTask;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import net.coobird.thumbnailator.tasks.io.BufferedImageSink;
import net.coobird.thumbnailator.tasks.io.BufferedImageSource;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.ArgumentCaptor;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link Thumbnailator} class.
 *
 * @author coobird
 *
 */
@RunWith(Enclosed.class)
@SuppressWarnings("deprecation")
public class ThumbnailatorTest {

	public static class Tests {

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnailsAsCollection(Collection, Rename, int, int)}
		 * where,
		 * <p>
		 * 1) All parameters are correct, except
		 * a) The Collection is null
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an NullPointerException.
		 *
		 * @throws IOException
		 */
		@Test(expected = NullPointerException.class)
		public void testCreateThumbnailCollections_nullCollection() throws IOException {
			try {
				Thumbnailator.createThumbnailsAsCollection(
						null,
						Rename.PREFIX_DOT_THUMBNAIL,
						50,
						50
				);

				fail();
			} catch (NullPointerException e) {
				assertEquals("Collection of Files is null.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnailsAsCollection(Collection, Rename, int, int)}
		 * where,
		 * <p>
		 * 1) All parameters are correct, except
		 * a) The Rename is null
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an NullPointerException.
		 *
		 * @throws IOException
		 */
		@Test(expected = NullPointerException.class)
		public void testCreateThumbnailCollections_nullRename() throws IOException {
			/*
			 * The files to make thumbnails of.
			 */
			List<File> files = Collections.singletonList(
					new File("nameDoesntMatter.jpg")
			);

			try {
				Thumbnailator.createThumbnailsAsCollection(
						files,
						null,
						50,
						50
				);

				fail();
			} catch (NullPointerException e) {
				assertEquals("Rename is null.", e.getMessage());
				throw e;
			}
		}



		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnails(Collection, Rename, int, int)}
		 * where,
		 * <p>
		 * 1) All parameters are correct, except
		 * a) The Collection is null
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an NullPointerException.
		 *
		 * @throws IOException
		 */
		@Test(expected = NullPointerException.class)
		public void testCreateThumbnails_nullCollection() throws IOException {
			try {
				Thumbnailator.createThumbnails(
						null,
						Rename.PREFIX_DOT_THUMBNAIL,
						50,
						50
				);

				fail();
			} catch (NullPointerException e) {
				assertEquals("Collection of Files is null.", e.getMessage());
				throw e;
			}
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnails(Collection, Rename, int, int)}
		 * where,
		 * <p>
		 * 1) All parameters are correct, except
		 * a) The Rename is null
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an NullPointerException.
		 *
		 * @throws IOException
		 */
		@Test(expected = NullPointerException.class)
		public void testCreateThumbnails_nullRename() throws IOException {
			/*
			 * The files to make thumbnails of.
			 */
			List<File> files = Collections.singletonList(
					new File("nameDoesntMatter.jpg")
			);

			try {
				Thumbnailator.createThumbnails(
						files,
						null,
						50,
						50
				);

				fail();
			} catch (NullPointerException e) {
				assertEquals("Rename is null.", e.getMessage());
				throw e;
			}
		}



		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnail(InputStream, OutputStream, int, int)}
		 * where,
		 * <p>
		 * 1) InputStream is null
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an NullPointerException.
		 *
		 * @throws IOException
		 */
		@Test(expected = NullPointerException.class)
		public void testCreateThumbnail_IOII_nullIS() throws IOException {
			/*
			 * Actual test
			 */
			InputStream is = null;
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			Thumbnailator.createThumbnail(is, os, 50, 50);

			fail();
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnail(InputStream, OutputStream, int, int)}
		 * where,
		 * <p>
		 * 1) OutputStream is null
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an NullPointerException.
		 *
		 * @throws IOException
		 */
		@Test(expected = NullPointerException.class)
		public void testCreateThumbnail_IOII_nullOS() throws IOException {
			/*
			 * Actual test
			 */
			byte[] bytes = makeImageData("jpg", 200, 200);
			InputStream is = new ByteArrayInputStream(bytes);
			ByteArrayOutputStream os = null;

			Thumbnailator.createThumbnail(is, os, 50, 50);

			fail();
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnail(InputStream, OutputStream, int, int)}
		 * where,
		 * <p>
		 * 1) InputStream is null
		 * 2) OutputStream is null
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an NullPointerException.
		 *
		 * @throws IOException
		 */
		@Test(expected = NullPointerException.class)
		public void testCreateThumbnail_IOII_nullISnullOS() throws IOException {
			Thumbnailator.createThumbnail((InputStream) null, null, 50, 50);
			fail();
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnail(InputStream, OutputStream, int, int)}
		 * where,
		 * <p>
		 * 1) InputStream throws an IOException during read.
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an IOException.
		 *
		 * @throws IOException
		 */
		@Test(expected = IOException.class)
		public void testCreateThumbnail_IOII_IOExceptionFromIS() throws IOException {
			/*
			 * Actual test
			 */
			InputStream is = mock(InputStream.class);
			doThrow(new IOException("read error!")).when(is).read();
			doThrow(new IOException("read error!")).when(is).read((byte[]) any());
			doThrow(new IOException("read error!")).when(is).read((byte[]) any(), anyInt(), anyInt());

			ByteArrayOutputStream os = new ByteArrayOutputStream();

			Thumbnailator.createThumbnail(is, os, 50, 50);

			fail();
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnail(InputStream, OutputStream, int, int)}
		 * where,
		 * <p>
		 * 1) OutputStream throws an IOException during read.
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an IOException.
		 *
		 * @throws IOException
		 */
		@Test(expected = IOException.class)
		public void testCreateThumbnail_IOII_IOExceptionFromOS() throws IOException {
			/*
			 * Actual test
			 */
			byte[] bytes = makeImageData("png", 200, 200);
			InputStream is = new ByteArrayInputStream(bytes);

			OutputStream os = mock(OutputStream.class);
			doThrow(new IOException("write error!")).when(os).write(anyInt());
			doThrow(new IOException("write error!")).when(os).write((byte[]) any());
			doThrow(new IOException("write error!")).when(os).write((byte[]) any(), anyInt(), anyInt());

			Thumbnailator.createThumbnail(is, os, 50, 50);

			fail();
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnail(InputStream, OutputStream, String, int, int)}
		 * where,
		 * <p>
		 * 1) InputStream throws an IOException during read.
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an IOException.
		 *
		 * @throws IOException
		 */
		@Test(expected = IOException.class)
		public void testCreateThumbnail_IOSII_IOExceptionFromIS() throws IOException {
			/*
			 * Actual test
			 */
			InputStream is = mock(InputStream.class);
			doThrow(new IOException("read error!")).when(is).read();
			doThrow(new IOException("read error!")).when(is).read((byte[]) any());
			doThrow(new IOException("read error!")).when(is).read((byte[]) any(), anyInt(), anyInt());

			ByteArrayOutputStream os = new ByteArrayOutputStream();

			Thumbnailator.createThumbnail(is, os, "png", 50, 50);

			fail();
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnail(InputStream, OutputStream, String, int, int)}
		 * where,
		 * <p>
		 * 1) OutputStream throws an IOException during read.
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an IOException.
		 *
		 * @throws IOException
		 */
		@Test(expected = IOException.class)
		public void testCreateThumbnail_IOSII_IOExceptionFromOS() throws IOException {
			/*
			 * Actual test
			 */
			byte[] bytes = makeImageData("png", 200, 200);
			InputStream is = new ByteArrayInputStream(bytes);

			OutputStream os = mock(OutputStream.class);
			doThrow(new IOException("write error!")).when(os).write(anyInt());
			doThrow(new IOException("write error!")).when(os).write((byte[]) any());
			doThrow(new IOException("write error!")).when(os).write((byte[]) any(), anyInt(), anyInt());

			Thumbnailator.createThumbnail(is, os, "png", 50, 50);

			fail();
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnail(File, File, int, int)}
		 * where,
		 * <p>
		 * 1) Input File is null
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an NullPointerException.
		 *
		 * @throws IOException
		 */
		@Test(expected = NullPointerException.class)
		public void testCreateThumbnail_FFII_nullInputFile() throws IOException {
			/*
			 * Actual test
			 */
			File inputFile = null;
			File outputFile = new File("bar.jpg");

			Thumbnailator.createThumbnail(inputFile, outputFile, 50, 50);

			fail();
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnail(File, File, int, int)}
		 * where,
		 * <p>
		 * 1) Output File is null
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an NullPointerException.
		 *
		 * @throws IOException
		 */
		@Test(expected = NullPointerException.class)
		public void testCreateThumbnail_FFII_nullOutputFile() throws IOException {
			/*
			 * Actual test
			 */
			File inputFile = new File("foo.jpg");
			File outputFile = null;

			Thumbnailator.createThumbnail(inputFile, outputFile, 50, 50);

			fail();
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnail(File, File, int, int)}
		 * where,
		 * <p>
		 * 1) Input File is null
		 * 2) Output File is null
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an NullPointerException.
		 *
		 * @throws IOException
		 */
		@Test(expected = NullPointerException.class)
		public void testCreateThumbnail_FFII_nullInputAndOutputFiles() throws IOException {
			Thumbnailator.createThumbnail((File) null, null, 50, 50);
			fail();
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnail(File, File, int, int)}
		 * where,
		 * <p>
		 * 1) Input File does not exist.
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an IOException.
		 *
		 * @throws IOException
		 */
		@Test
		public void testCreateThumbnail_FFII_nonExistentInputFile() throws IOException {
			/*
			 * Actual test
			 */
			File inputFile = new File("foo.jpg");
			File outputFile = new File("bar.jpg");

			try {
				Thumbnailator.createThumbnail(inputFile, outputFile, 50, 50);
				fail();
			} catch (IOException e) {
				assertEquals("Input file does not exist.", e.getMessage());
			}
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnail(File, File, int, int)}
		 * where,
		 * <p>
		 * 1) A problem occurs while writing to the file.
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an IOException.
		 *
		 * @throws IOException
		 */
		@Ignore
		public void testCreateThumbnail_FFII_IOExceptionOnWrite() throws IOException {
			//Cannot craft a test case to test this condition.
			fail();
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnail(File, int, int)}
		 * where,
		 * <p>
		 * 1) Input File is null
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an NullPointerException.
		 *
		 * @throws IOException
		 */
		@Test(expected = NullPointerException.class)
		public void testCreateThumbnail_FII_nullInputFile() throws IOException {
			Thumbnailator.createThumbnail((File) null, 50, 50);
			fail();
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnail(BufferedImage, int, int)}
		 * where,
		 * <p>
		 * 1) Method arguments are correct
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will complete successfully.
		 */
		@Test
		public void testCreateThumbnail_BII_CorrectUsage() {
			/*
			 * Actual test
			 */
			BufferedImage img =
					new BufferedImageBuilder(200, 200, BufferedImage.TYPE_INT_ARGB).build();

			BufferedImage thumbnail = Thumbnailator.createThumbnail(img, 50, 50);

			assertEquals(50, thumbnail.getWidth());
			assertEquals(50, thumbnail.getHeight());
			assertEquals(BufferedImage.TYPE_INT_ARGB, thumbnail.getType());
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnail(Image, int, int)}
		 * where,
		 * <p>
		 * 1) Method arguments are correct
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will complete successfully.
		 */
		@Test
		public void testCreateThumbnail_III_CorrectUsage() {
			/*
			 * Actual test
			 */
			BufferedImage img =
					new BufferedImageBuilder(200, 200, BufferedImage.TYPE_INT_ARGB).build();

			Image thumbnail = Thumbnailator.createThumbnail((Image) img, 50, 50);

			assertEquals(50, thumbnail.getWidth(null));
			assertEquals(50, thumbnail.getHeight(null));
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnail(net.coobird.thumbnailator.tasks.ThumbnailTask)}
		 * where,
		 * <p>
		 * 1) The correct parameters are given.
		 * 2) The size is specified for the ThumbnailParameter.
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) The ResizerFactory is being used.
		 *
		 * @throws IOException
		 */
		@Test
		public void testCreateThumbnail_ThumbnailTask_ResizerFactoryBeingUsed_UsingSize() throws IOException {
			// given
			BufferedImageSource source = new BufferedImageSource(
					new BufferedImageBuilder(200, 200, BufferedImage.TYPE_INT_ARGB).build()
			);
			BufferedImageSink sink = new BufferedImageSink();
			ResizerFactory resizerFactory = spy(DefaultResizerFactory.getInstance());

			ThumbnailParameter param =
					new ThumbnailParameterBuilder()
							.size(100, 100)
							.resizerFactory(resizerFactory)
							.build();


			// when
			Thumbnailator.createThumbnail(
					new SourceSinkThumbnailTask<BufferedImage, BufferedImage>(
							param, source, sink
					)
			);

			// then
			verify(resizerFactory)
					.getResizer(new Dimension(200, 200), new Dimension(100, 100));
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnail(net.coobird.thumbnailator.tasks.ThumbnailTask)}
		 * where,
		 * <p>
		 * 1) The correct parameters are given.
		 * 2) The scale is specified for the ThumbnailParameter.
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) The ResizerFactory is being used.
		 *
		 * @throws IOException
		 */
		@Test
		public void testCreateThumbnail_ThumbnailTask_ResizerFactoryBeingUsed_UsingScale() throws IOException {
			// given
			BufferedImageSource source = new BufferedImageSource(
					new BufferedImageBuilder(200, 200, BufferedImage.TYPE_INT_ARGB).build()
			);
			BufferedImageSink sink = new BufferedImageSink();
			ResizerFactory resizerFactory = spy(DefaultResizerFactory.getInstance());

			ThumbnailParameter param =
					new ThumbnailParameterBuilder()
							.scale(0.5)
							.resizerFactory(resizerFactory)
							.build();


			// when
			Thumbnailator.createThumbnail(
					new SourceSinkThumbnailTask<BufferedImage, BufferedImage>(
							param, source, sink
					)
			);

			// then
			verify(resizerFactory)
					.getResizer(new Dimension(200, 200), new Dimension(100, 100));
		}
	}

	public static class FileIOTests {

		@Rule
		public TemporaryFolder temporaryFolder = new TemporaryFolder();

		private boolean isTemporaryFolderEmpty() {
			String[] files = temporaryFolder.getRoot().list();
			if (files == null) {
				throw new IllegalStateException("Temporary folder didn't exist. Shouldn't happen.");
			}
			return files.length == 0;
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnailsAsCollection(Collection, Rename, int, int)}
		 * where,
		 * <p>
		 * 1) All parameters are correct
		 * a) The Collection is an empty List.
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will complete successfully.
		 *
		 * @throws IOException
		 */
		@Test
		public void testCreateThumbnailCollections_NoErrors_EmptyList() throws IOException {
			/*
			 * The files to make thumbnails of -- nothing!
			 */
			List<File> files = Collections.emptyList();

			Collection<File> resultingFiles =
					Thumbnailator.createThumbnailsAsCollection(
							files,
							Rename.PREFIX_DOT_THUMBNAIL,
							50,
							50
					);

			assertTrue(resultingFiles.isEmpty());
			assertTrue(isTemporaryFolderEmpty());
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnailsAsCollection(Collection, Rename, int, int)}
		 * where,
		 * <p>
		 * 1) All parameters are correct
		 * a) The Collection is an empty Set.
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will complete successfully.
		 *
		 * @throws IOException
		 */
		@Test
		public void testCreateThumbnailCollections_NoErrors_EmptySet() throws IOException {
			/*
			 * The files to make thumbnails of -- nothing!
			 */
			Set<File> files = Collections.emptySet();

			Collection<File> resultingFiles =
					Thumbnailator.createThumbnailsAsCollection(
							files,
							Rename.PREFIX_DOT_THUMBNAIL,
							50,
							50
					);

			assertTrue(resultingFiles.isEmpty());
			assertTrue(isTemporaryFolderEmpty());
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnailsAsCollection(Collection, Rename, int, int)}
		 * where,
		 * <p>
		 * 1) All parameters are correct
		 * 2) All data can be processed correctly.
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will complete successfully.
		 *
		 * @throws IOException
		 */
		@Test
		public void testCreateThumbnailCollections_NoErrors() throws IOException {
			File sourceFile1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			);
			File sourceFile2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);

			List<File> files = Arrays.asList(sourceFile1, sourceFile2);

			Collection<File> resultingFiles =
					Thumbnailator.createThumbnailsAsCollection(
							files,
							Rename.PREFIX_DOT_THUMBNAIL,
							50,
							50
					);

			/*
			 * Perform post-execution checks.
			 */
			Iterator<File> iter = resultingFiles.iterator();

			BufferedImage img0 = ImageIO.read(iter.next());
			assertEquals(50, img0.getWidth());
			assertEquals(50, img0.getHeight());

			BufferedImage img1 = ImageIO.read(iter.next());
			assertEquals(50, img1.getWidth());
			assertEquals(50, img1.getHeight());

			assertTrue(!iter.hasNext());
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnailsAsCollection(Collection, Rename, int, int)}
		 * where,
		 * <p>
		 * 1) All parameters are correct
		 * 2) A file that was specified does not exist
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an IOException.
		 *
		 * @throws IOException
		 */
		@Test(expected = IOException.class)
		public void testCreateThumbnailCollections_ErrorDuringProcessing_FileNotFound() throws IOException {
			File sourceFile = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			);
			File nonExistentFile = new File(temporaryFolder.getRoot(), "doesntExist.jpg");

			List<File> files = Arrays.asList(sourceFile, nonExistentFile);

			Thumbnailator.createThumbnailsAsCollection(
					files,
					Rename.PREFIX_DOT_THUMBNAIL,
					50,
					50
			);

			fail();
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnailsAsCollection(Collection, Rename, int, int)}
		 * where,
		 * <p>
		 * 1) All parameters are correct
		 * 2) The thumbnail cannot be written. (unsupported format)
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an UnsupportedFormatException.
		 *
		 * @throws IOException
		 */
		@Test(expected = UnsupportedFormatException.class)
		public void testCreateThumbnailCollections_ErrorDuringProcessing_CantWriteThumbnail() throws IOException {
			File sourceFile1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			);
			File sourceFile2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.gif", temporaryFolder
			);

			List<File> files = Arrays.asList(sourceFile1, sourceFile2);

			// This will force a UnsupportedFormatException when trying to output
			// a thumbnail whose source was a gif file.
			Rename brokenRenamer = new Rename() {
				@Override
				public String apply(String name, ThumbnailParameter param) {
					if (name.endsWith(".gif")) {
						return "thumbnail." + name + ".foobar";
					}

					return "thumbnail." + name;
				}
			};

			Thumbnailator.createThumbnailsAsCollection(
					files,
					brokenRenamer,
					50,
					50
			);

			fail();
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnailsAsCollection(Collection, Rename, int, int)}
		 * where,
		 * <p>
		 * 1) All parameters are correct
		 * 2) All data can be processed correctly.
		 * 3) The Collection is a List of a class extending File.
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will complete successfully.
		 *
		 * @throws IOException
		 */
		@Test
		public void testCreateThumbnailCollections_NoErrors_CollectionExtendsFile() throws IOException {
			class File2 extends File {
				private static final long serialVersionUID = 1L;

				public File2(String pathname) {
					super(pathname);
				}
			}

			File sourceFile1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			);
			File sourceFile2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);

			/*
			 * The files to make thumbnails of.
			 */
			List<File2> files = Arrays.asList(
					new File2(sourceFile1.getAbsolutePath()),
					new File2(sourceFile2.getAbsolutePath())
			);

			Collection<File> resultingFiles =
					Thumbnailator.createThumbnailsAsCollection(
							files,
							Rename.PREFIX_DOT_THUMBNAIL,
							50,
							50
					);

			/*
			 * Perform post-execution checks.
			 */
			Iterator<File> iter = resultingFiles.iterator();

			BufferedImage img0 = ImageIO.read(iter.next());
			assertEquals(50, img0.getWidth());
			assertEquals(50, img0.getHeight());

			BufferedImage img1 = ImageIO.read(iter.next());
			assertEquals(50, img1.getWidth());
			assertEquals(50, img1.getHeight());

			assertTrue(!iter.hasNext());
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnails(Collection, Rename, int, int)}
		 * where,
		 * <p>
		 * 1) All parameters are correct
		 * a) The Collection is an empty List.
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will complete successfully.
		 *
		 * @throws IOException
		 */
		@Test
		public void testCreateThumbnails_NoErrors_EmptyList() throws IOException {
			/*
			 * The files to make thumbnails of -- nothing!
			 */
			List<File> files = Collections.emptyList();

			Thumbnailator.createThumbnails(
					files,
					Rename.PREFIX_DOT_THUMBNAIL,
					50,
					50
			);

			assertTrue(isTemporaryFolderEmpty());
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnails(Collection, Rename, int, int)}
		 * where,
		 * <p>
		 * 1) All parameters are correct
		 * a) The Collection is an empty Set.
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will complete successfully.
		 *
		 * @throws IOException
		 */
		@Test
		public void testCreateThumbnails_NoErrors_EmptySet() throws IOException {
			/*
			 * The files to make thumbnails of -- nothing!
			 */
			Set<File> files = Collections.emptySet();

			Thumbnailator.createThumbnails(
					files,
					Rename.PREFIX_DOT_THUMBNAIL,
					50,
					50
			);

			assertTrue(isTemporaryFolderEmpty());
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnails(Collection, Rename, int, int)}
		 * where,
		 * <p>
		 * 1) All parameters are correct
		 * 2) All data can be processed correctly.
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will complete successfully.
		 *
		 * @throws IOException
		 */
		@Test
		public void testCreateThumbnails_NoErrors() throws IOException {
			File sourceFile1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			);
			File sourceFile2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);

			List<File> files = Arrays.asList(sourceFile1, sourceFile2);

			Thumbnailator.createThumbnails(
					files,
					Rename.PREFIX_DOT_THUMBNAIL,
					50,
					50
			);

			/*
			 * Perform post-execution checks.
			 */
			BufferedImage img0 =
					ImageIO.read(new File(temporaryFolder.getRoot(), "thumbnail.grid.jpg"));

			assertEquals(50, img0.getWidth());
			assertEquals(50, img0.getHeight());

			BufferedImage img1 =
					ImageIO.read(new File(temporaryFolder.getRoot(), "thumbnail.grid.png"));

			assertEquals(50, img1.getWidth());
			assertEquals(50, img1.getHeight());
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnails(Collection, Rename, int, int)}
		 * where,
		 * <p>
		 * 1) All parameters are correct
		 * 2) A file that was specified does not exist
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an IOException.
		 *
		 * @throws IOException
		 */
		@Test(expected = IOException.class)
		public void testCreateThumbnails_ErrorDuringProcessing_FileNotFound() throws IOException {
			File sourceFile = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			);
			File nonExistentFile = new File(temporaryFolder.getRoot(), "doesntExist.jpg");

			List<File> files = Arrays.asList(sourceFile, nonExistentFile);

			Thumbnailator.createThumbnails(
					files,
					Rename.PREFIX_DOT_THUMBNAIL,
					50,
					50
			);

			fail();
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnails(Collection, Rename, int, int)}
		 * where,
		 * <p>
		 * 1) All parameters are correct
		 * 2) The thumbnail cannot be written. (unsupported format)
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an UnsupportedFormatException.
		 *
		 * @throws IOException
		 */
		@Test(expected = UnsupportedFormatException.class)
		public void testCreateThumbnails_ErrorDuringProcessing_CantWriteThumbnail() throws IOException {
			File sourceFile1 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.jpg", temporaryFolder
			);
			File sourceFile2 = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.gif", temporaryFolder
			);

			List<File> files = Arrays.asList(sourceFile1, sourceFile2);

			// This will force a UnsupportedFormatException when trying to output
			// a thumbnail whose source was a gif file.
			Rename brokenRenamer = new Rename() {
				@Override
				public String apply(String name, ThumbnailParameter param) {
					if (name.endsWith(".gif")) {
						return "thumbnail." + name + ".foobar";
					}

					return "thumbnail." + name;
				}
			};

			Thumbnailator.createThumbnails(
					files,
					brokenRenamer,
					50,
					50
			);

			fail();
		}

		@Test
		public void renameGivenThumbnailParameter_createThumbnails() throws IOException {
			// given
			Rename rename = mock(Rename.class);
			when(rename.apply(anyString(), any(ThumbnailParameter.class)))
					.thenReturn("thumbnail.grid.png");

			File f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);

			// when
			Thumbnailator.createThumbnails(
					Collections.singletonList(f), rename, 50, 50
			);

			// then
			ArgumentCaptor<ThumbnailParameter> ac =
					ArgumentCaptor.forClass(ThumbnailParameter.class);

			verify(rename).apply(eq(f.getName()), ac.capture());
			assertEquals(new Dimension(50, 50), ac.getValue().getSize());
		}

		@Test
		public void renameGivenThumbnailParameter_createThumbnailsAsCollection() throws IOException {
			// given
			Rename rename = mock(Rename.class);
			when(rename.apply(anyString(), any(ThumbnailParameter.class)))
					.thenReturn("thumbnail.grid.png");

			File f = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);

			// when
			Thumbnailator.createThumbnailsAsCollection(
					Collections.singletonList(f), rename, 50, 50
			);

			// then
			ArgumentCaptor<ThumbnailParameter> ac =
					ArgumentCaptor.forClass(ThumbnailParameter.class);

			verify(rename).apply(eq(f.getName()), ac.capture());
			assertEquals(new Dimension(50, 50), ac.getValue().getSize());
		}

		/**
		 * Test for
		 * {@link Thumbnailator#createThumbnail(File, File, int, int)}
		 * where,
		 * <p>
		 * 1) A filename that is invalid
		 * <p>
		 * Expected outcome is,
		 * <p>
		 * 1) Processing will stop with an IOException.
		 *
		 * @throws IOException
		 */
		@Test
		public void testCreateThumbnail_FFII_invalidOutputFile() throws IOException {
			/*
			 * Actual test
			 */
			File inputFile = TestUtils.copyResourceToTemporaryFile(
					"Thumbnailator/grid.png", temporaryFolder
			);
			File outputFile = new File(
					temporaryFolder.getRoot(), "@\\#?/^%*&/|!!$:#"
			);

			try {
				Thumbnailator.createThumbnail(inputFile, outputFile, 50, 50);
				fail();
			} catch (IOException e) {
				// An IOException is expected. Likely a FileNotFoundException.
			}
		}
	}

	@RunWith(Parameterized.class)
	public static class InvalidDimensionsTests {
		@Parameterized.Parameters(name = "width={0}, height={1}")
		public static Object[][] values() {
			return new Object[][] {
					new Object[] {-42, 42},
					new Object[] {42, -42},
					new Object[] {-42, -42}
			};
		}

		@Parameterized.Parameter
		public int width;

		@Parameterized.Parameter(1)
		public int height;

		@Test(expected = IllegalArgumentException.class)
		public void testCreateThumbnailCollections() throws IOException {
			List<File> files = Arrays.asList(
					new File("foo.png"),
					new File("bar.jpg")
			);

			Thumbnailator.createThumbnailsAsCollection(
					files,
					Rename.PREFIX_DOT_THUMBNAIL,
					width,
					height
			);

			fail();
		}

		@Test(expected = IllegalArgumentException.class)
		public void testCreateThumbnails() throws IOException {
			List<File> files = Arrays.asList(
					new File("foo.png"),
					new File("bar.jpg")
			);

			Thumbnailator.createThumbnails(
					files,
					Rename.PREFIX_DOT_THUMBNAIL,
					width,
					height
			);

			fail();
		}

		@Test(expected = IllegalArgumentException.class)
		public void testCreateThumbnail_IOII() throws IOException {
			byte[] bytes = makeImageData("jpg", 200, 200);
			InputStream is = new ByteArrayInputStream(bytes);
			ByteArrayOutputStream os = new ByteArrayOutputStream();

			Thumbnailator.createThumbnail(is, os, width, height);

			fail();
		}

		@Test(expected = IllegalArgumentException.class)
		public void testCreateThumbnail_FII() throws IOException {
			File inputFile = new File("foo.jpg");

			Thumbnailator.createThumbnail(inputFile, width, height);

			fail();
		}

		@Test(expected = IllegalArgumentException.class)
		public void testCreateThumbnail_BII() {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnailator.createThumbnail(img, width, height);

			fail();
		}

		@Test(expected = IllegalArgumentException.class)
		public void testCreateThumbnail_FFII() throws IOException {
			File inputFile = new File("foo.jpg");
			File outputFile = new File("bar.jpg");

			Thumbnailator.createThumbnail(inputFile, outputFile, width, height);

			fail();
		}

		@Test(expected = IllegalArgumentException.class)
		public void testCreateThumbnail_III() {
			BufferedImage img = new BufferedImageBuilder(200, 200).build();

			Thumbnailator.createThumbnail((Image) img, width, height);

			fail();
		}
	}

	/**
	 * Returns test image data as an array of {@code byte}s.
	 *
	 * @param format Image format.
	 * @param width  Image width.
	 * @param height Image height.
	 * @throws IOException When a problem occurs while making image data.
	 * @return A {@code byte[]} of image data.
	 */
	private static byte[] makeImageData(String format, int width, int height)
			throws IOException {
		BufferedImage img = new BufferedImageBuilder(width, height)
				.imageType("jpg".equals(format) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB)
				.build();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(img, format, baos);

		return baos.toByteArray();
	}
}
