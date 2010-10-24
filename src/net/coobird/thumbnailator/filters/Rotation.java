package net.coobird.thumbnailator.filters;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;

/**
 * A class containing rotation filters.
 * <p>
 * Aside from the three {@link Rotator}s provided as class constants, a
 * {@link Rotator} which performs a rotation by an arbituary angle can be
 * obtained through the {@link Rotation#newRotator(double)} method.
 *  
 * @author coobird
 *
 */
public class Rotation
{
	/**
	 * This class is not intended to be instantiated. 
	 */
	private Rotation() {}
	
	/**
	 * An {@link ImageFilter} which applies a rotation to an image.
	 * <p>
	 * An instance of a {@link Rotator} can be obtained through the
	 * {@link Rotation#newRotator(double)} method.
	 * 
	 * @author coobird
	 *
	 */
	public abstract static class Rotator implements ImageFilter
	{
		/**
		 * This class is not intended to be instantiated. 
		 */
		private Rotator() {}
	}
	
	/**
	 * Performs a rotation of a specified image.
	 * <p>
	 * This method will only rotate images at a multiple of 90 degrees.
	 * 
	 * @param img			Image to rotate.
	 * @param angle			The angle to rotate the image by.
	 * @return				The rotated image.
	 */
	private static BufferedImage rotate(BufferedImage img, int angle)
	{
		if (angle % 90 != 0)
		{
			throw new IllegalArgumentException("The specified angle is not in" +
					" a multiple of 90.");
		}
		
		int width = img.getWidth();
		int height = img.getHeight();
		
		BufferedImage newImage;
		
		if (angle % 180 == 90)
		{
			newImage = new BufferedImageBuilder(height, width).build();
		}
		else 
		{
			newImage = new BufferedImageBuilder(width, height).build();
		}
		
		Graphics2D g = newImage.createGraphics();
		g.rotate(Math.toRadians(angle), width / 2.0, height / 2.0);
		g.drawImage(img, 0, 0, null);
		g.dispose();
		
		return newImage;
	}
	
	/**
	 * Creates a new instance of {@code Rotator} which rotates an image at
	 * the specified angle.
	 * <p>
	 * When the {@link Rotator} returned by this method is applied, the image
	 * will be rotated clockwise by the specified angle.
	 * 
	 * @param angle			The angle at which the instance of {@code Rotator}
	 * 						is to rotate a image it acts upon.
	 * @return				An instance of {@code Rotator} which will rotate
	 * 						a given image.
	 */
	public static Rotator newRotator(final double angle)
	{
		Rotator r = new Rotator() {
			
			private double[] calculatePosition(double x, double y, double angle)
			{
				angle = Math.toRadians(angle);
				
				double nx = (Math.cos(angle) * x) - (Math.sin(angle) * y);
				double ny = (Math.sin(angle) * x) + (Math.cos(angle) * y);

				return new double[] {nx, ny};
			}
			
			public BufferedImage apply(BufferedImage img)
			{
				int width = img.getWidth();
				int height = img.getHeight();
				
				BufferedImage newImage;
				
				double[][] newPositions = new double[4][];
				newPositions[0] = calculatePosition(0, 0, angle);
				newPositions[1] = calculatePosition(width, 0, angle);
				newPositions[2] = calculatePosition(0, height, angle);
				newPositions[3] = calculatePosition(width, height, angle);
				
				double minX = Math.min(
						Math.min(newPositions[0][0], newPositions[1][0]), 
						Math.min(newPositions[2][0], newPositions[3][0])
				);
				double maxX = Math.max(
						Math.max(newPositions[0][0], newPositions[1][0]),
						Math.max(newPositions[2][0], newPositions[3][0])
				);
				double minY = Math.min(
						Math.min(newPositions[0][1], newPositions[1][1]),
						Math.min(newPositions[2][1], newPositions[3][1])
				);
				double maxY = Math.max(
						Math.max(newPositions[0][1], newPositions[1][1]), 
						Math.max(newPositions[2][1], newPositions[3][1])
				);
				
				int newWidth = (int)Math.round(maxX - minX);
				int newHeight = (int)Math.round(maxY - minY);
				newImage = new BufferedImageBuilder(newWidth, newHeight).build();
				
				Graphics2D g = newImage.createGraphics();
				
				/*
				 * TODO consider RenderingHints to use.
				 * The following are hints which have been chosen to give
				 * decent image quality. In the future, there may be a need
				 * to have a way to change these settings.
				 */
				g.setRenderingHint(
						RenderingHints.KEY_INTERPOLATION,
						RenderingHints.VALUE_INTERPOLATION_BILINEAR
				);
				g.setRenderingHint(
						RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON
				);
				
				double w = newWidth / 2.0;
				double h = newHeight / 2.0;
				g.rotate(Math.toRadians(angle), w, h);
				int centerX = (int)Math.round((newWidth - width) / 2.0);
				int centerY = (int)Math.round((newHeight - height) / 2.0);
				
				g.drawImage(img, centerX, centerY, null);
				g.dispose();
				
				return newImage;
			}
		};
		
		return r;
	}

	/**
	 * A {@code Rotator} which will rotate a specified image to the left 90
	 * degrees.
	 */
	public static final Rotator LEFT_90_DEGREES = new Rotator() {
		public BufferedImage apply(BufferedImage img)
		{
			return rotate(img, -90);
		}
	};
	
	/**
	 * A {@code Rotator} which will rotate a specified image to the right 90
	 * degrees.
	 */
	public static final Rotator RIGHT_90_DEGREES = new Rotator() {
		public BufferedImage apply(BufferedImage img)
		{
			return rotate(img, 90);
		}
	};
	
	/**
	 * A {@code Rotator} which will rotate a specified image to the 180 degrees.
	 */
	public static final Rotator ROTATE_180_DEGREES = new Rotator() {
		public BufferedImage apply(BufferedImage img)
		{
			return rotate(img, 180);
		}
	};
}
