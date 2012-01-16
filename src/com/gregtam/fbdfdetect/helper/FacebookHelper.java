package com.gregtam.fbdfdetect.helper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Logger;

import com.google.appengine.repackaged.com.google.common.base.Join;
import com.gregtam.fbdfdetect.constants.FacebookConstants;
import com.gregtam.fbdfdetect.constants.FrameworkConstants;

public class FacebookHelper
{
	private static final Logger log = Logger.getLogger(FacebookHelper.class
			.getName());

	private static final String apiKey = "07c9e188f53163b7b1dc55ccac0493db";
	private static final String secret = "d2523e80c83cdc010b338ef256a336bd";
	private static final String clientId = "179434102067131";

	public static final String baseHost = "https://fbdfdetect.appspot.com";
	// public static final String baseHost = "http://localhost:8888";
	public static final String appId = "fb";

	// deploy pass
	// kethbsx566uy5hf7

	// servlet url for the authentication servlet/filter
	public static final String redirectURL = "http://www.gregtam.com";

	private static final String[] perms = new String[]
	{ "email" };

	public static String getAPIKey()
	{
		return apiKey;
	}

	public static String getSecret()
	{
		return secret;
	}

	public static String getAppAuthentication()
	{
		StringBuilder sb = new StringBuilder();

		// add the base and action
		sb.append(FacebookConstants.FB_BASE_URL);
		sb.append(FrameworkConstants.URL_DELIMITER);

		// authorize action
		sb.append(FacebookConstants.FB_AUTHORIZE);
		sb.append(FrameworkConstants.URL_QUERY);

		// client id
		sb.append(FacebookConstants.KEY_CLIENT_ID);
		sb.append(FrameworkConstants.URL_EQUALS);
		sb.append(clientId);

		// display
		sb.append(FrameworkConstants.URL_CONCAT);
		sb.append(FacebookConstants.KEY_DISPLAY);
		sb.append(FrameworkConstants.URL_EQUALS);
		sb.append("page");

		// redirect url
		sb.append(FrameworkConstants.URL_CONCAT);
		sb.append(FacebookConstants.KEY_REDIRECT);
		sb.append(FrameworkConstants.URL_EQUALS);

		StringBuilder hb = new StringBuilder();

		hb.append(baseHost);
		hb.append(FrameworkConstants.URL_DELIMITER);
		hb.append(appId);
		hb.append(FrameworkConstants.CONTEXT_AUTHENTICATION);
		hb.append(FrameworkConstants.URL_DELIMITER);

		try
		{
			sb.append(URLEncoder.encode(hb.toString(), "UTF-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			log.info("error encoding url");
			e.printStackTrace();
		}

		// desired permissions
		sb.append(FrameworkConstants.URL_CONCAT);
		sb.append(FacebookConstants.KEY_SCOPE);
		sb.append(FrameworkConstants.URL_EQUALS);
		sb.append(Join.join(",", perms));

		return sb.toString();
	}

	public static String getAppAuthorization(String authCode)
	{

		StringBuilder sb = new StringBuilder();

		// add the base and action
		sb.append(FacebookConstants.FB_BASE_URL);
		sb.append(FrameworkConstants.URL_DELIMITER);

		// authorize action
		sb.append(FacebookConstants.FB_ACCESS);
		sb.append(FrameworkConstants.URL_QUERY);

		// client id
		sb.append(FacebookConstants.KEY_CLIENT_ID);
		sb.append(FrameworkConstants.URL_EQUALS);
		sb.append(clientId);

		// redirect url
		sb.append(FrameworkConstants.URL_CONCAT);
		sb.append(FacebookConstants.KEY_REDIRECT);
		sb.append(FrameworkConstants.URL_EQUALS);

		StringBuilder hb = new StringBuilder();

		hb.append(baseHost);
		hb.append(FrameworkConstants.URL_DELIMITER);
		hb.append(appId);
		hb.append(FrameworkConstants.CONTEXT_AUTHENTICATION);
		hb.append(FrameworkConstants.URL_DELIMITER);

		try
		{
			sb.append(URLEncoder.encode(hb.toString(), "UTF-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			log.info("error encoding url");
			e.printStackTrace();
		}

		// client secret
		sb.append(FrameworkConstants.URL_CONCAT);
		sb.append(FacebookConstants.KEY_CLIENT_SECRET);
		sb.append(FrameworkConstants.URL_EQUALS);
		sb.append(secret);

		// auth code
		sb.append(FrameworkConstants.URL_CONCAT);
		sb.append(FacebookConstants.KEY_CODE);
		sb.append(FrameworkConstants.URL_EQUALS);

		StringBuilder ab = new StringBuilder();

		ab.append(authCode);

		try
		{
			sb.append(URLEncoder.encode(ab.toString(), "UTF-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			log.info("error encoding authcode");
			e.printStackTrace();
		}

		return sb.toString();

	}

	public static String getCurrentUser(String accessCode)
	{
		StringBuilder sb = new StringBuilder();

		// add the base and action
		sb.append(FacebookConstants.FB_BASE_URL);
		sb.append(FrameworkConstants.URL_DELIMITER);

		// get me
		sb.append(FacebookConstants.FB_ME);
		sb.append(FrameworkConstants.URL_QUERY);

		// access token
		sb.append(FacebookConstants.KEY_ACCESS_TOKEN);
		sb.append(FrameworkConstants.URL_EQUALS);

		StringBuilder ab = new StringBuilder();

		ab.append(accessCode);

		try
		{
			sb.append(URLEncoder.encode(ab.toString(), "UTF-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			log.info("error encoding accessCode");
			e.printStackTrace();
		}

		sb.append(FrameworkConstants.URL_CONCAT);
		sb.append(FacebookConstants.FB_FIELDS);
		sb.append(FrameworkConstants.URL_EQUALS);

		StringBuilder fieldBuilder = new StringBuilder();
		fieldBuilder.append(FacebookConstants.KEY_ID);
		fieldBuilder.append(FrameworkConstants.URL_COMMA);
		fieldBuilder.append(FacebookConstants.KEY_FIRST_NAME);
		fieldBuilder.append(FrameworkConstants.URL_COMMA);
		fieldBuilder.append(FacebookConstants.KEY_LAST_NAME);
		fieldBuilder.append(FrameworkConstants.URL_COMMA);
		fieldBuilder.append(FacebookConstants.KEY_NAME);
		fieldBuilder.append(FrameworkConstants.URL_COMMA);
		fieldBuilder.append(FacebookConstants.KEY_EMAIL);
		fieldBuilder.append(FrameworkConstants.URL_COMMA);
		fieldBuilder.append(FacebookConstants.KEY_VERIFIED);
		fieldBuilder.append(FrameworkConstants.URL_COMMA);
		fieldBuilder.append(FacebookConstants.KEY_PICTURE);

		sb.append(fieldBuilder.toString());

		log.info("me url: " + sb.toString());

		return sb.toString();
	}

	public static String getFriends(String accessCode)
	{
		StringBuilder sb = new StringBuilder();

		// add the base and action
		sb.append(FacebookConstants.FB_BASE_URL);
		sb.append(FrameworkConstants.URL_DELIMITER);

		// get friends action
		sb.append(FacebookConstants.FB_FRIENDS);
		sb.append(FrameworkConstants.URL_QUERY);

		// access token
		sb.append(FrameworkConstants.URL_CONCAT);
		sb.append(FacebookConstants.KEY_ACCESS_TOKEN);
		sb.append(FrameworkConstants.URL_EQUALS);

		StringBuilder ab = new StringBuilder();

		ab.append(accessCode);

		try
		{
			sb.append(URLEncoder.encode(ab.toString(), "UTF-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			log.info("error encoding accessCode");
			e.printStackTrace();
		}

		return sb.toString();
	}

}
