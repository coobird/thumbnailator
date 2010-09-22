package net.coobird.thumbnailator.resizers;

import java.awt.image.BufferedImage;

/**
 * A class which performs a resize operation on a source image and outputs the
 * result to a destination image.
 * 
 * @author coobird
 *
 */
public abstract class AbstractResizer implements Resizer
{
	/**
	 * This class is not intended to be instantiated.
	 */
	protected AbstractResizer() {}
	
	/**
	 * <p>
	 * Performs a resize operation from a source image and outputs to a
	 * destination image.
	 * </p>
	 * <p>
	 * If the source or destination image is {@code null}, then a 
	 * {@link NullPointerException} will be thrown.
	 * </p>
	 * 
	 * @param srcImage		The source image.
	 * @param destImage		The destination image.
	 * 
	 * @throws NullPointerException		When the source and/or the destination 
	 * 									image is {@code null}.
	 */
	public void resize(BufferedImage srcImage, BufferedImage destImage) 
	{
		if (srcImage == null || destImage == null)
		{
			throw new NullPointerException(
					"The source and/or destination image is null."
			);
		}
	}
}
