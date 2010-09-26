package net.coobird.thumbnailator.resizers;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
	 * Rendering hints to use when resizing an image.
	 */
	protected final Map<RenderingHints.Key, Object> RENDERING_HINTS;
	protected final Map<RenderingHints.Key, Object> UNMODIFIABLE_RENDERING_HINTS;
	
	protected static final RenderingHints.Key KEY_INTERPOLATION = 
		RenderingHints.KEY_INTERPOLATION;


	protected AbstractResizer(
			Object interpolationValue,
			Map<RenderingHints.Key, Object> hints
	)
	{
		RENDERING_HINTS = new HashMap<RenderingHints.Key, Object>();
		RENDERING_HINTS.put(KEY_INTERPOLATION, interpolationValue);
		
		if (
				hints.containsKey(KEY_INTERPOLATION)
				&& !interpolationValue.equals(hints.get(KEY_INTERPOLATION))
		)
		{
			throw new IllegalArgumentException("Cannot change the " +
					"RenderingHints.KEY_INTERPOLATION value.");
		}
		
		RENDERING_HINTS.putAll(hints);
		
		UNMODIFIABLE_RENDERING_HINTS = Collections.unmodifiableMap(RENDERING_HINTS);
	}
	
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
		performChecks(srcImage, destImage);
		
		int width = destImage.getWidth();
		int height = destImage.getHeight();
		
		Graphics2D g = destImage.createGraphics();
		
		g.setRenderingHints(RENDERING_HINTS);
		
		g.drawImage(srcImage, 0, 0, width, height, null);
		g.dispose();
	}
	
	/**
	 * 
	 * @param srcImage
	 * @param destImage
	 */
	protected void performChecks(BufferedImage srcImage, BufferedImage destImage)
	{
		if (srcImage == null || destImage == null)
		{
			throw new NullPointerException(
					"The source and/or destination image is null."
			);
		}
	}
	
	/**
	 * @return
	 */
	public Map<RenderingHints.Key, Object> getRenderingHints()
	{
		return UNMODIFIABLE_RENDERING_HINTS;
	}
}
