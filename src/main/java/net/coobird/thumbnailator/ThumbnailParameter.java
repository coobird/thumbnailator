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
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.resizers.FixedResizerFactory;
import net.coobird.thumbnailator.geometry.Region;
import net.coobird.thumbnailator.resizers.Resizer;
import net.coobird.thumbnailator.resizers.ResizerFactory;

/**
 * This class is used to specify the parameters to use when creating a thumbnail.
 * <p>
 * An instance of {@link ThumbnailParameter} is mutable -- it should not be
 * reused for multiple resizes, as the parameters can change behind the scenes
 * as the resizing process progresses.
 * 
 * @author coobird
 *
 */
public class ThumbnailParameter {
	/**
	 * A constant used to denote that the output format of the thumbnail should
	 * be the same as the format of the original image.
	 */
	public static final String ORIGINAL_FORMAT = null;

	/**
	 * A constant used to denote that the output format of the thumbnail should
	 * be the determined from available information such as the file name of
	 * the thumbnail.
	 * <p>
	 * If a suitable output format cannot be determined, then the implementation
	 * should behave as if {@link #ORIGINAL_FORMAT} was specified.
	 */
	public static final String DETERMINE_FORMAT = "\0";
	
	/**
	 * A constant used to denote that the output format type of the thumbnail
	 * should be the default type of the codec being used.
	 */
	public static final String DEFAULT_FORMAT_TYPE = null;
	
	/**
	 * A constant used to denote that the default compression quality settings
	 * should be used when creating the thumbnail.
	 */
	public static final float DEFAULT_QUALITY = Float.NaN;
	
	/**
	 * A constant used to denote that the image type of the original image
	 * should be used when creating the thumbnail.
	 */
	public static final int ORIGINAL_IMAGE_TYPE = -1;
	
	/**
	 * A constant used to denote that the default image type should be used
	 * when creating the thumbnail.
	 */
	public static final int DEFAULT_IMAGE_TYPE = BufferedImage.TYPE_INT_ARGB;
	
	/**
	 * The thumbnail size.
	 * <p>
	 * If this field is set, then the {@link #scalingFactor} field will be set
	 * as {@link Double#NaN} to indicate that it is not set.
	 */
	private final Dimension thumbnailSize;
	
	/**
	 * The scaling factor to apply to the width when creating a thumbnail from
	 * the original image.
	 * <p>
	 * If this field is set, then the {@link #thumbnailSize} field will be set
	 * as {@code null} to indicate that it is not set.
	 */
	private final double widthScalingFactor;
	
	/**
	 * The scaling factor to apply to the height when creating a thumbnail from
	 * the original image.
	 * <p>
	 * If this field is set, then the {@link #thumbnailSize} field will be set
	 * as {@code null} to indicate that it is not set.
	 */
	private final double heightScalingFactor;
	
	/**
	 * Indicated whether or not the thumbnail should retain the aspect ratio
	 * the same as the original image when the aspect ratio of the desired
	 * dimensions for the thumbnail does not match the ratio of the original
	 * image.
	 */
	private final boolean keepAspectRatio;
	
	/**
	 * The output format for the thumbnail.
	 * <p>
	 * A value of {@link ThumbnailParameter#ORIGINAL_FORMAT} indicates that the
	 * image format of the original image should be used as the output format.
	 * <p>
	 * A value of {@link ThumbnailParameter#DETERMINE_FORMAT} indicates that the
	 * output format of the thumbnail should be the determined from the
	 * information available, such as the output file name of the thumbnail.
	 */
	private final String outputFormat;
	
	/**
	 * The output format type for the thumbnail.
	 * <p>
	 * A value of {@link ThumbnailParameter#DEFAULT_FORMAT_TYPE} indicates
	 * that the default type of the specified compression format should be used
	 * as the output format type.
	 */
	private final String outputFormatType;
	
	/**
	 * The output quality settings which will be used by the image compressor.
	 * <p>
	 * An acceptable value is in the range of {@code 0.0f} to {@code 1.0f},
	 * where {@code 0.0f} is for the lowest quality setting and {@code 1.0f} for
	 * the highest quality setting.
	 * <p>
	 * A value of {@link Float#NaN} indicates that the default quality settings
	 * of the output codec should be used.
	 */
	private final float outputQuality;
	
