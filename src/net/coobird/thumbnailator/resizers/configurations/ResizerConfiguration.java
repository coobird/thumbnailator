package net.coobird.thumbnailator.resizers.configurations;

import java.awt.RenderingHints;

import net.coobird.thumbnailator.resizers.Resizer;

/**
 * An interface which are implemented by classes and enums which provide
 * configuration information for {@link Resizer}s.
 * 
 * @author coobird
 *
 */
public interface ResizerConfiguration
{
	/**
	 * Returns a rendering hint key.
	 * 
	 * @return		Rendering hint key.
	 */
	public RenderingHints.Key getKey();
	
	/**
	 * Returns a rendering hint value.
	 * 
	 * @return		Rendering hint value.
	 */
	public Object getValue();
}
