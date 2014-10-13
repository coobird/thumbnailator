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
public enum Dithering implements ResizerConfiguration
{
	/**
	 * A hint used to enable dithering.
	 */
	ENABLE(RenderingHints.VALUE_DITHER_ENABLE),
	
	/**
	 * A hint used to disable dithering.
	 */
	DISABLE(RenderingHints.VALUE_DITHER_DISABLE),
	
	/**
	 * A hint to use the default dithering settings.
	 */
	DEFAULT(RenderingHints.VALUE_DITHER_DEFAULT),
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
