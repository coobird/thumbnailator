/**
 * 
 */
package net.coobird.thumbnailator.test.matchers;

import net.coobird.thumbnailator.events.ThumbnailatorEvent;
import net.coobird.thumbnailator.events.ThumbnailatorEvent.Phase;

import org.mockito.ArgumentMatcher;

public class EventThatFailedWithAnyMessage
	extends ArgumentMatcher<ThumbnailatorEvent>
{
	private final Phase expectedPhase;
	private final Class<?> cls;
	
	/**
	 * @param expectedPhase
	 */
	public EventThatFailedWithAnyMessage(Phase expectedPhase, Class<? extends Throwable> cls)
	{
		super();
		this.expectedPhase = expectedPhase;
		this.cls = cls;
	}
	
	@Override
	public boolean matches(Object argument)
	{
		if (!(argument instanceof ThumbnailatorEvent))
		{
			return false;
		}
		
		ThumbnailatorEvent event = (ThumbnailatorEvent)argument;
		Throwable t = event.getCause();
		
		if (!cls.isAssignableFrom(t.getClass()))
		{
			return false;
		}
		else if (event.getPhase() != expectedPhase)
		{
			return false;
		}
		
		return true;
	}
}