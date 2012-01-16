package com.gregtam.fbdfdetect.web;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.gregtam.fbdfdetect.constants.FacebookConstants;
import com.gregtam.fbdfdetect.constants.FrameworkConstants;
import com.gregtam.fbdfdetect.helper.UserCacheManager;
import com.gregtam.fbdfdetect.model.AuthObject;
import com.gregtam.fbdfdetect.model.FbdfUser;
import com.gregtam.fbdfdetect.model.SessionObject;

public class SessionManagerFilter implements Filter
{

	private static final Logger log = Logger
			.getLogger(SessionManagerFilter.class.getName());

	public static final long TIME_TO_SECONDS = 1000;

	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException
	{
		log.info("enter session filter: " + request.getLocalPort());

		if (UserCacheManager.getInstance().hasChanged())
		{
			UserCacheManager.getInstance().refreshList();
		}

		HttpServletRequest httpRequest = (HttpServletRequest) request;

		// find out if there is a session related to this user
		HttpSession httpSession = httpRequest.getSession(true);
		SessionObject mySession = (SessionObject) httpSession
				.getAttribute(FrameworkConstants.SESSION_OBJECT);

		AuthObject myAuth = (AuthObject) httpRequest
				.getAttribute(FacebookConstants.AUTH_OBJECT);

		// have a session
		if (mySession != null)
		{
			log.info("sf: has session");
			// if invalid user
			if (mySession.getFbId() == SessionObject.INVALID_USER)
			{
				log.info("sf: invalid user");
				if (myAuth != null)
				{
					log.info("sf: has auth 1");
					if (myAuth.getStatus().equals(AuthObject.Status.SUCCESS))
					{
						log.info("sf: auth success");
						// create new user
						mySession.setAccessCode(myAuth.getAccessCode());
						mySession.setExpires(myAuth.getExpires());
						mySession.setAuthCode(myAuth.getAuthCode());
						mySession.setState(SessionObject.NEW_STATE);
					}
					else
					{
						log.info("sf: auth fail");
						// if it was an error then catch here
						mySession.setState(SessionObject.INVALID_STATE);
					}
				}
				else
				{
					log.info("sf: no auth");
					// this is login
					mySession.setState(SessionObject.LOGIN_STATE);
				}
			}
			// has valid user
			else
			{
				log.info("sf: has modify user");

				FbdfUser myUser = UserCacheManager.getInstance().getUser(
						mySession.getFbId());

				if (myUser != null)
				{
					log.info("sf: user not null");
					// see if the access code is valid
					if (validateAccessCode(myUser))
					{
						log.info("sf: ac valid");
						// store user into session
						mySession.setAccessCode(myUser.getAccessCode());
						mySession
								.setExpires(Long.toString(myUser.getExpires()));
						mySession.setFbId(myUser.getFbId());
						mySession.setAuthCode(myUser.getAuthCode());
						mySession.setState(SessionObject.VALID_STATE);
					}
					else
					{
						log.info("sf: ac not valid");
						if (myAuth != null)
						{
							log.info("sf: has auth 2");
							if (myAuth.getStatus().equals(
									AuthObject.Status.SUCCESS))
							{
								log.info("sf: auth success");
								// create new user
								mySession.setAccessCode(myAuth.getAccessCode());
								mySession.setExpires(myAuth.getExpires());
								mySession.setAuthCode(myAuth.getAuthCode());
								mySession.setState(SessionObject.NEW_STATE);
							}
							else
							{
								log.info("sf: auth fail");
								mySession.setState(SessionObject.INVALID_STATE);
							}
						}
						else
						{
							log.info("sf: auth invalid");
							// require reauth, was my access revoked?
							mySession.setFbId(myUser.getFbId());
							mySession.setState(SessionObject.REAUTH_STATE);
						}
					}
				}
				else
				{
					log.info("sf: user null");
					// came back from authentication - i should have auth object
					if (myAuth != null)
					{
						log.info("sf: has auth 3");
						if (myAuth.getStatus()
								.equals(AuthObject.Status.SUCCESS))
						{
							log.info("sf: auth success");
							// create new user
							mySession.setAccessCode(myAuth.getAccessCode());
							mySession.setExpires(myAuth.getExpires());
							mySession.setAuthCode(myAuth.getAuthCode());
							mySession.setState(SessionObject.NEW_STATE);
						}
						else
						{
							log.info("sf: auth fail");
							// if it was an error then catch here
							mySession.setState(SessionObject.INVALID_STATE);
						}
					}
					else
					{
						log.info("sf: auth invalid");
						mySession.setState(SessionObject.LOGIN_STATE);
					}
				}
			}

		}
		else
		{
			log.info("sf: no session");
			mySession = new SessionObject();

			if (myAuth != null)
			{
				log.info("sf: has auth 4");
				if (myAuth.getStatus().equals(AuthObject.Status.SUCCESS))
				{
					log.info("sf: auth success");
					// create new user
					mySession.setAccessCode(myAuth.getAccessCode());
					mySession.setExpires(myAuth.getExpires());
					mySession.setAuthCode(myAuth.getAuthCode());
					mySession.setState(SessionObject.NEW_STATE);
				}
				else
				{
					log.info("sf: auth fail");
					// if it was an error then catch here
					mySession.setState(SessionObject.INVALID_STATE);
				}
			}
			else
			{
				log.info("sf: no auth");
				mySession.setState(SessionObject.LOGIN_STATE);
			}

		}

		// reset the session
		httpSession.setAttribute(FrameworkConstants.SESSION_OBJECT, mySession);

		chain.doFilter(request, response);
		// dont need to run on the way back
		return;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
		// TODO Auto-generated method stub
	}

	private boolean validateAccessCode(FbdfUser myUser)
	{

		Date currentTime = new Date();
		Date oldTime = myUser.getCreateToken();

		long cTime = currentTime.getTime();
		log.info("current time: " + cTime);

		long oTime = oldTime.getTime();
		log.info("original time: " + oTime);

		long seconds = myUser.getExpires() * TIME_TO_SECONDS;
		log.info("expiration: " + seconds);

		long sessionExpiration = oTime + seconds;
		log.info("cacluated exp: " + sessionExpiration);

		if (cTime > sessionExpiration)
		{
			return false;
		}

		return true;
	}

}
