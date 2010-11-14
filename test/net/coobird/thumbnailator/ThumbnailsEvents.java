package net.coobird.thumbnailator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import net.coobird.thumbnailator.events.ThumbnailatorEvent;
import net.coobird.thumbnailator.events.ThumbnailatorEventListener;
import net.coobird.thumbnailator.tasks.ThumbnailTask;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class ThumbnailsEvents
{
	@Test
	public void successfulTaskProcessing() throws IOException
	{
		File inputFile = new File("test-resources/Thumbnailator/grid.jpg");
		File outputFile = File.createTempFile("thumbnailator-testing-", ".png");
		outputFile.deleteOnExit();
		
		ThumbnailatorEventListener listener = mock(ThumbnailatorEventListener.class);
		
		Thumbnails.of(inputFile)
			.size(50, 50)
			.notify(listener)
			.toFile(outputFile);
		
		verify(listener, never()).beginBufferedImage(any(BufferedImage.class));
		verify(listener, never()).processedBufferedImage(any(BufferedImage.class), any(BufferedImage.class));
		
		verify(listener, never()).beginTask(any(ThumbnailTask.class));
		verify(listener, never()).processingTask(any(ThumbnailatorEvent.class), any(ThumbnailTask.class));
		verify(listener, never()).processedTask(any(ThumbnailTask.class));
		verify(listener, never()).failedProcessingTask(any(ThumbnailTask.class));
		
		verify(listener).beginFile(inputFile);
		verify(listener, atLeastOnce()).processingFile(any(ThumbnailatorEvent.class), eq(inputFile));
		verify(listener).processedFile(inputFile, outputFile);
		verify(listener, never()).failedProcessingFile(any(File.class));
		
	}

}
