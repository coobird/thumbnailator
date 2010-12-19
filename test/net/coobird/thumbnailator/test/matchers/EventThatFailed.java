/**
 * 
 */
package net.coobird.thumbnailator.test.matchers;

import net.coobird.thumbnailator.events.ThumbnailatorEvent;
import net.coobird.thumbnailator.events.ThumbnailatorEvent.Phase;

import org.mockito.ArgumentMatcher;

public class EventThatFailed
	extends ArgumentMatcher<ThumbnailatorEvent>
{
	private final Phase expectedPhase;
	private final Class<?> cls;
	private final String message;
	
	/**
	 * @param expectedPhase
	 */
	public EventThatFailed(Phase expectedPhase, Class<? extends Throwable> cls, String message)
	{
		super();
		this.expectedPhase = expectedPhase;
		this.cls = cls;
		this.message = message;
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
		else if (!t.getMessage().equals(message))
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