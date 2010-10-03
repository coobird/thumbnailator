/**
 * 
 */
package net.coobird.thumbnailator.resizers.configuration;

import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;


public enum Antialiasing implements ResizerConfiguration
{
	ON(RenderingHints.VALUE_ANTIALIAS_ON),
	OFF(RenderingHints.VALUE_ANTIALIAS_OFF),
	DEFAULT(RenderingHints.VALUE_ANTIALIAS_DEFAULT),
	;
	
	/**
	 * 
	 */
	private final Object value;
	
	/**
	 * @param value
	 */
	private Antialiasing(Object value)
	{
		this.value = value;
	}

	public Key getKey()
	{
		return RenderingHints.KEY_ANTIALIASING;
	}

	public Object getValue()
	{
		return value;
	}
}