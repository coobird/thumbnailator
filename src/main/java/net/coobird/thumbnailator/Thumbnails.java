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

package net.coobird.thumbnailator;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.filters.Canvas;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.filters.Pipeline;
import net.coobird.thumbnailator.filters.Rotation;
import net.coobird.thumbnailator.filters.Watermark;
import net.coobird.thumbnailator.geometry.AbsoluteSize;
import net.coobird.thumbnailator.geometry.Coordinate;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.geometry.Region;
import net.coobird.thumbnailator.geometry.Size;
import net.coobird.thumbnailator.name.Rename;
import net.coobird.thumbnailator.resizers.BicubicResizer;
import net.coobird.thumbnailator.resizers.BilinearResizer;
import net.coobird.thumbnailator.resizers.DefaultResizerFactory;
import net.coobird.thumbnailator.resizers.FixedResizerFactory;
import net.coobird.thumbnailator.resizers.ProgressiveBilinearResizer;
import net.coobird.thumbnailator.resizers.Resizer;
import net.coobird.thumbnailator.resizers.ResizerFactory;
import net.coobird.thumbnailator.resizers.configurations.AlphaInterpolation;
import net.coobird.thumbnailator.resizers.configurations.Antialiasing;
import net.coobird.thumbnailator.resizers.configurations.Dithering;
import net.coobird.thumbnailator.resizers.configurations.Rendering;
import net.coobird.thumbnailator.resizers.configurations.ScalingMode;
import net.coobird.thumbnailator.tasks.SourceSinkThumbnailTask;
import net.coobird.thumbnailator.tasks.io.BufferedImageSink;
import net.coobird.thumbnailator.tasks.io.BufferedImageSource;
import net.coobird.thumbnailator.tasks.io.FileImageSink;
import net.coobird.thumbnailator.tasks.io.FileImageSource;
import net.coobird.thumbnailator.tasks.io.ImageSource;
import net.coobird.thumbnailator.tasks.io.InputStreamImageSource;
import net.coobird.thumbnailator.tasks.io.OutputStreamImageSink;
import net.coobird.thumbnailator.tasks.io.URLImageSource;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;

/**
 * Provides a fluent interface to create thumbnails.
 * <p>
 * This is the main entry point for creating thumbnails with Thumbnailator.
 * <p>
 * By using the Thumbnailator's fluent interface, it is possible to write
 * thumbnail generation code which resembles written English.
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
    .outputFormat("jpeg")
    .asFiles(Rename.PREFIX_DOT_THUMBNAIL);

// English: "Make thumbnails of files in the directory, with a size of 200x200,
             with output format of JPEG, and save them as files while renaming
             the files to be prefixed with a 'thumbnail.'."
 * </pre>
 * </DD>
 * </DL>
 * For more examples, please visit the <a href="http://code.google.com/p/thumbnailator/">
 * Thumbnailator</a> project page.
 * <p> 
 * <h2>Important Implementation Notes</h2>
 * Upon calling one of the {@code Thumbnails.of(...)} methods, <em>in the
 * current implementation</em>, an instance of an inner class of this class is
 * returned. In most cases, the returned instance should not be used by
 * storing it in a local variable, as changes in the internal implementation
 * could break code in the future.
 * <p>
 * As a rule of thumb, <em>always method chain from the {@code Thumbnails.of}
 * all the way until the output method (e.g. {@code toFile}, {@code asBufferedImage},
 * etc.) is called without breaking them down into single statements.</em>
 * See the "Usage" section above for the intended use of the Thumbnailator's
 * fluent interface. 
 * <DL>
 * <DT><B>Unintended Use:</B></DT>
 * <DD>
 * <pre>
// Unintended use - not recommended!
Builder&lt;File&gt; instance = Thumbnails.of("path/to/image");
instance.size(200, 200);
instance.asFiles("path/to/thumbnail");
 * </pre>
 * </DD>
 * </DL>
 * 
 * @author coobird
 *
 */
public final class Thumbnails {
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
	private static void validateDimensions(int width, int height) {
		if (width <= 0 && height <= 0) {
			throw new IllegalArgumentException(
					"Destination image dimensions must not be less than " +
					"0 pixels."
			);

		} else if (width <= 0 || height <= 0) {
			String dimension = width == 0 ? "width" : "height";
			
			throw new IllegalArgumentException(
					"Destination image " + dimension + " must not be " +
					"less than or equal to 0 pixels."
			);
		}		
	}
	
	private static void checkForNull(Object o, String message) {
		if (o == null) {
			throw new NullPointerException(message);
		}
	}
	
	private static void checkForEmpty(Object[] o, String message) {
		if (o.length == 0) {
			throw new IllegalArgumentException(message);
		}
	}
	
