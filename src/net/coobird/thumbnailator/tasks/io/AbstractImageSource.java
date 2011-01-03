package net.coobird.thumbnailator.tasks.io;

/**
 * An abstract class for {@link ImageSource}s.
 * 
 * @author coobird
 *
 */
public abstract class AbstractImageSource<T> implements ImageSource<T>
{
	/**
	 * The image format of the input image.
	 */
	protected String inputFormatName;
	
	/**
	 * Indicates whether the input has already been read.
	 */
	protected boolean hasReadInput = false;
	
	/**
	 * Indicates that the {@link ImageSource} has completed reading the input
	 * file, and returns the value given in the argument. 
	 * 
	 * @param <V>			The return value type.
	 * @param returnValue	The return value of the {@link #read()} method.
	 * @return				The return value of the {@link #read()} method.
	 */
	protected <V> V finishedReading(V returnValue)
	{
		hasReadInput = true;
		return returnValue;
	}
	
	public String getInputFormatName()
	{
		if (!hasReadInput)
		{
			throw new IllegalStateException("Input has not been read yet.");
		}
		return inputFormatName;
	}
}
