package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;

import net.coobird.thumbnailator.TestUtils;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
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
		
		String formatName = TestUtils.getFormatName(new FileInputStream(outputFile));
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
		
		String formatName = TestUtils.getFormatName(new FileInputStream(outputFile));
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
		
		String formatName = TestUtils.getFormatName(new FileInputStream(outputFile));
		assertEquals("JPEG", formatName);
	}
	
	@Test
	public void write_ValidImage_SetOutputFormat_OutputFileHasNoExtension() throws IOException
	{
		// given
		File outputFile = new File("test-resources/Thumbnailator/test");
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		FileImageSink sink = new FileImageSink(outputFile);
		
		// when
		sink.setOutputFormatName("JPEG");
		sink.write(imgToWrite);
		
		// then
		outputFile = new File("test-resources/Thumbnailator/test.JPEG");
		outputFile.deleteOnExit();
		
		assertEquals(outputFile.getAbsoluteFile(), sink.getSink().getAbsoluteFile());
		
		BufferedImage writtenImg = ImageIO.read(outputFile);
		assertTrue(BufferedImageComparer.isRGBSimilar(imgToWrite, writtenImg));
		
		String formatName = TestUtils.getFormatName(new FileInputStream(outputFile));
		assertEquals("JPEG", formatName);
	}
	
	@Test
	public void write_ValidImage_InvalidFileExtension() throws IOException
	{
		// given
		File outputFile = new File("test-resources/Thumbnailator/test.foo");
		outputFile.deleteOnExit();
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		FileImageSink sink = new FileImageSink(outputFile);
		
		// when
		try
		{
			sink.write(imgToWrite);
			fail();
		}
		catch (UnsupportedFormatException e)
		{
			// then
		}
	}
	
	@Test
	public void write_ValidImage_InvalidFileExtension_OutputFormatSetToValidFormat() throws IOException
	{
		// given
		File outputFile = new File("test-resources/Thumbnailator/test.foo");
		File actualOutputFile = new File("test-resources/Thumbnailator/test.foo.png");
		actualOutputFile.deleteOnExit();
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		FileImageSink sink = new FileImageSink(outputFile);
		sink.setOutputFormatName("png");
		
		// when
		sink.write(imgToWrite);
		
		// then
		assertEquals(actualOutputFile.getCanonicalFile(), sink.getSink().getCanonicalFile());
		
		BufferedImage writtenImg = ImageIO.read(actualOutputFile);
		assertTrue(BufferedImageComparer.isRGBSimilar(imgToWrite, writtenImg));
		
		String formatName = TestUtils.getFormatName(new FileInputStream(actualOutputFile));
		assertEquals("png", formatName);
	}
	
	@Test
	public void write_ValidImage_WriterCantCompress() throws IOException
	{
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
		
		File outputFile = new File("test-resources/Thumbnailator/test.foo");
		outputFile.deleteOnExit();
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		
		ThumbnailParameter param = mock(ThumbnailParameter.class);
		when(param.getOutputQuality()).thenReturn(0.8f);
		when(param.getOutputFormatType()).thenReturn(ThumbnailParameter.DEFAULT_FORMAT_TYPE);
		
		FileImageSink sink = new FileImageSink(outputFile);
		sink.setThumbnailParameter(param);
		
		// when
		sink.write(imgToWrite);
		
		// then
		assertEquals(outputFile, sink.getSink());

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
	public void write_ValidImage_WriterCanCompress_NoCompressionTypeFromWriter() throws IOException
	{
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
		
		File outputFile = new File("test-resources/Thumbnailator/test.foo");
		outputFile.deleteOnExit();
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		
		ThumbnailParameter param = mock(ThumbnailParameter.class);
		when(param.getOutputQuality()).thenReturn(0.8f);
		when(param.getOutputFormatType()).thenReturn(ThumbnailParameter.DEFAULT_FORMAT_TYPE);
		
		FileImageSink sink = new FileImageSink(outputFile);
		sink.setThumbnailParameter(param);
		
		// when
		sink.write(imgToWrite);
		
		// then
		assertEquals(outputFile, sink.getSink());
		
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
	public void write_ValidImage_WriterCanCompress_HasCompressionTypeFromWriter() throws IOException
	{
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
		
		File outputFile = new File("test-resources/Thumbnailator/test.foo");
		outputFile.deleteOnExit();
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		
		ThumbnailParameter param = mock(ThumbnailParameter.class);
		when(param.getOutputQuality()).thenReturn(0.8f);
		when(param.getOutputFormatType()).thenReturn(ThumbnailParameter.DEFAULT_FORMAT_TYPE);
		
		FileImageSink sink = new FileImageSink(outputFile);
		sink.setThumbnailParameter(param);
		
		// when
		sink.write(imgToWrite);
		
		// then
		assertEquals(outputFile, sink.getSink());
		
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
		
		String formatName = TestUtils.getFormatName(new FileInputStream(outputFile));
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
		
		String formatName = TestUtils.getFormatName(new FileInputStream(outputFile));
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
		
		String formatName = TestUtils.getFormatName(new FileInputStream(outputFile));
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
		
		String formatName = TestUtils.getFormatName(new FileInputStream(outputFile));
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
		
		String formatName = TestUtils.getFormatName(new FileInputStream(outputFile));
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
		
		String formatName = TestUtils.getFormatName(new FileInputStream(outputFile));
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
		
		String formatName = TestUtils.getFormatName(new FileInputStream(outputFile));
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
		
		String formatName = TestUtils.getFormatName(new FileInputStream(outputFile));
		assertEquals("JPEG", formatName);
	}
	
	@Test(expected=UnsupportedFormatException.class)
	public void write_ValidImage_SetOutputFormatWithOriginalFormatConstant_NoFileExtension() throws IOException
	{
		// given
		File outputFile = new File("test-resources/Thumbnailator/test");
		outputFile.deleteOnExit();
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		FileImageSink sink = new FileImageSink(outputFile);
		
		try
		{
			// when
			sink.setOutputFormatName(ThumbnailParameter.ORIGINAL_FORMAT);
			sink.write(imgToWrite);
		}
		catch (UnsupportedFormatException e)
		{
			// then
			assertEquals("Could not determine output format.", e.getMessage());
			throw e;
		}
	}
	
	@Test
	public void constructorFile_write_allowOverwriteTrue() throws IOException
	{
		// set up
		File sourceFile = new File("test-resources/Thumbnailator/grid.png");
		File f = new File("test-resources/Thumbnailator/tmp-grid.png");
		
		// copy the image to a temporary file.
		FileInputStream fis = new FileInputStream(sourceFile);
		FileOutputStream fos = new FileOutputStream(f);
		fis.getChannel().transferTo(0, sourceFile.length(), fos.getChannel());
		fis.close();
		fos.close();
		
		// given
		FileImageSink sink = new FileImageSink(f, true);
		
		// when
		sink.write(ImageIO.read(f));
		
		// then
		assertTrue(f.exists());
		
		// clean ups
		f.delete();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorFile_write_allowOverwriteFalse() throws IOException
	{
		// set up
		File sourceFile = new File("test-resources/Thumbnailator/grid.png");
		File f = new File("test-resources/Thumbnailator/tmp-grid.png");
		
		// copy the image to a temporary file.
		FileInputStream fis = new FileInputStream(sourceFile);
		FileOutputStream fos = new FileOutputStream(f);
		fis.getChannel().transferTo(0, sourceFile.length(), fos.getChannel());
		fis.close();
		fos.close();
		
		// given
		FileImageSink sink = new FileImageSink(f, false);
		
		// when
		try
		{
			sink.write(ImageIO.read(f));
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("The destination file exists.", e.getMessage());
			throw e;
		}
		
		// clean ups
		f.delete();
	}
	
	@Test
	public void constructorString_write_allowOverwriteTrue() throws IOException
	{
		// set up
		File sourceFile = new File("test-resources/Thumbnailator/grid.png");
		File f = new File("test-resources/Thumbnailator/tmp-grid.png");
		
		// copy the image to a temporary file.
		FileInputStream fis = new FileInputStream(sourceFile);
		FileOutputStream fos = new FileOutputStream(f);
		fis.getChannel().transferTo(0, sourceFile.length(), fos.getChannel());
		fis.close();
		fos.close();
		
		// given
		FileImageSink sink = new FileImageSink(f.getAbsolutePath(), true);
		
		// when
		sink.write(ImageIO.read(f));
		
		// then
		assertTrue(f.exists());
		
		// clean ups
		f.delete();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorString_write_allowOverwriteFalse() throws IOException
	{
		// set up
		File sourceFile = new File("test-resources/Thumbnailator/grid.png");
		File f = new File("test-resources/Thumbnailator/tmp-grid.png");
		
		// copy the image to a temporary file.
		FileInputStream fis = new FileInputStream(sourceFile);
		FileOutputStream fos = new FileOutputStream(f);
		fis.getChannel().transferTo(0, sourceFile.length(), fos.getChannel());
		fis.close();
		fos.close();
		
		// given
		FileImageSink sink = new FileImageSink(f.getAbsolutePath(), false);
		
		// when
		try
		{
			sink.write(ImageIO.read(f));
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("The destination file exists.", e.getMessage());
			throw e;
		}
		
		// clean ups
		f.delete();
	}
}