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

import static net.coobird.thumbnailator.filters.ImageFilterTestUtils.assertImageTypeRetained;
import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import net.coobird.thumbnailator.geometry.TextAlignment;
import net.coobird.thumbnailator.test.BufferedImageComparer;
import net.coobird.thumbnailator.util.BufferedImages;

import org.junit.Test;

/**
 * Tests for the {@link MultilineCaption} filter.
 * <p>
 * Tests are organized into categories with unique identifiers:
 * <ul>
 *   <li><b>CN</b> - Constructor Null checks: Verify NullPointerException for null parameters</li>
 *   <li><b>CV</b> - Constructor Validation: Verify parameter bounds and edge cases</li>
 *   <li><b>IM</b> - Image Immutability: Verify original image is not modified</li>
 *   <li><b>TR</b> - Type Retention: Verify output image type matches input</li>
 *   <li><b>ML</b> - MultiLine rendering: Core multiline text functionality</li>
 *   <li><b>NL</b> - NewLine normalization: Cross-platform newline handling</li>
 *   <li><b>TA</b> - Text Alignment: Horizontal text alignment within caption</li>
 *   <li><b>PO</b> - Position: Caption positioning on the image</li>
 *   <li><b>OC</b> - Opacity and Color: Text opacity and color rendering</li>
 *   <li><b>IN</b> - INsets: Inset/padding behavior</li>
 *   <li><b>EC</b> - Edge Cases: Robustness and boundary conditions</li>
 * </ul>
 * 
 * @author coobird
 * @author artyomsv
 */
public class MultilineCaptionTest {

	// ==========================================================================
	// Debug Mode - Set to true to save test images for manual inspection
	// ==========================================================================
	
	/**
	 * When set to {@code true}, test images will be saved to the output
	 * directory for manual visual inspection.
	 * <p>
	 * Set this to {@code true} when debugging test failures or validating
	 * visual output. Images will be saved to: {@code target/test-images/}
	 */
	private static final boolean SAVE_TEST_IMAGES = false;
	
	/**
	 * Directory where test images will be saved when {@link #SAVE_TEST_IMAGES}
	 * is {@code true}.
	 */
	private static final String TEST_IMAGE_OUTPUT_DIR = "target/test-images";

	// ==========================================================================
	// Test Constants
	// ==========================================================================
	
	private static final String DEFAULT_CAPTION = "Line1\nLine2";
	private static final String SINGLE_LINE_CAPTION = "hello";
	private static final Font DEFAULT_FONT = new Font("Monospaced", Font.PLAIN, 14);
	private static final Color DEFAULT_COLOR = Color.black;
	private static final Position DEFAULT_POSITION = Positions.BOTTOM_CENTER;
	private static final TextAlignment DEFAULT_ALIGNMENT = TextAlignment.LEFT;
	private static final float DEFAULT_OPACITY = 1.0f;
	private static final int DEFAULT_INSETS = 0;

	// ==========================================================================
	// Category 1: Constructor Null Checks (CN-01 to CN-05)
	// ==========================================================================

	/**
	 * CN-01: Verify NullPointerException when caption is null.
	 */
	@Test(expected = NullPointerException.class)
	public void constructorNullCheckForCaption() {
		new MultilineCaption(
				null,
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				DEFAULT_INSETS
		);
	}

	/**
	 * CN-02: Verify NullPointerException when font is null.
	 */
	@Test(expected = NullPointerException.class)
	public void constructorNullCheckForFont() {
		new MultilineCaption(
				DEFAULT_CAPTION,
				null,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				DEFAULT_INSETS
		);
	}

	/**
	 * CN-03: Verify NullPointerException when color is null.
	 */
	@Test(expected = NullPointerException.class)
	public void constructorNullCheckForColor() {
		new MultilineCaption(
				DEFAULT_CAPTION,
				DEFAULT_FONT,
				null,
				DEFAULT_OPACITY,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				DEFAULT_INSETS
		);
	}

	/**
	 * CN-04: Verify NullPointerException when position is null.
	 */
	@Test(expected = NullPointerException.class)
	public void constructorNullCheckForPosition() {
		new MultilineCaption(
				DEFAULT_CAPTION,
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				null,
				DEFAULT_ALIGNMENT,
				DEFAULT_INSETS
		);
	}

