package net.coobird.thumbnailator.resizers;

import java.awt.image.BufferedImage;

/**
 * This interface is implemented by classes which perform resizing operations.
 * 
 * @author coobird
 *
 */
public interface Resizer
{
	/**
	 * Resizes an image.
	 * <p>
	 * The source image is resized to fit the dimensions of the destination
	 * image and drawn.
	 * 
	 * @param srcImage		The source image.
	 * @param destImage		The destination image.
	 */
	public void resize(BufferedImage srcImage, BufferedImage destImage);
}
