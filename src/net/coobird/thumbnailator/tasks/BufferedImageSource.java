package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedImageSource extends AbstractImageSource
{
	private final BufferedImage img;
	
	/**
	 * @param img
	 */
	public BufferedImageSource(BufferedImage img)
	{
		super();
		this.img = img;
	}

	public BufferedImage read() throws IOException
	{
		inputFormatName = "BufferedImage";
		return img;
	}
}
