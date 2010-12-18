package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageSource
{
	public BufferedImage read() throws IOException;
}