	/**
	 * The image type of the {@code BufferedImage} used for the thumbnail.
	 */
	private final int imageType;
	
	/**
	 * {@link ImageFilter}s to apply to the thumbnail.
	 * <p>
	 * The filters will be applied after the original image has been resized.
	 */
	private final List<ImageFilter> filters;
	
	/**
	 * The {@link ResizerFactory} for obtaining a {@link Resizer} that is
	 * to be used when performing an image resizing operation.
	 */
	private final ResizerFactory resizerFactory;

	/**
	 * The region of the source image to use when creating a thumbnail.
	 * <p>
	 * A value of {@code null} represents that the entire source image should
	 * be used to create the thumbnail.
	 */
	private final Region sourceRegion;
	
	/**
	 * Whether or not to fit the thumbnail within the specified dimensions.
	 * <p>
	 * If {@code true} is specified, then the thumbnail will be sized to fit
	 * within the specified dimensions, if the thumbnail is going to exceed
	 * those dimensions.
	 */
	private final boolean fitWithinDimensions;
	
	/**
	 * Whether or not to use the Exif orientation metadata to orient the
	 * thumbnails.
	 */
	private final boolean useExifOrientation;
	
	/**
	 * Private constructor which sets all the required fields, and performs
	 * validation of the given arguments.
	 * <p>
	 * This constructor is to be called from all the public constructors.
	 * 
	 * @param thumbnailSize		The size of the thumbnail to generate.
	 * @param widthScalingFactor	The scaling factor to apply to the width
	 * 								when creating a	thumbnail from the original
	 * 								image.
	 * @param heightScalingFactor	The scaling factor to apply to the height
	 * 								when creating a	thumbnail from the original
	 * 								image.
	 * @param sourceRegion		The region of the source image to use when
	 * 							creating a thumbnail.
	 * 							A value of {@code null} indicates that the
	 * 							entire source image should be used to create
	 * 							the thumbnail.
	 * @param keepAspectRatio	Indicates whether or not the thumbnail should
	 * 							maintain the aspect ratio of the original image.
	 * @param outputFormat		A string indicating the compression format
	 * 							that should be applied on the thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#ORIGINAL_FORMAT}
	 * 							should be provided if the same image format as
	 * 							the original should	be used for the thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#DETERMINE_FORMAT}
	 * 							should be provided if the output format of the
	 * 							thumbnail should be the determined from the
	 * 							information available, such as the output file
	 * 							name of the thumbnail.
	 * @param outputFormatType	A string indicating the compression type that
	 * 							should be used when writing the thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#DEFAULT_FORMAT_TYPE}
	 * 							should be provided if the thumbnail should be
	 * 							written using the default compression type of
	 * 							the codec specified in {@code outputFormat}.
	 * @param outputQuality		A value from {@code 0.0f} to {@code 1.0f} which
	 * 							indicates the quality setting to use for the
	 * 							compression of the thumbnail. {@code 0.0f}
	 * 							indicates the lowest quality, {@code 1.0f}
	 * 							indicates the highest quality setting for the
	 * 							compression.
	 * 							{@link ThumbnailParameter#DEFAULT_QUALITY}
	 * 							should be specified when the codec's default
	 * 							compression quality settings should be used.
	 * @param imageType 		The {@link BufferedImage} image type of the
	 * 							thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#DEFAULT_IMAGE_TYPE}
	 *							should be specified when the default image
	 *							type should be used when creating the thumbnail.
	 * @param filters			The {@link ImageFilter}s to apply to the
	 * 							thumbnail.
	 * 							A value of {@code null} will be recognized as
	 * 							no filters are to be applied.
	 * 							The filters are applied after the original
	 * 							image has been resized.
	 * @param resizerFactory	The {@link ResizerFactory} for obtaining a
	 * 							{@link Resizer} that is to be used when
	 * 							performing an image resizing operation.
	 * @param fitWithinDimensions	Whether or not to fit the thumbnail within
	 * 								the specified dimensions.
	 * 								<p>
	 * 								If {@code true} is specified, then the
	 * 								thumbnail will be sized to fit within the
	 * 								specified dimensions, if the thumbnail is
	 * 								going to exceed those dimensions.
	 * @param useExifOrientation	Whether or not to use the Exif metadata to
	 * 								determine the orientation of the thumbnail.
	 * 								<p>
	 * 								If {@code true} is specified, then the
	 * 								Exif metadata will be used to determine
	 * 								the orientation of the thumbnail.
	 * 
	 * @throws IllegalArgumentException 	If the scaling factor is not a
	 * 										rational number or is less than or
	 * 										equal to 0, or if the
	 * 										{@link ResizerFactory} is null.
	 */
	private ThumbnailParameter(
			Dimension thumbnailSize,
			double widthScalingFactor,
			double heightScalingFactor,
			Region sourceRegion,
			boolean keepAspectRatio,
			String outputFormat,
			String outputFormatType,
			float outputQuality,
			int imageType,
			List<ImageFilter> filters,
			ResizerFactory resizerFactory,
			boolean fitWithinDimensions,
			boolean useExifOrientation
	) {
		// The following 2 fields are set by the public constructors.
		this.thumbnailSize = thumbnailSize;
		this.widthScalingFactor = widthScalingFactor;
		this.heightScalingFactor = heightScalingFactor;
		
		this.keepAspectRatio = keepAspectRatio;
		
		this.sourceRegion = sourceRegion;
		this.outputFormat = outputFormat;
		this.outputFormatType = outputFormatType;
		
		/*
		 * Note:
		 * The value of DEFAULT_QUALITY is Float.NaN which cannot be compared
		 * by using the regular == operator. Therefore, to check that NaN is
		 * being used, one must use the Float.NaN method.
		 */
		if ( (outputQuality < 0.0f || outputQuality > 1.0f) &&
				!Float.isNaN(outputQuality) ) {
			throw new IllegalArgumentException("The output quality must be " +
					"between 0.0f and 1.0f, or Float.NaN to use the default " +
			"compression quality of codec being used.");
		}
		
		this.outputQuality = outputQuality;
		this.imageType = imageType;
		
		// Creating a new ArrayList, as `filters` should be mutable as of 0.4.3.
		if (filters == null) {
			this.filters = new ArrayList<ImageFilter>();
		} else {
			this.filters = new ArrayList<ImageFilter>(filters);
		}
				
		if (resizerFactory == null) {
			throw new IllegalArgumentException("Resizer cannot be null");
		}
		
		this.resizerFactory = resizerFactory;
		this.fitWithinDimensions = fitWithinDimensions;
		this.useExifOrientation = useExifOrientation;
	}
	
