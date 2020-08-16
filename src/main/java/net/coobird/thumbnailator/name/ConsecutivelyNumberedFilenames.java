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

import java.io.File;
import java.io.IOException;
import java.util.Formatter;
import java.util.Iterator;

/**
 * This class is used to produce file names based on a given format string
 * and an internal counter which increments every time a new file name
 * is produced.
 * 
 * @author coobird
 *
 */
public class ConsecutivelyNumberedFilenames implements Iterable<File> {
	/**
	 * The iterator to return upon the {@link #iterator()} method being called.
	 */
	private final Iterator<File> iter;

	/**
	 * Instantiates an {@code ConsecutivelyNumberedFilenames} object which
	 * returns {@link File}s with file names which are consecutively numbered
	 * beginning from {@code 0}.
	 * <p>
	 * <h3>File name sequence</h3>
	 * <ol>
	 * <li><code>0</code></li>
	 * <li><code>1</code></li>
	 * <li><code>2</code></li>
	 * <li><code>3</code></li>
	 * </ol>
	 * and so on.
	 */
	public ConsecutivelyNumberedFilenames() {
		this.iter = new ConsecutivelyNumberedFilenamesIterator(new File("").getParentFile(), "%d", 0);
	}

	/**
	 * Instantiates an {@code ConsecutivelyNumberedFilenames} object which
	 * returns {@link File}s with file names which are consecutively numbered
	 * beginning from the given value.
	 * <p>
	 * <h3>File name sequence</h3>
	 * For a case where the given value is {@code 5}:
	 * <ol>
	 * <li><code>5</code></li>
	 * <li><code>6</code></li>
	 * <li><code>7</code></li>
	 * <li><code>8</code></li>
	 * </ol>
	 * and so on.
	 * 
	 * @param start		The value from which to start counting.
	 */
	public ConsecutivelyNumberedFilenames(int start) {
		this.iter = new ConsecutivelyNumberedFilenamesIterator(new File("").getParentFile(), "%d", start);
	}
	
	/**
	 * Instantiates an {@code ConsecutivelyNumberedFilenames} object which
	 * returns {@link File}s with file names which are consecutively numbered
	 * beginning from {@code 0}, with the directory specified.
	 * <p>
	 * <h3>File name sequence</h3>
	 * For a case where the parent directory is {@code /foo/bar/}:
	 * <ol>
	 * <li><code>/foo/bar/0</code></li>
	 * <li><code>/foo/bar/1</code></li>
	 * <li><code>/foo/bar/2</code></li>
	 * <li><code>/foo/bar/3</code></li>
	 * </ol>
	 * and so on.
	 * 
 	 * @param dir			The directory in which the files are to be located.
 	 * @throws IOException	If the specified directory path is not a directory,
 	 * 						or if does not exist.
	 */
	public ConsecutivelyNumberedFilenames(File dir) throws IOException {
		checkDirectory(dir);
		this.iter = new ConsecutivelyNumberedFilenamesIterator(dir, "%d", 0);
	}
	
	/**
	 * Instantiates an {@code ConsecutivelyNumberedFilenames} object which
	 * returns {@link File}s with file names which are based on a format string.
	 * The numbering will be consecutive from {@code 0}.
	 * <p>
	 * The format string should contain the string {@code %d} which will be
	 * replaced with a consecutively counted number. Additional formatting
	 * can be applied. For more details, please refer to the section on
	 * <em>Numeric</em> formatting in the Java API specification for the
	 * {@link Formatter} class.
	 * <p>
	 * <h3>File name sequence</h3>
	 * For a case where the format string is {@code image-%d}:
	 * <ol>
	 * <li><code>image-0</code></li>
	 * <li><code>image-1</code></li>
	 * <li><code>image-2</code></li>
	 * <li><code>image-3</code></li>
	 * </ol>
	 * and so on.
	 * 
	 * @param format		The format string to use.
	 */
	public ConsecutivelyNumberedFilenames(String format) {
		this.iter = new ConsecutivelyNumberedFilenamesIterator(new File("").getParentFile(), format, 0);
	}

	/**
	 * Instantiates an {@code ConsecutivelyNumberedFilenames} object which
	 * returns {@link File}s with file names which are consecutively numbered
	 * beginning from from the given value, with the directory specified.
	 * <p>
	 * <h3>File name sequence</h3>
	 * For a case where the parent directory is {@code /foo/bar/}, and the
	 * specified value is {@code 5}:
	 * <ol>
	 * <li><code>/foo/bar/5</code></li>
	 * <li><code>/foo/bar/6</code></li>
	 * <li><code>/foo/bar/7</code></li>
	 * <li><code>/foo/bar/8</code></li>
	 * </ol>
	 * and so on.
	 * 
	 * @param dir			The directory in which the files are to be located.
	 * @param start			The value from which to start counting.
 	 * @throws IOException	If the specified directory path is not a directory,
 	 * 						or if does not exist.
	 */
	public ConsecutivelyNumberedFilenames(File dir, int start) throws IOException {
		checkDirectory(dir);
		this.iter = new ConsecutivelyNumberedFilenamesIterator(dir, "%d", start);
	}
	
