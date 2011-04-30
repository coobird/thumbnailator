package net.coobird.thumbnailator.geometry;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * A representation of a region, using a {@link Position} object and a
 * {@link Dimension} object.
 * 
 * @author coobird
 *
 */
public final class Region
{
	/**
	 * Position of the region.
	 */
	private final Position position;
	
	/**
	 * Size of the region.
	 */
	private final Size size;
	
	/**
	 * Instantiates a representation of a region from a {@link Position} and
	 * {@link Size}. 
	 * 
	 * @param position		Position of the region.
	 * @param size			Size of the region.
	 */
	public Region(Position position, Size size)
	{
		super();
		this.position = position;
		this.size = size;
	}
	
	/**
	 * Returns the position of the region.
	 * 
	 * @return 				Position of the region.
	 */
	public Position getPosition()
	{
		return position;
	}
	
	/**
	 * Returns the size of the region.
	 * 
	 * @return 				Size of the region.
	 */
	public Size getSize()
	{
		return size;
	}
	
	public Rectangle calculate(int enclosingWidth, int enclosingHeight)
	{
		Dimension d = size.calculate(enclosingWidth, enclosingHeight);
		int width = d.width;
		int height = d.height;

		Point p = position.calculate(
				enclosingWidth, enclosingHeight, width, height, 0, 0, 0, 0
		);
		
		return new Rectangle(p, d);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Region [position=" + position + ", size=" + size + "]";
	}
}
