package net.coobird.thumbnailator.filters;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.junit.Test;

public class FlipTest {
	
	private static int SIZE = 160;
	
	@Test
	public void flipHorizontal() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("test-resources/Exif/original.png"));
		
		// when
		BufferedImage result = Flip.HORIZONTAL.apply(img);
		
		// then
		assertEquals(Color.black.getRGB(), result.getRGB(0, 0));
		assertEquals(Color.black.getRGB(), result.getRGB(0, SIZE / 2 - 1));
		assertEquals(Color.white.getRGB(), result.getRGB(0, SIZE - 1));
		assertEquals(Color.black.getRGB(), result.getRGB(SIZE - 1, 0));
		assertEquals(Color.black.getRGB(), result.getRGB(SIZE - 1, SIZE / 2 - 1));
		assertEquals(Color.black.getRGB(), result.getRGB(SIZE - 1, SIZE - 1));
	}
	
	@Test
	public void flipVertical() throws Exception {
		// given
		BufferedImage img = ImageIO.read(new File("test-resources/Exif/original.png"));
		
		// when
		BufferedImage result = Flip.VERTICAL.apply(img);
		
		// then
		assertEquals(Color.black.getRGB(), result.getRGB(0, 0));
		assertEquals(Color.black.getRGB(), result.getRGB(0, SIZE / 2 - 1));
		assertEquals(Color.black.getRGB(), result.getRGB(0, SIZE - 1));
		assertEquals(Color.white.getRGB(), result.getRGB(SIZE - 1, 0));
		assertEquals(Color.black.getRGB(), result.getRGB(SIZE - 1, SIZE / 2 - 1));
		assertEquals(Color.black.getRGB(), result.getRGB(SIZE - 1, SIZE - 1));
	}
}
