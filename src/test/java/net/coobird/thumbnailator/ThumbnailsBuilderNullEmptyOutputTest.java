/*
 * Thumbnailator - a thumbnail generation library
 *
 * Copyright (c) 2008-2020 Chris Kroells
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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

public class ThumbnailsBuilderNullEmptyOutputTest {

	@Test(expected=NullPointerException.class)
	public void asFiles_Iterable_Null() throws IOException {
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try {
			// when
			Thumbnails.of(img)
				.size(50, 50)
				.asFiles((Iterable<File>)null);
		} catch (NullPointerException e) {
			// then
			assertEquals("File name iterable is null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void asFiles_Iterable_Empty() throws IOException {
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try {
			// when
			Thumbnails.of(img)
				.size(50, 50)
				.asFiles(Collections.<File>emptyList());
		} catch (IndexOutOfBoundsException e) {
			// then
			assertEquals("Not enough file names provided by iterator.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void asFiles_Iterable_NotEnough() throws IOException {
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		File outFile = new File("src/test/resources/Thumbnailator/ofFilesNotEnough.png");
		outFile.deleteOnExit();
		
		try {
			// when
			Thumbnails.of(img, img)
				.size(50, 50)
				.asFiles(Arrays.asList(outFile));
		} catch (IndexOutOfBoundsException e) {
			// then
			assertEquals("Not enough file names provided by iterator.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void toFiles_Iterable_Null() throws IOException {
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try {
			// when
			Thumbnails.of(img)
				.size(50, 50)
				.toFiles((Iterable<File>)null);
		} catch (NullPointerException e) {
			// then
			assertEquals("File name iterable is null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void toFiles_Iterable_Empty() throws IOException {
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try {
			// when
			Thumbnails.of(img)
				.size(50, 50)
				.toFiles(Collections.<File>emptyList());
		} catch (IndexOutOfBoundsException e) {
			// then
			assertEquals("Not enough file names provided by iterator.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void toFiles_Iterable_NotEnough() throws IOException {
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		File outFile = new File("src/test/resources/Thumbnailator/ofFilesNotEnough.png");
		outFile.deleteOnExit();
		
		try {
			// when
			Thumbnails.of(img, img)
				.size(50, 50)
				.toFiles(Arrays.asList(outFile));
		} catch (IndexOutOfBoundsException e) {
			// then
			assertEquals("Not enough file names provided by iterator.", e.getMessage());
			throw e;
		}
	}

	@Test(expected=NullPointerException.class)
	public void asFiles_Rename_Null() throws IOException {
		// given
		File f = new File("src/test/resources/Thumbnailator/grid.png");
		
		try {
			// when
			Thumbnails.of(f)
				.size(50, 50)
				.asFiles((Rename)null);
		} catch (NullPointerException e) {
			// then
			assertEquals("Rename is null.", e.getMessage());
			throw e;
		}
	}

	@Test(expected=NullPointerException.class)
	public void toFiles_Rename_Null() throws IOException {
		// given
		File f = new File("src/test/resources/Thumbnailator/grid.png");
		
		try {
			// when
			Thumbnails.of(f)
				.size(50, 50)
				.toFiles((Rename)null);
		} catch (NullPointerException e) {
			// then
			assertEquals("Rename is null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void toFile_File_Null() throws IOException {
		// given
		File f = new File("src/test/resources/Thumbnailator/grid.png");
		
		try {
			// when
			Thumbnails.of(f)
				.size(50, 50)
				.toFile((File)null);
		} catch (NullPointerException e) {
			// then
			assertEquals("File cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void toFile_String_Null() throws IOException {
		// given
		File f = new File("src/test/resources/Thumbnailator/grid.png");
		
		try {
			// when
			Thumbnails.of(f)
				.size(50, 50)
				.toFile((String)null);
		} catch (NullPointerException e) {
			// then
			assertEquals("File cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void toOutputStream() throws IOException {
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try {
			// when
			Thumbnails.of(img)
				.size(50, 50)
				.outputFormat("png")
				.toOutputStream((OutputStream)null);
		} catch (NullPointerException e) {
			// then
			assertEquals("OutputStream cannot be null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=NullPointerException.class)
	public void toOutputStreams_Iterable_Null() throws IOException {
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try {
			// when
			Thumbnails.of(img)
				.size(50, 50)
				.outputFormat("png")
				.toOutputStreams((Iterable<OutputStream>)null);
		} catch (NullPointerException e) {
			// then
			assertEquals("OutputStream iterable is null.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void toOutputStreams_Iterable_Empty() throws IOException {
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		
		try {
			// when
			Thumbnails.of(img)
				.size(50, 50)
				.outputFormat("png")
				.toOutputStreams(Collections.<OutputStream>emptyList());
		} catch (IndexOutOfBoundsException e) {
			// then
			assertEquals("Not enough file names provided by iterator.", e.getMessage());
			throw e;
		}
	}
	
	@Test(expected=IndexOutOfBoundsException.class)
	public void toOutputStreams_Iterable_NotEnough() throws IOException {
		// given
		BufferedImage img = new BufferedImageBuilder(200, 200).build();
		OutputStream os = mock(OutputStream.class);
		
		try {
			// when
			Thumbnails.of(img, img)
				.size(50, 50)
				.outputFormat("png")
				.toOutputStreams(Arrays.asList(os));
		} catch (IndexOutOfBoundsException e) {
			// then
			assertEquals("Not enough file names provided by iterator.", e.getMessage());
			
			verify(os, atLeastOnce()).write(any(byte[].class), anyInt(), anyInt());
			throw e;
		}
	}
}