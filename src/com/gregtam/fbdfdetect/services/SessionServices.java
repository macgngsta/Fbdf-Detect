package com.gregtam.fbdfdetect.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import com.gregtam.fbdfdetect.constants.FrameworkConstants;
import com.gregtam.fbdfdetect.model.FbdfUser;

public class SessionServices
{
	private static final Logger log = Logger.getLogger(SessionServices.class
			.getName());

	private static SessionServices _instance = null;

	private Map<String, FbdfUser> sessionUsers;

	private SessionServices()
	{
		sessionUsers = new ConcurrentHashMap<String, FbdfUser>();
	}

	public static synchronized SessionServices getInstance()
	{
		if (_instance == null)
		{
			_instance = new SessionServices();
		}

		return _instance;
	}

	public FbdfUser getUserFromSession(HttpServletRequest httpRequest)
	{
		FbdfUser user = null;

		String key = (String) httpRequest.getSession().getAttribute(
				FrameworkConstants.KEY_SESSION_USER);

		if (key != null && !key.isEmpty())
		{
			if (sessionUsers.containsKey(key))
			{
				user = sessionUsers.get(key);
				log.info("got user from session");
			}
		}

		return user;
	}

	public void setUserToSession(HttpServletRequest httpRequest, FbdfUser user)
	{
		if (user != null)
		{
			String newKey = createKeyFromUser(user);
			if (newKey != null && !newKey.isEmpty())
			{
				httpRequest.getSession().setAttribute(
						FrameworkConstants.KEY_SESSION_USER, newKey);

				sessionUsers.put(newKey, user);
				log.info("put user in session");
			}
		}
	}

	private String createKeyFromUser(FbdfUser user)
	{
		StringBuilder sb = new StringBuilder();

		if (user != null)
		{
			sb.append(user.getFbId());
			sb.append(".");
			sb.append(user.getFullName());

			return CryptoServices.getInstance().encryptString(sb.toString());
		}

		return sb.toString();

	}

}
