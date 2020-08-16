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

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.builders.ThumbnailParameterBuilder;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.makers.FixedSizeThumbnailMaker;
import net.coobird.thumbnailator.makers.ScaledThumbnailMaker;
import net.coobird.thumbnailator.name.Rename;
import net.coobird.thumbnailator.resizers.DefaultResizerFactory;
import net.coobird.thumbnailator.resizers.Resizer;
import net.coobird.thumbnailator.tasks.ThumbnailTask;

/**
 * This class provides static utility methods which perform generation of
 * thumbnails using Thumbnailator.
 * <p>
 * When images are resized, the aspect ratio of the images are preserved.
 * 
 * @author coobird
 *
 */
public final class Thumbnailator {
	/**
	 * This class is not intended to be instantiated.
	 */
	private Thumbnailator() {}
	
	/**
	 * Creates a thumbnail from parameters specified in a {@link ThumbnailTask}.
	 * 
	 * @param task				A {@link ThumbnailTask} to execute.
	 * @throws IOException		Thrown when a problem occurs when creating a
	 * 							thumbnail.
	 */
	public static void createThumbnail(ThumbnailTask<?, ?> task) throws IOException {
		ThumbnailParameter param = task.getParam();
		
		// Obtain the original image.
		BufferedImage sourceImage = task.read();

		// Decide the image type of the destination image.
		int imageType = param.getType();
		/*
		 * If the imageType indicates that the image type of the original image
		 * should be used in the thumbnail, then obtain the image type of the
		 * original.
		 * 
		 * If the original type is a custom type, then the default image type
		 * will be used.
		 */
		if (param.useOriginalImageType()) {
			int imageTypeToUse = sourceImage.getType();
			
			if (imageTypeToUse == BufferedImage.TYPE_CUSTOM) {
				imageType = ThumbnailParameter.DEFAULT_IMAGE_TYPE;
			} else {
				imageType = sourceImage.getType();
			}
		}
		
		BufferedImage destinationImage;
		
		if (param.getSize() != null) {
			// Get the dimensions of the original and thumbnail images.
			int destinationWidth = param.getSize().width;
			int destinationHeight = param.getSize().height;
			
			// Create the thumbnail.
			destinationImage =
				new FixedSizeThumbnailMaker()
					.size(destinationWidth, destinationHeight)
					.keepAspectRatio(param.isKeepAspectRatio())
					.fitWithinDimensions(param.fitWithinDimenions())
					.imageType(imageType)
					.resizerFactory(param.getResizerFactory())
					.make(sourceImage);

		} else if (!Double.isNaN(param.getWidthScalingFactor())) {
			// Create the thumbnail.
			destinationImage =
				new ScaledThumbnailMaker()
					.scale(param.getWidthScalingFactor(), param.getHeightScalingFactor())
					.imageType(imageType)
					.resizerFactory(param.getResizerFactory())
					.make(sourceImage);

		} else {
			throw new IllegalStateException("Parameters to make thumbnail" +
					" does not have scaling factor nor thumbnail size specified.");
		}
		
		// Perform the image filters
		for (ImageFilter filter : param.getImageFilters()) {
			destinationImage = filter.apply(destinationImage);
		}
		
		// Write the thumbnail image to the destination.
		task.write(destinationImage);
		
		sourceImage.flush();
		destinationImage.flush();
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
	) {

		validateDimensions(width, height);
		
		Dimension imgSize = new Dimension(img.getWidth(), img.getHeight());
		Dimension thumbnailSize = new Dimension(width, height);
		Resizer resizer =
			DefaultResizerFactory.getInstance()
					.getResizer(imgSize, thumbnailSize);
		
		BufferedImage thumbnailImage =
			new FixedSizeThumbnailMaker(width, height, true, true)
					.resizer(resizer)
					.make(img);
		
		return thumbnailImage;
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
	) throws IOException {

		validateDimensions(width, height);
		
		if (inFile == null) {
			throw new NullPointerException("Input file is null.");
		} else if (outFile == null) {
			throw new NullPointerException("Output file is null.");
		}
		
		if (!inFile.exists()) {
			throw new IOException("Input file does not exist.");
		}

		Thumbnails.of(inFile)
			.size(width, height)
			.toFile(outFile);
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
	) throws IOException {

		validateDimensions(width, height);
		
		if (f == null) {
			throw new NullPointerException("Input file is null.");
		}
		
		return Thumbnails.of(f).size(width, height).asBufferedImage();
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
	) {

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

	/**
	 * Creates a thumbnail from image data streamed from an {@link InputStream}
	 * and streams the data out to an {@link OutputStream}.
	 * <p>
	 * The thumbnail will be stored in the same format as the original image.
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
	) throws IOException {

		Thumbnailator.createThumbnail(
				is, os, ThumbnailParameter.ORIGINAL_FORMAT, width, height);
	}
	
	/**
	 * Creates a thumbnail from image data streamed from an {@link InputStream}
	 * and streams the data out to an {@link OutputStream}, with the specified
	 * format for the output data.
	 * 
	 * @param is			The {@link InputStream} from which to obtain
	 * 						image data.
	 * @param os			The {@link OutputStream} to send thumbnail data to.
	 * @param format		The image format to use to store the thumbnail data.
	 * @param width			The width of the thumbnail.
	 * @param height		The height of the thumbnail.
	 * @throws IOException	Thrown when a problem occurs when reading from
	 * 						{@code File} representing an image file.
	 * @throws IllegalArgumentException		If the specified output format is
	 * 										not supported.
	 */
	public static void createThumbnail(
			InputStream is,
			OutputStream os,
			String format,
			int width,
			int height
	) throws IOException {

		validateDimensions(width, height);
		
		if (is == null) {
			throw new NullPointerException("InputStream is null.");
		} else if (os == null) {
			throw new NullPointerException("OutputStream is null.");
		}

		Thumbnails.of(is)
			.size(width, height)
			.outputFormat(format)
			.toOutputStream(os);
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
	 * @deprecated		This method has been deprecated in favor of using the
	 * 					{@link Thumbnails#fromFiles(Iterable)} interface.
	 * 					This method will be removed in 0.5.0, and will not be
	 * 					further maintained.
	 */
	public static Collection<File> createThumbnailsAsCollection(
			Collection<? extends File> files,
			Rename rename,
			int width,
			int height
	) throws IOException {

		validateDimensions(width, height);
		
		if (files == null) {
			throw new NullPointerException("Collection of Files is null.");
		}
		if (rename == null) {
			throw new NullPointerException("Rename is null.");
		}
		
		ArrayList<File> resultFiles = new ArrayList<File>();
		
		ThumbnailParameter param =
			new ThumbnailParameterBuilder()
				.size(width, height)
				.build();
		
		for (File inFile : files) {
			File outFile =
				new File(inFile.getParent(), rename.apply(inFile.getName(), param));
			
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
	 * @deprecated		This method has been deprecated in favor of using the
	 * 					{@link Thumbnails#fromFiles(Iterable)} interface.
	 * 					This method will be removed in 0.5.0, and will not be
	 * 					further maintained.
	 */
	public static void createThumbnails(
			Collection<? extends File> files,
			Rename rename,
			int width,
			int height
	) throws IOException {

		validateDimensions(width, height);
		
		if (files == null) {
			throw new NullPointerException("Collection of Files is null.");
		}
		if (rename == null) {
			throw new NullPointerException("Rename is null.");
		}
		
		ThumbnailParameter param =
			new ThumbnailParameterBuilder()
				.size(width, height)
				.build();
		
		for (File inFile : files) {
			File outFile =
				new File(inFile.getParent(), rename.apply(inFile.getName(), param));
			
			createThumbnail(inFile, outFile, width, height);
		}
	}

	/**
	 * Performs validation on the specified dimensions.
	 * <p>
	 * If any of the dimensions are less than or equal to 0, an
	 * {@code IllegalArgumentException} is thrown with an message specifying the
	 * reason for the exception.
	 * <p>
	 * This method is used to perform a check on the output dimensions of a
	 * thumbnail for the {@link Thumbnailator#createThumbnail} methods.
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
}
