package net.coobird.thumbnailator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.filters.Watermark;
import net.coobird.thumbnailator.makers.FixedSizeThumbnailMaker;
import net.coobird.thumbnailator.resizers.ResizerFactory;
import net.coobird.thumbnailator.tasks.FileThumbnailTask;
import net.coobird.thumbnailator.tasks.StreamThumbnailTask;

/**
 * This class provides static utility methods which perform generation of
 * thumbnails using Thumbnailator.
 * <p>
 * When images are resized, the aspect ratio of the images are preserved.
 * <p>
 * Note: This class does not provide good support for large images.
 * For very large images, it is possible for an {@link OutOfMemoryError} to
 * occur during processing. 
 * 
 * @author coobird
 *
 */
public final class Thumbnails
{
	/**
	 * This class is not intended to be instantiated.
	 */
	private Thumbnails() {}
	
	/**
	 * This class is used to rename file names.
	 * 
	 * @author coobird
	 *
	 */
	public abstract static class Rename
	{
		/**
		 * The default constructor is intended only to be called implicitly
		 * by the classes implementing the functionality of the {@link Rename}
		 * class.
		 */
		protected Rename() {}
		
		/**
		 * Applies the function performed by this {@code Rename} on the
		 * specified name.
		 * 
		 * @param name			Name to apply the function on.
		 * @return				The name after the function has been applied.
		 */
		public abstract String apply(String name);
		
