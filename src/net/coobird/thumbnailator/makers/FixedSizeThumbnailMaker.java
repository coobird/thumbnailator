package net.coobird.thumbnailator.makers;

import java.awt.image.BufferedImage;

/**
 * A {@link ThumbnailMaker} which resizes an image to a specified dimension
 * when producing a thumbnail.
 * <p>
 * Optionally, if the aspect ratio of the thumbnail is to be maintained the same
 * as the original image (by calling the {@link #keepAspectRatio(boolean)} 
 * method with the value {@code true}), then the dimensions specified by the
 * {@link #size(int, int)} method, {@link #FixedSizeThumbnailMaker(int, int)} or
 * {@link #FixedSizeThumbnailMaker(int, int, boolean)} constructor will be used
 * as the maximum constraint of dimensions of the thumbnail.
 * <p>
 * In other words, when the aspect ratio is to be kept constant, then
 * thumbnails which are created will be sized to fit inside the dimensions
 * specified by the size parameter. 
 * <p>
 * <DL>
 * <DT><B>Usage:</B></DT>
 * <DD>
 * The following example demonstrates how to create a thumbnail which fits
 * within 200 pixels by 200 pixels, while preserving the aspect ratio of the
 * source image:
 * <pre>
BufferedImage img = ImageIO.read(new File("sourceImage.jpg"));

BufferedImage thumbnail = new FixedSizeThumbnailMaker()
        .size(200, 200)
        .keepAspectRatio(true)
        .make(img);
 * </pre>
 * </DD>
 * </DL>
 * 
 * @author coobird
 *
 */
public class FixedSizeThumbnailMaker extends ThumbnailMaker
{
	private static final String PARAM_SIZE = "size";
	private static final String PARAM_KEEP_RATIO = "keepRatio";
	
	private int width;
	private int height;
	private boolean keepRatio;
	
	/**
	 * Creates a {@link FixedSizeThumbnailMaker}.
	 * <p>
	 * The size of the resulting thumbnail, and whether or not the aspect ratio
	 * of the original image should be maintained in the thumbnail must be
	 * set before this instance is able to produce thumbnails.
	 */
	public FixedSizeThumbnailMaker()
	{
		super();
		ready.unset(PARAM_SIZE);
		ready.unset(PARAM_KEEP_RATIO);
	}
	
	/**
	 * Creates a {@link FixedSizeThumbnailMaker} which is to create thumbnails
	 * with the specified size.
	 * <p>
	 * Before this instance is able to produce thumbnails, whether or not the
	 * aspect ratio of the original image should be maintained in the thumbnail
	 * must be specified by calling the {@link #keepAspectRatio(boolean)}
	 * method.
	 * 
	 * @param width			The width of the thumbnail to produce.
	 * @param height		The height of the thumbnails to produce.
	 */
	public FixedSizeThumbnailMaker(int width, int height)
	{
		this();
		size(width, height);
	}
	
	/**
	 * Creates a {@link FixedSizeThumbnailMaker} which is to create thumbnails
	 * with the specified size, and also preserves the aspect ratio of the 
	 * original image in the thumbnail.
	 * 
	 * @param width			The width of the thumbnail to produce.
	 * @param height		The height of the thumbnails to produce.
	 * @param aspectRatio	Whether or not to maintain the aspect ratio in the
	 * 						thumbnail the same as the original image.
	 * 						<p>
	 * 						If {@code true} is specified, then the 
	 * 						thumbnail image will have the same aspect ratio
	 * 						as the original image.
	 */
	public FixedSizeThumbnailMaker(int width, int height, boolean aspectRatio)
	{
		this();
		size(width, height);
		keepAspectRatio(aspectRatio);
	}
	
	/**
	 * Sets the size of the thumbnail to produce.
	 * 
	 * @param width				The width of the thumbnail to produce.
	 * @param height			The height of the thumbnails to produce.
	 * @return					A reference to this object.
	 */
	public FixedSizeThumbnailMaker size(int width, int height)
	{
		this.width = width;
		this.height = height;

		ready.set(PARAM_SIZE);
		return this;
	}
	
	/**
	 * Sets whether or not the thumbnail is to maintain the aspect ratio of
	 * the original image.
	 * 
	 * @param keep			Whether or not to maintain the aspect ratio in the
	 * 						thumbnail the same as the original image.
	 * 						<p>
	 * 						If {@code true} is specified, then the 
	 * 						thumbnail image will have the same aspect ratio
	 * 						as the original image.
 	 * @return				A reference to this object.
	 */
	public FixedSizeThumbnailMaker keepAspectRatio(boolean keep)
	{
		this.keepRatio = keep;

		ready.set(PARAM_KEEP_RATIO);
		return this;
	}
	
	@Override
	public BufferedImage make(BufferedImage img)
	{
		int targetWidth = this.width;
		int targetHeight = this.height;

		if (keepRatio)
		{
			int sourceWidth = img.getWidth();
			int sourceHeight = img.getHeight();
			
			double sourceRatio = (double)sourceWidth / (double)sourceHeight;
			double targetRatio = (double)targetWidth / (double)targetHeight;
			
			/*
			 *  If the ratios are not the same, then choose a width and height
			 *  which will fit within the target width and height.
			 */
			if (Double.compare(sourceRatio, targetRatio) != 0)
			{
				if (sourceRatio > targetRatio)
				{
					targetWidth = width;
					targetHeight = (int)(targetWidth / sourceRatio);
				}
				else 
				{
					targetWidth = (int)(targetHeight * sourceRatio);
					targetHeight = height;
				}
			}
		}
		
		return super.makeThumbnail(img, targetWidth, targetHeight);
	}
}