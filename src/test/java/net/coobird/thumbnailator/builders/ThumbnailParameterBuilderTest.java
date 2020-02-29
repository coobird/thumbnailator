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

package net.coobird.thumbnailator.builders;

import static org.junit.Assert.*;

import java.awt.Dimension;
import java.util.Collections;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.resizers.Resizer;
import net.coobird.thumbnailator.resizers.ResizerFactory;
import net.coobird.thumbnailator.resizers.Resizers;

import org.junit.Test;

public class ThumbnailParameterBuilderTest {

	/**
	 * Test for the {@link ThumbnailParameterBuilder#build()} method, where
	 * <ol>
	 * <li>No methods on the builder is called</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalStateException occurs</li>
	 * </ol>
	 */
	@Test(expected=IllegalStateException.class)
	public void build_NothingSet() {
		new ThumbnailParameterBuilder().build();
		
		fail();
	}
	
	/**
	 * Test for the {@link ThumbnailParameterBuilder#build()} method, where
	 * <ol>
	 * <li>Only the size(int,int) is set</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A ThumbnailParameter with default values is built.</li>
	 * </ol>
	 */
	@Test
	public void build_OnlySize_IntInt() {
		ThumbnailParameter param = new ThumbnailParameterBuilder()
				.size(100, 100)
				.build();

		assertEquals(new Dimension(100, 100), param.getSize());
		assertTrue(Double.isNaN(param.getHeightScalingFactor()));
		assertTrue(Double.isNaN(param.getWidthScalingFactor()));
		assertEquals(ThumbnailParameter.ORIGINAL_FORMAT, param.getOutputFormat());
		assertEquals(ThumbnailParameter.DEFAULT_FORMAT_TYPE, param.getOutputFormatType());
		assertTrue(Float.isNaN(param.getOutputQuality()));
		assertEquals(Resizers.PROGRESSIVE, param.getResizer());
		assertEquals(ThumbnailParameter.DEFAULT_IMAGE_TYPE, param.getType());
		assertEquals(Collections.emptyList(), param.getImageFilters());
	}
	
	/**
	 * Test for the {@link ThumbnailParameterBuilder#build()} method, where
	 * <ol>
	 * <li>Only the size(Dimension) is set</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A ThumbnailParameter with default values is built.</li>
	 * </ol>
	 */
	@Test
	public void build_OnlySize_Dimension() {
		ThumbnailParameter param = new ThumbnailParameterBuilder()
				.size(new Dimension(100, 100))
				.build();
		
		assertEquals(new Dimension(100, 100), param.getSize());
		assertTrue(Double.isNaN(param.getWidthScalingFactor()));
		assertTrue(Double.isNaN(param.getHeightScalingFactor()));
		assertEquals(ThumbnailParameter.ORIGINAL_FORMAT, param.getOutputFormat());
		assertEquals(ThumbnailParameter.DEFAULT_FORMAT_TYPE, param.getOutputFormatType());
		assertTrue(Float.isNaN(param.getOutputQuality()));
		assertEquals(Resizers.PROGRESSIVE, param.getResizer());
		assertEquals(ThumbnailParameter.DEFAULT_IMAGE_TYPE, param.getType());
		assertEquals(Collections.emptyList(), param.getImageFilters());
	}
	
	/**
	 * Test for the {@link ThumbnailParameterBuilder#build()} method, where
	 * <ol>
	 * <li>Only the scale(double) is set</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A ThumbnailParameter with default values is built.</li>
	 * </ol>
	 */
	@Test
	public void build_OnlyScale() {
		ThumbnailParameter param = new ThumbnailParameterBuilder()
				.scale(0.5)
				.build();
		
		assertEquals(null, param.getSize());
		assertTrue(Double.compare(0.5, param.getWidthScalingFactor()) == 0);
		assertTrue(Double.compare(0.5, param.getHeightScalingFactor()) == 0);
		assertEquals(ThumbnailParameter.ORIGINAL_FORMAT, param.getOutputFormat());
		assertEquals(ThumbnailParameter.DEFAULT_FORMAT_TYPE, param.getOutputFormatType());
		assertTrue(Float.isNaN(param.getOutputQuality()));
		assertEquals(Resizers.PROGRESSIVE, param.getResizer());
		assertEquals(ThumbnailParameter.DEFAULT_IMAGE_TYPE, param.getType());
		assertEquals(Collections.emptyList(), param.getImageFilters());
	}
	
	/**
	 * Test for the {@link ThumbnailParameterBuilder#build()} method, where
	 * <ol>
	 * <li>Only the scale(double) is set with Double.NaN</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalArgumentException is thrown.</li>
	 * </ol>
	 */
	@Test(expected=IllegalArgumentException.class)
	public void build_OnlyScale_NaN() {
		new ThumbnailParameterBuilder().scale(Double.NaN).build();
		fail();
	}
	
	/**
	 * Test for the {@link ThumbnailParameterBuilder#build()} method, where
	 * <ol>
	 * <li>Only the scale(double) is set with Double.POSITIVE_INFINITY</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalArgumentException is thrown.</li>
	 * </ol>
	 */
	@Test(expected=IllegalArgumentException.class)
	public void build_OnlyScale_PositiveInfinity() {
		new ThumbnailParameterBuilder().scale(Double.POSITIVE_INFINITY).build();
		fail();
	}
	
