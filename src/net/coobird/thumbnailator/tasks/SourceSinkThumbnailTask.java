package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.tasks.io.ImageSink;
import net.coobird.thumbnailator.tasks.io.ImageSource;

public class SourceSinkThumbnailTask extends ThumbnailTask
{
	private final ImageSource source;
	private final ImageSink destination;

	public SourceSinkThumbnailTask(ThumbnailParameter param, ImageSource source, ImageSink destination)
	{
		super(param);
		this.source = source;
		
		destination.setThumbnailParameter(param);
		this.destination = destination;
	}

	@Override
	public BufferedImage read() throws IOException
	{
		BufferedImage img = source.read();
		inputFormatName = source.getInputFormatName();
		
		return img;
	}

	@Override
	public void write(BufferedImage img) throws IOException
	{
		String formatName;
		if (param.getOutputFormat() == ThumbnailParameter.ORIGINAL_FORMAT)
		{
			formatName = inputFormatName;
		}
		else
		{
			formatName = param.getOutputFormat();
		}
		
		destination.setOutputFormatName(formatName);
		destination.write(img);
	}
}