	/**
	 * Perform validations on the {@code thumbnailSize} field.
	 */
	private void validateThumbnailSize() {
		if (thumbnailSize == null) {
			throw new IllegalArgumentException("Thumbnail size cannot be null.");

		} else if (thumbnailSize.width < 0 || thumbnailSize.height < 0) {
			throw new IllegalArgumentException("Thumbnail dimensions must be greater than 0.");
		}
	}
	
	/**
	 * Perform validations on the {@code scalingFactor} field.
	 */
	private void validateScalingFactor() {
		if (widthScalingFactor <= 0.0 || heightScalingFactor <= 0.0) {
			throw new IllegalArgumentException("Scaling factor is less than or equal to 0.");

		} else if (Double.isNaN(widthScalingFactor) || Double.isInfinite(widthScalingFactor)) {
			throw new IllegalArgumentException("Scaling factor must be a rational number.");

		} else if (Double.isNaN(heightScalingFactor) || Double.isInfinite(heightScalingFactor)) {
			throw new IllegalArgumentException("Scaling factor must be a rational number.");
		}
	}
	
	/**
	 * Creates an object holding the parameters needed in order to make a
	 * thumbnail.
	 * 
	 * @param thumbnailSize		The size of the thumbnail to generate.
	 * @param sourceRegion		The region of the source image to use when
	 * 							creating a thumbnail.
	 * 							A value of {@code null} indicates that the
	 * 							entire source image should be used to create
	 * 							the thumbnail.
	 * @param keepAspectRatio	Indicates whether or not the thumbnail should
	 * 							maintain the aspect ratio of the original image.
	 * @param outputFormat		A string indicating the compression format
	 * 							that should be applied on the thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#ORIGINAL_FORMAT}
	 * 							should be provided if the same image format as
	 * 							the original should	be used for the thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#DETERMINE_FORMAT}
	 * 							should be provided if the output format of the
	 * 							thumbnail should be the determined from the
	 * 							information available, such as the output file
	 * 							name of the thumbnail.
	 * @param outputFormatType	A string indicating the compression type that
	 * 							should be used when writing the thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#DEFAULT_FORMAT_TYPE}
	 * 							should be provided if the thumbnail should be
	 * 							written using the default compression type of
	 * 							the codec specified in {@code outputFormat}.
	 * @param outputQuality		A value from {@code 0.0f} to {@code 1.0f} which
	 * 							indicates the quality setting to use for the
	 * 							compression of the thumbnail. {@code 0.0f}
	 * 							indicates the lowest quality, {@code 1.0f}
	 * 							indicates the highest quality setting for the
	 * 							compression.
	 * 							{@link ThumbnailParameter#DEFAULT_QUALITY}
	 * 							should be specified when the codec's default
	 * 							compression quality settings should be used.
	 * @param imageType 		The {@link BufferedImage} image type of the
	 * 							thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#DEFAULT_IMAGE_TYPE}
	 *							should be specified when the default image
	 *							type should be used when creating the thumbnail.
	 * @param filters			The {@link ImageFilter}s to apply to the
	 * 							thumbnail.
	 * 							A value of {@code null} will be recognized as
	 * 							no filters are to be applied.
	 * 							The filters are applied after the original
	 * 							image has been resized.
	 * @param resizer			The {@link Resizer} to use when performing the
	 * 							resizing operation to create a thumbnail.
	 * @param fitWithinDimensions	Whether or not to fit the thumbnail within
	 * 								the specified dimensions.
	 * 								<p>
	 * 								If {@code true} is specified, then the
	 * 								thumbnail will be sized to fit within the
	 * 								specified dimensions, if the thumbnail is
	 * 								going to exceed those dimensions.
	 * @param useExifOrientation	Whether or not to use the Exif metadata to
	 * 								determine the orientation of the thumbnail.
	 * 								<p>
	 * 								If {@code true} is specified, then the
	 * 								Exif metadata will be used to determine
	 * 								the orientation of the thumbnail.
	 *
	 * @throws IllegalArgumentException 	If size is {@code null} or if the
	 * 										dimensions are negative, or if the
	 * 										{@link Resizer} is null.
	 * @since	0.4.3
	 */
	public ThumbnailParameter(
			Dimension thumbnailSize,
			Region sourceRegion,
			boolean keepAspectRatio,
			String outputFormat,
			String outputFormatType,
			float outputQuality,
			int imageType,
			List<ImageFilter> filters,
			Resizer resizer,
			boolean fitWithinDimensions,
			boolean useExifOrientation
	) {
		this(
				thumbnailSize,
				Double.NaN,
				Double.NaN,
				sourceRegion,
				keepAspectRatio,
				outputFormat,
				outputFormatType,
				outputQuality,
				imageType,
				filters,
				new FixedResizerFactory(resizer),
				fitWithinDimensions,
				useExifOrientation
		);
		
		validateThumbnailSize();
	}
	
