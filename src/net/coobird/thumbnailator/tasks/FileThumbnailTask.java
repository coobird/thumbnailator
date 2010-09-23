package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import net.coobird.thumbnailator.ThumbnailParameter;

/**
 * A thumbnail generation task which reads and writes data from and to a 
 * {@link File}.
 * 
 * @author coobird
 *
 */
public class FileThumbnailTask extends ThumbnailTask
{
	/**
	 * The {@link File} from which image data is read from.
	 */
	private final File sourceFile;
	
	/**
	 * The {@link File} to which image data is written to.
	 */
	private final File destinationFile;
	
	/**
	 * Creates a {@link ThumbnailTask} in which image data is read from the 
	 * specified {@link File} and is output to a specified {@link File}, using
	 * the parameters provided in the specified {@link ThumbnailParameter}.
	 * 
	 * @param param				The parameters to use to create the thumbnail.
	 * @param sourceFile		The {@link File} from which image data is read.
	 * @param destinationFile	The {@link File} to which thumbnail is written.
	 */
	public FileThumbnailTask(ThumbnailParameter param, File sourceFile, File destinationFile)
	{
		super(param);
		this.sourceFile = sourceFile;
		this.destinationFile = destinationFile;
	}

	@Override
	public BufferedImage read() throws IOException
	{
		/* TODO refactor.
		 * The following code has been adapted from the 
		 * StreamThumbnailTask.read method.
		 */
		ImageInputStream iis = ImageIO.createImageInputStream(sourceFile);
		
		Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
		if (!readers.hasNext())
		{
			throw new IOException(
					"No acceptable ImageReader found for " + 
					sourceFile.getPath() + ".");
		}
		
		ImageReader reader = readers.next();
		reader.setInput(iis);
		inputFormatName = reader.getFormatName();
		
		return reader.read(0);
	}

	@Override
	public boolean write(BufferedImage img) throws IOException
	{
		/* TODO refactor.
		 * The following code has been adapted from the 
		 * StreamThumbnailTask.write method.
		 */
		
		String formatName;
		if (param.getOutputFormat() == ThumbnailParameter.ORIGINAL_FORMAT)
		{
			formatName = inputFormatName;
		}
		else
		{
			formatName = param.getOutputFormat();
		}
			
		Iterator<ImageWriter> writers = 
			ImageIO.getImageWritersByFormatName(formatName);
		
		if (!writers.hasNext())
		{
			return false;
		}
		
		ImageWriter writer = writers.next();
		
		ImageWriteParam writeParam = writer.getDefaultWriteParam();
		if (writeParam.canWriteCompressed())
		{
			writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			
			/*
			 * Sets the compression quality, if specified.
			 * 
			 * Note:
			 * The value to denote that the codec's default compression quality
			 * should be used is Float.NaN. 
			 */
			if (!Float.isNaN(param.getOutputQuality()))
			{
				writeParam.setCompressionQuality(param.getOutputQuality());
			}
		}
		
		ImageOutputStream ios = 
			ImageIO.createImageOutputStream(destinationFile);
		
		writer.setOutput(ios);
		writer.write(null, new IIOImage(img, null, null), writeParam);
		
		return true;
	}
}
