package net.coobird.thumbnailator;

import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.resizers.Resizer;
import net.coobird.thumbnailator.resizers.Resizers;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;

import org.junit.Test;
import static org.junit.Assert.*;

import static org.mockito.Mockito.*;


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
			.size(50, 50)
			.asBufferedImage();
		
		assertEquals(50, thumbnail.getWidth());
		assertEquals(50, thumbnail.getHeight());
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
			.size(120, 50)
			.keepAspectRatio(true)
			.asBufferedImage();
		
		assertEquals(50, thumbnail.getWidth());
		assertEquals(50, thumbnail.getHeight());
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
			.size(120, 50)
			.keepAspectRatio(false)
			.asBufferedImage();
		
		assertEquals(120, thumbnail.getWidth());
		assertEquals(50, thumbnail.getHeight());
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
			.scale(0.25f)
			.asBufferedImage();
		
		assertEquals(50, thumbnail.getWidth());
		assertEquals(50, thumbnail.getHeight());
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
	@Test
	public void scaleWithAspectRatioTrue()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try
		{
			BufferedImage thumbnail = Thumbnails.of(img)
				.scale(0.5f)
				.keepAspectRatio(true)
				.asBufferedImage();
			
			fail();
		}
		catch (IllegalStateException e)
		{
			assertTrue(e.getMessage().contains("scaling factor has already been specified"));
		}
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
	@Test
	public void scaleWithAspectRatioFalse()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try
		{
			BufferedImage thumbnail = Thumbnails.of(img)
				.scale(0.5f)
				.keepAspectRatio(false)
				.asBufferedImage();
			
			fail();
		}
		catch (IllegalStateException e)
		{
			assertTrue(e.getMessage().contains("scaling factor has already been specified"));
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>The keepAspectRatio method is called before the size method.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An {@link IllegalStateException} is thrown</li>
	 * </ol>
	 */	
	@Test
	public void keepAspectRatioBeforeSize()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try
		{
			Thumbnails.of(img)
				.keepAspectRatio(false);
			
			fail();
		}
		catch (IllegalStateException e)
		{
			assertTrue(e.getMessage().contains("unless the size parameter has already been specified."));
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>The keepAspectRatio method is called after the scale method.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An {@link IllegalStateException} is thrown</li>
	 * </ol>
	 */	
	@Test
	public void keepAspectRatioAfterScale()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try
		{
			BufferedImage thumbnail = Thumbnails.of(img)
				.scale(0.5f)
				.keepAspectRatio(false)
				.asBufferedImage();
			
			fail();
		}
		catch (IllegalStateException e)
		{
			assertTrue(e.getMessage().contains("scaling factor has already been specified"));
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>The resizer method is called</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The specified Resizer is called for resizing.</li>
	 * </ol>
	 */	
	@Test
	public void resizerOnly()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		Resizer resizer = mock(Resizer.class);
	
		BufferedImage thumbnail = Thumbnails.of(img)
			.size(200, 200)
			.resizer(resizer)
			.asBufferedImage();
		
		verify(resizer).resize(img, thumbnail);
		verifyNoMoreInteractions(resizer);
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>The resizer method is called twice</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An {@link IllegalStateException} is thrown</li>
	 * </ol>
	 */	
	@Test(expected=IllegalStateException.class)
	public void resizerTwice()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		BufferedImage thumbnail = Thumbnails.of(img)
			.size(200, 200)
			.resizer(Resizers.PROGRESSIVE)
			.resizer(Resizers.PROGRESSIVE)
			.asBufferedImage();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>The scalingMode method is called</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The thumbnail is successfully created.</li>
	 * </ol>
	 */	
	@Test
	public void scalingModeOnly()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		BufferedImage thumbnail = Thumbnails.of(img)
			.size(200, 200)
			.scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
			.asBufferedImage();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>The scalingMode method is called twice</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An {@link IllegalStateException} is thrown</li>
	 * </ol>
	 */	
	@Test(expected=IllegalStateException.class)
	public void scalingModeTwice()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		BufferedImage thumbnail = Thumbnails.of(img)
			.size(200, 200)
			.scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
			.scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
			.asBufferedImage();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>The resizer method is called, then</li>
	 * <li>The scalingMode method is called</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An {@link IllegalStateException} is thrown</li>
	 * </ol>
	 */	
	@Test(expected=IllegalStateException.class)
	public void resizerThenScalingMode()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		BufferedImage thumbnail = Thumbnails.of(img)
			.size(200, 200)
			.resizer(Resizers.PROGRESSIVE)
			.scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
			.asBufferedImage();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>The scalingMode method is called, then</li>
	 * <li>The resizer method is called</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An {@link IllegalStateException} is thrown</li>
	 * </ol>
	 */	
	@Test(expected=IllegalStateException.class)
	public void scalingModeThenResizer()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		BufferedImage thumbnail = Thumbnails.of(img)
			.size(200, 200)
			.scalingMode(ScalingMode.PROGRESSIVE_BILINEAR)
			.resizer(Resizers.PROGRESSIVE)
			.asBufferedImage();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>The imageType method is not called</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The image type of the resulting image is the same as the original
	 * image</li>
	 * </ol>
	 */	
	@Test
	public void imageTypeNotCalled()
	{
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_BYTE_INDEXED);
		
		BufferedImage thumbnail = Thumbnails.of(img)
			.size(200, 200)
			.asBufferedImage();
		
		assertEquals(BufferedImage.TYPE_BYTE_INDEXED, thumbnail.getType());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>The imageType method is called with the same type as the original
	 * image</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The image type of the resulting image is the same as the original
	 * image</li>
	 * </ol>
	 */	
	@Test
	public void imageTypeCalledSameType()
	{
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_BYTE_GRAY);
		
		BufferedImage thumbnail = Thumbnails.of(img)
			.size(200, 200)
			.imageType(BufferedImage.TYPE_BYTE_GRAY)
			.asBufferedImage();
		
		assertEquals(BufferedImage.TYPE_BYTE_GRAY, thumbnail.getType());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>The imageType method is called with the different type as the
	 * original image</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The image type of the resulting image is the the specified by the
	 * imageType method</li>
	 * </ol>
	 */	
	@Test
	public void imageTypeCalledDifferentType()
	{
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_BYTE_GRAY);
		
		BufferedImage thumbnail = Thumbnails.of(img)
		.size(200, 200)
		.imageType(BufferedImage.TYPE_BYTE_GRAY)
		.asBufferedImage();
		
		assertEquals(BufferedImage.TYPE_BYTE_GRAY, thumbnail.getType());
	}
}
