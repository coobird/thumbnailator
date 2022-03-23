package net.coobird.thumbnailator;

public class Validator {
    /**
     * Performs validation on the specified dimensions.
     * <p>
     * If any of the dimensions are less than or equal to 0, an
     * {@code IllegalArgumentException} is thrown with an message specifying the
     * reason for the exception.
     * <p>
     *
     * @param width		The width to validate.
     * @param height	The height to validate.
     */
    public void validateDimensions(int width, int height) {
        if (width <= 0 && height <= 0) {
            throw new IllegalArgumentException(
                    "Destination image dimensions must not be less than " +
                            "0 pixels."
            );

        } else if (width <= 0 || height <= 0) {
            String dimension = width == 0 ? "width" : "height";

            throw new IllegalArgumentException(
                    "Destination image " + dimension + " must not be " +
                            "less than or equal to 0 pixels."
            );
        }
    }
}
