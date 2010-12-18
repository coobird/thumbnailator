package net.coobird.thumbnailator.tasks;

import net.coobird.thumbnailator.ThumbnailParameter;

public abstract class AbstractImageSink implements ImageSink
{
	protected String outputFormat;
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
