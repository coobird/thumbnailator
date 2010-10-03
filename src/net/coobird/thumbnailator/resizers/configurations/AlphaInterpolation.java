/**
 * 
 */
package net.coobird.thumbnailator.resizers.configurations;

import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;


public enum AlphaInterpolation implements ResizerConfiguration
{
	SPEED(RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED),
	QUALITY(RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY),
	DEFAULT(RenderingHints.VALUE_ALPHA_INTERPOLATION_DEFAULT),
	;
	
	/**
	 * 
	 */
	private final Object value;
	
	/**
	 * @param value
	 */
	private AlphaInterpolation(Object value)
	{
		this.value = value;
	}

	public Key getKey()
	{
		return RenderingHints.KEY_ALPHA_INTERPOLATION;
	}

	public Object getValue()
	{
		return value;
	}
}