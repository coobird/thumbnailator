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

package net.coobird.thumbnailator.tasks;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import net.coobird.thumbnailator.TestUtils;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.resizers.Resizers;

import net.coobird.thumbnailator.test.BufferedImageComparer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import javax.imageio.ImageIO;

public class FileThumbnailTaskTest {

	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Test(expected=NullPointerException.class)
	public void nullParameter() throws IOException {
		// given
		File inputFile = TestUtils.copyResourceToTemporaryFile(
				"Thumbnailator/grid.jpg",
				temporaryFolder
		);
		File outputFile = temporaryFolder.newFile("output.png");

		try {
			// when
			new FileThumbnailTask(null, inputFile, outputFile);
			fail();
		} catch (NullPointerException e) {
			// then
			assertEquals("The parameter is null.", e.getMessage());
			throw e;
		}
	}
	
	@Test
	public void testRead_CorrectUsage() throws IOException {
		ThumbnailParameter param = new ThumbnailParameter(
				new Dimension(50, 50),
				null,
				true,
				"jpg",
				ThumbnailParameter.DEFAULT_FORMAT_TYPE,
				ThumbnailParameter.DEFAULT_QUALITY,
				BufferedImage.TYPE_INT_ARGB,
				null,
				Resizers.PROGRESSIVE,
				true,
				true
		);
		
		File inputFile = TestUtils.copyResourceToTemporaryFile(
				"Thumbnailator/grid.jpg",
				temporaryFolder
		);
		File outputFile = temporaryFolder.newFile("output.png");

		FileThumbnailTask task =
			new FileThumbnailTask(param, inputFile, outputFile);
		
		task.read();
	}

	@Test
	public void testWrite() throws IOException {
		ThumbnailParameter param = new ThumbnailParameter(
				new Dimension(50, 50),
				null,
				true,
				"png",
				ThumbnailParameter.DEFAULT_FORMAT_TYPE,
				ThumbnailParameter.DEFAULT_QUALITY,
				BufferedImage.TYPE_INT_ARGB,
				null,
				Resizers.PROGRESSIVE,
				true,
				true
		);

		// When inputFile is read, then an exception should be thrown.
		// Lack of exception means no interaction with input.
		File inputFile = temporaryFolder.newFile("random-doesnt-exist");
		File outputFile = temporaryFolder.newFile("output.png");

		FileThumbnailTask task = new FileThumbnailTask(param, inputFile, outputFile);
		BufferedImage img = new BufferedImageBuilder(50, 50).build();

		task.write(img);

		BufferedImage outputImage = ImageIO.read(outputFile);
		assertTrue(BufferedImageComparer.isRGBSimilar(img, outputImage));
	}

	@Test
	public void testGetParam() throws IOException {
		ThumbnailParameter param = new ThumbnailParameter(
				new Dimension(50, 50),
				null,
				true,
				"jpg",
				ThumbnailParameter.DEFAULT_FORMAT_TYPE,
				ThumbnailParameter.DEFAULT_QUALITY,
				BufferedImage.TYPE_INT_ARGB,
				null,
				Resizers.PROGRESSIVE,
				true,
				true
		);

		// When inputFile is read, then an exception should be thrown.
		// Lack of exception means no interaction with input.
		File inputFile = temporaryFolder.newFile("random-doesnt-exist");
		File outputFile = new File(temporaryFolder.getRoot(), "shouldnt-exist");
		
		FileThumbnailTask task = new FileThumbnailTask(param, inputFile, outputFile);
		
		assertEquals(param, task.getParam());
		assertFalse(outputFile.exists());
	}
}
