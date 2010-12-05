package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.events.ThumbnailatorEvent;
import net.coobird.thumbnailator.events.ThumbnailatorEventListener;
import net.coobird.thumbnailator.events.ThumbnailatorEventNotifier;

/**
 * This class is used by {@link ThumbnailTask} implementations which is used
 * when creating thumbnails from external sources and destinations.
 * <p>
 * If the image handled by a {@link ThumbnailTask} contains multiple images,
 * only the first image will be read by the {@link #read()} method. Any
 * subsequent images will be ignored. 
 * <p>
 * <h3>Notes on events</h3>
 * {@link ThumbnailTask}s will notify registered 
 * {@link ThumbnailatorEventListener}s of 
 * read ({@link ThumbnailatorEvent.Phase#ACQUIRE}) and 
 * write ({@link ThumbnailatorEvent.Phase#OUTPUT}) events.
 * <p>
 * The {@code progress} value returned as part of the {@link ThumbnailatorEvent}
 * will be in the range {@code 0.0} to {@code 1.0}, however, whether the range
 * is inclusive or exclusive is dependent on the implementation. Furthermore,
 * the implementation need not define whether the range is guaranteed to be
 * inclusive or not under all circumstances. Under error conditions, the
 * {@code progress} value will be {@link Double#NaN}.   
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
	
	protected final ThumbnailatorEventNotifier notifier;
	
	/**
	 * String indicating the image format of the input image.
	 * <p>
	 * To be used for situtions where the output image format should be the
	 * same as the input image format. 
	 */
	protected String inputFormatName;
	
	/**
	 * Constant used to obtain the first image when reading an image file. 
	 */
	protected static final int FIRST_IMAGE_INDEX = 0;
	
	/**
	 * Instantiates a {@link ThumbnailTask} with the parameters to use when
	 * creating thumbnails.
	 * 
	 * @param param			The parameters to use when creating thumbnails.
	 */
	protected ThumbnailTask(ThumbnailParameter param)
	{
		this.param = param;
		this.notifier = new ThumbnailatorEventNotifier();
	}
	
	/**
	 * Instantiates a {@link ThumbnailTask} with the parameters to use when
	 * creating thumbnails, and also a {@link List} of 
	 * {@link ThumbnailatorEventListener}s which should be notified of events
	 * which occur during processing.
	 * 
	 * @param param			The parameters to use when creating thumbnails.
	 */
	protected ThumbnailTask(ThumbnailParameter param, List<ThumbnailatorEventListener> listeners)
	{
		this.param = param;
		this.notifier = new ThumbnailatorEventNotifier(listeners);
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
	 * @throws UnsupportedFormatException	When an image file which is to be
	 * 										read or written is unsupported. 
	 * @throws IOException	Thrown when an I/O problem occurs when writing the
	 * 						image.
	 */
	public abstract void write(BufferedImage img) throws IOException;
	
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
	
	/**
	 * Returns an unmodifiable list of {@link ThumbnailatorEventListener}s
	 * associated with this {@link ThumbnailTask}.
	 * 
	 * @return	A {@link List} of {@link ThumbnailatorEventListener}s associated
	 * 			with this {@link ThumbnailTask}.
	 */
	public List<ThumbnailatorEventListener> getListeners()
	{
		return notifier.getListeners();
	}
	
	/**
	 * Returns the source from which the image to resize is obtained from.
	 * 
	 * @return		The source.
	 */
	public abstract Object getSource();
	
	/**
	 * Returns the destination to which the thumbnail is output to.
	 * 
	 * @return		The destination.
	 */
	public abstract Object getDestination();
}
