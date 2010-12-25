package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedImageSource extends AbstractImageSource
{
	public static final String INPUT_FORMAT_NAME = "BufferedImage";
	private final BufferedImage img;
	
	/**
	 * @param img
	 */
	public BufferedImageSource(BufferedImage img)
	{
		super();
		
		if (img == null)
		{
			throw new NullPointerException("Image cannot be null.");
		}
		
		this.img = img;
	}

	public BufferedImage read() throws IOException
	{
		inputFormatName = INPUT_FORMAT_NAME;
		return img;
	}
}
