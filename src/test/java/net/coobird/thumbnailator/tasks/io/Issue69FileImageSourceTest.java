package net.coobird.thumbnailator.tasks.io;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.builders.ThumbnailParameterBuilder;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.*;

/**
 * Tests to see whether memory conservation code is triggered when the
 * {@code thumbnailator.conserveMemoryWorkaround} system property is set. 
 * <p>
 * These tests will not necessarily be successful, as the workaround is only
 * triggered under conditions where the JVM memory is deemed to be low, which
 * will depend upon the environment in which the tests are run.
 */
public class Issue69FileImageSourceTest {
	
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	public File tempFile;
	public static int SIZE = 8000;
	
	@Before
	public void prepareSource() throws IOException {
		tempFile = tempFolder.newFile("temp.jpg");
		
		BufferedImage img = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setPaint(new GradientPaint(0, 0, Color.blue, SIZE, SIZE, Color.red));
		g.dispose();
		ImageIO.write(img, "jpg", tempFile);
		
		System.setProperty("thumbnailator.conserveMemoryWorkaround", "");
	}
	
	@Test
	public void fromFileBySizeWorkaroundDisabled() throws IOException {
		// given
		ThumbnailParameter param = new ThumbnailParameterBuilder().size(200, 200).build();
		FileImageSource source = new FileImageSource(tempFile);
		source.setThumbnailParameter(param);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertEquals(SIZE, img.getWidth());
		assertEquals(SIZE, img.getHeight());
	}
	
	@Test
	public void fromFileBySizeWorkaroundEnabled() throws IOException {
		// given
		System.setProperty("thumbnailator.conserveMemoryWorkaround", "true");
		
		ThumbnailParameter param = new ThumbnailParameterBuilder().size(200, 200).build();
		FileImageSource source = new FileImageSource(tempFile);
		source.setThumbnailParameter(param);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertTrue(img.getWidth() < SIZE);
		assertTrue(img.getWidth() >= 600);
		assertTrue(img.getHeight() < SIZE);
		assertTrue(img.getHeight() >= 600);
	}
	
	@Test
	public void fromFileByScaleWorkaroundDisabled() throws IOException {
		// given
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(0.1).build();
		FileImageSource source = new FileImageSource(tempFile);
		source.setThumbnailParameter(param);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertEquals(SIZE, img.getWidth());
		assertEquals(SIZE, img.getHeight());
	}
	
	@Test
	public void fromFileByScaleWorkaroundEnabled() throws IOException {
		// given
		System.setProperty("thumbnailator.conserveMemoryWorkaround", "true");
		
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(0.1).build();
		FileImageSource source = new FileImageSource(tempFile);
		source.setThumbnailParameter(param);
		
		// when
		BufferedImage img = source.read();
		
		// then
		assertTrue(img.getWidth() < SIZE);
		assertTrue(img.getWidth() >= 600);
		assertTrue(img.getHeight() < SIZE);
		assertTrue(img.getHeight() >= 600);
	}
}
