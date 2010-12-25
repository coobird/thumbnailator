package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * An interface to be implemented by classes which read or retrieve images
 * from which a thumbnail should be produced.
 * 
 * @author coobird
 *
 */
public interface ImageSource
{
	/**
	 * Retrieves the image from the source.
	 * 
	 * @return					The image.
	 * @throws IOException		When a problem occurs while reading or obtaining
	 * 							the image.
	 */
	public BufferedImage read() throws IOException;
	
	/**
	 * Returns the name of the image format.
	 * 
	 * @return					The image format name.
	 */
	public String getInputFormatName();
}
