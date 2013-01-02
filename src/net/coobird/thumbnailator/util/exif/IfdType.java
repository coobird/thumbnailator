package net.coobird.thumbnailator.util.exif;

/**
 * This enum corresponds to the types of data present in an IFD,
 * as defined in Section 4.6.2 of the Exif Specification version 2.3.
 * 
 * @author coobird
 *
 */
public enum IfdType 
{
	/**
	 * An 8-bit unsigned integer value.
	 */
	BYTE(1, 1),
	
	/**
	 * An 8-bit value containing a single 7-bit ASCII character.
	 * The final byte is NULL-terminated.
	 */
	ASCII(2, 1),
	
	/**
	 * A 16-bit unsigned integer value.  
	 */
	SHORT(3, 2),
	
	/**
	 * A 32-bit unsigned integer value.  
	 */
	LONG(4, 4),
	
	/**
	 * Two {@link #LONG} values, where the first {@code LONG} is the
	 * numerator, while the second {@code LONG} is the denominator.
	 */
	RATIONAL(5, LONG.size() * 2),
	
	/**
	 * An 8-bit value which can be value as defined elsewhere. 
	 */
	UNDEFINED(7, 1),
	
	/**
	 * A 32-bit signed integer value using 2's complement.
	 */
	SLONG(9, 4),
	
	/**
	 * Two {@link #SLONG} values, where the first {@code SLONG} is the
	 * numerator, while the second {@code SLONG} is the denominator. 
	 */
	SRATIONAL(5, SLONG.size() * 2),
	;
	
	private int value;
	private int size;
	
	private IfdType(int value, int size) 
	{
		this.value = value;
		this.size = size;
	}
	
	/**
	 * Returns the size in bytes for this IFD type.
	 * @return	Size in bytes for this IFD type.
	 */
	public int size() 
	{
		return size;
	}
	
	/**
	 * Returns the IFD type as a type value.
	 * @return	IFD type as a type value.
	 */
	public int value() 
	{
		return value;
	}
	
	/**
	 * Returns the {@link IfdType} corresponding to the given IFD type value.
	 * 
	 * @param value		The IFD type value.
	 * @return			{@link IfdType} corresponding to the IDF type value.
	 * 					Return {@code null} if the given value does not
	 * 					correspond to a	valid {@link IfdType}.
	 */
	public static IfdType typeOf(int value) 
	{
		for (IfdType type : IfdType.values()) 
		{
			if (type.value == value) 
			{
				return type;
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
		return "IfdType [type=" + this.name() + ", value=" + value + ", size=" + size + "]";
	}
}
