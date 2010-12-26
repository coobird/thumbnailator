package net.coobird.thumbnailator.tasks.io;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;

public class URLImageSourceTest
{
	/*
	 * TODO Need test cases which utilize a proxy.
	 */
	
	@Test(expected=NullPointerException.class)
	public void givenNullURL() throws IOException
	{
		try
		{
			// given
			// when
			URL url = null;
			new URLImageSource(url);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("URL cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void givenNullString() throws IOException
	{
		try
		{
			// given
			// when
			String url = null;
			new URLImageSource(url);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("URL cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void givenURL_givenNullProxy() throws IOException
	{
		try
		{
			// given
			// when
			new URLImageSource(new URL("file:test-resources/Thumbnailator/grid.png"), null);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("Proxy cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void givenString_givenNullProxy() throws IOException
	{
		try
		{
			// given
			// when
			new URLImageSource("file:test-resources/Thumbnailator/grid.png", null);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("Proxy cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test
	public void fileExists_Png() throws IOException
	{
		// given
		URLImageSource source = new URLImageSource(new URL("file:test-resources/Thumbnailator/grid.png"));
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertEquals(100, img.getWidth());
		assertEquals(100, img.getHeight());
		assertEquals("png", source.getInputFormatName());
	}
	
	@Test
	public void fileExists_Jpeg() throws IOException
	{
		// given
		URLImageSource source = new URLImageSource(new URL("file:test-resources/Thumbnailator/grid.jpg"));
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertEquals(100, img.getWidth());
		assertEquals(100, img.getHeight());
		assertEquals("JPEG", source.getInputFormatName());
	}
	
	@Test
	public void fileExists_Bmp() throws IOException
	{
		// given
		URLImageSource source = new URLImageSource(new URL("file:test-resources/Thumbnailator/grid.bmp"));
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertEquals(100, img.getWidth());
		assertEquals(100, img.getHeight());
		assertEquals("bmp", source.getInputFormatName());
	}
	
	@Test(expected=IOException.class)
	public void fileDoesNotExists() throws IOException
	{
		// given
		URLImageSource source = new URLImageSource(new URL("file:notfound"));
		
		try
		{
			// when
			source.read();
		}
		catch (IOException e)
		{
			// then
			assertThat(e.getMessage(), containsString("Could not open connection to URL:"));
			throw e;
		}
		fail();
	}
}