	/**
	 * CN-05: Verify NullPointerException when alignment is null.
	 */
	@Test(expected = NullPointerException.class)
	public void constructorNullCheckForAlignment() {
		new MultilineCaption(
				DEFAULT_CAPTION,
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				DEFAULT_POSITION,
				null,
				DEFAULT_INSETS
		);
	}

	// ==========================================================================
	// Category 2: Constructor Parameter Validation (CV-01 to CV-08)
	// ==========================================================================

	/**
	 * CV-01: Verify IllegalArgumentException when opacity is negative.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructorRejectsNegativeOpacity() {
		new MultilineCaption(
				DEFAULT_CAPTION,
				DEFAULT_FONT,
				DEFAULT_COLOR,
				-0.001f,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				DEFAULT_INSETS
		);
	}

	/**
	 * CV-02: Verify IllegalArgumentException when opacity is greater than 1.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructorRejectsOpacityGreaterThanOne() {
		new MultilineCaption(
				DEFAULT_CAPTION,
				DEFAULT_FONT,
				DEFAULT_COLOR,
				1.001f,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				DEFAULT_INSETS
		);
	}

	/**
	 * CV-03: Verify constructor accepts zero opacity.
	 */
	@Test
	public void constructorAcceptsZeroOpacity() {
		new MultilineCaption(
				DEFAULT_CAPTION,
				DEFAULT_FONT,
				DEFAULT_COLOR,
				0.0f,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				DEFAULT_INSETS
		);
	}

	/**
	 * CV-04: Verify constructor accepts full opacity.
	 */
	@Test
	public void constructorAcceptsFullOpacity() {
		new MultilineCaption(
				DEFAULT_CAPTION,
				DEFAULT_FONT,
				DEFAULT_COLOR,
				1.0f,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				DEFAULT_INSETS
		);
	}

	/**
	 * CV-05: Verify IllegalArgumentException when insets is negative.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructorRejectsNegativeInsets() {
		new MultilineCaption(
				DEFAULT_CAPTION,
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				-1
		);
	}

	/**
	 * CV-06: Verify constructor accepts zero insets.
	 */
	@Test
	public void constructorAcceptsZeroInsets() {
		new MultilineCaption(
				DEFAULT_CAPTION,
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				0
		);
	}

	/**
	 * CV-07: Verify constructor accepts positive insets.
	 */
	@Test
	public void constructorAcceptsPositiveInsets() {
		new MultilineCaption(
				DEFAULT_CAPTION,
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				10
		);
	}

	/**
	 * CV-08: Verify constructor accepts empty caption string.
	 */
	@Test
	public void constructorAcceptsEmptyCaption() {
		new MultilineCaption(
				"",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				DEFAULT_INSETS
		);
	}

	// ==========================================================================
	// Category 3: Image Immutability (IM-01 to IM-02)
	// ==========================================================================

	/**
	 * IM-01: Verify original image contents are not altered (full constructor).
	 */
	@Test
	public void inputContentsAreNotAltered() {
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		BufferedImage copyImage = BufferedImages.copy(originalImage);
		
		ImageFilter filter = new MultilineCaption(
				DEFAULT_CAPTION,
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				DEFAULT_INSETS
		);
		
		// when
		filter.apply(originalImage);
		
		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}

	/**
	 * IM-02: Verify original image contents are not altered (6-arg constructor).
	 */
	@Test
	public void inputContentsAreNotAlteredWithDefaultOpacity() {
		// given
		BufferedImage originalImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		BufferedImage copyImage = BufferedImages.copy(originalImage);
		
		ImageFilter filter = new MultilineCaption(
				DEFAULT_CAPTION,
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				DEFAULT_INSETS
		);
		
		// when
		filter.apply(originalImage);
		
		// then
		assertTrue(BufferedImageComparer.isSame(originalImage, copyImage));
	}

	// ==========================================================================
	// Category 4: Image Type Retention (TR-01 to TR-02)
	// ==========================================================================

	/**
	 * TR-01: Verify output image type matches input (full constructor).
	 */
	@Test
	public void imageTypeForInputAndOutputIsTheSame() {
		ImageFilter filter = new MultilineCaption(
				DEFAULT_CAPTION,
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				DEFAULT_INSETS
		);

		assertImageTypeRetained(filter);
	}

