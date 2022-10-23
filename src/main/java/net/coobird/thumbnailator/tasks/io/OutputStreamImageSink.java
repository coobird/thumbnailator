/*
 * Thumbnailator - a thumbnail generation library
 *
 * Copyright (c) 2008-2022 Chris Kroells
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import net.coobird.thumbnailator.util.BufferedImages;
import net.coobird.thumbnailator.util.ThumbnailatorUtils;

/**
 * An {@link ImageSink} which specifies an {@link OutputStream} to which the
 * thumbnail image should be written to.
 * 
 * @author coobird
 *
 */
public class OutputStreamImageSink extends AbstractImageSink<OutputStream> {
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
	public OutputStreamImageSink(OutputStream os) {
		super();
		
		if (os == null) {
			throw new NullPointerException("OutputStream cannot be null.");
		}
		
		this.os = os;
	}

	/**
	 * Writes the resulting image to the {@link OutputStream}.
	 * 
	 * @param img							The image to write.
	 * @throws UnsupportedFormatException	When an unsupported format has been
	 * 										specified by the
	 * 										{@link #setOutputFormatName(String)}
	 * 										method.
	 * @throws IOException					When a problem occurs while writing
	 * 										the image.
	 * @throws NullPointerException		If the image is {@code null}.
	 * @throws IllegalStateException	If the output format has not been set
	 * 									by calling the
	 * 									{@link #setOutputFormatName(String)}
	 * 									method.
	 */
	public void write(BufferedImage img) throws IOException {
		super.write(img);
		
		if (outputFormat == null) {
			throw new IllegalStateException("Output format has not been set.");
		}
		
		String formatName = outputFormat;
			
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(formatName);
		
		if (!writers.hasNext()) {
			throw new UnsupportedFormatException(
					formatName,
					"No suitable ImageWriter found for " + formatName + "."
			);
		}
		
		ImageWriter writer = writers.next();
		
		ImageWriteParam writeParam = writer.getDefaultWriteParam();
		if (writeParam.canWriteCompressed()) {
			/*
			 * Sets the compression format type, if specified.
			 *
			 * Note:
			 * The value to denote that the codec's default compression type
			 * should be used is null.
			 */
			String compressionType = null;
			if (param != null && param.getOutputFormatType() != ThumbnailParameter.DEFAULT_FORMAT_TYPE) {
				compressionType = param.getOutputFormatType();

			} else {
				List<String> supportedFormats =
						ThumbnailatorUtils.getSupportedOutputFormatTypes(formatName);

				if (!supportedFormats.isEmpty()) {
					compressionType = supportedFormats.get(0);
				}
			}
			if (compressionType != null) {
				setCompressionModeExplicit(writeParam);
				writeParam.setCompressionType(compressionType);
			}

			/*
			 * Sets the compression quality, if specified.
			 *
			 * Note:
			 * The value to denote that the codec's default compression quality
			 * should be used is Float.NaN.
			 */
			if (param != null && !Float.isNaN(param.getOutputQuality())) {
				setCompressionModeExplicit(writeParam);
				writeParam.setCompressionQuality(param.getOutputQuality());

			} else if (isPng(formatName) && isJava9OrNewer() && isDefaultPngWriter(writer)) {
				/*
				 * Before Java 9, the PNG writer bundled with the JRE was
				 * using maximum compression.
				 * To replicate the behavior in Java 9+, the compression
				 * quality is set to 0.0f to trigger maximum compression.
				 * See Issue #156: https://github.com/coobird/thumbnailator/issues/156
				 */
				setCompressionModeExplicit(writeParam);
				writeParam.setCompressionQuality(0.0f);
			}
		}

		/*
		 * The following line is not surrounded by a try-catch, as catching
		 * the `IOException` and re-throwing would not give a good feedback as
		 * to what is causing the original `IOException`.
		 * 
		 * It would have been informative to have the `IOException` which
		 * caused this problem, but the `IOException` in Java 5 does not
		 * have a "cause" parameter.
		 * 
		 * The "cause" parameter has been introduced in Java 6:
		 * http://docs.oracle.com/javase/6/docs/api/java/io/IOException.html#IOException%28java.lang.String,%20java.lang.Throwable%29
		 * 
		 * TODO Include `cause` in exception when moving codebase to Java 6+
		 *
		 * TODO Whether to surround this portion of code in a try-catch
		 *      is debatable, as it wouldn't really add more utility.
		 *
		 *      Furthermore, there are other calls in this method which will
		 *      throw `IOException`s, but they are not surrounded by try-catch
		 *      blocks. (A similar example existed in the `FileImageSink`
		 *      where the `ImageIO.createImageOutputStream` was not surrounded
		 *      in a try-catch.)
		 *
		 * Related issue:
		 * https://github.com/coobird/thumbnailator/issues/37
		 */
		ImageOutputStream ios = ImageIO.createImageOutputStream(os);
		
		if (ios == null) {
			throw new IOException("Could not open OutputStream.");
		}
		
		/*
		 * Note:
		 * The following code is a workaround for the JPEG writer which ships
		 * with the JDK.
		 * 
		 * At issue is, that the JPEG writer appears to write the alpha
		 * channel when it should not. Such images end up with wrong colors.
		 * https://bugs.openjdk.java.net/browse/JDK-8041459
		 *
		 * To circumvent this, images to be saved as JPEG will be copied to
		 * another BufferedImage without an alpha channel before it is saved.
		 *
		 * Furthermore, as of OpenJDK 11, if an BufferedImage with an alpha
		 * channel is given to the JPEG writer, it will throw an exception.
		 * https://bugs.openjdk.java.net/browse/JDK-8204188
		 * 
		 * Also, the BMP writer appears not to support ARGB, so an RGB image
		 * will be produced before saving.
		 */
		if (isJpegOrBmp(formatName)) {
			img = BufferedImages.copy(img, BufferedImage.TYPE_INT_RGB);
		}
		
		writer.setOutput(ios);
		writer.write(null, new IIOImage(img, null, null), writeParam);
		
		/*
		 * Dispose the writer to free resources.
		 * 
		 * This seems to be the main culprit of `OutOfMemoryError`s which
		 * started to frequently appear with Java 7 Update 21.
		 * 
		 * Issue:
		 * https://github.com/coobird/thumbnailator/issues/42
		 */
		writer.dispose();
		
		ios.close();
	}

	/**
	 * Sets the compression mode to explicit, if not already.
	 * A check exists to prevent setting the explicit mode more than once,
	 * as any previously set parameters will be discarded.
	 *
	 * @param writeParam	Current image writer parameters.
	 */
	private void setCompressionModeExplicit(ImageWriteParam writeParam) {
		if (writeParam.getCompressionMode() != ImageWriteParam.MODE_EXPLICIT) {
			writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		}
	}

	private boolean isJpegOrBmp(String formatName) {
		return formatName.equalsIgnoreCase("jpg")
				|| formatName.equalsIgnoreCase("jpeg")
				|| formatName.equalsIgnoreCase("bmp");
	}

	private boolean isPng(String formatName) {
		return formatName.equalsIgnoreCase("png");
	}

	private boolean isDefaultPngWriter(ImageWriter writer) {
		String writerClassName = writer.getClass().getName();
		return "com.sun.imageio.plugins.png.PNGImageWriter".equals(writerClassName);
	}

	private boolean isJava9OrNewer() {
		String version = System.getProperty("java.specification.version");
		// Up to Java 8, specification version was 1.x.
		return version != null && !version.contains(".");
	}

	public OutputStream getSink() {
		return os;
	}
}
