package net.coobird.thumbnailator.tasks.io;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;

public class BufferedImageSourceTest
{
	@Test(expected=NullPointerException.class)
	public void givenNullImage() throws IOException
	{
		try
		{
			// given
			// when
			new BufferedImageSource(null);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("Image cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test
	public void givenValidImage() throws IOException
	{
		// given
		BufferedImage sourceImage = ImageIO.read(new File("test-resources/Thumbnailator/grid.png"));
		BufferedImageSource source = new BufferedImageSource(sourceImage);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertSame(sourceImage, img);
		assertEquals(100, img.getWidth());
		assertEquals(100, img.getHeight());
		assertEquals(null, source.getInputFormatName());
	}
}
