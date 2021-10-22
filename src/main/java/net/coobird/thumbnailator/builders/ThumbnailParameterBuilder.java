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

package net.coobird.thumbnailator.builders;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.resizers.DefaultResizerFactory;
import net.coobird.thumbnailator.resizers.FixedResizerFactory;
import net.coobird.thumbnailator.geometry.Region;
import net.coobird.thumbnailator.resizers.Resizer;
import net.coobird.thumbnailator.resizers.ResizerFactory;

/**
 * <p>
 * A builder for generating {@link ThumbnailParameter}.
 * </p>
 * <p>
 * The default values assigned to the {@link ThumbnailParameter} created by
 * the {@link ThumbnailParameterBuilder} are as follows:
 * </p>
 * <dl>
 * <dt>width</dt>
 * <dd>Unassigned. Must be set by the {@link #size(int, int)} method.</dd>
 * <dt>height</dt>
 * <dd>Unassigned. Must be set by the {@link #size(int, int)} method.</dd>
 * <dt>scaling factor</dt>
 * <dd>Unassigned. Must be set by the {@link #scale(double)} method or
 * {@link #scale(double, double)} method.</dd>
 * <dt>source region</dt>
 * <dd>Uses the entire source image.</dd>
 * <dt>image type</dt>
 * <dd>See {@link ThumbnailParameter#DEFAULT_IMAGE_TYPE}. Same as
 * {@link BufferedImage#TYPE_INT_ARGB}.</dd>
 * <dt>aspect ratio</dt>
 * <dd>Maintain the aspect ratio of the original image.</dd>
 * <dt>output quality</dt>
 * <dd>See {@link ThumbnailParameter#DEFAULT_QUALITY}.</dd>
 * <dt>output format</dt>
 * <dd>See {@link ThumbnailParameter#ORIGINAL_FORMAT}. Maintains the same
 * image format as the original image.</dd>
 * <dt>output format type</dt>
 * <dd>See {@link ThumbnailParameter#DEFAULT_FORMAT_TYPE}. Uses the default
 * format type of the codec used to create the thumbnail image.</dd>
 * <dt>image filters</dt>
 * <dd>None.</dd>
 * <dt>resizer factory</dt>
 * <dd>{@link DefaultResizerFactory} is used.</dd>
 * <dt>resizer</dt>
 * <dd>The default {@link Resizer} returned by the {@link ResizerFactory}.</dd>
 * <dt>use of Exif metadata for orientation</dt>
 * <dd>Use the Exif metadata to determine the orientation of the thumbnail.</dd>
 * </dl>
 * 
 * @author coobird
 *
 */
public final class ThumbnailParameterBuilder {
	private static final int UNINITIALIZED = -1;
	
	private int width = UNINITIALIZED;
	private int height = UNINITIALIZED;
	private double widthScalingFactor = Double.NaN;
	private double heightScalingFactor = Double.NaN;
	private int imageType = ThumbnailParameter.DEFAULT_IMAGE_TYPE;
	private boolean keepAspectRatio = true;
	private float thumbnailQuality = ThumbnailParameter.DEFAULT_QUALITY;
	private String thumbnailFormat = ThumbnailParameter.ORIGINAL_FORMAT;
	private String thumbnailFormatType = ThumbnailParameter.DEFAULT_FORMAT_TYPE;
	private List<ImageFilter> filters = Collections.emptyList();
	private ResizerFactory resizerFactory = DefaultResizerFactory.getInstance();
	private Region sourceRegion = null;
	private boolean fitWithinDimensions = true;
	private boolean useExifOrientation = true;
	
	/**
	 * Creates an instance of a {@link ThumbnailParameterBuilder}.
	 */
	public ThumbnailParameterBuilder() {}
	
	/**
	 * Sets the image type fo the thumbnail.
	 * 
	 * @param type			The image type of the thumbnail.
	 * @return				A reference to this object.
	 */
	public ThumbnailParameterBuilder imageType(int type) {
		imageType = type;
		return this;
	}
	
	/**
	 * Sets the size of the thumbnail.
	 * 
	 * @param size		The dimensions of the thumbnail.
	 * @return			A reference to this object.
	 */
	public ThumbnailParameterBuilder size(Dimension size) {
		size(size.width, size.height);
		return this;
	}
	
