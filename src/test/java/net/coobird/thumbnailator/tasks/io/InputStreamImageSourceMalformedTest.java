/*
 * Thumbnailator - a thumbnail generation library
 *
 * Copyright (c) 2008-2021 Chris Kroells
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

package net.coobird.thumbnailator.tasks.io;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class InputStreamImageSourceMalformedTest {

	@Parameterized.Parameters(name = "type={0}, length={1}")
	public static Collection<Object> testCases() {
		List<Object[]> cases = new ArrayList<Object[]>();
		for (String type : Arrays.asList("jpg", "png", "bmp")) {
			for (int i = 1; i <= 40; i++) {
				cases.add(new Object[] { type, i });
			}
		}
		return Arrays.asList(cases.toArray());
	}

	@Parameterized.Parameter
	public String type;

	@Parameterized.Parameter(value = 1)
	public Integer length;

	@Before	@After
	public void cleanup() {
		System.clearProperty("thumbnailator.disableExifWorkaround");
	}

	@Test
	public void terminatesProperlyWithWorkaround() {
		runTest();
	}

	@Test
	public void terminatesProperlyWithoutWorkaround() {
		System.setProperty("thumbnailator.disableExifWorkaround", "true");
		runTest();
	}

	/**
	 * Test to check that reading an abnormal file won't cause image reading
	 * to end up in a bad state like in an infinite loop.
	 */
	private void runTest() {
		try {
			byte[] bytes = new byte[length];
			InputStream sourceIs = ClassLoader.getSystemResourceAsStream(String.format("Thumbnailator/grid.%s", type));
			sourceIs.read(bytes);
			sourceIs.close();

			ByteArrayInputStream is = new ByteArrayInputStream(bytes);
			InputStreamImageSource source = new InputStreamImageSource(is);

			source.read();

		} catch (Exception e) {
			// terminates properly, even if an exception is thrown.
		}
	}
}
