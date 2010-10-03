package net.coobird.thumbnailator;

import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * A class which tests the behavior of the builder interface of the
 * {@link Thumbnails} class.
 * 
 * @author coobird
 *
 */
public class ThumbnailsBuilderTest
{
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>The size method is called.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The thumbnail is successfully created.</li>
	 * <li>The thumbnail dimensions are that which is specified by the 
	 * size method.</li>
	 * </ol>
	 */
	@Test
	public void sizeOnly()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		BufferedImage thumbnail = Thumbnails.of(img)
			.size(100, 100)
			.asBufferedImage();
		
		assertEquals(100, thumbnail.getWidth());
		assertEquals(100, thumbnail.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>The size method is called.</li>
	 * <li>The keepAspectRatio method is true.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The thumbnail is successfully created.</li>
	 * <li>The thumbnail dimensions are not the same as what is specified
	 * by the size method, but one that keeps the aspect ratio of the
	 * original.</li>
	 * </ol>
	 */
	@Test
	public void sizeWithAspectRatioTrue()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		BufferedImage thumbnail = Thumbnails.of(img)
			.size(120, 100)
			.keepAspectRatio(true)
			.asBufferedImage();
		
		assertEquals(100, thumbnail.getWidth());
		assertEquals(100, thumbnail.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>The size method is called.</li>
	 * <li>The keepAspectRatio method is false.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The thumbnail is successfully created.</li>
	 * <li>The thumbnail dimensions are that which is specified by the 
	 * size method.</li>
	 * </ol>
	 */
	@Test
	public void sizeWithAspectRatioFalse()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		BufferedImage thumbnail = Thumbnails.of(img)
			.size(120, 100)
			.keepAspectRatio(false)
			.asBufferedImage();
		
		assertEquals(120, thumbnail.getWidth());
		assertEquals(100, thumbnail.getHeight());
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>The scale method is called.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The thumbnail is successfully created.</li>
	 * <li>The thumbnail dimensions are determined by the value passed into the
	 * scale method.</li>
	 * </ol>
	 */
	@Test
	public void scaleOnly()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		BufferedImage thumbnail = Thumbnails.of(img)
			.scale(0.5f)
			.asBufferedImage();
		
		assertEquals(100, thumbnail.getWidth());
		assertEquals(100, thumbnail.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>The scale method is called.</li>
	 * <li>The keepAspectRatio method is true.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An {@link IllegalStateException} is thrown</li>
	 * </ol>
	 */	
	@Test(expected=IllegalStateException.class)
	public void scaleWithAspectRatioTrue()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		BufferedImage thumbnail = Thumbnails.of(img)
			.scale(0.5f)
			.keepAspectRatio(true)
			.asBufferedImage();

		fail();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>The scale method is called.</li>
	 * <li>The keepAspectRatio method is false.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An {@link IllegalStateException} is thrown</li>
	 * </ol>
	 */	
	@Test(expected=IllegalStateException.class)
	public void scaleWithAspectRatioFalse()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		BufferedImage thumbnail = Thumbnails.of(img)
			.scale(0.5f)
			.keepAspectRatio(false)
			.asBufferedImage();
		
		fail();
	}
}
