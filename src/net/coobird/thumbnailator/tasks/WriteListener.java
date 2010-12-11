/**
 * 
 */
package net.coobird.thumbnailator.tasks;

import javax.imageio.ImageWriter;
import javax.imageio.event.IIOWriteProgressListener;

import net.coobird.thumbnailator.events.ThumbnailatorEvent;
import net.coobird.thumbnailator.events.ThumbnailatorEventListener;
import net.coobird.thumbnailator.events.ThumbnailatorEvent.Phase;

class WriteListener implements IIOWriteProgressListener
{
	private final ThumbnailatorEventListener notifier;
	private final Object source;
	
	/**
	 * @param notifier
	 * @param source
	 */
	public WriteListener(ThumbnailatorEventListener notifier, Object source)
	{
		super();
		this.notifier = notifier;
		this.source = source;
	}

	public void writeAborted(ImageWriter writer)
	{
		notifier.failedProcessing(
				new ThumbnailatorEvent(Phase.OUTPUT, Double.NaN), source
		);
	}
	
	public void thumbnailStarted(ImageWriter writer, int imageIndex,
			int thumbnailIndex) {}
	
	public void thumbnailProgress(ImageWriter writer, float percentageDone) {}
	
	public void thumbnailComplete(ImageWriter writer) {}
	
	public void imageStarted(ImageWriter writer, int imageIndex)
	{
		notifier.processing(
				new ThumbnailatorEvent(Phase.OUTPUT, 0.0), source
		);
	}
	
	public void imageProgress(ImageWriter writer, float percentageDone)
	{
		double percent = percentageDone / 100d;
		notifier.processing(
				new ThumbnailatorEvent(Phase.OUTPUT, percent), source
		);
	}
	
	public void imageComplete(ImageWriter writer)
	{
		notifier.processing(
				new ThumbnailatorEvent(Phase.OUTPUT, 1.0), source
		);
	}
}