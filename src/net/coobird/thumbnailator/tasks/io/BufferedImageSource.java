package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * An {@link ImageSource} which uses a {@link BufferedImage} as the source
 * image.
 * 
 * @author coobird
 *
 */
public class BufferedImageSource extends AbstractImageSource<BufferedImage>
{
	/**
	 * The image that should be used as the source for making a thumbnail. 
	 */
	private final BufferedImage img;
	
	/**
	 * Instantiates a {@link BufferedImageSource} object with the 
	 * {@link BufferedImage} that should be used as the source image for making
	 * thumbnails.
	 * 
	 * @param img		The source image.
	 * @throws NullPointerException		If the image is null.
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
		inputFormatName = null;
		return finishedReading(img);
	}

	public BufferedImage getSource()
	{
		return img;
	}
}
