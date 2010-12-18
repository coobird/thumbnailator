package net.coobird.thumbnailator.tasks;


public abstract class AbstractImageSource implements ImageSource
{
	protected String inputFormatName;
	
	public String getInputFormatName()
	{
		if (inputFormatName == null)
		{
			throw new IllegalStateException("Input has not been read yet.");
		}
		return inputFormatName;
	}
}
