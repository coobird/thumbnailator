package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class InputStreamImageSource extends AbstractImageSource
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
		ImageInputStream iis = ImageIO.createImageInputStream(is);
		
		if (iis == null)
		{
			throw new IOException("Could not open InputStream.");
		}
		
		Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
		if (!readers.hasNext())
		{
			throw new UnsupportedFormatException(
					UnsupportedFormatException.UNKNOWN,
					"No suitable ImageReader found for source data."
			);
		}
		
		ImageReader reader = readers.next();
		reader.setInput(iis);
		inputFormatName = reader.getFormatName();
		
		BufferedImage img = reader.read(ThumbnailTask.FIRST_IMAGE_INDEX);
		
		iis.close();
		
		return img;
	}
}
