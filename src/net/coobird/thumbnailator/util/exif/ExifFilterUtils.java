package net.coobird.thumbnailator.util.exif;

import net.coobird.thumbnailator.filters.Flip;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.filters.Pipeline;
import net.coobird.thumbnailator.filters.Rotation;

/**
 * An utility class which returns a suitable {@link ImageFilter} to perform
 * the transformations necessary to properly orient an image according to the
 * Exif metadata.
 * 
 * @author coobird
 *
 */
public final class ExifFilterUtils
{
	/**
	 * This class should not be instantiated. 
	 */
	private ExifFilterUtils() {};
	
	/**
	 * Returns a {@link ImageFilter} which will perform the transformations
	 * required to properly orient the thumbnail according to the Exif
	 * orientation.
	 * 
	 * @param orientation	The Exif orientation
	 * @return				{@link ImageFilter}s required to properly
	 * 						orient the image.
	 */
	public static ImageFilter getFilterForOrientation(Orientation orientation)
	{
		Pipeline filters = new Pipeline();
		
		if (orientation == Orientation.TOP_RIGHT)
		{
			filters.add(Flip.HORIZONTAL);
		}
		else if (orientation == Orientation.BOTTOM_RIGHT)
		{
			filters.add(Rotation.ROTATE_180_DEGREES);
		}
		else if (orientation == Orientation.BOTTOM_LEFT)
		{
			filters.add(Rotation.ROTATE_180_DEGREES);
			filters.add(Flip.HORIZONTAL);
		}
		else if (orientation == Orientation.LEFT_TOP)
		{
			filters.add(Rotation.RIGHT_90_DEGREES);
			filters.add(Flip.HORIZONTAL);
		}
		else if (orientation == Orientation.RIGHT_TOP)
		{
			filters.add(Rotation.RIGHT_90_DEGREES);
		}
		else if (orientation == Orientation.RIGHT_BOTTOM)
		{
			filters.add(Rotation.LEFT_90_DEGREES);
			filters.add(Flip.HORIZONTAL);
		}
		else if (orientation == Orientation.LEFT_BOTTOM)
		{
			filters.add(Rotation.LEFT_90_DEGREES);
		}
		
		return filters;
	}
}
