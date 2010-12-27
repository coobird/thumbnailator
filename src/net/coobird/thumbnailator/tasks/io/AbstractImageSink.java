package net.coobird.thumbnailator.tasks.io;

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
	
	public void setOutputFormatName(String format)
	{
		outputFormat = format;
	}
	
	public void setThumbnailParameter(ThumbnailParameter param)
	{
		this.param = param;
	}
}
