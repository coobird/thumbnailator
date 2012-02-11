package net.coobird.thumbnailator.name;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.builders.ThumbnailParameterBuilder;

import org.junit.Test;
import static org.junit.Assert.*;

public class RenameTest
{
	@Test
	public void renameNoChange_NameGiven_ParamNull()
	{
		// given
		String name = "filename";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.NO_CHANGE.apply(name, param);
		
		// then
		assertEquals("filename", filename);
	}
	
	@Test
	public void renameNoChange_NameGiven_ParamGiven()
	{
		// given
		String name = "filename";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.NO_CHANGE.apply(name, param);
		
		// then
		assertEquals("filename", filename);
	}
	
	@Test
	public void renamePrefixDotThumbnail_NameGiven_ParamNull()
	{
		// given
		String name = "filename";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.PREFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail.filename", filename);
	}
	
	@Test
	public void renamePrefixDotThumbnail_NameGiven_ParamGiven()
	{
		// given
		String name = "filename";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.PREFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail.filename", filename);
	}
	
	@Test
	public void renamePrefixHyphenThumbnail_NameGiven_ParamNull()
	{
		// given
		String name = "filename";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.PREFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail-filename", filename);
	}
	
	@Test
	public void renamePrefixHyphenThumbnail_NameGiven_ParamGiven()
	{
		// given
		String name = "filename";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.PREFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail-filename", filename);
	}
	
	@Test
	public void renameSuffixDotThumbnail_NameGiven_ParamNull()
	{
		// given
		String name = "filename";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.SUFFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename.thumbnail", filename);
	}
	
	@Test
	public void renameSuffixDotThumbnail_NameGiven_ParamGiven()
	{
		// given
		String name = "filename";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.SUFFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename.thumbnail", filename);
	}
	
	@Test
	public void renameSuffixHyphenThumbnail_NameGiven_ParamNull()
	{
		// given
		String name = "filename";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.SUFFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename-thumbnail", filename);
	}
	
	@Test
	public void renameSuffixHyphenThumbnail_NameGiven_ParamGiven()
	{
		// given
		String name = "filename";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.SUFFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename-thumbnail", filename);
	}
	
	@Test
	public void renameNoChange_NameGiven_ParamNull_WithExtension()
	{
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.NO_CHANGE.apply(name, param);
		
		// then
		assertEquals("filename.jpg", filename);
	}
	
	@Test
	public void renameNoChange_NameGiven_ParamGiven_WithExtension()
	{
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.NO_CHANGE.apply(name, param);
		
		// then
		assertEquals("filename.jpg", filename);
	}
	
	@Test
	public void renamePrefixDotThumbnail_NameGiven_ParamNull_WithExtension()
	{
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.PREFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail.filename.jpg", filename);
	}
	
	@Test
	public void renamePrefixDotThumbnail_NameGiven_ParamGiven_WithExtension()
	{
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.PREFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail.filename.jpg", filename);
	}
	
	@Test
	public void renamePrefixHyphenThumbnail_NameGiven_ParamNull_WithExtension()
	{
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.PREFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail-filename.jpg", filename);
	}
	
	@Test
	public void renamePrefixHyphenThumbnail_NameGiven_ParamGiven_WithExtension()
	{
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.PREFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("thumbnail-filename.jpg", filename);
	}
	
	@Test
	public void renameSuffixDotThumbnail_NameGiven_ParamNull_WithExtension()
	{
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.SUFFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename.thumbnail.jpg", filename);
	}
	
	@Test
	public void renameSuffixDotThumbnail_NameGiven_ParamGiven_WithExtension()
	{
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.SUFFIX_DOT_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename.thumbnail.jpg", filename);
	}
	
	@Test
	public void renameSuffixHyphenThumbnail_NameGiven_ParamNull_WithExtension()
	{
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = null;
		
		// when
		String filename = Rename.SUFFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename-thumbnail.jpg", filename);
	}
	
	@Test
	public void renameSuffixHyphenThumbnail_NameGiven_ParamGiven_WithExtension()
	{
		// given
		String name = "filename.jpg";
		ThumbnailParameter param = new ThumbnailParameterBuilder().scale(1.0).build();
		
		// when
		String filename = Rename.SUFFIX_HYPHEN_THUMBNAIL.apply(name, param);
		
		// then
		assertEquals("filename-thumbnail.jpg", filename);
	}
}
