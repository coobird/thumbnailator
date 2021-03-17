package net.coobird.thumbnailator.filters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * An {@link ImageFilter} which will enclose an image into a specified
 * border.
 * <p>
 * The fill color used for the enclosing image can be specified, along with
 * whether or not to crop an image if it is larger than the enclosing image.
 * 
 * @author marcoreni
 * @since 0.4.9
 *
 */
public class Border implements ImageFilter
{
	/**
	 * The size of the border left.
	 */
	private final int borderLeft;
	
	/**
	 * The size of the border right.
	 */
	private final int borderRight;
	
	/**
	 * The size of the border top.
	 */
	private final int borderTop;
	
	/**
	 * The size of the border bottom.
	 */
	private final int borderBottom;
	
	/**
	 * The fill color for the background.
	 */
	private final Color fillColor;
	
	/**
	 * Instantiates a {@code Border} filter.
	 * <p>
	 * No fill color will be applied to the filtered image. If the image to
	 * filter does not have a transparency channel, the image will be filled
	 * black.
	 * <p>
	 * Crops the enclosed image if the enclosing image is smaller.
	 * 
	 * @param size 			The size of the border.
	 */
	public Border(int size)
	{
		this(size, size, size, size, null);
	}
	
	/**
	 * Instantiates a {@code Border} filter.
	 * <p>
	 * No fill color will be applied to the filtered image. If the image to
	 * filter does not have a transparency channel, the image will be filled
	 * black.
	 * 
	 * @param horizontalSize		Size of the left and right border.
	 * @param verticalSize 			Size of the top and bottom border.
	 */
	public Border(int horizontalSize, int verticalSize)
	{
		this(verticalSize, horizontalSize, verticalSize, horizontalSize, null);
	}
	
	/**
	 * Instantiates a {@code Border} filter.
	 * <p>
	 * 
	 * @param size			The size of the border.
	 * @param fillColor		The color to fill portions of the image which is
	 * 						not covered by the enclosed image. Portions of the
	 * 						image which is transparent will be filled with
	 * 						the specified color as well.
	 */
	public Border(int size, Color fillColor)
	{
		this(size, size, size, size, fillColor);
	}
	
	/**
	 * Instantiates a {@code Border} filter.
	 * 
	 * @param borderTop     The size of the border top.
	 * @param borderRight   The size of the border right.
	 * @param borderBottom  The size of the border bottom.
	 * @param borderLeft    The size of the border left.
	 * @param fillColor		The color to fill portions of the image which is
	 * 						not covered by the enclosed image. Portions of the
	 * 						image which is transparent will be filled with
	 * 						the specified color as well.
	 */
	public Border(int borderTop, int borderRight, int borderBottom, int borderLeft, Color fillColor)
	{
		super();
		this.borderTop = borderTop;
		this.borderRight = borderRight;
		this.borderBottom = borderBottom;
		this.borderLeft = borderLeft;

		this.fillColor = fillColor;
	}

	public BufferedImage apply(BufferedImage img)
	{
		int imageFinalWidth = img.getWidth() + borderLeft + borderRight;
		int imageFinalHeight = img.getHeight() + borderTop + borderBottom;
		int containedImageWidth = img.getWidth();
		int containedImageHeight = img.getHeight();
		
		BufferedImage finalImage =
			new BufferedImage(imageFinalWidth, imageFinalHeight, img.getType());
		
		Graphics g = finalImage.getGraphics();
		
		if (fillColor == null && !img.getColorModel().hasAlpha())
		{
			/*
			 * Fulfills the specification to use a black fill color for images
			 * w/o alpha, if the fill color isn't specified.
			 */
			g.setColor(Color.black);
			g.fillRect(0, 0, imageFinalWidth, imageFinalHeight);
		}
		else if (fillColor != null)
		{
			g.setColor(fillColor);
			g.fillRect(0, 0, imageFinalWidth, imageFinalHeight);
		}
		
		g.drawImage(img, borderLeft, borderTop, containedImageWidth, containedImageHeight, null);
		g.dispose();
		
		return finalImage;
	}
}
