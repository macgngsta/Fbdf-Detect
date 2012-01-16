package com.gregtam.fbdfdetect.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.gregtam.fbdfdetect.dao.ActivityDAOJdo;
import com.gregtam.fbdfdetect.dao.FriendAssociationDAOJdo;
import com.gregtam.fbdfdetect.dao.IActivityDAO;
import com.gregtam.fbdfdetect.dao.IFriendAssociationDAO;
import com.gregtam.fbdfdetect.model.ActivityItem;
import com.gregtam.fbdfdetect.model.FriendAssociation;
import com.gregtam.fbdfdetect.model.FriendItem;
import com.gregtam.fbdfdetect.model.SearchableValue;

public class FriendListManager
{

	private static final Logger log = Logger.getLogger(FriendListManager.class
			.getName());
	private long fbId;

	private Map<Long, SearchableValue> storeList;

	private IFriendAssociationDAO friendListDao;
	private IActivityDAO activityDao;

	public static long DEFAULT_START = 0;
	public static long DEFAULT_LIMIT = 10;

	public FriendListManager(long fbid)
	{
		this.fbId = fbid;
		storeList = new ConcurrentHashMap<Long, SearchableValue>();
		friendListDao = new FriendAssociationDAOJdo();
		activityDao = new ActivityDAOJdo();

		loadList();
	}

	public void refreshList()
	{
		storeList.clear();
		loadList();
	}

	public SearchableValue getAssociation(long fbId)
	{
		if (storeList.containsKey(fbId))
		{
			return storeList.get(fbId);
		}

		return null;
	}

	public void addAssociation(FriendAssociation assoc, String name)
	{
		addAssociation(assoc, name, true);
	}

	private void addAssociation(FriendAssociation assoc, String name,
			boolean store)
	{
		SearchableValue sv = new SearchableValue(assoc.getFriendId(), name);
		// update to my storedlist
		storeList.put(assoc.getFriendId(), sv);
		// update the dao
		friendListDao.addAssociation(assoc);

		if (store)
		{
			// added a friend activity
			activityDao.addActivity(new ActivityItem(this.fbId, assoc
					.getFriendId(), ActivityItem.ADD, buildAddString(name)));
		}

	}

	public void updateFriendInfo(SearchableValue oldInfo, FriendItem newInfo)
	{

		// update to activity
		/*
		 * activityDao.addActivity(new ActivityItem(this.fbId, oldInfo.getKey(),
		 * ActivityItem.UPDATE, buildUpdateString(oldInfo.getFullName(),
		 * newInfo.getFullName())));
		 */

		// dont need to do anything on my storedlist
		// update the friendinfo dao
		FriendInfoCacheManager.getInstance().addUpdateFriend(newInfo);

	}

	public void findRemovedFriends()
	{
		List<Long> toBeRemoved = new ArrayList<Long>();

		for (Map.Entry<Long, SearchableValue> entry : storeList.entrySet())
		{
			SearchableValue sv = entry.getValue();
			if (!sv.isTouched())
			{
				// added activity for removals
				activityDao.addActivity(new ActivityItem(this.fbId, Long
						.valueOf(entry.getKey()), ActivityItem.DELETE,
						buildDeleteString(sv.getFullName())));

				sv.setTouched(true);

				// remove from associations
				toBeRemoved.add(sv.getKey());
			}
		}

		// must remove them after
		for (Long key : toBeRemoved)
		{
			removeAssociation(key);
		}
	}

	private void removeAssociation(long toRemove)
	{
		// update to my storedlist
		if (storeList.containsKey(toRemove))
		{
			// update the dao
			friendListDao.removeAssociation(new FriendAssociation(this.fbId,
					toRemove));
			storeList.remove(toRemove);
		}
	}

	public void addInitial(List<FriendItem> friends)
	{
		int count = 0;

		if (friends != null && !friends.isEmpty())
		{
			for (FriendItem fi : friends)
			{
				addAssociation(new FriendAssociation(this.fbId, fi.getFbId()),
						fi.getFullName(), false);
				count++;
			}

		}

		// this is initial load
		activityDao.addActivity(new ActivityItem(this.fbId, new Long(0),
				ActivityItem.INITIAL_LOAD, buildInitialString(count)));
	}

	public boolean hasAssociation(long fbId)
	{
		return storeList.containsKey(fbId);
	}

	public int getNumFriends()
	{
		return storeList.size();
	}

	public List<ActivityItem> getActivity(long start, long limit)
	{
		return activityDao.listActivityforUser(this.fbId, start, limit, true);
	}

	public List<ActivityItem> getAcitivty()
	{
		log.info("getting activity...");

		return activityDao.listActivityforUser(this.fbId, DEFAULT_START,
				DEFAULT_LIMIT, true);
	}

	private void loadList()
	{
		List<FriendAssociation> friendAssociations = friendListDao
				.listAssociationsForUser(this.fbId, true);

		if (friendAssociations != null && !friendAssociations.isEmpty())
		{
			for (FriendAssociation fa : friendAssociations)
			{
				FriendItem fItem = FriendInfoCacheManager.getInstance()
						.getFriend(fa.getFriendId());

				if (fItem != null)
				{
					// add the friend to the storedlist
					storeList.put(
							fItem.getFbId(),
							new SearchableValue(fItem.getFbId(), fItem
									.getFullName()));
				}
			}
		}
		else
		{
			log.info("friend assoc empty or null");
		}
	}

	private String buildUpdateString(String n, String n2)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(n);
		sb.append(" &raquo; ");
		sb.append(n2);
		return sb.toString();
	}

	private String buildAddString(String n)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(n);
		return sb.toString();
	}

	private String buildDeleteString(String n)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(n);
		return sb.toString();
	}

	private String buildInitialString(int len)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("initial load: ");
		sb.append(len);
		return sb.toString();
	}
}