	/**
	 * Creates an object holding the parameters needed in order to make a
	 * thumbnail.
	 * 
	 * @param widthScalingFactor	The scaling factor to apply to the width
	 * 								when creating a	thumbnail from the original
	 * 								image.
	 * @param heightScalingFactor	The scaling factor to apply to the height
	 * 								when creating a	thumbnail from the original
	 * 								image.
	 * @param sourceRegion		The region of the source image to use when
	 * 							creating a thumbnail.
	 * 							A value of {@code null} indicates that the
	 * 							entire source image should be used to create
	 * 							the thumbnail.
	 * @param keepAspectRatio	Indicates whether or not the thumbnail should
	 * 							maintain the aspect ratio of the original image.
	 * @param outputFormat		A string indicating the compression format
	 * 							that should be applied on the thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#ORIGINAL_FORMAT}
	 * 							should be provided if the same image format as
	 * 							the original should	be used for the thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#DETERMINE_FORMAT}
	 * 							should be provided if the output format of the
	 * 							thumbnail should be the determined from the
	 * 							information available, such as the output file
	 * 							name of the thumbnail.
	 * @param outputFormatType	A string indicating the compression type that
	 * 							should be used when writing the thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#DEFAULT_FORMAT_TYPE}
	 * 							should be provided if the thumbnail should be
	 * 							written using the default compression type of
	 * 							the codec specified in {@code outputFormat}.
	 * @param outputQuality		A value from {@code 0.0f} to {@code 1.0f} which
	 * 							indicates the quality setting to use for the
	 * 							compression of the thumbnail. {@code 0.0f}
	 * 							indicates the lowest quality, {@code 1.0f}
	 * 							indicates the highest quality setting for the
	 * 							compression.
	 * 							{@link ThumbnailParameter#DEFAULT_QUALITY}
	 * 							should be specified when the codec's default
	 * 							compression quality settings should be used.
	 * @param imageType 		The {@link BufferedImage} image type of the
	 * 							thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#DEFAULT_IMAGE_TYPE}
	 *							should be specified when the default image
	 *							type should be used when creating the thumbnail.
	 * @param filters			The {@link ImageFilter}s to apply to the
	 * 							thumbnail.
	 * 							A value of {@code null} will be recognized as
	 * 							no filters are to be applied.
	 * 							The filters are applied after the original
	 * 							image has been resized.
	 * @param resizer			The {@link Resizer} to use when performing the
	 * 							resizing operation to create a thumbnail.
	 * @param fitWithinDimensions	Whether or not to fit the thumbnail within
	 * 								the specified dimensions.
	 * 								<p>
	 * 								If {@code true} is specified, then the
	 * 								thumbnail will be sized to fit within the
	 * 								specified dimensions, if the thumbnail is
	 * 								going to exceed those dimensions.
	 * @param useExifOrientation	Whether or not to use the Exif metadata to
	 * 								determine the orientation of the thumbnail.
	 * 								<p>
	 * 								If {@code true} is specified, then the
	 * 								Exif metadata will be used to determine
	 * 								the orientation of the thumbnail.
	 * 
	 * @throws IllegalArgumentException 	If the scaling factor is not a
	 * 										rational number or is less than or
	 * 										equal to 0, or if the
	 * 										{@link Resizer} is null.
	 * @since	0.4.3
	 */
	public ThumbnailParameter(
			double widthScalingFactor,
			double heightScalingFactor,
			Region sourceRegion,
			boolean keepAspectRatio,
			String outputFormat,
			String outputFormatType,
			float outputQuality,
			int imageType,
			List<ImageFilter> filters,
			Resizer resizer,
			boolean fitWithinDimensions,
			boolean useExifOrientation
	) {
		this(
				null,
				widthScalingFactor,
				heightScalingFactor,
				sourceRegion,
				keepAspectRatio,
				outputFormat,
				outputFormatType,
				outputQuality,
				imageType,
				filters,
				new FixedResizerFactory(resizer),
				fitWithinDimensions,
				useExifOrientation
		);
		
		validateScalingFactor();
	}
	
