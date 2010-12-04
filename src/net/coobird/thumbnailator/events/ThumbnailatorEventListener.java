package net.coobird.thumbnailator.events;


/**
 * An interface for receiving events from Thumbnailator.
 * 
 * @author coobird
 *
 */
public interface ThumbnailatorEventListener
{
	/**
	 * Notifies that processing is beginning.
	 * 
	 * @param source		The source.
	 */
	public void beginProcessing(Object source);
	
	/**
	 * Notifies that processing has failed.
	 * 
	 * @param event			An object indicating the current progress.
	 * @param source		The source.
	 */
	public void failedProcessing(ThumbnailatorEvent event, Object source);
	
	/**
	 * Notifies the progress of the processing the source.
	 * 
	 * @param source		The source.
	 */
	public void processing(ThumbnailatorEvent event, Object source);
	
	/**
	 * Notifies that processing has completed.
	 * 
	 * @param source		The source.
	 * @param destination	The destination.
	 */
	public void finishedProcessing(Object source, Object destination);
}
