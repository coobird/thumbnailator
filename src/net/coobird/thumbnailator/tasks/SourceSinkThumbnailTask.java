package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.tasks.io.ImageSink;
import net.coobird.thumbnailator.tasks.io.ImageSource;

public class SourceSinkThumbnailTask<S, D> extends ThumbnailTask<S, D>
{
	private final ImageSource<S> source;
	private final ImageSink<D> destination;

	public SourceSinkThumbnailTask(ThumbnailParameter param, ImageSource<S> source, ImageSink<D> destination)
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

	@Override
	public S getSource()
	{
		return source.getSource();
	}
	
	@Override
	public D getDestination()
	{
		return destination.getSink();
	}
}
