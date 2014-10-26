package net.coobird.thumbnailator.builders;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * A builder for creating {@link BufferedImage} with specified parameters.
 * 
 * @author coobird
 *
 */
public final class BufferedImageBuilder
{
	/**
	 * The default image type of the {@link BufferedImage}s to be created
	 * by this builder.
	 */
	private static final int DEFAULT_TYPE = BufferedImage.TYPE_INT_ARGB;
	
	/**
	 * The image type to use for the {@link BufferedImage} that is to be
	 * created.
	 */
	private int imageType;
	
	/**
	 * The width to use for the {@link BufferedImage} that is to be created.
	 */
	private int width;
	
	/**
	 * The height to use for the {@link BufferedImage} that is to be created.
	 */
	private int height;
	
	/**
	 * Instantiates a {@code BufferedImageBuilder} with the specified size, and
	 * the default image type.
	 * 
	 * @param size			The size of the {@link BufferedImage} to build.
	 */
	public BufferedImageBuilder(Dimension size)
	{
		this(size.width, size.height);
	}
	
	/**
	 * Instantiates a {@code BufferedImageBuilder} with the specified size and
	 * image type.
	 * 
	 * @param size			The size of the {@link BufferedImage} to build.
	 * @param imageType		The image type of the {@link BufferedImage} to build.
	 */
	public BufferedImageBuilder(Dimension size, int imageType)
	{
		this(size.width, size.height, imageType);
	}
	
	/**
	 * Instantiates a {@code BufferedImageBuilder} with the specified size, and
	 * the default image type.
	 * 
	 * @param width			The width of the {@link BufferedImage} to build.
	 * @param height		The height of the {@link BufferedImage} to build.
	 */
	public BufferedImageBuilder(int width, int height)
	{
		this(width, height, DEFAULT_TYPE);
	}
	
	/**
	 * Instantiates a {@code BufferedImageBuilder} with the specified size and
	 * image type.
	 * 
	 * @param width			The width of the {@link BufferedImage} to build.
	 * @param height		The height of the {@link BufferedImage} to build.
	 * @param imageType		The image type of the {@link BufferedImage} to build.
	 */
	public BufferedImageBuilder(int width, int height, int imageType)
	{
		size(width, height);
		imageType(imageType);
	}
	
	/**
	 * Generates a new {@code BufferedImage}.
	 * 
	 * @return		Returns a newly created {@link BufferedImage} from the
	 * 				parameters set in the {@link BufferedImageBuilder}.
	 */
	public BufferedImage build()
	{
		return new BufferedImage(width, height, imageType);
	}

	/**
	 * Sets the type of the image of the {@link BufferedImage}.
	 * 
	 * @param imageType		The image type to use.
	 * @return				This {@link BufferedImageBuilder} instance.
	 */
	public BufferedImageBuilder imageType(int imageType)
	{
		this.imageType = imageType;
		
		return this;
	}
	
	/**
	 * Sets the size for the {@code BufferedImage}.
	 * 
	 * @param width			The width of the image to create.
	 * @param height		The height of the image to create.
	 * @return				This {@link BufferedImageBuilder} instance.
	 */
	public BufferedImageBuilder size(int width, int height)
	{
		width(width);
		height(height);
		
		return this;
	}
	
	/**
	 * Sets the width for the {@link BufferedImage}.
	 * 
	 * @param width			The width of the image to create.
	 * @return				This {@link BufferedImageBuilder} instance.
	 */
	public BufferedImageBuilder width(int width)
	{
		if (width <= 0)
		{
			throw new IllegalArgumentException(
					"Width must be greater than 0."
			);
		}
		
		this.width = width;
		
		return this;
	}
	
	/**
	 * Sets the height for the {@link BufferedImage}.
	 * 
	 * @param height		The height of the image to create.
	 * @return				This {@link BufferedImageBuilder} instance.
	 */
	public BufferedImageBuilder height(int height)
	{
		if (height <= 0)
		{
			throw new IllegalArgumentException(
					"Height must be greater than 0."
			);
		}

		this.height = height;
		
		return this;
	}
}
