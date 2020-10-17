package net.coobird.thumbnailator.filters;

import java.awt.image.BufferedImage;

import static org.junit.Assert.assertEquals;

public class ImageFilterTestUtils {
    private static final int[] IMAGE_TYPES = new int[] {
            BufferedImage.TYPE_INT_ARGB,
            BufferedImage.TYPE_INT_RGB,
            BufferedImage.TYPE_BYTE_GRAY,
            BufferedImage.TYPE_BYTE_INDEXED,
            BufferedImage.TYPE_3BYTE_BGR,
            BufferedImage.TYPE_4BYTE_ABGR,
            BufferedImage.TYPE_4BYTE_ABGR_PRE,
            BufferedImage.TYPE_BYTE_BINARY,
            BufferedImage.TYPE_INT_ARGB_PRE,
            BufferedImage.TYPE_INT_BGR,
            BufferedImage.TYPE_USHORT_555_RGB,
            BufferedImage.TYPE_USHORT_565_RGB,
            BufferedImage.TYPE_USHORT_GRAY
    };

    public static void assertImageTypeRetained(ImageFilter filter) {
        for (int bufferedImageType : IMAGE_TYPES) {
            // given
            BufferedImage originalImage = new BufferedImage(200, 200, bufferedImageType);

            // when
            BufferedImage finalImage = filter.apply(originalImage);

            // then
            assertEquals(originalImage.getType(), finalImage.getType());
        }
    }
}
