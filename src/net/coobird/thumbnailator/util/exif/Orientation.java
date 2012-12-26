package net.coobird.thumbnailator.util.exif;

/**
 * Orientation of the EXIF metadata.
 * <p>
 * As defined in Section 4.6.4 of the Exif Specification version 2.3.
 * 
 * @author coobird
 *
 */
public enum Orientation 
{
	TOP_LEFT(1),
	TOP_RIGHT(2),
	BOTTOM_RIGHT(3),
	BOTTOM_LEFT(4),
	LEFT_TOP(5),
	RIGHT_TOP(6),
	RIGHT_BOTTOM(7),
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
	
	@Override
	public String toString() 
	{
		return "Orientation [type=" + value + "]";
	}
}
