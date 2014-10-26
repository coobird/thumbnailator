package net.coobird.thumbnailator.resizers;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Map;

/**
 * Image resizer class using bilinear interpolation for the resizing operation.
 * 
 * @author coobird
 *
 */
public class BilinearResizer extends AbstractResizer
{
	/**
	 * Instantiates a {@link BilinearResizer} with default rendering hints.
	 */
	public BilinearResizer()
	{
		this(Collections.<RenderingHints.Key, Object>emptyMap());
	}
	
	/**
	 * Instantiates a {@link BilinearResizer} with the specified rendering
	 * hints.
	 * 
	 * @param hints		Additional rendering hints to apply.
	 */
	public BilinearResizer(Map<RenderingHints.Key, Object> hints)
	{
		super(RenderingHints.VALUE_INTERPOLATION_BILINEAR, hints);
	}
	
	/**
	 * <p>
	 * Resizes an image using bilinear interpolation.
	 * </p>
	 * <p>
	 * If the source and/or destination image is {@code null}, then a
	 * {@link NullPointerException} will be thrown.
	 * </p>
	 * 
	 * @param srcImage		The source image.
	 * @param destImage		The destination image.
	 * 
	 * @throws NullPointerException		When the source and/or the destination
	 * 									image is {@code null}.
	 */
	@Override
	public void resize(BufferedImage srcImage, BufferedImage destImage)
			throws NullPointerException
	{
		super.resize(srcImage, destImage);
	}
}
