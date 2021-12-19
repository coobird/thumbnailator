/*
 * Thumbnailator - a thumbnail generation library
 *
 * Copyright (c) 2008-2021 Chris Kroells
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

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.test.BufferedImageAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class ExifWorkaroundTest {

    @Parameterized.Parameters(name = "tags={0}")
    public static Collection<List<String>> tagOrder() {
        return Arrays.asList(
                Arrays.asList("app0.segment", "exif.segment"),
                Arrays.asList("exif.segment", "app0.segment"),
                Arrays.asList("app0.segment", "exif.segment", "xmp.segment"),
                Arrays.asList("app0.segment", "xmp.segment", "exif.segment"),
                Arrays.asList("exif.segment", "app0.segment", "xmp.segment"),
                Arrays.asList("xmp.segment", "app0.segment", "exif.segment"),
                Arrays.asList("exif.segment", "xmp.segment", "app0.segment"),
                Arrays.asList("xmp.segment", "exif.segment", "app0.segment")
        );
    }

    @Parameterized.Parameter
    public List<String> tags;

    private InputStream getFromResource(String name) {
        return this.getClass().getClassLoader().getResourceAsStream("Exif/fragments/" + name);
    }

    private InputStream buildJpeg() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        List<String> resources = new ArrayList<String>();
        resources.add("soi.segment");
        resources.addAll(tags);
        resources.add("rest");

        for (String resource : resources) {
            InputStream is = getFromResource(resource);
            while (is.available() > 0) {
                baos.write(is.read());
            }
        }

        return new ByteArrayInputStream(baos.toByteArray());
    }

    @Test
    public void withWorkaround() throws IOException {
        BufferedImage result = Thumbnails.of(buildJpeg())
                .scale(1.0f)
                .asBufferedImage();

        assertPasses(result);
    }

    @Test
    public void withoutWorkaround() throws IOException {
        System.setProperty("thumbnailator.disableExifWorkaround", "true");

        BufferedImage result = Thumbnails.of(buildJpeg())
                .scale(1.0f)
                .asBufferedImage();

        if (tags.get(0).equals("app0.segment")) {
            assertPasses(result);
        } else {
            assertFails(result);
        }
    }

    @Before @After
    public void cleanup() {
        System.clearProperty("thumbnailator.disableExifWorkaround");
    }

    private void assertPasses(BufferedImage result) {
        BufferedImageAssert.assertMatches(
                result,
                new float[] {
                        1, 1, 1,
                        1, 1, 1,
                        1, 0, 0,
                }
        );
    }

    private void assertFails(BufferedImage result) {
        BufferedImageAssert.assertMatches(
                result,
                new float[] {
                        1, 1, 1,
                        1, 1, 1,
                        0, 0, 1,
                }
        );
    }
}
