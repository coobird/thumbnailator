package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import net.coobird.thumbnailator.tasks.UnsupportedFormatException;

/**
 * An {@link ImageSource} which uses an {@link InputStream} to read the
 * source image.
 * 
 * @author coobird
 *
 */
public class InputStreamImageSource extends AbstractImageSource<InputStream>
{
	/**
	 * The index used to obtain the first image in an image file.
	 */
	private static final int FIRST_IMAGE_INDEX = 0;
	
	/**
	 * The {@link InputStream} from which the source image is to be read. 
	 */
	private final InputStream is;
	
	/**
	 * Instantiates an {@link InputStreamImageSource} with the 
	 * {@link InputStream} which will be used to read the source image.
	 * 
	 * @param is		The {@link InputStream} which is to be used to obtain
	 * 					the source image.
	 * @throws NullPointerException		If the {@link InputStream} is 
	 * 									{@code null}.
	 */
	public InputStreamImageSource(InputStream is)
	{
		super();
		
		if (is == null)
		{
			throw new NullPointerException("InputStream cannot be null.");
		}
		
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
		
		BufferedImage img = reader.read(FIRST_IMAGE_INDEX);
		
		iis.close();
		
		return img;
	}

	public InputStream getSource()
	{
		return is;
	}
}
