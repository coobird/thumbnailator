package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.coobird.thumbnailator.ThumbnailParameter;

/**
 * An abstract class for {@link ImageSink}s.
 * 
 * @author coobird
 *
 */
public abstract class AbstractImageSink<T> implements ImageSink<T>
{
	/**
	 * The name of the format to output the image as.
	 */
	protected String outputFormat;
	
	/**
	 * The parameters that should be used when storing the image.
	 */
	protected ThumbnailParameter param;
	
	/**
	 * Default constructor.
	 */
	protected AbstractImageSink() {}
	
	public void setOutputFormatName(String format)
	{
		outputFormat = format;
	}
	
	public void setThumbnailParameter(ThumbnailParameter param)
	{
		this.param = param;
	}

	public void write(BufferedImage img) throws IOException
	{
		if (img == null)
		{
			throw new NullPointerException("Cannot write a null image.");
		}
		
		if (ThumbnailParameter.DETERMINE_FORMAT.equals(outputFormat))
		{
			outputFormat = preferredOutputFormatName();
		}
	}
	
	public String preferredOutputFormatName()
	{
		return ThumbnailParameter.ORIGINAL_FORMAT;
	}
}