	/**
	 * Test for the {@link ThumbnailParameterBuilder#build()} method, where
	 * <ol>
	 * <li>Only the scale(double) is set with Double.NEGATIVE_INFINITY</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>An IllegalArgumentException is thrown.</li>
	 * </ol>
	 */
	@Test(expected=IllegalArgumentException.class)
	public void build_OnlyScale_NegativeInfinity() {
		new ThumbnailParameterBuilder().scale(Double.NEGATIVE_INFINITY).build();
		fail();
	}

	@Test
	public void build_OnlyScaleTwoArg() {
		// given, when
		ThumbnailParameter param =
			new ThumbnailParameterBuilder().scale(0.6, 0.4).build();
		
		// then
		assertEquals(null, param.getSize());
		assertTrue(Double.compare(0.6, param.getWidthScalingFactor()) == 0);
		assertTrue(Double.compare(0.4, param.getHeightScalingFactor()) == 0);
		assertEquals(ThumbnailParameter.ORIGINAL_FORMAT, param.getOutputFormat());
		assertEquals(ThumbnailParameter.DEFAULT_FORMAT_TYPE, param.getOutputFormatType());
		assertTrue(Float.isNaN(param.getOutputQuality()));
		assertEquals(Resizers.PROGRESSIVE, param.getResizer());
		assertEquals(ThumbnailParameter.DEFAULT_IMAGE_TYPE, param.getType());
		assertEquals(Collections.emptyList(), param.getImageFilters());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void build_OnlyScaleTwoArg_NaN_Valid() {
		new ThumbnailParameterBuilder().scale(Double.NaN, 0.4).build();
		fail();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void build_OnlyScaleTwoArg_Valid_NaN() {
		new ThumbnailParameterBuilder().scale(0.6, Double.NaN).build();
		fail();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void build_OnlyScaleTwoArg_NaN_NaN() {
		new ThumbnailParameterBuilder().scale(Double.NaN, Double.NaN).build();
		fail();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void build_OnlyScaleTwoArg_PositiveInfinity_Valid() {
		new ThumbnailParameterBuilder().scale(Double.POSITIVE_INFINITY, 0.4).build();
		fail();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void build_OnlyScaleTwoArg_Valid_PositiveInfinity() {
		new ThumbnailParameterBuilder().scale(0.6, Double.POSITIVE_INFINITY).build();
		fail();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void build_OnlyScaleTwoArg_PositiveInfinity_PositiveInfinity() {
		new ThumbnailParameterBuilder().scale(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).build();
		fail();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void build_OnlyScaleTwoArg_NegativeInfinity_Valid() {
		new ThumbnailParameterBuilder().scale(Double.NEGATIVE_INFINITY, 0.4).build();
		fail();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void build_OnlyScaleTwoArg_Valid_NegativeInfinity() {
		new ThumbnailParameterBuilder().scale(0.6, Double.NEGATIVE_INFINITY).build();
		fail();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void build_OnlyScaleTwoArg_NegativeInfinity_NegativeInfinity() {
		new ThumbnailParameterBuilder().scale(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).build();
		fail();
	}
	
	/**
	 * Test for the {@link ThumbnailParameterBuilder#build()} method, where
	 * <ol>
	 * <li>Where resizer is called with a specific Resizer</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A ThumbnailParameter will contain the specified Resizer.</li>
	 * </ol>
	 */
	@Test
	public void build_calledResizer_returnsGivenResizer() {
		ThumbnailParameter param = new ThumbnailParameterBuilder()
			.scale(0.5)
			.resizer(Resizers.BICUBIC)
			.build();
		
		assertEquals(Resizers.BICUBIC, param.getResizer());
	}
	
	/**
	 * Test for the {@link ThumbnailParameterBuilder#build()} method, where
	 * <ol>
	 * <li>Where resizer is called with a specific Resizer</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A ThumbnailParameter will contain a ResizerFactory which will return
	 * the specified Resizer.</li>
	 * </ol>
	 */
	@Test
	public void build_calledResizer_returnedResizerFactoryReturnsResizer() {
		ThumbnailParameter param = new ThumbnailParameterBuilder()
			.scale(0.5)
			.resizer(Resizers.BICUBIC)
			.build();
		
		assertEquals(Resizers.BICUBIC, param.getResizerFactory().getResizer());
	}
	
	/**
	 * Test for the {@link ThumbnailParameterBuilder#build()} method, where
	 * <ol>
	 * <li>Where resizerFactory is called with a specific ResizerFactory</li>
	 * </ol>
	 * and the expected outcome is,
	 * <ol>
	 * <li>A ThumbnailParameter will contain the specified ResizerFactory.</li>
	 * </ol>
	 */
	@Test
	public void build_calledResizerFactory() {
		ResizerFactory rf = new ResizerFactory() {
			public Resizer getResizer(Dimension arg0, Dimension arg1) {
				return null;
			}
			
			public Resizer getResizer() {
				return null;
			}
		};
		
		ThumbnailParameter param = new ThumbnailParameterBuilder()
			.scale(0.5)
			.resizerFactory(rf)
			.build();
		
		assertEquals(rf, param.getResizerFactory());
	}

}
