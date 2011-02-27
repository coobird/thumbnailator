package net.coobird.thumbnailator.filters;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.filters.Rotation;
import net.coobird.thumbnailator.test.BufferedImageComparer;
import net.coobird.thumbnailator.util.BufferedImages;

import org.junit.Test;

/**
 * Tests for the {@link Rotation} filter.
 * 
 * @author coobird
 *
 */
public class RotationTest
{
	/**
	 * Checks that the input image contents are not altered, when using the
	 * {@link Rotation#newRotator(double)} method.
	 */
	@Test
	public void inputContentsAreNotAltered_SpecifiedRotator() 
	{
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		BufferedImage copyImage = BufferedImages.copy(originalImage);
		
		ImageFilter filter = Rotation.newRotator(45);
		
		// when
		filter.apply(originalImage);
		
		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}
	
	/**
	 * Checks that the input image contents are not altered, when using one of
	 * the constants defined in the {@link Rotation} class.
	 */
	@Test
	public void inputContentsAreNotAltered_UsingConstantField() 
	{
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		BufferedImage copyImage = BufferedImages.copy(originalImage);
		
		ImageFilter filter = Rotation.LEFT_90_DEGREES;
		
		// when
		filter.apply(originalImage);
		
		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}
}
