package net.coobird.thumbnailator;

import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.filters.Pipeline;
import net.coobird.thumbnailator.filters.Rotation;
import net.coobird.thumbnailator.filters.Watermark;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.name.Rename;
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
import net.coobird.thumbnailator.tasks.SourceSinkThumbnailTask;
import net.coobird.thumbnailator.tasks.io.BufferedImageSink;
import net.coobird.thumbnailator.tasks.io.BufferedImageSource;
import net.coobird.thumbnailator.tasks.io.FileImageSink;
import net.coobird.thumbnailator.tasks.io.FileImageSource;
import net.coobird.thumbnailator.tasks.io.ImageSource;
import net.coobird.thumbnailator.tasks.io.OutputStreamImageSink;

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
	public static Builder<File> of(String... files)
	{
		checkForNull(files, "Cannot specify null for input files.");
		checkForEmpty(files, "Cannot specify an empty array for input files.");
		return Builder.of(files);
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
	public static Builder<File> of(File... files)
	{
		checkForNull(files, "Cannot specify null for input files.");
		checkForEmpty(files, "Cannot specify an empty array for input files.");
		return Builder.of(files);
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
	public static Builder<BufferedImage> of(BufferedImage... images)
	{
		checkForNull(images, "Cannot specify null for images.");
		checkForEmpty(images, "Cannot specify an empty array for images.");
		return Builder.of(images);
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
	public static Builder<File> fromFilenames(Collection<String> files)
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
	public static Builder<File> fromFiles(Collection<File> files)
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
	public static Builder<BufferedImage> fromImages(Collection<BufferedImage> images)
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
	public static class Builder<T>
	{
		private final List<ImageSource<T>> sources;
		
		private Builder(List<ImageSource<T>> sources)
		{
			this.sources = sources;
			statusMap.put(Properties.OUTPUT_FORMAT, Status.OPTIONAL);
		}
		
		private static Builder<File> of(String... filenames)
		{
			List<ImageSource<File>> sources = new ArrayList<ImageSource<File>>();
			for (String f : filenames)
			{
				sources.add(new FileImageSource(f));
			}
			
			return new Builder<File>(sources);
		}
		
		private static Builder<File> of(File... files)
		{
			List<ImageSource<File>> sources = new ArrayList<ImageSource<File>>();
			for (File f : files)
			{
				sources.add(new FileImageSource(f));
			}
			
			return new Builder<File>(sources);
		}
		
		private static Builder<BufferedImage> of(BufferedImage... images)
		{
			List<ImageSource<BufferedImage>> sources = new ArrayList<ImageSource<BufferedImage>>();
			for (BufferedImage img : images)
			{
				sources.add(new BufferedImageSource(img));
			}
			
			return new Builder<BufferedImage>(sources);
		}

		private final class BufferedImageIterable implements
				Iterable<BufferedImage>
		{
			public Iterator<BufferedImage> iterator()
			{
				return new Iterator<BufferedImage>() {
					
					Iterator<ImageSource<T>> sourceIter = sources.iterator();

					public boolean hasNext()
					{
						return sourceIter.hasNext();
					}

					public BufferedImage next()
					{
						ImageSource<T> source = sourceIter.next();
						BufferedImageSink destination = new BufferedImageSink();
						
						try
						{
							Thumbnailator.createThumbnail(
									new SourceSinkThumbnailTask<T, BufferedImage>(makeParam(), source, destination)
							);
						}
						catch (IOException e)
						{
							return null;
						}
						
						return destination.getSink();
					}

					public void remove()
					{
						throw new UnsupportedOperationException(
								"Cannot remove elements from this iterator."
						);
					}
				};
			}
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
		public Builder<T> size(int width, int height)
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
		public Builder<T> scale(double scale)
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
		public Builder<T> imageType(int type)
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
		public Builder<T> scalingMode(ScalingMode config)
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
		public Builder<T> resizer(Resizer resizer)
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
		public Builder<T> alphaInterpolation(AlphaInterpolation config)
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
		public Builder<T> dithering(Dithering config)
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
		public Builder<T> antialiasing(Antialiasing config)
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
		public Builder<T> rendering(Rendering config)
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
		public Builder<T> keepAspectRatio(boolean keep)
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
		public Builder<T> outputQuality(float quality)
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
		public Builder<T> outputQuality(double quality)
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
		public Builder<T> outputFormat(String format)
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
		public Builder<T> outputFormatType(String formatType)
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
		public Builder<T> watermark(Watermark w)
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
		public Builder<T> watermark(BufferedImage image)
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
		public Builder<T> watermark(BufferedImage image, float opacity)
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
		public Builder<T> watermark(Position position, BufferedImage image, float opacity)
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
		public Builder<T> rotate(double angle)
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
		public Builder<T> addFilter(ImageFilter filter)
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
		public Builder<T> addFilters(List<ImageFilter> filters)
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
				imageTypeToUse = ThumbnailParameter.ORIGINAL_IMAGE_TYPE;
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
		 * Create the thumbnails and return as a {@link Iterable} of 
		 * {@link BufferedImage}s.
		 * <p>
		 * For situations where multiple thumbnails are being generated, this
		 * method is preferred over the {@link #asBufferedImages()} method,
		 * as (1) the processing does not have to complete before the method
		 * returns and (2) the thumbnails can be retrieved one at a time,
		 * potentially reducing the number of thumbnails which need to be 
		 * retained in the heap memory, potentially reducing the chance of 
		 * {@link OutOfMemoryError}s from occurring.  
		 * <p>
		 * If an {@link IOException} occurs during the processing of the
		 * thumbnail, the {@link Iterable} will return a {@code null} for that
		 * element.
		 * 
		 * @return		An {@link Iterable} which will provide an 
		 * 				{@link Iterator} which returns thumbnails as
		 * 				{@link BufferedImage}s.
		 */
		public Iterable<BufferedImage> iterableBufferedImages()
		{
			checkReadiness();
			/*
			 * TODO To get the precise error information, there would have to
			 * be an event notification mechanism.
			 */
			return new BufferedImageIterable();
		}
		
		/**
		 * Create the thumbnails and return as a {@link List} of 
		 * {@link BufferedImage}s.
		 * <p>
		 * <h3>Note about performance</h3>
		 * If there are many thumbnails generated at once, it is possible that
		 * the Java virtual machine's heap space will run out and an
		 * {@link OutOfMemoryError} could result.
		 * <p>
		 * If many thumbnails are being processed at once, then using the
		 * {@link #iterableBufferedImages()} method would be preferable. 
		 * 
		 * @return		A list of thumbnails.
		 * @throws IOException					If an problem occurred during
		 * 										the reading of the original
		 * 										images.
		 */
		public List<BufferedImage> asBufferedImages() throws IOException
		{
			checkReadiness();
			
			List<BufferedImage> thumbnails = new ArrayList<BufferedImage>();
			
			// Create thumbnails
			for (ImageSource<T> source : sources)
			{
				BufferedImageSink destination = new BufferedImageSink();
				
				Thumbnailator.createThumbnail(
					new SourceSinkThumbnailTask<T, BufferedImage>(makeParam(), source, destination)
				);
				
				thumbnails.add(destination.getSink());
			}
			
			return thumbnails;
		}
		
		/**
		 * Creates a thumbnail and returns it as a {@link BufferedImage}.
		 * <p>
		 * To call this method, the thumbnail must have been created from a
		 * single source.
		 * 
		 * @return		A thumbnail as a {@link BufferedImage}.
		 * @throws IOException					If an problem occurred during
		 * 										the reading of the original
		 * 										image.
		 * @throws IllegalArgumentException		If multiple original images are
		 * 										specified.
		 */
		public BufferedImage asBufferedImage() throws IOException
		{
			checkReadiness();
			
			if (sources.size() > 1)
			{
				throw new IllegalArgumentException("Cannot create one thumbnail from multiple original images.");
			}
			
			BufferedImageSink destination = new BufferedImageSink();
			
			Thumbnailator.createThumbnail(
				new SourceSinkThumbnailTask<T, BufferedImage>(makeParam(), sources.get(0), destination)
			);
				
			return destination.getSink();
		}
		
		/**
		 * Creates the thumbnails and stores them to the files, and returns
		 * a {@link List} of {@link File}s to the thumbnails. 
		 * <p>
		 * The file names for the thumbnails are obtained from the given
		 * {@link Iterable}.
		 * 
		 * @param iterable			An {@link Iterable} which returns an
		 * 							{@link Iterator} which returns file names
		 * 							which should be assigned to each thumbnail.
		 * @return					A list of {@link File}s of the thumbnails
		 * 							which were created.
		 * @throws IOException		If a problem occurs while reading the
		 * 							original images or writing the thumbnails 
		 * 							to files. 
		 */
		public List<File> asFiles(Iterable<File> iterable) throws IOException
		{
			checkReadiness();
			
			if (iterable == null)
			{
				throw new NullPointerException("File name iterable is null.");
			}
			
			List<File> destinationFiles = new ArrayList<File>();
			
			ThumbnailParameter param = makeParam();
			
			Iterator<File> filenameIter = iterable.iterator();
			
			for (ImageSource<T> source : sources)
			{
				if (!filenameIter.hasNext())
				{
					throw new IndexOutOfBoundsException(
							"Not enough file names provided by iterator."
					);
				}
				
				FileImageSink destination = new FileImageSink(filenameIter.next());
				
				Thumbnailator.createThumbnail(
						new SourceSinkThumbnailTask<T, File>(param, source, destination)
				);
				
				destinationFiles.add(destination.getSink());
			}
			
			return destinationFiles;
		}
		
		/**
		 * Creates the thumbnails and stores them to the files.
		 * <p>
		 * The file names for the thumbnails are obtained from the given
		 * {@link Iterable}.
		 * 
		 * @param iterable			An {@link Iterable} which returns an
		 * 							{@link Iterator} which returns file names
		 * 							which should be assigned to each thumbnail.
		 * @throws IOException		If a problem occurs while reading the
		 * 							original images or writing the thumbnails 
		 * 							to files.
		 */
		public void toFiles(Iterable<File> iterable) throws IOException
		{
			asFiles(iterable);
		}
		
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
		 * @throws IOException		If a problem occurs while reading the
		 * 							original images or writing the thumbnails 
		 * 							to files. 
		 * @throws IllegalStateException		If the original images are not
		 * 										from files.
		 */
		public List<File> asFiles(Rename rename) throws IOException
		{
			checkReadiness();
			
			if (!(sources.get(0) instanceof FileImageSource))
			{
				throw new IllegalStateException("Cannot create thumbnails to files if original images are not from files.");
			}
			
			if (rename == null)
			{
				throw new NullPointerException("Rename is null.");
			}

			List<File> destinationFiles = new ArrayList<File>();
			
			ThumbnailParameter param = makeParam();
			
			for (ImageSource<T> source : sources)
			{
				File f = ((FileImageSource)source).getSource();
				
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
		 * @throws IOException		If a problem occurs while reading the
		 * 							original images or writing the thumbnails 
		 * 							to files. 
		 * 							thumbnails to files. 
		 * @throws IllegalStateException		If the original images are not
		 * 										from files.
		 */
		public void toFiles(Rename rename) throws IOException
		{
			asFiles(rename);
		}

		/**
		 * Create a thumbnail and writes it to a {@link File}.
		 * <p>
		 * To call this method, the thumbnail must have been created from a
		 * single source.
		 * 
		 * @param outFile			The file to which the thumbnail is to be
		 * 							written to.
		 * @throws IOException		If a problem occurs while reading the
		 * 							original images or writing the thumbnails 
		 * 							to files. 
		 * @throws IllegalArgumentException		If multiple original image files
		 * 										are	specified.
		 */
		public void toFile(File outFile) throws IOException
		{
			checkReadiness();
			
			if (sources.size() > 1)
			{
				throw new IllegalArgumentException("Cannot output multiple thumbnails to one file.");
			}
			
			ImageSource<T> source = sources.get(0);
			FileImageSink destination = new FileImageSink(outFile);
			
			Thumbnailator.createThumbnail(
					new SourceSinkThumbnailTask<T, File>(makeParam(), source, destination)
			);
		}
		
		/**
		 * Create a thumbnail and writes it to a {@link File}.
		 * <p>
		 * To call this method, the thumbnail must have been created from a
		 * single source.
		 * 
		 * @param outFilepath		The file to which the thumbnail is to be
		 * 							written to.
		 * @throws IOException		If a problem occurs while reading the
		 * 							original images or writing the thumbnails 
		 * 							to files. 
		 * @throws IllegalArgumentException		If multiple original image files
		 * 										are	specified.
		 */
		public void toFile(String outFilepath) throws IOException
		{
			checkReadiness();
			
			if (sources.size() > 1)
			{
				throw new IllegalArgumentException("Cannot output multiple thumbnails to one file.");
			}
			
			ImageSource<T> source = sources.get(0);
			FileImageSink destination = new FileImageSink(outFilepath);
			
			Thumbnailator.createThumbnail(
					new SourceSinkThumbnailTask<T, File>(makeParam(), source, destination)
			);
		}
		
		/**
		 * Create a thumbnail and writes it to a {@link OutputStream}.
		 * <p>
		 * To call this method, the thumbnail must have been created from a
		 * single source.
		 * 
		 * @param os				The output stream to which the thumbnail
		 * 							is to be written to.
		 * @throws IOException		If a problem occurs while reading the
		 * 							original images or writing the thumbnails. 
		 * @throws IllegalArgumentException		If multiple original image files
		 * 										are	specified.
		 */
		public void toOutputStream(OutputStream os) throws IOException
		{
			checkReadiness();
			
			if (sources.size() > 1)
			{
				throw new IllegalArgumentException("Cannot output multiple thumbnails to one stream.");
			}
			
			ImageSource<T> source = sources.get(0);
			OutputStreamImageSink destination = new OutputStreamImageSink(os);
			
			Thumbnailator.createThumbnail(
					new SourceSinkThumbnailTask<T, OutputStream>(makeParam(), source, destination)
			);
		}
	}
}