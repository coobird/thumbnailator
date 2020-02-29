/*
 * Thumbnailator - a thumbnail generation library
 *
 * Copyright (c) 2008-2020 Chris Kroells
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

package net.coobird.thumbnailator.util.exif;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * An utility class used to obtain the orientation information from a given
 * Exif metadata.
 * 
 * @author coobird
 *
 */
public final class ExifUtils {
	
	private static final String EXIF_MAGIC_STRING = "Exif";
	
	/**
	 * This class should not be instantiated.
	 */
	private ExifUtils() {};
	
	/**
	 * Returns the orientation obtained from the Exif metadata.
	 * 
	 * @param reader		An {@link ImageReader} which is reading the
	 * 						target image.
	 * @param imageIndex	The index of the image from which the Exif
	 * 						metadata should be read from.
	 * @return				The orientation information obtained from the
	 * 						Exif metadata, as a {@link Orientation} enum.
	 * @throws IOException				When an error occurs during reading.
	 * @throws IllegalArgumentException	If the {@link ImageReader} does not
	 * 									have the target image set, or if the
	 * 									reader does not have a JPEG open.
	 */
	public static Orientation getExifOrientation(ImageReader reader, int imageIndex) throws IOException {
		IIOMetadata metadata = reader.getImageMetadata(imageIndex);
		Node rootNode = metadata.getAsTree("javax_imageio_jpeg_image_1.0");

		NodeList childNodes = rootNode.getChildNodes();

		// Look for the APP1 containing Exif data, and retrieve it.
		for (int i = 0; i < childNodes.getLength(); i++) {
			if ("markerSequence".equals(childNodes.item(i).getNodeName())) {
				NodeList markerSequenceChildren = childNodes.item(i).getChildNodes();

				for (int j = 0; j < markerSequenceChildren.getLength(); j++) {
					IIOMetadataNode metadataNode = (IIOMetadataNode) (markerSequenceChildren.item(j));

					byte[] bytes = (byte[]) metadataNode.getUserObject();
					if (bytes == null) {
						continue;
					}

					byte[] magicNumber = new byte[4];
					ByteBuffer.wrap(bytes).get(magicNumber);

					if (EXIF_MAGIC_STRING.equals(new String(magicNumber))) {
						return getOrientationFromExif(bytes);
					}
				}
			}
		}

		return null;
	}

	private static Orientation getOrientationFromExif(byte[] exifData) {
		// Needed to make byte-wise reading easier.
		ByteBuffer buffer = ByteBuffer.wrap(exifData);

		byte[] exifId = new byte[4];
		buffer.get(exifId);

		if (!EXIF_MAGIC_STRING.equals(new String(exifId))) {
			return null;
		}

		// read the \0 after the Exif
		buffer.get();
		// read the padding byte
		buffer.get();

		byte[] tiffHeader = new byte[8];
		buffer.get(tiffHeader);

		/*
		 * The first 2 bytes of the TIFF header contains either:
		 *   "II" for Intel byte alignment (little endian), or
		 *   "MM" for Motorola byte alignment (big endian)
		 */
		ByteOrder bo;
		if (tiffHeader[0] == 'I' && tiffHeader[1] == 'I') {
			bo = ByteOrder.LITTLE_ENDIAN;
		} else {
			bo = ByteOrder.BIG_ENDIAN;
		}

		byte[] numFields = new byte[2];
		buffer.get(numFields);

		int nFields = ByteBuffer.wrap(numFields).order(bo).getShort();

		byte[] ifd = new byte[12];
		for (int i = 0; i < nFields; i++) {
			buffer.get(ifd);
			IfdStructure ifdStructure = readIFD(ifd, bo);

			// Return the orientation from the orientation IFD
			if (ifdStructure.getTag() == 0x0112) {
				return Orientation.typeOf(ifdStructure.getOffsetValue());
			}
		}

		return null;
	}

	private static IfdStructure readIFD(byte[] ifd, ByteOrder bo) {
		ByteBuffer buffer = ByteBuffer.wrap(ifd).order(bo);

		short tag = buffer.getShort();
		short type = buffer.getShort();
		int count = buffer.getInt();

		IfdType ifdType = IfdType.typeOf(type);
		int offsetValue = 0;
		
		/*
		 * Per section 4.6.2 of the Exif Spec, if value is smaller than
		 * 4 bytes, it will exist in the earlier byte.
		 */
		int byteSize = count * ifdType.size();

		if (byteSize <= 4) {
			if (ifdType == IfdType.SHORT) {
				for (int i = 0; i < count; i++) {
					offsetValue = buffer.getShort();
				}
			} else if (ifdType == IfdType.BYTE || ifdType == IfdType.ASCII || ifdType == IfdType.UNDEFINED) {
				for (int i = 0; i < count; i++) {
					offsetValue = buffer.get();
				}
			} else {
				offsetValue = buffer.getInt();
			}
		} else {
			offsetValue = buffer.getInt();
		}
		
		return new IfdStructure(tag, type, count, offsetValue);
	}
}
