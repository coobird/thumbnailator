package net.coobird.thumbnailator.tasks.io;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.Proxy;
import java.net.URL;

public class URLImageSource extends AbstractImageSource
{
	private final URL url;
	private final Proxy proxy;

	/**
	 * @param is
	 */
	public URLImageSource(URL url)
	{
		super();
		this.url = url;
		this.proxy = null;
	}
	
	/**
	 * @param url
	 * @param proxy
	 */
	public URLImageSource(URL url, Proxy proxy)
	{
		super();
		this.url = url;
		this.proxy = proxy;
	}

	public BufferedImage read() throws IOException
	{
		InputStreamImageSource source;
		try
		{
			if (proxy != null)
			{
				source = new InputStreamImageSource(url.openConnection(proxy).getInputStream());
			}
			else
			{
				source = new InputStreamImageSource(url.openStream());
			}
		}
		catch (IOException e)
		{
			throw new IOException("Could not open connection to URL: " + url);
		}
		
		BufferedImage img;
		try
		{
			img = source.read();
		}
		catch (Exception e)
		{
			throw new IOException("Could not obtain image from URL: " + url);
		}
		
		this.inputFormatName = source.getInputFormatName();
		
		return img;
	}
}