	private static void checkForEmpty(Iterable<?> o, String message) {
		if (!o.iterator().hasNext()) {
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
	public static Builder<File> of(String... files) {
		checkForNull(files, "Cannot specify null for input files.");
		checkForEmpty(files, "Cannot specify an empty array for input files.");
		return Builder.ofStrings(Arrays.asList(files));
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
	public static Builder<File> of(File... files) {
		checkForNull(files, "Cannot specify null for input files.");
		checkForEmpty(files, "Cannot specify an empty array for input files.");
		return Builder.ofFiles(Arrays.asList(files));
	}
	
	/**
	 * Indicate to make thumbnails from the specified {@link URL}s.
	 * 
	 * @param urls		{@link URL} objects of image files for which thumbnails
	 * 					are to be produced for.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty array.
	 */
	public static Builder<URL> of(URL... urls) {
		checkForNull(urls, "Cannot specify null for input URLs.");
		checkForEmpty(urls, "Cannot specify an empty array for input URLs.");
		return Builder.ofUrls(Arrays.asList(urls));
	}
	
	/**
	 * Indicate to make thumbnails from the specified {@link InputStream}s.
	 * <p>
	 * Note that the {@link InputStream#close()} method will not be called
	 * upon reading the source image from the {@link InputStream}. 
	 * 
	 * @param inputStreams		{@link InputStream}s which provide the images
	 * 							for which thumbnails are to be produced for.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty array.
	 */
	public static Builder<? extends InputStream> of(InputStream... inputStreams) {
		checkForNull(inputStreams, "Cannot specify null for InputStreams.");
		checkForEmpty(inputStreams, "Cannot specify an empty array for InputStreams.");
		return Builder.ofInputStreams(Arrays.asList(inputStreams));
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
	public static Builder<BufferedImage> of(BufferedImage... images) {
		checkForNull(images, "Cannot specify null for images.");
		checkForEmpty(images, "Cannot specify an empty array for images.");
		return Builder.ofBufferedImages(Arrays.asList(images));
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
	 * @since 	0.3.1
	 */
	public static Builder<File> fromFilenames(Iterable<String> files) {
		checkForNull(files, "Cannot specify null for input files.");
		checkForEmpty(files, "Cannot specify an empty collection for input files.");
		return Builder.ofStrings(files);
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
	 * @since 	0.3.1
	 */
	public static Builder<File> fromFiles(Iterable<File> files) {
		checkForNull(files, "Cannot specify null for input files.");
		checkForEmpty(files, "Cannot specify an empty collection for input files.");
		return Builder.ofFiles(files);
	}

	/**
	 * Indicate to make thumbnails for images with the specified {@link URL}s.
	 * 
	 * @param urls		URLs of the images for which thumbnails
	 * 					are to be produced.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty collection.
	 * @since 	0.3.1
	 */
	public static Builder<URL> fromURLs(Iterable<URL> urls) {
		checkForNull(urls, "Cannot specify null for input URLs.");
		checkForEmpty(urls, "Cannot specify an empty collection for input URLs.");
		return Builder.ofUrls(urls);
	}
	
	/**
	 * Indicate to make thumbnails for images obtained from the specified
	 * {@link InputStream}s.
	 * <p>
	 * Note that the {@link InputStream#close()} method will not be called
	 * upon reading the source image from the {@link InputStream}. 
	 * 
	 * @param inputStreams		{@link InputStream}s which provide images for
	 * 							which thumbnails are to be produced.
	 * @return			Reference to a builder object which is used to
	 * 					specify the parameters for creating the thumbnail.
	 * @throws NullPointerException		If the argument is {@code null}.
	 * @throws IllegalArgumentException	If the argument is an empty collection.
	 * @since 	0.3.1
	 */
	public static Builder<InputStream> fromInputStreams(Iterable<? extends InputStream> inputStreams) {
		checkForNull(inputStreams, "Cannot specify null for InputStreams.");
		checkForEmpty(inputStreams, "Cannot specify an empty collection for InputStreams.");
		return Builder.ofInputStreams(inputStreams);
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
	 * @since 	0.3.1
	 */
	public static Builder<BufferedImage> fromImages(Iterable<BufferedImage> images) {
		checkForNull(images, "Cannot specify null for images.");
		checkForEmpty(images, "Cannot specify an empty collection for images.");
		return Builder.ofBufferedImages(images);
	}

	/**
	 * The builder interface for Thumbnailator to set up the thumbnail
	 * generation task.
	 * <p>
	 * Thumbnailator is intended to be used by calling one of the
	 * {@code Thumbnails.of(...)} methods, then chaining methods such as
	 * {@link #size(int, int)} and {@link #outputQuality(double)} to set up
	 * the thumbnail generation parameters. (See "Intended Use" below.)
	 * The end result should be code that resembles English.
	 * <p>
	 * In most cases, holding an instance of this class in a local variable,
	 * such as seen in the "Unintended Use" example below, is more verbose
	 * and less future-proof, as changes to this class (which is just an
	 * inner class of the {@link Thumbnails} class) can lead to broken code  
	 * when attempting to use future releases of Thumbnailator.
	 * <p>
	 * <DL>
	 * <DT><B>Intended Use:</B></DT>
	 * <DD>
	 * <pre>
// Intended use - recommended!
Thumbnails.of("path/to/image")
    .size(200, 200)
    .asFile("path/to/thumbnail");

// English: "Make a thumbnail of 'path/to/image' with a size of 200x200,
             and save it as a file to 'path/to/thumbnail'."
	 * </pre>
	 * </DD>
	 * <DT><B>Unintended Use:</B></DT>
	 * <DD>
	 * <pre>
// Unintended use - not recommended!
Builder&lt;File&gt; instance = Thumbnails.of("path/to/image");
instance.size(200, 200);
instance.asFiles("path/to/thumbnail");
	 * </pre>
	 * </DD>
	 * </DL>
	 * <p>
	 * An instance of this class provides the fluent interface in the form of
	 * method chaining. Through the fluent interface, the parameters used for
	 * the thumbnail creation, such as {@link #size(int, int)} and 
	 * {@link #outputQuality(double)} can be set up. Finally, to execute the
	 * thumbnail creation, one of the output methods whose names start with
	 * {@code to} (e.g. {@link #toFiles(Rename)}) or {@code as} 
	 * (e.g. {@link #asBufferedImages()}) is called.
	 * <p>
	 * An instance of this class is obtained by calling one of:
	 * <ul>
	 * <li>{@link Thumbnails#of(BufferedImage...)}</li>
	 * <li>{@link Thumbnails#of(File...)}</li>
	 * <li>{@link Thumbnails#of(String...)}</li>
	 * <li>{@link Thumbnails#of(InputStream...)}</li>
	 * <li>{@link Thumbnails#of(URL...)}</li>
	 * <li>{@link Thumbnails#fromImages(Iterable)}</li>
	 * <li>{@link Thumbnails#fromFiles(Iterable)}</li>
	 * <li>{@link Thumbnails#fromFilenames(Iterable)}</li>
	 * <li>{@link Thumbnails#fromInputStreams(Iterable)}</li>
	 * <li>{@link Thumbnails#fromURLs(Iterable)}</li>
	 * </ul>
 	 * 
	 * @author coobird
	 *
	 */
	public static class Builder<T> {
		private final Iterable<ImageSource<T>> sources;
		
		private Builder(Iterable<ImageSource<T>> sources) {
			this.sources = sources;
			statusMap.put(Properties.OUTPUT_FORMAT, Status.OPTIONAL);
		}
		
		private static final class StringImageSourceIterator implements
				Iterable<ImageSource<File>> {

			private final Iterable<String> filenames;
		
			private StringImageSourceIterator(Iterable<String> filenames) {
				this.filenames = filenames;
			}
		
			public Iterator<ImageSource<File>> iterator() {
				return new Iterator<ImageSource<File>>() {
					Iterator<String> iter = filenames.iterator();
					
					public boolean hasNext() {
						return iter.hasNext();
					}
		
					public ImageSource<File> next() {
						return new FileImageSource(iter.next());
					}
		
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		}
		
		private static final class FileImageSourceIterator implements
				Iterable<ImageSource<File>> {

			private final Iterable<File> files;
			
			private FileImageSourceIterator(Iterable<File> files) {
				this.files = files;
			}
			
			public Iterator<ImageSource<File>> iterator() {
				return new Iterator<ImageSource<File>>() {
					Iterator<File> iter = files.iterator();
					
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					public ImageSource<File> next() {
						return new FileImageSource(iter.next());
					}
					
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		}
		
		private static final class URLImageSourceIterator implements
				Iterable<ImageSource<URL>> {

			private final Iterable<URL> urls;
			
			private URLImageSourceIterator(Iterable<URL> urls) {
				this.urls = urls;
			}
			
			public Iterator<ImageSource<URL>> iterator() {
				return new Iterator<ImageSource<URL>>() {
					Iterator<URL> iter = urls.iterator();
					
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					public ImageSource<URL> next() {
						return new URLImageSource(iter.next());
					}
					
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		}
		
		private static final class InputStreamImageSourceIterator implements
				Iterable<ImageSource<InputStream>> {

			private final Iterable<? extends InputStream> inputStreams;
			
			private InputStreamImageSourceIterator(Iterable<? extends InputStream> inputStreams) {
				this.inputStreams = inputStreams;
			}
			
			public Iterator<ImageSource<InputStream>> iterator() {
				return new Iterator<ImageSource<InputStream>>() {
					Iterator<? extends InputStream> iter = inputStreams.iterator();
					
					public boolean hasNext() {
						return iter.hasNext();
					}

					public ImageSource<InputStream> next() {
						return new InputStreamImageSource(iter.next());
					}
					
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		}
		
		private static final class BufferedImageImageSourceIterator implements
			Iterable<ImageSource<BufferedImage>> {

			private final Iterable<BufferedImage> image;
			
			private BufferedImageImageSourceIterator(Iterable<BufferedImage> images) {
				this.image = images;
			}
			
			public Iterator<ImageSource<BufferedImage>> iterator() {
				return new Iterator<ImageSource<BufferedImage>>() {
					Iterator<BufferedImage> iter = image.iterator();
					
					public boolean hasNext() {
						return iter.hasNext();
					}
					
					public ImageSource<BufferedImage> next() {
						return new BufferedImageSource(iter.next());
					}
					
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		}

		private static Builder<File> ofStrings(Iterable<String> filenames) {
			Iterable<ImageSource<File>> iter = new StringImageSourceIterator(filenames);
			return new Builder<File>(iter);
		}
		
		private static Builder<File> ofFiles(Iterable<File> files) {
			Iterable<ImageSource<File>> iter = new FileImageSourceIterator(files);
			return new Builder<File>(iter);
		}
		
		private static Builder<URL> ofUrls(Iterable<URL> urls) {
			Iterable<ImageSource<URL>> iter = new URLImageSourceIterator(urls);
			return new Builder<URL>(iter);
		}
		
		private static Builder<InputStream> ofInputStreams(Iterable<? extends InputStream> inputStreams) {
			Iterable<ImageSource<InputStream>> iter = new InputStreamImageSourceIterator(inputStreams);
			return new Builder<InputStream>(iter);
		}
		
		private static Builder<BufferedImage> ofBufferedImages(Iterable<BufferedImage> images) {
			Iterable<ImageSource<BufferedImage>> iter = new BufferedImageImageSourceIterator(images);
			return new Builder<BufferedImage>(iter);
		}

		private final class BufferedImageIterable implements
				Iterable<BufferedImage> {

			public Iterator<BufferedImage> iterator() {
				return new Iterator<BufferedImage>() {
					Iterator<ImageSource<T>> sourceIter = sources.iterator();

					public boolean hasNext() {
						return sourceIter.hasNext();
					}

					public BufferedImage next() {
						ImageSource<T> source = sourceIter.next();
						BufferedImageSink destination = new BufferedImageSink();
						
						try {
							Thumbnailator.createThumbnail(
									new SourceSinkThumbnailTask<T, BufferedImage>(makeParam(), source, destination)
							);
						} catch (IOException e) {
							return null;
						}
						
						return destination.getSink();
					}

					public void remove() {
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
		private static enum Status {
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
		private static interface Property {
			public String getName();
		}

		/**
		 * Enum of properties which can be set by this builder.
		 * 
		 * @author coobird
		 *
		 */
		private static enum Properties implements Property {
			SIZE("size"),
			WIDTH("width"),
			HEIGHT("height"),
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
			SOURCE_REGION("sourceRegion"),
			RESIZER_FACTORY("resizerFactory"),
			ALLOW_OVERWRITE("allowOverwrite"),
			CROP("crop"),
			USE_EXIF_ORIENTATION("useExifOrientation"),
			;
			
			private final String name;
			
			private Properties(String name) {
				this.name = name;
			}
		
			public String getName() {
				return name;
			}
		}

		/**
		 * Map to keep track of whether a property has been properly set or not.
		 */
		private final Map<Properties, Status> statusMap = new HashMap<Properties, Status>();

		/*
		 * Populates the property map.
		 */
		{
			statusMap.put(Properties.SIZE, Status.NOT_READY);
			statusMap.put(Properties.WIDTH, Status.OPTIONAL);
			statusMap.put(Properties.HEIGHT, Status.OPTIONAL);
			statusMap.put(Properties.SCALE, Status.NOT_READY);
			statusMap.put(Properties.SOURCE_REGION, Status.OPTIONAL);
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
			statusMap.put(Properties.RESIZER_FACTORY, Status.OPTIONAL);
			statusMap.put(Properties.ALLOW_OVERWRITE, Status.OPTIONAL);
			statusMap.put(Properties.CROP, Status.OPTIONAL);
			statusMap.put(Properties.USE_EXIF_ORIENTATION, Status.OPTIONAL);
		}

		/**
		 * Updates the property status map.
		 * 
		 * @param property		The property to update.
		 * @param newStatus		The new status.
		 */
		private void updateStatus(Properties property, Status newStatus) {
			if (statusMap.get(property) == Status.ALREADY_SET) {
				throw new IllegalStateException(
						property.getName() + " is already set.");
			}
			
			/*
			 * The `newStatus != Status.CANNOT_SET` condition will allow the
			 * status to be set to CANNOT_SET to be set multiple times.
			 */
			if (newStatus != Status.CANNOT_SET && statusMap.get(property) == Status.CANNOT_SET) {
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
		
		private static final int DIMENSION_NOT_SPECIFIED = -1;
		
		/*
		 * Defines the fields for the builder interface, and assigns the
		 * default values.
		 */
		private int width = DIMENSION_NOT_SPECIFIED;
		private int height = DIMENSION_NOT_SPECIFIED;
		private double scaleWidth = Double.NaN;
		private double scaleHeight = Double.NaN;
		
		private Region sourceRegion;
		
		private int imageType = IMAGE_TYPE_UNSPECIFIED;
		private boolean keepAspectRatio = true;
		
		private String outputFormat = ThumbnailParameter.DETERMINE_FORMAT;
		private String outputFormatType = ThumbnailParameter.DEFAULT_FORMAT_TYPE;
		private float outputQuality = ThumbnailParameter.DEFAULT_QUALITY;
		
		private ScalingMode scalingMode = ScalingMode.PROGRESSIVE_BILINEAR;
		private AlphaInterpolation alphaInterpolation = AlphaInterpolation.DEFAULT;
		private Dithering dithering = Dithering.DEFAULT;
		private Antialiasing antialiasing = Antialiasing.DEFAULT;
		private Rendering rendering = Rendering.DEFAULT;
		
		private ResizerFactory resizerFactory = DefaultResizerFactory.getInstance();
		
		private boolean allowOverwrite = true;
		
		private boolean fitWithinDimenions = true;
		
		private boolean useExifOrientation = true;
		
		/**
		 * This field should be set to the {@link Position} to be used for
		 * cropping if cropping is enabled. If cropping is disabled, then
		 * this field should be left {@code null}.
		 */
		private Position croppingPosition = null;
		
		/**
		 * The {@link ImageFilter}s that should be applied when creating the
		 * thumbnail.
		 */
		private Pipeline filterPipeline = new Pipeline();
		
		/**
		 * Sets the size of the thumbnail.
		 * <p>
		 * For example, to create thumbnails which should fit within a
		 * bounding rectangle of 640 x 480, the following code can be used:
		 * <pre><code>
Thumbnails.of(image)
    .size(640, 480)
    .toFile(thumbnail);
		 * </code></pre>
		 * <p>
		 * In the above code, the thumbnail will preserve the aspect ratio
		 * of the original image. If the thumbnail should be forced to the
		 * specified size, the {@link #forceSize(int, int)} method can
		 * be used instead of this method.
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
		public Builder<T> size(int width, int height) {
			updateStatus(Properties.SIZE, Status.ALREADY_SET);
			updateStatus(Properties.SCALE, Status.CANNOT_SET);
			
			validateDimensions(width, height);
			this.width = width;
			this.height = height;
			
			return this;
		}
		
		/**
		 * Sets the width of the thumbnail.
		 * <p>
		 * The thumbnail will have the dimensions constrained by the specified
		 * width, and the aspect ratio of the original image will be preserved
		 * by the thumbnail.
		 * <p>
		 * Once this method is called, calling the {@link #size(int, int)} or
		 * the {@link #scale(double)} method will result in an
		 * {@link IllegalStateException}.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param width			The width of the thumbnail.
		 * @return				Reference to this object.
		 * @since 	0.3.5
		 */
		public Builder<T> width(int width) {
			if (statusMap.get(Properties.SIZE) != Status.CANNOT_SET) {
				updateStatus(Properties.SIZE, Status.CANNOT_SET);
			}
			if (statusMap.get(Properties.SCALE) != Status.CANNOT_SET) {
				updateStatus(Properties.SCALE, Status.CANNOT_SET);
			}
			updateStatus(Properties.WIDTH, Status.ALREADY_SET);
			
			validateDimensions(width, Integer.MAX_VALUE);
			this.width = width;
			
			return this;
		}
		
		/**
		 * Sets the height of the thumbnail.
		 * <p>
		 * The thumbnail will have the dimensions constrained by the specified
		 * height, and the aspect ratio of the original image will be preserved
		 * by the thumbnail.
		 * <p>
		 * Once this method is called, calling the {@link #size(int, int)} or
		 * the {@link #scale(double)} method will result in an
		 * {@link IllegalStateException}.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param height		The height of the thumbnail.
		 * @return				Reference to this object.
		 * @since 	0.3.5
		 */
		public Builder<T> height(int height) {
			if (statusMap.get(Properties.SIZE) != Status.CANNOT_SET) {
				updateStatus(Properties.SIZE, Status.CANNOT_SET);
			}
			if (statusMap.get(Properties.SCALE) != Status.CANNOT_SET) {
				updateStatus(Properties.SCALE, Status.CANNOT_SET);
			}
			updateStatus(Properties.HEIGHT, Status.ALREADY_SET);
			
			validateDimensions(Integer.MAX_VALUE, height);
			this.height = height;
			
			return this;
		}
		
		/**
		 * Sets the size of the thumbnail.
		 * <p>
		 * The thumbnails will be forced to the specified size, therefore,
		 * the aspect ratio of the original image will not be preserved in
		 * the thumbnails. Calling this method will be equivalent to calling
		 * the {@link #size(int, int)} method in conjunction with the
		 * {@link #keepAspectRatio(boolean)} method with the value {@code false}.
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
		 * @since 	0.3.2
		 */
		public Builder<T> forceSize(int width, int height) {
			updateStatus(Properties.SIZE, Status.ALREADY_SET);
			updateStatus(Properties.KEEP_ASPECT_RATIO, Status.ALREADY_SET);
			updateStatus(Properties.SCALE, Status.CANNOT_SET);
			
			validateDimensions(width, height);
			this.width = width;
			this.height = height;
			this.keepAspectRatio = false;
			
			return this;
		}
		
		/**
		 * Sets the scaling factor of the thumbnail.
		 * <p>
		 * For example, to create thumbnails which are 50% the size of the
		 * original, the following code can be used:
		 * <pre><code>
Thumbnails.of(image)
    .scale(0.5)
    .toFile(thumbnail);
		 * </code></pre>
		 * <p>
		 * Once this method is called, calling the {@link #size(int, int)}
		 * method, or the {@link #scale(double, double)} method, or the
		 * {@link #keepAspectRatio(boolean)} method will result in an
		 * {@link IllegalStateException}.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param scale			The scaling factor to use when creating a
		 * 						thumbnail.
		 * 						<p>
		 * 						The value must be a {@code double} which is
		 * 						greater than {@code 0.0}, and not
		 * 						{@link Double#POSITIVE_INFINITY}.
		 * @return				Reference to this object.
		 */
		public Builder<T> scale(double scale) {
			return scale(scale, scale);
		}
		
		/**
		 * Sets the scaling factor for the width and height of the thumbnail.
		 * <p>
		 * If the scaling factor for the width and height are not equal, then
		 * the thumbnail will not preserve the aspect ratio of the original
		 * image.
		 * <p>
		 * For example, to create thumbnails which are 50% the width of the
		 * original, while 75% the height of the original, the following code
		 * can be used:
		 * <pre><code>
Thumbnails.of(image)
    .scale(0.5, 0.75)
    .toFile(thumbnail);
		 * </code></pre>
		 * <p>
		 * Once this method is called, calling the {@link #size(int, int)}
		 * method, or the {@link #scale(double)} method, or the
		 * {@link #keepAspectRatio(boolean)} method will result in an
		 * {@link IllegalStateException}.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param scaleWidth	The scaling factor to use for the width when
		 * 						creating a thumbnail.
		 * 						<p>
		 * 						The value must be a {@code double} which is
		 * 						greater than {@code 0.0}, and not
		 * 						{@link Double#POSITIVE_INFINITY}.
		 * @param scaleHeight	The scaling factor to use for the height when
		 * 						creating a thumbnail.
		 * 						<p>
		 * 						The value must be a {@code double} which is
		 * 						greater than {@code 0.0}, and not
		 * 						{@link Double#POSITIVE_INFINITY}.
		 * @return				Reference to this object.
		 * @since 	0.3.10
		 */
		public Builder<T> scale(double scaleWidth, double scaleHeight) {
			updateStatus(Properties.SCALE, Status.ALREADY_SET);
			updateStatus(Properties.SIZE, Status.CANNOT_SET);
			updateStatus(Properties.KEEP_ASPECT_RATIO, Status.CANNOT_SET);
			
			if (scaleWidth <= 0.0 || scaleHeight <= 0.0) {
				throw new IllegalArgumentException(
						"The scaling factor is equal to or less than 0."
				);
			}
			if (Double.isNaN(scaleWidth) || Double.isNaN(scaleHeight)) {
				throw new IllegalArgumentException(
						"The scaling factor is not a number."
				);
			}
			if (Double.isInfinite(scaleWidth) || Double.isInfinite(scaleHeight)) {
				throw new IllegalArgumentException(
						"The scaling factor cannot be infinity."
				);
			}
			
			this.scaleWidth = scaleWidth;
			this.scaleHeight = scaleHeight;
			
			return this;
		}
		
		/**
		 * Specifies the source region from which the thumbnail is to be
		 * created from.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param sourceRegion	Source region to use when creating a thumbnail.
		 * 						<p>
		 * @return				Reference to this object.
		 * @throws NullPointerException	If the source region object is
		 * 								{@code null}.
		 * @since 	0.3.4
		 */
		public Builder<T> sourceRegion(Region sourceRegion) {
			if (sourceRegion == null) {
				throw new NullPointerException("Region cannot be null.");
			}
			
			updateStatus(Properties.SOURCE_REGION, Status.ALREADY_SET);
			this.sourceRegion = sourceRegion;
			return this;
		}
		
		/**
		 * Specifies the source region from which the thumbnail is to be
		 * created from.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param position		Position of the source region.
		 * @param size			Size of the source region.
		 * @return				Reference to this object.
		 * @throws NullPointerException	If the position and/or size is
		 * 								{@code null}.
		 * @since 	0.3.4
		 */
		public Builder<T> sourceRegion(Position position, Size size) {
			if (position == null) {
				throw new NullPointerException("Position cannot be null.");
			}
			if (size == null) {
				throw new NullPointerException("Size cannot be null.");
			}
			
			return sourceRegion(new Region(position, size));
		}

		/**
		 * Specifies the source region from which the thumbnail is to be
		 * created from.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param x				The horizontal-compoennt of the top left-hand
		 * 						corner of the source region.
		 * @param y				The vertical-compoennt of the top left-hand
		 * 						corner of the source region.
		 * @param width			Width of the source region.
		 * @param height		Height of the source region.
		 * @return				Reference to this object.
		 * @throws IllegalArgumentException	If the width and/or height is
		 * 									less than or equal to {@code 0}.
		 * @since 	0.3.4
		 */
		public Builder<T> sourceRegion(int x, int y, int width, int height) {
			if (width <= 0 || height <= 0) {
				throw new IllegalArgumentException(
						"Width and height must be greater than 0."
				);
			}
			
			return sourceRegion(
					new Coordinate(x, y),
					new AbsoluteSize(width, height)
			);
		}
		
		/**
		 * Specifies the source region from which the thumbnail is to be
		 * created from.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param position		Position of the source region.
		 * @param width			Width of the source region.
		 * @param height		Height of the source region.
		 * @return				Reference to this object.
		 * @throws NullPointerException		If the position and/or size is
		 * 									{@code null}.
		 * @throws IllegalArgumentException	If the width and/or height is
		 * 									less than or equal to {@code 0}.
		 * @since 	0.3.4
		 */
		public Builder<T> sourceRegion(Position position, int width, int height) {
			if (position == null) {
				throw new NullPointerException("Position cannot be null.");
			}
			if (width <= 0 || height <= 0) {
				throw new IllegalArgumentException(
						"Width and height must be greater than 0."
				);
			}
			
			return sourceRegion(
					position,
					new AbsoluteSize(width, height)
			);
		}
		
		/**
		 * Specifies the source region from which the thumbnail is to be
		 * created from.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param region		A rectangular region which specifies the source
		 * 						region to use when creating the thumbnail.
		 * @throws NullPointerException		If the region is {@code null}.
		 * @since 	0.3.4
		 */
		public Builder<T> sourceRegion(Rectangle region) {
			if (region == null) {
				throw new NullPointerException("Region cannot be null.");
			}
			
			return sourceRegion(
					new Coordinate(region.x, region.y),
					new AbsoluteSize(region.getSize())
			);
		}
		
		/**
		 * Crops the thumbnail to the size specified when calling the
		 * {@link #size(int, int)} method, positioned by the given
		 * {@link Position} object.
		 * <p>
		 * Calling this method will guarantee that the size of the thumbnail
		 * will be exactly the dimensions specified in the
		 * {@link #size(int, int)} method.
		 * <p>
		 * Internally, the resizing is performed in two steps.
		 * First, the thumbnail will be sized so that one of the dimensions will
		 * be sized exactly to the dimension specified in the {@code size}
		 * method, while allowing the other dimension to overhang the specified
		 * dimension. Then, the thumbnail will be cropped to the dimensions
		 * specified in the {@code size} method, positioned using the speficied
		 * {@link Position} object.
		 * <p>
		 * Once this method is called, calling the {@link #scale(double)} method
		 * will result in an {@link IllegalStateException}.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 *
		 * @param position		The position to which the thumbnail should be
		 * 						cropped to. For example, if
		 * 						{@link Positions#CENTER} is specified, the
		 * 						resulting thumbnail will be made by cropping to
		 * 						the center of the image.
		 * @throws NullPointerException		If the position is {@code null}.
		 * @since 	0.4.0
		 */
		public Builder<T> crop(Position position) {
			checkForNull(position, "Position cannot be null.");
			
			updateStatus(Properties.CROP, Status.ALREADY_SET);
			updateStatus(Properties.SCALE, Status.CANNOT_SET);

			croppingPosition = position;
			fitWithinDimenions = false;
			return this;
		}
		
		/**
		 * Specifies whether or not to overwrite files which already exist if
		 * they have been specified as destination files.
		 * <p>
		 * This method will change the output behavior of the following methods:
		 * <ul>
		 * <li>{@link #toFile(File)}</li>
		 * <li>{@link #toFile(String)}</li>
		 * <li>{@link #toFiles(Iterable)}</li>
		 * <li>{@link #toFiles(Rename)}</li>
		 * <li>{@link #asFiles(Iterable)}</li>
		 * <li>{@link #asFiles(Rename)}</li>
		 * </ul>
		 * The behavior of methods which are not listed above will not be
		 * affected by calling this method.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param allowOverwrite	If {@code true} then existing files will be
		 * 							overwritten if specified as a destination.
		 * 							If {@code false}, then the existing files
		 * 							will not be altered. For specific behavior,
		 * 							please refer to the specific output methods
		 * 							listed above.
		 * 						
		 * @since 	0.3.7
		 */
		public Builder<T> allowOverwrite(boolean allowOverwrite) {
			updateStatus(Properties.ALLOW_OVERWRITE, Status.ALREADY_SET);
			this.allowOverwrite = allowOverwrite;
			
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
		public Builder<T> imageType(int type) {
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
		public Builder<T> scalingMode(ScalingMode config) {
			checkForNull(config, "Scaling mode is null.");
			updateStatus(Properties.SCALING_MODE, Status.ALREADY_SET);
			updateStatus(Properties.RESIZER, Status.CANNOT_SET);
			updateStatus(Properties.RESIZER_FACTORY, Status.CANNOT_SET);
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
		 * <p>
		 * This method cannot be called in conjunction with the
		 * {@link #resizerFactory(ResizerFactory)} method.
		 * 
		 * @param resizer		The scaling operation to use.
		 * @return				Reference to this object.
		 */
		public Builder<T> resizer(Resizer resizer) {
			checkForNull(resizer, "Resizer is null.");
			updateStatus(Properties.RESIZER, Status.ALREADY_SET);
			updateStatus(Properties.RESIZER_FACTORY, Status.CANNOT_SET);
			updateStatus(Properties.SCALING_MODE, Status.CANNOT_SET);
			this.resizerFactory = new FixedResizerFactory(resizer);
			return this;
		}
		
		/**
		 * Sets the {@link ResizerFactory} object to use to decide what kind of
		 * resizing operation is to be used when creating the thumbnail.
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * <p>
		 * This method cannot be called in conjunction with the
		 * {@link #resizer(Resizer)} method.
		 * 
		 * @param resizerFactory		The scaling operation to use.
		 * @return						Reference to this object.
		 * @since	0.4.0
		 */
		public Builder<T> resizerFactory(ResizerFactory resizerFactory) {
			checkForNull(resizerFactory, "ResizerFactory is null.");
			updateStatus(Properties.RESIZER_FACTORY, Status.ALREADY_SET);
			updateStatus(Properties.RESIZER, Status.CANNOT_SET);
			
			// disable the methods which set parameters for the Resizer
			updateStatus(Properties.SCALING_MODE, Status.CANNOT_SET);
			updateStatus(Properties.ALPHA_INTERPOLATION, Status.CANNOT_SET);
			updateStatus(Properties.DITHERING, Status.CANNOT_SET);
			updateStatus(Properties.ANTIALIASING, Status.CANNOT_SET);
			updateStatus(Properties.RENDERING, Status.CANNOT_SET);
			
			this.resizerFactory = resizerFactory;
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
		 * <p>
		 * This method cannot be called in conjunction with the
		 * {@link #resizerFactory(ResizerFactory)} method.
		 * 
		 * @param config		The alpha interpolation mode.
		 * @return				Reference to this object.
		 */
		public Builder<T> alphaInterpolation(AlphaInterpolation config) {
			checkForNull(config, "Alpha interpolation is null.");
			updateStatus(Properties.RESIZER_FACTORY, Status.CANNOT_SET);
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
		 * <p>
		 * This method cannot be called in conjunction with the
		 * {@link #resizerFactory(ResizerFactory)} method.
		 * 
		 * @param config		The dithering mode.
		 * @return				Reference to this object.
		 */
		public Builder<T> dithering(Dithering config) {
			checkForNull(config, "Dithering is null.");
			updateStatus(Properties.RESIZER_FACTORY, Status.CANNOT_SET);
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
		 * <p>
		 * This method cannot be called in conjunction with the
		 * {@link #resizerFactory(ResizerFactory)} method.
		 * 
		 * @param config		The antialiasing mode.
		 * @return				Reference to this object.
		 */
		public Builder<T> antialiasing(Antialiasing config) {
			checkForNull(config, "Antialiasing is null.");
			updateStatus(Properties.RESIZER_FACTORY, Status.CANNOT_SET);
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
		 * <p>
		 * This method cannot be called in conjunction with the
		 * {@link #resizerFactory(ResizerFactory)} method.
		 * 
		 * @param config		The rendering mode.
		 * @return				Reference to this object.
		 */
		public Builder<T> rendering(Rendering config) {
			checkForNull(config, "Rendering is null.");
			updateStatus(Properties.RESIZER_FACTORY, Status.CANNOT_SET);
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
		 * or the {@link #scale(double, double)} method will result in a
		 * {@link IllegalStateException}.
		 * 
		 * @param keep			{@code true} if the thumbnail is to maintain
		 * 						the aspect ratio of the original image,
		 * 						{@code false} otherwise.
		 * @return				Reference to this object.
		 * 
		 * @throws IllegalStateException	If
		 * 									<ol>
		 * 									<li>the {@link #size(int, int)} has
		 * 									not yet been called to specify the
		 * 									size of the thumbnail, or</li>
		 * 									<li>the {@link #scale(double)}
		 * 									method has been called, or</li>
		 * 									<li>the
		 * 									{@link #scale(double, double)}
		 * 									method has been called, or</li>
		 * 									<li>the {@link #width(int)} and/or
		 * 									{@link #height(int)} has been called
		 * 									and not preserving the aspect ratio
		 * 									is desired.</li>
		 * 									</ol>
		 */
		public Builder<T> keepAspectRatio(boolean keep) {
			if (statusMap.get(Properties.SCALE) == Status.ALREADY_SET) {
				throw new IllegalStateException("Cannot specify whether to " +
						"keep the aspect ratio if the scaling factor has " +
						"already been specified.");
			}
			if (statusMap.get(Properties.SIZE) == Status.NOT_READY) {
				throw new IllegalStateException("Cannot specify whether to " +
						"keep the aspect ratio unless the size parameter has " +
						"already been specified.");
			}
			if ((statusMap.get(Properties.WIDTH) == Status.ALREADY_SET ||
					statusMap.get(Properties.HEIGHT) == Status.ALREADY_SET) &&
					!keep
			) {
				throw new IllegalStateException("The aspect ratio must be " +
						"preserved when the width and/or height parameter " +
						"has already been specified.");
			}
			
			updateStatus(Properties.KEEP_ASPECT_RATIO, Status.ALREADY_SET);
			keepAspectRatio = keep;
			return this;
		}
		
		/**
		 * Sets the output quality of the compression algorithm used to
		 * compress the thumbnail when it is written to an external destination
		 * such as a file or output stream.
		 * <p>
		 * The value is a {@code float} between {@code 0.0f} and {@code 1.0f}
		 * where {@code 0.0f} indicates the minimum quality and {@code 1.0f}
		 * indicates the maximum quality settings should be used for by the
		 * compression codec.
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * <p>
		 * Calling this method in conjunction with {@link #asBufferedImage()}
		 * or {@link #asBufferedImages()} will not result in any changes to the
		 * final result.
		 * <p>
		 * Calling this method multiple times, or the
		 * {@link #outputQuality(double)} in conjunction with this method will
		 * result in an {@link IllegalStateException} to be thrown.
		 * 
		 * @param quality		The compression quality to use when writing
		 * 						the thumbnail.
		 * @return				Reference to this object.
		 * @throws IllegalArgumentException	If the argument is less than
		 * 									{@code 0.0f} or is greater than
		 * 									{@code 1.0f}.
		 */
		public Builder<T> outputQuality(float quality) {
			if (quality < 0.0f || quality > 1.0f) {
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
		 * Sets the output quality of the compression algorithm used to
		 * compress the thumbnail when it is written to an external destination
		 * such as a file or output stream.
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
		 * Calling this method in conjunction with {@link #asBufferedImage()}
		 * or {@link #asBufferedImages()} will not result in any changes to the
		 * final result.
		 * <p>
		 * Calling this method multiple times, or the
		 * {@link #outputQuality(float)} in conjunction with this method will
		 * result in an {@link IllegalStateException} to be thrown.
		 * 
		 * @param quality		The compression quality to use when writing
		 * 						the thumbnail.
		 * @return				Reference to this object.
		 * @throws IllegalArgumentException	If the argument is less than
		 * 									{@code 0.0d} or is greater than
		 * 									{@code 1.0d}.
 		 */
		public Builder<T> outputQuality(double quality) {
			if (quality < 0.0d || quality > 1.0d) {
				throw new IllegalArgumentException(
						"The quality setting must be in the range 0.0d and " +
						"1.0d, inclusive."
				);
			}
			
			updateStatus(Properties.OUTPUT_QUALITY, Status.ALREADY_SET);
			outputQuality = (float)quality;
			if (outputQuality < 0.0f) {
				outputQuality = 0.0f;
			} else if (outputQuality > 1.0f) {
				outputQuality = 1.0f;
			}
			return this;
		}
		
		/**
		 * Sets the compression format to use when writing the thumbnail.
		 * <p>
		 * For example, to set the output format to JPEG, the following code
		 * can be used:
		 * <pre><code>
Thumbnails.of(image)
    .size(640, 480)
    .outputFormat("JPEG")
    .toFile(thumbnail);
		 * </code></pre>
		 * or, alternatively:
		 * <pre><code>
Thumbnails.of(image)
    .size(640, 480)
    .outputFormat("jpg")
    .toFile(thumbnail);
		 * </code></pre>
		 * <p>
		 * Currently, whether or not the compression format string is valid
		 * dependents on whether the Java Image I/O API recognizes the string
		 * as a format that it supports for output. (Valid format names can
		 * be obtained by calling the {@link ImageIO#getWriterFormatNames()}
		 * method.)
		 * <p>
		 * Calling this method to set this parameter is optional.
		 * <p>
		 * Calling this method in conjunction with {@link #asBufferedImage()}
		 * or {@link #asBufferedImages()} will not result in any changes to the
		 * final result.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param format		The compression format to use when writing
		 * 						the thumbnail.
		 * @return				Reference to this object.
		 * @throws IllegalArgumentException	If an unsupported format is
		 * 									specified.
		 */
		public Builder<T> outputFormat(String format) {
			if (!ThumbnailatorUtils.isSupportedOutputFormat(format)) {
				throw new IllegalArgumentException(
						"Specified format is not supported: " + format
				);
			}
			
			updateStatus(Properties.OUTPUT_FORMAT, Status.ALREADY_SET);
			outputFormat = format;
			return this;
		}
		
		/**
		 * Sets the compression format to use the same format as the original
		 * image.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @return				Reference to this object.
		 * @since	0.4.0
		 */
		public Builder<T> useOriginalFormat() {
			updateStatus(Properties.OUTPUT_FORMAT, Status.ALREADY_SET);
			outputFormat = ThumbnailParameter.ORIGINAL_FORMAT;
			return this;
		}
		
		/**
		 * Sets whether or not to use the Exif metadata when orienting the
		 * thumbnail.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @param useExifOrientation	{@code true} if the Exif metadata
		 * 								should be used to determine the
		 * 								orientation of the thumbnail,
		 * 								{@code false} otherwise.
		 * @return						Reference to this object.
		 * @since	0.4.3
		 */
		public Builder<T> useExifOrientation(boolean useExifOrientation) {
			updateStatus(Properties.USE_EXIF_ORIENTATION, Status.ALREADY_SET);
			this.useExifOrientation = useExifOrientation;
			return this;
		}
		
		/**
		 * Indicates that the output format should be determined from the
		 * available information when writing the thumbnail image.
		 * <p>
		 * For example, calling this method will cause the output format to be
		 * determined from the file extension if thumbnails are written to
		 * files.
		 * <p>
		 * Calling this method multiple times will result in an
		 * {@link IllegalStateException} to be thrown.
		 * 
		 * @return				Reference to this object.
		 * @since	0.4.0
		 */
		public Builder<T> determineOutputFormat() {
			updateStatus(Properties.OUTPUT_FORMAT, Status.ALREADY_SET);
			outputFormat = ThumbnailParameter.DETERMINE_FORMAT;
			return this;
		}
		
		private boolean isOutputFormatNotSet() {
			return outputFormat == null || ThumbnailParameter.DETERMINE_FORMAT.equals(outputFormat);
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
		public Builder<T> outputFormatType(String formatType) {
			/*
			 * If the output format is the original format, and the format type
			 * is being specified, it's going to be likely that the specified
			 * type will not be present in all the formats, so we'll disallow
			 * it. (e.g. setting type to "JPEG", and if the original formats
			 * were JPEG and PNG, then we'd have a problem.
			 */
			if (formatType != ThumbnailParameter.DEFAULT_FORMAT_TYPE
					&& isOutputFormatNotSet()) {
				throw new IllegalArgumentException(
						"Cannot set the format type if a specific output " +
						"format has not been specified."
				);
			}
			
			if (!ThumbnailatorUtils.isSupportedOutputFormatType(outputFormat, formatType)) {
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
			
			if (!statusMap.containsKey(Properties.OUTPUT_FORMAT)) {
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
		public Builder<T> watermark(Watermark w) {
			if (w == null) {
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
		public Builder<T> watermark(BufferedImage image) {
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
		public Builder<T> watermark(BufferedImage image, float opacity) {
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
		public Builder<T> watermark(Position position, BufferedImage image, float opacity) {
			filterPipeline.add(new Watermark(position, image, opacity));
			return this;
		}

		/**
		 * Sets the image, opacity, position and insets for the watermark to
		 * apply on to the thumbnail.
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
		 * @param insets		Inset size around the watermark.
		 * 						Cannot be negative.
		 * @return				Reference to this object.
		 */
		public Builder<T> watermark(Position position, BufferedImage image, float opacity, int insets) {
			filterPipeline.add(new Watermark(position, image, opacity, insets));
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
		public Builder<T> rotate(double angle) {
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
		public Builder<T> addFilter(ImageFilter filter) {
			if (filter == null) {
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
		public Builder<T> addFilters(List<ImageFilter> filters) {
			if (filters == null) {
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
		private void checkReadiness() {
			for (Map.Entry<Properties, Status> s : statusMap.entrySet()) {
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
		 * @param mode		The scaling mode to use to create thumbnails.
		 * @return			The {@link Resizer} which is suitable for the
		 * 					specified scaling mode and builder state.
		 */
		private Resizer makeResizer(ScalingMode mode) {

			Map<RenderingHints.Key, Object> hints =
				new HashMap<RenderingHints.Key, Object>();
			
			hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION, alphaInterpolation.getValue());
			hints.put(RenderingHints.KEY_DITHERING, dithering.getValue());
			hints.put(RenderingHints.KEY_ANTIALIASING, antialiasing.getValue());
			hints.put(RenderingHints.KEY_RENDERING, rendering.getValue());
			
			if (mode == ScalingMode.BILINEAR) {
				return new BilinearResizer(hints);
			} else if (mode == ScalingMode.BICUBIC) {
				return new BicubicResizer(hints);
			} else if (mode == ScalingMode.PROGRESSIVE_BILINEAR) {
				return new ProgressiveBilinearResizer(hints);
			} else {
				return new ProgressiveBilinearResizer(hints);
			}
		}

		private void prepareResizerFactory() {
			/*
			 * If the scalingMode has been set, then use scalingMode to obtain
			 * a resizer, else, use the resizer field.
			 */
			if (statusMap.get(Properties.SCALING_MODE) == Status.ALREADY_SET) {
				this.resizerFactory = new FixedResizerFactory(makeResizer(scalingMode));
			}
		}

		/**
		 * Returns a {@link ThumbnailParameter} from the current builder state.
		 * 
		 * @return			A {@link ThumbnailParameter} from the current
		 * 					builder state.
		 */
		private ThumbnailParameter makeParam() {
			prepareResizerFactory();
			
			int imageTypeToUse = imageType;
			if (imageType == IMAGE_TYPE_UNSPECIFIED) {
				imageTypeToUse = ThumbnailParameter.ORIGINAL_IMAGE_TYPE;
			}

			/*
			 * croppingPosition being non-null means that a crop should
			 * take place.
			 */
			if (croppingPosition != null) {
				filterPipeline.addFirst(new Canvas(width, height, croppingPosition));
			}
			
			if (Double.isNaN(scaleWidth)) {
				// If the dimensions were specified, do the following.
				
				// Check that at least one dimension is specified.
				// If it's not, it's a bug.
				if (
						width == DIMENSION_NOT_SPECIFIED &&
						height == DIMENSION_NOT_SPECIFIED
				) {
					throw new IllegalStateException(
							"The width or height must be specified. If this " +
							"exception is thrown, it is due to a bug in the " +
							"Thumbnailator library."
					);
				}
				
				// Set the unspecified dimension to a default value.
				if (width == DIMENSION_NOT_SPECIFIED) {
					width = Integer.MAX_VALUE;
				}
				if (height == DIMENSION_NOT_SPECIFIED) {
					height = Integer.MAX_VALUE;
				}
				
				return new ThumbnailParameter(
						new Dimension(width, height),
						sourceRegion,
						keepAspectRatio,
						outputFormat,
						outputFormatType,
						outputQuality,
						imageTypeToUse,
						filterPipeline.getFilters(),
						resizerFactory,
						fitWithinDimenions,
						useExifOrientation
				);

			} else {
				// If the scaling factor was specified
				return new ThumbnailParameter(
						scaleWidth,
						scaleHeight,
						sourceRegion,
						keepAspectRatio,
						outputFormat,
						outputFormatType,
						outputQuality,
						imageTypeToUse,
						filterPipeline.getFilters(),
						resizerFactory,
						fitWithinDimenions,
						useExifOrientation
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
		public Iterable<BufferedImage> iterableBufferedImages() {
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
		public List<BufferedImage> asBufferedImages() throws IOException {
			checkReadiness();
			
			List<BufferedImage> thumbnails = new ArrayList<BufferedImage>();
			
			// Create thumbnails
			for (ImageSource<T> source : sources) {
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
		public BufferedImage asBufferedImage() throws IOException {
			checkReadiness();
			
			Iterator<ImageSource<T>> iter = sources.iterator();
			ImageSource<T> source = iter.next();
			
			if (iter.hasNext()) {
				throw new IllegalArgumentException("Cannot create one thumbnail from multiple original images.");
			}
			
			BufferedImageSink destination = new BufferedImageSink();
			
			Thumbnailator.createThumbnail(
				new SourceSinkThumbnailTask<T, BufferedImage>(makeParam(), source, destination)
			);
				
			return destination.getSink();
		}
		
		/**
		 * Creates the thumbnails and stores them to the files, and returns
		 * a {@link List} of {@link File}s to the thumbnails.
		 * <p>
		 * When the destination file exists, and overwriting files has been
		 * disabled by calling the {@link #allowOverwrite(boolean)} method
		 * with {@code false}, then the thumbnail with the destination file
		 * already existing will not be written and the corresponding
		 * {@code File} object will not be included in the {@code List} returned
		 * by this method.
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
		 * @since 	0.3.7
		 */
		public List<File> asFiles(Iterable<File> iterable) throws IOException {
			checkReadiness();
			
			if (iterable == null) {
				throw new NullPointerException("File name iterable is null.");
			}
			
			List<File> destinationFiles = new ArrayList<File>();
			
			Iterator<File> filenameIter = iterable.iterator();
			
			for (ImageSource<T> source : sources) {
				if (!filenameIter.hasNext()) {
					throw new IndexOutOfBoundsException(
							"Not enough file names provided by iterator."
					);
				}
				
				ThumbnailParameter param = makeParam();
				
				FileImageSink destination = new FileImageSink(filenameIter.next(), allowOverwrite);
				
				try {
					Thumbnailator.createThumbnail(
							new SourceSinkThumbnailTask<T, File>(param, source, destination)
					);
					
					destinationFiles.add(destination.getSink());

				} catch (IllegalArgumentException e) {
					/*
					 * Handle the IllegalArgumentException which is thrown when
					 * the destination file already exists by not adding the
					 * current file to the destinationFiles list.
					 */
				}
			}
			
			return destinationFiles;
		}
		
		/**
		 * Creates the thumbnails and stores them to the files.
		 * <p>
		 * When the destination file exists, and overwriting files has been
		 * disabled by calling the {@link #allowOverwrite(boolean)} method
		 * with {@code false}, then the thumbnail with the destination file
		 * already existing will not be written.
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
		 * @since 	0.3.7
		 */
		public void toFiles(Iterable<File> iterable) throws IOException {
			asFiles(iterable);
		}
		
		/**
		 * Creates thumbnails and stores them to files using the
		 * {@link Rename} function to determine the filenames. The thubnail
		 * files are returned as a {@link List}.
		 * <p>
		 * When the destination file exists, and overwriting files has been
		 * disabled by calling the {@link #allowOverwrite(boolean)} method
		 * with {@code false}, then the thumbnail with the destination file
		 * already existing will not be written and the corresponding
		 * {@code File} object will not be included in the {@code List} returned
		 * by this method.
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
		 * @since 	0.3.7
		 */
		public List<File> asFiles(Rename rename) throws IOException {
			return asFiles(null, rename);
		}
		
		/**
		 * Creates thumbnails and stores them to files in the directory
		 * specified by the given {@link File} object, and using the
		 * {@link Rename} function to determine the filenames. The thubnail
		 * files are returned as a {@link List}.
		 * <p>
		 * When the destination file exists, and overwriting files has been
		 * disabled by calling the {@link #allowOverwrite(boolean)} method
		 * with {@code false}, then the thumbnail with the destination file
		 * already existing will not be written and the corresponding
		 * {@code File} object will not be included in the {@code List} returned
		 * by this method.
		 * <p>
		 * Extra caution should be taken when using this method, as there are
		 * no protections in place to prevent file name collisions resulting
		 * from creating thumbnails from files in separate directories but
		 * having the same name. In such a case, the behavior will be depend
		 * on the behavior of the {@link #allowOverwrite(boolean)} as
		 * described in the previous paragraph.
		 * <p>
		 * To call this method, the thumbnails must have been creates from
		 * files by calling the {@link Thumbnails#of(File...)} method.
		 *
		 * @param destinationDir	The destination directory to which the
		 * 							thumbnails should be written to.
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
		 * @throws IllegalArgumentException		If the destination directory
		 * 										is not a directory.
		 * @since 	0.4.7
		 */
		public List<File> asFiles(File destinationDir, Rename rename) throws IOException {
			checkReadiness();
			
			if (rename == null) {
				throw new NullPointerException("Rename is null.");
			}
			
			if (destinationDir != null && !destinationDir.isDirectory()) {
				throw new IllegalArgumentException("Given destination is not a directory.");
			}

			List<File> destinationFiles = new ArrayList<File>();
			
			for (ImageSource<T> source : sources) {
				if (!(source instanceof FileImageSource)) {
					throw new IllegalStateException("Cannot create thumbnails to files if original images are not from files.");
				}
				
				ThumbnailParameter param = makeParam();
				
				File f = ((FileImageSource)source).getSource();
				
				File actualDestDir = destinationDir == null ? f.getParentFile() : destinationDir;
				File destinationFile = new File(actualDestDir, rename.apply(f.getName(), param));
				
				FileImageSink destination = new FileImageSink(destinationFile, allowOverwrite);
				
				try {
					Thumbnailator.createThumbnail(
							new SourceSinkThumbnailTask<T, File>(param, source, destination)
					);
					
					destinationFiles.add(destination.getSink());

				} catch (IllegalArgumentException e) {
					/*
					 * Handle the IllegalArgumentException which is thrown when
					 * the destination file already exists by not adding the
					 * current file to the destinationFiles list.
					 */
				}
			}
			
			return destinationFiles;
		}

		/**
		 * Creates thumbnails and stores them to files using the
		 * {@link Rename} function to determine the filenames.
		 * <p>
		 * When the destination file exists, and overwriting files has been
		 * disabled by calling the {@link #allowOverwrite(boolean)} method
		 * with {@code false}, then the thumbnail with the destination file
		 * already existing will not be written.
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
		 * @since 	0.3.7
		 */
		public void toFiles(Rename rename) throws IOException {
			toFiles(null, rename);
		}
		
		/**
		 * Creates thumbnails and stores them to files in the directory
		 * specified by the given {@link File} object, and using the
		 * {@link Rename} function to determine the filenames.
		 * <p>
		 * When the destination file exists, and overwriting files has been
		 * disabled by calling the {@link #allowOverwrite(boolean)} method
		 * with {@code false}, then the thumbnail with the destination file
		 * already existing will not be written.
		 * <p>
		 * Extra caution should be taken when using this method, as there are
		 * no protections in place to prevent file name collisions resulting
		 * from creating thumbnails from files in separate directories but
		 * having the same name. In such a case, the behavior will be depend
		 * on the behavior of the {@link #allowOverwrite(boolean)} as
		 * described in the previous paragraph.
		 * <p>
		 * To call this method, the thumbnails must have been creates from
		 * files by calling the {@link Thumbnails#of(File...)} method.
		 * 
		 * @param destinationDir	The destination directory to which the
		 * 							thumbnails should be written to.
		 * @param rename			The rename function which is used to
		 * 							determine the filenames of the thumbnail
		 * 							files to write.
		 * @throws IOException		If a problem occurs while reading the
		 * 							original images or writing the thumbnails
		 * 							to files.
		 * 							thumbnails to files.
		 * @throws IllegalStateException		If the original images are not
		 * 										from files.
		 * @throws IllegalArgumentException		If the destination directory
		 * 										is not a directory.
		 * @since 	0.4.7
		 */
		public void toFiles(File destinationDir, Rename rename) throws IOException {
			asFiles(destinationDir, rename);
		}

		/**
		 * Create a thumbnail and writes it to a {@link File}.
		 * <p>
		 * When the destination file exists, and overwriting files has been
		 * disabled by calling the {@link #allowOverwrite(boolean)} method
		 * with {@code false}, then an {@link IllegalArgumentException} will
		 * be thrown.
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
		 * 										are	specified, or if the
		 * 										destination file exists, and
		 * 										overwriting files is disabled.
		 */
		public void toFile(File outFile) throws IOException {
			checkReadiness();
			
			Iterator<ImageSource<T>> iter = sources.iterator();
			ImageSource<T> source = iter.next();
			
			if (iter.hasNext()) {
				throw new IllegalArgumentException("Cannot output multiple thumbnails to one file.");
			}
			
			FileImageSink destination = new FileImageSink(outFile, allowOverwrite);
			
			Thumbnailator.createThumbnail(
					new SourceSinkThumbnailTask<T, File>(makeParam(), source, destination)
			);
		}
		
		/**
		 * Create a thumbnail and writes it to a {@link File}.
		 * <p>
		 * When the destination file exists, and overwriting files has been
		 * disabled by calling the {@link #allowOverwrite(boolean)} method
		 * with {@code false}, then an {@link IllegalArgumentException} will
		 * be thrown.
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
		 * 										are	specified, or if the
		 * 										destination file exists, and
		 * 										overwriting files is disabled.
		 */
		public void toFile(String outFilepath) throws IOException {
			checkReadiness();
			
			Iterator<ImageSource<T>> iter = sources.iterator();
			ImageSource<T> source = iter.next();
			
			if (iter.hasNext()) {
				throw new IllegalArgumentException("Cannot output multiple thumbnails to one file.");
			}
			
			FileImageSink destination = new FileImageSink(outFilepath, allowOverwrite);
			
			Thumbnailator.createThumbnail(
					new SourceSinkThumbnailTask<T, File>(makeParam(), source, destination)
			);
		}
		
		/**
		 * Create a thumbnail and writes it to a {@link OutputStream}.
		 * <p>
		 * To call this method, the thumbnail must have been created from a
		 * single source.
		 * <p>
		 * Note that the {@link OutputStream#close()} method will not be
		 * called upon the completion of the thumbnail being written to the
		 * {@link OutputStream}.
		 * 
		 * @param os				The output stream to which the thumbnail
		 * 							is to be written to.
		 * @throws IOException		If a problem occurs while reading the
		 * 							original images or writing the thumbnails.
		 * @throws IllegalArgumentException		If multiple original image files
		 * 										are	specified.
		 * @throws IllegalStateException		If the output format has not
		 * 										been specified through the
		 * 										{@link #outputFormat(String)}
		 * 										method.
		 */
		public void toOutputStream(OutputStream os) throws IOException {
			checkReadiness();
			
			Iterator<ImageSource<T>> iter = sources.iterator();
			ImageSource<T> source = iter.next();
			
			if (iter.hasNext()) {
				throw new IllegalArgumentException("Cannot output multiple thumbnails to a single OutputStream.");
			}
			
			/*
			 * if the image is from a BufferedImage, then we require that the
			 * output format be set. (or else, we can't tell what format to
			 * output as!)
			 */
			if (source instanceof BufferedImageSource) {
				if (isOutputFormatNotSet()) {
					throw new IllegalStateException(
							"Output format not specified."
					);
				}
			}
			
			OutputStreamImageSink destination = new OutputStreamImageSink(os);
			
			Thumbnailator.createThumbnail(
					new SourceSinkThumbnailTask<T, OutputStream>(makeParam(), source, destination)
			);
		}
		
		/**
		 * Creates the thumbnails and writes them to {@link OutputStream}s
		 * provided by the {@link Iterable}.
		 * <p>
		 * Note that the {@link OutputStream#close()} method will not be
		 * called upon the completion of the thumbnail being written to the
		 * {@link OutputStream}.
		 * 
		 * @param iterable			An {@link Iterable} which returns an
		 * 							{@link Iterator} which returns the
		 * 							output stream which should be assigned to
		 * 							each thumbnail.
		 * @throws IOException		If a problem occurs while reading the
		 * 							original images or writing the thumbnails.
		 * @throws IllegalStateException		If the output format has not
		 * 										been specified through the
		 * 										{@link #outputFormat(String)}
		 * 										method.
		 */
		public void toOutputStreams(Iterable<? extends OutputStream> iterable) throws IOException {
			checkReadiness();
			
			if (iterable == null) {
				throw new NullPointerException("OutputStream iterable is null.");
			}
			
			Iterator<? extends OutputStream> osIter = iterable.iterator();
			
			for (ImageSource<T> source : sources) {
				/*
				 * if the image is from a BufferedImage, then we require that the
				 * output format be set. (or else, we can't tell what format to
				 * output as!)
				 */
				if (source instanceof BufferedImageSource) {
					if (isOutputFormatNotSet()) {
						throw new IllegalStateException(
								"Output format not specified."
						);
					}
				}
				
				if (!osIter.hasNext()) {
					throw new IndexOutOfBoundsException(
							"Not enough file names provided by iterator."
					);
				}
				
				OutputStreamImageSink destination = new OutputStreamImageSink(osIter.next());
				
				Thumbnailator.createThumbnail(
						new SourceSinkThumbnailTask<T, OutputStream>(makeParam(), source, destination)
				);
			}
		}
	}
}
