/**
 * 
 */
package net.coobird.thumbnailator.resizers.configuration;

import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;


public enum Dithering implements ResizerConfiguration
{
	ENABLE(RenderingHints.VALUE_DITHER_ENABLE),
	DISABLE(RenderingHints.VALUE_DITHER_DISABLE),
	DEFAULT(RenderingHints.VALUE_DITHER_DEFAULT),
	;
	
	/**
	 * 
	 */
	private final Object value;
	
	/**
	 * @param value
	 */
	private Dithering(Object value)
	{
		this.value = value;
	}

	public Key getKey()
	{
		return RenderingHints.KEY_DITHERING;
	}

	public Object getValue()
	{
		return value;
	}
}