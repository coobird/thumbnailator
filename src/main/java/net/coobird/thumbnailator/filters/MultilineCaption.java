/*
 * Thumbnailator - a thumbnail generation library
 *
 * Copyright (c) 2008-2025 Chris Kroells
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.coobird.thumbnailator.filters;

import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.TextAlignment;
import net.coobird.thumbnailator.util.BufferedImages;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * An {@link ImageFilter} which overlays a multiline text caption on an image.
 * <p>
 * This filter supports rendering text with multiple lines, where lines are
 * separated by newline characters. All common newline formats are supported:
 * <ul>
 *   <li>{@code \n} - Unix/Linux/macOS</li>
 *   <li>{@code \r\n} - Windows</li>
 *   <li>{@code \r} - Classic Mac OS</li>
 * </ul>
 * <p>
 * Text alignment can be controlled using the {@link TextAlignment} enum,
 * allowing left, center, or right alignment of lines within the caption block.
 *
 * @author artyomsv
 * @see Caption
 * @see TextAlignment
 * @since 0.4.22
 */
public class MultilineCaption implements ImageFilter {

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
    private final Color color;

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
     * The horizontal alignment of text lines.
     */
    private final TextAlignment alignment;

    /**
     * The insets for the text to draw.
     */
    private final int insets;

    /**
     * Instantiates a filter which adds a multiline text caption to an image.
     *
     * @param caption   The text of the caption. May contain newline
     *                  characters ({@code \n}, {@code \r\n}, or {@code \r})
     *                  to create multiple lines.
     * @param font      The font of the caption.
     * @param color     The color of the caption.
     * @param alpha     The opacity level of caption.
     *                  <p>
     *                  The value should be between {@code 0.0f} and
     *                  {@code 1.0f}, where {@code 0.0f} is completely
     *                  transparent, and {@code 1.0f} is completely opaque.
     * @param position  The position of the caption on the image.
     * @param alignment The horizontal alignment of text lines within
     *                  the caption block.
     * @param insets    The inset size around the caption. Cannot be negative.
     * @throws NullPointerException     If any of {@code caption}, {@code font},
     *                                  {@code color}, {@code position}, or
     *                                  {@code alignment} is {@code null}.
     * @throws IllegalArgumentException If {@code alpha} is not between
     *                                  {@code 0.0f} and {@code 1.0f}, or if
     *                                  {@code insets} is negative.
     */
    public MultilineCaption(String caption, Font font, Color color, float alpha,
                            Position position, TextAlignment alignment, int insets) {
        if (caption == null) {
            throw new NullPointerException("Caption is null.");
        }
        if (font == null) {
            throw new NullPointerException("Font is null.");
        }
        if (color == null) {
            throw new NullPointerException("Color is null.");
        }
        if (alpha > 1.0f || alpha < 0.0f) {
            throw new IllegalArgumentException("Opacity is out of range of " +
                    "between 0.0f and 1.0f.");
        }
        if (position == null) {
            throw new NullPointerException("Position is null.");
        }
        if (alignment == null) {
            throw new NullPointerException("Alignment is null.");
        }
        if (insets < 0) {
            throw new IllegalArgumentException("Insets cannot be negative.");
        }

        this.caption = caption;
        this.font = font;
        this.color = color;
        this.alpha = alpha;
        this.position = position;
        this.alignment = alignment;
        this.insets = insets;
    }

    /**
     * Instantiates a filter which adds a multiline text caption to an image.
     * <p>
     * The opacity of the caption will be 100% opaque.
     *
     * @param caption   The text of the caption. May contain newline
     *                  characters ({@code \n}, {@code \r\n}, or {@code \r})
     *                  to create multiple lines.
     * @param font      The font of the caption.
     * @param color     The color of the caption.
     * @param position  The position of the caption on the image.
     * @param alignment The horizontal alignment of text lines within
     *                  the caption block.
     * @param insets    The inset size around the caption. Cannot be negative.
     * @throws NullPointerException     If any of {@code caption}, {@code font},
     *                                  {@code color}, {@code position}, or
     *                                  {@code alignment} is {@code null}.
     * @throws IllegalArgumentException If {@code insets} is negative.
     */
    public MultilineCaption(String caption, Font font, Color color,
                            Position position, TextAlignment alignment, int insets) {
        this(caption, font, color, 1.0f, position, alignment, insets);
    }

    /**
     * Applies the multiline caption filter to the given image.
     *
     * @param img The image to apply the caption to.
     * @return        A new image with the caption applied.
     */
    public BufferedImage apply(BufferedImage img) {
        BufferedImage newImage = BufferedImages.copy(img);

        Graphics2D g = newImage.createGraphics();

        // Enable anti-aliasing for smooth text
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g.setFont(font);
        g.setColor(color);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        FontMetrics metrics = g.getFontMetrics();

        // Normalize newlines to \n for cross-platform support
        String normalizedCaption = normalizeNewlines(caption);

        // Split into lines
        String[] lines = normalizedCaption.split("\n", -1);

        // Calculate dimensions for each line and find the maximum width
        int[] lineWidths = new int[lines.length];
        int maxWidth = 0;
        for (int i = 0; i < lines.length; i++) {
            lineWidths[i] = metrics.stringWidth(lines[i]);
            if (lineWidths[i] > maxWidth) {
                maxWidth = lineWidths[i];
            }
        }

        int lineHeight = metrics.getHeight();
        int totalHeight = lineHeight * lines.length;

        int imageWidth = img.getWidth();
        int imageHeight = img.getHeight();

        // Calculate position for the entire caption block
        Point p = position.calculate(
                imageWidth, imageHeight, maxWidth, totalHeight,
                insets, insets, insets, insets
        );

        // Draw each line with proper alignment
        int y = p.y + metrics.getAscent();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            int lineWidth = lineWidths[i];

            // Calculate x position based on alignment
            int x = calculateXPosition(p.x, maxWidth, lineWidth, alignment);

            g.drawString(line, x, y);

            y += lineHeight;
        }

        g.dispose();

        return newImage;
    }

    /**
     * Normalizes all newline variants to Unix-style {@code \n}.
     * <p>
     * Handles:
     * <ul>
     *   <li>{@code \r\n} (Windows) → {@code \n}</li>
     *   <li>{@code \r} (Classic Mac) → {@code \n}</li>
     *   <li>{@code \n} (Unix) → unchanged</li>
     * </ul>
     *
     * @param text The text to normalize.
     * @return        The text with normalized newlines.
     */
    private String normalizeNewlines(String text) {
        // Replace \r\n first (Windows), then \r (Classic Mac)
        // \n (Unix) is already the target format
        return text.replace("\r\n", "\n").replace("\r", "\n");
    }

    /**
     * Calculates the x-coordinate for a line based on alignment.
     *
     * @param baseX     The base x-coordinate for left alignment.
     * @param maxWidth  The maximum width of all lines.
     * @param lineWidth The width of the current line.
     * @param alignment The text alignment.
     * @return                The calculated x-coordinate.
     */
    private int calculateXPosition(int baseX, int maxWidth, int lineWidth,
                                   TextAlignment alignment) {
        switch (alignment) {
            case LEFT:
                return baseX;
            case CENTER:
                return baseX + (maxWidth - lineWidth) / 2;
            case RIGHT:
                return baseX + (maxWidth - lineWidth);
            default:
                return baseX;
        }
    }
}
