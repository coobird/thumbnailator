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
BufferedImage thumbnail = new ScaledThumbnailMaker()
        .scale(0.25)
        .make(img);
 * </pre>
 * </DD>
 * </DL>
 * It is also possible to independently specify the scaling factor for the
 * width and height. (If the two scaling factors are not equal then the aspect 
 * ratio of the original image will not be preserved.)
 * <p>
 * <DL>
 * <DT><B>Usage:</B></DT>
 * <DD>
 * The following example demonstrates how to create a thumbnail which is scaled
 * 50% in the width and 75% in the height:
 * <pre>
BufferedImage img = ImageIO.read(new File("sourceImage.jpg"));
BufferedImage thumbnail = new ScaledThumbnailMaker()
        .scale(0.50, 0.75)
        .make(img);
 * </pre>
 * </DD>
 * </DL>
 * 
 * @author coobird
 *
 */
public final class ScaledThumbnailMaker extends ThumbnailMaker
{
	private static final String PARAM_SCALE = "scale";
	
	/**
	 * The scaling factor to apply to the width when resizing an image to 
	 * create a thumbnail.
	 */
	private double widthFactor;
	
	/**
	 * The scaling factor to apply to the height when resizing an image to 
	 * create a thumbnail.
	 */
	private double heightFactor;
	
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
	 * Creates an instance of {@code ScaledThumbnailMaker} with the specified 
	 * scaling factors for the width and height.
	 * 
	 * @param widthFactor		The scaling factor to apply to the width when 
	 * 							resizing an image to create a thumbnail.
	 * @param heightFactor		The scaling factor to apply to the height when 
	 * 							resizing an image to create a thumbnail.
	 * @since	0.3.10
	 */
	public ScaledThumbnailMaker(double widthFactor, double heightFactor)
	{
		this();
		scale(widthFactor, heightFactor);
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
		
		this.widthFactor = factor;
		this.heightFactor = factor;
		ready.set(PARAM_SCALE);
		return this;
	}
	
	/**
	 * <p>
	 * Sets the scaling factors for the thumbnail.
	 * </p>
	 * 
	 * @param widthFactor		The scaling factor to apply to the width when 
	 * 							resizing an image to create a thumbnail.
	 * @param heightFactor		The scaling factor to apply to the height when 
	 * 							resizing an image to create a thumbnail.
	 * @return					A reference to this object.
	 * @throws IllegalStateException	If the scaling factor has already
	 * 									been previously set.
	 * @since	0.3.10
	 */
	public ScaledThumbnailMaker scale(double widthFactor, double heightFactor)
	{
		if (ready.isSet(PARAM_SCALE))
		{
			throw new IllegalStateException(
					"The scaling factor has already been set."
			);
		}
		
		this.widthFactor = widthFactor;
		this.heightFactor = heightFactor;
		ready.set(PARAM_SCALE);
		return this;
	}
	
	@Override
	public BufferedImage make(BufferedImage img)
	{
		int width = (int)(img.getWidth() * widthFactor);
		int height = (int)(img.getHeight() * heightFactor);

		return super.makeThumbnail(img, width, height);
	}
}
