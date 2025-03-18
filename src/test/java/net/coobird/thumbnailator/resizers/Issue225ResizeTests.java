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

package net.coobird.thumbnailator.resizers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class Issue225ResizeTests {

    private static final Color[][] COLOR_BLOCKS = new Color[][] {
            new Color[] { Color.red, Color.green, Color.blue },
            new Color[] { Color.green, Color.blue, Color.red },
            new Color[] { Color.blue, Color.red, Color.green }
    };

    private static final int SOURCE_WIDTH = 120;
    private static final int SOURCE_HEIGHT = 240;

    /**
     * Creates a RGB test pattern consisting of 3 columns by 6 rows.
     */
    private static BufferedImage createTestImage() {
        int width = SOURCE_WIDTH;
        int height = SOURCE_HEIGHT;

        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.createGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);

        int blockSize = 40;
        int i = 0;
        for (int y = 0; y < height; y += blockSize) {
            g.setColor(COLOR_BLOCKS[i % 3][0]);
            g.fillRect(0, y, blockSize, blockSize);
            g.setColor(COLOR_BLOCKS[i % 3][1]);
            g.fillRect(blockSize, y, blockSize, blockSize);
            g.setColor(COLOR_BLOCKS[i % 3][2]);
            g.fillRect(blockSize * 2, y, blockSize, blockSize);
            i++;
        }

        g.dispose();
        return img;
    }

    private static int round(double v) {
        return (int) Math.round(v);
    }

    /**
     * Check each color block to verify if expected color is present.
     */
    private static void assertImage(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();

        int blockWidth = round(width / 3.0);
        int blockHeight = round(height / 6.0);
        int halfBlockWidth = round(blockWidth / 2.0);
        int halfBlockHeight = round(blockHeight / 2.0);

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 3; j++) {
                assertEquals(
                        String.format("mismatch at i=%s, j=%s", i, j),
                        COLOR_BLOCKS[i % 3][j],
                        new Color(
                                img.getRGB(
                                        (blockWidth * j) + halfBlockWidth,
                                        (blockHeight * (i % 3)) + halfBlockHeight
                                )
                        )
                );
            }
        }
    }

    @Parameterized.Parameters(name = "width={0}, height={1}")
    public static Collection<Object[]> testCases() {
        return Arrays.asList(
                new Object[] { SOURCE_WIDTH, SOURCE_HEIGHT },
                new Object[] { SOURCE_WIDTH * 2, SOURCE_HEIGHT * 2 },
                new Object[] { SOURCE_WIDTH * 3, SOURCE_HEIGHT * 3 },
                new Object[] { SOURCE_WIDTH / 2, SOURCE_HEIGHT / 2 },
                new Object[] { SOURCE_WIDTH / 3, SOURCE_HEIGHT / 3 },
                
                // Test cases for aspect ratio not preserved
                new Object[] { SOURCE_WIDTH * 2, SOURCE_HEIGHT / 2 },
                new Object[] { SOURCE_WIDTH / 2, SOURCE_HEIGHT * 2 },
                new Object[] { SOURCE_WIDTH * 3, SOURCE_HEIGHT / 3 },
                new Object[] { SOURCE_WIDTH / 3, SOURCE_HEIGHT * 3 }
        );
    }

    @Parameterized.Parameter
    public int width;

    @Parameterized.Parameter(1)
    public int height;

    private static void resizerTest(Resizer resizer, int targetWidth, int targetHeight) {
        // given
        BufferedImage sourceImage = createTestImage();
        BufferedImage thumbnail = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

        // when
        resizer.resize(sourceImage, thumbnail);

        // then
        assertEquals(
                new Dimension(targetWidth, targetHeight),
                new Dimension(thumbnail.getWidth(), thumbnail.getHeight())
        );
        assertImage(thumbnail);
    }

    @Test
    public void bilinearResizerTest() {
        resizerTest(Resizers.BILINEAR, width, height);
    }

    @Test
    public void bicubicResizerTest() {
        resizerTest(Resizers.BICUBIC, width, height);
    }

    @Test
    public void progressiveResizerTest() {
        resizerTest(Resizers.PROGRESSIVE, width, height);
    }
}
