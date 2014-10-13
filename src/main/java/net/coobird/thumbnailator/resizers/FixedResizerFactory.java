package net.coobird.thumbnailator.resizers;

import java.awt.Dimension;

/**
 * A {@link ResizerFactory} that returns a specific {@link Resizer}
 * unconditionally.
 * 
 * @author coobird
 * @since	0.4.0
 */
public class FixedResizerFactory implements ResizerFactory
{
	/**
	 * The resizer which is to be returned unconditionally by this class.
	 */
	private final Resizer resizer;
	
	/**
	 * Creates an instance of the {@link FixedResizerFactory} which returns
	 * the speicifed {@link Resizer} under all circumstances.
	 * 
	 * @param resizer		The {@link Resizer} instance that is to be returned
	 * 						under all circumstances.
	 */
	public FixedResizerFactory(Resizer resizer)
	{
		this.resizer = resizer;
	}

	public Resizer getResizer()
	{
		return resizer;
	}

	public Resizer getResizer(Dimension originalSize, Dimension thumbnailSize)
	{
		return resizer;
	}
}
