package net.coobird.thumbnailator.geometry;

import java.awt.Dimension;

/**
 * A {@link Size} object which indicates that the size of the enclosed object
 * should be the specified absolute size.
 * 
 * @author coobird
 *
 */
public class AbsoluteSize implements Size
{
	/**
	 * The size of the object.
	 */
	private final Dimension size;
	
	/**
	 * Instantiates an object which indicates size of an object.
	 * 
	 * @param size		Size of the enclosed object.
	 */
	public AbsoluteSize(Dimension size)
	{
		this.size = new Dimension(size);
	}
	
	/**
	 * Instantiates an object which indicates size of an object.
	 * 
	 * @param width		Width of the enclosed object.
	 * @param height	Height of the enclosed object.
	 */
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
