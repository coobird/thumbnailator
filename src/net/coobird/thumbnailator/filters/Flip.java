package net.coobird.thumbnailator.filters;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;

/**
 * A class containing flip transformation filters.
 * 
 * @author coobird
 *
 */
public class Flip {
	
	/**
	 * An image filter which performs a horizontal flip of the image. 
	 */
	public static final ImageFilter HORIZONTAL = new ImageFilter() {
		public BufferedImage apply(BufferedImage img) {
			int width = img.getWidth();
			int height = img.getHeight();
			
			BufferedImage newImage = 
					new BufferedImageBuilder(width, height).build();
			
			Graphics g = newImage.getGraphics();
			g.drawImage(img, width, 0, 0, height, 0, 0, width, height, null); 
			g.dispose();
			
			return newImage;
		};
	};
	
	/**
	 * An image filter which performs a vertical flip of the image. 
	 */
	public static final ImageFilter VERTICAL = new ImageFilter() {
		public BufferedImage apply(BufferedImage img) {
			int width = img.getWidth();
			int height = img.getHeight();
			
			BufferedImage newImage = 
					new BufferedImageBuilder(width, height).build();
			
			Graphics g = newImage.getGraphics();
			g.drawImage(img, 0, height, width, 0, 0, 0, width, height, null); 
			g.dispose();
			
			return newImage;
		};
	};
}
