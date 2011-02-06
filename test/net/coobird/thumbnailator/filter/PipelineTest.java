package net.coobird.thumbnailator.filter;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import org.junit.Test;

import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.filters.Pipeline;

import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link Pipeline} class.
 * 
 * @author coobird
 *
 */
public class PipelineTest
{
	@Test
	public void singleFilter_ArrayConstructor() 
	{
		// given
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		
		ImageFilter filter1 = mock(ImageFilter.class);
		
		Pipeline pipeline = new Pipeline(filter1);
		
		// when
		pipeline.apply(img);
		
		// then
		verify(filter1).apply(any(BufferedImage.class));
	}
	
	@Test
	public void singleFilter_ListConstructor() 
	{
		// given
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		
		ImageFilter filter1 = mock(ImageFilter.class);
		
		Pipeline pipeline = new Pipeline(Arrays.asList(filter1));
		
		// when
		pipeline.apply(img);
		
		// then
		verify(filter1).apply(any(BufferedImage.class));
	}
	
	@Test
	public void singleFilter_EmptyConstructor_Add() 
	{
		// given
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		
		ImageFilter filter1 = mock(ImageFilter.class);
		
		Pipeline pipeline = new Pipeline();
		pipeline.add(filter1);
		
		// when
		pipeline.apply(img);
		
		// then
		verify(filter1).apply(any(BufferedImage.class));
	}
	
	@Test
	public void singleFilter_EmptyConstructor_AddAll() 
	{
		// given
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		
		ImageFilter filter1 = mock(ImageFilter.class);
		
		Pipeline pipeline = new Pipeline();
		pipeline.addAll(Arrays.asList(filter1));
		
		// when
		pipeline.apply(img);
		
		// then
		verify(filter1).apply(any(BufferedImage.class));
	}
	
	@Test
	public void multipleFilter_ArrayConstructor() 
	{
		// given
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		
		ImageFilter filter1 = mock(ImageFilter.class);
		ImageFilter filter2 = mock(ImageFilter.class);
		
		Pipeline pipeline = new Pipeline(filter1, filter2);
		
		// when
		pipeline.apply(img);
		
		// then
		verify(filter1).apply(any(BufferedImage.class));
		verify(filter2).apply(any(BufferedImage.class));
	}
	
	@Test
	public void multipleFilter_ListConstructor() 
	{
		// given
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		
		ImageFilter filter1 = mock(ImageFilter.class);
		ImageFilter filter2 = mock(ImageFilter.class);
		
		Pipeline pipeline = new Pipeline(Arrays.asList(filter1, filter2));
		
		// when
		pipeline.apply(img);
		
		// then
		verify(filter1).apply(any(BufferedImage.class));
		verify(filter2).apply(any(BufferedImage.class));
	}
	
	@Test
	public void multipleFilter_EmptyConstructor_Add() 
	{
		// given
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		
		ImageFilter filter1 = mock(ImageFilter.class);
		ImageFilter filter2 = mock(ImageFilter.class);
		
		Pipeline pipeline = new Pipeline();
		pipeline.add(filter1);
		pipeline.add(filter2);
		
		// when
		pipeline.apply(img);
		
		// then
		verify(filter1).apply(any(BufferedImage.class));
		verify(filter2).apply(any(BufferedImage.class));
	}
	
	@Test
	public void multipleFilter_EmptyConstructor_AddAll() 
	{
		// given
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		
		ImageFilter filter1 = mock(ImageFilter.class);
		ImageFilter filter2 = mock(ImageFilter.class);
		
		Pipeline pipeline = new Pipeline();
		pipeline.addAll(Arrays.asList(filter1, filter2));
		
		// when
		pipeline.apply(img);
		
		// then
		verify(filter1).apply(any(BufferedImage.class));
		verify(filter2).apply(any(BufferedImage.class));
	}
}
