package net.coobird.thumbnailator.events;


public class ThumbnailatorEvent
{
	/**
	 * The value returned by the {@link #getProgress()} method when this
	 * event was caused by a failure during processing.
	 */
	public static final double ERROR = Double.NaN;
	
	public enum Phase
	{
		/**
		 * The image is being acquired.
		 */
		ACQUIRE,
		
		/**
		 * The image is being resized.
		 */
		RESIZE,
		
		/**
		 * The image is being filtered.
		 */
		FILTER,
		
		/**
		 * The image is being output.
		 */
		OUTPUT,
		;
	}
	
	/**
	 * The current phase of the image processing. 
	 */
	private final Phase phase;
	
	/**
	 * The progress in this phase of processing.
	 * <p>
	 * The {@code double} ranges from {@code 0.0} to {@code 1.0}, where
	 * {@code 0.0} indicates no progress, and {@code 1.0} indicates completion.
	 * If an anomaly has occurred, {@link Double#NaN} will be used.
	 */
	private final double progress;
	
	/**
	 * The cause for the failure of processing.
	 * <p>
	 * If a failure has not occurred, or if the cause of a failure is not
	 * available, then this field will be {@code null}.
	 */
	private final Throwable throwable;
	
	
	/**
	 * @param phase
	 * @param progress
	 */
	public ThumbnailatorEvent(Phase phase, double progress)
	{
		super();
		this.phase = phase;
		this.progress = progress;
		this.throwable = null;
	}
	
	/**
	 * Instantiates an event which indicates that an failure has occurred
	 * during processing, and a cause for the failure is available. 
	 * 
	 * @param phase
	 * @param t
	 */
	public ThumbnailatorEvent(Phase phase, Throwable t)
	{
		super();
		this.phase = phase;
		this.progress = ERROR;
		this.throwable = t;
	}
	

	/**
	 * @return the phase
	 */
	public Phase getPhase()
	{
		return phase;
	}

	/**
	 * @return the progress
	 */
	public double getProgress()
	{
		return progress;
	}
	
	/**
	 * Returns the cause of the failure.
	 * <p>
	 * If no cause is available, or if a failure has not occurred, then 
	 * {@code null} is returned.
	 * 
	 * @return the throwable
	 */
	public Throwable getCause()
	{
		return throwable;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((phase == null) ? 0 : phase.hashCode());
		long temp;
		temp = Double.doubleToLongBits(progress);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((throwable == null) ? 0 : throwable.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ThumbnailatorEvent other = (ThumbnailatorEvent) obj;
		if (phase == null)
		{
			if (other.phase != null)
				return false;
		}
		else if (!phase.equals(other.phase))
			return false;
		if (Double.doubleToLongBits(progress) != Double
				.doubleToLongBits(other.progress))
			return false;
		if (throwable == null)
		{
			if (other.throwable != null)
				return false;
		}
		else if (!throwable.equals(other.throwable))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "ThumbnailatorEvent [phase=" + phase + ", progress=" + progress
				+ ", throwable=" + throwable + "]";
	}
}
