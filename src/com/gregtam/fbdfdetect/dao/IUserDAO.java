package com.gregtam.fbdfdetect.dao;

import java.util.List;

import com.gregtam.fbdfdetect.model.FbdfUser;

public interface IUserDAO
{
	void addUser(FbdfUser user);

	void removeUser(FbdfUser user);

	void removeAllUsers(Long fbid);

	void updateUser(FbdfUser user, boolean otherInfo);

	List<FbdfUser> listUsers(boolean flush);
}
