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
	 * A {@code Rename} which does not alter the given file name.
	 * <p>
	 * <dt>Example</dt>
	 * <ul>
	 * <li>Before: {@code picture.jpg}</li>
	 * <li>After: {@code picture.jpg}</li>
	 * </ul>
	 */
	public static final Rename NO_CHANGE = new Rename() {
		@Override
		public String apply(String name)
		{
			return name;
		}
	};
	
	/**
	 * Appends {@code thumbnail.} to the beginning of the file name.
	 * <p>
	 * <dt>Example</dt>
	 * <ul>
	 * <li>Before: {@code picture.jpg}</li>
	 * <li>After: {@code thumbnail.picture.jpg}</li>
	 * </ul>
	 */
	public static final Rename PREFIX_DOT_THUMBNAIL = new Rename() {
		@Override
		public String apply(String fileName)
		{
			return appendPrefix(fileName, "thumbnail.");
		}
	};
	
	/**
	 * Appends {@code thumbnail-} to the beginning of the file name.
	 * <p>
	 * <dt>Example</dt>
	 * <ul>
	 * <li>Before: {@code picture.jpg}</li>
	 * <li>After: {@code thumbnail-picture.jpg}</li>
	 * </ul>
	 */
	public static final Rename PREFIX_HYPTHEN_THUMBNAIL = new Rename() {
		@Override
		public String apply(String fileName)
		{
			return appendPrefix(fileName, "thumbnail-");
		}
	};
	
	/**
	 * Appends {@code .thumbnail} to the file name prior to the extension of
	 * the file.
	 * <p>
	 * <dt>Example</dt>
	 * <ul>
	 * <li>Before: {@code picture.jpg}</li>
	 * <li>After: {@code picture.thumbnail.jpg}</li>
	 * </ul>
	 */
	public static final Rename SUFFIX_DOT_THUMBNAIL = new Rename() {
		@Override
		public String apply(String fileName)
		{
			return appendSuffix(fileName, ".thumbnail");
		}
	};
	
	/**
	 * Appends {@code -thumbnail} to the file name prior to the extension of
	 * the file.
	 * <p>
	 * <dt>Example</dt>
	 * <ul>
	 * <li>Before: {@code picture.jpg}</li>
	 * <li>After: {@code picture-thumbnail.jpg}</li>
	 * </ul>
	 */
	public static final Rename SUFFIX_HYPTHEN_THUMBNAIL = new Rename() {
		@Override
		public String apply(String fileName)
		{
			return appendSuffix(fileName, "-thumbnail");
		}
	};

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