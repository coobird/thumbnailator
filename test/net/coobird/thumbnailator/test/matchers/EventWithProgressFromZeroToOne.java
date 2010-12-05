/**
 * 
 */
package net.coobird.thumbnailator.test.matchers;

import net.coobird.thumbnailator.events.ThumbnailatorEvent;
import net.coobird.thumbnailator.events.ThumbnailatorEvent.Phase;

import org.mockito.ArgumentMatcher;

public class EventWithProgressFromZeroToOne  
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