	/**
	 * Sets the size of the thumbnail.
	 * 
	 * @param width		The width of the thumbnail.
	 * @param height	The height of the thumbnail.
	 * @return			A reference to this object.
	 * @throws IllegalArgumentException	If the widht or height is less than 0.
	 */
	public ThumbnailParameterBuilder size(int width, int height) {
		if (width < 0) {
			throw new IllegalArgumentException("Width must be greater than 0.");
		}
		if (height < 0) {
			throw new IllegalArgumentException("Height must be greater than 0.");
		}
		
		this.width = width;
		this.height = height;
		return this;
	}
	
	/**
	 * Sets the scaling factor of the thumbnail.
	 * 
	 * @param scalingFactor		The scaling factor of the thumbnail.
	 * @return					A reference to this object.
	 * @throws IllegalArgumentException		If the scaling factor is not a
	 * 										rational number, or if it is less
	 * 										than {@code 0.0}.
	 */
	public ThumbnailParameterBuilder scale(double scalingFactor) {
		return scale(scalingFactor, scalingFactor);
	}
	
	/**
	 * Sets the scaling factor of the thumbnail.
	 * 
	 * @param widthScalingFactor		The scaling factor to use for the width
	 * 									when creating the thumbnail.
	 * @param heightScalingFactor		The scaling factor to use for the height
	 * 									when creating the thumbnail.
	 * @return							A reference to this object.
	 * @throws IllegalArgumentException		If the scaling factor is not a
	 * 										rational number, or if it is less
	 * 										than {@code 0.0}.
	 * @since	0.3.10
	 */
	public ThumbnailParameterBuilder scale(double widthScalingFactor, double heightScalingFactor) {
		if (widthScalingFactor <= 0.0 || heightScalingFactor <= 0.0) {
			throw new IllegalArgumentException("Scaling factor is less than or equal to 0.");

		} else if (Double.isNaN(widthScalingFactor) || Double.isInfinite(widthScalingFactor)) {
			throw new IllegalArgumentException("Scaling factor must be a rational number.");

		} else if (Double.isNaN(heightScalingFactor) || Double.isInfinite(heightScalingFactor)) {
			throw new IllegalArgumentException("Scaling factor must be a rational number.");
		}
		
		this.widthScalingFactor = widthScalingFactor;
		this.heightScalingFactor = heightScalingFactor;
		return this;
	}
	
	/**
	 * Sets the region of the source image to use when creating a thumbnail.
	 * 
	 * @param sourceRegion		The region of the source image to use when
	 * 							creating a thumbnail.
	 * @return			A reference to this object.
	 * @since	0.3.4
	 */
	public ThumbnailParameterBuilder region(Region sourceRegion) {
		this.sourceRegion = sourceRegion;
		return this;
	}
	
	/**
	 * Sets whether or not the thumbnail is to maintain the aspect ratio of
	 * the original image.
	 * 
	 * @param keep		{@code true} if the aspect ratio of the original image
	 * 					is to be maintained in the thumbnail, {@code false}
	 * 					otherwise.
	 * @return			A reference to this object.
	 */
	public ThumbnailParameterBuilder keepAspectRatio(boolean keep) {
		this.keepAspectRatio = keep;
		return this;
	}
	
	/**
	 * Sets the compression quality setting of the thumbnail.
	 * <p>
	 * An acceptable value is in the range of {@code 0.0f} to {@code 1.0f},
	 * where {@code 0.0f} is for the lowest quality setting and {@code 1.0f} for
	 * the highest quality setting.
	 * <p>
	 * If the default compression quality is to be used, then the value
	 * {@link ThumbnailParameter#DEFAULT_QUALITY} should be used.
	 * 
	 * @param quality		The compression quality setting of the thumbnail.
	 * @return				A reference to this object.
	 */
	public ThumbnailParameterBuilder quality(float quality) {
		this.thumbnailQuality = quality;
		return this;
	}

	/**
	 * Sets the output format of the thumbnail.
	 * 
	 * @param format		The output format of the thumbnail.
	 * @return				A reference to this object.
	 */
	public ThumbnailParameterBuilder format(String format) {
		this.thumbnailFormat = format;
		return this;
	}
	
	/**
	 * Sets the output format type of the thumbnail.
	 * 
	 * @param formatType	The output format type of the thumbnail.
	 * @return				A reference to this object.
	 */
	public ThumbnailParameterBuilder formatType(String formatType) {
		this.thumbnailFormatType = formatType;
		return this;
	}
	
