package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.coobird.thumbnailator.ThumbnailParameter;

/**
 * An interface to be implemented by classes which stores the image resulting
 * from a thumbnail generation task.
 * 
 * @author coobird
 *
 */
public interface ImageSink<T>
{
	/**
	 * Writes the resulting image to a destination.
	 * 
	 * @param img				The image to write or store.
	 * @throws IOException		When a problem occurs while writing or storing
	 * 							the image.
	 */
	public void write(BufferedImage img) throws IOException;
	
	/**
	 * Sets the output format of the resulting image.
	 * <p>
	 * For {@link ImageSink}s which stores raw images, the format name specified
	 * by this method may be ignored.
	 * 
	 * @param format			File format with which to store the image.
	 */
	public void setOutputFormatName(String format);
	
	/**
	 * Sets the {@link ThumbnailParameter} from which to retrieve parameters
	 * to use when storing the image.
	 * 
	 * @param param				The {@link ThumbnailParameter} with image
	 * 							writing parameters.
	 */
	public void setThumbnailParameter(ThumbnailParameter param);
	
	public T getSink();
}
