package net.coobird.thumbnailator.tasks;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.builders.ThumbnailParameterBuilder;
import net.coobird.thumbnailator.events.ThumbnailatorEventListener;
import net.coobird.thumbnailator.events.ThumbnailatorEvent.Phase;
import net.coobird.thumbnailator.test.matchers.EventWithProgressFromZeroToOne;
import net.coobird.thumbnailator.test.matchers.EventThatFailed;

import org.junit.Test;

public class FileThumbnailTaskEvents
{
	@Test
	public void successfulProcessing() throws IOException
	{
		File inputFile = new File("test-resources/Thumbnailator/grid.jpg");
		File outputFile = File.createTempFile("thumbnailator-testing-", ".jpg");
		outputFile.deleteOnExit();
		
		ThumbnailatorEventListener mockListener = mock(ThumbnailatorEventListener.class);
		
		ThumbnailParameter param = new ThumbnailParameterBuilder()
				.size(50, 50)
				.build();
		
		ThumbnailTask task = new FileThumbnailTask(param, inputFile, outputFile, Arrays.asList(mockListener));
		
		// Exercise the task.
		BufferedImage img = task.read();
		task.write(img);
		
		// Check the interactions.
		verify(mockListener, atLeastOnce())
				.processing(argThat(new EventWithProgressFromZeroToOne(Phase.ACQUIRE)), same(inputFile));
		verify(mockListener, atLeastOnce())
				.processing(argThat(new EventWithProgressFromZeroToOne(Phase.OUTPUT)), same(inputFile));
		
		verifyNoMoreInteractions(mockListener);
	}
	
	@Test
	public void failedWhileRead_FileNotFound() throws IOException
	{
		File inputFile = new File("test-resources/Thumbnailator/grida.jpg");
		File outputFile = File.createTempFile("thumbnailator-testing-", ".jpg");
		outputFile.deleteOnExit();
		
		ThumbnailatorEventListener mockListener = mock(ThumbnailatorEventListener.class);
		
		ThumbnailParameter param = new ThumbnailParameterBuilder()
				.size(50, 50)
				.build();
		
		ThumbnailTask task = new FileThumbnailTask(param, inputFile, outputFile, Arrays.asList(mockListener));
		
		// Exercise the task.
		try
		{
			task.read();
			fail();
		}
		catch (FileNotFoundException e)
		{
		}
		
		// Check the interactions.
		String sourcePath = inputFile.getPath();
		verify(mockListener)
				.failedProcessing(argThat(new EventThatFailed(Phase.ACQUIRE, FileNotFoundException.class, "Could not find file: " + sourcePath)), same(inputFile));
		
		verifyNoMoreInteractions(mockListener);
	}
	
	@Test
	public void failedWhileWrite_InvalidFilename() throws IOException
	{
		File inputFile = new File("test-resources/Thumbnailator/grid.jpg");
		File outputFile = new File("test-resources/!@@#@#&*%.jpg");
		outputFile.deleteOnExit();
		
		ThumbnailatorEventListener mockListener = mock(ThumbnailatorEventListener.class);
		
		ThumbnailParameter param = new ThumbnailParameterBuilder()
				.size(50, 50)
				.build();
		
		ThumbnailTask task = new FileThumbnailTask(param, inputFile, outputFile, Arrays.asList(mockListener));
		
		// Exercise the task.
		try
		{
			BufferedImage img = task.read();
			task.write(img);
			fail();
		}
		catch (IOException e)
		{
		}
		
		// Check the interactions.
		verify(mockListener, atLeastOnce())
				.processing(argThat(new EventWithProgressFromZeroToOne(Phase.ACQUIRE)), same(inputFile));
		verify(mockListener)
				.failedProcessing(argThat(new EventThatFailed(Phase.OUTPUT, IOException.class, "Could not open output file.")), same(inputFile));
		
		verifyNoMoreInteractions(mockListener);
	}
}
