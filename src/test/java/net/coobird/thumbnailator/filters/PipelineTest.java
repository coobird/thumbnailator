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
public class PipelineTest {

	@Test
	public void singleFilter_ArrayConstructor() {
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
	public void singleFilter_ListConstructor() {
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
	public void singleFilter_EmptyConstructor_Add() {
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
	public void singleFilter_EmptyConstructor_AddAll() {
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
	public void multipleFilter_ArrayConstructor() {
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
	public void multipleFilter_ListConstructor() {
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
	public void multipleFilter_EmptyConstructor_Add() {
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
	public void multipleFilter_EmptyConstructor_AddAll() {
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
	public void filterOrderForTwo() {
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
	public void filterOrderForTwoWithAddFirstForFirst() {
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
	public void filterOrderForTwoWithAddFirstForSecond() {
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
	public void filterOrderForThree() {
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
	public void filterOrderForThreeWithAddFirstForFirst() {
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
	public void filterOrderForThreeWithAddFirstForSecond() {
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
	public void filterOrderForThreeWithAddFirstForThird() {
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
	
	@Test
	public void getFiltersWhereFiltersGivenByListConstructor() {
		// given
		ImageFilter filter1 = mock(ImageFilter.class);
		ImageFilter filter2 = mock(ImageFilter.class);

		List<ImageFilter> filters = new ArrayList<ImageFilter>();
		filters.add(filter1);
		filters.add(filter2);
		
		Pipeline pipeline = new Pipeline(filters);
		
		// when
		List<ImageFilter> returned = pipeline.getFilters();
		
		// then
		assertEquals(filters, returned);
	}
	
	@Test
	public void getFiltersWhereFiltersGivenByVarargsConstructor() {
		// given
		ImageFilter filter1 = mock(ImageFilter.class);
		ImageFilter filter2 = mock(ImageFilter.class);
		
		Pipeline pipeline = new Pipeline(filter1, filter2);
		
		// when
		List<ImageFilter> returned = pipeline.getFilters();
		
		// then
		assertEquals(Arrays.asList(filter1, filter2), returned);
	}
	
	@Test
	public void getFiltersWhereFiltersGivenByNoArgsConstructor() {
		// given
		Pipeline pipeline = new Pipeline();
		
		// when
		List<ImageFilter> returned = pipeline.getFilters();
		
		// then
		assertEquals(Arrays.asList(), returned);
	}
	
	@Test
	public void getFiltersWhereFiltersAddedByAdd() {
		// given
		ImageFilter filter1 = mock(ImageFilter.class);
		ImageFilter filter2 = mock(ImageFilter.class);
		
		Pipeline pipeline = new Pipeline();
		pipeline.add(filter1);
		pipeline.add(filter2);
		
		// when
		List<ImageFilter> returned = pipeline.getFilters();
		
		// then
		assertEquals(Arrays.asList(filter1, filter2), returned);
	}
	
	@Test
	public void getFiltersWhereFiltersAddedByAddAll() {
		// given
		ImageFilter filter1 = mock(ImageFilter.class);
		ImageFilter filter2 = mock(ImageFilter.class);
		
		Pipeline pipeline = new Pipeline();
		pipeline.addAll(Arrays.asList(filter1, filter2));
		
		// when
		List<ImageFilter> returned = pipeline.getFilters();
		
		// then
		assertEquals(Arrays.asList(filter1, filter2), returned);
	}
	
	@Test
	public void getFiltersWhereFiltersAddedByAddFirst() {
		// given
		ImageFilter filter1 = mock(ImageFilter.class);
		ImageFilter filter2 = mock(ImageFilter.class);
		
		Pipeline pipeline = new Pipeline();
		pipeline.add(filter1);
		pipeline.addFirst(filter2);
		
		// when
		List<ImageFilter> returned = pipeline.getFilters();
		
		// then
		assertEquals(Arrays.asList(filter2, filter1), returned);
	}
}
