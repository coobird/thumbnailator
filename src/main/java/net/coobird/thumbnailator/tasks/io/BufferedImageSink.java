package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * An {@link ImageSink} which stores the resulting thumbnail to a
 * {@link BufferedImage}.
 * 
 * @author coobird
 *
 */
public class BufferedImageSink extends AbstractImageSink<BufferedImage>
{
	/**
	 * The {@link BufferedImage} which holds the thumbnail.
	 */
	private BufferedImage img;
	
	/**
	 * Indicates whether the thumbnail has been written to this object.
	 */
	private boolean written = false;
	
	public void write(BufferedImage img) throws IOException
	{
		super.write(img);
		
		this.img = img;
		written = true;
	}
	
	/**
	 * Returns the thumbnail.
	 * 
	 * @return							The thumbnail.
	 * @throws IllegalStateException	If a thumbnail has not been stored to
	 * 									this {@link BufferedImageSink} yet.
	 */
	public BufferedImage getSink()
	{
		if (!written)
		{
			throw new IllegalStateException("BufferedImageSink has not been written to yet.");
		}
		return img;
	}

	@Override
	public void setOutputFormatName(String format)
	{
		// do nothing
	}
}
