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

package net.coobird.thumbnailator.tasks.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.builders.ThumbnailParameterBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests to see whether memory conservation code is triggered when the
 * {@code thumbnailator.conserveMemoryWorkaround} system property is set. 
 * <p>
 * These tests may not necessarily be successful, as the workaround is only
 * triggered under conditions where the JVM memory is deemed to be low, which
 * will depend upon the environment in which the tests are run.
 */
public class Issue69InputStreamImageSourceTest {
	
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	public static byte[] sourceByteArray;
	public static int SIZE = 8000;
	
	static {
		BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		g.setPaint(new GradientPaint(0, 0, Color.blue, SIZE, SIZE, Color.red));
		g.dispose();
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.createImageOutputStream(baos);
			ImageIO.write(img, "jpg", baos);
			baos.close();
			sourceByteArray = baos.toByteArray();
		} catch (IOException e) {
			fail();
		}
	}
	
	@Before @After
	public void clearSystemProperty() {
		System.clearProperty("thumbnailator.conserveMemoryWorkaround");
	}
	
	@Test
	public void fromInputStreamBySizeWorkaroundDisabled() throws IOException {
		// given
		// The "thumbnailator.conserveMemoryWorkaround" system property is not set.

		ThumbnailParameter param = new ThumbnailParameterBuilder().size(200, 200).build();
		InputStreamImageSource source = new InputStreamImageSource(new ByteArrayInputStream(sourceByteArray));
		source.setThumbnailParameter(param);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertEquals(SIZE, img.getWidth());
		assertEquals(SIZE, img.getHeight());
	}
	
	@Test
	public void fromInputStreamBySizeWorkaroundEnabled() throws IOException {
		// given
		System.setProperty("thumbnailator.conserveMemoryWorkaround", "true");
		
		ThumbnailParameter param = new ThumbnailParameterBuilder().size(200, 200).build();
		InputStreamImageSource source = new InputStreamImageSource(new ByteArrayInputStream(sourceByteArray));
		source.setThumbnailParameter(param);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertTrue(img.getWidth() < SIZE);
		assertTrue(img.getWidth() >= 600);
		assertTrue(img.getHeight() < SIZE);
		assertTrue(img.getHeight() >= 600);
	}
	
	@Test
	public void fromInputStreamByScaleWorkaroundDisabled() throws IOException {
		// given
		// The "thumbnailator.conserveMemoryWorkaround" system property is not set.
		
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(0.1).build();
		InputStreamImageSource source = new InputStreamImageSource(new ByteArrayInputStream(sourceByteArray));
		source.setThumbnailParameter(param);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertEquals(SIZE, img.getWidth());
		assertEquals(SIZE, img.getHeight());
	}
	
	@Test
	public void fromInputStreamByScaleWorkaroundEnabled() throws IOException {
		// given
		System.setProperty("thumbnailator.conserveMemoryWorkaround", "true");
		
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(0.1).build();
		InputStreamImageSource source = new InputStreamImageSource(new ByteArrayInputStream(sourceByteArray));
		source.setThumbnailParameter(param);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertTrue(img.getWidth() < SIZE);
		assertTrue(img.getWidth() >= 600);
		assertTrue(img.getHeight() < SIZE);
		assertTrue(img.getHeight() >= 600);
	}
}
