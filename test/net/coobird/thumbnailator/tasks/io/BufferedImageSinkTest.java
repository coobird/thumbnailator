package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;

public class BufferedImageSinkTest
{
	@Test
	public void writeImage() throws IOException
	{
		// given
		BufferedImage img = 
			new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
		
		// when
		BufferedImageSink sink = new BufferedImageSink();
		sink.write(img);
		
		// then
		assertSame(img, sink.getSink());
	}
	
	@Test(expected=NullPointerException.class)
	public void writeNull() throws IOException
	{
		// given
		BufferedImage img = null;
		
		try
		{
			// when
			new BufferedImageSink().write(img);
			fail();
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("Cannot write a null image.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IllegalStateException.class)
	public void getSink_BeforeWrite() throws IOException
	{
		// given
		
		try
		{
			// when
			new BufferedImageSink().getSink();
			fail();
		}
		catch (IllegalStateException e)
		{
			// then
			assertEquals("BufferedImageSink has not been written to yet.", e.getMessage());
			throw e;
		}
	}
	
	@Test
	public void setOutputFormatName_DoesntAffectAnything() throws IOException
	{
		// given
		BufferedImageSink sink0 = new BufferedImageSink();
		BufferedImageSink sink1 = new BufferedImageSink();
		
		BufferedImage img = 
			new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
		
		// when
		sink0.setOutputFormatName("PNG");
		
		sink0.write(img);
		sink1.write(img);
		
		// then
		assertSame(img, sink0.getSink());
		assertSame(img, sink1.getSink());
	}
}
