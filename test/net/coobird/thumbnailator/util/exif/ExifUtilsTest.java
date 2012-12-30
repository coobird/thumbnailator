package net.coobird.thumbnailator.util.exif;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.junit.Test;

/**
 * Tests the {@link ExifUtils} class to check that the Exif Orientation
 * tag is correctly acquired by the 
 * {@link ExifUtils#getExifOrientation(ImageReader, int)} method.
 * <p>
 * The Exif Orientation tags has been added to the source images by using
 * <a href="http://owl.phy.queensu.ca/~phil/exiftool/index.html">ExifTool</a>.
 * 
 * @author coobird
 *
 */
public class ExifUtilsTest {
	
	@Test
	public void exifOrientation1() throws Exception 
	{
		// given
		ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
		reader.setInput(ImageIO.createImageInputStream(new File("test-resources/Exif/orientation_1.jpg")));
		
		// when
		Orientation orientation = ExifUtils.getExifOrientation(reader, 0);
		
		// then
		assertEquals(Orientation.typeOf(1), orientation);
	}
	
	@Test
	public void exifOrientation2() throws Exception
	{
		// given
		ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
		reader.setInput(ImageIO.createImageInputStream(new File("test-resources/Exif/orientation_2.jpg")));
		
		// when
		Orientation orientation = ExifUtils.getExifOrientation(reader, 0);
		
		// then
		assertEquals(Orientation.typeOf(2), orientation);
	}
	
	@Test
	public void exifOrientation3() throws Exception
	{
		// given
		ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
		reader.setInput(ImageIO.createImageInputStream(new File("test-resources/Exif/orientation_3.jpg")));
		
		// when
		Orientation orientation = ExifUtils.getExifOrientation(reader, 0);
		
		// then
		assertEquals(Orientation.typeOf(3), orientation);
	}
	
	@Test
	public void exifOrientation4() throws Exception
	{
		// given
		ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
		reader.setInput(ImageIO.createImageInputStream(new File("test-resources/Exif/orientation_4.jpg")));
		
		// when
		Orientation orientation = ExifUtils.getExifOrientation(reader, 0);
		
		// then
		assertEquals(Orientation.typeOf(4), orientation);
	}
	
	@Test
	public void exifOrientation5() throws Exception 
	{
		// given
		ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
		reader.setInput(ImageIO.createImageInputStream(new File("test-resources/Exif/orientation_5.jpg")));
		
		// when
		Orientation orientation = ExifUtils.getExifOrientation(reader, 0);
		
		// then
		assertEquals(Orientation.typeOf(5), orientation);
	}
	
	@Test
	public void exifOrientation6() throws Exception 
	{
		// given
		ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
		reader.setInput(ImageIO.createImageInputStream(new File("test-resources/Exif/orientation_6.jpg")));
		
		// when
		Orientation orientation = ExifUtils.getExifOrientation(reader, 0);
		
		// then
		assertEquals(Orientation.typeOf(6), orientation);
	}
	
	@Test
	public void exifOrientation7() throws Exception 
	{
		// given
		ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
		reader.setInput(ImageIO.createImageInputStream(new File("test-resources/Exif/orientation_7.jpg")));
		
		// when
		Orientation orientation = ExifUtils.getExifOrientation(reader, 0);
		
		// then
		assertEquals(Orientation.typeOf(7), orientation);
	}
	
	@Test
	public void exifOrientation8() throws Exception
	{
		// given
		ImageReader reader = ImageIO.getImageReadersByFormatName("jpg").next();
		reader.setInput(ImageIO.createImageInputStream(new File("test-resources/Exif/orientation_8.jpg")));
		
		// when
		Orientation orientation = ExifUtils.getExifOrientation(reader, 0);
		
		// then
		assertEquals(Orientation.typeOf(8), orientation);
	}
}
