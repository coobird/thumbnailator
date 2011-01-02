package net.coobird.thumbnailator;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.name.ConsecutivelyNumberedFilenames;
import net.coobird.thumbnailator.name.Rename;

import org.junit.Test;

public class ThumbnailsBuilderInputOutputTest
{

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(BufferedImage...)</li>
	 * <li>where the BufferedImage[] is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void of_BufferedImage_null()
	{
		BufferedImage[] img = null;
		
		try
		{
			Thumbnails.of(img);
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Cannot specify null for images.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(File...)</li>
	 * <li>where the File[] is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void of_File_null()
	{
		File[] f = null;
		
		try
		{
			Thumbnails.of(f);
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Cannot specify null for input files.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(String...)</li>
	 * <li>where the String[] is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void of_Strings_null()
	{
		String[] f = null;
		
		try
		{
			Thumbnails.of(f);
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Cannot specify null for input files.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(BufferedImage...)</li>
	 * <li>where the BufferedImage[] is length 0.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void of_BufferedImage_empty()
	{
		BufferedImage[] img = new BufferedImage[0];
		
		try
		{
			Thumbnails.of(img);
			fail();
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Cannot specify an empty array for images.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(File...)</li>
	 * <li>where the File[] is length 0.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void of_File_empty()
	{
		File[] f = new File[0];
		
		try
		{
			Thumbnails.of(f);
			fail();
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Cannot specify an empty array for input files.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(String...)</li>
	 * <li>where the String[] is length 0.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void of_Strings_empty()
	{
		String[] f = new String[0];
		
		try
		{
			Thumbnails.of(f);
			fail();
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Cannot specify an empty array for input files.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromImages(Collection)</li>
	 * <li>where the Collection is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void fromImages_Collection_null()
	{
		try
		{
			Thumbnails.fromImages(null);
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Cannot specify null for images.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFiles(Collection)</li>
	 * <li>where the Collection is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void fromFiles_Collection_null()
	{
		try
		{
			Thumbnails.fromFiles(null);
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Cannot specify null for input files.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFilenames(Collection)</li>
	 * <li>where the Collection is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void fromFilenames_Collection_null()
	{
		try
		{
			Thumbnails.fromFilenames(null);
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Cannot specify null for input files.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromImages(Collection)</li>
	 * <li>where the Collection is empty.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void fromImages_Collection_empty()
	{
		try
		{
			Thumbnails.fromImages(Collections.<BufferedImage>emptyList());
			fail();
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Cannot specify an empty collection for images.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFiles(Collection)</li>
	 * <li>where the Collection is empty.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void fromFiles_Collection_empty()
	{
		try
		{
			Thumbnails.fromFiles(Collections.<File>emptyList());
			fail();
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Cannot specify an empty collection for input files.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFilenames(Collection)</li>
	 * <li>where the Collection is empty.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void fromFilenames_Collection_empty()
	{
		try
		{
			Thumbnails.fromFilenames(Collections.<String>emptyList());
			fail();
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Cannot specify an empty collection for input files.", e.getMessage());
			throw e;
		}
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
	public void of_BufferedImage_asBufferedImage() throws IOException
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		BufferedImage thumbnail = Thumbnails.of(img)
			.size(100, 100)
			.asBufferedImage();
		
		assertEquals(100, thumbnail.getWidth());
		assertEquals(100, thumbnail.getHeight());
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
	public void of_BufferedImages_asBufferedImage() throws IOException
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		Thumbnails.of(img, img)
			.size(100, 100)
			.asBufferedImage();
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
	public void of_BufferedImage_asBufferedImages() throws IOException
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		List<BufferedImage> thumbnails = Thumbnails.of(img)
			.size(100, 100)
			.asBufferedImages();
		
		assertEquals(100, thumbnails.get(0).getWidth());
		assertEquals(100, thumbnails.get(0).getHeight());
		assertEquals(1, thumbnails.size());
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
	public void of_BufferedImages_asBufferedImages() throws IOException
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		List<BufferedImage> thumbnails = Thumbnails.of(img, img)
			.size(100, 100)
			.asBufferedImages();
		
		assertEquals(100, thumbnails.get(0).getWidth());
		assertEquals(100, thumbnails.get(0).getHeight());
		assertEquals(100, thumbnails.get(1).getWidth());
		assertEquals(100, thumbnails.get(1).getHeight());
		assertEquals(2, thumbnails.size());
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
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		BufferedImage thumbnail = Thumbnails.fromImages(Arrays.asList(img))
			.size(100, 100)
			.asBufferedImage();
		
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
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		Thumbnails.fromImages(Arrays.asList(img, img))
			.size(100, 100)
			.asBufferedImage();
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
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		List<BufferedImage> thumbnails = Thumbnails.fromImages(Arrays.asList(img))
			.size(100, 100)
			.asBufferedImages();
		
		assertEquals(100, thumbnails.get(0).getWidth());
		assertEquals(100, thumbnails.get(0).getHeight());
		assertEquals(1, thumbnails.size());
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
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		List<BufferedImage> thumbnails = Thumbnails.fromImages(Arrays.asList(img, img))
			.size(100, 100)
			.asBufferedImages();
		
		assertEquals(100, thumbnails.get(0).getWidth());
		assertEquals(100, thumbnails.get(0).getHeight());
		assertEquals(100, thumbnails.get(1).getWidth());
		assertEquals(100, thumbnails.get(1).getHeight());
		assertEquals(2, thumbnails.size());
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
		File f = new File("test-resources/Thumbnailator/grid.png");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.toFile(outFile);
		
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
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		Thumbnails.of(f1)
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
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
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		List<File> thumbnails = Thumbnails.of(f1)
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
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
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		
		// when
		Thumbnails.of(f1)
			.size(50, 50)
			.toFiles(new ConsecutivelyNumberedFilenames(new File("test-resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		File outFile = new File("test-resources/Thumbnailator/temp-0.png");
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
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		
		// when
		List<File> thumbnails = Thumbnails.of(f1)
			.size(50, 50)
			.asFiles(new ConsecutivelyNumberedFilenames(new File("test-resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		File outFile1 = new File("test-resources/Thumbnailator/temp-0.png");
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
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		
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
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		
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
		File f = new File("test-resources/Thumbnailator/grid.png");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
		Thumbnails.of(f, f)
			.size(50, 50)
			.toFile(outFile);
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
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File f2 = new File("test-resources/Thumbnailator/grid.jpg");
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		Thumbnails.of(f1, f2)
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
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
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File f2 = new File("test-resources/Thumbnailator/grid.jpg");
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		List<File> thumbnails = Thumbnails.of(f1, f2)
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
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
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File f2 = new File("test-resources/Thumbnailator/grid.jpg");

		// when
		Thumbnails.of(f1, f2)
			.size(50, 50)
			.toFiles(new ConsecutivelyNumberedFilenames(new File("test-resources/Thumbnailator"), "temp-%d.png"));
		
		// then
		File outFile1 = new File("test-resources/Thumbnailator/temp-0.png");
		File outFile2 = new File("test-resources/Thumbnailator/temp-1.png.JPEG");
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
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File f2 = new File("test-resources/Thumbnailator/grid.jpg");

		// when
		List<File> thumbnails = Thumbnails.of(f1, f2)
			.size(50, 50)
			.asFiles(new ConsecutivelyNumberedFilenames(new File("test-resources/Thumbnailator"), "temp-%d.png"));
		
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
	public void of_Files_asBufferedImage() throws IOException
	{
		// given
		File f = new File("test-resources/Thumbnailator/grid.png");
		
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
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File f2 = new File("test-resources/Thumbnailator/grid.jpg");
		
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
		File f = new File("test-resources/Thumbnailator/grid.png");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
		Thumbnails.fromFiles(Arrays.asList(f))
			.size(50, 50)
			.toFile(outFile);
		
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
		File f = new File("test-resources/Thumbnailator/grid.png");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
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
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		Thumbnails.fromFiles(Arrays.asList(f1))
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
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
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File f2 = new File("test-resources/Thumbnailator/grid.jpg");
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		Thumbnails.fromFiles(Arrays.asList(f1, f2))
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
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
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		List<File> thumbnails = Thumbnails.fromFiles(Arrays.asList(f1))
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
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
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File f2 = new File("test-resources/Thumbnailator/grid.jpg");
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		List<File> thumbnails = Thumbnails.fromFiles(Arrays.asList(f1, f2))
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		assertEquals(2, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
		BufferedImage fromFileImage2 = ImageIO.read(thumbnails.get(0));
		
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
		String f = "test-resources/Thumbnailator/grid.png";
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.toFile(outFile);
		
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
		String f = "test-resources/Thumbnailator/grid.png";
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
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
		String f1 = "test-resources/Thumbnailator/grid.png";
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		Thumbnails.of(f1)
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
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
		String f1 = "test-resources/Thumbnailator/grid.png";
		String f2 = "test-resources/Thumbnailator/grid.jpg";
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		Thumbnails.of(f1, f2)
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
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
		String f1 = "test-resources/Thumbnailator/grid.png";
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		List<File> thumbnails = Thumbnails.of(f1)
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
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
		String f1 = "test-resources/Thumbnailator/grid.png";
		String f2 = "test-resources/Thumbnailator/grid.jpg";
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		List<File> thumbnails = Thumbnails.of(f1, f2)
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
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
		String f = "test-resources/Thumbnailator/grid.png";
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
		Thumbnails.fromFilenames(Arrays.asList(f))
			.size(50, 50)
			.toFile(outFile);
		
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
		String f = "test-resources/Thumbnailator/grid.png";
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
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
		String f1 = "test-resources/Thumbnailator/grid.png";
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		Thumbnails.fromFilenames(Arrays.asList(f1))
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
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
		String f1 = "test-resources/Thumbnailator/grid.png";
		String f2 = "test-resources/Thumbnailator/grid.jpg";
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		Thumbnails.fromFilenames(Arrays.asList(f1, f2))
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
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
		String f1 = "test-resources/Thumbnailator/grid.png";
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		List<File> thumbnails = Thumbnails.fromFilenames(Arrays.asList(f1))
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
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
		String f1 = "test-resources/Thumbnailator/grid.png";
		String f2 = "test-resources/Thumbnailator/grid.jpg";
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		List<File> thumbnails = Thumbnails.fromFilenames(Arrays.asList(f1, f2))
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		assertEquals(2, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		BufferedImage fromFileImage2 = ImageIO.read(outFile2);
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		assertEquals(50, fromFileImage2.getWidth());
		assertEquals(50, fromFileImage2.getHeight());
	}
}
