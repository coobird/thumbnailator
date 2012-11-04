package net.coobird.thumbnailator.filters;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.filters.Rotation;
import net.coobird.thumbnailator.test.BufferedImageAssert;
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
	
	@Test
	public void imageRotatedLeft90Degrees() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("test-resources/Exif/original.png"));
		
		// when
		BufferedImage result = Rotation.LEFT_90_DEGREES.apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result, 
				new float[] {
						1, 1, 0,
						1, 1, 0,
						1, 1, 1,
				}
		);
	}
	
	@Test
	public void imageRotatedRight90Degrees() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("test-resources/Exif/original.png"));
		
		// when
		BufferedImage result = Rotation.RIGHT_90_DEGREES.apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result, 
				new float[] {
						1, 1, 1,
						0, 1, 1,
						0, 1, 1,
				}
		);
	}
	
	@Test
	public void imageRotated180Degrees() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("test-resources/Exif/original.png"));
		
		// when
		BufferedImage result = Rotation.ROTATE_180_DEGREES.apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result, 
				new float[] {
						0, 0, 1,
						1, 1, 1,
						1, 1, 1,
				}
		);
	}
}
