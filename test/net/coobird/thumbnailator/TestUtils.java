package net.coobird.thumbnailator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;

public class TestUtils
{
	/**
	 * Copies a file.
	 * 
	 * @param sourceFile		The source file.
	 * @param destFile			The destination file.
	 * @throws IOException		If an IOException is thrown.
	 */
	public static void copyFile(File sourceFile, File destFile) throws IOException
	{
		FileInputStream fis = new FileInputStream(sourceFile);
		FileOutputStream fos = new FileOutputStream(destFile);
		fis.getChannel().transferTo(0, sourceFile.length(), fos.getChannel());
		fis.close();
		fos.close();
	}

	/**
	 * Returns the format of an image which is read through the {@link InputStream}.
	 * 
	 * @param is			The {@link InputStream} to an image.
	 * @return				File format of the image.
	 * @throws IOException
	 */
	public static String getFormatName(InputStream is) throws IOException
	{
		return ImageIO.getImageReaders(
				ImageIO.createImageInputStream(is)
		).next().getFormatName();
	}

	public static void makeTemporaryDirectory(String dir)
	{
		new File(dir).mkdirs();
	}

	public static void deleteTemporaryDirectory(String dir)
	{
		File tmpDir = new File(dir);
		for (File f : tmpDir.listFiles())
		{
			f.delete();
		}
		tmpDir.delete();
		
		File tmpParentDir = tmpDir.getParentFile(); 
		if (tmpParentDir.isDirectory() && tmpParentDir.getName().equals("tmp"))
		{
			tmpParentDir.delete();
		}
	}
	
	public static File createTempFile(String dir, String ext) throws IOException
	{
		return new File(
				dir,
				"tmp-" + Math.abs(new Random().nextLong()) + "." + ext
		);
	}
}
