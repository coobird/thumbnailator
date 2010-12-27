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
	
	public String getInputFormatName()
	{
		if (inputFormatName == null)
		{
			throw new IllegalStateException("Input has not been read yet.");
		}
		return inputFormatName;
	}
}