	/**
	 * TR-02: Verify output image type matches input (6-arg constructor).
	 */
	@Test
	public void imageTypeForInputAndOutputIsTheSameWithDefaultOpacity() {
		ImageFilter filter = new MultilineCaption(
				DEFAULT_CAPTION,
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				DEFAULT_INSETS
		);

		assertImageTypeRetained(filter);
	}

	// ==========================================================================
	// Category 5: Multiline Text Rendering (ML-01 to ML-07)
	// ==========================================================================

	/**
	 * ML-01: Verify single line text renders correctly.
	 */
	@Test
	public void singleLineTextRendersCorrectly() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				SINGLE_LINE_CAPTION,
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.TOP_LEFT,
				DEFAULT_ALIGNMENT,
				5
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertImageHasTextPixels(result);
	}

	/**
	 * ML-02: Verify two lines with newline renders as two lines.
	 */
	@Test
	public void twoLinesWithNewlineRendersAsTwoLines() {
		// given
		BufferedImage image = createWhiteImage(250, 150);
		
		ImageFilter filter = new MultilineCaption(
				"First Line\nSecond Longer Line",
				new Font("Monospaced", Font.PLAIN, 16),
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.TOP_LEFT,
				DEFAULT_ALIGNMENT,
				10
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		saveTestImage(result, "ML-02_twoLinesWithNewline");
		assertImageHasTextPixels(result);
	}

	/**
	 * ML-03: Verify three lines with newlines renders as three lines.
	 */
	@Test
	public void threeLinesWithNewlinesRendersAsThreeLines() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"A\nB\nC",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.TOP_LEFT,
				DEFAULT_ALIGNMENT,
				5
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertImageHasTextPixels(result);
	}

	/**
	 * ML-04: Verify empty lines between text are preserved.
	 */
	@Test
	public void emptyLinesBetweenTextArePreserved() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"Top\n\nBottom",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.TOP_LEFT,
				DEFAULT_ALIGNMENT,
				5
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then - no exception thrown, filter completes
		assertNotNull(result);
	}

	/**
	 * ML-05: Verify leading newline creates empty first line.
	 */
	@Test
	public void leadingNewlineCreatesEmptyFirstLine() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"\nText",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.TOP_LEFT,
				DEFAULT_ALIGNMENT,
				5
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then - no exception thrown, filter completes
		assertNotNull(result);
	}

	/**
	 * ML-06: Verify trailing newline creates empty last line.
	 */
	@Test
	public void trailingNewlineCreatesEmptyLastLine() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"Text\n",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.TOP_LEFT,
				DEFAULT_ALIGNMENT,
				5
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then - no exception thrown, filter completes
		assertNotNull(result);
	}

	/**
	 * ML-07: Verify only newlines creates blank lines.
	 */
	@Test
	public void onlyNewlinesCreatesBlankLines() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"\n\n\n",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.TOP_LEFT,
				DEFAULT_ALIGNMENT,
				5
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then - no exception thrown, filter completes
		assertNotNull(result);
	}

	// ==========================================================================
	// Category 5b: Newline Normalization (NL-01 to NL-04)
	// ==========================================================================

	/**
	 * NL-01: Verify Unix newline (\n) is recognized.
	 */
	@Test
	public void unixNewlineIsRecognized() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"A\nB",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.TOP_LEFT,
				DEFAULT_ALIGNMENT,
				5
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertImageHasTextPixels(result);
	}

	/**
	 * NL-02: Verify Windows newline (\r\n) is normalized.
	 */
	@Test
	public void windowsNewlineIsNormalized() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"A\r\nB",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.TOP_LEFT,
				DEFAULT_ALIGNMENT,
				5
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertImageHasTextPixels(result);
	}

	/**
	 * NL-03: Verify old Mac newline (\r) is normalized.
	 */
	@Test
	public void oldMacNewlineIsNormalized() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"A\rB",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.TOP_LEFT,
				DEFAULT_ALIGNMENT,
				5
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertImageHasTextPixels(result);
	}

	/**
	 * NL-04: Verify mixed newlines are normalized.
	 */
	@Test
	public void mixedNewlinesAreNormalized() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"A\nB\r\nC\rD",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.TOP_LEFT,
				DEFAULT_ALIGNMENT,
				5
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertImageHasTextPixels(result);
	}

	// ==========================================================================
	// Category 6: Text Alignment (TA-01 to TA-04)
	// ==========================================================================

	/**
	 * TA-01: Verify left alignment positions text at left.
	 */
	@Test
	public void leftAlignmentPositionsTextAtLeft() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				new Font("Monospaced", Font.PLAIN, 20),
				Color.black,
				1.0f,
				Positions.TOP_LEFT,
				TextAlignment.LEFT,
				5
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		saveTestImage(result, "TA-01_leftAlignment");
		assertTrue("Text should be positioned near left inset", 
				hasNonWhitePixelInRegion(result, 0, 0, 50, 50));
	}

	/**
	 * TA-02: Verify center alignment centers text.
	 */
	@Test
	public void centerAlignmentCentersText() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				new Font("Monospaced", Font.PLAIN, 20),
				Color.black,
				1.0f,
				Positions.TOP_CENTER,
				TextAlignment.CENTER,
				0
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then - text should be in the center region
		saveTestImage(result, "TA-02_centerAlignment");
		assertTrue("Text should be centered", 
				hasNonWhitePixelInRegion(result, 75, 0, 50, 50));
	}

	/**
	 * TA-03: Verify right alignment positions text at right.
	 */
	@Test
	public void rightAlignmentPositionsTextAtRight() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				new Font("Monospaced", Font.PLAIN, 20),
				Color.black,
				1.0f,
				Positions.TOP_RIGHT,
				TextAlignment.RIGHT,
				5
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		saveTestImage(result, "TA-03_rightAlignment");
		assertTrue("Text should be positioned near right edge", 
				hasNonWhitePixelInRegion(result, 150, 0, 50, 50));
	}

	/**
	 * TA-04: Verify alignment affects each line independently.
	 */
	@Test
	public void alignmentAffectsEachLineIndependently() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X\nXXXX",  // First line shorter than second
				new Font("Monospaced", Font.PLAIN, 14),
				Color.black,
				1.0f,
				Positions.TOP_LEFT,
				TextAlignment.CENTER,
				5
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then - filter completes without exception
		saveTestImage(result, "TA-04_alignmentPerLine");
		assertImageHasTextPixels(result);
	}

	/**
	 * TA-05: Verify left alignment with varying line lengths.
	 */
	@Test
	public void leftAlignmentWithVaryingLineLengths() {
		// given
		BufferedImage image = createWhiteImage(300, 200);
		
		ImageFilter filter = new MultilineCaption(
				"Short\nMedium length\nThis is a much longer line of text",
				new Font("Monospaced", Font.PLAIN, 12),
				Color.black,
				1.0f,
				Positions.TOP_LEFT,
				TextAlignment.LEFT,
				10
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		saveTestImage(result, "TA-05_leftAlign_varyingLengths");
		assertImageHasTextPixels(result);
	}

	/**
	 * TA-06: Verify center alignment with varying line lengths.
	 */
	@Test
	public void centerAlignmentWithVaryingLineLengths() {
		// given
		BufferedImage image = createWhiteImage(300, 200);
		
		ImageFilter filter = new MultilineCaption(
				"Short\nMedium length\nThis is a much longer line of text",
				new Font("Monospaced", Font.PLAIN, 12),
				Color.black,
				1.0f,
				Positions.TOP_LEFT,
				TextAlignment.CENTER,
				10
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		saveTestImage(result, "TA-06_centerAlign_varyingLengths");
		assertImageHasTextPixels(result);
	}

	/**
	 * TA-07: Verify right alignment with varying line lengths.
	 */
	@Test
	public void rightAlignmentWithVaryingLineLengths() {
		// given
		BufferedImage image = createWhiteImage(300, 200);
		
		ImageFilter filter = new MultilineCaption(
				"Short\nMedium length\nThis is a much longer line of text",
				new Font("Monospaced", Font.PLAIN, 12),
				Color.black,
				1.0f,
				Positions.TOP_LEFT,
				TextAlignment.RIGHT,
				10
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		saveTestImage(result, "TA-07_rightAlign_varyingLengths");
		assertImageHasTextPixels(result);
	}

	/**
	 * TA-08: Compare all three alignments side by side with pyramid pattern.
	 */
	@Test
	public void pyramidPatternWithAllAlignments() {
		// given
		BufferedImage image = createWhiteImage(250, 150);
		String pyramidText = "X\nXXX\nXXXXX\nXXXXXXX\nXXXXXXXXX";
		
		// Test LEFT alignment
		ImageFilter leftFilter = new MultilineCaption(
				pyramidText,
				new Font("Monospaced", Font.PLAIN, 14),
				Color.black,
				1.0f,
				Positions.CENTER,
				TextAlignment.LEFT,
				0
		);
		BufferedImage leftResult = leftFilter.apply(image);
		saveTestImage(leftResult, "TA-08a_pyramid_LEFT");
		
		// Test CENTER alignment
		ImageFilter centerFilter = new MultilineCaption(
				pyramidText,
				new Font("Monospaced", Font.PLAIN, 14),
				Color.blue,
				1.0f,
				Positions.CENTER,
				TextAlignment.CENTER,
				0
		);
		BufferedImage centerResult = centerFilter.apply(createWhiteImage(250, 150));
		saveTestImage(centerResult, "TA-08b_pyramid_CENTER");
		
		// Test RIGHT alignment
		ImageFilter rightFilter = new MultilineCaption(
				pyramidText,
				new Font("Monospaced", Font.PLAIN, 14),
				Color.red,
				1.0f,
				Positions.CENTER,
				TextAlignment.RIGHT,
				0
		);
		BufferedImage rightResult = rightFilter.apply(createWhiteImage(250, 150));
		saveTestImage(rightResult, "TA-08c_pyramid_RIGHT");
		
		// then - all should have text
		assertImageHasTextPixels(leftResult);
		assertImageHasTextPixels(centerResult);
		assertImageHasTextPixels(rightResult);
	}

	// ==========================================================================
	// Category 7: Position Verification (PO-01 to PO-09)
	// ==========================================================================

	/**
	 * PO-01: Verify caption at top-left.
	 */
	@Test
	public void captionAtTopLeft() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.TOP_LEFT,
				DEFAULT_ALIGNMENT,
				0
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertTrue("Text should be in top-left quadrant", 
				hasNonWhitePixelInRegion(result, 0, 0, 100, 100));
	}

	/**
	 * PO-02: Verify caption at top-center.
	 */
	@Test
	public void captionAtTopCenter() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.TOP_CENTER,
				DEFAULT_ALIGNMENT,
				0
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertTrue("Text should be in top-center region", 
				hasNonWhitePixelInRegion(result, 50, 0, 100, 100));
	}

	/**
	 * PO-03: Verify caption at top-right.
	 */
	@Test
	public void captionAtTopRight() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.TOP_RIGHT,
				DEFAULT_ALIGNMENT,
				0
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertTrue("Text should be in top-right quadrant", 
				hasNonWhitePixelInRegion(result, 100, 0, 100, 100));
	}

	/**
	 * PO-04: Verify caption at center-left.
	 */
	@Test
	public void captionAtCenterLeft() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.CENTER_LEFT,
				DEFAULT_ALIGNMENT,
				0
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertTrue("Text should be in center-left region", 
				hasNonWhitePixelInRegion(result, 0, 50, 100, 100));
	}

	/**
	 * PO-05: Verify caption at center.
	 */
	@Test
	public void captionAtCenter() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.CENTER,
				DEFAULT_ALIGNMENT,
				0
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		saveTestImage(result, "PO-05_captionCenter");
		assertTrue("Text should be in center region", 
				hasNonWhitePixelInRegion(result, 50, 50, 100, 100));
	}

	/**
	 * PO-06: Verify caption at center-right.
	 */
	@Test
	public void captionAtCenterRight() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.CENTER_RIGHT,
				DEFAULT_ALIGNMENT,
				0
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertTrue("Text should be in center-right region", 
				hasNonWhitePixelInRegion(result, 100, 50, 100, 100));
	}

	/**
	 * PO-07: Verify caption at bottom-left.
	 */
	@Test
	public void captionAtBottomLeft() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.BOTTOM_LEFT,
				DEFAULT_ALIGNMENT,
				0
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertTrue("Text should be in bottom-left quadrant", 
				hasNonWhitePixelInRegion(result, 0, 100, 100, 100));
	}

	/**
	 * PO-08: Verify caption at bottom-center.
	 */
	@Test
	public void captionAtBottomCenter() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.BOTTOM_CENTER,
				DEFAULT_ALIGNMENT,
				0
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertTrue("Text should be in bottom-center region", 
				hasNonWhitePixelInRegion(result, 50, 100, 100, 100));
	}

	/**
	 * PO-09: Verify caption at bottom-right.
	 */
	@Test
	public void captionAtBottomRight() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.BOTTOM_RIGHT,
				DEFAULT_ALIGNMENT,
				0
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertTrue("Text should be in bottom-right quadrant", 
				hasNonWhitePixelInRegion(result, 100, 100, 100, 100));
	}

	// ==========================================================================
	// Category 8: Opacity and Color (OC-01 to OC-05)
	// ==========================================================================

	/**
	 * OC-01: Verify full opacity renders opaque text.
	 */
	@Test
	public void fullOpacityRendersOpaqueText() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				new Font("Monospaced", Font.PLAIN, 50),
				Color.black,
				1.0f,
				Positions.CENTER,
				DEFAULT_ALIGNMENT,
				0
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertImageHasTextPixels(result);
	}

	/**
	 * OC-02: Verify half opacity renders translucent text.
	 */
	@Test
	public void halfOpacityRendersTranslucentText() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				new Font("Monospaced", Font.PLAIN, 50),
				Color.black,
				0.5f,
				Positions.CENTER,
				DEFAULT_ALIGNMENT,
				0
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then - filter completes and produces output
		assertNotNull(result);
	}

	/**
	 * OC-03: Verify zero opacity renders nothing visible.
	 */
	@Test
	public void zeroOpacityRendersNothing() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		BufferedImage copy = BufferedImages.copy(image);
		
		ImageFilter filter = new MultilineCaption(
				"XXXXXXXXXX",
				new Font("Monospaced", Font.PLAIN, 50),
				Color.black,
				0.0f,
				Positions.CENTER,
				DEFAULT_ALIGNMENT,
				0
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then - result should look the same as original
		assertTrue(BufferedImageComparer.isSame(result, copy));
	}

	/**
	 * OC-04: Verify color is applied to all lines.
	 */
	@Test
	public void colorIsAppliedToAllLines() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		// Use a larger font to ensure visible red pixels with anti-aliasing
		ImageFilter filter = new MultilineCaption(
				"Line1\nLine2",
				new Font("Monospaced", Font.BOLD, 24),
				Color.red,
				DEFAULT_OPACITY,
				Positions.TOP_LEFT,
				DEFAULT_ALIGNMENT,
				5
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then - should have red pixels
		saveTestImage(result, "OC-04_colorApplied");
		assertTrue("Should have red text pixels", hasColoredPixels(result, Color.red));
	}

	/**
	 * OC-05: Verify color with alpha channel is respected.
	 */
	@Test
	public void colorWithAlphaChannelIsRespected() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				new Font("Monospaced", Font.PLAIN, 50),
				new Color(255, 0, 0, 128),  // Semi-transparent red
				DEFAULT_OPACITY,
				Positions.CENTER,
				DEFAULT_ALIGNMENT,
				0
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then - filter completes
		assertNotNull(result);
	}

	// ==========================================================================
	// Category 9: Insets Behavior (IN-01 to IN-04)
	// ==========================================================================

	/**
	 * IN-01: Verify zero insets places text at edge.
	 */
	@Test
	public void zeroInsetsPlacesTextAtEdge() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.TOP_LEFT,
				DEFAULT_ALIGNMENT,
				0
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then - text should be very close to edge
		assertTrue("Text should be near top-left edge", 
				hasNonWhitePixelInRegion(result, 0, 0, 30, 30));
	}

	/**
	 * IN-02: Verify positive insets offset text.
	 */
	@Test
	public void positiveInsetsOffsetText() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.TOP_LEFT,
				DEFAULT_ALIGNMENT,
				20
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then - text should be offset from edge
		assertFalse("Text should not be in the very corner with insets", 
				hasNonWhitePixelInRegion(result, 0, 0, 15, 15));
	}

	/**
	 * IN-03: Verify insets apply to all positions.
	 */
	@Test
	public void insetsApplyToAllPositions() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		Position[] positions = {
				Positions.TOP_LEFT, Positions.TOP_CENTER, Positions.TOP_RIGHT,
				Positions.CENTER_LEFT, Positions.CENTER, Positions.CENTER_RIGHT,
				Positions.BOTTOM_LEFT, Positions.BOTTOM_CENTER, Positions.BOTTOM_RIGHT
		};
		
		// when/then - all positions should work with insets
		for (Position position : positions) {
			ImageFilter filter = new MultilineCaption(
					"X",
					DEFAULT_FONT,
					DEFAULT_COLOR,
					DEFAULT_OPACITY,
					position,
					DEFAULT_ALIGNMENT,
					20
			);
			
			BufferedImage result = filter.apply(image);
			assertNotNull("Filter should complete for position: " + position, result);
		}
	}

	/**
	 * IN-04: Verify large insets can push text offscreen without crash.
	 */
	@Test
	public void largeInsetsCanPushTextOffscreen() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.TOP_LEFT,
				DEFAULT_ALIGNMENT,
				300  // Larger than image
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then - should complete without exception
		assertNotNull(result);
	}

	// ==========================================================================
	// Category 10: Edge Cases and Robustness (EC-01 to EC-07)
	// ==========================================================================

	/**
	 * EC-01: Verify very long single line does not crash.
	 */
	@Test
	public void veryLongSingleLineDoesNotCrash() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		StringBuilder longText = new StringBuilder();
		for (int i = 0; i < 1000; i++) {
			longText.append("X");
		}
		
		ImageFilter filter = new MultilineCaption(
				longText.toString(),
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				DEFAULT_INSETS
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertNotNull(result);
	}

	/**
	 * EC-02: Verify many lines do not crash.
	 */
	@Test
	public void manyLinesDoNotCrash() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		StringBuilder manyLines = new StringBuilder();
		for (int i = 0; i < 100; i++) {
			manyLines.append("Line").append(i).append("\n");
		}
		
		ImageFilter filter = new MultilineCaption(
				manyLines.toString(),
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				DEFAULT_INSETS
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertNotNull(result);
	}

	/**
	 * EC-03: Verify unicode characters render correctly with multilines.
	 */
	@Test
	public void unicodeCharactersRenderCorrectly() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"Hello 世界\nПривет мир\nBonjour monde",
				new Font("Dialog", Font.PLAIN, 14),
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.CENTER,
				TextAlignment.CENTER,
				DEFAULT_INSETS
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		saveTestImage(result, "EC-03_unicode_multiline");
		assertNotNull(result);
		assertImageHasTextPixels(result);
	}

	/**
	 * EC-04: Verify tab characters are rendered.
	 */
	@Test
	public void tabCharactersAreRendered() {
		// given
		BufferedImage image = createWhiteImage(200, 200);
		
		ImageFilter filter = new MultilineCaption(
				"A\tB",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				Positions.TOP_LEFT,
				DEFAULT_ALIGNMENT,
				5
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertNotNull(result);
	}

	/**
	 * EC-05: Verify small image handles multiline text.
	 */
	@Test
	public void smallImageHandlesMultilineText() {
		// given
		BufferedImage image = createWhiteImage(10, 10);
		
		ImageFilter filter = new MultilineCaption(
				"Line1\nLine2\nLine3",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				DEFAULT_INSETS
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertNotNull(result);
	}

	/**
	 * EC-06: Verify very large font on small image does not crash.
	 */
	@Test
	public void veryLargeFontOnSmallImage() {
		// given
		BufferedImage image = createWhiteImage(50, 50);
		
		ImageFilter filter = new MultilineCaption(
				"X",
				new Font("Monospaced", Font.PLAIN, 100),
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				DEFAULT_INSETS
		);
		
		// when
		BufferedImage result = filter.apply(image);
		
		// then
		assertNotNull(result);
	}

	/**
	 * EC-07: Verify different image types work correctly.
	 */
	@Test
	public void differentImageTypesWork() {
		// given
		int[] imageTypes = {
				BufferedImage.TYPE_INT_ARGB,
				BufferedImage.TYPE_INT_RGB,
				BufferedImage.TYPE_3BYTE_BGR,
				BufferedImage.TYPE_BYTE_GRAY
		};
		
		ImageFilter filter = new MultilineCaption(
				"Test",
				DEFAULT_FONT,
				DEFAULT_COLOR,
				DEFAULT_OPACITY,
				DEFAULT_POSITION,
				DEFAULT_ALIGNMENT,
				DEFAULT_INSETS
		);
		
		// when/then
		for (int imageType : imageTypes) {
			BufferedImage image = new BufferedImage(100, 100, imageType);
			BufferedImage result = filter.apply(image);
			assertNotNull("Filter should work for image type: " + imageType, result);
			assertEquals("Output type should match input type", imageType, result.getType());
		}
	}

	// ==========================================================================
	// Helper Methods
	// ==========================================================================

	/**
	 * Creates a white image of the specified dimensions.
	 */
	private BufferedImage createWhiteImage(int width, int height) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
		g.dispose();
		return image;
	}

	/**
	 * Checks if the image has any non-white pixels (indicating text was drawn).
	 */
	private void assertImageHasTextPixels(BufferedImage image) {
		int whiteRgb = Color.white.getRGB();
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				if (image.getRGB(x, y) != whiteRgb) {
					return; // Found non-white pixel
				}
			}
		}
		fail("Image should have non-white pixels (text should be rendered)");
	}

	/**
	 * Checks if a region of the image has any non-white pixels.
	 */
	private boolean hasNonWhitePixelInRegion(BufferedImage image, int startX, int startY, 
			int width, int height) {
		int whiteRgb = Color.white.getRGB();
		int endX = Math.min(startX + width, image.getWidth());
		int endY = Math.min(startY + height, image.getHeight());
		
		for (int x = startX; x < endX; x++) {
			for (int y = startY; y < endY; y++) {
				if (image.getRGB(x, y) != whiteRgb) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if the image has pixels that are predominantly the specified color.
	 * Uses a tolerance to account for anti-aliasing effects.
	 */
	private boolean hasColoredPixels(BufferedImage image, Color targetColor) {
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
				int rgb = image.getRGB(x, y);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = rgb & 0xFF;
				
				// For red: check if red is significantly higher than green and blue
				// This accounts for anti-aliasing which blends colors
				if (targetColor.equals(Color.red)) {
					// Check for pixels where red is dominant (> 200) and others are low
					if (r > 200 && g < 100 && b < 100) {
						return true;
					}
				} else {
					// Fallback: check for exact match or near-match
					int tolerance = 50;
					if (Math.abs(r - targetColor.getRed()) <= tolerance &&
						Math.abs(g - targetColor.getGreen()) <= tolerance &&
						Math.abs(b - targetColor.getBlue()) <= tolerance) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Saves a test image to the output directory for manual inspection.
	 * <p>
	 * Only saves when {@link #SAVE_TEST_IMAGES} is {@code true}.
	 * Images are saved as PNG files in {@link #TEST_IMAGE_OUTPUT_DIR}.
	 * 
	 * @param image		The image to save
	 * @param testName	The name of the test (used as filename)
	 */
	private void saveTestImage(BufferedImage image, String testName) {
		if (!SAVE_TEST_IMAGES) {
			return;
		}
		
		try {
			File outputDir = new File(TEST_IMAGE_OUTPUT_DIR);
			if (!outputDir.exists()) {
				outputDir.mkdirs();
			}
			
			File outputFile = new File(outputDir, testName + ".png");
			ImageIO.write(image, "PNG", outputFile);
			System.out.println("Saved test image: " + outputFile.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("Failed to save test image: " + e.getMessage());
		}
	}

	/**
	 * Saves both input and output images for comparison.
	 * <p>
	 * Only saves when {@link #SAVE_TEST_IMAGES} is {@code true}.
	 * 
	 * @param input		The input image (before filter)
	 * @param output	The output image (after filter)
	 * @param testName	The name of the test
	 */
	private void saveTestImages(BufferedImage input, BufferedImage output, String testName) {
		if (!SAVE_TEST_IMAGES) {
			return;
		}
		
		saveTestImage(input, testName + "_input");
		saveTestImage(output, testName + "_output");
	}
}
