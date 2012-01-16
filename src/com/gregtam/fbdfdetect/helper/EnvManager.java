package com.gregtam.fbdfdetect.helper;

import java.util.Properties;

import com.gregtam.fbdfdetect.constants.FrameworkConstants;

public class EnvManager
{
	private Properties properties;
	private static EnvManager _instance;

	private EnvManager()
	{
		properties = new Properties();
		reset();
	}

	public static synchronized EnvManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new EnvManager();
		}

		return _instance;
	}

	public String getProperty(String key)
	{
		if (properties.contains(key))
		{
			return properties.getProperty(key);
		}

		return null;
	}

	public void addProperty(String key, String value)
	{
		properties.put(key, value);
	}

	public void removeProperty(String key)
	{
		if (properties.contains(key))
		{
			properties.remove(key);
		}
	}

	public void reset()
	{
		// DEFAULT properties to use
		properties.put(FrameworkConstants.POST_ENV, "local");
		properties.put(FrameworkConstants.POST_CACHE, "y");
	}
}
