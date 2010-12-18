package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.Thumbnailator;
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
		
		File sourceFile = new File("test-resources/Thumbnailator/grid.png");
		
		InputStreamImageSource source = new InputStreamImageSource(new FileInputStream(sourceFile));
		BufferedImageSink destination = new BufferedImageSink();
		
		// when
		Thumbnailator.createThumbnail(new SourceSinkThumbnailTask(param, source, destination));

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
		
		File sourceFile = new File("test-resources/Thumbnailator/grid.png");
		File destinationFile = new File("test-resources/Thumbnailator/out-grid.png");
		destinationFile.deleteOnExit();
		
		InputStreamImageSource source = new InputStreamImageSource(new FileInputStream(sourceFile));
		OutputStreamImageSink destination = new OutputStreamImageSink(new FileOutputStream(destinationFile));

		// when
		Thumbnailator.createThumbnail(new SourceSinkThumbnailTask(param, source, destination));
		
		// then
		BufferedImage thumbnail = ImageIO.read(destinationFile);
		assertEquals(50, thumbnail.getWidth());
		assertEquals(50, thumbnail.getHeight());
		String formatName = ImageIO.getImageReaders(ImageIO.createImageInputStream(destinationFile)).next().getFormatName();
		assertEquals("png", formatName);
	}
	
	@Test
	public void task_ChangeOutputFormat_InputStream_OutputStream() throws IOException
	{
		// given
		ThumbnailParameter param = 
			new ThumbnailParameterBuilder().size(50, 50).format("jpg").build();
		
		File sourceFile = new File("test-resources/Thumbnailator/grid.png");
		File destinationFile = new File("test-resources/Thumbnailator/out-grid.jpg");
		destinationFile.deleteOnExit();
		
		InputStreamImageSource source = new InputStreamImageSource(new FileInputStream(sourceFile));
		OutputStreamImageSink destination = new OutputStreamImageSink(new FileOutputStream(destinationFile));
		
		// when
		Thumbnailator.createThumbnail(new SourceSinkThumbnailTask(param, source, destination));
		
		// then
		BufferedImage thumbnail = ImageIO.read(destinationFile);
		assertEquals(50, thumbnail.getWidth());
		assertEquals(50, thumbnail.getHeight());
		String formatName = ImageIO.getImageReaders(ImageIO.createImageInputStream(destinationFile)).next().getFormatName();
		assertEquals("JPEG", formatName);
	}
}
