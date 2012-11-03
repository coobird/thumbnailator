package net.coobird.thumbnailator.filters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.test.BufferedImageComparer;
import net.coobird.thumbnailator.util.BufferedImages;

import org.junit.Test;

public class FlipTest {
	
	private static int SIZE = 160;
	
	@Test
	public void flipHorizontal() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("test-resources/Exif/original.png"));
		
		// when
		BufferedImage result = Flip.HORIZONTAL.apply(img);
		
		// then
		assertEquals(Color.black.getRGB(), result.getRGB(0, 0));
		assertEquals(Color.black.getRGB(), result.getRGB(0, SIZE / 2 - 1));
		assertEquals(Color.white.getRGB(), result.getRGB(0, SIZE - 1));
		assertEquals(Color.black.getRGB(), result.getRGB(SIZE - 1, 0));
		assertEquals(Color.black.getRGB(), result.getRGB(SIZE - 1, SIZE / 2 - 1));
		assertEquals(Color.black.getRGB(), result.getRGB(SIZE - 1, SIZE - 1));
	}
	
	@Test
	public void flipVertical() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("test-resources/Exif/original.png"));
		
		// when
		BufferedImage result = Flip.VERTICAL.apply(img);
		
		// then
		assertEquals(Color.black.getRGB(), result.getRGB(0, 0));
		assertEquals(Color.black.getRGB(), result.getRGB(0, SIZE / 2 - 1));
		assertEquals(Color.black.getRGB(), result.getRGB(0, SIZE - 1));
		assertEquals(Color.white.getRGB(), result.getRGB(SIZE - 1, 0));
		assertEquals(Color.black.getRGB(), result.getRGB(SIZE - 1, SIZE / 2 - 1));
		assertEquals(Color.black.getRGB(), result.getRGB(SIZE - 1, SIZE - 1));
	}
	
	/**
	 * Checks that the input image contents are not altered, when using the
	 * {@link Flip#HORIZONTAL}.
	 */
	@Test
	public void inputContentsAreNotAltered_UsingFlipHorizontal() 
	{
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		BufferedImage copyImage = BufferedImages.copy(originalImage);
		
		ImageFilter filter = Flip.HORIZONTAL;
		
		// when
		filter.apply(originalImage);
		
		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}
	
	/**
	 * Checks that the input image contents are not altered, when using the
	 * {@link Flip#VERTICAL}.
	 */
	@Test
	public void inputContentsAreNotAltered_UsingFlipVertical() 
	{
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		BufferedImage copyImage = BufferedImages.copy(originalImage);
		
		ImageFilter filter = Flip.VERTICAL;
		
		// when
		filter.apply(originalImage);
		
		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}
}
