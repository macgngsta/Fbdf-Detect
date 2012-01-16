package com.gregtam.fbdfdetect.helper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.gregtam.fbdfdetect.dao.IUserDAO;
import com.gregtam.fbdfdetect.dao.UserDAOJdo;
import com.gregtam.fbdfdetect.model.FbdfUser;

public class UserCacheManager
{
	private static final Logger log = Logger.getLogger(UserCacheManager.class
			.getName());

	private Map<Long, FbdfUser> currentAvailableUsers;
	private IUserDAO userDao;
	private static UserCacheManager _instance;
	private boolean isChange = false;

	private UserCacheManager()
	{
		currentAvailableUsers = new ConcurrentHashMap<Long, FbdfUser>();
		userDao = new UserDAOJdo();
		loadList();
	}

	public static synchronized UserCacheManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new UserCacheManager();
		}

		return _instance;
	}

	public void refreshList()
	{
		log.info("refresh the user list");
		currentAvailableUsers.clear();
		loadList();
	}

	public FbdfUser getUser(long fbId)
	{
		if (currentAvailableUsers.containsKey(fbId))
		{
			return currentAvailableUsers.get(fbId);
		}

		return null;
	}

	public void addUpdateUser(FbdfUser newUser, boolean otherInfo)
	{
		log.info("adding new user");

		if (currentAvailableUsers.containsKey(newUser.getFbId()))
		{
			// dont update the whole list on update
			currentAvailableUsers.put(newUser.getFbId(), newUser);
			userDao.updateUser(newUser, otherInfo);
			this.isChange = true;
		}
		else
		{
			userDao.addUser(newUser);
			this.isChange = true;
		}
	}

	private void loadList()
	{
		log.info("loading list...");
		List<FbdfUser> tempList = userDao.listUsers(true);
		if (tempList != null && !tempList.isEmpty())
		{
			for (FbdfUser fbUser : tempList)
			{
				currentAvailableUsers.put(fbUser.getFbId(), fbUser);
			}
		}

		this.isChange = false;
	}

	public boolean hasChanged()
	{
		return this.isChange;
	}

	public Collection<FbdfUser> getAll()
	{
		return (Collection<FbdfUser>) this.currentAvailableUsers;
	}
}
