package net.coobird.thumbnailator.events;


public class ThumbnailatorEvent 
{
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
	 * @param phase
	 * @param progress
	 */
	public ThumbnailatorEvent(Phase phase, double progress)
	{
		super();
		this.phase = phase;
		this.progress = progress;
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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((phase == null) ? 0 : phase.hashCode());
		long temp;
		temp = Double.doubleToLongBits(progress);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		return true;
	}
	
	@Override
	public String toString()
	{
		return "ThumbnailatorEvent [phase=" + phase + ", progress=" + progress
				+ "]";
	}
}
