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

package net.coobird.thumbnailator.name;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.builders.ThumbnailParameterBuilder;

import org.junit.Test;
import static org.junit.Assert.*;

public class RenameTest {

	@Test
	public void renameNoChange_NameGiven_ParamNull() {
		// given
		String name = "filename";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.NO_CHANGE.apply(name, param);
		
		// then
		assertEquals("filename", filename);
	}
	
	@Test
	public void renameNoChange_NameGiven_ParamGiven() {
		// given
		String name = "filename";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.NO_CHANGE.apply(name, param);
		
		// then
		assertEquals("filename", filename);
	}
	
	@Test
	public void renamePrefixDotThumbnail_NameGiven_ParamNull() {
		// given
		String name = "filename";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.PREFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail.filename", filename);
	}
	
	@Test
	public void renamePrefixDotThumbnail_NameGiven_ParamGiven() {
		// given
		String name = "filename";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.PREFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail.filename", filename);
	}
	
	@Test
	public void renamePrefixHyphenThumbnail_NameGiven_ParamNull() {
		// given
		String name = "filename";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.PREFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail-filename", filename);
	}
	
	@Test
	public void renamePrefixHyphenThumbnail_NameGiven_ParamGiven() {
		// given
		String name = "filename";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.PREFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail-filename", filename);
	}
	
	@Test
	public void renameSuffixDotThumbnail_NameGiven_ParamNull() {
		// given
		String name = "filename";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.SUFFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename.thumbnail", filename);
	}
	
	@Test
	public void renameSuffixDotThumbnail_NameGiven_ParamGiven() {
		// given
		String name = "filename";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.SUFFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename.thumbnail", filename);
	}
	
	@Test
	public void renameSuffixHyphenThumbnail_NameGiven_ParamNull() {
		// given
		String name = "filename";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.SUFFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename-thumbnail", filename);
	}
	
	@Test
	public void renameSuffixHyphenThumbnail_NameGiven_ParamGiven() {
		// given
		String name = "filename";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.SUFFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename-thumbnail", filename);
	}
	
	@Test
	public void renameNoChange_NameGiven_ParamNull_WithExtension() {
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.NO_CHANGE.apply(name, param);
		
		// then
		assertEquals("filename.jpg", filename);
	}
	
	@Test
	public void renameNoChange_NameGiven_ParamGiven_WithExtension() {
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.NO_CHANGE.apply(name, param);
		
		// then
		assertEquals("filename.jpg", filename);
	}
	
	@Test
	public void renamePrefixDotThumbnail_NameGiven_ParamNull_WithExtension() {
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.PREFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail.filename.jpg", filename);
	}
	
	@Test
	public void renamePrefixDotThumbnail_NameGiven_ParamGiven_WithExtension() {
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.PREFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail.filename.jpg", filename);
	}
	
	@Test
	public void renamePrefixHyphenThumbnail_NameGiven_ParamNull_WithExtension() {
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.PREFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail-filename.jpg", filename);
	}
	
	@Test
	public void renamePrefixHyphenThumbnail_NameGiven_ParamGiven_WithExtension() {
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.PREFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail-filename.jpg", filename);
	}
	
	@Test
	public void renameSuffixDotThumbnail_NameGiven_ParamNull_WithExtension() {
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.SUFFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename.thumbnail.jpg", filename);
	}
	
	@Test
	public void renameSuffixDotThumbnail_NameGiven_ParamGiven_WithExtension() {
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.SUFFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename.thumbnail.jpg", filename);
	}
	
	@Test
	public void renameSuffixHyphenThumbnail_NameGiven_ParamNull_WithExtension() {
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.SUFFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename-thumbnail.jpg", filename);
	}
	
	@Test
	public void renameSuffixHyphenThumbnail_NameGiven_ParamGiven_WithExtension() {
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.SUFFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename-thumbnail.jpg", filename);
	}
	
	@Test
	public void renameNoChange_NameGiven_ParamNull_WithMultipleDots() {
		// given
		String name = "filename.middle.jpg";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.NO_CHANGE.apply(name, param);
		
		// then
		assertEquals("filename.middle.jpg", filename);
	}
	
	@Test
	public void renameNoChange_NameGiven_ParamGiven_WithMultipleDots() {
		// given
		String name = "filename.middle.jpg";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.NO_CHANGE.apply(name, param);
		
		// then
		assertEquals("filename.middle.jpg", filename);
	}
	
	@Test
	public void renamePrefixDotThumbnail_NameGiven_ParamNull_WithMultipleDots() {
		// given
		String name = "filename.middle.jpg";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.PREFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail.filename.middle.jpg", filename);
	}
	
	@Test
	public void renamePrefixDotThumbnail_NameGiven_ParamGiven_WithMultipleDots() {
		// given
		String name = "filename.middle.jpg";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.PREFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail.filename.middle.jpg", filename);
	}
	
	@Test
	public void renamePrefixHyphenThumbnail_NameGiven_ParamNull_WithMultipleDots() {
		// given
		String name = "filename.middle.jpg";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.PREFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail-filename.middle.jpg", filename);
	}
	
	@Test
	public void renamePrefixHyphenThumbnail_NameGiven_ParamGiven_WithMultipleDots() {
		// given
		String name = "filename.middle.jpg";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.PREFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail-filename.middle.jpg", filename);
	}
	
	@Test
	public void renameSuffixDotThumbnail_NameGiven_ParamNull_WithMultipleDots() {
		// given
		String name = "filename.middle.jpg";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.SUFFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename.middle.thumbnail.jpg", filename);
	}
	
	@Test
	public void renameSuffixDotThumbnail_NameGiven_ParamGiven_WithMultipleDots() {
		// given
		String name = "filename.middle.jpg";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.SUFFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename.middle.thumbnail.jpg", filename);
	}
	
	@Test
	public void renameSuffixHyphenThumbnail_NameGiven_ParamNull_WithMultipleDots() {
		// given
		String name = "filename.middle.jpg";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.SUFFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename.middle-thumbnail.jpg", filename);
	}
	
	@Test
	public void renameSuffixHyphenThumbnail_NameGiven_ParamGiven_WithMultipleDots() {
		// given
		String name = "filename.middle.jpg";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.SUFFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename.middle-thumbnail.jpg", filename);
	}
}
