package net.coobird.thumbnailator.filter;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.filters.Caption;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.test.BufferedImageComparer;
import net.coobird.thumbnailator.util.BufferedImages;

import org.junit.Test;

/**
 * Tests for the {@link Caption} filter.
 * 
 * @author coobird
 *
 */
public class CaptionTest
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
		
		ImageFilter filter = new Caption(
				"hello", 
				new Font("Monospaced", Font.PLAIN, 14), 
				Color.black, 
				Positions.BOTTOM_CENTER, 0
		);
		
		// when
		filter.apply(originalImage);
		
		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}
}
