package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.coobird.thumbnailator.ThumbnailParameter;

public class SourceSinkThumbnailTask extends ThumbnailTask
{
	private final ImageSource source;
	private final ImageSink destination;

	protected SourceSinkThumbnailTask(ThumbnailParameter param, ImageSource source, ImageSink destination)
	{
		super(param);
		this.source = source;
		this.destination = destination;
	}

	@Override
	public BufferedImage read() throws IOException
	{
		return source.read();
	}

	@Override
	public void write(BufferedImage img) throws IOException
	{
		destination.write(img);
	}
}
