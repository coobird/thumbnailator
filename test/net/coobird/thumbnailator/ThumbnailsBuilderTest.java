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
	public void ofBufferedImageAoBufferedImage()
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
	@Test(expected=IllegalStateException.class)
	public void ofBufferedImagesAsBufferedImage()
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		BufferedImage thumbnail = Thumbnails.of(img, img)
			.size(100, 100)
			.asBufferedImage();
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
	public void ofBufferedImagesAsBufferedImages()
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
	public void ofFileToFile() throws IOException
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
	public void ofFilesToFile() throws IOException
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
	public void ofFileToFiles() throws IOException
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
	public void ofFilesToFiles() throws IOException
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
	public void ofFileAsFiles() throws IOException
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
	public void ofFilesAsFiles() throws IOException
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
	public void ofStringToFile() throws IOException
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
	public void ofStringsToFile() throws IOException
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
	public void ofStringToFiles() throws IOException
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
	public void ofStringsToFiles() throws IOException
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
	public void ofStringAsFiles() throws IOException
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
	public void ofStringsAsFiles() throws IOException
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
}
