package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedImageSource implements ImageSource
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
		return img;
	}
}
