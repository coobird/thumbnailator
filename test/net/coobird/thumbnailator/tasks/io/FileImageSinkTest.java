package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.spi.IIORegistry;
import javax.imageio.spi.ImageWriterSpi;

import net.coobird.thumbnailator.TestUtils;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import net.coobird.thumbnailator.test.BufferedImageComparer;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;


public class FileImageSinkTest
{
	/**
	 * The temporary directory to use when creating files to use for this test.
	 */
	private static final String TMPDIR = 
			"test-resources/tmp/FileImageSinkTest";
	
	@BeforeClass
	public static void makeTemporaryDirectory()
	{
		TestUtils.makeTemporaryDirectory(TMPDIR);
	}
	
	@AfterClass
	public static void deleteTemporaryDirectory()
	{
		TestUtils.deleteTemporaryDirectory(TMPDIR);
	}

	@Test
	public void validFilename_File()
	{
		// given
		File f = new File(TMPDIR, "test.png");
		
		// when
		FileImageSink sink = new FileImageSink(f);
		
		// then
		assertEquals(f, sink.getSink()); 
	}
	
	@Test
	public void validFilename_String()
	{
		// given
		String f = TMPDIR + "/test.png";
		
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
		File f = new File(TMPDIR, "test.png");
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
		File outputFile = new File(TMPDIR, "test.png");
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
		File outputFile = new File(TMPDIR, "test.png");
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
		File outputFile = new File(TMPDIR, "test.png");
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		FileImageSink sink = new FileImageSink(outputFile);
		
		// when
		sink.setOutputFormatName("JPEG");
		sink.write(imgToWrite);
		
		// then
		outputFile = new File(TMPDIR, "test.png.JPEG");
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
		File outputFile = new File(TMPDIR, "test");
		
		BufferedImage imgToWrite = 
			new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		FileImageSink sink = new FileImageSink(outputFile);
		
		// when
		sink.setOutputFormatName("JPEG");
		sink.write(imgToWrite);
		
		// then
		outputFile = new File(TMPDIR, "test.JPEG");
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
		File outputFile = new File(TMPDIR, "test.foo");
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
		File outputFile = new File(TMPDIR, "test.foo");
		File actualOutputFile = new File(TMPDIR, "test.foo.png");
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
		
		File outputFile = new File(TMPDIR, "test.foo");
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
		
		File outputFile = new File(TMPDIR, "test.foo");
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
		
		File outputFile = new File(TMPDIR, "test.foo");
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
		File outputFile = new File(TMPDIR, "test.bmp");
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
		File outputFile = new File(TMPDIR, "test.bmp");
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
		File outputFile = new File(TMPDIR, "test.bmp");
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
		File outputFile = new File(TMPDIR, "test.png");
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
		File outputFile = new File(TMPDIR, "test.bmp");
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
		File outputFile = new File(TMPDIR, "test.jpg");
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
		File outputFile = new File(TMPDIR, "test.jpeg");
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
		File outputFile = new File(TMPDIR, "test.Jpeg");
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
		File outputFile = new File(TMPDIR, "test");
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
	public void write_NoExtentionSpecified() throws IOException
	{
		// set up
		File f = new File(TMPDIR, "tmp-" + Math.abs(new Random().nextLong()));
		
		// given
		FileImageSink sink = new FileImageSink(f);
		
		// when
		try
		{
			sink.write(new BufferedImageBuilder(100, 100).build());
			fail();
		}
		catch (UnsupportedFormatException e)
		{
			// then
		}
	}
	
	@Test
	public void write_SpecifiedExtensionIsPNG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "PNG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("png", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIspng() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "png");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("png", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPng() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Png");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("png", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPNG_SpecifiedOutputFormatIspng() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "PNG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("png");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("png", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIspng_SpecifiedOutputFormatIspng() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "png");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("png");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("png", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPng_SpecifiedOutputFormatIspng() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Png");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("png");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("png", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPNG_SpecifiedOutputFormatIsPNG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "PNG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("PNG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("png", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIspng_SpecifiedOutputFormatIsPNG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "png");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("PNG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("png", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPng_SpecifiedOutputFormatIsPNG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Png");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("PNG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("png", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPNG_SpecifiedOutputFormatIsPng() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "PNG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Png");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("png", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIspng_SpecifiedOutputFormatIsPng() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "png");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Png");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("png", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPng_SpecifiedOutputFormatIsPng() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Png");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Png");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("png", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPNG_SpecifiedOutputFormatIsjpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "PNG");
		File expectedFile = new File(f.getAbsolutePath() + ".jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("jpg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(expectedFile, sink.getSink());
		assertTrue(expectedFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(expectedFile)));
	}
	
