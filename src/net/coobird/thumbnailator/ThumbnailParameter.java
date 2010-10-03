package net.coobird.thumbnailator;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.resizers.Resizer;

/**
 * This class is used to specify the parameters to use when creating a thumbnail.
 * 
 * @author coobird
 *
 */
public class ThumbnailParameter
{
	/**
	 * A constant used to denote that the output format of the thumbnail should
	 * be the same as the format of the original image.
	 */
	public static final String ORIGINAL_FORMAT = null;
	
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
	 * The scaling factor to use when creating a thumbnail from the original
	 * image.
	 * <p>
	 * If this field is set, then the {@link #thumbnailSize} field will be set
	 * as {@code null} to indicate that it is not set.
	 */
	private final double scalingFactor;
	
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
	 * The {@link Resizer} to use when performing the resizing operation to
	 * create a thumbnail.
	 */
	private final Resizer resizer;
	
	
	/**
	 * Creates an object holding the parameters needed in order to make a
	 * thumbnail.
	 * 
	 * @param thumbnailSize		The size of the thumbnail to generate.
	 * @param keepAspectRatio	Indicates whether or not the thumbnail should
	 * 							maintain the aspect ratio of the original image.
	 * @param outputFormat		A string indicating the compression format
	 * 							that should be applied on the thumbnail.
	 * 							A value of 
	 * 							{@link ThumbnailParameter#ORIGINAL_FORMAT} 
	 * 							should be provided if the same image format as
	 * 							the original should	be used for the thumbnail.
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
	 * 
	 * @throws IllegalArgumentException 	If size is {@code null} or if the 
	 * 										dimensions are negative. 
	 */
	public ThumbnailParameter(
			Dimension thumbnailSize,
			boolean keepAspectRatio,
			String outputFormat,
			String outputFormatType,
			float outputQuality,
			int imageType,
			List<ImageFilter> filters,
			Resizer resizer
	)
	{
		if (thumbnailSize == null)
		{
			throw new IllegalArgumentException("Thumbnail size cannot be null.");
		} 
		else if (thumbnailSize.width < 0 || thumbnailSize.height < 0)
		{
			throw new IllegalArgumentException("Thumbnail dimensions must be greater than 0.");
		}

		this.thumbnailSize = thumbnailSize;
		this.scalingFactor = Double.NaN;
		
		this.keepAspectRatio = keepAspectRatio;
		
		this.outputFormat = outputFormat;
		this.outputFormatType = outputFormatType;
		
		/*
		 * Note:
		 * The value of DEFAULT_QUALITY is Float.NaN which cannot be compared
		 * by using the regular == operator. Therefore, to check that NaN is
		 * being used, one must use the Float.NaN method.
		 */
		if ( (outputQuality < 0.0f || outputQuality > 1.0f) &&
				!Float.isNaN(outputQuality) )
		{
			throw new IllegalArgumentException("The output quality must be " +
					"between 0.0f and 1.0f, or Float.NaN to use the default " +
			"compression quality of codec being used.");
		}
		
		this.outputQuality = outputQuality;
		this.imageType = imageType;
		
		this.filters = filters == null ? 
				Collections.<ImageFilter>emptyList() : filters;
				
		if (resizer == null)
		{
			throw new IllegalArgumentException("Resizer cannot be null");
		}
		this.resizer = resizer;
	}

	/**
	 * Creates an object holding the parameters needed in order to make a
	 * thumbnail.
	 * 
	 * @param thumbnailSize		The size of the thumbnail to generate.
	 * @param keepAspectRatio	Indicates whether or not the thumbnail should
	 * 							maintain the aspect ratio of the original image.
	 * @param outputFormat		A string indicating the compression format
	 * 							that should be applied on the thumbnail.
	 * 							A value of 
	 * 							{@link ThumbnailParameter#ORIGINAL_FORMAT} 
	 * 							should be provided if the same image format as
	 * 							the original should	be used for the thumbnail.
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
	 * 
	 * @throws IllegalArgumentException 	If scaling factor is less than or
	 * 										equal to 0. 
	 */
	public ThumbnailParameter(
			double scalingFactor,
			boolean keepAspectRatio,
			String outputFormat,
			String outputFormatType,
			float outputQuality,
			int imageType,
			List<ImageFilter> filters,
			Resizer resizer
	)
	{
		if (scalingFactor <= 0.0)
		{
			throw new IllegalArgumentException("Scaling factor is less than or equal to 0.");
		} 

		this.scalingFactor = scalingFactor;
		this.thumbnailSize = null;
		
		this.keepAspectRatio = keepAspectRatio;
		
		this.outputFormat = outputFormat;
		this.outputFormatType = outputFormatType;
		
		/*
		 * Note:
		 * The value of DEFAULT_QUALITY is Float.NaN which cannot be compared
		 * by using the regular == operator. Therefore, to check that NaN is
		 * being used, one must use the Float.NaN method.
		 */
		if ( (outputQuality < 0.0f || outputQuality > 1.0f) &&
				!Float.isNaN(outputQuality) )
		{
			throw new IllegalArgumentException("The output quality must be " +
					"between 0.0f and 1.0f, or Float.NaN to use the default " +
					"compression quality of codec being used.");
		}
		
		this.outputQuality = outputQuality;
		this.imageType = imageType;
		
		this.filters = filters == null ? 
				Collections.<ImageFilter>emptyList() : filters;
		
		if (resizer == null)
		{
			throw new IllegalArgumentException("Resizer cannot be null");
		}
		this.resizer = resizer;
	}
	
	
	/**
	 * Returns the size of the thumbnail.
	 * <p>
	 * Returns {@code null} if the scaling factor is set rather than the 
	 * explicit thumbnail size.
	 * 
	 * @return		The size of the thumbnail.
	 */
	public Dimension getSize()
	{
		if (thumbnailSize != null)
		{
			return (Dimension)thumbnailSize.clone();
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Returns the scaling factor for the thumbnail.
	 * <p>
	 * Returns {@link Double#NaN} if the thumbnail size is set rather than the 
	 * scaling factor.
	 * 
	 * @return		The scaling factor for the thumbnail.
	 */
	public double getScalingFactor()
	{
		return scalingFactor;
	}

	/**
	 * Returns the type of image. The value returned is the constant used for
	 * image types of {@link BufferedImage}.
	 * 
	 * @return		The type of the image.
	 */
	public int getType()
	{
		return imageType;
	}

	/**
	 * Returns whether or not the thumbnail is to maintain the aspect ratio of
	 * the source image when creating the thumbnail.
	 * 
	 * @return 		{@code true} if the thumbnail is to maintain the aspect
	 * 				ratio of the original image, {@code false} otherwise.
	 */
	public boolean isKeepAspectRatio()
	{
		return keepAspectRatio;
	}

	/**
	 * Returns the output format for the thumbnail.
	 * <p>
	 * If the output format is to use the same compression format as the
	 * original image, this method will return
	 * {@link ThumbnailParameter#ORIGINAL_FORMAT}.
	 * 
	 * @return 		The output format for the thumbnail.
	 */
	public String getOutputFormat()
	{
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
	public String getOutputFormatType()
	{
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
	public float getOutputQuality()
	{
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
	public List<ImageFilter> getImageFilters()
	{
		return Collections.unmodifiableList(filters);
	}
	
	/**
	 * Returns the {@link Resizer} to use when performing the resizing
	 * operation to create the thumbnail.
	 * 
	 * @return		The {@link Resizer} to use when creating a thumbnail.
	 */
	public Resizer getResizer()
	{
		return resizer;
	}
}