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

package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

/**
 * An {@link ImageSource} which retrieves a source image from a URL.
 * 
 * @author coobird
 *
 */
public class URLImageSource extends AbstractImageSource<URL> {
	/**
	 * The URL from which to retrieve the source image.
	 */
	private final URL url;
	
	/**
	 * The proxy to use to connect to the image URL.
	 * <p>
	 * If a proxy is not required, then this field will be {@code null}.
	 */
	private final Proxy proxy;

	/**
	 * Instantiates an {@link URLImageSource} with the URL from which the
	 * source image should be retrieved from.
	 * 
	 * @param url		URL to the source image.
	 * @throws NullPointerException		If the URL is null
	 */
	public URLImageSource(URL url) {
		super();
		
		if (url == null) {
			throw new NullPointerException("URL cannot be null.");
		}
		
		this.url = url;
		this.proxy = null;
	}
	
	/**
	 * Instantiates an {@link URLImageSource} with the URL from which the
	 * source image should be retrieved from.
	 * 
	 * @param url		URL to the source image.
	 * @throws NullPointerException		If the URL is null
	 * @throws MalformedURLException	If the URL is not valid.
	 */
	public URLImageSource(String url) throws MalformedURLException {
		super();
		
		if (url == null) {
			throw new NullPointerException("URL cannot be null.");
		}
		
		this.url = new URL(url);
		this.proxy = null;
	}
	
	/**
	 * Instantiates an {@link URLImageSource} with the URL from which the
	 * source image should be retrieved from, along with the proxy to use
	 * to connect to the aforementioned URL.
	 * 
	 * @param url		URL to the source image.
	 * @param proxy		Proxy to use to connect to the URL.
	 * @throws NullPointerException		If the URL and or the proxy is null
	 */
	public URLImageSource(URL url, Proxy proxy) {
		super();
		
		if (url == null) {
			throw new NullPointerException("URL cannot be null.");
		} else if (proxy == null) {
			throw new NullPointerException("Proxy cannot be null.");
		}
		
		this.url = url;
		this.proxy = proxy;
	}
	
	/**
	 * Instantiates an {@link URLImageSource} with the URL from which the
	 * source image should be retrieved from, along with the proxy to use
	 * to connect to the aforementioned URL.
	 * 
	 * @param url		URL to the source image.
	 * @param proxy		Proxy to use to connect to the URL.
	 * @throws NullPointerException		If the URL and or the proxy is null
	 * @throws MalformedURLException	If the URL is not valid.
	 */
	public URLImageSource(String url, Proxy proxy) throws MalformedURLException {
		super();
		
		if (url == null) {
			throw new NullPointerException("URL cannot be null.");
		} else if (proxy == null) {
			throw new NullPointerException("Proxy cannot be null.");
		}
		
		this.url = new URL(url);
		this.proxy = proxy;
	}

	public BufferedImage read() throws IOException {
		InputStream is;
		try {
			if (proxy != null) {
				is = url.openConnection(proxy).getInputStream();
			} else {
				is = url.openStream();
			}

		} catch (IOException e) {
			throw new IOException("Could not open connection to URL: " + url);
		}

		InputStreamImageSource source = new InputStreamImageSource(is);
		source.setThumbnailParameter(param);
		
		BufferedImage img;
		try {
			img = source.read();
		} catch (Exception e) {
			throw new IOException("Could not obtain image from URL: " + url);
		} finally {
			is.close();
		}

		this.inputFormatName = source.getInputFormatName();
		
		return finishedReading(img);
	}

	/**
	 * Returns the URL from which the source image is retrieved from.
	 * 
	 * @return the url		The URL to the source image.s
	 */
	public URL getSource() {
		return url;
	}

	/**
	 * Returns the proxy to use when connecting to the URL to retrieve the
	 * source image.
	 * 
	 * @return the proxy	The proxy used to connect to a URL.
	 */
	public Proxy getProxy() {
		return proxy;
	}
}