	/**
	 * Creates an object holding the parameters needed in order to make a
	 * thumbnail.
	 * 
	 * @param thumbnailSize		The size of the thumbnail to generate.
	 * @param sourceRegion		The region of the source image to use when
	 * 							creating a thumbnail.
	 * 							A value of {@code null} indicates that the
	 * 							entire source image should be used to create
	 * 							the thumbnail.
	 * @param keepAspectRatio	Indicates whether or not the thumbnail should
	 * 							maintain the aspect ratio of the original image.
	 * @param outputFormat		A string indicating the compression format
	 * 							that should be applied on the thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#ORIGINAL_FORMAT}
	 * 							should be provided if the same image format as
	 * 							the original should	be used for the thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#DETERMINE_FORMAT}
	 * 							should be provided if the output format of the
	 * 							thumbnail should be the determined from the
	 * 							information available, such as the output file
	 * 							name of the thumbnail.
	 * @param outputFormatType	A string indicating the compression type that
	 * 							should be used when writing the thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#DEFAULT_FORMAT_TYPE}
	 * 							should be provided if the thumbnail should be
	 * 							written using the default compression type of
	 * 							the codec specified in {@code outputFormat}.
	 * @param outputQuality		A value from {@code 0.0f} to {@code 1.0f} which
	 * 							indicates the quality setting to use for the
	 * 							compression of the thumbnail. {@code 0.0f}
	 * 							indicates the lowest quality, {@code 1.0f}
	 * 							indicates the highest quality setting for the
	 * 							compression.
	 * 							{@link ThumbnailParameter#DEFAULT_QUALITY}
	 * 							should be specified when the codec's default
	 * 							compression quality settings should be used.
	 * @param imageType 		The {@link BufferedImage} image type of the
	 * 							thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#DEFAULT_IMAGE_TYPE}
	 *							should be specified when the default image
	 *							type should be used when creating the thumbnail.
	 * @param filters			The {@link ImageFilter}s to apply to the
	 * 							thumbnail.
	 * 							A value of {@code null} will be recognized as
	 * 							no filters are to be applied.
	 * 							The filters are applied after the original
	 * 							image has been resized.
	 * @param resizerFactory	The {@link ResizerFactory} for obtaining a
	 * 							{@link Resizer} that is to be used when
	 * 							performing an image resizing operation.
	 * @param fitWithinDimensions	Whether or not to fit the thumbnail within
	 * 								the specified dimensions.
	 * 								<p>
	 * 								If {@code true} is specified, then the
	 * 								thumbnail will be sized to fit within the
	 * 								specified dimensions, if the thumbnail is
	 * 								going to exceed those dimensions.
	 * @param useExifOrientation	Whether or not to use the Exif metadata to
	 * 								determine the orientation of the thumbnail.
	 * 								<p>
	 * 								If {@code true} is specified, then the
	 * 								Exif metadata will be used to determine
	 * 								the orientation of the thumbnail.
	 * 
	 * @throws IllegalArgumentException 	If size is {@code null} or if the
	 * 										dimensions are negative, or if the
	 * 										{@link ResizerFactory} is null.
	 * @since	0.4.3
	 */
	public ThumbnailParameter(
			Dimension thumbnailSize,
			Region sourceRegion,
			boolean keepAspectRatio,
			String outputFormat,
			String outputFormatType,
			float outputQuality,
			int imageType,
			List<ImageFilter> filters,
			ResizerFactory resizerFactory,
			boolean fitWithinDimensions,
			boolean useExifOrientation
	) {
		this(
				thumbnailSize,
				Double.NaN,
				Double.NaN,
				sourceRegion,
				keepAspectRatio,
				outputFormat,
				outputFormatType,
				outputQuality,
				imageType,
				filters,
				resizerFactory,
				fitWithinDimensions,
				useExifOrientation
		);
		
		validateThumbnailSize();
	}
	
