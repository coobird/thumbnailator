package net.coobird.thumbnailator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

public final class ThumbnailatorUtils
{
	/**
	 *  This class is not intended to be instantiated.
	 */
	private ThumbnailatorUtils() {}
	
	public static List<String> getSupportedOutputFormats()
	{
		return Arrays.asList(ImageIO.getWriterFormatNames());
	}
	
	public static boolean isSupportedOutputFormat(String format)
	{
		for (String supportedFormat : getSupportedOutputFormats())
		{
			if (supportedFormat.equals(format))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public static List<String> getSupportedOutputFormatTypes(String format)
	{
		if (format == null)
		{
			throw new NullPointerException("Format name cannot be null.");
		}
		
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(format); 
		if (!writers.hasNext())
		{
			return Collections.emptyList();
		}
		
		String[] types = 
			writers.next().getDefaultWriteParam().getCompressionTypes();
		
		if (types == null)
		{
			return Collections.emptyList();
		}
		else
		{
			return Arrays.asList(types);
		}
	}
	
	public static boolean isSupportedOutputFormatType(String format, String type)
	{
		for (String supportedType : getSupportedOutputFormatTypes(format))
		{
			if (supportedType.equals(type))
			{
				return true;
			}
		}
		
		return false;
	}
}
