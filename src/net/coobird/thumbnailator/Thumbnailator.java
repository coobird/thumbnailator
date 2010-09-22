package net.coobird.thumbnailator;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.filters.Watermark;
import net.coobird.thumbnailator.resizers.Resizer;
import net.coobird.thumbnailator.resizers.ResizerFactory;
import net.coobird.thumbnailator.tasks.ThumbnailTask;

/**
 * This class provides utilities methods to exectute {@link ThumbnailTask}s.
 * 
 * @author coobird
 *
 */
public class Thumbnailator
{
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
	public static void createThumbnail(ThumbnailTask task) throws IOException
	{
		ThumbnailParameter param = task.getParam();
		
		BufferedImage sourceImage = task.read();
		BufferedImage destinationImage = new BufferedImageBuilder(
				param.getSize(),
				param.getType()
		).build(); 
		
		// Create thumbnail
		Dimension sourceSize = new Dimension(sourceImage.getWidth(), sourceImage.getHeight());
		Dimension destinationSize = param.getSize();
		
		Resizer r = ResizerFactory.getResizer(sourceSize, destinationSize);
		r.resize(sourceImage, destinationImage);
		
		// Add watermarks
		for (Watermark w : param.getWatermarks())
		{
			destinationImage = w.apply(destinationImage);
		}
		
		task.write(destinationImage);
	}
}