		/**
		 * Appends a suffix to a filename.
		 * 
		 * @param fileName		File name to add a suffix on.
		 * @param suffix		The suffix to add.
		 * @return				File name with specified suffixed affixed.
		 */
		private static String appendSuffix(String fileName, String suffix)
		{
			String newFileName = "";
			
			int indexOfDot = fileName.indexOf('.');
			
			if (indexOfDot != -1)
			{
				newFileName = fileName.substring(0, indexOfDot);
				newFileName += suffix;
				newFileName += fileName.substring(indexOfDot);
			}
			else
			{
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
		private static String appendPrefix(String fileName, String prefix)
		{
			return prefix + fileName;
		}
	}
	
	/**
	 * Appends {@code thumbnail-} to the beginning of the file name.
	 * <p>
	 * <dt>Example</dt>
	 * <ul>
	 * <li>Before: {@code picture.jpg}</li>
	 * <li>After: {@code thumbnail-picture.jpg}</li>
	 * </ul>
	 */
	public static final Rename PREFIX_HYPTHEN_THUMBNAIL_RENAME = new Rename() {
		@Override
		public String apply(String fileName)
		{
			return Thumbnails.Rename.appendPrefix(fileName, "thumbnail-");
		}
	};
	
	/**
	 * Appends {@code -thumbnail} to the file name prior to the extension of
	 * the file.
	 * <p>
	 * <dt>Example</dt>
	 * <ul>
	 * <li>Before: {@code picture.jpg}</li>
	 * <li>After: {@code picture-thumbnail.jpg}</li>
	 * </ul>
	 */
	public static final Rename SUFFIX_HYPTHEN_THUMBNAIL_RENAME = new Rename() {
		@Override
		public String apply(String fileName)
		{
			return Thumbnails.Rename.appendSuffix(fileName, "-thumbnail");
		}
	};
	
	/**
	 * Appends {@code .thumbnail} to the file name prior to the extension of
	 * the file.
	 * <p>
	 * <dt>Example</dt>
	 * <ul>
	 * <li>Before: {@code picture.jpg}</li>
	 * <li>After: {@code picture.thumbnail.jpg}</li>
	 * </ul>
	 */
	public static final Rename SUFFIX_DOT_THUMBNAIL_RENAME = new Rename() {
		@Override
		public String apply(String fileName)
		{
			return Thumbnails.Rename.appendSuffix(fileName, ".thumbnail");
		}
	};
	
	/**
	 * Appends {@code thumbnail.} to the beginning of the file name.
	 * <p>
	 * <dt>Example</dt>
	 * <ul>
	 * <li>Before: {@code picture.jpg}</li>
	 * <li>After: {@code thumbnail.picture.jpg}</li>
	 * </ul>
	 */
	public static final Rename PREFIX_DOT_THUMBNAIL_RENAME = new Rename() {
		@Override
		public String apply(String fileName)
		{
			return Thumbnails.Rename.appendPrefix(fileName, "thumbnail.");
		}
	};
	
	/**
	 * A {@code Rename} which does not alter the given file name.
	 * <p>
	 * <dt>Example</dt>
	 * <ul>
	 * <li>Before: {@code picture.jpg}</li>
	 * <li>After: {@code picture.jpg}</li>
	 * </ul>
	 */
	public static final Rename NULL_RENAME = new Rename() {
		@Override
		public String apply(String name)
		{
			return name;
		}
	};
	
	/**
	 * Performs validation on the specified dimensions.
	 * <p>
	 * If any of the dimensions are less than or equal to 0, an 
	 * {@code IllegalArgumentException} is thrown with an message specifying the
	 * reason for the exception.
	 * <p>
	 * This method is used to perform a check on the output dimensions of a
	 * thumbnail for the {@link Thumbnails#createThumbnail} methods.
	 * 
	 * @param width		The width to validate.
	 * @param height	The height to validate.
	 */
	private static void validateDimensions(int width, int height)
	{
		if (width <= 0 && height <= 0)
		{
			throw new IllegalArgumentException(
					"Destination image dimensions must not be less than " +
					"0 pixels."
			);
		}
		else if (width <= 0 || height <= 0)
		{
			String dimension = width == 0 ? "width" : "height";
			
			throw new IllegalArgumentException(
					"Destination image " + dimension + " must not be " +
					"less than or equal to 0 pixels."
			);
		}		
	}
	
	/**
	 * Creates thumbnails from a specified {@link Collection} of {@link File}s.
	 * The filenames of the resulting thumbnails are determined by applying  
	 * the specified {@link Rename}.
	 * <p>
	 * The order of the thumbnail {@code File}s in the returned 
	 * {@code Collection} will be the same as the order as the source list.
	 * 
	 * @param files			A {@code Collection} containing {@code File} objects
	 * 						of image files. 
	 * @param rename		The renaming function to use.
	 * @param width			The width of the thumbnail.
	 * @param height		The height of the thumbnail.
	 * @throws IOException	Thrown when a problem occurs when reading from 
	 * 						{@code File} representing an image file. 						
	 */
	public static Collection<File> createThumbnailCollection(
			Collection<? extends File> files,
			Rename rename,
			int width,
			int height
	) 
	throws IOException
	{
		validateDimensions(width, height);
		
		if (files == null)
		{
			throw new NullPointerException("Collection of Files is null.");
		}
		if (rename == null)
		{
			throw new NullPointerException("Rename is null.");
		}
		
		ArrayList<File> resultFiles = new ArrayList<File>();
		
		for (File inFile : files)
		{
			File outFile = 
				new File(inFile.getParent(), rename.apply(inFile.getName()));
			
			createThumbnail(inFile, outFile, width, height);
			
			resultFiles.add(outFile);
		}
		
		return Collections.unmodifiableList(resultFiles);
	}
	
	/**
	 * Creates thumbnails from a specified {@code Collection} of {@code File}s.
	 * The filenames of the resulting thumbnails are determined by applying  
	 * the specified {@code Rename} function. 
	 * 
	 * @param files			A {@code Collection} containing {@code File} objects
	 * 						of image files. 
	 * @param rename		The renaming function to use.
	 * @param width			The width of the thumbnail.
	 * @param height		The height of the thumbnail.
	 * @throws IOException	Thrown when a problem occurs when reading from 
	 * 						{@code File} representing an image file.
	 */
	public static void createThumbnails(
			Collection<? extends File> files,
			Rename rename,
			int width,
			int height
	) 
	throws IOException
	{
		validateDimensions(width, height);
		
		if (files == null)
		{
			throw new NullPointerException("Collection of Files is null.");
		}
		if (rename == null)
		{
			throw new NullPointerException("Rename is null.");
		}
		
		for (File inFile : files)
		{
			File outFile = 
				new File(inFile.getParent(), rename.apply(inFile.getName()));
			
			createThumbnail(inFile, outFile, width, height);
		}
	}
	
	
	/**
	 * Creates a thumbnail from image data streamed from an {@link InputStream}
	 * and streams the data out to an {@link OutputStream}. 
	 * 
	 * @param is			The {@link InputStream} from which to obtain
	 * 						image data.
	 * @param os			The {@link OutputStream} to send thumbnail data to.
	 * @param width			The width of the thumbnail.
	 * @param height		The height of the thumbnail.
	 * @throws IOException	Thrown when a problem occurs when reading from 
	 * 						{@code File} representing an image file.
	 */
	public static void createThumbnail(
			InputStream is,
			OutputStream os,
			int width,
			int height
	) throws IOException
	{
		validateDimensions(width, height);
		
		if (is == null)
		{
			throw new NullPointerException("InputStream is null.");
		} 
		else if (os == null)
		{
			throw new NullPointerException("OutputStream is null.");
		}
		
		ThumbnailParameter param = 
			new ThumbnailParameter(
					new Dimension(width, height),
					Collections.<Watermark>emptyList(),
					true,
					ThumbnailParameter.ORIGINAL_FORMAT,
					ThumbnailParameter.DEFAULT_QUALITY,
					BufferedImage.TYPE_INT_ARGB
			);
		
		Thumbnailator.createThumbnail(new StreamThumbnailTask(param, is, os));
	}
	
	/**
	 * Creates a thumbnail from an source image and writes the thumbnail to
	 * a destination file.
	 * <p>
	 * The image format to use for the thumbnail will be determined from the
	 * file extension. However, if the image format cannot be determined, then,
	 * the same image format as the original image will be used when writing
	 * the thumbnail. 
	 * 
	 * 
	 * @param inFile		The {@link File} from which image data is read.
	 * @param outFile		The {@link File} to which thumbnail is written.
	 * @param width			The width of the thumbnail.
	 * @param height		The height of the thumbnail.
	 * @throws IOException	Thrown when a problem occurs when reading from 
	 * 						{@code File} representing an image file.
	 */
	public static void createThumbnail(
			File inFile,
			File outFile,
			int width,
			int height
	) throws IOException
	{
		validateDimensions(width, height);
		
		if (inFile == null)
		{
			throw new NullPointerException("Input file is null.");
		}
		else if (outFile == null)
		{
			throw new NullPointerException("Output file is null.");
		}
		
		if (!inFile.exists())
		{
			throw new IOException("Input file does not exist.");
		}

		/*
		 * Determine the output file format.
		 * 
		 * Check to be sure the format is supported, and if not, then use
		 * the original file format.
		 */
		String fileName = outFile.getName();
		String fileExtension = null; 
		if (
				fileName.contains(".") 
				&& fileName.lastIndexOf('.') != fileName.length() - 1
		)
		{
			int lastIndex = fileName.lastIndexOf('.');
			fileExtension = fileName.substring(lastIndex + 1); 
		}
			
		String format = ThumbnailParameter.ORIGINAL_FORMAT;
		if (fileExtension != null)
		{
			for (String supportedFormatName : ImageIO.getWriterFormatNames())
			{
				if (supportedFormatName.equals(fileExtension))
				{
					format = supportedFormatName;
					break;
				}
			}
		}
		
		ThumbnailParameter param = 
			new ThumbnailParameter(
					new Dimension(width, height),
					Collections.<Watermark>emptyList(),
					true,
					format,
					ThumbnailParameter.DEFAULT_QUALITY,
					BufferedImage.TYPE_INT_ARGB
			);
		
		Thumbnailator.createThumbnail(
				new FileThumbnailTask(param, inFile, outFile));
	}
	
	/**
	 * Creates a thumbnail from an image file, and returns as a 
	 * {@link BufferedImage}.
	 * 
	 * @param f				The {@link File} from which image data is read.
	 * @param width			The width of the thumbnail.
	 * @param height		The height of the thumbnail.
	 * @return				The thumbnail image as a {@link BufferedImage}.
	 * @throws IOException	Thrown when a problem occurs when reading from 
	 * 						{@code File} representing an image file.
	 */
	public static BufferedImage createThumbnail(
			File f,
			int width,
			int height
	) throws IOException
	{
		validateDimensions(width, height);
		
		if (f == null)
		{
			throw new NullPointerException("Input file is null.");
		}
		
		return createThumbnail(ImageIO.read(f), width, height);
	}
	
	/**
	 * Creates a thumbnail.
	 * <p>
	 * The resulting thumbnail uses the default image type.
	 * <p>
	 * When the image is resized, the aspect ratio will be preserved.
	 * <p>
	 * When the specified dimensions does not have the same aspect ratio as the
	 * source image, the specified dimensions will be used as the absolute
	 * boundary of the thumbnail.
	 * <p>
	 * For example, if the source image of 100 pixels by 100 pixels, and the 
	 * desired thumbnail size is 50 pixels by 100 pixels, then the resulting 
	 * thumbnail will be 50 pixels by 50 pixels, as the constraint will be
	 * 50 pixels for the width, and therefore, by preserving the aspect ratio,
	 * the height will be required to be 50 pixels.  
	 * </p>
	 * 
	 * @param img				The source image.
	 * @param width				The width of the thumbnail.
	 * @param height			The height of the thumbnail.
	 * @return					Resulting thumbnail.
	 */
	public static BufferedImage createThumbnail(
			BufferedImage img, 
			int width, 
			int height
	)
	{
		validateDimensions(width, height);
		
		Dimension imgSize = new Dimension(img.getWidth(), img.getHeight());
		Dimension thumbnailSize = new Dimension(width, height);
		
		BufferedImage thumbnailImage = 
			new FixedSizeThumbnailMaker(width, height, true)
					.resizer(ResizerFactory.getResizer(imgSize, thumbnailSize))
					.make(img); 
		
		return thumbnailImage;
	}
	
	/**
	 * Creates a thumbnail from an {@link Image}.
	 * <p>
	 * The resulting {@link BufferedImage} uses the default image type.
	 * <p>
	 * When the image is resized, the aspect ratio will be preserved.
	 * <p>
	 * When the specified dimensions does not have the same aspect ratio as the
	 * source image, the specified dimensions will be used as the absolute
	 * boundary of the thumbnail.
	 * 
	 * @param img			The source image.
	 * @param width			The width of the thumbnail.
	 * @param height		The height of the thumbnail.
	 * @return				The thumbnail image as an {@link Image}.
	 */
	public static Image createThumbnail(
			Image img, 
			int width, 
			int height
	)
	{
		validateDimensions(width, height);
		
		// Copy the image from Image into a new BufferedImage.
		BufferedImage srcImg =
			new BufferedImageBuilder(
					img.getWidth(null),
					img.getHeight(null)
			).build();
		
		Graphics g = srcImg.createGraphics();
		g.drawImage(img, width, height, null);
		g.dispose();
		
		return createThumbnail(srcImg, width, height);
	}
}
