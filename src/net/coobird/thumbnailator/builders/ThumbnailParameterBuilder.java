package net.coobird.thumbnailator.builders;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.filters.Watermark;

/**
 * A builder for generating {@link ThumbnailParameter}.
 * 
 * @author coobird
 *
 */
public final class ThumbnailParameterBuilder
{
	private int width;
	private int height;
	private int imageType;
	private List<Watermark> watermarks;
	private boolean keepAspectRatio;
	private float thumbnailQuality;
	private String thumbnailFormat;
	
	/**
	 * Creates an instance of a {@link ThumbnailParameterBuilder}.
	 */
	public ThumbnailParameterBuilder()
	{
		watermarks = Collections.emptyList();
	}
	
	/**
	 * Sets the image type fo the thumbnail.
	 * 
	 * @param type			The image type of the thumbnail.
	 * @return				A reference to this object.
	 */
	public ThumbnailParameterBuilder imageType(int type)
	{
		imageType = type;
		return this;
	}
	
	/**
	 * Sets the watermark to apply to the thumbnail.
	 * 
	 * @param watermark		A watermark to apply to the thumbnail.
	 * @return				A reference to this object.
	 */
	public ThumbnailParameterBuilder watermark(Watermark watermark)
	{
		watermarks = Arrays.asList(watermark);
		return this;
	}
	
	/**
	 * Sets the watermarks to apply to the thumbnail.
	 * <p>
	 * The order of the watermarks applied will be in the order the watermarks
	 * are in the {@link List}. Therefore, the watermark which appears at the
	 * beginning of the list will be the watermark which is applied first.
	 * 
	 * @param watermarks	A list of watermarks to apply to the thumbnail.
	 * @return				A reference to this object.
	 */
	public ThumbnailParameterBuilder watermarks(List<Watermark> watermarks)
	{
		this.watermarks = new ArrayList<Watermark>(watermarks);
		return this;
	}
	
	/**
	 * Sets the size of the thumbnail.
	 * 
	 * @param size		The dimensions of the thumbnail.
	 * @return			A reference to this object.
	 */
	public ThumbnailParameterBuilder size(Dimension size)
	{
		size(size.width, size.height);
		return this;
	}
	
	/**
	 * Sets the size of the thumbnail.
	 * 
	 * @param width		The width of the thumbnail.
	 * @param height	The height of the thumbnail.
	 * @return			A reference to this object.
	 */
	public ThumbnailParameterBuilder size(int width, int height)
	{
		this.width = width;
		this.height = height;
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
	public ThumbnailParameterBuilder keepAspectRatio(boolean keep)
	{
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
	public ThumbnailParameterBuilder quality(float quality)
	{
		this.thumbnailQuality = quality;
		return this;
	}

	/**
	 * Sets the output format of the thumbnail.
	 * 
	 * @param format		The output format of the thumbnail.
	 * @return				A reference to this object.
	 */
	public ThumbnailParameterBuilder format(String format)
	{
		this.thumbnailFormat = format;
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
	 */
	public ThumbnailParameter build()
	{
		ThumbnailParameter param = new ThumbnailParameter(
				new Dimension(width, height),
				watermarks,
				keepAspectRatio,
				thumbnailFormat,
				thumbnailQuality,
				imageType
		);
			
		return param;
	}
}