	/**
	 * Creates an object holding the parameters needed in order to make a
	 * thumbnail.
	 * 
	 * @param widthScalingFactor	The scaling factor to apply to the width
	 * 								when creating a	thumbnail from the original
	 * 								image.
	 * @param heightScalingFactor	The scaling factor to apply to the height
	 * 								when creating a	thumbnail from the original
	 * 								image.
	 * @param sourceRegion		The region of the source image to use when
	 * 							creating a thumbnail.
	 * 							A value of {@code null} indicates that the
	 * 							entire source image should be used to create
	 * 							the thumbnail.
	 * @param keepAspectRatio	Indicates whether or not the thumbnail should
	 * 							maintain the aspect ratio of the original image.
	 * @param outputFormat		A string indicating the compression format
	 * 							that should be applied on the thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#ORIGINAL_FORMAT}
	 * 							should be provided if the same image format as
	 * 							the original should	be used for the thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#DETERMINE_FORMAT}
	 * 							should be provided if the output format of the
	 * 							thumbnail should be the determined from the
	 * 							information available, such as the output file
	 * 							name of the thumbnail.
	 * @param outputFormatType	A string indicating the compression type that
	 * 							should be used when writing the thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#DEFAULT_FORMAT_TYPE}
	 * 							should be provided if the thumbnail should be
	 * 							written using the default compression type of
	 * 							the codec specified in {@code outputFormat}.
	 * @param outputQuality		A value from {@code 0.0f} to {@code 1.0f} which
	 * 							indicates the quality setting to use for the
	 * 							compression of the thumbnail. {@code 0.0f}
	 * 							indicates the lowest quality, {@code 1.0f}
	 * 							indicates the highest quality setting for the
	 * 							compression.
	 * 							{@link ThumbnailParameter#DEFAULT_QUALITY}
	 * 							should be specified when the codec's default
	 * 							compression quality settings should be used.
	 * @param imageType 		The {@link BufferedImage} image type of the
	 * 							thumbnail.
	 * 							A value of
	 * 							{@link ThumbnailParameter#DEFAULT_IMAGE_TYPE}
	 *							should be specified when the default image
	 *							type should be used when creating the thumbnail.
	 * @param filters			The {@link ImageFilter}s to apply to the
	 * 							thumbnail.
	 * 							A value of {@code null} will be recognized as
	 * 							no filters are to be applied.
	 * 							The filters are applied after the original
	 * 							image has been resized.
	 * @param resizerFactory	The {@link ResizerFactory} for obtaining a
	 * 							{@link Resizer} that is to be used when
	 * 							performing an image resizing operation.
	 * @param fitWithinDimensions	Whether or not to fit the thumbnail within
	 * 								the specified dimensions.
	 * 								<p>
	 * 								If {@code true} is specified, then the
	 * 								thumbnail will be sized to fit within the
	 * 								specified dimensions, if the thumbnail is
	 * 								going to exceed those dimensions.
	 * @param useExifOrientation	Whether or not to use the Exif metadata to
	 * 								determine the orientation of the thumbnail.
	 * 								<p>
	 * 								If {@code true} is specified, then the
	 * 								Exif metadata will be used to determine
	 * 								the orientation of the thumbnail.
	 * 
	 * @throws IllegalArgumentException 	If the scaling factor is not a
	 * 										rational number or is less than or
	 * 										equal to 0, or if the
	 * 										{@link ResizerFactory} is null.
	 * @since	0.4.3
	 */
	public ThumbnailParameter(
			double widthScalingFactor,
			double heightScalingFactor,
			Region sourceRegion,
			boolean keepAspectRatio,
			String outputFormat,
			String outputFormatType,
			float outputQuality,
			int imageType,
			List<ImageFilter> filters,
			ResizerFactory resizerFactory,
			boolean fitWithinDimensions,
			boolean useExifOrientation
	) {
		this(
				null,
				widthScalingFactor,
				heightScalingFactor,
				sourceRegion,
				keepAspectRatio,
				outputFormat,
				outputFormatType,
				outputQuality,
				imageType,
				filters,
				resizerFactory,
				fitWithinDimensions,
				useExifOrientation
		);
		
		validateScalingFactor();
	}
	
