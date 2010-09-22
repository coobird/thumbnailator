package net.coobird.thumbnailator.filters;

import java.awt.image.BufferedImage;

/**
 * This interface is to be implemented by classes which performs an image
 * filtering operation on a {@link BufferedImage}.
 * 
 * @author coobird
 *
 */
public interface ImageFilter
{
	/**
	 * Applies a image filtering operation on an image.
	 * 
	 * @param img		The image to apply the filtering on.
	 * @return			The resulting image after applying this filter.
	 */
	public BufferedImage apply(BufferedImage img);
}
