/*
 * Thumbnailator - a thumbnail generation library
 *
 * Copyright (c) 2008-2020 Chris Kroells
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

package net.coobird.thumbnailator.util.exif;

import net.coobird.thumbnailator.filters.Flip;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.filters.Pipeline;
import net.coobird.thumbnailator.filters.Rotation;

/**
 * An utility class which returns a suitable {@link ImageFilter} to perform
 * the transformations necessary to properly orient an image according to the
 * Exif metadata.
 * 
 * @author coobird
 *
 */
public final class ExifFilterUtils {
	/**
	 * This class should not be instantiated.
	 */
	private ExifFilterUtils() {};
	
	/**
	 * Returns a {@link ImageFilter} which will perform the transformations
	 * required to properly orient the thumbnail according to the Exif
	 * orientation.
	 * 
	 * @param orientation	The Exif orientation
	 * @return				{@link ImageFilter}s required to properly
	 * 						orient the image.
	 */
    public static ImageFilter getFilterForOrientation(Orientation orientation) {
        Pipeline filters = new Pipeline();

        if (orientation == Orientation.TOP_RIGHT) {
            filters.add(Flip.HORIZONTAL);

        } else if (orientation == Orientation.BOTTOM_RIGHT) {
            filters.add(Rotation.ROTATE_180_DEGREES);

        } else if (orientation == Orientation.BOTTOM_LEFT) {
            filters.add(Rotation.ROTATE_180_DEGREES);
            filters.add(Flip.HORIZONTAL);

        } else if (orientation == Orientation.LEFT_TOP) {
            filters.add(Rotation.RIGHT_90_DEGREES);
            filters.add(Flip.HORIZONTAL);

        } else if (orientation == Orientation.RIGHT_TOP) {
            filters.add(Rotation.RIGHT_90_DEGREES);

        } else if (orientation == Orientation.RIGHT_BOTTOM) {
            filters.add(Rotation.LEFT_90_DEGREES);
            filters.add(Flip.HORIZONTAL);

        } else if (orientation == Orientation.LEFT_BOTTOM) {
            filters.add(Rotation.LEFT_90_DEGREES);
        }

        return filters;
    }
}
