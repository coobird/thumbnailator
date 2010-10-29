package net.coobird.thumbnailator.events;

import java.awt.image.BufferedImage;
import java.io.File;

import net.coobird.thumbnailator.tasks.ThumbnailTask;

/**
 * An interface for receiving events from Thumbnailator.
 * 
 * @author coobird
 *
 */
public interface ThumbnailatorEventListener
{
	/**
	 * Notifies that a {@link ThumbnailTask} is going to be processed.
	 * 
	 * @param task			The {@link ThumbnailTask} which will be processed.
	 */
	public void beginTask(ThumbnailTask task);
	
	/**
	 * Notifies that a {@link File} is going to be processed.
	 * 
	 * @param sourceFile	The {@link File} which will be processed.
	 */
	public void beginFile(File sourceFile);
	
	/**
	 * Notifies that a {@link BufferedImage} is going to be processed.
	 * 
	 * @param sourceFile	The {@link BufferedImage} which will be processed.
	 */
	public void beginBufferedImage(BufferedImage sourceImage);
	
	
	/**
	 * Notifies the progress of the processing of a {@link ThumbnailTask}.
	 * 
	 * @param event			An object indicating the current progress.
	 * @param task			The {@link ThumbnailTask} that is being processed.
	 */
	public void progressTask(ThumbnailatorEvent event, ThumbnailTask task);
	
	/**
	 * Notifies the progress of the processing of a {@link File}.
	 * 
	 * @param event			An object indicating the current progress.
	 * @param sourceFile	The {@link BufferedImage} that is being processed.
	 */
	public void progressFile(ThumbnailatorEvent event, File sourceFile);
	
	/**
	 * Notifies that a {@link ThumbnailTask} is has been processed.
	 * 
	 * @param task			The {@link ThumbnailTask} that has been processed.
	 */
	public void processedTask(ThumbnailTask task);
	
	/**
	 * Notifies that a {@link File} is has been processed.
	 * 
	 * @param sourceFile			The source image file.
	 * @param destinationFile		The file containing the thumbnail image.
	 */
	public void processedFile(File sourceFile, File destinationFile);
	
	/**
	 * Notifies that a {@link BufferedImage} has been processed.
	 * 
	 * @param sourceImage			The source image.
	 * @param destinationImage		The thumbnail image.
	 */
	public void processedBufferedImage(BufferedImage sourceImage, BufferedImage destinationImage);
}
