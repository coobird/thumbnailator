package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.tasks.io.InputStreamImageSource;
import net.coobird.thumbnailator.tasks.io.OutputStreamImageSink;

/**
 * A thumbnail generation task which streams data from an {@link InputStream}
 * to an {@link OutputStream}.
 * <p>
 * This class does not close the {@link InputStream} and {@link OutputStream}
 * upon the completion of processing.
 * <p>
 * Only the first image obtained from the data stream will be read. Subsequent
 * images will be ignored.
 * 
 * @author coobird
 *
 */
public class StreamThumbnailTask extends ThumbnailTask
{
	/**
	 * The {@link SourceSinkThumbnailTask} used to perform the task.
	 */
	private final SourceSinkThumbnailTask task;
	
	/**
	 * Creates a {@link ThumbnailTask} in which streamed image data from the 
	 * specified {@link InputStream} is output to a specified 
	 * {@link OutputStream}, using the parameters provided in the specified
	 * {@link ThumbnailParameter}.
	 * 
	 * @param param		The parameters to use to create the thumbnail.
	 * @param is		The {@link InputStream} from which to obtain image data.
	 * @param os		The {@link OutputStream} to send thumbnail data to.
	 */
	public StreamThumbnailTask(ThumbnailParameter param, InputStream is, OutputStream os)
	{
		super(param);
		this.task = new SourceSinkThumbnailTask(
				param,
				new InputStreamImageSource(is),
				new OutputStreamImageSink(os)
		);
	}

	@Override
	public BufferedImage read() throws IOException
	{
		return task.read();
	}

	@Override
	public void write(BufferedImage img) throws IOException
	{
		task.write(img);
	}

	@Override
	public ThumbnailParameter getParam()
	{
		return task.getParam();
	}
}