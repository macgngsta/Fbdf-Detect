package com.gregtam.fbdfdetect.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

/**
 * Takes a url and get the full string back
 * 
 * @author gtam
 * 
 */
public class IOUtil
{
	private static final Logger log = Logger.getLogger(IOUtil.class.getName());

	public static String getURLContents(String url) throws RuntimeException
	{

		StringBuilder sb = new StringBuilder();

		try
		{
			URL myUrl = new URL(url);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					myUrl.openStream()));

			String line;

			while ((line = reader.readLine()) != null)
			{
				sb.append(line);
			}

			reader.close();

		}
		catch (MalformedURLException e)
		{
			log.info("invalid url: " + e);
			throw new RuntimeException("invalid url");
		}
		catch (IOException e)
		{
			log.info("couldnt read url: " + e);
			throw new RuntimeException("couldnt read url");
		}

		return sb.toString();
	}

	public static boolean parseValue(String input)
	{
		if (validate(input))
		{
			if (input.equalsIgnoreCase("y"))
			{
				return true;
			}
		}

		return false;
	}

	public static boolean validate(String s)
	{
		if (s != null && !s.isEmpty())
			return true;
		return false;
	}
}
