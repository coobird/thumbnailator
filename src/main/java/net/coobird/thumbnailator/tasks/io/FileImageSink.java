package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import net.coobird.thumbnailator.util.BufferedImages;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;

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
	
	private final boolean allowOverwrite;
	
	/**
	 * Instantiates a {@link FileImageSink} with the file to which the thumbnail
	 * should be written to.
	 * <p>
	 * The output format to use will be determined from the file extension.
	 * If another format should be used, then the
	 * {@link #setOutputFormatName(String)} should be called with the desired
	 * output format name.
	 * <p>
	 * When the destination file exists, then this {@code FileImageSink} will
	 * overwrite the existing file.
	 * 
	 * @param destinationFile		The destination file.
	 * @throws NullPointerException	If the specified file is {@code null}.
	 */
	public FileImageSink(File destinationFile)
	{
		this(destinationFile, true);
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
	 * @param destinationFile		The destination file.
	 * @param allowOverwrite		Whether or not the {@code FileImageSink}
	 * 								should overwrite the destination file if
	 * 								it already exists.
	 * @throws NullPointerException	If the specified file is {@code null}.
	 */
	public FileImageSink(File destinationFile, boolean allowOverwrite)
	{
		super();
		
		if (destinationFile == null)
		{
			throw new NullPointerException("File cannot be null.");
		}
		
		this.destinationFile = destinationFile;
		this.outputFormat = getExtension(destinationFile);
		this.allowOverwrite = allowOverwrite;
	}
	
	/**
	 * Instantiates a {@link FileImageSink} with the file to which the thumbnail
	 * should be written to.
	 * <p>
	 * The output format to use will be determined from the file extension.
	 * If another format should be used, then the
	 * {@link #setOutputFormatName(String)} should be called with the desired
	 * output format name.
	 * <p>
	 * When the destination file exists, then this {@code FileImageSink} will
	 * overwrite the existing file.
	 * 
	 * @param destinationFilePath	The destination file path.
	 * @throws NullPointerException	If the specified file path is {@code null}.
	 */
	public FileImageSink(String destinationFilePath)
	{
		this(destinationFilePath, true);
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
	 * @param allowOverwrite		Whether or not the {@code FileImageSink}
	 * 								should overwrite the destination file if
	 * 								it already exists.
	 * @throws NullPointerException	If the specified file path is {@code null}.
	 */
	public FileImageSink(String destinationFilePath, boolean allowOverwrite)
	{
		super();
		
		if (destinationFilePath == null)
		{
			throw new NullPointerException("File cannot be null.");
		}
		
		this.destinationFile = new File(destinationFilePath);
		this.outputFormat = getExtension(destinationFile);
		this.allowOverwrite = allowOverwrite;
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
		
		ImageWriter iw;
		try
		{
			iw = ImageIO.getImageWritersByFormatName(formatName).next();
		}
		catch (NoSuchElementException e)
		{
			return false;
		}
		
		String[] suffixes = iw.getOriginatingProvider().getFileSuffixes();
		
		for (String suffix : suffixes)
		{
			if (fileExtension.equalsIgnoreCase(suffix))
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
	
	@Override
	public String preferredOutputFormatName()
	{
		String fileExtension = getExtension(destinationFile);

		if (fileExtension != null)
		{
			Iterator<ImageReader> rIter = ImageIO.getImageReadersBySuffix(fileExtension);
			
			if (rIter.hasNext())
			{
				try
				{
					return rIter.next().getFormatName();
				}
				catch (IOException e)
				{
					return ThumbnailParameter.ORIGINAL_FORMAT;
				}
			}
		}
		
		return outputFormat;
	}
	
	/**
	 * Writes the resulting image to a file.
	 * 
	 * @param img							The image to write.
	 * @throws UnsupportedFormatException	When an unsupported format has been
	 * 										specified by the
	 * 										{@link #setOutputFormatName(String)}
	 * 										method, or if the output format
	 * 										has not been set and cannot be
	 * 										determined from the file name.
	 * @throws IOException					When a problem occurs while writing
	 * 										the image.
	 * @throws NullPointerException			If the image is {@code null}.
	 * @throws IllegalArgumentException		If this {@code FileImageSink} does
	 * 										not permit overwriting the
	 * 										destination file and the destination
	 * 										file already exists.
	 */
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
		if (formatName != null && (fileExtension == null || !isMatchingFormat(formatName, fileExtension)))
		{
			destinationFile = new File(destinationFile.getAbsolutePath() + "." + formatName);
		}
		
		if (!allowOverwrite && destinationFile.exists()) {
			throw new IllegalArgumentException("The destination file exists.");
		}
		
		/*
		 * If a formatName is not specified, then attempt to determine it from
		 * the file extension.
		 */
		if (formatName == null && fileExtension != null)
		{
			Iterator<ImageReader> rIter = ImageIO.getImageReadersBySuffix(fileExtension);
			
			if (rIter.hasNext())
			{
				formatName = rIter.next().getFormatName();
			}
		}
		
		if (formatName == null)
		{
			throw new UnsupportedFormatException(
					formatName,
					"Could not determine output format."
			);
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
			else
			{
				List<String> supportedFormats =
					ThumbnailatorUtils.getSupportedOutputFormatTypes(formatName);
				
				if (!supportedFormats.isEmpty())
				{
					writeParam.setCompressionType(supportedFormats.get(0));
				}
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
		
		/*
		 * Here, an explicit FileOutputStream is being created, as using a
		 * File object directly to obtain an ImageOutputStream was causing
		 * a problem where if the destination file already exists, then the
		 * image data was being written to the beginning of the file rather than
		 * creating a new file.
		 */
		ImageOutputStream ios;
		FileOutputStream fos;

		/*
		 * The following two lines used to be surrounded by a try-catch,
		 * but it has been removed, as the IOException which it was
		 * throwing in the catch block was not giving good feedback as to
		 * what was causing the original IOException.
		 * 
		 * It would have been informative to have the IOException which
		 * caused this problem, but the IOException in Java 5 does not
		 * have a "cause" parameter.
		 * 
		 * The "cause" parameter has been introduced in Java 6:
		 * http://docs.oracle.com/javase/6/docs/api/java/io/IOException.html#IOException%28java.lang.String,%20java.lang.Throwable%29
		 *
		 * TODO Whether to surround this portion of code in a try-catch
		 *      again is debatable, as it wouldn't really add more utility.
		 *
		 *      Furthermore, there are other calls in this method which will
		 *      throw IOExceptions, but they are not surrounded by try-catch
		 *      blocks. (A similar example exists in the OutputStreamImageSink
		 *      where the ImageIO.createImageOutputStream is not surrounded
		 *      in a try-catch.)
		 *
		 * Related issue:
		 * http://code.google.com/p/thumbnailator/issues/detail?id=37
		 */
		fos = new FileOutputStream(destinationFile);
		ios = ImageIO.createImageOutputStream(fos);
		
		if (ios == null || fos == null)
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
		
		/*
		 * Dispose the writer to free resources.
		 * 
		 * This seems to be the main culprit of `OutOfMemoryError`s which
		 * started to frequently appear with Java 7 Update 21.
		 * 
		 * Issue:
		 * http://code.google.com/p/thumbnailator/issues/detail?id=42
		 */
		writer.dispose();
		
		ios.close();
		fos.close();
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
