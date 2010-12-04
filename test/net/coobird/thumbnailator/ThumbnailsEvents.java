package net.coobird.thumbnailator;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.events.ThumbnailatorEvent;
import net.coobird.thumbnailator.events.ThumbnailatorEventListener;
import net.coobird.thumbnailator.events.ThumbnailatorEvent.Phase;
import net.coobird.thumbnailator.filters.ImageFilter;

import org.junit.Test;
import org.mockito.ArgumentMatcher;

import static org.junit.Assert.*;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;

public class ThumbnailsEvents
{
	@Test
	public void successfulFileProcessing_Single() throws IOException
	{
		// This test checks for parts that internally uses ThumbnailTasks
		
		File inputFile = new File("test-resources/Thumbnailator/grid.jpg");
		File outputFile = File.createTempFile("thumbnailator-testing-", ".png");
		outputFile.deleteOnExit();
		
		ThumbnailatorEventListener mockListener = mock(ThumbnailatorEventListener.class);
		
		Thumbnails.of(inputFile)
			.size(50, 50)
			.notify(mockListener)
			.toFile(outputFile);
		
		verify(mockListener).beginProcessing(inputFile);
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 0.0)), same(inputFile));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.ACQUIRE)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 1.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 0.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 1.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 0.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 1.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 0.0)), same(inputFile));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.OUTPUT)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 1.0)), same(inputFile));
		verify(mockListener).finishedProcessing(inputFile, outputFile);
		
		verifyNoMoreInteractions(mockListener);
	}
	
	@Test
	public void successfulFileProcessing_Multiple() throws IOException
	{
		// This test checks for parts that internally uses ThumbnailTasks
		
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
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 0.0)), same(inputFile1));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.ACQUIRE)), same(inputFile1));
		verify(mockListener, atLeastOnce()).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 1.0)), same(inputFile1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 0.0)), same(inputFile1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 1.0)), same(inputFile1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 0.0)), same(inputFile1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 1.0)), same(inputFile1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 0.0)), same(inputFile1));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.OUTPUT)), same(inputFile1));
		verify(mockListener, atLeastOnce()).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 1.0)), same(inputFile1));
		verify(mockListener).finishedProcessing(inputFile1, outputFile1);
		
		verify(mockListener).beginProcessing(inputFile2);
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 0.0)), same(inputFile2));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.ACQUIRE)), same(inputFile2));
		verify(mockListener, atLeastOnce()).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 1.0)), same(inputFile2));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 0.0)), same(inputFile2));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 1.0)), same(inputFile2));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 0.0)), same(inputFile2));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 1.0)), same(inputFile2));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 0.0)), same(inputFile2));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.OUTPUT)), same(inputFile2));
		verify(mockListener, atLeastOnce()).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 1.0)), same(inputFile2));
		verify(mockListener).finishedProcessing(inputFile2, outputFile2);
		
		verifyNoMoreInteractions(mockListener);
	}
	
	@Test
	public void successfulFilenameProcessing_Single() throws IOException
	{
		// This test checks for parts that internally uses ThumbnailTasks
		
		String inputFile = "test-resources/Thumbnailator/grid.jpg";
		File outputFile = File.createTempFile("thumbnailator-testing-", ".png");
		outputFile.deleteOnExit();
		
		ThumbnailatorEventListener mockListener = mock(ThumbnailatorEventListener.class);
		
		Thumbnails.of(inputFile)
			.size(50, 50)
			.notify(mockListener)
			.toFile(outputFile);
		
		File inputFileFile = new File(inputFile);
		
		verify(mockListener).beginProcessing(inputFileFile);
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 0.0)), eq(inputFileFile));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.ACQUIRE)), eq(inputFileFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 1.0)), eq(inputFileFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 0.0)), eq(inputFileFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 1.0)), eq(inputFileFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 0.0)), eq(inputFileFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 1.0)), eq(inputFileFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 0.0)), eq(inputFileFile));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.OUTPUT)), eq(inputFileFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 1.0)), eq(inputFileFile));
		verify(mockListener).finishedProcessing(inputFileFile, outputFile);
		
		verifyNoMoreInteractions(mockListener);
	}
	
	@Test
	public void successfulFilenameProcessing_Multiple() throws IOException
	{
		// This test checks for parts that internally uses ThumbnailTasks
		
		String inputFile1 = "test-resources/Thumbnailator/grid.jpg";
		String inputFile2 = "test-resources/Thumbnailator/grid.png";
		
		File outputFile1 = new File("test-resources/Thumbnailator/thumbnail.grid.jpg");
		File outputFile2 = new File("test-resources/Thumbnailator/thumbnail.grid.png");
		outputFile1.deleteOnExit();
		outputFile2.deleteOnExit();
		
		ThumbnailatorEventListener mockListener = mock(ThumbnailatorEventListener.class);
		
		Thumbnails.of(inputFile1, inputFile2)
			.size(50, 50)
			.notify(mockListener)
			.toFiles(Rename.PREFIX_DOT_THUMBNAIL);
		
		File inputFileFile1 = new File(inputFile1);
		File inputFileFile2 = new File(inputFile2);
		
		verify(mockListener).beginProcessing(inputFileFile1);
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 0.0)), eq(inputFileFile1));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.ACQUIRE)), eq(inputFileFile1));
		verify(mockListener, atLeastOnce()).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 1.0)), eq(inputFileFile1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 0.0)), eq(inputFileFile1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 1.0)), eq(inputFileFile1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 0.0)), eq(inputFileFile1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 1.0)), eq(inputFileFile1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 0.0)), eq(inputFileFile1));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.OUTPUT)), eq(inputFileFile1));
		verify(mockListener, atLeastOnce()).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 1.0)), eq(inputFileFile1));
		verify(mockListener).finishedProcessing(inputFileFile1, outputFile1);
		
		verify(mockListener).beginProcessing(inputFileFile2);
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 0.0)), eq(inputFileFile2));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.ACQUIRE)), eq(inputFileFile2));
		verify(mockListener, atLeastOnce()).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 1.0)), eq(inputFileFile2));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 0.0)), eq(inputFileFile2));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 1.0)), eq(inputFileFile2));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 0.0)), eq(inputFileFile2));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 1.0)), eq(inputFileFile2));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 0.0)), eq(inputFileFile2));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.OUTPUT)), eq(inputFileFile2));
		verify(mockListener, atLeastOnce()).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 1.0)), eq(inputFileFile2));
		verify(mockListener).finishedProcessing(inputFileFile2, outputFile2);
		
		verifyNoMoreInteractions(mockListener);
	}
	
	@Test
	public void successfulBufferedImageProcessing_Single() throws IOException
	{
		BufferedImage sourceImage = 
			ImageIO.read(new File("test-resources/Thumbnailator/grid.jpg"));
		
		ThumbnailatorEventListener mockListener = mock(ThumbnailatorEventListener.class);
		
		BufferedImage destinationImage = Thumbnails.of(sourceImage)
			.size(50, 50)
			.notify(mockListener)
			.asBufferedImage();
		
		verify(mockListener).beginProcessing(sourceImage);
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 0.0)), same(sourceImage));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 1.0)), same(sourceImage));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 0.0)), same(sourceImage));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 1.0)), same(sourceImage));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 0.0)), same(sourceImage));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 1.0)), same(sourceImage));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 0.0)), same(sourceImage));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 1.0)), same(sourceImage));
		verify(mockListener).finishedProcessing(sourceImage, destinationImage);
		
		verifyNoMoreInteractions(mockListener);
	}
	
	@Test
	public void successfulBufferedImageProcessing_Multiple() throws IOException
	{
		BufferedImage sourceImage0 = 
			ImageIO.read(new File("test-resources/Thumbnailator/grid.jpg"));
		BufferedImage sourceImage1 = 
			ImageIO.read(new File("test-resources/Thumbnailator/grid.jpg"));
		
		ThumbnailatorEventListener mockListener = mock(ThumbnailatorEventListener.class);
		
		List<BufferedImage> destinationImages = Thumbnails.of(sourceImage0, sourceImage1)
			.size(50, 50)
			.notify(mockListener)
			.asBufferedImages();
		
		verify(mockListener).beginProcessing(sourceImage0);
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 0.0)), same(sourceImage0));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 1.0)), same(sourceImage0));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 0.0)), same(sourceImage0));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 1.0)), same(sourceImage0));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 0.0)), same(sourceImage0));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 1.0)), same(sourceImage0));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 0.0)), same(sourceImage0));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 1.0)), same(sourceImage0));
		verify(mockListener).finishedProcessing(sourceImage0, destinationImages.get(0));
		
		verify(mockListener).beginProcessing(sourceImage1);
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 0.0)), same(sourceImage1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 1.0)), same(sourceImage1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 0.0)), same(sourceImage1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 1.0)), same(sourceImage1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 0.0)), same(sourceImage1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 1.0)), same(sourceImage1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 0.0)), same(sourceImage1));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 1.0)), same(sourceImage1));
		verify(mockListener).finishedProcessing(sourceImage1, destinationImages.get(1));
		
		verifyNoMoreInteractions(mockListener);
	}
	
	@Test
	public void successfulProcessing_WithSingleFilterCall() throws IOException
	{
		// This test checks for parts that internally uses ThumbnailTasks
		
		File inputFile = new File("test-resources/Thumbnailator/grid.jpg");
		File outputFile = File.createTempFile("thumbnailator-testing-", ".png");
		outputFile.deleteOnExit();
		
		ThumbnailatorEventListener mockListener = mock(ThumbnailatorEventListener.class);
		
		Thumbnails.of(inputFile)
			.size(50, 50)
			.notify(mockListener)
			.addFilter(new NullFilter())
			.toFile(outputFile);
		
		verify(mockListener).beginProcessing(inputFile);
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 0.0)), same(inputFile));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.ACQUIRE)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 1.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 0.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 1.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 0.0)), same(inputFile));
		verify(mockListener, atLeastOnce()).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 1.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 0.0)), same(inputFile));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.OUTPUT)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 1.0)), same(inputFile));
		verify(mockListener).finishedProcessing(inputFile, outputFile);
		
		verifyNoMoreInteractions(mockListener);
	}
	
	@Test
	public void successfulProcessing_WithMultipleFilterCalls() throws IOException
	{
		// This test checks for parts that internally uses ThumbnailTasks
		
		File inputFile = new File("test-resources/Thumbnailator/grid.jpg");
		File outputFile = File.createTempFile("thumbnailator-testing-", ".png");
		outputFile.deleteOnExit();
		
		ThumbnailatorEventListener mockListener = mock(ThumbnailatorEventListener.class);
		
		Thumbnails.of(inputFile)
		.size(50, 50)
		.notify(mockListener)
		.addFilter(new NullFilter())
		.addFilter(new NullFilter())
		.toFile(outputFile);
		
		verify(mockListener).beginProcessing(inputFile);
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 0.0)), same(inputFile));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.ACQUIRE)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 1.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 0.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 1.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 0.0)), same(inputFile));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.FILTER)), same(inputFile));
		verify(mockListener, atLeastOnce()).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 1.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 0.0)), same(inputFile));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.OUTPUT)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 1.0)), same(inputFile));
		verify(mockListener).finishedProcessing(inputFile, outputFile);
		
		verifyNoMoreInteractions(mockListener);
	}
	
	
	@Test
	public void successfulProcessing_WithSingleFiltersCall() throws IOException
	{
		// This test checks for parts that internally uses ThumbnailTasks
		
		File inputFile = new File("test-resources/Thumbnailator/grid.jpg");
		File outputFile = File.createTempFile("thumbnailator-testing-", ".png");
		outputFile.deleteOnExit();
		
		ThumbnailatorEventListener mockListener = mock(ThumbnailatorEventListener.class);
		
		Thumbnails.of(inputFile)
			.size(50, 50)
			.notify(mockListener)
			.addFilters(Arrays.<ImageFilter>asList(new NullFilter(), new NullFilter()))
			.toFile(outputFile);
		
		verify(mockListener).beginProcessing(inputFile);
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 0.0)), same(inputFile));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.ACQUIRE)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 1.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 0.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 1.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 0.0)), same(inputFile));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.FILTER)), same(inputFile));
		verify(mockListener, atLeastOnce()).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 1.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 0.0)), same(inputFile));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.OUTPUT)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 1.0)), same(inputFile));
		verify(mockListener).finishedProcessing(inputFile, outputFile);
		
		verifyNoMoreInteractions(mockListener);
	}
	
	@Test
	public void successfulProcessing_WithMultipleFiltersCall() throws IOException
	{
		// This test checks for parts that internally uses ThumbnailTasks
		
		File inputFile = new File("test-resources/Thumbnailator/grid.jpg");
		File outputFile = File.createTempFile("thumbnailator-testing-", ".png");
		outputFile.deleteOnExit();
		
		ThumbnailatorEventListener mockListener = mock(ThumbnailatorEventListener.class);
		
		Thumbnails.of(inputFile)
			.size(50, 50)
			.notify(mockListener)
			.addFilters(Arrays.<ImageFilter>asList(new NullFilter(), new NullFilter()))
			.addFilters(Arrays.<ImageFilter>asList(new NullFilter(), new NullFilter()))
			.toFile(outputFile);
		
		verify(mockListener).beginProcessing(inputFile);
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 0.0)), same(inputFile));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.ACQUIRE)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.ACQUIRE, 1.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 0.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.RESIZE, 1.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 0.0)), same(inputFile));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.FILTER)), same(inputFile));
		verify(mockListener, atLeastOnce()).processing(eq(new ThumbnailatorEvent(Phase.FILTER, 1.0)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 0.0)), same(inputFile));
		verify(mockListener, atLeastOnce()).processing(argThat(new EventWithProgressFromZeroToOne(Phase.OUTPUT)), same(inputFile));
		verify(mockListener).processing(eq(new ThumbnailatorEvent(Phase.OUTPUT, 1.0)), same(inputFile));
		verify(mockListener).finishedProcessing(inputFile, outputFile);
		
		verifyNoMoreInteractions(mockListener);
	}


	private static class NullFilter implements ImageFilter
	{
		public BufferedImage apply(BufferedImage img)
		{
			return img;
		}
	}

	private static class EventWithProgressFromZeroToOne 
		extends ArgumentMatcher<ThumbnailatorEvent>
	{
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
}

