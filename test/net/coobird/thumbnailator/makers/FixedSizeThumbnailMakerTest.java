package net.coobird.thumbnailator.makers;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.resizers.ResizerFactory;
import net.coobird.thumbnailator.resizers.Resizers;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * A class which tests the behavior of the 
 * {@link FixedSizeThumbnailMaker} class.
 * 
 * @author coobird
 *
 */
public class FixedSizeThumbnailMakerTest
{
	/**
	 * A convenience method to make a test image.
	 */
	private static BufferedImage makeTestImage200x200() {
		return new BufferedImageBuilder(200, 200).build();
	}
	
	/**
	 * Test for the {@link FixedSizeThumbnailMaker} class where,
	 * <ol>
	 * <li>It is initialized with the no-args constructor</li>
	 * <li>the keepAspectRatio method is not called</li>
	 * <li>And finally the make method is called</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException occurs.</li>
	 * </ol>
	 */
	@Test(expected=IllegalStateException.class)
	public void uninitializedWithNoArgConstructor()
	{
		BufferedImage img = makeTestImage200x200();
		
		new FixedSizeThumbnailMaker().make(img);
		
		fail();
	}
	
	/**
	 * Test for the {@link FixedSizeThumbnailMaker} class where,
	 * <ol>
	 * <li>It is initialized with the two argument constructor</li>
	 * <li>the keepAspectRatio method is not called</li>
	 * <li>And finally the make method is called</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException occurs.</li>
	 * </ol>
	 */
	@Test(expected=IllegalStateException.class)
	public void uninitializedWithTwoArgConstructor()
	{
		BufferedImage img = makeTestImage200x200();
		
		new FixedSizeThumbnailMaker(100, 100).make(img);
		
		fail();
	}

	/**
	 * Test for the {@link FixedSizeThumbnailMaker} class where,
	 * <ol>
	 * <li>It is initialized with the no argument constructor</li>
	 * <li>The keepAspectRatio method is called with true</li>
	 * <li>And finally the make method is called</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException occurs, due to not specifying the
	 * dimensions of the thumbnail.</li>
	 * </ol>
	 */
	@Test(expected=IllegalStateException.class)
	public void unintializedNoArgConstructorAndAspectRatioSpecified()
	{
		BufferedImage img = makeTestImage200x200();
		
		new FixedSizeThumbnailMaker()
				.keepAspectRatio(true)
				.make(img);
	}

	/**
	 * Test for the {@link FixedSizeThumbnailMaker} class where,
	 * <ol>
	 * <li>It is initialized with the two argument constructor</li>
	 * <li>The keepAspectRatio method is called with true</li>
	 * <li>And finally the make method is called</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A thumbnail image is created with the specified size, and the
	 * imageType is the default type.</li>
	 * </ol>
	 */
	@Test
	public void twoArgConstructorAndAspectRatioSpecified()
	{
		BufferedImage img = makeTestImage200x200();
		
		BufferedImage thumbnail = new FixedSizeThumbnailMaker(100, 100)
				.keepAspectRatio(true)
				.make(img);
		
		assertEquals(100, thumbnail.getWidth());
		assertEquals(100, thumbnail.getHeight());
		assertEquals(BufferedImage.TYPE_INT_ARGB, thumbnail.getType());
	}
	
	/**
	 * Test for the {@link FixedSizeThumbnailMaker} class where,
	 * <ol>
	 * <li>It is initialized with the two argument constructor</li>
	 * <li>The keepAspectRatio method is called with true</li>
	 * <li>The vertical dimension is smaller</li>
	 * <li>And finally the make method is called</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A thumbnail image is created with the size based on the
	 * vertical dimensions, keeping the aspect ratio of the original
	 * and the imageType is the default type.</li>
	 * </ol>
	 */
	@Test
	public void keepAspectRatioWithOffRatioTargetSizeForVertical()
	{
		BufferedImage img = makeTestImage200x200();
		
		BufferedImage thumbnail = new FixedSizeThumbnailMaker(100, 50)
				.keepAspectRatio(true)
				.make(img);
		
		assertEquals(50, thumbnail.getWidth());
		assertEquals(50, thumbnail.getHeight());
		assertEquals(BufferedImage.TYPE_INT_ARGB, thumbnail.getType());
	}

