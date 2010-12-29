package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import net.coobird.thumbnailator.BufferedImages;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;

/**
 * An {@link ImageSink} which specifies an {@link OutputStream} to which the
 * thumbnail image should be written to.
 * 
 * @author coobird
 *
 */
public class OutputStreamImageSink extends AbstractImageSink<OutputStream>
{
	/**
	 * The {@link OutputStream} to which the thumbnail image is to be 
	 * written to.
	 */
	private final OutputStream os;
	
	/**
	 * Instantiates an {@link OutputStreamImageSink} with the 
	 * {@link OutputStream} to which the thumbnail should be written to.
	 * 
	 * @param os		The {@link OutputStream} to write the thumbnail to.
	 * @throws NullPointerException		If the {@link OutputStream} is 
	 * 									{@code null}.
	 */
	public OutputStreamImageSink(OutputStream os)
	{
		super();
		
		if (os == null)
		{
			throw new NullPointerException("OutputStream cannot be null.");
		}
		
		this.os = os;
	}

	public void write(BufferedImage img) throws IOException
	{
		if (outputFormat == null)
		{
			throw new IllegalStateException("Output format has not been set.");
		}
		
		if (img == null)
		{
			throw new NullPointerException("Cannot write a null image.");
		}

		
		String formatName = outputFormat;
			
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

	public OutputStream getSink()
	{
		return os;
	}
}
