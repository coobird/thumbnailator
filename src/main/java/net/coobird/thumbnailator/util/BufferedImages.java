package net.coobird.thumbnailator.util;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * This class provides convenience methods for using {@link BufferedImage}s.
 * 
 * @author coobird
 *
 */
public final class BufferedImages
{
	/**
	 * This class is not intended to be instantiated.
	 */
	private BufferedImages() {}
	
	/**
	 * Returns a {@link BufferedImage} which is a graphical copy of the
	 * specified image.
	 * 
	 * @param img		The image to copy.
	 * @return			A copy of the specified image.
	 */
	public static BufferedImage copy(BufferedImage img)
	{
		return copy(img, img.getType());
	}
	
	/**
	 * Returns a {@link BufferedImage} with the specified image type, where the
	 * graphical content is a copy of the specified image.
	 * 
	 * @param img		The image to copy.
	 * @param imageType	The image type for the image to return.
	 * @return			A copy of the specified image.
	 */
	public static BufferedImage copy(BufferedImage img, int imageType)
	{
		int width = img.getWidth();
		int height = img.getHeight();
		
		BufferedImage newImage = new BufferedImage(width, height, imageType);
		Graphics g = newImage.createGraphics();
		
		g.drawImage(img, 0, 0, null);
		
		g.dispose();
		
		return newImage;
	}
}