package net.coobird.thumbnailator.filters;

import java.awt.image.BufferedImage;

/**
 * This interface is to be implemented by classes which performs an image
 * filtering operation on a {@link BufferedImage}.
 * <p>
 * The general contract for classes implementing {@link ImageFilter} is that
 * they should not change the contents of the {@link BufferedImage} which is
 * given as the argument for the {@link #apply(BufferedImage)} method.
 * <p>
 * The filter should make a copy of the given {@link BufferedImage}, and
 * perform the filtering operations on the copy, then return the copy.
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