	/**
	 * Sets the {@link ImageFilter}s to apply to the thumbnail.
	 * <p>
	 * These filters will be applied after the original image is resized.
	 * 
	 * @param filters		The output format type of the thumbnail.
	 * @return				A reference to this object.
	 */
	public ThumbnailParameterBuilder filters(List<ImageFilter> filters) {
		if (filters == null) {
			throw new NullPointerException("Filters is null.");
		}
		
		this.filters = filters;
		return this;
	}
	
	/**
	 * Sets the {@link Resizer} to use when performing the resizing operation
	 * to create the thumbnail.
	 * <p>
	 * Calling this method after {@link #resizerFactory(ResizerFactory)} will
	 * cause the {@link ResizerFactory} used by the resulting
	 * {@link ThumbnailParameter} to only return the specified {@link Resizer}.
	 *
	 * @param resizer		The {@link Resizer} to use when creating the
	 * 						thumbnail.
	 * @return				A reference to this object.
	 */
	public ThumbnailParameterBuilder resizer(Resizer resizer) {
		if (resizer == null) {
			throw new NullPointerException("Resizer is null.");
		}
		
		this.resizerFactory = new FixedResizerFactory(resizer);
		return this;
	}
	
	/**
	 * Sets the {@link ResizerFactory} to use to obtain a {@link Resizer} when
	 * performing the resizing operation to create the thumbnail.
	 * <p>
	 * Calling this method after {@link #resizer(Resizer)} could result in
	 * {@link Resizer}s not specified in the {@code resizer} method to be used
	 * when creating thumbnails.
	 * 
	 * 
	 * @param resizerFactory	The {@link ResizerFactory} to use when obtaining
	 * 							a {@link Resizer} to create the thumbnail.
	 * @return					A reference to this object.
	 * @since	0.4.0
	 */
	public ThumbnailParameterBuilder resizerFactory(ResizerFactory resizerFactory) {
		if (resizerFactory == null) {
			throw new NullPointerException("Resizer is null.");
		}
		
		this.resizerFactory = resizerFactory;
		return this;
	}
	
	/**
	 * Sets whether or not the thumbnail should fit within the specified
	 * dimensions.
	 * 
	 * @param fit		{@code true} if the thumbnail should be sized to fit
	 *					within the specified dimensions, if the thumbnail
	 * 					is going to exceed those dimensions.
	 * @return			A reference to this object.
	 * @since	0.4.0
	 */
	public ThumbnailParameterBuilder fitWithinDimensions(boolean fit) {
		this.fitWithinDimensions = fit;
		return this;
	}
	
	/**
	 * Sets whether or not the Exif metadata should be used to determine the
	 * orientation of the thumbnail.
	 * 
	 * @param use		{@code true} if the Exif metadata should be used
	 * 					to determine the orientation of the thumbnail,
	 * 					{@code false} otherwise.
	 * @return			A reference to this object.
	 * @since	0.4.3
	 */
	public ThumbnailParameterBuilder useExifOrientation(boolean use) {
		this.useExifOrientation = use;
		return this;
	}

	/**
	 * Returns a {@link ThumbnailParameter} from the parameters which are
	 * currently set.
	 * <p>
	 * This method will throw a {@link IllegalArgumentException} required
	 * parameters for the {@link ThumbnailParameter} have not been set.
	 * 
	 * @return		A {@link ThumbnailParameter} with parameters set through
	 * 				the use of this builder.
	 * @throws IllegalStateException	If neither the size nor the scaling
	 * 									factor has been set.
	 */
	public ThumbnailParameter build() {
		if (!Double.isNaN(widthScalingFactor)) {
			// If scaling factor has been set.
			return new ThumbnailParameter(
					widthScalingFactor,
					heightScalingFactor,
					sourceRegion,
					keepAspectRatio,
					thumbnailFormat,
					thumbnailFormatType,
					thumbnailQuality,
					imageType,
					filters,
					resizerFactory,
					fitWithinDimensions,
					useExifOrientation
			);

		} else if (width != UNINITIALIZED && height != UNINITIALIZED) {
			return new ThumbnailParameter(
					new Dimension(width, height),
					sourceRegion,
					keepAspectRatio,
					thumbnailFormat,
					thumbnailFormatType,
					thumbnailQuality,
					imageType,
					filters,
					resizerFactory,
					fitWithinDimensions,
					useExifOrientation
			);
		} else {
			throw new IllegalStateException(
					"The size nor the scaling factor has been set."
			);
		}
	}
}
