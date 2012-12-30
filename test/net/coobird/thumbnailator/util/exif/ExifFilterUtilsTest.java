package net.coobird.thumbnailator.util.exif;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.test.BufferedImageAssert;
import net.coobird.thumbnailator.util.BufferedImages;

import org.junit.Test;

/**
 * Tests that the {@link ImageFilter}s returned by the 
 * {@link ExifFilterUtils#getFilterForOrientation(Orientation)} method can
 * correctly oriente an image according to the EXIF orientation metadata.
 * <p>
 * The source images were created from the examples of how the letter "F"
 * would appear when the various Exif Orientations are applied, as seen in
 * <a href="http://sylvana.net/jpegcrop/exif_orientation.html">this page</a>.
 * 
 * @author coobird
 *
 */
public class ExifFilterUtilsTest {
	
	@Test
	public void correctOrientation1() throws Exception
	{
		// given
		BufferedImage img = ImageIO.read(new File("test-resources/Exif/source_1.jpg"));
		img = BufferedImages.copy(img, BufferedImage.TYPE_INT_ARGB);
		
		// when
		BufferedImage result = 
				ExifFilterUtils.getFilterForOrientation(Orientation.TOP_LEFT).apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result, 
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}
	
	@Test
	public void correctOrientation2() throws Exception
	{
		// given
		BufferedImage img = ImageIO.read(new File("test-resources/Exif/source_2.jpg"));
		img = BufferedImages.copy(img, BufferedImage.TYPE_INT_ARGB);
		
		// when
		BufferedImage result = 
				ExifFilterUtils.getFilterForOrientation(Orientation.TOP_RIGHT).apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result, 
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}
	
	@Test
	public void correctOrientation3() throws Exception
	{
		// given
		BufferedImage img = ImageIO.read(new File("test-resources/Exif/source_3.jpg"));
		img = BufferedImages.copy(img, BufferedImage.TYPE_INT_ARGB);
		
		// when
		BufferedImage result = 
				ExifFilterUtils.getFilterForOrientation(Orientation.BOTTOM_RIGHT).apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result, 
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}
	
	@Test
	public void correctOrientation4() throws Exception
	{
		// given
		BufferedImage img = ImageIO.read(new File("test-resources/Exif/source_4.jpg"));
		img = BufferedImages.copy(img, BufferedImage.TYPE_INT_ARGB);
		
		// when
		BufferedImage result = 
				ExifFilterUtils.getFilterForOrientation(Orientation.BOTTOM_LEFT).apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result, 
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}
	
	@Test
	public void correctOrientation5() throws Exception 
	{
		// given
		BufferedImage img = ImageIO.read(new File("test-resources/Exif/source_5.jpg"));
		img = BufferedImages.copy(img, BufferedImage.TYPE_INT_ARGB);
		
		// when
		BufferedImage result = 
				ExifFilterUtils.getFilterForOrientation(Orientation.LEFT_TOP).apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result, 
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}
	
	@Test
	public void correctOrientation6() throws Exception
	{
		// given
		BufferedImage img = ImageIO.read(new File("test-resources/Exif/source_6.jpg"));
		img = BufferedImages.copy(img, BufferedImage.TYPE_INT_ARGB);
		
		// when
		BufferedImage result = 
				ExifFilterUtils.getFilterForOrientation(Orientation.RIGHT_TOP).apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result, 
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}
	
	@Test
	public void correctOrientation7() throws Exception
	{
		// given
		BufferedImage img = ImageIO.read(new File("test-resources/Exif/source_7.jpg"));
		img = BufferedImages.copy(img, BufferedImage.TYPE_INT_ARGB);
		
		// when
		BufferedImage result = 
				ExifFilterUtils.getFilterForOrientation(Orientation.RIGHT_BOTTOM).apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result, 
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}
	
	@Test
	public void correctOrientation8() throws Exception
	{
		// given
		BufferedImage img = ImageIO.read(new File("test-resources/Exif/source_8.jpg"));
		img = BufferedImages.copy(img, BufferedImage.TYPE_INT_ARGB);
		
		// when
		BufferedImage result = 
				ExifFilterUtils.getFilterForOrientation(Orientation.LEFT_BOTTOM).apply(img);
		
		// then
		BufferedImageAssert.assertMatches(
				result, 
				new float[] {
						1, 1, 1,
						1, 1, 1,
						1, 0, 0,
				}
		);
	}
}
