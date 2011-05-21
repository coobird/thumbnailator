package net.coobird.thumbnailator.resizers;

import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Map;

/**
 * A {@link Resizer} which does not actually resize the image.
 * <p>
 * The source image will be drawn at the origin of the destination image.
 * 
 * @author coobird
 *
 */
public class NullResizer extends AbstractResizer
{
	public NullResizer()
	{
		this(
				RenderingHints.VALUE_INTERPOLATION_BILINEAR,
				Collections.<Key, Object>emptyMap()
		);
	}
	
	private NullResizer(Object interpolationValue, Map<Key, Object> hints)
	{
		super(interpolationValue, hints);
	}

	public void resize(BufferedImage srcImage, BufferedImage destImage)
	{
		super.performChecks(srcImage, destImage);
		
		Graphics g = destImage.getGraphics();
		g.drawImage(srcImage, 0, 0, null);
		g.dispose();
	}
}