	/**
	 * Instantiates an {@code ConsecutivelyNumberedFilenames} object which
	 * returns {@link File}s with file names which are based on a format string,
	 * located in the directory specified. The numbering will be consecutively
	 * counted from {@code 0}.
	 * <p>
	 * The format string should contain the string {@code %d} which will be
	 * replaced with a consecutively counted number. Additional formatting
	 * can be applied. For more details, please refer to the section on
	 * <em>Numeric</em> formatting in the Java API specification for the
	 * {@link Formatter} class.
	 * <p>
	 * <h3>File name sequence</h3>
	 * For a case where the parent directory is {@code /foo/bar/},
	 * with the format string {@code image-%d}:
	 * <ol>
	 * <li><code>/foo/bar/image-0</code></li>
	 * <li><code>/foo/bar/image-1</code></li>
	 * <li><code>/foo/bar/image-2</code></li>
	 * <li><code>/foo/bar/image-3</code></li>
	 * </ol>
	 * and so on.
	 * 
	 * @param dir			The directory in which the files are to be located.
	 * @param format		The format string to use.
 	 * @throws IOException	If the specified directory path is not a directory,
 	 * 						or if does not exist.
	 */
	public ConsecutivelyNumberedFilenames(File dir, String format) throws IOException {
		checkDirectory(dir);
		this.iter = new ConsecutivelyNumberedFilenamesIterator(dir, format, 0);
	}

	/**
	 * Instantiates an {@code ConsecutivelyNumberedFilenames} object which
	 * returns {@link File}s with file names which are based on a format string.
	 * The numbering will be consecutive from the specified value.
	 * <p>
	 * The format string should contain the string {@code %d} which will be
	 * replaced with a consecutively counted number. Additional formatting
	 * can be applied. For more details, please refer to the section on
	 * <em>Numeric</em> formatting in the Java API specification for the
	 * {@link Formatter} class.
	 * <p>
	 * <h3>File name sequence</h3>
	 * For a case where the parent directory is {@code /foo/bar/}, and the
	 * specified value is {@code 5}, with the format string {@code image-%d}:
	 * <ol>
	 * <li><code>image-5</code></li>
	 * <li><code>image-6</code></li>
	 * <li><code>image-7</code></li>
	 * <li><code>image-8</code></li>
	 * </ol>
	 * and so on.
	 * 
	 * @param format		The format string to use.
	 * @param start			The value from which to start counting.
	 */
	public ConsecutivelyNumberedFilenames(String format, int start) {
		this.iter = new ConsecutivelyNumberedFilenamesIterator(new File("").getParentFile(), format, start);
	}

	/**
	 * Instantiates an {@code ConsecutivelyNumberedFilenames} object which
	 * returns {@link File}s with file names which are based on a format string,
	 * located in the directory specified. The numbering will be consecutive
	 * from the specified value.
	 * <p>
	 * The format string should contain the string {@code %d} which will be
	 * replaced with a consecutively counted number. Additional formatting
	 * can be applied. For more details, please refer to the section on
	 * <em>Numeric</em> formatting in the Java API specification for the
	 * {@link Formatter} class.
	 * <p>
	 * <h3>File name sequence</h3>
	 * For a case where the parent directory is {@code /foo/bar/}, and the
	 * specified value is {@code 5}, with format string {@code image-%d}:
	 * <ol>
	 * <li><code>/foo/bar/image-5</code></li>
	 * <li><code>/foo/bar/image-6</code></li>
	 * <li><code>/foo/bar/image-7</code></li>
	 * <li><code>/foo/bar/image-8</code></li>
	 * </ol>
	 * and so on.
	 * 
	 * @param dir			The directory in which the files are to be located.
	 * @param format		The format string to use.
	 * @param start			The value from which to start counting.
 	 * @throws IOException	If the specified directory path is not a directory,
 	 * 						or if does not exist.
	 */
	public ConsecutivelyNumberedFilenames(File dir, String format, int start) throws IOException {
		checkDirectory(dir);
		this.iter = new ConsecutivelyNumberedFilenamesIterator(dir, format, start);
	}

	private static void checkDirectory(File dir) throws IOException {
		if (!dir.isDirectory()) {
			throw new IOException(
					"Specified path is not a directory or does not exist."
			);
		}
	}

	private static class ConsecutivelyNumberedFilenamesIterator implements Iterator<File> {
		private final File dir;
		private final String format;
		private int count;
		
		public ConsecutivelyNumberedFilenamesIterator(File dir, String format, int start) {
			super();
			this.dir = dir;
			this.format = format;
			this.count = start;
		}

		public boolean hasNext() {
			return true;
		}

		public File next() {
			File f = new File(dir, String.format(format, count));
			count++;
			return f;
		}

		public void remove() {
			throw new UnsupportedOperationException(
					"Cannot remove elements from this iterator."
			);
		}
	}

	/**
	 * Returns an iterator which generates file names according to the rules
	 * specified by this object.
	 * 
	 * @return		An iterator which generates file names.
	 */
	public Iterator<File> iterator() {
		return iter;
	}
}
