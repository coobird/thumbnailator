package net.coobird.thumbnailator.filters;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

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
	
	@Test
	public void filterOrderForTwo()
	{
		// given
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		final List<Integer> order = new ArrayList<Integer>();
		
		ImageFilter one = new ImageFilter() {
			public BufferedImage apply(BufferedImage img) {
				order.add(1);
				return img;
			}
		};
		
		ImageFilter two = new ImageFilter() {
			public BufferedImage apply(BufferedImage img) {
				order.add(2);
				return img;
			}
		};
		
		Pipeline pipeline = new Pipeline();
		pipeline.add(one);
		pipeline.add(two);
		
		// when
		pipeline.apply(img);
		
		// then
		assertEquals(Arrays.asList(1, 2), order);
	}
	
	@Test
	public void filterOrderForTwoWithAddFirstForFirst()
	{
		// given
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		final List<Integer> order = new ArrayList<Integer>();
		
		ImageFilter one = new ImageFilter() {
			public BufferedImage apply(BufferedImage img) {
				order.add(1);
				return img;
			}
		};
		
		ImageFilter two = new ImageFilter() {
			public BufferedImage apply(BufferedImage img) {
				order.add(2);
				return img;
			}
		};
		
		Pipeline pipeline = new Pipeline();
		pipeline.addFirst(one);
		pipeline.add(two);
		
		// when
		pipeline.apply(img);
		
		// then
		assertEquals(Arrays.asList(1, 2), order);
	}
	
	@Test
	public void filterOrderForTwoWithAddFirstForSecond()
	{
		// given
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		final List<Integer> order = new ArrayList<Integer>();
		
		ImageFilter one = new ImageFilter() {
			public BufferedImage apply(BufferedImage img) {
				order.add(1);
				return img;
			}
		};
		
		ImageFilter two = new ImageFilter() {
			public BufferedImage apply(BufferedImage img) {
				order.add(2);
				return img;
			}
		};
		
		Pipeline pipeline = new Pipeline();
		pipeline.add(one);
		pipeline.addFirst(two);
		
		// when
		pipeline.apply(img);
		
		// then
		assertEquals(Arrays.asList(2, 1), order);
	}
	
	@Test
	public void filterOrderForThree()
	{
		// given
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		final List<Integer> order = new ArrayList<Integer>();
		
		ImageFilter one = new ImageFilter() {
			public BufferedImage apply(BufferedImage img) {
				order.add(1);
				return img;
			}
		};
		
		ImageFilter two = new ImageFilter() {
			public BufferedImage apply(BufferedImage img) {
				order.add(2);
				return img;
			}
		};
		
		ImageFilter three = new ImageFilter() {
			public BufferedImage apply(BufferedImage img) {
				order.add(3);
				return img;
			}
		};
		
		Pipeline pipeline = new Pipeline();
		pipeline.add(one);
		pipeline.add(two);
		pipeline.add(three);
		
		// when
		pipeline.apply(img);
		
		// then
		assertEquals(Arrays.asList(1, 2, 3), order);
	}
	
	@Test
	public void filterOrderForThreeWithAddFirstForFirst()
	{
		// given
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		final List<Integer> order = new ArrayList<Integer>();
		
		ImageFilter one = new ImageFilter() {
			public BufferedImage apply(BufferedImage img) {
				order.add(1);
				return img;
			}
		};
		
		ImageFilter two = new ImageFilter() {
			public BufferedImage apply(BufferedImage img) {
				order.add(2);
				return img;
			}
		};
		
		ImageFilter three = new ImageFilter() {
			public BufferedImage apply(BufferedImage img) {
				order.add(3);
				return img;
			}
		};
		
		Pipeline pipeline = new Pipeline();
		pipeline.addFirst(one);
		pipeline.add(two);
		pipeline.add(three);
		
		// when
		pipeline.apply(img);
		
		// then
		assertEquals(Arrays.asList(1, 2, 3), order);
	}
	
	@Test
	public void filterOrderForThreeWithAddFirstForSecond()
	{
		// given
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		final List<Integer> order = new ArrayList<Integer>();
		
		ImageFilter one = new ImageFilter() {
			public BufferedImage apply(BufferedImage img) {
				order.add(1);
				return img;
			}
		};
		
		ImageFilter two = new ImageFilter() {
			public BufferedImage apply(BufferedImage img) {
				order.add(2);
				return img;
			}
		};
		
		ImageFilter three = new ImageFilter() {
			public BufferedImage apply(BufferedImage img) {
				order.add(3);
				return img;
			}
		};
		
		Pipeline pipeline = new Pipeline();
		pipeline.add(one);
		pipeline.addFirst(two);
		pipeline.add(three);
		
		// when
		pipeline.apply(img);
		
		// then
		assertEquals(Arrays.asList(2, 1, 3), order);
	}
	
	@Test
	public void filterOrderForThreeWithAddFirstForThird()
	{
		// given
		BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
		final List<Integer> order = new ArrayList<Integer>();
		
		ImageFilter one = new ImageFilter() {
			public BufferedImage apply(BufferedImage img) {
				order.add(1);
				return img;
			}
		};
		
		ImageFilter two = new ImageFilter() {
			public BufferedImage apply(BufferedImage img) {
				order.add(2);
				return img;
			}
		};
		
		ImageFilter three = new ImageFilter() {
			public BufferedImage apply(BufferedImage img) {
				order.add(3);
				return img;
			}
		};
		
		Pipeline pipeline = new Pipeline();
		pipeline.add(one);
		pipeline.add(two);
		pipeline.addFirst(three);
		
		// when
		pipeline.apply(img);
		
		// then
		assertEquals(Arrays.asList(3, 1, 2), order);
	}
}
