package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedImageSink extends AbstractImageSink
{
	private BufferedImage img;
	private boolean written = false;
	
	public void write(BufferedImage img) throws IOException
	{
		this.img = img;
		written = true;
	}
	
	public BufferedImage getImage()
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
