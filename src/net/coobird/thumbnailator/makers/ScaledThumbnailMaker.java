package net.coobird.thumbnailator.makers;

import java.awt.image.BufferedImage;

/**
 * A {@link ThumbnailMaker} which scales an image by a specified scaling factor
 * when producing a thumbnail.
 * <p>
 * <DL>
 * <DT><B>Usage:</B></DT>
 * <DD>
 * The following example demonstrates how to create a thumbnail which is 25%
 * the size of the source image:
 * <pre>
BufferedImage img = ImageIO.read(new File("sourceImage.jpg"));
BufferedImage thumbnail = new ScaledThumbnailMaker().scale(0.25).make(img);
 * </pre>
 * </DD>
 * </DL>
 * 
 * @author coobird
 *
 */
public class ScaledThumbnailMaker extends ThumbnailMaker
{
	
	private static final String PARAM_SCALE = "scale";
	
	/**
	 * The scaling factor to apply when resizing an image to create a thumbnail.
	 */
	private double factor;
	
	/**
	 * <p>
	 * Creates an instance of {@code ScaledThumbnailMaker} without the 
	 * scaling factor specified.
	 * </p>
	 * <p>
	 * To use this {@code ScaledThumbnailMaker}, one must specify the 
	 * scaling factor to use by calling the {@link #scale(double)} method
	 * before generating a thumbnail.
	 * </p>
	 */
	public ScaledThumbnailMaker()
	{
		super();
		ready.unset(PARAM_SCALE);
	}
	
	/**
	 * Creates an instance of {@code ScaledThumbnailMaker} with the specified 
	 * scaling factor.
	 * 
	 * @param factor			The scaling factor to apply when resizing an
	 * 							image to create a thumbnail.
	 */
	public ScaledThumbnailMaker(double factor)
	{
		this();
		scale(factor);
	}
	
	/**
	 * <p>
	 * Sets the scaling factor for the thumbnail.
	 * </p>
	 * <p>
	 * The aspect ratio of the resulting image is unaltered from the original.
	 * </p>
	 * 
	 * @param factor			The scaling factor to apply when resizing an
	 * 							image to create a thumbnail.
	 * @return					A reference to this object.
	 * @throws IllegalStateException	If the scaling factor has already
	 * 									been previously set.
	 */
	public ScaledThumbnailMaker scale(double factor)
	{
		if (ready.isSet(PARAM_SCALE))
		{
			throw new IllegalStateException(
					"The scaling factor has already been set."
			);
		}
		
		this.factor = factor;
		ready.set(PARAM_SCALE);
		return this;
	}
	
	@Override
	public BufferedImage make(BufferedImage img)
	{
		int width = (int)(img.getWidth() * factor);
		int height = (int)(img.getHeight() * factor);
		
		return super.makeThumbnail(img, width, height);
	}
}
