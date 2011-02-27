package net.coobird.thumbnailator.filters;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.filters.Canvas;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.test.BufferedImageComparer;
import net.coobird.thumbnailator.util.BufferedImages;

import org.junit.Test;

/**
 * Tests for the {@link Canvas} filter.
 * 
 * @author coobird
 *
 */
public class CanvasTest
{
	/**
	 * Checks that the input image contents are not altered.
	 */
	@Test
	public void inputContentsAreNotAltered_WidthHeightPositionConstructor() 
	{
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		BufferedImage copyImage = BufferedImages.copy(originalImage);
		
		ImageFilter filter = new Canvas(100, 100, Positions.CENTER);
		
		// when
		filter.apply(originalImage);
		
		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}
	
	/**
	 * Checks that the input image contents are not altered.
	 */
	@Test
	public void inputContentsAreNotAltered_WidthHeightPositionCropConstructor() 
	{
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		BufferedImage copyImage = BufferedImages.copy(originalImage);
		
		ImageFilter filter = new Canvas(100, 100, Positions.CENTER, true);
		
		// when
		filter.apply(originalImage);
		
		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}
	
	/**
	 * Checks that the input image contents are not altered.
	 */
	@Test
	public void inputContentsAreNotAltered_WidthHeightPositionColorConstructor() 
	{
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		BufferedImage copyImage = BufferedImages.copy(originalImage);
		
		ImageFilter filter = new Canvas(100, 100, Positions.CENTER, Color.black);
		
		// when
		filter.apply(originalImage);
		
		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}
	
	/**
	 * Checks that the input image contents are not altered.
	 */
	@Test
	public void inputContentsAreNotAltered_WidthHeightPositionCropColorConstructor() 
	{
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		BufferedImage copyImage = BufferedImages.copy(originalImage);
		
		ImageFilter filter = new Canvas(100, 100, Positions.CENTER, true, Color.black);
		
		// when
		filter.apply(originalImage);
		
		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}
	
	/**
	 * Checks that the image is cropped
	 */
	@Test
	public void croppingEnabled_WidthHeightPositionCropColorConstructor() 
	{
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		ImageFilter filter = new Canvas(100, 100, Positions.CENTER, true, Color.black);
		
		// when
		BufferedImage resultImage = filter.apply(originalImage);
		
		// then
		assertEquals(100, resultImage.getWidth());
		assertEquals(100, resultImage.getHeight());
	}
	
	/**
	 * Checks that the image is not cropped
	 */
	@Test
	public void croppingDisabled_WidthHeightExceeds_WidthHeightPositionCropColorConstructor() 
	{
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		ImageFilter filter = new Canvas(100, 100, Positions.CENTER, false, Color.black);
		
		// when
		BufferedImage resultImage = filter.apply(originalImage);
		
		// then
		assertEquals(200, resultImage.getWidth());
		assertEquals(200, resultImage.getHeight());
	}
	
	/**
	 * Checks that the image is not cropped
	 * - the original width exceeds the specified width
	 * - the original height is within the specified height
	 */
	@Test
	public void croppingDisabled_WidthExceeds_WidthHeightPositionCropColorConstructor() 
	{
		// given
		BufferedImage originalImage = new BufferedImage(200, 90, BufferedImage.TYPE_INT_ARGB);
		ImageFilter filter = new Canvas(100, 100, Positions.CENTER, false, Color.black);
		
		// when
		BufferedImage resultImage = filter.apply(originalImage);
		
		// then
		assertEquals(200, resultImage.getWidth());
		assertEquals(100, resultImage.getHeight());
	}
	
	/**
	 * Checks that the image is not cropped
	 * - the original width is within the specified height
	 * - the original height exceeds the specified width
	 */
	@Test
	public void croppingDisabled_HeightExceeds_WidthHeightPositionCropColorConstructor() 
	{
		// given
		BufferedImage originalImage = new BufferedImage(90, 200, BufferedImage.TYPE_INT_ARGB);
		ImageFilter filter = new Canvas(100, 100, Positions.CENTER, false, Color.black);
		
		// when
		BufferedImage resultImage = filter.apply(originalImage);
		
		// then
		assertEquals(100, resultImage.getWidth());
		assertEquals(200, resultImage.getHeight());
	}
	
	/**
	 * Checks that the image is enclosed
	 */
	@Test
	public void croppingEnabled_WidthHeightSmaller_WidthHeightPositionCropColorConstructor() 
	{
		// given
		BufferedImage originalImage = new BufferedImage(90, 90, BufferedImage.TYPE_INT_ARGB);
		ImageFilter filter = new Canvas(100, 100, Positions.CENTER, true, Color.black);
		
		// when
		BufferedImage resultImage = filter.apply(originalImage);
		
		// then
		assertEquals(100, resultImage.getWidth());
		assertEquals(100, resultImage.getHeight());
	}
	
	/**
	 * Checks that the image is enclosed
	 */
	@Test
	public void croppingDisabled_WidthHeightSmaller_WidthHeightPositionCropColorConstructor() 
	{
		// given
		BufferedImage originalImage = new BufferedImage(90, 90, BufferedImage.TYPE_INT_ARGB);
		ImageFilter filter = new Canvas(100, 100, Positions.CENTER, false, Color.black);
		
		// when
		BufferedImage resultImage = filter.apply(originalImage);
		
		// then
		assertEquals(100, resultImage.getWidth());
		assertEquals(100, resultImage.getHeight());
	}
}
