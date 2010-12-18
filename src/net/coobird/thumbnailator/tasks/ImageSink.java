package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageSink
{
	public void write(BufferedImage img) throws IOException;
}
