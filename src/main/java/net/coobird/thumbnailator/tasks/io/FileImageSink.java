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

package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;

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
public class FileImageSink implements ImageSink<File> {
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

	private String outputFormat;

	private ThumbnailParameter param;

	/**
	 * An {@link ImageSink} which actually performs the image writing
	 * operations. This {@link ImageSink} can change during the lifecycle
	 * of the {@link FileImageSink} class.
	 */
	private ImageSink<?> imageSink = new UninitializedImageSink();
	
	/**
	 * Temporary placeholder {@link ImageSink} which will be used before
	 * the {@link #read()} method is used. Basically a way to use the
	 * implementation of the {@link AbstractImageSink} without having to
	 * instantiate a {@link OutputStreamImageSink} object before needed.
	 */
	private static class UninitializedImageSink extends AbstractImageSink<Void> {
		public Void getSink() {
			throw new IllegalStateException("This should not happen.");
		}
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
	 * @param destinationFile		The destination file.
	 * @throws NullPointerException	If the specified file is {@code null}.
	 */
	public FileImageSink(File destinationFile) {
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
	public FileImageSink(File destinationFile, boolean allowOverwrite) {
		super();
		
		if (destinationFile == null) {
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
	public FileImageSink(String destinationFilePath) {
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
	public FileImageSink(String destinationFilePath, boolean allowOverwrite) {
		super();
		
		if (destinationFilePath == null) {
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
	private static boolean isMatchingFormat(String formatName, String fileExtension) throws UnsupportedFormatException {
		if (formatName == null || fileExtension == null) {
			return false;
		}
		
		ImageWriter iw;
		try {
			iw = ImageIO.getImageWritersByFormatName(formatName).next();

		} catch (NoSuchElementException e) {
			throw new UnsupportedFormatException(
					formatName,
					"No suitable ImageWriter found for " + formatName + "."
			);
		}
		
		String[] suffixes = iw.getOriginatingProvider().getFileSuffixes();
		
		for (String suffix : suffixes) {
			if (fileExtension.equalsIgnoreCase(suffix)) {
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
	private static String getExtension(File f) {
		String fileName = f.getName();
		if (
				fileName.indexOf('.') != -1
				&& fileName.lastIndexOf('.') != fileName.length() - 1
		) {
			int lastIndex = fileName.lastIndexOf('.');
			return fileName.substring(lastIndex + 1);
		}
		
		return null;
	}
	
	public String preferredOutputFormatName() {
		String fileExtension = getExtension(destinationFile);

		if (fileExtension != null) {
			Iterator<ImageReader> rIter = ImageIO.getImageReadersBySuffix(fileExtension);
			
			if (rIter.hasNext()) {
				try {
					return rIter.next().getFormatName();
				} catch (IOException e) {
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
	public void write(BufferedImage img) throws IOException {
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
		if (formatName != null && (fileExtension == null || !isMatchingFormat(formatName, fileExtension))) {
			destinationFile = new File(destinationFile.getAbsolutePath() + "." + formatName);
		}
		
		if (!allowOverwrite && destinationFile.exists()) {
			throw new IllegalArgumentException("The destination file exists.");
		}
		
		/*
		 * If a formatName is not specified, then attempt to determine it from
		 * the file extension.
		 */
		if (formatName == null && fileExtension != null) {
			Iterator<ImageReader> rIter = ImageIO.getImageReadersBySuffix(fileExtension);
			
			if (rIter.hasNext()) {
				formatName = rIter.next().getFormatName();
			}
		}
		
		if (formatName == null) {
			throw new UnsupportedFormatException(
					formatName,
					"Could not determine output format."
			);
		}

		OutputStream os = createOutputStream(destinationFile);
		imageSink = new OutputStreamImageSink(os);
		imageSink.setThumbnailParameter(param);
		imageSink.setOutputFormatName(formatName);
		try {
			imageSink.write(img);
		} finally {
			os.close();
		}
	}

	// Visible for testing only.
	OutputStream createOutputStream(File destinationFile) throws IOException {
		return new FileOutputStream(destinationFile);
	}

	/**
	 * Returns the destination file of the thumbnail image.
	 * <p>
	 * If the final destination of the thumbnail changes in the course of
	 * writing the thumbnail. (For example, if the file extension for the given
	 * destination did not match the destination file format, then the correct
	 * file extension could be appended.)
	 * 
	 * @return the destinationFile
	 */
	public File getSink() {
		return destinationFile;
	}
	
	public void setOutputFormatName(String format) {
		this.outputFormat = format;
		this.imageSink.setOutputFormatName(format);
	}

	public void setThumbnailParameter(ThumbnailParameter param) {
		this.param = param;
		this.imageSink.setThumbnailParameter(param);
	}
}
