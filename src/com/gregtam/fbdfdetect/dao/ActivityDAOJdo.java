package com.gregtam.fbdfdetect.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.gregtam.fbdfdetect.model.ActivityItem;

public class ActivityDAOJdo implements IActivityDAO
{

	private static final Logger log = Logger.getLogger(UserDAOJdo.class
			.getName());

	@Override
	public void addActivity(ActivityItem activity)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();
		try
		{
			pm.makePersistent(activity);
			log.info("activity: " + activity + "saved.");
		}
		catch (Exception e)
		{
			log.info("issue saving activity " + e);
		}
		finally
		{
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	public void removeAllActivity(Long fbid)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();

		Collection<ActivityItem> activities;

		if (fbid != null)
		{
			// dont use transactions
			Query query = pm.newQuery(ActivityItem.class, "this.userId=fbid");
			query.declareParameters("Long fbid");
			query.setOrdering("this.modifyTime desc");

			activities = (Collection<ActivityItem>) query.execute(fbid);
		}
		else
		{
			String query = "select from " + ActivityItem.class.getName();
			activities = (Collection<ActivityItem>) pm.newQuery(query)
					.execute();
		}

		pm.deletePersistentAll(activities);
	}

	@Override
	public void removeActivity(ActivityItem activity)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();
		try
		{
			pm.currentTransaction().begin();
			// look up the object
			activity = pm.getObjectById(ActivityItem.class, activity.getKey());
			pm.deletePersistent(activity);

			pm.currentTransaction().commit();
			log.info("activity: " + activity + "removed.");
		}
		catch (Exception e)
		{
			log.info("issue removing activity " + e);
			pm.currentTransaction().rollback();
			throw new RuntimeException(e);
		}
		finally
		{
			pm.close();
		}
	}

	@Override
	public void updateActivity(ActivityItem activity)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();

		Long friendId = activity.getFriendId();
		Date modify = activity.getModifyTime();
		int type = activity.getType();
		Long userId = activity.getUserId();

		try
		{
			pm.currentTransaction().begin();
			// look up the object
			activity = pm.getObjectById(ActivityItem.class, activity.getKey());

			activity.setFriendId(friendId);
			activity.setModifyTime(modify);
			activity.setType(type);
			activity.setUserId(userId);

			pm.makePersistent(activity);

			pm.currentTransaction().commit();
			log.info("activity: " + activity + "updated.");
		}
		catch (Exception e)
		{
			log.info("issue updating activity " + e);
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
	public List<ActivityItem> listActivity(long start, long limit, boolean flush)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();

		Query query = pm.newQuery(ActivityItem.class);
		query.setRange(start, limit);

		List<ActivityItem> activities = (List<ActivityItem>) pm.newQuery(query)
				.execute();

		if (flush)
		{
			// flush out all activities
			if (activities != null)
			{
				activities.size();
			}
		}
		return activities;

	}

	// TODO: add start limit range 0,5

	@SuppressWarnings("unchecked")
	@Override
	public List<ActivityItem> listActivityforUser(Long fid, long start,
			long limit, boolean flush)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();

		Query query = pm.newQuery(ActivityItem.class, "this.userId==fbid");
		query.declareParameters("Long fbid");
		query.setRange(start, limit);
		query.setOrdering("this.modifyTime desc");

		List<ActivityItem> activities = (List<ActivityItem>) query.execute(fid);

		if (flush)
		{
			// flush out all activities
			if (activities != null)
			{
				activities.size();
			}
		}
		return activities;
	}
}
