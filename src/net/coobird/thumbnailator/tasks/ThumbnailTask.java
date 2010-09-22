package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.coobird.thumbnailator.ThumbnailParameter;

/**
 * This class is used by {@link ThumbnailTask} implementations which is used
 * when creating thumbnails from external sources and destinations.
 * 
 * @author coobird
 *
 */
public abstract class ThumbnailTask
{
	/**
	 * The parameters to use when creating a thumbnail.
	 */
	protected final ThumbnailParameter param;
	
	/**
	 * String indicating the image format of the input image.
	 * <p>
	 * To be used for situtions where the output image format should be the
	 * same as the input image format. 
	 */
	protected String inputFormatName;
	
	/**
	 * Instantiates a {@link ThumbnailTask} with the parameters to use when
	 * creating thumbnails.
	 * 
	 * @param param			The parameters to use when creating thumbnails.
	 */
	protected ThumbnailTask(ThumbnailParameter param)
	{
		this.param = param;
	}
	
	/**
	 * Reads a source image.
	 * 
	 * @return					The image which was obtained from the source.
	 * @throws IOException		Thrown when an I/O problem occurs when reading
	 * 							from the image source.
	 */
	/*
	 * Future changes note: The public interface of this method may have to be
	 * changed to support reading images tile-by-tile. This change may be
	 * required in order to support large images.
	 */
	public abstract BufferedImage read() throws IOException;
	
	/**
	 * Writes the thumbnail to the destination.
	 * 
	 * @param img			The image to write.
	 * @return				Returns {@code false} if the thumbnail could not be
	 * 						written to the destination.
	 * @throws IOException	Thrown when an I/O problem occurs when writing the
	 * 						image.
	 */
	public abstract boolean write(BufferedImage img) throws IOException;
	
	/**
	 * Returns the {@link ThumbnailParameter} for this {@link ThumbnailTask},
	 * used when performing a thumbnail generation operation.
	 * 
	 * @return				The parameters to use when generating thumbnails.
	 */
	public ThumbnailParameter getParam()
	{
		return param;
	}
}
