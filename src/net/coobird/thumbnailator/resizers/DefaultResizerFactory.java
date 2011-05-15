package net.coobird.thumbnailator.resizers;

import java.awt.Dimension;


/**
 * This class provides factory methods which provides suitable {@link Resizer}s
 * for a given situation.
 * <p>
 * <DL>
 * <DT><B>Usage:</B></DT>
 * <DD>
 * The following example code demonstrates how to use {@link DefaultResizerFactory}
 * in order to obtain the optimal {@link Resizer}, and using that in order to
 * perform the resizing operation.
 * <p>
 * <pre>
BufferedImage sourceImage = new BufferedImageBuilder(400, 400).build();
BufferedImage destImage = new BufferedImageBuilder(200, 200).build();

Dimension sourceSize = new Dimension(sourceImage.getWidth(), sourceImage.getHeight());
Dimension destSize = new Dimension(destImage.getWidth(), destImage.getHeight());

// Obtain the optimal Resizer for this resizing operation.
Resizer resizer = DefaultResizerFactory.getInstance().getResizer(sourceSize, destSize);

// Perform the resizing using the Resizer obtained from the ResizerFactory.
resizer.resize(sourceImage, destImage);
 * </pre>
 * </DD>
 * </DL>
 * When a specific {@link Resizer} is required, the {@link Resizers} enum
 * is another way to obtain {@link Resizer}s.
 * <p>
 * 
 * @see Resizers
 *  
 * @author coobird
 *
 */
public class DefaultResizerFactory implements ResizerFactory
{
	private static final DefaultResizerFactory INSTANCE = new DefaultResizerFactory();

	/**
	 * This class is not intended to be instantiated via the constructor. 
	 */
	private DefaultResizerFactory() {}
	
	/**
	 * Returns an instance of this class.
	 * 
	 * @return		An instance of this class.
	 */
	public static ResizerFactory getInstance()
	{
		return INSTANCE;
	}
	
	public Resizer getResizer()
	{
		return Resizers.PROGRESSIVE;
	}
	
	public Resizer getResizer(Dimension originalSize, Dimension thumbnailSize)
	{
		int ow = originalSize.width;
		int oh = originalSize.height;
		int tw = thumbnailSize.width;
		int th = thumbnailSize.height;
		
		if (tw < ow && th < oh)
		{
			if (tw < (ow / 2) && th < (oh / 2))
			{
				return Resizers.PROGRESSIVE;
			}
			else
			{
				return Resizers.BILINEAR;
			}
		}
		else if (tw > ow && th > oh)
		{
			return Resizers.BICUBIC;
		}
		else if (tw == ow && th == oh)
		{
			return getResizer();
		}
		else
		{
			return getResizer();
		}
	}
}