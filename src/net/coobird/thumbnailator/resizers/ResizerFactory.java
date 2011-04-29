package net.coobird.thumbnailator.resizers;

import java.awt.Dimension;

public interface ResizerFactory
{
	/**
	 * Returns the default {@link Resizer}.
	 * 
	 * @return				The default {@code Resizer}.
	 */
	public Resizer getResizer();
	
	/**
	 * Returns a suitable {@link Resizer}, given the {@link Dimension}s of the
	 * original image and the thumbnail image.
	 *  
	 * @param originalSize			The size of the original image.
	 * @param thumbnailSize			The size of the thumbnail.
	 * @return						The suitable {@code Resizer} to perform the
	 * 								resizing operation for the given condition.
	 */
	public Resizer getResizer(Dimension originalSize, Dimension thumbnailSize);
}
