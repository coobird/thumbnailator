package net.coobird.thumbnailator;

import java.io.File;
import java.io.IOException;

import net.coobird.thumbnailator.events.ThumbnailatorEvent;
import net.coobird.thumbnailator.events.ThumbnailatorEventListener;
import net.coobird.thumbnailator.events.ThumbnailatorEvent.Phase;

import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.mockito.Mockito.*;

public class ThumbnailsEvents
{
	@Test
	public void successfulTaskProcessing() throws IOException
	{
		File inputFile = new File("test-resources/Thumbnailator/grid.jpg");
		File outputFile = File.createTempFile("thumbnailator-testing-", ".png");
		outputFile.deleteOnExit();
		
		ThumbnailatorEventListener mockListener = mock(ThumbnailatorEventListener.class);
		
		Thumbnails.of(inputFile)
			.size(50, 50)
			.notify(mockListener)
			.toFile(outputFile);
		
		verify(mockListener).beginProcessing(inputFile);
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 0.0)), eq(inputFile));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.ACQUIRE)), eq(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 1.0)), eq(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 0.0)), eq(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 1.0)), eq(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 0.0)), eq(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 1.0)), eq(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 0.0)), eq(inputFile));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.OUTPUT)), eq(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 1.0)), eq(inputFile));
		verify(mockListener).finishedProcessing(inputFile, outputFile);
		
		verifyNoMoreInteractions(mockListener);
	}
	
	@Test
	public void successfulTaskProcessingMultipleFiles() throws IOException
	{
		File inputFile1 = new File("test-resources/Thumbnailator/grid.jpg");
		File inputFile2 = new File("test-resources/Thumbnailator/grid.png");

		File outputFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg");
		File outputFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outputFile1.deleteOnExit();
		outputFile2.deleteOnExit();
		
		ThumbnailatorEventListener mockListener = mock(ThumbnailatorEventListener.class);
		
		Thumbnails.of(inputFile1, inputFile2)
			.size(50, 50)
			.notify(mockListener)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		verify(mockListener).beginProcessing(inputFile1);
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 0.0)), eq(inputFile1));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.ACQUIRE)), eq(inputFile1));
		verify(mockListener, atLeastOnce()).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 1.0)), eq(inputFile1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 0.0)), eq(inputFile1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 1.0)), eq(inputFile1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 0.0)), eq(inputFile1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 1.0)), eq(inputFile1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 0.0)), eq(inputFile1));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.OUTPUT)), eq(inputFile1));
		verify(mockListener, atLeastOnce()).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 1.0)), eq(inputFile1));
		verify(mockListener).finishedProcessing(inputFile1, outputFile1);
		
		verify(mockListener).beginProcessing(inputFile2);
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 0.0)), eq(inputFile2));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.ACQUIRE)), eq(inputFile2));
		verify(mockListener, atLeastOnce()).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 1.0)), eq(inputFile2));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 0.0)), eq(inputFile2));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 1.0)), eq(inputFile2));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 0.0)), eq(inputFile2));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 1.0)), eq(inputFile2));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 0.0)), eq(inputFile2));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.OUTPUT)), eq(inputFile2));
		verify(mockListener, atLeastOnce()).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 1.0)), eq(inputFile2));
		verify(mockListener).finishedProcessing(inputFile2, outputFile2);
		
		verifyNoMoreInteractions(mockListener);
	}

}

class EventWithProgressFromZeroToOne extends ArgumentMatcher<ThumbnailatorEvent> {
	
	private final Phase expectedPhase;
	
	/**
	 * @param expectedPhase
	 */
	public EventWithProgressFromZeroToOne(Phase expectedPhase)
	{
		super();
		this.expectedPhase = expectedPhase;
	}

	@Override
	public boolean matches(Object argument)
	{
		if (!(argument instanceof ThumbnailatorEvent))
		{
			return false;
		}
		
		double val = ((ThumbnailatorEvent)argument).getProgress();
		Phase p = ((ThumbnailatorEvent)argument).getPhase();
		
		return (val < 1.0 || val >= 0.0) && (p == expectedPhase);
	}
}