	/**
	 * Returns the size of the thumbnail.
	 * <p>
	 * Returns {@code null} if the scaling factor is set rather than the
	 * explicit thumbnail size.
	 * 
	 * @return		The size of the thumbnail.
	 */
	public Dimension getSize() {
		if (thumbnailSize != null) {
			return (Dimension)thumbnailSize.clone();
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the scaling factor to apply to the width when creating the
	 * thumbnail.
	 * <p>
	 * Returns {@link Double#NaN} if the thumbnail size is set rather than the
	 * scaling factor.
	 * 
	 * @return		The width scaling factor for the thumbnail.
	 * @since	0.3.10
	 */
	public double getWidthScalingFactor() {
		return widthScalingFactor;
	}
	
	/**
	 * Returns the scaling factor to apply to the height when creating the
	 * thumbnail.
	 * <p>
	 * Returns {@link Double#NaN} if the thumbnail size is set rather than the
	 * scaling factor.
	 * 
	 * @return		The height scaling factor for the thumbnail.
	 * @since	0.3.10
	 */
	public double getHeightScalingFactor() {
		return heightScalingFactor;
	}

	/**
	 * Returns the type of image. The value returned is the constant used for
	 * image types of {@link BufferedImage}.
	 * 
	 * @return		The type of the image.
	 */
	public int getType() {
		return imageType;
	}

	/**
	 * Returns whether or not the thumbnail is to maintain the aspect ratio of
	 * the source image when creating the thumbnail.
	 * 
	 * @return 		{@code true} if the thumbnail is to maintain the aspect
	 * 				ratio of the original image, {@code false} otherwise.
	 */
	public boolean isKeepAspectRatio() {
		return keepAspectRatio;
	}

	/**
	 * Returns the output format for the thumbnail.
	 * <p>
	 * If the output format is to use the same compression format as the
	 * original image, this method will return
	 * {@link ThumbnailParameter#ORIGINAL_FORMAT}.
	 * <p>
	 * If the output format should be determined from the information available
	 * such as the file name of the thumbnail, then this method will return
	 * {@link ThumbnailParameter#DETERMINE_FORMAT}.
	 * 
	 * @return 		The output format for the thumbnail.
	 */
	public String getOutputFormat() {
		return outputFormat;
	}
	
	/**
	 * Returns the output format type for the thumbnail.
	 * <p>
	 * If the default compression type of the compression format is to be used,
	 * then this method will return
	 * {@link ThumbnailParameter#DEFAULT_FORMAT_TYPE}.
	 *
	 * @return 		The output format type for the thumbnail.
	 */
	public String getOutputFormatType() {
		return outputFormatType;
	}

	/**
	 * Returns the compression quality settings for the thumbnail.
	 * <p>
	 * The value is in the range of {@code 0.0f} to {@code 1.0f},
	 * where {@code 0.0f} is for the lowest quality setting and {@code 1.0f} for
	 * the highest quality setting.
	 * <p>
	 * If the default compression quality is to be used, then this method will
	 * return {@link ThumbnailParameter#DEFAULT_QUALITY}.
	 * 
	 * @return 		The compression quality settings for the thumbnail.
	 */
	public float getOutputQuality() {
		return outputQuality;
	}

	/**
	 * Returns the list of {@link ImageFilter}s which are applied to the
	 * thumbnail.
	 * <p>
	 * These filters are applied after the original image has been resized.
	 * 
	 * @return		The {@link ImageFilter}s which are applied to the thumbnail.
	 */
	public List<ImageFilter> getImageFilters() {
		return filters;
	}
	
	/**
	 * Returns the default {@link Resizer} that will be used when performing the
	 * resizing operation to create a thumbnail.
	 * 
	 * @return		The default {@link Resizer} to use when performing a resize
	 * 				operation.
	 */
	public Resizer getResizer() {
		return resizerFactory.getResizer();
	}
	
	/**
	 * Returns the {@link ResizerFactory} for obtaining a {@link Resizer} which
	 * is to be used when performing the resizing operation to create a
	 * thumbnail.
	 * 
	 * @return		The {@link ResizerFactory} to use to obtain the
	 * 				{@link Resizer}.
	 */
	public ResizerFactory getResizerFactory() {
		return resizerFactory;
	}
	
	/**
	 * Returns whether or not the original image type should be used for the
	 * thumbnail.
	 * 
	 * @return		{@code true} if the original image type should be used,
	 * 				{@code false} otherwise.
	 */
	public boolean useOriginalImageType() {
		return imageType == ORIGINAL_IMAGE_TYPE;
	}
	
	/**
	 * Returns the region of the source image to use when creating a thumbnail,
	 * represented by a {@link Region} object.
	 * 
	 * @return		The {@code Region} object representing the source region
	 * 				to use when creating a thumbnail.
	 * 				<p>
     * 				A value of {@code null} indicates that the entire source
     * 				image should be used to create the thumbnail.
	 */
	public Region getSourceRegion() {
		return sourceRegion;
	}
	
	/**
	 * Returns whether or not to fit the thumbnail within the specified
	 * dimensions.
	 * 
	 * @return		{@code true} is returned when the thumbnail should be sized
	 * 				to fit within the specified dimensions, if the thumbnail
	 * 				is going to exceed those dimensions.
	 * @since	0.4.0
	 */
	public boolean fitWithinDimenions() {
		return fitWithinDimensions;
	}
	
	/**
	 * Returns whether or not the Exif metadata should be used to determine
	 * the orientation of the thumbnail.
	 *
	 * @return		{@code true} is returned when the Exif metadata should be
	 * 				used to decide the orientation of the thumbnail,
	 * 				{@code false} otherwise.
	 * @since	0.4.3
	 */
	public boolean useExifOrientation() {
		return useExifOrientation;
	}
}
