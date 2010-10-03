/**
 * 
 */
package net.coobird.thumbnailator.resizers.configuration;

import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;


public enum Rendering implements ResizerConfiguration
{
	SPEED(RenderingHints.VALUE_RENDER_SPEED),
	QUALITY(RenderingHints.VALUE_RENDER_QUALITY),
	DEFAULT(RenderingHints.VALUE_RENDER_DEFAULT),
	;
	
	/**
	 * 
	 */
	private final Object value;
	
	/**
	 * @param value
	 */
	private Rendering(Object value)
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