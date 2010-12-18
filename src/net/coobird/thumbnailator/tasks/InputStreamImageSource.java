package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class InputStreamImageSource implements ImageSource
{
	private final InputStream is;
	
	/**
	 * @param is
	 */
	public InputStreamImageSource(InputStream is)
	{
		super();
		this.is = is;
	}

	public BufferedImage read() throws IOException
	{
		return ImageIO.read(is);
	}
}
