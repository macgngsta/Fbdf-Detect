package com.gregtam.fbdfdetect.helper;

import com.gregtam.fbdfdetect.constants.FrameworkConstants;

public class UrlContextManager
{
	public static boolean isAuthentication(String context)
	{

		StringBuilder sb = new StringBuilder();
		sb.append(FrameworkConstants.CONTEXT_AUTHENTICATION);
		sb.append(FrameworkConstants.URL_DELIMITER);

		return context.startsWith(sb.toString());
	}

	public static boolean isAuthorization(String context)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(FrameworkConstants.CONTEXT_AUTHENTICATION);

		return context.startsWith(sb.toString());
	}

	public static boolean isUpdate(String context)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(FrameworkConstants.CONTEXT_UPDATE);

		return context.startsWith(sb.toString());
	}

	public static boolean isPrivacy(String context)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(FrameworkConstants.CONTEXT_PRIVACY);

		return context.startsWith(sb.toString());
	}

	public static boolean isRaw(String context)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(FrameworkConstants.CONTEXT_RAW);

		return context.startsWith(sb.toString());
	}
}
