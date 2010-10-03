/**
 * 
 */
package net.coobird.thumbnailator.resizers.configurations;

import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;

/**
 * An enum which is used to specify the dithering settings of the
 * resizing operations.
 * 
 * @author coobird
 *
 */
public enum Rendering implements ResizerConfiguration
{
	/**
	 * A hint used to emphasize speed when rendering.
	 */
	SPEED(RenderingHints.VALUE_RENDER_SPEED),
	
	/**
	 * A hint used to emphasize quality when rendering.
	 */
	QUALITY(RenderingHints.VALUE_RENDER_QUALITY),
	
	/**
	 * A hint to use the default rendering settings.
	 */
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