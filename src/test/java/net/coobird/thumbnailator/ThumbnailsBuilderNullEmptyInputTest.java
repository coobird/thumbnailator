package net.coobird.thumbnailator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

public class ThumbnailsBuilderNullEmptyInputTest
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
	 * <li>Thumbnails.of(URL...)</li>
	 * <li>where the URL[] is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void of_URLs_null()
	{
		URL[] url = null;
		
		try
		{
			Thumbnails.of(url);
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Cannot specify null for input URLs.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(InputStream...)</li>
	 * <li>where the InputStream[] is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void of_InputStreams_null()
	{
		InputStream[] is = null;
		
		try
		{
			Thumbnails.of(is);
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Cannot specify null for InputStreams.", e.getMessage());
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
	 * <li>Thumbnails.of(URL...)</li>
	 * <li>where the URL[] is length 0.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void of_URLs_empty()
	{
		URL[] url = new URL[0];
		
		try
		{
			Thumbnails.of(url);
			fail();
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Cannot specify an empty array for input URLs.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(InputStream...)</li>
	 * <li>where the InputStream[] is length 0.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void of_InputStreams_empty()
	{
		InputStream[] is = new InputStream[0];
		
		try
		{
			Thumbnails.of(is);
			fail();
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Cannot specify an empty array for InputStreams.", e.getMessage());
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
			Thumbnails.fromImages((Collection<BufferedImage>)null);
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
			Thumbnails.fromFiles((Collection<File>)null);
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
			Thumbnails.fromFilenames((Collection<String>)null);
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
	 * <li>Thumbnails.fromURLs(Collection)</li>
	 * <li>where the Collection is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void fromURLs_Collection_null()
	{
		try
		{
			Thumbnails.fromURLs((Collection<URL>)null);
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Cannot specify null for input URLs.", e.getMessage());
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
	 * <li>Thumbnails.fromURLs(Collection)</li>
	 * <li>where the Collection is empty.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void fromURLs_Collection_empty()
	{
		try
		{
			Thumbnails.fromURLs(Collections.<URL>emptyList());
			fail();
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Cannot specify an empty collection for input URLs.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromInputStreams(Collection)</li>
	 * <li>where the Collection is empty.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void fromInputStreams_Collection_empty()
	{
		try
		{
			Thumbnails.fromInputStreams(Collections.<InputStream>emptyList());
			fail();
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Cannot specify an empty collection for InputStreams.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromImages(Iterable)</li>
	 * <li>where the Iterable is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void fromImages_Iterable_null()
	{
		try
		{
			Thumbnails.fromImages((Iterable<BufferedImage>)null);
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
	 * <li>Thumbnails.fromFiles(Iterable)</li>
	 * <li>where the Iterable is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void fromFiles_Iterable_null()
	{
		try
		{
			Thumbnails.fromFiles((Iterable<File>)null);
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
	 * <li>Thumbnails.fromFilenames(Iterable)</li>
	 * <li>where the Iterable is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void fromFilenames_Iterable_null()
	{
		try
		{
			Thumbnails.fromFilenames((Iterable<String>)null);
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
	 * <li>Thumbnails.fromURLs(Iterable)</li>
	 * <li>where the Iterable is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void fromURLs_Iterable_null()
	{
		try
		{
			Thumbnails.fromURLs((Iterable<URL>)null);
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Cannot specify null for input URLs.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromImages(Iterable)</li>
	 * <li>where the Iterable is empty.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void fromImages_Iterable_empty()
	{
		try
		{
			Thumbnails.fromImages((Iterable<BufferedImage>)Collections.<BufferedImage>emptyList());
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
	 * <li>Thumbnails.fromFiles(Iterable)</li>
	 * <li>where the Iterable is empty.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void fromFiles_Iterable_empty()
	{
		try
		{
			Thumbnails.fromFiles((Iterable<File>)Collections.<File>emptyList());
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
	 * <li>Thumbnails.fromFilenames(Iterable)</li>
	 * <li>where the Iterable is empty.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void fromFilenames_Iterable_empty()
	{
		try
		{
			Thumbnails.fromFilenames((Iterable<String>)Collections.<String>emptyList());
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
	 * <li>Thumbnails.fromURLs(Iterable)</li>
	 * <li>where the Iterable is empty.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void fromURLs_Iterable_empty()
	{
		try
		{
			Thumbnails.fromURLs((Iterable<URL>)Collections.<URL>emptyList());
			fail();
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Cannot specify an empty collection for input URLs.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromInputStreams(Iterable)</li>
	 * <li>where the Iterable is empty.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void fromInputStreams_Iterable_empty()
	{
		try
		{
			Thumbnails.fromInputStreams((Iterable<InputStream>)Collections.<InputStream>emptyList());
			fail();
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Cannot specify an empty collection for InputStreams.", e.getMessage());
			throw e;
		}
	}
}