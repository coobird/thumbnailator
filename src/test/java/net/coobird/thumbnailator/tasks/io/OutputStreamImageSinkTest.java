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

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;

import net.coobird.thumbnailator.TestUtils;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.test.BufferedImageComparer;

import org.junit.Test;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;


public class OutputStreamImageSinkTest {
	@Test
	public void validOutputStream() {
		// given
		OutputStream os = mock(OutputStream.class);
		
		// when
		OutputStreamImageSink sink = new OutputStreamImageSink(os);
		
		// then
		assertEquals(os, sink.getSink());
	}
	
	@Test(expected=NullPointerException.class)
	public void nullOutputStream() {
		// given
		OutputStream f = null;
		
		try {
			// when
			new OutputStreamImageSink(f);
		} catch (NullPointerException e) {
			// then
			assertEquals("OutputStream cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void write_NullImage() throws IOException {
		// given
		OutputStream os = mock(OutputStream.class);

		BufferedImage img = null;
		
		OutputStreamImageSink sink = new OutputStreamImageSink(os);
		sink.setOutputFormatName("png");
		
		try {
			// when
			sink.write(img);
		} catch (NullPointerException e) {
			// then
			assertEquals("Cannot write a null image.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public void write_ValidImage_SetOutputFormat_NotSet() throws IOException {
		// given
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		BufferedImage imgToWrite =
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		OutputStreamImageSink sink = new OutputStreamImageSink(os);
		
		try {
			// when
			sink.write(imgToWrite);
		} catch (IllegalStateException e) {
			// then
			assertEquals("Output format has not been set.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public void write_ValidImage_SetOutputFormat_OriginalFormat() throws IOException {
		// given
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		BufferedImage imgToWrite =
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		OutputStreamImageSink sink = new OutputStreamImageSink(os);
		sink.setOutputFormatName(ThumbnailParameter.ORIGINAL_FORMAT);
		
		try {
			// when
			sink.write(imgToWrite);
		} catch (IllegalStateException e) {
			// then
			assertEquals("Output format has not been set.", e.getMessage());
			throw e;
		}
	}
	
	@Test
	public void write_ValidImage_SetOutputFormat() throws IOException {
		// given
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		BufferedImage imgToWrite =
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		OutputStreamImageSink sink = new OutputStreamImageSink(os);
		
		// when
		sink.setOutputFormatName("png");
		sink.write(imgToWrite);
		
		// then
		assertEquals(os, sink.getSink());
		
		byte[] imageData = os.toByteArray();
		BufferedImage writtenImg = ImageIO.read(new ByteArrayInputStream(imageData));
		assertTrue(BufferedImageComparer.isRGBSimilar(imgToWrite, writtenImg));
		
		String formatName = TestUtils.getFormatName(new ByteArrayInputStream(imageData));
		assertEquals("png", formatName);
	}
	
	@Test
	public void write_ValidImage_SetThumbnailParameter_BMP_QualityAndOutputFormatType_BothDefault() throws IOException {
		// given
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		BufferedImage imgToWrite =
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		ThumbnailParameter param = mock(ThumbnailParameter.class);
		when(param.getOutputQuality()).thenReturn(ThumbnailParameter.DEFAULT_QUALITY);
		when(param.getOutputFormatType()).thenReturn(ThumbnailParameter.DEFAULT_FORMAT_TYPE);
		
		OutputStreamImageSink sink = new OutputStreamImageSink(os);
		sink.setThumbnailParameter(param);
		sink.setOutputFormatName("bmp");
		
		// when
		sink.write(imgToWrite);
		
		// then
		assertEquals(os, sink.getSink());
		
		byte[] imageData = os.toByteArray();
		BufferedImage writtenImg = ImageIO.read(new ByteArrayInputStream(imageData));
		assertTrue(BufferedImageComparer.isRGBSimilar(imgToWrite, writtenImg));
		
		String formatName = TestUtils.getFormatName(new ByteArrayInputStream(imageData));
		assertEquals("bmp", formatName);
		
		verify(param, atLeastOnce()).getOutputQuality();
		verify(param, atLeastOnce()).getOutputFormatType();
	}
	
	@Test
	public void write_ValidImage_SetThumbnailParameter_BMP_QualityAndOutputFormatType_BothNonDefault() throws IOException {
		// given
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		BufferedImage imgToWrite =
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		ThumbnailParameter param = mock(ThumbnailParameter.class);
		when(param.getOutputQuality()).thenReturn(0.5f);
		when(param.getOutputFormatType()).thenReturn("BI_BITFIELDS");
		
		OutputStreamImageSink sink = new OutputStreamImageSink(os);
		sink.setThumbnailParameter(param);
		sink.setOutputFormatName("bmp");
		
		// when
		sink.write(imgToWrite);
		
		// then
		assertEquals(os, sink.getSink());
		
		byte[] imageData = os.toByteArray();
		BufferedImage writtenImg = ImageIO.read(new ByteArrayInputStream(imageData));
		assertTrue(BufferedImageComparer.isRGBSimilar(imgToWrite, writtenImg));
		
		String formatName = TestUtils.getFormatName(new ByteArrayInputStream(imageData));
		assertEquals("bmp", formatName);
		
		verify(param, atLeastOnce()).getOutputQuality();
		verify(param, atLeastOnce()).getOutputFormatType();
	}
	
	@Test
	public void write_ValidImage_SetThumbnailParameter_BMP_OutputFormatType() throws IOException {
		// given
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		BufferedImage imgToWrite =
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		ThumbnailParameter param = mock(ThumbnailParameter.class);
		when(param.getOutputQuality()).thenReturn(ThumbnailParameter.DEFAULT_QUALITY);
		when(param.getOutputFormatType()).thenReturn("BI_BITFIELDS");
		
		OutputStreamImageSink sink = new OutputStreamImageSink(os);
		sink.setThumbnailParameter(param);
		sink.setOutputFormatName("bmp");
		
		// when
		sink.write(imgToWrite);
		
		// then
		assertEquals(os, sink.getSink());
		
		byte[] imageData = os.toByteArray();
		BufferedImage writtenImg = ImageIO.read(new ByteArrayInputStream(imageData));
		assertTrue(BufferedImageComparer.isRGBSimilar(imgToWrite, writtenImg));
		
		String formatName = TestUtils.getFormatName(new ByteArrayInputStream(imageData));
		assertEquals("bmp", formatName);
		
		verify(param, atLeastOnce()).getOutputQuality();
	}
	
	@Test
	public void write_ValidImage_WriterCantCompress() throws IOException {
		// given
		ImageWriteParam iwParam = mock(ImageWriteParam.class);
		ImageWriter writer = mock(ImageWriter.class);
		ImageWriterSpi spi = mock(ImageWriterSpi.class);
		
		when(iwParam.canWriteCompressed()).thenReturn(false);
		
		when(writer.getDefaultWriteParam()).thenReturn(iwParam);
		when(writer.getOriginatingProvider()).thenReturn(spi);
		
		when(spi.getFormatNames()).thenReturn(new String[] {"foo", "FOO"});
		when(spi.getFileSuffixes()).thenReturn(new String[] {"foo", "FOO"});
		when(spi.createWriterInstance()).thenReturn(writer);
		when(spi.createWriterInstance(anyObject())).thenReturn(writer);
		IIORegistry.getDefaultInstance().registerServiceProvider(spi);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		BufferedImage imgToWrite =
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		
		ThumbnailParameter param = mock(ThumbnailParameter.class);
		when(param.getOutputQuality()).thenReturn(0.8f);
		when(param.getOutputFormatType()).thenReturn(ThumbnailParameter.DEFAULT_FORMAT_TYPE);
		
		OutputStreamImageSink sink = new OutputStreamImageSink(os);
		sink.setThumbnailParameter(param);
		sink.setOutputFormatName("foo");
		
		// when
		sink.write(imgToWrite);
		
		// then
		verify(iwParam, never()).setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		verify(iwParam, never()).setCompressionType(anyString());
		verify(iwParam, never()).setCompressionQuality(anyFloat());
		
		// - check to see that parameters were not read, as this format doesn't
		// support compression.
		verify(param, never()).getOutputQuality();
		verify(param, never()).getOutputFormatType();
		
		// clean up
		IIORegistry.getDefaultInstance().deregisterServiceProvider(spi);
	}
	
	@Test
	public void write_ValidImage_WriterCanCompress_NoCompressionTypeFromWriter() throws IOException {
		// given
		ImageWriteParam iwParam = mock(ImageWriteParam.class);
		ImageWriter writer = mock(ImageWriter.class);
		ImageWriterSpi spi = mock(ImageWriterSpi.class);
		
		when(iwParam.canWriteCompressed()).thenReturn(true);
		when(iwParam.getCompressionTypes()).thenReturn(null);
		
		when(writer.getDefaultWriteParam()).thenReturn(iwParam);
		when(writer.getOriginatingProvider()).thenReturn(spi);
		
		when(spi.getFormatNames()).thenReturn(new String[] {"foo", "FOO"});
		when(spi.getFileSuffixes()).thenReturn(new String[] {"foo", "FOO"});
		when(spi.createWriterInstance()).thenReturn(writer);
		when(spi.createWriterInstance(anyObject())).thenReturn(writer);
		IIORegistry.getDefaultInstance().registerServiceProvider(spi);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		BufferedImage imgToWrite =
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		
		ThumbnailParameter param = mock(ThumbnailParameter.class);
		when(param.getOutputQuality()).thenReturn(0.8f);
		when(param.getOutputFormatType()).thenReturn(ThumbnailParameter.DEFAULT_FORMAT_TYPE);
		
		OutputStreamImageSink sink = new OutputStreamImageSink(os);
		sink.setThumbnailParameter(param);
		sink.setOutputFormatName("foo");
		
		// when
		sink.write(imgToWrite);
		
		// then
		verify(iwParam, atLeastOnce()).setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		verify(iwParam, never()).setCompressionType(anyString());
		verify(iwParam, atLeastOnce()).setCompressionQuality(0.8f);
		
		// - check to see that parameters was read
		verify(param, atLeastOnce()).getOutputQuality();
		verify(param, atLeastOnce()).getOutputFormatType();
		
		// clean up
		IIORegistry.getDefaultInstance().deregisterServiceProvider(spi);
	}
	
	@Test
	public void write_ValidImage_WriterCanCompress_HasCompressionTypeFromWriter() throws IOException {
		// given
		ImageWriteParam iwParam = mock(ImageWriteParam.class);
		ImageWriter writer = mock(ImageWriter.class);
		ImageWriterSpi spi = mock(ImageWriterSpi.class);
		
		when(iwParam.canWriteCompressed()).thenReturn(true);
		when(iwParam.getCompressionTypes()).thenReturn(new String[] {"FOOBAR"});
		
		when(writer.getDefaultWriteParam()).thenReturn(iwParam);
		when(writer.getOriginatingProvider()).thenReturn(spi);
		
		when(spi.getFormatNames()).thenReturn(new String[] {"foo", "FOO"});
		when(spi.getFileSuffixes()).thenReturn(new String[] {"foo", "FOO"});
		when(spi.createWriterInstance()).thenReturn(writer);
		when(spi.createWriterInstance(anyObject())).thenReturn(writer);
		IIORegistry.getDefaultInstance().registerServiceProvider(spi);
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		BufferedImage imgToWrite =
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		
		ThumbnailParameter param = mock(ThumbnailParameter.class);
		when(param.getOutputQuality()).thenReturn(0.8f);
		when(param.getOutputFormatType()).thenReturn(ThumbnailParameter.DEFAULT_FORMAT_TYPE);
		
		OutputStreamImageSink sink = new OutputStreamImageSink(os);
		sink.setThumbnailParameter(param);
		sink.setOutputFormatName("foo");
		
		// when
		sink.write(imgToWrite);
		
		// then
		verify(iwParam, atLeastOnce()).setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		verify(iwParam, atLeastOnce()).setCompressionType("FOOBAR");
		verify(iwParam, atLeastOnce()).setCompressionQuality(0.8f);
		
		// - check to see that parameters was read
		verify(param, atLeastOnce()).getOutputQuality();
		verify(param, atLeastOnce()).getOutputFormatType();
		
		// clean up
		IIORegistry.getDefaultInstance().deregisterServiceProvider(spi);
	}

	@Test
	public void write_DoesNotCloseOutputStream() throws IOException {
		// given
		OutputStream os = mock(OutputStream.class);

		BufferedImage imgToWrite =
				new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

		OutputStreamImageSink sink = new OutputStreamImageSink(os);
		sink.setOutputFormatName("jpeg");

		// when
		sink.write(imgToWrite);

		// then
		verify(os, never()).close();
	}
}
