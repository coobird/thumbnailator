package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class OutputStreamImageSink implements ImageSink
{
	private final OutputStream os;
	private final String formatName;
	
	/**
	 * @param os
	 */
	public OutputStreamImageSink(OutputStream os, String formatName)
	{
		super();
		this.os = os;
		this.formatName = formatName;
	}

	public void write(BufferedImage img) throws IOException
	{
		ImageIO.write(img, formatName, os);
	}
}
