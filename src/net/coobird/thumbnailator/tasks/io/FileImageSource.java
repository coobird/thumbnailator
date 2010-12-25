package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import net.coobird.thumbnailator.tasks.UnsupportedFormatException;

/**
 * 
 * @author coobird
 *
 */
public class FileImageSource extends AbstractImageSource
{
	/**
	 * The index used to obtain the first image in an image file.
	 */
	private static final int FIRST_IMAGE_INDEX = 0;
	
	/**
	 * The file from which the image should be obtained. 
	 */
	private final File sourceFile;
	
	/**
	 * @param sourceFile
	 */
	public FileImageSource(File sourceFile)
	{
		super();
		this.sourceFile = sourceFile;
	}
	
	/**
	 * @param sourceFile
	 */
	public FileImageSource(String sourceFilePath)
	{
		super();
		this.sourceFile = new File(sourceFilePath);
	}

	public BufferedImage read() throws IOException
	{
		if (!sourceFile.exists())
		{
			throw new FileNotFoundException(
					"Could not find file: " + sourceFile.getAbsolutePath()
			);
		}
		
		/* TODO refactor.
		 * The following code has been adapted from the 
		 * StreamThumbnailTask.read method.
		 */
		ImageInputStream iis = ImageIO.createImageInputStream(sourceFile);
		
		if (iis == null)
		{
			throw new IOException(
					"Could not open file: " + sourceFile.getAbsolutePath());
		}
		
		Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
		if (!readers.hasNext())
		{
			String sourcePath = sourceFile.getPath();
			throw new UnsupportedFormatException(
					UnsupportedFormatException.UNKNOWN,
					"No suitable ImageReader found for " + sourcePath + "."
			);
		}
		
		ImageReader reader = readers.next();
		reader.setInput(iis);
		inputFormatName = reader.getFormatName();

		BufferedImage img = reader.read(FIRST_IMAGE_INDEX);
		
		iis.close();
		
		return img;
	}

	/**
	 * Returns the source file from which an image is read.
	 * 
	 * @return 		The {@code File} representation of the source file.
	 */
	public File getFile()
	{
		return sourceFile;
	}
}
