package net.coobird.thumbnailator.events;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.coobird.thumbnailator.tasks.ThumbnailTask;

public class ThumbnailatorEventNotifier implements ThumbnailatorEventListener
{
	protected final List<ThumbnailatorEventListener> listeners;
	
	public ThumbnailatorEventNotifier()
	{
		listeners = new ArrayList<ThumbnailatorEventListener>();
	}
	
	public ThumbnailatorEventNotifier(List<ThumbnailatorEventListener> listeners)
	{
		this.listeners = listeners;
	}
	
	public boolean add(ThumbnailatorEventListener listener)
	{
		return listeners.add(listener);
	}
	
	public List<ThumbnailatorEventListener> getListeners()
	{
		return Collections.unmodifiableList(listeners);
	}
	
	public void beginBufferedImage(BufferedImage sourceImage)
	{
		for (ThumbnailatorEventListener listener : listeners)
		{
			listener.beginBufferedImage(sourceImage);
		}
	}

	public void beginFile(File sourceFile)
	{
		for (ThumbnailatorEventListener listener : listeners)
		{
			listener.beginFile(sourceFile);
		}
	}

	public void beginTask(ThumbnailTask task)
	{
		for (ThumbnailatorEventListener listener : listeners)
		{
			listener.beginTask(task);
		}
	}

	public void failedProcessingFile(File sourceFile)
	{
		for (ThumbnailatorEventListener listener : listeners)
		{
			listener.failedProcessingFile(sourceFile);
		}
	}

	public void failedProcessingTask(ThumbnailTask task)
	{
		for (ThumbnailatorEventListener listener : listeners)
		{
			listener.failedProcessingTask(task);
		}
	}

	public void processedBufferedImage(BufferedImage sourceImage,
			BufferedImage destinationImage)
	{
		for (ThumbnailatorEventListener listener : listeners)
		{
			listener.processedBufferedImage(sourceImage, destinationImage);
		}
	}

	public void processedFile(File sourceFile, File destinationFile)
	{
		for (ThumbnailatorEventListener listener : listeners)
		{
			listener.processedFile(sourceFile, destinationFile);
		}
		
	}

	public void processedTask(ThumbnailTask task)
	{
		for (ThumbnailatorEventListener listener : listeners)
		{
			listener.processedTask(task);
		}
	}

	public void processingFile(ThumbnailatorEvent event, File sourceFile)
	{
		for (ThumbnailatorEventListener listener : listeners)
		{
			listener.processingFile(event, sourceFile);
		}
	}

	public void processingTask(ThumbnailatorEvent event, ThumbnailTask task)
	{
		for (ThumbnailatorEventListener listener : listeners)
		{
			listener.processingTask(event, task);
		}
	}
}
