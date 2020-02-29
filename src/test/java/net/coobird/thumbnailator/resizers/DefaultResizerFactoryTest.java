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

package net.coobird.thumbnailator.resizers;

import static org.junit.Assert.*;

import java.awt.Dimension;

import org.junit.Test;

public class DefaultResizerFactoryTest {

	@Test
	public void defaultResizer() {
		// given
		Resizer resizer = Resizers.PROGRESSIVE;
		ResizerFactory factory = DefaultResizerFactory.getInstance();
		
		// when
		Resizer receivedResizer = factory.getResizer();

		// then
		assertEquals(resizer, receivedResizer);
	}
	
	@Test
	public void resizerWhereSourceSmallerThanDestination() {
		// given
		Resizer resizer = Resizers.BICUBIC;
		ResizerFactory factory = DefaultResizerFactory.getInstance();
		
		Dimension sourceDimension = new Dimension(100, 100);
		Dimension targetDimension = new Dimension(200, 200);
		
		// when
		Resizer receivedResizer = factory.getResizer(sourceDimension, targetDimension);
		
		// then
		assertEquals(resizer, receivedResizer);
	}
	
	@Test
	public void resizerWhereSourceLargerThanDestination_LessThan2Times() {
		// given
		Resizer resizer = Resizers.BILINEAR;
		ResizerFactory factory = DefaultResizerFactory.getInstance();
		
		Dimension sourceDimension = new Dimension(200, 200);
		Dimension targetDimension = new Dimension(150, 150);
		
		// when
		Resizer receivedResizer = factory.getResizer(sourceDimension, targetDimension);
		
		// then
		assertEquals(resizer, receivedResizer);
	}
	
	@Test
	public void resizerWhereSourceLargerThanDestination_2Times() {
		// given
		Resizer resizer = Resizers.BILINEAR;
		ResizerFactory factory = DefaultResizerFactory.getInstance();
		
		Dimension sourceDimension = new Dimension(200, 200);
		Dimension targetDimension = new Dimension(100, 100);
		
		// when
		Resizer receivedResizer = factory.getResizer(sourceDimension, targetDimension);
		
		// then
		assertEquals(resizer, receivedResizer);
	}
	
	@Test
	public void resizerWhereSourceLargerThanDestination_MoreThan2Times() {
		// given
		Resizer resizer = Resizers.PROGRESSIVE;
		ResizerFactory factory = DefaultResizerFactory.getInstance();
		
		Dimension sourceDimension = new Dimension(200, 200);
		Dimension targetDimension = new Dimension(50, 50);
		
		// when
		Resizer receivedResizer = factory.getResizer(sourceDimension, targetDimension);
		
		// then
		assertEquals(resizer, receivedResizer);
	}
	
	@Test
	public void resizerWhereSourceSameSizeAsDestination() {
		// given
		Resizer resizer = Resizers.NULL;
		ResizerFactory factory = DefaultResizerFactory.getInstance();
		
		Dimension sourceDimension = new Dimension(100, 100);
		Dimension targetDimension = new Dimension(100, 100);
		
		// when
		Resizer receivedResizer = factory.getResizer(sourceDimension, targetDimension);
		
		// then
		assertEquals(resizer, receivedResizer);
	}
	
	@Test
	public void resizerWhereSourceHeightLargerThanDestination() {
		// given
		Resizer resizer = Resizers.PROGRESSIVE;
		ResizerFactory factory = DefaultResizerFactory.getInstance();
		
		Dimension sourceDimension = new Dimension(100, 200);
		Dimension targetDimension = new Dimension(100, 100);
		
		// when
		Resizer receivedResizer = factory.getResizer(sourceDimension, targetDimension);
		
		// then
		assertEquals(resizer, receivedResizer);
	}
	
	@Test
	public void resizerWhereSourceHeightSmallerThanDestination() {
		// given
		Resizer resizer = Resizers.PROGRESSIVE;
		ResizerFactory factory = DefaultResizerFactory.getInstance();
		
		Dimension sourceDimension = new Dimension(100, 50);
		Dimension targetDimension = new Dimension(100, 100);
		
		// when
		Resizer receivedResizer = factory.getResizer(sourceDimension, targetDimension);
		
		// then
		assertEquals(resizer, receivedResizer);
	}
	
	@Test
	public void resizerWhereSourceWidthLargerThanDestination() {
		// given
		Resizer resizer = Resizers.PROGRESSIVE;
		ResizerFactory factory = DefaultResizerFactory.getInstance();
		
		Dimension sourceDimension = new Dimension(200, 100);
		Dimension targetDimension = new Dimension(100, 100);
		
		// when
		Resizer receivedResizer = factory.getResizer(sourceDimension, targetDimension);
		
		// then
		assertEquals(resizer, receivedResizer);
	}
	
	@Test
	public void resizerWhereSourceWidthSmallerThanDestination() {
		// given
		Resizer resizer = Resizers.PROGRESSIVE;
		ResizerFactory factory = DefaultResizerFactory.getInstance();
		
		Dimension sourceDimension = new Dimension(50, 100);
		Dimension targetDimension = new Dimension(100, 100);
		
		// when
		Resizer receivedResizer = factory.getResizer(sourceDimension, targetDimension);
		
		// then
		assertEquals(resizer, receivedResizer);
	}
}
