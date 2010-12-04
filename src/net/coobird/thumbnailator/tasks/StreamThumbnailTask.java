package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import net.coobird.thumbnailator.BufferedImages;
import net.coobird.thumbnailator.ThumbnailParameter;

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
	 * {@link InputStream} which is used to retrieve image data.
	 */
	private final InputStream is;
	
	/**
	 * {@link OutputStream} to which resized image data is written to. 
	 */
	private final OutputStream os;
	
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
		this.is = is;
		this.os = os;
	}

	@Override
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
		
		BufferedImage img = reader.read(FIRST_IMAGE_INDEX);
		
		iis.close();
		
		return img;
	}

	@Override
	public void write(BufferedImage img) throws IOException
	{
		String formatName;
		if (param.getOutputFormat() == ThumbnailParameter.ORIGINAL_FORMAT)
		{
			formatName = inputFormatName;
		}
		else
		{
			formatName = param.getOutputFormat();
		}
			
		Iterator<ImageWriter> writers = 
			ImageIO.getImageWritersByFormatName(formatName);
		
		if (!writers.hasNext())
		{
			throw new UnsupportedFormatException(
					formatName,
					"No suitable ImageWriter found for " + formatName + "."
			);
		}
		
		ImageWriter writer = writers.next();
		
		ImageWriteParam writeParam = writer.getDefaultWriteParam();
		if (writeParam.canWriteCompressed())
		{
			writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
			
			/*
			 * Sets the compression quality, if specified.
			 * 
			 * Note:
			 * The value to denote that the codec's default compression quality
			 * should be used is Float.NaN. 
			 */
			if (!Float.isNaN(param.getOutputQuality()))
			{
				writeParam.setCompressionQuality(param.getOutputQuality());
			}
			
			/*
			 * Sets the compression format type, if specified.
			 * 
			 * Note:
			 * The value to denote that the codec's default compression type
			 * should be used is null. 
			 */
			if (param.getOutputFormatType() != ThumbnailParameter.DEFAULT_FORMAT_TYPE)
			{
				writeParam.setCompressionType(param.getOutputFormatType());
			}
		}
		
		ImageOutputStream ios = ImageIO.createImageOutputStream(os);
		
		if (ios == null)
		{
			throw new IOException("Could not open OutputStream.");
		}
		
		/*
		 * Note:
		 * The following code is a workaround for the JPEG writer which ships
		 * with the JDK.
		 * 
		 * At issue is, that the JPEG writer appears to write the alpha
		 * channel when it should not. To circumvent this, images which are
		 * to be saved as a JPEG will be copied to another BufferedImage without
		 * an alpha channel before it is saved.
		 * 
		 * Also, the BMP writer appears not to support ARGB, so an RGB image
		 * will be produced before saving.
		 */
		if (
				formatName.equalsIgnoreCase("jpg")
				|| formatName.equalsIgnoreCase("jpeg")
				|| formatName.equalsIgnoreCase("bmp")
		)
		{
			img = BufferedImages.copy(img, BufferedImage.TYPE_INT_RGB);
		}
		
		writer.setOutput(ios);
		writer.write(null, new IIOImage(img, null, null), writeParam);
		
		ios.close();
	}

	@Override
	public Object getSource()
	{
		// TODO Auto-generated method stub
		return is;
	}
	
	@Override
	public Object getDestination()
	{
		// TODO Auto-generated method stub
		return os;
	}
}