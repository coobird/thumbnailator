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

package net.coobird.thumbnailator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;

public class TestUtils {
	/**
	 * Copies a specified length of the source file.
	 * 
	 * @param sourceFile		The source file.
	 * @param destFile			The destination file.
	 * @param length			Length of source file to copy.
	 * @throws IOException		If an IOException is thrown.
	 */
	public static void copyFile(File sourceFile, File destFile, long length) throws IOException {
		FileInputStream fis = new FileInputStream(sourceFile);
		FileOutputStream fos = new FileOutputStream(destFile);
		fis.getChannel().transferTo(0, length, fos.getChannel());
		fis.close();
		fos.close();
	}

	/**
	 * Copies a file.
	 *
	 * @param sourceFile		The source file.
	 * @param destFile			The destination file.
	 * @throws IOException		If an IOException is thrown.
	 */
	public static void copyFile(File sourceFile, File destFile) throws IOException {
		copyFile(sourceFile, destFile, sourceFile.length());
	}

	/**
	 * Returns the format of an image which is read through the {@link InputStream}.
	 * 
	 * @param is			The {@link InputStream} to an image.
	 * @return				File format of the image.
	 * @throws IOException
	 */
	public static String getFormatName(InputStream is) throws IOException {
		return ImageIO.getImageReaders(
				ImageIO.createImageInputStream(is)
		).next().getFormatName();
	}

	public static void makeTemporaryDirectory(String dir) {
		new File(dir).mkdirs();
	}

	public static void deleteTemporaryDirectory(String dir) {
		File tmpDir = new File(dir);
		for (File f : tmpDir.listFiles()) {
			f.delete();
		}
		tmpDir.delete();
		
		File tmpParentDir = tmpDir.getParentFile();
		if (tmpParentDir.isDirectory() && tmpParentDir.getName().equals("tmp")) {
			tmpParentDir.delete();
		}
	}
	
	public static File createTempFile(String dir, String ext) throws IOException {
		return createTempFile(new File(dir), ext);
	}

	public static File createTempFile(File dir, String ext) throws IOException {
		return new File(
				dir,
				"tmp-" + Math.abs(new Random().nextLong()) + "." + ext
		);
	}
}
