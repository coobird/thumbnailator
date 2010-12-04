package net.coobird.thumbnailator.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	
	public void beginProcessing(Object source)
	{
		for (ThumbnailatorEventListener listener : listeners)
		{
			listener.beginProcessing(source);
		}
	}

	public void failedProcessing(ThumbnailatorEvent event, Object source)
	{
		for (ThumbnailatorEventListener listener : listeners)
		{
			listener.failedProcessing(event, source);
		}
	}

	public void processing(ThumbnailatorEvent event, Object source)
	{
		for (ThumbnailatorEventListener listener : listeners)
		{
			listener.processing(event, source);
		}
	}
	
	public void finishedProcessing(Object source, Object destination)
	{
		for (ThumbnailatorEventListener listener : listeners)
		{
			listener.finishedProcessing(source, destination);
		}
	}
}
