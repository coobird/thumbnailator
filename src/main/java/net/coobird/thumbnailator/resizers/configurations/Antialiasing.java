/**
 * 
 */
package net.coobird.thumbnailator.resizers.configurations;

import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;

/**
 * An enum which is used to specify the antialiasing settings of the
 * resizing operations.
 * 
 * @author coobird
 *
 */
public enum Antialiasing implements ResizerConfiguration
{
	/**
	 * A hint to enable antialiasing.
	 */
	ON(RenderingHints.VALUE_ANTIALIAS_ON),
	
	/**
	 * A hint to disable antialiasing.
	 */
	OFF(RenderingHints.VALUE_ANTIALIAS_OFF),
	
	/**
	 * A hint to use the default antialiasing settings.
	 */
	DEFAULT(RenderingHints.VALUE_ANTIALIAS_DEFAULT),
	;
	
	/**
	 * The field used to hold the rendering hint.
	 */
	private final Object value;
	
	/**
	 * Instantiates this enum.
	 * 
	 * @param value		The rendering hint value.
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