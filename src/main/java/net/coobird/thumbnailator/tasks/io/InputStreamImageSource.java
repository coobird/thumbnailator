/*
 * Thumbnailator - a thumbnail generation library
 *
 * Copyright (c) 2008-2023 Chris Kroells
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

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.geometry.Region;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import net.coobird.thumbnailator.util.Configurations;
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
public class InputStreamImageSource extends AbstractImageSource<InputStream> {
	/**
	 * The index used to obtain the first image in an image file.
	 */
	private static final int FIRST_IMAGE_INDEX = 0;
	
	/**
	 * A {@link InputStream} from which the source image is to be read.
	 */
	private InputStream is;
	
	/**
	 * Instantiates an {@link InputStreamImageSource} with the
	 * {@link InputStream} which will be used to read the source image.
	 * 
	 * @param is		The {@link InputStream} which is to be used to obtain
	 * 					the source image.
	 * @throws NullPointerException		If the {@link InputStream} is
	 * 									{@code null}.
	 */
	public InputStreamImageSource(InputStream is) {
		super();

		if (is == null) {
			throw new NullPointerException("InputStream cannot be null.");
		}

		if (!Configurations.DISABLE_EXIF_WORKAROUND.getBoolean()) {
			this.is = new ExifCaptureInputStream(is);
		} else {
			this.is = is;
		}
	}

	@Override
	public void setThumbnailParameter(ThumbnailParameter param) {
		super.setThumbnailParameter(param);

		if (param == null || !param.useExifOrientation()) {
			if (is instanceof ExifCaptureInputStream) {
				// Revert to original `InputStream` and use that directly.
				is = ((ExifCaptureInputStream)is).is;
			}
		}
	}

	/**
	 * An {@link InputStream} which intercepts the data stream to find Exif
	 * data and captures it if present.
	 */
	private static final class ExifCaptureInputStream extends InputStream {
		/**
		 * Original {@link InputStream} which reads from the image source.
		 */
		private final InputStream is;

		// Following are states for this input stream.

		/**
		 * Flag to indicate data stream should be intercepted and collected.
		 */
		private boolean doIntercept = true;

		/**
		 * A threshold on how much data to be intercepted.
		 * This is a safety mechanism to prevent buffering too much information.
		 */
		private static final int INTERCEPT_THRESHOLD = 1024 * 1024;

		/**
		 * Buffer to collect the input data to read JPEG images for JFIF marker segments.
		 * This will also be used to store the Exif data, if found.
 		 */
		private byte[] buffer = new byte[0];

		/**
		 * Current position for reading the buffer.
		 */
		int position = 0;

		/**
		 * Total bytes intercepted from the data stream.
		 */
		int totalRead = 0;

		/**
		 * Number of remaining bytes to skip ahead in the buffer.
		 * This value is positive when next location to skip to is outside the
		 * buffer's current contents.
		 */
		int remainingSkip = 0;

		/**
		 * Marker for the beginning of the APP1 marker segment.
		 * Its position is where the APP1 marker starts, not the payload.
		 */
		private int startApp1 = Integer.MIN_VALUE;

		/**
		 * Marker for the end of the APP1 marker segment.
		 */
		private int endApp1 = Integer.MAX_VALUE;

		/**
		 * A flag to indicate that we expect APP1 payload (which contains Exif
		 * contents) is being streamed, so they should be captured into the
		 * {@code buffer}.
		 */
		private boolean doCaptureApp1 = false;

		/**
		 * A flag to indicate that the {@code buffer} contains the complete
		 * Exif information.
		 */
		private boolean hasCapturedExif = false;

		/**
		 * A flag to indicate whether to output debug logs.
		 */
		private final boolean isDebug = Configurations.DEBUG_LOG_EXIF_WORKAROUND.getBoolean()
				|| Configurations.DEBUG_LOG.getBoolean();

		/**
		 * Returns Exif data captured from the JPEG image.
		 * @return	Returns captured Exif data, or {@code null} if unavailable.
		 */
		private byte[] getExifData() {
			return hasCapturedExif ? buffer : null;
		}

		// TODO Any performance penalties?
		private ExifCaptureInputStream(InputStream is) {
			this.is = is;
		}

		/**
		 * Terminate intercept.
		 * Drops the collected buffer to relieve pressure on memory.
		 *
		 * Do not call this when Exif was found, as buffer (containing Exif)
		 * will be lost.
		 */
		private void terminateIntercept() {
			doIntercept = false;
			buffer = null;
		}

		/**
		 * Debug message.
		 */
		private void debugln(String format, Object... args) {
			if (isDebug) {
				System.err.printf("[thumbnailator.exifWorkaround] " + format + "%n", args);
			}
		}

		/**
		 * Debug message, optimized to reduce calls on Arrays.toString.
		 */
		private void debugln(String format, byte[] array) {
			if (isDebug) {
				debugln(format, Arrays.toString(array));
			}
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			int bytesRead = is.read(b, off, len);
			if (bytesRead == -1) {
				return bytesRead;
			}

			if (!doIntercept) {
				debugln("Skip intercept.");
				return bytesRead;
			}

			if (off != 0) {
				debugln("Offset: %s != 0; terminating intercept.", off);
				terminateIntercept();
				return bytesRead;
			}

			totalRead += bytesRead;
			if (totalRead > INTERCEPT_THRESHOLD) {
				debugln("Exceeded intercept threshold, terminating intercept. %s > %s", totalRead, INTERCEPT_THRESHOLD);
				terminateIntercept();
				return bytesRead;
			}

			debugln("Total read: %s", totalRead);
			debugln("Bytes read: %s", bytesRead);

			byte[] tmpBuffer = new byte[totalRead];
			System.arraycopy(buffer, 0, tmpBuffer, 0, Math.min(tmpBuffer.length, buffer.length));
			System.arraycopy(b, off, tmpBuffer, totalRead - bytesRead, bytesRead);
			buffer = tmpBuffer;

			debugln("Source: %s", b);
			debugln("Buffer: %s", buffer);

			while (position < totalRead && (totalRead - position) >= 2) {
				debugln("Start loop, position: %s", position);

				if (remainingSkip > 0) {
					position += remainingSkip;
					remainingSkip = 0;
					debugln("Skip requested, new position: %s", position);
					continue;
				}

				if (doCaptureApp1) {
					// Check we can buffer up to "Exif" identifier.
					if (startApp1 + 8 > position) {
						debugln("APP1 shorter than expected, terminating intercept.");
						terminateIntercept();
						break;
					}
					byte[] header = new byte[4];
					System.arraycopy(buffer, startApp1 + 4, header, 0, header.length);

					if (new String(header).equals("Exif")) {
						debugln("Found Exif!");
						hasCapturedExif = true;
						doIntercept = false;
						byte[] exifData = new byte[endApp1 - (startApp1 + 4)];
						System.arraycopy(buffer, startApp1 + 4, exifData, 0, exifData.length);
						buffer = exifData;
						break;
					} else {
						debugln("APP1 was not Exif.");
						hasCapturedExif = false;
						doIntercept = true;
						doCaptureApp1 = false;
					}
				}

				if (position == 0 && totalRead >= 2) {
					// Check the first two bytes of stream to see if SOI exists.
					// If SOI is not found, this is not a JPEG.
					debugln("Check if JPEG. buffer: %s", buffer);
					if (!(buffer[position] == (byte) 0xFF && buffer[position + 1] == (byte) 0xD8)) {
						// Not SOI, so it's not a JPEG.
						// We no longer need to keep intercepting.
						debugln("JFIF SOI not found. Not JPEG.");
						terminateIntercept();
						break;
					}

					position += 2;
					continue;
				}

				debugln("Prior to 2-byte section. position: %s, total read: %s", position, totalRead);
				if (position + 2 <= totalRead) {
					if (buffer[position] == (byte) 0xFF) {
						if (buffer[position + 1] >= (byte) 0xD0 && buffer[position + 1] <= (byte) 0xD7) {
							// RSTn - a 2-byte marker.
							debugln("Found RSTn marker.");
							position += 2;
							continue;
						} else if (buffer[position + 1] == (byte) 0xDA || buffer[position + 1] == (byte) 0xD9) {
							// 0xDA -> SOS - Start of Scan
							// 0xD9 -> EOI - End of Image
							// In both cases, terminate the scan for Exif data.
							debugln("Stop scan for Exif. Found: %s, %s", buffer[position], buffer[position + 1]);
							terminateIntercept();
							break;
						}
					}
				}

				debugln("Prior to 4-byte section. position: %s, total read: %s", position, totalRead);
				if (position + 4 <= totalRead) {
					try {
						if (buffer[position] == (byte) 0xFF) {
							if (buffer[position + 1] == (byte) 0xE1) {
								// APP1
								doCaptureApp1 = true;
								startApp1 = position;

								// payload + marker
								int incrementBy = getPayloadLength(buffer[position + 2], buffer[position + 3]) + 4;
								debugln("Prior to 2-byte section. position: %s, total read: %s", position, totalRead);

								int newPosition = incrementBy + position;
								endApp1 = newPosition;
								debugln("Found APP1. position: %s, total read: %s, increment by: %s", position, totalRead, incrementBy);
								debugln("Found APP1. start: %s, end: %s", startApp1, endApp1);
								if (newPosition > totalRead) {
									remainingSkip = newPosition - totalRead;
									position = totalRead;
									debugln("Skip request; remaining skip: %s", remainingSkip);
								} else {
									position = newPosition;
									debugln("No skip needed; new position: %s", newPosition);
								}
								continue;

							} else if (buffer[1] == (byte) 0xDD) {
								// DRI (this is a 4-byte marker w/o payload.)
								debugln("Found DRI.");
								position += 4;
								continue;
							}

							// Other markers like APP0, DQT don't need any special processing.

							int incrementBy = getPayloadLength(buffer[position + 2], buffer[position + 3]) + 4;
							int newPosition = incrementBy + position;
							debugln("Other 4-byte. position: %s, total read: %s, increment by: %s", position, totalRead, incrementBy);
							debugln("Other 4-byte. start: %s, end: %s", startApp1, endApp1);
							if (newPosition > totalRead) {
								remainingSkip = newPosition - totalRead;
								position = totalRead;
								debugln("Skip request; remaining skip: %s", remainingSkip);
							} else {
								position = newPosition;
								debugln("No skip needed; new position: %s", newPosition);
							}
							continue;
						}
					} catch (Exception e) {
						// Immediately drop everything, as we can't recover.
						// TODO Record what went wrong.
						debugln("[Exception] Exception thrown. Terminating intercept.");
						debugln("[Exception] %s", e.toString());
						for (StackTraceElement el : e.getStackTrace()) {
							debugln("[Exception] %s", el.toString());
						}
						terminateIntercept();
						break;
					}
				}

				if (totalRead <= 6) {
					// SOI (2 bytes) + marker+length (4 bytes) == 6 bytes
					// If we didn't find a 2-byte (standalone) marker, then
					// we'll need to wait around to get enough one for 4-byte.
					debugln("Not enough data read. Attempt one additional read.");
					break;
				}

				terminateIntercept();
				debugln("Shouldn't be here. Terminating intercept.");
				break;
			}

			return bytesRead;
		}

		@Override
		public int read() throws IOException {
			return is.read();
		}

		/**
		 * Returns the payload length from the marker header.
		 * @param a			First byte of payload length.
		 * @param b			Second byte of payload length.
		 * @return			Length as an integer.
		 */
		private static int getPayloadLength(byte a, byte b) {
			int length = ByteBuffer.wrap(new byte[] {a, b}).getShort() - 2;
			if (length <= 0) {
				throw new IllegalStateException(
						"Expected a positive payload length, but was " + length
				);
			}

			return length;
		}
	}

	public BufferedImage read() throws IOException {
		ImageInputStream iis = ImageIO.createImageInputStream(is);
		
		if (iis == null) {
			throw new IOException("Could not open InputStream.");
		}
		
		Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
		if (!readers.hasNext()) {
			iis.close();
			throw new UnsupportedFormatException(
					UnsupportedFormatException.UNKNOWN,
					"No suitable ImageReader found for source data."
			);
		}
		
		ImageReader reader = readers.next();
		reader.setInput(iis);

		boolean isExceptionThrown = false;
		try {
			BufferedImage img = readImage(reader);
			return finishedReading(img);

		} catch (IOException e) {
			isExceptionThrown = true;
			throw e;

		} finally {
			/*
			 * Dispose the reader to free resources.
			 *
			 * This seems to be one of the culprits which was causing
			 * `OutOfMemoryError`s which began appearing frequently with
			 * Java 7 Update 21.
			 *
			 * Issue:
			 * https://github.com/coobird/thumbnailator/issues/42
			 */
			reader.dispose();

			try {
				iis.close();
			} catch (IOException e) {
				// TODO If above Java 7, we can use Throwable.addSuppressed
				// Suppress this exception from superseding the original exception.
				// Original exception is likely to be more informational than this one.
				if (!isExceptionThrown) {
					throw e;
				}
			}
		}
	}

	private BufferedImage readImage(ImageReader reader) throws IOException {
		Orientation orientation = null;
		try {
			if (param.useExifOrientation()) {

				// Attempt to use Exif reader of the ImageReader.
				// If the ImageReader fails like seen in Issue #108, use the
				// backup method of using the captured Exif data.
				boolean useExifFromRawData = false;
				try {
					orientation = ExifUtils.getExifOrientation(reader, FIRST_IMAGE_INDEX);
				} catch (Exception e) {
					// TODO Would be useful to capture why it didn't work.
					useExifFromRawData = true;
				}

				if (useExifFromRawData && is instanceof ExifCaptureInputStream) {
					byte[] exifData = ((ExifCaptureInputStream)is).getExifData();
					if (exifData != null) {
						orientation = ExifUtils.getOrientationFromExif(exifData);
					}
				}

				// Skip this code block if there's no rotation needed.
				if (orientation != null && orientation != Orientation.TOP_LEFT) {
					List<ImageFilter> filters = param.getImageFilters();

					// EXIF orientation filter is added to the beginning, as
					// it should be performed early to prevent mis-orientation
					// in later filters.
					filters.add(0, ExifFilterUtils.getFilterForOrientation(orientation));
				}
			}
		} catch (Exception e) {
			// If something goes wrong, then skip the orientation-related
			// processing.
			// TODO Ought to have some way to track errors.
		}

		inputFormatName = reader.getFormatName();

		ImageReadParam irParam = reader.getDefaultReadParam();
		int width = reader.getWidth(FIRST_IMAGE_INDEX);
		int height = reader.getHeight(FIRST_IMAGE_INDEX);

		if (param != null && param.getSourceRegion() != null) {
			Region region = param.getSourceRegion();
			irParam.setSourceRegion(
					calculateSourceRegion(width, height, orientation, region)
			);
		}

		/*
		 * FIXME Workaround to enable subsampling for large source images.
		 *
		 * Issue:
		 * https://github.com/coobird/thumbnailator/issues/69
		 */
		if (param != null &&
				Configurations.CONSERVE_MEMORY_WORKAROUND.getBoolean() &&
				width > 1800 && height > 1800 &&
				(width * height * 4L > Runtime.getRuntime().freeMemory() / 4)
		) {
			int subsampling = 1;

			// Calculate the maximum subsampling that can be used.
			if (param.getSize() != null && (param.getSize().width * 2 < width && param.getSize().height * 2 < height)) {
				int targetWidth = param.getSize().width;
				int targetHeight = param.getSize().height;

				// Handle cases where .width() or .height() is called. (Issue 161)
				targetWidth = targetWidth != Integer.MAX_VALUE ? targetWidth : targetHeight;
				targetHeight = targetHeight != Integer.MAX_VALUE ? targetHeight : targetWidth;

				double widthScaling = (double)width / (double)targetWidth;
				double heightScaling = (double)height / (double)targetHeight;

				subsampling = (int)Math.floor(Math.min(widthScaling, heightScaling));

			} else if (param.getSize() == null) {
				subsampling = (int)Math.max(1, Math.floor(1 / Math.max(param.getHeightScalingFactor(), param.getWidthScalingFactor())));
			}

			// Prevent excessive subsampling that can ruin image quality.
			// This will ensure that at least a 600 x 600 image will be used as source.
			for (; (width / subsampling) < 600 || (height / subsampling) < 600; subsampling--);

			// If scaling factor based resize is used, need to change the scaling factor.
			if (param.getSize() == null) {
				try {
					Class<?> c = param.getClass();
					Field heightField = c.getDeclaredField("heightScalingFactor");
					Field widthField = c.getDeclaredField("widthScalingFactor");
					heightField.setAccessible(true);
					widthField.setAccessible(true);
					heightField.set(param, param.getHeightScalingFactor() * (double)subsampling);
					widthField.set(param, param.getWidthScalingFactor() * (double)subsampling);

				} catch (Exception e) {
					// If we can't update the parameter, then disable subsampling.
					subsampling = 1;
				}
			}

			irParam.setSourceSubsampling(subsampling, subsampling, 0, 0);
		}

		return reader.read(FIRST_IMAGE_INDEX, irParam);
	}

	private Rectangle calculateSourceRegion(int width, int height, Orientation orientation, Region region) {
		boolean flipHorizontal = false;
		boolean flipVertical = false;
		boolean swapDimensions = false;

		/*
		 * Fix for Issue 207:
		 * https://github.com/coobird/thumbnailator/issues/207
		 *
		 * Source region should be selected from the image _after_ applying
		 * the Exif orientation. Therefore, we need to change the source
		 * region based on the Exif orientation, as source pixels will be
		 * oriented differently.
		 */
		if (orientation == Orientation.TOP_RIGHT) {
			flipHorizontal = true;

		} else if (orientation == Orientation.BOTTOM_RIGHT) {
			flipHorizontal = true;
			flipVertical = true;

		} else if (orientation == Orientation.BOTTOM_LEFT) {
			flipVertical = true;

		} else if (orientation == Orientation.LEFT_TOP) {
			swapDimensions = true;

		} else if (orientation == Orientation.RIGHT_TOP) {
			flipVertical = true;
			swapDimensions = true;

		} else if (orientation == Orientation.RIGHT_BOTTOM) {
			flipHorizontal = true;
			flipVertical = true;
			swapDimensions = true;

		} else if (orientation == Orientation.LEFT_BOTTOM) {
			flipHorizontal = true;
			swapDimensions = true;
		}

		return region.calculate(
				width, height, flipHorizontal, flipVertical, swapDimensions
		);
	}

	public InputStream getSource() {
		return is;
	}
}
