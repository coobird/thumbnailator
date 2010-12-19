/**
 * 
 */
package net.coobird.thumbnailator.tasks;

import javax.imageio.ImageReader;
import javax.imageio.event.IIOReadProgressListener;

import net.coobird.thumbnailator.events.ThumbnailatorEvent;
import net.coobird.thumbnailator.events.ThumbnailatorEventListener;
import net.coobird.thumbnailator.events.ThumbnailatorEvent.Phase;

class ReadListener implements IIOReadProgressListener
{
	private final ThumbnailatorEventListener notifier;
	private final Object source;
	
	/**
	 * @param notifier
	 * @param source
	 */
	public ReadListener(ThumbnailatorEventListener notifier, Object source)
	{
		super();
		this.notifier = notifier;
		this.source = source;
	}

	public void thumbnailStarted(ImageReader reader, int imageIndex,
			int thumbnailIndex) {}
	
	public void thumbnailProgress(ImageReader reader, float percentageDone) {}
	
	public void thumbnailComplete(ImageReader reader) {}
	
	public void sequenceStarted(ImageReader reader, int minIndex) {}
	
	public void sequenceComplete(ImageReader reader) {}
	
	public void readAborted(ImageReader reader)
	{
		notifier.failedProcessing(
				new ThumbnailatorEvent(Phase.ACQUIRE, Double.NaN), source
		);
	}
	
	public void imageStarted(ImageReader reader, int imageIndex)
	{
		notifier.processing(
				new ThumbnailatorEvent(Phase.ACQUIRE, 0.0), source
		);
	}
	
	public void imageProgress(ImageReader reader, float percentageDone)
	{
		double percent = percentageDone / 100d;
		notifier.processing(
				new ThumbnailatorEvent(Phase.ACQUIRE, percent), source
		);
	}
	
	public void imageComplete(ImageReader reader)
	{
		notifier.processing(
				new ThumbnailatorEvent(Phase.ACQUIRE, 1.0), source
		);
	}
}