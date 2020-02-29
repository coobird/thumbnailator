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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.coobird.thumbnailator.ThumbnailParameter;
import net.coobird.thumbnailator.tasks.io.InputStreamImageSource;
import net.coobird.thumbnailator.tasks.io.OutputStreamImageSink;

/**
 * A thumbnail generation task which streams data from an {@link InputStream}
 * to an {@link OutputStream}.
 * <p>
 * This class does not close the {@link InputStream} and {@link OutputStream}
 * upon the completion of processing.
 * <p>
 * Only the first image obtained from the data stream will be read. Subsequent
 * images will be ignored.
 * 
 * @author coobird
 *
 */
public class StreamThumbnailTask extends ThumbnailTask<InputStream, OutputStream> {
	/**
	 * The {@link SourceSinkThumbnailTask} used to perform the task.
	 */
	private final SourceSinkThumbnailTask<InputStream, OutputStream> task;
	
	/**
	 * Creates a {@link ThumbnailTask} in which streamed image data from the
	 * specified {@link InputStream} is output to a specified
	 * {@link OutputStream}, using the parameters provided in the specified
	 * {@link ThumbnailParameter}.
	 * 
	 * @param param		The parameters to use to create the thumbnail.
	 * @param is		The {@link InputStream} from which to obtain image data.
	 * @param os		The {@link OutputStream} to send thumbnail data to.
	 * @throws NullPointerException		If the parameter is {@code null}.
	 */
	public StreamThumbnailTask(ThumbnailParameter param, InputStream is, OutputStream os) {
		super(param);
		this.task = new SourceSinkThumbnailTask<InputStream, OutputStream>(
				param,
				new InputStreamImageSource(is),
				new OutputStreamImageSink(os)
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
	public InputStream getSource() {
		return task.getSource();
	}

	@Override
	public OutputStream getDestination() {
		return task.getDestination();
	}
}
