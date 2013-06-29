package net.coobird.thumbnailator.resizers;

import static org.junit.Assert.*;

import java.awt.Dimension;

import org.junit.Test;

public class FixedResizerFactoryTest
{
	@Test
	public void defaultResizer()
	{
		// given
		Resizer resizer = Resizers.BICUBIC;
		ResizerFactory factory = new FixedResizerFactory(resizer);
		
		// when
		Resizer receivedResizer = factory.getResizer();

		// then
		assertEquals(resizer, receivedResizer);
	}
	
	@Test
	public void resizerWhereSourceSmallerThanDestination()
	{
		// given
		Resizer resizer = Resizers.BICUBIC;
		ResizerFactory factory = new FixedResizerFactory(resizer);
		
		Dimension sourceDimension = new Dimension(100, 100);
		Dimension targetDimension = new Dimension(200, 200);
		
		// when
		Resizer receivedResizer = factory.getResizer(sourceDimension, targetDimension);
		
		// then
		assertEquals(resizer, receivedResizer);
	}
	
	@Test
	public void resizerWhereSourceLargerThanDestination()
	{
		// given
		Resizer resizer = Resizers.BICUBIC;
		ResizerFactory factory = new FixedResizerFactory(resizer);
		
		Dimension sourceDimension = new Dimension(200, 200);
		Dimension targetDimension = new Dimension(100, 100);
		
		// when
		Resizer receivedResizer = factory.getResizer(sourceDimension, targetDimension);
		
		// then
		assertEquals(resizer, receivedResizer);
	}
	
	@Test
	public void resizerWhereSourceSameSizeAsDestination()
	{
		// given
		Resizer resizer = Resizers.BICUBIC;
		ResizerFactory factory = new FixedResizerFactory(resizer);
		
		Dimension sourceDimension = new Dimension(100, 100);
		Dimension targetDimension = new Dimension(100, 100);
		
		// when
		Resizer receivedResizer = factory.getResizer(sourceDimension, targetDimension);
		
		// then
		assertEquals(resizer, receivedResizer);
	}
	
	@Test
	public void resizerWhereSourceHeightLargerThanDestination()
	{
		// given
		Resizer resizer = Resizers.BICUBIC;
		ResizerFactory factory = new FixedResizerFactory(resizer);
		
		Dimension sourceDimension = new Dimension(100, 200);
		Dimension targetDimension = new Dimension(100, 100);
		
		// when
		Resizer receivedResizer = factory.getResizer(sourceDimension, targetDimension);
		
		// then
		assertEquals(resizer, receivedResizer);
	}
	
	@Test
	public void resizerWhereSourceHeightSmallerThanDestination()
	{
		// given
		Resizer resizer = Resizers.BICUBIC;
		ResizerFactory factory = new FixedResizerFactory(resizer);
		
		Dimension sourceDimension = new Dimension(100, 50);
		Dimension targetDimension = new Dimension(100, 100);
		
		// when
		Resizer receivedResizer = factory.getResizer(sourceDimension, targetDimension);
		
		// then
		assertEquals(resizer, receivedResizer);
	}
	
	@Test
	public void resizerWhereSourceWidthLargerThanDestination()
	{
		// given
		Resizer resizer = Resizers.BICUBIC;
		ResizerFactory factory = new FixedResizerFactory(resizer);
		
		Dimension sourceDimension = new Dimension(200, 100);
		Dimension targetDimension = new Dimension(100, 100);
		
		// when
		Resizer receivedResizer = factory.getResizer(sourceDimension, targetDimension);
		
		// then
		assertEquals(resizer, receivedResizer);
	}
	
	@Test
	public void resizerWhereSourceWidthSmallerThanDestination()
	{
		// given
		Resizer resizer = Resizers.BICUBIC;
		ResizerFactory factory = new FixedResizerFactory(resizer);
		
		Dimension sourceDimension = new Dimension(50, 100);
		Dimension targetDimension = new Dimension(100, 100);
		
		// when
		Resizer receivedResizer = factory.getResizer(sourceDimension, targetDimension);
		
		// then
		assertEquals(resizer, receivedResizer);
	}
}
