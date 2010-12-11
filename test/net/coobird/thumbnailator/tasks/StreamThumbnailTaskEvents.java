package net.coobird.thumbnailator.tasks;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.builders.ThumbnailParameterBuilder;
import net.coobird.thumbnailator.events.ThumbnailatorEventListener;
import net.coobird.thumbnailator.events.ThumbnailatorEvent.Phase;
import net.coobird.thumbnailator.test.matchers.EventThatFailedWithAnyMessage;
import net.coobird.thumbnailator.test.matchers.EventWithProgressFromZeroToOne;
import net.coobird.thumbnailator.test.matchers.EventThatFailed;

import org.junit.Test;

public class StreamThumbnailTaskEvents
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
		
		InputStream is = new FileInputStream(inputFile);
		OutputStream os = new FileOutputStream(outputFile);
		
		ThumbnailTask task = new StreamThumbnailTask(param, is, os, Arrays.asList(mockListener));
		
		// Exercise the task.
		BufferedImage img = task.read();
		task.write(img);
		
		// Check the interactions.
		verify(mockListener, atLeastOnce())
				.processing(argThat(new EventWithProgressFromZeroToOne(Phase.ACQUIRE)), same(is));
		verify(mockListener, atLeastOnce())
				.processing(argThat(new EventWithProgressFromZeroToOne(Phase.OUTPUT)), same(is));
		
		verifyNoMoreInteractions(mockListener);
	}
	
	@Test
	public void failedWhileRead_CantFindReader() throws IOException
	{
		ThumbnailatorEventListener mockListener = mock(ThumbnailatorEventListener.class);
		
		ThumbnailParameter param = new ThumbnailParameterBuilder()
				.size(50, 50)
				.build();
		
		InputStream is = mock(InputStream.class);
		when(is.available()).thenThrow(new IOException("Can't read input."));
		when(is.read()).thenThrow(new IOException("Can't read input."));
		when(is.read((byte[])anyObject())).thenThrow(new IOException("Can't read input."));
		when(is.read((byte[])anyObject(), anyInt(), anyInt())).thenThrow(new IOException("Can't read input."));
		
		OutputStream os = mock(OutputStream.class);
		doThrow(new IOException("Unexpected write.")).when(os).write(anyByte());
		
		ThumbnailTask task = new StreamThumbnailTask(param, is, os, Arrays.asList(mockListener));
		
		// Exercise the task.
		try
		{
			task.read();
			fail();
		}
		catch (IOException e)
		{
		}
		
		// Check the interactions.
		verify(mockListener)
				.failedProcessing(argThat(new EventThatFailed(Phase.ACQUIRE, UnsupportedFormatException.class, "No suitable ImageReader found for source data.")), same(is));
		
		verifyNoMoreInteractions(mockListener);
	}
	
	@Test
	public void failedWhileRead_CantReadData() throws IOException
	{
		ThumbnailatorEventListener mockListener = mock(ThumbnailatorEventListener.class);
		
		ThumbnailParameter param = new ThumbnailParameterBuilder()
				.size(50, 50)
				.build();
		
		byte[] imageData = new byte[20]; 
		new FileInputStream("test-resources/Thumbnailator/grid.png").read(imageData);
		
		InputStream is = new ByteArrayInputStream(imageData);
		OutputStream os = mock(OutputStream.class);
		doThrow(new IOException("Unexpected write.")).when(os).write(anyByte());
		
		ThumbnailTask task = new StreamThumbnailTask(param, is, os, Arrays.asList(mockListener));
		
		// Exercise the task.
		try
		{
			task.read();
			fail();
		}
		catch (IOException e)
		{
		}
		
		// Check the interactions.
		verify(mockListener)
			.failedProcessing(argThat(new EventThatFailedWithAnyMessage(Phase.ACQUIRE, IOException.class)), same(is));
		
		verifyNoMoreInteractions(mockListener);
	}
	
	@Test
	public void failedWhileWrite_CantFindWriter() throws IOException
	{
		File inputFile = new File("test-resources/Thumbnailator/grid.jpg");
		File outputFile = new File("test-resources/grid.foo");
		outputFile.deleteOnExit();
		
		ThumbnailatorEventListener mockListener = mock(ThumbnailatorEventListener.class);
		
		ThumbnailParameter param = new ThumbnailParameterBuilder()
				.size(50, 50)
				.format("foo")
				.build();
		
		InputStream is = new FileInputStream(inputFile);
		OutputStream os = new ByteArrayOutputStream();
		
		ThumbnailTask task = new StreamThumbnailTask(param, is, os, Arrays.asList(mockListener));
		
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
				.processing(argThat(new EventWithProgressFromZeroToOne(Phase.ACQUIRE)), same(is));
		verify(mockListener)
				.failedProcessing(argThat(new EventThatFailed(Phase.OUTPUT, IOException.class, "No suitable ImageWriter found for foo.")), same(is));
		
		verifyNoMoreInteractions(mockListener);
	}
	@Test
	public void failedWhileWrite_CantWriteData() throws IOException
	{
		File inputFile = new File("test-resources/Thumbnailator/grid.jpg");
		File outputFile = new File("test-resources/!@@#@#&*%.jpg");
		outputFile.deleteOnExit();
		
		ThumbnailatorEventListener mockListener = mock(ThumbnailatorEventListener.class);
		
		ThumbnailParameter param = new ThumbnailParameterBuilder()
				.size(50, 50)
				.build();
		
		InputStream is = new FileInputStream(inputFile);
		OutputStream os = mock(OutputStream.class);
		doThrow(new IOException("Can't write.")).when(os).write(anyInt());
		doThrow(new IOException("Can't write.")).when(os).write((byte[])any());
		doThrow(new IOException("Can't write.")).when(os).write((byte[])any(), anyInt(), anyInt());
		
		ThumbnailTask task = new StreamThumbnailTask(param, is, os, Arrays.asList(mockListener));
		
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
				.processing(argThat(new EventWithProgressFromZeroToOne(Phase.ACQUIRE)), same(is));
		verify(mockListener, atLeastOnce())
				.processing(argThat(new EventWithProgressFromZeroToOne(Phase.OUTPUT)), same(is));
		verify(mockListener)
				.failedProcessing(argThat(new EventThatFailed(Phase.OUTPUT, IOException.class, "Can't write.")), same(is));
		
		verifyNoMoreInteractions(mockListener);
	}
}
