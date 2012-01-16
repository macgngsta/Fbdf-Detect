package com.gregtam.fbdfdetect.dao;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.gregtam.fbdfdetect.model.FriendAssociation;

public class FriendAssociationDAOJdo implements IFriendAssociationDAO
{

	private static final Logger log = Logger
			.getLogger(FriendAssociationDAOJdo.class.getName());

	@Override
	public void addAssociation(FriendAssociation association)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();
		try
		{
			Key key = KeyFactory.createKey(
					FriendAssociation.class.getSimpleName(),
					association.getFbId() + "" + association.getFriendId());
			association.setKey(key);
			pm.makePersistent(association);
			log.info("association: " + association + "saved.");
		}
		catch (Exception e)
		{
			log.info("issue saving association " + e);
		}
		finally
		{
			pm.close();
		}
	}

	@Override
	public void removeAssociation(FriendAssociation association)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();
		try
		{
			pm.currentTransaction().begin();
			// look up the object
			Key key = KeyFactory.createKey(
					FriendAssociation.class.getSimpleName(),
					association.getFbId() + "" + association.getFriendId());

			association = pm.getObjectById(FriendAssociation.class, key);
			pm.deletePersistent(association);

			pm.currentTransaction().commit();
			log.info("association: " + association + "removed.");
		}
		catch (Exception e)
		{
			log.info("issue removing association " + e);
			pm.currentTransaction().rollback();
			throw new RuntimeException(e);
		}
		finally
		{
			pm.close();
		}

	}

	@SuppressWarnings("unchecked")
	public void removeAllAssociations(Long fbid)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();

		Collection<FriendAssociation> assocs;

		if (fbid != null)
		{
			// dont use transactions
			Query query = pm.newQuery(FriendAssociation.class,
					"this.userId==fbid");
			query.declareParameters("Long fbid");
			query.setOrdering("this.modifyTime desc");

			assocs = (Collection<FriendAssociation>) query.execute(fbid);
		}
		else
		{
			String query = "select from " + FriendAssociation.class.getName();
			assocs = (Collection<FriendAssociation>) pm.newQuery(query)
					.execute();
		}

		pm.deletePersistentAll(assocs);
	}

	@Override
	public void updateAssociation(FriendAssociation association)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();

		Long fbId = association.getFbId();
		Long friendId = association.getFriendId();

		try
		{
			pm.currentTransaction().begin();
			// look up the object
			Key key = KeyFactory.createKey(
					FriendAssociation.class.getSimpleName(),
					association.getFbId() + "" + association.getFriendId());

			association = pm.getObjectById(FriendAssociation.class, key);

			association.setFbId(fbId);
			association.setFriendId(friendId);

			pm.makePersistent(association);

			pm.currentTransaction().commit();
			log.info("association: " + association + "updated.");
		}
		catch (Exception e)
		{
			log.info("issue updating association " + e);
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
	public List<FriendAssociation> listAssociations(boolean flush)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();

		String query = "select from " + FriendAssociation.class.getName();
		List<FriendAssociation> assocs = (List<FriendAssociation>) pm.newQuery(
				query).execute();

		if (flush)
		{
			// flush out all associations
			if (assocs != null)
			{
				assocs.size();
			}
		}
		return assocs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<FriendAssociation> listAssociationsForUser(Long fid,
			boolean flush)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();
		Query query = pm.newQuery(FriendAssociation.class, "this.fbId==fbid");
		query.declareParameters("Long fbid");

		List<FriendAssociation> assocs = (List<FriendAssociation>) query
				.execute(fid);

		if (flush)
		{
			// flush out all associations
			if (assocs != null)
			{
				assocs.size();
			}
		}
		return assocs;
	}

}
