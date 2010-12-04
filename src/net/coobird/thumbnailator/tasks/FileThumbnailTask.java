package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.event.IIOWriteProgressListener;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import net.coobird.thumbnailator.BufferedImages;
import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.events.ThumbnailatorEvent;
import net.coobird.thumbnailator.events.ThumbnailatorEventListener;
import net.coobird.thumbnailator.events.ThumbnailatorEvent.Phase;

/**
 * A thumbnail generation task which reads and writes data from and to a 
 * {@link File}.
 * <p>
 * Only the first image included in the image file will be read. Subsequent
 * images included in the image file will be ignored.
 * 
 * @author coobird
 *
 */
public class FileThumbnailTask extends ThumbnailTask
{
	/**
	 * The {@link File} from which image data is read from.
	 */
	private final File sourceFile;
	
	/**
	 * The {@link File} to which image data is written to.
	 */
	private File destinationFile;
	
	/**
	 * Creates a {@link ThumbnailTask} in which image data is read from the 
	 * specified {@link File} and is output to a specified {@link File}, using
	 * the parameters provided in the specified {@link ThumbnailParameter}.
	 * 
	 * @param param				The parameters to use to create the thumbnail.
	 * @param sourceFile		The {@link File} from which image data is read.
	 * @param destinationFile	The {@link File} to which thumbnail is written.
	 */
	public FileThumbnailTask(ThumbnailParameter param, File sourceFile, File destinationFile)
	{
		super(param);
		this.sourceFile = sourceFile;
		this.destinationFile = destinationFile;
	}
	
	/**
	 * Creates a {@link ThumbnailTask} in which image data is read from the 
	 * specified {@link File} and is output to a specified {@link File}, using
	 * the parameters provided in the specified {@link ThumbnailParameter}.
	 * 
	 * @param param				The parameters to use to create the thumbnail.
	 * @param sourceFile		The {@link File} from which image data is read.
	 * @param destinationFile	The {@link File} to which thumbnail is written.
	 */
	public FileThumbnailTask(ThumbnailParameter param, File sourceFile, File destinationFile, List<ThumbnailatorEventListener> listeners)
	{
		super(param, listeners);
		this.sourceFile = sourceFile;
		this.destinationFile = destinationFile;
	}

	@Override
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

		reader.addIIOReadProgressListener(new IIOReadProgressListener() {
			
			public void thumbnailStarted(ImageReader source, int imageIndex,
					int thumbnailIndex) {}
			
			public void thumbnailProgress(ImageReader source, float percentageDone) {}
			
			public void thumbnailComplete(ImageReader source) {}
			
			public void sequenceStarted(ImageReader source, int minIndex) {}
			
			public void sequenceComplete(ImageReader source) {}
			
			public void readAborted(ImageReader source)
			{
				notifier.failedProcessing(new ThumbnailatorEvent(Phase.ACQUIRE, Double.NaN), sourceFile);
			}
			
			public void imageStarted(ImageReader source, int imageIndex)
			{
				notifier.processing(new ThumbnailatorEvent(Phase.ACQUIRE, 0.0), sourceFile);
			}
			
			public void imageProgress(ImageReader source, float percentageDone)
			{
				notifier.processing(new ThumbnailatorEvent(Phase.ACQUIRE, percentageDone / 100d), sourceFile);
			}
			
			public void imageComplete(ImageReader source)
			{
				notifier.processing(new ThumbnailatorEvent(Phase.ACQUIRE, 1.0), sourceFile);
			}
		});
		
		BufferedImage img = reader.read(FIRST_IMAGE_INDEX);
		
		iis.close();
		
		return img;
	}

	@Override
	public void write(BufferedImage img) throws IOException
	{
		/* TODO refactor.
		 * The following code has been adapted from the 
		 * StreamThumbnailTask.write method.
		 */
		
		String formatName;
		if (param.getOutputFormat() == ThumbnailParameter.ORIGINAL_FORMAT || inputFormatName.equalsIgnoreCase(param.getOutputFormat()))
		{
			formatName = inputFormatName;
		}
		else
		{
			formatName = param.getOutputFormat();
			
			/*
			 * Add or replace the file extension of the output file.
			 * 
			 * If the file extension matches the output format's extension,
			 * then leave as is.
			 * 
			 * Else, append the extension for the output format to the filename. 
			 */
			String fileExtension = null; 
			String fileName = destinationFile.getName();
			if (
					fileName.contains(".") 
					&& fileName.lastIndexOf('.') != fileName.length() - 1
			)
			{
				int lastIndex = fileName.lastIndexOf('.');
				fileExtension = fileName.substring(lastIndex + 1); 
			}
			
			if (fileExtension == null || !fileExtension.equalsIgnoreCase(formatName)) 
			{
				destinationFile = new File(destinationFile.getAbsolutePath() + "." + formatName);
			}
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
		
		ImageOutputStream ios = 
			ImageIO.createImageOutputStream(destinationFile);
		
		if (ios == null)
		{
			throw new IOException("Could not open output file.");
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
		
		
		writer.addIIOWriteProgressListener(new IIOWriteProgressListener() {
			
			public void writeAborted(ImageWriter source)
			{
				notifier.failedProcessing(new ThumbnailatorEvent(Phase.OUTPUT, Double.NaN), sourceFile);
			}
			
			public void thumbnailStarted(ImageWriter source, int imageIndex,
					int thumbnailIndex)
			{
			}
			
			public void thumbnailProgress(ImageWriter source, float percentageDone)
			{
			}
			
			public void thumbnailComplete(ImageWriter source)
			{
			}
			
			public void imageStarted(ImageWriter source, int imageIndex)
			{
				notifier.processing(new ThumbnailatorEvent(Phase.OUTPUT, 0.0), sourceFile);
			}
			
			public void imageProgress(ImageWriter source, float percentageDone)
			{
				notifier.processing(new ThumbnailatorEvent(Phase.OUTPUT, percentageDone / 100d), sourceFile);
			}
			
			public void imageComplete(ImageWriter source)
			{
				notifier.processing(new ThumbnailatorEvent(Phase.OUTPUT, 1.0), sourceFile);
			}
		});
		
		writer.setOutput(ios);
		writer.write(null, new IIOImage(img, null, null), writeParam);
		
		ios.close();
	}

	@Override
	public Object getSource()
	{
		// TODO Auto-generated method stub
		return sourceFile;
	}
	
	@Override
	public Object getDestination()
	{
		// TODO Auto-generated method stub
		return destinationFile;
	}
}
