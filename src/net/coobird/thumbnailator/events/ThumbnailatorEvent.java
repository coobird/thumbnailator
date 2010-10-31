package net.coobird.thumbnailator.events;


public class ThumbnailatorEvent 
{
	private final int currentTask;
	private final int totalTasks;
	private final int successfulTasks;
	private final int failedTasks;
	private final double currentPercentage;
	
	/**
	 * @param currentTask
	 * @param totalTasks
	 * @param successfulTasks
	 * @param failedTasks
	 * @param currentPercentage
	 */
	public ThumbnailatorEvent(int currentTask, int totalTasks,
			int successfulTasks, int failedTasks, double currentPercentage)
	{
		super();
		this.currentTask = currentTask;
		this.totalTasks = totalTasks;
		this.successfulTasks = successfulTasks;
		this.failedTasks = failedTasks;
		this.currentPercentage = currentPercentage;
	}

	public int getCurrentTask()
	{
		return currentTask;
	}

	public int getTotalTasks()
	{
		return totalTasks;
	}

	public int getSuccessfulTasks()
	{
		return successfulTasks;
	}

	public int getFailedTasks()
	{
		return failedTasks;
	}

	public double getCurrentPercentage()
	{
		return currentPercentage;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(currentPercentage);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + currentTask;
		result = prime * result + failedTasks;
		result = prime * result + successfulTasks;
		result = prime * result + totalTasks;
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
		if (Double.doubleToLongBits(currentPercentage) != Double
				.doubleToLongBits(other.currentPercentage))
			return false;
		if (currentTask != other.currentTask)
			return false;
		if (failedTasks != other.failedTasks)
			return false;
		if (successfulTasks != other.successfulTasks)
			return false;
		if (totalTasks != other.totalTasks)
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "ThumbnailatorEvent [currentTask=" + currentTask
				+ ", totalTasks=" + totalTasks + ", successfulTasks="
				+ successfulTasks + ", failedTasks=" + failedTasks
				+ ", currentPercentage=" + currentPercentage + "]";
	}
}
