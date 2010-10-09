package net.coobird.thumbnailator;

/**
 * This class is used to rename file names.
 * 
 * @author coobird
 *
 */
public abstract class Rename
{
	/**
	 * The default constructor is intended only to be called implicitly
	 * by the classes implementing the functionality of the {@link Rename}
	 * class.
	 */
	protected Rename() {}
	
	/**
	 * Applies the function performed by this {@code Rename} on the
	 * specified name.
	 * 
	 * @param name			Name to apply the function on.
	 * @return				The name after the function has been applied.
	 */
	public abstract String apply(String name);
	
	/**
	 * Appends a suffix to a filename.
	 * 
	 * @param fileName		File name to add a suffix on.
	 * @param suffix		The suffix to add.
	 * @return				File name with specified suffixed affixed.
	 */
	protected String appendSuffix(String fileName, String suffix)
	{
		String newFileName = "";
		
		int indexOfDot = fileName.indexOf('.');
		
		if (indexOfDot != -1)
		{
			newFileName = fileName.substring(0, indexOfDot);
			newFileName += suffix;
			newFileName += fileName.substring(indexOfDot);
		}
		else
		{
			newFileName = fileName + suffix;
		}
		
		return newFileName;
	}
	
	/**
	 * Appends a prefix to a filename.
	 * 
	 * @param fileName		File name to add a prefix on.
	 * @param prefix		The prefix to add.
	 * @return				File name with the specified prefix affixed.
	 */
	protected String appendPrefix(String fileName, String prefix)
	{
		return prefix + fileName;
	}
}