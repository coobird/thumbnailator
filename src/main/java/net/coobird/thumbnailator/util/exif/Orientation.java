package net.coobird.thumbnailator.util.exif;

/**
 * Representation for the Orientation (Tag 274) in the Exif metadata, as
 * defined in Section 4.6.4 of the Exif Specification version 2.3.
 * 
 * @author coobird
 *
 */
public enum Orientation
{
	/**
	 * Orientation 1.
	 * <ul>
	 * <li>First row: visual top of the image</li>
	 * <li>First column: visual left-hand side of the image</li>
	 * </ul>
	 */
	TOP_LEFT(1),
	
	/**
	 * Orientation 2.
	 * <ul>
	 * <li>First row: visual top of the image</li>
	 * <li>First column: visual right-hand side of the image</li>
	 * </ul>
	 */
	TOP_RIGHT(2),
	
	/**
	 * Orientation 3.
	 * <ul>
	 * <li>First row: visual bottom of the image</li>
	 * <li>First column: visual right-hand side of the image</li>
	 * </ul>
	 */
	BOTTOM_RIGHT(3),
	
	/**
	 * Orientation 4.
	 * <ul>
	 * <li>First row: visual bottom of the image</li>
	 * <li>First column: visual left-hand side of the image</li>
	 * </ul>
	 */	
	BOTTOM_LEFT(4),
	
	/**
	 * Orientation 5.
	 * <ul>
	 * <li>First row: visual left-hand side of the image</li>
	 * <li>First column: visual top of the image</li>
	 * </ul>
	 */	
	LEFT_TOP(5),
	
	/**
	 * Orientation 6.
	 * <ul>
	 * <li>First row: visual right-hand side of the image</li>
	 * <li>First column: visual top of the image</li>
	 * </ul>
	 */	
	RIGHT_TOP(6),
	
	/**
	 * Orientation 7.
	 * <ul>
	 * <li>First row: visual right-hand side of the image</li>
	 * <li>First column: visual bottom of the image</li>
	 * </ul>
	 */	
	RIGHT_BOTTOM(7),
	
	/**
	 * Orientation 8.
	 * <ul>
	 * <li>First row: visual left-hand side of the image</li>
	 * <li>First column: visual bottom of the image</li>
	 * </ul>
	 */	
	LEFT_BOTTOM(8),
	;
	
	private int value;
	private Orientation(int value)
	{
		this.value = value;
	}

	/**
	 * Returns the {@link Orientation} corresponding to the given orientation
	 * value.
	 * 
	 * @param value		The orientation value.
	 * @return			{@link Orientation} corresponding to the orientation
	 * 					value. Return {@code null} if the given value does not
	 * 					correspond to a	valid {@link Orientation}.
	 */
	public static Orientation typeOf(int value)
	{
		for (Orientation orientation : Orientation.values())
		{
			if (orientation.value == value)
			{
				return orientation;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns a textual {@link String} reprensentation of this enum.
	 * @return			A textual representation of this enum.
	 */
	@Override
	public String toString()
	{
		return "Orientation [type=" + value + "]";
	}
}
