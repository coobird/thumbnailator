package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.coobird.thumbnailator.ThumbnailParameter;

public interface ImageSink
{
	public void write(BufferedImage img) throws IOException;
	public void setOutputFormatName(String format);
	public void setThumbnailParameter(ThumbnailParameter param);
}
