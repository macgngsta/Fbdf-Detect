package com.gregtam.fbdfdetect.helper;

import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.gregtam.fbdfdetect.constants.FacebookConstants;
import com.gregtam.fbdfdetect.constants.FrameworkConstants;
import com.gregtam.fbdfdetect.model.AuthObject;
import com.gregtam.fbdfdetect.model.FbdfRequest;
import com.gregtam.fbdfdetect.model.FbdfUser;
import com.gregtam.fbdfdetect.model.SessionObject;
import com.gregtam.fbdfdetect.services.UserServices;
import com.gregtam.fbdfdetect.web.FacebookAuthenticationFilter;

/**
 * The purpose of this class is to build the fbdf request from HTTP
 * 
 * @author gtam
 * 
 */
public class FbdfRequestBuilder
{
	private static final Logger log = Logger.getLogger(FbdfRequestBuilder.class
			.getName());

	private ServletRequest req;

	public FbdfRequestBuilder(ServletRequest req)
	{
		this.req = req;
	}

	public FbdfRequest processHttp()
	{
		// init
		FbdfUser myUser;

		FbdfRequest myRequest = new FbdfRequest();

		HttpServletRequest httpRequest = (HttpServletRequest) req;

		HttpSession httpSession = httpRequest.getSession(true);
		SessionObject mySession = (SessionObject) httpSession
				.getAttribute(FrameworkConstants.SESSION_OBJECT);

		// log.info("path: " + httpRequest.getPathInfo());
		// log.info("query: " + httpRequest.getQueryString());
		// log.info("context: " + httpRequest.getServletPath());

		// String context = httpRequest.getServletPath();
		String path = httpRequest.getPathInfo();

		log.info("path: " + path);

		UserServices myUserSrv = new UserServices();

		if (mySession != null)
		{

			switch (mySession.getState())
			{

			// returning user
			case SessionObject.VALID_STATE:

				log.info("valid state");
				myUser = UserCacheManager.getInstance().getUser(
						mySession.getFbId());

				myRequest.setFbUser(myUser);

				if (UrlContextManager.isUpdate(path))
				{
					log.info("update action");
					myRequest.setAction(FbdfRequest.ReqAction.UPDATE);
				}
				else if (UrlContextManager.isRaw(path))
				{
					log.info("raw action");
					updatePageData(myRequest);
					myRequest.setAction(FbdfRequest.ReqAction.ACTIVITY);
				}
				else if (UrlContextManager.isAuthorization(path))
				{
					log.info("add/update user action");
					// add new user
					myUser = myUserSrv.authFacebookLogin(
							mySession.getAccessCode(),
							Long.parseLong(mySession.getExpires()),
							mySession.getAuthCode());

					myRequest.setFbUser(myUser);

					// make sure to update session on new user
					mySession.setFbId(myUser.getFbId());

					UserCacheManager.getInstance().addUpdateUser(myUser, false);

					myRequest.setAction(FbdfRequest.ReqAction.REDIR_DASHBOARD);
				}
				else
				{
					log.info("dashboard action");
					// default to dashboard
					myRequest.setAction(FbdfRequest.ReqAction.DASHBOARD);
				}

				break;

			// reauth
			case SessionObject.REAUTH_STATE:

				log.info("reauth state");
				// use the auth code to reauth
				myUser = UserCacheManager.getInstance().getUser(
						mySession.getFbId());

				String oAuth = myUser.getAuthCode();

				// now test to see if this auth code is still valid
				if (oAuth != null && !oAuth.isEmpty())
				{
					log.info("oauth avail");
					// if it exists then we can run the usual auth step 2
					AuthObject myAuth = FacebookAuthenticationFilter
							.processStep2Auth(oAuth);

					if (myAuth != null)
					{
						if (myAuth.getStatus()
								.equals(AuthObject.Status.SUCCESS))
						{

							// get user info
							myUser = myUserSrv.authFacebookLogin(
									mySession.getAccessCode(),
									Long.parseLong(mySession.getExpires()),
									mySession.getAuthCode());

							myUser.setCreateToken(myAuth.getoAuthCreation());

							myRequest.setFbUser(myUser);

							// make sure to update session on new user
							mySession.setFbId(myUser.getFbId());
							mySession.setAccessCode(myAuth.getAccessCode());
							mySession.setExpires(myAuth.getExpires());
							mySession.setAuthCode(myAuth.getAuthCode());

							UserCacheManager.getInstance().addUpdateUser(
									myUser, false);

							myRequest
									.setAction(FbdfRequest.ReqAction.REDIR_DASHBOARD);
						}
						else
						{
							// auth was invalid
							myRequest.setAction(FbdfRequest.ReqAction.INVALID);
						}

						httpRequest.setAttribute(FacebookConstants.AUTH_OBJECT,
								myAuth);
					}
					else
					{
						myRequest.setAction(FbdfRequest.ReqAction.INVALID);
					}

				}
				else
				{
					log.info("oauth not avail");
					// oAuth never got stored in user
					// we do a full auth
					String step1Url = FacebookHelper.getAppAuthentication();
					myRequest.setUrl(step1Url);

					myRequest.setFbUser(myUser);
					myRequest.setAction(FbdfRequest.ReqAction.RE_AUTH);
				}

				break;
			// completely new user
			case SessionObject.NEW_STATE:

				log.info("new state");
				// add new user
				myUser = myUserSrv.authFacebookLogin(mySession.getAccessCode(),
						Long.parseLong(mySession.getExpires()),
						mySession.getAuthCode());

				myRequest.setFbUser(myUser);

				// make sure to update session on new user
				mySession.setFbId(myUser.getFbId());

				UserCacheManager.getInstance().addUpdateUser(myUser, false);

				myRequest.setAction(FbdfRequest.ReqAction.REDIR_DASHBOARD);

				break;

			case SessionObject.LOGIN_STATE:

				log.info("login state");

				myRequest.setAction(FbdfRequest.ReqAction.LOGIN);

				break;

			case SessionObject.INVALID_STATE:
			default:
				log.info("error state");
				// goto error
				myRequest.setAction(FbdfRequest.ReqAction.INVALID);
			}
		}
		else
		{
			log.info("session is null");
			// goto error
			myRequest.setAction(FbdfRequest.ReqAction.INVALID);
		}

		if (UrlContextManager.isPrivacy(path))
		{
			// privacy
			myRequest.setAction(FbdfRequest.ReqAction.PRIVACY);
		}

		// update the session
		httpSession.setAttribute(FrameworkConstants.SESSION_OBJECT, mySession);

		return myRequest;
	}

	private void updatePageData(FbdfRequest myRequest)
	{
		// check for pagelimit
		String start = (String) req.getParameter(FrameworkConstants.POST_START);

		String limit = (String) req.getParameter(FrameworkConstants.POST_LIMIT);

		if (IOUtil.validate(start))
		{
			myRequest.setStart(Long.parseLong(start));
		}
		if (IOUtil.validate(limit))
		{
			myRequest.setLimit(Long.parseLong(limit));
		}
	}
}
