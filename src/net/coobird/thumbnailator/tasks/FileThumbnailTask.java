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

import net.coobird.thumbnailator.BufferedImages;
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
	private File destinationFile;
	
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
		
		if (iis == null)
		{
			throw new IOException("Could not open input file.");
		}
		
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

		BufferedImage img = reader.read(0);
		
		iis.close();
		
		return img;
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
			destinationFile = new File(destinationFile.getAbsolutePath() + "." + formatName);
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
			
			/*
			 * Sets the compression format type, if specified.
			 * 
			 * Note:
			 * The value to denote that the codec's default compression type
			 * should be used is null. 
			 */
			if (param.getOutputFormatType() != ThumbnailParameter.DEFAULT_FORMAT_TYPE)
			{
				writeParam.setCompressionType(param.getOutputFormatType());
			}
		}
		
		ImageOutputStream ios = 
			ImageIO.createImageOutputStream(destinationFile);
		
		if (ios == null)
		{
			throw new IOException("Could not open output file.");
		}
		
		/*
		 * Note:
		 * The following code is a workaround for the JPEG writer which ships
		 * with the JDK.
		 * 
		 * At issue is, that the JPEG writer appears to write the alpha
		 * channel when it should not. To circumvent this, images which are
		 * to be saved as a JPEG will be copied to another BufferedImage without
		 * an alpha channel before it is saved.
		 */
		if (
				formatName.equalsIgnoreCase("jpg")
				|| formatName.equalsIgnoreCase("jpeg")
		)
		{
			img = BufferedImages.copy(img, BufferedImage.TYPE_INT_RGB);
		}
		
		writer.setOutput(ios);
		writer.write(null, new IIOImage(img, null, null), writeParam);
		
		ios.close();
		
		return true;
	}
}
