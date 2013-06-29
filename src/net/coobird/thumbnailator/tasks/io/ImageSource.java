package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.coobird.thumbnailator.ThumbnailParameter;

/**
 * An interface to be implemented by classes which read or retrieve images
 * from which a thumbnail should be produced.
 * 
 * @param <T> 		The source class from which the source image is retrieved
 * 					or read.
 * @author coobird
 *
 */
public interface ImageSource<T>
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
	 * @return							The image format name. If there is no
	 * 									image format information, then
	 * 									{@code null} will be returned.
	 * @throws IllegalStateException	If the source image has not been
	 * 									read yet.
	 */
	public String getInputFormatName();
	
	/**
	 * Sets the {@link ThumbnailParameter} from which to retrieve parameters
	 * to use when retrieving the image.
	 * 
	 * @param param				The {@link ThumbnailParameter} with image
	 * 							reading parameters.
	 */
	public void setThumbnailParameter(ThumbnailParameter param);	
	
	/**
	 * Returns the source from which the image is read or retrieved.
	 * 
	 * @return					The source of the image.
	 */
	public T getSource();
}
