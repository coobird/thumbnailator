package net.coobird.thumbnailator.makers;

public class DimensionValidation {
    static void validate(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Width and height must be greater than zero.");
        }
    }

    static void validateScalingFactor(double factor) {
        if (factor <= 0) {
            throw new IllegalArgumentException("Scaling factor must be greater than zero.");
        }
    }
}

