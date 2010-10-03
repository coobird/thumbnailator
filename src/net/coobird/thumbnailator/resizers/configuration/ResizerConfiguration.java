/**
 * 
 */
package net.coobird.thumbnailator.resizers.configuration;

import java.awt.RenderingHints;

public interface ResizerConfiguration
{
	public RenderingHints.Key getKey();
	public Object getValue();
}