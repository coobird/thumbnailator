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

package net.coobird.thumbnailator.makers;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.resizers.FixedResizerFactory;
import net.coobird.thumbnailator.resizers.Resizer;
import net.coobird.thumbnailator.resizers.DefaultResizerFactory;
import net.coobird.thumbnailator.resizers.ResizerFactory;

/**
 * An abstract class which provides support functionalities for
 * {@link ThumbnailMaker} implementations.
 * 
 * @author coobird
 *
 */
public abstract class ThumbnailMaker {
	/**
	 * String used for an exception message.
	 */
	private static final String NOT_READY_FOR_MAKE = "Maker not ready to " +
			"make thumbnail.";
	
	/**
	 * Used when determining whether the "imageType" parameter has been set
	 * already or not.
	 */
	private static final String PARAM_IMAGE_TYPE = "imageType";
	
	
	/**
	 * Used when determining whether the "resizer" parameter has been set
	 * already or not.
	 */
	private static final String PARAM_RESIZER = "resizer";
	
	/**
	 * Used when determining whether the "resizerFactory" parameter has been set
	 * already or not.
	 */
	private static final String PARAM_RESIZERFACTORY = "resizerFactory";
	
	/**
	 * Class which keeps track of the parameters being set for the
	 * {@link ThumbnailMaker}.
	 * <p>
	 * This class provides functionality to determine whether or not all the
	 * required parameters have been set or not.
	 * 
	 * @author coobird
	 *
	 */
	protected final static class ReadinessTracker {
		private final Map<String, Boolean> alreadySetMap = new HashMap<String, Boolean>();
		
		/**
		 * Returns whether or not the {@link ThumbnailMaker} has all its
		 * required parameter set to be able to make a thumbnail.
		 * 
		 * @return			{@code true} if the {@link ThumbnailMaker} is ready
		 * 					to make thumbnails, {@code false} otherwise.
		 */
		protected boolean isReady() {
			for (Map.Entry<String, Boolean> entry : alreadySetMap.entrySet()) {
				if (!entry.getValue()) {
					return false;
				}
			}
			return true;
		}
		
		/**
		 * Tells the {@link ReadinessTracker} that the given parameter has not
		 * yet been set by the {@link ThumbnailMaker}.
		 * 
		 * @param parameterName		The parameter which has not been set.
		 */
		protected void unset(String parameterName) {
			alreadySetMap.put(parameterName, false);
		}
		
		/**
		 * Tells the {@link ReadinessTracker} that the given parameter has been
		 * set by the {@link ThumbnailMaker}.
		 * 
		 * @param parameterName		The parameter to be marked as being set.
		 */
		protected void set(String parameterName) {
			alreadySetMap.put(parameterName, true);
		}
		
		/**
		 * Returns whether the specified parameter has already been set.
		 * 
		 * @param parameterName		The parameter to check whether it has been
		 * 							already set or not
		 * @return					{@code true} if the parameter has been set,
		 * 							{@code false} otherwise.
		 */
		protected boolean isSet(String parameterName) {
			return alreadySetMap.get(parameterName);
		}
	}
	
	/**
	 * Object used to keep track whether the required parameters for creating
	 * a thumbnail has been set.
	 */
	protected final ReadinessTracker ready;
	
	/**
	 * Default image type of the thumbnails created by {@link ThumbnailMaker}.
	 */
	private static final int DEFAULT_IMAGE_TYPE = BufferedImage.TYPE_INT_ARGB;
	
	/**
	 * The image type of the resulting thumbnail.
	 */
	protected int imageType;
	
	/**
	 * The {@link ResizerFactory} which is used to obtain a {@link Resizer}
	 * for the resizing operation.
	 * <p>
	 * By delaying the decision of picking the {@link Resizer} to use until
	 * when the thumbnail is to be created could lead to a more suitable
	 * {@link Resizer} being picked, as the dimensions for the source and
	 * destination images are known at that time.
	 */
	protected ResizerFactory resizerFactory;

	/**
	 * Creates and initializes an instance of {@link ThumbnailMaker}.
	 */
	public ThumbnailMaker() {
		ready = new ReadinessTracker();
		ready.unset(PARAM_IMAGE_TYPE);
		ready.unset(PARAM_RESIZER);
		ready.unset(PARAM_RESIZERFACTORY);
		defaultImageType();
		defaultResizerFactory();
	}
	
