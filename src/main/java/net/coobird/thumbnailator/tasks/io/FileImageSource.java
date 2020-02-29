package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;

/**
 * An {@link ImageSource} which reads the source image from a file.
 * 
 * @author coobird
 *
 */
public class FileImageSource implements ImageSource<File> {
	/**
	 * The file from which the image should be obtained.
	 */
	private final File sourceFile;
	
	/**
	 * An {@link ImageSource} which actually performs the image source
	 * operations. This {@link ImageSource} can change during the lifecycle
	 * of the {@link FileImageSource} class.
	 */
	private ImageSource<?> imageSource = new UninitializedImageSource();
	
	/**
	 * A {@link ThumbnailParameter} object that is given by 
	 * {@link #setThumbnailParameter(ThumbnailParameter)}. This copy is needed
	 * when the {@link #imageSource} is being replacing during the lifecycle
	 * of this class.
	 */
	private ThumbnailParameter param;
	
	/**
	 * Temporary placeholder {@link ImageSource} which will be used before
	 * the {@link #read()} method is used. Basically a way to use the
	 * implementation of the {@link AbstractImageSource} without having to
	 * instantiate a {@link InputStreamImageSource} object before needed.
	 */
	private static class UninitializedImageSource extends AbstractImageSource<Void> {
		public BufferedImage read() throws IOException {
			throw new IllegalStateException("This should not happen.");
		}

		public Void getSource() {
			throw new IllegalStateException("This should not happen.");
		}
	}
	
	/**
	 * Instantiates a {@link FileImageSource} with the specified file as
	 * the source image.
	 * 
	 * @param sourceFile		The source image file.
	 * @throws NullPointerException	If the image is null.
	 */
	public FileImageSource(File sourceFile) {
		super();
		
		if (sourceFile == null) {
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
	public FileImageSource(String sourceFilePath) {
		super();
		
		if (sourceFilePath == null) {
			throw new NullPointerException("File cannot be null.");
		}
		
		this.sourceFile = new File(sourceFilePath);
	}

	public BufferedImage read() throws IOException {
		try {
			FileInputStream fis = new FileInputStream(sourceFile);
			imageSource = new InputStreamImageSource(fis);
			imageSource.setThumbnailParameter(param);
			BufferedImage img = imageSource.read();
			fis.close();
			return img;

		} catch (FileNotFoundException e) {
			throw new FileNotFoundException(
					"Could not find file: " + sourceFile.getAbsolutePath()
			);

		} catch (UnsupportedFormatException e) {
			String sourcePath = sourceFile.getAbsolutePath();
			throw new UnsupportedFormatException(
					UnsupportedFormatException.UNKNOWN,
					"No suitable ImageReader found for " + sourcePath + "."
			);
		}
	}

	/**
	 * Returns the source file from which an image is read.
	 * 
	 * @return 		The {@code File} representation of the source file.
	 */
	public File getSource() {
		return sourceFile;
	}

	public String getInputFormatName() {
		return imageSource.getInputFormatName();
	}

	public void setThumbnailParameter(ThumbnailParameter param) {
		// We need to keep "param" when we replace "imageSource" in the
		// "read" method.
		this.param = param;
		
		imageSource.setThumbnailParameter(param);
	}
}
