package net.coobird.thumbnailator.geometry;

import java.awt.Dimension;

/**
 * This interface is implemented by classes which calculate the size of an
 * object inside of an enclosing object.
 * 
 * @author coobird
 * @since	0.3.4
 *
 */
public interface Size
{
	/**
	 * Calculates the size of the object.
	 * 
	 * @param width			Width of the object which encloses the object
	 * 						for which the size should be determined.
	 * @param height		Height of the object which encloses the object
	 * 						for which the size should be determined.
	 * @return				Calculated size of the object.
	 * @throws IllegalArgumentException	If the width and/or height is less than
	 * 									or equal to {@code 0}.	
	 */
	public Dimension calculate(int width, int height);
}
