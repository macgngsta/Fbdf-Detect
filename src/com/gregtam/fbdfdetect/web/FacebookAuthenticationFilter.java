package com.gregtam.fbdfdetect.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.google.appengine.repackaged.org.json.JSONException;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.gregtam.fbdfdetect.constants.FacebookConstants;
import com.gregtam.fbdfdetect.constants.FrameworkConstants;
import com.gregtam.fbdfdetect.helper.FacebookHelper;
import com.gregtam.fbdfdetect.helper.IOUtil;
import com.gregtam.fbdfdetect.helper.UrlContextManager;
import com.gregtam.fbdfdetect.model.AuthObject;

public class FacebookAuthenticationFilter implements Filter
{

	private static final Logger log = Logger
			.getLogger(FacebookAuthenticationFilter.class.getName());

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException
	{
		/*
		 * There are 3 steps to a facebook authorization.
		 * 
		 * 1. user authentication 2. app authorization 3. app authentication
		 * 
		 * 
		 * STEP 1 The client first invokes:
		 * https://www.facebook.com/dialog/oauth
		 * ?client_id=YOUR_APP_ID&redirect_uri=YOUR_URL&scope=email,read_stream
		 * This is actually both the user authentication and the app
		 * authorization
		 * 
		 * we pass our redirect_uri and a scope of what we want access to
		 * 
		 * STEP 2 if not authorized:
		 * http://redirect_uri?error_reason=user_denied
		 * &error=access_denied&error_description=The+user+denied+your+request.
		 * 3 params error_reason, error, error_description
		 * 
		 * if authorized: http://redirect_uri?code=A_CODE_GENERATED_BY_SERVER 1
		 * param code
		 * 
		 * STEP 3 use the code from step 2 to get an authorization token
		 * https://
		 * graph.facebook.com/oauth/access_token?client_id=YOUR_APP_ID&redirect_uri
		 * =YOUR_URL&client_secret=YOUR_APP_SECRET&code=THE_CODE_FROM_ABOVE 4
		 * params client_id, redirect_uri, client_secret, code
		 * 
		 * if not authenticated: returns json of error, type, message
		 * 
		 * if authenticated: it will return access_token and expires in the body
		 * - we must run the authentication step again if it has expired
		 * 
		 * at this point we can now gain access to the user object which we
		 * should grab.
		 */

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		String context = httpRequest.getServletPath();
		String path = httpRequest.getPathInfo();

		@SuppressWarnings("unchecked")
		Map<String, String> params = request.getParameterMap();

		// log.info("path: " + path);
		// log.info("query: " + httpRequest.getQueryString());
		// log.info("context: " + context);

		AuthObject auth = new AuthObject();

		if (path != null && !path.isEmpty())
		{
			if (UrlContextManager.isAuthentication(path))
			{
				if (params.containsKey(FacebookConstants.AUTH_CODE))
				{
					String authCode = (String) httpRequest
							.getParameter(FacebookConstants.AUTH_CODE);

					auth = processStep2Auth(authCode);
				}
				else if (params.containsKey(FacebookConstants.AUTH_ERROR))
				{

					log.info("app authorization failure");
					auth.setStatus(AuthObject.Status.ERROR);
					auth.setErrorType((String) httpRequest
							.getParameter(FacebookConstants.AUTH_ERROR));
					auth.setErrorMessage((String) httpRequest
							.getParameter(FacebookConstants.AUTH_ERROR_DESCRIPTION));
				}
				else
				{
					// bad request
					log.info("app auth: invalid params");
					auth.setStatus(AuthObject.Status.ERROR);
					auth.setErrorType("invalid params");
					auth.setErrorMessage("Invalid params were sent to the auth url pattern.");
				}
			}
			else
			{
				// let it fall through the facebook
				// authentication/authorization
				// filter
				// let the other chains handle it
				log.info("not fb auth related");
			}
		}

		// put the authobject on the request
		httpRequest.setAttribute(FacebookConstants.AUTH_OBJECT, auth);

		chain.doFilter(request, response);
		// dont need to run on the way back
		return;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
		// TODO Auto-generated method stub

	}

	public static AuthObject processStep2Auth(String authCode)
	{
		AuthObject auth = new AuthObject();

		auth.setAuthCode(authCode);

		log.info("auth code: " + authCode);

		String urlAuth = FacebookHelper.getAppAuthorization(authCode);

		log.info("auth url: " + urlAuth);

		// send the authorization code
		String contents = IOUtil.getURLContents(urlAuth);

		Map<String, String> authKeys = new HashMap<String, String>();

		// log.info("auth call: " + contents);

		if (contents.contains(FrameworkConstants.URL_CONCAT))
		{
			// split the concat
			String[] pairs = contents.toString().split(
					FrameworkConstants.URL_CONCAT);

			for (String pair : pairs)
			{
				String[] kv = pair.split(FrameworkConstants.URL_EQUALS);
				authKeys.put(kv[0], kv[1]);
			}

			// we got valid response from facebook
			if (authKeys.containsKey(FacebookConstants.KEY_ACCESS_TOKEN)
					&& authKeys.containsKey(FacebookConstants.KEY_EXPIRES))
			{
				log.info("app authentication success");
				auth.setStatus(AuthObject.Status.SUCCESS);
				auth.setAccessCode(authKeys
						.get(FacebookConstants.KEY_ACCESS_TOKEN));
				auth.setExpires(authKeys.get(FacebookConstants.KEY_EXPIRES));
			}
			else
			{
				// looked like a successful response, but didnt
				// part
				// correctly
				log.info("app authentication failure: validation");
				auth.setStatus(AuthObject.Status.ERROR);
				auth.setErrorType("invalid response");
				auth.setErrorMessage("Facebook did not return a valid response for app authentication.");
			}
		}
		else
		{
			// this is an json error - most likely user denied
			// access
			try
			{
				JSONObject errorResponse = new JSONObject(contents);
				JSONObject errorJson = (JSONObject) errorResponse
						.get(FacebookConstants.AUTH_ERROR);

				log.info("app authentication failure: json response");
				auth.setStatus(AuthObject.Status.ERROR);
				auth.setErrorType((String) errorJson
						.get(FacebookConstants.AUTH_TYPE));
				auth.setErrorMessage((String) errorJson
						.get(FacebookConstants.AUTH_MESSAGE));

			}
			catch (JSONException e)
			{
				// fb did not return a proper error response
				log.info("app authentication failure: " + e);
				auth.setStatus(AuthObject.Status.ERROR);
				auth.setErrorType("invalid response");
				auth.setErrorMessage("Facebook did not return a valid response for app authentication.");
			}
		}

		return auth;
	}

}
