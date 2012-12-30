package net.coobird.thumbnailator.util.exif;

/**
 * IFD type.
 * <p> 
 * As defined in Section 4.6.2 of the Exif Specification version 2.3.
 * 
 * @author coobird
 *
 */
public enum IfdType 
{
	BYTE(1, 1),
	ASCII(2, 1),
	SHORT(3, 2),
	LONG(4, 4),
	RATIONAL(5, LONG.size() * 2),
	UNDEFINED(7, 1),
	SLONG(9, 4),
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
	
	@Override
	public String toString() 
	{
		return "IfdType [type=" + this.name() + ", value=" + value + ", size=" + size + "]";
	}
}
