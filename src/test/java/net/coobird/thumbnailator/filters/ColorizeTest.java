package net.coobird.thumbnailator.filters;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.filters.Colorize;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.test.BufferedImageComparer;
import net.coobird.thumbnailator.util.BufferedImages;

import org.junit.Test;

/**
 * Tests for the {@link Colorize} filter.
 * 
 * @author coobird
 *
 */
public class ColorizeTest
{
	/**
	 * Checks that the input image contents are not altered.
	 */
	@Test
	public void inputContentsAreNotAltered()
	{
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		BufferedImage copyImage = BufferedImages.copy(originalImage);
		
		ImageFilter filter = new Colorize(Color.blue);
		
		// when
		filter.apply(originalImage);
		
		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}
}
