package net.coobird.thumbnailator.util.exif;

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
	
	/*
	 * Returns the byte size of this type
	 */
	public int size() 
	{
		return size;
	}
	
	public int value() 
	{
		return value;
	}
	
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
