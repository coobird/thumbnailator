package net.coobird.thumbnailator;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.filters.Pipeline;
import net.coobird.thumbnailator.filters.Rotation;
import net.coobird.thumbnailator.filters.Watermark;
import net.coobird.thumbnailator.makers.FixedSizeThumbnailMaker;
import net.coobird.thumbnailator.makers.ScaledThumbnailMaker;
import net.coobird.thumbnailator.makers.ThumbnailMaker;
import net.coobird.thumbnailator.resizers.BicubicResizer;
import net.coobird.thumbnailator.resizers.BilinearResizer;
import net.coobird.thumbnailator.resizers.ProgressiveBilinearResizer;
import net.coobird.thumbnailator.resizers.Resizer;
import net.coobird.thumbnailator.resizers.Resizers;
import net.coobird.thumbnailator.resizers.configurations.AlphaInterpolation;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import net.coobird.thumbnailator.resizers.configurations.Dithering;
import net.coobird.thumbnailator.resizers.configurations.Rendering;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;
import net.coobird.thumbnailator.tasks.FileThumbnailTask;

/**
 * This class provides a fluent interface to create thumbnails.
 * <DL>
 * <DT><B>Usage:</B></DT>
 * <DD>
 * The following example code demonstrates how to use the fluent interface
 * to create a thumbnail from multiple files from a directory, resizing them to
 * a maximum of 200 pixels by 200 pixels while preserving the aspect ratio of
 * the original, then saving the resulting thumbnails as JPEG images with file
 * names having {@code thumbnail.} appended to the beginning of the file name. 
 * <p>
 * <pre>
Thumbnails.of(directory.listFiles())
    .size(200, 200)
    .keepAspectRatio(true)
    .outputFormat("jpeg")
    .asFiles(Rename.PREFIX_DOT_THUMBNAIL);
 * </pre>
 * </DD>
 * </DL>
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
	 * @deprecated 		This class has been moved to the 
	 * 					{@link Rename} class, and is subject to removal
	 * 					in future versions of Thumbnailator.
	 * 
	 * @author coobird
	 *
	 */
	@Deprecated
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
	 * 
	 * @deprecated 		This constant has been moved to the 
	 * 					{@link net.coobird.thumbnailator.Rename} class, and is
	 * 					subject to removal in future versions of Thumbnailator.
	 */
	@Deprecated
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
	 * 
	 * @deprecated 		This constant has been moved to the 
	 * 					{@link net.coobird.thumbnailator.Rename} class, and is
	 * 					subject to removal in future versions of Thumbnailator.
	 */
	@Deprecated
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
	 * 
	 * @deprecated 		This constant has been moved to the 
	 * 					{@link net.coobird.thumbnailator.Rename} class, and is
	 * 					subject to removal in future versions of Thumbnailator.
	 */
	@Deprecated
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
	 * 
	 * @deprecated 		This constant has been moved to the 
	 * 					{@link net.coobird.thumbnailator.Rename} class, and is
	 * 					subject to removal in future versions of Thumbnailator.
	 */
	@Deprecated
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
	 * 
	 * @deprecated 		This constant has been moved to the 
	 * 					{@link net.coobird.thumbnailator.Rename} class, and is
	 * 					subject to removal in future versions of Thumbnailator.
	 */
	@Deprecated
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
	 * 
	 * @deprecated 		This method has been moved to the 
	 * 					{@link Thumbnailator} class, and is subject to removal
	 * 					in future versions of Thumbnailator.
	 */
	@Deprecated
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
	 * 
	 * @deprecated 		This method has been moved to the 
	 * 					{@link Thumbnailator} class, and is subject to removal
	 * 					in future versions of Thumbnailator.
	 */
	@Deprecated
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
	 * 
	 * @deprecated 		This method has been moved to the 
	 * 					{@link Thumbnailator} class, and is subject to removal
	 * 					in future versions of Thumbnailator.
	 */
	@Deprecated
	public static void createThumbnail(
			InputStream is,
			OutputStream os,
			int width,
			int height
	) throws IOException
	{
		Thumbnailator.createThumbnail(is, os, width, height);
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
	 * 
	 * @deprecated 		This method has been moved to the 
	 * 					{@link Thumbnailator} class, and is subject to removal
	 * 					in future versions of Thumbnailator.
	 */
	@Deprecated
	public static void createThumbnail(
			File inFile,
			File outFile,
			int width,
			int height
	) throws IOException
	{
		Thumbnailator.createThumbnail(inFile, outFile, width, height);
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
	 * 
	 * @deprecated 		This method has been moved to the 
	 * 					{@link Thumbnailator} class, and is subject to removal
	 * 					in future versions of Thumbnailator.
	 */
	@Deprecated
	public static BufferedImage createThumbnail(
			File f,
			int width,
			int height
	) throws IOException
	{
		return Thumbnailator.createThumbnail(f, width, height);
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
	 * 
	 * @deprecated 		This method has been moved to the 
	 * 					{@link Thumbnailator} class, and is subject to removal
	 * 					in future versions of Thumbnailator.
	 */
	@Deprecated
	public static BufferedImage createThumbnail(
			BufferedImage img, 
			int width, 
			int height
	)
	{
		return Thumbnailator.createThumbnail(img, width, height);
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
	 * 
	 * @deprecated 		This method has been moved to the 
	 * 					{@link Thumbnailator} class, and is subject to removal
	 * 					in future versions of Thumbnailator.
	 */
	@Deprecated
	public static Image createThumbnail(
			Image img, 
			int width, 
			int height
	)
	{
		return Thumbnailator.createThumbnail(img, width, height);
	}
	
	private static void checkForNull(Object o, String message)
	{
		if (o == null)
		{
			throw new NullPointerException(message);
		}
	}
	
	private static void checkForEmpty(Object[] o, String message)
	{
		if (o.length == 0)
		{
			throw new IllegalArgumentException(message);
		}
	}
	
	private static void checkForEmpty(Collection<?> o, String message)
	{
		if (o.size() == 0)
		{
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * Indicate to make thumbnails for images with the specified filenames.  
	 * 
	 * @param files		File names of image files for which thumbnails
	 * 					are to be produced for.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty array.
	 */
	public static Builder of(String... files)
	{
		checkForNull(files, "Cannot specify null for input files.");
		checkForEmpty(files, "Cannot specify an empty array for input files.");
		return new Builder(files);
	}
	
	/**
	 * Indicate to make thumbnails from the specified {@link File}s.  
	 * 
	 * @param files		{@link File} objects of image files for which thumbnails
	 * 					are to be produced for.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty array.
	 */
	public static Builder of(File... files)
	{
		checkForNull(files, "Cannot specify null for input files.");
		checkForEmpty(files, "Cannot specify an empty array for input files.");
		return new Builder(files);
	}
	
	/**
	 * Indicate to make thumbnails from the specified {@link BufferedImage}s.
	 * 
	 * @param images	{@link BufferedImage}s for which thumbnails
	 * 					are to be produced for.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty array.
	 */
	public static Builder of(BufferedImage... images)
	{
		checkForNull(images, "Cannot specify null for images.");
		checkForEmpty(images, "Cannot specify an empty array for images.");
		return new Builder(images);
	}

	/**
	 * Indicate to make thumbnails for images with the specified filenames.  
	 * 
	 * @param files		File names of image files for which thumbnails
	 * 					are to be produced for.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty collection.
	 */
	public static Builder fromFilenames(Collection<String> files)
	{
		checkForNull(files, "Cannot specify null for input files.");
		checkForEmpty(files, "Cannot specify an empty collection for input files.");
		return of(files.toArray(new String[files.size()]));
	}
	
	/**
	 * Indicate to make thumbnails from the specified {@link File}s.  
	 * 
	 * @param files		{@link File} objects of image files for which thumbnails
	 * 					are to be produced for.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty collection.
	 */
	public static Builder fromFiles(Collection<File> files)
	{
		checkForNull(files, "Cannot specify null for input files.");
		checkForEmpty(files, "Cannot specify an empty collection for input files.");
		return of(files.toArray(new File[files.size()]));
	}
	
	/**
	 * Indicate to make thumbnails from the specified {@link BufferedImage}s.
	 * 
	 * @param images	{@link BufferedImage}s for which thumbnails
	 * 					are to be produced for.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty collection.
	 */
	public static Builder fromImages(Collection<BufferedImage> images)
	{
		checkForNull(images, "Cannot specify null for images.");
		checkForEmpty(images, "Cannot specify an empty collection for images.");
		return of(images.toArray(new BufferedImage[images.size()]));
	}

	/**
	 * A builder interface for Thumbnailator.
	 * <p>
	 * An instance of this class is obtained by calling one of:
	 * <ul>
	 * <li>{@link Thumbnails#of(BufferedImage...)}</li>
	 * <li>{@link Thumbnails#of(File...)}</li>
	 * <li>{@link Thumbnails#of(String...)}</li>
	 * <li>{@link Thumbnails#fromImages(Collection)}</li>
	 * <li>{@link Thumbnails#fromFiles(Collection)}</li>
	 * <li>{@link Thumbnails#fromFilenames(Collection)}</li>
	 * </ul>
 	 * 
	 * @author coobird
	 *
	 */
	public static class Builder
	{
		private List<File> files = null;
		private List<BufferedImage> images = null;
		
		private Builder(String... filenames)
		{
			statusMap.put(Properties.OUTPUT_FORMAT, Status.OPTIONAL);
			files = new ArrayList<File>();
			for (String filename : filenames)
			{
				files.add(new File(filename));
			}
		}
		
		private Builder(File... files)
		{
			statusMap.put(Properties.OUTPUT_FORMAT, Status.OPTIONAL);
			this.files = Arrays.asList(files);
		}
		
		private Builder(BufferedImage... images)
		{
			statusMap.put(Properties.OUTPUT_FORMAT, Status.OPTIONAL);
			this.images = Arrays.asList(images);
		}

		/**
		 * Status of each property.
		 * 
		 * @author coobird
		 *
		 */
		private static enum Status
		{
			OPTIONAL,
			READY,
			NOT_READY,
			ALREADY_SET,
			CANNOT_SET,
		}

		/**
		 * Interface used by {@link Properties}.
		 * 
		 * @author coobird
		 *
		 */
		private static interface Property
		{
			public String getName();
		}

		/**
		 * Enum of properties which can be set by this builder.
		 * 
		 * @author coobird
		 *
		 */
		private static enum Properties implements Property
		{
			SIZE("size"),
			SCALE("scale"),
			IMAGE_TYPE("imageType"),
			SCALING_MODE("scalingMode"),
			ALPHA_INTERPOLATION("alphaInterpolation"),
			ANTIALIASING("antialiasing"),
			DITHERING("dithering"),
			RENDERING("rendering"),
			KEEP_ASPECT_RATIO("keepAspectRatio"),
			OUTPUT_FORMAT("outputFormat"),
			OUTPUT_FORMAT_TYPE("outputFormatType"),
			OUTPUT_QUALITY("outputQuality"),
			RESIZER("resizer"),
			;
			
			private final String name;
			
			private Properties(String name)
			{
				this.name = name;
			}
		
			public String getName()
			{
				return name;
			}
		}

		/**
		 * Map to keep track of whether a property has been properly set or not.
		 */
		private final Map<Properties, Status> statusMap = new HashMap<Properties, Status>();

		/**
		 * Populates the property map.
		 */
		{
			statusMap.put(Properties.SIZE, Status.NOT_READY);
			statusMap.put(Properties.SCALE, Status.NOT_READY);
			statusMap.put(Properties.IMAGE_TYPE, Status.OPTIONAL);
			statusMap.put(Properties.SCALING_MODE, Status.OPTIONAL);
			statusMap.put(Properties.ALPHA_INTERPOLATION, Status.OPTIONAL);
			statusMap.put(Properties.ANTIALIASING, Status.OPTIONAL);
			statusMap.put(Properties.DITHERING, Status.OPTIONAL);
			statusMap.put(Properties.RENDERING, Status.OPTIONAL);
			statusMap.put(Properties.KEEP_ASPECT_RATIO, Status.OPTIONAL);
			statusMap.put(Properties.OUTPUT_FORMAT, Status.OPTIONAL);
			statusMap.put(Properties.OUTPUT_FORMAT_TYPE, Status.OPTIONAL);
			statusMap.put(Properties.OUTPUT_QUALITY, Status.OPTIONAL);
			statusMap.put(Properties.RESIZER, Status.OPTIONAL);
		}

		/**
		 * Updates the property status map.
		 * 
		 * @param property		The property to update.
		 * @param newStatus		The new status.
		 */
		private void updateStatus(Properties property, Status newStatus)
		{
			if (statusMap.get(property) == Status.ALREADY_SET)
			{
				throw new IllegalStateException(
						property.getName() + " is already set.");
			}
			if (statusMap.get(property) == Status.CANNOT_SET)
			{
				throw new IllegalStateException(
						property.getName() + " cannot be set.");
			}
			
			statusMap.put(property, newStatus);
		}

		/**
		 * An constant used to indicate that the imageType has not been
		 * specified. When this constant is encountered, one should use the
		 * {@link ThumbnailParameter#DEFAULT_IMAGE_TYPE} as the value for
		 * imageType.
		 */
		private static int IMAGE_TYPE_UNSPECIFIED = -1;
		
		/*
		 * Defines the fields for the builder interface, and assigns the
		 * default values.
		 */
		private int width = -1;
		private int height = -1;
		private double scale = Double.NaN;
		
		private int imageType = IMAGE_TYPE_UNSPECIFIED;
		private boolean keepAspectRatio = true;
		
		private String outputFormat = ThumbnailParameter.ORIGINAL_FORMAT;
		private String outputFormatType = ThumbnailParameter.DEFAULT_FORMAT_TYPE;
		private float outputQuality = ThumbnailParameter.DEFAULT_QUALITY;
		
		private ScalingMode scalingMode = ScalingMode.PROGRESSIVE_BILINEAR;
		private AlphaInterpolation alphaInterpolation = AlphaInterpolation.DEFAULT;
		private Dithering dithering = Dithering.DEFAULT;
		private Antialiasing antialiasing = Antialiasing.DEFAULT;
		private Rendering rendering = Rendering.DEFAULT;
		
		private Resizer resizer = Resizers.PROGRESSIVE;
		
		/**
		 * The {@link ImageFilter}s that should be applied when creating the
		 * thumbnail.
		 */
		private Pipeline filterPipeline = new Pipeline();
		
		/**
		 * Sets the size of the thumbnail.
		 * <p>
		 * Once this method is called, calling the {@link #scale(double)} method
		 * will result in an {@link IllegalStateException}.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param width			The width of the thumbnail.
		 * @param height		The height of the thumbnail.
		 * @return				Reference to this object.
		 */
		public Builder size(int width, int height)
		{
			updateStatus(Properties.SIZE, Status.ALREADY_SET);
			updateStatus(Properties.SCALE, Status.CANNOT_SET);
			
			validateDimensions(width, height);
			this.width = width;
			this.height = height;
			
			return this;
		}
		
		/**
		 * Sets the scaling factor of the thumbnail.
		 * <p>
		 * Once this method is called, caling the {@link #size(int, int)} method
		 * and the {@link #keepAspectRatio(boolean)} will result in an
		 * {@link IllegalStateException}.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param scale			The scaling factor to use when creating a
		 * 						thumbnail.
		 * 						<p>
		 * 						The value must be a {@code double} which is
		 * 						greater than {@code 0.0}.
		 * @return				Reference to this object.
		 */
		public Builder scale(double scale)
		{
			updateStatus(Properties.SCALE, Status.ALREADY_SET);
			updateStatus(Properties.SIZE, Status.CANNOT_SET);
			updateStatus(Properties.KEEP_ASPECT_RATIO, Status.CANNOT_SET);
			
			if (scale <= 0)
			{
				throw new IllegalArgumentException(
						"The scaling factor is equal to or less than 0."
				);
			}
			
			this.scale = scale;
			
			return this;
		}
		
		/**
		 * Sets the image type of the thumbnail.
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param type			The image type of the thumbnail.
		 * @return				Reference to this object.
		 */
		public Builder imageType(int type)
		{
			updateStatus(Properties.IMAGE_TYPE, Status.ALREADY_SET);
			imageType = type;
			return this;
		}

		/**
		 * Sets the resizing scaling mode to use when creating the thumbnail.
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param config		The scaling mode to use.
		 * @return				Reference to this object.
		 */
		public Builder scalingMode(ScalingMode config)
		{
			checkForNull(config, "Scaling mode is null.");
			updateStatus(Properties.SCALING_MODE, Status.ALREADY_SET);
			updateStatus(Properties.RESIZER, Status.CANNOT_SET);
			scalingMode = config;
			return this;
		}
		
		/**
		 * Sets the resizing operation to use when creating the thumbnail.
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param resizer		The scaling operation to use.
		 * @return				Reference to this object.
		 */
		public Builder resizer(Resizer resizer)
		{
			checkForNull(resizer, "Resizer is null.");
			updateStatus(Properties.RESIZER, Status.ALREADY_SET);
			updateStatus(Properties.SCALING_MODE, Status.CANNOT_SET);
			this.resizer = resizer;
			return this;
		}
		
		/**
		 * Sets the alpha interpolation mode when performing the resizing
		 * operation to generate the thumbnail.
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param config		The alpha interpolation mode.
		 * @return				Reference to this object.
		 */
		public Builder alphaInterpolation(AlphaInterpolation config)
		{
			checkForNull(config, "Alpha interpolation is null.");
			updateStatus(Properties.ALPHA_INTERPOLATION, Status.ALREADY_SET);
			alphaInterpolation = config;
			return this;
		}

		/**
		 * Sets the dithering mode when performing the resizing
		 * operation to generate the thumbnail.
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param config		The dithering mode.
		 * @return				Reference to this object.
		 */
		public Builder dithering(Dithering config)
		{
			checkForNull(config, "Dithering is null.");
			updateStatus(Properties.DITHERING, Status.ALREADY_SET);
			dithering = config;
			return this;
		}
		
		/**
		 * Sets the antialiasing mode when performing the resizing
		 * operation to generate the thumbnail.
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException}.
		 * 
		 * @param config		The antialiasing mode.
		 * @return				Reference to this object.
		 */
		public Builder antialiasing(Antialiasing config)
		{
			checkForNull(config, "Antialiasing is null.");
			updateStatus(Properties.ANTIALIASING, Status.ALREADY_SET);
			antialiasing = config;
			return this;
		}
		
		/**
		 * Sets the rendering mode when performing the resizing
		 * operation to generate the thumbnail.
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param config		The rendering mode.
		 * @return				Reference to this object.
		 */
		public Builder rendering(Rendering config)
		{
			checkForNull(config, "Rendering is null.");
			updateStatus(Properties.RENDERING, Status.ALREADY_SET);
			rendering = config;
			return this;
		}
		
		/**
		 * Sets whether or not to keep the aspect ratio of the original image
		 * for the thumbnail.
		 * <p>
		 * Calling this method without first calling the {@link #size(int, int)}
		 * method will result in an {@link IllegalStateException} to be thrown.
		 * <p>
		 * If this method is not called when, by default the aspect ratio of
		 * the original image is preserved for the thumbnail.
		 * <p>
		 * Calling this method after calling the {@link #scale(double)} method
		 * will result in a {@link IllegalStateException}. 
		 * 
		 * @param keep			{@code true} if the thumbnail is to maintain
		 * 						the aspect ratio of the original image,
		 * 						{@code false} otherwise.
		 * @return				Reference to this object.
		 * 
		 * @throws IllegalStateException	If the {@link #size(int, int)} has
		 * 									not yet been called to specify the
		 * 									size of the thumbnail, or if
		 * 									the {@link #scale(double)} method
		 * 									has been called.
		 */
		public Builder keepAspectRatio(boolean keep)
		{
			if (statusMap.get(Properties.SCALE) == Status.ALREADY_SET)
			{
				throw new IllegalStateException("Cannot specify whether to " +
						"keep the aspect ratio if the scaling factor has " +
						"already been specified.");
			}
			if (statusMap.get(Properties.SIZE) != Status.ALREADY_SET)
			{
				throw new IllegalStateException("Cannot specify whether to " +
						"keep the aspect ratio unless the size parameter has " +
						"already been specified.");
			}
			
			updateStatus(Properties.KEEP_ASPECT_RATIO, Status.ALREADY_SET);
			keepAspectRatio = keep;
			return this;
		}
		
		/**
		 * Sets the compression output quality of the thumbnail.
		 * <p> 
		 * The value is a {@code float} between {@code 0.0f} and {@code 1.0f}
		 * where {@code 0.0f} indicates the minimum quality and {@code 1.0f}
		 * indicates the maximum quality settings should be used for by the
		 * compression codec. 
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * <p>
		 * Calling this method multiple times, or the 
		 * {@link #outputQuality(double)} in conjunction with this method will
		 * result in an {@link IllegalStateException} to be thrown.
		 * 
		 * @param quality		The quality of the 
		 * @return				Reference to this object.
		 * @throws IllegalArgumentException	If the argument is less than
		 * 									{@code 0.0f} or is greater than
		 * 									{@code 1.0f}.
		 */
		public Builder outputQuality(float quality)
		{
			if (quality < 0.0f || quality > 1.0f)
			{
				throw new IllegalArgumentException(
						"The quality setting must be in the range 0.0f and " +
						"1.0f, inclusive."
				);
			}
			updateStatus(Properties.OUTPUT_QUALITY, Status.ALREADY_SET);
			outputQuality = quality;
			return this;
		}
		
		/**
		 * Sets the compression output quality of the thumbnail.
		 * <p> 
		 * The value is a {@code double} between {@code 0.0d} and {@code 1.0d}
		 * where {@code 0.0d} indicates the minimum quality and {@code 1.0d}
		 * indicates the maximum quality settings should be used for by the
		 * compression codec. 
		 * <p>
		 * This method is a convenience method for {@link #outputQuality(float)}
		 * where the {@code double} argument type is accepted instead of a
		 * {@code float}.
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * <p>
		 * Calling this method multiple times, or the 
		 * {@link #outputQuality(float)} in conjunction with this method will
		 * result in an {@link IllegalStateException} to be thrown.
		 * 
		 * @param quality		The quality of the 
		 * @return				Reference to this object.
		 * @throws IllegalArgumentException	If the argument is less than
		 * 									{@code 0.0d} or is greater than
		 * 									{@code 1.0d}.
 		 */
		public Builder outputQuality(double quality)
		{
			if (quality < 0.0d || quality > 1.0d)
			{
				throw new IllegalArgumentException(
						"The quality setting must be in the range 0.0d and " +
						"1.0d, inclusive."
				);
			}
			
			updateStatus(Properties.OUTPUT_QUALITY, Status.ALREADY_SET);
			outputQuality = (float)quality;
			if (outputQuality < 0.0f)
			{
				outputQuality = 0.0f;
			}
			else if (outputQuality > 1.0f)
			{
				outputQuality = 1.0f;
			}
			return this;
		}
		
		/**
		 * Sets the compression format to use when writing the thumbnail.
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param format		The compression format.
		 * @return				Reference to this object.
		 * @throws IllegalArgumentException	If an unsupported format is
		 * 									specified.
		 */
		public Builder outputFormat(String format)
		{
			if (!ThumbnailatorUtils.isSupportedOutputFormat(format))
			{
				throw new IllegalArgumentException(
						"Specified format is not supported: " + format
				);
			}
			
			updateStatus(Properties.OUTPUT_FORMAT, Status.ALREADY_SET);
			outputFormat = format;
			return this;
		}
		
		/**
		 * Sets the compression format type of the thumbnail to write.
		 * <p>
		 * If the default type for the compression codec should be used, a 
		 * value of {@link ThumbnailParameter#DEFAULT_FORMAT_TYPE} should be
		 * used. 
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * <p>
		 * Furthermore, if this method is called, then calling the 
		 * {@link #outputFormat} method is disabled, in order to prevent
		 * cases where the output format type does not exist in the format
		 * specified for the {@code outputFormat} method.
		 *  
		 * @param formatType	The compression format type 
		 * @return				Reference to this object.
		 * @throws IllegalArgumentException	If an unsupported format type is
		 * 									specified for the current output
		 * 									format type. Or, if the output
		 * 									format has not been specified before
		 * 									this method was called.
		 */
		public Builder outputFormatType(String formatType)
		{
			/*
			 * If the output format is the original format, and the format type
			 * is being specified, it's going to be likely that the specified
			 * type will not be present in all the formats, so we'll disallow
			 * it. (e.g. setting type to "JPEG", and if the original formats
			 * were JPEG and PNG, then we'd have a problem. 
			 */
			if (formatType != ThumbnailParameter.DEFAULT_FORMAT_TYPE 
					&& outputFormat == ThumbnailParameter.ORIGINAL_FORMAT)
			{
				throw new IllegalArgumentException(
						"Cannot set the format type if a specific output " +
						"format has not been specified."
				);
			}
			
			if (!ThumbnailatorUtils.isSupportedOutputFormatType(outputFormat, formatType))
			{
				throw new IllegalArgumentException(
						"Specified format type (" + formatType + ") is not " +
						" supported for the format: " + outputFormat
				);
			}
			
			/*
			 * If the output format type is set, then we'd better make the
			 * output format unchangeable, or else we'd risk having a type
			 * that is not part of the output format.
			 */
			updateStatus(Properties.OUTPUT_FORMAT_TYPE, Status.ALREADY_SET);
			
			if (!statusMap.containsKey(Properties.OUTPUT_FORMAT))
			{
				updateStatus(Properties.OUTPUT_FORMAT, Status.CANNOT_SET);
			}
			outputFormatType = formatType;
			return this;
		}
		
		/**
		 * Sets the watermark to apply on the thumbnail.
		 * <p>
		 * This method can be called multiple times to apply multiple
		 * watermarks.
		 * <p>
		 * If multiple watermarks are to be applied, the watermarks will be
		 * applied in the order that this method is called.
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * 
		 * @param w				The watermark to apply to the thumbnail.
		 * @return				Reference to this object.
		 */
		public Builder watermark(Watermark w)
		{
			if (w == null)
			{
				throw new NullPointerException("Watermark is null.");
			}
			
			filterPipeline.add(w);
			
			return this;
		}
		
		/**
		 * Sets the image of the watermark to apply on the thumbnail.
		 * <p>
		 * This method is a convenience method for the 
		 * {@link #watermark(Position, BufferedImage, float)} method, where
		 * the opacity is 50%, and the position is set to center of the
		 * thumbnail:
		 * <p>
		 * <pre>
watermark(Positions.CENTER, image, 0.5f);
		 * </pre>
		 * This method can be called multiple times to apply multiple
		 * watermarks.
		 * <p>
		 * If multiple watermarks are to be applied, the watermarks will be
		 * applied in the order that this method is called.
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * 
		 * @param image			The image of the watermark.
		 * @return				Reference to this object.
		 */
		public Builder watermark(BufferedImage image)
		{
			return watermark(Positions.CENTER, image, 0.5f);
		}
		
		/**
		 * Sets the image and opacity of the watermark to apply on
		 * the thumbnail.
		 * <p>
		 * This method is a convenience method for the 
		 * {@link #watermark(Position, BufferedImage, float)} method, where
		 * the opacity is 50%:
		 * <p>
		 * <pre>
watermark(Positions.CENTER, image, opacity);
		 * </pre>
		 * This method can be called multiple times to apply multiple
		 * watermarks.
		 * <p>
		 * If multiple watermarks are to be applied, the watermarks will be
		 * applied in the order that this method is called.
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * 
		 * @param image			The image of the watermark.
		 * @param opacity		The opacity of the watermark.
		 * 						<p>
		 * 						The value should be between {@code 0.0f} and 
		 * 						{@code 1.0f}, where {@code 0.0f} is completely 
		 * 						transparent, and {@code 1.0f} is completely
		 * 						opaque.
		 * @return				Reference to this object.
		 */
		public Builder watermark(BufferedImage image, float opacity)
		{
			return watermark(Positions.CENTER, image, opacity);
		}
		
		/**
		 * Sets the image and opacity and position of the watermark to apply on
		 * the thumbnail.
		 * <p>
		 * This method can be called multiple times to apply multiple
		 * watermarks.
		 * <p>
		 * If multiple watermarks are to be applied, the watermarks will be
		 * applied in the order that this method is called.
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * 
		 * @param position		The position of the watermark.
		 * @param image			The image of the watermark.
		 * @param opacity		The opacity of the watermark.
		 * 						<p>
		 * 						The value should be between {@code 0.0f} and 
		 * 						{@code 1.0f}, where {@code 0.0f} is completely 
		 * 						transparent, and {@code 1.0f} is completely
		 * 						opaque.
		 * @return				Reference to this object.
		 */
		public Builder watermark(Position position, BufferedImage image, float opacity)
		{
			filterPipeline.add(new Watermark(position, image, opacity));
			return this;
		}
		
		/*
		 * rotation
		 */
		
		/**
		 * Sets the amount of rotation to apply to the thumbnail.
		 * <p>
		 * The thumbnail will be rotated clockwise by the angle specified.
		 * <p>
		 * This method can be called multiple times to apply multiple
		 * rotations.
		 * <p>
		 * If multiple rotations are to be applied, the rotations will be
		 * applied in the order that this method is called.
		 * <p>
		 * Calling this method to set this parameter is optional.
		 *  
		 * @param angle			Angle in degrees.
		 * @return				Reference to this object.
		 */
		public Builder rotate(double angle)
		{
			filterPipeline.add(Rotation.newRotator(angle));
			return this;
		}

		
		/*
		 * other filters
		 */
		
		/**
		 * Adds a {@link ImageFilter} to apply to the thumbnail.
		 * <p>
		 * This method can be called multiple times to apply multiple
		 * filters.
		 * <p>
		 * If multiple filters are to be applied, the filters will be
		 * applied in the order that this method is called.
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * 
		 * @param filter		An image filter to apply to the thumbnail.
		 * @return				Reference to this object.
		 */
		public Builder addFilter(ImageFilter filter)
		{
			if (filter == null)
			{
				throw new NullPointerException("Filter is null.");
			}
			
			filterPipeline.add(filter);
			return this;
		}
		
		/**
		 * Adds multiple {@link ImageFilter}s to apply to the thumbnail.
		 * <p>
		 * This method can be called multiple times to apply multiple
		 * filters.
		 * <p>
		 * If multiple filters are to be applied, the filters will be
		 * applied in the order that this method is called.
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * 
		 * @param filters		A list of filters to apply to the thumbnail.
		 * @return				Reference to this object.
		 */
		public Builder addFilters(List<ImageFilter> filters)
		{
			if (filters == null)
			{
				throw new NullPointerException("Filters is null.");
			}
			
			filterPipeline.addAll(filters);
			return this;
		}
		
		/**
		 * Checks whether the builder is ready to create thumbnails.
		 * 
		 * @throws IllegalStateException	If the builder is not ready to
		 * 									create thumbnails, due to some
		 * 									parameters not being set.
		 */
		private void checkReadiness()
		{
			for (Map.Entry<Properties, Status> s : statusMap.entrySet())
			{
				if (s.getValue() == Status.NOT_READY) {
					throw new IllegalStateException(s.getKey().getName() +
							" is not set.");
				}
			}
		}
		
		/**
		 * Returns a {@link Resizer} which is suitable for the current
		 * builder state.
		 * 
		 * @return			The {@link Resizer} which is suitable for the
		 * 					current builder state.
		 */
		private Resizer makeResizer()
		{
			/*
			 * If the scalingMode has been set, then use scalingMode to obtain
			 * a resizer, else, use the resizer field.
			 */
			if (statusMap.get(Properties.SCALING_MODE) == Status.ALREADY_SET)
			{
				return makeResizer(scalingMode);
			}
			else
			{
				return this.resizer;
			}
		}

		/**
		 * Returns a {@link Resizer} which is suitable for the current
		 * builder state.
		 * 
		 * @param mode		The scaling mode to use to create thumbnails.
		 * @return			The {@link Resizer} which is suitable for the
		 * 					specified scaling mode and builder state.
		 */
		private Resizer makeResizer(ScalingMode mode)
		{
			Map<RenderingHints.Key, Object> hints = 
				new HashMap<RenderingHints.Key, Object>();
			
			hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, alphaInterpolation.getValue());
			hints.put(RenderingHints.KEY_DITHERING, dithering.getValue());
			hints.put(RenderingHints.KEY_ANTIALIASING, antialiasing.getValue());
			hints.put(RenderingHints.KEY_RENDERING, rendering.getValue());
			
			if (mode == ScalingMode.BILINEAR)
			{
				return new BilinearResizer(hints);
			}
			else if (mode == ScalingMode.BICUBIC)
			{
				return new BicubicResizer(hints);
			}
			else if (mode == ScalingMode.PROGRESSIVE_BILINEAR)
			{
				return new ProgressiveBilinearResizer(hints);
			}
			else
			{
				return new ProgressiveBilinearResizer(hints);
			}
		}

		/**
		 * Returns a {@link ThumbnailMaker} which is appropriate for the
		 * currrent builder state.
		 * 
		 * @param r			The {@link Resizer} to use with the 
		 * 					{@link ThumbnailMaker}.
		 * @param imageType	The image type for the thumbnail.
		 * @return			The {@link ThumbnailMaker} which is suitable for
		 * 					the current builder state.
		 */
		private ThumbnailMaker makeThumbnailMaker(Resizer r, int imageType)
		{
			int imageTypeToUse = imageType;
			if (imageType == IMAGE_TYPE_UNSPECIFIED)
			{
				imageTypeToUse = ThumbnailParameter.DEFAULT_IMAGE_TYPE;
			}
			
			if (!Double.isNaN(scale))
			{
				return new ScaledThumbnailMaker(scale)
						.resizer(r)
						.imageType(imageTypeToUse);
			}
			else
			{
				return new FixedSizeThumbnailMaker(width, height, keepAspectRatio)
						.resizer(r)
						.imageType(imageTypeToUse);
			}
		}
		
		/**
		 * Returns a {@link ThumbnailParameter} from the current builder state.
		 * 
		 * @return			A {@link ThumbnailParameter} from the current
		 * 					builder state.
		 */
		private ThumbnailParameter makeParam()
		{
			Resizer resizer = makeResizer();
			
			int imageTypeToUse = imageType;
			if (imageType == IMAGE_TYPE_UNSPECIFIED)
			{
				imageTypeToUse = ThumbnailParameter.DEFAULT_IMAGE_TYPE;
			}
			
			if (Double.isNaN(scale))
			{
				return new ThumbnailParameter(
						new Dimension(width, height),
						keepAspectRatio,
						outputFormat,
						outputFormatType,
						outputQuality,
						imageTypeToUse,
						filterPipeline.getFilters(),
						resizer
				);
			}
			else
			{
				return new ThumbnailParameter(
						scale,
						keepAspectRatio,
						outputFormat,
						outputFormatType,
						outputQuality,
						imageTypeToUse,
						filterPipeline.getFilters(),
						resizer
				);
			}
		}

		/**
		 * Returns an {@link Iterable} which iterates over given files or
		 * images to return the original images from which the thumbnails
		 * should be made.
		 * 
		 * @return			An {@link Iterable} which provides the original
		 * 					images for which thumbnails should be made for. 
		 */
		private Iterable<BufferedImage> getOriginalImages()
		{
			class BufferedImageIterator implements Iterator<BufferedImage>
			{
				private final Iterator<BufferedImage> iter;
				
				public BufferedImageIterator(List<BufferedImage> images)
				{
					iter = images.iterator();
				}
		
				public boolean hasNext()
				{
					return iter.hasNext();
				}
		
				public BufferedImage next()
				{
					return iter.next();
				}
		
				public void remove()
				{
					throw new UnsupportedOperationException("Cannot remove elements from this iterator.");
				}
			}
			
			class FileIterator implements Iterator<BufferedImage>
			{
				private final Iterator<File> iter;
				
				public FileIterator(List<File> files)
				{
					iter = files.iterator();
				}
				
				public boolean hasNext()
				{
					return iter.hasNext();
				}
				
				public BufferedImage next()
				{
					try
					{
						return ImageIO.read(iter.next());
					}
					catch (IOException e)
					{
						throw new IllegalArgumentException("Could not open image file.", e);
					}
				}
				
				public void remove()
				{
					throw new UnsupportedOperationException(
							"Cannot remove elements from this iterator.");
				}
				
			}
			
			if (images != null)
			{
				return new Iterable<BufferedImage>() {
					public Iterator<BufferedImage> iterator()
					{
						return new BufferedImageIterator(images);
					}
				};
			}
			else if (files != null)
			{
				return new Iterable<BufferedImage>() {
					public Iterator<BufferedImage> iterator()
					{
						return new FileIterator(files);
					}
				};
			}
			else
			{
				throw new IllegalStateException(
						"No input source has been set.");
			}
		}

		/**
		 * Create the thumbnails and return as a {@link List} of 
		 * {@link BufferedImage}s.
		 * 
		 * @return		A list of thumbnails.
		 */
		public List<BufferedImage> asBufferedImages()
		{
			checkReadiness();
			Resizer r = makeResizer();
			
			List<BufferedImage> thumbnails = new ArrayList<BufferedImage>();
			
			// Create thumbnails
			/*
			 * TODO This code could cause very large images to cause
			 * OutOfMemoryErrors -- getOriginalImages will open each image at a  
			 * time, however, if each image is large, then it could deplete 
			 * the heap.
			 */
			for (BufferedImage img : getOriginalImages())
			{
				ThumbnailMaker maker = makeThumbnailMaker(r, img.getType());
				
				BufferedImage thumbnailImg = maker.make(img);
				
				// Apply image filters
				thumbnailImg = filterPipeline.apply(thumbnailImg);
				
				thumbnails.add(thumbnailImg);
			}
			
			return thumbnails;
		}
		
		/**
		 * Creates a thumbnail and returns it as a {@link BufferedImage}.
		 * <p>
		 * When multiple images are specified through one of the 
		 * {@link Thumbnails#of} methods, only the first image will be
		 * processed.
		 * 
		 * @return		A thumbnail as a {@link BufferedImage}.
		 * @throws IllegalArgumentException		If multiple original images are
		 * 										specified.
		 */
		public BufferedImage asBufferedImage()
		{
			checkReadiness();
			
			if (images.size() > 1)
			{
				throw new IllegalArgumentException("Cannot create one thumbnail from multiple original images.");
			}
			
			Resizer r = makeResizer();
			
			BufferedImage img = getOriginalImages().iterator().next();
			
			// Create thumbnails
			ThumbnailMaker maker = makeThumbnailMaker(r, img.getType());
			BufferedImage thumbnailImg = maker.make(img);
			
			// Apply image filters
			thumbnailImg = filterPipeline.apply(thumbnailImg);
			
			return thumbnailImg;
		}
		
		/*
		 * TODO A method which will accept a function used to generate names.
		 * Perhaps an Iterator which creates names.
		 * 
		 * This way, the source can be a list of BufferedImages, and the
		 * thumbnails can be output to files.
		 */
		
		/**
		 * Creates the thumbnails and stores them to the files, using the 
		 * {@code Rename} function to determine the filenames. The files
		 * are returned as {@link List}.
		 * <p>
		 * To call this method, the thumbnails must have been creates from
		 * files by calling the {@link Thumbnails#of(File...)} method.
		 * 
		 * @param rename			The rename function which is used to
		 * 							determine the filenames of the thumbnail
		 * 							files to write.
		 * @return					A list of {@link File}s of the thumbnails
		 * 							which were created.
		 * 
		 * @throws IOException		If a problem occurs while writing the
		 * 							thumbnails to files. 
		 * @throws IllegalStateException		If the original images are not
		 * 										from files.
		 */
		public List<File> asFiles(net.coobird.thumbnailator.Rename rename) throws IOException
		{
			checkReadiness();
			
			if (files == null)
			{
				throw new IllegalStateException("Cannot create thumbnails to files if original images are not from files.");
			}
			
			if (rename == null)
			{
				throw new NullPointerException("Rename is null.");
			}

			List<File> destinationFiles = new ArrayList<File>();
			
			
			ThumbnailParameter param = makeParam();
			
			for (File f : files)
			{
				File destinationFile = 
					new File(f.getParent(), rename.apply(f.getName()));
				
				destinationFiles.add(destinationFile);
				
				Thumbnailator.createThumbnail(new FileThumbnailTask(param, f, destinationFile));
			}
			
			return destinationFiles;
		}

		/**
		 * Creates the thumbnails and stores them to the files, using the 
		 * {@code Rename} function to determine the filenames.
		 * <p>
		 * To call this method, the thumbnails must have been creates from
		 * files by calling the {@link Thumbnails#of(File...)} method.
		 * 
		 * @param rename			The rename function which is used to
		 * 							determine the filenames of the thumbnail
		 * 							files to write.
		 * 
		 * @throws IOException		If a problem occurs while writing the
		 * 							thumbnails to files. 
		 * @throws IllegalStateException		If the original images are not
		 * 										from files.
		 */
		public void toFiles(net.coobird.thumbnailator.Rename rename) throws IOException
		{
			asFiles(rename);
		}

		/**
		 * Create a thumbnail and writes it to a {@link File}.
		 * <p>
		 * To call this method, the thumbnail must have been created from a
		 * single {@link File} by calling the {@link Thumbnails#of(File...)}
		 * method.
		 * 
		 * @param outFile			The file to which the thumbnail is to be
		 * 							written to.
		 * 
		 * @throws IOException		If a problem occurs while writing the
		 * 							thumbnails to files. 
		 * @throws IllegalStateException		If the original images are not
		 * 										from files.
		 * @throws IllegalArgumentException		If multiple original image files
		 * 										are	specified.
		 */
		public void toFile(File outFile) throws IOException
		{
			checkReadiness();
			
			if (files == null)
			{
				throw new IllegalStateException("Cannot create thumbnails to files if original images are not from files.");
			}
			else if (files.size() > 1)
			{
				throw new IllegalArgumentException("Cannot output multiple thumbnails to one file.");
			}
			
			ThumbnailParameter param = makeParam();
			
			Thumbnailator.createThumbnail(new FileThumbnailTask(param, files.get(0), outFile));
		}
	}
}