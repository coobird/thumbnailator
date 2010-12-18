package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.builders.ThumbnailParameterBuilder;

import static org.junit.Assert.*;

import org.junit.Test;

public class SourceSinkThumbnailTaskTest
{
	@Test
	public void task_SizeOnly_InputStream_BufferedImage() throws IOException
	{
		// given
		ThumbnailParameter param = 
			new ThumbnailParameterBuilder().size(50, 50).build();
		
		ByteArrayInputStream is = 
			new ByteArrayInputStream(makeImageData("png", 200, 200));
		
		InputStreamImageSource source = new InputStreamImageSource(is);
		BufferedImageSink destination = new BufferedImageSink();
		
		// when
		Thumbnailator.createThumbnail(
				new SourceSinkThumbnailTask(param, source, destination)
		);

		// then
		BufferedImage thumbnail = destination.getImage();
		assertEquals(50, thumbnail.getWidth());
		assertEquals(50, thumbnail.getHeight());
	}
	
	
	@Test
	public void task_SizeOnly_InputStream_OutputStream() throws IOException
	{
		// given
		ThumbnailParameter param = 
			new ThumbnailParameterBuilder().size(50, 50).build();
		
		byte[] imageData = makeImageData("png", 200, 200);
		ByteArrayInputStream is = new ByteArrayInputStream(imageData);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		InputStreamImageSource source = new InputStreamImageSource(is);
		OutputStreamImageSink destination = new OutputStreamImageSink(os);

		// when
		Thumbnailator.createThumbnail(
				new SourceSinkThumbnailTask(param, source, destination)
		);
		
		// then
		ByteArrayInputStream destIs = new ByteArrayInputStream(os.toByteArray());
		BufferedImage thumbnail = ImageIO.read(destIs);
		assertEquals(50, thumbnail.getWidth());
		assertEquals(50, thumbnail.getHeight());
		
		destIs = new ByteArrayInputStream(os.toByteArray());
		String formatName = getFormatName(destIs);
		
		assertEquals("png", formatName);
	}
	
	@Test
	public void task_ChangeOutputFormat_InputStream_OutputStream() throws IOException
	{
		// given
		ThumbnailParameter param = 
			new ThumbnailParameterBuilder().size(50, 50).format("jpg").build();
		
		byte[] imageData = makeImageData("png", 200, 200);
		ByteArrayInputStream is = new ByteArrayInputStream(imageData);
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		InputStreamImageSource source = new InputStreamImageSource(is);
		OutputStreamImageSink destination = new OutputStreamImageSink(os);
		
		// when
		Thumbnailator.createThumbnail(
				new SourceSinkThumbnailTask(param, source, destination)
		);
		
		// then
		ByteArrayInputStream destIs = new ByteArrayInputStream(os.toByteArray());
		BufferedImage thumbnail = ImageIO.read(destIs);
		assertEquals(50, thumbnail.getWidth());
		assertEquals(50, thumbnail.getHeight());
		
		destIs = new ByteArrayInputStream(os.toByteArray());
		String formatName = getFormatName(destIs);
		assertEquals("JPEG", formatName);
	}
	
	private static String getFormatName(InputStream is) throws IOException
	{
		return ImageIO.getImageReaders(
				ImageIO.createImageInputStream(is)
		).next().getFormatName();
	}


	/**
	 * Returns test image data as an array of {@code byte}s.
	 * 
	 * @param format			Image format.
	 * @param width				Image width.
	 * @param height			Image height.
	 * @return					A {@code byte[]} of image data.
	 * @throws IOException		When a problem occurs while making image data.
	 */
	private static byte[] makeImageData(String format, int width, int height)
			throws IOException
	{
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(img, format, baos);
		
		return baos.toByteArray();
	}
}
