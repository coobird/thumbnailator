package net.coobird.thumbnailator.tasks.io;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.geometry.Region;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import net.coobird.thumbnailator.util.exif.ExifFilterUtils;
import net.coobird.thumbnailator.util.exif.ExifUtils;
import net.coobird.thumbnailator.util.exif.Orientation;

/**
 * An {@link ImageSource} which uses an {@link InputStream} to read the
 * source image.
 * 
 * @author coobird
 *
 */
public class InputStreamImageSource extends AbstractImageSource<InputStream>
{
	/**
	 * The index used to obtain the first image in an image file.
	 */
	private static final int FIRST_IMAGE_INDEX = 0;
	
	/**
	 * The {@link InputStream} from which the source image is to be read.
	 */
	private final InputStream is;
	
	/**
	 * Instantiates an {@link InputStreamImageSource} with the
	 * {@link InputStream} which will be used to read the source image.
	 * 
	 * @param is		The {@link InputStream} which is to be used to obtain
	 * 					the source image.
	 * @throws NullPointerException		If the {@link InputStream} is
	 * 									{@code null}.
	 */
	public InputStreamImageSource(InputStream is)
	{
		super();
		
		if (is == null)
		{
			throw new NullPointerException("InputStream cannot be null.");
		}
		
		this.is = is;
	}

	public BufferedImage read() throws IOException
	{
		ImageInputStream iis = ImageIO.createImageInputStream(is);
		
		if (iis == null)
		{
			throw new IOException("Could not open InputStream.");
		}
		
		Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
		if (!readers.hasNext())
		{
			throw new UnsupportedFormatException(
					UnsupportedFormatException.UNKNOWN,
					"No suitable ImageReader found for source data."
			);
		}
		
		ImageReader reader = readers.next();
		reader.setInput(iis);
		inputFormatName = reader.getFormatName();
		
		try
		{
			if (param.useExifOrientation())
			{
				Orientation orientation;
				orientation =
						ExifUtils.getExifOrientation(reader, FIRST_IMAGE_INDEX);
				
				// Skip this code block if there's no rotation needed.
				if (orientation != null && orientation != Orientation.TOP_LEFT)
				{
					List<ImageFilter> filters = param.getImageFilters();
					
					// EXIF orientation filter is added to the beginning, as
					// it should be performed early to prevent mis-orientation
					// in later filters.
					filters.add(0, ExifFilterUtils.getFilterForOrientation(orientation));
				}
			}
		}
		catch (Exception e)
		{
			// If something goes wrong, then skip the orientation-related
			// processing.
			// TODO Ought to have some way to track errors.
		}
		
		BufferedImage img;
		if (param != null && param.getSourceRegion() != null)
		{
			Region region = param.getSourceRegion();
			int width = reader.getWidth(FIRST_IMAGE_INDEX);
			int height = reader.getHeight(FIRST_IMAGE_INDEX);
			
			Rectangle sourceRegion = region.calculate(width, height);
			
			ImageReadParam irParam = reader.getDefaultReadParam();
			irParam.setSourceRegion(sourceRegion);
			
			img = reader.read(FIRST_IMAGE_INDEX, irParam);
		}
		else
		{
			img = reader.read(FIRST_IMAGE_INDEX);
		}

		/*
		 * Dispose the reader to free resources.
		 * 
		 * This seems to be one of the culprits which was causing
		 * `OutOfMemoryError`s which began appearing frequently with
		 * Java 7 Update 21.
		 * 
		 * Issue:
		 * http://code.google.com/p/thumbnailator/issues/detail?id=42
		 */
		reader.dispose();
		iis.close();
		
		return finishedReading(img);
	}

	public InputStream getSource()
	{
		return is;
	}
}
