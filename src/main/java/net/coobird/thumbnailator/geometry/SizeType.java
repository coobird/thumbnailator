package net.coobird.thumbnailator.geometry;

public class SizeType {
    public void checkDimesion(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
                    "Width and height must be greater than 0."
            );
        }
    }
}
