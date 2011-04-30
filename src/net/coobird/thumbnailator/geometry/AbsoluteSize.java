package net.coobird.thumbnailator.geometry;

import java.awt.Dimension;

public class AbsoluteSize implements Size
{
	private final Dimension size;
	
	public AbsoluteSize(Dimension size)
	{
		this.size = new Dimension(size);
	}
	
	public AbsoluteSize(int width, int height)
	{
		this.size = new Dimension(width, height);
	}

	public Dimension calculate(int width, int height)
	{
		return new Dimension(size);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "AbsoluteSize [width=" + size.width + ", height=" + size.height + "]";
	}
}
