package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageSource
{
	public BufferedImage read() throws IOException;
	public String getInputFormatName();
}
