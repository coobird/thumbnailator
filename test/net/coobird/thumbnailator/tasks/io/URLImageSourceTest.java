package net.coobird.thumbnailator.tasks.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;


public class URLImageSourceTest
{
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
		
		// when
		try
		{
			source.read();
		}
		catch (IOException e)
		{
			assertEquals("Could not open connection to URL: file:notfound", e.getMessage());
			throw e;
		}
		
		// then
		fail();
	}
}
