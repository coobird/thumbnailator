package net.coobird.thumbnailator.tasks;

import java.io.IOException;

/**
 * An exception used to indicate that the specified format could not be
 * used in an operation.
 * 
 * @author coobird
 *
 */
public class UnsupportedFormatException extends IOException
{
	/**
	 * An ID used for serialization.
	 */
	private static final long serialVersionUID = 1254432584303852552L;
	
	/**
	 * The format name which was not supported.
	 */
	private final String formatName;

	/**
	 * A constant which is used to indicate an unknown format.
	 */
	public static final String UNKNOWN = "<unknown>";

	/**
	 * Instantiates a {@link UnsupportedFormatException} with the unsupported
	 * format.
	 * 
	 * @param formatName	Format name.
	 */
	public UnsupportedFormatException(String formatName)
	{
		super();
		this.formatName = formatName;
	}

	/**
	 * Instantiates a {@link UnsupportedFormatException} with the unsupported
	 * format and a detailed message.
	 * 
	 * @param formatName	Format name.
	 * @param s				A message detailing the exception.
	 */
	public UnsupportedFormatException(String formatName, String s)
	{
		super(s);
		this.formatName = formatName;
	}

	/**
	 * Returns the format name which is not supported.
	 * 
	 * @return			Format name.
	 */
	public String getFormatName()
	{
		return formatName;
	}
}
