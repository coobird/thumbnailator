package net.coobird.thumbnailator.resizers;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link Resizer} which performs resizing operations by using 
 * progressive bilinear scaling.
 * <p>
 * The resizing technique used in this class is based on the technique
 * discussed in <em>Chapter 4: Images</em> of 
 * <a href="http://filthyrichclients.org">Filthy Rich Clients</a>
 * by Chet Haase and Romain Guy.
 * <p>
 * The actual implemenation of the technique is independent of the code which 
 * is provided in the book.
 * 
 * @author coobird
 *
 */
public class ProgressiveBilinearResizer extends AbstractResizer
{
	/**
	 * Rendering hints to use when resizing an image.
	 */
	private static final Map<RenderingHints.Key, Object> RENDERING_HINTS;
	
	/**
	 * Initializes the rendering hint map to use bilinear interpolation.
	 */
	static {
		RENDERING_HINTS = new HashMap<RenderingHints.Key, Object>();
		RENDERING_HINTS.put(
				RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR
		);
	}
	
	/**
	 * <p>
	 * Resizes an image using the progressive bilinear scaling technique.
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
		
		int currentWidth = srcImage.getWidth();
		int currentHeight = srcImage.getHeight();
		
		final int targetWidth = destImage.getWidth();
		final int targetHeight = destImage.getHeight();
		
		// Temporary image used for in-place resizing of image.
		BufferedImage tempImage = new BufferedImage(
				currentWidth,
				currentHeight,
				destImage.getType()
		);
		
		Graphics2D g = tempImage.createGraphics();
		g.setRenderingHints(RENDERING_HINTS);
		
		/*
		 * Determine the size of the first resize step should be.
		 * 1) Beginning from the target size
		 * 2) Increase each dimension by 2
		 * 3) Until reaching the original size
		 */
		int startWidth = targetWidth;
		int startHeight = targetHeight;
		
		while (startWidth < currentWidth && startHeight < currentHeight)
		{
			startWidth *= 2;
			startHeight *= 2;
		}
		
		currentWidth = startWidth / 2;
		currentHeight = startHeight / 2;

		// Perform first resize step.
		g.drawImage(srcImage, 0, 0, currentWidth, currentHeight, null);
		
		// Perform an in-place progressive bilinear resize.
		while (	(currentWidth >= targetWidth * 2) && (currentHeight >= targetHeight * 2) )
		{
			currentWidth /= 2;
			currentHeight /= 2;
			
			if (currentWidth < targetWidth)
			{
				currentWidth = targetWidth;
			}
			if (currentHeight < targetHeight)
			{
				currentHeight = targetHeight;
			}
			
			g.drawImage(
					tempImage,
					0, 0, currentWidth, currentHeight,
					0, 0, currentWidth * 2, currentHeight * 2,
					null
			);
		}
		
		g.dispose();
		
		// Draw the resized image onto the destination image.
		Graphics destg = destImage.createGraphics();
		destg.drawImage(tempImage, 0, 0, targetWidth, targetHeight, 0, 0, currentWidth, currentHeight, null);
		destg.dispose();
	}
}