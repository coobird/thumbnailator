package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import net.coobird.thumbnailator.BufferedImages;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;

/**
 * 
 * 
 * @author coobird
 *
 */
public class FileImageSink extends AbstractImageSink
{
	private File destinationFile;
	
	/**
	 * @param destinationFile
	 */
	public FileImageSink(File destinationFile)
	{
		super();
		this.destinationFile = destinationFile;
	}
	
	/**
	 * @param destinationFile
	 */
	public FileImageSink(String destinationFilePath)
	{
		this(new File(destinationFilePath));
	}
	
	/**
	 * Determines whether an specified format name and file extension are
	 * for the same format.
	 * 
	 * @param formatName			Format name.
	 * @param fileExtension			File extension.
	 * @return						Returns {@code true} if the specified file
	 * 								extension is valid for the specified format.
	 */
	private boolean isMatchingFormat(String formatName, String fileExtension)
	{
		String[] suffixes = ImageIO.getImageWritersByFormatName(formatName)
							.next().getOriginatingProvider().getFileSuffixes();
		
		for (String suffix : suffixes)
		{
			if (fileExtension.equals(suffix))
			{
				return true;
			}
		}
		return false;
	}

	public void write(BufferedImage img) throws IOException
	{
		/* TODO refactor.
		 * The following code has been adapted from the 
		 * StreamThumbnailTask.write method.
		 */
		
		/*
		 * Add or replace the file extension of the output file.
		 * 
		 * If the file extension matches the output format's extension,
		 * then leave as is.
		 * 
		 * Else, append the extension for the output format to the filename. 
		 */
		String fileExtension = null; 
		String fileName = destinationFile.getName();
		if (
				fileName.indexOf('.') != -1 
				&& fileName.lastIndexOf('.') != fileName.length() - 1
		)
		{
			int lastIndex = fileName.lastIndexOf('.');
			fileExtension = fileName.substring(lastIndex + 1); 
		}
		
		String formatName = outputFormat;
		if (fileExtension == null || !isMatchingFormat(formatName, fileExtension)) 
		{
			destinationFile = new File(destinationFile.getAbsolutePath() + "." + formatName);
		}
		
		// Checks for available writers for the format.
		Iterator<ImageWriter> writers = 
			ImageIO.getImageWritersByFormatName(formatName);
		
		if (!writers.hasNext())
		{
			throw new UnsupportedFormatException(
					formatName, 
					"No suitable ImageWriter found for " + formatName + "."
			);
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
		 * 
		 * Also, the BMP writer appears not to support ARGB, so an RGB image
		 * will be produced before saving.
		 */
		if (
				formatName.equalsIgnoreCase("jpg")
				|| formatName.equalsIgnoreCase("jpeg")
				|| formatName.equalsIgnoreCase("bmp")
		)
		{
			img = BufferedImages.copy(img, BufferedImage.TYPE_INT_RGB);
		}
		
		writer.setOutput(ios);
		writer.write(null, new IIOImage(img, null, null), writeParam);
		
		ios.close();
	}

	/**
	 * Returns the detination file of the thumbnail image.
	 * <p>
	 * If the final destination of the thumbnail changes in the course of
	 * writing the thumbnail. (For example, if the file extension for the given
	 * destination did not match the destination file format, then the correct
	 * file extension could be appended.)
	 * 
	 * @return the destinationFile
	 */
	public File getDestinationFile()
	{
		return destinationFile;
	}
}