	@Test
	public void write_SpecifiedExtensionIspng_SpecifiedOutputFormatIsjpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "png");
		File expectedFile = new File(f.getAbsolutePath() + ".jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("jpg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(expectedFile, sink.getSink());
		assertTrue(expectedFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(expectedFile)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPng_SpecifiedOutputFormatIsjpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Png");
		File expectedFile = new File(f.getAbsolutePath() + ".jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("jpg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(expectedFile, sink.getSink());
		assertTrue(expectedFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(expectedFile)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPNG_SpecifiedOutputFormatIsjpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "PNG");
		File expectedFile = new File(f.getAbsolutePath() + ".jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("jpeg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(expectedFile, sink.getSink());
		assertTrue(expectedFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(expectedFile)));
	}
	
	@Test
	public void write_SpecifiedExtensionIspng_SpecifiedOutputFormatIsjpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "png");
		File expectedFile = new File(f.getAbsolutePath() + ".jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("jpeg");

		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(expectedFile, sink.getSink());
		assertTrue(expectedFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(expectedFile)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPng_SpecifiedOutputFormatIsjpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Png");
		File expectedFile = new File(f.getAbsolutePath() + ".jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("jpeg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(expectedFile, sink.getSink());
		assertTrue(expectedFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(expectedFile)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPNG_SpecifiedOutputFormatIsJPG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "PNG");
		File expectedFile = new File(f.getAbsolutePath() + ".JPG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("JPG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(expectedFile, sink.getSink());
		assertTrue(expectedFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(expectedFile)));
	}
	
	@Test
	public void write_SpecifiedExtensionIspng_SpecifiedOutputFormatIsJPG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "png");
		File expectedFile = new File(f.getAbsolutePath() + ".JPG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("JPG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(expectedFile, sink.getSink());
		assertTrue(expectedFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(expectedFile)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPng_SpecifiedOutputFormatIsJPG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Png");
		File expectedFile = new File(f.getAbsolutePath() + ".JPG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("JPG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(expectedFile, sink.getSink());
		assertTrue(expectedFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(expectedFile)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPNG_SpecifiedOutputFormatIsJPEG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "PNG");
		File expectedFile = new File(f.getAbsolutePath() + ".JPEG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("JPEG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(expectedFile, sink.getSink());
		assertTrue(expectedFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(expectedFile)));
	}
	
	@Test
	public void write_SpecifiedExtensionIspng_SpecifiedOutputFormatIsJPEG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "png");
		File expectedFile = new File(f.getAbsolutePath() + ".JPEG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("JPEG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(expectedFile, sink.getSink());
		assertTrue(expectedFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(expectedFile)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPng_SpecifiedOutputFormatIsJPEG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Png");
		File expectedFile = new File(f.getAbsolutePath() + ".JPEG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("JPEG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(expectedFile, sink.getSink());
		assertTrue(expectedFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(expectedFile)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPNG_SpecifiedOutputFormatIsJpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "PNG");
		File expectedFile = new File(f.getAbsolutePath() + ".Jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Jpg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(expectedFile, sink.getSink());
		assertTrue(expectedFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(expectedFile)));
	}
	
	@Test
	public void write_SpecifiedExtensionIspng_SpecifiedOutputFormatIsJpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "png");
		File expectedFile = new File(f.getAbsolutePath() + ".Jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Jpg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(expectedFile, sink.getSink());
		assertTrue(expectedFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(expectedFile)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPng_SpecifiedOutputFormatIsJpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Png");
		File expectedFile = new File(f.getAbsolutePath() + ".Jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Jpg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(expectedFile, sink.getSink());
		assertTrue(expectedFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(expectedFile)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPNG_SpecifiedOutputFormatIsJpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "PNG");
		File expectedFile = new File(f.getAbsolutePath() + ".Jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Jpeg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(expectedFile, sink.getSink());
		assertTrue(expectedFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(expectedFile)));
	}
	
	@Test
	public void write_SpecifiedExtensionIspng_SpecifiedOutputFormatIsJpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "png");
		File expectedFile = new File(f.getAbsolutePath() + ".Jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Jpeg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(expectedFile, sink.getSink());
		assertTrue(expectedFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(expectedFile)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsPng_SpecifiedOutputFormatIsJpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Png");
		File expectedFile = new File(f.getAbsolutePath() + ".Jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Jpeg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(expectedFile, sink.getSink());
		assertTrue(expectedFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(expectedFile)));
	}

	@Test
	public void write_SpecifiedExtensionIsJPG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJPEG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsjpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsjpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}

	@Test
	public void write_SpecifiedExtensionIsJPG_SpecifiedOutputFormatIsjpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("jpg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJPEG_SpecifiedOutputFormatIsjpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("jpg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsjpg_SpecifiedOutputFormatIsjpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("jpg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsjpeg_SpecifiedOutputFormatIsjpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("jpg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJpg_SpecifiedOutputFormatIsjpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("jpg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJpeg_SpecifiedOutputFormatIsjpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("jpg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJPG_SpecifiedOutputFormatIsjpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("jpeg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJPEG_SpecifiedOutputFormatIsjpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("jpeg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsjpg_SpecifiedOutputFormatIsjpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("jpeg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsjpeg_SpecifiedOutputFormatIsjpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("jpeg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJpg_SpecifiedOutputFormatIsjpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("jpeg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJpeg_SpecifiedOutputFormatIsjpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("jpeg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJPG_SpecifiedOutputFormatIsJPG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("JPG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJPEG_SpecifiedOutputFormatIsJPG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("JPG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsjpg_SpecifiedOutputFormatIsJPG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("JPG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsjpeg_SpecifiedOutputFormatIsJPG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("JPG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJpg_SpecifiedOutputFormatIsJPG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("JPG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJpeg_SpecifiedOutputFormatIsJPG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("JPG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJPG_SpecifiedOutputFormatIsJPEG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("JPEG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJPEG_SpecifiedOutputFormatIsJPEG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("JPEG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsjpg_SpecifiedOutputFormatIsJPEG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("JPEG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsjpeg_SpecifiedOutputFormatIsJPEG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("JPEG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJpg_SpecifiedOutputFormatIsJPEG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("JPEG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJpeg_SpecifiedOutputFormatIsJPEG() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("JPEG");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJPG_SpecifiedOutputFormatIsJpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Jpg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJPEG_SpecifiedOutputFormatIsJpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Jpg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsjpg_SpecifiedOutputFormatIsJpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Jpg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsjpeg_SpecifiedOutputFormatIsJpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Jpg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJpg_SpecifiedOutputFormatIsJpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Jpg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJpeg_SpecifiedOutputFormatIsJpg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Jpg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJPG_SpecifiedOutputFormatIsJpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Jpeg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJPEG_SpecifiedOutputFormatIsJpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Jpeg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsjpg_SpecifiedOutputFormatIsJpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Jpeg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsjpeg_SpecifiedOutputFormatIsJpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Jpeg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJpg_SpecifiedOutputFormatIsJpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Jpeg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void write_SpecifiedExtensionIsJpeg_SpecifiedOutputFormatIsJpeg() throws IOException
	{
		// set up
		File f = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// given
		FileImageSink sink = new FileImageSink(f);
		sink.setOutputFormatName("Jpeg");
		
		// when
		sink.write(new BufferedImageBuilder(100, 100).build());
		
		// then
		assertEquals(f, sink.getSink());
		assertTrue(f.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(f)));
	}
	
	@Test
	public void constructorFile_write_allowOverwriteTrue() throws IOException
	{
		// set up
		File sourceFile = new File("test-resources/Thumbnailator/grid.png");
		File f = new File(TMPDIR, "tmp-grid.png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, f);
		
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
		File f = new File(TMPDIR, "tmp-grid.png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, f);
		
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
		File f = new File(TMPDIR, "tmp-grid.png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, f);
		
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
		File f = new File(TMPDIR, "tmp-grid.png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, f);
		
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
	
	@Test
	public void preferredOutputFormatName_FileIsjpg()
	{
		// given
		File f = new File(TMPDIR, "tmp.jpg");
		
		// when
		FileImageSink sink = new FileImageSink(f);
		
		// then
		assertEquals("JPEG", sink.preferredOutputFormatName());
	}
	
	@Test
	public void preferredOutputFormatName_FileIsjpeg()
	{
		// given
		File f = new File(TMPDIR, "tmp.jpeg");
		
		// when
		FileImageSink sink = new FileImageSink(f);
		
		// then
		assertEquals("JPEG", sink.preferredOutputFormatName());
	}
	
	@Test
	public void preferredOutputFormatName_FileIsJpg()
	{
		// given
		File f = new File(TMPDIR, "tmp.Jpg");
		
		// when
		FileImageSink sink = new FileImageSink(f);
		
		// then
		assertEquals("JPEG", sink.preferredOutputFormatName());
	}
	
	@Test
	public void preferredOutputFormatName_FileIsJpeg()
	{
		// given
		File f = new File(TMPDIR, "tmp.Jpeg");
		
		// when
		FileImageSink sink = new FileImageSink(f);
		
		// then
		assertEquals("JPEG", sink.preferredOutputFormatName());
	}
	
	@Test
	public void preferredOutputFormatName_FileIsJPG()
	{
		// given
		File f = new File(TMPDIR, "tmp.JPG");
		
		// when
		FileImageSink sink = new FileImageSink(f);
		
		// then
		assertEquals("JPEG", sink.preferredOutputFormatName());
	}
	
	@Test
	public void preferredOutputFormatName_FileIsJPEG()
	{
		// given
		File f = new File(TMPDIR, "tmp.JPEG");
		
		// when
		FileImageSink sink = new FileImageSink(f);
		
		// then
		assertEquals("JPEG", sink.preferredOutputFormatName());
	}
	
	@Test
	public void preferredOutputFormatName_FileIspng()
	{
		// given
		File f = new File(TMPDIR, "tmp.png");
		
		// when
		FileImageSink sink = new FileImageSink(f);
		
		// then
		assertEquals("png", sink.preferredOutputFormatName());
	}
	
	@Test
	public void preferredOutputFormatName_FileIsPng()
	{
		// given
		File f = new File(TMPDIR, "tmp.Png");
		
		// when
		FileImageSink sink = new FileImageSink(f);
		
		// then
		assertEquals("png", sink.preferredOutputFormatName());
	}
	
	@Test
	public void preferredOutputFormatName_FileIsPNG()
	{
		// given
		File f = new File(TMPDIR, "tmp.PNG");
		
		// when
		FileImageSink sink = new FileImageSink(f);
		
		// then
		assertEquals("png", sink.preferredOutputFormatName());
	}
}