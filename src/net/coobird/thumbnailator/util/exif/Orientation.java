package net.coobird.thumbnailator.util.exif;

public enum Orientation {
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
	private Orientation(int value) {
		this.value = value;
	}
	
	public static Orientation typeOf(int value) {
		for (Orientation orientation : Orientation.values()) {
			if (orientation.value == value) {
				return orientation;
			}
		}
		
		return null;
	}
	
	@Override
	public String toString() {
		return "Orientation [type=" + value + "]";
	}
}
