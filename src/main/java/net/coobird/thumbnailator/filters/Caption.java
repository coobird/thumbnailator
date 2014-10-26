package net.coobird.thumbnailator.filters;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.util.BufferedImages;

/**
 * An {@link ImageFilter} which will overlay a text caption to an image.
 * 
 * @author coobird
 *
 */
public class Caption implements ImageFilter
{
	/**
	 * The text of the caption.
	 */
	private final String caption;
	
	/**
	 * The font of text to add.
	 */
	private final Font font;
	
	/**
	 * The color of the text to add.
	 */
	private final Color c;
	
	/**
	 * The opacity level of the text to add.
	 * <p>
	 * The value should be between {@code 0.0f} to {@code 1.0f}, where
	 * {@code 0.0f} is completely transparent, and {@code 1.0f} is completely
	 * opaque.
	 */
	private final float alpha;
	
	/**
	 * The position at which the text should be drawn.
	 */
	private final Position position;
	
	/** 
	 * The insets for the text to draw.
	 */
	private final int insets;
	
	/**
	 * Instantiates a filter which adds a text caption to an image.
	 * 
	 * @param caption	The text of the caption.
	 * @param font		The font of the caption.
	 * @param c			The color of the caption.
	 * @param alpha		The opacity level of caption.
	 * 					<p>
	 * 					The value should be between {@code 0.0f} and
	 * 					{@code 1.0f}, where {@code 0.0f} is completely
	 * 					transparent, and {@code 1.0f} is completely opaque.
	 * @param position	The position of the caption.
	 * @param insets	The inset size around the caption.
	 */
	public Caption(String caption, Font font, Color c, float alpha,
			Position position, int insets)
	{
		this.caption = caption;
		this.font = font;
		this.c = c;
		this.alpha = alpha;
		this.position = position;
		this.insets = insets;
	}

	/**
	 * Instantiates a filter which adds a text caption to an image.
	 * <p>
	 * The opacity of the caption will be 100% opaque.
	 * 
	 * @param caption	The text of the caption.
	 * @param font		The font of the caption.
	 * @param c			The color of the caption.
	 * @param position	The position of the caption.
	 * @param insets	The inset size around the caption.
	 */
	public Caption(String caption, Font font, Color c, Position position,
			int insets)
	{
		this.caption = caption;
		this.font = font;
		this.c = c;
		this.alpha = 1.0f;
		this.position = position;
		this.insets = insets;
	}

	public BufferedImage apply(BufferedImage img)
	{
		BufferedImage newImage = BufferedImages.copy(img);
		
		Graphics2D g = newImage.createGraphics();
		g.setFont(font);
		g.setColor(c);
		g.setComposite(
				AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha)
		);
		
		int imageWidth = img.getWidth();
		int imageHeight = img.getHeight();
		
		int captionWidth = g.getFontMetrics().stringWidth(caption);
		int captionHeight = g.getFontMetrics().getHeight() / 2;
		
		Point p = position.calculate(
				imageWidth,	imageHeight, captionWidth, 0,
				insets, insets, insets, insets
		);

		double yRatio = p.y / (double)img.getHeight();
		int yOffset = (int)((1.0 - yRatio) * captionHeight);
		
		g.drawString(caption, p.x, p.y + yOffset);
		
		g.dispose();
		
		return newImage;
	}
}