	/**
	 * Makes a thumbnail.
	 * 
	 * @param img		The source image.
	 * @return			The thumbnail created from the source image, using the
	 * 					parameters set by the {@link ThumbnailMaker}.
	 */
	public abstract BufferedImage make(BufferedImage img);
	
	/**
	 * Makes a thumbnail of the specified dimensions, from the specified
	 * source image.
	 * 
	 * @param img		The source image.
	 * @param width		The target width of the thumbnail.
	 * @param height	The target height of the thumbnail.
	 * @return			The thumbnail image.
	 * @throws IllegalStateException		If the {@code ThumbnailMaker} is
	 * 										not ready to create thumbnails.
	 * @throws IllegalArgumentException		If the width and/or height is less
	 * 										than or equal to zero.
	 */
	protected BufferedImage makeThumbnail(BufferedImage img, int width, int height) {
		if (!ready.isReady()) {
			throw new IllegalStateException(ThumbnailMaker.NOT_READY_FOR_MAKE);
		}
		
		if (width <= 0) {
			throw new IllegalArgumentException(
					"Width must be greater than zero."
			);
		}
		if (height <= 0) {
			throw new IllegalArgumentException(
					"Height must be greater than zero."
			);
		}

		BufferedImage thumbnailImage =
			new BufferedImageBuilder(width, height, imageType).build();
		
		Dimension imgSize = new Dimension(img.getWidth(), img.getHeight());
		Dimension thumbnailSize = new Dimension(width, height);
		
		Resizer resizer = resizerFactory.getResizer(imgSize, thumbnailSize);
		
		resizer.resize(img, thumbnailImage);
		
		return thumbnailImage;
	}

	/**
	 * Sets the type for the {@link BufferedImage} to produce.
	 * 
	 * @param imageType		The type of the {@code BufferedImage}.
	 * @return				A reference to this object.
	 */
	public ThumbnailMaker imageType(int imageType) {
		this.imageType = imageType;
		ready.set(PARAM_IMAGE_TYPE);
		return this;
	}
	
	/**
	 * Sets the type of the {@link BufferedImage} to be the default type.
	 * 
	 * @return				A reference to this object.
	 */
	public ThumbnailMaker defaultImageType() {
		return imageType(DEFAULT_IMAGE_TYPE);
	}
	
	/**
	 * Sets the {@link Resizer} which is used for the resizing operation.
	 * 
	 * @param resizer		The {@link Resizer} to use when resizing the image
	 * 						to create the thumbnail.
	 * @return				A reference to this object.
	 */
	public ThumbnailMaker resizer(Resizer resizer) {
		this.resizerFactory = new FixedResizerFactory(resizer);
		ready.set(PARAM_RESIZER);
		ready.set(PARAM_RESIZERFACTORY);
		return this;
	}
	
	/**
	 * Sets the {@link Resizer} to use the default {@link Resizer}.
	 * 
	 * @return				A reference to this object.
	 */
	public ThumbnailMaker defaultResizer() {
		return defaultResizerFactory();
	}
	
	/**
	 * Sets the {@link ResizerFactory} which is used to obtain a {@link Resizer}
	 * for the resizing operation.
	 * 
	 * @param resizerFactory		The {@link ResizerFactory} to obtain the
	 * 						{@link Resizer} used when resizing the image
	 * 						to create the thumbnail.
	 * @return				A reference to this object.
	 * @since	0.4.0
	 */
	public ThumbnailMaker resizerFactory(ResizerFactory resizerFactory) {
		this.resizerFactory = resizerFactory;
		ready.set(PARAM_RESIZER);
		ready.set(PARAM_RESIZERFACTORY);
		return this;
	}
	
	/**
	 * Sets the {@link ResizerFactory} to use {@link DefaultResizerFactory}.
	 * 
	 * @return				A reference to this object.
	 * @since	0.4.0
	 */
	public ThumbnailMaker defaultResizerFactory() {
		this.resizerFactory = DefaultResizerFactory.getInstance();
		ready.set(PARAM_RESIZER);
		ready.set(PARAM_RESIZERFACTORY);
		return this;
	}
}
