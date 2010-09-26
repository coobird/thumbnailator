package net.coobird.thumbnailator.resizers;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Map;

/**
 * Image resizer class using bicubic interpolation for the resizing operation.
 * 
 * @author coobird
 *
 */
public class BicubicResizer extends AbstractResizer
{
	/**
	 * 
	 */
	public BicubicResizer()
	{
		this(Collections.<RenderingHints.Key, Object>emptyMap());
	}
	
	/**
	 * 
	 * @param hints
	 */
	public BicubicResizer(Map<RenderingHints.Key, Object> hints)
	{
		super(RenderingHints.VALUE_INTERPOLATION_BICUBIC, hints);
	}
	
	/**
	 * <p>
	 * Resizes an image using bicubic interpolation.
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