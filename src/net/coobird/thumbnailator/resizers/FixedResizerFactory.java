package net.coobird.thumbnailator.resizers;

import java.awt.Dimension;

public class FixedResizerFactory implements ResizerFactory
{
	private final Resizer resizer;
	
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
