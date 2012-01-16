package com.gregtam.fbdfdetect.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.gregtam.fbdfdetect.model.FriendItem;

public class FriendDAOJdo implements IFriendDAO
{

	private static final Logger log = Logger.getLogger(FriendDAOJdo.class
			.getName());

	@Override
	public void addFriend(FriendItem friend)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();
		try
		{
			pm.makePersistent(friend);
			log.info("friend: " + friend + "saved.");
		}
		catch (Exception e)
		{
			log.info("issue saving friend " + e);
		}
		finally
		{
			pm.close();
		}
	}

	@Override
	public void removeFriend(FriendItem friend)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();
		try
		{
			pm.currentTransaction().begin();

			long fbid = friend.getFbId();
			Query query = pm.newQuery(FriendItem.class, "this.fbId==fbid");
			query.declareParameters("Long fbid");

			FriendItem toDelete = (FriendItem) query.execute(fbid);

			pm.deletePersistent(toDelete);

			pm.currentTransaction().commit();
			log.info("friend: " + friend + "removed.");
		}
		catch (Exception e)
		{
			log.info("issue removing friend " + e);
			pm.currentTransaction().rollback();
			throw new RuntimeException(e);
		}
		finally
		{
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public void removeAllFriends(Long fbid)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();

		Collection<FriendItem> friends;

		String query = "select from " + FriendItem.class.getName();
		friends = (Collection<FriendItem>) pm.newQuery(query).execute();

		pm.deletePersistentAll(friends);
	}

	@Override
	public void updateFriend(FriendItem friend)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();

		Long fbId = friend.getFbId();
		String name = friend.getFullName();
		Date modify = friend.getLastModify();

		try
		{
			pm.currentTransaction().begin();
			// look up the object
			Query query = pm.newQuery(FriendItem.class, "this.fbId==fbid");
			query.declareParameters("Long fbid");

			FriendItem toUpdate = (FriendItem) query.execute(fbId);

			toUpdate.setFullName(name);
			toUpdate.setLastModify(modify);

			pm.makePersistent(toUpdate);

			pm.currentTransaction().commit();

			log.info("friend: " + toUpdate + "updated.");
		}
		catch (Exception e)
		{
			log.info("issue updating friend " + e);
			pm.currentTransaction().rollback();
			throw new RuntimeException(e);
		}
		finally
		{
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FriendItem> listFriends(boolean flush)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();

		String query = "select from " + FriendItem.class.getName();
		List<FriendItem> friends = (List<FriendItem>) pm.newQuery(query)
				.execute();

		if (flush)
		{
			if (friends != null)
			{
				int len = friends.size();
				log.info("loaded  " + len + " friends");
			}
		}
		return friends;
	}

	@Override
	public FriendItem getFriendById(long fbid)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();

		Query query = pm.newQuery(FriendItem.class, "this.fbId==fbid");
		query.declareParameters("Long fbid");

		return (FriendItem) query.execute(fbid);
	}

}
