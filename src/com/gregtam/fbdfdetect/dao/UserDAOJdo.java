package com.gregtam.fbdfdetect.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.gregtam.fbdfdetect.model.FbdfUser;

public class UserDAOJdo implements IUserDAO
{
	private static final Logger log = Logger.getLogger(UserDAOJdo.class
			.getName());

	@Override
	public void addUser(FbdfUser user)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();
		try
		{
			pm.makePersistent(user);
			log.info("user: " + user + "saved.");
		}
		catch (Exception e)
		{
			log.info("issue saving user " + e);
		}
		finally
		{
			pm.close();
		}
	}

	@Override
	public void removeUser(FbdfUser user)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();
		try
		{
			pm.currentTransaction().begin();
			// look up the object
			user = pm.getObjectById(FbdfUser.class, user.getFbId());
			pm.deletePersistent(user);

			pm.currentTransaction().commit();
			log.info("user: " + user + "removed.");
		}
		catch (Exception e)
		{
			log.info("issue removing user " + e);
			pm.currentTransaction().rollback();
			throw new RuntimeException(e);
		}
		finally
		{
			pm.close();
		}

	}

	@SuppressWarnings("unchecked")
	public void removeAllUsers(Long fbid)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();

		Collection<FbdfUser> friends;

		if (fbid != null)
		{
			// dont use transactions
			Query query = pm.newQuery(FbdfUser.class, "this.userId==fbid");
			query.declareParameters("Long fbid");
			query.setOrdering("this.modifyTime desc");

			friends = (Collection<FbdfUser>) query.execute(fbid);
		}
		else
		{
			String query = "select from " + FbdfUser.class.getName();
			friends = (Collection<FbdfUser>) pm.newQuery(query).execute();
		}

		pm.deletePersistentAll(friends);
	}

	@Override
	public void updateUser(FbdfUser user, boolean otherInfo)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();

		String accessCode = user.getAccessCode();
		String email = user.getEmail();
		long expires = user.getExpires();
		String name = user.getFullName();
		boolean verified = user.isVerified();
		String pictureUrl = user.getPictureURL();
		int friendNumber = user.getFriendNumber();
		int previousFriendNumber = user.getPreviousFriendNumber();
		Date token = user.getCreateToken();
		String authCode = user.getAuthCode();

		try
		{
			pm.currentTransaction().begin();
			// look up the object
			user = pm.getObjectById(FbdfUser.class, user.getFbId());

			user.setAccessCode(accessCode);
			user.setEmail(email);
			user.setExpires(expires);
			// set as current time
			user.setLastModify(new Date());
			user.setFullName(name);
			user.setVerified(verified);
			user.setPictureURL(pictureUrl);
			user.setAuthCode(authCode);
			user.setCreateToken(token);

			if (otherInfo)
			{
				user.setFriendNumber(friendNumber);
				user.setPreviousFriendNumber(previousFriendNumber);
			}

			pm.makePersistent(user);

			pm.currentTransaction().commit();
			log.info("user: " + user + "updated.");
		}
		catch (Exception e)
		{
			log.info("issue updating user " + e);
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
	public List<FbdfUser> listUsers(boolean flush)
	{
		PersistenceManager pm = PMF.getPersistenceManagerFactory()
				.getPersistenceManager();

		String query = "select from " + FbdfUser.class.getName();

		List<FbdfUser> users = (List<FbdfUser>) pm.newQuery(query).execute();

		if (flush)
		{
			if (users != null)
			{
				users.size();
			}
		}
		return users;
	}

}
