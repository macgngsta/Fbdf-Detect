package com.gregtam.fbdfdetect.helper;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.gregtam.fbdfdetect.dao.FriendDAOJdo;
import com.gregtam.fbdfdetect.dao.IFriendDAO;
import com.gregtam.fbdfdetect.model.FriendItem;

public class FriendInfoCacheManager
{
	private static final Logger log = Logger
			.getLogger(FriendInfoCacheManager.class.getName());

	private Map<Long, FriendItem> friendInformation;
	private IFriendDAO friendDao;
	private static FriendInfoCacheManager _instance;
	private boolean isChange = false;

	private FriendInfoCacheManager()
	{
		friendInformation = new ConcurrentHashMap<Long, FriendItem>();
		friendDao = new FriendDAOJdo();
		loadList();
	}

	public static synchronized FriendInfoCacheManager getInstance()
	{
		if (_instance == null)
		{
			_instance = new FriendInfoCacheManager();
		}

		return _instance;
	}

	public void refreshList()
	{
		log.info("refresh the friend information");
		friendInformation.clear();
		loadList();
	}

	public FriendItem getFriend(long fbId)
	{
		if (friendInformation.containsKey(fbId))
		{
			return friendInformation.get(fbId);
		}

		return null;
	}

	public void addUpdateFriend(FriendItem friend)
	{
		// log.info("friend: " + friend);

		if (friendInformation.containsKey(friend.getFbId()))
		{
			FriendItem oldFriend = friendInformation.get(friend.getFbId());

			// pull the current info
			if (isDifferent(friend, oldFriend))
			{
				log.info("updating old friend");

				friendInformation.put(friend.getFbId(), friend);
				friendDao.updateFriend(friend);

				// track change
				this.isChange = true;
			}

		}
		else
		{
			log.info("adding new friend");
			friendDao.addFriend(friend);

			// track change
			this.isChange = true;
		}
	}

	private void loadList()
	{
		log.info("loading list...");
		List<FriendItem> tempList = friendDao.listFriends(true);
		if (tempList != null && !tempList.isEmpty())
		{
			for (FriendItem fItem : tempList)
			{
				friendInformation.put(fItem.getFbId(), fItem);
			}
		}

		// track change
		this.isChange = false;
	}

	private boolean isDifferent(FriendItem newFriend, FriendItem oldFriend)
	{
		if (newFriend.getFbId() == oldFriend.getFbId())
		{
			if (!newFriend.getFullName().equals(oldFriend.getFullName()))
			{
				return true;
			}
		}

		return false;
	}

	public boolean hasChanged()
	{
		return this.isChange;
	}

	public Collection<FriendItem> getAll()
	{
		return (Collection<FriendItem>) this.friendInformation;
	}
}
