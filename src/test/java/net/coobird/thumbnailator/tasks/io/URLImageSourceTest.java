package net.coobird.thumbnailator.tasks.io;

import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.builders.ThumbnailParameterBuilder;
import net.coobird.thumbnailator.geometry.AbsoluteSize;
import net.coobird.thumbnailator.geometry.Coordinate;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.geometry.Region;
import net.coobird.thumbnailator.test.BufferedImageAssert;
import net.coobird.thumbnailator.test.BufferedImageComparer;

import org.junit.Test;

public class URLImageSourceTest
{
	@Test
	public void proxySpecfied() throws IOException
	{
		// given
		Proxy proxy = Proxy.NO_PROXY;
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Thumbnailator/grid.png"), proxy);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertEquals(100, img.getWidth());
		assertEquals(100, img.getHeight());
		assertEquals("png", source.getInputFormatName());
	}
	
	@Test(expected=NullPointerException.class)
	public void givenNullURL() throws IOException
	{
		try
		{
			// given
			// when
			URL url = null;
			new URLImageSource(url);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("URL cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void givenNullString() throws IOException
	{
		try
		{
			// given
			// when
			String url = null;
			new URLImageSource(url);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("URL cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void givenURL_givenNullProxy() throws IOException
	{
		try
		{
			// given
			// when
			new URLImageSource(new URL("file:src/test/resources/Thumbnailator/grid.png"), null);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("Proxy cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void givenString_givenNullProxy() throws IOException
	{
		try
		{
			// given
			// when
			new URLImageSource("file:src/test/resources/Thumbnailator/grid.png", null);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("Proxy cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test
	public void fileExists_Png() throws IOException
	{
		// given
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Thumbnailator/grid.png"));
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertEquals(100, img.getWidth());
		assertEquals(100, img.getHeight());
		assertEquals("png", source.getInputFormatName());
	}
	
	@Test
	public void fileExists_Jpeg() throws IOException
	{
		// given
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Thumbnailator/grid.jpg"));
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertEquals(100, img.getWidth());
		assertEquals(100, img.getHeight());
		assertEquals("JPEG", source.getInputFormatName());
	}
	
	@Test
	public void fileExists_Bmp() throws IOException
	{
		// given
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Thumbnailator/grid.bmp"));
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertEquals(100, img.getWidth());
		assertEquals(100, img.getHeight());
		assertEquals("bmp", source.getInputFormatName());
	}
	
	@Test(expected=IOException.class)
	public void fileDoesNotExists() throws IOException
	{
		// given
		URLImageSource source = new URLImageSource(new URL("file:notfound"));
		
		try
		{
			// when
			source.read();
		}
		catch (IOException e)
		{
			// then
			assertThat(e.getMessage(), containsString("Could not open connection to URL:"));
			throw e;
		}
		fail();
	}
	
	@Test(expected=IllegalStateException.class)
	public void fileExists_getInputFormatNameBeforeRead() throws IOException
	{
		// given
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Thumbnailator/grid.png"));
		
		try
		{
			// when
			source.getInputFormatName();
		}
		catch (IllegalStateException e)
		{
			// then
			assertEquals("Input has not been read yet.", e.getMessage());
			throw e;
		}
	}

	
	/*
	 *
	 *     +------+-----------+
	 *     |XXXXXX|           |
	 *     |XXXXXX|           |
	 *     +------+           |
	 *     |      region      |
	 *     |                  |
	 *     |                  |
	 *     |                  |
	 *     |                  |
	 *     +------------------+
	 *                        source
	 */
	@Test
	public void appliesSourceRegion() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		BufferedImage sourceImage = ImageIO.read(sourceFile);
		
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Thumbnailator/grid.png"));
		source.setThumbnailParameter(
				new ThumbnailParameterBuilder()
					.region(new Region(Positions.TOP_LEFT, new AbsoluteSize(40, 40)))
					.size(20, 20)
					.build()
		);
		
		// when
		BufferedImage img = source.read();
			
		// then
		BufferedImage expectedImg = sourceImage.getSubimage(0, 0, 40, 40);
		assertTrue(BufferedImageComparer.isRGBSimilar(expectedImg, img));
	}
	
	/*
	 *
	 *     +------------------+ source
	 *     |  +------------------+
	 *     |  |XXXXXXXXXXXXXXX|  |
	 *     |  |XXXXXXXXXXXXXXX|  |
	 *     |  |XX  final  XXXX|  |
	 *     |  |XX  region XXXX|  |
	 *     |  |XXXXXXXXXXXXXXX|  |
	 *     |  |XXXXXXXXXXXXXXX|  |
	 *     |  |XXXXXXXXXXXXXXX|  |
	 *     +--|---------------+  |
	 *        +------------------+
	 *                             region
	 */
	@Test
	public void appliesSourceRegionTooBig() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		BufferedImage sourceImage = ImageIO.read(sourceFile);
		
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Thumbnailator/grid.png"));
		source.setThumbnailParameter(
				new ThumbnailParameterBuilder()
					.region(new Region(new Coordinate(20, 20), new AbsoluteSize(100, 100)))
					.size(80, 80)
					.build()
		);
		
		// when
		BufferedImage img = source.read();
		
		// then
		BufferedImage expectedImg = sourceImage.getSubimage(20, 20, 80, 80);
		assertTrue(BufferedImageComparer.isRGBSimilar(expectedImg, img));
	}
	
	/*
	 *   +-----------------+
	 *   |                 |
	 *   | +---------------|--+
	 *   | |XXXXXXXXXXXXXXX|  |
	 *   | |XXXXXXXXXXXXXXX|  |
	 *   | |XXXX final XXXX|  |
	 *   | |XXXX regionXXXX|  |
	 *   | |XXXXXXXXXXXXXXX|  |
	 *   | |XXXXXXXXXXXXXXX|  |
	 *   +-----------------+  |
	 *     |                region
	 *     +------------------+
	 *                        source
	 */
	@Test
	public void appliesSourceRegionBeyondOrigin() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		BufferedImage sourceImage = ImageIO.read(sourceFile);
		
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Thumbnailator/grid.png"));
		source.setThumbnailParameter(
				new ThumbnailParameterBuilder()
					.region(new Region(new Coordinate(-20, -20), new AbsoluteSize(100, 100)))
					.size(80, 80)
					.build()
		);
		
		// when
		BufferedImage img = source.read();
		
		// then
		BufferedImage expectedImg = sourceImage.getSubimage(0, 0, 80, 80);
		assertTrue(BufferedImageComparer.isRGBSimilar(expectedImg, img));
	}
	
	@Test
	public void appliesSourceRegionNotSpecified() throws IOException
	{
		// given
		File sourceFile = new File("src/test/resources/Thumbnailator/grid.png");
		BufferedImage sourceImage = ImageIO.read(sourceFile);
		
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Thumbnailator/grid.png"));
		source.setThumbnailParameter(
				new ThumbnailParameterBuilder()
					.size(20, 20)
					.build()
		);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertTrue(BufferedImageComparer.isRGBSimilar(sourceImage, img));
	}
	
	
	@Test
	public void readImageUnaffectedForOrientation1() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_1.jpg");
		BufferedImage sourceImage = ImageIO.read(sourceFile);

		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Exif/source_1.jpg"));

		ThumbnailParameter param =
				new ThumbnailParameterBuilder().size(20, 20).build();
		source.setThumbnailParameter(param);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertTrue(BufferedImageComparer.isRGBSimilar(sourceImage, img));
	}

	@Test
	public void readImageUnaffectedForOrientation2() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_2.jpg");
		BufferedImage sourceImage = ImageIO.read(sourceFile);

		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Exif/source_2.jpg"));

		ThumbnailParameter param =
				new ThumbnailParameterBuilder().size(20, 20).build();
		source.setThumbnailParameter(param);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertTrue(BufferedImageComparer.isRGBSimilar(sourceImage, img));
	}
	
	@Test
	public void readImageUnaffectedForOrientation3() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_3.jpg");
		BufferedImage sourceImage = ImageIO.read(sourceFile);

		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Exif/source_3.jpg"));

		ThumbnailParameter param =
				new ThumbnailParameterBuilder().size(20, 20).build();
		source.setThumbnailParameter(param);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertTrue(BufferedImageComparer.isRGBSimilar(sourceImage, img));
	}
	
	@Test
	public void readImageUnaffectedForOrientation4() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_4.jpg");
		BufferedImage sourceImage = ImageIO.read(sourceFile);

		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Exif/source_4.jpg"));

		ThumbnailParameter param =
				new ThumbnailParameterBuilder().size(20, 20).build();
		source.setThumbnailParameter(param);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertTrue(BufferedImageComparer.isRGBSimilar(sourceImage, img));
	}
	
	@Test
	public void readImageUnaffectedForOrientation5() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_5.jpg");
		BufferedImage sourceImage = ImageIO.read(sourceFile);

		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Exif/source_5.jpg"));

		ThumbnailParameter param =
				new ThumbnailParameterBuilder().size(20, 20).build();
		source.setThumbnailParameter(param);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertTrue(BufferedImageComparer.isRGBSimilar(sourceImage, img));
	}
	
	@Test
	public void readImageUnaffectedForOrientation6() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_6.jpg");
		BufferedImage sourceImage = ImageIO.read(sourceFile);

		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Exif/source_6.jpg"));

		ThumbnailParameter param =
				new ThumbnailParameterBuilder().size(20, 20).build();
		source.setThumbnailParameter(param);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertTrue(BufferedImageComparer.isRGBSimilar(sourceImage, img));
	}
	
	@Test
	public void readImageUnaffectedForOrientation7() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_7.jpg");
		BufferedImage sourceImage = ImageIO.read(sourceFile);

		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Exif/source_7.jpg"));

		ThumbnailParameter param =
				new ThumbnailParameterBuilder().size(20, 20).build();
		source.setThumbnailParameter(param);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertTrue(BufferedImageComparer.isRGBSimilar(sourceImage, img));
	}
	
	@Test
	public void readImageUnaffectedForOrientation8() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_8.jpg");
		BufferedImage sourceImage = ImageIO.read(sourceFile);

		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Exif/source_8.jpg"));

		ThumbnailParameter param =
				new ThumbnailParameterBuilder().size(20, 20).build();
		source.setThumbnailParameter(param);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertTrue(BufferedImageComparer.isRGBSimilar(sourceImage, img));
	}
	
	@Test
	public void containsCorrectFilterForOrientation1() throws Exception
	{
		// given
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Exif/source_1.jpg"));
		
		ThumbnailParameter param =
				new ThumbnailParameterBuilder().size(20, 20).build();
		source.setThumbnailParameter(param);
		
		// when
		source.read();
		
		// then
		assertTrue(param.getImageFilters().isEmpty());
	}	
	
	@Test
	public void containsCorrectFilterForOrientation2() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_2.jpg");
		BufferedImage sourceImage = ImageIO.read(sourceFile);
		
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Exif/source_2.jpg"));
		
		ThumbnailParameter param =
				new ThumbnailParameterBuilder().size(20, 20).build();
		source.setThumbnailParameter(param);
		
		// when
		source.read();
		
		// then
		BufferedImage result = param.getImageFilters().get(0).apply(sourceImage);
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
	public void containsCorrectFilterForOrientation3() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_3.jpg");
		BufferedImage sourceImage = ImageIO.read(sourceFile);
		
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Exif/source_3.jpg"));
		
		ThumbnailParameter param =
				new ThumbnailParameterBuilder().size(20, 20).build();
		source.setThumbnailParameter(param);
		
		// when
		source.read();
		
		// then
		BufferedImage result = param.getImageFilters().get(0).apply(sourceImage);
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
	public void containsCorrectFilterForOrientation4() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_4.jpg");
		BufferedImage sourceImage = ImageIO.read(sourceFile);
		
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Exif/source_4.jpg"));
		
		ThumbnailParameter param =
				new ThumbnailParameterBuilder().size(20, 20).build();
		source.setThumbnailParameter(param);
		
		// when
		source.read();
		
		// then
		BufferedImage result = param.getImageFilters().get(0).apply(sourceImage);
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
	public void containsCorrectFilterForOrientation5() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_5.jpg");
		BufferedImage sourceImage = ImageIO.read(sourceFile);
		
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Exif/source_5.jpg"));
		
		ThumbnailParameter param =
				new ThumbnailParameterBuilder().size(20, 20).build();
		source.setThumbnailParameter(param);
		
		// when
		source.read();
		
		// then
		BufferedImage result = param.getImageFilters().get(0).apply(sourceImage);
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
	public void containsCorrectFilterForOrientation6() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_6.jpg");
		BufferedImage sourceImage = ImageIO.read(sourceFile);
		
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Exif/source_6.jpg"));
		
		ThumbnailParameter param =
				new ThumbnailParameterBuilder().size(20, 20).build();
		source.setThumbnailParameter(param);
		
		// when
		source.read();
		
		// then
		BufferedImage result = param.getImageFilters().get(0).apply(sourceImage);
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
	public void containsCorrectFilterForOrientation7() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_7.jpg");
		BufferedImage sourceImage = ImageIO.read(sourceFile);
		
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Exif/source_7.jpg"));
		
		ThumbnailParameter param =
				new ThumbnailParameterBuilder().size(20, 20).build();
		source.setThumbnailParameter(param);
		
		// when
		source.read();
		
		// then
		BufferedImage result = param.getImageFilters().get(0).apply(sourceImage);
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
	public void containsCorrectFilterForOrientation8() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_8.jpg");
		BufferedImage sourceImage = ImageIO.read(sourceFile);
		
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Exif/source_8.jpg"));
		
		ThumbnailParameter param =
				new ThumbnailParameterBuilder().size(20, 20).build();
		source.setThumbnailParameter(param);
		
		// when
		source.read();
		
		// then
		BufferedImage result = param.getImageFilters().get(0).apply(sourceImage);
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
	public void useExifOrientationIsTrue_OrientationHonored() throws Exception
	{
		// given
		File sourceFile = new File("src/test/resources/Exif/source_2.jpg");
		BufferedImage sourceImage = ImageIO.read(sourceFile);
		
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Exif/source_2.jpg"));
		
		ThumbnailParameter param =
				new ThumbnailParameterBuilder()
						.size(20, 20)
						.useExifOrientation(true)
						.build();
		
		source.setThumbnailParameter(param);
		
		// when
		source.read();
		
		// then
		BufferedImage result = param.getImageFilters().get(0).apply(sourceImage);
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
	public void useExifOrientationIsFalse_OrientationIgnored() throws Exception
	{
		// given
		URLImageSource source = new URLImageSource(new URL("file:src/test/resources/Exif/source_2.jpg"));
		
		ThumbnailParameter param =
				new ThumbnailParameterBuilder()
						.size(20, 20)
						.useExifOrientation(false)
						.build();
		
		source.setThumbnailParameter(param);
		
		// when
		BufferedImage result = source.read();
		
		// then
		assertTrue(param.getImageFilters().isEmpty());
		BufferedImageAssert.assertMatches(
				result,
				new float[] {
						1, 1, 1,
						1, 1, 1,
						0, 0, 1,
				}
		);
	}
}
