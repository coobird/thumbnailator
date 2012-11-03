package net.coobird.thumbnailator.util.exif;

public class IfdStructure {
	private final int tag;
	private final IfdType type;
	private final int count;
	private final int offsetValue;
	
	public IfdStructure(int tag, int type, int count, int offsetValue) {
		super();
		this.tag = tag;
		this.type = IfdType.typeOf(type);
		this.count = count;
		this.offsetValue = offsetValue;
	}

	public int getTag() {
		return tag;
	}

	public IfdType getType() {
		return type;
	}

	public int getCount() {
		return count;
	}

	public int getOffsetValue() {
		return offsetValue;
	}
	
	public boolean isValue() {
		// The offsetValue field contains a value if the size of the value is less than or equal to 4 bytes
		// see "Value Offset" in 4.6.3 of Exif 2.3 specification
		return type.size() * count <= 4;
	}
	
	public boolean isOffset() {
		return !isValue();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + offsetValue;
		result = prime * result + tag;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
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

	@Override
	public String toString() {
		return "IfdStructure [tag=" + Integer.toHexString(tag) + ", type=" + type + ", count="
				+ count + ", offsetValue=" + offsetValue + "]";
	}
}
