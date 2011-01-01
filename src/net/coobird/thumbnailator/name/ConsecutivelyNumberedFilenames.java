package net.coobird.thumbnailator.name;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ConsecutivelyNumberedFilenames implements Iterable<File>
{
	private final Iterator<File> iter;

	public ConsecutivelyNumberedFilenames()
	{
		this.iter = new ConsecutivelyNumberedFilenamesIterator(new File("").getParentFile(), "%d", 0);
	}

	public ConsecutivelyNumberedFilenames(int start)
	{
		this.iter = new ConsecutivelyNumberedFilenamesIterator(new File("").getParentFile(), "%d", start);
	}
	
	private static void checkDirectory(File dir) throws IOException
	{
		if (!dir.isDirectory())
		{
			throw new IOException(
					"Specified path is not a directory or does not exist."
			);
		}
	}

	public ConsecutivelyNumberedFilenames(File dir) throws IOException
	{
		checkDirectory(dir);
		this.iter = new ConsecutivelyNumberedFilenamesIterator(dir, "%d", 0);
	}
	
	public ConsecutivelyNumberedFilenames(String format)
	{
		this.iter = new ConsecutivelyNumberedFilenamesIterator(new File("").getParentFile(), format, 0);
	}

	public ConsecutivelyNumberedFilenames(File dir, int start) throws IOException
	{
		checkDirectory(dir);
		this.iter = new ConsecutivelyNumberedFilenamesIterator(dir, "%d", start);
	}
	
	public ConsecutivelyNumberedFilenames(File dir, String format) throws IOException
	{
		checkDirectory(dir);
		this.iter = new ConsecutivelyNumberedFilenamesIterator(dir, format, 0);
	}

	public ConsecutivelyNumberedFilenames(String format, int start)
	{
		this.iter = new ConsecutivelyNumberedFilenamesIterator(new File("").getParentFile(), format, start);
	}

	public ConsecutivelyNumberedFilenames(File dir, String format, int start) throws IOException
	{
		checkDirectory(dir);
		this.iter = new ConsecutivelyNumberedFilenamesIterator(dir, format, start);
	}

	private class ConsecutivelyNumberedFilenamesIterator implements Iterator<File>
	{
		private final File dir;
		private final String format;
		private int count;
		
		public ConsecutivelyNumberedFilenamesIterator(File dir, String format, int start)
		{
			super();
			this.dir = dir;
			this.format = format;
			this.count = start;
		}

		public boolean hasNext()
		{
			return true;
		}

		public File next()
		{
			File f = new File(dir, String.format(format, count));
			count++;
			return f;
		}

		public void remove()
		{
			throw new UnsupportedOperationException(
					"Cannot remove elements from this iterator."
			);
		}
	}

	public Iterator<File> iterator()
	{
		return iter;
	}
}
