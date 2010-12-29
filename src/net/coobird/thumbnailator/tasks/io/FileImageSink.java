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
 * An {@link ImageSink} which writes the resulting thumbnail to a file.
 * <p>
 * Under certain circumstances, the destination file can change in the course
 * of processing. 
 * <p>
 * This can occur in cases where the file extension does not
 * match the output format set by the {@link #setOutputFormatName(String)}
 * method. In this case, the file name will have a file extension corresponding
 * to the output format set in the above method to be appended to the file
 * name originally provided when instantiating the {@link FileImageSink} object. 
 * 
 * @author coobird
 *
 */
public class FileImageSink extends AbstractImageSink<File>
{
	/**
	 * The file to which the thumbnail is written to.
	 * <p>
	 * Under certain circumstances, the {@link File} object can be replaced
	 * in the course of processing. This can occur in cases where the file
	 * extension has been changed due to incongruence between the extension
	 * and the desired output format.
	 */
	private File destinationFile;
	
	/**
	 * Instantiates a {@link FileImageSink} with the file to which the thumbnail
	 * should be written to.
	 * <p>
	 * The output format to use will be determined from the file extension.
	 * If another format should be used, then the 
	 * {@link #setOutputFormatName(String)} should be called with the desired
	 * output format name.
	 * 
	 * @param destinationFile		The destination file.
	 * @throws NullPointerException	If the file is null.
	 */
	public FileImageSink(File destinationFile)
	{
		super();
		
		if (destinationFile == null)
		{
			throw new NullPointerException("File cannot be null.");
		}
		
		this.destinationFile = destinationFile;
		this.outputFormat = getExtension(destinationFile);
	}
	
	/**
	 * Instantiates a {@link FileImageSink} with the file to which the thumbnail
	 * should be written to.
	 * <p>
	 * The output format to use will be determined from the file extension.
	 * If another format should be used, then the 
	 * {@link #setOutputFormatName(String)} should be called with the desired
	 * output format name.
	 * 
	 * @param destinationFilePath	The destination file path.
	 * @throws NullPointerException	If the filepath is null.
	 */
	public FileImageSink(String destinationFilePath)
	{
		super();
		
		if (destinationFilePath == null)
		{
			throw new NullPointerException("File cannot be null.");
		}
		
		this.destinationFile = new File(destinationFilePath);
		this.outputFormat = getExtension(destinationFile);
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
	private static boolean isMatchingFormat(String formatName, String fileExtension)
	{
		if (formatName == null || fileExtension == null)
		{
			return false;
		}
		
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
	
	/**
	 * Returns the file extension of the given {@link File}.
	 * 
	 * @param f			The file.
	 * @return			The extension of the file.
	 */
	private static String getExtension(File f)
	{
		String fileName = f.getName();
		if (
				fileName.indexOf('.') != -1 
				&& fileName.lastIndexOf('.') != fileName.length() - 1
		)
		{
			int lastIndex = fileName.lastIndexOf('.');
			return fileName.substring(lastIndex + 1); 
		}
		
		return null;
	}

	public void write(BufferedImage img) throws IOException
	{
		super.write(img);
		
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
		String fileExtension = getExtension(destinationFile); 
		
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
		if (writeParam.canWriteCompressed() && param != null)
		{
			writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			
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
	public File getSink()
	{
		return destinationFile;
	}
}
