package net.coobird.thumbnailator.tasks.io;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
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
 * An {@link ImageSource} which reads the source image from a file.
 * 
 * @author coobird
 *
 */
public class FileImageSource extends AbstractImageSource<File>
{
	/**
	 * The index used to obtain the first image in an image file.
	 */
	private static final int FIRST_IMAGE_INDEX = 0;
	
	/**
	 * The file from which the image should be obtained.
	 */
	private final File sourceFile;
	
	/**
	 * Instantiates a {@link FileImageSource} with the specified file as
	 * the source image.
	 * 
	 * @param sourceFile		The source image file.
	 * @throws NullPointerException	If the image is null.
	 */
	public FileImageSource(File sourceFile)
	{
		super();
		
		if (sourceFile == null)
		{
			throw new NullPointerException("File cannot be null.");
		}
		
		this.sourceFile = sourceFile;
	}
	
	/**
	 * Instantiates a {@link FileImageSource} with the specified file as
	 * the source image.
	 * 
	 * @param sourceFilePath	The filepath of the source image file.
	 * @throws NullPointerException	If the image is null.
	 */
	public FileImageSource(String sourceFilePath)
	{
		super();
		
		if (sourceFilePath == null)
		{
			throw new NullPointerException("File cannot be null.");
		}
		
		this.sourceFile = new File(sourceFilePath);
	}

	public BufferedImage read() throws IOException
	{
		if (!sourceFile.exists())
		{
			throw new FileNotFoundException(
					"Could not find file: " + sourceFile.getAbsolutePath()
			);
		}
		
		/* TODO refactor.
		 * The following code has been adapted from the
		 * StreamThumbnailTask.read method.
		 */
		ImageInputStream iis = ImageIO.createImageInputStream(sourceFile);
		
		if (iis == null)
		{
			throw new IOException(
					"Could not open file: " + sourceFile.getAbsolutePath());
		}
		
		Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
		if (!readers.hasNext())
		{
			String sourcePath = sourceFile.getPath();
			throw new UnsupportedFormatException(
					UnsupportedFormatException.UNKNOWN,
					"No suitable ImageReader found for " + sourcePath + "."
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
		
		ImageReadParam irParam = reader.getDefaultReadParam();
		int width = reader.getWidth(FIRST_IMAGE_INDEX);
		int height = reader.getHeight(FIRST_IMAGE_INDEX);
		
		if (param != null && param.getSourceRegion() != null)
		{
			Region region = param.getSourceRegion();
			Rectangle sourceRegion = region.calculate(width, height);
			
			irParam.setSourceRegion(sourceRegion);
		}
		
		/*
		 * FIXME Workaround to enable subsampling for large source images.
		 * 
		 * Issue:
		 * https://code.google.com/p/thumbnailator/issues/detail?id=69
		 */
		if (param != null && 
				"true".equals(System.getProperty("thumbnailator.conserveMemoryWorkaround")) &&
				width > 1800 && height > 1800 &&
				(width * height * 4 > Runtime.getRuntime().freeMemory() / 4)
		)
		{
			int subsampling = 1;
			
			// Calculate the maximum subsampling that can be used.
			if (param.getSize() != null && (param.getSize().width * 2 < width && param.getSize().height * 2 < height))
			{
				double widthScaling = (double)width / (double)param.getSize().width;
				double heightScaling = (double)height / (double)param.getSize().height;
				subsampling = (int)Math.floor(Math.min(widthScaling, heightScaling));
			}
			else if (param.getSize() == null)
			{
				subsampling = (int)Math.max(1, Math.floor(1 / Math.max(param.getHeightScalingFactor(), param.getWidthScalingFactor())));
			}
			
			// Prevent excessive subsampling that can ruin image quality.
			// This will ensure that at least a 600 x 600 image will be used as source.
			for (; (width / subsampling) < 600 || (height / subsampling) < 600; subsampling--);
			
			// If scaling factor based resize is used, need to change the scaling factor.
			if (param.getSize() == null)
			{
				try
				{
					Class<?> c = param.getClass();
					Field heightField = c.getDeclaredField("heightScalingFactor");
					Field widthField = c.getDeclaredField("widthScalingFactor");
					heightField.setAccessible(true);
					widthField.setAccessible(true);
					heightField.set(param, param.getHeightScalingFactor() * (double)subsampling);
					widthField.set(param, param.getWidthScalingFactor() * (double)subsampling);
				}
				catch (Exception e)
				{
					// If we can't update the parameter, then disable subsampling.
					subsampling = 1;
				}
			}
			
			irParam.setSourceSubsampling(subsampling, subsampling, 0, 0);
		}
		
		img = reader.read(FIRST_IMAGE_INDEX, irParam);
		
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

	/**
	 * Returns the source file from which an image is read.
	 * 
	 * @return 		The {@code File} representation of the source file.
	 */
	public File getSource()
	{
		return sourceFile;
	}
}
