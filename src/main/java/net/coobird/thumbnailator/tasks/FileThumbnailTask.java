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

package net.coobird.thumbnailator.tasks;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.tasks.io.FileImageSink;
import net.coobird.thumbnailator.tasks.io.FileImageSource;

/**
 * A thumbnail generation task which reads and writes data from and to a
 * {@link File}.
 * <p>
 * Only the first image included in the image file will be read. Subsequent
 * images included in the image file will be ignored.
 * 
 * @author coobird
 *
 */
public class FileThumbnailTask extends ThumbnailTask<File, File> {
	/**
	 * The {@link SourceSinkThumbnailTask} used to perform the task.
	 */
	private final SourceSinkThumbnailTask<File, File> task;
	
	/**
	 * Creates a {@link ThumbnailTask} in which image data is read from the
	 * specified {@link File} and is output to a specified {@link File}, using
	 * the parameters provided in the specified {@link ThumbnailParameter}.
	 * 
	 * @param param				The parameters to use to create the thumbnail.
	 * @param sourceFile		The {@link File} from which image data is read.
	 * @param destinationFile	The {@link File} to which thumbnail is written.
	 * @throws NullPointerException		If the parameter is {@code null}.
	 */
	public FileThumbnailTask(ThumbnailParameter param, File sourceFile, File destinationFile) {
		super(param);
		this.task = new SourceSinkThumbnailTask<File, File>(
				param,
				new FileImageSource(sourceFile),
				new FileImageSink(destinationFile)
		);
	}

	@Override
	public BufferedImage read() throws IOException {
		return task.read();
	}
	
	@Override
	public void write(BufferedImage img) throws IOException {
		task.write(img);
	}

	@Override
	public ThumbnailParameter getParam() {
		return task.getParam();
	}

	@Override
	public File getSource() {
		return task.getSource();
	}
	
	@Override
	public File getDestination() {
		return task.getDestination();
	}
}
