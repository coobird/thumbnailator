package net.coobird.thumbnailator;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import net.coobird.thumbnailator.filters.Watermark;
import net.coobird.thumbnailator.makers.FixedSizeThumbnailMaker;
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
		
		// Obtain the original image.
		BufferedImage sourceImage = task.read();

		// Get the dimensions of the original and thumbnail images. 
		Dimension sourceSize = 
			new Dimension(sourceImage.getWidth(), sourceImage.getHeight());
		Dimension destinationSize = param.getSize();
		int destinationWidth = param.getSize().width;
		int destinationHeight = param.getSize().height;
		
		// Create the thumbnail.
		BufferedImage destinationImage =
			new FixedSizeThumbnailMaker()
				.size(destinationWidth, destinationHeight)
				.keepAspectRatio(param.isKeepAspectRatio())
				.imageType(param.getType())
				.resizer(ResizerFactory.getResizer(sourceSize, destinationSize))
				.make(sourceImage);
		
		// Add watermarks.
		for (Watermark w : param.getWatermarks())
		{
			destinationImage = w.apply(destinationImage);
		}
		
		// Write the thumbnail image to the destination.
		task.write(destinationImage);
	}
}
