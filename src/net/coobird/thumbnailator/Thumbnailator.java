package net.coobird.thumbnailator;

import java.awt.image.BufferedImage;
import java.io.IOException;

import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.makers.FixedSizeThumbnailMaker;
import net.coobird.thumbnailator.makers.ScaledThumbnailMaker;
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
		
		BufferedImage destinationImage;
		
		if (param.getSize() != null)
		{
			// Get the dimensions of the original and thumbnail images. 
			int destinationWidth = param.getSize().width;
			int destinationHeight = param.getSize().height;
			
			// Create the thumbnail.
			destinationImage =
				new FixedSizeThumbnailMaker()
					.size(destinationWidth, destinationHeight)
					.keepAspectRatio(param.isKeepAspectRatio())
					.imageType(param.getType())
					.resizer(param.getResizer())
					.make(sourceImage);
		}
		else if (!Double.isNaN(param.getScalingFactor()))
		{
			// Create the thumbnail.
			destinationImage =
				new ScaledThumbnailMaker()
					.scale(param.getScalingFactor())
					.imageType(param.getType())
					.resizer(param.getResizer())
					.make(sourceImage);
		}
		else
		{
			throw new IllegalStateException("Parameters to make thumbnail" +
					" does not have scaling factor nor thumbnail size specified.");
		}
		
		// Perform the image filters
		for (ImageFilter filter : param.getImageFilters())
		{
			destinationImage = filter.apply(destinationImage);
		}
		
		// Write the thumbnail image to the destination.
		task.write(destinationImage);
	}
}
