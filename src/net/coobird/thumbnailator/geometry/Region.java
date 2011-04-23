package net.coobird.thumbnailator.geometry;

import java.awt.Dimension;

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
	 * Dimension of the region.
	 */
	private final Dimension dimension;
	
	/**
	 * Instantiates a representation of a region from a {@link Position} and
	 * {@link Dimension}. 
	 * 
	 * @param position		Position of the region.
	 * @param dimension		Dimension of the region.
	 */
	public Region(Position position, Dimension dimension)
	{
		super();
		this.position = position;
		this.dimension = dimension;
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
	 * Returns the dimensions of the region.
	 * 
	 * @return 				Dimension of the region.
	 */
	public Dimension getDimension()
	{
		return dimension;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Region [dimension=" + dimension + ", position=" + position + "]";
	}
}
