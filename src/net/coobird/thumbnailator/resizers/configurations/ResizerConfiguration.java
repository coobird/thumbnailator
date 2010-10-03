package net.coobird.thumbnailator.resizers.configurations;

import java.awt.RenderingHints;

/**
 * @author coobird
 *
 */
public interface ResizerConfiguration
{
	public RenderingHints.Key getKey();
	public Object getValue();
}