package com.gregtam.fbdfdetect;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.gregtam.fbdfdetect.constants.FacebookConstants;
import com.gregtam.fbdfdetect.constants.FrameworkConstants;
import com.gregtam.fbdfdetect.exceptions.InvalidAccessException;
import com.gregtam.fbdfdetect.helper.FacebookHelper;
import com.gregtam.fbdfdetect.helper.FriendInfoCacheManager;
import com.gregtam.fbdfdetect.helper.FriendListManager;
import com.gregtam.fbdfdetect.helper.IOUtil;
import com.gregtam.fbdfdetect.helper.UserCacheManager;
import com.gregtam.fbdfdetect.model.FbdfUser;
import com.gregtam.fbdfdetect.model.FriendAssociation;
import com.gregtam.fbdfdetect.model.FriendItem;
import com.gregtam.fbdfdetect.model.SearchableValue;

public class ProcessFriendsTask extends HttpServlet
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4216665796048824381L;

	private static final Logger log = Logger.getLogger(ProcessFriendsTask.class
			.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		processFriends(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		// TODO Auto-generated method stub
		doGet(req, resp);
	}

	private void processFriends(HttpServletRequest req, HttpServletResponse resp)
			throws IOException
	{
		String strCallResult = "";
		resp.setContentType("text/plain");

		try
		{
			// get fbid parameter
			String fbid = req
					.getParameter(FrameworkConstants.QUEUE_PF_PARAM_FBID);

			if (fbid != null && !fbid.isEmpty())
			{
				FbdfUser myUser = UserCacheManager.getInstance().getUser(
						Long.parseLong(fbid));

				if (myUser != null)
				{
					myUser.setLastModify(new Date());

					if (processFriends(myUser))
					{
						// refresh the list
						if (UserCacheManager.getInstance().hasChanged())
						{
							UserCacheManager.getInstance().refreshList();
						}

						strCallResult = "SUCCESS: process friends for " + fbid;
						log.info(strCallResult);
					}
					else
					{
						strCallResult = "FAIL: process friends: encountered error";
						log.info(strCallResult);
					}
				}
				else
				{
					strCallResult = "FAIL: process friends: invalid user";
					log.info(strCallResult);
				}
			}
			else
			{
				strCallResult = "FAIL: process friends: invalid fbid";
				log.info(strCallResult);
			}

		}
		catch (Exception ex)
		{
			strCallResult = "FAIL: process friends: " + ex.getMessage();
			log.info(strCallResult);

		}

		resp.getWriter().println(strCallResult);
	}

	private boolean processFriends(FbdfUser myUser)
			throws InvalidAccessException
	{
		List<FriendItem> friendList = new ArrayList<FriendItem>();

		try
		{
			JSONObject resp = new JSONObject(
					IOUtil.getURLContents(FacebookHelper.getFriends(myUser
							.getAccessCode())));
			JSONArray data = resp.getJSONArray(FacebookConstants.KEY_DATA);

			int len = data.length();
			myUser.setFriendNumber(len);
			log.info("num of friends in json: " + len);

			// get new list
			for (int i = 0; i < len; i++)
			{
				JSONObject keyValue = data.getJSONObject(i);
				Long friendId = Long.valueOf((String) keyValue
						.get(FacebookConstants.KEY_ID));
				String name = (String) keyValue.get(FacebookConstants.KEY_NAME);

				FriendItem friend = new FriendItem(friendId, name);
				friendList.add(friend);
				// add friend to db
				FriendInfoCacheManager.getInstance().addUpdateFriend(friend);
			}

			log.info("pulling friends from db...");
			// pulls the list from memory
			FriendListManager flManager = new FriendListManager(
					myUser.getFbId());
			int dsLength = flManager.getNumFriends();
			myUser.setPreviousFriendNumber(dsLength);
			log.info("num of friends in db: " + dsLength);

			log.info("user: " + myUser);

			// update user
			UserCacheManager.getInstance().addUpdateUser(myUser, true);

			if (dsLength > 0)
			{
				int found = 0;

				// check for updates
				for (FriendItem f : friendList)
				{
					long fbid = f.getFbId();

					if (flManager.hasAssociation(fbid))
					{

						// updated friend information
						SearchableValue temp = flManager.getAssociation(fbid);

						if (f.getFullName().compareTo(temp.getFullName()) != 0)
						{
							log.info("should be update of full name");
							// update my friend info
							flManager.updateFriendInfo(temp, new FriendItem(
									fbid, f.getFullName()));
						}

						temp.setTouched(true);
						found++;

					}
					else
					{
						log.info("add a new association");
						// update my current list also
						flManager.addAssociation(
								new FriendAssociation(myUser.getFbId(), fbid),
								f.getFullName());
					}
				}

				// some friends were removed
				if (dsLength - found > 0)
				{
					log.info("friends might have been deleted");
					flManager.findRemovedFriends();
				}
			}
			else
			{

				flManager.addInitial(friendList);
			}

			// if it has changed then refresh the list
			if (FriendInfoCacheManager.getInstance().hasChanged())
			{
				log.info("friend info has changed -- updating");
				FriendInfoCacheManager.getInstance().refreshList();
			}

			return true;
		}
		catch (JSONException e)
		{
			log.info("couldnt read json" + e);
		}
		catch (RuntimeException e)
		{
			e.printStackTrace();
			log.info("some error occurred in getting friends" + e);
		}

		return false;
	}

}
