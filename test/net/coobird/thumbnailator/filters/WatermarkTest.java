package net.coobird.thumbnailator.filters;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.filters.Watermark;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.test.BufferedImageComparer;
import net.coobird.thumbnailator.util.BufferedImages;

import org.junit.Test;

/**
 * Tests for the {@link Watermark} filter.
 * 
 * @author coobird
 *
 */
public class WatermarkTest
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
		
		BufferedImage watermarkImg = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
		
		ImageFilter filter = new Watermark(Positions.BOTTOM_CENTER, watermarkImg, 0.5f);
		
		// when
		filter.apply(originalImage);
		
		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}
}
