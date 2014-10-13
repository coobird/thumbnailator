package net.coobird.thumbnailator.tasks.io;

import net.coobird.thumbnailator.ThumbnailParameter;

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
	 * The parameters that should be used when retrieving the image.
	 */
	protected ThumbnailParameter param;
	
	/**
	 * Indicates whether the input has already been read.
	 */
	protected boolean hasReadInput = false;
	
	/**
	 * Default constructor.
	 */
	protected AbstractImageSource() {}
	
	/**
	 * Indicates that the {@link ImageSource} has completed reading the input
	 * file, and returns the value given in the argument.
	 * <p>
	 * This method should be used by implementation classes when returning
	 * the result of the {@link #read()} method, as shown in the following
	 * example code:
<pre>
return finishedReading(sourceImage);
</pre>
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
	
	public void setThumbnailParameter(ThumbnailParameter param)
	{
		this.param = param;
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
