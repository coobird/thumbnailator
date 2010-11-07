package net.coobird.thumbnailator.concurrent;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.concurrent.Callable;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Rename;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.tasks.ThumbnailTask;

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
public class ConcurrentThumbnailator
{
	/**
	 * This class is not intended to be instantiated.
	 */
	private ConcurrentThumbnailator() {}
	
	/**
	 * Creates a thumbnail from parameters specified in a {@link ThumbnailTask}.
	 * 
	 * @param task				A {@link ThumbnailTask} to execute.
	 * @throws IOException		Thrown when a problem occurs when creating a
	 * 							thumbnail.
	 */
	public static Callable<Void> createThumbnail(final ThumbnailTask task)
	{
		if (task == null)
		{
			throw new NullPointerException("The task is null.");
		}
		
		return new Callable<Void>() {
			public Void call() throws Exception
			{
				Thumbnailator.createThumbnail(task);
				return null;
			}
		};
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
	public static Callable<BufferedImage> createThumbnail(
			final BufferedImage img, 
			final int width, 
			final int height
	)
	{
		validateDimensions(width, height);
		
		return new Callable<BufferedImage>() {
			public BufferedImage call() throws Exception
			{
				return Thumbnailator.createThumbnail(img, width, height);
			}
		};
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
	public static Callable<Void> createThumbnail(
			final File inFile,
			final File outFile,
			final int width,
			final int height
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

		return new Callable<Void>() {
			public Void call() throws Exception
			{
				Thumbnailator.createThumbnail(inFile, outFile, width, height);
				return null;
			}
		};
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
	public static Callable<BufferedImage> createThumbnail(
			final File f,
			final int width,
			final int height
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
	public static Callable<Image> createThumbnail(
			final Image img, 
			final int width, 
			final int height
	)
	{
		validateDimensions(width, height);
		
		return new Callable<Image>() {
			public Image call() throws Exception
			{
				return Thumbnailator.createThumbnail(img, width, height);
			}
		};
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
	public static Callable<Void> createThumbnail(
			final InputStream is,
			final OutputStream os,
			final int width,
			final int height
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
		
		return new Callable<Void>() {
			public Void call() throws Exception
			{
				Thumbnailator.createThumbnail(is, os, width, height);
				return null;
			}
		};
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
	 */
	public static Callable<Collection<File>> createThumbnailsAsCollection(
			final Collection<? extends File> files,
			final Rename rename,
			final int width,
			final int height
	) 
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
		
		return new Callable<Collection<File>>() {
			public Collection<File> call() throws Exception
			{
				return Thumbnailator.createThumbnailsAsCollection(
						files, rename, width, height
				);
			}
		};
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
	 */
	public static Callable<Void> createThumbnails(
			final Collection<? extends File> files,
			final Rename rename,
			final int width,
			final int height
	) 
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
		
		return new Callable<Void>() {
			public Void call() throws Exception
			{
				Thumbnailator.createThumbnails(files, rename, width, height);
				return null;
			}
		};
	}

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
}