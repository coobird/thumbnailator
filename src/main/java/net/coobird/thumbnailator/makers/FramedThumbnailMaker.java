package net.coobird.thumbnailator.makers;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.filters.Watermark;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * @author Panos Bariamis (pbaris)
 */
public final class FramedThumbnailMaker extends FixedSizeThumbnailMaker {

    private static final String PARAM_USE_FRAME = "useFrame";
    private static final String PARAM_FRAME_COLOR = "frameColor";

    private Color frameColor;

    public FramedThumbnailMaker(int width, int height) {
        super(width, height);
        ready.set(PARAM_USE_FRAME);
        ready.unset(PARAM_FRAME_COLOR);
    }

    public FramedThumbnailMaker frameColor(Color color) {
        if (ready.isSet(PARAM_FRAME_COLOR)) {
            throw new IllegalStateException("The frame color has already been set.");
        }

        if (color == null) {
            throw new IllegalArgumentException("Color it's not an instance");
        }

        this.frameColor = color;

        ready.set(PARAM_FRAME_COLOR);
        return this;
    }

    @Override
    public FramedThumbnailMaker size(int width, int height) {
        return (FramedThumbnailMaker)super.size(width, height);
    }

    @Override
    public FramedThumbnailMaker keepAspectRatio(boolean keep) {
        return (FramedThumbnailMaker)super.keepAspectRatio(keep);
    }

    @Override
    public FramedThumbnailMaker fitWithinDimensions(boolean fit) {
        return (FramedThumbnailMaker)super.fitWithinDimensions(fit);
    }

    @Override
    public BufferedImage make(BufferedImage img) {
        BufferedImage framedImage = new BufferedImageBuilder(getWidth(), getHeight(), TYPE_INT_ARGB).build();
        Graphics2D g = framedImage.createGraphics();
        g.setColor(frameColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.dispose();

        img = super.make(img);
        Watermark wm = new Watermark(Positions.CENTER, img, 1.0f);
        framedImage = wm.apply(framedImage);

        return framedImage;
    }
}
