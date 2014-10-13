package net.coobird.thumbnailator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.builders.ThumbnailParameterBuilder;
import net.coobird.thumbnailator.name.ConsecutivelyNumberedFilenames;
import net.coobird.thumbnailator.name.Rename;
import net.coobird.thumbnailator.test.BufferedImageAssert;
import net.coobird.thumbnailator.test.BufferedImageComparer;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ThumbnailsBuilderInputOutputTest
{
	/**
	 * The temporary directory to use when creating files to use for this test.
	 */
	private static final String TMPDIR =
			"src/test/resources/tmp/ThumbnailsBuilderInputOutputTest";
	
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
	public void of_BufferedImage_toFile_File_NoOutputFormatSpecified() throws IOException
	{
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
	public void of_BufferedImage_toFile_String_NoOutputFormatSpecified() throws IOException
	{
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
	 * <li>toFiles(Iterable<File>)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Processing completes successfully. Image format is determined
	 * by the extension of the file.</li>
	 * </ol>
	 * @throws IOException
	 */	
	@Test
	public void of_BufferedImage_toFiles_Iterable_NoOutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		// when
		Thumbnails.of(img)
			.size(50, 50)
			.toFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		File outFile = new File("src/test/resources/Thumbnailator/temp-0.png");
		outFile.deleteOnExit();
		
		assertEquals("png", TestUtils.getFormatName(new FileInputStream(outFile)));
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile);
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
	}	

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(BufferedImage)</li>
	 * <li>asFiles(Iterable<File>)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Processing completes successfully. Image format is determined
	 * by the extension of the file.</li>
	 * </ol>
	 * @throws IOException
	 */	
	@Test
	public void of_BufferedImage_asFiles_Iterable_NoOutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		// when
		List<File> thumbnails = Thumbnails.of(img)
			.size(50, 50)
			.asFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		assertEquals(1, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
		assertEquals("png", TestUtils.getFormatName(new FileInputStream(thumbnails.get(0))));
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		
		// clean up
		thumbnails.get(0).deleteOnExit();
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
	public void of_BufferedImage_asBufferedImage_NoOutputFormatSpecified() throws IOException
	{
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
	public void of_BufferedImage_asBufferedImages_NoOutputFormatSpecified() throws IOException
	{
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
	public void of_BufferedImage_toOutputStream_NoOutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		try
		{
			// when
			Thumbnails.of(img)
				.size(50, 50)
				.toOutputStream(os);
			
			fail();
		}
		catch (IllegalStateException e)
		{
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
	public void of_BufferedImage_toOutputStreams_NoOutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		try
		{
			// when
			Thumbnails.of(img)
				.size(50, 50)
				.toOutputStreams(Arrays.asList(os));
			
			fail();
		}
		catch (IllegalStateException e)
		{
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
	public void of_BufferedImage_iterableBufferedImages_NoOutputFormatSpecified() throws IOException
	{
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
	public void of_BufferedImage_toFile_File_OutputFormatSpecified() throws IOException
	{
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
	public void of_BufferedImage_toFile_String_OutputFormatSpecified() throws IOException
	{
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
	public void of_BufferedImage_toFiles_Iterable_OutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img1 = new BufferedImageBuilder(200, 200).build();
		
		// when
		Thumbnails.of(img1)
			.size(50, 50)
			.outputFormat("png")
			.toFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		File outFile = new File("src/test/resources/Thumbnailator/temp-0.png");
		outFile.deleteOnExit();
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile);
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
	}	
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(BufferedImage)</li>
	 * <li>outputFormat("png")</li>
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
	public void of_BufferedImage_asFiles_Iterable_OutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img1 = new BufferedImageBuilder(200, 200).build();
		
		// when
		List<File> thumbnails = Thumbnails.of(img1)
			.size(50, 50)
			.outputFormat("png")
			.asFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		File outFile = new File("src/test/resources/Thumbnailator/temp-0.png");
		outFile.deleteOnExit();
		
		assertEquals(1, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
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
	public void of_BufferedImage_asBufferedImage_OutputFormatSpecified() throws IOException
	{
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
	public void of_BufferedImage_asBufferedImages_OutputFormatSpecified() throws IOException
	{
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
	public void of_BufferedImage_toOutputStream_OutputFormatSpecified() throws IOException
	{
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
	public void of_BufferedImage_toOutputStreams_OutputFormatSpecified() throws IOException
	{
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
	public void of_BufferedImage_iterableBufferedImages_OutputFormatSpecified() throws IOException
	{
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
	public void of_BufferedImages_toFile_File_NoOutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		File destFile = new File("src/test/resources/Thumbnailator/tmp.png");
		destFile.deleteOnExit();
		
		try
		{
			// when
			Thumbnails.of(img, img)
				.size(100, 100)
				.toFile(destFile);
		}
		catch (IllegalArgumentException e)
		{
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
	public void of_BufferedImages_toFile_String_NoOutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		String destFilePath = "src/test/resources/Thumbnailator/tmp.png";
		
		try
		{
			// when
			Thumbnails.of(img, img)
				.size(100, 100)
				.toFile(destFilePath);
		}
		catch (IllegalArgumentException e)
		{
			// then
			assertEquals("Cannot output multiple thumbnails to one file.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
	 * <li>toFiles(Iterable<File>)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Processing completes successfully. Image format is determined
	 * by the extension of the file.</li>
	 * </ol>
	 * @throws IOException
	 */	
	@Test
	public void of_BufferedImages_toFiles_Iterable_NoOutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		// when
		Thumbnails.of(img, img)
			.size(50, 50)
			.toFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		File outFile = new File("src/test/resources/Thumbnailator/temp-0.png");
		outFile.deleteOnExit();
		
		assertEquals("png", TestUtils.getFormatName(new FileInputStream(outFile)));
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile);
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
	}	

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
	 * <li>asFiles(Iterable<File>)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Processing completes successfully. Image format is determined
	 * by the extension of the file.</li>
	 * </ol>
	 * @throws IOException
	 */	
	@Test
	public void of_BufferedImages_asFiles_Iterable_NoOutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		// when
		List<File> thumbnails = Thumbnails.of(img, img)
			.size(50, 50)
			.asFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		assertEquals(2, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
		assertEquals("png", TestUtils.getFormatName(new FileInputStream(thumbnails.get(0))));
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		
		BufferedImage fromFileImage2 = ImageIO.read(thumbnails.get(1));
		assertEquals("png", TestUtils.getFormatName(new FileInputStream(thumbnails.get(1))));
		assertEquals(50, fromFileImage2.getWidth());
		assertEquals(50, fromFileImage2.getHeight());
		
		// clean up
		thumbnails.get(0).deleteOnExit();
		thumbnails.get(1).deleteOnExit();
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
	public void of_BufferedImages_asBufferedImage_NoOutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try
		{
			// when
			Thumbnails.of(img, img)
				.size(100, 100)
				.asBufferedImage();
		}
		catch (IllegalArgumentException e)
		{
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
	public void of_BufferedImages_asBufferedImages_NoOutputFormatSpecified() throws IOException
	{
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
	public void of_BufferedImages_toOutputStream_NoOutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		try
		{
			// when
			Thumbnails.of(img, img)
				.size(50, 50)
				.toOutputStream(os);
			
			fail();
		}
		catch (IllegalArgumentException e)
		{
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
	public void of_BufferedImages_toOutputStreams_NoOutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		try
		{
			// when
			Thumbnails.of(img, img)
				.size(50, 50)
				.toOutputStreams(Arrays.asList(os));
			
			fail();
		}
		catch (IllegalStateException e)
		{
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
	public void of_BufferedImages_iterableBufferedImages_NoOutputFormatSpecified() throws IOException
	{
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
	public void of_BufferedImages_toFile_File_OutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		File destFile = new File("src/test/resources/Thumbnailator/tmp.png");
		destFile.deleteOnExit();
		
		try
		{
			// when
			Thumbnails.of(img, img)
				.size(100, 100)
				.outputFormat("png")
				.toFile(destFile);
		}
		catch (IllegalArgumentException e)
		{
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
	public void of_BufferedImages_toFile_String_OutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		String destFilePath = "src/test/resources/Thumbnailator/tmp.png";
		
		try
		{
			// when
			Thumbnails.of(img, img)
				.size(100, 100)
				.outputFormat("png")
				.toFile(destFilePath);
		}
		catch (IllegalArgumentException e)
		{
			// then
			assertEquals("Cannot output multiple thumbnails to one file.", e.getMessage());
			throw e;
		}
		finally
		{
			// clean up
			new File(destFilePath).deleteOnExit();
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
	 * <li>outputFormat("png")</li>
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
	public void of_BufferedImages_toFiles_Iterable_OutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		// when
		Thumbnails.of(img, img)
			.size(50, 50)
			.outputFormat("png")
			.toFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/temp-0.png");
		outFile1.deleteOnExit();
		File outFile2 = new File("src/test/resources/Thumbnailator/temp-1.png");
		outFile2.deleteOnExit();
		
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
	 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
	 * <li>outputFormat("png")</li>
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
	public void of_BufferedImages_asFiles_Iterable_OutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		// when
		List<File> thumbnails = Thumbnails.of(img, img)
			.size(50, 50)
			.outputFormat("png")
			.asFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		File outFile = new File("src/test/resources/Thumbnailator/temp-0.png");
		outFile.deleteOnExit();
		
		assertEquals(2, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		
		BufferedImage fromFileImage2 = ImageIO.read(thumbnails.get(1));
		assertEquals(50, fromFileImage2.getWidth());
		assertEquals(50, fromFileImage2.getHeight());
		
		// clean up
		thumbnails.get(0).deleteOnExit();
		thumbnails.get(1).deleteOnExit();
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
	public void of_BufferedImages_asBufferedImage_OutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try
		{
			// when
			Thumbnails.of(img, img)
				.size(100, 100)
				.outputFormat("png")
				.asBufferedImage();
		}
		catch (IllegalArgumentException e)
		{
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
	public void of_BufferedImages_asBufferedImages_OutputFormatSpecified() throws IOException
	{
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
	public void of_BufferedImages_toOutputStream_OutputFormatSpecified() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		try
		{
			// when
			Thumbnails.of(img, img)
				.size(50, 50)
				.outputFormat("png")
				.toOutputStream(os);
			
			fail();
		}
		catch (IllegalArgumentException e)
		{
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
	public void of_BufferedImages_toOutputStreams_OutputFormatSpecified() throws IOException
	{
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
	public void of_BufferedImages_iterableBufferedImages_OutputFormatSpecified() throws IOException
	{
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
	public void fromImages_Single_asBufferedImage() throws IOException
	{
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
	public void fromImages_Multiple_asBufferedImage() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try
		{
			// when
			Thumbnails.fromImages(Arrays.asList(img, img))
				.size(100, 100)
				.asBufferedImage();
		}
		catch (IllegalArgumentException e)
		{
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
	public void fromImages_Single_asBufferedImages() throws IOException
	{
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
	public void fromImages_Multiple_asBufferedImages() throws IOException
	{
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
	public void fromImagesIterable_Single_asBufferedImage() throws IOException
	{
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
	public void fromImagesIterable_Multiple_asBufferedImage() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try
		{
			// when
			Thumbnails.fromImages((Iterable<BufferedImage>)Arrays.asList(img, img))
				.size(100, 100)
				.asBufferedImage();
		}
		catch (IllegalArgumentException e)
		{
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
	public void fromImagesIterable_Single_asBufferedImages() throws IOException
	{
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
	public void fromImagesIterable_Multiple_asBufferedImages() throws IOException
	{
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
	public void of_File_toFile() throws IOException
	{
		// given
		File f = new File("src/test/resources/Thumbnailator/grid.png");
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
	public void of_File_toFiles_Rename() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		// when
		Thumbnails.of(f1)
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
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
	public void of_File_asFiles_Rename() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
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
	public void of_File_toFiles_Iterable() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		
		// when
		Thumbnails.of(f1)
			.size(50, 50)
			.toFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		File outFile = new File("src/test/resources/Thumbnailator/temp-0.png");
		outFile.deleteOnExit();
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile);
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
	public void of_File_asFiles_Iterable() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		
		// when
		List<File> thumbnails = Thumbnails.of(f1)
			.size(50, 50)
			.asFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/temp-0.png");
		outFile1.deleteOnExit();
		
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
	public void of_File_asBufferedImage() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		
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
	public void of_File_asBufferedImages() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		
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
	public void of_File_toOutputStream() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
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
	public void of_File_toOutputStreams() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
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
	public void of_File_iterableBufferedImages() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		
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
	public void of_Files_toFile() throws IOException
	{
		// given
		File f = new File("src/test/resources/Thumbnailator/grid.png");
		File outFile = new File("src/test/resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
		try
		{
			// when
			Thumbnails.of(f, f)
				.size(50, 50)
				.toFile(outFile);
		}
		catch (IllegalArgumentException e)
		{
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
	public void of_Files_toFiles_Rename() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		File f2 = new File("src/test/resources/Thumbnailator/grid.jpg");
		
		// when
		Thumbnails.of(f1, f2)
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("src/test/resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
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
	public void of_Files_asFiles_Rename() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		File f2 = new File("src/test/resources/Thumbnailator/grid.jpg");
		
		// when
		List<File> thumbnails = Thumbnails.of(f1, f2)
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("src/test/resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
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
	public void of_Files_toFiles_Iterable() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		File f2 = new File("src/test/resources/Thumbnailator/grid.jpg");

		// when
		Thumbnails.of(f1, f2)
			.size(50, 50)
			.toFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/temp-0.png");
		File outFile2 = new File("src/test/resources/Thumbnailator/temp-1.png");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
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
	public void of_Files_asFiles_Iterable() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		File f2 = new File("src/test/resources/Thumbnailator/grid.jpg");

		// when
		List<File> thumbnails = Thumbnails.of(f1, f2)
			.size(50, 50)
			.asFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		assertEquals(2, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		
		BufferedImage fromFileImage2 = ImageIO.read(thumbnails.get(1));
		assertEquals(50, fromFileImage2.getWidth());
		assertEquals(50, fromFileImage2.getHeight());
		
		// clean up
		thumbnails.get(0).deleteOnExit();
		thumbnails.get(1).deleteOnExit();
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
	public void of_Files_asBufferedImage() throws IOException
	{
		// given
		File f = new File("src/test/resources/Thumbnailator/grid.png");
		
		try
		{
			// when
			Thumbnails.of(f, f)
				.size(50, 50)
				.asBufferedImage();
		}
		catch (IllegalArgumentException e)
		{
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
	public void of_Files_asBufferedImages() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		File f2 = new File("src/test/resources/Thumbnailator/grid.jpg");
		
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
	public void of_Files_toOutputStream() throws IOException
	{
		// given
		File f = new File("src/test/resources/Thumbnailator/grid.png");
		OutputStream os = mock(OutputStream.class);
		
		try
		{
			// when
			Thumbnails.of(f, f)
				.size(50, 50)
				.toOutputStream(os);
		}
		catch (IllegalArgumentException e)
		{
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
	public void of_Files_toOutputStreams() throws IOException
	{
		// given
		File f = new File("src/test/resources/Thumbnailator/grid.png");
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
	public void of_Files_iterableBufferedImages() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		File f2 = new File("src/test/resources/Thumbnailator/grid.jpg");
		
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
	public void fromFiles_Single_toFile() throws IOException
	{
		// given
		File f = new File("src/test/resources/Thumbnailator/grid.png");
		File outFile = new File("src/test/resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
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
	public void fromFiles_Multiple_toFile() throws IOException
	{
		// given
		File f = new File("src/test/resources/Thumbnailator/grid.png");
		File outFile = new File("src/test/resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
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
	public void fromFiles_Single_toFiles() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		// when
		Thumbnails.fromFiles(Arrays.asList(f1))
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		
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
	public void fromFiles_Multiple_toFiles() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		File f2 = new File("src/test/resources/Thumbnailator/grid.jpg");
		
		// when
		Thumbnails.fromFiles(Arrays.asList(f1, f2))
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("src/test/resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
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
	public void fromFiles_Single_asFiles() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
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
	public void fromFiles_Multiple_asFiles() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		File f2 = new File("src/test/resources/Thumbnailator/grid.jpg");
		
		// when
		List<File> thumbnails = Thumbnails.fromFiles(Arrays.asList(f1, f2))
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("src/test/resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
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
	public void fromFilesIterable_Single_toFile() throws IOException
	{
		// given
		File f = new File("src/test/resources/Thumbnailator/grid.png");
		File outFile = new File("src/test/resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
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
	public void fromFilesIterable_Multiple_toFile() throws IOException
	{
		// given
		File f = new File("src/test/resources/Thumbnailator/grid.png");
		File outFile = new File("src/test/resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
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
	public void fromFilesIterable_Single_toFiles() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		// when
		Thumbnails.fromFiles((Iterable<File>)Arrays.asList(f1))
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		
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
	public void fromFilesIterable_Multiple_toFiles() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		File f2 = new File("src/test/resources/Thumbnailator/grid.jpg");
		
		// when
		Thumbnails.fromFiles((Iterable<File>)Arrays.asList(f1, f2))
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("src/test/resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
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
	public void fromFilesIterable_Single_asFiles() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
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
	public void fromFilesIterable_Multiple_asFiles() throws IOException
	{
		// given
		File f1 = new File("src/test/resources/Thumbnailator/grid.png");
		File f2 = new File("src/test/resources/Thumbnailator/grid.jpg");
		
		// when
		List<File> thumbnails = Thumbnails.fromFiles((Iterable<File>)Arrays.asList(f1, f2))
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("src/test/resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
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
	public void of_String_toFile() throws IOException
	{
		// given
		String f = "src/test/resources/Thumbnailator/grid.png";
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
	public void of_Strings_toFile() throws IOException
	{
		// given
		String f = "src/test/resources/Thumbnailator/grid.png";
		File outFile = new File("src/test/resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
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
	public void of_String_toFiles() throws IOException
	{
		// given
		String f1 = "src/test/resources/Thumbnailator/grid.png";
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		// when
		Thumbnails.of(f1)
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		
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
	public void of_Strings_toFiles() throws IOException
	{
		// given
		String f1 = "src/test/resources/Thumbnailator/grid.png";
		String f2 = "src/test/resources/Thumbnailator/grid.jpg";
		
		// when
		Thumbnails.of(f1, f2)
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("src/test/resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
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
	public void of_String_asFiles() throws IOException
	{
		// given
		String f1 = "src/test/resources/Thumbnailator/grid.png";
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
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
	public void of_Strings_asFiles() throws IOException
	{
		// given
		String f1 = "src/test/resources/Thumbnailator/grid.png";
		String f2 = "src/test/resources/Thumbnailator/grid.jpg";
		
		// when
		List<File> thumbnails = Thumbnails.of(f1, f2)
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("src/test/resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
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
	public void fromFilenames_Single_toFile() throws IOException
	{
		// given
		String f = "src/test/resources/Thumbnailator/grid.png";
		File outFile = new File("src/test/resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
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
	public void fromFilenames_Multiple_toFile() throws IOException
	{
		// given
		String f = "src/test/resources/Thumbnailator/grid.png";
		File outFile = new File("src/test/resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
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
	public void fromFilenames_Single_toFiles() throws IOException
	{
		// given
		String f1 = "src/test/resources/Thumbnailator/grid.png";
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		// when
		Thumbnails.fromFilenames(Arrays.asList(f1))
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		
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
	public void fromFilenames_Multiple_toFiles() throws IOException
	{
		// given
		String f1 = "src/test/resources/Thumbnailator/grid.png";
		String f2 = "src/test/resources/Thumbnailator/grid.jpg";
		
		// when
		Thumbnails.fromFilenames(Arrays.asList(f1, f2))
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("src/test/resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
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
	public void fromFilenames_Single_asFiles() throws IOException
	{
		// given
		String f1 = "src/test/resources/Thumbnailator/grid.png";
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
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
	public void fromFilenames_Multiple_asFiles() throws IOException
	{
		// given
		String f1 = "src/test/resources/Thumbnailator/grid.png";
		String f2 = "src/test/resources/Thumbnailator/grid.jpg";
		
		// when
		List<File> thumbnails = Thumbnails.fromFilenames(Arrays.asList(f1, f2))
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("src/test/resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
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
	public void fromFilenamesIterable_Single_toFile() throws IOException
	{
		// given
		String f = "src/test/resources/Thumbnailator/grid.png";
		File outFile = new File("src/test/resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
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
	public void fromFilenamesIterable_Multiple_toFile() throws IOException
	{
		// given
		String f = "src/test/resources/Thumbnailator/grid.png";
		File outFile = new File("src/test/resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
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
	public void fromFilenamesIterable_Single_toFiles() throws IOException
	{
		// given
		String f1 = "src/test/resources/Thumbnailator/grid.png";
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		// when
		Thumbnails.fromFilenames((Iterable<String>)Arrays.asList(f1))
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		
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
	public void fromFilenamesIterable_Multiple_toFiles() throws IOException
	{
		// given
		String f1 = "src/test/resources/Thumbnailator/grid.png";
		String f2 = "src/test/resources/Thumbnailator/grid.jpg";
		
		// when
		Thumbnails.fromFilenames((Iterable<String>)Arrays.asList(f1, f2))
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("src/test/resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
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
	public void fromFilenamesIterable_Single_asFiles() throws IOException
	{
		// given
		String f1 = "src/test/resources/Thumbnailator/grid.png";
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
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
	public void fromFilenamesIterable_Multiple_asFiles() throws IOException
	{
		// given
		String f1 = "src/test/resources/Thumbnailator/grid.png";
		String f2 = "src/test/resources/Thumbnailator/grid.jpg";
		
		// when
		List<File> thumbnails = Thumbnails.fromFilenames((Iterable<String>)Arrays.asList(f1, f2))
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("src/test/resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
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
	public void of_URL_toFile() throws IOException
	{
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
	 * <li>toFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException occurs.</li>
	 * </ol>
	 * @throws IOException
	 */	
	@Test(expected=IllegalStateException.class)
	public void of_URL_toFiles_Rename() throws IOException
	{
		// given
		URL f1 = new File("src/test/resources/Thumbnailator/grid.png").toURL();
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		try
		{
			// when
			Thumbnails.of(f1)
				.size(50, 50)
				.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		}
		catch (IllegalStateException e)
		{
			// then
			assertEquals("Cannot create thumbnails to files if original images are not from files.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(URL)</li>
	 * <li>asFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException occurs.</li>
	 * </ol>
	 * @throws IOException
	 */	
	@Test(expected=IllegalStateException.class)
	public void of_URL_asFiles_Rename() throws IOException
	{
		// given
		URL f1 = new File("src/test/resources/Thumbnailator/grid.png").toURL();
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		try
		{
			// when
			Thumbnails.of(f1)
				.size(50, 50)
				.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		}
		catch (IllegalStateException e)
		{
			// then
			assertEquals("Cannot create thumbnails to files if original images are not from files.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(URL)</li>
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
	public void of_URL_toFiles_Iterable() throws IOException
	{
		// given
		URL f1 = new File("src/test/resources/Thumbnailator/grid.png").toURL();
		
		// when
		Thumbnails.of(f1)
			.size(50, 50)
			.toFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		File outFile = new File("src/test/resources/Thumbnailator/temp-0.png");
		outFile.deleteOnExit();
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile);
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(URL)</li>
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
	public void of_URL_asFiles_Iterable() throws IOException
	{
		// given
		URL f1 = new File("src/test/resources/Thumbnailator/grid.png").toURL();
		
		// when
		List<File> thumbnails = Thumbnails.of(f1)
			.size(50, 50)
			.asFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/temp-0.png");
		outFile1.deleteOnExit();
		
		assertEquals(1, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
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
	public void of_URL_asBufferedImage() throws IOException
	{
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
	public void of_URL_asBufferedImages() throws IOException
	{
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
	public void of_URL_toOutputStream() throws IOException
	{
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
	public void of_URL_toOutputStreams() throws IOException
	{
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
	public void of_URL_iterableBufferedImages() throws IOException
	{
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
	public void of_URLs_toFile() throws IOException
	{
		// given
		URL f = new File("src/test/resources/Thumbnailator/grid.png").toURL();
		File outFile = new File("src/test/resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
		try
		{
			// when
			Thumbnails.of(f, f)
				.size(50, 50)
				.toFile(outFile);
		}
		catch (IllegalArgumentException e)
		{
			// then
			assertEquals("Cannot output multiple thumbnails to one file.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(URL, URL)</li>
	 * <li>toFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException occurs.</li>
	 * </ol>
	 * @throws IOException
	 */	
	@Test(expected=IllegalStateException.class)
	public void of_URLs_toFiles_Rename() throws IOException
	{
		// given
		URL f1 = new File("src/test/resources/Thumbnailator/grid.png").toURL();
		URL f2 = new File("src/test/resources/Thumbnailator/grid.jpg").toURL();
	
		try
		{
			// when
			Thumbnails.of(f1, f2)
				.size(50, 50)
				.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		}
		catch (IllegalStateException e)
		{
			// then
			assertEquals("Cannot create thumbnails to files if original images are not from files.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(URL, URL)</li>
	 * <li>asFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException occurs.</li>
	 * </ol>
	 * @throws IOException
	 */	
	@Test(expected=IllegalStateException.class)
	public void of_URLs_asFiles_Rename() throws IOException
	{
		// given
		URL f1 = new File("src/test/resources/Thumbnailator/grid.png").toURL();
		URL f2 = new File("src/test/resources/Thumbnailator/grid.jpg").toURL();
		
		try
		{
			// when
			Thumbnails.of(f1, f2)
				.size(50, 50)
				.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		}
		catch (IllegalStateException e)
		{
			// then
			assertEquals("Cannot create thumbnails to files if original images are not from files.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(URL, URL)</li>
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
	public void of_URLs_toFiles_Iterable() throws IOException
	{
		// given
		URL f1 = new File("src/test/resources/Thumbnailator/grid.png").toURL();
		URL f2 = new File("src/test/resources/Thumbnailator/grid.jpg").toURL();
	
		// when
		Thumbnails.of(f1, f2)
			.size(50, 50)
			.toFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/temp-0.png");
		File outFile2 = new File("src/test/resources/Thumbnailator/temp-1.png");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
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
	 * <li>Thumbnails.of(URL, URL)</li>
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
	public void of_URLs_asFiles_Iterable() throws IOException
	{
		// given
		URL f1 = new File("src/test/resources/Thumbnailator/grid.png").toURL();
		URL f2 = new File("src/test/resources/Thumbnailator/grid.jpg").toURL();
	
		// when
		List<File> thumbnails = Thumbnails.of(f1, f2)
			.size(50, 50)
			.asFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		assertEquals(2, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		
		BufferedImage fromFileImage2 = ImageIO.read(thumbnails.get(1));
		assertEquals(50, fromFileImage2.getWidth());
		assertEquals(50, fromFileImage2.getHeight());
		
		// clean up
		thumbnails.get(0).deleteOnExit();
		thumbnails.get(1).deleteOnExit();
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
	public void of_URLs_asBufferedImage() throws IOException
	{
		// given
		URL f = new File("src/test/resources/Thumbnailator/grid.png").toURL();
		
		try
		{
			// when
			Thumbnails.of(f, f)
				.size(50, 50)
				.asBufferedImage();
		}
		catch (IllegalArgumentException e)
		{
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
	public void of_URLs_asBufferedImages() throws IOException
	{
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
	public void of_URLs_toOutputStream() throws IOException
	{
		// given
		URL f = new File("src/test/resources/Thumbnailator/grid.png").toURL();
		OutputStream os = mock(OutputStream.class);
		
		try
		{
			// when
			Thumbnails.of(f, f)
				.size(50, 50)
				.toOutputStream(os);
		}
		catch (IllegalArgumentException e)
		{
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
	public void of_URLs_toOutputStreams() throws IOException
	{
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
	public void of_URLs_iterableBufferedImages() throws IOException
	{
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
	public void fromURLs_Single_asBufferedImage() throws IOException
	{
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
	public void fromURLs_Multiple_asBufferedImage() throws IOException
	{
		// given
		URL url = new File("src/test/resources/Thumbnailator/grid.png").toURL();
		
		try
		{
			// when
			Thumbnails.fromURLs(Arrays.asList(url, url))
				.size(100, 100)
				.asBufferedImage();
		}
		catch (IllegalArgumentException e)
		{
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
	public void fromURLs_Single_asBufferedImages() throws IOException
	{
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
	public void fromURLs_Multiple_asBufferedImages() throws IOException
	{
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
	public void fromURLsIterable_Single_asBufferedImage() throws IOException
	{
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
	public void fromURLsIterable_Multiple_asBufferedImage() throws IOException
	{
		// given
		URL url = new File("src/test/resources/Thumbnailator/grid.png").toURL();
		
		try
		{
			// when
			Thumbnails.fromURLs((Iterable<URL>)Arrays.asList(url, url))
				.size(100, 100)
				.asBufferedImage();
		}
		catch (IllegalArgumentException e)
		{
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
	public void fromURLsIterable_Single_asBufferedImages() throws IOException
	{
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
	public void fromURLsIterable_Multiple_asBufferedImages() throws IOException
	{
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
	public void of_InputStream_toFile() throws IOException
	{
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
	 * <li>toFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException occurs.</li>
	 * </ol>
	 * @throws IOException
	 */	
	@Test(expected=IllegalStateException.class)
	public void of_InputStream_toFiles_Rename() throws IOException
	{
		// given
		InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		try
		{
			// when
			Thumbnails.of(is)
				.size(50, 50)
				.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		}
		catch (IllegalStateException e)
		{
			// then
			assertEquals("Cannot create thumbnails to files if original images are not from files.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(InputStream)</li>
	 * <li>asFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException occurs.</li>
	 * </ol>
	 * @throws IOException
	 */	
	@Test(expected=IllegalStateException.class)
	public void of_InputStream_asFiles_Rename() throws IOException
	{
		// given
		InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		File outFile1 = new File("src/test/resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		try
		{
			// when
			Thumbnails.of(is)
				.size(50, 50)
				.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		}
		catch (IllegalStateException e)
		{
			// then
			assertEquals("Cannot create thumbnails to files if original images are not from files.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(InputStream)</li>
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
	public void of_InputStream_toFiles_Iterable() throws IOException
	{
		// given
		InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		
		// when
		Thumbnails.of(is)
			.size(50, 50)
			.toFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		File outFile = new File("src/test/resources/Thumbnailator/temp-0.png");
		outFile.deleteOnExit();
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile);
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(InputStream)</li>
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
	public void of_InputStream_asFiles_Iterable() throws IOException
	{
		// given
		InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		
		// when
		List<File> thumbnails = Thumbnails.of(is)
			.size(50, 50)
			.asFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/temp-0.png");
		outFile1.deleteOnExit();
		
		assertEquals(1, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
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
	public void of_InputStream_asBufferedImage() throws IOException
	{
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
	public void of_InputStream_asBufferedImages() throws IOException
	{
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
	public void of_InputStream_toOutputStream() throws IOException
	{
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
	public void of_InputStream_toOutputStreams() throws IOException
	{
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
	public void of_InputStream_iterableBufferedImages() throws IOException
	{
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
	public void of_InputStreams_toFile() throws IOException
	{
		// given
		InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		File outFile = new File("src/test/resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
		try
		{
			// when
			Thumbnails.of(is, is)
				.size(50, 50)
				.toFile(outFile);
		}
		catch (IllegalArgumentException e)
		{
			// then
			assertEquals("Cannot output multiple thumbnails to one file.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(InputStream, InputStream)</li>
	 * <li>toFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException occurs.</li>
	 * </ol>
	 * @throws IOException
	 */	
	@Test(expected=IllegalStateException.class)
	public void of_InputStreams_toFiles_Rename() throws IOException
	{
		// given
		InputStream is1 = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		InputStream is2 = new FileInputStream("src/test/resources/Thumbnailator/grid.jpg");
		
		try
		{
			// when
			Thumbnails.of(is1, is2)
				.size(50, 50)
				.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		}
		catch (IllegalStateException e)
		{
			// then
			assertEquals("Cannot create thumbnails to files if original images are not from files.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(InputStream, InputStream)</li>
	 * <li>asFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException occurs.</li>
	 * </ol>
	 * @throws IOException
	 */	
	@Test(expected=IllegalStateException.class)
	public void of_InputStreams_asFiles_Rename() throws IOException
	{
		// given
		InputStream is1 = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		InputStream is2 = new FileInputStream("src/test/resources/Thumbnailator/grid.jpg");
		
		try
		{
			// when
			Thumbnails.of(is1, is2)
				.size(50, 50)
				.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		}
		catch (IllegalStateException e)
		{
			// then
			assertEquals("Cannot create thumbnails to files if original images are not from files.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(InputStream, InputStream)</li>
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
	public void of_InputStreams_toFiles_Iterable() throws IOException
	{
		// given
		InputStream is1 = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		InputStream is2 = new FileInputStream("src/test/resources/Thumbnailator/grid.jpg");
		
		// when
		Thumbnails.of(is1, is2)
			.size(50, 50)
			.toFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		File outFile1 = new File("src/test/resources/Thumbnailator/temp-0.png");
		File outFile2 = new File("src/test/resources/Thumbnailator/temp-1.png");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
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
	 * <li>Thumbnails.of(InputStream, InputStream)</li>
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
	public void of_InputStreams_asFiles_Iterable() throws IOException
	{
		// given
		InputStream is1 = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		InputStream is2 = new FileInputStream("src/test/resources/Thumbnailator/grid.jpg");
		
		// when
		List<File> thumbnails = Thumbnails.of(is1, is2)
			.size(50, 50)
			.asFiles(new ConsecutivelyNumberedFilenames(new File("src/test/resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		assertEquals(2, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		
		BufferedImage fromFileImage2 = ImageIO.read(thumbnails.get(1));
		assertEquals(50, fromFileImage2.getWidth());
		assertEquals(50, fromFileImage2.getHeight());
		
		// clean up
		thumbnails.get(0).deleteOnExit();
		thumbnails.get(1).deleteOnExit();
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
	public void of_InputStreams_asBufferedImage() throws IOException
	{
		// given
		InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		
		try
		{
			// when
			Thumbnails.of(is, is)
				.size(50, 50)
				.asBufferedImage();
		}
		catch (IllegalArgumentException e)
		{
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
	public void of_InputStreams_asBufferedImages() throws IOException
	{
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
	public void of_InputStreams_toOutputStream() throws IOException
	{
		// given
		InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		OutputStream os = mock(OutputStream.class);
		
		try
		{
			// when
			Thumbnails.of(is, is)
				.size(50, 50)
				.toOutputStream(os);
		}
		catch (IllegalArgumentException e)
		{
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
	public void of_InputStreams_toOutputStreams() throws IOException
	{
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
	public void of_InputStreams_iterableBufferedImages() throws IOException
	{
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
	public void fromInputStreams_Single_asBufferedImage() throws IOException
	{
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
	public void fromInputStreams_Multiple_asBufferedImage() throws IOException
	{
		// given
		InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		
		try
		{
			// when
			Thumbnails.fromInputStreams(Arrays.asList(is, is))
				.size(100, 100)
				.asBufferedImage();
		}
		catch (IllegalArgumentException e)
		{
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
	public void fromInputStreams_Single_asBufferedImages() throws IOException
	{
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
	public void fromInputStreams_Multiple_asBufferedImages() throws IOException
	{
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
	 * <li>Thumbnails.fromImages([FileInputStream])</li>
	 * <li>asBufferedImage()</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A BufferedImage is returned</li>
	 * </ol>
	 */	
	@Test
	public void fromInputStreams_Single_FileInputStream_asBufferedImage() throws IOException
	{
		// given
		FileInputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		
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
	 * <li>Thumbnails.fromImages([FileInputStream, FileInputStream])</li>
	 * <li>asBufferedImage()</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void fromInputStreams_Multiple_FileInputStream_asBufferedImage() throws IOException
	{
		// given
		FileInputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		
		try
		{
			// when
			Thumbnails.fromInputStreams(Arrays.asList(is, is))
				.size(100, 100)
				.asBufferedImage();
		}
		catch (IllegalArgumentException e)
		{
			// then
			assertEquals("Cannot create one thumbnail from multiple original images.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromImages([FileInputStream])</li>
	 * <li>asBufferedImages()</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException is thrown.</li>
	 * </ol>
	 */	
	@Test
	public void fromInputStreams_Single_FileInputStream_asBufferedImages() throws IOException
	{
		// given
		FileInputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		
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
	 * <li>Thumbnails.fromImages([FileInputStream, FileInputStream])</li>
	 * <li>asBufferedImage()</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException is thrown.</li>
	 * </ol>
	 */	
	@Test
	public void fromInputStream_Multiple_FileInputStream_asBufferedImages() throws IOException
	{
		// given
		FileInputStream fis1 = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		FileInputStream fis2 = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		
		// when
		List<BufferedImage> thumbnails = Thumbnails.fromInputStreams(Arrays.asList(fis1, fis2))
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
	public void of_InputStream_FileInputStream_toFile() throws IOException
	{
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
	public void fromInputStreamsIterable_Single_asBufferedImage() throws IOException
	{
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
	public void fromInputStreamsIterable_Multiple_asBufferedImage() throws IOException
	{
		// given
		InputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		
		try
		{
			// when
			Thumbnails.fromInputStreams((Iterable<InputStream>)Arrays.asList(is, is))
				.size(100, 100)
				.asBufferedImage();
		}
		catch (IllegalArgumentException e)
		{
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
	public void fromInputStreamsIterable_Single_asBufferedImages() throws IOException
	{
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
	public void fromInputStreamsIterable_Multiple_asBufferedImages() throws IOException
	{
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
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromImages(Iterable[FileInputStream])</li>
	 * <li>asBufferedImage()</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A BufferedImage is returned</li>
	 * </ol>
	 */	
	@Test
	public void fromInputStreamsIterable_Single_FileInputStream_asBufferedImage() throws IOException
	{
		// given
		FileInputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		
		// when
		BufferedImage thumbnail = Thumbnails.fromInputStreams((Iterable<FileInputStream>)Arrays.asList(is))
			.size(100, 100)
			.asBufferedImage();
		
		// then
		assertEquals(100, thumbnail.getWidth());
		assertEquals(100, thumbnail.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromImages(Iterable[FileInputStream, FileInputStream])</li>
	 * <li>asBufferedImage()</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void fromInputStreamsIterable_Multiple_FileInputStream_asBufferedImage() throws IOException
	{
		// given
		FileInputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		
		try
		{
			// when
			Thumbnails.fromInputStreams((Iterable<FileInputStream>)Arrays.asList(is, is))
				.size(100, 100)
				.asBufferedImage();
		}
		catch (IllegalArgumentException e)
		{
			// then
			assertEquals("Cannot create one thumbnail from multiple original images.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromImages(Iterable[FileInputStream])</li>
	 * <li>asBufferedImages()</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException is thrown.</li>
	 * </ol>
	 */	
	@Test
	public void fromInputStreamsIterable_Single_FileInputStream_asBufferedImages() throws IOException
	{
		// given
		FileInputStream is = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		
		// when
		List<BufferedImage> thumbnails = Thumbnails.fromInputStreams((Iterable<FileInputStream>)Arrays.asList(is))
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
	 * <li>Thumbnails.fromImages(Iterable[FileInputStream, FileInputStream])</li>
	 * <li>asBufferedImage()</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException is thrown.</li>
	 * </ol>
	 */	
	@Test
	public void fromInputStreamIterable_Multiple_FileInputStream_asBufferedImages() throws IOException
	{
		// given
		FileInputStream fis1 = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		FileInputStream fis2 = new FileInputStream("src/test/resources/Thumbnailator/grid.png");
		
		// when
		List<BufferedImage> thumbnails = Thumbnails.fromInputStreams((Iterable<FileInputStream>)Arrays.asList(fis1, fis2))
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
	 * <li>input is a file</li>
	 * <li>output is via toFile</li>
	 * <li>where the input and output file is the same</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The file size will be smaller after the resize.</li>
	 * </ol>
	 */	
	@Test
	public void fileSizeDecreasesAfterResize() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = new File("src/test/resources/Thumbnailator/tmp-grid.png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, f);
		
		// given
		long fileSizeBefore = f.length();
		
		// when
		Thumbnails.of(f)
			.size(100, 100)
			.toFile(f);
		
		// then
		long fileSizeAfter = f.length();
		f.delete();
		
		assertTrue(fileSizeAfter < fileSizeBefore);
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
	public void toFile_File_AllowOverwrite() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, f);
		
		// given
		long fileSizeBefore = f.length();
		
		// when
		Thumbnails.of(f)
			.size(100, 100)
			.allowOverwrite(true)
			.toFile(f);
		
		// then
		long fileSizeAfter = f.length();
		f.delete();
		
		assertTrue(fileSizeAfter < fileSizeBefore);
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
	public void toFile_File_DisallowOverwrite() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, f);
		
		// given
		// when
		try
		{
			Thumbnails.of(f)
				.size(100, 100)
				.allowOverwrite(false)
				.toFile(f);
			
			fail();
		}
		catch (IllegalArgumentException e)
		{
			// then
			assertEquals("The destination file exists.", e.getMessage());
			assertTrue(sourceFile.length() == f.length());
			f.delete();
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
	public void toFile_String_AllowOverwrite() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, f);
		
		// given
		long fileSizeBefore = f.length();
		
		// when
		Thumbnails.of(f)
			.size(100, 100)
			.allowOverwrite(true)
			.toFile(f.getAbsolutePath());
		
		// then
		long fileSizeAfter = f.length();
		f.delete();
		
		assertTrue(fileSizeAfter < fileSizeBefore);
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
	public void toFile_String_DisallowOverwrite() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, f);
		
		// given
		// when
		try
		{
			Thumbnails.of(f)
				.size(100, 100)
				.allowOverwrite(false)
				.toFile(f.getAbsolutePath());
			
			fail();
		}
		catch (IllegalArgumentException e)
		{
			// then
			assertEquals("The destination file exists.", e.getMessage());
			assertTrue(sourceFile.length() == f.length());
			f.delete();
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Iterable) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>single file specified, and it does not exist.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is written</li>
	 * </ol>
	 */	
	@Test
	public void toFilesIterable_AllowOverwrite_SingleFile_OutputFileDoesNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		
		// given
		
		// when
		Thumbnails.of(originalFile)
			.size(100, 100)
			.allowOverwrite(true)
			.toFiles(Arrays.asList(fileThatDoesntExist));
		
		// then
		assertTrue(fileThatDoesntExist.exists());
		
		// clean up
		originalFile.delete();
		fileThatDoesntExist.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Iterable) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>single file specified, and it exists</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is overwritten</li>
	 * </ol>
	 */	
	@Test
	public void toFilesIterable_AllowOverwrite_SingleFile_OutputFileExists() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatExists = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		TestUtils.copyFile(sourceFile, fileThatExists);
		
		// given
		
		// when
		Thumbnails.of(originalFile)
			.size(100, 100)
			.allowOverwrite(true)
			.toFiles(Arrays.asList(fileThatExists));
		
		// then
		assertTrue(fileThatExists.exists());
		assertFalse(sourceFile.length() == fileThatExists.length());
		
		// clean up
		originalFile.delete();
		fileThatExists.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Iterable) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>single file specified, and it does not exist.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is written</li>
	 * </ol>
	 */	
	@Test
	public void toFilesIterable_DisallowOverwrite_SingleFile_OutputFileDoesNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		
		// given
		
		// when
		Thumbnails.of(originalFile)
			.size(100, 100)
			.allowOverwrite(false)
			.toFiles(Arrays.asList(fileThatDoesntExist));
		
		// then
		assertTrue(fileThatDoesntExist.exists());
		
		// clean up
		originalFile.delete();
		fileThatDoesntExist.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Iterable) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>single file specified, and it exists</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is not written</li>
	 * </ol>
	 */	
	@Test
	public void toFilesIterable_DisallowOverwrite_SingleFile_OutputFileExists() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatExists = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		TestUtils.copyFile(sourceFile, fileThatExists);
		
		// given
		
		// when
		Thumbnails.of(originalFile)
			.size(100, 100)
			.allowOverwrite(false)
			.toFiles(Arrays.asList(fileThatExists));
		
		// then
		assertTrue(fileThatExists.exists());
		assertTrue(sourceFile.length() == fileThatExists.length());
		
		// clean up
		originalFile.delete();
		fileThatExists.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Iterable) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>multiple files are specified</li>
	 * <li>all of the output files do not exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is overwritten</li>
	 * </ol>
	 */	
	@Test
	public void toFilesIterable_AllowOverwrite_MultipleFiles_AllOutputFilesDoNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist1 = TestUtils.createTempFile(TMPDIR, "png");
		File fileThatDoesntExist2 = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		
		// given
		
		// when
		Thumbnails.of(originalFile, originalFile)
			.size(100, 100)
			.allowOverwrite(true)
			.toFiles(Arrays.asList(fileThatDoesntExist1, fileThatDoesntExist2));
		
		// then
		assertTrue(fileThatDoesntExist1.exists());
		assertTrue(fileThatDoesntExist2.exists());
		
		// clean up
		originalFile.delete();
		fileThatDoesntExist1.delete();
		fileThatDoesntExist2.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Iterable) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>multiple files are specified</li>
	 * <li>some of the output files exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is overwritten</li>
	 * </ol>
	 */	
	@Test
	public void toFilesIterable_AllowOverwrite_MultipleFiles_SomeOutputFilesDoNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist = TestUtils.createTempFile(TMPDIR, "png");
		File fileThatExists = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		TestUtils.copyFile(sourceFile, fileThatExists);
		
		// given
		
		// when
		Thumbnails.of(originalFile, originalFile)
			.size(100, 100)
			.allowOverwrite(true)
			.toFiles(Arrays.asList(fileThatDoesntExist, fileThatExists));
		
		// then
		assertTrue(fileThatDoesntExist.exists());
		assertTrue(fileThatExists.exists());
		assertFalse(sourceFile.length() == fileThatExists.length());
		
		// clean up
		originalFile.delete();
		fileThatDoesntExist.delete();
		fileThatExists.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Iterable) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>multiple files are specified</li>
	 * <li>some of the output files exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is overwritten</li>
	 * </ol>
	 */	
	@Test
	public void toFilesIterable_AllowOverwrite_MultipleFiles_AllOutputFilesExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatExists1 = TestUtils.createTempFile(TMPDIR, "png");
		File fileThatExists2 = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		TestUtils.copyFile(sourceFile, fileThatExists1);
		TestUtils.copyFile(sourceFile, fileThatExists2);
		
		// given
		
		// when
		Thumbnails.of(originalFile, originalFile)
			.size(100, 100)
			.allowOverwrite(true)
			.toFiles(Arrays.asList(fileThatExists1, fileThatExists2));
		
		// then
		assertTrue(fileThatExists1.exists());
		assertTrue(fileThatExists2.exists());
		assertFalse(sourceFile.length() == fileThatExists1.length());
		assertFalse(sourceFile.length() == fileThatExists2.length());
		
		// clean up
		originalFile.delete();
		fileThatExists1.delete();
		fileThatExists2.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Iterable) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>multiple files are specified</li>
	 * <li>all of the output files do not exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Only non-existent files are output</li>
	 * </ol>
	 */	
	@Test
	public void toFilesIterable_DisallowOverwrite_MultipleFiles_AllOutputFilesDoNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist1 = TestUtils.createTempFile(TMPDIR, "png");
		File fileThatDoesntExist2 = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		
		// given
		
		// when
		Thumbnails.of(originalFile, originalFile)
			.size(100, 100)
			.allowOverwrite(false)
			.toFiles(Arrays.asList(fileThatDoesntExist1, fileThatDoesntExist2));
		
		// then
		assertTrue(fileThatDoesntExist1.exists());
		assertTrue(fileThatDoesntExist2.exists());
		
		// clean up
		originalFile.delete();
		fileThatDoesntExist1.delete();
		fileThatDoesntExist2.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Iterable) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>multiple files are specified</li>
	 * <li>some of the output files exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Only non-existent files are output</li>
	 * </ol>
	 */	
	@Test
	public void toFilesIterable_DisallowOverwrite_MultipleFiles_SomeOutputFilesDoNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist = TestUtils.createTempFile(TMPDIR, "png");
		File fileThatExists = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		TestUtils.copyFile(sourceFile, fileThatExists);
		
		// given
		
		// when
		Thumbnails.of(originalFile, originalFile)
			.size(100, 100)
			.allowOverwrite(false)
			.toFiles(Arrays.asList(fileThatDoesntExist, fileThatExists));
		
		// then
		assertTrue(fileThatDoesntExist.exists());
		assertTrue(fileThatExists.exists());
		assertTrue(sourceFile.length() == fileThatExists.length());
		
		// clean up
		originalFile.delete();
		fileThatDoesntExist.delete();
		fileThatExists.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Iterable) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>multiple files are specified</li>
	 * <li>some of the output files exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Only non-existent files are output</li>
	 * </ol>
	 */	
	@Test
	public void toFilesIterable_DisallowOverwrite_MultipleFiles_AllOutputFilesExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatExists1 = TestUtils.createTempFile(TMPDIR, "png");
		File fileThatExists2 = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		TestUtils.copyFile(sourceFile, fileThatExists1);
		TestUtils.copyFile(sourceFile, fileThatExists2);
		
		// given
		
		// when
		Thumbnails.of(originalFile, originalFile)
			.size(100, 100)
			.allowOverwrite(false)
			.toFiles(Arrays.asList(fileThatExists1, fileThatExists2));
		
		// then
		assertTrue(fileThatExists1.exists());
		assertTrue(fileThatExists2.exists());
		assertTrue(sourceFile.length() == fileThatExists1.length());
		assertTrue(sourceFile.length() == fileThatExists2.length());
		
		// clean up
		originalFile.delete();
		fileThatExists1.delete();
		fileThatExists2.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument asFiles(Iterable) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>single file specified, and it does not exist.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is written</li>
	 * <li>The returned list contains only files which were written</li>
	 * </ol>
	 */	
	@Test
	public void asFilesIterable_AllowOverwrite_SingleFile_OutputFileDoesNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist1 = TestUtils.createTempFile(TMPDIR, "png");
		File fileThatDoesntExist2 = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile, originalFile)
			.size(100, 100)
			.allowOverwrite(true)
			.asFiles(Arrays.asList(fileThatDoesntExist1, fileThatDoesntExist2));
		
		// then
		assertTrue(fileThatDoesntExist1.exists());
		assertTrue(fileThatDoesntExist2.exists());
		assertEquals(Arrays.asList(fileThatDoesntExist1, fileThatDoesntExist2), list);
		
		// clean up
		originalFile.delete();
		fileThatDoesntExist1.delete();
		fileThatDoesntExist2.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Iterable) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>single file specified, and it does not exist.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is overwritten</li>
	 * <li>The returned list contains only files which were written</li>
	 * </ol>
	 */	
	@Test
	public void asFilesIterable_AllowOverwrite_SingleFile_OutputFileExists() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatExists1 = TestUtils.createTempFile(TMPDIR, "png");
		File fileThatExists2 = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		TestUtils.copyFile(sourceFile, fileThatExists1);
		TestUtils.copyFile(sourceFile, fileThatExists2);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile, originalFile)
			.size(100, 100)
			.allowOverwrite(true)
			.asFiles(Arrays.asList(fileThatExists1, fileThatExists2));
		
		// then
		assertTrue(fileThatExists1.exists());
		assertTrue(fileThatExists2.exists());
		assertFalse(sourceFile.length() == fileThatExists1.length());
		assertFalse(sourceFile.length() == fileThatExists2.length());
		assertEquals(Arrays.asList(fileThatExists1, fileThatExists2), list);
		
		// clean up
		originalFile.delete();
		fileThatExists1.delete();
		fileThatExists2.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Iterable) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>single file specified, and it does not exist.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is written</li>
	 * <li>The returned list contains only files which were written</li>
	 * </ol>
	 */	
	@Test
	public void asFilesIterable_DisallowOverwrite_SingleFiles_OutputFileDoesNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist1 = TestUtils.createTempFile(TMPDIR, "png");
		File fileThatDoesntExist2 = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile, originalFile)
			.size(100, 100)
			.allowOverwrite(false)
			.asFiles(Arrays.asList(fileThatDoesntExist1, fileThatDoesntExist2));
		
		// then
		assertTrue(fileThatDoesntExist1.exists());
		assertTrue(fileThatDoesntExist2.exists());
		assertEquals(Arrays.asList(fileThatDoesntExist1, fileThatDoesntExist2), list);
		
		// clean up
		originalFile.delete();
		fileThatDoesntExist1.delete();
		fileThatDoesntExist2.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Iterable) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>single file specified, and it does not exist.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is not written</li>
	 * <li>The returned list is empty</li>
	 * </ol>
	 */	
	@Test
	public void asFilesIterable_DisallowOverwrite_SingleFile_OutputFileExists() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatExists1 = TestUtils.createTempFile(TMPDIR, "png");
		File fileThatExists2 = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		TestUtils.copyFile(sourceFile, fileThatExists1);
		TestUtils.copyFile(sourceFile, fileThatExists2);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile, originalFile)
			.size(100, 100)
			.allowOverwrite(false)
			.asFiles(Arrays.asList(fileThatExists1, fileThatExists2));
		
		// then
		assertTrue(fileThatExists1.exists());
		assertTrue(fileThatExists2.exists());
		assertTrue(sourceFile.length() == fileThatExists1.length());
		assertTrue(sourceFile.length() == fileThatExists2.length());
		assertEquals(Collections.emptyList(), list);
		
		// clean up
		originalFile.delete();
		fileThatExists1.delete();
		fileThatExists2.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument asFiles(Iterable) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>multiple files are specified</li>
	 * <li>all of the output files do not exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is overwritten</li>
	 * <li>The returned list contains only files which were written</li>
	 * </ol>
	 */	
	@Test
	public void asFilesIterable_AllowOverwrite_MultipleFiles_AllOutputFilesDoNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist1 = TestUtils.createTempFile(TMPDIR, "png");
		File fileThatDoesntExist2 = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile, originalFile)
			.size(100, 100)
			.allowOverwrite(true)
			.asFiles(Arrays.asList(fileThatDoesntExist1, fileThatDoesntExist2));
		
		// then
		assertTrue(fileThatDoesntExist1.exists());
		assertTrue(fileThatDoesntExist2.exists());
		assertEquals(Arrays.asList(fileThatDoesntExist1, fileThatDoesntExist2), list);
		
		// clean up
		originalFile.delete();
		fileThatDoesntExist1.delete();
		fileThatDoesntExist2.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Iterable) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>multiple files are specified</li>
	 * <li>some of the output files exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is overwritten</li>
	 * <li>The returned list contains only files which were written</li>
	 * </ol>
	 */	
	@Test
	public void asFilesIterable_AllowOverwrite_MultipleFiles_SomeOutputFilesDoNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist = TestUtils.createTempFile(TMPDIR, "png");
		File fileThatExists = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		TestUtils.copyFile(sourceFile, fileThatExists);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile, originalFile)
			.size(100, 100)
			.allowOverwrite(true)
			.asFiles(Arrays.asList(fileThatDoesntExist, fileThatExists));
		
		// then
		assertTrue(fileThatDoesntExist.exists());
		assertTrue(fileThatExists.exists());
		assertFalse(sourceFile.length() == fileThatExists.length());
		assertEquals(Arrays.asList(fileThatDoesntExist, fileThatExists), list);
		
		// clean up
		originalFile.delete();
		fileThatDoesntExist.delete();
		fileThatExists.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Iterable) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>multiple files are specified</li>
	 * <li>some of the output files exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is overwritten</li>
	 * <li>The returned list contains only files which were written</li>
	 * </ol>
	 */	
	@Test
	public void asFilesIterable_AllowOverwrite_MultipleFiles_AllOutputFilesExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatExists1 = TestUtils.createTempFile(TMPDIR, "png");
		File fileThatExists2 = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		TestUtils.copyFile(sourceFile, fileThatExists1);
		TestUtils.copyFile(sourceFile, fileThatExists2);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile, originalFile)
			.size(100, 100)
			.allowOverwrite(true)
			.asFiles(Arrays.asList(fileThatExists1, fileThatExists2));
		
		// then
		assertTrue(fileThatExists1.exists());
		assertTrue(fileThatExists2.exists());
		assertFalse(sourceFile.length() == fileThatExists1.length());
		assertFalse(sourceFile.length() == fileThatExists2.length());
		assertEquals(Arrays.asList(fileThatExists1, fileThatExists2), list);
		
		// clean up
		originalFile.delete();
		fileThatExists1.delete();
		fileThatExists2.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Iterable) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>multiple files are specified</li>
	 * <li>all of the output files do not exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Only non-existent files are output</li>
	 * <li>The returned list contains only files which were written</li>
	 * </ol>
	 */	
	@Test
	public void asFilesIterable_DisallowOverwrite_MultipleFiles_AllOutputFilesDoNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist1 = TestUtils.createTempFile(TMPDIR, "png");
		File fileThatDoesntExist2 = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile, originalFile)
			.size(100, 100)
			.allowOverwrite(false)
			.asFiles(Arrays.asList(fileThatDoesntExist1, fileThatDoesntExist2));
		
		// then
		assertTrue(fileThatDoesntExist1.exists());
		assertTrue(fileThatDoesntExist2.exists());
		assertEquals(Arrays.asList(fileThatDoesntExist1, fileThatDoesntExist2), list);
		
		// clean up
		originalFile.delete();
		fileThatDoesntExist1.delete();
		fileThatDoesntExist2.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Iterable) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>multiple files are specified</li>
	 * <li>some of the output files exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Only non-existent files are output</li>
	 * <li>The returned list contains only files which were written</li>
	 * </ol>
	 */	
	@Test
	public void asFilesIterable_DisallowOverwrite_MultipleFiles_SomeOutputFilesDoNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist = TestUtils.createTempFile(TMPDIR, "png");
		File fileThatExists = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		TestUtils.copyFile(sourceFile, fileThatExists);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile, originalFile)
			.size(100, 100)
			.allowOverwrite(false)
			.asFiles(Arrays.asList(fileThatDoesntExist, fileThatExists));
		
		// then
		assertTrue(fileThatDoesntExist.exists());
		assertTrue(fileThatExists.exists());
		assertTrue(sourceFile.length() == fileThatExists.length());
		assertEquals(Arrays.asList(fileThatDoesntExist), list);
		
		// clean up
		originalFile.delete();
		fileThatDoesntExist.delete();
		fileThatExists.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Iterable) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>multiple files are specified</li>
	 * <li>some of the output files exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Only non-existent files are output</li>
	 * <li>The returned list contains only files which were written</li>
	 * </ol>
	 */	
	@Test
	public void asFilesIterable_DisallowOverwrite_MultipleFiles_AllOutputFilesExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatExists1 = TestUtils.createTempFile(TMPDIR, "png");
		File fileThatExists2 = TestUtils.createTempFile(TMPDIR, "png");
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		TestUtils.copyFile(sourceFile, fileThatExists1);
		TestUtils.copyFile(sourceFile, fileThatExists2);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile, originalFile)
			.size(100, 100)
			.allowOverwrite(false)
			.asFiles(Arrays.asList(fileThatExists1, fileThatExists2));
		
		// then
		assertTrue(fileThatExists1.exists());
		assertTrue(fileThatExists2.exists());
		assertTrue(sourceFile.length() == fileThatExists1.length());
		assertTrue(sourceFile.length() == fileThatExists2.length());
		assertEquals(Collections.emptyList(), list);
		
		// clean up
		originalFile.delete();
		fileThatExists1.delete();
		fileThatExists2.delete();
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Rename) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>single file specified, and it does not exist.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is written</li>
	 * </ol>
	 */	
	@Test
	public void toFilesRename_AllowOverwrite_SingleFile_OutputFileDoesNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist = makeRenamedFile(originalFile, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		
		// given
		
		// when
		Thumbnails.of(originalFile)
			.size(100, 100)
			.allowOverwrite(true)
			.toFiles(rename);
		
		// then
		assertTrue(fileThatDoesntExist.exists());
		
		// clean up
		originalFile.delete();
		fileThatDoesntExist.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Rename) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>single file specified, and it exists</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is overwritten</li>
	 * </ol>
	 */	
	@Test
	public void toFilesRename_AllowOverwrite_SingleFile_OutputFileExists() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatExists = makeRenamedFile(originalFile, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		TestUtils.copyFile(sourceFile, fileThatExists);
		
		// given
		
		// when
		Thumbnails.of(originalFile)
			.size(100, 100)
			.allowOverwrite(true)
			.toFiles(rename);
		
		// then
		assertTrue(fileThatExists.exists());
		assertFalse(sourceFile.length() == fileThatExists.length());
		
		// clean up
		originalFile.delete();
		fileThatExists.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Rename) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>single file specified, and it does not exist.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is written</li>
	 * </ol>
	 */	
	@Test
	public void toFilesRename_DisallowOverwrite_SingleFile_OutputFileDoesNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist = makeRenamedFile(originalFile, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		
		// given
		
		// when
		Thumbnails.of(originalFile)
			.size(100, 100)
			.allowOverwrite(false)
			.toFiles(rename);
		
		// then
		assertTrue(fileThatDoesntExist.exists());
		
		// clean up
		originalFile.delete();
		fileThatDoesntExist.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Rename) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>single file specified, and it exists</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is not written</li>
	 * </ol>
	 */	
	@Test
	public void toFilesRename_DisallowOverwrite_SingleFile_OutputFileExists() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatExists = makeRenamedFile(originalFile, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		TestUtils.copyFile(sourceFile, fileThatExists);
		
		// given
		
		// when
		Thumbnails.of(originalFile)
			.size(100, 100)
			.allowOverwrite(false)
			.toFiles(rename);
		
		// then
		assertTrue(fileThatExists.exists());
		assertTrue(sourceFile.length() == fileThatExists.length());
		
		// clean up
		originalFile.delete();
		fileThatExists.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Rename) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>multiple files are specified</li>
	 * <li>all of the output files do not exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is overwritten</li>
	 * </ol>
	 */	
	@Test
	public void toFilesRename_AllowOverwrite_MultipleFiles_AllOutputFilesDoNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile1 = TestUtils.createTempFile(TMPDIR, "png");
		File originalFile2 = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist1 = makeRenamedFile(originalFile1, rename);
		File fileThatDoesntExist2 = makeRenamedFile(originalFile2, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile1);
		TestUtils.copyFile(sourceFile, originalFile2);
		
		// given
		
		// when
		Thumbnails.of(originalFile1, originalFile2)
			.size(100, 100)
			.allowOverwrite(true)
			.toFiles(rename);
		
		// then
		assertTrue(fileThatDoesntExist1.exists());
		assertTrue(fileThatDoesntExist2.exists());
		
		// clean up
		originalFile1.delete();
		originalFile2.delete();
		fileThatDoesntExist1.delete();
		fileThatDoesntExist2.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Rename) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>multiple files are specified</li>
	 * <li>some of the output files exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is overwritten</li>
	 * </ol>
	 */	
	@Test
	public void toFilesRename_AllowOverwrite_MultipleFiles_SomeOutputFilesDoNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile1 = TestUtils.createTempFile(TMPDIR, "png");
		File originalFile2 = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist = makeRenamedFile(originalFile1, rename);
		File fileThatExists = makeRenamedFile(originalFile2, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile1);
		TestUtils.copyFile(sourceFile, originalFile2);
		TestUtils.copyFile(sourceFile, fileThatExists);
		
		// given
		
		// when
		Thumbnails.of(originalFile1, originalFile2)
			.size(100, 100)
			.allowOverwrite(true)
			.toFiles(rename);
		
		// then
		assertTrue(fileThatDoesntExist.exists());
		assertTrue(fileThatExists.exists());
		assertFalse(sourceFile.length() == fileThatExists.length());
		
		// clean up
		originalFile1.delete();
		originalFile2.delete();
		fileThatDoesntExist.delete();
		fileThatExists.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Rename) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>multiple files are specified</li>
	 * <li>some of the output files exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is overwritten</li>
	 * </ol>
	 */	
	@Test
	public void toFilesRename_AllowOverwrite_MultipleFiles_AllOutputFilesExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile1 = TestUtils.createTempFile(TMPDIR, "png");
		File originalFile2 = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatExists1 = makeRenamedFile(originalFile1, rename);
		File fileThatExists2 = makeRenamedFile(originalFile2, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile1);
		TestUtils.copyFile(sourceFile, originalFile2);
		TestUtils.copyFile(sourceFile, fileThatExists1);
		TestUtils.copyFile(sourceFile, fileThatExists2);
		
		// given
		
		// when
		Thumbnails.of(originalFile1, originalFile2)
			.size(100, 100)
			.allowOverwrite(true)
			.toFiles(rename);
		
		// then
		assertTrue(fileThatExists1.exists());
		assertTrue(fileThatExists2.exists());
		assertFalse(sourceFile.length() == fileThatExists1.length());
		assertFalse(sourceFile.length() == fileThatExists2.length());
		
		// clean up
		originalFile1.delete();
		originalFile2.delete();
		fileThatExists1.delete();
		fileThatExists2.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Rename) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>multiple files are specified</li>
	 * <li>all of the output files do not exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Only non-existent files are output</li>
	 * </ol>
	 */	
	@Test
	public void toFilesRename_DisallowOverwrite_MultipleFiles_AllOutputFilesDoNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile1 = TestUtils.createTempFile(TMPDIR, "png");
		File originalFile2 = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist1 = makeRenamedFile(originalFile1, rename);
		File fileThatDoesntExist2 = makeRenamedFile(originalFile2, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile1);
		TestUtils.copyFile(sourceFile, originalFile2);
		
		// given
		
		// when
		Thumbnails.of(originalFile1, originalFile2)
			.size(100, 100)
			.allowOverwrite(false)
			.toFiles(rename);
		
		// then
		assertTrue(fileThatDoesntExist1.exists());
		assertTrue(fileThatDoesntExist2.exists());
		
		// clean up
		originalFile1.delete();
		originalFile2.delete();
		fileThatDoesntExist1.delete();
		fileThatDoesntExist2.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Rename) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>multiple files are specified</li>
	 * <li>some of the output files exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Only non-existent files are output</li>
	 * </ol>
	 */	
	@Test
	public void toFilesRename_DisallowOverwrite_MultipleFiles_SomeOutputFilesDoNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile1 = TestUtils.createTempFile(TMPDIR, "png");
		File originalFile2 = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist = makeRenamedFile(originalFile1, rename);
		File fileThatExists = makeRenamedFile(originalFile2, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile1);
		TestUtils.copyFile(sourceFile, originalFile2);
		TestUtils.copyFile(sourceFile, fileThatExists);
		
		// given
		
		// when
		Thumbnails.of(originalFile1, originalFile2)
			.size(100, 100)
			.allowOverwrite(false)
			.toFiles(rename);
		
		// then
		assertTrue(fileThatDoesntExist.exists());
		assertTrue(fileThatExists.exists());
		assertTrue(sourceFile.length() == fileThatExists.length());
		
		// clean up
		originalFile1.delete();
		originalFile2.delete();
		fileThatDoesntExist.delete();
		fileThatExists.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument toFiles(Rename) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>multiple files are specified</li>
	 * <li>some of the output files exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Only non-existent files are output</li>
	 * </ol>
	 */	
	@Test
	public void toFilesRename_DisallowOverwrite_MultipleFiles_AllOutputFilesExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile1 = TestUtils.createTempFile(TMPDIR, "png");
		File originalFile2 = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatExists1 = makeRenamedFile(originalFile1, rename);
		File fileThatExists2 = makeRenamedFile(originalFile2, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile1);
		TestUtils.copyFile(sourceFile, originalFile2);
		TestUtils.copyFile(sourceFile, fileThatExists1);
		TestUtils.copyFile(sourceFile, fileThatExists2);
		
		// given
		
		// when
		Thumbnails.of(originalFile1, originalFile2)
			.size(100, 100)
			.allowOverwrite(false)
			.toFiles(rename);
		
		// then
		assertTrue(fileThatExists1.exists());
		assertTrue(fileThatExists2.exists());
		assertTrue(sourceFile.length() == fileThatExists1.length());
		assertTrue(sourceFile.length() == fileThatExists2.length());
		
		// clean up
		originalFile1.delete();
		originalFile2.delete();
		fileThatExists1.delete();
		fileThatExists2.delete();
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument asFiles(Rename) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>single file specified, and it does not exist.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is written</li>
	 * </ol>
	 */	
	@Test
	public void asFilesRename_AllowOverwrite_SingleFile_OutputFileDoesNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist = makeRenamedFile(originalFile, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile)
			.size(100, 100)
			.allowOverwrite(true)
			.asFiles(rename);
		
		// then
		assertTrue(fileThatDoesntExist.exists());
		assertEquals(Arrays.asList(fileThatDoesntExist), list);
		
		// clean up
		originalFile.delete();
		fileThatDoesntExist.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument asFiles(Rename) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>single file specified, and it exists</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is overwritten</li>
	 * </ol>
	 */	
	@Test
	public void asFilesRename_AllowOverwrite_SingleFile_OutputFileExists() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatExists = makeRenamedFile(originalFile, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		TestUtils.copyFile(sourceFile, fileThatExists);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile)
			.size(100, 100)
			.allowOverwrite(true)
			.asFiles(rename);
		
		// then
		assertTrue(fileThatExists.exists());
		assertFalse(sourceFile.length() == fileThatExists.length());
		assertEquals(Arrays.asList(fileThatExists), list);

		// clean up
		originalFile.delete();
		fileThatExists.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument asFiles(Rename) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>single file specified, and it does not exist.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is written</li>
	 * </ol>
	 */	
	@Test
	public void asFilesRename_DisallowOverwrite_SingleFile_OutputFileDoesNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist = makeRenamedFile(originalFile, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile)
			.size(100, 100)
			.allowOverwrite(false)
			.asFiles(rename);
		
		// then
		assertTrue(fileThatDoesntExist.exists());
		assertEquals(Arrays.asList(fileThatDoesntExist), list);
		
		// clean up
		originalFile.delete();
		fileThatDoesntExist.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument asFiles(Rename) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>single file specified, and it exists</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is not written</li>
	 * </ol>
	 */	
	@Test
	public void asFilesRename_DisallowOverwrite_SingleFile_OutputFileExists() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatExists = makeRenamedFile(originalFile, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile);
		TestUtils.copyFile(sourceFile, fileThatExists);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile)
			.size(100, 100)
			.allowOverwrite(false)
			.asFiles(rename);
		
		// then
		assertTrue(fileThatExists.exists());
		assertTrue(sourceFile.length() == fileThatExists.length());
		assertEquals(Collections.emptyList(), list);
		
		// clean up
		originalFile.delete();
		fileThatExists.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument asFiles(Rename) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>multiple files are specified</li>
	 * <li>all of the output files do not exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is overwritten</li>
	 * </ol>
	 */	
	@Test
	public void asFilesRename_AllowOverwrite_MultipleFiles_AllOutputFilesDoNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile1 = TestUtils.createTempFile(TMPDIR, "png");
		File originalFile2 = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist1 = makeRenamedFile(originalFile1, rename);
		File fileThatDoesntExist2 = makeRenamedFile(originalFile2, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile1);
		TestUtils.copyFile(sourceFile, originalFile2);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile1, originalFile2)
			.size(100, 100)
			.allowOverwrite(true)
			.asFiles(rename);
		
		// then
		assertTrue(fileThatDoesntExist1.exists());
		assertTrue(fileThatDoesntExist2.exists());
		assertEquals(Arrays.asList(fileThatDoesntExist1, fileThatDoesntExist2), list);
		
		// clean up
		originalFile1.delete();
		originalFile2.delete();
		fileThatDoesntExist1.delete();
		fileThatDoesntExist2.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument asFiles(Rename) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>multiple files are specified</li>
	 * <li>some of the output files exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is overwritten</li>
	 * </ol>
	 */	
	@Test
	public void asFilesRename_AllowOverwrite_MultipleFiles_SomeOutputFilesDoNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile1 = TestUtils.createTempFile(TMPDIR, "png");
		File originalFile2 = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist = makeRenamedFile(originalFile1, rename);
		File fileThatExists = makeRenamedFile(originalFile2, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile1);
		TestUtils.copyFile(sourceFile, originalFile2);
		TestUtils.copyFile(sourceFile, fileThatExists);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile1, originalFile2)
			.size(100, 100)
			.allowOverwrite(true)
			.asFiles(rename);
		
		// then
		assertTrue(fileThatDoesntExist.exists());
		assertTrue(fileThatExists.exists());
		assertFalse(sourceFile.length() == fileThatExists.length());
		assertEquals(Arrays.asList(fileThatDoesntExist, fileThatExists), list);
		
		// clean up
		originalFile1.delete();
		originalFile2.delete();
		fileThatDoesntExist.delete();
		fileThatExists.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument asFiles(Rename) is called</li>
	 * <li>allowOverwrite is true</li>
	 * <li>multiple files are specified</li>
	 * <li>some of the output files exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The destination file is overwritten</li>
	 * </ol>
	 */	
	@Test
	public void asFilesRename_AllowOverwrite_MultipleFiles_AllOutputFilesExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile1 = TestUtils.createTempFile(TMPDIR, "png");
		File originalFile2 = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatExists1 = makeRenamedFile(originalFile1, rename);
		File fileThatExists2 = makeRenamedFile(originalFile2, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile1);
		TestUtils.copyFile(sourceFile, originalFile2);
		TestUtils.copyFile(sourceFile, fileThatExists1);
		TestUtils.copyFile(sourceFile, fileThatExists2);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile1, originalFile2)
			.size(100, 100)
			.allowOverwrite(true)
			.asFiles(rename);
		
		// then
		assertTrue(fileThatExists1.exists());
		assertTrue(fileThatExists2.exists());
		assertFalse(sourceFile.length() == fileThatExists1.length());
		assertFalse(sourceFile.length() == fileThatExists2.length());
		assertEquals(Arrays.asList(fileThatExists1, fileThatExists2), list);
		
		// clean up
		originalFile1.delete();
		originalFile2.delete();
		fileThatExists1.delete();
		fileThatExists2.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument asFiles(Rename) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>multiple files are specified</li>
	 * <li>all of the output files do not exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Only non-existent files are output</li>
	 * </ol>
	 */	
	@Test
	public void asFilesRename_DisallowOverwrite_MultipleFiles_AllOutputFilesDoNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile1 = TestUtils.createTempFile(TMPDIR, "png");
		File originalFile2 = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist1 = makeRenamedFile(originalFile1, rename);
		File fileThatDoesntExist2 = makeRenamedFile(originalFile2, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile1);
		TestUtils.copyFile(sourceFile, originalFile2);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile1, originalFile2)
			.size(100, 100)
			.allowOverwrite(true)
			.asFiles(rename);
		
		// then
		assertTrue(fileThatDoesntExist1.exists());
		assertTrue(fileThatDoesntExist2.exists());
		assertEquals(Arrays.asList(fileThatDoesntExist1, fileThatDoesntExist2), list);
		
		// clean up
		originalFile1.delete();
		originalFile2.delete();
		fileThatDoesntExist1.delete();
		fileThatDoesntExist2.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument asFiles(Rename) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>multiple files are specified</li>
	 * <li>some of the output files exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Only non-existent files are output</li>
	 * </ol>
	 */	
	@Test
	public void asFilesRename_DisallowOverwrite_MultipleFiles_SomeOutputFilesDoNotExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile1 = TestUtils.createTempFile(TMPDIR, "png");
		File originalFile2 = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatDoesntExist = makeRenamedFile(originalFile1, rename);
		File fileThatExists = makeRenamedFile(originalFile2, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile1);
		TestUtils.copyFile(sourceFile, originalFile2);
		TestUtils.copyFile(sourceFile, fileThatExists);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile1, originalFile2)
			.size(100, 100)
			.allowOverwrite(false)
			.asFiles(rename);
		
		// then
		assertTrue(fileThatDoesntExist.exists());
		assertTrue(fileThatExists.exists());
		assertTrue(sourceFile.length() == fileThatExists.length());
		assertEquals(Arrays.asList(fileThatDoesntExist), list);
		
		// clean up
		originalFile1.delete();
		originalFile2.delete();
		fileThatDoesntExist.delete();
		fileThatExists.delete();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>the two argument asFiles(Rename) is called</li>
	 * <li>allowOverwrite is false</li>
	 * <li>multiple files are specified</li>
	 * <li>some of the output files exist</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Only non-existent files are output</li>
	 * </ol>
	 */	
	@Test
	public void asFilesRename_DisallowOverwrite_MultipleFiles_AllOutputFilesExist() throws IOException
	{
		// set up
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		
		Rename rename = Rename.PREFIX_DOT_THUMBNAIL;
		File originalFile1 = TestUtils.createTempFile(TMPDIR, "png");
		File originalFile2 = TestUtils.createTempFile(TMPDIR, "png");
		
		File fileThatExists1 = makeRenamedFile(originalFile1, rename);
		File fileThatExists2 = makeRenamedFile(originalFile2, rename);
		
		// copy the image to a temporary file.
		TestUtils.copyFile(sourceFile, originalFile1);
		TestUtils.copyFile(sourceFile, originalFile2);
		TestUtils.copyFile(sourceFile, fileThatExists1);
		TestUtils.copyFile(sourceFile, fileThatExists2);
		
		// given
		
		// when
		List<File> list = Thumbnails.of(originalFile1, originalFile2)
			.size(100, 100)
			.allowOverwrite(false)
			.asFiles(rename);
		
		// then
		assertTrue(fileThatExists1.exists());
		assertTrue(fileThatExists2.exists());
		assertTrue(sourceFile.length() == fileThatExists1.length());
		assertTrue(sourceFile.length() == fileThatExists2.length());
		assertEquals(Collections.emptyList(), list);
		
		// clean up
		originalFile1.delete();
		originalFile2.delete();
		fileThatExists1.delete();
		fileThatExists2.delete();
	}
	
	@Test
	public void toFile_SourceExtensionIsjpg_DestExtensionIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpg_DestExtensionIsJPG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpg_DestExtensionIsJpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpg_DestExtensionIsjpeg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpg_DestExtensionIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpg_DestExtensionIsJpeg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpeg_DestExtensionIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpeg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpeg_DestExtensionIsJPG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpeg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpeg_DestExtensionIsJpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpeg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpeg_DestExtensionIsjpeg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpeg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpeg_DestExtensionIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpeg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpeg_DestExtensionIsJpeg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpeg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJPG_DestExtensionIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "JPG");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJPG_DestExtensionIsJPG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "JPG");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJPG_DestExtensionIsJpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "JPG");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJPG_DestExtensionIsjpeg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "JPG");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJPG_DestExtensionIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "JPG");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJPG_DestExtensionIsJpeg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "JPG");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJPEG_DestExtensionIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "JPEG");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJPEG_DestExtensionIsJPG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "JPEG");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJPEG_DestExtensionIsJpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "JPEG");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJPEG_DestExtensionIsjpeg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "JPEG");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJPEG_DestExtensionIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "JPEG");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJPEG_DestExtensionIsJpeg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "JPEG");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJpg_DestExtensionIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "Jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJpg_DestExtensionIsJPG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "Jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJpg_DestExtensionIsJpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "Jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJpg_DestExtensionIsjpeg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "Jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJpg_DestExtensionIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "Jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJpg_DestExtensionIsJpeg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "Jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJpeg_DestExtensionIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "Jpeg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJpeg_DestExtensionIsJPG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "Jpeg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJpeg_DestExtensionIsJpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "Jpeg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJpeg_DestExtensionIsjpeg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "Jpeg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJpeg_DestExtensionIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "Jpeg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsJpeg_DestExtensionIsJpeg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "Jpeg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpg_DestExtensionIsjpg_OutputFormatIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("jpg")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpg_DestExtensionIsJPG_OutputFormatIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("jpg")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpg_DestExtensionIsJpg_OutputFormatIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("jpg")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpg_DestExtensionIsjpeg_OutputFormatIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("jpg")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpg_DestExtensionIsJPEG_OutputFormatIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("jpg")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpg_DestExtensionIsJpeg_OutputFormatIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("jpg")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}

	@Test
	public void toFile_SourceExtensionIsjpg_DestExtensionIsjpg_OutputFormatIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("JPEG")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpg_DestExtensionIsJPG_OutputFormatIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("JPEG")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpg_DestExtensionIsJpg_OutputFormatIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("JPEG")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpg_DestExtensionIsjpeg_OutputFormatIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("JPEG")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpg_DestExtensionIsJPEG_OutputFormatIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("JPEG")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsjpg_DestExtensionIsJpeg_OutputFormatIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.jpg");
		File f = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("JPEG")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}

	@Test
	public void toFile_SourceExtensionIspng_DestExtensionIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIspng_DestExtensionIsJPG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIspng_DestExtensionIsJpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIspng_DestExtensionIsjpeg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIspng_DestExtensionIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIspng_DestExtensionIsJpeg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsPNG_DestExtensionIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "PNG");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsPNG_DestExtensionIsJPG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "PNG");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsPNG_DestExtensionIsJpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "PNG");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsPNG_DestExtensionIsjpeg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "PNG");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsPNG_DestExtensionIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "PNG");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsPNG_DestExtensionIsJpeg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "PNG");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsPng_DestExtensionIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "Png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsPng_DestExtensionIsJPG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "Png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsPng_DestExtensionIsJpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "Png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsPng_DestExtensionIsjpeg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "Png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsPng_DestExtensionIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "Png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIsPng_DestExtensionIsJpeg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "Png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}

	@Test
	public void toFile_SourceExtensionIspng_DestExtensionIsjpg_OutputFormatIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("jpg")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIspng_DestExtensionIsJPG_OutputFormatIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("jpg")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIspng_DestExtensionIsJpg_OutputFormatIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("jpg")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIspng_DestExtensionIsjpeg_OutputFormatIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("jpg")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIspng_DestExtensionIsJPEG_OutputFormatIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("jpg")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIspng_DestExtensionIsJpeg_OutputFormatIsjpg() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("jpg")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}

	@Test
	public void toFile_SourceExtensionIspng_DestExtensionIsjpg_OutputFormatIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("JPEG")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIspng_DestExtensionIsJPG_OutputFormatIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("JPEG")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIspng_DestExtensionIsJpg_OutputFormatIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("JPEG")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIspng_DestExtensionIsjpeg_OutputFormatIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("JPEG")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIspng_DestExtensionIsJPEG_OutputFormatIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "JPEG");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("JPEG")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFile_SourceExtensionIspng_DestExtensionIsJpeg_OutputFormatIsJPEG() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		File f = TestUtils.createTempFile(TMPDIR, "png");
		TestUtils.copyFile(sourceFile, f);
		
		File destFile = TestUtils.createTempFile(TMPDIR, "Jpeg");
		
		// when
		Thumbnails.of(f)
			.size(10, 10)
			.outputFormat("JPEG")
			.toFile(destFile);
		
		// then
		assertTrue(destFile.exists());
		assertEquals("JPEG", TestUtils.getFormatName(new FileInputStream(destFile)));
	}
	
	@Test
	public void toFiles_Rename_WritesToSameDir_AllInputFromSameDir() throws IOException
	{
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
	public void toFiles_Rename_WritesToSameDir_InputsFromDifferentDir() throws IOException
	{
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
	public void toFiles_Rename_WritesToSpecifiedDir_AllInputFromSameDir() throws IOException
	{
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
	public void toFiles_Rename_WritesToSpecifiedDir_InputsFromDifferentDir() throws IOException
	{
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
	public void toFiles_Rename_WritesToSpecifiedDir_InputsFromDifferentDir_InputSameName() throws IOException
	{
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
	public void toFiles_Rename_WritesToSpecifiedDir_InputsFromDifferentDir_InputSameName_OverwriteFalse() throws IOException
	{
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
	public void toFiles_Rename_WritesToSpecifiedDir_InputsFromDifferentDir_InputSameName_OverwriteTrue() throws IOException
	{
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
	public void toFiles_Rename_WritesToSpecifiedDir_OutputDirDoesntExist() throws IOException
	{
		// given
		File sourceFile1 = new File("src/test/resources/Thumbnailator/grid.png");
		String tmpDir1 = TMPDIR + "/rename/1";
		String targetDir = TMPDIR + "/target";
		TestUtils.makeTemporaryDirectory(tmpDir1);
		
		File f1 = new File(tmpDir1, "grid.png");
		TestUtils.copyFile(sourceFile1, f1);
		
		try
		{
			// when
			Thumbnails.of(f1)
				.size(100, 100)
				.toFiles(new File(targetDir), Rename.PREFIX_DOT_THUMBNAIL);
			
			fail();
		}
		catch (IllegalArgumentException e)
		{
			// then
		}
		
		// cleanup
		TestUtils.deleteTemporaryDirectory(tmpDir1);
		TestUtils.deleteTemporaryDirectory(TMPDIR + "/rename");
	}
	
	@Test
	public void toFiles_Rename_WritesToSpecifiedDir_OutputDirIsntADir() throws IOException
	{
		// given
		File sourceFile1 = new File("src/test/resources/Thumbnailator/grid.png");
		String tmpDir1 = TMPDIR + "/rename/1";
		String targetDir = TMPDIR + "/target";
		TestUtils.makeTemporaryDirectory(tmpDir1);
		new File(targetDir).createNewFile();
		
		File f1 = new File(tmpDir1, "grid.png");
		TestUtils.copyFile(sourceFile1, f1);
		
		try
		{
			// when
			Thumbnails.of(f1)
				.size(100, 100)
				.toFiles(new File(targetDir), Rename.PREFIX_DOT_THUMBNAIL);
			
			fail();
		}
		catch (IllegalArgumentException e)
		{
			// then
		}
		
		// cleanup
		TestUtils.deleteTemporaryDirectory(tmpDir1);
		TestUtils.deleteTemporaryDirectory(TMPDIR + "/rename");
		new File(targetDir).delete();
	}	
	
	@Test
	public void asFiles_Rename_WritesToSameDir_AllInputFromSameDir() throws IOException
	{
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
	public void asFiles_Rename_WritesToSameDir_InputsFromDifferentDir() throws IOException
	{
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
	public void asFiles_Rename_WritesToSpecifiedDir_AllInputFromSameDir() throws IOException
	{
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
	public void asFiles_Rename_WritesToSpecifiedDir_InputsFromDifferentDir() throws IOException
	{
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
	public void asFiles_Rename_WritesToSpecifiedDir_InputsFromDifferentDir_InputSameName() throws IOException
	{
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
	public void asFiles_Rename_WritesToSpecifiedDir_InputsFromDifferentDir_InputSameName_OverwriteFalse() throws IOException
	{
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
	public void asFiles_Rename_WritesToSpecifiedDir_InputsFromDifferentDir_InputSameName_OverwriteTrue() throws IOException
	{
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
	public void asFiles_Rename_WritesToSpecifiedDir_OutputDirDoesntExist() throws IOException
	{
		// given
		File sourceFile1 = new File("src/test/resources/Thumbnailator/grid.png");
		String tmpDir1 = TMPDIR + "/rename/1";
		String targetDir = TMPDIR + "/target";
		TestUtils.makeTemporaryDirectory(tmpDir1);
		
		File f1 = new File(tmpDir1, "grid.png");
		TestUtils.copyFile(sourceFile1, f1);
		
		try
		{
			// when
			Thumbnails.of(f1)
				.size(100, 100)
				.asFiles(new File(targetDir), Rename.PREFIX_DOT_THUMBNAIL);
			
			fail();
		}
		catch (IllegalArgumentException e)
		{
			// then
		}
		
		// cleanup
		TestUtils.deleteTemporaryDirectory(tmpDir1);
		TestUtils.deleteTemporaryDirectory(TMPDIR + "/rename");
	}
	
	@Test
	public void asFiles_Rename_WritesToSpecifiedDir_OutputDirIsntADir() throws IOException
	{
		// given
		File sourceFile1 = new File("src/test/resources/Thumbnailator/grid.png");
		String tmpDir1 = TMPDIR + "/rename/1";
		String targetDir = TMPDIR + "/target";
		TestUtils.makeTemporaryDirectory(tmpDir1);
		new File(targetDir).createNewFile();
		
		File f1 = new File(tmpDir1, "grid.png");
		TestUtils.copyFile(sourceFile1, f1);
		
		try
		{
			// when
			Thumbnails.of(f1)
				.size(100, 100)
				.asFiles(new File(targetDir), Rename.PREFIX_DOT_THUMBNAIL);
			
			fail();
		}
		catch (IllegalArgumentException e)
		{
			// then
		}
		
		// cleanup
		TestUtils.deleteTemporaryDirectory(tmpDir1);
		TestUtils.deleteTemporaryDirectory(TMPDIR + "/rename");
		new File(targetDir).delete();
	}
	
	@Test
	public void useOriginalFormat() throws IOException
	{
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
	public void determineOutputFormat() throws IOException
	{
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
	public void correctOrientation1() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_1.jpg");
		
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
	public void correctOrientation2() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_2.jpg");
		
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
	public void correctOrientation3() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_3.jpg");
		
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
	public void correctOrientation4() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_4.jpg");
		
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
	public void correctOrientation5() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_5.jpg");
		
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
	public void correctOrientation6() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_6.jpg");
		
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
	public void correctOrientation7() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_7.jpg");
		
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
	public void correctOrientation8() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_8.jpg");
		
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
	public void correctOrientation1FromInputStream() throws IOException
	{
		// given
		FileInputStream is = new FileInputStream("src/test/resources/Exif/source_1.jpg");
		
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
	
	@Test
	public void correctOrientation2FromInputStream() throws Exception
	{
		// given
		FileInputStream is = new FileInputStream("src/test/resources/Exif/source_2.jpg");
		
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
	
	@Test
	public void correctOrientation3FromInputStream() throws Exception
	{
		// given
		FileInputStream is = new FileInputStream("src/test/resources/Exif/source_3.jpg");
		
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
	
	@Test
	public void correctOrientation4FromInputStream() throws Exception
	{
		// given
		FileInputStream is = new FileInputStream("src/test/resources/Exif/source_4.jpg");
		
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
	
	@Test
	public void correctOrientation5FromInputStream() throws Exception
	{
		// given
		FileInputStream is = new FileInputStream("src/test/resources/Exif/source_5.jpg");
		
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
	
	@Test
	public void correctOrientation6FromInputStream() throws Exception
	{
		// given
		FileInputStream is = new FileInputStream("src/test/resources/Exif/source_6.jpg");
		
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
	
	@Test
	public void correctOrientation7FromInputStream() throws Exception
	{
		// given
		FileInputStream is = new FileInputStream("src/test/resources/Exif/source_7.jpg");
		
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
	
	@Test
	public void correctOrientation8FromInputStream() throws Exception
	{
		// given
		FileInputStream is = new FileInputStream("src/test/resources/Exif/source_8.jpg");
		
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
	
	@Test
	public void multipleCallsCorrectOrientation_SameOrientation_asBufferedImages() throws IOException
	{
		// given
		// when
		List<BufferedImage> results =
			Thumbnails.of("src/test/resources/Exif/source_1.jpg", "src/test/resources/Exif/source_1.jpg")
				.size(100, 100)
				.asBufferedImages();
		
		// then
		assertEquals(results.size(), 2);
		BufferedImageAssert.assertMatches(
				results.get(0),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
		BufferedImageAssert.assertMatches(
				results.get(1),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}

	@Test
	public void multipleCallsCorrectOrientation_DifferentOrientation_asBufferedImages() throws IOException
	{
		// given
		// when
		List<BufferedImage> results =
			Thumbnails.of("src/test/resources/Exif/source_2.jpg", "src/test/resources/Exif/source_1.jpg")
				.size(100, 100)
				.asBufferedImages();
		
		// then
		assertEquals(results.size(), 2);
		BufferedImageAssert.assertMatches(
				results.get(0),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
		BufferedImageAssert.assertMatches(
				results.get(1),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}

	@Test
	public void multipleCallsCorrectOrientation_SameOrientation_iterableBufferedImages() throws IOException
	{
		// given
		// when
		Iterable<BufferedImage> results =
			Thumbnails.of("src/test/resources/Exif/source_1.jpg", "src/test/resources/Exif/source_1.jpg")
				.size(100, 100)
				.iterableBufferedImages();
		
		// then
		Iterator<BufferedImage> iter = results.iterator();
		BufferedImageAssert.assertMatches(
				iter.next(),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
		BufferedImageAssert.assertMatches(
				iter.next(),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
		assertFalse(iter.hasNext());
	}

	@Test
	public void multipleCallsCorrectOrientation_DifferentOrientation_iterableBufferedImages() throws IOException
	{
		// given
		// when
		Iterable<BufferedImage> results =
				Thumbnails.of("src/test/resources/Exif/source_2.jpg", "src/test/resources/Exif/source_1.jpg")
					.size(100, 100)
					.iterableBufferedImages();

		
		// then
		Iterator<BufferedImage> iter = results.iterator();
		BufferedImageAssert.assertMatches(
				iter.next(),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
		BufferedImageAssert.assertMatches(
				iter.next(),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
		assertFalse(iter.hasNext());
	}
	
	@Test
	public void multipleCallsCorrectOrientation_SameOrientation_asFiles() throws IOException
	{
		// given
		File outFile1 = TestUtils.createTempFile(TMPDIR, "jpg");
		File outFile2 = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// when
		List<File> results =
			Thumbnails.of("src/test/resources/Exif/source_1.jpg", "src/test/resources/Exif/source_1.jpg")
				.size(100, 100)
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
	public void multipleCallsCorrectOrientation_DifferentOrientation_asFiles() throws IOException
	{
		// given
		File outFile1 = TestUtils.createTempFile(TMPDIR, "jpg");
		File outFile2 = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// when
		List<File> results =
			Thumbnails.of("src/test/resources/Exif/source_2.jpg", "src/test/resources/Exif/source_1.jpg")
				.size(100, 100)
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
	public void multipleCallsCorrectOrientation_SameOrientation_asFilesRename() throws IOException
	{
		// given
		File sourceFile1 = TestUtils.createTempFile(TMPDIR, "jpg");
		File sourceFile2 = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(new File("src/test/resources/Exif/source_1.jpg"), sourceFile1);
		TestUtils.copyFile(new File("src/test/resources/Exif/source_1.jpg"), sourceFile2);
		
		// when
		List<File> results =
			Thumbnails.of(sourceFile1, sourceFile2)
				.size(100, 100)
				.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
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
	public void multipleCallsCorrectOrientation_DifferentOrientation_asFilesRename() throws IOException
	{
		// given
		File sourceFile1 = TestUtils.createTempFile(TMPDIR, "jpg");
		File sourceFile2 = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(new File("src/test/resources/Exif/source_2.jpg"), sourceFile1);
		TestUtils.copyFile(new File("src/test/resources/Exif/source_1.jpg"), sourceFile2);
		
		// when
		List<File> results =
			Thumbnails.of(sourceFile1, sourceFile2)
				.size(100, 100)
				.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
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
	public void multipleCallsCorrectOrientation_SameOrientation_toFiles() throws IOException
	{
		// given
		File outFile1 = TestUtils.createTempFile(TMPDIR, "jpg");
		File outFile2 = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// when
		Thumbnails.of("src/test/resources/Exif/source_1.jpg", "src/test/resources/Exif/source_1.jpg")
			.size(100, 100)
			.toFiles(Arrays.asList(outFile1, outFile2));
		
		// then
		BufferedImageAssert.assertMatches(
				ImageIO.read(outFile1),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
		BufferedImageAssert.assertMatches(
				ImageIO.read(outFile2),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}

	@Test
	public void multipleCallsCorrectOrientation_DifferentOrientation_toFiles() throws IOException
	{
		// given
		File outFile1 = TestUtils.createTempFile(TMPDIR, "jpg");
		File outFile2 = TestUtils.createTempFile(TMPDIR, "jpg");
		
		// when
		Thumbnails.of("src/test/resources/Exif/source_2.jpg", "src/test/resources/Exif/source_1.jpg")
			.size(100, 100)
			.toFiles(Arrays.asList(outFile1, outFile2));
		
		// then
		BufferedImageAssert.assertMatches(
				ImageIO.read(outFile1),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
		BufferedImageAssert.assertMatches(
				ImageIO.read(outFile2),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}

	@Test
	public void multipleCallsCorrectOrientation_SameOrientation_toFilesRename() throws IOException
	{
		// given
		File sourceFile1 = TestUtils.createTempFile(TMPDIR, "jpg");
		File sourceFile2 = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(new File("src/test/resources/Exif/source_1.jpg"), sourceFile1);
		TestUtils.copyFile(new File("src/test/resources/Exif/source_1.jpg"), sourceFile2);
		
		// when
		Thumbnails.of(sourceFile1, sourceFile2)
			.size(100, 100)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		BufferedImageAssert.assertMatches(
				ImageIO.read(new File(TMPDIR, "thumbnail." + sourceFile1.getName())),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
		BufferedImageAssert.assertMatches(
				ImageIO.read(new File(TMPDIR, "thumbnail." + sourceFile2.getName())),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}

	@Test
	public void multipleCallsCorrectOrientation_DifferentOrientation_toFilesRename() throws IOException
	{
		// given
		File sourceFile1 = TestUtils.createTempFile(TMPDIR, "jpg");
		File sourceFile2 = TestUtils.createTempFile(TMPDIR, "jpg");
		TestUtils.copyFile(new File("src/test/resources/Exif/source_2.jpg"), sourceFile1);
		TestUtils.copyFile(new File("src/test/resources/Exif/source_1.jpg"), sourceFile2);
		
		// when
		Thumbnails.of(sourceFile1, sourceFile2)
			.size(100, 100)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		BufferedImageAssert.assertMatches(
				ImageIO.read(new File(TMPDIR, "thumbnail." + sourceFile1.getName())),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
		BufferedImageAssert.assertMatches(
				ImageIO.read(new File(TMPDIR, "thumbnail." + sourceFile2.getName())),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}

	@Test
	public void multipleCallsCorrectOrientation_SameOrientation_toOutputStreams() throws IOException
	{
		// given
		ByteArrayOutputStream os1 = new ByteArrayOutputStream();
		ByteArrayOutputStream os2 = new ByteArrayOutputStream();
				
		// when
		Thumbnails.of("src/test/resources/Exif/source_1.jpg", "src/test/resources/Exif/source_1.jpg")
			.size(100, 100)
			.toOutputStreams(Arrays.asList(os1, os2));
		
		// then
		BufferedImageAssert.assertMatches(
				ImageIO.read(new ByteArrayInputStream(os1.toByteArray())),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
		BufferedImageAssert.assertMatches(
				ImageIO.read(new ByteArrayInputStream(os2.toByteArray())),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}

	@Test
	public void multipleCallsCorrectOrientation_DifferentOrientation_toOutputStreams() throws IOException
	{
		// given
		ByteArrayOutputStream os1 = new ByteArrayOutputStream();
		ByteArrayOutputStream os2 = new ByteArrayOutputStream();
		
		// when
		Thumbnails.of("src/test/resources/Exif/source_2.jpg", "src/test/resources/Exif/source_1.jpg")
			.size(100, 100)
			.toOutputStreams(Arrays.asList(os1, os2));
		
		// then
		BufferedImageAssert.assertMatches(
				ImageIO.read(new ByteArrayInputStream(os1.toByteArray())),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
		BufferedImageAssert.assertMatches(
				ImageIO.read(new ByteArrayInputStream(os2.toByteArray())),
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}

	@Test
	public void useExifOrientationIsTrue_OrientationHonored() throws IOException
	{
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
	public void useExifOrientationIsFalse_OrientationIgnored() throws IOException
	{
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
	public void toOutputStreamFailsWithoutOutputFormatSpecifiedForBufferedImage() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		
		try
		{
			// when
			Thumbnails.of(img)
				.size(100, 100)
				.toOutputStream(baos);
			
			fail();
		}
		catch (Exception e)
		{
			// then
		}
	}
	
	@Test
	public void toOutputStreamImageFormatMatchesInputForPngStream() throws IOException
	{
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
	public void toOutputStreamImageFormatMatchesInputForJpegStream() throws IOException
	{
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
	public void toOutputStreamImageFormatMatchesInputForPngFile() throws IOException
	{
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
	public void toOutputStreamImageFormatMatchesInputForJpegFile() throws IOException
	{
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
	public void toOutputStreamImageFormatMatchesOutputFormatForPng() throws IOException
	{
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
	public void toOutputStreamImageFormatMatchesOutputFormatForJpeg() throws IOException
	{
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
	public void toOutputStreamImageFormatMatchesOutputFormatForPngWithBufferedImageInput() throws IOException
	{
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
	public void toOutputStreamImageFormatMatchesOutputFormatForJpegWithBufferedImageInput() throws IOException
	{
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
	
	private File makeRenamedFile(File f, Rename rename)
	{
		ThumbnailParameter param =
			new ThumbnailParameterBuilder()
				.size(100, 100)
				.build();
		
		return new File(f.getParent(), Rename.PREFIX_DOT_THUMBNAIL.apply(f.getName(), param));
	}
}
