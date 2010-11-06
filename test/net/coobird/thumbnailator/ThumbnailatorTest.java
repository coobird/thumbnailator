package net.coobird.thumbnailator;

import static org.junit.Assert.*;

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

import org.junit.Ignore;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ThumbnailatorTest
{
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnailCollection(Collection, Rename, int, int)}
	 * where,
	 * 
	 * 1) Width is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnailCollections_negativeWidth() throws IOException
	{
		/*
		 * The files to make thumbnails of.
		 */
		List<File> files = Arrays.asList(
				new File("test-resources/Thumbnailator/grid.jpg"),
				new File("test-resources/Thumbnailator/grid.png")
		);
		
		Thumbnailator.createThumbnailsAsCollection(
				files,
				Rename.PREFIX_DOT_THUMBNAIL,
				-42,
				50
		);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnailsAsCollection(Collection, Rename, int, int)}
	 * where,
	 * 
	 * 1) Height is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnailCollections_negativeHeight() throws IOException
	{
		/*
		 * The files to make thumbnails of.
		 */
		List<File> files = Arrays.asList(
				new File("test-resources/Thumbnailator/grid.jpg"),
				new File("test-resources/Thumbnailator/grid.png")
		);
		
		Thumbnailator.createThumbnailsAsCollection(
				files,
				Rename.PREFIX_DOT_THUMBNAIL,
				50,
				-42
		);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnailsAsCollection(Collection, Rename, int, int)}
	 * where,
	 * 
	 * 1) Width is negative.
	 * 2) Height is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnailCollections_negativeWidthAndHeight() throws IOException
	{
		/*
		 * The files to make thumbnails of.
		 */
		List<File> files = Arrays.asList(
				new File("test-resources/Thumbnailator/grid.jpg"),
				new File("test-resources/Thumbnailator/grid.png")
		);
		
		Thumbnailator.createThumbnailsAsCollection(
				files,
				Rename.PREFIX_DOT_THUMBNAIL,
				-42,
				-42
		);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnailsAsCollection(Collection, Rename, int, int)}
	 * where,
	 * 
	 * 1) All parameters are correct, except
	 *    a) The Collection is null
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an NullPointerException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=NullPointerException.class)
	public void testCreateThumbnailCollections_nullCollection() throws IOException
	{
		try
		{
			Thumbnailator.createThumbnailsAsCollection(
					null,
					Rename.PREFIX_DOT_THUMBNAIL,
					50,
					50
			);
			
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Collection of Files is null.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnailsAsCollection(Collection, Rename, int, int)}
	 * where,
	 * 
	 * 1) All parameters are correct, except
	 *    a) The Rename is null
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an NullPointerException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=NullPointerException.class)
	public void testCreateThumbnailCollections_nullRename() throws IOException
	{
		/*
		 * The files to make thumbnails of.
		 */
		List<File> files = Arrays.asList(
				new File("test-resources/Thumbnailator/grid.jpg"),
				new File("test-resources/Thumbnailator/grid.png"),
				new File("test-resources/Thumbnailator/grid.bmp")
		);
		
		try
		{
			Thumbnailator.createThumbnailsAsCollection(
					files,
					null,
					50,
					50
			);
			
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Rename is null.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnailsAsCollection(Collection, Rename, int, int)}
	 * where,
	 * 
	 * 1) All parameters are correct
	 *    a) The Collection is an empty List.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will complete successfully.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateThumbnailCollections_NoErrors_EmptyList() throws IOException
	{
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
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnailsAsCollection(Collection, Rename, int, int)}
	 * where,
	 * 
	 * 1) All parameters are correct
	 *    a) The Collection is an empty Set.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will complete successfully.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateThumbnailCollections_NoErrors_EmptySet() throws IOException
	{
		/*
		 * The files to make thumbnails of -- nothing!
		 */
		Set<File> files = Collections.emptySet();
		
		Thumbnailator.createThumbnailsAsCollection(
				files,
				Rename.PREFIX_DOT_THUMBNAIL,
				50,
				50
		);
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnailsAsCollection(Collection, Rename, int, int)}
	 * where,
	 * 
	 * 1) All parameters are correct
	 * 2) All data can be processed correctly.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will complete successfully.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateThumbnailCollections_NoErrors() throws IOException
	{
		/*
		 * The files to make thumbnails of.
		 */
		List<File> files = Arrays.asList(
				new File("test-resources/Thumbnailator/grid.jpg"),
				new File("test-resources/Thumbnailator/grid.png")
		);
		
		/*
		 * Used to perform clean up.
		 */
		for (File f : files)
		{
			String fileName = f.getName();
			String newFileName = 
				Rename.PREFIX_DOT_THUMBNAIL.apply(fileName);
			
			new File(f.getParent(), newFileName).deleteOnExit();
		}
		
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
	 * 
	 * 1) All parameters are correct
	 * 2) An problem occurs while processing.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IOException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IOException.class)
	public void testCreateThumbnailCollections_ErrorDuringProcessing() throws IOException
	{
		/*
		 * The files to make thumbnails of.
		 */
		List<File> files = Arrays.asList(
				new File("test-resources/Thumbnailator/grid.jpg"),
				new File("test-resources/Thumbnailator/grid.png"),
				new File("test-resources/Thumbnailator/grid.bmp")
		);
		
		/*
		 * Used to perform clean up.
		 */
		for (File f : files)
		{
			String fileName = f.getName();
			String newFileName = 
				Rename.PREFIX_DOT_THUMBNAIL.apply(fileName);
			
			new File(f.getParent(), newFileName).deleteOnExit();
		}
		
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
	 * 
	 * 1) All parameters are correct
	 * 2) All data can be processed correctly.
	 * 3) The Collection is a List of a class extending File.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will complete successfully.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateThumbnailCollections_NoErrors_CollectionExtendsFile() throws IOException
	{
		class File2 extends File
		{
			private static final long serialVersionUID = 1L;
			public File2(String pathname)
			{
				super(pathname);
			}
		}
		
		/*
		 * The files to make thumbnails of.
		 */
		List<File2> files = Arrays.asList(
				new File2("test-resources/Thumbnailator/grid.jpg"),
				new File2("test-resources/Thumbnailator/grid.png")
		);
		
		/*
		 * Used to perform clean up.
		 */
		for (File f : files)
		{
			String fileName = f.getName();
			String newFileName = 
				Rename.PREFIX_DOT_THUMBNAIL.apply(fileName);
			
			new File(f.getParent(), newFileName).deleteOnExit();
		}
		
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
	 * 
	 * 1) Width is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnails_negativeWidth() throws IOException
	{
		/*
		 * The files to make thumbnails of.
		 */
		List<File> files = Arrays.asList(
				new File("test-resources/Thumbnailator/grid.jpg"),
				new File("test-resources/Thumbnailator/grid.png")
		);
		
		Thumbnailator.createThumbnails(
				files,
				Rename.PREFIX_DOT_THUMBNAIL,
				-42,
				50
		);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnails(Collection, Rename, int, int)}
	 * where,
	 * 
	 * 1) Height is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnails_negativeHeight() throws IOException
	{
		/*
		 * The files to make thumbnails of.
		 */
		List<File> files = Arrays.asList(
				new File("test-resources/Thumbnailator/grid.jpg"),
				new File("test-resources/Thumbnailator/grid.png")
		);
		
		Thumbnailator.createThumbnails(
				files,
				Rename.PREFIX_DOT_THUMBNAIL,
				50,
				-42
		);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnails(Collection, Rename, int, int)}
	 * where,
	 * 
	 * 1) Width is negative.
	 * 2) Height is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnails_negativeWidthAndHeight() throws IOException
	{
		/*
		 * The files to make thumbnails of.
		 */
		List<File> files = Arrays.asList(
				new File("test-resources/Thumbnailator/grid.jpg"),
				new File("test-resources/Thumbnailator/grid.png")
		);
		
		Thumbnailator.createThumbnails(
				files,
				Rename.PREFIX_DOT_THUMBNAIL,
				-42,
				-42
		);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnails(Collection, Rename, int, int)}
	 * where,
	 * 
	 * 1) All parameters are correct, except
	 *    a) The Collection is null
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an NullPointerException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=NullPointerException.class)
	public void testCreateThumbnails_nullCollection() throws IOException
	{
		try
		{
			Thumbnailator.createThumbnails(
					null,
					Rename.PREFIX_DOT_THUMBNAIL,
					50,
					50
			);
			
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Collection of Files is null.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnails(Collection, Rename, int, int)}
	 * where,
	 * 
	 * 1) All parameters are correct, except
	 *    a) The Rename is null
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an NullPointerException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=NullPointerException.class)
	public void testCreateThumbnails_nullRename() throws IOException
	{
		/*
		 * The files to make thumbnails of.
		 */
		List<File> files = Arrays.asList(
				new File("test-resources/Thumbnailator/grid.jpg"),
				new File("test-resources/Thumbnailator/grid.png"),
				new File("test-resources/Thumbnailator/grid.bmp")
		);
		
		try
		{
			Thumbnailator.createThumbnails(
					files,
					null,
					50,
					50
			);
			
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Rename is null.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnails(Collection, Rename, int, int)}
	 * where,
	 * 
	 * 1) All parameters are correct
	 *    a) The Collection is an empty List.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will complete successfully.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateThumbnails_NoErrors_EmptyList() throws IOException
	{
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
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnails(Collection, Rename, int, int)}
	 * where,
	 * 
	 * 1) All parameters are correct
	 *    a) The Collection is an empty Set.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will complete successfully.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateThumbnails_NoErrors_EmptySet() throws IOException
	{
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
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnails(Collection, Rename, int, int)}
	 * where,
	 * 
	 * 1) All parameters are correct
	 * 2) All data can be processed correctly.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will complete successfully.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateThumbnails_NoErrors() throws IOException
	{
		/*
		 * The files to make thumbnails of.
		 */
		List<File> files = Arrays.asList(
				new File("test-resources/Thumbnailator/grid.jpg"),
				new File("test-resources/Thumbnailator/grid.png")
		);
		
		/*
		 * Used to perform clean up.
		 */
		for (File f : files)
		{
			String fileName = f.getName();
			String newFileName = 
				Rename.PREFIX_DOT_THUMBNAIL.apply(fileName);
			
			new File(f.getParent(), newFileName).deleteOnExit();
		}
		
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
			ImageIO.read(new File("test-resources/Thumbnailator/thumbnail.grid.jpg"));
		
		assertEquals(50, img0.getWidth());
		assertEquals(50, img0.getHeight());
		
		BufferedImage img1 = 
			ImageIO.read(new File("test-resources/Thumbnailator/thumbnail.grid.png"));
		
		assertEquals(50, img1.getWidth());
		assertEquals(50, img1.getHeight());
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnails(Collection, Rename, int, int)}
	 * where,
	 * 
	 * 1) All parameters are correct
	 * 2) An problem occurs while processing.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IOException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IOException.class)
	public void testCreateThumbnails_ErrorDuringProcessing() throws IOException
	{
		/*
		 * The files to make thumbnails of.
		 */
		List<File> files = Arrays.asList(
				new File("test-resources/Thumbnailator/grid.jpg"),
				new File("test-resources/Thumbnailator/grid.png"),
				new File("test-resources/Thumbnailator/grid.bmp")
		);
		
		/*
		 * Used to perform clean up.
		 */
		for (File f : files)
		{
			String fileName = f.getName();
			String newFileName = 
				Rename.PREFIX_DOT_THUMBNAIL.apply(fileName);
			
			new File(f.getParent(), newFileName).deleteOnExit();
		}
		
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
	 * {@link Thumbnailator#createThumbnail(InputStream, OutputStream, int, int)}
	 * where,
	 * 
	 * 1) InputStream is null
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an NullPointerException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=NullPointerException.class)
	public void testCreateThumbnail_IOII_nullIS() throws IOException
	{
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
	 * 
	 * 1) OutputStream is null
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an NullPointerException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=NullPointerException.class)
	public void testCreateThumbnail_IOII_nullOS() throws IOException
	{
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
	 * 
	 * 1) InputStream is null
	 * 2) OutputStream is null
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an NullPointerException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=NullPointerException.class)
	public void testCreateThumbnail_IOII_nullISnullOS() throws IOException
	{
		Thumbnailator.createThumbnail((InputStream)null, null, 50, 50);
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(InputStream, OutputStream, int, int)}
	 * where,
	 * 
	 * 1) Width is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnail_IOII_negativeWidth() throws IOException
	{
		/*
		 * Actual test
		 */
		byte[] bytes = makeImageData("jpg", 200, 200);
		InputStream is = new ByteArrayInputStream(bytes);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		Thumbnailator.createThumbnail(is, os, -42, 50);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(InputStream, OutputStream, int, int)}
	 * where,
	 * 
	 * 1) Height is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnail_IOII_negativeHeight() throws IOException
	{
		/*
		 * Actual test
		 */
		byte[] bytes = makeImageData("jpg", 200, 200);
		InputStream is = new ByteArrayInputStream(bytes);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		Thumbnailator.createThumbnail(is, os, 50, -42);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(InputStream, OutputStream, int, int)}
	 * where,
	 * 
	 * 1) Width is negative.
	 * 2) Height is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnail_IOII_negativeWidthAndHeight() throws IOException
	{
		/*
		 * Actual test
		 */
		byte[] bytes = makeImageData("jpg", 200, 200);
		InputStream is = new ByteArrayInputStream(bytes);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		Thumbnailator.createThumbnail(is, os, -42, -42);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(InputStream, OutputStream, int, int)}
	 * where,
	 * 
	 * 1) Method arguments are correct
	 * 2) Input data is a JPEG image
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will complete successfully.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateThumbnail_IOII_Jpg() throws IOException
	{
		/*
		 * Actual test
		 */
		byte[] bytes = makeImageData("jpg", 200, 200);
		InputStream is = new ByteArrayInputStream(bytes);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		Thumbnailator.createThumbnail(is, os, 50, 50);
		
		/*
		 * Post-test checks
		 */
		InputStream thumbIs = new ByteArrayInputStream(os.toByteArray());
		BufferedImage thumb = ImageIO.read(thumbIs);
		
		assertEquals(50, thumb.getWidth());
		assertEquals(50, thumb.getHeight());
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(InputStream, OutputStream, int, int)}
	 * where,
	 * 
	 * 1) Method arguments are correct
	 * 2) Input data is a PNG image
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will complete successfully.
	 * 
	 * @throws IOException
	 */	
	@Test
	public void testCreateThumbnail_IOII_Png() throws IOException
	{
		/*
		 * Actual test
		 */
		byte[] bytes = makeImageData("png", 200, 200);
		InputStream is = new ByteArrayInputStream(bytes);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		Thumbnailator.createThumbnail(is, os, 50, 50);
		
		/*
		 * Post-test checks
		 */
		InputStream thumbIs = new ByteArrayInputStream(os.toByteArray());
		BufferedImage thumb = ImageIO.read(thumbIs);
		
		assertEquals(50, thumb.getWidth());
		assertEquals(50, thumb.getHeight());
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(InputStream, OutputStream, int, int)}
	 * where,
	 * 
	 * 1) Method arguments are correct
	 * 2) Input data is a BMP image 
	 *    -> writing to a BMP is not supported by default.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IOException.
	 * 
	 * @throws IOException
	 */	
	@Test(expected=IOException.class)
	public void testCreateThumbnail_IOII_Bmp() throws IOException
	{
		/*
		 * Actual test
		 */
		byte[] bytes = makeImageData("bmp", 200, 200);
		InputStream is = new ByteArrayInputStream(bytes);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		Thumbnailator.createThumbnail(is, os, 50, 50);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(InputStream, OutputStream, int, int)}
	 * where,
	 * 
	 * 1) InputStream throws an IOException during read.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IOException.
	 * 
	 * @throws IOException
	 */	
	@Test(expected=IOException.class)
	public void testCreateThumbnail_IOII_IOExceptionFromIS() throws IOException
	{
		/*
		 * Actual test
		 */
		InputStream is = mock(InputStream.class);
		doThrow(new IOException("read error!")).when(is).read();
		doThrow(new IOException("read error!")).when(is).read((byte[])any());
		doThrow(new IOException("read error!")).when(is).read((byte[])any(), anyInt(), anyInt());
		
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		Thumbnailator.createThumbnail(is, os, 50, 50);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(InputStream, OutputStream, int, int)}
	 * where,
	 * 
	 * 1) OutputStream throws an IOException during read.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IOException.
	 * 
	 * @throws IOException
	 */	
	@Test(expected=IOException.class)
	public void testCreateThumbnail_IOII_IOExceptionFromOS() throws IOException
	{
		/*
		 * Actual test
		 */
		byte[] bytes = makeImageData("png", 200, 200);
		InputStream is = new ByteArrayInputStream(bytes);
		
		OutputStream os = mock(OutputStream.class);
		doThrow(new IOException("write error!")).when(os).write(anyInt());
		doThrow(new IOException("write error!")).when(os).write((byte[])any());
		doThrow(new IOException("write error!")).when(os).write((byte[])any(), anyInt(), anyInt());
		
		Thumbnailator.createThumbnail(is, os, 50, 50);
		
		fail();
	}

	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(File, File, int, int)}
	 * where,
	 * 
	 * 1) Input File is null
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an NullPointerException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=NullPointerException.class)
	public void testCreateThumbnail_FFII_nullInputFile() throws IOException
	{
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
	 * 
	 * 1) Output File is null
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an NullPointerException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=NullPointerException.class)
	public void testCreateThumbnail_FFII_nullOutputFile() throws IOException
	{
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
	 * 
	 * 1) Input File is null
	 * 2) Output File is null
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an NullPointerException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=NullPointerException.class)
	public void testCreateThumbnail_FFII_nullInputAndOutputFiles() throws IOException
	{
		Thumbnailator.createThumbnail((File)null, null, 50, 50);
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(File, File, int, int)}
	 * where,
	 * 
	 * 1) Width is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnail_FFII_negativeWidth() throws IOException
	{
		/*
		 * Actual test
		 */
		File inputFile = new File("foo.jpg");
		File outputFile = new File("bar.jpg");
		
		Thumbnailator.createThumbnail(inputFile, outputFile, -42, 50);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(File, File, int, int)}
	 * where,
	 * 
	 * 1) Height is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnail_FFII_negativeHeight() throws IOException
	{
		/*
		 * Actual test
		 */
		File inputFile = new File("foo.jpg");
		File outputFile = new File("bar.jpg");
		
		Thumbnailator.createThumbnail(inputFile, outputFile, 50, -42);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(File, File, int, int)}
	 * where,
	 * 
	 * 1) Width is negative.
	 * 2) Height is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnail_FFII_negativeWidthAndHeight() throws IOException
	{
		/*
		 * Actual test
		 */
		File inputFile = new File("foo.jpg");
		File outputFile = new File("bar.jpg");
		
		Thumbnailator.createThumbnail(inputFile, outputFile, -42, -42);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(File, File, int, int)}
	 * where,
	 * 
	 * 1) Method arguments are correct
	 * 2) Input file is a JPEG image
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will complete successfully.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateThumbnail_FFII_Jpg() throws IOException
	{
		/*
		 * Actual test
		 */
		File inputFile = new File("test-resources/Thumbnailator/grid.jpg");
		File outputFile = new File("test-resources/Thumbnailator/tmp.jpg");
		outputFile.deleteOnExit();
		
		Thumbnailator.createThumbnail(inputFile, outputFile, 50, 50);
		
		assertTrue(outputFile.exists());
		BufferedImage img = ImageIO.read(outputFile);
		assertEquals(50, img.getWidth());
		assertEquals(50, img.getHeight());
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(File, File, int, int)}
	 * where,
	 * 
	 * 1) Method arguments are correct
	 * 2) Input file is a PNG image
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will complete successfully.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateThumbnail_FFII_Png() throws IOException
	{
		/*
		 * Actual test
		 */
		File inputFile = new File("test-resources/Thumbnailator/grid.png");
		File outputFile = new File("test-resources/Thumbnailator/tmp.png");
		outputFile.deleteOnExit();
		
		Thumbnailator.createThumbnail(inputFile, outputFile, 50, 50);
		
		assertTrue(outputFile.exists());
		BufferedImage img = ImageIO.read(outputFile);
		assertEquals(50, img.getWidth());
		assertEquals(50, img.getHeight());
	}

	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(File, File, int, int)}
	 * where,
	 * 
	 * 1) Method arguments are correct
	 * 2) Input data is a BMP image 
	 *    -> writing to a BMP is not supported by default.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IOException.
	 * 
	 * @throws IOException
	 */	
	@Test(expected=IOException.class)
	public void testCreateThumbnail_FFII_Bmp() throws IOException
	{
		/*
		 * Actual test
		 */
		File inputFile = new File("test-resources/Thumbnailator/grid.bmp");
		File outputFile = new File("test-resources/Thumbnailator/tmp.bmp");
		outputFile.deleteOnExit();
		
		Thumbnailator.createThumbnail(inputFile, outputFile, 50, 50);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(File, File, int, int)}
	 * where,
	 * 
	 * 1) Method arguments are correct
	 * 2) There is transcoding taking place:
	 *   a) Input file is a JPEG image
	 *   b) Input file is a PNG image
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will complete successfully.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateThumbnail_FFII_Transcoding() throws IOException
	{
		/*
		 * Actual test
		 */
		File inputFile = new File("test-resources/Thumbnailator/grid.jpg");
		File outputFile = File.createTempFile("thumbnailator-testing-", ".png");
		outputFile.deleteOnExit();
		
		Thumbnailator.createThumbnail(inputFile, outputFile, 50, 50);
		
		assertTrue(outputFile.exists());
		BufferedImage img = ImageIO.read(outputFile);
		assertEquals(
				"png",
				ImageIO.getImageReaders(
						ImageIO.createImageInputStream(outputFile)
				).next().getFormatName()
		);
		assertEquals(50, img.getWidth());
		assertEquals(50, img.getHeight());
	}	
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(File, File, int, int)}
	 * where,
	 * 
	 * 1) Input File does not exist.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IOException.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateThumbnail_FFII_nonExistentInputFile() throws IOException
	{
		/*
		 * Actual test
		 */
		File inputFile = new File("foo.jpg");
		File outputFile = new File("bar.jpg");
		
		try
		{
			Thumbnailator.createThumbnail(inputFile, outputFile, 50, 50);
			fail();
		}
		catch (IOException e)
		{
			assertEquals("Input file does not exist.", e.getMessage());
		}
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(File, File, int, int)}
	 * where,
	 * 
	 * 1) A filename that is invalid
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IOException.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateThumbnail_FFII_invalidOutputFile() throws IOException
	{
		/*
		 * Actual test
		 */
		File inputFile = new File("test-resources/Thumbnailator/grid.jpg");
		File outputFile = new File("@\\*&!!#"); 
		
		try
		{
			Thumbnailator.createThumbnail(inputFile, outputFile, 50, 50);
			fail();
		}
		catch (IOException e)
		{
			assertEquals("Could not open output file.", e.getMessage());
		}
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(File, File, int, int)}
	 * where,
	 * 
	 * 1) A problem occurs while writing to the file.
	 *   
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IOException.
	 * 
	 * @throws IOException
	 */
	@Ignore
	public void testCreateThumbnail_FFII_IOExceptionOnWrite() throws IOException
	{
		//Cannot craft a test case to test this condition.
		fail();
	}

	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(File, int, int)}
	 * where,
	 * 
	 * 1) Input File is null
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an NullPointerException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=NullPointerException.class)
	public void testCreateThumbnail_FII_nullInputFile() throws IOException
	{
		Thumbnailator.createThumbnail((File)null, 50, 50);
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(File, int, int)}
	 * where,
	 * 
	 * 1) Width is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnail_FII_negativeWidth() throws IOException
	{
		/*
		 * Actual test
		 */
		File inputFile = new File("foo.jpg");
		
		Thumbnailator.createThumbnail(inputFile, -42, 50);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(File, int, int)}
	 * where,
	 * 
	 * 1) Height is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnail_FII_negativeHeight() throws IOException
	{
		/*
		 * Actual test
		 */
		File inputFile = new File("foo.jpg");
		
		Thumbnailator.createThumbnail(inputFile, 50, -42);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(File, int, int)}
	 * where,
	 * 
	 * 1) Width is negative.
	 * 2) Height is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 * @throws IOException
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnail_FII_negativeWidthAndHeight() throws IOException
	{
		/*
		 * Actual test
		 */
		File inputFile = new File("foo.jpg");
		
		Thumbnailator.createThumbnail(inputFile, -42, -42);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(File, int, int)}
	 * where,
	 * 
	 * 1) Method arguments are correct
	 * 2) Input file is a JPEG image
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will complete successfully.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateThumbnail_FII_Jpg() throws IOException
	{
		/*
		 * Actual test
		 */
		File inputFile = new File("test-resources/Thumbnailator/grid.jpg");
		
		BufferedImage img = Thumbnailator.createThumbnail(inputFile, 50, 50);
		
		assertEquals(50, img.getWidth());
		assertEquals(50, img.getHeight());
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(File, int, int)}
	 * where,
	 * 
	 * 1) Method arguments are correct
	 * 2) Input file is a PNG image
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will complete successfully.
	 * 
	 * @throws IOException
	 */
	@Test
	public void testCreateThumbnail_FII_Png() throws IOException
	{
		/*
		 * Actual test
		 */
		File inputFile = new File("test-resources/Thumbnailator/grid.png");
		
		BufferedImage img = Thumbnailator.createThumbnail(inputFile, 50, 50);
		
		assertEquals(50, img.getWidth());
		assertEquals(50, img.getHeight());
	}

	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(File, int, int)}
	 * where,
	 * 
	 * 1) Method arguments are correct
	 * 2) Input data is a BMP image
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will complete successfully.
	 * 
	 * @throws IOException
	 */	
	@Test
	public void testCreateThumbnail_FII_Bmp() throws IOException
	{
		/*
		 * Actual test
		 */
		File inputFile = new File("test-resources/Thumbnailator/grid.bmp");
		
		BufferedImage img = Thumbnailator.createThumbnail(inputFile, 50, 50);
		
		assertEquals(50, img.getWidth());
		assertEquals(50, img.getHeight());
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(BufferedImage, int, int)}
	 * where,
	 * 
	 * 1) Width is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnail_BII_negativeWidth()
	{
		/*
		 * Actual test
		 */
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		Thumbnailator.createThumbnail(img, -42, 50);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(BufferedImage, int, int)}
	 * where,
	 * 
	 * 1) Height is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnail_BII_negativeHeight()
	{
		/*
		 * Actual test
		 */
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		Thumbnailator.createThumbnail(img, 50, -42);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(BufferedImage, int, int)}
	 * where,
	 * 
	 * 1) Width is negative.
	 * 2) Height is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnail_BII_negativeWidthAndHeight()
	{
		/*
		 * Actual test
		 */
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		Thumbnailator.createThumbnail(img, -42, -42);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(BufferedImage, int, int)}
	 * where,
	 * 
	 * 1) Method arguments are correct
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will complete successfully.
	 * 
	 */
	@Test
	public void testCreateThumbnail_BII_CorrectUsage()
	{
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
	 * 
	 * 1) Width is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnail_III_negativeWidth()
	{
		/*
		 * Actual test
		 */
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		Thumbnailator.createThumbnail((Image)img, -42, 50);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(Image, int, int)}
	 * where,
	 * 
	 * 1) Height is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnail_III_negativeHeight()
	{
		/*
		 * Actual test
		 */
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		Thumbnailator.createThumbnail((Image)img, 50, -42);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(Image, int, int)}
	 * where,
	 * 
	 * 1) Width is negative.
	 * 2) Height is negative.
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will stop with an IllegalArgumentException.
	 * 
	 */
	@Test(expected=IllegalArgumentException.class)
	public void testCreateThumbnail_III_negativeWidthAndHeight()
	{
		/*
		 * Actual test
		 */
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		Thumbnailator.createThumbnail((Image)img, -42, -42);
		
		fail();
	}
	
	/**
	 * Test for 
	 * {@link Thumbnailator#createThumbnail(Image, int, int)}
	 * where,
	 * 
	 * 1) Method arguments are correct
	 * 
	 * Expected outcome is,
	 * 
	 * 1) Processing will complete successfully.
	 * 
	 */
	@Test
	public void testCreateThumbnail_III_CorrectUsage()
	{
		/*
		 * Actual test
		 */
		BufferedImage img = 
			new BufferedImageBuilder(200, 200, BufferedImage.TYPE_INT_ARGB).build();
		
		Image thumbnail = Thumbnailator.createThumbnail((Image)img, 50, 50);
		
		assertEquals(50, thumbnail.getWidth(null));
		assertEquals(50, thumbnail.getHeight(null));
	}
	
	/**
	 * Returns test image data as an array of {@code byte}s.
	 * 
	 * @param format			Image format.
	 * @param width				Image width.
	 * @param height			Image height.
	 * @return					A {@code byte[]} of image data.
	 * @throws IOException		When a problem occurs while making image data.
	 */
	private byte[] makeImageData(String format, int width, int height)
	throws IOException
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(img, format, baos);
		
		return baos.toByteArray();
	}
}