/*
 * Thumbnailator - a thumbnail generation library
 *
 * Copyright (c) 2008-2020 Chris Kroells
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.coobird.thumbnailator.name;

import net.coobird.thumbnailator.ThumbnailParameter;

/**
 * This class is used to rename file names.
 * 
 * @author coobird
 *
 */
public abstract class Rename {
	/**
	 * <p>
	 * A {@code Rename} which does not alter the given file name.
	 * </p>
	 * <p>
	 * For example, given {@code picture.jpg}, result is {@code picture.jpg}.
	 * </p>
	 * <p>
	 * Note: The {@link #apply(String, ThumbnailParameter)} method does not use
	 * the {@code param} parameter. A value of {@code null} for {@code param} is
	 * permitted.
	 * </p>
	 */
	public static final Rename NO_CHANGE = new Rename() {
		@Override
		public String apply(String name, ThumbnailParameter param) {
			return name;
		}
	};
	
	/**
	 * <p>
	 * Appends {@code thumbnail.} to the beginning of the file name.
	 * </p>
	 * <p>
	 * For example, given {@code picture.jpg}, result is
	 * {@code thumbnail.picture.jpg}.
	 * </p>
	 * <p>
	 * Note: The {@link #apply(String, ThumbnailParameter)} method does not use
	 * the {@code param} parameter. A value of {@code null} for {@code param} is
	 * permitted.
	 * </p>
	 */
	public static final Rename PREFIX_DOT_THUMBNAIL = new Rename() {
		@Override
		public String apply(String fileName, ThumbnailParameter param) {
			return appendPrefix(fileName, "thumbnail.");
		}
	};
	
	/**
	 * <p>
	 * Appends {@code thumbnail-} to the beginning of the file name.
	 * </p>
	 * <p>
	 * For example, given {@code picture.jpg}, result is
	 * {@code thumbnail-picture.jpg}.
	 * </p>
	 * <p>
	 * Note: The {@link #apply(String, ThumbnailParameter)} method does not use
	 * the {@code param} parameter. A value of {@code null} for {@code param} is
	 * permitted.
	 * </p>
	 * 
	 * @deprecated 		Please use the correctly spelled
	 * 					{@link Rename#PREFIX_HYPHEN_THUMBNAIL}. This constant
	 * 					will be removed in Thumbnailator 0.5.0.
	 */
	@Deprecated
	public static final Rename PREFIX_HYPTHEN_THUMBNAIL = Rename.PREFIX_HYPHEN_THUMBNAIL;
	
	/**
	 * <p>
	 * Appends {@code thumbnail-} to the beginning of the file name.
	 * </p>
	 * <p>
	 * For example, given {@code picture.jpg}, result is
	 * {@code thumbnail-picture.jpg}.
	 * </p>
	 * <p>
	 * Note: The {@link #apply(String, ThumbnailParameter)} method does not use
	 * the {@code param} parameter. A value of {@code null} for {@code param} is
	 * permitted.
	 * </p>
	 */
	public static final Rename PREFIX_HYPHEN_THUMBNAIL = new Rename() {
		@Override
		public String apply(String fileName, ThumbnailParameter param) {
			return appendPrefix(fileName, "thumbnail-");
		}
	};
	
	/**
	 * <p>
	 * Appends {@code .thumbnail} to the file name prior to the extension of
	 * the file.
	 * </p>
	 * <p>
	 * For example, given {@code picture.jpg}, result is
	 * {@code picture.thumbnail.jpg}.
	 * </p>
	 * <p>
	 * Note: The {@link #apply(String, ThumbnailParameter)} method does not use
	 * the {@code param} parameter. A value of {@code null} for {@code param} is
	 * permitted.
	 * </p>
	 */
	public static final Rename SUFFIX_DOT_THUMBNAIL = new Rename() {
		@Override
		public String apply(String fileName, ThumbnailParameter param) {
			return appendSuffix(fileName, ".thumbnail");
		}
	};
	
	/**
	 * <p>
	 * Appends {@code -thumbnail} to the file name prior to the extension of
	 * the file.
	 * </p>
	 * <p>
	 * For example, given {@code picture.jpg}, result is
	 * {@code picture-thumbnail.jpg}.
	 * </p>
	 * <p>
	 * Note: The {@link #apply(String, ThumbnailParameter)} method does not use
	 * the {@code param} parameter. A value of {@code null} for {@code param} is
	 * permitted.
	 * </p>
	 * 
	 * @deprecated 		Please use the correctly spelled
	 * 					{@link Rename#SUFFIX_HYPHEN_THUMBNAIL}. This constant
	 * 					will be removed in Thumbnailator 0.5.0.
	 */
	@Deprecated
	public static final Rename SUFFIX_HYPTHEN_THUMBNAIL = Rename.SUFFIX_HYPHEN_THUMBNAIL;
	
	/**
	 * <p>
	 * Appends {@code -thumbnail} to the file name prior to the extension of
	 * the file.
	 * </p>
	 * <p>
	 * For example, given {@code picture.jpg}, result is
	 * {@code picture-thumbnail.jpg}.
	 * </p>
	 * <p>
	 * Note: The {@link #apply(String, ThumbnailParameter)} method does not use
	 * the {@code param} parameter. A value of {@code null} for {@code param} is
	 * permitted.
	 * </p>
	 */
	public static final Rename SUFFIX_HYPHEN_THUMBNAIL = new Rename() {
		@Override
		public String apply(String fileName, ThumbnailParameter param) {
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
	protected String appendSuffix(String fileName, String suffix) {
		String newFileName = "";
		
		int indexOfDot = fileName.lastIndexOf('.');
		
		if (indexOfDot != -1) {
			newFileName = fileName.substring(0, indexOfDot);
			newFileName += suffix;
			newFileName += fileName.substring(indexOfDot);
		} else {
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
	protected String appendPrefix(String fileName, String prefix) {
		return prefix + fileName;
	}
}
