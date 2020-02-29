package net.coobird.thumbnailator.util.exif;

/**
 * IFD structure as defined in Section 4.6.2 of the Exif Specification
 * version 2.3.
 * 
 * @author coobird
 *
 */
public class IfdStructure
{
	private final int tag;
	private final IfdType type;
	private final int count;
	private final int offsetValue;
	
	/**
	 * Instantiates a IFD with the given attributes.
	 * 
	 * @param tag			The tag element.
	 * @param type			The type element.
	 * @param count			The count of values.
	 * @param offsetValue	The offset or value.
	 */
	public IfdStructure(int tag, int type, int count, int offsetValue)
	{
		super();
		this.tag = tag;
		this.type = IfdType.typeOf(type);
		this.count = count;
		this.offsetValue = offsetValue;
	}

	/**
	 * Returns the tag element in the IFD structure.
	 * @return		An integer representation of the tag element.
	 * 				Should be a value between 0x00 to 0xFF.
	 */
	public int getTag()
	{
		return tag;
	}

	/**
	 * Returns the type element in the IFD structure.
	 * @return		An {@link IfdType} enum indicating the type.
	 */
	public IfdType getType()
	{
		return type;
	}

	/**
	 * Returns the count element in the IFD structure, indicating the number
	 * of values the value field..
	 * @return		A count indicating the number of values.
	 */
	public int getCount()
	{
		return count;
	}

	/**
	 * Returns either the offset or value of the IFD.
	 * @return		Either the offset or value. The type of the returned value
	 * 				can be determined by the return of the {@link #isOffset()}
	 * 				or {@link #isValue()} method.
	 */
	public int getOffsetValue()
	{
		return offsetValue;
	}
	
	/**
	 * Returns whether the value returned by the {@link #getOffsetValue()}
	 * method is an actual value.
	 * @return		{@code true} if the value returned by the
	 * 				{@link #getOffsetValue()} method is a value, {@code false}
	 * 				otherwise.
	 */
	public boolean isValue()
	{
		/*
		 * The offsetValue field contains a value if the size of the value is
		 * less than or equal to 4 bytes see "Value Offset" in Section 4.6.3
		 * of the Exif version 2.3 specification.
		 */
		return type.size() * count <= 4;
	}
	
	/**
	 * Returns whether the value returned by the {@link #getOffsetValue()}
	 * method is an offset value.
	 * @return		{@code true} if the value returned by the
	 * 				{@link #getOffsetValue()} method is a offset value,
	 * 				{@code false} otherwise.
	 */
	public boolean isOffset()
	{
		return !isValue();
	}

	/**
	 * Returns the calculated hash code for this object.
	 * @return		Hash code for this object.
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + offsetValue;
		result = prime * result + tag;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/**
	 * Returns whether this object is equal to the given object.
	 * @return		{@code true} if the given object and this object is
	 * 				equivalent, {@code false} otherwise.
	 */
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IfdStructure other = (IfdStructure) obj;
		if (count != other.count)
			return false;
		if (offsetValue != other.offsetValue)
			return false;
		if (tag != other.tag)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	/**
	 * Returns a textual {@link String} reprensentation of this object.
	 * @return			A textual representation of this object.
	 */
	@Override
	public String toString()
	{
		return "IfdStructure [tag=" + Integer.toHexString(tag) +
				", type=" + type + ", count=" + count +
				", offsetValue=" + offsetValue + "]";
	}
}
