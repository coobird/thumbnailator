package net.coobird.thumbnailator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.resizers.Resizer;
import net.coobird.thumbnailator.resizers.Resizers;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;

import org.junit.Test;


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
			Thumbnails.of(img)
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
			Thumbnails.of(img)
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
			Thumbnails.of(img)
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
		
		Thumbnails.of(img)
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
		
		Thumbnails.of(img)
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
		
		Thumbnails.of(img)
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
		
		Thumbnails.of(img)
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
		
		Thumbnails.of(img)
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

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>The imageType method is called twice</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalStateException.class)
	public void imageTypeCalledTwice()
	{
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_BYTE_GRAY);
		
		Thumbnails.of(img)
			.size(200, 200)
			.imageType(BufferedImage.TYPE_BYTE_GRAY)
			.imageType(BufferedImage.TYPE_BYTE_GRAY)
			.asBufferedImage();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>outputQuality(float) is 0.0f</li>
	 * <li>toFile(File)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The outputQuality is allowed</li>
	 * <li>The thumbnail is successfully produced</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void outputQuality_float_ValidArg_ZeroZero() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.jpg");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.jpg");
		outFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.outputFormat("jpg")
			.outputQuality(0.0f)
			.toFile(outFile);
		
		BufferedImage thumbnail = ImageIO.read(outFile);
		
		assertEquals(50, thumbnail.getWidth());
		assertEquals(50, thumbnail.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>outputQuality(float) is 0.5f</li>
	 * <li>toFile(File)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The outputQuality is allowed</li>
	 * <li>The thumbnail is successfully produced</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void outputQuality_float_ValidArg_ZeroFive() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.jpg");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.jpg");
		outFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.outputFormat("jpg")
			.outputQuality(0.5f)
			.toFile(outFile);
		
		BufferedImage thumbnail = ImageIO.read(outFile);
		
		assertEquals(50, thumbnail.getWidth());
		assertEquals(50, thumbnail.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>outputQuality(float) is 1.0f</li>
	 * <li>toFile(File)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The outputQuality is allowed</li>
	 * <li>The thumbnail is successfully produced</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void outputQuality_float_ValidArg_OneZero() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.jpg");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.jpg");
		outFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.outputFormat("jpg")
			.outputQuality(1.0f)
			.toFile(outFile);
		
		BufferedImage thumbnail = ImageIO.read(outFile);
		
		assertEquals(50, thumbnail.getWidth());
		assertEquals(50, thumbnail.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>outputQuality(float) is negative</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalArgumentException is thrown.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void outputQuality_float_InvalidArg_Negative() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.jpg");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.jpg");
		outFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.outputFormat("jpg")
			.outputQuality(-0.01f);
	}

	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>outputQuality(float) is greater than 1.0d</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalArgumentException is thrown.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void outputQuality_float_InvalidArg_OverOne() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.jpg");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.jpg");
		outFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.outputFormat("jpg")
			.outputQuality(1.01f);
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>outputQuality(double) is 0.0d</li>
	 * <li>toFile(File)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The outputQuality is allowed</li>
	 * <li>The thumbnail is successfully produced</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void outputQuality_double_ValidArg_ZeroZero() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.jpg");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.jpg");
		outFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.outputFormat("jpg")
			.outputQuality(0.0d)
			.toFile(outFile);
		
		BufferedImage thumbnail = ImageIO.read(outFile);
		
		assertEquals(50, thumbnail.getWidth());
		assertEquals(50, thumbnail.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>outputQuality(double) is 0.5d</li>
	 * <li>toFile(File)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The outputQuality is allowed</li>
	 * <li>The thumbnail is successfully produced</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void outputQuality_double_ValidArg_ZeroFive() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.jpg");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.jpg");
		outFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.outputFormat("jpg")
			.outputQuality(0.5d)
			.toFile(outFile);
		
		BufferedImage thumbnail = ImageIO.read(outFile);
		
		assertEquals(50, thumbnail.getWidth());
		assertEquals(50, thumbnail.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>outputQuality(double) is 1.0d</li>
	 * <li>toFile(File)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The outputQuality is allowed</li>
	 * <li>The thumbnail is successfully produced</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void outputQuality_double_ValidArg_OneZero() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.jpg");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.jpg");
		outFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.outputFormat("jpg")
			.outputQuality(1.0d)
			.toFile(outFile);
		
		BufferedImage thumbnail = ImageIO.read(outFile);
		
		assertEquals(50, thumbnail.getWidth());
		assertEquals(50, thumbnail.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>outputQuality(double) is negative</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalArgumentException is thrown.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void outputQuality_double_InvalidArg_Negative() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.jpg");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.jpg");
		outFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.outputFormat("jpg")
			.outputQuality(-0.01d);
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>outputQuality(double) is greater than 1.0d</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalArgumentException is thrown.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void outputQuality_double_InvalidArg_OverOne() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.jpg");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.jpg");
		outFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.outputFormat("jpg")
			.outputQuality(1.01d);
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>outputFormat</li>
	 * <li>toFile(File)</li>
	 * <li>format name matches the file extension</li>
	 * <li>format is same as the original format</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The output format of the image is one that is specified.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void outputFormat_SameAsOriginal_SameAsFileExtension() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.png");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.outputFormat("png")
			.toFile(outFile);
		
		BufferedImage fromFileImage = ImageIO.read(outFile);
		
		String formatName = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile)).next().getFormatName();
		assertEquals("png", formatName);
		
		assertEquals(50, fromFileImage.getWidth());
		assertEquals(50, fromFileImage.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>outputFormat</li>
	 * <li>toFile(File)</li>
	 * <li>format name matches the file extension</li>
	 * <li>format is different from the original format</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The output format of the image is one that is specified.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void outputFormat_DiffersFromOriginal_SameAsFileExtension() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.png");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.jpg");
		outFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.outputFormat("jpg")
			.toFile(outFile);
		
		BufferedImage fromFileImage = ImageIO.read(outFile);
		
		String formatName = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile)).next().getFormatName();
		assertEquals("JPEG", formatName);
		
		assertEquals(50, fromFileImage.getWidth());
		assertEquals(50, fromFileImage.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>outputFormat</li>
	 * <li>toFile(File)</li>
	 * <li>format name matches the file extension</li>
	 * <li>format is different from the original format</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The output format of the image is one that is specified.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void outputFormat_DiffersFromOriginal_SameAsFileExtension_Jpeg() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.png");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.jpg");
		outFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.outputFormat("jpeg")
			.toFile(outFile);
		
		BufferedImage fromFileImage = ImageIO.read(outFile);
		
		String formatName = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile)).next().getFormatName();
		assertEquals("JPEG", formatName);
		
		assertEquals(50, fromFileImage.getWidth());
		assertEquals(50, fromFileImage.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>outputFormat</li>
	 * <li>toFile(File)</li>
	 * <li>format name differs from the file extension</li>
	 * <li>format is same as the original format</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The output format of the image is one that is specified.</li>
	 * <li>The format extension is appended to the output filename.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void outputFormat_SameAsOriginal_DiffersFromFileExtension() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.png");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.jpg");
		
		File actualOutFile = new File("test-resources/Thumbnailator/grid.tmp.jpg.png");
		actualOutFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.outputFormat("png")
			.toFile(outFile);
		
		BufferedImage fromFileImage = ImageIO.read(actualOutFile);
		
		String formatName = ImageIO.getImageReaders(ImageIO.createImageInputStream(actualOutFile)).next().getFormatName();
		assertEquals("png", formatName);
		
		assertEquals(50, fromFileImage.getWidth());
		assertEquals(50, fromFileImage.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>outputFormat</li>
	 * <li>toFile(File)</li>
	 * <li>format name differs from the file extension</li>
	 * <li>format differs from the original format</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The output format of the image is one that is specified.</li>
	 * <li>The format extension is appended to the output filename.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void outputFormat_DiffersFromOriginal_DiffersFromFileExtension() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.png");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");

		File actualOutFile = new File("test-resources/Thumbnailator/grid.tmp.png.jpg");
		actualOutFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.outputFormat("jpg")
			.toFile(outFile);
		
		BufferedImage fromFileImage = ImageIO.read(actualOutFile);
		
		String formatName = ImageIO.getImageReaders(ImageIO.createImageInputStream(actualOutFile)).next().getFormatName();
		assertEquals("JPEG", formatName);
		
		assertEquals(50, fromFileImage.getWidth());
		assertEquals(50, fromFileImage.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>outputFormat</li>
	 * <li>toFile(File)</li>
	 * <li>format name differs from the file extension</li>
	 * <li>format differs from the original format</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The output format of the image is one that is specified.</li>
	 * <li>The format extension is appended to the output filename.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void outputFormat_DiffersFromOriginal_DiffersFromFileExtension_Jpeg() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.png");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");
		
		File actualOutFile = new File("test-resources/Thumbnailator/grid.tmp.png.jpeg");
		actualOutFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.outputFormat("jpeg")
			.toFile(outFile);
		
		BufferedImage fromFileImage = ImageIO.read(actualOutFile);
		
		String formatName = ImageIO.getImageReaders(ImageIO.createImageInputStream(actualOutFile)).next().getFormatName();
		assertEquals("JPEG", formatName);
		
		assertEquals(50, fromFileImage.getWidth());
		assertEquals(50, fromFileImage.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>multiple files</li>
	 * <li>outputFormat</li>
	 * <li>toFile(File)</li>
	 * <li>format name is same as file extension</li>
	 * <li>format is same as original format</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The output format of the image is one that is specified.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void outputFormat_Multiple_SameAsOriginal_SameAsExtension_Both() throws IOException
	{
		// given
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File f2 = new File("test-resources/Thumbnailator/igrid.png");
		
		// when
		Thumbnails.of(f1, f2)
			.size(50, 50)
			.outputFormat("png")
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.igrid.png");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		BufferedImage fromFileImage2 = ImageIO.read(outFile2);
		
		String formatName1 = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile1)).next().getFormatName();
		assertEquals("png", formatName1);
		String formatName2 = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile2)).next().getFormatName();
		assertEquals("png", formatName2);
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		assertEquals(50, fromFileImage2.getWidth());
		assertEquals(50, fromFileImage2.getHeight());
	}
	
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>multiple files</li>
	 * <li>outputFormat</li>
	 * <li>toFile(File)</li>
	 * <li>1st file: file extension same as format specified</li>
	 * <li>2nd file: file extension differs from format specified</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The output format of the image is one that is specified.</li>
	 * <li>For the file with the different format and extension, the extension
	 * of the specified format will be added to the file name.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void outputFormat_Multiple_FirstExtensionSame_SecondExtensionDifferent() throws IOException
	{
		// given
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File f2 = new File("test-resources/Thumbnailator/grid.jpg");
		
		// when
		Thumbnails.of(f1, f2)
		.size(50, 50)
		.outputFormat("png")
		.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg.png");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		BufferedImage fromFileImage2 = ImageIO.read(outFile2);
		
		String formatName1 = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile1)).next().getFormatName();
		assertEquals("png", formatName1);
		String formatName2 = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile2)).next().getFormatName();
		assertEquals("png", formatName2);
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		assertEquals(50, fromFileImage2.getWidth());
		assertEquals(50, fromFileImage2.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>multiple files</li>
	 * <li>outputFormat</li>
	 * <li>toFile(File)</li>
	 * <li>1st file: file extension differs from format specified</li>
	 * <li>2nd file: file extension same as format specified</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The output format of the image is one that is specified.</li>
	 * <li>For the file with the different format and extension, the extension
	 * of the specified format will be added to the file name.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void outputFormat_Multiple_FirstExtensionDifferent_SecondExtensionSame() throws IOException
	{
		// given
		File f1 = new File("test-resources/Thumbnailator/grid.jpg");
		File f2 = new File("test-resources/Thumbnailator/grid.png");
		
		// when
		Thumbnails.of(f1, f2)
			.size(50, 50)
			.outputFormat("png")
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		BufferedImage fromFileImage2 = ImageIO.read(outFile2);
		
		String formatName1 = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile1)).next().getFormatName();
		assertEquals("png", formatName1);
		String formatName2 = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile2)).next().getFormatName();
		assertEquals("png", formatName2);
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		assertEquals(50, fromFileImage2.getWidth());
		assertEquals(50, fromFileImage2.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>multiple files</li>
	 * <li>outputFormat</li>
	 * <li>toFile(File)</li>
	 * <li>1st file: file extension differs from format specified</li>
	 * <li>2nd file: file extension differs from format specified</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The output format of the image is one that is specified.</li>
	 * <li>For the file with the different format and extension, the extension
	 * of the specified format will be added to the file name.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void outputFormat_Multiple_FirstExtensionDifferent_SecondExtensionDifferent() throws IOException
	{
		// given
		File f1 = new File("test-resources/Thumbnailator/grid.jpg");
		File f2 = new File("test-resources/Thumbnailator/grid.bmp");
		
		// when
		Thumbnails.of(f1, f2)
			.size(50, 50)
			.outputFormat("png")
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		// then
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.bmp.png");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		BufferedImage fromFileImage2 = ImageIO.read(outFile2);
		
		String formatName1 = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile1)).next().getFormatName();
		assertEquals("png", formatName1);
		String formatName2 = ImageIO.getImageReaders(ImageIO.createImageInputStream(outFile2)).next().getFormatName();
		assertEquals("png", formatName2);
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		assertEquals(50, fromFileImage2.getWidth());
		assertEquals(50, fromFileImage2.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>outputFormat with a supported format</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>No exception is thrown.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void outputFormat_SupportedFormat() throws IOException
	{
		// given
		File f1 = new File("test-resources/Thumbnailator/grid.jpg");
		
		// when
		Thumbnails.of(f1)
			.size(50, 50)
			.outputFormat("png");
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>outputFormat with a supported format</li>
	 * <li>toFile(File)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalArgumentException is thrown.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void outputFormat_UnsupportedFormat() throws IOException
	{
		// given
		File f1 = new File("test-resources/Thumbnailator/grid.jpg");
		
		// when
		Thumbnails.of(f1)
			.size(50, 50)
			.outputFormat("unsupported");
		
		// then
		// expect an IllagelArgumentExceptio.
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(BufferedImage...)</li>
	 * <li>where the BufferedImage[] is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void of_BufferedImage_null()
	{
		BufferedImage[] img = null;
		
		try
		{
			Thumbnails.of(img);
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Cannot specify null for images.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(File...)</li>
	 * <li>where the File[] is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void of_File_null()
	{
		File[] f = null;
		
		try
		{
			Thumbnails.of(f);
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Cannot specify null for input files.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(String...)</li>
	 * <li>where the String[] is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void of_Strings_null()
	{
		String[] f = null;
		
		try
		{
			Thumbnails.of(f);
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Cannot specify null for input files.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(BufferedImage...)</li>
	 * <li>where the BufferedImage[] is length 0.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void of_BufferedImage_empty()
	{
		BufferedImage[] img = new BufferedImage[0];
		
		try
		{
			Thumbnails.of(img);
			fail();
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Cannot specify an empty array for images.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(File...)</li>
	 * <li>where the File[] is length 0.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void of_File_empty()
	{
		File[] f = new File[0];
		
		try
		{
			Thumbnails.of(f);
			fail();
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Cannot specify an empty array for input files.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(String...)</li>
	 * <li>where the String[] is length 0.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void of_Strings_empty()
	{
		String[] f = new String[0];
		
		try
		{
			Thumbnails.of(f);
			fail();
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Cannot specify an empty array for input files.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromImages(Collection)</li>
	 * <li>where the Collection is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void fromImages_Collection_null()
	{
		try
		{
			Thumbnails.fromImages(null);
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Cannot specify null for images.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFiles(Collection)</li>
	 * <li>where the Collection is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void fromFiles_Collection_null()
	{
		try
		{
			Thumbnails.fromFiles(null);
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Cannot specify null for input files.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFilenames(Collection)</li>
	 * <li>where the Collection is null.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A NullPointerException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=NullPointerException.class)
	public void fromFilenames_Collection_null()
	{
		try
		{
			Thumbnails.fromFilenames(null);
			fail();
		}
		catch (NullPointerException e)
		{
			assertEquals("Cannot specify null for input files.", e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromImages(Collection)</li>
	 * <li>where the Collection is empty.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void fromImages_Collection_empty()
	{
		try
		{
			Thumbnails.fromImages(Collections.<BufferedImage>emptyList());
			fail();
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Cannot specify an empty collection for images.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFiles(Collection)</li>
	 * <li>where the Collection is empty.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void fromFiles_Collection_empty()
	{
		try
		{
			Thumbnails.fromFiles(Collections.<File>emptyList());
			fail();
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Cannot specify an empty collection for input files.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFilenames(Collection)</li>
	 * <li>where the Collection is empty.</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A IllegalArgumentException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void fromFilenames_Collection_empty()
	{
		try
		{
			Thumbnails.fromFilenames(Collections.<String>emptyList());
			fail();
		}
		catch (IllegalArgumentException e)
		{
			assertEquals("Cannot specify an empty collection for input files.", e.getMessage());
			throw e;
		}
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(BufferedImage)</li>
	 * <li>asBufferedImage()</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A BufferedImage is returned</li>
	 * </ol>
	 */	
	@Test
	public void of_BufferedImage_asBufferedImage()
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
	 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
	 * <li>asBufferedImage()</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void of_BufferedImages_asBufferedImage()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		Thumbnails.of(img, img)
			.size(100, 100)
			.asBufferedImage();
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(BufferedImage)</li>
	 * <li>asBufferedImages()</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException is thrown.</li>
	 * </ol>
	 */	
	@Test
	public void of_BufferedImage_asBufferedImages()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		List<BufferedImage> thumbnails = Thumbnails.of(img)
			.size(100, 100)
			.asBufferedImages();
		
		assertEquals(100, thumbnails.get(0).getWidth());
		assertEquals(100, thumbnails.get(0).getHeight());
		assertEquals(1, thumbnails.size());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(BufferedImage, BufferedImage)</li>
	 * <li>asBufferedImage()</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException is thrown.</li>
	 * </ol>
	 */	
	@Test
	public void of_BufferedImages_asBufferedImages()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		List<BufferedImage> thumbnails = Thumbnails.of(img, img)
			.size(100, 100)
			.asBufferedImages();
		
		assertEquals(100, thumbnails.get(0).getWidth());
		assertEquals(100, thumbnails.get(0).getHeight());
		assertEquals(100, thumbnails.get(1).getWidth());
		assertEquals(100, thumbnails.get(1).getHeight());
		assertEquals(2, thumbnails.size());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromImages([BufferedImage])</li>
	 * <li>asBufferedImage()</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A BufferedImage is returned</li>
	 * </ol>
	 */	
	@Test
	public void fromImages_Single_asBufferedImage()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		BufferedImage thumbnail = Thumbnails.fromImages(Arrays.asList(img))
			.size(100, 100)
			.asBufferedImage();
		
		assertEquals(100, thumbnail.getWidth());
		assertEquals(100, thumbnail.getHeight());
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromImages([BufferedImage, BufferedImage])</li>
	 * <li>asBufferedImage()</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException is thrown.</li>
	 * </ol>
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void fromImages_Multiple_asBufferedImage()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		Thumbnails.fromImages(Arrays.asList(img, img))
			.size(100, 100)
			.asBufferedImage();
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromImages([BufferedImage])</li>
	 * <li>asBufferedImages()</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException is thrown.</li>
	 * </ol>
	 */	
	@Test
	public void fromImages_Single_asBufferedImages()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		List<BufferedImage> thumbnails = Thumbnails.fromImages(Arrays.asList(img))
			.size(100, 100)
			.asBufferedImages();
		
		assertEquals(100, thumbnails.get(0).getWidth());
		assertEquals(100, thumbnails.get(0).getHeight());
		assertEquals(1, thumbnails.size());
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromImages([BufferedImage, BufferedImage])</li>
	 * <li>asBufferedImage()</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException is thrown.</li>
	 * </ol>
	 */	
	@Test
	public void fromImages_Multiple_asBufferedImages()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		List<BufferedImage> thumbnails = Thumbnails.fromImages(Arrays.asList(img, img))
			.size(100, 100)
			.asBufferedImages();
		
		assertEquals(100, thumbnails.get(0).getWidth());
		assertEquals(100, thumbnails.get(0).getHeight());
		assertEquals(100, thumbnails.get(1).getWidth());
		assertEquals(100, thumbnails.get(1).getHeight());
		assertEquals(2, thumbnails.size());
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(File)</li>
	 * <li>toFile(File)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An image is written to the specified file.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void of_File_toFile() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.png");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.toFile(outFile);
		
		BufferedImage fromFileImage = ImageIO.read(outFile);
		
		assertEquals(50, fromFileImage.getWidth());
		assertEquals(50, fromFileImage.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(File, File)</li>
	 * <li>toFile(File)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalArgumentException is thrown.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void of_Files_toFile() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.png");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
		Thumbnails.of(f, f)
			.size(50, 50)
			.toFile(outFile);
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(File)</li>
	 * <li>toFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An image is generated and written to a file whose name is generated
	 * from the Rename object.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void of_File_toFiles() throws IOException
	{
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		Thumbnails.of(f1)
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(File, File)</li>
	 * <li>toFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Two images are generated and written to a file whose name is
	 * generated from the Rename object.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void of_Files_toFiles() throws IOException
	{
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File f2 = new File("test-resources/Thumbnailator/grid.jpg");
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		Thumbnails.of(f1, f2)
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		BufferedImage fromFileImage2 = ImageIO.read(outFile2);
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		assertEquals(50, fromFileImage2.getWidth());
		assertEquals(50, fromFileImage2.getHeight());
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(File)</li>
	 * <li>asFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An image is generated and written to a file whose name is generated
	 * from the Rename object.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void of_File_asFiles() throws IOException
	{
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		List<File> thumbnails = Thumbnails.of(f1)
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		assertEquals(1, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(File, File)</li>
	 * <li>asFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Two images are generated and written to a file whose name is
	 * generated from the Rename object.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void of_Files_asFiles() throws IOException
	{
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File f2 = new File("test-resources/Thumbnailator/grid.jpg");
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		List<File> thumbnails = Thumbnails.of(f1, f2)
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		assertEquals(2, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
		BufferedImage fromFileImage2 = ImageIO.read(thumbnails.get(0));
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		assertEquals(50, fromFileImage2.getWidth());
		assertEquals(50, fromFileImage2.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFiles([File])</li>
	 * <li>toFile(File)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An image is written to the specified file.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void fromFiles_Single_toFile() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.png");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
		Thumbnails.fromFiles(Arrays.asList(f))
			.size(50, 50)
			.toFile(outFile);
		
		BufferedImage fromFileImage = ImageIO.read(outFile);
		
		assertEquals(50, fromFileImage.getWidth());
		assertEquals(50, fromFileImage.getHeight());
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFiles([File, File])</li>
	 * <li>toFile(File)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalArgumentException is thrown.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void fromFiles_Multiple_toFile() throws IOException
	{
		File f = new File("test-resources/Thumbnailator/grid.png");
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
		Thumbnails.fromFiles(Arrays.asList(f, f))
			.size(50, 50)
			.toFile(outFile);
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFiles([File])</li>
	 * <li>toFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An image is generated and written to a file whose name is generated
	 * from the Rename object.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void fromFiles_Single_toFiles() throws IOException
	{
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		Thumbnails.fromFiles(Arrays.asList(f1))
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFiles([File, File])</li>
	 * <li>toFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Two images are generated and written to a file whose name is
	 * generated from the Rename object.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void fromFiles_Multiple_toFiles() throws IOException
	{
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File f2 = new File("test-resources/Thumbnailator/grid.jpg");
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		Thumbnails.fromFiles(Arrays.asList(f1, f2))
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		BufferedImage fromFileImage2 = ImageIO.read(outFile2);
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		assertEquals(50, fromFileImage2.getWidth());
		assertEquals(50, fromFileImage2.getHeight());
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFiles([File])</li>
	 * <li>asFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An image is generated and written to a file whose name is generated
	 * from the Rename object.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void fromFiles_Single_asFiles() throws IOException
	{
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		List<File> thumbnails = Thumbnails.fromFiles(Arrays.asList(f1))
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		assertEquals(1, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFiles([File, File])</li>
	 * <li>asFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Two images are generated and written to a file whose name is
	 * generated from the Rename object.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void fromFiles_Multiple_asFiles() throws IOException
	{
		File f1 = new File("test-resources/Thumbnailator/grid.png");
		File f2 = new File("test-resources/Thumbnailator/grid.jpg");
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		List<File> thumbnails = Thumbnails.fromFiles(Arrays.asList(f1, f2))
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		assertEquals(2, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
		BufferedImage fromFileImage2 = ImageIO.read(thumbnails.get(0));
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		assertEquals(50, fromFileImage2.getWidth());
		assertEquals(50, fromFileImage2.getHeight());
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(String)</li>
	 * <li>toFile(File)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An image is written to the specified file.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void of_String_toFile() throws IOException
	{
		String f = "test-resources/Thumbnailator/grid.png";
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
		Thumbnails.of(f)
			.size(50, 50)
			.toFile(outFile);
		
		BufferedImage fromFileImage = ImageIO.read(outFile);
		
		assertEquals(50, fromFileImage.getWidth());
		assertEquals(50, fromFileImage.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(String, String)</li>
	 * <li>toFile(File)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalArgumentException is thrown.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void of_Strings_toFile() throws IOException
	{
		String f = "test-resources/Thumbnailator/grid.png";
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
		Thumbnails.of(f, f)
			.size(50, 50)
			.toFile(outFile);
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(String)</li>
	 * <li>toFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An image is generated and written to a file whose name is generated
	 * from the Rename object.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void of_String_toFiles() throws IOException
	{
		String f1 = "test-resources/Thumbnailator/grid.png";
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		Thumbnails.of(f1)
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(String, String)</li>
	 * <li>toFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Two images are generated and written to a file whose name is
	 * generated from the Rename object.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void of_Strings_toFiles() throws IOException
	{
		String f1 = "test-resources/Thumbnailator/grid.png";
		String f2 = "test-resources/Thumbnailator/grid.jpg";
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		Thumbnails.of(f1, f2)
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		BufferedImage fromFileImage2 = ImageIO.read(outFile2);
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		assertEquals(50, fromFileImage2.getWidth());
		assertEquals(50, fromFileImage2.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(String)</li>
	 * <li>toFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An image is generated and written to a file whose name is generated
	 * from the Rename object.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void of_String_asFiles() throws IOException
	{
		String f1 = "test-resources/Thumbnailator/grid.png";
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		List<File> thumbnails = Thumbnails.of(f1)
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		assertEquals(1, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
	}
	
	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.of(String, String)</li>
	 * <li>toFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Two images are generated and written to a file whose name is
	 * generated from the Rename object.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void of_Strings_asFiles() throws IOException
	{
		String f1 = "test-resources/Thumbnailator/grid.png";
		String f2 = "test-resources/Thumbnailator/grid.jpg";
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		List<File> thumbnails = Thumbnails.of(f1, f2)
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		assertEquals(2, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		BufferedImage fromFileImage2 = ImageIO.read(outFile2);
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		assertEquals(50, fromFileImage2.getWidth());
		assertEquals(50, fromFileImage2.getHeight());
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFilenames([String])</li>
	 * <li>toFile(File)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An image is written to the specified file.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void fromFilenames_Single_toFile() throws IOException
	{
		String f = "test-resources/Thumbnailator/grid.png";
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
		Thumbnails.fromFilenames(Arrays.asList(f))
			.size(50, 50)
			.toFile(outFile);
		
		BufferedImage fromFileImage = ImageIO.read(outFile);
		
		assertEquals(50, fromFileImage.getWidth());
		assertEquals(50, fromFileImage.getHeight());
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFilenames([String, String])</li>
	 * <li>toFile(File)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalArgumentException is thrown.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test(expected=IllegalArgumentException.class)
	public void fromFilenames_Multiple_toFile() throws IOException
	{
		String f = "test-resources/Thumbnailator/grid.png";
		File outFile = new File("test-resources/Thumbnailator/grid.tmp.png");
		outFile.deleteOnExit();
		
		Thumbnails.fromFilenames(Arrays.asList(f, f))
			.size(50, 50)
			.toFile(outFile);
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFilenames([String])</li>
	 * <li>toFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An image is generated and written to a file whose name is generated
	 * from the Rename object.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void fromFilenames_Single_toFiles() throws IOException
	{
		String f1 = "test-resources/Thumbnailator/grid.png";
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		Thumbnails.fromFilenames(Arrays.asList(f1))
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFilenames([String, String])</li>
	 * <li>toFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Two images are generated and written to a file whose name is
	 * generated from the Rename object.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void fromFilenames_Multiple_toFiles() throws IOException
	{
		String f1 = "test-resources/Thumbnailator/grid.png";
		String f2 = "test-resources/Thumbnailator/grid.jpg";
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		Thumbnails.fromFilenames(Arrays.asList(f1, f2))
			.size(50, 50)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		BufferedImage fromFileImage2 = ImageIO.read(outFile2);
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		assertEquals(50, fromFileImage2.getWidth());
		assertEquals(50, fromFileImage2.getHeight());
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFilenames([String])</li>
	 * <li>toFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An image is generated and written to a file whose name is generated
	 * from the Rename object.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void fromFilenames_Single_asFiles() throws IOException
	{
		String f1 = "test-resources/Thumbnailator/grid.png";
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outFile1.deleteOnExit();
		
		List<File> thumbnails = Thumbnails.fromFilenames(Arrays.asList(f1))
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		assertEquals(1, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(thumbnails.get(0));
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
	}

	/**
	 * Test for the {@link Thumbnails.Builder} class where,
	 * <ol>
	 * <li>Thumbnails.fromFilenames([String, String])</li>
	 * <li>toFiles(Rename)</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>Two images are generated and written to a file whose name is
	 * generated from the Rename object.</li>
	 * </ol>
	 * @throws IOException 
	 */	
	@Test
	public void fromFilenames_Multiple_asFiles() throws IOException
	{
		String f1 = "test-resources/Thumbnailator/grid.png";
		String f2 = "test-resources/Thumbnailator/grid.jpg";
		File outFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		File outFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg");
		outFile1.deleteOnExit();
		outFile2.deleteOnExit();
		
		List<File> thumbnails = Thumbnails.fromFilenames(Arrays.asList(f1, f2))
			.size(50, 50)
			.asFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		assertEquals(2, thumbnails.size());
		
		BufferedImage fromFileImage1 = ImageIO.read(outFile1);
		BufferedImage fromFileImage2 = ImageIO.read(outFile2);
		
		assertEquals(50, fromFileImage1.getWidth());
		assertEquals(50, fromFileImage1.getHeight());
		assertEquals(50, fromFileImage2.getWidth());
		assertEquals(50, fromFileImage2.getHeight());
	}
}
