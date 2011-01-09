package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.test.BufferedImageComparer;

import org.junit.Test;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;


public class OutputStreamImageSinkTest
{
	@Test
	public void validOutputStream()
	{
		// given
		OutputStream os = mock(OutputStream.class);
		
		// when
		OutputStreamImageSink sink = new OutputStreamImageSink(os);
		
		// then
		assertEquals(os, sink.getSink()); 
	}
	
	@Test(expected=NullPointerException.class)
	public void nullOutputStream()
	{
		// given
		OutputStream f = null;
		
		try
		{
			// when
			new OutputStreamImageSink(f);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("OutputStream cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void write_NullImage() throws IOException
	{
		// given
		OutputStream os = mock(OutputStream.class);

		BufferedImage img = null;
		
		OutputStreamImageSink sink = new OutputStreamImageSink(os);
		sink.setOutputFormatName("png");
		
		try
		{
			// when
			sink.write(img);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("Cannot write a null image.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public void write_ValidImage_SetOutputFormat_NotSet() throws IOException
	{
		// given
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		OutputStreamImageSink sink = new OutputStreamImageSink(os);
		
		try
		{
			// when
			sink.write(imgToWrite);
		}
		catch (IllegalStateException e)
		{
			// then
			assertEquals("Output format has not been set.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public void write_ValidImage_SetOutputFormat_OriginalFormat() throws IOException
	{
		// given
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		OutputStreamImageSink sink = new OutputStreamImageSink(os);
		sink.setOutputFormatName(ThumbnailParameter.ORIGINAL_FORMAT);
		
		try
		{
			// when
			sink.write(imgToWrite);
		}
		catch (IllegalStateException e)
		{
			// then
			assertEquals("Output format has not been set.", e.getMessage());
			throw e;
		}
	}
	
	@Test
	public void write_ValidImage_SetOutputFormat() throws IOException
	{
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
		
		String formatName = getFormatName(new ByteArrayInputStream(imageData));
		assertEquals("png", formatName);
	}
	
	@Test
	public void write_ValidImage_SetThumbnailParameter_BMP_QualityAndOutputFormatType_BothDefault() throws IOException
	{
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
		
		String formatName = getFormatName(new ByteArrayInputStream(imageData));
		assertEquals("bmp", formatName);
		
		verify(param, atLeastOnce()).getOutputQuality();
		verify(param, atLeastOnce()).getOutputFormatType();
	}
	
	@Test
	public void write_ValidImage_SetThumbnailParameter_BMP_QualityAndOutputFormatType_BothNonDefault() throws IOException
	{
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
		
		String formatName = getFormatName(new ByteArrayInputStream(imageData));
		assertEquals("bmp", formatName);
		
		verify(param, atLeastOnce()).getOutputQuality();
		verify(param, atLeastOnce()).getOutputFormatType();
	}
	
	@Test
	public void write_ValidImage_SetThumbnailParameter_BMP_OutputFormatType() throws IOException
	{
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
		
		String formatName = getFormatName(new ByteArrayInputStream(imageData));
		assertEquals("bmp", formatName);
		
		verify(param, atLeastOnce()).getOutputQuality();
	}
	
	/**
	 * Returns the format of an image which is read through the {@link InputStream}.
	 * 
	 * @param is			The {@link InputStream} to an image.
	 * @return				File format of the image.
	 * @throws IOException
	 */
	private static String getFormatName(InputStream is) throws IOException
	{
		return ImageIO.getImageReaders(
				ImageIO.createImageInputStream(is)
		).next().getFormatName();
	}
}
