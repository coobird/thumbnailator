package net.coobird.thumbnailator.resizers;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * A {@link Resizer} which does not actually resize the image.
 * <p>
 * The source image will be drawn at the origin of the destination image.
 * 
 * @author coobird
 *
 */
public class NullResizer implements Resizer
{
	public void resize(BufferedImage srcImage, BufferedImage destImage)
	{
		Graphics g = destImage.getGraphics();
		g.drawImage(srcImage, 0, 0, null);
		g.dispose();
	}
}
