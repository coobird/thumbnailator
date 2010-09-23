package net.coobird.thumbnailator;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

import net.coobird.thumbnailator.filters.Watermark;

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
	 * A constant used to denote that the default compression quality settings
	 * should be used when creating the thumbnail.
	 */
	public static final float DEFAULT_QUALITY = Float.NaN;
	
	/**
	 * The thumbnail size.
	 */
	private final Dimension thumbnailSize;
	
	/**
	 * A list of watermarks to place on the thumbnail. The watermark with the
	 * highest index will be placed on top.
	 */
	private final List<Watermark> watermarks;
	
	/**
	 * Indicated whether or not the thumbnail should retain the aspect ratio
	 * the same as the original image when the aspect ratio of the desired
	 * dimensions for the thumbnail does not match the ratio of the original
	 * image.
	 */
	private final boolean keepAspectRatio;
	
	/**
	 * The output format for the thumbnail.
	 */
	private final String outputFormat;
	
	/**
	 * The output quality settings which will be used by the image compressor.
	 * <p>
	 * An acceptable value is in the range of {@code 0.0f} to {@code 1.0f},
	 * where {@code 0.0f} is for the lowest quality setting and {@code 1.0f} for
	 * the highest quality setting.
	 * <p>
	 * A value of {@code Float#NaN} indicates that the default quality settings
	 * of the output codec should be used.
	 */
	private final float outputQuality;
	
	/**
	 * The image type of the {@code BufferedImage} used for the thumbnail.
	 */
	private final int imageType;
	
	/**
	 * Creates an object holding the parameters needed in order to make a
	 * thumbnail.
	 * 
	 * @param thumbnailSize		The size of the thumbnail to generate.
	 * @param watermarks		The {@link Watermark}s to place on the thumbnail.
	 * 							An empty {@link List} or {@code null} can be
	 * 							used to signify that no watermarks should be
	 * 							applied to the thumbnail.
	 * @param keepAspectRatio	Indicates whether or not the thumbnail should
	 * 							maintain the aspect ratio of the original image.
	 * @param outputFormat		A string indicating the type of compression
	 * 							that should be applied on the thumbnail.
	 * 							A value of 
	 * 							{@link ThumbnailParameter#ORIGINAL_FORMAT} 
	 * 							should be provided if the same image format as
	 * 							the original should	be used for the thumbnail.
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
	 * @throws IllegalArgumentException 	If size is {@code null} or if the 
	 * 										dimensions are negative. 
	 */
	public ThumbnailParameter(
			Dimension thumbnailSize,
			List<Watermark> watermarks,
			boolean keepAspectRatio,
			String outputFormat,
			float outputQuality,
			int imageType
	)
	{
		if (thumbnailSize == null)
		{
			throw new IllegalArgumentException("Thumbnail size must not be null.");
		} 
		else if (thumbnailSize.width < 0 || thumbnailSize.height < 0)
		{
			throw new IllegalArgumentException("Thumbnail dimensions must be greater than 0.");
		}

		this.thumbnailSize = thumbnailSize;
		
		this.watermarks = watermarks != null ? 
				watermarks : Collections.<Watermark>emptyList();
		
		this.keepAspectRatio = keepAspectRatio;
		
		this.outputFormat = outputFormat;
		
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
	}
	
	/**
	 * Returns the size of the thumbnail.
	 * 
	 * @return		The size of the thumbnail.
	 */
	public Dimension getSize()
	{
		return (Dimension)thumbnailSize.clone();
	}

	/**
	 * Returns the watermarks associated with the thumbnail.
	 * 
	 * This method returns a {@link List} which is an unmodifiable copy of the
	 * internal {@code List} containing the watermarks. 
	 * 
	 * @return		The watermarks associated with the thumbnail.
	 */
	public List<Watermark> getWatermarks()
	{
		return watermarks != null ? 
				Collections.unmodifiableList(watermarks) : 
				Collections.<Watermark>emptyList();
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
	 * 
	 * @return 		The output format for the thumbnail.
	 */
	public String getOutputFormat()
	{
		return outputFormat;
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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + imageType;
		result = prime * result + (keepAspectRatio ? 1231 : 1237);
		result = prime * result
				+ ((outputFormat == null) ? 0 : outputFormat.hashCode());
		result = prime * result + Float.floatToIntBits(outputQuality);
		result = prime * result
				+ ((thumbnailSize == null) ? 0 : thumbnailSize.hashCode());
		result = prime * result
				+ ((watermarks == null) ? 0 : watermarks.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ThumbnailParameter other = (ThumbnailParameter) obj;
		if (imageType != other.imageType)
			return false;
		if (keepAspectRatio != other.keepAspectRatio)
			return false;
		if (outputFormat == null)
		{
			if (other.outputFormat != null)
				return false;
		}
		else if (!outputFormat.equals(other.outputFormat))
			return false;
		if (Float.floatToIntBits(outputQuality) != Float
				.floatToIntBits(other.outputQuality))
			return false;
		if (thumbnailSize == null)
		{
			if (other.thumbnailSize != null)
				return false;
		}
		else if (!thumbnailSize.equals(other.thumbnailSize))
			return false;
		if (watermarks == null)
		{
			if (other.watermarks != null)
				return false;
		}
		else if (!watermarks.equals(other.watermarks))
			return false;
		return true;
	}

	@Override
	public String toString()
	{
		return "ThumbnailParameter [thumbnailSize=" + thumbnailSize
				+ ", watermarks=" + watermarks + ", keepAspectRatio="
				+ keepAspectRatio + ", outputFormat=" + outputFormat
				+ ", outputQuality=" + outputQuality + ", imageType="
				+ imageType + "]";
	}
}