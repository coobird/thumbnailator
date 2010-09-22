package net.coobird.thumbnailator.resizers;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Image resizer class using bicubic interpolation for the resizing operation.
 * 
 * @author coobird
 *
 */
public class BicubicResizer extends AbstractResizer
{
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
		
		int width = destImage.getWidth();
		int height = destImage.getHeight();
		
		Graphics2D g = destImage.createGraphics();
		
		g.setRenderingHint(
				RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC
		);
		
		g.drawImage(srcImage, 0, 0, width, height, null);
		g.dispose();
	}
}