package net.coobird.thumbnailator.name;

import net.coobird.thumbnailator.ThumbnailParameter;

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
	 * Note: The {@link #apply(String, ThumbnailParameter)} method does not use
	 * the {@code param} parameter. A value of {@code null} for {@code param} is
	 * permitted.
	 * <p>
	 * <dt>Example</dt>
	 * <ul>
	 * <li>Before: {@code picture.jpg}</li>
	 * <li>After: {@code picture.jpg}</li>
	 * </ul>
	 */
	public static final Rename NO_CHANGE = new Rename() {
		@Override
		public String apply(String name, ThumbnailParameter param)
		{
			return name;
		}
	};
	
	/**
	 * Appends {@code thumbnail.} to the beginning of the file name.
	 * <p>
	 * Note: The {@link #apply(String, ThumbnailParameter)} method does not use
	 * the {@code param} parameter. A value of {@code null} for {@code param} is
	 * permitted.
	 * <p>
	 * <dt>Example</dt>
	 * <ul>
	 * <li>Before: {@code picture.jpg}</li>
	 * <li>After: {@code thumbnail.picture.jpg}</li>
	 * </ul>
	 */
	public static final Rename PREFIX_DOT_THUMBNAIL = new Rename() {
		@Override
		public String apply(String fileName, ThumbnailParameter param)
		{
			return appendPrefix(fileName, "thumbnail.");
		}
	};
	
	/**
	 * Appends {@code thumbnail-} to the beginning of the file name.
	 * <p>
	 * Note: The {@link #apply(String, ThumbnailParameter)} method does not use
	 * the {@code param} parameter. A value of {@code null} for {@code param} is
	 * permitted.
	 * <p>
	 * <dt>Example</dt>
	 * <ul>
	 * <li>Before: {@code picture.jpg}</li>
	 * <li>After: {@code thumbnail-picture.jpg}</li>
	 * </ul>
	 * 
	 * @deprecated 		Please use the correctly spelled
	 * 					{@link Rename#PREFIX_HYPHEN_THUMBNAIL}. This constant
	 * 					will be removed in Thumbnailator 0.5.0.
	 */
	@Deprecated
	public static final Rename PREFIX_HYPTHEN_THUMBNAIL = Rename.PREFIX_HYPHEN_THUMBNAIL;
	
	/**
	 * Appends {@code thumbnail-} to the beginning of the file name.
	 * <p>
	 * Note: The {@link #apply(String, ThumbnailParameter)} method does not use
	 * the {@code param} parameter. A value of {@code null} for {@code param} is
	 * permitted.
	 * <p>
	 * <dt>Example</dt>
	 * <ul>
	 * <li>Before: {@code picture.jpg}</li>
	 * <li>After: {@code thumbnail-picture.jpg}</li>
	 * </ul>
	 */
	public static final Rename PREFIX_HYPHEN_THUMBNAIL = new Rename() {
		@Override
		public String apply(String fileName, ThumbnailParameter param)
		{
			return appendPrefix(fileName, "thumbnail-");
		}
	};
	
	/**
	 * Appends {@code .thumbnail} to the file name prior to the extension of
	 * the file.
	 * <p>
	 * Note: The {@link #apply(String, ThumbnailParameter)} method does not use
	 * the {@code param} parameter. A value of {@code null} for {@code param} is
	 * permitted.
	 * <p>
	 * <dt>Example</dt>
	 * <ul>
	 * <li>Before: {@code picture.jpg}</li>
	 * <li>After: {@code picture.thumbnail.jpg}</li>
	 * </ul>
	 */
	public static final Rename SUFFIX_DOT_THUMBNAIL = new Rename() {
		@Override
		public String apply(String fileName, ThumbnailParameter param)
		{
			return appendSuffix(fileName, ".thumbnail");
		}
	};
	
	/**
	 * Appends {@code -thumbnail} to the file name prior to the extension of
	 * the file.
	 * <p>
	 * Note: The {@link #apply(String, ThumbnailParameter)} method does not use
	 * the {@code param} parameter. A value of {@code null} for {@code param} is
	 * permitted.
	 * <p>
	 * <dt>Example</dt>
	 * <ul>
	 * <li>Before: {@code picture.jpg}</li>
	 * <li>After: {@code picture-thumbnail.jpg}</li>
	 * </ul>
	 * 
	 * @deprecated 		Please use the correctly spelled
	 * 					{@link Rename#SUFFIX_HYPHEN_THUMBNAIL}. This constant
	 * 					will be removed in Thumbnailator 0.5.0.
	 */
	@Deprecated
	public static final Rename SUFFIX_HYPTHEN_THUMBNAIL = Rename.SUFFIX_HYPHEN_THUMBNAIL;
	
	/**
	 * Appends {@code -thumbnail} to the file name prior to the extension of
	 * the file.
	 * <p>
	 * Note: The {@link #apply(String, ThumbnailParameter)} method does not use
	 * the {@code param} parameter. A value of {@code null} for {@code param} is
	 * permitted.
	 * <p>
	 * <dt>Example</dt>
	 * <ul>
	 * <li>Before: {@code picture.jpg}</li>
	 * <li>After: {@code picture-thumbnail.jpg}</li>
	 * </ul>
	 */
	public static final Rename SUFFIX_HYPHEN_THUMBNAIL = new Rename() {
		@Override
		public String apply(String fileName, ThumbnailParameter param)
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
	 * specified name and thumbnail creation parameters.
	 * 
	 * @param name			Name to apply the function on.
	 * 						<em>The file name should not include the directory
	 * 						in which the file resides in.</em>
	 * @param param			Parameters used to create the thumbnail.
	 * @return				The name after the function has been applied.
	 */
	public abstract String apply(String name, ThumbnailParameter param);
	
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
		
		int indexOfDot = fileName.lastIndexOf('.');
		
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
