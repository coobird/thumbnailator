package net.coobird.thumbnailator.resizers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.test.BufferedImageComparer;

import org.junit.Test;
import static org.junit.Assert.*;

public class NullResizerTest
{
	@Test
	public void sourceAndDestSameDimension() throws IOException
	{
		// given
		BufferedImage srcImage = ImageIO.read(new File("src/test/resources/Thumbnailator/grid.png"));
		BufferedImage destImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		// when
		new NullResizer().resize(srcImage, destImage);
		
		// then
		assertEquals(srcImage.getWidth(), destImage.getWidth());
		assertEquals(srcImage.getHeight(), destImage.getHeight());
		assertTrue(BufferedImageComparer.isRGBSimilar(srcImage, destImage));
	}
	
	@Test
	public void sourceSmallerThanDest() throws IOException
	{
		// given
		BufferedImage srcImage = ImageIO.read(new File("src/test/resources/Thumbnailator/grid.png"));
		BufferedImage destImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		
		// when
		new NullResizer().resize(srcImage, destImage);
		
		// then
		assertTrue(BufferedImageComparer.isRGBSimilar(srcImage, destImage.getSubimage(0, 0, 100, 100)));
	}
	
	@Test
	public void sourceLargerThanDest() throws IOException
	{
		// given
		BufferedImage srcImage = ImageIO.read(new File("src/test/resources/Thumbnailator/grid.png"));
		BufferedImage destImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
		
		// when
		new NullResizer().resize(srcImage, destImage);
		
		// then
		assertTrue(BufferedImageComparer.isRGBSimilar(srcImage.getSubimage(0, 0, 50, 50), destImage));
	}
	
	@Test
	public void resizeNullAndNull()
	{
		// given
		BufferedImage srcImage = null;
		BufferedImage destImage = null;
		
		try
		{
			// when
			new NullResizer().resize(srcImage, destImage);
			fail();
		}
		catch (Exception e)
		{
			// then
			assertEquals("The source and/or destination image is null.", e.getMessage());
		}
	}
	
	@Test
	public void resizeSpecifiedAndNull()
	{
		// given
		BufferedImage srcImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		BufferedImage destImage = null;
		
		try
		{
			// when
			new NullResizer().resize(srcImage, destImage);
			fail();
		}
		catch (Exception e)
		{
			// then
			assertEquals("The source and/or destination image is null.", e.getMessage());
		}
	}
	
	@Test
	public void resizeNullAndSpecified()
	{
		// given
		BufferedImage srcImage = null;
		BufferedImage destImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		
		try
		{
			// when
			new NullResizer().resize(srcImage, destImage);
			fail();
		}
		catch (Exception e)
		{
			// then
			assertEquals("The source and/or destination image is null.", e.getMessage());
		}
	}
}
