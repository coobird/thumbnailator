package net.coobird.thumbnailator.geometry;

import java.awt.Dimension;

/**
 * This interface is implemented by classes which calculate the size of an
 * object inside of an enclosing object.
 * 
 * @author coobird
 *
 */
public interface Size
{
	/**
	 * Calculates the size of the object. 
	 * 
	 * @param width			The width of the object which encloses the object 
	 * 						for which the size should be determined.
	 * @param height		The height of the object which encloses the object 
	 * 						for which the size should be determined.
	 * @return				The calculated size of the object.
	 */
	public Dimension calculate(int width, int height);
}
