package net.coobird.thumbnailator.tasks.io;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;


public class FileImageSourceTest
{
	@Test
	public void fileExists_Png() throws IOException
	{
		// given
		FileImageSource source = new FileImageSource(new File("test-resources/Thumbnailator/grid.png"));
		
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
		FileImageSource source = new FileImageSource(new File("test-resources/Thumbnailator/grid.jpg"));
		
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
		FileImageSource source = new FileImageSource(new File("test-resources/Thumbnailator/grid.bmp"));
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertEquals(100, img.getWidth());
		assertEquals(100, img.getHeight());
		assertEquals("bmp", source.getInputFormatName());
	}
	
	@Test(expected=FileNotFoundException.class)
	public void fileDoesNotExists() throws IOException
	{
		// given
		FileImageSource source = new FileImageSource(new File("notfound"));
		
		try
		{
			// when
			source.read();
		}
		catch (FileNotFoundException e)
		{
			// then
			assertThat(e.getMessage(), containsString("Could not find file"));
			throw e;
		}
		fail();
	}
	
	@Test
	public void fileExists_Png_AsString() throws IOException
	{
		// given
		FileImageSource source = new FileImageSource("test-resources/Thumbnailator/grid.png");
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertEquals(100, img.getWidth());
		assertEquals(100, img.getHeight());
		assertEquals("png", source.getInputFormatName());
	}
	
	@Test
	public void fileExists_Jpeg_AsString() throws IOException
	{
		// given
		FileImageSource source = new FileImageSource("test-resources/Thumbnailator/grid.jpg");
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertEquals(100, img.getWidth());
		assertEquals(100, img.getHeight());
		assertEquals("JPEG", source.getInputFormatName());
	}
	
	@Test
	public void fileExists_Bmp_AsString() throws IOException
	{
		// given
		FileImageSource source = new FileImageSource("test-resources/Thumbnailator/grid.bmp");
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertEquals(100, img.getWidth());
		assertEquals(100, img.getHeight());
		assertEquals("bmp", source.getInputFormatName());
	}
	
	@Test(expected=FileNotFoundException.class)
	public void fileDoesNotExists_AsString() throws IOException
	{
		// given
		FileImageSource source = new FileImageSource("notfound");
		
		try
		{
			// when
			source.read();
		}
		catch (FileNotFoundException e)
		{
			// then
			assertThat(e.getMessage(), containsString("Could not find file"));
			throw e;
		}
		fail();
	}
	
	@Test(expected=IllegalStateException.class)
	public void fileExists_getInputFormatNameBeforeRead() throws IOException
	{
		// given
		FileImageSource source = new FileImageSource(new File("test-resources/Thumbnailator/grid.png"));
		
		try
		{
			// when
			source.getInputFormatName();
		}
		catch (IllegalStateException e)
		{
			// then
			assertEquals("Input has not been read yet.", e.getMessage());
			throw e;
		}
	}
}
