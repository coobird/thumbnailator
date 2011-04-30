package net.coobird.thumbnailator.geometry;

import java.awt.Dimension;

public class RelativeSize implements Size
{
	private final double scalingFactor;
	
	public RelativeSize(double scalingFactor)
	{
		super();
		this.scalingFactor = scalingFactor;
	}

	public Dimension calculate(int width, int height)
	{
		int newWidth = (int)Math.round(width * scalingFactor);
		int newHeight = (int)Math.round(height * scalingFactor);
		return new Dimension(newWidth, newHeight);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "RelativeSize [scalingFactor=" + scalingFactor + "]";
	}
}
