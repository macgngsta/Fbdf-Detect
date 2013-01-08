package com.gregtam.fbdfdetect.services;

import java.util.logging.Logger;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.gregtam.fbdfdetect.constants.FacebookConstants;
import com.gregtam.fbdfdetect.helper.FacebookHelper;
import com.gregtam.fbdfdetect.helper.IOUtil;
import com.gregtam.fbdfdetect.model.FbdfUser;

public class UserServices
{
	private static final Logger log = Logger.getLogger(UserServices.class
			.getName());

	public static final long SECONDS_TO_MILLI = 1000;

	public UserServices()
	{

	}

	public FbdfUser authFacebookLogin(String accessCode, long expires,
			String authCode)
	{
		try
		{
			JSONObject resp = new JSONObject(
					IOUtil.getURLContents(FacebookHelper
							.getCurrentUser(accessCode)));

			Long fbId = Long.valueOf(resp.getString(FacebookConstants.KEY_ID));
			String name = resp.getString(FacebookConstants.KEY_NAME);
			String email = resp.getString(FacebookConstants.KEY_EMAIL);
			Boolean verified = Boolean.valueOf(resp
					.getString(FacebookConstants.KEY_VERIFIED));
			String pictureURL = resp.getString(FacebookConstants.KEY_PICTURE);

			if (fbId != null)
			{
				FbdfUser myUser = new FbdfUser(fbId, accessCode, name, email,
						expires, verified, pictureURL, authCode);

				log.info("created user: " + myUser);

				return myUser;
			}

			log.info("could not create user");

		}
		catch (JSONException e)
		{
			log.info("couldnt read json" + e);
		}
		catch (RuntimeException e)
		{
			log.info("some error occurred in auth" + e);
		}

		return null;
	}

}
