package com.gregtam.fbdfdetect.helper;

import com.gregtam.fbdfdetect.dao.ActivityDAOJdo;
import com.gregtam.fbdfdetect.dao.FriendAssociationDAOJdo;
import com.gregtam.fbdfdetect.dao.FriendDAOJdo;
import com.gregtam.fbdfdetect.dao.IActivityDAO;
import com.gregtam.fbdfdetect.dao.IFriendAssociationDAO;
import com.gregtam.fbdfdetect.dao.IFriendDAO;
import com.gregtam.fbdfdetect.dao.IUserDAO;
import com.gregtam.fbdfdetect.dao.UserDAOJdo;

public class DataManager
{
	private IActivityDAO activityDao;
	private IFriendDAO friendDao;
	private IFriendAssociationDAO assocDao;
	private IUserDAO userDao;

	public DataManager()
	{
		activityDao = new ActivityDAOJdo();
		friendDao = new FriendDAOJdo();
		assocDao = new FriendAssociationDAOJdo();
		userDao = new UserDAOJdo();
	}

	public void removeAllActivities()
	{
		activityDao.removeAllActivity(null);
	}

	public void removeAllActivitesByUser(long fbid)
	{
		activityDao.removeAllActivity(new Long(fbid));
	}

	public void removeAllFriends()
	{
		friendDao.removeAllFriends(null);
		FriendInfoCacheManager.getInstance().refreshList();
	}

	public void removeAllAssociations()
	{
		assocDao.removeAllAssociations(null);
	}

	public void removeAllAssociationsByUser(long fbid)
	{
		assocDao.removeAllAssociations(new Long(fbid));
	}

	public void removeAllUsers()
	{
		userDao.removeAllUsers(null);
		UserCacheManager.getInstance().refreshList();
	}

	public void removeUser(long fbid)
	{
		// userDao.removeUser();
	}
}
