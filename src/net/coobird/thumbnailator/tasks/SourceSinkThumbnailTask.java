package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.tasks.io.ImageSink;
import net.coobird.thumbnailator.tasks.io.ImageSource;

/**
 * A {@link ThumbnailTask} which holds an {@link ImageSource} from which the
 * image is read or retrieved, and an {@link ImageSink} to which the thumbnail
 * is stored or written.
 * <p>
 * This class will take care of handing off information from the
 * {@link ImageSource} to the {@link ImageSink}. For example, the output format
 * that should be used by the {@link ImageSink} will be handed off if the
 * {@link ThumbnailParameter#ORIGINAL_FORMAT} parameter is set.
 * 
 * @author coobird
 *
 * @param <S> 		The source class from which the source image is retrieved
 * 					or read.
 * @param <D> 		The destination class to which the thumbnail is stored
 * 					or written.
 */
public class SourceSinkThumbnailTask<S, D> extends ThumbnailTask<S, D>
{
	/**
	 * The source from which the image is retrieved or read.
	 */
	private final ImageSource<S> source;
	
	/**
	 * The destination to which the thumbnail is stored or written.
	 */
	private final ImageSink<D> destination;

	/**
	 * Creates a {@link ThumbnailTask} in which an image is retrived from the
	 * specified {@link ImageSource} and written to the specified
	 * {@link ImageSink}, using the parameters provided in the specified
	 * {@link ThumbnailParameter}.
	 * 
	 * @param param				The parameters to use to create the thumbnail.
	 * @param source			The source from which the image is retrieved
	 * 							or read from.
	 * @param destination		The destination to which the thumbnail is
	 * 							stored or written to.
	 * @throws NullPointerException		If either the parameter,
	 * 									{@link ImageSource} or {@link ImageSink}
	 * 									is {@code null}.
	 */
	public SourceSinkThumbnailTask(ThumbnailParameter param, ImageSource<S> source, ImageSink<D> destination)
	{
		super(param);
		if (source == null)
		{
			throw new NullPointerException("ImageSource cannot be null.");
		}
		if (destination == null)
		{
			throw new NullPointerException("ImageSink cannot be null.");
		}
		
		source.setThumbnailParameter(param);
		this.source = source;
		
		destination.setThumbnailParameter(param);
		this.destination = destination;
	}

	@Override
	public BufferedImage read() throws IOException
	{
		BufferedImage img = source.read();
		inputFormatName = source.getInputFormatName();
		
		return img;
	}

	@Override
	public void write(BufferedImage img) throws IOException
	{
		String paramOutputFormat = param.getOutputFormat();
		String formatName = null;
		
		if (ThumbnailParameter.DETERMINE_FORMAT.equals(paramOutputFormat))
		{
			paramOutputFormat = destination.preferredOutputFormatName();
		}
		
		if (paramOutputFormat == ThumbnailParameter.ORIGINAL_FORMAT)
		{
			formatName = inputFormatName;
		}
		else
		{
			formatName = paramOutputFormat;
		}

		destination.setOutputFormatName(formatName);
		destination.write(img);
	}

	@Override
	public S getSource()
	{
		return source.getSource();
	}
	
	@Override
	public D getDestination()
	{
		return destination.getSink();
	}
}