	/**
	 * Test for the {@link FixedSizeThumbnailMaker} class where,
	 * <ol>
	 * <li>It is initialized with the two argument constructor</li>
	 * <li>The keepAspectRatio method is called with true</li>
	 * <li>The horizontal dimension is smaller</li>
	 * <li>And finally the make method is called</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A thumbnail image is created with the size based on the
	 * horizontal dimensions, keeping the aspect ratio of the original
	 * and the imageType is the default type.</li>
	 * </ol>
	 */
	@Test
	public void keepAspectRatioWithOffRatioTargetSizeForHorizontal()
	{
		BufferedImage img = makeTestImage200x200();
		
		BufferedImage thumbnail = new FixedSizeThumbnailMaker(50, 100)
			.keepAspectRatio(true)
			.make(img);
		
		assertEquals(50, thumbnail.getWidth());
		assertEquals(50, thumbnail.getHeight());
		assertEquals(BufferedImage.TYPE_INT_ARGB, thumbnail.getType());
	}
	
	/**
	 * Test for the {@link FixedSizeThumbnailMaker} class where,
	 * <ol>
	 * <li>It is initialized with the two argument constructor</li>
	 * <li>The keepAspectRatio method is called with false</li>
	 * <li>The vertical dimension is smaller</li>
	 * <li>And finally the make method is called</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A thumbnail image is created with the size that matches the
	 * specified dimensions, and the imageType is the default type.</li>
	 * </ol>
	 */
	@Test
	public void noKeepAspectRatioWithOffRatioTargetSizeForVertical()
	{
		BufferedImage img = makeTestImage200x200();
		
		BufferedImage thumbnail = new FixedSizeThumbnailMaker(100, 50)
			.keepAspectRatio(false)
			.make(img);
		
		assertEquals(100, thumbnail.getWidth());
		assertEquals(50, thumbnail.getHeight());
		assertEquals(BufferedImage.TYPE_INT_ARGB, thumbnail.getType());
	}
	
	/**
	 * Test for the {@link FixedSizeThumbnailMaker} class where,
	 * <ol>
	 * <li>It is initialized with the two argument constructor</li>
	 * <li>The keepAspectRatio method is called with false</li>
	 * <li>The horizontal dimension is smaller</li>
	 * <li>And finally the make method is called</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A thumbnail image is created with the size that matches the
	 * specified dimensions, and the imageType is the default type.</li>
	 * </ol>
	 */
	@Test
	public void noKeepAspectRatioWithOffRatioTargetSizeForHorizontal()
	{
		BufferedImage img = makeTestImage200x200();
		
		BufferedImage thumbnail = new FixedSizeThumbnailMaker(50, 100)
			.keepAspectRatio(false)
			.make(img);
		
		assertEquals(50, thumbnail.getWidth());
		assertEquals(100, thumbnail.getHeight());
		assertEquals(BufferedImage.TYPE_INT_ARGB, thumbnail.getType());
	}
	
	/**
	 * Test for the {@link FixedSizeThumbnailMaker} class where,
	 * <ol>
	 * <li>It is initialized with the two argument constructor</li>
	 * <li>The size method is called</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException is thrown because the size has already
	 * been set.</li>
	 * </ol>
	 */
	@Test(expected=IllegalStateException.class)
	public void twoArgConstructorThenSize()
	{
		BufferedImage img = makeTestImage200x200();
		
		new FixedSizeThumbnailMaker(50, 100)
			.size(50, 100)
			.make(img);
	}
	/**
	 * Test for the {@link FixedSizeThumbnailMaker} class where,
	 * <ol>
	 * <li>It is initialized with the three argument constructor</li>
	 * <li>The aspectRatio method is called</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException is thrown because the aspectRatio has
	 * already been set.</li>
	 * </ol>
	 */
	@Test(expected=IllegalStateException.class)
	public void threeArgConstructorThenSize()
	{
		BufferedImage img = makeTestImage200x200();
		
		new FixedSizeThumbnailMaker(50, 100, true)
			.keepAspectRatio(true)
			.make(img);
	}
	
	/**
	 * Test for the {@link FixedSizeThumbnailMaker} class where,
	 * <ol>
	 * <li>An thumbnail is to be made</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>The ResizerFactory is used to obtain a Resizer.</li>
	 * </ol>
	 */
	@Test
	public void verifyResizerFactoryBeingCalled()
	{
		// given
		BufferedImage img = makeTestImage200x200();
		ResizerFactory resizerFactory = mock(ResizerFactory.class);
		when(resizerFactory.getResizer(any(Dimension.class), any(Dimension.class)))
				.thenReturn(Resizers.PROGRESSIVE);
		
		// when
		new FixedSizeThumbnailMaker(100, 100)
				.keepAspectRatio(true)
				.resizerFactory(resizerFactory)
				.make(img);
		
		// then
		verify(resizerFactory, atLeastOnce())
				.getResizer(new Dimension(200, 200), new Dimension(100, 100));
	}
}
