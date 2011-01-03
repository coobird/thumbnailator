package net.coobird.thumbnailator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;

import net.coobird.thumbnailator.builders.BufferedImageBuilder;
import net.coobird.thumbnailator.name.Rename;

import org.junit.Test;

public class ThumbnailsBuilderNullEmptyOutputTest
{
	@Test(expected=NullPointerException.class)
	public void of_Files_asFiles_Iterable_Null() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try
		{
			// when
			Thumbnails.of(img)
				.size(50, 50)
				.asFiles((Iterable<File>)null);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("File name iterable is null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void of_Files_asFiles_Iterable_Empty() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try
		{
			// when
			Thumbnails.of(img)
				.size(50, 50)
				.asFiles(Collections.<File>emptyList());
		}
		catch (IndexOutOfBoundsException e)
		{
			// then
			assertEquals("Not enough file names provided by iterator.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void of_Files_asFiles_Iterable_NotEnough() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		File outFile = new File("test-resources/Thumbnailator/ofFilesNotEnough.png");
		outFile.deleteOnExit();
		
		try
		{
			// when
			Thumbnails.of(img, img)
				.size(50, 50)
				.asFiles(Arrays.asList(outFile));
		}
		catch (IndexOutOfBoundsException e)
		{
			// then
			assertEquals("Not enough file names provided by iterator.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void of_Files_toFiles_Iterable_Null() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try
		{
			// when
			Thumbnails.of(img)
				.size(50, 50)
				.toFiles((Iterable<File>)null);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("File name iterable is null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void of_Files_toFiles_Iterable_Empty() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try
		{
			// when
			Thumbnails.of(img)
				.size(50, 50)
				.toFiles(Collections.<File>emptyList());
		}
		catch (IndexOutOfBoundsException e)
		{
			// then
			assertEquals("Not enough file names provided by iterator.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void of_Files_toFiles_Iterable_NotEnough() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		File outFile = new File("test-resources/Thumbnailator/ofFilesNotEnough.png");
		outFile.deleteOnExit();
		
		try
		{
			// when
			Thumbnails.of(img, img)
				.size(50, 50)
				.toFiles(Arrays.asList(outFile));
		}
		catch (IndexOutOfBoundsException e)
		{
			// then
			assertEquals("Not enough file names provided by iterator.", e.getMessage());
			throw e;
		}
	}

	@Test(expected=NullPointerException.class)
	public void of_Files_asFiles_Rename_Null() throws IOException
	{
		// given
		File f = new File("test-resources/Thumbnailator/grid.png");
		
		try
		{
			// when
			Thumbnails.of(f)
				.size(50, 50)
				.asFiles((Rename)null);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("Rename is null.", e.getMessage());
			throw e;
		}
	}

	@Test(expected=NullPointerException.class)
	public void of_Files_toFiles_Rename_Null() throws IOException
	{
		// given
		File f = new File("test-resources/Thumbnailator/grid.png");
		
		try
		{
			// when
			Thumbnails.of(f)
				.size(50, 50)
				.toFiles((Rename)null);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("Rename is null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void of_Files_toFile_File_Null() throws IOException
	{
		// given
		File f = new File("test-resources/Thumbnailator/grid.png");
		
		try
		{
			// when
			Thumbnails.of(f)
				.size(50, 50)
				.toFile((File)null);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("File cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void of_Files_toFile_String_Null() throws IOException
	{
		// given
		File f = new File("test-resources/Thumbnailator/grid.png");
		
		try
		{
			// when
			Thumbnails.of(f)
				.size(50, 50)
				.toFile((String)null);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("File cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void of_Files_toOutputStream() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try
		{
			// when
			Thumbnails.of(img)
				.size(50, 50)
				.outputFormat("png")
				.toOutputStream((OutputStream)null);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("OutputStream cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void of_Files_toOutputStreams_Iterable_Null() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try
		{
			// when
			Thumbnails.of(img)
				.size(50, 50)
				.outputFormat("png")
				.toOutputStreams((Iterable<OutputStream>)null);
		}
		catch (NullPointerException e)
		{
			// then
			assertEquals("OutputStream iterable is null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void of_Files_toOutputStreams_Iterable_Empty() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try
		{
			// when
			Thumbnails.of(img)
				.size(50, 50)
				.outputFormat("png")
				.toOutputStreams(Collections.<OutputStream>emptyList());
		}
		catch (IndexOutOfBoundsException e)
		{
			// then
			assertEquals("Not enough file names provided by iterator.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void of_Files_toOutputStreams_Iterable_NotEnough() throws IOException
	{
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		OutputStream os = mock(OutputStream.class);
		
		try
		{
			// when
			Thumbnails.of(img, img)
				.size(50, 50)
				.outputFormat("png")
				.toOutputStreams(Arrays.asList(os));
		}
		catch (IndexOutOfBoundsException e)
		{
			// then
			assertEquals("Not enough file names provided by iterator.", e.getMessage());
			
			verify(os, atLeastOnce()).write(any(byte[].class), anyInt(), anyInt());
			throw e;
		}
	}
}