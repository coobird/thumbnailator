package net.coobird.thumbnailator.tasks.io;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.coobird.thumbnailator.tasks.UnsupportedFormatException;

import org.junit.Test;


public class InputStreamImageSourceTest
{
	@Test(expected=NullPointerException.class)
	public void givenNullInputStream() throws IOException
	{
		try
		{
			// given
			// when
			new InputStreamImageSource(null);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("InputStream cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test
	public void fileExists_Png() throws IOException
	{
		// given
		InputStreamImageSource source = new InputStreamImageSource(new FileInputStream("test-resources/Thumbnailator/grid.png"));
		
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
		InputStreamImageSource source = new InputStreamImageSource(new FileInputStream("test-resources/Thumbnailator/grid.jpg"));
		
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
		InputStreamImageSource source = new InputStreamImageSource(new FileInputStream("test-resources/Thumbnailator/grid.bmp"));
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertEquals(100, img.getWidth());
		assertEquals(100, img.getHeight());
		assertEquals("bmp", source.getInputFormatName());
	}
	
	@Test(expected=UnsupportedFormatException.class)
	public void cannotDetermineImageFormat() throws IOException
	{
		// given
		InputStream is = mock(InputStream.class);
		when(is.read()).thenThrow(new IOException("Failed on read."));
		when(is.read(any(byte[].class))).thenThrow(new IOException("Failed on read."));
		when(is.read(any(byte[].class), anyInt(), anyInt())).thenThrow(new IOException("Failed on read."));
		
		InputStreamImageSource source = new InputStreamImageSource(is);
		
		// when
		try
		{
			source.read();
			fail();
		}
		catch (UnsupportedFormatException e)
		{
			// then
			assertEquals("No suitable ImageReader found for source data.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IOException.class)
	public void badImage_Png() throws IOException
	{
		try
		{
			// given
			byte[] bytes = new byte[100]; 
			new FileInputStream("test-resources/Thumbnailator/grid.png").read(bytes);
			
			ByteArrayInputStream is = new ByteArrayInputStream(bytes);
			InputStreamImageSource source = new InputStreamImageSource(is);
			
			// when
			source.read();
		}
		catch (IOException e)
		{
			// then
			assertEquals("Error reading PNG image data", e.getMessage());
			throw e;
		}
		fail();
	}
}
