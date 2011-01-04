package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.test.BufferedImageComparer;

import org.junit.Test;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;


public class FileImageSinkTest
{
	@Test
	public void validFilename_File()
	{
		// given
		File f = new File("test-resources/Thumbnailator/test.png");
		
		// when
		FileImageSink sink = new FileImageSink(f);
		
		// then
		assertEquals(f, sink.getSink()); 
	}
	
	@Test
	public void validFilename_String()
	{
		// given
		String f = "test-resources/Thumbnailator/test.png";
		
		// when
		FileImageSink sink = new FileImageSink(f);
		
		// then
		assertEquals(new File(f), sink.getSink()); 
	}
	
	@Test(expected=NullPointerException.class)
	public void nullFilename_File()
	{
		// given
		File f = null;
		
		try
		{
			// when
			new FileImageSink(f);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("File cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void nullFilename_String()
	{
		// given
		String f = null;
		
		try
		{
			// when
			new FileImageSink(f);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("File cannot be null.", e.getMessage());
			throw e;
		}
	}

	@Test(expected=NullPointerException.class)
	public void write_NullImage() throws IOException
	{
		// given
		File f = new File("test-resources/Thumbnailator/test.png");
		f.deleteOnExit();

		BufferedImage img = null;
		
		FileImageSink sink = new FileImageSink(f);
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
	
	@Test
	public void write_ValidImage() throws IOException
	{
		// given
		File outputFile = new File("test-resources/Thumbnailator/test.png");
		outputFile.deleteOnExit();
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		FileImageSink sink = new FileImageSink(outputFile);
		
		// when
		sink.write(imgToWrite);
		
		// then
		assertEquals(outputFile, sink.getSink());
		
		BufferedImage writtenImg = ImageIO.read(outputFile);
		assertTrue(BufferedImageComparer.isRGBSimilar(imgToWrite, writtenImg));
		
		String formatName = getFormatName(new FileInputStream(outputFile));
		assertEquals("png", formatName);
	}
	
	@Test
	public void write_ValidImage_SetOutputFormatWithSameAsExtension() throws IOException
	{
		// given
		File outputFile = new File("test-resources/Thumbnailator/test.png");
		outputFile.deleteOnExit();
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		FileImageSink sink = new FileImageSink(outputFile);
		
		// when
		sink.setOutputFormatName("png");
		sink.write(imgToWrite);
		
		// then
		assertEquals(outputFile, sink.getSink());
		
		BufferedImage writtenImg = ImageIO.read(outputFile);
		assertTrue(BufferedImageComparer.isRGBSimilar(imgToWrite, writtenImg));
		
		String formatName = getFormatName(new FileInputStream(outputFile));
		assertEquals("png", formatName);
	}
	
	@Test
	public void write_ValidImage_SetOutputFormatWithDifferentExtension() throws IOException
	{
		// given
		File outputFile = new File("test-resources/Thumbnailator/test.png");
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		FileImageSink sink = new FileImageSink(outputFile);
		
		// when
		sink.setOutputFormatName("JPEG");
		sink.write(imgToWrite);
		
		// then
		outputFile = new File("test-resources/Thumbnailator/test.png.JPEG");
		outputFile.deleteOnExit();
		
		assertEquals(outputFile.getAbsoluteFile(), sink.getSink().getAbsoluteFile());
		
		BufferedImage writtenImg = ImageIO.read(outputFile);
		assertTrue(BufferedImageComparer.isRGBSimilar(imgToWrite, writtenImg));
		
		String formatName = getFormatName(new FileInputStream(outputFile));
		assertEquals("JPEG", formatName);
	}
	
	@Test
	public void write_ValidImage_SetThumbnailParameter_BMP_QualityAndOutputFormatType_BothDefault() throws IOException
	{
		// given
		File outputFile = new File("test-resources/Thumbnailator/test.bmp");
		outputFile.deleteOnExit();
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		ThumbnailParameter param = mock(ThumbnailParameter.class);
		when(param.getOutputQuality()).thenReturn(ThumbnailParameter.DEFAULT_QUALITY);
		when(param.getOutputFormatType()).thenReturn(ThumbnailParameter.DEFAULT_FORMAT_TYPE);
		
		FileImageSink sink = new FileImageSink(outputFile);
		sink.setThumbnailParameter(param);
		
		// when
		sink.write(imgToWrite);
		
		// then
		assertEquals(outputFile, sink.getSink());
		
		BufferedImage writtenImg = ImageIO.read(outputFile);
		assertTrue(BufferedImageComparer.isRGBSimilar(imgToWrite, writtenImg));
		
		String formatName = getFormatName(new FileInputStream(outputFile));
		assertEquals("bmp", formatName);
		
		verify(param, atLeastOnce()).getOutputQuality();
		verify(param, atLeastOnce()).getOutputFormatType();
	}
	
	@Test
	public void write_ValidImage_SetThumbnailParameter_BMP_QualityAndOutputFormatType_BothNonDefault() throws IOException
	{
		// given
		File outputFile = new File("test-resources/Thumbnailator/test.bmp");
		outputFile.deleteOnExit();
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		ThumbnailParameter param = mock(ThumbnailParameter.class);
		when(param.getOutputQuality()).thenReturn(0.5f);
		when(param.getOutputFormatType()).thenReturn("BI_BITFIELDS");
		
		FileImageSink sink = new FileImageSink(outputFile);
		sink.setThumbnailParameter(param);
		
		// when
		sink.write(imgToWrite);
		
		// then
		assertEquals(outputFile, sink.getSink());
		
		BufferedImage writtenImg = ImageIO.read(outputFile);
		assertTrue(BufferedImageComparer.isRGBSimilar(imgToWrite, writtenImg));
		
		String formatName = getFormatName(new FileInputStream(outputFile));
		assertEquals("bmp", formatName);
		
		verify(param, atLeastOnce()).getOutputQuality();
		verify(param, atLeastOnce()).getOutputFormatType();
	}
	
	@Test
	public void write_ValidImage_SetThumbnailParameter_BMP_OutputFormatType() throws IOException
	{
		// given
		File outputFile = new File("test-resources/Thumbnailator/test.bmp");
		outputFile.deleteOnExit();
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		ThumbnailParameter param = mock(ThumbnailParameter.class);
		when(param.getOutputQuality()).thenReturn(ThumbnailParameter.DEFAULT_QUALITY);
		when(param.getOutputFormatType()).thenReturn("BI_BITFIELDS");
		
		FileImageSink sink = new FileImageSink(outputFile);
		sink.setThumbnailParameter(param);
		
		// when
		sink.write(imgToWrite);
		
		// then
		assertEquals(outputFile, sink.getSink());
		
		BufferedImage writtenImg = ImageIO.read(outputFile);
		assertTrue(BufferedImageComparer.isRGBSimilar(imgToWrite, writtenImg));
		
		String formatName = getFormatName(new FileInputStream(outputFile));
		assertEquals("bmp", formatName);
		
		verify(param, atLeastOnce()).getOutputFormatType();
	}
	
	@Test
	public void write_ValidImage_SetOutputFormatWithOriginalFormatConstant_FileExtension_png() throws IOException
	{
		// given
		File outputFile = new File("test-resources/Thumbnailator/test.png");
		outputFile.deleteOnExit();
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		FileImageSink sink = new FileImageSink(outputFile);
		
		// when
		sink.setOutputFormatName(ThumbnailParameter.ORIGINAL_FORMAT);
		sink.write(imgToWrite);
		
		// then
		assertEquals(outputFile, sink.getSink());
		
		BufferedImage writtenImg = ImageIO.read(outputFile);
		assertTrue(BufferedImageComparer.isRGBSimilar(imgToWrite, writtenImg));
		
		String formatName = getFormatName(new FileInputStream(outputFile));
		assertEquals("png", formatName);
	}
	
	@Test
	public void write_ValidImage_SetOutputFormatWithOriginalFormatConstant_FileExtension_bmp() throws IOException
	{
		// given
		File outputFile = new File("test-resources/Thumbnailator/test.bmp");
		outputFile.deleteOnExit();
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		FileImageSink sink = new FileImageSink(outputFile);
		
		// when
		sink.setOutputFormatName(ThumbnailParameter.ORIGINAL_FORMAT);
		sink.write(imgToWrite);
		
		// then
		assertEquals(outputFile, sink.getSink());
		
		BufferedImage writtenImg = ImageIO.read(outputFile);
		assertTrue(BufferedImageComparer.isRGBSimilar(imgToWrite, writtenImg));
		
		String formatName = getFormatName(new FileInputStream(outputFile));
		assertEquals("bmp", formatName);
	}
	
	@Test
	public void write_ValidImage_SetOutputFormatWithOriginalFormatConstant_FileExtension_jpg() throws IOException
	{
		// given
		File outputFile = new File("test-resources/Thumbnailator/test.jpg");
		outputFile.deleteOnExit();
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		FileImageSink sink = new FileImageSink(outputFile);
		
		// when
		sink.setOutputFormatName(ThumbnailParameter.ORIGINAL_FORMAT);
		sink.write(imgToWrite);
		
		// then
		assertEquals(outputFile, sink.getSink());
		
		BufferedImage writtenImg = ImageIO.read(outputFile);
		assertTrue(BufferedImageComparer.isRGBSimilar(imgToWrite, writtenImg));
		
		String formatName = getFormatName(new FileInputStream(outputFile));
		assertEquals("JPEG", formatName);
	}
	
	@Test
	public void write_ValidImage_SetOutputFormatWithOriginalFormatConstant_FileExtension_jpeg() throws IOException
	{
		// given
		File outputFile = new File("test-resources/Thumbnailator/test.jpeg");
		outputFile.deleteOnExit();
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		FileImageSink sink = new FileImageSink(outputFile);
		
		// when
		sink.setOutputFormatName(ThumbnailParameter.ORIGINAL_FORMAT);
		sink.write(imgToWrite);
		
		// then
		assertEquals(outputFile, sink.getSink());
		
		BufferedImage writtenImg = ImageIO.read(outputFile);
		assertTrue(BufferedImageComparer.isRGBSimilar(imgToWrite, writtenImg));
		
		String formatName = getFormatName(new FileInputStream(outputFile));
		assertEquals("JPEG", formatName);
	}
	
	@Test
	public void write_ValidImage_SetOutputFormatWithOriginalFormatConstant_FileExtension_Jpeg() throws IOException
	{
		// given
		File outputFile = new File("test-resources/Thumbnailator/test.Jpeg");
		outputFile.deleteOnExit();
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		FileImageSink sink = new FileImageSink(outputFile);
		
		// when
		sink.setOutputFormatName(ThumbnailParameter.ORIGINAL_FORMAT);
		sink.write(imgToWrite);
		
		// then
		assertEquals(outputFile, sink.getSink());
		
		BufferedImage writtenImg = ImageIO.read(outputFile);
		assertTrue(BufferedImageComparer.isRGBSimilar(imgToWrite, writtenImg));
		
		String formatName = getFormatName(new FileInputStream(outputFile));
		assertEquals("JPEG", formatName);
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
